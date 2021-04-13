package com.kkb.service;

import com.kkb.dao.BaseAdminDao;
import com.kkb.dao.impl.AdminDaoMysql;

import java.util.Date;

public class AdminService {
    private static BaseAdminDao dao = new AdminDaoMysql();

    /**
     * 后台服务层，根据用户名，更新登录时间及ip地址
     * @param ip 待更新的ip地址
     * @param date 待更新的登录时间
     * @param username 更新指定的用户名
     */
    public static void updateLoginTimeAndIp(String ip, Date date, String username) {
        dao.updateLoginTimeAndIp(ip, date, username);
    }

    /**
     * 后台服务层，登录验证
     * @param username 用户名
     * @param password 密码
     * @return true为登录成功，false为登录失败
     */
    public static boolean login(String username, String password) {
        return dao.login(username, password);
    }
}
