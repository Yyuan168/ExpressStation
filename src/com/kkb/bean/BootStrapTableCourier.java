package com.kkb.bean;

import com.kkb.utils.DateFormatUtil;


public class BootStrapTableCourier {
    private int id;
    private String cname;
    private String sysPhone;
    private String idNumber;
    private String password;
    private int deliveryNum;
    private String registerTime;
    private String loginTime;

    public BootStrapTableCourier() {
    }

    public BootStrapTableCourier(Courier courier) {
        this.id = courier.getId();
        this.cname = courier.getCname();
        this.sysPhone = courier.getSysPhone();
        this.idNumber = courier.getIdNumber();
        this.password = courier.getPassword();
        this.deliveryNum = courier.getDeliveryNum();
        this.registerTime = DateFormatUtil.getFormat(courier.getRegisterTime());
        this.loginTime = courier.getLoginTime() == null? "暂未登录" : DateFormatUtil.getFormat(courier.getLoginTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getSysPhone() {
        return sysPhone;
    }

    public void setSysPhone(String sysPhone) {
        this.sysPhone = sysPhone;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDeliveryNum() {
        return deliveryNum;
    }

    public void setDeliveryNum(int deliveryNum) {
        this.deliveryNum = deliveryNum;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }
}
