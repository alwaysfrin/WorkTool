package ibm;

import com.ibm.mq.constants.MQConstants;
import pojo.MQParam;
import com.ibm.mq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Description :   IBMMQ，发送数据至队列线程
 * @Reference :
 *      2027 missing reply queue
 *      2033 NO_MSG_AVAILABLE  队列中没有数据
 *      2085 MQRC_UNKNOWN_OBJECT_NAME   队列名称错误
 * @Author :    frin
 * @Date : 2018-08-08 09:30
 * @Modify :
 **/
public class IBMSenderThread implements Runnable {
    public static final Logger LOGGER = LoggerFactory.getLogger(IBMSenderThread.class);
    private String sendData;
    private MQParam mqParam;
    private MQQueueManager qMgr;

    public IBMSenderThread(MQParam param, String data){
        this.mqParam = param;
        this.sendData = data;
    }

    public void run() {
        try {
            sendMessage(sendData);
        }catch(Exception e){
            LOGGER.error("发送队列出错：" + e.getMessage());
        }
    }

    public void sendMessage(String sendData) throws MQException {
        MQEnvironment.hostname = mqParam.getHost();
        MQEnvironment.port = mqParam.getPort();
        MQEnvironment.channel = mqParam.getChannel();
        MQEnvironment.CCSID = mqParam.getCcsid();
        MQEnvironment.userID = mqParam.getUserId();
        MQEnvironment.password = mqParam.getPassword();

        // java程序连接mq的方式有两种，一是客户机方式，一是绑定方式，
        // 默认是客户机方式，当mq部署在本地的时候，就需要用绑定方式
        // 本机IP是10.24.28.139连接10.23.117.134的时候不需要下句
        //MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY,MQC.TRANSPORT_MQSERIES_BINDINGS);

        try {
            qMgr = new MQQueueManager(mqParam.getQueueManager());
            // 设置队列参数
            //int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQPMO_NEW_MSG_ID | MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE;
            int openOptions = MQConstants.MQOO_OUTPUT;
            //打开队列
            MQQueue queue = qMgr.accessQueue(mqParam.getQueueName(), openOptions);

            MQMessage msg = new MQMessage();
            //msg.messageId = "001".getBytes();   //消息id
            msg.messageType = MQConstants.MQMT_REQUEST; //消息类型
            msg.replyToQueueName = mqParam.getReplyToQueueName();    //发送至：队列名称

            msg.writeString(sendData);
            //msg.writeUTF(sendData);
            //msg.writeObject(sendData);
            msg.expiry = -1;    //不过期

            MQPutMessageOptions po = new MQPutMessageOptions();
            queue.put(msg,po);

            //qMgr.commit();
            LOGGER.info("队列信息已发送：" + sendData);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            //e.printStackTrace();
        } catch (MQException e) {
            LOGGER.error(e.getMessage());
            //e.printStackTrace();
        }finally{
            if(qMgr != null){
                qMgr.close();
            }
        }
    }
}

