package com.yxk.controller;

import com.yxk.entity.User;
import com.yxk.common.BaseResponse;
import com.yxk.utils.ResultUtils;
import com.yxk.dto.UserLoginDTO;
import com.yxk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前登录用户
     */
    @GetMapping("/info")
    public BaseResponse<User> getUserByToken(HttpServletRequest request) {
        User user = userService.getCurrentUserInfo(request);
        return ResultUtils.success(user);
    }

    @PostMapping("/login")
    public BaseResponse<String> login(@RequestBody UserLoginDTO userLoginDto, HttpServletRequest request) {
        String userAccount = userLoginDto.getUserAccount();
        String userPassword = userLoginDto.getUserPassword();
        String token = userService.login(userAccount, userPassword, request);
        return ResultUtils.success(token);
    }

}
