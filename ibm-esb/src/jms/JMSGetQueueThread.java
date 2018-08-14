package jms;

import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import pojo.MQParam;
import com.ibm.jms.JMSBytesMessage;
import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description :	jms从队列中接收信息线程
 * @Reference :
 *      2033 NO_MSG_AVAILABLE  队列中没有数据
 *      2085 MQRC_UNKNOWN_OBJECT_NAME   队列名称错误
 * @Author :    frin
 * @Date : 2018-08-08 09:30
 * @Modify :
 **/
public class JMSGetQueueThread implements MessageListener, Runnable {

	public static final Logger LOGGER = LoggerFactory.getLogger(JMSGetQueueThread.class);
	//private int transportType = JMSC.MQJMS_TP_CLIENT_MQ_TCPIP;	//传输类型

	private MQParam mqParam;
	private QueueConnectionFactory queueConnFactory;
	private QueueConnection queueConn;
	private QueueSession session;
	private QueueReceiver receiver;
	private Queue queue;
	boolean quit = false;

	TextMessage jmsResponseMsg;

	public JMSGetQueueThread(MQParam mqParam){
		this.mqParam = mqParam;
	}

	/*
	 * 从队列中接受到的信息
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(javax.jms.Message msg) {
		try {
			LOGGER.info("received message Id=" + msg.getJMSMessageID());
			 if( msg instanceof TextMessage ) {
					String replyString = ((TextMessage) msg).getText();
					LOGGER.info("接收到的信息:"+": "+replyString);
			 } else if( msg instanceof JMSBytesMessage ) {
			 	//ibm mq 发送的数据格式
				 JMSBytesMessage bytesMessage = (JMSBytesMessage) msg;
				 LOGGER.info("接收到的信息:"+": "+bytesMessage.readUTF());
			 } else {
				 LOGGER.error("无法解析数据，接收到的信息Class：" + msg.getClass() + "，msgType："
						 					+ msg.getJMSType() + ",toString:" + msg);
			 }
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			//e.printStackTrace();
		} finally {
			//接收到数据后的处理
		}
	}

	public void run() {
		this.initialize();

		synchronized (this) {
			LOGGER.info("===进入接收队列线程===");

			while (!this.quit) {
				try {
					this.wait();
				} catch (InterruptedException ie) {
					LOGGER.error(ie.getMessage());
					//ie.printStackTrace();
				}
			}

			LOGGER.info("接收队列信息结束...");
			try {
				session.close();
				receiver.close();
				queueConn.stop();
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
				//e.printStackTrace();
			}
		}
	}

	//手动调用关闭
	public void quit() {
		synchronized (this) {
			quit = true;
			this.notifyAll();
		}
	}

	private void initialize() {
		try {
			//参数配置
			MQQueueConnectionFactory mqQueue = new MQQueueConnectionFactory();
			mqQueue.setHostName(mqParam.getHost());
			mqQueue.setPort(mqParam.getPort());
			mqQueue.setChannel(mqParam.getChannel());
			mqQueue.setCCSID(mqParam.getCcsid());
			mqQueue.setQueueManager(mqParam.getQueueManager());
			//mqQueue.setTransportType(transportType);

			//创建连接
			queueConnFactory = (QueueConnectionFactory) mqQueue;
			queueConn = queueConnFactory.createQueueConnection();
			session = queueConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

			MQQueue mQueue = new MQQueue();
			mQueue.setCCSID(mqParam.getCcsid());
			mQueue.setBaseQueueName(mqParam.getQueueName());

			queue = (Queue) mQueue;
			receiver = session.createReceiver(queue);

			//在指定的订阅返回队列进行监听，一旦消息到达，立即取出进行处理
			receiver.setMessageListener(this);

			jmsResponseMsg = session.createTextMessage();
			queueConn.start();

			//LOGGER.info("==收到的信息==="+jmsResponseMsg.getText());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			//e.printStackTrace();
		}
	}
}
