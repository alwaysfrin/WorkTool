import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.exception.*;
import javafish.clients.opc.variant.Variant;

import java.text.SimpleDateFormat;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class Test2 {
    public static void main(String[] args) throws InterruptedException {
        Test2 test = new Test2();
        JOpc.coInitialize();
        JOpc jopc = new JOpc("localhost", "Matrikon.OPC.Simulation", "JOPC1");
//		    JOpc jopc = new JOpc("192.168.10.68", "Matrikon.OPC.Simulation.1", "JOPC1");
        OpcItem item1 = new OpcItem("Random.ArrayOfReal8", true, "");
//		    OpcItem item1 = new OpcItem("Random.Int2", true, "");
        OpcGroup group = new OpcGroup("group1", true, 1000, 0.0f);
        group.addItem(item1);
        jopc.addGroup(group);
        try {
            jopc.connect();
            System.out.println("JOPC client is connected...");
        } catch (ConnectivityException e2) {
            e2.printStackTrace();
        }
/*
        try {
            jopc.registerGroups();
            System.out.println("OPCGroup are registered...");
        } catch (UnableAddGroupException e2) {
            e2.printStackTrace();
        } catch (UnableAddItemException e2) {
            e2.printStackTrace();
        }

        synchronized (test) {
            test.wait(50);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        // Synchronous reading of item
        int cycles = 100;
        int acycle = 0;
        while (acycle++ < cycles) {
            synchronized (test) {
                test.wait(1000);
            }
            try {
                OpcItem responseItem = jopc.synchReadItem(group, item1);
                System.out.println(responseItem);
                System.out.println(sdf.format(responseItem.getTimeStamp().getTime()) + ":" + Variant.getVariantName(responseItem.getDataType()) + ": " + responseItem.getValue());
            } catch (ComponentNotFoundException e1) {
                e1.printStackTrace();
            } catch (SynchReadException e) {
                e.printStackTrace();
            }
        }*/
        JOpc.coUninitialize();
    }
}
