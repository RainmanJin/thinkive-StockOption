package com.thinkive.market.service.conn;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Receiver extends Thread
{
    protected static BlockingQueue<byte[]> queue      = new LinkedBlockingQueue<byte[]>();
    
    /**
     * ��ǰ������ĸ���
     */
    protected static long                  count      = 0;
    
    /**
     * �Ƿ��򻺴���з������������
     */
    private static boolean                 isReceived = false;
    
    public static boolean isReceived()
    {
        return isReceived;
    }
    
    public static void setReceived(boolean isReceived)
    {
        Receiver.isReceived = isReceived;
        if ( !isReceived )
        {
            queue.clear();
            count = 0;
        }
    }
    
    /**
     * @��������ȡ����
     * @���ߣ���֪֮
     * @ʱ�䣺2012-4-28 ����5:02:33
     * @return
     * @throws Exception 
     */
    public static byte[] getData() throws Exception
    {
        return queue.take();
    }
    
    /**
     * @��������ȡ����������
     * @���ߣ���֪֮
     * @ʱ�䣺2012-5-8 ����12:21:16
     * @return
     */
    public static int getBufferSize()
    {
        return queue.size();
    }
    
    /**
     * @��������ȡ������ĸ���
     * @���ߣ���֪֮
     * @ʱ�䣺2013-4-9 ����1:53:05
     * @return
     */
    public static long getCount()
    {
        return count;
    }
}
