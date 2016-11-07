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
 * @描述: socket连接器
 * @版权: Copyright (c) 2012
 * @公司: 思迪科技
 * @作者: 岳知之
 * @版本: 1.0
 * @创建日期: 2012-3-28
 * @创建时间: 下午2:21:36
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
     * @描述：发起连接
     * @作者：岳知之
     * @时间：2012-3-28 下午2:22:57
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
            if (!(packetHead[0] == 'T' && packetHead[1] == 'K')) // 若最前面的两个字符不是TK，则包不正常
            {
                return null;
            }

            headBuffer.clear();

            headBuffer = ByteBuffer.wrap(dataBuffer, 2, HEAD_LENGTH - 2);
            headBuffer.order(ByteOrder.LITTLE_ENDIAN);

            // 头部数据已经收到，继续往下处理
            int msgTypeNo = headBuffer.getInt(); // 消息类型编号 funcno
            if (msgTypeNo == 12000) {
                //调用功能号出错
                logger.warn("	--	@调用功能号出错	--	");
            }
            int msgVersionNo = headBuffer.getInt(); // 消息版本编号
            int keepField = headBuffer.getInt(); // 保留字段
            int bodyLength = headBuffer.getInt(); // 得到包数据长度

            if (bodyLength > MAX_PACKET_SIZE) // 包异常
            {
                logger.warn("bodyLength包数据长度过长");
                return null;
            }

			/*读完包头后 ，继续读包体的内容*/
            PackageData data = new PackageData();
            if (bodyLength > 0) {
                //如果成交明细数据不为0 继续解析包体内容
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
     * @描述：关闭socket连接
     * @作者：岳知之
     * @时间：2012-3-28 下午2:17:29
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
            if (count == -1) //已经到达末尾
            {
                if (readLength != bufLength) //若实际读取的数据和需要读取的数据不匹配，则报错
                {
                    throw new Exception("read data wrong");
                }
            }
            readLength += count;

            if (readLength == bufLength) //已经读取完，则返回
            {
                return;
            }
            remainLength = bufLength - readLength;
        }
        while (true);
    }

}
