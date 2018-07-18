import javafish.clients.opc.JCustomOpc;
import javafish.clients.opc.JOpc;
import javafish.clients.opc.SynchReadItemExample;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.exception.ConnectivityException;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class Test2 {

    /**
     * 测试连接
     * @param args
     */
    public static void main(String[] args){
        JCustomOpc.coInitialize();
        JCustomOpc opc = new MyJOpc("localhost", "Kepware.KEPServerEX.V5", "JOPC1");

        try {
            opc.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JCustomOpc.coUninitialize();
        }
    }
}
