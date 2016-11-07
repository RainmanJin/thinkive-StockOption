package com.thinkive;

import com.thinkive.base.config.Configuration;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyServer {
    private static QueuedThreadPool pool = new QueuedThreadPool();

    private Server webServer = null;

    public JettyServer() throws ClassNotFoundException {
        int port = Configuration.getInt("jetty.port");
        int maxidletime = Configuration.getInt("jetty.maxidletime");
        int minthreads = Configuration.getInt("jetty.minthreads");
        int maxthreads = Configuration.getInt("jetty.maxthreads");

        webServer = new Server();
        Connector connector = new SelectChannelConnector();

        connector.setPort(port);
        //1������������ʱ��
        connector.setMaxIdleTime(maxidletime);

        webServer.setConnectors(new Connector[]{connector});

        //�����߳�
        pool.setMinThreads(minthreads);
        pool.setMaxThreads(maxthreads);
        pool.setDetailedDump(false);
        webServer.setThreadPool(pool);

        WebAppContext context = new WebAppContext();
        //����Ӧ�ø�Ŀ¼����
        context.setContextPath("/");
        //���ø�Ŀ¼ 
        context.setResourceBase(Configuration.getString("jetty.webroot", "./WebRoot"));
        context.setDescriptor(Configuration.getString("jetty.confpath", "./classes/jetty-web.xml"));

        //���� servlet����
//        context.addServlet((Class<? extends Servlet>) Class.forName("com.thinkive.market.web.MarketServlet"), "/cgi-bin/market/market");
//        context.addServlet((Class<? extends Servlet>) Class.forName("com.thinkive.market.web.JsonServlet"), "/market/json");
//        context.addServlet((Class<? extends Servlet>) Class.forName("com.thinkive.market.web.TestServlet"), "/market/test");
//        context.addServlet((Class<? extends Servlet>) Class.forName("com.thinkive.market.web.StateServlet"), "/market/state");

        webServer.setHandler(context);

        webServer.setStopAtShutdown(true);
        webServer.setSendServerVersion(true);
        webServer.setSendDateHeader(true);
        webServer.setGracefulShutdown(1000);
        webServer.setDumpAfterStart(false);
        webServer.setDumpBeforeStop(false);
    }

    public void start() throws Exception {
        webServer.start();
        webServer.join();
    }
}
