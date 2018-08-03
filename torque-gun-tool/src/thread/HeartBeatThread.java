package thread;

import pojo.TorqueParam;
import pojo.TorqueSocket;
import util.ConstWords;
import util.SocketTool;
import util.UtilTool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Description :   心跳和关闭端口线程
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class HeartBeatThread extends Thread {

    private TorqueSocket torqueSocket;  // 端口
    private int idx = 1;    //计数器
    private int maxTimeLimit = 300; //最大空闲时间，秒，超过后不发送心跳，并自动关闭连接
    private boolean workFlag = true;    //是否继续

    public HeartBeatThread(TorqueSocket s){
        this.torqueSocket = s;
        this.torqueSocket.setHeartBeatThread(this);
        this.torqueSocket.setHeartBeatThreadStatus(true);

        SocketTool.TORQUE_THREAD_LIST.add(this);
    }

    @Override
    public void run() {
        //System.out.println("心跳，检测socket状态：" + !torqueSocket.getSocket().isClosed());
        if(torqueSocket.getSocket().isClosed()){
            //已经关闭
        }else{
            while(workFlag && !torqueSocket.getSocket().isClosed()){
                if(idx < maxTimeLimit) {
                    if (idx % 14 == 0) {
                        //发送心跳{
                        //System.out.println("发送心跳：" + idx);
                        UtilTool.sendCommand(torqueSocket.getSocket(), ConstWords.HEART_BEAT_COMMAND);
                    }
                    idx++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }else{
                    System.out.println("操作时限：" + idx + "，结束心跳");
                    workFlag = false;
                }
            }
        }
        try {
            //端口关闭
            if(!torqueSocket.getSocket().isClosed()) {
                SocketTool.closeToqueSocket(torqueSocket);
            }
            SocketTool.TORQUE_SOCKET_MAP.remove(torqueSocket.getIp() + torqueSocket.getPort());
        } catch (IOException e) {
            System.out.println("关闭socket出错：" + e.getMessage());
        }
        System.out.println("心跳线程已结束...");
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
