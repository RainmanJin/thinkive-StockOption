package com.thinkive.market.web;

import com.thinkive.base.config.Configuration;
import com.thinkive.base.util.DateHelper;
import com.thinkive.market.Library;
import com.thinkive.market.service.cache.HQDataCache;
import com.thinkive.market.service.cache.HQStateCache;
import com.thinkive.market.service.conn.Receiver;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @描述: 用于处理HTTP协议请求
 * @版权: Copyright (c) 2012
 * @公司: 思迪科技
 * @作者: 岳知之
 * @版本: 1.0
 * @创建日期: 2012-4-21
 * @创建时间: 下午2:56:11
 */
public class StateServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(StateServlet.class);

    /**
     * @描述：获取系统使用情况
     * @作者：岳知之
     * @时间：2013-11-16 下午5:57:56
     */
    public static String getSystemState() {
        StringBuffer buffer = new StringBuffer();
        long udpQueueSize = Receiver.getBufferSize();
        long udpcount = Receiver.getCount();
        boolean isReceived = true;
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

    public static Thread[] findAllThreads() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup topGroup = group;
        while (group != null) {
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
     */
    public static String getMemery() {

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
     */
    public static String getThreadState() {
        StringBuffer buffer = new StringBuffer();
        Thread[] threadGroup = findAllThreads();
        for (int i = 0; i < threadGroup.length; i++) {
            Thread t = threadGroup[i];
            Integer priority = new Integer(t.getPriority());
            Boolean isAlive = new Boolean(t.isAlive());
            Boolean isDaemon = new Boolean(t.isDaemon());
            Boolean isInterrupted = new Boolean(t.isInterrupted());
            String name = t.getName();
            if (name.indexOf("Worker") >= 0 || name.indexOf("Receive") >= 0 || name.indexOf("Updater") >= 0) {
                buffer.append("Thread[" + name + "](isAlive=" + isAlive + ")\n");
            }
        }
        return buffer.toString();
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
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
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
