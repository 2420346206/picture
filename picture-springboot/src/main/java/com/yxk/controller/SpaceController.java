package com.yxk.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxk.common.BaseResponse;
import com.yxk.common.ErrorCode;
import com.yxk.model.dto.SpaceAddDTO;
import com.yxk.model.dto.SpaceQueryDTO;
import com.yxk.model.entity.User;
import com.yxk.model.vo.SpaceVO;
import com.yxk.service.SpaceService;
import com.yxk.service.UserService;
import com.yxk.utils.ResultUtils;
import com.yxk.utils.ThrowUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/space")
public class SpaceController {

    @Autowired
    private UserService userService;

    @Autowired
    private SpaceService spaceService;

    @PostMapping("/add")
    public BaseResponse<Long> addSpace(@RequestBody SpaceAddDTO spaceAddDTO, HttpServletRequest request) {
        ThrowUtils.throwIf(spaceAddDTO == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getCurrentUserInfo(request);
        long newId = spaceService.addSpace(spaceAddDTO, loginUser);
        return ResultUtils.success(newId);
    }

    @PostMapping("/list/page")
    public BaseResponse<Page<SpaceVO>> spaceList(@RequestBody SpaceQueryDTO spaceQueryDTO, HttpServletRequest request) {
        Page<SpaceVO> spacePage = spaceService.getSpaceVOPage(spaceQueryDTO, request);
        return ResultUtils.success(spacePage);
    }

}
