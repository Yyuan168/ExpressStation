package com.kkb.mvc;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@WebServlet(name = "DispatcherServlet")
public class DispatcherServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        // 初始化加载配置文件
        String path = config.getInitParameter("contentConfigLocation");
        // 通过配置文件路径，获取输入流
        InputStream is = DispatcherServlet.class.getClassLoader().getResourceAsStream(path);
        // 将is传入HandlerMapping.load()方法，对配置文件进行加载
        HandlerMapping.load(is);

    }

    // 用service处理请求
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 获取请求的uri
        String uri = req.getRequestURI();
        // 2. 通过调用HandlerMapping的get方法获得相应处理请求的MVCMapping映射
        HandlerMapping.MVCMapping mvcMapping = HandlerMapping.get(uri);
        // 2.2 若没有找到相应的mvc映射，返回404页面
        if (mvcMapping == null) {
            resp.sendError(404, "自定义MVC: 请求地址 " + uri + " 没有对应的处理方法");
            return;
        }
        // 3. 从mvcMapping获取属性
        Object obj = mvcMapping.getObj();
        Method method = mvcMapping.getMethod();
        // 4. 调用对应method，获取该方法返回的内容（文字或响应的页面地址）
        Object result = null;
        try {
            result = method.invoke(obj, req, resp);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        // 5. 根据mvcMapping的类型，分别作出相应处理
        switch (mvcMapping.getType()) {
            case TEXT:
                // 返回文本内容
                resp.getWriter().print((String) result);
                break;
            case VIEW:
                // 返回页面地址
                resp.sendRedirect((String) result);
                break;
        }
    }
}
