import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class TCPTransferServer {
    public static void main(String[] args) throws Exception{
        ServerSocket ss = null;
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        BufferedReader input = null;

        try {
            // 监听3333端口
            ss = new ServerSocket(3333);
            // 等待接收客户端的请求

            socket = ss.accept();
            while (true) {
                // 获取连接对象的输入流
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // 获取客户端的输入信息
                String str = in.readLine();

                input = new BufferedReader(new InputStreamReader(System.in));

                System.out.println("服务器显示-->客户端输入数据：" + str);

                out = new PrintWriter(socket.getOutputStream(), true);
                // 将数据输出到客户端
                String info = "from server return = " + input.readLine();

                out.println(info);
                out.flush();
            }
        }catch (Exception e){
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
            if(ss != null){
                ss.close();
            }
        }
    }

}
