package com.yxk.service;

import com.yxk.bean.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    User get();

    User login(String userAccount, String userPassword, HttpServletRequest request);

    User getUserByToken(String token);

}
