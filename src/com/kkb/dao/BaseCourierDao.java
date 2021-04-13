package com.kkb.dao;

import com.kkb.bean.Courier;

import java.util.List;
import java.util.Map;

public interface BaseCourierDao {
    /**
     * 查询控制台所需快递员数据
     * @return 返回Map集合
     *         {size:快递员总人数, day:今日新注册快递员数量}
     */
    Map<String,Integer> console();

    /**
     * 查询所有快递员
     * @param limit 分页标记，true为分页，false为不分页
     * @param offset_pageNumber limit为true时，传入offset第几个快递员记录开始和pageNumber多少个快递员记录
     * @return 返回查询快递员的集合
     */
    List<Courier> findAll(boolean limit, int... offset_pageNumber);

    /**
     * 根据快递员身份证号码，查询快递员的详细信息
     * @param idNumber 快递员身份证号码
     * @return 返回快递对象，不存在则返回null
     */
    Courier findByIDNumber(String idNumber);

    /**
     * 根据快递员手机号码，查询快递员的详细信息
     * @param sysPhone 快递员手机号码
     * @return 返回快递员对象，不存在则返回null
     */
    Courier findBySysPhone(String sysPhone);

    /**
     * 添加快递员
     * @param c 待添加的快递员
     * @return 返回添加结果，true为添加成功， false为添加失败
     */
    boolean insert(Courier c);

    /**
     * 修改快递源信息
     * @param id 待修改的快递员id
     * @param newCourier 新的快递员对象（cname, sysPhone, idNumber, password）
     * @return 返回修改的结果，true为修改成功，false为修改失败
     */
    boolean update(int id, Courier newCourier);

    /**
     * 根据id,删除快递员
     * @param id 要删除的快递员id
     * @return 返回删除的结果，true为删除成功，false为删除失败
     */
    boolean delete(int id);

    /**
     * 用于快递员登录系统时记录登录时间
     * @param sysPhone 快递员手机号码
     * @return 返回记录的结果，true为记录成功，false为记录失败
     */
    boolean login(String sysPhone);

    /**
     * 用于快递员录入快件时让该快递员的快件数自增
     * @param id 快递员id
     * @return 返回自增的结果，true为自增成功，false为自增失败
     */
    boolean addDeliveryNum(int id);
}
