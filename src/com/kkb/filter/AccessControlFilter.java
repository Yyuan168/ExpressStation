package com.kkb.filter;

import com.kkb.utils.UserUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter({"/admin/index.html,","/admin/views/*","/express/*","/courier/*","/user/*"})
public class AccessControlFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (UserUtil.isLogin(request.getSession())) {
            chain.doFilter(request, response);
        } else {
            response.sendError(404, "发生异常，请登录后进行操作");
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
