package com.thinkive.market.function.version_1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thinkive.base.util.ConvertHelper;
import com.thinkive.market.bean.StockOption;
import com.thinkive.market.common.impl.RequestImpl;
import com.thinkive.market.common.impl.ResponseImpl;
import com.thinkive.market.service.cache.HQDataCache;
import com.thinkive.server.Request;
import com.thinkive.server.Response;

/**
 * @描述: 查询个股期权详情数据
 * @版权: Copyright (c) 2012 
 * @公司: 思迪科技 
 * @作者: 熊攀
 * @版本: 1.0 
 * @创建日期: 2015-1-16 
 * @创建时间: 下午2:14:16
 */
public class Function60003 extends BaseFunction
{
	private static Logger logger = Logger.getLogger(Function60003.class);
	
	@Override
	public void service(Request req, Response resp)
	{
		RequestImpl request = (RequestImpl) req;
		ResponseImpl response = (ResponseImpl) resp;
		String SecurityID = getStrParameter(request, "SecurityID"); // 合约编码   8位数   唯一标识
		String market = getMarketParam(request);
		
		String field = getStrParameter(request, "field",
				"24:34:39:2:3:1:9:12:40:36:46:37:41:42:43:19:18:17:10:11:25:6:14:44:45");//出参
		int[] fieldArray = ConvertHelper.strArrayToIntArray(field.split(":"));
		
		List stockOptionList = getStockOptionList(market + SecurityID);
		
		Map result = new HashMap();
		try
		{
			List list = new ArrayList();
			if ( null != stockOptionList && stockOptionList.size() > 0 )
			{
				for (int k = 0; k < stockOptionList.size(); k++)
				{
					StockOption stockOption = (StockOption) stockOptionList.get(k);
					if ( stockOption != null )
					{
						Object item[] = new Object[fieldArray.length];
						for (int i = 0; i < fieldArray.length; i++)
						{
							item[i] = stockOption.getDataBySort(fieldArray[i]);
							if ( fieldArray[i] == 40 )
							{//隐波
								item[i] = 0;
							}
							if ( fieldArray[i] == 36 )
							{//结算
								item[i] = 0;
							}
						}
						list.add(item);
					}
				}
			}
			
			result.put("errorNo", 0);
			result.put("errorInfo", "");
			result.put("results", list);
		}
		catch (Exception e)
		{
			result.put("errorNo", -600031);
			result.put("errorInfo", e.getMessage());
			logger.info("", e);
		}
		
		response.write(result);
	}
	
	private List getStockOptionList(String key)
	{
		
		Map stockOptionKeyMap = HQDataCache.getStockOptionMap();
		List stockOptionList = new ArrayList();
		
		if ( stockOptionKeyMap != null && stockOptionKeyMap.size() > 0 )
		{
			StockOption stockOption = (StockOption) stockOptionKeyMap.get(key);
			if ( null != stockOption )
			{
				stockOptionList.add(stockOption);
			}
		}
		
		return stockOptionList;
	}
	
}
