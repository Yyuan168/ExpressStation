package com.kkb.bean;

import java.sql.Timestamp;

public class Courier implements WXClient{
    private int id;
    private String cname;
    private String sysPhone;
    private String idNumber;
    private String password;
    private int deliveryNum;
    private Timestamp registerTime;
    private Timestamp loginTime;

    @Override
    public String toString() {
        return "Courier{" +
                "id=" + id +
                ", cname='" + cname + '\'' +
                ", sysPhone='" + sysPhone + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", password='" + password + '\'' +
                ", deliveryNum=" + deliveryNum +
                ", registerTime=" + registerTime +
                ", loginTime=" + loginTime +
                '}';
    }



    public Courier(String cname, String sysPhone, String idNumber, String password) {
        this.cname = cname;
        this.sysPhone = sysPhone;
        this.idNumber = idNumber;
        this.password = password;
    }

    public Courier(int id, String cname, String sysPhone, String idNumber, String password, int deliveryNum, Timestamp registerTime, Timestamp loginTime) {
        this.id = id;
        this.cname = cname;
        this.sysPhone = sysPhone;
        this.idNumber = idNumber;
        this.password = password;
        this.deliveryNum = deliveryNum;
        this.registerTime = registerTime;
        this.loginTime = loginTime;
    }

    public Courier() {
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

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String getType() {
        return "Courier";
    }

    @Override
    public String getPhonenumber() {
        return getSysPhone();
    }
}
