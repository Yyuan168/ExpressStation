package com.kkb.bean;

import com.kkb.utils.DateFormatUtil;

public class BootStrapTableExpress {
    private int id;
    private String number;
    private String username;
    private String userphone;
    private String company;
    private String code;
    private String intime;
    private String outtime;
    private String status;
    private String sysPhone;

    public BootStrapTableExpress() {
    }

    public BootStrapTableExpress(Express express) {
        this.id = express.getId();
        this.number = express.getNumber();
        this.username = express.getUsername();
        this.userphone = express.getUserphone();
        this.company = express.getCompany();
        this.code = express.getCode() == null? "已取件" : express.getCode();
        this.intime = DateFormatUtil.getFormat(express.getIntime());
        this.outtime = express.getOuttime() == null? "未出库" : DateFormatUtil.getFormat(express.getOuttime());
        this.status = express.getStatus() == 1? "已取件" : "待取件";
        this.sysPhone = express.getSysPhone();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSysPhone() {
        return sysPhone;
    }

    public void setSysPhone(String sysPhone) {
        this.sysPhone = sysPhone;
    }
}
