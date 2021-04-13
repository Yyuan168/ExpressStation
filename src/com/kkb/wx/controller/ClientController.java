package com.kkb.wx.controller;

import com.kkb.bean.Courier;
import com.kkb.bean.Message;
import com.kkb.bean.User;
import com.kkb.bean.WXClient;
import com.kkb.mvc.ResponseBody;
import com.kkb.service.CourierService;
import com.kkb.service.UserService;
import com.kkb.utils.JSONUtil;
import com.kkb.utils.RandomCodeUtil;
import com.kkb.utils.UserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ClientController {

    @ResponseBody("/wx/loginSms.do")
    public String SendSMS(HttpServletRequest request, HttpServletResponse response) {
        String clientPhone = request.getParameter("clientPhone");
        String code = RandomCodeUtil.getCode();
        // 由于发送不了短信不能实现以下方法
//        boolean flag = SMSUtil.loginSMS(clientPhone, code);
        // 改为后台打印提示手机和验证码，手机按照后台控制台显示的验证码进行登录
        System.out.println("登录手机: " + clientPhone + " , 验证码: " + code);
        Message msg = new Message();
//        if (flag) {
//            // 短信发送成功
//            msg.setStatus(0);
//            msg.setResult("验证码已发送，请查收!");
//        } else {
//            //短信发送失败
//            msg.setStatus(1);
//            msg.setResult("验证码发送失败，请检查手机号或稍后再试");
//        }
        msg.setStatus(0);
        msg.setResult("验证码已发送，请查收!");
        UserUtil.setClientLoginCode(request.getSession(), clientPhone, code);
        return JSONUtil.toJSON(msg);
    }

    @ResponseBody("/wx/login.do")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        String clientPhone = request.getParameter("clientPhone");
        String code = request.getParameter("code");
        String sysCode = UserUtil.getClientLoginCode(request.getSession(), clientPhone);
        Message msg = new Message();
        if (sysCode == null) {
            // 这个手机号未获取短信
            msg.setStatus(-1);
            msg.setResult("该手机未获取短信");
        } else if (sysCode.equals(code)) {
            // 手机号码和短信一致，登录成功
            // 接收到手机和验证码后，先到数据库查找是否存在该用户，有就登录，没有就注册
            // 注册的必须是用户，快递员不可以注册
            // 若快递员与用户的手机号码相同，快递员的权限更高，登录快递员界面
            Courier courier = CourierService.findBySysPhone(clientPhone);
            User user = UserService.findByUserPhone(clientPhone);

            if (courier == null && user == null) {
                // 注册普通用户
                User newUser = new User(clientPhone);
                // 登录普通用户
                UserUtil.setWxClient(request.getSession(), newUser);
                UserService.login(clientPhone);
                msg.setStatus(0);
                msg.setResult("注册普通用户");
            } else if (courier != null) {
                // 登录快递员账户
                UserUtil.setWxClient(request.getSession(), courier);
                CourierService.login(clientPhone);
                msg.setStatus(1);
                msg.setResult("登录快递员用户");
            } else {
                // 登录普通用户
                UserUtil.setWxClient(request.getSession(), user);
                UserService.login(clientPhone);
                msg.setStatus(2);
                msg.setResult("登录普通用户");
            }
        } else {
            // 验证码不一致，登录失败
            msg.setStatus(-2);
            msg.setResult("验证码不一致，请检查");
        }
        return JSONUtil.toJSON(msg);
    }

    @ResponseBody("/wx/update.do")
    public String update(HttpServletRequest request, HttpServletResponse response) {
        String newUsername = request.getParameter("newUsername");
        String newPhonenumber = request.getParameter("newPhonenumber");
        String code = request.getParameter("code");
        HttpSession session = request.getSession();
        String sysCode = UserUtil.getClientLoginCode(session, newPhonenumber);
        Message msg = new Message();
        if (sysCode == null) {
            // 这个手机号未获取短信
            msg.setStatus(-1);
            msg.setResult("该手机未获取短信");
        } else if (sysCode.equals(code)) {
            // 手机号码和短信一致，验证成功
            WXClient wxClient = UserUtil.getWxClient(session);
            if ("Courier".equals(wxClient.getType())) {
                // 修改为快递员账户
                Courier oldCourier = CourierService.findBySysPhone(wxClient.getPhonenumber());
                oldCourier.setCname(newUsername);
                oldCourier.setSysPhone(newPhonenumber);
                if (CourierService.update(oldCourier.getId(), oldCourier)) {
                    // 将登陆的对象更新
                    UserUtil.setWxClient(session, oldCourier);
                    msg.setStatus(0);
                    msg.setResult("修改成功");
                } else {
                    msg.setStatus(-3);
                    msg.setResult("修改失败");
                }
            } else {
                // 修改为普通用户
                User oldUser = UserService.findByUserPhone(wxClient.getPhonenumber());
                oldUser.setUserName(newUsername);
                oldUser.setUserPhone(newPhonenumber);
                if ("true".equals(UserService.update(oldUser.getId(), oldUser))) {
                    // 将登陆的对象更新
                    UserUtil.setWxClient(session, oldUser);
                    msg.setStatus(0);
                    msg.setResult("修改成功");
                } else {
                    msg.setStatus(-3);
                    msg.setResult("修改失败");
                }
            }
        } else {
            // 验证码不一致，登录失败
            msg.setStatus(-2);
            msg.setResult("验证码不一致，请检查");
        }
        return JSONUtil.toJSON(msg);
    }

    @ResponseBody("/wx/add.do")
    public String add(HttpServletRequest request, HttpServletResponse response) {
        String newUsername = request.getParameter("newUsername");
        String newPhonenumber = request.getParameter("newPhonenumber");
        String newPassword = request.getParameter("newPassword");
        String code = request.getParameter("code");
        HttpSession session = request.getSession();
        String sysCode = UserUtil.getClientLoginCode(session, newPhonenumber);
        Message msg = new Message();
        if (sysCode == null) {
            // 这个手机号未获取短信
            msg.setStatus(-1);
            msg.setResult("该手机未获取短信");
        } else if (sysCode.equals(code)) {
            // 手机号码和短信一致，验证成功
            // 添加普通用户
            User newUser = new User(newUsername, newPhonenumber, newPassword);
            if ("true".equals(UserService.insert(newUser))) {
                // 将登陆的对象更新
                UserUtil.setWxClient(session, newUser);
                // 记录登录时间
                UserService.login(newPhonenumber);
                msg.setStatus(0);
                msg.setResult("添加成功");
            } else {
                msg.setStatus(-3);
                msg.setResult("添加失败");
            }

        } else {
            // 验证码不一致，登录失败
            msg.setStatus(-2);
            msg.setResult("验证码不一致，请检查");
        }
        return JSONUtil.toJSON(msg);
    }

    @ResponseBody("/wx/logout.do")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 1. 销毁session
        request.getSession().invalidate();
        // 2. 给客户端回复消息
        Message message = new Message(0);
        return JSONUtil.toJSON(message);
    }

    @ResponseBody("/wx/clientInfo.do")
    public String getClientInfo(HttpServletRequest request, HttpServletResponse response) {
        WXClient client = UserUtil.getWxClient(request.getSession());
        Message msg = new Message();
        if ("Courier".equals(client.getType())) {
            // 登录的是快递员
            msg.setStatus(0);
        } else {
            // 登录的是普通用户
            msg.setStatus(1);
        }
        msg.setResult(client.getPhonenumber());
        msg.setData(client);
        return JSONUtil.toJSON(msg);
    }
}