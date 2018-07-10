package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
@WebServlet(name = "LodopServlet",value="/listlodop")
public class LodopServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, String> formMap = (HashMap<String, String>) this.getServletContext().getAttribute("formMap");
        if (formMap == null) {
            formMap = new HashMap<String, String>();
        }
        this.getServletContext().setAttribute("formMap",formMap);

        String method = request.getParameter("method");
        if("save".equals(method)) {
            String modelName = request.getParameter("modelName");
            String modelDesc = request.getParameter("modelDesc");

            formMap.put(modelName,modelDesc);
        }else if("delete".equals(method)) {
            String modelName = request.getParameter("modelName");
            formMap.remove(modelName);
        }
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}
