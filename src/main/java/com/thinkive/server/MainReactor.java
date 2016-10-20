package com.thinkive.server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.thinkive.base.config.Configuration;


/**
 * 描述:  主反映器(负责ACCEPT)
 * 版权:   Copyright (c) 2011
 * 公司:   思迪科技
 * 作者:   欧阳
 * 版本:   1.0
 * 创建日期: 2011-3-8
 * 创建时间: 10:32:56
 */
public class MainReactor implements Runnable
{
    private static Logger logger = Logger.getLogger(MainReactor.class);

    //事件选择器
    private Selector selector;
    //服务socket通道
    private ServerSocketChannel svrChannel;
    //服务器监听的端口
    private int port;
    //子反应器
    private List<SubReactor> subReactorList = new ArrayList<SubReactor>();

    //当前反应器计数器
    private int next = 0;

    public MainReactor(int port) throws Exception
    {
        this.port = port;

        int size = Configuration.getInt("server.subReactorSize", 10);
        //初始化子反应器
        for (int i = 0; i < size; i++)
        {
            SubReactor subReactor = new SubReactor();
            subReactorList.add(subReactor);
            new Thread(subReactor).start();
        }
    }

    public void run()
    {
        try
        {
            //初始化Selector对象
            selector = Selector.open();
            //初始化服务器Channel对象
            svrChannel = ServerSocketChannel.open();
            //将服务器Channel设置为非阻塞模式
            svrChannel.configureBlocking(false);
            //把Socket绑定到端口上
            ServerSocket svrSocket = svrChannel.socket();
            svrSocket.bind(new InetSocketAddress(port));
            //把接收缓冲区设为8K
            svrSocket.setReceiveBufferSize(8 * 1024);
            //服务器端通道注册OP_ACCEPT事件
            svrChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true)
            {
                try
                {
                    //当有已注册的事件发生时,select()返回值将大于0
                    int num = selector.select(1000);
                    if (num > 0)
                    {
                        Set selectedKeys = selector.selectedKeys();
                        Iterator it = selectedKeys.iterator();
                        while (it.hasNext())
                        {
                            SelectionKey key = (SelectionKey) it.next();
                            it.remove();//删除当前将要处理的选择键
                            dispatch(key);
                        }
                        selectedKeys.clear();
                    }
                }
                catch (Exception ex)
                {
                    logger.error("", ex);
                }
            }
        }
        catch (Exception ex)
        {
            logger.error("", ex);
        }
    }

    /**
     * 分派和处理事件
     *
     * @param key
     */
    private void dispatch(SelectionKey key)
    {
        //如果是有客户端连接请求
        if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT)
        {
            doClientAcceptEvent(key);
        }
    }

    /**
     * 处理客户接受事件
     *
     * @param key
     */
    private void doClientAcceptEvent(SelectionKey key)
    {
        SocketChannel clientChannel = null;
        int maxConnections = Configuration.getInt("server.maxConnections");
        try
        {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            //获取客户端套接字
            clientChannel = server.accept();

            
            if (StateManager.getClientCount() >= maxConnections)  //已经达到最大连接数,不能再继续连接
            {
                StateLogger.WriteLog("已经达到最大连接数，客户连接被主动关闭。");
                clientChannel.socket().close();
            }
            else
            {
                //把客户端的发送缓效冲区和接收缓冲区设为8K
                clientChannel.socket().setReceiveBufferSize(8 * 1024);
                clientChannel.socket().setSendBufferSize(8 * 1024);
                //设置超时为5秒钟
                clientChannel.socket().setSoTimeout(5000);
                //设置通道为非阻塞模式
                clientChannel.configureBlocking(false);

                //获得通道唯一性标识
                String uniqueKey = getUniqueKey();


                SubReactor subReactor = subReactorList.get(next);
                //把客户加入全局客户列表
                SocketClient client = new SocketClient();
                client.setUniqueKey(uniqueKey);
                client.setSubReactor(subReactor);
                client.setSocketChannel(clientChannel);
                client.setConnTime(System.currentTimeMillis());
                client.setLastAccessTime(System.currentTimeMillis());
                StateManager.getClientChannelMap().put(clientChannel, client);
                StateManager.getClientKeyMap().put(uniqueKey, client);

                //加入当前客户到子反应器
                subReactor.addAcceptRequest(clientChannel);
                next = (next == subReactorList.size() - 1) ? 0 : next;

                StateLogger.WriteLog("通道标识为[" + uniqueKey + "]的客户连接成功");
            }
        }
        catch (Exception ex)
        {
            logger.info("", ex);
            try
            {
                if (clientChannel != null)
                {
                    clientChannel.close();
                }
            }
            catch (Exception e)
            {
            }
        }

    }

    /**
     * 返回和通道关联的唯一性16位KEY
     *
     * @return
     */
    private static String getUniqueKey()
    {
        String key =String.valueOf((long)(Math.random()*1000000000000000L));
        return key;
    }
}
