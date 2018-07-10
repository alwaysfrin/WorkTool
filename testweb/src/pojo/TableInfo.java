package pojo;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class TableInfo {
    private int picWidth;   //画布宽度
    private int picHeight;  //画布高度
    private int tableWidth; //表哥宽
    private int tableCount; //表格数
    private int rowNum;     //行数
    private int cellNum;    //列数
    private String borderLine; //边框
    private String tableBackgroundColor;    //表格背景色

    public int getPicWidth() {
        return picWidth;
    }

    public void setPicWidth(int picWidth) {
        this.picWidth = picWidth;
    }

    public int getPicHeight() {
        return picHeight;
    }

    public void setPicHeight(int picHeight) {
        this.picHeight = picHeight;
    }

    public int getTableWidth() {
        return tableWidth;
    }

    public void setTableWidth(int tableWidth) {
        this.tableWidth = tableWidth;
    }

    public int getTableCount() {
        return tableCount;
    }

    public void setTableCount(int tableCount) {
        this.tableCount = tableCount;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getCellNum() {
        return cellNum;
    }

    public void setCellNum(int cellNum) {
        this.cellNum = cellNum;
    }

    public String getBorderLine() {
        return borderLine;
    }

    public void setBorderLine(String borderLine) {
        this.borderLine = borderLine;
    }

    public String getTableBackgroundColor() {
        return tableBackgroundColor;
    }

    public void setTableBackgroundColor(String tableBackgroundColor) {
        this.tableBackgroundColor = tableBackgroundColor;
    }
}
