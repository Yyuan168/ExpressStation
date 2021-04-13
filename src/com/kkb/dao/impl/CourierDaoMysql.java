package com.kkb.dao.impl;

import com.kkb.bean.Courier;
import com.kkb.dao.BaseCourierDao;
import com.kkb.utils.DruidUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourierDaoMysql implements BaseCourierDao {

    // SQL语句
    // 用于查询数据库中全部快递员（总数+当日新增）
    public static final String SQL_CONSOLE = "SELECT COUNT(*) AS DATA_SIZE, COUNT(TO_DAYS(REGISTERTIME)=TO_DAYS(NOW()) OR NULL) AS DATA_DAY FROM COURIER";
    // 用于查询数据库中的所有快递员信息
    public static final String SQL_FIND_ALL = "SELECT * FROM COURIER";
    // 用于分页查询数据库中的快递员信息
    public static final String SQL_FIND_LIMIT = "SELECT * FROM COURIER LIMIT ?, ?";
    // 通过身份证号码查询快递员信息
    public static final String SQL_FIND_BY_IDNUMBER = "SELECT * FROM COURIER WHERE IDNUMBER = ?";
    // 通过快递员手机查询快递信息
    public static final String SQL_FIND_BY_SYSPHONE = "SELECT * FROM COURIER WHERE SYSPHONE = ?";
    // 添加快递员
    public static final String SQL_INSERT = "INSERT INTO COURIER (CNAME, SYSPHONE, IDNUMBER, PASSWORD, DELIVERYNUM, REGISTERTIME) VALUES (?, ?, ?, ?, 0, NOW())";
    // 修改快递员信息
    public static final String SQL_UPDATE = "UPDATE COURIER SET CNAME = ?, SYSPHONE = ?, IDNUMBER = ?, PASSWORD = ? WHERE ID = ?";
    // 删除快递员
    public static final String SQL_DELETE = "DELETE FROM COURIER WHERE ID = ?";
    // 用于记录快递员登录时间
    public static final String SQL_LOGIN = "UPDATE COURIER SET LOGINTIME = NOW() WHERE SYSPHONE = ?";
    // 用于快递员录入快递时快递件数自增
    public static final String SQL_ADD_DELIVERY_NUM = "UPDATE COURIER SET DELIVERYNUM = DELIVERYNUM + 1 WHERE ID = ?";

    /**
     * 查询控制台所需快递员数据
     *
     * @return 返回Map集合
     * {size:快递员总人数, day:今日新注册快递员数量}
     */
    @Override
    public Map<String, Integer> console() {
        Map<String, Integer> map = new HashMap<>();
        // 1. 获取数据库的连接
        Connection connection = DruidUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        // 2. 预编译SQL语句
        try {
            preparedStatement = connection.prepareStatement(SQL_CONSOLE);
            // 3. 填充参数（可选）
            // 4. 执行SQL语句
            resultSet = preparedStatement.executeQuery();
            // 5. 获取执行的结果
            if (resultSet.next()) {
                map.put("data_size", resultSet.getInt("data_size"));
                map.put("data_day", resultSet.getInt("data_day"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            // 6. 资源的释放
            DruidUtil.close(connection, preparedStatement, resultSet);
        }
        return map;
    }

    /**
     * 查询所有快递员
     *
     * @param limit             分页标记，true为分页，false为不分页
     * @param offset_pageNumber limit为true时，传入offset第几个快递员记录开始和pageNumber多少个快递员记录
     * @return 返回查询快递员的集合
     */
    @Override
    public List<Courier> findAll(boolean limit, int... offset_pageNumber) {
        List<Courier> data = new ArrayList<>();
        // 1. 获取数据库的连接
        Connection connection = DruidUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        // 2. 预编译SQL语句
        try {
            if (limit) {
                preparedStatement = connection.prepareStatement(SQL_FIND_LIMIT);
                // 3. 填充参数（可选）
                preparedStatement.setInt(1, offset_pageNumber[0]);
                preparedStatement.setInt(2, offset_pageNumber[1]);
            } else {
                preparedStatement = connection.prepareStatement(SQL_FIND_ALL);
            }
            // 4. 执行SQL语句
            resultSet = preparedStatement.executeQuery();
            // 5. 获取执行的结果
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String cname = resultSet.getString("cname");
                String sysPhone = resultSet.getString("sysPhone");
                String idNumber = resultSet.getString("idNumber");
                String password = resultSet.getString("password");
                int deliveryNum = resultSet.getInt("deliveryNum");
                Timestamp registerTime = resultSet.getTimestamp("registerTime");
                Timestamp loginTime = resultSet.getTimestamp("loginTime");
                data.add(new Courier(id, cname, sysPhone, idNumber, password, deliveryNum, registerTime, loginTime));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            // 6. 资源的释放
            DruidUtil.close(connection, preparedStatement, resultSet);
        }
        return data;
    }

    /**
     * 根据快递员身份证号码，查询快递员的详细信息
     *
     * @param idNumber 快递员身份证号码
     * @return 返回快递对象，不存在则返回null
     */
    @Override
    public Courier findByIDNumber(String idNumber) {
        // 1. 获取数据库的连接
        Connection connection = DruidUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        // 2. 预编译SQL语句
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_BY_IDNUMBER);
            // 3. 填充参数（可选）
            preparedStatement.setString(1, idNumber);
            // 4. 执行SQL语句
            resultSet = preparedStatement.executeQuery();
            // 5. 获取执行的结果
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String cname = resultSet.getString("cname");
                String sysPhone = resultSet.getString("sysPhone");
//                String idNumber = resultSet.getString("idNumber");
                String password = resultSet.getString("password");
                int deliveryNum = resultSet.getInt("deliveryNum");
                Timestamp registerTime = resultSet.getTimestamp("registerTime");
                Timestamp loginTime = resultSet.getTimestamp("loginTime");
                Courier courier = new Courier(id, cname, sysPhone, idNumber, password, deliveryNum, registerTime, loginTime);
                return courier;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            // 6. 资源的释放
            DruidUtil.close(connection, preparedStatement, resultSet);
        }
        return null;
    }

    /**
     * 根据快递员手机号码，查询快递员的详细信息
     *
     * @param sysPhone 快递员手机号码
     * @return 返回快递员对象，不存在则返回null
     */
    @Override
    public Courier findBySysPhone(String sysPhone) {
        // 1. 获取数据库的连接
        Connection connection = DruidUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        // 2. 预编译SQL语句
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_BY_SYSPHONE);
            // 3. 填充参数（可选）
            preparedStatement.setString(1, sysPhone);
            // 4. 执行SQL语句
            resultSet = preparedStatement.executeQuery();
            // 5. 获取执行的结果
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String cname = resultSet.getString("cname");
//                String sysPhone = resultSet.getString("sysPhone");
                String idNumber = resultSet.getString("idNumber");
                String password = resultSet.getString("password");
                int deliveryNum = resultSet.getInt("deliveryNum");
                Timestamp registerTime = resultSet.getTimestamp("registerTime");
                Timestamp loginTime = resultSet.getTimestamp("loginTime");
                Courier courier = new Courier(id, cname, sysPhone, idNumber, password, deliveryNum, registerTime, loginTime);
                return courier;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            // 6. 资源的释放
            DruidUtil.close(connection, preparedStatement, resultSet);
        }
        return null;
    }

    /**
     * 添加快递员
     *
     * @param c 待添加的快递员
     * @return 返回添加结果，true为添加成功， false为添加失败
     */
    @Override
    public boolean insert(Courier c) {
        // 1. 获取数据库的连接
        Connection connection = DruidUtil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            // 2. 预编译SQL语句
            preparedStatement = connection.prepareStatement(SQL_INSERT);
            // 3. 填充参数
            preparedStatement.setString(1, c.getCname());
            preparedStatement.setString(2, c.getSysPhone());
            preparedStatement.setString(3, c.getIdNumber());
            preparedStatement.setString(4, c.getPassword());
            // 4. 执行SQL语句，并获取执行结果
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            // 5. 释放资源
            DruidUtil.close(connection, preparedStatement);
        }
        return false;
    }

    /**
     * 修改快递源信息
     *
     * @param id         待修改的快递员id
     * @param newCourier 新的快递员对象（cname, sysPhone, idNumber, password）
     * @return 返回修改的结果，true为修改成功，false为修改失败
     */
    @Override
    public boolean update(int id, Courier newCourier) {
        Connection connection = DruidUtil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE);
            preparedStatement.setString(1, newCourier.getCname());
            preparedStatement.setString(2, newCourier.getSysPhone());
            preparedStatement.setString(3, newCourier.getIdNumber());
            preparedStatement.setString(4, newCourier.getPassword());
            preparedStatement.setInt(5, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DruidUtil.close(connection, preparedStatement);
        }
        return false;
    }

    /**
     * 根据id,删除快递员
     *
     * @param id 要删除的快递员id
     * @return 返回删除的结果，true为删除成功，false为删除失败
     */
    @Override
    public boolean delete(int id) {
        Connection connection = DruidUtil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DruidUtil.close(connection, preparedStatement);
        }
        return false;
    }

    /**
     * 用于快递员登录系统时记录登录时间
     *
     * @param sysPhone 快递员手机
     * @return 返回记录的结果，true为记录成功，false为记录失败
     */
    @Override
    public boolean login(String sysPhone) {
        Connection connection = DruidUtil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_LOGIN);
            preparedStatement.setString(1, sysPhone);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DruidUtil.close(connection, preparedStatement);
        }
        return false;
    }

    /**
     * 用于快递员录入快件时让该快递员的快件数自增
     *
     * @param id 快递员id
     * @return 返回自增的结果，true为自增成功，false为自增失败
     */
    @Override
    public boolean addDeliveryNum(int id) {
        Connection connection = DruidUtil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_ADD_DELIVERY_NUM);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DruidUtil.close(connection, preparedStatement);
        }
        return false;
    }

}
