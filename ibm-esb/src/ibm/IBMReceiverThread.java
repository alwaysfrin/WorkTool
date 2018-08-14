package ibm;

import com.ibm.mq.constants.MQConstants;
import pojo.MQParam;
import com.ibm.mq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
                    LOGGER.info("数据大小：" + dataSize +"，内容：" + rcvMessage.readStringOfByteLength(dataSize));
                    LOGGER.info(" ID: " + new String(rcvMessage.messageId,0,rcvMessage.messageId.length,"GBK") + " Type: " + rcvMessage.messageType);
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
    }
}

