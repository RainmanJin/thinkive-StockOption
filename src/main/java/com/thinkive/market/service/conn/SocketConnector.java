package com.thinkive.market.service.conn;

import com.thinkive.base.config.Configuration;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @����: socket������
 * @��Ȩ: Copyright (c) 2012
 * @��˾: ˼�ϿƼ�
 * @����: ��֪֮
 * @�汾: 1.0
 * @��������: 2012-3-28
 * @����ʱ��: ����2:21:36
 */
public class SocketConnector {

    private static final int HEAD_LENGTH = 18;
    private static final int TIMEOUT = Configuration.getInt("thinkconv.timeout", 30000);
    private static Logger logger = Logger.getLogger(SocketConnector.class);
    private int MAX_PACKET_SIZE = 6 * 1024 * 1024;
    private Socket socket = new Socket();

    private InetSocketAddress socketAddress;

    private OutputStream os = null;

    private InputStream is = null;

    public SocketConnector(InetSocketAddress socketAddress) throws IOException {
        this.socketAddress = socketAddress;

    }

    /**
     * @��������������
     * @���ߣ���֪֮
     * @ʱ�䣺2012-3-28 ����2:22:57
     */
    public void connect() throws IOException {
        if (!socket.isConnected()) {
            this.socket.setSoTimeout(TIMEOUT);
            this.socket.connect(socketAddress);
            if (logger.isDebugEnabled()) {
                logger.debug("Socket connected to [" + socketAddress.toString() + "].");
            }
        }
    }

    public PackageData request(byte[] reequest) throws Exception {
        if (!socket.isConnected()) {
            throw new IllegalStateException("Socket not connected to[" + socketAddress + "].");
        }

        try {
            os = this.socket.getOutputStream();
            os.write(reequest);
            os.flush();
            is = this.socket.getInputStream();

            byte[] dataBuffer = new byte[HEAD_LENGTH];//18
            readFixedLenToBuffer(is, dataBuffer);
            ByteBuffer headBuffer = ByteBuffer.wrap(dataBuffer, 0, 2);
            headBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byte[] packetHead = new byte[2];
            headBuffer.get(packetHead);
            if (!(packetHead[0] == 'T' && packetHead[1] == 'K')) // ����ǰ��������ַ�����TK�����������
            {
                return null;
            }

            headBuffer.clear();

            headBuffer = ByteBuffer.wrap(dataBuffer, 2, HEAD_LENGTH - 2);
            headBuffer.order(ByteOrder.LITTLE_ENDIAN);

            // ͷ�������Ѿ��յ����������´���
            int msgTypeNo = headBuffer.getInt(); // ��Ϣ���ͱ�� funcno
            if (msgTypeNo == 12000) {
                //���ù��ܺų���
                logger.warn("	--	@���ù��ܺų���	--	");
            }
            int msgVersionNo = headBuffer.getInt(); // ��Ϣ�汾���
            int keepField = headBuffer.getInt(); // �����ֶ�
            int bodyLength = headBuffer.getInt(); // �õ������ݳ���

            if (bodyLength > MAX_PACKET_SIZE) // ���쳣
            {
                logger.warn("bodyLength�����ݳ��ȹ���");
                return null;
            }

			/*�����ͷ�� �����������������*/
            PackageData data = new PackageData();
            if (bodyLength > 0) {
                //����ɽ���ϸ���ݲ�Ϊ0 ����������������
                byte[] msg = new byte[bodyLength];
                readFixedLenToBuffer(is, msg);

                data.setBodyLength(bodyLength);
                data.setData(msg);
                data.setKeepField(keepField);
                data.setMsgTypeNo(msgTypeNo);
                data.setMsgVersionNo(msgVersionNo);
            }

            return data;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @�������ر�socket����
     * @���ߣ���֪֮
     * @ʱ�䣺2012-3-28 ����2:17:29
     */
    public void close() {
        try {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            if (socket != null) {
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            }
        } catch (IOException e) {

        }
    }

    private void readFixedLenToBuffer(InputStream inStream, byte[] buffer) throws Exception {
        int count = 0;
        int remainLength = buffer.length;
        int bufLength = buffer.length;
        int readLength = 0;
        do {
            count = inStream.read(buffer, readLength, remainLength);
            if (count == -1) //�Ѿ�����ĩβ
            {
                if (readLength != bufLength) //��ʵ�ʶ�ȡ�����ݺ���Ҫ��ȡ�����ݲ�ƥ�䣬�򱨴�
                {
                    throw new Exception("read data wrong");
                }
            }
            readLength += count;

            if (readLength == bufLength) //�Ѿ���ȡ�꣬�򷵻�
            {
                return;
            }
            remainLength = bufLength - readLength;
        }
        while (true);
    }

}
