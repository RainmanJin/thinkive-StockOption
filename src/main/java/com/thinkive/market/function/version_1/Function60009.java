package com.thinkive.market.function.version_1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thinkive.market.bean.StockOption;
import com.thinkive.market.common.impl.RequestImpl;
import com.thinkive.market.common.impl.ResponseImpl;
import com.thinkive.market.service.cache.HQDataCache;
import com.thinkive.server.Request;
import com.thinkive.server.Response;

/**
 * @描述: 根据标的市场代码和证券代码获取到期月份 和  距离到期日的天数    如：   1501(21天)
 * @版权: Copyright (c) 2012 
 * @公司: 思迪科技 
 * @作者: 熊攀
 * @版本: 1.0 
 * @创建日期: 2015-1-16
 * @创建时间: 下午2:14:16
 */
public class Function60009 extends BaseFunction
{
	private static Logger logger = Logger.getLogger(Function60009.class);
	
	@Override
	public void service(Request req, Response resp)
	{
		RequestImpl request = (RequestImpl) req;
		ResponseImpl response = (ResponseImpl) resp;
		
		String stockCode = getStrParameter(request, "stock_code"); //股票代码
		String market = getStrParameter(request, "market"); //市场代码
		
		StockOption[] stockOptionArray = HQDataCache.getStockOptionArray();
		
		Map stockOptionKeyMap = HQDataCache.getStockOptionMap();
		Map result = new HashMap();
		
		try
		{
			
			List list = new ArrayList();
			
			List stockOptionListIDList = new ArrayList();
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
							StockOption stockOption = (StockOption) stockOptionKeyMap.get(key);
							
							String ListID = stockOption.getListID();//1501
							int LeaveDay = stockOption.getLeaveDay();//21   tian
							
							int k = 0;
							for (int m = 0; m < stockOptionListIDList.size(); m++)
							{
								if ( stockOptionListIDList.get(m).equals(ListID) )
								{
									k = 1;
									break;
								}
							}
							stockOptionListIDList.add(ListID);
							
							if ( k != 1 )
							{
								String item[] = new String[2];
								item[0] = ListID;
								item[1] = String.valueOf(LeaveDay);
								list.add(item);
							}
						}
					}
				}
				
			}
			
			if ( null != list && list.size() > 0 )
			{
				//到期天数  排序   从小到大
				for (int n = 0; n < list.size() - 1; n++)
				{
					
					for (int j = 1; j < list.size() - n; j++)
					{
						
						String item[] = (String[]) list.get(j - 1);
						String item1[] = (String[]) list.get(j);
						/*if(Integer.parseInt((String) item[1])>Integer.parseInt((String) item1[1])){*/
						if ( Integer.parseInt(item[1]) > Integer.parseInt(item1[1]) )
						{
							list.set(j - 1, item1);
							list.set(j, item);
						}
					}
				}
			}
			result.put("errorNo", 0);
			result.put("errorInfo", "");
			result.put("results", list);
			
		}
		catch (Exception e)
		{
			result.put("errorNo", -600091);
			result.put("errorInfo", e.getMessage());
			logger.info("", e);
		}
		response.write(result);
		
	}
}
