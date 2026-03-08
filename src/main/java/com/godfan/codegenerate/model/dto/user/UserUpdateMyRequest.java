package com.godfan.codegenerate.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新个人信息请求类
 */
@Data
public class UserUpdateMyRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户密码（可选）
     */
    private String userPassword;

    /**
     * 原密码验证（修改密码时需要）
     */
    private String oldPassword;

    private static final long serialVersionUID = 1L;
}
