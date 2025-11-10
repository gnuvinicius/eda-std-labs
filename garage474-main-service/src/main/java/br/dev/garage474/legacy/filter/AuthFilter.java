package br.dev.garage474.legacy.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


//@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());
        boolean loggedIn = req.getSession().getAttribute("user") != null;
        boolean isLoginPage = path.equals("/auth") || path.startsWith("/resources");

//        if (!loggedIn && !isLoginPage) {
//            resp.sendRedirect(req.getContextPath() + "/auth");
//        } else {
//            chain.doFilter(request, response);
//        }
    }
}