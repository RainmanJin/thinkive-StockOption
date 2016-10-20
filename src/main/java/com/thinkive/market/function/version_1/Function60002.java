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
 * @描述: 根据标的  市场代码和证券代码  到期月份 和  距离到期日的天数    如：   1501(21天)  以及认购或认沽期权  获取期权合约
 * @版权: Copyright (c) 2012 
 * @公司: 思迪科技 
 * @作者: 熊攀
 * @版本: 1.0 
 * @创建日期: 2015-1-16
 * @创建时间: 下午2:14:16
 */
public class Function60002 extends BaseFunction
{
	private static Logger logger = Logger.getLogger(Function60002.class);
	
	@Override
	public void service(Request req, Response resp)
	{
		// TODO Auto-generated method stub
		
		RequestImpl request = (RequestImpl) req;
		ResponseImpl response = (ResponseImpl) resp;
		String stockCode = getStrParameter(request, "stock_code"); // 股票代码
		String market = getMarketParam(request);
		
		String deadlineMonth = getStrParameter(request, "deadlineMonth"); // 到期月份  例如：1501
		
		int iPC = getIntParameter(request, "ispc");// 1认购 0认沽
		
		String field = getStrParameter(request, "field", "34:35:2:3:1:39");//出参
		int[] fieldArray = ConvertHelper.strArrayToIntArray(field.split(":"));
		
		List stockOptionList = getStockOptionList(market, stockCode, iPC, deadlineMonth);
		
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
			result.put("errorNo", -600021);
			result.put("errorInfo", e.getMessage());
			logger.info("", e);
		}
		
		response.write(result);
	}
	
	private List getStockOptionList(String market, String stockCode, int iPC, String deadlineMonth)
	{
		StockOption[] stockOptionArray = HQDataCache.getStockOptionArray();
		
		Map<String, StockOption> stockOptionKeyMap = HQDataCache.getStockOptionMap();
		
		List stockOptionList = new ArrayList();
		
		if ( stockOptionArray != null && stockOptionArray.length > 0 )
		{
			for (int i = 0; i < stockOptionArray.length; i++)
			{
				if ( stockOptionArray[i].getMarket().equalsIgnoreCase(market)
						&& stockOptionArray[i].getUnderlyingSecurityID().equals(stockCode) )
				{
					String key = stockOptionArray[i].getMarket() + stockOptionArray[i].getSecurityID();// 合约编码
					if ( stockOptionKeyMap != null && stockOptionKeyMap.size() > 0 )
					{
						StockOption stockOption = stockOptionKeyMap.get(key);
						
						String ListID = stockOption.getListID();//1501
						int isPC = stockOption.getiPC();
						
						if ( isPC == iPC && ListID.equalsIgnoreCase(deadlineMonth) )
						{
							stockOptionList.add(stockOption);
						}
					}
				}
			}
		}
		
		return stockOptionList;
	}
	
}
