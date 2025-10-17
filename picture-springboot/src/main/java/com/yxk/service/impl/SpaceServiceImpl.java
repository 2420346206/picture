package com.yxk.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxk.common.BusinessException;
import com.yxk.common.ErrorCode;
import com.yxk.constant.SpaceOrderConstant;
import com.yxk.mapper.SpaceMapper;
import com.yxk.model.dto.SpaceAddDTO;
import com.yxk.model.dto.SpaceQueryDTO;
import com.yxk.model.entity.Picture;
import com.yxk.model.entity.Space;
import com.yxk.model.entity.SpaceUser;
import com.yxk.model.entity.User;
import com.yxk.model.enums.SpaceLevelEnum;
import com.yxk.model.enums.SpaceRoleEnum;
import com.yxk.model.enums.SpaceTypeEnum;
import com.yxk.model.vo.PictureVO;
import com.yxk.model.vo.SpaceCountVO;
import com.yxk.model.vo.SpaceVO;
import com.yxk.service.SpaceService;
import com.yxk.service.SpaceUserService;
import com.yxk.utils.ThrowUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SpaceServiceImpl extends ServiceImpl<SpaceMapper, Space> implements SpaceService {

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private SpaceUserService spaceUserService;

    @Override
    public Long addSpace(SpaceAddDTO spaceAddDTO, User user) {
        // 转换实体类和 DTO
        Space space = new Space();
        BeanUtils.copyProperties(spaceAddDTO, space);
        if (StrUtil.isBlank(space.getSpaceName())) {
            space.setSpaceName("默认空间");
        }
        if (space.getSpaceLevel() == null) {
            space.setSpaceLevel(SpaceLevelEnum.COMMON.getValue());
        }
        if (space.getSpaceType() == null) {
            space.setSpaceType(SpaceTypeEnum.PRIVATE.getValue());
        }
        Long userId = user.getId();
        space.setUserId(userId);
        // 校验参数
        this.validSpace(space, true);
        // 控制同一用户并发创建个人空间
        String lock = String.valueOf(userId).intern();
        synchronized (lock) {
            Long newSpaceId = transactionTemplate.execute(status -> {

                // 仅限制个人空间唯一
                if (SpaceTypeEnum.PRIVATE.getValue() == space.getSpaceType()) {
                    boolean exists = this.lambdaQuery()
                            .eq(Space::getUserId, userId)
                            .eq(Space::getSpaceType, space.getSpaceType())
                            .exists();
                    ThrowUtils.throwIf(exists, ErrorCode.OPERATION_ERROR, "每个用户只能创建一个个人空间");
                }

                // 创建空间
                boolean result = this.save(space);
                ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "保存空间到数据库失败");

                // 如果是团队空间，自动添加创建者为管理员
                if (SpaceTypeEnum.TEAM.getValue() == space.getSpaceType()) {
                    SpaceUser spaceUser = new SpaceUser();
                    spaceUser.setSpaceId(space.getId());
                    spaceUser.setUserId(userId);
                    spaceUser.setSpaceRole(SpaceRoleEnum.ADMIN.getValue());
                    result = spaceUserService.save(spaceUser);
                    ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "创建团队成员记录失败");
                }

                return space.getId();
            });
            return Optional.ofNullable(newSpaceId).orElse(-1L);
        }
    }

    @Override
    public void validSpace(Space space, boolean add) {
        ThrowUtils.throwIf(space == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        String spaceName = space.getSpaceName();
        Integer spaceLevel = space.getSpaceLevel();
        SpaceLevelEnum spaceLevelEnum = SpaceLevelEnum.getEnumByValue(spaceLevel);
        Integer spaceType = space.getSpaceType();
        SpaceTypeEnum spaceTypeEnum = SpaceTypeEnum.getEnumByValue(spaceType);
        // 创建时校验
        if (add) {
            if (StrUtil.isBlank(spaceName)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间名称不能为空");
            }
            if (spaceLevel == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间级别不能为空");
            }
            if (spaceType == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间类别不能为空");
            }
        }
        // 修改数据时，空间名称进行校验
        if (StrUtil.isNotBlank(spaceName) && spaceName.length() > 30) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间名称过长");
        }
        // 修改数据时，空间级别进行校验
        if (spaceLevel != null && spaceLevelEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间级别不存在");
        }
        // 修改数据时，空间类别进行校验
        if (spaceType != null && spaceTypeEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间类别不存在");
        }
    }

    @Override
    public Page<SpaceVO> getSpaceVOPage(SpaceQueryDTO spaceQueryDTO, HttpServletRequest request) {
        // 分页对象
        Page<Space> spacePage = new Page<>(spaceQueryDTO.getCurrent(), spaceQueryDTO.getPageSize());

        // 构造查询条件
        LambdaQueryWrapper<Space> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(Space::getSpaceType, SpaceTypeEnum.TEAM.getValue());

        // 空间名称搜索
        if (StringUtils.isNotBlank(spaceQueryDTO.getSpaceName())) {
            lambdaQueryWrapper.like(Space::getSpaceName, spaceQueryDTO.getSpaceName());
        }

        // 空间类型过滤
        if (spaceQueryDTO.getSpaceLevel() != null) {
            lambdaQueryWrapper.eq(Space::getSpaceLevel, spaceQueryDTO.getSpaceLevel());
        }

        // 按创建时间排序
        if (SpaceOrderConstant.DESC.equals(spaceQueryDTO.getOrder())) {
            lambdaQueryWrapper.orderByDesc(Space::getCreateTime);
        } else {
            lambdaQueryWrapper.orderByAsc(Space::getCreateTime);
        }

        // 执行分页查询
        Page<Space> spaceList = this.page(spacePage, lambdaQueryWrapper);

        // 获取分页里的数据记录
        List<Space> records = spaceList.getRecords();

        // 转换成 VO 列表
        List<SpaceVO> spaceVOList = new ArrayList<>();
        for (Space space: records) {
            SpaceVO spaceVO = new SpaceVO();
            BeanUtils.copyProperties(space ,spaceVO);
            spaceVO.setSpaceLevelText(spaceVO.getSpaceLevelText());
            spaceVOList.add(spaceVO);
        }

        // 构造返回的分页对象（保持分页信息）
        Page<SpaceVO> result = new Page<>(spacePage.getCurrent(), spacePage.getSize(), spacePage.getTotal());
        result.setRecords(spaceVOList);

        return result;
    }

    @Override
    public List<SpaceCountVO> spaceCount() {
        List<SpaceCountVO> result = new ArrayList<>();

        return result;
    }

}
