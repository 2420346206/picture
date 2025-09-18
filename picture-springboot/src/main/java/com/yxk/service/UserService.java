package com.yxk.service;

import com.yxk.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    /**
     * 获取当前登录用户
     */
    User getCurrentUserInfo(HttpServletRequest request);

    /**
     * 登录后返回 token
     */
    String login(String userAccount, String userPassword, HttpServletRequest request);

}
