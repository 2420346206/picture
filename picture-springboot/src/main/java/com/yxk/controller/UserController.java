package com.yxk.controller;

import com.yxk.bean.User;
import com.yxk.common.BaseResponse;
import com.yxk.common.ResultUtils;
import com.yxk.from.UserLoginFrom;
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
    @GetMapping("/get")
    public User getLoginUser() {
        return userService.get();
    }

    @PostMapping("/login")
    public BaseResponse<User> login(@RequestBody UserLoginFrom userLoginFrom, HttpServletRequest request) {
        String userAccount = userLoginFrom.getUserAccount();
        String userPassword = userLoginFrom.getUserPassword();
        User user = userService.login(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @GetMapping("/info")
    public BaseResponse<User> getUserByToken(@RequestParam("token") String token) {
        User user = userService.getUserByToken(token);
        return ResultUtils.success(user);
    }

}
