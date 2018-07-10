package util;

import pojo.TableCell;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class TableTool {

    /**
     * 生成详细的table
     * @param rowNum
     * @param cellNum
     * @param sb
     */
    public static void makeTableDetail(int tableWidth,int rowNum, int cellNum,String borderLine,String tableBackgroundColor, StringBuffer sb) {
        if(borderLine.lastIndexOf("px") == -1){
            borderLine += "px";
        }
        sb.append("<table width='"+tableWidth+"' border='"+borderLine
                        +"' cellpadding='0' cellspacing='0' style='float:left;margin:2px;background:"+tableBackgroundColor+";'>");
        System.out.println(sb.toString());
        for (int i = 0; i < rowNum; i++) {
            sb.append("<tr>");
            for (int j = 0; j < cellNum; j++) {
                sb.append("<td>&nbsp;</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
    }


    /**
     * 生成详细的table
     * @param rowNum
     * @param cellNum
     * @param sb
     */
    public static void makeTableDetail(int tableWidth,int rowNum, int cellNum,String borderLine,String tableBackgroundColor, StringBuffer sb,ArrayList<TableCell> tableCellList) {
        HashMap<Integer,HashMap<Integer, TableCell>> tableMap = new HashMap<Integer,HashMap<Integer, TableCell>>();
        for(TableCell tableCell : tableCellList){
            HashMap<Integer, TableCell> detailMap = tableMap.get(tableCell.getRowCount());
            if(detailMap == null){
                detailMap = new HashMap<Integer, TableCell>();
                detailMap.put(tableCell.getCellCount(), tableCell);
                tableMap.put(tableCell.getRowCount(),detailMap);
            }else{
                detailMap.put(tableCell.getCellCount(), tableCell);
            }
        }

        if(borderLine.lastIndexOf("px") == -1){
            borderLine += "px";
        }
        sb.append("<table width='"+tableWidth+"px' border='"+borderLine
                        +"' cellpadding='0' cellspacing='0' style='float:left;margin:2px;background:"+tableBackgroundColor+";'>");
        for (int i = 1; i <= rowNum; i++) {
            sb.append("<tr>");
            for (int j = 1; j <= cellNum; j++) {
                TableCell tableCell = tableMap.get(i).get(j);
                if(tableCell.getColType() != 4){
                    //正常显示
                    sb.append("<td ");
                    caseColType(tableCell,sb);  //跨行跨列
                    caseFontSize(tableCell,sb); //字体大小及样式
                    sb.append(">");
                    caseCellDetail(tableCell,sb); //内容详情
                    sb.append("&nbsp;</td>");
                }
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
    }

    public static void caseColType(TableCell tableCell, StringBuffer sb){
        switch (tableCell.getColType()){
            case 2:
                sb.append(" colSpan='"+ tableCell.getColTypeVal()+"'");
                break;
            case 3:
                sb.append(" rowSpan='"+ tableCell.getColTypeVal()+"'");
                break;
            default:break;
        }
        switch (tableCell.getPosition()){
            case 1:
                sb.append(" align='left'");
                break;
            case 2:
                sb.append(" align='center'");
                break;
            case 3:
                sb.append(" align='right'");
                break;
            default:break;
        }
    }

    public static void caseFontSize(TableCell tableCell, StringBuffer sb){
        sb.append(" style='");
        if(tableCell.getWidthSize() != null && tableCell.getWidthSize().length() > 0){
            sb.append("width:" + tableCell.getWidthSize());
            if(tableCell.getWidthSize().lastIndexOf("px") > 0){
                sb.append(";");
            }else{
                sb.append("px;");
            }
        }
        if(tableCell.getHeightSize() != null && tableCell.getHeightSize().length() > 0){
            sb.append("height:" + tableCell.getHeightSize());
            if(tableCell.getHeightSize().lastIndexOf("px") > 0){
                sb.append(";");
            }else{
                sb.append("px;");
            }
        }
        if(tableCell.getFontSize() != null && tableCell.getFontSize().length() > 0){
            sb.append("font-size:" + tableCell.getFontSize() + ";");
        }
        switch (tableCell.getFontWeight()){
            case 1:
                sb.append("font-weight: bold;");
                break;
            case 2:
                sb.append("font-style: italic;");
                break;
            case 3:
                sb.append("font-weight: bold;font-style: italic;");
                break;
            default:break;
        }
        sb.append("'");
    }

    public static void caseCellDetail(TableCell tableCell, StringBuffer sb){
        switch (tableCell.getCellType()){
            case 1:
                sb.append(tableCell.getCellTypeVal());
                break;
            case 2:
                sb.append(tableCell.getCellTypeVal());
                break;
            case 3:
                sb.append(tableCell.getCellTypeVal());
                break;
            case 4:
                sb.append(tableCell.getCellTypeVal());
                break;
            case 5:
                sb.append(tableCell.getCellTypeVal());
                break;
            default:
                    break;
        }
    }
}
