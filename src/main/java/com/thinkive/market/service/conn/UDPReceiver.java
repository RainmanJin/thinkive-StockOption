package com.thinkive.market.service.conn;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.thinkive.base.config.Configuration;

/**
 * @描述: UPD服务器用于接受接受器送来的数据
 * @版权: Copyright (c) 2012 
 * @公司: 思迪科技 
 * @作者: 岳知之
 * @版本: 1.0 
 * @创建日期: 2012-4-28 
 * @创建时间: 下午5:01:29
 */
public class UDPReceiver extends Receiver
{
    private static Logger   logger      = Logger.getLogger(UDPReceiver.class);
    
    private int             port        = Configuration.getInt("receiver.port");                                   ;
    
    private static int      BUFFER_SIZE = 1024 * 4;
    
    private static String[] allowIP     = Configuration.getString("receiver.allowedip", "127.0.0.1").split("\\|");
    
    public UDPReceiver()
    {
        setName("UDPReceiver");
        DispathWorker1 worker = new DispathWorker1();
        worker.start();
    }
    
    public UDPReceiver(Thread worker)
    {
        setName("UDPReceiver");
        worker.start();
    }
    
    public void run()
    {
        try
        {
            //建立
            DatagramChannel dc = DatagramChannel.open();
            dc.configureBlocking(false);
            
            SocketAddress address = new InetSocketAddress(port);
            //本地绑定端口
            DatagramSocket ds = dc.socket();
            ds.bind(address);
            
            //注册
            Selector selector = Selector.open();
            dc.register(selector, SelectionKey.OP_READ);
            
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            
            while (true)
            {
                int num = selector.select();
                //如果选择器数目为0，则结束循环
                if ( num == 0 )
                {
                    continue;
                }
                //得到选择键列表
                Set Keys = selector.selectedKeys();
                Iterator it = Keys.iterator();
                
                while (it.hasNext())
                {
                    SelectionKey k = (SelectionKey) it.next();
                    if ( (k.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ )
                    {
                        DatagramChannel cc = (DatagramChannel) k.channel();
                        //接收数据并读到buffer中
                        buffer.clear();
                        SocketAddress client = cc.receive(buffer);
                        if ( client != null && client instanceof InetSocketAddress
                                && isAllow(((InetSocketAddress) client).getAddress().getHostAddress()) )
                        {
                            buffer.flip();
                            if ( buffer.remaining() <= 0 )
                            {
                                logger.info("udp buffer remaining length <= 0");
                            }
                            byte[] data = new byte[buffer.remaining()];
                            buffer.get(data);
                            if ( isReceived() )
                            {
                                queue.offer(data);
                                count++;
                            }
                        }
                    }
                }
                Keys.clear();
            }
            
        }
        catch (Exception ie)
        {
            logger.warn("UDP接收失败", ie);
        }
    }
    
    /**
     * @描述：允许的IP
     * @作者：岳知之
     * @时间：2013-4-10 下午3:05:46
     * @param ip
     * @return
     */
    public static boolean isAllow(String ip)
    {
        for (int i = 0; i < allowIP.length; i++)
        {
            if ( allowIP[i].equals(ip) )
            {
                return true;
            }
        }
        return false;
    }
}
