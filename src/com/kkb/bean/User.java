package com.kkb.bean;

import java.sql.Timestamp;

public class User implements WXClient{
    private int id;
    private String userName;
    private String userPhone;
    private String idNumber;
    private String password;
    private Timestamp registerTime;
    private Timestamp loginTime;

    public User(String userPhone) {
        this.userPhone = userPhone;
    }

    public User(String userName, String userPhone, String password) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.password = password;
    }

    public User(String userName, String userPhone, String idNumber, String password) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.idNumber = idNumber;
        this.password = password;
    }

    public User(int id, String userName, String userPhone, String idNumber, String password, Timestamp registerTime, Timestamp loginTime) {
        this.id = id;
        this.userName = userName;
        this.userPhone = userPhone;
        this.idNumber = idNumber;
        this.password = password;
        this.registerTime = registerTime;
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", password='" + password + '\'' +
                ", registerTime=" + registerTime +
                ", loginTime=" + loginTime +
                '}';
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
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
        return "User";
    }

    @Override
    public String getPhonenumber() {
        return getUserPhone();
    }
}
