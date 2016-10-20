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
 * @����: ����վ����������
 * @��Ȩ: Copyright (c) 2015 
 * @��˾: Thinkive 
 * @����: ���ӿ�
 * @��������: 2015��12��15�� ����6:55:35
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
