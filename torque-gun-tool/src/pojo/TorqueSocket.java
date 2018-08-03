package pojo;

import thread.HeartBeatThread;
import thread.TorqueListenerThread;

import java.net.Socket;

/**
 * @Description :   端口扭矩枪对象
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class TorqueSocket {

    private Socket socket;
    private String ip;
    private int port;
    private TorqueListenerThread listenerThread;
    private boolean listenerThreadStatus = false;   //监听线程是否结束
    private HeartBeatThread heartBeatThread;
    private boolean heartBeatThreadStatus = false;  //心跳线程是否结束

    public TorqueSocket(Socket socket) {
        this.socket = socket;
        this.ip = socket.getInetAddress().getHostAddress();
        this.port = socket.getPort();
    }

    public TorqueSocket(Socket socket, TorqueListenerThread listenerThread, HeartBeatThread heartBeatThread) {
        this.socket = socket;
        this.listenerThread = listenerThread;
        this.heartBeatThread = heartBeatThread;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public TorqueListenerThread getListenerThread() {
        return listenerThread;
    }

    public void setListenerThread(TorqueListenerThread listenerThread) {
        this.listenerThread = listenerThread;
    }

    public HeartBeatThread getHeartBeatThread() {
        return heartBeatThread;
    }

    public void setHeartBeatThread(HeartBeatThread heartBeatThread) {
        this.heartBeatThread = heartBeatThread;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean getListenerThreadStatus() {
        return listenerThreadStatus;
    }

    public void setListenerThreadStatus(boolean listenerThreadStatus) {
        this.listenerThreadStatus = listenerThreadStatus;
    }

    public boolean getHeartBeatThreadStatus() {
        return heartBeatThreadStatus;
    }

    public void setHeartBeatThreadStatus(boolean heartBeatThreadStatus) {
        this.heartBeatThreadStatus = heartBeatThreadStatus;
    }
}
