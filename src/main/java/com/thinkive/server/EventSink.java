package com.thinkive.server;

import org.apache.log4j.Logger;

import com.thinkive.AppServer;
import com.thinkive.market.service.works.WorkerManager;

public final class EventSink
{
    private static Logger logger = Logger.getLogger(EventSink.class);
    
    /**
     * ��ϵͳ����ʱ����
     */
    public static void onStart()
    {
        //OLDAppServer.main(null);
    	AppServer.main(null);
    }
    
    /**
     * ��ϵͳ�ر�ʱ����
     */
    public static void onStop()
    {
        WorkerManager workerManager = WorkerManager.getInstance();
        workerManager.stop();
        //�ڴ˷����ڱ���ر������������߳�
    }
}
