package com.kkb.bean;

import java.sql.Timestamp;

public class Express {
    private int id;
    private String number;
    private String username;
    private String userphone;
    private String company;
    private String code;
    private Timestamp intime;
    private Timestamp outtime;
    private int status;
    private String sysPhone;

    public Express() {
    }

    public Express(int id, String number, String username, String userphone, String company, String code, Timestamp intime, Timestamp outtime, int status, String sysPhone) {
        this.id = id;
        this.number = number;
        this.username = username;
        this.userphone = userphone;
        this.company = company;
        this.code = code;
        this.intime = intime;
        this.outtime = outtime;
        this.status = status;
        this.sysPhone = sysPhone;
    }

    public Express(String number, String username, String userphone, String company, String code, String sysPhone) {
        this.number = number;
        this.username = username;
        this.userphone = userphone;
        this.company = company;
        this.code = code;
        this.sysPhone = sysPhone;
    }

    public Express(String number, String username, String userphone, String company, String sysPhone) {
        this.number = number;
        this.username = username;
        this.userphone = userphone;
        this.company = company;
        this.sysPhone = sysPhone;
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

    public Timestamp getIntime() {
        return intime;
    }

    public void setIntime(Timestamp intime) {
        this.intime = intime;
    }

    public Timestamp getOuttime() {
        return outtime;
    }

    public void setOuttime(Timestamp outtime) {
        this.outtime = outtime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSysPhone() {
        return sysPhone;
    }

    public void setSysPhone(String sysPhone) {
        this.sysPhone = sysPhone;
    }

    @Override
    public String toString() {
        return "Express{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", username='" + username + '\'' +
                ", userphone='" + userphone + '\'' +
                ", company='" + company + '\'' +
                ", code='" + code + '\'' +
                ", intime=" + intime +
                ", outtime=" + outtime +
                ", status=" + status +
                ", sysPhone='" + sysPhone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Express express = (Express) o;

        if (id != express.id) return false;
        if (status != express.status) return false;
        if (number != null ? !number.equals(express.number) : express.number != null) return false;
        if (username != null ? !username.equals(express.username) : express.username != null) return false;
        if (userphone != null ? !userphone.equals(express.userphone) : express.userphone != null) return false;
        if (company != null ? !company.equals(express.company) : express.company != null) return false;
        if (code != null ? !code.equals(express.code) : express.code != null) return false;
        if (intime != null ? !intime.equals(express.intime) : express.intime != null) return false;
        if (outtime != null ? !outtime.equals(express.outtime) : express.outtime != null) return false;
        return sysPhone != null ? sysPhone.equals(express.sysPhone) : express.sysPhone == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (userphone != null ? userphone.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (intime != null ? intime.hashCode() : 0);
        result = 31 * result + (outtime != null ? outtime.hashCode() : 0);
        result = 31 * result + status;
        result = 31 * result + (sysPhone != null ? sysPhone.hashCode() : 0);
        return result;
    }
}
