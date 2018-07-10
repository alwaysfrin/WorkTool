package pojo;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class TableCell {
    private int rowCount;   //行数
    private int cellCount;  //列数

    private int cellType;   //列类型   1标题,2内容,3条码,4二维码,5图片
    private String cellTypeVal; //列值

    private int colType;    //跨行跨列类型 1 正常,2 跨列,3 跨行,4 不显示
    private int colTypeVal; //跨行跨列值

    private String widthSize;    //列宽
    private String heightSize;    //行高
    private String fontSize;    //字体大小
    private int position; //位置，1 左对齐，2 居中，3 右对齐
    private int fontWeight; //字体粗斜  1 加粗，2 斜体，3 加粗斜体

    public String getWidthSize() {
        return widthSize;
    }

    public void setWidthSize(String widthSize) {
        this.widthSize = widthSize;
    }

    public String getHeightSize() {
        return heightSize;
    }

    public void setHeightSize(String heightSize) {
        this.heightSize = heightSize;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getCellCount() {
        return cellCount;
    }

    public void setCellCount(int cellCount) {
        this.cellCount = cellCount;
    }

    public int getCellType() {
        return cellType;
    }

    public void setCellType(int cellType) {
        this.cellType = cellType;
    }

    public String getCellTypeVal() {
        return cellTypeVal;
    }

    public void setCellTypeVal(String cellTypeVal) {
        this.cellTypeVal = cellTypeVal;
    }

    public int getColType() {
        return colType;
    }

    public void setColType(int colType) {
        this.colType = colType;
    }

    public int getColTypeVal() {
        return colTypeVal;
    }

    public void setColTypeVal(int colTypeVal) {
        this.colTypeVal = colTypeVal;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontWeight() {
        return fontWeight;
    }

    public void setFontWeight(int fontWeight) {
        this.fontWeight = fontWeight;
    }
}
