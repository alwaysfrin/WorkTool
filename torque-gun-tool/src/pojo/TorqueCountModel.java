package pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description :   扭枪获取数据统计模型
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class TorqueCountModel {

    private String vin; //vin码
    private List<TorqueParam> successParamList; // 扭枪成功的记录
    private List<TorqueParam> failedParamList;  // 扭枪失败的记录

    private transient int successCount;   //扭枪成功数量
    private transient int failedCount;    //扭枪失败数量
    private transient int totalCount;     //获取扭枪的总数
    private transient List<TorqueParam> totalPramList;    // 扭枪所有的记录

    public TorqueCountModel(String vin) {
        this.vin = vin;
        successCount = 0;
        failedCount = 0;
        totalCount = 0;
        this.successParamList = new ArrayList<TorqueParam>();
        this.failedParamList = new ArrayList<TorqueParam>();
        totalPramList = new ArrayList<TorqueParam>();
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public List<TorqueParam> getSuccessParamList() {
        return successParamList;
    }

    public void setSuccessParamList(List<TorqueParam> successParamList) {
        this.successParamList = successParamList;
    }

    public List<TorqueParam> getFailedParamList() {
        return failedParamList;
    }

    public void setFailedParamList(List<TorqueParam> failedParamList) {
        this.failedParamList = failedParamList;
    }

    public int getTotalCount(){
        return getSuccessCount() + getFailedCount();
    }

    public int getSuccessCount() {
        return successParamList.size();
    }

    public int getFailedCount() {
        return failedParamList.size();
    }

    public List<TorqueParam> getTotalPramList() {
        totalPramList = new ArrayList<TorqueParam>();
        totalPramList.addAll(successParamList);
        totalPramList.addAll(failedParamList);
        return totalPramList;
    }

    @Override
    public String toString() {
        return "TorqueCountModel{" +
                "vin='" + vin + '\'' +
                //", successParamList=" + successParamList +
                //", failedParamList=" + failedParamList +
                ", successCount=" + getSuccessCount() +
                ", failedCount=" + getFailedCount() +
                ", totalCount=" + getTotalCount() +
                //", totalPramList=" + getTotalPramList() +
                '}';
    }
}
