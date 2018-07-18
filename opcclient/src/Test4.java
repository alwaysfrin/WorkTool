import javafish.clients.opc.JEasyOpc;
import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class Test4 {

    /**
     * 线程监听
     * @param args
     */
    public static void main(String[] args){

        JEasyOpc.coInitialize();

        JEasyOpc jopc = new JEasyOpc("localhost", "Kepware.KEPServerEX.V5", "JOPC1");
        OpcItem item1 = new OpcItem("Device1.Tag1", true, "Channel1");
        OpcGroup group = new OpcGroup("group1", true, 500, 0.0f);

        group.addItem(item1);
        jopc.addGroup(group);

        OpcListener opcListener = new OpcListener();
        group.addAsynchListener(opcListener);

        //开始监听
        jopc.start();

        try{
            Thread.sleep(2000);

            /*System.out.println("JOPC active: " + jopc.ping());

            jopc.connect();
            System.out.println("JOPC client is connected...");

            jopc.registerGroups();
            System.out.println("OPCGroup are registered...");

            jopc.registerItem(group,item1);
            System.out.println("OPCItem are registered...");*/

            //更新频率
            jopc.setGroupUpdateTime(group, 1000);
            System.out.println("更新监听频率...");

            Thread.sleep(5000); //持续5秒

            jopc.terminate();//中止线程
            System.out.println("===线程正常终止====");
            //线程中止需要延续时间，不然group无法正常取消注册
            Thread.sleep(1000);
        }catch(Exception e){
            e.printStackTrace();
        }

        JEasyOpc.coUninitialize();
        System.out.println("===调用完成====");
    }
}
