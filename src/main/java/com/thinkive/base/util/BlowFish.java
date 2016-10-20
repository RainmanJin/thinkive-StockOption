package com.thinkive.base.util;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class BlowFish
{
    
    private static final byte[] PWD = "SZKINGDOM".getBytes();
    
    public static String decrypt(String input, byte[] keyAry)
    {
        try
        {
            if ( keyAry == null )
                keyAry = PWD;
            SecretKeySpec key = new SecretKeySpec(keyAry, "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            
            byte[] data = hexStr2Bytes(input);
            byte[] decryptData = cipher.doFinal(data, 0, data.length);
            return new String(decryptData);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String encrypt(String input, byte[] keyAry)
    {
        try
        {
            if ( keyAry == null )
                keyAry = PWD;
            SecretKeySpec key = new SecretKeySpec(keyAry, "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ByteArrayInputStream bis = new ByteArrayInputStream(input.getBytes());
            CipherOutputStream cos = new CipherOutputStream(bos, cipher);
            int theByte = 0;
            while ((theByte = bis.read()) != -1)
            {
                cos.write(theByte);
            }
            cos.close();
            bis.close();
            return byte2HexStr(bos.toByteArray());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 字符串转换成十六进制字符串
     */
    public static String str2HexStr(String str)
    {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++)
        {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString();
    }
    
    /**
     * 十六进制转换字符串
     */
    public static String hexStr2Str(String hexStr)
    {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++)
        {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }
    
    /**
     * bytes转换成十六进制字符串
     */
    public static String byte2HexStr(byte[] b)
    {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++)
        {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if ( stmp.length() == 1 )
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }
    
    private static byte uniteBytes(String src0, String src1)
    {
        byte b0 = Byte.decode("0x" + src0).byteValue();
        b0 = (byte) (b0 << 4);
        byte b1 = Byte.decode("0x" + src1).byteValue();
        byte ret = (byte) (b0 | b1);
        return ret;
    }
    
    /**
     * 十六进制字符串转换成bytes
     */
    public static byte[] hexStr2Bytes(String src)
    {
        int m = 0, n = 0;
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++)
        {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = uniteBytes(src.substring(i * 2, m), src.substring(m, n));
        }
        return ret;
    }
    
    /**
     *String的字符串转换成unicode的String
     */
    public static String stringToUnicode(String strText) throws Exception
    {
        char c;
        String strRet = "";
        int intAsc;
        String strHex;
        for (int i = 0; i < strText.length(); i++)
        {
            c = strText.charAt(i);
            intAsc = (int) c;
            strHex = Integer.toHexString(intAsc);
            if ( intAsc > 128 )
            {
                strRet += "\\u" + strHex;
            }
            else
            {
                // 低位在前面补00
                strRet += "\\u00" + strHex;
            }
        }
        return strRet;
    }
    
    public static void main(String[] args)
    {
        BlowFish bl = new BlowFish();
        System.out.println("BC42F61D9C531CD3解密后的内容为：" + bl.decrypt("BC42F61D9C531CD3", null));
        System.out.println("123123加密后的内容为：" + bl.encrypt("1231    ", null));
        // e93578ed
        String t = "0061|0000000026|58748506|KDGATEWAY1.2|0|交易成功|0|1|1|100|||100|";
        
    }
    
}
