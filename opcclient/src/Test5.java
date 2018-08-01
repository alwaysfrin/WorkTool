import javafish.clients.opc.JCustomOpc;
import javafish.clients.opc.property.PropertyLoader;

import java.util.Properties;
import java.util.Set;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class Test5 {
    public static void main(String[] args){
        Properties props = PropertyLoader.loadProperties(JCustomOpc.class);
        Set<String> names = props.stringPropertyNames();
        for(String name : names){
            System.out.println(name  + " : " + props.getProperty(name));
        }

        System.out.println(props.get("library.path"));

        System.loadLibrary(props.getProperty("library.path"));
    }
}
