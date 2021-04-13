package com.kkb.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "CharSetFilter", urlPatterns = "*.do")
public class CharSetFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        // 对所有前端和后台之间的交流在编码上进行过滤
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/json;charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
