package opc.servlet;

import javafish.clients.opc.JCustomOpc;
import javafish.clients.opc.JOpc;
import javafish.clients.opc.property.PropertyLoader;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Properties;
import java.util.Set;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
@WebServlet(name = "opcservice", value = "/opcservice")
public class OpcServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String opcKey = request.getParameter("opcKey");
        System.out.println(opcKey);

        try {
            System.out.println(System.getProperty("java.library.path") + "============"+System.getProperty("java.class.path"));
            /*Properties props = PropertyLoader.loadProperties(JCustomOpc.class);
            Set<String> names = props.stringPropertyNames();
            for (String name : names) {
                System.out.println(name + " : " + props.getProperty(name));
            }*/
            JOpc.coInitialize();
        }catch(Exception e){
            e.printStackTrace();
        }

        /*System.out.println(props.get("library.path"));

        System.loadLibrary(props.getProperty("library.path"));

        Writer writer = response.getWriter();
        ((PrintWriter) writer).println("1111111");*/

        /*JOpc.coInitialize();

        JOpc jopc = new JOpc("localhost", "Kepware.KEPServerEX.V5", "JOPC1");
        System.out.println(jopc);*/
    }
}
