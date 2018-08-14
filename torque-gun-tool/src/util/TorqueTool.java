package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.TorqueCountModel;
import pojo.TorqueParam;
import pojo.TorqueSocket;
import thread.TorqueListenerThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description : 端口处理工具类
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class TorqueTool {
    public static final Logger LOGGER = LoggerFactory.getLogger(TorqueTool.class);

    //扭枪连接端口集合
    public static HashMap<String, TorqueSocket> TORQUE_SOCKET_MAP = new HashMap<String,TorqueSocket>();
    //扭枪采集数据统计集合
    public static HashMap<String, TorqueCountModel> TORQUE_COUNT_MAP = new HashMap<String,TorqueCountModel>();

    //线程统计
    public static List<Thread> TORQUE_THREAD_LIST = new ArrayList<Thread>();

    /**
     * 自动开启线程，获取扭矩枪信息
     * @param ip
     * @param port
     * @param vin
     * @return
     * @throws IOException
     */
    public static TorqueSocket startSimpleToqueThread(String ip,int port,String vin) throws IOException {
        TorqueSocket torqueSocket = null;
        TorqueCountModel countModel = TORQUE_COUNT_MAP.get(vin);
        if(countModel == null){
            //还未存在此条码扭矩信息，新连接
            torqueSocket = getToqueSocket(ip,port,true);
        }else{
            //已存在条码扭矩，查看是否有老的连接
            torqueSocket = getToqueSocket(ip,port,false);
        }

        if(!torqueSocket.getListenerThreadStatus()){
            //线程已关闭
            //启动新监听线程
            new TorqueListenerThread(torqueSocket, vin).start();
        }

        return torqueSocket;
    }

    /**
     * 获取扭矩socket
     * @param ip
     * @param port
     * @return
     * @throws IOException
     */
    public static TorqueSocket getToqueSocket(String ip,int port,boolean needNew) throws IOException {
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

    public static void closeToqueSocket(TorqueSocket torqueSocket) throws IOException {
        closeToqueSocket(torqueSocket,torqueSocket.getIp(),torqueSocket.getPort());
    }

    /**
     * 根据ip和端口关闭socket
     * @param torqueSocket
     * @param ip
     * @param port
     */
    public static void closeToqueSocket(TorqueSocket torqueSocket,String ip,int port) {
        try {
            if (torqueSocket != null) {
                closeToqueSocketThread(torqueSocket);
            }
        }catch(IOException e){
            LOGGER.info(e.getMessage());
        }

        try {
            TorqueSocket torqueSocketInMap = TORQUE_SOCKET_MAP.get(ip + port);
            if (torqueSocketInMap != null) {
                closeToqueSocketThread(torqueSocketInMap);
            }
        }catch(IOException e){
            LOGGER.info(e.getMessage());
        }
        TORQUE_SOCKET_MAP.remove(ip + port);
    }

    /**
     * 关闭线程以及socket
     * @param torqueSocket
     * @throws IOException
     */
    public static void closeToqueSocketThread(TorqueSocket torqueSocket) throws IOException {
        LOGGER.info("关闭:" + torqueSocket + "," + torqueSocket.getListenerThread());
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
    public static void addTORQUE_COUNT_MAP(String vin, TorqueParam param) throws IOException {
        if(TORQUE_COUNT_MAP.size() > TorqueConstWords.TORQUE_MODEL_MAX_COUNT){
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
    public static TorqueCountModel getTORQUE_COUNT_MAP(String vin) throws IOException {
        TorqueCountModel countModel = TORQUE_COUNT_MAP.get(vin);
        if(countModel == null){
            countModel = new TorqueCountModel(vin);
        }
        return countModel;
    }

    /**
     * 检测主机和端口是否正常
     * @param host
     * @param port
     * @return
     */
    public static boolean checkHostAndPort(String host, int port){
        boolean isConnect = true;
        try {
            InetAddress.getByName(host).isReachable(100);
            System.out.print("【"+host+"】可访问，");
        } catch (IOException e) {
            System.out.print("【"+host+"】不可访问，");
            isConnect = false;
            e.printStackTrace();
        }
        Socket socket = new Socket();
        try {
            SocketAddress add = new InetSocketAddress(host, port);
            socket.connect(add, 100);
            LOGGER.info("【端口："+port+"】 可以访问");
        }catch(Exception e){
            isConnect = false;
            LOGGER.info("【端口："+port+"】 无法访问");
        }
        return isConnect;
    }

    /**
     * 发送命令至扭矩枪
     * @param socket
     * @param command
     * @return
     */
    public static boolean sendCommand(Socket socket,String command) {
        try {
            if (socket != null && !socket.isClosed()) {
                OutputStream os = socket.getOutputStream();
                os.write(command.getBytes());
                os.flush();

                return true;
            }else{
                return false;
            }
        }catch(IOException e){
            return false;
        }
    }

    public static String getInputData(Socket socket) throws IOException {
        InputStream input = socket.getInputStream();
        String result = null;
        byte[] buffer = new byte[1024];

        int length = input.read(buffer);
        if(length > 0) {
            result = new String(buffer, 0, length);
            //LOGGER.info("长度：" + result.length() + "，获取数据：" + result);
        }
        return result;
    }

    /**
     * 封装普通扭矩枪返回的信息
     * @param socket
     * @return
     * @throws IOException
     */
    public static TorqueParam packTorqueParam(Socket socket) throws IOException {
        TorqueParam param = null;
        String data = getInputData(socket);
        if(data != null && data.length() > 0){
            param = new TorqueParam(data);
            if("9999".equals(param.getCommandCode())){
                // 返回心跳信息，递归
                param = packTorqueParam(socket);
            }
        }

        return param;
    }

    /**
     * 获取扭矩数据
     * @param socket
     * @return
     * @throws IOException
     */
    public static TorqueParam getTorqueRequest(Socket socket) throws IOException {
        TorqueParam param = null;
        String data = getInputData(socket);
        if(data.length() > 0) {
            param = new TorqueParam(data);
            if ("9999".equals(param.getCommandCode())) {
                // 返回心跳信息，递归
                param = getTorqueRequest(socket);
            }else if (data.indexOf("006") > 0) {
                //扭矩数据，返回ack
                sendCommand(socket, TorqueConstWords.SUBSCRIBTE_SUBMIT_COMMAND);
            }
        }

        return param;
    }
}
