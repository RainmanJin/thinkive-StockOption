package com.thinkive.common.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @描述: 防跨站攻击过滤器
 * @版权: Copyright (c) 2015 
 * @公司: Thinkive 
 * @作者: 王嵊俊
 * @创建日期: 2015年12月15日 下午6:55:35
 */
public class XSSFilter implements Filter
{
	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{

		if (request instanceof HttpServletRequest)
		{
			XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
			chain.doFilter(xssRequest, response);

		} else
		{
			chain.doFilter(request, response);
		}

	}

	@Override
	public void destroy()
	{
	}

}
