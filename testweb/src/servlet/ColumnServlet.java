package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
@WebServlet(name = "ColumnServlet",value="/columnshow")
public class ColumnServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        StringBuffer sb = new StringBuffer();
        //生成展示table
        for(int rowNumIdx=1; rowNumIdx<=rowNum;rowNumIdx++) {
            sb.append("<h2>第"+rowNumIdx+"行</h2><p>");
            for(int cellNumIdx=1; cellNumIdx<=cellNum;cellNumIdx++) {
                sb.append(cellNumIdx+".&nbsp;&nbsp;");
                getTextType(String.valueOf(rowNumIdx),String.valueOf(cellNumIdx),sb);
                getFontStyle(String.valueOf(rowNumIdx),String.valueOf(cellNumIdx),sb);
                getCombine(String.valueOf(rowNumIdx),String.valueOf(cellNumIdx),sb);
                sb.append("<br/>");
            }
            sb.append("</p>");
        }

        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(sb.toString());
    }

    public void getCombine(String row,String cell,StringBuffer sb){
        sb.append("&nbsp;&nbsp;<select id='colType"+row+cell+"' data-cell='"+cell+"' data-row='"+row+"'>");
        sb.append("<option value='1' selected='selected'>默认行列</option>");
        sb.append("<option value='2'>跨列</option>");
        sb.append("<option value='3'>跨行</option>");
        sb.append("<option value='4'>不显示</option>");
        sb.append("</select>");
        sb.append("跨数<input type='text' size='5' id='colTypeVal"+row+cell+"'>&nbsp;&nbsp;");
    }

    public void getTextType(String row,String cell,StringBuffer sb){
        sb.append("<select id='position"+row+cell+"'>");
        sb.append("<option value='1'>左对齐</option>");
        sb.append("<option value='2'>居中</option>");
        sb.append("<option value='3'>右对齐</option>");
        sb.append("</select>");
        sb.append("<select id='cellType"+row+cell+"'>");
        sb.append("<option value='1'>标题</option>");
        sb.append("<option value='2'>内容</option>");
        sb.append("<option value='3'>条码</option>");
        sb.append("<option value='4'>二维码</option>");
        sb.append("<option value='5'>图片</option>");
        sb.append("</select>");
        sb.append("<input id='cellTypeVal"+row+cell+"' type='text'>&nbsp;&nbsp;");
    }

    public void getFontStyle(String row,String cell,StringBuffer sb){
        sb.append("宽px：<input id='widthSize"+row+cell+"' type='text' size='5'>&nbsp;&nbsp;");
        sb.append("高px：<input id='heightSize"+row+cell+"' type='text' size='5'>&nbsp;&nbsp;");
        sb.append("字体px：<input id='fontSize"+row+cell+"' type='text' size='5'>&nbsp;&nbsp;");
        sb.append("<select id='fontWeight"+row+cell+"'>");
        sb.append("<option selected='selected' value='-1'>默认粗斜</option>");
        sb.append("<option value='1'>加粗</option>");
        sb.append("<option value='2'>斜体</option>");
        sb.append("<option value='3'>加粗斜体</option>");
        sb.append("</select>");
    }
}
