package com.thinkive.market.service.works.task;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thinkive.market.bean.BaseInfo;
import com.thinkive.market.service.cache.HQDataCache;
import com.thinkive.market.service.cache.HQStateCache;
import com.thinkive.market.service.dao.ThinkConvDao;
import com.thinkive.market.service.util.MarketUtil;

/**
 * @描述: 初始化个股期权基础数据
 * @版权: Copyright (c) 2012 
 * @公司: 思迪科技 
 * @作者: 熊攀
 * @版本: 1.0 
 * @创建日期: 2012-3-20 
 * @创建时间: 下午4:36:31
 */
public class BaseInfoTask extends BaseConvTask
{
	private static Logger		logger		= Logger.getLogger(BaseInfoTask.class);
											
	public static final String	BASE_INFO	= "base_info";
											
	@Override
	public void init(String param) throws Exception
	{
		ThinkConvDao convDao = null;
		
		Map<String, BaseInfo> baseInfoMap = HQDataCache.getData(BASE_INFO);
		
		if ( baseInfoMap == null )
		{
			baseInfoMap = new HashMap<String, BaseInfo>();
			HQDataCache.setData(BASE_INFO, baseInfoMap);
		}
		
		try
		{
			String market = HQStateCache.getInitMarket();
			
			if ( market == null )
			{
				return;
			}
			
			convDao = new ThinkConvDao();
			
			Map<String, BaseInfo> baseInfo = convDao.queryAllBaseInfo(market);
			
			logger.warn("	--	@系统初始化	--	【" + market + "】市场，拉取基础数据个数：" + baseInfo.size());
			
			baseInfoMap.putAll(baseInfo);
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
	public void update()
	{
	
	}
	
	@Override
	public void clear()
	{
		Map<String, ?> map = HQDataCache.getData(BASE_INFO);
		MarketUtil.cleanMapAtInit(map);
	}
	
	public static void main(String[] args)
	{
		ThinkConvDao convDao = null;
		try
		{
			convDao = new ThinkConvDao();
			Map baseInfo;
			try
			{
				baseInfo = convDao.queryAllBaseInfo();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		finally
		{
			if ( convDao != null )
			{
				convDao.close();
			}
		}
	}
	
}
