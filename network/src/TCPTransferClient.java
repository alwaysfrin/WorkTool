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
public class TCPTransferClient implements Runnable{
    public static void main(String[] args){
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        BufferedReader input = null;
        try{
            // 请求指定ip和端口号的服务器
            socket = new Socket("127.0.0.1", 3333);

            while (true) {
                out = new PrintWriter(socket.getOutputStream(), true);
                // 接收控制台的输入
                input = new BufferedReader(new InputStreamReader(System.in));
                String info = input.readLine();

                out.println(info);
                out.flush();

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String str = in.readLine();
                System.out.println("客户端显示--》服务器的信息：" + str);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try{
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
            }catch(IOException e){
                e.printStackTrace();;
            }
        }
    }


    @Override
    public void run() {

    }
}
