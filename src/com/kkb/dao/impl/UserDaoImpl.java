package com.kkb.dao.impl;

import com.kkb.bean.User;
import com.kkb.dao.BaseUserDao;
import com.kkb.exception.DuplicateIDNumberException;
import com.kkb.exception.DuplicateUserNameException;
import com.kkb.exception.DuplicateUserPhoneException;
import com.kkb.utils.DruidUtil;

import java.sql.*;
import java.util.Date;
import java.util.*;

public class UserDaoImpl implements BaseUserDao {
    private static final String SQL_USER_CONSOLE = "select count(id) data1_size," +
            "count((TO_DAYS(NOW())=TO_DAYS(registertime)) or null ) data1_perDay from USER";
    private static final String SQL_FIND_ALL = "select * from USER";
    private static final String SQL_FIND_BY_PAGE = "select * from USER limit ?,?";
    private static final String SQL_FIND_BY_ID = "select * from USER where id=?";
    private static final String SQL_FIND_BY_USERNAME = "select * from USER where username=?";
    private static final String SQL_FIND_BY_USERPHONE = "select * from USER where userphone=?";
    private static final String SQL_FIND_BY_IDNUMBER = "select * from USER where idnumber=?";
    private static final String SQL_INSERT = "insert into USER (username,userphone,idnumber,password,registertime,logintime) values(?,?,?,?,?,?) ";
    private static final String SQL_DELETE = "delete from USER where id=?";
    private static final String SQL_UPDATE = "update USER set username=?,userphone=?,idnumber=?,password=? where id=?";
    private static final String SQL_LOGIN = "UPDATE USER SET LOGINTIME = NOW() WHERE USERPHONE = ?";
    @Override
    public List<Map<String, Integer>> console() {
        Connection conn = DruidUtil.getConnection();
        PreparedStatement prState = null;
        ResultSet rs = null;
        ArrayList<Map<String, Integer>> data = new ArrayList<>();
        try {
            prState = conn.prepareStatement(SQL_USER_CONSOLE);
            rs = prState.executeQuery();
            while (rs.next()){
                int data1_size= rs.getInt("data1_size");
                int data1_perDay= rs.getInt("data1_perDay");
                Map<String,Integer> map = new HashMap<>();
                map.put("data1_size",data1_size);
                map.put("data1_perDay",data1_perDay);
                data.add(map);
            }
            return data;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DruidUtil.close(conn,prState,rs);
        }
        return null;
    }

