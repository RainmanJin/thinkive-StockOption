package com.thinkive.market.function.version_1;

import java.io.File;

import org.apache.log4j.Logger;

import com.thinkive.base.config.Configuration;
import com.thinkive.base.util.ConvertHelper;
import com.thinkive.market.common.impl.RequestImpl;
import com.thinkive.server.Function;

/**
 * 描述: 版权: Copyright (c) 2007 公司: 思迪科技 作者: 易庆锋 版本: 1.0 创建日期: 2009-1-13 创建时间:
 * 14:30:48
 */
public abstract class BaseFunction implements Function
{
	private static Logger	logger	= Logger.getLogger(BaseFunction.class);
									
	protected static String	SOH		= new String(new byte[] { 1 });
									
	protected static String	STX		= new String(new byte[] { 2 });
									
	/**
	 * 获得请求参数
	 * 
	 * @param name
	 * @return
	 */
	public String getStrParameter(RequestImpl request, String name)
	{
		String value = (String) request.getParameter(name);
		return (value == null) ? "" : value;
	}
	
	/**
	 * 获得请求参数
	 * 
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public String getStrParameter(RequestImpl request, String name, String defaultValue)
	{
		String value = getStrParameter(request, name);
		return (value.length() == 0) ? defaultValue : value;
	}
	
	/**
	 * 获得整型参数
	 * 
	 * @param name
	 * @return
	 */
	public int getIntParameter(RequestImpl request, String name)
	{
		String value = getStrParameter(request, name);
		try
		{
			return Integer.parseInt(value);
		}
		catch (Exception ex)
		{
			return 0;
		}
	}
	
	/**
	 * 获得整型参数
	 * 
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public int getIntParameter(RequestImpl request, String name, int defaultValue)
	{
		String value = (String) request.getParameter(name);
		if ( value == null ) // 不存在
		{
			return defaultValue;
		}
		
		try
		{
			return Integer.parseInt(value);
		}
		catch (Exception ex)
		{
			return 0;
		}
	}
	
	/**
	 * 获得length长度的字节数组，从头开始用str进行填充，不足的补0
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	protected byte[] getBytes(String str, int length)
	{
		byte[] strBytes = str.getBytes();
		if ( strBytes.length >= length )
		{
			byte[] temp = new byte[length];
			System.arraycopy(strBytes, 0, temp, 0, length);
			return temp;
		}
		
		byte[] temp = new byte[length];
		for (int i = 0; i < length; i++)
		{
			temp[i] = 0;
		}
		
		for (int i = 0; i < strBytes.length; i++)
		{
			temp[i] = strBytes[i];
		}
		
		return temp;
	}
	
	/**
	 * @描述：格式化大数字,大于1000000标记为M 大于1000标记为K,保留1位小数
	 * @作者：岳知之
	 * @时间：2012-5-7 下午7:59:06
	 * @param str
	 * @return
	 */
	public static String formatBigNum(String str)
	{
		String numStr = str;
		float num = ConvertHelper.strToFloat(str);
		if ( num >= 1000000 )
		{
			numStr = (int) (((num / 1000000.0f) + 0.05f) * 10) / 10f + "M";
		}
		else if ( num >= 1000 )
		{
			numStr = (int) (((num / 1000.0f) + 0.05f) * 10) / 10f + "K";
		}
		return numStr;
	}
	
	/**
	 * 返回可用的文件路径 xiongpan 2014-10-11
	 * 
	 * @param key
	 * @param filePath
	 * @return
	 */
	protected String getEnableFile(String key, String filePath)
	{
		String path = filePath;
		String[] paths = Configuration.getString(key).split("\\|");
		for (int i = 0; i < paths.length; i++)
		{
			path = paths[i] + filePath;
			if ( new File(path).exists() )
			{
				break;
			}
			else
			{
				logger.error("路径为'" + path + "'的文件不存在.");
			}
			
		}
		return path;
	}
	
	protected String getMarketParam(RequestImpl request)
	{
		String market = getStrParameter(request, "market");
		if ( market == null )
		{
			market = "SH";
		}
		market = market.toUpperCase();
		
		return market;
	}
}
