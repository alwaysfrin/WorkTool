package jms;

import javax.jms.DeliveryMode;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import pojo.MQParam;
import com.ibm.mq.jms.MQQueueConnectionFactory;
//import com.ibm.msg.client.wmq.v6.jms.internal.JMSC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description :	jms发送信息至队列线程
 * @Reference :
 *      2033 NO_MSG_AVAILABLE  队列中没有数据
 *      2085 MQRC_UNKNOWN_OBJECT_NAME   队列名称错误
 * @Author :    frin
 * @Date : 2018-08-08 09:30
 * @Modify :
 **/
public class JMSSendDataThread {
	public static final Logger LOGGER = LoggerFactory.getLogger(JMSSendDataThread.class);

	private MQParam mqParam;
	//private int transportType = JMSC.MQJMS_TP_CLIENT_MQ_TCPIP;	//传输类型

	private QueueConnection connection = null;

	public TextMessage sendDataToQue(MQParam mqParam,String data) throws Throwable {
		this.mqParam = mqParam;
		LOGGER.info("mq初始化...");
		init();
		LOGGER.info("mq开始发送信息...");
		TextMessage message = action(data);
		LOGGER.info("mq执行成功...");
		end();

		return message;
	}

	public void init() throws Throwable {
		QueueConnectionFactory factory = new MQQueueConnectionFactory();
		((MQQueueConnectionFactory) factory).setQueueManager(mqParam.getQueueManager());
		((MQQueueConnectionFactory) factory).setCCSID(mqParam.getCcsid());
		((MQQueueConnectionFactory) factory).setChannel(mqParam.getChannel());
		((MQQueueConnectionFactory) factory).setHostName(mqParam.getHost());
		((MQQueueConnectionFactory) factory).setPort(mqParam.getPort());
		//((MQQueueConnectionFactory) factory).setTransportType(transportType);

		connection = factory.createQueueConnection();
		connection.start();
	}

	public TextMessage action(String data) throws Throwable {
		QueueSession session = null;
		QueueSender queueSender = null;
		QueueReceiver queueReceiver = null;
		TextMessage sendMessage = null;
		try {
			session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue sendQueue = session.createQueue(mqParam.getQueueName());

			sendMessage = session.createTextMessage();
			sendMessage.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);

			//返回信息
			Queue receiveQueue = session.createQueue(mqParam.getReplyToQueueName());
			sendMessage.setJMSReplyTo(receiveQueue);

			//发布消息
			//sendMessage.setJMSMessageID();	//可以手动放入msgId
			sendMessage.setText(data);

			queueSender = session.createSender(sendQueue);
			queueSender.send(sendMessage, DeliveryMode.NON_PERSISTENT, 3, 0);

		} catch (Exception e) {
			sendMessage = null;
			LOGGER.error(e.getMessage());
			//e.printStackTrace();
		} finally {
			if (queueReceiver != null) {
				queueReceiver.close();
			}
			if (queueSender != null) {
				queueSender.close();
			}
			if (session != null) {
				session.close();
			}
		}

		return sendMessage;
	}

	public int end() throws Throwable {
		if (connection != null) {
			connection.stop();
			connection.close();
		}

		return 0;
	}
}
