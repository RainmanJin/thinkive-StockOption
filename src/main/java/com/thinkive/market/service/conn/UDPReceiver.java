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
 * @����: UPD���������ڽ��ܽ���������������
 * @��Ȩ: Copyright (c) 2012 
 * @��˾: ˼�ϿƼ� 
 * @����: ��֪֮
 * @�汾: 1.0 
 * @��������: 2012-4-28 
 * @����ʱ��: ����5:01:29
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
            //����
            DatagramChannel dc = DatagramChannel.open();
            dc.configureBlocking(false);
            
            SocketAddress address = new InetSocketAddress(port);
            //���ذ󶨶˿�
            DatagramSocket ds = dc.socket();
            ds.bind(address);
            
            //ע��
            Selector selector = Selector.open();
            dc.register(selector, SelectionKey.OP_READ);
            
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            
            while (true)
            {
                int num = selector.select();
                //���ѡ������ĿΪ0�������ѭ��
                if ( num == 0 )
                {
                    continue;
                }
                //�õ�ѡ����б�
                Set Keys = selector.selectedKeys();
                Iterator it = Keys.iterator();
                
                while (it.hasNext())
                {
                    SelectionKey k = (SelectionKey) it.next();
                    if ( (k.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ )
                    {
                        DatagramChannel cc = (DatagramChannel) k.channel();
                        //�������ݲ�����buffer��
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
            logger.warn("UDP����ʧ��", ie);
        }
    }
    
    /**
     * @�����������IP
     * @���ߣ���֪֮
     * @ʱ�䣺2013-4-10 ����3:05:46
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
