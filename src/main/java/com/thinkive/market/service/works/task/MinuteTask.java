package com.thinkive.market.service.works.task;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.thinkive.base.config.Configuration;
import com.thinkive.market.bean.StockOption;
import com.thinkive.market.service.cache.HQDataCache;
import com.thinkive.market.service.cache.HQStateCache;
import com.thinkive.market.service.dao.ThinkConvDao;

/**
 * @描述: 个股期权分时数据处理
 * @版权: Copyright (c) 2012 
 * @公司: 思迪科技 
 * @作者: 熊攀
 * @版本: 1.0 
 * @创建日期: 2015-1-17
 * @创建时间: 下午11:35:39
 */
public class MinuteTask extends BaseConvTask
{
	private static Logger		logger		= Logger.getLogger("MinuteTask");
											
	public static final String	MINUTE_DATA	= "minutedata";
											
	//测试分时数据   从c网关拉取
	public static void main(String[] args)
	{
		ThinkConvDao convDao = null;
		convDao = new ThinkConvDao(false);
		String market = "0";
		String stockCode = "11000001";
		try
		{
			//List minuteHq = 
			convDao.queryMinuteData(stockCode, market, 0);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if ( convDao != null )
			{
				convDao.close();
			}
		}
	}
	
	@Override
	public void init(String param)
	{
		//如果为空则需要初始化分时图
		ThinkConvDao convDao = null;
		try
		{	
			convDao = new ThinkConvDao(false);
			Map minuteMap = (Map) HQDataCache.getData(MINUTE_DATA);
			minuteMap = new ConcurrentHashMap();
			logger.info("个股期权分时图数据开始初始化-------");
			StockOption[] stockOptionArray = HQDataCache.getStockOptionArray();
			if ( null != stockOptionArray && stockOptionArray.length > 0 )
			{
				for (int i = 0; i < stockOptionArray.length; i++)
				{
					String stockCode = stockOptionArray[i].getSecurityID();
					String market = stockOptionArray[i].getMarket();
					
					List minuteHq = convDao.queryMinuteData(stockCode, market, 0);
					minuteMap.put(market + stockCode, minuteHq);
				}
			}
			else
			{
				logger.warn("个股期权列表stockOptionArray为空！！！");
			}
			
			HQDataCache.setData(MINUTE_DATA, minuteMap);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			logger.warn("ThinkConvDao转码机连接异常！！！");
		}
		finally
		{
			if ( convDao != null )
			{
				convDao.close();
			}
		}
		
	}
	
	@Override
	public void update() throws Exception
	{
		/*
		if ( !HQStateCache.isNeedUpdate() )
		{
			return;
		}
		
		ThinkConvDao convDao = null;
		try
		{
			convDao = new ThinkConvDao(false);
			
			Map minuteMap = (Map) HQDataCache.getData(MINUTE_DATA);
			//分时行情如果不为空并且接收模式为none 则 更新分时数据
			if ( null != minuteMap )
			{
				logger.info("个股期权分时数据开始更新-------");
				// long t1 = System.currentTimeMillis();
				//分时数据不接收了
				String mode = Configuration.getString("receiver.mode");
				//if ("none".equalsIgnoreCase(mode) ) {
				//none表示如果接收服务器没启动 ，则拉取更新分时数据     否则不拉取   只接收数据
				//以前的java网关没有推送分时数据  不管是不是none   都要更新
				StockOption[] stockOptionArray = HQDataCache.getStockOptionArray();
				for (int i = 0; i < stockOptionArray.length; i++)
				{
					String stockCode = stockOptionArray[i].getSecurityID();
					String market = stockOptionArray[i].getMarket();
					List minuteHq = (List) minuteMap.get(market + stockCode);
					//获取增量
					List minuteHqInc = convDao.queryMinuteData(stockCode, market, minuteHq.size());
					minuteHq.addAll(minuteHqInc);
					minuteMap.put(market + stockCode, minuteHq);
				}
				//}
				HQDataCache.setData(MINUTE_DATA, minuteMap);
				// logger.info("分时图更新完成，耗时：" + (System.currentTimeMillis() - t1));
			}
			//分时行情如果为空则需要初始化分时图
			else
			{
				minuteMap = new ConcurrentHashMap();
				StockOption[] stockOptionArray = HQDataCache.getStockOptionArray();
				if ( null != stockOptionArray && stockOptionArray.length > 0 )
				{
					for (int i = 0; i < stockOptionArray.length; i++)
					{
						String stockCode = stockOptionArray[i].getSecurityID();
						String market = stockOptionArray[i].getMarket();
						List minuteHq = convDao.queryMinuteData(stockCode, market, 0);
						minuteMap.put(market + stockCode, minuteHq);
					}
				}
				else
				{
					logger.warn("个股期权列表stockOptionArray为空！！！");
				}
				HQDataCache.setData(MINUTE_DATA, minuteMap);
			}
			
		}
		finally
		{
			if ( convDao != null )
			{
				convDao.close();
			}
		}
		
		*/}
		
	@Override
	public void clear()
	{
		HQDataCache.setData(MINUTE_DATA, null);
	}
	
}
