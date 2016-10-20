package com.thinkive.market.service.conn.push;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.thinkive.market.bean.DealData;
import com.thinkive.market.service.cache.HQDataCache;
import com.thinkive.market.service.util.ByteStrHelper;
import com.thinkive.market.service.works.task.DealDataTask;

/**
 * @描述: 个股期权成交明细数据 从转码机接收数据
 * @版权: Copyright (c) 2013
 * @公司: 思迪科技
 * @作者: 熊攀
 * @版本: 1.0
 * @创建日期: 2015-1-17
 * @创建时间: 上午11:15:27
 */
public class Updater10001 extends Updater
{
	private static Logger logger = Logger.getLogger(Updater10001.class);

	private static Updater updater;

	private Updater10001()
	{
	}

	public static Updater getInstance()
	{
		if (updater == null)
		{
			updater = new Updater10001();
			updater.setName("Updater10001");
			updater.start();
		}
		return updater;
	}

	/**
	 * @描述：更新成交明细
	 * @作者：熊攀
	 * @时间：2015-1-21 下午5:34:52
	 * @param data
	 */
	@Override
	public void update(byte[] b)
	{
		Map dealData = (Map) HQDataCache.getData(DealDataTask.DEAL_DATA);
		if (dealData == null)
		{
			dealData = new ConcurrentHashMap();
		}
		ByteBuffer dataBuffer = ByteBuffer.wrap(b);
		dataBuffer.order(ByteOrder.LITTLE_ENDIAN);

		String market = ByteStrHelper.getString(dataBuffer, 2);
		String codeTotal = ByteStrHelper.getString(dataBuffer, 8);// 8个字节
		
		int count = (b.length - 8) / 16;
		List dealInc = new ArrayList();
		for (int i = 0; i < count; i++)
		{
			short minute = dataBuffer.getShort();
			float now = dataBuffer.getFloat();
			int thedeal = dataBuffer.getInt();
			short flag = dataBuffer.getShort();
			float yesterday = dataBuffer.getFloat();
			DealData deal = new DealData();
			deal.setMinute(minute);
			deal.setNow(now);
			deal.setThedeal(thedeal);
			deal.setFlag(flag);
			deal.setYesterday(yesterday);

			if (deal.getThedeal() > 0)
			{
				dealInc.add(deal);
			}
		}

		String key = market+codeTotal;

		List deal = (List) dealData.get(key);
		if (deal == null)
		{
			deal = new ArrayList();
		}
		deal.addAll(dealInc);
		dealData.put(key, deal);
		HQDataCache.setData(DealDataTask.DEAL_DATA, dealData);
		logger.debug("Updater10001处理包成功，key:" + key + ",size:" + deal.size());
	}
}
