package com.thinkive;

import com.thinkive.base.config.Configuration;
import com.thinkive.base.util.StringHelper;
import com.thinkive.market.service.conn.TCPReceiver1;
import com.thinkive.market.service.conn.UDPReceiver;
import com.thinkive.market.service.works.WorkerManager;

import org.apache.log4j.Logger;

import java.util.TimeZone;

/**
 * 描述: 版权: Copyright (c) 2014
 * 公司: 思迪科技 作者: 熊攀
 * 版本: 1.0
 * 创建日期: 2015-01-14
 * 创建时间: 10:29:38
 */
public class AppServer {

    private static Logger logger = Logger.getLogger(AppServer.class);

    public static void main(String[] args) {
//		start();
        jettyStart();
    }

    public static void jettyStart() {
        String jettyPort = Configuration.getString("jetty.port");
        if (StringHelper.isNotEmpty(jettyPort)) {
            new Thread() {
                @Override
                public void run() {
                    JettyServer server;
                    try {
                        server = new JettyServer();
                        server.start();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                ;
            }.start();
        }
    }

    public static void start() {
        try {
            String timezone = Configuration.getString("general.timezone");
            if (StringHelper.isNotEmpty(timezone)) {
                TimeZone.setDefault(TimeZone.getTimeZone(timezone));
            }
            logger.info("当前时区[" + TimeZone.getDefault() + "]");

            String mode = Configuration.getString("receiver.mode");

            if ("udp".equalsIgnoreCase(mode)) {
                UDPReceiver udpserver = new UDPReceiver();
                udpserver.start();
                logger.info("UDP接收服务器启动");
            } else if ("tcp".equalsIgnoreCase(mode)) {
                TCPReceiver1 tcpserver = new TCPReceiver1();
                tcpserver.start();
                logger.info("TCP接收服务器启动");
            } else {
                logger.warn("接收服务器不启动，请检查好拉取TASK是否配置");
            }
            WorkerManager workerManager = WorkerManager.getInstance();
            workerManager.start();

        } catch (Exception ex) {
            logger.error("启动服务器出错: " + ex.getMessage());
            System.exit(-1);
        }

    }
}
