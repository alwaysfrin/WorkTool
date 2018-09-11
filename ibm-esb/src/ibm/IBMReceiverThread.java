package ibm;

import com.ibm.mq.constants.MQConstants;
import pojo.MQParam;
import com.ibm.mq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description :
 * @Reference :
 *      2027 missing reply queue
 *      2033 NO_MSG_AVAILABLE  队列中没有数据
 *      2085 MQRC_UNKNOWN_OBJECT_NAME   队列名称错误
 * @Author :
 * @Date :
 * @Modify :
 **/
public class IBMReceiverThread implements Runnable{
    public static final Logger LOGGER = LoggerFactory.getLogger(IBMReceiverThread.class);
    private MQParam mqParam;
    private MQQueueManager qMgr;
    private MQMessage rcvMessage;
    private MQQueue queue;
    public static final int WAIT_INTERVAL = 5000;    //接收队列响应时间
    private int dataSize;

    public IBMReceiverThread(MQParam param){
        this.mqParam = param;
    }

    public void run() {
        try {
            recvMessage();
        } catch (MQException e) {
            LOGGER.error("接受队列信息出错：" + e.getMessage());
        }
    }

    public void recvMessage() throws MQException {
        MQEnvironment.hostname = mqParam.getHost();
        MQEnvironment.port = mqParam.getPort();
        MQEnvironment.channel = mqParam.getChannel();
        MQEnvironment.CCSID = mqParam.getCcsid();
        MQEnvironment.userID = mqParam.getUserId();
        MQEnvironment.password = mqParam.getPassword();

        //msgId和队列内容
        Map<byte[],String> resultMap = new HashMap<>();
        try {
            LOGGER.info("进入线程开始接收队列数据");
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

            int openOptions = MQConstants.MQOO_INPUT_AS_Q_DEF | MQConstants.MQOO_OUTPUT | MQConstants.MQOO_INQUIRE;
            queue = qMgr.accessQueue(mqParam.getQueueName(), openOptions);

            StringBuffer result = null;
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

                    //LOGGER.info("数据大小：{}，内容：{}",totalDataSize,result.toString());
                    //LOGGER.info("MessageID: {}，Message-Type: {}",new String(rcvMessage.messageId,0,rcvMessage.messageId.length,"GBK"),rcvMessage.messageType);
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
            LOGGER.info("已全部获取数据!");
        } catch (MQException ex) {
            LOGGER.info("获取mq数据出错: Completion Code "
                            + ex.completionCode + " Reason Code "
                            + ex.reasonCode + ex.getMessage());
        } finally {
            if(queue != null) {
                queue.close();
            }
            if(qMgr != null){
                qMgr.disconnect();
                qMgr.close();
            }
        }

        try {
            LOGGER.info("获取队列{}条：",resultMap.size());
            for (byte[] msgId : resultMap.keySet()) {
                LOGGER.info("MessageID: {}，数据: {}", new String(msgId, 0, msgId.length, "GBK"), resultMap.get(msgId));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

