import ibm.IBMMQTool;
import ibm.IBMReceiverThread;
import ibm.IBMSenderThread;
import jms.JMSGetQueueThread;
import jms.JMSSendDataThread;
import pojo.MQParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.TextMessage;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Refrence:
 * @Author: frin
 * @Date: 2018/8/8 13:50
 * @Modify:
 **/
public class ESBTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(ESBTest.class);

    public static void main(String[] args){
        /***************** IBM Que Test***************/

        MQParam param = new MQParam();
        /*param.setHost("10.86.92.193");
        param.setPort(30000);
        param.setCcsid(1208);
        param.setChannel("SVRCONN_GW_IN");
        param.setQueueManager("ESB_IN");
        param.setQueueName("EIS.QUEUE.REQUEST.OUT.GCDCHZWMES");
        param.setReplyToQueueName("EIS.QUEUE.RESPONSE.OUT.GCDCHZWMES");
        */
        param.setHost("10.86.92.194");
        param.setPort(30099);
        param.setCcsid(1208);
        param.setChannel("SVRCONN_GW_OUT");
        param.setQueueManager("ESB_OUT");
        param.setQueueName("EIS.QUEUE.REQUEST.OUT.GCDCHZWMES");
        param.setReplyToQueueName("EIS.QUEUE.RESPONSE.IN.GCDCHZWMES");

        IBMMQTool.sendToMQ(param,"send a new Data");

        List<String> sendDataList = new ArrayList<String>();
        sendDataList.add("data2");
        sendDataList.add("data3");
        IBMMQTool.sendToMQ(param, sendDataList);

        //ibm mq 接收数据
        Map<byte[],String> receiveMap = IBMMQTool.receiveDataFromQueue(param);
        for(byte[] msgId : receiveMap.keySet()){
            try {
                LOGGER.info(new String(msgId,0,msgId.length,"UTF-8") + "," + receiveMap.get(msgId));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        /******* 线程发送及接收数据 ************/
        testIBMMQReceiveData();
        testIBMMQSendData();

        /***************** JMS Test***************/

        //JMS send
        param = new MQParam();
        param.setHost("10.86.92.194");
        param.setPort(30099);
        param.setCcsid(1208);
        param.setChannel("SVRCONN_GW_OUT");
        param.setQueueManager("ESB_OUT");
        param.setQueueName("EIS.QUEUE.REQUEST.OUT.GCDCHZWMES");
        param.setReplyToQueueName("EIS.QUEUE.RESPONSE.IN.GCDCHZWMES");

        LOGGER.info("开始接收队列信息...");
        (new JMSGetQueueThread(param)).run();

        //JMS receive

        try {
			/*MQParam param = new MQParam();
			param.setHost("10.86.92.193");
			param.setPort(30000);
			param.setCcsid(1208);
			param.setChannel("SVRCONN_GW_IN");
			param.setQueueManager("ESB_IN");
			param.setQueueName("EIS.QUEUE.REQUEST.OUT.GCDCHZWMES");
			param.setReplyToQueueName("EIS.QUEUE.RESPONSE.OUT.GCDCHZWMES");*/

            param = new MQParam();
            param.setHost("10.86.92.194");
            param.setPort(30099);
            param.setCcsid(1208);
            param.setChannel("SVRCONN_GW_OUT");
            param.setQueueManager("ESB_OUT");
            param.setQueueName("EIS.QUEUE.REQUEST.OUT.GCDCHZWMES");
            param.setReplyToQueueName("EIS.QUEUE.RESPONSE.IN.GCDCHZWMES");

            String sendStr = "<info>test send msg</info>";

            TextMessage msg = new JMSSendDataThread().sendDataToQue(param,sendStr);

            if(msg != null) {
                LOGGER.info("数据已发送，messageID:" + msg.getJMSMessageID() + ",data:" + msg.getText());
            }else{
                LOGGER.info("数据发送出错。");
            }
        } catch (Throwable e) {
            LOGGER.error(e.getMessage());
            //e.printStackTrace();
        }
    }

    public static void testIBMMQReceiveData(){
        /*MQParam param = new MQParam();
        param.setHost("10.86.92.193");
        param.setPort(30000);
        param.setCcsid(1208);
        param.setChannel("SVRCONN_GW_IN");
        param.setQueueManager("ESB_IN");
        param.setQueueName("EIS.QUEUE.REQUEST.OUT.GCDCHZWMES");
        param.setReplyToQueueName("EIS.QUEUE.RESPONSE.OUT.GCDCHZWMES");*/

        MQParam param = new MQParam();
        param.setHost("10.86.92.194");
        param.setPort(30099);
        param.setCcsid(1208);
        param.setChannel("SVRCONN_GW_OUT");
        param.setQueueManager("ESB_OUT");
        param.setQueueName("EIS.QUEUE.REQUEST.OUT.GCDCHZWMES");
        param.setReplyToQueueName("EIS.QUEUE.RESPONSE.IN.GCDCHZWMES");
        param.setUserId("mqm");
        param.setPassword("mqm");

        new Thread(new IBMSenderThread(param,"data1")).run();
        new Thread(new IBMSenderThread(param,"datata22")).run();
        new Thread(new IBMSenderThread(param,"datatatata333")).run();
    }

    public static void testIBMMQSendData(){
        /*MQParam param = new MQParam();
        param.setHost("10.86.92.193");
        param.setPort(30000);
        param.setCcsid(1208);
        param.setChannel("SVRCONN_GW_IN");
        param.setQueueManager("ESB_IN");
        param.setQueueName("EIS.QUEUE.REQUEST.OUT.GCDCHZWMES");
        param.setReplyToQueueName("EIS.QUEUE.RESPONSE.OUT.GCDCHZWMES");
        param.setUserId("mqm");
        param.setPassword("mqm");*/

        MQParam param = new MQParam();
        param.setHost("10.86.92.194");
        param.setPort(30099);
        param.setCcsid(1208);
        param.setChannel("SVRCONN_GW_OUT");
        param.setQueueManager("ESB_OUT");
        param.setQueueName("EIS.QUEUE.REQUEST.OUT.GCDCHZWMES");
        param.setUserId("mqm");
        param.setPassword("mqm");
        new Thread(new IBMReceiverThread(param)).run();
    }
}
