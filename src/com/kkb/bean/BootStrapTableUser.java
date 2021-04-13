package com.kkb.bean;


import com.kkb.utils.DateFormatUtil;

public class BootStrapTableUser {
    private int id;
    private String userName;
    private String userPhone;
    private String password;
    private String idNumber;
    private String registerTime;
    private String loginTime;

    //接受父类属性的值，直接构造
    public BootStrapTableUser(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.userPhone = user.getUserPhone();
        this.password = user.getPassword();
        this.idNumber = user.getIdNumber();
        this.registerTime = DateFormatUtil.getFormat(user.getRegisterTime());
        this.loginTime = user.getRegisterTime()==null?"无登陆信息":DateFormatUtil.getFormat(user.getRegisterTime());
    }

    public BootStrapTableUser() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
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
