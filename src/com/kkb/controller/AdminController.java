package com.kkb.controller;

import com.kkb.bean.Message;
import com.kkb.mvc.ResponseBody;
import com.kkb.service.AdminService;
import com.kkb.utils.JSONUtil;
import com.kkb.utils.UserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class AdminController {

    @ResponseBody("/admin/login.do")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        // 1. 接收参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // 2. 调用Service传入参数，并获取结果
        boolean result = AdminService.login(username, password);
        // 3. 根据结果，准备不同的返回Message数据
        Message msg = null;
        if (result) {
            msg = new Message(0, "登录成功");
            // 更新登录时间及ip地址
            AdminService.updateLoginTimeAndIp(request.getRemoteAddr(), new Date(), username);
            UserUtil.AdminLogin(request.getSession(), username);
        } else {
            msg = new Message(-1, "登录失败");
        }

        // 4. 转换为JSON格式，返回给ajax
        return JSONUtil.toJSON(msg);
    }

    @ResponseBody("/admin/logout.do")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 1. 销毁session
        request.getSession().invalidate();
        // 2. 给客户端恢复消息
        Message msg = new Message(0);
        return JSONUtil.toJSON(msg);
    }
}
