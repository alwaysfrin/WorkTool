package util;

import pojo.TorqueParam;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class UtilTool {

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
            System.out.println("【端口："+port+"】 可以访问");
        }catch(Exception e){
            isConnect = false;
            System.out.println("【端口："+port+"】 无法访问");
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
            //System.out.println("长度：" + result.length() + "，获取数据：" + result);
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
    public  static TorqueParam getTorqueRequest(Socket socket) throws IOException {
        TorqueParam param = null;
        String data = getInputData(socket);
        if(data.length() > 0) {
            param = new TorqueParam(data);
            if ("9999".equals(param.getCommandCode())) {
                // 返回心跳信息，递归
                param = getTorqueRequest(socket);
            }else if (data.indexOf("006") > 0) {
                //扭矩数据，返回ack
                UtilTool.sendCommand(socket, ConstWords.SUBSCRIBTE_SUBMIT_COMMAND);
            }
        }

        return param;
    }
}
