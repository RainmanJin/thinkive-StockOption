package com.thinkive.market.service.works.task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

import org.apache.log4j.Logger;

import com.thinkive.base.config.Configuration;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.util.ConvertHelper;
import com.thinkive.base.util.DateHelper;
import com.thinkive.base.util.StringHelper;
import com.thinkive.market.service.cache.HQDataCache;
import com.thinkive.server.Request;
import com.thinkive.server.Response;
import com.thinkive.server.database.ElianghuaService;

/**
 * @描述: 更新成交明细
 * @版权: Copyright (c) 2012 
 * @公司: 思迪科技 
 * @作者: 岳知之
 * @版本: 1.0 
 * @创建日期: 2012-3-17 
 * @创建时间: 下午12:05:10
 */
public class PushSignTask extends BaseConvTask
{
    private static Logger      logger        = Logger.getLogger("PushSignTask");
    
    public static final String DEAL_DATA     = "dealdata";
    
    private static Timestamp   strategy2Time = new Timestamp(0);
    
    private static Timestamp   strategy1Time = new Timestamp(0);
    
    /**
     * @描述：初始化，获取最新的触发事件
     * @作者：岳知之
     * @时间：2013-4-8 下午7:32:17
     */
    @Override
    public void init(String param)
    {
        int count = 1;
        int index = 0;
        String type = "Strategy1";
        List strategy01 = elianghuaService.findFactRTSignalList(type, count);
        
        type = "Strategy2";
        List strategy02 = elianghuaService.findFactRTSignalList(type, count);
        
        if ( strategy02 != null && strategy02.size() > index )
        {
            DataRow rtsSign = (DataRow) strategy02.get(index);
            String securityCode = rtsSign.getString("securitycode");
            Timestamp trigeredtime = (Timestamp) rtsSign.get("trigeredtime");
            int date = Integer.parseInt(DateHelper.formatDate(trigeredtime, "yyyyMMdd"));
            int time = Integer.parseInt(DateHelper.formatDate(trigeredtime, "HHmmss"));
            strategy2Time = trigeredtime;
        }
        if ( strategy01 != null && strategy01.size() > index )
        {
            DataRow rtsSign = (DataRow) strategy01.get(index);
            String securityCode = rtsSign.getString("securitycode");
            Timestamp trigeredtime = (Timestamp) rtsSign.get("trigeredtime");
            int date = Integer.parseInt(DateHelper.formatDate(trigeredtime, "yyyyMMdd"));
            int time = Integer.parseInt(DateHelper.formatDate(trigeredtime, "HHmmss"));
            
            strategy1Time = trigeredtime;
        }
    }
    
    @Override
    public void update()
    {
        
        byte data[] = getPushData();
        if ( data != null && data.length > 10 )
        {
            byte b[] = new byte[data.length + 50];
            System.arraycopy(data, 0, b, 0, data.length);
            try
            {
                send(8001, b);
                logger.warn("发送数据 长度:" + b.length);
            }
            catch (Exception e)
            {
                logger.warn("发送数据失败", e);
            }
        }
        
    }
    
    /**
     * @描述：获取推送数据
     * @作者：岳知之
     * @时间：2013-4-8 下午7:32:09
     * @return
     */
    private byte[] getPushData()
    {
        int type_1 = 0x8000;
        int type_2 = 0x4000;
        
        List abitList = elianghuaService.findStrategyAbitList();
        int abitTotal = 0;
        //获取信号长度
        int size = 2;
        //                size += (abitList == null ? 0 : abitList.size()) * 4;
        List strategy01 = new ArrayList();
        List strategy02 = new ArrayList();
        if ( abitList != null && abitList.size() > 0 )
        {
            for (int i = 0; i < abitList.size(); i++)
            {
                DataRow abitDataRow = (DataRow) abitList.get(i);
                if ( i == 0 )
                {
                    abitTotal = abitDataRow.getInt("total");
                }
                int abit = abitDataRow.getInt("abit");
                String type = "Strategy1";
                if ( abit == type_1 ) // 策略一
                {
                    type = "Strategy1";
                    strategy01 = elianghuaService.findFactRTSignalList(type, strategy1Time);
                    size += 4 + strategy01.size() * 18;
                }
                else if ( abit == type_2 ) // 策略二
                {
                    type = "Strategy2";
                    strategy02 = elianghuaService.findFactRTSignalList(type, strategy2Time);
                    size += 4 + strategy02.size() * 18;
                }
            }
        }
        
        ByteBuffer backBuffer = ByteBuffer.allocate(size);
        backBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] abitArray = intToBytes(abitTotal);
        backBuffer.put((byte) abitArray[1]);
        backBuffer.put((byte) abitArray[0]);
        
