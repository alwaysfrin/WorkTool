import pojo.TorqueParam;
import util.ConstWords;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class TCPTransfer {

    public static void main(String[] args) throws IOException {
        TCPTransfer tool = new TCPTransfer();
        String host = "10.34.36.144";
        int port = 4545;

        //tool.checkHostAndPort(host, port);

        //tool.doProgram(host,port);

        Map<String,String> map = new HashMap<String,String>();
        map.put("10.34.36.1444545","123");
        System.out.println(map.size());
    }


    public void doProgram(String host,int port) throws IOException {
        Socket socket = null;
        BufferedReader in = null;
        OutputStream out = null;
        InputStream input = null;

        try{
            System.out.print("请求tcp连接--->");
            socket = new Socket(host,port);

            System.out.println("已连接!Success!");

            out = socket.getOutputStream();
            input = socket.getInputStream();

            System.out.println("==开始通讯==" + ConstWords.START_COMMAND);
            this.sendCommand(ConstWords.START_COMMAND,out);
            getReturnData(input);

            /*System.out.println("==开始订阅==" + ConstWords.SUBSCRIBTE_COMMAND);
            this.sendCommand(ConstWords.SUBSCRIBTE_COMMAND,out);
            getReturnData(input);
            System.out.println("==开始订阅==" + ConstWords.SUBSCRIBTE_COMMAND);
            this.sendCommand(ConstWords.SUBSCRIBTE_COMMAND,out);
            getReturnData(input);
            System.out.println("第1次");
            getReturnData(input);
            this.sendCommand(ConstWords.SUBSCRIBTE_SUBMIT_COMMAND,out);*/

            String snCommand = ConstWords.getSNCommand("123456");
            System.out.println("==发送条码==" + snCommand);
            this.sendCommand(snCommand,out);
            getReturnData(input);

            /*System.out.println("==开始订阅==" + ConstWords.SUBSCRIBTE_COMMAND);
            this.sendCommand(ConstWords.SUBSCRIBTE_COMMAND,out);
            getReturnData(input);

            System.out.println("第1次");
            getReturnData(input);
            this.sendCommand(ConstWords.SUBSCRIBTE_SUBMIT_COMMAND,out);*/
            /*


            //System.out.println("==请求获取数据==" + ConstWords.SUBSCRIBTE_SUBMIT_COMMAND);
            System.out.println("第2次");
            getReturnData(input);
            this.sendCommand(ConstWords.SUBSCRIBTE_SUBMIT_COMMAND,out);

            System.out.println("第3次");
            getReturnData(input);
            //this.sendCommand(ConstWords.SUBSCRIBTE_SUBMIT_COMMAND,out);

            System.out.println("重复第3次");
            this.sendCommand(ConstWords.SUBSCRIBTE_LAST_COMMAND,out);
            getReturnData(input);
            this.sendCommand(ConstWords.SUBSCRIBTE_SUBMIT_COMMAND,out);

            System.out.println("第4次");
            getReturnData(input);
            this.sendCommand(ConstWords.SUBSCRIBTE_SUBMIT_COMMAND,out);

            System.out.println("==通讯结束==" + ConstWords.END_COMMAND);
            this.sendCommand(ConstWords.END_COMMAND,out);
            getReturnData(input);*/


            //监听端口
            /*n

            //监听端口
            new ListenPortThread(socket.getInputStream()).run();
            Thread.sleep(1000);*/

        }catch(Exception e){
            System.out.println("无法连接!" + e.getMessage());
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

    public void checkHost(String host){
        try {
            InetAddress.getByName(host).isReachable(100);
            System.out.println("【"+host+"】可访问");
        } catch (IOException e) {
            System.out.println("【"+host+"】不可访问");
            e.printStackTrace();
        }
    }

    public void checkHostAndPort(String host, int port){
        try {
            InetAddress.getByName(host).isReachable(100);
            System.out.print("【"+host+"】可访问，");
        } catch (IOException e) {
            System.out.print("【"+host+"】不可访问，");
            e.printStackTrace();
        }
        Socket socket = new Socket();
        try {
            SocketAddress add = new InetSocketAddress(host, port);
            socket.connect(add, 100);
            System.out.println("【端口："+port+"】 可以访问");
        }catch(Exception e){
            System.out.println("【端口："+port+"】 无法访问");
        }
    }

    public void getReturnData(InputStream input){
        /*input = socket.getInputStream();
        byte[] buffer = new byte[1024];
        int length = 0;

        try {
            while((length = input.read(buffer)) != -1){
                String result = new String(buffer,0,length);
                System.out.println("长度："+result.length()+"/"+length+",获取数据：" + result);
            }
            System.out.println("===获取数据结束===");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //System.out.println("===获取数据开始===");
        try {
            byte[] buffer = new byte[1024];
            int length = input.read(buffer);
            if(length > 0) {
                String result = new String(buffer, 0, length);
                System.out.println("长度：" + result.length() + "/" + length + ",获取数据：" + result);

                TorqueParam param = new TorqueParam(result);
                if(result.indexOf("006") > 0) {
                    System.out.println("返回代码【" + param.getCommandCode() + "】操作状态【" + param.isSuccess() + "】获取扭矩：" + param.getTorque()
                            + "，全部次数：" + param.getBatchSize() + "，成功次数：" + param.getBatchCounter() + "，本批成功状态：" + param.getBatchStatus()
                            + "，扭矩状态：" + param.getBatchSize() + "，扭角状态：" + param.getBatchCounter() + "，过程条码：" + param.getSerialNumber());
                }else{
                    System.out.println("返回代码【" + param.getCommandCode() + "】操作状态【" + param.isSuccess()+"】");
                }
            }else{
                System.out.println("没有数据...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("===获取数据结束===");
    }

    public void getReturnDataWhile(InputStream input){
        byte[] buffer = new byte[1024];
        int length = 0;

        System.out.println("===获取数据开始===");
        try {
            while((length = input.read(buffer)) != -1){
                String result = new String(buffer,0,length);
                System.out.println("长度："+result.length()+"/"+length+",获取数据：" + result);
            }
            System.out.println("===获取数据结束===");
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
            System.out.println("开启端口输入监听...");
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;

            try {
                while((length = input.read(buffer)) != -1){
                    arrayOutputStream.write(buffer,0,length);
                    System.out.println("获取数据：" + buffer.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("端口输入监听结束...");
        }
    }
}
