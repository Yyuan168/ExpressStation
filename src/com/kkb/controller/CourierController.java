package com.kkb.controller;

import com.kkb.bean.*;
import com.kkb.mvc.ResponseBody;
import com.kkb.service.CourierService;
import com.kkb.utils.JSONUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourierController {
    @ResponseBody("/courier/console.do")
    public String console(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Integer> map = CourierService.console();
        Message msg = new Message();
        if (map.size() == 0) {
            msg.setStatus(-1);
        } else {
            msg.setStatus(0);
        }
        msg.setData(map);
        return JSONUtil.toJSON(msg);
    }

    @ResponseBody("/courier/list.do")
    public String list(HttpServletRequest request, HttpServletResponse response) {
        // 1. 获取查询数据的起始索引值
        int offset = Integer.parseInt(request.getParameter("offset"));
        // 2. 获取当前页查询的数据量
        int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        // 3. 进行查询
        List<Courier> list = CourierService.findAll(true, offset, pageNumber);
        // 4. 将集合封装为bootstrap-table识别的格式
        ArrayList<BootStrapTableCourier> bootStrapList = new ArrayList<>();
        for (Courier courier : list) {
            bootStrapList.add(new BootStrapTableCourier(courier));
        }
        ResultData<BootStrapTableCourier> data = new ResultData<>();
        data.setRows(bootStrapList);
        // 5. 设置总快递数量
        Map<String, Integer> console = CourierService.console();
        data.setTotal(console.get("data_size"));
        return JSONUtil.toJSON(data);
    }

    @ResponseBody("/courier/insert.do")
    public String insert(HttpServletRequest request, HttpServletResponse response) {
        String cname = request.getParameter("cname");
        String sysPhone = request.getParameter("sysPhone");
        String idNumber = request.getParameter("idNumber");
        String password = request.getParameter("password");
        // 服务层插入
        boolean flag = CourierService.insert(new Courier(cname, sysPhone, idNumber, password));
        Message message = new Message();
        if (flag) {
            // 录入成功
            message.setStatus(0);
            message.setResult("添加成功");
        } else {
            // 录入失败
            message.setStatus(-1);
            message.setResult("添加失败");
        }
        return JSONUtil.toJSON(message);
    }

    @ResponseBody("/courier/find.do")
    public String find(HttpServletRequest request, HttpServletResponse response) {
        String sysPhone = request.getParameter("sysPhone");
        Courier courier = CourierService.findBySysPhone(sysPhone);
        Message msg = new Message();
        if (courier == null) {
            // 没找到
            msg.setStatus(-1);
            msg.setResult("快递员不存在");
        } else {
            // 找到了
            msg.setStatus(0);
            msg.setResult("查询成功");
            msg.setData(courier);
        }
        return JSONUtil.toJSON(msg);
    }

    @ResponseBody("/courier/update.do")
    public String update(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        String cname = request.getParameter("cname");
        String sysPhone = request.getParameter("sysPhone");
        String idNumber = request.getParameter("idNumber");
        String password = request.getParameter("password");
        Courier newCourier = new Courier(cname, sysPhone, idNumber, password);
        boolean flag = CourierService.update(id, newCourier);
        Message msg = new Message();
        if (flag) {
            // 修改成功
            msg.setStatus(0);
            msg.setResult("修改成功");
        } else {
            msg.setStatus(-1);
            msg.setResult("修改失败");
        }
        return JSONUtil.toJSON(msg);
    }

    @ResponseBody("/courier/delete.do")
    public String delete(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean flag = CourierService.delete(id);
        Message msg = new Message();
        if (flag) {
            // 修改成功
            msg.setStatus(0);
            msg.setResult("删除成功");
        } else {
            msg.setStatus(-1);
            msg.setResult("删除失败");
        }
        return JSONUtil.toJSON(msg);
    }

    @ResponseBody("/courier/check.do")
    public String check(HttpServletRequest request, HttpServletResponse response) {
        String type = request.getParameter("type");
        String content = request.getParameter("content");
        Message msg = new Message();
        switch (type) {
            case "sysPhone":
                if (CourierService.findBySysPhone(content) == null) {
                    // 输入内容不存在
                    msg.setStatus(0);
                    msg.setResult("该手机号码不存在");
                } else {
                    msg.setStatus(-1);
                    msg.setResult("该手机号码已存在");
                }
                break;
            case "idNumber":
                if (CourierService.findByIDNumber(content) == null) {
                    // 输入内容不存在
                    msg.setStatus(0);
                    msg.setResult("该身份证号码不存在");
                } else {
                    msg.setStatus(-1);
                    msg.setResult("该身份证号码已存在");
                }
                break;
        }
        return JSONUtil.toJSON(msg);
    }
}
