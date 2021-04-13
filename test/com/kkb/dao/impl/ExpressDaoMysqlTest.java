package com.kkb.dao.impl;

import com.kkb.bean.Express;
import com.kkb.dao.BaseExpressDao;
import com.kkb.exception.DuplicateCodeException;
import org.junit.Test;

public class ExpressDaoMysqlTest {

    BaseExpressDao baseExpressDao = new ExpressDaoMysql();

    @Test
    public void console() {
        System.out.println(baseExpressDao.console());
    }

    @Test
    public void findAll() {
        System.out.println(baseExpressDao.findAll(false));
    }

    @Test
    public void findByNumber() {
        System.out.println(baseExpressDao.findByNumber("0"));
    }

    @Test
    public void findByCode() {
        System.out.println(baseExpressDao.findByCode("0"));
    }

    @Test
    public void findByUserPhone() {
        System.out.println(baseExpressDao.findByUserPhone("13843838438"));
    }

    @Test
    public void findBySysPhone() {
        System.out.println(baseExpressDao.findBySysPhone("18888888888"));
    }

    @Test
    public void insert() {
        Express express = new Express("555", "李四", "13843838438", "顺丰速递", "666666","18888888888");
        try {
            System.out.println(baseExpressDao.insert(express));
        } catch (DuplicateCodeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insert100() {
        for (int i = 0; i < 100; i++) {
            Express express = new Express(Integer.toString(600 + i), "李四", "13843838438", "顺丰速递", Integer.toString(123456 + i),"18888888888");
            try {
                System.out.println(baseExpressDao.insert(express));
            } catch (DuplicateCodeException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void update() {
        Express newExpress = new Express();
        newExpress.setNumber("5555");
        newExpress.setCompany("哈哈快递");
        newExpress.setUsername("王二");
        newExpress.setStatus(1);
        System.out.println(baseExpressDao.update(1, newExpress));
    }

    @Test
    public void updateStatus() {
        System.out.println(baseExpressDao.updateStatus("123456"));
    }

    @Test
    public void delete() {
        System.out.println(baseExpressDao.delete(5));
    }
}