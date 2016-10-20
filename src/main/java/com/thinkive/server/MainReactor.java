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
 * ����:  ����ӳ��(����ACCEPT)
 * ��Ȩ:   Copyright (c) 2011
 * ��˾:   ˼�ϿƼ�
 * ����:   ŷ��
 * �汾:   1.0
 * ��������: 2011-3-8
 * ����ʱ��: 10:32:56
 */
public class MainReactor implements Runnable
{
    private static Logger logger = Logger.getLogger(MainReactor.class);

    //�¼�ѡ����
    private Selector selector;
    //����socketͨ��
    private ServerSocketChannel svrChannel;
    //�����������Ķ˿�
    private int port;
    //�ӷ�Ӧ��
    private List<SubReactor> subReactorList = new ArrayList<SubReactor>();

    //��ǰ��Ӧ��������
    private int next = 0;

    public MainReactor(int port) throws Exception
    {
        this.port = port;

        int size = Configuration.getInt("server.subReactorSize", 10);
        //��ʼ���ӷ�Ӧ��
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
            //��ʼ��Selector����
            selector = Selector.open();
            //��ʼ��������Channel����
            svrChannel = ServerSocketChannel.open();
            //��������Channel����Ϊ������ģʽ
            svrChannel.configureBlocking(false);
            //��Socket�󶨵��˿���
            ServerSocket svrSocket = svrChannel.socket();
            svrSocket.bind(new InetSocketAddress(port));
            //�ѽ��ջ�������Ϊ8K
            svrSocket.setReceiveBufferSize(8 * 1024);
            //��������ͨ��ע��OP_ACCEPT�¼�
            svrChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true)
            {
                try
                {
                    //������ע����¼�����ʱ,select()����ֵ������0
                    int num = selector.select(1000);
                    if (num > 0)
                    {
                        Set selectedKeys = selector.selectedKeys();
                        Iterator it = selectedKeys.iterator();
                        while (it.hasNext())
                        {
                            SelectionKey key = (SelectionKey) it.next();
                            it.remove();//ɾ����ǰ��Ҫ�����ѡ���
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
     * ���ɺʹ����¼�
     *
     * @param key
     */
    private void dispatch(SelectionKey key)
    {
        //������пͻ�����������
        if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT)
        {
            doClientAcceptEvent(key);
        }
    }

    /**
     * ����ͻ������¼�
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
            //��ȡ�ͻ����׽���
            clientChannel = server.accept();

            
            if (StateManager.getClientCount() >= maxConnections)  //�Ѿ��ﵽ���������,�����ټ�������
            {
                StateLogger.WriteLog("�Ѿ��ﵽ������������ͻ����ӱ������رա�");
                clientChannel.socket().close();
            }
            else
            {
                //�ѿͻ��˵ķ��ͻ�Ч�����ͽ��ջ�������Ϊ8K
                clientChannel.socket().setReceiveBufferSize(8 * 1024);
                clientChannel.socket().setSendBufferSize(8 * 1024);
                //���ó�ʱΪ5����
                clientChannel.socket().setSoTimeout(5000);
                //����ͨ��Ϊ������ģʽ
                clientChannel.configureBlocking(false);

                //���ͨ��Ψһ�Ա�ʶ
                String uniqueKey = getUniqueKey();


                SubReactor subReactor = subReactorList.get(next);
                //�ѿͻ�����ȫ�ֿͻ��б�
                SocketClient client = new SocketClient();
                client.setUniqueKey(uniqueKey);
                client.setSubReactor(subReactor);
                client.setSocketChannel(clientChannel);
                client.setConnTime(System.currentTimeMillis());
                client.setLastAccessTime(System.currentTimeMillis());
                StateManager.getClientChannelMap().put(clientChannel, client);
                StateManager.getClientKeyMap().put(uniqueKey, client);

                //���뵱ǰ�ͻ����ӷ�Ӧ��
                subReactor.addAcceptRequest(clientChannel);
                next = (next == subReactorList.size() - 1) ? 0 : next;

                StateLogger.WriteLog("ͨ����ʶΪ[" + uniqueKey + "]�Ŀͻ����ӳɹ�");
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
     * ���غ�ͨ��������Ψһ��16λKEY
     *
     * @return
     */
    private static String getUniqueKey()
    {
        String key =String.valueOf((long)(Math.random()*1000000000000000L));
        return key;
    }
}
