import javafish.clients.opc.JCustomOpc;
import javafish.clients.opc.browser.JOpcBrowser;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class Test3 {

    /**
     * 检索opc内容
     * @param args
     */
    public static void main(String[] args){
        JOpcBrowser.coInitialize();
        try {
            String[] opcServers = JOpcBrowser.getOpcServers("localhost");

            for(String str : opcServers){
                System.out.println("server prog id : "+str);
            }

            JOpcBrowser jbrowser = new JOpcBrowser("localhost","Kepware.KEPServerEX.V5", "JOPCBrowser1");
            jbrowser.connect();

            String[] root = jbrowser.getOpcBranch("");
            for(String leaf : root){
                System.out.println("root branch : "+leaf);

                //每个item由四个值组成，分别代表item name、value type、item path、value。并由;号隔开。
                /*String[] items = jbrowser.getOpcItems(leaf, true);
                for(String item : items){
                    System.out.println(leaf + "= item : " + item);
                }*/
            }

            String[] items = jbrowser.getOpcBranch("Channel1");
            for(String item : items){
                System.out.println("= item : " + item);
            }

            //每个item由四个值组成，分别代表item name、value type、item path、value。并由;号隔开。
            String[] tags = jbrowser.getOpcItems("Channel1.Device1",true);
            for(String tag : tags){
                System.out.println("= tag : " + tag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JCustomOpc.coUninitialize();
        }
    }
}
