package servlet;

import util.TableTool;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
@WebServlet(name = "getTableShow",value = "/tableshow")
public class TableServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String tableWidthStr = request.getParameter("tableWidth");
        String tableCountStr = request.getParameter("tableCount");
        int tableWidth = 1; //表格宽度
        int tableCount = 1;    //表格数量
        try{
            tableWidth = Integer.parseInt(tableWidthStr);
        }catch (Exception e){}
        try{
            tableCount = Integer.parseInt(tableCountStr);
        }catch (Exception e){}

        String rowNumStr = request.getParameter("rowNum");
        String cellNumStr = request.getParameter("cellNum");

        int rowNum = 1;
        int cellNum = 1;
        try{
            rowNum = Integer.parseInt(rowNumStr);
        }catch (Exception e){}
        try{
            cellNum = Integer.parseInt(cellNumStr);
        }catch (Exception e){}

        String borderLine = request.getParameter("borderLine");
        String tableBackgroundColor = request.getParameter("tableBackgroundColor");
        StringBuffer sb = new StringBuffer();
        //生成展示table
        for(int tableRowIdx=0; tableRowIdx<tableCount;tableRowIdx++) {
            TableTool.makeTableDetail(tableWidth, rowNum, cellNum,borderLine,tableBackgroundColor, sb);
        }

        response.getWriter().print(sb.toString());
    }

}
