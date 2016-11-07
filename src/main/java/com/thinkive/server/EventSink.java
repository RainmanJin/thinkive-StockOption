package com.thinkive.server;

import com.thinkive.AppServer;
import com.thinkive.market.service.works.WorkerManager;

import org.apache.log4j.Logger;

public final class EventSink {
    private static Logger logger = Logger.getLogger(EventSink.class);

    /**
     * 在系统启动时调用
     */
    public static void onStart() {
        //OLDAppServer.main(null);
        AppServer.main(null);
    }

    /**
     * 在系统关闭时调用
     */
    public static void onStop() {
        WorkerManager workerManager = WorkerManager.getInstance();
        workerManager.stop();
        //在此方法内必须关闭所有启动的线程
    }
}
