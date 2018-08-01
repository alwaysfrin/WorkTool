import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class SocketTransferServer {

    public static void main(String[] args){
        String host = "10.34.36.144";
        int port = 4545;
        
        checkHost(host);
        checkHostAndPort(host,port);
    }

    public static void checkHost(String host) {
        try{
            InetAddress.getByName(host).isReachable(100);
            System.out.println("【"+host+"】可以访问");
        }catch(Exception e){
            System.out.println("【"+host+"】不可访问");
        }
    }

    public static void checkHostAndPort(String host,int port){
        try{
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host,port),100);
            System.out.println("【"+host+":"+port+"】可以访问");
        }catch(Exception e){
            System.out.println("【"+host+":"+port+"】不可访问");
        }
    }
}
