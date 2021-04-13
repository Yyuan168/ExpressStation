package com.kkb.dao;



import com.kkb.bean.User;
import com.kkb.exception.DuplicateIDNumberException;
import com.kkb.exception.DuplicateUserNameException;
import com.kkb.exception.DuplicateUserPhoneException;

import java.util.List;
import java.util.Map;

public interface BaseUserDao {
    /**
     * 用户部分的控制台显示（1.用户人数，日注册量）
     * @return
     */
    List<Map<String,Integer>> console();

    /**
     * 查询所有快递
     * @param limit    判断是否要分页
     * @param offset    从第几页开始查询
     * @param pageNumber    每一页的信息条目数量
     * @return
     */
    List<User> findAll(boolean limit , int offset, int pageNumber);

    /**
     * 添加新的快递
     * @param u
     * @return
     */
    boolean insert(User u) throws DuplicateUserNameException, DuplicateUserPhoneException, DuplicateIDNumberException;

    /**
     * 删除指定快递单号的快递
     * @param id
     * @return
     */
    boolean delete(int id);

    /**
     * 根据原有快递单号，修改快递信息
     * @param id
     * @param newUser
     * @return
     */
    boolean update(int id,User newUser) throws DuplicateUserNameException, DuplicateUserPhoneException, DuplicateIDNumberException;

    /**
     * 根据用户id查找用户数据
     * @param id
     * @return
     */
    User findById(int id);

    /**
     * 根据用户userName查找用户数据
     * @param userName
     * @return
     */
    User findByUserName(String userName);

    /**
     * 根据用户id查找用户数据
     * @param userPhone
     * @return
     */
    User findByUserPhone(String userPhone);

    /**
     * 根据用户身份证号查找用户数据
     * @param idNumber
     * @return
     */
    User findByIdNumber(String idNumber);

    /**
     * 用于普通用户登录系统时记录登录时间
     * @param userPhone 普通用户手机
     * @return 返回记录的结果，true为记录成功，false为记录失败
     */
    boolean login(String userPhone);
}
