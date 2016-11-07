package com.thinkive.server;

import com.thinkive.server.event.ServerListener;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 描述: 子反映器
 * 版权: Copyright (c) 2011
 * 公司: 思迪科技
 * 作者: 欧阳
 * 版本: 1.0
 * 创建日期: 2011-3-8
 * 创建时间: 10:32:56
 */
public class SubReactor implements Runnable {
    private static Logger logger = Logger.getLogger(SubReactor.class);

    // 事件选择器
    private Selector selector;

    private ConcurrentLinkedQueue readKeyQueue = new ConcurrentLinkedQueue();         // 回应池

    private ConcurrentLinkedQueue acceptKeyQueue = new ConcurrentLinkedQueue();       // 接收客户端连接池

    // 事件处理器
    private ServerListener handler = new ServerHandler();
    // 最大包为1M,若超过，则此包不正常
    private int MAX_PACKET_SIZE = 1024 * 1024;
    // 接收数据缓冲区缺省大小
    private int BUFFER_SIZE = 1024 * 4;
    // 头长度,18字节
    private int HEAD_LENGTH = 35;

    public SubReactor() throws Exception {
    }

    public void run() {
        try {
            // 初始化Selector对象
            selector = Selector.open();
            while (true) {
                try {
                    addAcceptRegister();
                    addRegister();
                    // 当有已注册的事件发生时,select()返回值将大于0
                    int num = selector.select(1000);
                    if (num > 0) {
                        Set selectedKeys = selector.selectedKeys();
                        Iterator it = selectedKeys.iterator();
                        while (it.hasNext()) {
                            SelectionKey key = (SelectionKey) it.next();
                            it.remove();// 删除当前将要处理的选择键
                            dispatch(key);
                        }
                        selectedKeys.clear();
                    }
                } catch (Exception e) {
                    logger.info("", e);
                }
            }
        } catch (Exception e) {
            logger.info("", e);
        }
    }

