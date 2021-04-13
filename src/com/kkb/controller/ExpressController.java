package com.kkb.controller;

import com.kkb.bean.BootStrapTableExpress;
import com.kkb.bean.Express;
import com.kkb.bean.Message;
import com.kkb.bean.ResultData;
import com.kkb.mvc.ResponseBody;
import com.kkb.service.ExpressService;
import com.kkb.utils.JSONUtil;
import com.kkb.utils.UserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpressController {

    @ResponseBody("/express/console.do")
    public String console(HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Integer>> data = ExpressService.console();
        Message message = new Message();
        // 数据库查询失败
        if(data.size() == 0) {
            message.setStatus(-1);
        } else {
            message.setStatus(0);
        }
        message.setData(data);
        String json = JSONUtil.toJSON(message);
        return json;
    }

    @ResponseBody("/express/list.do")
    public String list(HttpServletRequest request, HttpServletResponse response) {
        // 1. 获取查询数据的起始索引值
        int offset = Integer.parseInt(request.getParameter("offset"));
        // 2. 获取当前页查询的数据量
        int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        // 3. 进行查询
        List<Express> list = ExpressService.findAll(true, offset, pageNumber);
        // 4. 将集合封装为bootstrap-table识别的格式
        ArrayList<BootStrapTableExpress> bootStrapList = new ArrayList<>();
        for (Express express : list) {
            bootStrapList.add(new BootStrapTableExpress(express));
        }
        ResultData<BootStrapTableExpress> data = new ResultData<>();
        data.setRows(bootStrapList);
        // 5. 设置总快递数量
        List<Map<String, Integer>> console = ExpressService.console();
        data.setTotal(console.get(0).get("data1_size"));
        return JSONUtil.toJSON(data);
    }

    @ResponseBody("/express/insert.do")
    public String insert(HttpServletRequest request, HttpServletResponse response) {
        String number = request.getParameter("number");
        String company = request.getParameter("company");
        String username = request.getParameter("username");
        String userphone = request.getParameter("userphone");
        // 服务层插入
        boolean flag = ExpressService.insert(new Express(number, username, userphone, company, UserUtil.getUserphone(request.getSession())));
        Message message = new Message();
        if (flag) {
            // 录入成功
            message.setStatus(0);
            message.setResult("录入成功");
        } else {
            // 录入失败
            message.setStatus(-1);
            message.setResult("录入失败");
        }
        return JSONUtil.toJSON(message);
    }

    @ResponseBody("/express/find.do")
    public String find(HttpServletRequest request, HttpServletResponse response) {
        String number = request.getParameter("number");
        Express express = ExpressService.findByNumber(number);
        Message msg = new Message();
        if (express == null) {
            // 没找到
            msg.setStatus(-1);
            msg.setResult("单号不存在");
        } else {
            // 找到了
            msg.setStatus(0);
            msg.setResult("查询成功");
            msg.setData(new BootStrapTableExpress(express));
        }
        return JSONUtil.toJSON(msg);
    }

    @ResponseBody("/express/update.do")
    public String update(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        String number = request.getParameter("number");
        String company = request.getParameter("company");
        String username = request.getParameter("username");
        String userphone = request.getParameter("userphone");
        Express newExpress = new Express(number, username, userphone, company, UserUtil.getUserphone(request.getSession()));
        boolean flag = ExpressService.update(id, newExpress);
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

    @ResponseBody("/express/delete.do")
    public String delete(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        Message msg = new Message();
        if (ExpressService.delete(id)) {
            // 删除成功
            msg.setStatus(0);
            msg.setResult("删除成功");
        } else {
            // 删除失败
            msg.setStatus(-1);
            msg.setResult("删除失败");
        }
        return JSONUtil.toJSON(msg);
    }
}
