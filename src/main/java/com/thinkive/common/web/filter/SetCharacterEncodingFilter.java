/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.thinkive.common.web.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;

/**
 * 描述:
 * 版权:	 Copyright (c) 2005
 * 公司:	 思迪科技
 * 作者:	 易庆锋
 * 版本:	 1.0
 * 创建日期: 2006-10-11
 * 创建时间: 10:47:40
 */
public class SetCharacterEncodingFilter implements Filter
{
    private static Logger logger = Logger.getLogger(SetCharacterEncodingFilter.class);

    protected String encoding = null;

    protected FilterConfig filterConfig = null;

    protected boolean ignore = true;

    public void destroy()
    {
        if (logger.isInfoEnabled())
            logger.info("SetCharacterEncodingFilter destroy ----------");
        this.encoding = null;
        this.filterConfig = null;
    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    {
        try
        {
            // Conditionally select and set the character encoding to be used
            if (ignore || (request.getCharacterEncoding() == null))
            {
                String encoding = selectEncoding(request);
                if (encoding != null)
                {
                    request.setCharacterEncoding(encoding);
                    response.setContentType("text/html; charset=" + encoding);
                }
            }

            chain.doFilter(request, response);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    public void init(FilterConfig filterConfig) throws ServletException
    {
        if (logger.isInfoEnabled())
            logger.info("SetCharacterEncodingFilter init ----------");

        this.filterConfig = filterConfig;
        this.encoding = filterConfig.getInitParameter("encoding");
        String value = filterConfig.getInitParameter("ignore");
        if (value == null)
            this.ignore = true;
        else if (value.equalsIgnoreCase("true"))
            this.ignore = true;
        else if (value.equalsIgnoreCase("yes"))
            this.ignore = true;
        else
            this.ignore = false;
    }

    protected String selectEncoding(ServletRequest request)
    {
        return (this.encoding);
    }

}
