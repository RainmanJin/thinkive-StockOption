package com.thinkive.server;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述: 应用程序全局状态
 * 版权: Copyright (c) 2011
 * 公司: 思迪科技
 * 作者: 欧阳
 * 版本: 1.0
 * 创建日期: 2011-9-8
 * 创建时间: 10:49:17
 */
public class StateManager {
    //中心服务器是否运行
    private static boolean isCenterServerRuning = false;

    //当前连接的客户集合 key 为通道
    private static Map<SocketChannel, SocketClient> clientChannelMap = new ConcurrentHashMap<SocketChannel, SocketClient>();

    //当前连接的客户集合key 为通道标识
    private static Map<String, SocketClient> clientKeyMap = new ConcurrentHashMap<String, SocketClient>();

    /**
     * 中心服务器是否正在运行
     */
    public static boolean isCenterServerRuning() {
        return isCenterServerRuning;
    }

    /**
     * 设置中心服务器是否正在运行
     */
    public static void setCenterServerRuning(boolean isRuning) {
        StateManager.isCenterServerRuning = isRuning;
    }

    /**
     * 获取当前连接的客户集合
     */
    public static Map<SocketChannel, SocketClient> getClientChannelMap() {
        return clientChannelMap;
    }

    /**
     * 获得当前连接的客户端数量
     */
    public static int getClientCount() {
        return clientChannelMap.size();
    }

    /**
     * 获取当前连接的客户集合
     */
    public static Map<String, SocketClient> getClientKeyMap() {
        return clientKeyMap;
    }

}
