package com.thinkive.market.service.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class ByteStrHelper {
    /**
     * 获得length长度的字节数组，从头开始用str进行填充，不足的补0
     */
    public static byte[] getBytes(String str, int length) {
        byte[] strBytes = str.getBytes();
        if (strBytes.length >= length) {
            byte[] temp = new byte[length];
            System.arraycopy(strBytes, 0, temp, 0, length);
            return temp;
        }

        byte[] temp = new byte[length];
        for (int i = 0; i < length; i++) {
            temp[i] = 0;
        }

        for (int i = 0; i < strBytes.length; i++) {
            temp[i] = strBytes[i];
        }

        return temp;
    }


    /**
     * @描述：根据ByteBuffer和长度串获取字符
     * @作者：岳知之
     * @时间：2013-1-27 上午11:26:11
     */
    public static String getString(ByteBuffer dataBuffer, int length) {
        String str = null;
        try {
            byte[] codeBuffer = new byte[length];
            dataBuffer.get(codeBuffer);
            str = new String(codeBuffer, 0, length, "GBK").trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }
}
