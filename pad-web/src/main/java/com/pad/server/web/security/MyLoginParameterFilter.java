package com.pad.server.web.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class MyLoginParameterFilter implements Filter {

    public static final String PARAMETER_NAME_ACCOUNT_TYPE = "accountType";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestContextHolder.getRequestAttributes().setAttribute(PARAMETER_NAME_ACCOUNT_TYPE, request.getParameter(PARAMETER_NAME_ACCOUNT_TYPE), RequestAttributes.SCOPE_REQUEST);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}
