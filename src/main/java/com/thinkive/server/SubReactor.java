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
 * ����: �ӷ�ӳ��
 * ��Ȩ: Copyright (c) 2011
 * ��˾: ˼�ϿƼ�
 * ����: ŷ��
 * �汾: 1.0
 * ��������: 2011-3-8
 * ����ʱ��: 10:32:56
 */
public class SubReactor implements Runnable {
    private static Logger logger = Logger.getLogger(SubReactor.class);

    // �¼�ѡ����
    private Selector selector;

    private ConcurrentLinkedQueue readKeyQueue = new ConcurrentLinkedQueue();         // ��Ӧ��

    private ConcurrentLinkedQueue acceptKeyQueue = new ConcurrentLinkedQueue();       // ���տͻ������ӳ�

    // �¼�������
    private ServerListener handler = new ServerHandler();
    // ����Ϊ1M,����������˰�������
    private int MAX_PACKET_SIZE = 1024 * 1024;
    // �������ݻ�����ȱʡ��С
    private int BUFFER_SIZE = 1024 * 4;
    // ͷ����,18�ֽ�
    private int HEAD_LENGTH = 35;

    public SubReactor() throws Exception {
    }

    public void run() {
        try {
            // ��ʼ��Selector����
            selector = Selector.open();
            while (true) {
                try {
                    addAcceptRegister();
                    addRegister();
                    // ������ע����¼�����ʱ,select()����ֵ������0
                    int num = selector.select(1000);
                    if (num > 0) {
                        Set selectedKeys = selector.selectedKeys();
                        Iterator it = selectedKeys.iterator();
                        while (it.hasNext()) {
                            SelectionKey key = (SelectionKey) it.next();
                            it.remove();// ɾ����ǰ��Ҫ�����ѡ���
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
     * ����µ�ͨ��ע��
     */
    private void addAcceptRegister() {
        while (!acceptKeyQueue.isEmpty()) {
            SocketChannel clientChannel = null;
            try {
                clientChannel = (SocketChannel) acceptKeyQueue.poll();
                // �������������¼�
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
     * ����µ�ͨ��ע��
     */
    private void addRegister() {
        while (!readKeyQueue.isEmpty()) {
            SelectionKey key = (SelectionKey) readKeyQueue.poll();
            key.interestOps(SelectionKey.OP_READ);
        }
    }

    /**
     * �ύ�µĿͻ��������������ӷ�ӳ���̵߳����ӳ��� ע�⣺�˷����ᱻ���̷߳���
     */
    public void addAcceptRequest(SocketChannel clientChannel) {
        acceptKeyQueue.add(clientChannel);
        selector.wakeup();
    }

    /**
     * �ύ�µĿͻ��˶��������������̵߳Ļ�Ӧ���� ע�⣺�˷����ᱻ���̷߳���
     */
    public void processReadRequest(SelectionKey key) {
        readKeyQueue.add(key);
        selector.wakeup();
    }

    /**
     * ���ɺʹ����¼�
     */
    private void dispatch(SelectionKey key) {
        try {
            // �����ͨ����׼�����¼�
            if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                doClientReadEvent(key);
            }
        } catch (CancelledKeyException ex) {
        }
    }

    /**
     * ����ͻ����¼�
     */
    private void doClientReadEvent(SelectionKey key) {
        // ȡ�����ϵ�ע��ֵ
        key.interestOps(0);
        processRead(key);

    }

    /**
     * �رտͻ�ͨ��,�÷����ᱻ���̷߳���
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

            // ���key�ϸ��ӵĶ���
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

        // �Ƿ���Ҫ�رտͻ���SOCKET,�����ݲ���ȷ�����Ѿ��������ݣ�����Ҫ�ر�
        boolean needClose = false;
        try {
            do {
                buffer.clear();
                count = clientChannel.read(buffer); // ��ȡ����

                if (count == -1) // �Ѿ��ﵽ����ĩβ
                {
                    needClose = true;
                }

                if (count <= 0) {
                    break;
                }

                //�ɹ���ȡ������,�����ݿ����뻺����
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
                    if (!(packetHead[0] == 'T' && packetHead[1] == 'K')) // ����ǰ��������ַ�����TK�����������
                    {
                        needClose = true;
                        break;
                    }
                    headBuffer.clear();

                    // �ж��Ƿ��Ѿ��յ���ͷ������
                    if (dataBuffer.length < HEAD_LENGTH) {
                        break;
                    }

                    headBuffer = ByteBuffer.wrap(dataBuffer, 0, HEAD_LENGTH);
                    headBuffer.order(ByteOrder.LITTLE_ENDIAN);
                    headBuffer.position(2); // ����ǰ�������ֽ�

                    // ͷ�������Ѿ��յ����������´���
                    int msgVersionNo = headBuffer.getInt(); // ��Ϣ�汾���
                    char msgType = (char) headBuffer.get(); // ��Ϣ�汾���
                    int bodyLength = headBuffer.getInt(); // ���峤��
                    int origbodyLength = headBuffer.getInt(); // ����ԭʼ����
                    short branchID = headBuffer.getShort(); // ��֧��
                    short commandID = headBuffer.getShort(); //���ܺ�
                    int flowNo = headBuffer.getInt(); // ��ˮ��
                    int errorNo = headBuffer.getInt(); // error��
                    headBuffer.get(new byte[8]);//Reserved    Byte(8) �����ֶ�

                    if (bodyLength > MAX_PACKET_SIZE) // ���쳣
                    {
                        needClose = true;
                        break;
                    }

                    // �ж��Ѿ��յ����ݵĳ���
                    int packetLength = bodyLength + HEAD_LENGTH;
                    if (dataBuffer.length < packetLength) // ����û����ȫ�����ܴ���
                    {
                        break;
                    }

                    //����������
                    byte[] msg = new byte[packetLength];
                    System.arraycopy(dataBuffer, 0, msg, 0, packetLength);
                    request.setData(msg);

                    //�����
                    SocketClient client = StateManager.getClientChannelMap().get(clientChannel);
                    if (client != null) {
                        // ���ÿͻ�������ʱ��
                        client.setLastAccessTime(System.currentTimeMillis());
                        //����ͻ������
                        SocketResponse response = new SocketResponse(clientChannel);
                        handler.onDataArrival(request, response);
                    }

                    // ʣ�����ݷ��뻺����
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
            processReadRequest(key); //���¶�ȡ����
        } else {
            closeClient(key); //�ر�ͨ��
        }
    }

    /**
     * ��������
     *
     * @param src  byte[] Դ��������
     * @param size int ���ݵ�������
     * @return byte[] ���ݺ������
     */
    public byte[] grow(byte[] src, int size) {
        byte[] tmp = new byte[src.length + size];
        System.arraycopy(src, 0, tmp, 0, src.length);
        return tmp;
    }

}
