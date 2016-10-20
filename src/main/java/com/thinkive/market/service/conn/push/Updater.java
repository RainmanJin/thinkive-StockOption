package com.thinkive.market.service.conn.push;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

public abstract class Updater extends Thread
{
    private static Logger           logger = Logger.getLogger(Updater.class);
    
    protected BlockingQueue<byte[]> queue  = new LinkedBlockingQueue<byte[]>();
    
    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                byte[] data = queue.take();
                update(data);
            }
            catch (Exception e)
            {
                logger.error("", e);
            }
        }
    }
    
    protected abstract void update(byte[] data);
    
    /**
     * @描述：向队列插入数据
     * @作者：岳知之
     * @时间：2013-10-29 下午10:32:02
     * @param data
     */
    public void add(byte[] data)
    {
        queue.offer(data);
    }
}
