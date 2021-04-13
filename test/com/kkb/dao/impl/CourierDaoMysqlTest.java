package com.kkb.dao.impl;

import com.kkb.bean.Courier;
import com.kkb.dao.BaseCourierDao;
import org.junit.Test;

import static org.junit.Assert.*;

public class CourierDaoMysqlTest {

    BaseCourierDao dao = new CourierDaoMysql();

    @Test
    public void console() {
        System.out.println(dao.console());
    }

    @Test
    public void findAll() {
//        System.out.println(dao.findAll(false));
        System.out.println(dao.findAll(true, 1, 1));
    }

    @Test
    public void findByIDNumber() {
//        System.out.println(dao.findByIDNumber("563875199507243364"));
        System.out.println(dao.findByIDNumber("888888888888888888"));
    }

    @Test
    public void findBySysPhone() {
//        System.out.println(dao.findBySysPhone("18888588888"));
        System.out.println(dao.findBySysPhone("18888585888"));
    }

    @Test
    public void insert() {
        System.out.println(dao.insert(new Courier("赵六", "152266998845", "454669199502153364", "156")));
    }

    @Test
    public void insertCouriers() {
//        for (int i = 0; i < 100; i++) {
//            dao.insert(new Courier("张三" + i, Long.toString(152266998846L + i), Long.toString(454669199502153364L + i), "123"));
//        }
    }

    @Test
    public void update() {
        System.out.println(dao.update(4, new Courier("赵六12", "152266998845", "454669199502153364", "156")));
    }

    @Test
    public void delete() {
        System.out.println(dao.delete(4));
    }

    @Test
    public void login() {

    }

    @Test
    public void addDeliveryNum() {
        System.out.println(dao.addDeliveryNum(5));
    }
}