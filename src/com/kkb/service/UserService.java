package com.kkb.service;



import com.kkb.bean.User;
import com.kkb.dao.BaseUserDao;
import com.kkb.dao.impl.UserDaoImpl;
import com.kkb.exception.DuplicateIDNumberException;
import com.kkb.exception.DuplicateUserNameException;
import com.kkb.exception.DuplicateUserPhoneException;

import java.util.List;
import java.util.Map;

public class UserService {
    private static final BaseUserDao DAO = new UserDaoImpl();

    public static List<Map<String,Integer>> console(){
        return DAO.console();
    }

    public static List<User> findAll(boolean limit , int offset, int pageNumber){
        return DAO.findAll(limit, offset, pageNumber);
    }

    /**
     * 根据原有快递单号，修改快递信息
     * @param id
     * @param newUser
     * @return
     */
    public static String update(int id,User newUser){
        User oldUser = findById(id);
        if (oldUser.getLoginTime() !=null){
            newUser.setLoginTime(oldUser.getLoginTime());
        }
        newUser.setRegisterTime(oldUser.getRegisterTime());
        try {
            return DAO.update(id,newUser)+"";
        } catch (DuplicateUserNameException e) {
            return "用户名已存在";
        } catch (DuplicateUserPhoneException e) {
            return "该手机号已注册";
        } catch (DuplicateIDNumberException e) {
            return "该身份证号已注册";
        }
    }

    /**
     * 添加新的快递
     * @param u
     * @return
     */
    public static String insert(User u){
        try {
            return DAO.insert(u)+"";
        } catch (DuplicateUserNameException e) {
            return "用户名已存在";
        } catch (DuplicateUserPhoneException e) {
            return "该手机号已注册";
        } catch (DuplicateIDNumberException e) {
            return "该身份证号已注册";
        }
    }

    /**
     * 删除指定快递单号的快递
     * @param id
     * @return
     */
    public static boolean delete(int id){return DAO.delete(id);}

    /**
     * 根据用户id查找用户数据
     * @param id
     * @return
     */
    public static User findById(int id){return DAO.findById(id);}

    /**
     * 根据用户userName查找用户数据
     * @param userName
     * @return
     */
    public static User findByUserName(String userName){return DAO.findByUserName(userName);}

    /**
     * 根据用户id查找用户数据
     * @param userPhone
     * @return
     */
    public static User findByUserPhone(String userPhone){return DAO.findByUserPhone(userPhone);}

    /**
     * 根据用户身份证号查找用户数据
     * @param idNumber
     * @return
     */
    public static User findByIdNumber(String idNumber){return DAO.findByIdNumber(idNumber);}

    /**
     * 用于普通用户登录系统时记录登录时间
     *
     * @param userPhone 普通用户手机
     * @return 返回记录的结果，true为记录成功，false为记录失败
     */
    public static boolean login(String userPhone) {
        return DAO.login(userPhone);
    }
}
