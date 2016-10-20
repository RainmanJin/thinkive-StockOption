package com.thinkive.common.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @����: ����վ����
 * @��Ȩ: Copyright (c) 2015 
 * @��˾: Thinkive 
 * @����: ���ӿ�
 * @��������: 2015��12��15�� ����6:56:15
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper
{
	HttpServletRequest orgRequest = null;

	public XssHttpServletRequestWrapper(HttpServletRequest request)
	{
		super(request);
		orgRequest = request;
	}

	/** 
	 * ����getParameter���������������Ͳ���ֵ����xss���ˡ�<br/> 
	 * �����Ҫ���ԭʼ��ֵ����ͨ��super.getParameterValues(name)����ȡ<br/> 
	 * getParameterNames,getParameterValues��getParameterMapҲ������Ҫ���� 
	 */
	@Override
	public String getParameter(String name)
	{
		String value = super.getParameter(xssEncode(name));
		if (value != null)
		{
			value = xssEncode(value);
		}
		return value;
	}

	/** 
	 * ����getHeader���������������Ͳ���ֵ����xss���ˡ�<br/> 
	 * �����Ҫ���ԭʼ��ֵ����ͨ��super.getHeaders(name)����ȡ<br/> 
	 * getHeaderNames Ҳ������Ҫ���� 
	 */
	@Override
	public String getHeader(String name)
	{

		String value = super.getHeader(xssEncode(name));
		if (value != null)
		{
			value = xssEncode(value);
		}
		return value;
	}

	/** 
	 * ����������xss©���İ���ַ�ֱ���滻��ȫ���ַ� 
	 *  
	 * @param s 
	 * @return 
	 */
	private static String xssEncode(String s)
	{
		if (s == null || s.isEmpty())
		{
			return s;
		}
		StringBuilder sb = new StringBuilder(s.length() + 16);
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			switch (c)
			{
			case '>':
				sb.append(">");// ת����ں�
				break;
			case '<':
				sb.append("<");// ת��С�ں�
				break;
			case '\'':
				sb.append("'");// ת�嵥����
				break;
			case '\"':
				sb.append("\"");// ת��˫����
				break;
			case '&':
				sb.append("&");// ת��&
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

	/** 
	 * ��ȡ��ԭʼ��request 
	 *  
	 * @return 
	 */
	public HttpServletRequest getOrgRequest()
	{
		return orgRequest;
	}

	/** 
	 * ��ȡ��ԭʼ��request�ľ�̬���� 
	 *  
	 * @return 
	 */
	public static HttpServletRequest getOrgRequest(HttpServletRequest req)
	{
		if (req instanceof XssHttpServletRequestWrapper)
		{
			return ((XssHttpServletRequestWrapper) req).getOrgRequest();
		}

		return req;
	}
}
