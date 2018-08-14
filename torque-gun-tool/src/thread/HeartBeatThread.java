package thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.TorqueSocket;
import util.TorqueConstWords;
import util.TorqueTool;

import java.io.IOException;

/**
 * @Description :   心跳和关闭端口线程
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class HeartBeatThread extends Thread {
    public static final Logger LOGGER = LoggerFactory.getLogger(HeartBeatThread.class);

    private TorqueSocket torqueSocket;  // 端口
    private int idx = 1;    //计数器
    private int maxTimeLimit = 300; //最大空闲时间，秒，超过后不发送心跳，并自动关闭连接
    private boolean workFlag = true;    //是否继续

    public HeartBeatThread(TorqueSocket s){
        this.torqueSocket = s;
        this.torqueSocket.setHeartBeatThread(this);
        this.torqueSocket.setHeartBeatThreadStatus(true);

        TorqueTool.TORQUE_THREAD_LIST.add(this);
    }

    @Override
    public void run() {
        //LOGGER.info("心跳，检测socket状态：" + !torqueSocket.getSocket().isClosed());
        if(torqueSocket.getSocket().isClosed()){
            //已经关闭
        }else{
            while(workFlag && !torqueSocket.getSocket().isClosed()){
                if(idx < maxTimeLimit) {
                    if (idx % 14 == 0) {
                        //发送心跳{
                        //LOGGER.info("发送心跳：" + idx);
                        TorqueTool.sendCommand(torqueSocket.getSocket(), TorqueConstWords.HEART_BEAT_COMMAND);
                    }
                    idx++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }else{
                    LOGGER.info("操作时限：" + idx + "，结束心跳");
                    workFlag = false;
                }
            }
        }
        try {
            //端口关闭
            if(!torqueSocket.getSocket().isClosed()) {
                TorqueTool.closeToqueSocket(torqueSocket);
            }
            TorqueTool.TORQUE_SOCKET_MAP.remove(torqueSocket.getIp() + torqueSocket.getPort());
        } catch (IOException e) {
            LOGGER.error("关闭socket出错：" + e.getMessage());
        }
        LOGGER.info("心跳线程已结束...");
    }

    /**
     * 停止发送心跳信息
     */
    public void stopWork(){
        workFlag = false;
    }

    /**
     * 重置时间
     */
    public void resetTime() {
        idx = 1;
    }
}
