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
 * @����: ��ʼ��������Ȩ��������
 * @��Ȩ: Copyright (c) 2012 
 * @��˾: ˼�ϿƼ� 
 * @����: ����
 * @�汾: 1.0 
 * @��������: 2012-3-20 
 * @����ʱ��: ����4:36:31
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
			
			logger.warn("	--	@ϵͳ��ʼ��	--	��" + market + "���г�����ȡ�������ݸ�����" + baseInfo.size());
			
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
