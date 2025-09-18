package com.yxk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginDto implements Serializable {

    private static final long serialVersionUID = 8735650154179439661L;

    /**
     * 账号
     */
    @JsonProperty("username")
    private String userAccount;

    /**
     * 密码
     */
    @JsonProperty("password")
    private String userPassword;

}
