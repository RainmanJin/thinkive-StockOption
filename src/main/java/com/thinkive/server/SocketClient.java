/*
 * Copyright (c) 2007 Your Corporation. All Rights Reserved.
 */

package com.thinkive.server;

import java.net.InetAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

/**
 * 描述:
 * 版权:	 Copyright (c) 2007
 * 公司:	 思迪科技
 * 作者:	 易庆锋
 * 版本:	 1.0
 * 创建日期: 2007-11-24
 * 创建时间: 15:10:32
 */
public class SocketClient
{
    //客户通道
    private SocketChannel channel = null;
    //客户最后的访问时间
    private long lastAccessTime = 0;
    //客户连接的时间
    private long connTime = 0;
    //所在的子反应器上
    private SubReactor subReactor = null;
    //通道标识
    private String uniqueKey = "";


    /**
     * 客户socket封装对象
     *
     * @param
     */
    public SocketClient()
    {
    }

    public void setSocketChannel(SocketChannel channel)
    {
        this.channel = channel;
    }

    /**
     * 返回客户端通道
     *
     * @return
     */
    public SocketChannel getSocketChannel()
    {
        return channel;
    }

    /**
     * 判断客户是否已经关闭
     *
     * @return
     */
    public boolean isClosed()
    {
        if (channel.socket() != null)
        {
            return channel.socket().isClosed();
        }
        return true;
    }

    /**
     * 返回客户的IP地址
     *
     * @return
     */
    public String getIP()
    {
        Socket socket = channel.socket();
        if (socket != null)
        {
            InetAddress address = socket.getInetAddress();
            if (address != null)
            {
                return address.getHostAddress();
            }
        }
        return "";
    }


    public int getPort()
    {
        Socket socket = channel.socket();
        if (socket != null)
        {
            return socket.getPort();
        }
        return 0;
    }

    /**
     * 设置客户最后访问时间
     *
     * @param lastAccessTime
     */
    public void setLastAccessTime(long lastAccessTime)
    {
        this.lastAccessTime = lastAccessTime;
    }

    /**
     * 获得客户最后访问时间
     */
    public long getLastAccessTime()
    {
        return lastAccessTime;
    }

    /**
     * 获得客户连接时间
     *
     * @return
     */
    public long getConnTime()
    {
        return connTime;
    }

    /**
     * 设置客户连接时间
     *
     * @param connTime
     */
    public void setConnTime(long connTime)
    {
        this.connTime = connTime;
    }

    /**
     * 获取和当前客户相关联的子反应器
     *
     * @return
     */
    public SubReactor getSubReactor()
    {
        return subReactor;
    }

    /**
     * 设置和当前客户相关联的子反应器
     *
     * @param subReactor
     */
    public void setSubReactor(SubReactor subReactor)
    {
        this.subReactor = subReactor;
    }

    /**
     * 获取客户唯一性KEY
     *
     * @return
     */
    public String getUniqueKey()
    {
        return uniqueKey;
    }

    /**
     * 设置客户唯一性KEY
     *
     * @param uniqueKey
     */
    public void setUniqueKey(String uniqueKey)
    {
        this.uniqueKey = uniqueKey;
    }


}