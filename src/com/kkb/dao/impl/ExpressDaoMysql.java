package com.kkb.dao.impl;

import com.kkb.bean.Express;
import com.kkb.dao.BaseExpressDao;
import com.kkb.exception.DuplicateCodeException;
import com.kkb.utils.DruidUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressDaoMysql implements BaseExpressDao {
    // SQL语句
    // 用于查询数据库中全部快递（总数+当日新增），待取件快递（总数+当日新增）
    public static final String SQL_CONSOLE = "SELECT COUNT(*) AS DATA1_SIZE, COUNT(TO_DAYS(INTIME)=TO_DAYS(NOW()) OR NULL) AS DATA1_DAY, COUNT(STATUS=0 OR NULL) AS DATA2_SIZE, COUNT(TO_DAYS(INTIME)=TO_DAYS(NOW()) AND STATUS=0 OR NULL) AS DATA2_DAY FROM EXPRESS";
    // 用于查询数据库中的所有快递信息
    public static final String SQL_FIND_ALL = "SELECT * FROM EXPRESS";
    // 用于分页查询数据库中的快递信息
    public static final String SQL_FIND_LIMIT = "SELECT * FROM EXPRESS LIMIT ?, ?";
    // 通过取件码查询快递信息
    public static final String SQL_FIND_BY_CODE = "SELECT * FROM EXPRESS WHERE CODE = ?";
    // 通过快递单号查询快递信息
    public static final String SQL_FIND_BY_NUMBER = "SELECT * FROM EXPRESS WHERE NUMBER = ?";
    // 通过录入人查询快递信息
    public static final String SQL_FIND_BY_SYSPHONE = "SELECT * FROM EXPRESS WHERE SYSPHONE = ?";
    // 通过用户手机号码查询快递信息
    public static final String SQL_FIND_BY_USERPHONE = "SELECT * FROM EXPRESS WHERE USERPHONE = ?";
    // 通过用户手机号码和状态码查询快递信息
    public static final String SQL_FIND_BY_USERPHONE_AND_STATUS = "SELECT * FROM EXPRESS WHERE USERPHONE = ? AND STATUS = ?";
    // 录入快递
    public static final String SQL_INSERT = "INSERT INTO EXPRESS (NUMBER, USERNAME, USERPHONE, COMPANY, CODE, INTIME, STATUS, SYSPHONE) VALUES (?, ?, ?, ?, ?, NOW(), 0, ?)";
    // 快递修改
    public static final String SQL_UPDATE = "UPDATE EXPRESS SET NUMBER = ?, USERNAME = ?, COMPANY = ?, USERPHONE = ? WHERE ID = ?";
    // 快递的状态码改变（取件）
    public static final String SQL_UPDATE_STATUS = "UPDATE EXPRESS SET STATUS = 1, OUTTIME = NOW(), CODE = NULL WHERE CODE = ?";
    // 删除快递
    public static final String SQL_DELETE = "DELETE FROM EXPRESS WHERE ID = ?";

    /**
     * 查询控制台所需快递数据
     *
     * @return 返回List集合
     * 第一个元素是 {size:快递总数, day:今日新增快递数}
     * 第二个元素是 {size:代取件数, day:今日新增待取件数}
     */
    @Override
    public List<Map<String, Integer>> console() {
        List<Map<String, Integer>> list = new ArrayList<>();
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
                Map<String, Integer> data1 = new HashMap<>();
                data1.put("data1_size", resultSet.getInt("data1_size"));
                data1.put("data1_day", resultSet.getInt("data1_day"));
                list.add(data1);
                Map<String, Integer> data2 = new HashMap<>();
                data2.put("data2_size", resultSet.getInt("data2_size"));
                data2.put("data2_day", resultSet.getInt("data2_day"));
                list.add(data2);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            // 6. 资源的释放
            DruidUtil.close(connection, preparedStatement, resultSet);
        }
        return list;
    }

    /**
     * 查询所有快递
     *
     * @param limit 分页标记，true为分页，false为不分页
     * @param offset_pageNumber limit为true时，传入offset第几个快递记录开始和pageNumber多少个快递记录
     * @return 返回查询快递的集合
     */
    @Override
    public List<Express> findAll(boolean limit, int... offset_pageNumber) {
        List<Express> data = new ArrayList<>();
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
                String number = resultSet.getString("number");
                String username = resultSet.getString("username");
                String userphone = resultSet.getString("userphone");
                String company = resultSet.getString("company");
                String code = resultSet.getString("code");
                Timestamp intime = resultSet.getTimestamp("intime");
                Timestamp outtime = resultSet.getTimestamp("outtime");
                int status = resultSet.getInt("status");
                String sysPhone = resultSet.getString("sysPhone");
                Express express = new Express(id, number, username, userphone, company, code, intime, outtime, status, sysPhone);
                data.add(express);
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
     * 根据快递单号，查询快递信息
     *
     * @param number 快递单号
     * @return 返回快递对象，不存在则返回null
     */
    @Override
    public Express findByNumber(String number) {
        // 1. 获取数据库的连接
        Connection connection = DruidUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        // 2. 预编译SQL语句
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_BY_NUMBER);
            // 3. 填充参数（可选）
            preparedStatement.setString(1, number);
            // 4. 执行SQL语句
            resultSet = preparedStatement.executeQuery();
            // 5. 获取执行的结果
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String userphone = resultSet.getString("userphone");
                String company = resultSet.getString("company");
                String code = resultSet.getString("code");
                Timestamp intime = resultSet.getTimestamp("intime");
                Timestamp outtime = resultSet.getTimestamp("outtime");
                int status = resultSet.getInt("status");
                String sysPhone = resultSet.getString("sysPhone");
                Express express = new Express(id, number, username, userphone, company, code, intime, outtime, status, sysPhone);
                return express;
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
     * 根据快递取件码，查询快递信息
     *
     * @param code 快递单号
     * @return 返回快递对象，不存在则返回null
     */
    @Override
    public Express findByCode(String code) {
        // 1. 获取数据库的连接
        Connection connection = DruidUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        // 2. 预编译SQL语句
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_BY_CODE);
            // 3. 填充参数（可选）
            preparedStatement.setString(1, code);
            // 4. 执行SQL语句
            resultSet = preparedStatement.executeQuery();
            // 5. 获取执行的结果
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String number = resultSet.getString("number");
                String username = resultSet.getString("username");
                String userphone = resultSet.getString("userphone");
                String company = resultSet.getString("company");
                Timestamp intime = resultSet.getTimestamp("intime");
                Timestamp outtime = resultSet.getTimestamp("outtime");
                int status = resultSet.getInt("status");
                String sysPhone = resultSet.getString("number");
                Express express = new Express(id, number, username, userphone, company, code, intime, outtime, status, sysPhone);
                return express;
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
     * 根据用户手机号码，查询他所有快递信息
     *
     * @param userphone 手机号码
     * @return 返回快递对象列表，不存在则返回空列表
     */
    @Override
    public List<Express> findByUserPhone(String userphone) {
        List<Express> data = new ArrayList<>();
        // 1. 获取数据库的连接
        Connection connection = DruidUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        // 2. 预编译SQL语句
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_BY_USERPHONE);
            // 3. 填充参数（可选）
            preparedStatement.setString(1, userphone);
            // 4. 执行SQL语句
            resultSet = preparedStatement.executeQuery();
            // 5. 获取执行的结果
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String number = resultSet.getString("number");
                String username = resultSet.getString("username");
                String company = resultSet.getString("company");
                String code = resultSet.getString("code");
                Timestamp intime = resultSet.getTimestamp("intime");
                Timestamp outtime = resultSet.getTimestamp("outtime");
                int status = resultSet.getInt("status");
                String sysPhone = resultSet.getString("sysPhone");
                Express express = new Express(id, number, username, userphone, company, code, intime, outtime, status, sysPhone);
                data.add(express);
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
     * 根据用户手机号码和是否取件，查询他相关的所有快递信息
     *
     * @param userphone 用户手机号码
     * @param status    取件状态，0为待取件，1为已取件
     * @return 返回快递对象列表，不存在则返回空列表
     */
    @Override
    public List<Express> findByUserPhoneAndStatus(String userphone, int status) {
        List<Express> data = new ArrayList<>();
        // 1. 获取数据库的连接
        Connection connection = DruidUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        // 2. 预编译SQL语句
        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_BY_USERPHONE_AND_STATUS);
            // 3. 填充参数（可选）
            preparedStatement.setString(1, userphone);
            preparedStatement.setInt(2, status);
            // 4. 执行SQL语句
            resultSet = preparedStatement.executeQuery();
            // 5. 获取执行的结果
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String number = resultSet.getString("number");
                String username = resultSet.getString("username");
                String company = resultSet.getString("company");
                String code = resultSet.getString("code");
                Timestamp intime = resultSet.getTimestamp("intime");
                Timestamp outtime = resultSet.getTimestamp("outtime");
                String sysPhone = resultSet.getString("sysPhone");
                Express express = new Express(id, number, username, userphone, company, code, intime, outtime, status, sysPhone);
                data.add(express);
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
     * 根据录入人手机号码，查询他录入的所有快递信息
     *
     * @param sysPhone 手机号码
     * @return 返回快递对象列表，不存在则返回空列表
     */
    @Override
    public List<Express> findBySysPhone(String sysPhone) {
        List<Express> data = new ArrayList<>();
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
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String number = resultSet.getString("number");
                String username = resultSet.getString("username");
                String userphone = resultSet.getString("userphone");
                String company = resultSet.getString("company");
                String code = resultSet.getString("code");
                Timestamp intime = resultSet.getTimestamp("intime");
                Timestamp outtime = resultSet.getTimestamp("outtime");
                int status = resultSet.getInt("status");
                Express express = new Express(id, number, username, userphone, company, code, intime, outtime, status, sysPhone);
                data.add(express);
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
     * 录入快递
     *
     * @param e 待录入的快递对象
     * @return 返回录入结果，true为录入成功， false为录入失败
     */
    @Override
    public boolean insert(Express e) throws DuplicateCodeException {
        // 1. 获取数据库的连接
        Connection connection = DruidUtil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            // 2. 预编译SQL语句
            preparedStatement = connection.prepareStatement(SQL_INSERT);
            // 3. 填充参数
            preparedStatement.setString(1, e.getNumber());
            preparedStatement.setString(2, e.getUsername());
            preparedStatement.setString(3, e.getUserphone());
            preparedStatement.setString(4, e.getCompany());
            preparedStatement.setString(5, e.getCode());
            preparedStatement.setString(6, e.getSysPhone());
            // 4. 执行SQL语句，并获取执行结果
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e1) {
            if (e1.getMessage().endsWith("for key 'code'")) {
                // 取件码重复，出现异常
                throw new DuplicateCodeException(e1.getMessage());
            } else {
                e1.printStackTrace();
            }
        } finally {
            // 5. 释放资源
            DruidUtil.close(connection, preparedStatement);
        }
        return false;
    }

    /**
     * 修改快递
     *
     * @param id         待修改的快递id
     * @param newExpress 新的快递对象（number, company, username, userphone）
     * @return 返回修改的结果，true为修改成功，false为修改失败
     */
    @Override
    public boolean update(int id, Express newExpress) {
        Connection connection = DruidUtil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE);
            preparedStatement.setString(1, newExpress.getNumber());
            preparedStatement.setString(2, newExpress.getUsername());
            preparedStatement.setString(3, newExpress.getCompany());
            preparedStatement.setString(4, newExpress.getUserphone());
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
     * 更改快递的状态为1，表示已取件
     *
     * @param code 要修改快递的取件码
     * @return 返回更新的结果，true为更新成功，false为更新失败
     */
    @Override
    public boolean updateStatus(String code) {
        Connection connection = DruidUtil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_STATUS);
            preparedStatement.setString(1, code);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DruidUtil.close(connection, preparedStatement);
        }
        return false;
    }

    /**
     * 根据id,删除单个快递信息
     *
     * @param id 要删除的快递id
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
}