        if ( strategy01 != null )
        {
            getData(backBuffer, strategy01, type_1);
        }
        
        if ( strategy02 != null )
        {
            getData(backBuffer, strategy02, type_2);
        }
        return backBuffer.array();
    }
    
    @Override
    public void clear()
    {
        HQDataCache.setData(DEAL_DATA, null);
    }
    
    private ElianghuaService elianghuaService = new ElianghuaService();
    
    public void service(Request request, Response response)
    {
        try
        {
            
            byte[] param = request.getData();
            if ( param != null && param.length == 4 )
            {
                int type_1 = 0x8000;
                int type_2 = 0x4000;
                ByteBuffer dataBuffer = ByteBuffer.wrap(param);
                dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
                int count = dataBuffer.getInt();
                if ( count == 0 )
                {
                    count = 1;
                }
                List abitList = elianghuaService.findStrategyAbitList();
                int abitTotal = 0;
                //获取信号长度
                int size = 2;
                //                size += (abitList == null ? 0 : abitList.size()) * 4;
                List strategy01 = new ArrayList();
                List strategy02 = new ArrayList();
                if ( abitList != null && abitList.size() > 0 )
                {
                    for (int i = 0; i < abitList.size(); i++)
                    {
                        DataRow abitDataRow = (DataRow) abitList.get(i);
                        if ( i == 0 )
                        {
                            abitTotal = abitDataRow.getInt("total");
                        }
                        int abit = abitDataRow.getInt("abit");
                        String type = "Strategy1";
                        if ( abit == type_1 ) // 策略一
                        {
                            type = "Strategy1";
                            strategy01 = elianghuaService.findFactRTSignalList(type, count);
                            size += 4 + strategy01.size() * 18;
                        }
                        else if ( abit == type_2 ) // 策略二
                        {
                            type = "Strategy2";
                            strategy02 = elianghuaService.findFactRTSignalList(type, count);
                            size += 4 + strategy02.size() * 18;
                        }
                    }
                }
                
                ByteBuffer backBuffer = ByteBuffer.allocate(size);
                backBuffer.order(ByteOrder.LITTLE_ENDIAN);
                byte[] abitArray = intToBytes(abitTotal);
                backBuffer.put((byte) abitArray[1]);
                backBuffer.put((byte) abitArray[0]);
                
                if ( strategy01 != null )
                {
                    getData(backBuffer, strategy01, type_1);
                }
                
                if ( strategy02 != null )
                {
                    getData(backBuffer, strategy02, type_2);
                }
                
                logger.info("Function007007 end");
                
                response.write(backBuffer.array());
            }
        }
        catch (Exception ex)
        {
            //发生异常后清理已输出数据
            response.clearData();
            response.setErrorNo( -700701);
            //输出错误日志信息
            ex.printStackTrace();
            logger.error("", ex);
        }
    }
    
    /**
     * @描述：策略标识
     * @作者：岳知之
     * @时间：2013-3-30 下午5:11:05
     * @param num
     * @return
     */
    public byte[] intToBytes(int num)
    {
        byte[] result = new byte[2];
        result[0] = (byte) (num & 0xFF);
        result[1] = (byte) ((num >> 8) & 0xFF);
        return result;
    }
    
    public byte[] getSignal(int signal, int sbit)
    {
        byte[] result = new byte[2];
        int upper = (int) Math.ceil(Math.log(signal) / Math.log(2));
        int lower = (int) Math.floor(Math.log(signal) / Math.log(2));
        int index = upper > lower ? upper : lower + 1;
        int length = 16;
        int num = 0;
        
        for (int i = 0; i < index; i++)
        {
            int ys = signal % 2;
            signal = signal / 2;
            if ( ys == 1 )
            {
                num += (int) Math.pow(2, length - 1 - i);
            }
        }
        
        num = num & sbit;
        
        result[0] = (byte) (num & 0xFF);
        result[1] = (byte) ((num >> 8) & 0xFF);
        return result;
    }
    
    public void getData(ByteBuffer backBuffer, List list, int abit)
    {
        int size = list.size();
        backBuffer.putInt(size);
        
        if ( size > 0 )
        {
            int sbit = elianghuaService.findSignCountByAbit(abit);
            for (int j = 0; j < size; j++)
            {
                DataRow rtsSign = (DataRow) list.get(j);
                String securityCode = rtsSign.getString("securitycode");
                Timestamp trigeredtime = (Timestamp) rtsSign.get("trigeredtime");
                int date = Integer.parseInt(DateHelper.formatDate(trigeredtime, "yyyyMMdd"));
                int time = Integer.parseInt(DateHelper.formatDate(trigeredtime, "HHmmss"));
                byte[] byteArray = getSignal(rtsSign.getInt("signal"), sbit);
                
                backBuffer.put(getBytes(securityCode, 8));
                backBuffer.putInt(date);
                backBuffer.putInt(time);
                backBuffer.put((byte) byteArray[1]);
                backBuffer.put((byte) byteArray[0]);
            }
        }
    }
    
    /**
     * @描述：在做String处理时和桌面端约定，String不使用\0结尾，所以全部是有效字符，注意区分和后端的getString之间的不同。这里构造String时没有用length-1
     * @作者：岳知之
     * @时间：2012-9-7 下午5:08:48
     * @param dataBuffer
     * @param length
     * @return
     */
    protected String getString(ByteBuffer dataBuffer, int length)
    {
        String str = null;
        try
        {
            byte[] codeBuffer = new byte[length];
            dataBuffer.get(codeBuffer);
            str = new String(codeBuffer, 0, length, "GBK").trim();
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }
    
    /**
     * 获得length长度的字节数组，从头开始用str进行填充，不足的补0
     *
     * @param str
     * @param length
     * @return
     */
    protected byte[] getBytes(String str, int length)
    {
        byte[] strBytes = str.getBytes();
        if ( strBytes.length >= length )
        {
            byte[] temp = new byte[length];
            System.arraycopy(strBytes, 0, temp, 0, length);
            return temp;
        }
        
        byte[] temp = new byte[length];
        for (int i = 0; i < length; i++)
        {
            temp[i] = 0;
        }
        
        for (int i = 0; i < strBytes.length; i++)
        {
            temp[i] = strBytes[i];
        }
        
        return temp;
    }
    
    public void request(byte[] reequest) throws Exception
    {
        Socket socket = new Socket();
        OutputStream os = null;
        InputStream is = null;
        //发起连接
        socket.connect(socketAddress);
        socket.setSoTimeout(10000);
        if ( logger.isDebugEnabled() )
        {
            logger.debug("Socket connected to [" + socketAddress.toString() + "].");
        }
        if ( !socket.isConnected() )
        {
            throw new IllegalStateException("Socket not connected to[" + socketAddress + "].");
        }
        
        try
        {
            os = socket.getOutputStream();
            os.write(reequest);
            os.flush();
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            try
            {
                if ( is != null )
                {
                    is.close();
                }
                if ( os != null )
                {
                    os.close();
                }
                if ( socket != null )
                {
                    socket.shutdownInput();
                    socket.shutdownOutput();
                    socket.close();
                }
            }
            catch (IOException e)
            {
                
            }
        }
    }
    
    public static byte[] decompress(byte[] data)
    {
        byte[] output = new byte[0];
        
        Inflater decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data);
        
        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
        try
        {
            byte[] buf = new byte[1024];
            while ( !decompresser.finished())
            {
                int i = decompresser.inflate(buf);
                o.write(buf, 0, i);
            }
            output = o.toByteArray();
        }
        catch (Exception e)
        {
            output = data;
            e.printStackTrace();
        }
        finally
        {
            try
            {
                o.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        decompresser.end();
        return output;
    }
    
    /**
     * @描述：解析客户配置的push服务器发送请求列表,只允许配置一个
     * @作者：岳知之
     * @时间：2013-1-22 下午7:46:31
     * @return
     */
    private static InetSocketAddress parseConfiguration()
    {
        String clientConfig = Configuration.getString("pushserver.host");
        if ( StringHelper.isEmpty(clientConfig) )
        {
            return new InetSocketAddress("127.0.0.1", 9096);
        }
        else
        {
            String[] addresses = clientConfig.split("\\|");
            InetSocketAddress[] socketAddresses = new InetSocketAddress[addresses.length];
            for (int i = 0; i < addresses.length; i++)
            {
                String[] hostPort = addresses[i].split(":");
                if ( hostPort != null && hostPort.length == 2 )
                {
                    socketAddresses[i] = new InetSocketAddress(hostPort[0], ConvertHelper.strToInt(hostPort[1]));
                }
            }
            return socketAddresses[0];
        }
    }
    
    private void send(int CommandID, byte[] data) throws Exception
    {
        int bagLen = HEAD_LENGTH + data.length;
        ByteBuffer dataBuffer = ByteBuffer.allocate(bagLen);
        dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
        
        dataBuffer.put((byte) 'T').put((byte) 'K');
        dataBuffer.putInt(0x10000);
        dataBuffer.put((byte) 0);
        dataBuffer.putInt(data.length);//BagLen  UINT    传输长度
        dataBuffer.putInt(data.length);//OrigLen Int 包体原始长度
        dataBuffer.putShort((short) 8);//BranchID    Short   1
        dataBuffer.putShort((short) CommandID);//CommandID   Short   1002
        dataBuffer.putInt((int) Math.round(Integer.MAX_VALUE * Math.random()));//FlowNo  Int 流水号
        dataBuffer.putInt(0);//error号
        dataBuffer.put(new byte[8]);//Reserved    Byte(8) 保留字段
        
        dataBuffer.put(data);
        dataBuffer.flip();
        request(dataBuffer.array());
        
    }
    
    /**
     * @描述：拼接两个字节数组
     * @作者：岳知之
     * @时间：2013-3-21 下午8:32:17
     * @param data1
     * @param data2
     * @return
     */
    private byte[] join(byte[] data1, byte[] data2)
    {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }
    
    public static void main(String[] args) throws Exception
    {
        PushSignTask pushSignTask = new PushSignTask();
        pushSignTask.init(null);
        byte data[] = pushSignTask.getPushData();
        byte ext[] = new byte[50];
        byte thinkive[] = "thinkive".getBytes();
        System.arraycopy(thinkive, 0, ext, 0, thinkive.length);
        pushSignTask.send(8001, pushSignTask.join(data, ext));
        
        ext = new byte[50];
        byte thinkive2[] = "thinkive1".getBytes();
        System.arraycopy(thinkive2, 0, ext, 0, thinkive2.length);
        pushSignTask.send(8001, pushSignTask.join(data, ext));
    }
    
    private static final int  HEAD_LENGTH   = 35;
    
    private InetSocketAddress socketAddress = parseConfiguration();
    
}
