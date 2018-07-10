package servlet;

import com.alibaba.fastjson.JSONObject;
import pojo.TableCell;
import util.TableTool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
@WebServlet(name = "MakeTableDetailServlet",value="/maketable")
public class MakeTableDetailServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jsonStr = getRequestPostStr(request);

        String tableWidthStr = request.getParameter("tableWidth");
        String tableCountStr = request.getParameter("tableCount");
        String rowNumStr = request.getParameter("rowNum");
        String cellNumStr = request.getParameter("cellNum");
        int tableWidth = 1; //表格宽度
        int tableCount = 1;    //表格数量
        int rowNum = 1;
        int cellNum = 1;
        try{
            tableWidth = Integer.parseInt(tableWidthStr);
        }catch (Exception e){}
        try{
            tableCount = Integer.parseInt(tableCountStr);
        }catch (Exception e){}
        try{
            rowNum = Integer.parseInt(rowNumStr);
        }catch (Exception e){}
        try{
            cellNum = Integer.parseInt(cellNumStr);
        }catch (Exception e){}

        String borderLine = request.getParameter("borderLine");
        String tableBackgroundColor = request.getParameter("tableBackgroundColor");
        ArrayList<TableCell> tableCellList = (ArrayList<TableCell>) JSONObject.parseArray(jsonStr, TableCell.class);
        StringBuffer sb = new StringBuffer();
        TableTool.makeTableDetail(tableWidth,rowNum, cellNum,borderLine,tableBackgroundColor, sb, tableCellList);

        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(sb.toString());
    }

    /***
     * 获取 request 中 json字符串的内容
     *
     * @param request
     * @return : <code>byte[]</code>
     * @throws IOException
     */
    public String getRequestJsonString(HttpServletRequest request)
            throws IOException {
        String submitMehtod = request.getMethod();
        // GET
        if (submitMehtod.equals("GET")) {
            return new String(request.getQueryString().getBytes("iso-8859-1"), "utf-8").replaceAll("%22", "\"");
            // POST
        } else {
            return getRequestPostStr(request);
        }
    }

    /**
     * 描述:获取 post 请求的 byte[] 数组
     * <pre>
     * 举例：
     * </pre>
     *
     * @param request
     * @return
     * @throws IOException
     */
    public byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

    /**
     * 描述:获取 post 请求内容
     * <pre>
     * 举例：
     * </pre>
     *
     * @param request
     * @return
     * @throws IOException
     */
    public String getRequestPostStr(HttpServletRequest request)
            throws IOException {
        byte buffer[] = getRequestPostBytes(request);
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        return new String(buffer, charEncoding);
    }
}
