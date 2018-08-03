package util;

import pojo.TorqueCountModel;
import pojo.TorqueParam;
import pojo.TorqueSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description :   端口处理工具类
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class SocketTool {
    //扭枪连接端口集合
    public static HashMap<String,TorqueSocket> TORQUE_SOCKET_MAP = new HashMap<String,TorqueSocket>();
    //扭枪采集数据统计集合
    public static HashMap<String, TorqueCountModel> TORQUE_COUNT_MAP = new HashMap<String,TorqueCountModel>();

    //线程统计
    public static List<Thread> TORQUE_THREAD_LIST = new ArrayList<Thread>();

    /**
     * 获取扭矩socket
     * @param ip
     * @param port
     * @return
     * @throws IOException
     */
    public synchronized static TorqueSocket getToqueSocket(String ip,int port,boolean needNew) throws IOException {
        TorqueSocket torqueSocket = TORQUE_SOCKET_MAP.get(ip + port);
        if (torqueSocket == null) {
            //还不存在
            torqueSocket = new TorqueSocket(new Socket(ip, port));
            TORQUE_SOCKET_MAP.put(ip + port, torqueSocket);
        }else {
            if (needNew) {
                //需要新的socket连接
                closeToqueSocket(torqueSocket,ip,port);
                torqueSocket = new TorqueSocket(new Socket(ip, port));
                TORQUE_SOCKET_MAP.put(ip + port, torqueSocket);
            }else {
                //不需要新的连接
                if (torqueSocket.getSocket().isClosed()) {
                    // 判断是否关闭
                    torqueSocket = new TorqueSocket(new Socket(ip, port));
                    TORQUE_SOCKET_MAP.put(ip + port, torqueSocket);
                }
            }
        }
        return torqueSocket;
    }

    public synchronized  static void closeToqueSocket(TorqueSocket torqueSocket) throws IOException {
        closeToqueSocket(torqueSocket,torqueSocket.getIp(),torqueSocket.getPort());
    }

    /**
     * 根据ip和端口关闭socket
     * @param torqueSocket
     * @param ip
     * @param port
     */
    public synchronized static void closeToqueSocket(TorqueSocket torqueSocket,String ip,int port) {
        try {
            if (torqueSocket != null) {
                closeToqueSocketThread(torqueSocket);
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

        try {
            TorqueSocket torqueSocketInMap = TORQUE_SOCKET_MAP.get(ip + port);
            if (torqueSocketInMap != null) {
                closeToqueSocketThread(torqueSocketInMap);
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        TORQUE_SOCKET_MAP.remove(ip + port);
    }

    /**
     * 关闭线程以及socket
     * @param torqueSocket
     * @throws IOException
     */
    public synchronized static void closeToqueSocketThread(TorqueSocket torqueSocket) throws IOException {
        System.out.println("关闭:" + torqueSocket + "," + torqueSocket.getListenerThread());
        //关闭心跳线程
        if(torqueSocket.getHeartBeatThreadStatus()) {
            torqueSocket.getHeartBeatThread().interrupt();
            torqueSocket.setHeartBeatThreadStatus(false);
        }
        //关闭获取数据线程
        if(torqueSocket.getListenerThreadStatus()) {
            torqueSocket.getListenerThread().interrupt();
            torqueSocket.setListenerThreadStatus(false);
        }
        if(!torqueSocket.getSocket().isClosed()) {
            torqueSocket.getSocket().close();
        }
    }

    /**
     * 统计采集的数据
     * @param vin
     * @param param
     * @throws IOException
     */
    public synchronized static void addTORQUE_COUNT_MAP(String vin, TorqueParam param) throws IOException {
        if(TORQUE_COUNT_MAP.size() > ConstWords.TORQUE_MODEL_MAX_COUNT){
            //已超出保存的过程条码扭矩记录
            TORQUE_COUNT_MAP.clear(); //清空
        }

        TorqueCountModel countModel = TORQUE_COUNT_MAP.get(vin);
        if(countModel == null){
            //还不存在
            countModel = new TorqueCountModel(vin);
            if(param.getDataSuccess()){
                //扭矩扭角都正常的数据
                countModel.getSuccessParamList().add(param);
            }else{
                countModel.getFailedParamList().add(param);
            }

            TORQUE_COUNT_MAP.put(vin,countModel);
        }else{
            if(param.getDataSuccess()){
                //扭矩扭角都正常的数据
                countModel.getSuccessParamList().add(param);
            }else{
                countModel.getFailedParamList().add(param);
            }
        }
    }

    /**
     * 根据条码获取已采集的数据
     * @param vin
     * @throws IOException
     */
    public synchronized static TorqueCountModel getTORQUE_COUNT_MAP(String vin) throws IOException {
        TorqueCountModel countModel = TORQUE_COUNT_MAP.get(vin);
        if(countModel == null){
            countModel = new TorqueCountModel(vin);
        }
        return countModel;
    }
}
