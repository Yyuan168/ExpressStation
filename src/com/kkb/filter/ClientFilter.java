package com.kkb.filter;

import com.kkb.bean.WXClient;
import com.kkb.utils.UserUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter({"/index.html",
        "/wxUserhome.html",
        "/wxIdCardUserInfoModify.html",
        "/pickExpress.html",
        "/personQRcode.html",
        "/lazyboard.html",
        "/userCheckStart.html",
        "/personQRcode.html",
        "/expressAssist.html",
        "/expressList.html"})
public class ClientFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        WXClient client = UserUtil.getWxClient(((HttpServletRequest) req).getSession());
        if (client != null)
            chain.doFilter(req, resp);
        else
            ((HttpServletResponse) resp).sendRedirect("/login.html");
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
