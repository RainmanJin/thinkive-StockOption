package com.thinkive.market.service.conn;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.thinkive.market.service.cache.ConnAddressManager;
import com.thinkive.market.service.cache.HQStateCache;

public class TCPReceiver1 extends Receiver
{
	private static final int	HEAD_LENGTH	= 16;
											
	private static Logger		logger		= Logger.getLogger(TCPReceiver1.class);
											
	public TCPReceiver1()
	{
		setName("TCPReceiver1");
		//System.out.println("TCPReceiver1���췽���޲� ******************");
		DispathWorker1 worker = new DispathWorker1();
		worker.start();
	}
	
	public void run()
	{
		while (true)
		{
			SocketChannel channel = null;
			try
			{
				// ��ǰ���ӵķ�������ַ */
				SocketAddress target = ConnAddressManager.getPush_currentAddress();
				if ( target == null )
				{
					Thread.sleep(1000);
					continue;
				}
				
				//System.out.println("����ת��������ַ���˿�=========="+target);
				
				// ���һ��Socketͨ��
				channel = SocketChannel.open();
				channel.configureBlocking(false);
				channel.connect(target);
				
				Selector selector = Selector.open();
				// ע��
				channel.register(selector, SelectionKey.OP_CONNECT);
				// ��ѯ����selector
				while (true)
				{
					selector.select();
					// ���selector��ѡ�е���ĵ�����
					Iterator ite = selector.selectedKeys().iterator();
					while (ite.hasNext())
					{
						SelectionKey key = (SelectionKey) ite.next();
						// ɾ����ѡ��key,�Է��ظ�����
						ite.remove();
						// �����¼�����
						if ( key.isConnectable() )
						{
							SocketChannel sc = (SocketChannel) key.channel();
							// ����������ӣ����������
							if ( sc.isConnectionPending() )
							{
								sc.finishConnect();
							}
							// ���óɷ�����
							sc.configureBlocking(false);
							
							int version = 0;
							int msgType = 2;// 2��ʾ����������Ϣ
							ByteBuffer byteBuffer = ByteBuffer.allocate(18);
							byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
							byteBuffer.put((byte) 'T').put((byte) 'K');
							byteBuffer.putInt(msgType);
							byteBuffer.putInt(version);
							byteBuffer.putInt(0);// ����
							byteBuffer.putInt(0);// ���ݳ���
							// byteBuffer.put(new byte[0]);
							byteBuffer.flip();
							
							//System.out.println("byteBuffer======="+byteBuffer);// java.nio.HeapByteBuffer[pos=0 lim=18 cap=18]
							
							sc.write(byteBuffer);
							//System.out.println("byteBuffer=" + byteBuffer);
							//System.out.println("sc==================" + sc);
							//System.out.println("sc.tostring()=======" + sc.toString());
							// �ںͷ�������ӳɹ�֮��Ϊ�˿��Խ��յ�����˵���Ϣ����Ҫ��ͨ�����ö���Ȩ�ޡ�
							sc.register(selector, SelectionKey.OP_READ);
							// ����˿ɶ����¼�
						}
						else if ( key.isReadable() )
						{
							SocketChannel sc = (SocketChannel) key.channel();
							
							ByteBuffer headBuffer = ByteBuffer.allocate(4);
							headBuffer.order(ByteOrder.LITTLE_ENDIAN);
							//System.out.println("sc1=======" + sc);
							readFixedLenToBuffer(sc, headBuffer);
							//System.out.println("headBuffer=======" + headBuffer);// headBuffer:java.nio.HeapByteBuffer[pos=4
							// lim=4
							// cap=4]
							headBuffer.flip();
							
							int length = headBuffer.getInt();// (length=0����������)
																// ���ݳ��ȣ����4���ֽڣ�����Ϣ�ܳ��ȣ�������Ϣͷ����Ϣ�塣
																//System.out.println("��length(ǰ�ĸ��ֽ�)=====" + length);
																
							if ( length > 16 )
							{
								// ByteBuffer dataBuffer = ByteBuffer.allocate(HEAD_LENGTH +length);
								ByteBuffer dataBuffer = ByteBuffer.allocate(length);
								//System.out.println("dataBuffer.capacity()======"+ dataBuffer.capacity());
								
								dataBuffer.order(ByteOrder.LITTLE_ENDIAN);
								dataBuffer.putInt(length);
								
								//System.out.println("dataBuffer======="+dataBuffer);//java.nio.HeapByteBuffer[pos=4 lim=32 cap=32]
								
								readFixedLenToBuffer(sc, dataBuffer);
								//System.out.println("dataBuffer="+dataBuffer);//java.nio.HeapByteBuffer[pos=32 lim=32 cap=32]
								dataBuffer.flip();
								// ���볤���жϣ���������������������Ϊ0��������⡣
								
								//Receiver.setReceived(true);
								//System.out.println("isReceived()="+ isReceived());
								if ( isReceived() && length > 16 )
								{
									//System.out.println("���վ��ս��ս��ս��ս��ս��ս���");
									//logger.warn("���վ��ս��ս��ս��ս��ս��ս���");
									queue.offer(dataBuffer.array());
									// System.out.println("queue.take()="+queue.take());;
									count++;
								}
							}
							else
							{
								//logger.warn("~~~~~~~~~~~��������~~~~~~~~~~~~~");
								//System.out.println("~~~~~~~~~~~��������~~~~~~~~~~~~~");
							}
						}
					}
				}
				
			}
			catch (Exception ie)
			{
				logger.warn("TCP����ʧ��", ie);
			}
			finally
			{
				if ( channel != null )
				{
					try
					{
						channel.close();
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private void readFixedLenToBuffer(SocketChannel sc, ByteBuffer buffer) throws Exception
	{
		int count = 0;
		int bufLength = buffer.remaining();
		int readLength = 0;
		do
		{
			count = sc.read(buffer);
			if ( count == -1 ) // �Ѿ�����ĩβ
			{
				throw new Exception("read data wrong");
			}
			
			readLength += count;
			
			if ( readLength == bufLength ) // �Ѿ���ȡ�꣬�򷵻�
			{
				return;
			}
		}
		while (true);
	}
}
