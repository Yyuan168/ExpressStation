package com.kkb.utils;

import com.kkb.bean.WXClient;

import javax.servlet.http.HttpSession;

public class UserUtil {
    public static void AdminLogin(HttpSession session, String adminUsername) {
        session.setAttribute("adminUsername", adminUsername);
    }

    public static boolean isLogin(HttpSession session) {
        return session.getAttribute("adminUsername") != null || session.getAttribute("wxClient") != null;
    }

    public static String getUserphone(HttpSession session) {
        WXClient client = (WXClient) session.getAttribute("wxClient");
        Object adminUsername = session.getAttribute("adminUsername");
        return client == null ? (adminUsername == null ? null : "18888888888") : client.getPhonenumber();
    }

    public static String getClientLoginCode(HttpSession session, String userPhone) {
        return (String) session.getAttribute(userPhone);
    }

    public static void setClientLoginCode(HttpSession session, String userPhone, String code) {
        session.setAttribute(userPhone, code);
    }

    public static void setWxClient(HttpSession session, WXClient client) {
        session.setAttribute("wxClient", client);
    }

    public static WXClient getWxClient(HttpSession session) {
        return (WXClient) session.getAttribute("wxClient");
    }
}
