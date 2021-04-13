package com.kkb.dao;

import com.kkb.bean.Express;
import com.kkb.exception.DuplicateCodeException;

import java.util.List;
import java.util.Map;

public interface BaseExpressDao {
    /**
     * 查询控制台所需快递数据
     * @return 返回List集合
     *         第一个元素是 {size:快递总数, day:今日新增快递数}
     *         第二个元素是 {size:代取件数, day:今日新增待取件数}
     */
    List<Map<String,Integer>> console();

    /**
     * 查询所有快递
     * @param limit 分页标记，true为分页，false为不分页
     * @param offset_pageNumber limit为true时，传入offset第几个快递记录开始和pageNumber多少个快递记录
     * @return 返回查询快递的集合
     */
    List<Express> findAll(boolean limit, int... offset_pageNumber);

    /**
     * 根据快递单号，查询快递信息
     * @param number 快递单号
     * @return 返回快递对象，不存在则返回null
     */
    Express findByNumber(String number);

    /**
     * 根据快递取件码，查询快递信息
     * @param code 快递单号
     * @return 返回快递对象，不存在则返回null
     */
    Express findByCode(String code);

    /**
     * 根据用户手机号码，查询他所有快递信息
     * @param userphone 手机号码
     * @return 返回快递对象列表，不存在则返回空列表
     */
    List<Express> findByUserPhone(String userphone);

    /**
     * 根据用户手机号码和是否取件，查询他相关的所有快递信息
     * @param userphone 用户手机号码
     * @param status 取件状态，0为待取件，1为已取件
     * @return 返回快递对象列表，不存在则返回空列表
     */
    List<Express> findByUserPhoneAndStatus(String userphone, int status);

    /**
     * 根据录入人手机号码，查询他录入的所有快递信息
     * @param sysPhone 手机号码
     * @return 返回快递对象列表，不存在则返回空列表
     */
    List<Express> findBySysPhone(String sysPhone);

    /**
     * 录入快递
     * @param e 待录入的快递对象
     * @return 返回录入结果，true为录入成功， false为录入失败
     */
    boolean insert(Express e) throws DuplicateCodeException;

    /**
     * 修改快递
     * @param id 待修改的快递id
     * @param newExpress 新的快递对象（number, company, username, userphone）
     * @return 返回修改的结果，true为修改成功，false为修改失败
     */
    boolean update(int id, Express newExpress);

    /**
     * 更改快递的状态为1，表示已取件
     * @param code 要修改快递的取件码
     * @return 返回更新的结果，true为更新成功，false为更新失败
     */
    boolean updateStatus(String code);

    /**
     * 根据id,删除单个快递信息
     * @param id 要删除的快递id
     * @return 返回删除的结果，true为删除成功，false为删除失败
     */
    boolean delete(int id);
}
