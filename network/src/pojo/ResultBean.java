package pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class ResultBean {

    private boolean success;
    private String msg = "";
    private String result;
    private Map<String,String> resultMap = new HashMap<String,String>();
    private List<Map> resultList = new ArrayList<Map>();

    public ResultBean(boolean success, String msg) {
        super();
        this.success = success;
        this.msg = msg;
    }

    public ResultBean(boolean success, String msg, String result) {
        super();
        this.success = success;
        this.msg = msg;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Map<String, String> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, String> resultMap) {
        this.resultMap = resultMap;
    }

    public List<Map> getResultList() {
        return resultList;
    }

    public void setResultList(List<Map> resultList) {
        this.resultList = resultList;
    }

    @Override
    public String toString() {
        return "ResultBean [success=" + success + ", msg=" + msg + ", result=" + result + ", resultMap=" + resultMap
                + ", resultList=" + resultList + "]";
    }
}
