import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.TorqueCountModel;
import pojo.TorqueParam;
import pojo.TorqueSocket;
import util.TorqueConstWords;
import util.TorqueTool;

import java.io.*;
import java.net.*;

/**
 * @Description:   扭矩枪测试类
 * @Refrence:
 * @Author: frin
 * @Date: 2018/8/4 14:29
 * @Modify:
 **/
public class TorqueTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(TorqueTest.class);

    public static void main(String[] args) throws IOException {
        String host = "10.34.36.144";
        int port = 4545;

        /************ 从扭矩枪开始获取数据获取数据 ***********/
        String vin = "123456";  //过程条码
        TorqueTool.startSimpleToqueThread(host,port,vin);

        /**************** 从map或数据库中获取对应的数据 *************/
        TorqueCountModel torqueCountModel = TorqueTool.TORQUE_COUNT_MAP.get(vin);
        if(torqueCountModel != null){
            //已经采集到了数据
            LOGGER.info("成功的扭矩记录数：" + torqueCountModel.getSuccessCount());
            for(TorqueParam param : torqueCountModel.getSuccessParamList()){
                LOGGER.info("返回参数：" + param.getCommandCode() + " " + param.isSuccess() + "，获取扭矩：" + param.getTorque()
                        +  "，需要成功的扭矩数：" + param.getBatchSize() + "，已成功的扭矩数：" + param.getBatchCounter() + "，本次任务的状态：" + param.getBatchStatus());
            }

            LOGGER.info("失败的扭矩记录数：" + torqueCountModel.getFailedCount());
            for(TorqueParam param : torqueCountModel.getFailedParamList()){
                LOGGER.info("返回参数：" + param.getCommandCode() + " " + param.isSuccess() + "，获取扭矩：" + param.getTorque()
                        +  "，需要成功的扭矩数：" + param.getBatchSize() + "，已成功的扭矩数：" + param.getBatchCounter() + "，本次任务的状态：" + param.getBatchStatus());
            }
        }

        /****************** 普通发送命令和接收返回信息 **********************/
        TorqueSocket torqueSocket = TorqueTool.getToqueSocket(host,port,true);
        TorqueTool.sendCommand(torqueSocket.getSocket(), TorqueConstWords.SUBSCRIBTE_COMMAND);
        TorqueParam param = TorqueTool.packTorqueParam(torqueSocket.getSocket());
        if(param.isSuccess()){
            LOGGER.info("扭矩枪返回成功，返回参数：" + param.getCommandCode() + " " + param.isSuccess());
        }else{
            LOGGER.info("扭矩枪返回失败，返回参数：" + param.getCommandCode() + " " + param.isSuccess());
        }


        /****************** 其他测试 **********************/
        //检测端口和ip
        TorqueTest tool = new TorqueTest();
        TorqueTool.checkHostAndPort(host, port);
        //与扭矩枪通讯（单线程）
        tool.doProgram(host,port);
    }

    /**
     * 普通功能测试
     * @param host
     * @param port
     * @throws IOException
     */
    public void doProgram(String host,int port) throws IOException {
        Socket socket = null;
        BufferedReader in = null;
        OutputStream out = null;
        InputStream input = null;

        try{
            System.out.print("请求tcp连接--->");
            socket = new Socket(host,port);

            LOGGER.info("已连接!Success!");

            out = socket.getOutputStream();
            input = socket.getInputStream();

            LOGGER.info("==开始通讯==" + TorqueConstWords.START_COMMAND);
            this.sendCommand(TorqueConstWords.START_COMMAND,out);
            getReturnData(input);

            /*LOGGER.info("==开始订阅==" + TorqueConstWords.SUBSCRIBTE_COMMAND);
            this.sendCommand(TorqueConstWords.SUBSCRIBTE_COMMAND,out);
            getReturnData(input);
            LOGGER.info("==开始订阅==" + TorqueConstWords.SUBSCRIBTE_COMMAND);
            this.sendCommand(TorqueConstWords.SUBSCRIBTE_COMMAND,out);
            getReturnData(input);
            LOGGER.info("第1次");
            getReturnData(input);
            this.sendCommand(TorqueConstWords.SUBSCRIBTE_SUBMIT_COMMAND,out);*/

            String snCommand = TorqueConstWords.getSNCommand("123456");
            LOGGER.info("==发送条码==" + snCommand);
            this.sendCommand(snCommand,out);
            getReturnData(input);

            /*LOGGER.info("==开始订阅==" + TorqueConstWords.SUBSCRIBTE_COMMAND);
            this.sendCommand(TorqueConstWords.SUBSCRIBTE_COMMAND,out);
            getReturnData(input);

            LOGGER.info("第1次");
            getReturnData(input);
            this.sendCommand(TorqueConstWords.SUBSCRIBTE_SUBMIT_COMMAND,out);*/
            /*


            //LOGGER.info("==请求获取数据==" + TorqueConstWords.SUBSCRIBTE_SUBMIT_COMMAND);
            LOGGER.info("第2次");
            getReturnData(input);
            this.sendCommand(TorqueConstWords.SUBSCRIBTE_SUBMIT_COMMAND,out);

            LOGGER.info("第3次");
            getReturnData(input);
            //this.sendCommand(TorqueConstWords.SUBSCRIBTE_SUBMIT_COMMAND,out);

            LOGGER.info("重复第3次");
            this.sendCommand(TorqueConstWords.SUBSCRIBTE_LAST_COMMAND,out);
            getReturnData(input);
            this.sendCommand(TorqueConstWords.SUBSCRIBTE_SUBMIT_COMMAND,out);

            LOGGER.info("第4次");
            getReturnData(input);
            this.sendCommand(TorqueConstWords.SUBSCRIBTE_SUBMIT_COMMAND,out);

            LOGGER.info("==通讯结束==" + TorqueConstWords.END_COMMAND);
            this.sendCommand(TorqueConstWords.END_COMMAND,out);
            getReturnData(input);*/


            //监听端口
            /*n

            //监听端口
            new ListenPortThread(socket.getInputStream()).run();
            Thread.sleep(1000);*/

        }catch(Exception e){
            LOGGER.info("无法连接!" + e.getMessage());
            e.printStackTrace();
        }finally {
            if(input != null){
                input.close();
            }
            if(out != null){
                out.close();
            }
            if(in != null){
                in.close();
            }
            if(socket != null){
                socket.close();
            }
        }
    }

    public void sendCommand(String command,OutputStream os) throws IOException {
        os.write(command.getBytes());
        os.flush();
    }

    /**
     * 检测url是否可连接
     *
     * @param reqUrl HTTP请求URL
     * @param cto 连接时间
     * @param cto 读取时间
     * @return 是否可连接，不能连接则显示原因
     */
    public boolean urlCheckNet(String reqUrl,int cto,int rto) {
        boolean isConnect = false;
        HttpURLConnection url_con = null;
        try {
            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setConnectTimeout(cto);
            url_con.setReadTimeout(rto);
            url_con.connect();

            //LOGGER.info(url_con.getHeaderField(0) + " -- " + url_con.getResponseMessage() + " -- " + url_con.getResponseCode());
            if(url_con.getResponseCode() == 200 || new Integer(url_con.getResponseCode()).toString().startsWith("2")
                    || url_con.getResponseMessage().indexOf( "OK ")> 0){
                isConnect = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
        return isConnect;
    }

    public void getReturnData(InputStream input){
        /*input = socket.getInputStream();
        byte[] buffer = new byte[1024];
        int length = 0;

        try {
            while((length = input.read(buffer)) != -1){
                String result = new String(buffer,0,length);
                LOGGER.info("长度："+result.length()+"/"+length+",获取数据：" + result);
            }
            LOGGER.info("===获取数据结束===");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //LOGGER.info("===获取数据开始===");
        try {
            byte[] buffer = new byte[1024];
            int length = input.read(buffer);
            if(length > 0) {
                String result = new String(buffer, 0, length);
                LOGGER.info("长度：" + result.length() + "/" + length + ",获取数据：" + result);

                TorqueParam param = new TorqueParam(result);
                if(result.indexOf("006") > 0) {
                    LOGGER.info("返回代码【" + param.getCommandCode() + "】操作状态【" + param.isSuccess() + "】获取扭矩：" + param.getTorque()
                            + "，全部次数：" + param.getBatchSize() + "，成功次数：" + param.getBatchCounter() + "，本批成功状态：" + param.getBatchStatus()
                            + "，扭矩状态：" + param.getBatchSize() + "，扭角状态：" + param.getBatchCounter() + "，过程条码：" + param.getSerialNumber());
                }else{
                    LOGGER.info("返回代码【" + param.getCommandCode() + "】操作状态【" + param.isSuccess()+"】");
                }
            }else{
                LOGGER.info("没有数据...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //LOGGER.info("===获取数据结束===");
    }

    public void getReturnDataWhile(InputStream input){
        byte[] buffer = new byte[1024];
        int length = 0;

        LOGGER.info("===获取数据开始===");
        try {
            while((length = input.read(buffer)) != -1){
                String result = new String(buffer,0,length);
                LOGGER.info("长度："+result.length()+"/"+length+",获取数据：" + result);
            }
            LOGGER.info("===获取数据结束===");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ListenPortThread implements Runnable{
        public InputStream input;
        public ListenPortThread(InputStream is){
            this.input = is;
        }
        @Override
        public void run() {
            LOGGER.info("开启端口输入监听...");
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;

            try {
                while((length = input.read(buffer)) != -1){
                    arrayOutputStream.write(buffer,0,length);
                    LOGGER.info("获取数据：" + buffer.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            LOGGER.info("端口输入监听结束...");
        }
    }
}
