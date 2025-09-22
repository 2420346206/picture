package com.yxk.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yxk.common.BusinessException;
import com.yxk.common.ErrorCode;
import com.yxk.constant.UserConstant;
import com.yxk.entity.User;
import com.yxk.mapper.UserMapper;
import com.yxk.service.UserService;
import com.yxk.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public User getCurrentUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(UserConstant.USER_LOGIN_STATE);
        return userMapper.selectById(userId);
    }

    @Override
    public String login(String userAccount, String userPassword, HttpServletRequest request) {
        // 校验
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号错误");
        }
        if (userPassword.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码错误");
        }
        // 查询数据库中的用户是否存在
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserAccount, userAccount);
        lambdaQueryWrapper.eq(User::getUserPassword, userPassword);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        // 不存在，抛异常
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或者密码错误");
        }
        // 返回token
        return jwtUtils.createToken(user.getId());
    }

}
