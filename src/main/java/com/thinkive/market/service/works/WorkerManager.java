package com.thinkive.market.service.works;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thinkive.base.util.ConvertHelper;
import com.thinkive.base.util.DateHelper;
import com.thinkive.base.util.StringHelper;

/**
 * @描述: 任务管理器，所有的线程任务在这里管理
 * @版权: Copyright (c) 2013 
 * @公司: 思迪科技 
 * @作者: 岳知之
 * @版本: 1.0 
 * @创建日期: 2013-11-5 
 * @创建时间: 下午3:25:26
 */
public class WorkerManager
{
	private static Logger			logger	= Logger.getLogger(WorkerManager.class);
											
	private static WorkerManager	INSTANCE;
									
	private List<DayWorker>			workers;
									
	private WorkerManager()
	{
		init();
	}
	
	public static WorkerManager getInstance()
	{
		if ( INSTANCE == null )
		{
			INSTANCE = new WorkerManager();
		}
		return INSTANCE;
	}
	
	private void init()
	{
		workers = new ArrayList();
		try
		{
			List taskList = TaskConfig.getTaskList();
			for (Iterator iterator = taskList.iterator(); iterator.hasNext();)
			{
				Map task = (Map) iterator.next();
				DayWorker worker = new DayWorker();
				//初始化ITask类，用于执行具体方法
				worker.setTask((ITask) Class.forName((String) task.get("task-class")).newInstance());
				worker.setDescription((String) task.get("description"));
				worker.setId((String) task.get("id"));
				worker.setType(ConvertHelper.strToInt((String) task.get("task-type")));
				worker.setInterval(ConvertHelper.strToInt((String) task.get("task-interval")));
				worker.setName((String) task.get("task-name"));
				worker.setParam((String) task.get("task-param"));
				
				String market = (String) task.get("task-market");
				if ( market == null )
				{
					market = "SH";
				}
				worker.setMarket(market);
				
				/*因为根据配置的时间获取的time是1970年当天的时间，
				 * 所以需要初始化成当天的时间
				 */
				//获取当天的日期作为增量
				String date = DateHelper.formatDate(new Date(), "yyyy-MM-dd");
				String begintime = (String) task.get("task-begintime");
				if ( StringHelper.isNotEmpty(begintime) )
				{
					worker.setBegintime(DateHelper.parseString(date + " " + begintime, "yyyy-MM-dd HH:mm").getTime());
				}
				String endtime = (String) task.get("task-endtime");
				if ( StringHelper.isNotEmpty(endtime) )
				{
					worker.setEndtime(DateHelper.parseString(date + " " + endtime, "yyyy-MM-dd HH:mm").getTime());
				}
				//如果task类型为0则说明是系统类型，在初始化的时候启动
				if ( worker.getType() == 0 )
				{
					worker.init();
				}
				new Thread(worker, "Worker-" + worker.getId()).start();
				workers.add(worker);
			}
		}
		catch (Exception e)
		{
			logger.error("任务管理器初始化出错", e);
		}
	}
	
	public void start()
	{
		for (Iterator iterator = workers.iterator(); iterator.hasNext();)
		{
			DayWorker worker = (DayWorker) iterator.next();
			worker.setRunning(true);
		}
	}
	
	public void stop()
	{
		for (Iterator iterator = workers.iterator(); iterator.hasNext();)
		{
			DayWorker worker = (DayWorker) iterator.next();
			worker.setRunning(false);
		}
	}
	
	/**
	 * @描述：根据ID获取worker对象。
	 * @作者：岳知之
	 * @时间：2012-3-21 下午5:09:04
	 * @param id
	 * @return
	 */
	public DayWorker getWorkerById(String id)
	{
		DayWorker target = null;
		if ( id != null )
		{
			for (Iterator iterator = workers.iterator(); iterator.hasNext();)
			{
				DayWorker worker = (DayWorker) iterator.next();
				if ( id.equals(worker.getId()) )
				{
					target = worker;
					break;
				}
			}
		}
		return target;
	}
	
	/**
	 * @描述：获取所有工作列表
	 * @作者：岳知之
	 * @时间：2013-2-28 下午4:24:38
	 * @return
	 */
	public List<DayWorker> getAllWorkerList()
	{
		return workers;
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		WorkerManager stateThread = new WorkerManager();
		stateThread.start();
		
	}
}
