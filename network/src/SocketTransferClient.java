import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class SocketTransferClient {
    public static void main(String[] args){
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("ip",8080),2000);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.print("");
            writer.flush();

            BufferedInputStream reader = new BufferedInputStream(socket.getInputStream());
            byte[] data = new byte[1024];
            while(true){
                int count = reader.available();
                reader.read(data,0,count);
                System.out.println(data.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
