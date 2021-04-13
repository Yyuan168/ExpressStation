package com.kkb.wx.controller;

import com.kkb.bean.Message;
import com.kkb.mvc.ResponseBody;
import com.kkb.mvc.ResponseView;
import com.kkb.service.ExpressService;
import com.kkb.utils.JSONUtil;
import com.kkb.utils.UserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class QRCodeController {
    @ResponseView("/wx/createQRCode.do")
    public String createQRCode(HttpServletRequest request, HttpServletResponse response) {
        // express | user
        String type = request.getParameter("type");
        HttpSession session = request.getSession();
        if ("express".equals(type)) {
            // 快件的二维码 : 被扫后，显示单个快件的信息
            session.setAttribute("qrCode", "express_" + request.getParameter("code"));
        } else {
            // 用户的二维码 : 被扫后，快递员（柜子）端展示用户所有快递
            session.setAttribute("qrCode", "userphone_" + UserUtil.getUserphone(session));
        }
        return "/personQRcode.html";
    }

    @ResponseBody("/wx/qrcode.do")
    public String getQRCode(HttpServletRequest request, HttpServletResponse response) {
        String qrCode = (String) request.getSession().getAttribute("qrCode");
        return JSONUtil.toJSON(qrCode == null ? new Message(-1, "取件码获取出错，请用户重新操作") : new Message(0, qrCode));
    }
}
