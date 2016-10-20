package com.thinkive.market.function.version_1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thinkive.market.bean.DealData;
import com.thinkive.market.common.impl.RequestImpl;
import com.thinkive.market.common.impl.ResponseImpl;
import com.thinkive.market.service.cache.HQDataCache;
import com.thinkive.market.service.works.task.DealDataTask;
import com.thinkive.server.Request;
import com.thinkive.server.Response;

/**
 * @描述: 个股期权成交明细
 * @版权: Copyright (c) 2012 
 * @公司: 思迪科技 
 * @作者: 熊攀
 * @版本: 1.0 
 * @创建日期: 2015-1-19 
 * @创建时间: 上午9:29:31
 */
public class Function60010 extends BaseFunction
{
	private static Logger logger = Logger.getLogger(Function60010.class);
	
	@Override
	public void service(Request req, Response resp)
	{
		RequestImpl request = (RequestImpl) req;
		ResponseImpl response = (ResponseImpl) resp;
		String SecurityID = getStrParameter(request, "SecurityID"); //期权编码   唯一标识
		String market = getMarketParam(request);
		int count = getIntParameter(request, "count", 0); //客户端已经存在的成交明细数量
		
		Map result = new HashMap();
		try
		{
			List list = new ArrayList();
			Map stockDealDataMap = (Map) HQDataCache.getData(DealDataTask.DEAL_DATA);
			
			List dealList = null;
			if ( null != stockDealDataMap && stockDealDataMap.size() > 0 )
			{
				
				dealList = (List) stockDealDataMap.get(market + SecurityID);
			}
			//   List dealList = (List) stockDealDataMap.get(key);
			if ( dealList != null )
			{
				int length = dealList.size();
				
				if ( count > length )
				{
					count = length;
				}
				
				result.put("size", length);
				for (int i = 0; i < count; i++)
				{
					DealData data = (DealData) dealList.get(length - count + i);
					
					Object item[] = new Object[5];
					item[0] = data.getMinute();
					item[1] = data.getNow();
					item[2] = data.getThedeal();
					item[3] = data.getFlag();
					item[4] = data.getYesterday();
					list.add(item);
				}
				
			}
			result.put("errorNo", 0);
			result.put("errorInfo", "");
			result.put("results", list);
			
		}
		catch (Exception e)
		{
			result.put("errorNo", -600101);
			result.put("errorInfo", e.getMessage());
			logger.info("", e);
		}
		response.write(result);
	}
	
}
