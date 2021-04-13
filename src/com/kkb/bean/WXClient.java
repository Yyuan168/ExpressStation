package com.kkb.bean;

// 用于存储微信登录对象的
public interface WXClient {
    // 获取登录用户类型
    String getType();
    // 获取对应手机号码
    String getPhonenumber();
}
