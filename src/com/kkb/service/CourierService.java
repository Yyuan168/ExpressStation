package com.kkb.service;

import com.kkb.bean.Courier;
import com.kkb.dao.BaseCourierDao;
import com.kkb.dao.impl.CourierDaoMysql;

import java.util.List;
import java.util.Map;

public class CourierService {

    private static final BaseCourierDao DAO = new CourierDaoMysql();

    /**
     * 查询控制台所需快递员数据
     *
     * @return 返回Map集合
     * {size:快递员总人数, day:今日新注册快递员数量}
     */
    public static Map<String, Integer> console() {
        return DAO.console();
    }

    /**
     * 查询所有快递员
     *
     * @param limit             分页标记，true为分页，false为不分页
     * @param offset_pageNumber limit为true时，传入offset第几个快递员记录开始和pageNumber多少个快递员记录
     * @return 返回查询快递员的集合
     */
    public static List<Courier> findAll(boolean limit, int... offset_pageNumber) {
        return DAO.findAll(limit, offset_pageNumber);
    }

    /**
     * 根据快递员身份证号码，查询快递员的详细信息
     *
     * @param idNumber 快递员身份证号码
     * @return 返回快递对象，不存在则返回null
     */
    public static Courier findByIDNumber(String idNumber) {
        return DAO.findByIDNumber(idNumber);
    }

    /**
     * 根据快递员手机号码，查询快递员的详细信息
     *
     * @param sysPhone 快递员手机号码
     * @return 返回快递员对象，不存在则返回null
     */
    public static Courier findBySysPhone(String sysPhone) {
        return DAO.findBySysPhone(sysPhone);
    }

    /**
     * 添加快递员
     *
     * @param c 待添加的快递员
     * @return 返回添加结果，true为添加成功， false为添加失败
     */
    public static boolean insert(Courier c) {
        return DAO.insert(c);
    }

    /**
     * 修改快递源信息
     *
     * @param id         待修改的快递员id
     * @param newCourier 新的快递员对象（cname, sysPhone, idNumber, password）
     * @return 返回修改的结果，true为修改成功，false为修改失败
     */
    public static boolean update(int id, Courier newCourier) {
        return DAO.update(id, newCourier);
    }

    /**
     * 根据id,删除快递员
     *
     * @param id 要删除的快递员id
     * @return 返回删除的结果，true为删除成功，false为删除失败
     */
    public static boolean delete(int id) {
        return DAO.delete(id);
    }

    /**
     * 用于快递员登录系统时记录登录时间
     *
     * @param sysPhone 快递员手机
     * @return 返回记录的结果，true为记录成功，false为记录失败
     */
    public static boolean login(String sysPhone) {
        return DAO.login(sysPhone);
    }

    /**
     * 用于快递员录入快件时让该快递员的快件数自增
     *
     * @param id 快递员id
     * @return 返回自增的结果，true为自增成功，false为自增失败
     */
    public static boolean addDeliveryNum(int id) {
        return DAO.addDeliveryNum(id);
    }
}
