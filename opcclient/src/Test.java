import javafish.clients.opc.JOpc;
import javafish.clients.opc.SynchReadItemExample;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.exception.ConnectivityException;
import javafish.clients.opc.exception.UnableAddGroupException;
import javafish.clients.opc.exception.UnableRemoveGroupException;
import javafish.clients.opc.exception.UnableRemoveItemException;
import javafish.clients.opc.lang.Translate;
import javafish.clients.opc.property.PropertyLoader;
import javafish.clients.opc.variant.Variant;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class Test {

    /**
     * 基本读写
     * @param args
     */
    public static void main(String[] args){
        /*Locale locale;
        Properties props = PropertyLoader.loadProperties(Translate.class.getName());
        String lang = props.getProperty("locale");

        if (lang != null && !lang.trim().equals("")) {
            locale = new Locale(props.getProperty("locale"));
        } else {
            locale = Locale.getDefault();
        }
        System.out.println(lang + " " + locale);
        System.out.println(props.getProperty("resource") + " " + locale);

        ResourceBundle resourceBundle = ResourceBundle.getBundle(props.getProperty("resource"), locale);

        resourceBundle = ResourceBundle.getBundle(props.getProperty("resource"), locale);*/

        SynchReadItemExample test = new SynchReadItemExample();

        JOpc.coInitialize();

        JOpc jopc = new JOpc("localhost", "Kepware.KEPServerEX.V5", "JOPC1");

        /**
         * param1 : item Name
         * param2 : 是否激活查询
         * param3 : 路径
         */
        OpcItem item1 = new OpcItem("Device1.Tag1", true, "Channel1");
        OpcItem item2 = new OpcItem("Channel1.Device1.Tag2", true, "");

        /**
         * 实例化分组，添加item
         * param1 : 组名
         * param2 : 是否激活查询
         * param3 : 查询频率（异步使用）
         * param4 : 不工作占比
         */
        OpcGroup group = new OpcGroup("group1", true, 500, 0.0f);
        group.addItem(item1);
        group.addItem(item2);

        jopc.addGroup(group);

        try {
            jopc.connect();
            System.out.println("JOPC client is connected...");

            jopc.registerGroups();
            System.out.println("OPCGroup are registered...");

            //jopc.registerItem(group,item1);
            //jopc.registerItem(group,item2);

            /*synchronized(test) {
                test.wait(50);
            }*/
        }catch (Exception e2) {
            e2.printStackTrace();
        }

        // Synchronous reading of item
        int cycles = 7;
        int acycle = 0;
        while (acycle++ < cycles) {
            try {
                synchronized(test) {
                    test.wait(1000);
                }

                OpcItem responseItem = jopc.synchReadItem(group, item1);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar cal = responseItem.getTimeStamp();
                Date date = cal.getTime();
                System.out.println("1 ==>"+ responseItem.getItemName() + " : " + responseItem.getValue()
                        + " : " + responseItem.getAccessPath() + " : " + responseItem.getDataType() + " : " + Variant.getVariantName(responseItem.getDataType()) + " : "
                        + sdf.format(date));

                /*OpcItem responseItem2 = jopc.synchReadItem(group, item2);
                cal = responseItem.getTimeStamp();
                date = cal.getTime();
                System.out.println("2 ==>"+ responseItem2.getItemName() + " : " + responseItem2.getValue()
                        + " : " + responseItem2.getAccessPath() + " : " + responseItem2.getDataType() + " : " + Variant.getVariantName(responseItem2.getDataType())
                        + sdf.format(date));*/
                //System.out.println(Variant.getVariantName(responseItem.getDataType()) + ": " + responseItem.getValue());

                //写数据
                //item1.setValue(new Variant(0));
                //jopc.synchWriteItem(group,item1);

                //item2.setValue(new Variant(acycle));
                //jopc.synchWriteItem(group,item2);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        //释放资源
        /*try {
            jopc.unregisterItem(group, item1);
            jopc.unregisterItem(group, item2);
            jopc.unregisterGroup(group);
        } catch (UnableRemoveItemException e) {
            e.printStackTrace();
        } catch (UnableRemoveGroupException e) {
            e.printStackTrace();
        }*/

        JOpc.coUninitialize();
    }
}