    /**
     * 添加新的通道注册
     */
    private void addAcceptRegister() {
        while (!acceptKeyQueue.isEmpty()) {
            SocketChannel clientChannel = null;
            try {
                clientChannel = (SocketChannel) acceptKeyQueue.poll();
                // 触发接受连接事件
                SocketRequest request = new SocketRequest(clientChannel);
                clientChannel.register(selector, SelectionKey.OP_READ, request);
            } catch (Exception ex) {
                logger.info("", ex);
                try {
                    if (clientChannel != null) {
                        clientChannel.close();
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 添加新的通道注册
     */
    private void addRegister() {
        while (!readKeyQueue.isEmpty()) {
            SelectionKey key = (SelectionKey) readKeyQueue.poll();
            key.interestOps(SelectionKey.OP_READ);
        }
    }

    /**
     * 提交新的客户端连接请求于子反映器线程的连接池中 注意：此方法会被多线程访问
     */
    public void addAcceptRequest(SocketChannel clientChannel) {
        acceptKeyQueue.add(clientChannel);
        selector.wakeup();
    }

    /**
     * 提交新的客户端读请求于主服务线程的回应池中 注意：此方法会被多线程访问
     */
    public void processReadRequest(SelectionKey key) {
        readKeyQueue.add(key);
        selector.wakeup();
    }

    /**
     * 分派和处理事件
     */
    private void dispatch(SelectionKey key) {
        try {
            // 如果是通道读准备好事件
            if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                doClientReadEvent(key);
            }
        } catch (CancelledKeyException ex) {
        }
    }

    /**
     * 处理客户读事件
     */
    private void doClientReadEvent(SelectionKey key) {
        // 取消键上的注册值
        key.interestOps(0);
        processRead(key);

    }

    /**
     * 关闭客户通道,该方法会被多线程访问
     */
    public void closeClient(SelectionKey key) {
        try {
            SocketChannel clientChannel = (SocketChannel) key.channel();
            SocketRequest request = (SocketRequest) key.attachment();
            handler.onClosed(request);

            SocketClient client = StateManager.getClientChannelMap().get(clientChannel);
            if (client != null) {
                StateManager.getClientKeyMap().remove(client.getUniqueKey());
                StateManager.getClientChannelMap().remove(clientChannel);
            }

            // 清除key上附加的对象
            key.attach(null);
            request.clearBuffer();

//            clientChannel.socket().setSoLinger(true, 1);
            clientChannel.socket().shutdownInput();
            clientChannel.socket().shutdownOutput();
            clientChannel.socket().close();
            clientChannel.close();
        } catch (Exception ex) {
        }
    }

    private void processRead(SelectionKey key) {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        int count = 0;

        // 是否需要关闭客户端SOCKET,若数据不正确或者已经读完数据，则需要关闭
        boolean needClose = false;
        try {
            do {
                buffer.clear();
                count = clientChannel.read(buffer); // 读取数据

                if (count == -1) // 已经达到流的末尾
                {
                    needClose = true;
                }

                if (count <= 0) {
                    break;
                }

                //成功读取了数据,把数据拷贝入缓冲区
                SocketRequest request = (SocketRequest) key.attachment();
                byte[] dataBuffer = request.getBuffer();
                dataBuffer = grow(dataBuffer, count);
                System.arraycopy(buffer.array(), 0, dataBuffer, dataBuffer.length - count, count);
                buffer.clear();
                request.setBuffer(dataBuffer);


                for (; ; ) {
                    dataBuffer = request.getBuffer();

                    if (dataBuffer.length < 2) {
                        break;
                    }

                    ByteBuffer headBuffer = ByteBuffer.wrap(dataBuffer, 0, 2);
                    headBuffer.order(ByteOrder.LITTLE_ENDIAN);
                    byte[] packetHead = new byte[2];
                    headBuffer.get(packetHead);
                    if (!(packetHead[0] == 'T' && packetHead[1] == 'K')) // 若最前面的两个字符不是TK，则包不正常
                    {
                        needClose = true;
                        break;
                    }
                    headBuffer.clear();

                    // 判断是否已经收到了头部数据
                    if (dataBuffer.length < HEAD_LENGTH) {
                        break;
                    }

                    headBuffer = ByteBuffer.wrap(dataBuffer, 0, HEAD_LENGTH);
                    headBuffer.order(ByteOrder.LITTLE_ENDIAN);
                    headBuffer.position(2); // 跳过前面两个字节

                    // 头部数据已经收到，继续往下处理
                    int msgVersionNo = headBuffer.getInt(); // 消息版本编号
                    char msgType = (char) headBuffer.get(); // 消息版本编号
                    int bodyLength = headBuffer.getInt(); // 包体长度
                    int origbodyLength = headBuffer.getInt(); // 包体原始长度
                    short branchID = headBuffer.getShort(); // 分支号
                    short commandID = headBuffer.getShort(); //功能号
                    int flowNo = headBuffer.getInt(); // 流水号
                    int errorNo = headBuffer.getInt(); // error号
                    headBuffer.get(new byte[8]);//Reserved    Byte(8) 保留字段

                    if (bodyLength > MAX_PACKET_SIZE) // 包异常
                    {
                        needClose = true;
                        break;
                    }

                    // 判断已经收的数据的长度
                    int packetLength = bodyLength + HEAD_LENGTH;
                    if (dataBuffer.length < packetLength) // 包还没有收全，不能处理
                    {
                        break;
                    }

                    //拷贝包数据
                    byte[] msg = new byte[packetLength];
                    System.arraycopy(dataBuffer, 0, msg, 0, packetLength);
                    request.setData(msg);

                    //处理包
                    SocketClient client = StateManager.getClientChannelMap().get(clientChannel);
                    if (client != null) {
                        // 设置客户最后访问时间
                        client.setLastAccessTime(System.currentTimeMillis());
                        //处理客户请求包
                        SocketResponse response = new SocketResponse(clientChannel);
                        handler.onDataArrival(request, response);
                    }

                    // 剩余数据放入缓冲区
                    byte[] tempBuffer = new byte[dataBuffer.length - packetLength];
                    System.arraycopy(dataBuffer, packetLength, tempBuffer, 0, dataBuffer.length - packetLength);
                    request.setBuffer(tempBuffer);

                } //for(;;)
            }
            while (false);
        } catch (ClosedChannelException ex) {
            needClose = true;
        } catch (IOException ex) {
            needClose = true;
        } catch (Exception ex) {
            needClose = true;
            logger.info("", ex);
        }

        if (!needClose) {
            processReadRequest(key); //重新读取数据
        } else {
            closeClient(key); //关闭通道
        }
    }

    /**
     * 数组扩容
     *
     * @param src  byte[] 源数组数据
     * @param size int 扩容的增加量
     * @return byte[] 扩容后的数组
     */
    public byte[] grow(byte[] src, int size) {
        byte[] tmp = new byte[src.length + size];
        System.arraycopy(src, 0, tmp, 0, src.length);
        return tmp;
    }

}
