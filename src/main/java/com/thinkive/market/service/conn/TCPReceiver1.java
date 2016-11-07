package com.thinkive.market.service.conn;

import com.thinkive.market.service.cache.ConnAddressManager;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class TCPReceiver1 extends Receiver {
    private static final int HEAD_LENGTH = 16;

    private static Logger logger = Logger.getLogger(TCPReceiver1.class);

    public TCPReceiver1() {
        setName("TCPReceiver1");
        DispathWorker1 worker = new DispathWorker1();
        worker.start();
    }

    public void run() {
        while (true) {
            SocketChannel channel = null;
            try {
                // 当前连接的服务器地址 */
                SocketAddress target = ConnAddressManager.getPush_currentAddress();
                if (target == null) {
                    Thread.sleep(1000);
                    continue;
                }


                // 获得一个Socket通道
                channel = SocketChannel.open();
                channel.configureBlocking(false);
                channel.connect(target);

                Selector selector = Selector.open();
                // 注册
                channel.register(selector, SelectionKey.OP_CONNECT);
                // 轮询访问selector
                while (true) {
                    selector.select();
                    // 获得selector中选中的项的迭代器
                    Iterator ite = selector.selectedKeys().iterator();
                    while (ite.hasNext()) {
                        SelectionKey key = (SelectionKey) ite.next();
                        // 删除已选的key,以防重复处理
                        ite.remove();
                        // 连接事件发生
                        if (key.isConnectable()) {
                            SocketChannel sc = (SocketChannel) key.channel();
                            // 如果正在连接，则完成连接
                            if (sc.isConnectionPending()) {
                                sc.finishConnect();
                            }
                            // 设置成非阻塞
                            sc.configureBlocking(false);

                            int version = 0;
                            int msgType = 2;// 2表示订阅推送信息
                            ByteBuffer byteBuffer = ByteBuffer.allocate(18);
                            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                            byteBuffer.put((byte) 'T').put((byte) 'K');
                            byteBuffer.putInt(msgType);
                            byteBuffer.putInt(version);
                            byteBuffer.putInt(0);// 备用
                            byteBuffer.putInt(0);// 内容长度
                            // byteBuffer.put(new byte[0]);
                            byteBuffer.flip();

                            sc.write(byteBuffer);
                            // 在和服务端连接成功之后，为了可以接收到服务端的信息，需要给通道设置读的权限。
                            sc.register(selector, SelectionKey.OP_READ);
                            // 获得了可读的事件
                        } else if (key.isReadable()) {
                            SocketChannel sc = (SocketChannel) key.channel();

                            ByteBuffer headBuffer = ByteBuffer.allocate(4);
                            headBuffer.order(ByteOrder.LITTLE_ENDIAN);
                            readFixedLenToBuffer(sc, headBuffer);
                            // lim=4
                            // cap=4]
                            headBuffer.flip();

                            int length = headBuffer.getInt();// (length=0就是心跳包)
                            // 数据长度：宽度4个字节，是消息总长度，包括消息头及消息体。

                            if (length > 16) {
                                ByteBuffer dataBuffer = ByteBuffer.allocate(length);

                                dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
                                dataBuffer.putInt(length);


                                readFixedLenToBuffer(sc, dataBuffer);
                                dataBuffer.flip();
                                // 加入长度判断，用来区分心跳包，长度为0是心跳检测。

                                if (length > 16) {
                                    queue.offer(dataBuffer.array());
                                    count++;
                                }
                            } else {
                                //logger.warn("~~~~~~~~~~~是心跳包~~~~~~~~~~~~~");
                            }
                        }
                    }
                }

            } catch (Exception ie) {
                logger.warn("TCP连接失败", ie);
            } finally {
                if (channel != null) {
                    try {
                        channel.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void readFixedLenToBuffer(SocketChannel sc, ByteBuffer buffer) throws Exception {
        int count = 0;
        int bufLength = buffer.remaining();
        int readLength = 0;
        do {
            count = sc.read(buffer);
            if (count == -1) // 已经到达末尾
            {
                throw new Exception("read data wrong");
            }

            readLength += count;

            if (readLength == bufLength) // 已经读取完，则返回
            {
                return;
            }
        }
        while (true);
    }
}
