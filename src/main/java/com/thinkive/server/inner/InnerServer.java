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

    private ConcurrentLinkedQueue readKeyQueue = new ConcurrentLinkedQueue();         // 回应池
    //端口
    private int port = 0;
    //事件选择器
    private Selector selector = null;
    //服务socket通道
    private ServerSocketChannel svrChannel = null;
    //服务工作队列
    private WorkQueue workQueue;  //工作线程队列

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
                    addRegister();
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
     * 分派和处理事件
     *
     * @param key
     */
    private void dispatch(SelectionKey key)
    {
        try
        {
            //如果是有客户端连接请求
            if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT)
            {
                doClientAcceptEvent(key);
            }
            //如果是通道读准备好事件
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
     * 添加新的通道注册
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
     * 提交新的客户端读请求于主服务线程的回应池中 注意：此方法会被多线程访问
     */
    public void processReadRequest(SelectionKey key)
    {
        readKeyQueue.add(key);
        selector.wakeup();
    }

    /**
     * 处理客户接受事件
     *
     * @param key
     */
    private void doClientAcceptEvent(SelectionKey key)
    {
        SocketChannel clientChannel = null;
        try
        {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            //获取客户端套接字
            clientChannel = server.accept();

            //把客户端的发送缓效冲区和接收缓冲区设为8K
            clientChannel.socket().setReceiveBufferSize(8 * 1024);
            clientChannel.socket().setSendBufferSize(8 * 1024);
            //设置超时为5秒钟
            clientChannel.socket().setSoTimeout(5000);

            //设置通道为非阻塞模式
            clientChannel.configureBlocking(false);

            Message message = new Message();
            //注册感兴趣事件并和请求对象建立关联
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
     * 处理客户读事件
     *
     * @param key
     */
    private void doClientReadEvent(SelectionKey key)
    {
        //取消键上的注册值
        key.interestOps(0);
        //把读请求放入工作对列
        workQueue.processRequest(key);
    }


    /**
     * 关闭客户通道,该方法会被多线程访问
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
     * 任务对列
     */
    class WorkQueue
    {
        //任务队列
        private final LinkedBlockingQueue taskQueue = new LinkedBlockingQueue();
        //线程池数组
        private List threadPool = new ArrayList();
        //线程池的大小
        private int poolSize = 0;

        /**
         * 初始化读服务线程池
         *
         * @param poolSize 最大线程池大小
         */
        public WorkQueue(int poolSize)
        {
            this.poolSize = poolSize;
            //初始化线程
            for (int i = 0; i < poolSize; i++)
            {
                Thread thread = new PoolThread();
                thread.start();
                threadPool.add(thread);
            }
        }


        /**
         * 处理客户请求,管理用户的联结池,并唤醒队列中的线程进行处理
         */
        public void processRequest(SelectionKey key)
        {
            //加入请求到队列中
            taskQueue.add(key);
        }


        /**
         * 获得当前等待队列中的任务数量
         *
         * @return
         */
        public int getWaitQueueCount()
        {
            return taskQueue.size();
        }

        /**
         * 获得线程的总数
         *
         * @return
         */
        public int getCurrentPoolSize()
        {
            return threadPool.size();
        }

        /**
         * 获得当前活动的线程的数量
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
         * 返回处理不活动装态的线程的数量
         *
         * @return
         */
        public int getNotActiveThreadCount()
        {
            return threadPool.size() - getActiveThreadCount();
        }

        /**
         * 内部工作线程
         */
        private class PoolThread extends Thread
        {
            private boolean isRunning = false;

            /**
             * 判断线程是否正在运行
             *
             * @return
             */
            public boolean isRunning()
            {
                return isRunning;
            }


            /**
             * 线程执行主体
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
                        //读取并处理客户端数据
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

            // 最大包为1M,若超过，则此包不正常
            private int MAX_PACKET_SIZE = 1024 * 1024;
            //接收数据缓冲区缺省大小
            private int BUFFER_SIZE = 1024 * 4;
            //头长度,18字节
            private int HEAD_LENGTH = 18;
            //通道标识长度
            private int KEY_LENGTH = 16;

            private void process(SelectionKey key)
            {
                SocketChannel clientChannel = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                int count = 0;

                //是否需要关闭客户端SOCKET,若数据不正确或者已经读完数据，则需要关闭
                boolean needClose = false;

                try
                {
                    do
                    {
                        buffer.clear();
                        count = clientChannel.read(buffer); //读取数据

                        if (count == -1) //已经达到流的末尾
                        {
                            needClose = true;
                        }

                        if (count <= 0)
                        {
                            break;
                        }

                        //成功读取了数据,把数据拷贝入缓冲区
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
                            if (!(packetHead[0] == 'T' && packetHead[1] == 'K')) //若最前面的两个字符不是TK，则包不正常
                            {
                                needClose = true;
                                break;
                            }
                            headBuffer.clear();

                            //判断是否已经收到了头部数据
                            if (dataBuffer.length < HEAD_LENGTH)
                            {
                                break;
                            }

                            headBuffer = ByteBuffer.wrap(dataBuffer, 0, 18);
                            headBuffer.order(ByteOrder.LITTLE_ENDIAN);
                            headBuffer.position(2); //跳过前面两个字节

                            //头部数据已经收到，继续往下处理
                            int msgTypeNo = headBuffer.getInt(); //消息类型
                            int msgVersionNo = headBuffer.getInt();  //消息版本
                            int keepField = headBuffer.getInt(); //保留字段
                            int bodyLength = headBuffer.getInt(); //得到包数据长度
                            if (bodyLength > MAX_PACKET_SIZE) //包异常
                            {
                                needClose = true;
                                break;
                            }

                            //判断已经收的数据的长度
                            int packetLength = bodyLength + HEAD_LENGTH + KEY_LENGTH; //后面多了16位的KEY
                            if (dataBuffer.length < packetLength) //包还没有收全，不能处理
                            {
                                break;
                            }

                            boolean bResult = writeMessageToClient(dataBuffer, packetLength);

                            try
                            {
                                String uniqueKey = new String(dataBuffer, packetLength - KEY_LENGTH, KEY_LENGTH);
                                if (bResult)
                                {
                                    StateLogger.WriteLog("发送消息给通道[" + uniqueKey + "]客户成功,包类型ID为["
                                            + msgTypeNo + "],包体长度为[" + bodyLength + "]");
                                }
                                else
                                {
                                    StateLogger.WriteLog("发送消息给通道[" + uniqueKey + "]客户失败,包类型ID为["
                                            + msgTypeNo + "]");
                                }
                            }
                            catch (Exception ex)
                            {
                            }

                            //剩余数据继续放入缓冲区
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
                    processReadRequest(key); //重新读取数据
                }
                else
                {
                    closeClient(key); //关闭通道
                }
            }

            /**
             * 写响应包到客户端
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
             * 数组扩容
             *
             * @param src  byte[] 源数组数据
             * @param size int 扩容的增加量
             * @return byte[] 扩容后的数组
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
