package com.kkb.dao.impl;

import com.kkb.dao.BaseAdminDao;
import com.kkb.utils.DruidUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AdminDaoMysql implements BaseAdminDao {

    private static final String SQL_UPDATE_LOGIN_TIME_IP = "UPDATE EADMIN SET LOGINIP = ?, LOGINTIME = ? WHERE USERNAME = ?";
    private static final String SQL_LOGIN = "SELECT ID FROM EADMIN WHERE USERNAME = ? AND PASSWORD = ?";

    /**
     * 根据用户名，更新登录时间及ip地址
     *
     * @param ip       待更新的ip地址
     * @param date     待更新的登录时间
     * @param username 更新指定的用户名
     */
    @Override
    public void updateLoginTimeAndIp(String ip, Date date, String username) {
        // 1. 从连接池获取连接
        Connection connection = DruidUtil.getConnection();
        // 2. 预处理sql语句
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_LOGIN_TIME_IP);
            // 3. 将参数填入SQL语句
            preparedStatement.setString(1, ip);
            preparedStatement.setTimestamp(2, new java.sql.Timestamp(date.getTime()));
            preparedStatement.setString(3, username);
            // 4. 执行SQL语句
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DruidUtil.close(connection, preparedStatement);
        }
    }

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @return true为登录成功，false为登录失败
     */
    @Override
    public boolean login(String username, String password) {
        // 1. 从连接池获取连接
        Connection connection = DruidUtil.getConnection();
        // 2. 预处理sql语句
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_LOGIN);
            // 3. 将参数填入SQL语句
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            // 4. 执行SQL语句
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DruidUtil.close(connection, preparedStatement, resultSet);
        }

        return false;
    }
}
