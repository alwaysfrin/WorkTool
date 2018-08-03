package servlet;

import pojo.TorqueParam;
import pojo.TorqueSocket;
import thread.HeartBeatThread;
import thread.TorqueListenerThread;
import util.ConstWords;
import util.SocketTool;
import util.UtilTool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.Socket;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
@WebServlet(name = "SocketService",value="/socket-service")
public class SocketService extends HttpServlet {
    private static Socket socket;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String host = "10.34.36.144";
        int port = 4545;

        String method = request.getParameter("method");
        if("start".equals(method)){
            if(UtilTool.checkHostAndPort(host,port)) {
                if(socket != null){
                    socket.close();
                    socket = null;
                }
                try {
                    System.out.print("请求tcp连接--->");
                    socket = new Socket(host, port);
                    System.out.println("已连接!Success!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if("stop".equals(method)){
            if(socket != null && !socket.isClosed()){
                System.out.println("关闭socket ： " + socket.isClosed());
                try {
                    long l1 = System.currentTimeMillis();
                    socket.close();
                    System.out.println("端口关闭："+socket.isClosed()+"，关闭端口耗时" + (System.currentTimeMillis() - l1) + "ms");
                } catch (Exception e) {
                    System.out.println("端口关闭出现异常，端口状态 ： " + socket.isClosed());
                    e.printStackTrace();
                }
            }else{
                System.out.println("关闭socket失败 ： " + socket.isClosed());
            }
        }else if("getdata".equals(method)){
            try {
                System.out.println("==开始通讯==" + ConstWords.START_COMMAND);
                UtilTool.sendCommand(socket, ConstWords.START_COMMAND);
                TorqueParam param = UtilTool.packTorqueParam(socket);
                System.out.println("返回参数：" + param.getCommandCode() + " " + param.isSuccess());

                System.out.println("==开始订阅==" + ConstWords.SUBSCRIBTE_COMMAND);
                UtilTool.sendCommand(socket,ConstWords.SUBSCRIBTE_COMMAND);
                param = UtilTool.packTorqueParam(socket);
                System.out.println("返回参数：" + param.getCommandCode() + " " + param.isSuccess());

                if(param.isSuccess()){
                    boolean continueWork = true;
                    while(continueWork) {
                        //心跳
                        //new HeartBeatThread(socket).start();

                        param = UtilTool.packTorqueParam(socket);
                        System.out.println("返回参数：" + param.getCommandCode() + " " + param.isSuccess() + "，获取扭矩：" + param.getTorque()
                                            +  "，getBatchSize：" + param.getBatchSize() +  "，getBatchCounter：" + param.getBatchCounter() + "，getBatchStatus：" + param.getBatchStatus());
                    }
                }
            }catch(Exception e){
                System.out.println("==获取数据出错==");
                e.printStackTrace();
            }
        }else if("dowork".equals(method)){
            String vin = request.getParameter("vin");   //过程条码
            String timesStr = request.getParameter("times");
            int times = 1;
            try {
                times = Integer.parseInt(timesStr);
            }catch (Exception e){
            }
            try {
                /*************** 先连接扭矩枪 ***************/
                boolean needNew = true;
                if(needNew) {
                    TorqueSocket torqueSocket = SocketTool.getToqueSocket(host, port, true);
                    //启动监听线程
                    new TorqueListenerThread(torqueSocket, times, vin).start();
                }else{
                    //不需要全新的连接
                    TorqueSocket torqueSocket = SocketTool.getToqueSocket(host, port, false);
                    if(!torqueSocket.getListenerThreadStatus()){
                        //线程已关闭
                        //启动新监听线程
                        new TorqueListenerThread(torqueSocket, times, vin).start();
                    }
                }
            }catch(Exception e){
                System.out.println("==获取数据出错==");
                e.printStackTrace();
            }
        }else if("stopwork".equals(method)){
            try {
                SocketTool.closeToqueSocket(null,host,port);
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("==关闭连接出错==");
            }
        }else if("cleanthread".equals(method)){
            SocketTool.TORQUE_THREAD_LIST.clear();
        }

        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}
