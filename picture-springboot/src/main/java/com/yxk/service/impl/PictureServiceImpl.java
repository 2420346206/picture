package com.yxk.service.impl;

import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yxk.common.ErrorCode;
import com.yxk.model.dto.PictureQueryDTO;
import com.yxk.utils.ThrowUtils;
import com.yxk.config.OssClientConfig;
import com.yxk.constant.UserConstant;
import com.yxk.model.dto.PictureCreateDTO;
import com.yxk.model.entity.Picture;
import com.yxk.mapper.PictureMapper;
import com.yxk.service.PictureService;
import com.yxk.model.vo.PictureVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PictureServiceImpl implements PictureService {

    private static final Logger log = LoggerFactory.getLogger(PictureServiceImpl.class);
    @Autowired
    private OssClientConfig ossClientConfig;

    @Autowired
    private OSS ossClient;

    @Autowired
    private PictureMapper pictureMapper;

    @Override
    public String upload(MultipartFile file) {
        String fileName = "uploads/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        try {
            ossClient.putObject(ossClientConfig.getBucket(), fileName, file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "https://" + ossClientConfig.getBucket() + ".oss-cn-hangzhou.aliyuncs.com/" + fileName;
    }

    @Override
    public Boolean createPicture(PictureCreateDTO pictureCreateDto, HttpServletRequest request) {
        Picture picture = new Picture();
        BeanUtils.copyProperties(pictureCreateDto, picture);
        // 将标签 list 转为 string
        picture.setTags(JSONUtil.toJsonStr(pictureCreateDto.getTags()));

        Long userId = (Long) request.getAttribute(UserConstant.USER_LOGIN_STATE);
        picture.setUserId(userId);

        int result = pictureMapper.insert(picture);
        ThrowUtils.throwIf(result < 1, ErrorCode.OPERATION_ERROR);

        return true;
    }

    /**
     * 分页获取图片封装
     */
    @Override
    public Page<PictureVO> getPictureVOPage(PictureQueryDTO pictureQueryDTO, HttpServletRequest request) {
        // 空间权限校验
        Long spaceId = pictureQueryDTO.getSpaceId();
        if (spaceId == null) {
            // 公开图库
            pictureQueryDTO.setNullSpaceId(true);
        }

        // 分页对象
        Page<Picture> picturePage = new Page<>(pictureQueryDTO.getCurrent(), pictureQueryDTO.getPageSize());

        // 构造查询条件
        LambdaQueryWrapper<Picture> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 关键词搜索（name 或 introduction）
        if (StringUtils.isNotBlank(pictureQueryDTO.getSearchText())) {
            lambdaQueryWrapper.like(Picture::getName, pictureQueryDTO.getSearchText())
                    .or()
                    .like(Picture::getIntroduction, pictureQueryDTO.getSearchText());
        }

        // 分类过滤
        if (StringUtils.isNotBlank(pictureQueryDTO.getCategory())) {
            lambdaQueryWrapper.eq(Picture::getCategory, pictureQueryDTO.getCategory());
        }

        // 标签过滤
        if (pictureQueryDTO.getTags() != null && !pictureQueryDTO.getTags().isEmpty()) {
            for (String tag : pictureQueryDTO.getTags()) {
                lambdaQueryWrapper.like(Picture::getTags, tag);
            }
        }

        // 执行分页查询，返回的是 Page<Picture>
        Page<Picture> pictureList = pictureMapper.selectPage(picturePage, lambdaQueryWrapper);

        // 获取分页里的数据记录
        List<Picture> records = pictureList.getRecords();

        // 转换成 VO 列表
        List<PictureVO> pictureVOList = new ArrayList<>();
        for (Picture picture : records) {
            pictureVOList.add(PictureVO.objToVo(picture));
        }

        // 构造返回的分页对象（保持分页信息）
        Page<PictureVO> result = new Page<>(picturePage.getCurrent(), picturePage.getSize(), picturePage.getTotal());
        result.setRecords(pictureVOList);

        return result;
    }

    @Override
    public List<String> categoryList() {
        LambdaQueryWrapper<Picture> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper
                .select(Picture::getCategory)
                .isNotNull(Picture::getCategory)
                .ne(Picture::getCategory, "");

        // 查询所有非空的 category
        List<Picture> pictureList = pictureMapper.selectList(lambdaQueryWrapper);

        // 提取出 category 字段
        List<String> categoryList = pictureList.stream()
                .map(Picture::getCategory)
                .collect(Collectors.toList());

        // 去重
        List<String> distinctCategoryList = categoryList.stream()
                .distinct()
                .collect(Collectors.toList());

        // 返回结果
        return distinctCategoryList;
    }

    @Override
    public List<String> tagList() {
        // 查询所有 Picture 记录
        LambdaQueryWrapper<Picture> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper
                .select(Picture::getTags)
                .isNotNull(Picture::getTags)
                .ne(Picture::getTags, "");

        List<Picture> pictureList = pictureMapper.selectList(lambdaQueryWrapper);

        // 提取每条记录的 tags 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> allTags = new ArrayList<>();

        for (Picture picture : pictureList) {
            String tagStr = picture.getTags();
            try {
                // 解析成 List<String>
                List<String> tags = objectMapper.readValue(tagStr, new TypeReference<List<String>>() {});
                allTags.addAll(tags);
            } catch (Exception e) {
                log.error("解析 tags JSON 出错: {}", tagStr, e);
            }
        }


        // 去重
        List<String> distinctTagList = allTags.stream()
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());

        // 5. 返回结果
        return distinctTagList;
    }
}
