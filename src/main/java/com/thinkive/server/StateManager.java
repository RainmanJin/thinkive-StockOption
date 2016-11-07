package com.thinkive.server;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ����: Ӧ�ó���ȫ��״̬
 * ��Ȩ: Copyright (c) 2011
 * ��˾: ˼�ϿƼ�
 * ����: ŷ��
 * �汾: 1.0
 * ��������: 2011-9-8
 * ����ʱ��: 10:49:17
 */
public class StateManager {
    //���ķ������Ƿ�����
    private static boolean isCenterServerRuning = false;

    //��ǰ���ӵĿͻ����� key Ϊͨ��
    private static Map<SocketChannel, SocketClient> clientChannelMap = new ConcurrentHashMap<SocketChannel, SocketClient>();

    //��ǰ���ӵĿͻ�����key Ϊͨ����ʶ
    private static Map<String, SocketClient> clientKeyMap = new ConcurrentHashMap<String, SocketClient>();

    /**
     * ���ķ������Ƿ���������
     */
    public static boolean isCenterServerRuning() {
        return isCenterServerRuning;
    }

    /**
     * �������ķ������Ƿ���������
     */
    public static void setCenterServerRuning(boolean isRuning) {
        StateManager.isCenterServerRuning = isRuning;
    }

    /**
     * ��ȡ��ǰ���ӵĿͻ�����
     */
    public static Map<SocketChannel, SocketClient> getClientChannelMap() {
        return clientChannelMap;
    }

    /**
     * ��õ�ǰ���ӵĿͻ�������
     */
    public static int getClientCount() {
        return clientChannelMap.size();
    }

    /**
     * ��ȡ��ǰ���ӵĿͻ�����
     */
    public static Map<String, SocketClient> getClientKeyMap() {
        return clientKeyMap;
    }

}
