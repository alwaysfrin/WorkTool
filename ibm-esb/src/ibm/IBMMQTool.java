package ibm;

import com.ibm.mq.constants.MQConstants;
import pojo.MQParam;
import com.ibm.mq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static final int CHARACTER_SET = 1208;
    public static final String FORMAT ="MQSTR";
    public static final int ENCODING =546;
    public static final int WAIT_INTERVAL = 5000;    //接收队列响应时间

    /**
     * 发送单条数据到队列
     * @param mqParam
     * @param data
     * @return
     */
    public static boolean sendToMQ(MQParam mqParam,String data){
        ArrayList<String> dataList = new ArrayList<String>();
        dataList.add(data);

        try {
            byte[] msgId = String.valueOf(System.currentTimeMillis()).getBytes("UTF-8");
            return sendToMQ(mqParam,msgId,data);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 发送单条队列消息
     * @param mqParam
     * @param msgId
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean sendToMQ(MQParam mqParam,byte[] msgId,String data) {
        ArrayList<String> dataList = new ArrayList<String>();
        dataList.add(data);

        return sendToMQ(mqParam, new HashMap<byte[],String>(){{
            put(msgId,data);
        }});
    }

    /**
     * 发送多条默认msgId的队列消息
     * @param mqParam
     * @param dataList
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean sendToMQ(MQParam mqParam,List<String> dataList) {
        byte[] msgId;
        HashMap<byte[],String> msgIdAndMessages = new HashMap<byte[],String>();

        try {
            for(String data : dataList) {
                msgId = String.valueOf(System.currentTimeMillis()).getBytes("UTF-8");
                msgIdAndMessages.put(msgId,data);
            }
        return sendToMQ(mqParam,msgIdAndMessages);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 发送多条数据到队列
     * @param mqParam
     * @param msgIdAndMessages
     * @return
     */
    public static boolean sendToMQ(MQParam mqParam,Map<byte[],String> msgIdAndMessages){
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
            for (Map.Entry<byte[],String> entry :msgIdAndMessages.entrySet()){
                msg = new MQMessage();
                msg.messageId = entry.getKey();
                msg.characterSet = CHARACTER_SET;
                msg.format = FORMAT;
                msg.encoding = ENCODING;

                msg.messageType = MQConstants.MQMT_REQUEST; //消息类型
                msg.replyToQueueName = mqParam.getReplyToQueueName();    //发送至：队列名称
                msg.expiry = -1;    //不过期

                msg.writeString(entry.getValue());

                po = new MQPutMessageOptions();
                queue.put(msg,po);

                LOGGER.info("队列信息已发送：" + entry.getValue());
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
    public static Map<byte[],String> receiveDataFromQueue(MQParam mqParam) {
        MQEnvironment.hostname = mqParam.getHost();
        MQEnvironment.port = mqParam.getPort();
        MQEnvironment.channel = mqParam.getChannel();
        MQEnvironment.CCSID = mqParam.getCcsid();
        MQEnvironment.userID = mqParam.getUserId();
        MQEnvironment.password = mqParam.getPassword();

        //msgId和队列内容
        Map<byte[],String> resultMap = new HashMap<>();

        MQQueueManager qMgr = null;
        MQMessage rcvMessage = null;
        MQQueue queue = null;
        StringBuffer result = null;
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
            gmo.waitInterval = WAIT_INTERVAL;

            LOGGER.info("接收 的对列："+mqParam.getQueueName());
            LOGGER.info("回应 的对列："+mqParam.getReplyToQueueName());

            int openOptions = MQConstants.MQOO_INPUT_AS_Q_DEF | MQConstants.MQOO_OUTPUT | MQConstants.MQOO_INQUIRE;
            queue = qMgr.accessQueue(mqParam.getQueueName(), openOptions);

            int totalDataSize = 0;  //接收数据大小
            byte[] totalDataByte = null;    //接收的字节
            int depth = queue.getCurrentDepth();  //内容深度
            LOGGER.info("开始循环接收队列信息，数据数量：" + depth);
            while (depth-- > 0) {
                try {
                    rcvMessage = new MQMessage();
                    rcvMessage.replyToQueueName = mqParam.getReplyToQueueName();    //发送至：队列名称
                    //接收队列消息
                    queue.get(rcvMessage, gmo);

                    result = new StringBuffer();
                    while((totalDataSize = rcvMessage.getDataLength()) > 0){
                        totalDataByte = new byte[totalDataSize];
                        LOGGER.info("数据大小：{}",totalDataSize);
                        rcvMessage.readFully(totalDataByte);

                        result.append(new String(totalDataByte,"UTF-8"));
                    }
                    resultMap.put(rcvMessage.messageId,result.toString());

                    LOGGER.info("数据大小：{}，内容：{}",totalDataSize,result.toString());
                    LOGGER.info("MessageID: {}，Message-Type: {}",new String(rcvMessage.messageId,0,rcvMessage.messageId.length,"GBK"),rcvMessage.messageType);
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
            LOGGER.info("成功获取队列数：" + resultMap.size());
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

        return resultMap;
    }
}
