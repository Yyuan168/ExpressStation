package com.kkb.controller;


import com.kkb.bean.BootStrapTableUser;
import com.kkb.bean.Message;
import com.kkb.bean.ResultData;
import com.kkb.bean.User;
import com.kkb.mvc.ResponseBody;
import com.kkb.service.UserService;
import com.kkb.utils.JSONUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserController {
    @ResponseBody("/user/console.do")
    public String console(HttpServletRequest request, HttpServletResponse response){
        List<Map<String,Integer>> console = UserService.console();
        Message message = new Message();
        if (console!=null){
            //如果成功获得值，则设置数据状态为代表成功获取的0,否则设置为-1
            message.setStatus(0);
        }else {
            message.setStatus(-1);
        }
        //防止值为0时会读取为null的问题发生
        if (console.get(0).get("data1_perDay")==null){
            console.get(0).put("data1_perDay",0);
        }
        //无论是否成功，都存一个值进去
        message.setData(console);
        //返回Ajax数据
        return JSONUtil.toJSON(message);
    }

    @ResponseBody("/user/list.do")
    public String list(HttpServletRequest request, HttpServletResponse response){
        boolean limit = true;
        /*//暂时前端没有传入limit
        boolean limit = Boolean.parseBoolean(request.getParameter("limit"));*/
        int offset = Integer.parseInt(request.getParameter("offset"));
        int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
        List<User> list1 = UserService.findAll(limit,offset,pageNumber);
        //把集合中的数据转换成bootstrapTable可以识别，且便于前端阅读的格式
        ArrayList<BootStrapTableUser>  list2 = new ArrayList<>();
        for (User u:list1){
            list2.add(new BootStrapTableUser(u));
        }
        //定义泛型的具体
        ResultData<BootStrapTableUser> data = new ResultData<>();
        //存入数据
        data.setRows(list2);
        //获取用户数量总数
        data.setTotal(UserService.console().get(0).get("data1_size"));
        //返回Ajax数据
        return JSONUtil.toJSON(data);
    }

    @ResponseBody("/user/add.do")
    public String add(HttpServletRequest request, HttpServletResponse response){
        String userName = request.getParameter("userName");
        String userPhone = request.getParameter("userPhone");
        String idNumber = request.getParameter("idNumber");
        String password = request.getParameter("password");
        User user = new User(userName,userPhone,idNumber,password);
        Message message = new Message();
        String result = UserService.insert(user);
        setResult(result,message);
        //返回Ajax数据
        return JSONUtil.toJSON(message);
    }

    @ResponseBody("/user/findByUserPhone.do")
    public String findByUserPhone(HttpServletRequest request, HttpServletResponse response){
        String userPhone = request.getParameter("userPhone");
        Message message = new Message();
        User user = UserService.findByUserPhone(userPhone);
        if (user !=null){
            message.setStatus(0);
            message.setResult("查询成功");
        }else {
            message.setStatus(-1);
            message.setResult("查询失败");
        }
        message.setData(user);
        //返回Ajax数据
        return JSONUtil.toJSON(message);
    }

    @ResponseBody("/user/delete.do")
    public String delete(HttpServletRequest request, HttpServletResponse response){
        int id = Integer.parseInt(request.getParameter("id"));
        Message message = new Message();
        boolean result = UserService.delete(id);
        if (result){
            message.setStatus(0);
            message.setResult("删除成功");
        }else {
            message.setStatus(-1);
            message.setResult("删除失败");
        }
        //返回Ajax数据
        return JSONUtil.toJSON(message);
    }

    @ResponseBody("/user/update.do")
    public String update(HttpServletRequest request, HttpServletResponse response){
        int id = Integer.parseInt(request.getParameter("id"));
        String userName = request.getParameter("userName");
        String userPhone = request.getParameter("userPhone");
        String idNumber = request.getParameter("idNumber");
        String password = request.getParameter("password");
        User user = new User(userName,userPhone,idNumber,password);
        Message message = new Message();
        String result = UserService.update(id,user);
        setResult(result,message);
        //返回Ajax数据
        return JSONUtil.toJSON(message);
    }

    @ResponseBody("/user/findById.do")
    public String findById(HttpServletRequest request, HttpServletResponse response){
        int id = Integer.parseInt(request.getParameter("id"));
        Message message = new Message();
        User user = UserService.findById(id);
        if (user !=null){
            message.setStatus(0);
            message.setResult("查询成功");
        }else {
            message.setStatus(-1);
            message.setResult("查询失败");
        }
        message.setData(user);
        //返回Ajax数据
        return JSONUtil.toJSON(message);
    }

    @ResponseBody("/user/findByUsername.do")
    public String findByUsername(HttpServletRequest request, HttpServletResponse response){
        String username = request.getParameter("username");
        Message message = new Message();
        User user = UserService.findByUserName(username);
        if (user !=null){
            message.setStatus(0);
            message.setResult("查询成功");
        }else {
            message.setStatus(-1);
            message.setResult("查询失败");
        }
        message.setData(user);
        //返回Ajax数据
        return JSONUtil.toJSON(message);
    }

    private void setResult(String result,Message message){
        switch (result){
            case "true":
                message.setStatus(0);
                message.setResult("注册成功");
                break;
            case "false":
                message.setStatus(-1);
                message.setResult("注册失败");
                break;
            case "用户名已存在":
                message.setStatus(6);
                message.setResult("该用户名已经被注册");
                break;
            case "该手机号已注册":
                message.setStatus(7);
                message.setResult("该手机号已经注册了账号");
                break;
            case "该身份证号已注册":
                message.setStatus(8);
                message.setResult("该身份证已经注册了账号");
                break;
        }
    }
}
