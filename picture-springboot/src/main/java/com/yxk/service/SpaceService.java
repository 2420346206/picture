package com.yxk.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yxk.model.dto.SpaceAddDTO;
import com.yxk.model.dto.SpaceQueryDTO;
import com.yxk.model.entity.Space;
import com.yxk.model.entity.User;
import com.yxk.model.vo.SpaceVO;

import javax.servlet.http.HttpServletRequest;

public interface SpaceService extends IService<Space> {

    /**
     * 创建团队空间
     */
    Long addSpace(SpaceAddDTO spaceAddDTO, User user);

    /**
     * 校验空间
     *
     * @param space
     * @param add   是否为创建时检验
     */
    void validSpace(Space space, boolean add);

    /**
     * 获取团队空间列表（分页）
     */
    Page<SpaceVO> getSpaceVOPage(SpaceQueryDTO spaceQueryDTO, HttpServletRequest request);
}