    @Override
    public List<User> findAll(boolean limit, int offset, int pageNumber) {
        Connection conn = DruidUtil.getConnection();
        PreparedStatement prState = null;
        ResultSet rs = null;
        List<User> data = new ArrayList<>();
        try {
            if (limit){
                //分页查询操作
                prState = conn.prepareStatement(SQL_FIND_BY_PAGE);
                //mysql的limit的起始值是从0开始算的,
                //但每次传入的值会由bootstrapTable主导变更，
                //前后端程序都不需要考虑页面变化后传值变化的逻辑
                prState.setInt(1,offset);
                prState.setInt(2,pageNumber);
            }else {
                //不分页查询操作
                prState = conn.prepareStatement(SQL_FIND_ALL);
            }
            rs = prState.executeQuery();
            while (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("username"));
                user.setUserPhone(rs.getString("userphone"));
                user.setIdNumber(rs.getString("idnumber"));
                user.setPassword(rs.getString("password"));
                user.setRegisterTime(rs.getTimestamp("registertime"));
                user.setLoginTime(rs.getTimestamp("logintime"));
                data.add(user);
            }
            return data;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }finally {
            DruidUtil.close(conn,prState,rs);
        }
        return null;
    }

    @Override
    public boolean insert(User u) throws DuplicateUserNameException, DuplicateUserPhoneException, DuplicateIDNumberException {
        Connection conn = DruidUtil.getConnection();
        PreparedStatement prState = null;
        try {
            //分页查询操作
            prState = conn.prepareStatement(SQL_INSERT);
            prState.setString(1,u.getUserName());
            prState.setString(2,u.getUserPhone());
            prState.setString(3,u.getIdNumber());
            prState.setString(4,u.getPassword());
            if (u.getRegisterTime()==null){
                //如果未有注册时间，则是第一次注册，设置注册时间
                Date date = new Date();
                long time = date.getTime();
                Timestamp timestamp = new Timestamp(time);
                prState.setTimestamp(5,timestamp);
            }else {
                //如果不为空，则证明已经注册过了，读取原有值进行设置
                prState.setTimestamp(5,u.getRegisterTime());
            }

            if (u.getLoginTime()==null){
                prState.setTimestamp(6,null);
            }else {
                //如果不为空，则读取原有值进行设置
                prState.setTimestamp(6,u.getLoginTime());
            }
            return prState.executeUpdate()>0?true:false;
        } catch (SQLException u1) {
            if (u1.getMessage().endsWith("for key 'username'")) {
                DuplicateUserNameException userNameException = new DuplicateUserNameException(u1.getMessage());
                throw userNameException;
            }else if (u1.getMessage().endsWith("for key 'userphone'")){
                DuplicateUserPhoneException userPhoneException = new DuplicateUserPhoneException(u1.getMessage());
                throw userPhoneException;
            }else if (u1.getMessage().endsWith("for key 'idnumber'")){
                DuplicateIDNumberException idNumberException = new DuplicateIDNumberException(u1.getMessage());
                throw idNumberException;
            }
            else {
                u1.printStackTrace();
            }
        }
        finally {
            DruidUtil.close(conn,prState);
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        Connection conn = DruidUtil.getConnection();
        PreparedStatement prState = null;
        try {
            //分页查询操作
            prState = conn.prepareStatement(SQL_DELETE);
            prState.setInt(1,id);
            return prState.executeUpdate()>0?true:false;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DruidUtil.close(conn,prState);
        }
        return false;
    }

    @Override
    public boolean update(int id, User newUser) throws DuplicateUserNameException, DuplicateUserPhoneException, DuplicateIDNumberException{
        Connection conn = DruidUtil.getConnection();
        PreparedStatement prState = null;
        try {
            //分页查询操作
            prState = conn.prepareStatement(SQL_UPDATE);
            prState.setString(1,newUser.getUserName());
            prState.setString(2,newUser.getUserPhone());
            prState.setString(3,newUser.getIdNumber());
            prState.setString(4,newUser.getPassword());
            prState.setInt(5,id);
            return prState.executeUpdate()>0?true:false;
        } catch (SQLException u1) {
            if (u1.getMessage().endsWith("for key 'username'")) {
                DuplicateUserNameException userNameException = new DuplicateUserNameException(u1.getMessage());
                throw userNameException;
            }else if (u1.getMessage().endsWith("for key 'userphone'")){
                DuplicateUserPhoneException userPhoneException = new DuplicateUserPhoneException(u1.getMessage());
                throw userPhoneException;
            }else if (u1.getMessage().endsWith("for key 'idnumber'")){
                DuplicateIDNumberException idNumberException = new DuplicateIDNumberException(u1.getMessage());
                throw idNumberException;
            }
            else {
                u1.printStackTrace();
            }
        } finally {
            DruidUtil.close(conn,prState);
        }
        return false;
    }

    @Override
    public User findById(int id) {
        Connection conn = DruidUtil.getConnection();
        PreparedStatement prState = null;
        ResultSet rs = null;
        try {
            //分页查询操作
            prState = conn.prepareStatement(SQL_FIND_BY_ID);
            //mysql的limit的起始值是从0开始算的,
            //但每次传入的值会由bootstrapTable主导变更，
            //前后端程序都不需要考虑页面变化后传值变化的逻辑
            prState.setInt(1,id);
            rs = prState.executeQuery();
            if (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("username"));
                user.setUserPhone(rs.getString("userphone"));
                user.setIdNumber(rs.getString("idnumber"));
                user.setPassword(rs.getString("password"));
                user.setRegisterTime(rs.getTimestamp("registertime"));
                user.setLoginTime(rs.getTimestamp("logintime"));
                return user;
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }finally {
            DruidUtil.close(conn,prState,rs);
        }
        return null;
    }

    @Override
    public User findByUserName(String userName) {
        Connection conn = DruidUtil.getConnection();
        PreparedStatement prState = null;
        ResultSet rs = null;
        try {
            //分页查询操作
            prState = conn.prepareStatement(SQL_FIND_BY_USERNAME);
            //mysql的limit的起始值是从0开始算的,
            //但每次传入的值会由bootstrapTable主导变更，
            //前后端程序都不需要考虑页面变化后传值变化的逻辑
            prState.setString(1,userName);
            rs = prState.executeQuery();
            if (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("username"));
                user.setUserPhone(rs.getString("userphone"));
                user.setIdNumber(rs.getString("idnumber"));
                user.setPassword(rs.getString("password"));
                user.setRegisterTime(rs.getTimestamp("registertime"));
                user.setLoginTime(rs.getTimestamp("logintime"));
                return user;
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }finally {
            DruidUtil.close(conn,prState,rs);
        }
        return null;
    }

    @Override
    public User findByUserPhone(String userPhone) {
        Connection conn = DruidUtil.getConnection();
        PreparedStatement prState = null;
        ResultSet rs = null;
        try {
            //分页查询操作
            prState = conn.prepareStatement(SQL_FIND_BY_USERPHONE);
            //mysql的limit的起始值是从0开始算的,
            //但每次传入的值会由bootstrapTable主导变更，
            //前后端程序都不需要考虑页面变化后传值变化的逻辑
            prState.setString(1,userPhone);
            rs = prState.executeQuery();
            if (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("username"));
                user.setUserPhone(rs.getString("userphone"));
                user.setIdNumber(rs.getString("idnumber"));
                user.setPassword(rs.getString("password"));
                user.setRegisterTime(rs.getTimestamp("registertime"));
                user.setLoginTime(rs.getTimestamp("logintime"));
                return user;
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }finally {
            DruidUtil.close(conn,prState,rs);
        }
        return null;
    }

    @Override
    public User findByIdNumber(String idNumber) {
        Connection conn = DruidUtil.getConnection();
        PreparedStatement prState = null;
        ResultSet rs = null;
        try {
            //分页查询操作
            prState = conn.prepareStatement(SQL_FIND_BY_IDNUMBER);
            //mysql的limit的起始值是从0开始算的,
            //但每次传入的值会由bootstrapTable主导变更，
            //前后端程序都不需要考虑页面变化后传值变化的逻辑
            prState.setString(1,idNumber);
            rs = prState.executeQuery();
            if (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("username"));
                user.setUserPhone(rs.getString("userphone"));
                user.setIdNumber(rs.getString("idnumber"));
                user.setPassword(rs.getString("password"));
                user.setRegisterTime(rs.getTimestamp("registertime"));
                user.setLoginTime(rs.getTimestamp("logintime"));
                return user;
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }finally {
            DruidUtil.close(conn,prState,rs);
        }
        return null;
    }

    /**
     * 用于普通用户登录系统时记录登录时间
     *
     * @param userPhone 普通用户手机
     * @return 返回记录的结果，true为记录成功，false为记录失败
     */
    @Override
    public boolean login(String userPhone) {
        Connection connection = DruidUtil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_LOGIN);
            preparedStatement.setString(1, userPhone);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DruidUtil.close(connection, preparedStatement);
        }
        return false;
    }
}
