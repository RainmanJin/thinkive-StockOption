package com.thinkive.market.service.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class ByteStrHelper {
    /**
     * ���length���ȵ��ֽ����飬��ͷ��ʼ��str������䣬����Ĳ�0
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
     * @����������ByteBuffer�ͳ��ȴ���ȡ�ַ�
     * @���ߣ���֪֮
     * @ʱ�䣺2013-1-27 ����11:26:11
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
