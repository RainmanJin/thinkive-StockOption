package com.thinkive;

import com.thinkive.base.config.Configuration;
import com.thinkive.base.util.StringHelper;
import com.thinkive.market.service.conn.TCPReceiver1;
import com.thinkive.market.service.conn.UDPReceiver;
import com.thinkive.market.service.works.WorkerManager;

import org.apache.log4j.Logger;

import java.util.TimeZone;

/**
 * ����: ��Ȩ: Copyright (c) 2014
 * ��˾: ˼�ϿƼ� ����: ����
 * �汾: 1.0
 * ��������: 2015-01-14
 * ����ʱ��: 10:29:38
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
            logger.info("��ǰʱ��[" + TimeZone.getDefault() + "]");

            String mode = Configuration.getString("receiver.mode");

            if ("udp".equalsIgnoreCase(mode)) {
                UDPReceiver udpserver = new UDPReceiver();
                udpserver.start();
                logger.info("UDP���շ���������");
            } else if ("tcp".equalsIgnoreCase(mode)) {
                TCPReceiver1 tcpserver = new TCPReceiver1();
                tcpserver.start();
                logger.info("TCP���շ���������");
            } else {
                logger.warn("���շ��������������������ȡTASK�Ƿ�����");
            }
            WorkerManager workerManager = WorkerManager.getInstance();
            workerManager.start();

        } catch (Exception ex) {
            logger.error("��������������: " + ex.getMessage());
            System.exit(-1);
        }

    }
}
