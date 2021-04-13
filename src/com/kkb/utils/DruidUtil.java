package com.kkb.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DruidUtil {

    private static DataSource ds;
    static {
        Properties ppt = new Properties();
        InputStream is = DruidUtil.class.getClassLoader().getResourceAsStream("druid.properties");
        try {
            ppt.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ds = DruidDataSourceFactory.createDataSource(ppt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从连接池中获取一个连接
     * @return 返回一个连接对象
     */
    public static Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * 用于关闭从连接对象获取的资源
     * @param connection 待释放的连接对象
     * @param statement 待释放的连接环境
     */
    public static void close(Connection connection, Statement statement) {
        try {
            connection.close();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        try {
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 用于关闭从连接对象获取的资源
     * @param connection 待释放的连接对象
     * @param statement 待释放的连接环境
     * @param resultSet 待释放的结果集合
     */
    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            connection.close();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

        try {
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            resultSet.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
