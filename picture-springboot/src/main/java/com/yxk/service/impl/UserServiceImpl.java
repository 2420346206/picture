package com.yxk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yxk.bean.User;
import com.yxk.mapper.UserMapper;
import com.yxk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getLoginUser(HttpServletRequest request) {
        return null;
    }

    @Override
    public User get() {
        return userMapper.selectById(1);
    }

    @Override
    public User login(String userAccount, String userPassword, HttpServletRequest request) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserAccount, userAccount);
        lambdaQueryWrapper.eq(User::getUserPassword, userPassword);
        return userMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public User getUserByToken(String token) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getToken, token);
        return userMapper.selectOne(lambdaQueryWrapper);
    }
}
