package com.kkb.dao;

import java.util.Date;

public interface BaseAdminDao {

    /**
     * 根据用户名，更新登录时间及ip地址
     * @param ip 待更新的ip地址
     * @param date 待更新的登录时间
     * @param username 更新指定的用户名
     */
    void updateLoginTimeAndIp(String ip, Date date, String username);

    /**
     * 登录验证
     * @param username 用户名
     * @param password 密码
     * @return true为登录成功，false为登录失败
     */
    boolean login(String username, String password);
}
