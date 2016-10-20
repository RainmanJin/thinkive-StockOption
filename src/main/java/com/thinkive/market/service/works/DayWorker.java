package com.thinkive.market.service.works;

import java.util.Calendar;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.thinkive.market.Library;

/**
 * @描述: 用于做任务调度的线程，执行每天的任务
 * @版权: Copyright (c) 2012
 * @公司: 思迪科技
 * @作者: 岳知之
 * @版本: 1.0
 * @创建日期: 2012-3-16
 * @创建时间: 上午10:38:29
 */
public class DayWorker extends BaseWorker implements Runnable
{
	private static Logger	logger	= Logger.getLogger(DayWorker.class);
									
	private boolean			isInit	= false;
									
	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				if ( isRunning() && isInit )// 线程总开关 如果任务已经初始化好了则开始运转
				{
					if ( getEndtime() == 0 )// 如果Endtime为0则表示永久任务，永远更新下去
					{
						update();
					}
					else
					{
						
						Calendar c = Calendar.getInstance();
						
						int nowDate = c.get(Calendar.DATE);// 当前日期
						// 开始日期
						c.setTimeInMillis(getBegintime());
						// 日期不相等，说明已经到了第二天
						if ( c.get(Calendar.DATE) != nowDate )
						{
							Calendar now = Calendar.getInstance();
							c.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
							setBegintime(c.getTimeInMillis());
							c.setTimeInMillis(getEndtime());
							c.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
							setEndtime(c.getTimeInMillis());
						}
						
						// 当开始时间小于当前时间，结束时间大于当前时间则执行更新
						if ( getBegintime() < System.currentTimeMillis() && getEndtime() > System.currentTimeMillis() )
						{
							update();
						}
						// 如果当前时间大于结束日期说明当天的行情更新已经结束，将初始化设为false以待下一个交易日的更新
						if ( getEndtime() > 0 && System.currentTimeMillis() > getEndtime() )
						{
							isInit = false;
							logger.warn(getName() + "如果当前时间大于结束日期说明当天的行情更新已经结束，将初始化设为false以待下一个交易日的更新");
						}
					}
					Thread.sleep(getInterval() > 0 ? getInterval() : 5000);
				}
				else
				{
					Thread.sleep(1000);
				}
			}
			catch (Exception e)
			{
				logger.warn("调用[" + getId() + "]" + getName() + "异常", e);
			}
		}
		
	}
	
	/**
	 * @throws Exception
	 * @描述：更新动作
	 * @作者：岳知之
	 * @时间：2013-2-28 下午2:37:14
	 */
	public void update() throws Exception
	{
		
		logger.debug("开始更新" + getName());
		long t1 = System.currentTimeMillis();
		// 执行任务
		task.update();
		
		long t2 = System.currentTimeMillis();
		logger.debug("执行更新" + getName() + "时间为" + (t2 - t1) + "ms");
		
	}
	
	/**
	 * @描述：初始化动作
	 * @作者：岳知之
	 * @时间：2013-2-28 下午2:37:24
	 */
	public void init()
	{
		logger.warn("	--	@系统初始化	--	开始初始化【" + getName() + "】");
		long t1 = System.currentTimeMillis();
		isInit = false;
		try
		{
			// 执行任务
			task.init(getParam());
			t1 = System.currentTimeMillis() - t1;
			// 初始化完成，状态置为true
			isInit = true;
			logger.warn("	--	@系统初始化	--	【" + getName() + "】初始化完成，耗时 [" + t1 + "ms]");
		}
		catch (Exception e)
		{
			logger.warn(getName() + "初始化失败，状态置为false", e); // TODO: handle
		}
		
	}
	
	/**
	 * @throws Exception
	 * @描述：清理数据
	 * @作者：岳知之
	 * @时间：2013-2-28 下午4:45:37
	 */
	public void clear() throws Exception
	{
		logger.debug("开始清理" + getName());
		task.clear();
		logger.debug("完成清理" + getName());
		
	}
}
