package com.kkb.wx.controller;

import com.kkb.bean.BootStrapTableExpress;
import com.kkb.bean.Express;
import com.kkb.bean.Message;
import com.kkb.bean.WXClient;
import com.kkb.mvc.ResponseBody;
import com.kkb.service.ExpressService;
import com.kkb.utils.DateFormatUtil;
import com.kkb.utils.JSONUtil;
import com.kkb.utils.UserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class ExpressController {
    @ResponseBody("/wx/findExpressList.do")
    public String findByClientPhone(HttpServletRequest request, HttpServletResponse response) {
        WXClient client = UserUtil.getWxClient(request.getSession());
        String clientPhone = client.getPhonenumber();
        List<Express> list = ExpressService.findByUserPhone(clientPhone);
        // 将Express转换成bootstrapExpress
        ArrayList<BootStrapTableExpress> bootStrapList = new ArrayList<>();
        for (Express express : list) {
            bootStrapList.add(new BootStrapTableExpress(express));
        }
        Message msg = new Message();
        if (list.isEmpty()) {
            // 未找到对应快递列表
            msg.setStatus(-1);
        } else {
            // 将快递列表按照已取件和未取件分组，并根据时间排序
            Stream<BootStrapTableExpress> status0Express = bootStrapList.stream().filter(bootStrapTableExpress -> "待取件".equals(bootStrapTableExpress.getStatus())).sorted((o1, o2) -> {
                long difference = DateFormatUtil.toTime(o1.getIntime()) - DateFormatUtil.toTime(o2.getIntime());
                if (difference > 0) return 1;
                else if (difference < 0) return -1;
                return 0;
            });
            Stream<BootStrapTableExpress> status1Express = bootStrapList.stream().filter(bootStrapTableExpress -> "已取件".equals(bootStrapTableExpress.getStatus())).sorted((o1, o2) -> {
                long difference = DateFormatUtil.toTime(o1.getIntime()) - DateFormatUtil.toTime(o2.getIntime());
                if (difference > 0) return 1;
                else if (difference < 0) return -1;
                return 0;
            });
            Object[] s0 = status0Express.toArray();
            Object[] s1 = status1Express.toArray();
            HashMap<String, Object> data = new HashMap<>();
            data.put("status0", s0);
            data.put("status1", s1);
            msg.setData(data);
        }

        return JSONUtil.toJSON(msg);
    }

    @ResponseBody("/wx/userExpressList.do")
    public String getExpressList(HttpServletRequest request, HttpServletResponse response) {
        String userphone = request.getParameter("userphone");
        List<Express> list = ExpressService.findByUserPhoneAndStatus(userphone, 0);
        Message msg = new Message();
        if (list.size() == 0) {
            msg.setStatus(-1);
            msg.setResult("未查询到相关快递");
        } else {
            msg.setStatus(0);
            ArrayList<BootStrapTableExpress> bootStrapList = new ArrayList<>();
            for (Express express : list) {
                bootStrapList.add(new BootStrapTableExpress(express));
            }
            msg.setResult("查询成功");
            msg.setData(bootStrapList);
        }
        return JSONUtil.toJSON(msg);
    }

    @ResponseBody("/wx/userExpress.do")
    public String getExpress(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");
        Express express = ExpressService.findByCode(code);
        Message msg = new Message();
        if (express == null) {
            msg.setStatus(-1);
            msg.setResult("未查询到相关快递");
        } else {
            msg.setStatus(0);
            msg.setResult("查询成功");
            msg.setData(new BootStrapTableExpress(express));
        }
        return JSONUtil.toJSON(msg);
    }

    @ResponseBody("/wx/updateStatus.do")
    public String updateStatus(HttpServletRequest request, HttpServletResponse response) {
        Message msg = new Message();
        if (ExpressService.updateStatus(request.getParameter("code"))) {
            msg.setStatus(0);
            msg.setResult("取件成功");
        } else {
            msg.setStatus(-1);
            msg.setResult("快件不存在");
        }
        return JSONUtil.toJSON(msg);
    }
}
