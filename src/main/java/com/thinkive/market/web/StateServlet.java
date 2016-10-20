package com.thinkive.market.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.thinkive.base.config.Configuration;
import com.thinkive.base.util.DateHelper;
import com.thinkive.market.Library;
import com.thinkive.market.service.cache.HQDataCache;
import com.thinkive.market.service.cache.HQStateCache;
import com.thinkive.market.service.conn.Receiver;

/**
 * @描述: 用于处理HTTP协议请求
 * @版权: Copyright (c) 2012
 * @公司: 思迪科技
 * @作者: 岳知之
 * @版本: 1.0
 * @创建日期: 2012-4-21
 * @创建时间: 下午2:56:11
 */
public class StateServlet extends HttpServlet
{
	private static Logger logger = Logger.getLogger(StateServlet.class);
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			response.setCharacterEncoding(Library.getEncoding());
			
			PrintWriter writer = response.getWriter();
			StringBuffer buffer = new StringBuffer();
			buffer.append("==================SYSTEM================\n");
			buffer.append(getSystemState()).append("\n");
			
			buffer.append("==================MEMERY================\n");
			buffer.append(getMemery()).append("\n");
			
			buffer.append("==================THREAD================\n");
			buffer.append(getThreadState()).append("\n");
			writer.print(buffer.toString());
			writer.flush();
			writer.close();
		}
		
		catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @描述：获取系统使用情况
	 * @作者：岳知之
	 * @时间：2013-11-16 下午5:57:56
	 * @return
	 */
	public static String getSystemState()
	{
		StringBuffer buffer = new StringBuffer();
		long udpQueueSize = Receiver.getBufferSize();
		long udpcount = Receiver.getCount();
		boolean isReceived = Receiver.isReceived();
		boolean isNeedUpdate = HQStateCache.isNeedUpdate();
		boolean isInitCompleted = HQStateCache.SH.isInitCompleted();
		String convDate = HQStateCache.SH.getConvDate();
		long convTime = HQStateCache.SH.getDbftime();
		String currentDate = HQStateCache.SH.getInitDate();
		String mode = Configuration.getString("receiver.mode");
		int stockLength = HQDataCache.getStockOptionArray().length;
		Calendar c = Calendar.getInstance();
		String sysDate = DateHelper.formatDate(new Date(c.getTimeInMillis()));
		
		buffer.append("ReceiveQueueSize = ").append(udpQueueSize).append("\n");
		buffer.append("ReceiveCount = ").append(udpcount).append("\n");
		buffer.append("ReceiveMode = ").append(mode).append("\n");
		buffer.append("isReceived = ").append(isReceived).append("\n");
		buffer.append("isNeedUpdate = ").append(isNeedUpdate).append("\n");
		buffer.append("isInitCompleted = ").append(isInitCompleted).append("\n");
		buffer.append("convDate = ").append(convDate).append("\n");
		buffer.append("convTime = ").append(DateHelper.formatDate(new Date(convTime), DateHelper.pattern_time))
				.append("\n");
		buffer.append("currentDate = ").append(currentDate).append("\n");
		buffer.append("sysDate = ").append(sysDate).append("\n");
		buffer.append("stockLength = ").append(stockLength).append("\n");
		return buffer.toString();
	}
	
	public static Thread[] findAllThreads()
	{
		ThreadGroup group = Thread.currentThread().getThreadGroup();
		ThreadGroup topGroup = group;
		while (group != null)
		{
			topGroup = group;
			group = group.getParent();
		}
		int estimatedSize = topGroup.activeCount() * 2;
		Thread[] slackList = new Thread[estimatedSize];
		int actualSize = topGroup.enumerate(slackList);
		Thread[] list = new Thread[actualSize];
		System.arraycopy(slackList, 0, list, 0, actualSize);
		return list;
	}
	
	/**
	 * @描述：获取内存使用情况
	 * @作者：岳知之
	 * @时间：2013-11-16 下午2:29:04
	 * @return
	 */
	public static String getMemery()
	{
		//        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		//        // 总的物理内存+虚拟内存
		//        long totalvirtualMemory = osmxb.getTotalSwapSpaceSize();
		//        System.out.println(totalvirtualMemory / 1024 / 1024);
		//        // 剩余的物理内存
		//        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();
		//        System.out.println(freePhysicalMemorySize / 1024 / 1024);
		//        Double compare = (Double) (1 - freePhysicalMemorySize * 1.0 / totalvirtualMemory) * 100;
		//                String str = "mem used:" + compare.intValue() + "%\n";
		String str = "";
		long heapSize = Runtime.getRuntime().totalMemory();
		long heapMaxSize = Runtime.getRuntime().maxMemory();
		long heapFreeSize = Runtime.getRuntime().freeMemory();
		str += ("heapSize = " + heapSize / 1024L / 1024L + " M") + "\n";
		str += ("heapMaxSize = " + heapMaxSize / 1024L / 1024L + " M") + "\n";
		str += ("heapFreeSize = " + heapFreeSize / 1024L / 1024L + " M") + "\n";
		str += "jvm mem used = " + (int) ((1 - (heapFreeSize * 1.0 / heapMaxSize)) * 100) + "%" + "\n";
		return str;
	}
	
	/**
	 * @描述：获取线程使用情况
	 * @作者：岳知之
	 * @时间：2013-11-16 下午2:29:04
	 * @return
	 */
	public static String getThreadState()
	{
		StringBuffer buffer = new StringBuffer();
		Thread[] threadGroup = findAllThreads();
		for (int i = 0; i < threadGroup.length; i++)
		{
			Thread t = threadGroup[i];
			Integer priority = new Integer(t.getPriority());
			Boolean isAlive = new Boolean(t.isAlive());
			Boolean isDaemon = new Boolean(t.isDaemon());
			Boolean isInterrupted = new Boolean(t.isInterrupted());
			String name = t.getName();
			if ( name.indexOf("Worker") >= 0 || name.indexOf("Receive") >= 0 || name.indexOf("Updater") >= 0 )
			{
				buffer.append("Thread[" + name + "](isAlive=" + isAlive + ")\n");
			}
		}
		return buffer.toString();
	}
	
}
