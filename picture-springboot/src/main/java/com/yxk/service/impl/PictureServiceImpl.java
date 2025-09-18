package com.yxk.service.impl;

import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSS;
import com.yxk.common.ErrorCode;
import com.yxk.common.ThrowUtils;
import com.yxk.config.OssClientConfig;
import com.yxk.constant.UserConstant;
import com.yxk.dto.PictureCreateDto;
import com.yxk.entity.Picture;
import com.yxk.entity.User;
import com.yxk.mapper.PictureMapper;
import com.yxk.service.PictureService;
import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

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
    public Boolean createPicture(PictureCreateDto pictureCreateDto, HttpServletRequest request) {
        Picture picture = new Picture();
        BeanUtils.copyProperties(pictureCreateDto, picture);
        // 将标签 list 转为 string
        picture.setTags(JSONUtil.toJsonStr(pictureCreateDto.getTags()));

        User userLogin = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        Long userId = userLogin.getId();
        picture.setUserId(userId);

        int result = pictureMapper.insert(picture);
        ThrowUtils.throwIf(result < 1, ErrorCode.OPERATION_ERROR);

        return true;
    }
}
