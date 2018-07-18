import javafish.clients.opc.JCustomOpc;
import javafish.clients.opc.asynch.AsynchEvent;
import javafish.clients.opc.asynch.OpcAsynchGroupListener;
import javafish.clients.opc.component.OpcItem;

import java.util.List;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class OpcListener implements OpcAsynchGroupListener {

    @Override
    public void getAsynchEvent(AsynchEvent event) {
        //System.out.println(((JCustomOpc)event.getSource()).getFullOpcServerName() + "=>" + event.getOPCGroup());
        List<OpcItem> list = event.getOPCGroup().getItems();
        for (OpcItem opcItem : list) {
            String key = opcItem.getItemName().trim();
            String value = opcItem.getValue().toString().trim();
            System.out.println("Package(监听):"+ event.getID() + ",数据监听: " + key + ":" + value);
        }

    }
}
