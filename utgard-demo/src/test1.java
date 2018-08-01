import org.jinterop.dcom.common.JIException;
import org.openscada.opc.dcom.list.ClassDetails;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.list.Categories;
import org.openscada.opc.lib.list.Category;
import org.openscada.opc.lib.list.ServerList;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.concurrent.Executors;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class test1 {

    public static void main(String[] args) throws JIException, UnknownHostException, AlreadyConnectedException {
        String host = "127.0.0.1";
        String domain = "";
        String user = "opcuser";
        String password = "opcpwd";

        String cId = "";
        String progId = "Kepware.KEPServerEX.V5";

        ServerList serverList = new ServerList(host,user,password,domain);

        Collection<ClassDetails> detailList = serverList.listServersWithDetails(
                new Category[]{ Categories.OPCDAServer10, Categories.OPCDAServer20, Categories.OPCDAServer30},
                new Category[]{}
        );

        for(ClassDetails detail : detailList){
            System.out.println(String.format("cId : %s", detail.getClsId()));
            System.out.println(String.format("ProgId : %s", detail.getProgId()));
            System.out.println(String.format("Desc : %s", detail.getDescription()));
        }

        ClassDetails kpDetail = detailList.iterator().next();
        cId = kpDetail.getClsId();

        ConnectionInformation ci = new ConnectionInformation();
        ci.setHost(host);
        ci.setDomain(domain);
        ci.setUser(user);
        ci.setPassword(password);

        //ci.setProgId(progId);
        ci.setClsid(cId);
        System.out.println(ci.getClsid());
        //System.out.println(ci.getClsOrProgId());

        final Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
        server.connect();

        System.out.println("opc has connected");
    }
}
