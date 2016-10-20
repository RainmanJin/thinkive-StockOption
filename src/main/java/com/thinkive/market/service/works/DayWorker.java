package com.thinkive.market.service.works;

import java.util.Calendar;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.thinkive.market.Library;

/**
 * @����: ������������ȵ��̣߳�ִ��ÿ�������
 * @��Ȩ: Copyright (c) 2012
 * @��˾: ˼�ϿƼ�
 * @����: ��֪֮
 * @�汾: 1.0
 * @��������: 2012-3-16
 * @����ʱ��: ����10:38:29
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
				if ( isRunning() && isInit )// �߳��ܿ��� ��������Ѿ���ʼ��������ʼ��ת
				{
					if ( getEndtime() == 0 )// ���EndtimeΪ0���ʾ����������Զ������ȥ
					{
						update();
					}
					else
					{
						
						Calendar c = Calendar.getInstance();
						
						int nowDate = c.get(Calendar.DATE);// ��ǰ����
						// ��ʼ����
						c.setTimeInMillis(getBegintime());
						// ���ڲ���ȣ�˵���Ѿ����˵ڶ���
						if ( c.get(Calendar.DATE) != nowDate )
						{
							Calendar now = Calendar.getInstance();
							c.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
							setBegintime(c.getTimeInMillis());
							c.setTimeInMillis(getEndtime());
							c.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
							setEndtime(c.getTimeInMillis());
						}
						
						// ����ʼʱ��С�ڵ�ǰʱ�䣬����ʱ����ڵ�ǰʱ����ִ�и���
						if ( getBegintime() < System.currentTimeMillis() && getEndtime() > System.currentTimeMillis() )
						{
							update();
						}
						// �����ǰʱ����ڽ�������˵���������������Ѿ�����������ʼ����Ϊfalse�Դ���һ�������յĸ���
						if ( getEndtime() > 0 && System.currentTimeMillis() > getEndtime() )
						{
							isInit = false;
							logger.warn(getName() + "�����ǰʱ����ڽ�������˵���������������Ѿ�����������ʼ����Ϊfalse�Դ���һ�������յĸ���");
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
				logger.warn("����[" + getId() + "]" + getName() + "�쳣", e);
			}
		}
		
	}
	
	/**
	 * @throws Exception
	 * @���������¶���
	 * @���ߣ���֪֮
	 * @ʱ�䣺2013-2-28 ����2:37:14
	 */
	public void update() throws Exception
	{
		
		logger.debug("��ʼ����" + getName());
		long t1 = System.currentTimeMillis();
		// ִ������
		task.update();
		
		long t2 = System.currentTimeMillis();
		logger.debug("ִ�и���" + getName() + "ʱ��Ϊ" + (t2 - t1) + "ms");
		
	}
	
	/**
	 * @��������ʼ������
	 * @���ߣ���֪֮
	 * @ʱ�䣺2013-2-28 ����2:37:24
	 */
	public void init()
	{
		logger.warn("	--	@ϵͳ��ʼ��	--	��ʼ��ʼ����" + getName() + "��");
		long t1 = System.currentTimeMillis();
		isInit = false;
		try
		{
			// ִ������
			task.init(getParam());
			t1 = System.currentTimeMillis() - t1;
			// ��ʼ����ɣ�״̬��Ϊtrue
			isInit = true;
			logger.warn("	--	@ϵͳ��ʼ��	--	��" + getName() + "����ʼ����ɣ���ʱ [" + t1 + "ms]");
		}
		catch (Exception e)
		{
			logger.warn(getName() + "��ʼ��ʧ�ܣ�״̬��Ϊfalse", e); // TODO: handle
		}
		
	}
	
	/**
	 * @throws Exception
	 * @��������������
	 * @���ߣ���֪֮
	 * @ʱ�䣺2013-2-28 ����4:45:37
	 */
	public void clear() throws Exception
	{
		logger.debug("��ʼ����" + getName());
		task.clear();
		logger.debug("�������" + getName());
		
	}
}
