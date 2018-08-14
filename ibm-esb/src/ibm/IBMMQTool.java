package ibm;

import com.ibm.mq.constants.MQConstants;
import pojo.MQParam;
import com.ibm.mq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: IBMMQ连接工具，包含从队列中获取数据和发送数据至队列
 *
 * @Refrence:
 * @Author :    frin
 * @Date : 2018-08-08 09:30
 * @Modify:
 *
 * mvn：
    <dependency>
        <groupId>com.ibm.mq</groupId>
        <artifactId>com.ibm.mq.allclient</artifactId>
        <version>RELEASE</version>
    </dependency>
 **/
public class IBMMQTool {
    public static final Logger LOGGER = LoggerFactory.getLogger(IBMMQTool.class);

    /**
     * 发送单条数据到队列
     * @param mqParam
     * @param data
     * @return
     */
    public static boolean sendToMQ(MQParam mqParam,String data){
        ArrayList<String> dataList = new ArrayList<String>();
        dataList.add(data);
        return sendToMQ(mqParam, dataList);
    }

    /**
     * 发送多条数据到队列
     * @param mqParam
     * @param dataList
     * @return
     */
    public static boolean sendToMQ(MQParam mqParam,List<String> dataList){
        boolean sendSuccess = false;
        MQEnvironment.hostname = mqParam.getHost();
        MQEnvironment.port = mqParam.getPort();
        MQEnvironment.channel = mqParam.getChannel();
        MQEnvironment.CCSID = mqParam.getCcsid();
        MQEnvironment.userID = mqParam.getUserId();
        MQEnvironment.password = mqParam.getPassword();

        MQQueueManager qMgr = null;
        MQMessage msg = null;
        MQPutMessageOptions po = null;

        //int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQPMO_NEW_MSG_ID | MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE;
        int openOptions = MQConstants.MQOO_OUTPUT;
        try{

            qMgr = new MQQueueManager(mqParam.getQueueManager());
            // 设置队列参数

            //打开队列
            MQQueue queue = qMgr.accessQueue(mqParam.getQueueName(), openOptions);

            for(String data : dataList) {
                msg = new MQMessage();
                //msg.messageId = "001".getBytes();   //消息id
                msg.messageType = MQConstants.MQMT_REQUEST; //消息类型
                msg.replyToQueueName = mqParam.getReplyToQueueName();    //发送至：队列名称

                msg.writeString(data);
                msg.expiry = -1;    //不过期

                po = new MQPutMessageOptions();
                queue.put(msg,po);

                LOGGER.info("队列信息已发送：" + data);
            }
            sendSuccess = true;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            //e.printStackTrace();
        } catch (MQException e) {
            LOGGER.error(e.getMessage());
            //e.printStackTrace();
        }finally{
            if(qMgr != null){
                try {
                    qMgr.close();
                } catch (MQException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
        return sendSuccess;
    }

    /**
     * 从队列中获取数据
     * @param mqParam
     * @return
     * @throws MQException
     */
    public static List<String> receiveDataFromQueue(MQParam mqParam) {
        MQEnvironment.hostname = mqParam.getHost();
        MQEnvironment.port = mqParam.getPort();
        MQEnvironment.channel = mqParam.getChannel();
        MQEnvironment.CCSID = mqParam.getCcsid();
        MQEnvironment.userID = mqParam.getUserId();
        MQEnvironment.password = mqParam.getPassword();

        List<String> resultList = new ArrayList<String>();

        MQQueueManager qMgr = null;
        MQMessage rcvMessage = null;
        MQQueue queue = null;
        String result = null;
        int dataSize = 0;
        try {
            qMgr = new MQQueueManager(mqParam.getQueueManager());

            // 获取消息参数设置
            MQGetMessageOptions gmo = new MQGetMessageOptions();
            gmo.options += MQConstants.MQGMO_SYNCPOINT;
            // under sync point control（在同步点控制下获取消息）
            gmo.options += MQConstants.MQGMO_WAIT;
            // on the Queue（如果在队列上没有消息则等待）
            gmo.options += MQConstants.MQGMO_FAIL_IF_QUIESCING;
            // Qeue Manager Quiescing（如果队列管理器停顿则失败）（设置等待的毫秒时间限制）
            gmo.waitInterval = 1000;

            //int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE;
            int openOptions = MQConstants.MQOO_OUTPUT;
            queue = qMgr.accessQueue(mqParam.getQueueName(), openOptions);

            int depth = queue.getCurrentDepth();  //内容深度
            LOGGER.info("开始循环接收队列信息，数据数量：" + depth);
            while (depth-- > 0) {
                try {
                    rcvMessage = new MQMessage();
                    queue.get(rcvMessage, gmo);

                    dataSize = rcvMessage.getDataLength();
                    result = rcvMessage.readStringOfByteLength(dataSize);
                    resultList.add(result);

                    LOGGER.info("数据大小：" + dataSize +"，内容：" + result);
                    LOGGER.info("MessageID: " + new String(rcvMessage.messageId,0,rcvMessage.messageId.length,"GBK") + " Type: " + rcvMessage.messageType);
                } catch (MQException mqe) {
                    LOGGER.error(mqe.getMessage());
                    //mqe.printStackTrace();
                    break;
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                    //e.printStackTrace();
                    break;
                }
            }
            LOGGER.info("成功获取队列数：" + resultList.size());
        } catch (MQException ex) {
            LOGGER.error("获取mq数据出错: Completion Code " + ex.completionCode + " Reason Code " + ex.reasonCode + ex.getMessage());
        } finally {
            try {
                if (queue != null) {
                    queue.close();
                }
                if (qMgr != null) {
                    qMgr.disconnect();
                    qMgr.close();
                }
            }catch(Exception e){
                LOGGER.error("队列连接无法正常关闭:" + e.getMessage());
            }
        }

        return resultList;
    }
}
