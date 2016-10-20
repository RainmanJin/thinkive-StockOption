package com.thinkive.server.inner;

import com.thinkive.base.config.Configuration;
import com.thinkive.server.SocketResponse;
import com.thinkive.server.SocketClient;
import com.thinkive.server.StateLogger;
import com.thinkive.server.StateManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class InnerServer implements Runnable
{
    private static Logger logger = Logger.getLogger(InnerServer.class);
    private static String SOH = new String("\1");
    private static String STX = new String("\2");

    private ConcurrentLinkedQueue readKeyQueue = new ConcurrentLinkedQueue();         // ��Ӧ��
    //�˿�
    private int port = 0;
    //�¼�ѡ����
    private Selector selector = null;
    //����socketͨ��
    private ServerSocketChannel svrChannel = null;
    //����������
    private WorkQueue workQueue;  //�����̶߳���

    public InnerServer(int port)
    {
        this.port = port;
        int poolSize = Configuration.getInt("innerServer.poolSize", 4);
        workQueue = new WorkQueue(poolSize);
    }

    public void run()
    {
        try
        {
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
                    addRegister();
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
                catch (Exception e)
                {
                    logger.error("", e);
                }
            }
        }
        catch (Exception e)
        {
            logger.error("", e);
        }
    }

    /**
     * ���ɺʹ����¼�
     *
     * @param key
     */
    private void dispatch(SelectionKey key)
    {
        try
        {
            //������пͻ�����������
            if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT)
            {
                doClientAcceptEvent(key);
            }
            //�����ͨ����׼�����¼�
            else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ)
            {
                doClientReadEvent(key);
            }
        }
        catch (Exception ex)
        {
            logger.info("", ex);
        }
    }

    /**
     * ����µ�ͨ��ע��
     */
    private void addRegister()
    {
        while (!readKeyQueue.isEmpty())
        {
            SelectionKey key = (SelectionKey) readKeyQueue.poll();
            key.interestOps(SelectionKey.OP_READ);
        }
    }

    /**
     * �ύ�µĿͻ��˶��������������̵߳Ļ�Ӧ���� ע�⣺�˷����ᱻ���̷߳���
     */
    public void processReadRequest(SelectionKey key)
    {
        readKeyQueue.add(key);
        selector.wakeup();
    }

    /**
     * ����ͻ������¼�
     *
     * @param key
     */
    private void doClientAcceptEvent(SelectionKey key)
    {
        SocketChannel clientChannel = null;
        try
        {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            //��ȡ�ͻ����׽���
            clientChannel = server.accept();

            //�ѿͻ��˵ķ��ͻ�Ч�����ͽ��ջ�������Ϊ8K
            clientChannel.socket().setReceiveBufferSize(8 * 1024);
            clientChannel.socket().setSendBufferSize(8 * 1024);
            //���ó�ʱΪ5����
            clientChannel.socket().setSoTimeout(5000);

            //����ͨ��Ϊ������ģʽ
            clientChannel.configureBlocking(false);

            Message message = new Message();
            //ע�����Ȥ�¼������������������
            clientChannel.register(selector, SelectionKey.OP_READ, message);
        }
        catch (Exception e)
        {
            logger.info("", e);
            try
            {
                if (clientChannel != null)
                {
                    clientChannel.close();
                }
            }
            catch (Exception ex)
            {
            }
        }
    }

    /**
     * ����ͻ����¼�
     *
     * @param key
     */
    private void doClientReadEvent(SelectionKey key)
    {
        //ȡ�����ϵ�ע��ֵ
        key.interestOps(0);
        //�Ѷ�������빤������
        workQueue.processRequest(key);
    }


    /**
     * �رտͻ�ͨ��,�÷����ᱻ���̷߳���
     *
     * @param key
     */
    public void closeClient(SelectionKey key)
    {
        try
        {
            SocketChannel clientChannel = (SocketChannel) key.channel();
            Message message = (Message) key.attachment();
            message.clear();
            key.attach(null);
            clientChannel.socket().setSoLinger(true, 0);
            clientChannel.socket().close();
            clientChannel.close();
        }
        catch (Exception ex)
        {
        }
    }

    /**
     * �������
     */
    class WorkQueue
    {
        //�������
        private final LinkedBlockingQueue taskQueue = new LinkedBlockingQueue();
        //�̳߳�����
        private List threadPool = new ArrayList();
        //�̳߳صĴ�С
        private int poolSize = 0;

        /**
         * ��ʼ���������̳߳�
         *
         * @param poolSize ����̳߳ش�С
         */
        public WorkQueue(int poolSize)
        {
            this.poolSize = poolSize;
            //��ʼ���߳�
            for (int i = 0; i < poolSize; i++)
            {
                Thread thread = new PoolThread();
                thread.start();
                threadPool.add(thread);
            }
        }


        /**
         * ����ͻ�����,�����û��������,�����Ѷ����е��߳̽��д���
         */
        public void processRequest(SelectionKey key)
        {
            //�������󵽶�����
            taskQueue.add(key);
        }


        /**
         * ��õ�ǰ�ȴ������е���������
         *
         * @return
         */
        public int getWaitQueueCount()
        {
            return taskQueue.size();
        }

        /**
         * ����̵߳�����
         *
         * @return
         */
        public int getCurrentPoolSize()
        {
            return threadPool.size();
        }

        /**
         * ��õ�ǰ����̵߳�����
         *
         * @return
         */
        public int getActiveThreadCount()
        {
            int count = 0;
            Iterator iter = threadPool.iterator();
            while (iter.hasNext())
            {
                PoolThread thread = (PoolThread) iter.next();
                if (thread.isRunning())
                {
                    ++count;
                }
            }
            return count;
        }

        /**
         * ���ش����װ̬���̵߳�����
         *
         * @return
         */
        public int getNotActiveThreadCount()
        {
            return threadPool.size() - getActiveThreadCount();
        }

        /**
         * �ڲ������߳�
         */
        private class PoolThread extends Thread
        {
            private boolean isRunning = false;

            /**
             * �ж��߳��Ƿ���������
             *
             * @return
             */
            public boolean isRunning()
            {
                return isRunning;
            }


            /**
             * �߳�ִ������
             */
            public void run()
            {
                while (true)
                {
                    try
                    {
                        isRunning = false;
                        SelectionKey key = (SelectionKey) taskQueue.take();
                        isRunning = true;
                        //��ȡ������ͻ�������
                        process(key);
                    }
                    catch (InterruptedException ex)
                    {
                    }
                    catch (Exception e)
                    {
                    }
                }
            }

            // ����Ϊ1M,����������˰�������
            private int MAX_PACKET_SIZE = 1024 * 1024;
            //�������ݻ�����ȱʡ��С
            private int BUFFER_SIZE = 1024 * 4;
            //ͷ����,18�ֽ�
            private int HEAD_LENGTH = 18;
            //ͨ����ʶ����
            private int KEY_LENGTH = 16;

            private void process(SelectionKey key)
            {
                SocketChannel clientChannel = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                int count = 0;

                //�Ƿ���Ҫ�رտͻ���SOCKET,�����ݲ���ȷ�����Ѿ��������ݣ�����Ҫ�ر�
                boolean needClose = false;

                try
                {
                    do
                    {
                        buffer.clear();
                        count = clientChannel.read(buffer); //��ȡ����

                        if (count == -1) //�Ѿ��ﵽ����ĩβ
                        {
                            needClose = true;
                        }

                        if (count <= 0)
                        {
                            break;
                        }

                        //�ɹ���ȡ������,�����ݿ����뻺����
                        Message message = (Message) key.attachment();
                        byte[] dataBuffer = message.getBuffer();
                        dataBuffer = grow(dataBuffer, count);
                        System.arraycopy(buffer.array(), 0, dataBuffer, dataBuffer.length - count, count);
                        buffer.clear();
                        message.setBuffer(dataBuffer);


                        for (; ;)
                        {
                            dataBuffer = message.getBuffer();

                            if (dataBuffer.length < 2)
                            {
                                break;
                            }

                            ByteBuffer headBuffer = ByteBuffer.wrap(dataBuffer, 0, 2);
                            headBuffer.order(ByteOrder.LITTLE_ENDIAN);
                            byte[] packetHead = new byte[2];
                            headBuffer.get(packetHead);
                            if (!(packetHead[0] == 'T' && packetHead[1] == 'K')) //����ǰ��������ַ�����TK�����������
                            {
                                needClose = true;
                                break;
                            }
                            headBuffer.clear();

                            //�ж��Ƿ��Ѿ��յ���ͷ������
                            if (dataBuffer.length < HEAD_LENGTH)
                            {
                                break;
                            }

                            headBuffer = ByteBuffer.wrap(dataBuffer, 0, 18);
                            headBuffer.order(ByteOrder.LITTLE_ENDIAN);
                            headBuffer.position(2); //����ǰ�������ֽ�

                            //ͷ�������Ѿ��յ����������´���
                            int msgTypeNo = headBuffer.getInt(); //��Ϣ����
                            int msgVersionNo = headBuffer.getInt();  //��Ϣ�汾
                            int keepField = headBuffer.getInt(); //�����ֶ�
                            int bodyLength = headBuffer.getInt(); //�õ������ݳ���
                            if (bodyLength > MAX_PACKET_SIZE) //���쳣
                            {
                                needClose = true;
                                break;
                            }

                            //�ж��Ѿ��յ����ݵĳ���
                            int packetLength = bodyLength + HEAD_LENGTH + KEY_LENGTH; //�������16λ��KEY
                            if (dataBuffer.length < packetLength) //����û����ȫ�����ܴ���
                            {
                                break;
                            }

                            boolean bResult = writeMessageToClient(dataBuffer, packetLength);

                            try
                            {
                                String uniqueKey = new String(dataBuffer, packetLength - KEY_LENGTH, KEY_LENGTH);
                                if (bResult)
                                {
                                    StateLogger.WriteLog("������Ϣ��ͨ��[" + uniqueKey + "]�ͻ��ɹ�,������IDΪ["
                                            + msgTypeNo + "],���峤��Ϊ[" + bodyLength + "]");
                                }
                                else
                                {
                                    StateLogger.WriteLog("������Ϣ��ͨ��[" + uniqueKey + "]�ͻ�ʧ��,������IDΪ["
                                            + msgTypeNo + "]");
                                }
                            }
                            catch (Exception ex)
                            {
                            }

                            //ʣ�����ݼ������뻺����
                            byte[] tempBuffer = new byte[dataBuffer.length - packetLength];
                            System.arraycopy(dataBuffer, packetLength, tempBuffer, 0, dataBuffer.length - packetLength);
                            message.setBuffer(tempBuffer);

                        } //for(;;)
                    }
                    while (false);
                }
                catch (ClosedChannelException ex)
                {
                    needClose = true;
                }
                catch (IOException ex)
                {
                    needClose = true;
                }
                catch (Exception ex)
                {
                    needClose = true;
                    logger.info("", ex);
                }

                if (!needClose)
                {
                    processReadRequest(key); //���¶�ȡ����
                }
                else
                {
                    closeClient(key); //�ر�ͨ��
                }
            }

            /**
             * д��Ӧ�����ͻ���
             *
             * @param dataBuffer
             * @param packetLength
             * @return
             */
            private boolean writeMessageToClient(byte[] dataBuffer, int packetLength)
            {

                String uniqueKey = new String(dataBuffer, packetLength - KEY_LENGTH, KEY_LENGTH);
                try
                {
                    SocketClient socketClient = StateManager.getClientKeyMap().get(uniqueKey);
                    if (socketClient != null)
                    {
                        SocketResponse response = new SocketResponse(socketClient.getSocketChannel());

                        ByteBuffer buffer = ByteBuffer.wrap(dataBuffer, 0, packetLength - KEY_LENGTH);
                        response.write(buffer);

                        return true;
                    }
                }
                catch (Exception e)
                {
                    logger.info("", e);
                }
                return false;
            }

            /**
             * ��������
             *
             * @param src  byte[] Դ��������
             * @param size int ���ݵ�������
             * @return byte[] ���ݺ������
             */
            public byte[] grow(byte[] src, int size)
            {
                byte[] tmp = new byte[src.length + size];
                System.arraycopy(src, 0, tmp, 0, src.length);
                return tmp;
            }
        }
    }

}
