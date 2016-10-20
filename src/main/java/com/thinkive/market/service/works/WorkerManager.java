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
 * @����: ��������������е��߳��������������
 * @��Ȩ: Copyright (c) 2013 
 * @��˾: ˼�ϿƼ� 
 * @����: ��֪֮
 * @�汾: 1.0 
 * @��������: 2013-11-5 
 * @����ʱ��: ����3:25:26
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
				//��ʼ��ITask�࣬����ִ�о��巽��
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
				
				/*��Ϊ�������õ�ʱ���ȡ��time��1970�굱���ʱ�䣬
				 * ������Ҫ��ʼ���ɵ����ʱ��
				 */
				//��ȡ�����������Ϊ����
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
				//���task����Ϊ0��˵����ϵͳ���ͣ��ڳ�ʼ����ʱ������
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
			logger.error("�����������ʼ������", e);
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
	 * @����������ID��ȡworker����
	 * @���ߣ���֪֮
	 * @ʱ�䣺2012-3-21 ����5:09:04
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
	 * @��������ȡ���й����б�
	 * @���ߣ���֪֮
	 * @ʱ�䣺2013-2-28 ����4:24:38
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
