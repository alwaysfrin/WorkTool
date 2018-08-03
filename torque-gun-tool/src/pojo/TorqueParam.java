package pojo;

/**
 * @Description : 扭矩枪参数模型
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class TorqueParam {

    /**
     * 内容长度
     */
    private long paramLength;

    /**
     * 命令编码
     * 0001 请求通信
     * 0002 开始数据交互
     * 0003 连接停止
     * 0004 错误数据
     * 0005 已接受命令
     * 9999 心跳反馈
     */
    private String commandCode;

    /**
     * 是否错误，通过命令编码0004进行判断
     */
    private boolean isSuccess;

    /**
     * 扭矩是否正常
     */
    private transient boolean isTorqueSuccess;

    /**
     * 扭角是否正常
     */
    private transient boolean isAngleSuccess;

    /**
     * 扭角和扭矩是否都正常
     */
    private transient boolean isDataSuccess;

    /**
     * Cell ID
     * Pos: 23~26
     */
    public long cellId;

    /**
     * Channel ID
     * Pos: 29~30
     */
    public long channelId;

    /**
     * 控制箱名称
     * Pos: 33~57
     */
    public String torqueControllerName;

    /**
     * 采集扭矩时扫描的条码
     * Pos: 60~84
     */
    public String serialNumber;

    /**
     * Job ID
     * Pos: 87~88
     */
    public long jobID;

    /**
     * 参数集 ID
     * Pos: 91~93
     */
    public long parameterSetID;

    /**
     * 本次扭矩需采集的总数量（枪数）
     * Pos: 96~99
     */
    public long batchSize;

    /**
     * 当前是第几枪
     * Pos: 102~105
     */
    public long batchCounter;

    /**
     * 扭矩状态 0=NG,  1=OK
     * Pos: 108
     */
    public long tighteningStatus;

    /**
     * 扭矩详细状态 0=Low,1=OK,2=High
     * Pos: 111
     */
    public long torqueStatus;

    /**
     * 扭角详细状态 0=Low,1=OK,2=High
     * Pos: 114
     */
    public long angleStatus;

    /**
     * 扭矩下限，需要除以 100
     * Pos: 117~122
     */
    public float torqueMinLimit;

    /**
     * 扭矩上限，需要除以 100
     * Pos: 125~130
     */
    public float torqueMaxLimit;

    /**
     * 扭矩目标值，需要除以 100
     * Pos: 133~138
     */
    public float torqueFinalTarget;

    /**
     * 扭矩值（需要采集的值），需要除以 100
     * Pos: 141~146
     */
    public float torque;

    /**
     * 扭角下限，需要除以 100
     * Pos: 149~153
     */
    public float angleMinLimit;

    /**
     * 扭角上限，需要除以 100
     * Pos: 156~160
     */
    public float angleMaxLimit;

    /**
     * 扭角目标值，需要除以 100
     * 163~167
     */
    public float angleFinalTarget;

    /**
     * 扭角（需要采集的值），需要除以 100
     * Pos: 170~174
     */
    public float angle;

    /**
     * 时间戳
     * Pos: 177~195
     */
    public String timeStamp;

    /**
     * 参数集最后一次调整的时间，格式：YYYY-MM-DD:HH:MM:SS
     * Pos: 198~216
     */
    public String parameterSetLastChangeTime;

    /**
     *  整组扭矩的状态 0=未打完, 1=OK, 2=not used
     * Pos: 219
     */
    public long batchStatus;

    /**
     * 每个扭矩的唯一编号，10位数字，递增
     * Pos: 222~231
     */
    public long tighteningID;

    public long getCellId() {
        return cellId;
    }

    public void setCellId(long cellId) {
        this.cellId = cellId;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public String getTorqueControllerName() {
        return torqueControllerName;
    }

    public void setTorqueControllerName(String torqueControllerName) {
        this.torqueControllerName = torqueControllerName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public long getJobID() {
        return jobID;
    }

    public void setJobID(long jobID) {
        this.jobID = jobID;
    }

    public long getParameterSetID() {
        return parameterSetID;
    }

    public void setParameterSetID(long parameterSetID) {
        this.parameterSetID = parameterSetID;
    }

    public long getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(long batchSize) {
        this.batchSize = batchSize;
    }

    public long getBatchCounter() {
        return batchCounter;
    }

    public void setBatchCounter(long batchCounter) {
        this.batchCounter = batchCounter;
    }

    public long getTighteningStatus() {
        return tighteningStatus;
    }

    public void setTighteningStatus(long tighteningStatus) {
        this.tighteningStatus = tighteningStatus;
    }

    public long getTorqueStatus() {
        return torqueStatus;
    }

    public void setTorqueStatus(long torqueStatus) {
        this.torqueStatus = torqueStatus;
    }

    public long getAngleStatus() {
        return angleStatus;
    }

    public void setAngleStatus(long angleStatus) {
        this.angleStatus = angleStatus;
    }

    public float getTorqueMinLimit() {
        return torqueMinLimit;
    }

    public void setTorqueMinLimit(float torqueMinLimit) {
        this.torqueMinLimit = torqueMinLimit;
    }

    public float getTorqueMaxLimit() {
        return torqueMaxLimit;
    }

    public void setTorqueMaxLimit(float torqueMaxLimit) {
        this.torqueMaxLimit = torqueMaxLimit;
    }

    public float getTorqueFinalTarget() {
        return torqueFinalTarget;
    }

    public void setTorqueFinalTarget(float torqueFinalTarget) {
        this.torqueFinalTarget = torqueFinalTarget;
    }

    public float getTorque() {
        return torque;
    }

    public void setTorque(float torque) {
        this.torque = torque;
    }

    public float getAngleMinLimit() {
        return angleMinLimit;
    }

    public void setAngleMinLimit(float angleMinLimit) {
        this.angleMinLimit = angleMinLimit;
    }

    public float getAngleMaxLimit() {
        return angleMaxLimit;
    }

    public void setAngleMaxLimit(float angleMaxLimit) {
        this.angleMaxLimit = angleMaxLimit;
    }

    public float getAngleFinalTarget() {
        return angleFinalTarget;
    }

    public void setAngleFinalTarget(float angleFinalTarget) {
        this.angleFinalTarget = angleFinalTarget;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getParameterSetLastChangeTime() {
        return parameterSetLastChangeTime;
    }

    public void setParameterSetLastChangeTime(String parameterSetLastChangeTime) {
        this.parameterSetLastChangeTime = parameterSetLastChangeTime;
    }

    public long getBatchStatus() {
        return batchStatus;
    }

    public long getParamLength() {
        return paramLength;
    }

    public void setParamLength(long paramLength) {
        this.paramLength = paramLength;
    }

    public String getCommandCode() {
        return commandCode;
    }

    public void setCommandCode(String commandCode) {
        this.commandCode = commandCode;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public void setBatchStatus(long batchStatus) {
        this.batchStatus = batchStatus;
    }

    public long getTighteningID() {
        return tighteningID;
    }

    public void setTighteningID(long tighteningID) {
        this.tighteningID = tighteningID;
    }

    /**
     * 扭矩是否正常
     * @return
     */
    public boolean getTorqueSuccess() {
        return torqueStatus == 1;
    }

    /**
     * 扭角是否正常
     * @return
     */
    public boolean getAngleSuccess() {
        return angleStatus == 1;
    }

    /**
     * 扭矩和扭角是否都正常
     * @return
     */
    public boolean getDataSuccess() {
        return getTorqueSuccess() && getAngleSuccess();
    }

    public TorqueParam(String totalParam) {
        this.setParamLength(Long.parseLong(totalParam.substring(0,4).trim()));
        this.setCommandCode(totalParam.substring(4,8));
        this.setSuccess(true);

        if(commandCode.startsWith("006")){
            //获取返回的扭矩数据
            //包含0061，0062，0063，0064，0065
            packDataReply(totalParam);
        }else if("0004".equals(commandCode)){
            //无法连接
            this.setSuccess(false);
        }else if("0001".equals(commandCode)){
            //0001 请求通信
        }else if("0002".equals(commandCode)){
            //0002 开始数据交互
        }else if("0003".equals(commandCode)){
            //0003 连接停止
        }else if("9999".equals(commandCode)){
            //9999 心跳反馈
        }else if("0005".equals(commandCode)){
            //0005 已接受命令
        }
    }

    public void packDataReply(String totalParam){
        //java的substring开始位为0，所以begin位需要-1
        this.cellId = Long.parseLong(totalParam.substring(22,26).trim());
        this.channelId = Long.parseLong(totalParam.substring(28,30).trim());
        this.torqueControllerName = totalParam.substring(32,57).trim();
        this.serialNumber = totalParam.substring(59,84).trim();
        this.jobID = Long.parseLong(totalParam.substring(86,88).trim());
        this.parameterSetID = Long.parseLong(totalParam.substring(90,93).trim());
        this.batchSize = Long.parseLong(totalParam.substring(95,99).trim());
        this.batchCounter = Long.parseLong(totalParam.substring(101,105).trim());
        this.tighteningStatus = Long.parseLong(totalParam.substring(107,108).trim());
        this.torqueStatus = Long.parseLong(totalParam.substring(110,111).trim());
        this.angleStatus = Long.parseLong(totalParam.substring(113,114).trim());
        this.torqueMinLimit = Float.parseFloat(totalParam.substring(116,122).trim()) / 100;
        this.torqueMaxLimit = Float.parseFloat(totalParam.substring(124,130).trim()) / 100;
        this.torqueFinalTarget = Float.parseFloat(totalParam.substring(132,138).trim()) / 100;
        this.torque = Float.parseFloat(totalParam.substring(140,146).trim()) / 100;
        this.angleMinLimit = Float.parseFloat(totalParam.substring(148,153).trim()) / 100;
        this.angleMaxLimit = Float.parseFloat(totalParam.substring(155,160).trim()) / 100;
        this.angleFinalTarget = Float.parseFloat(totalParam.substring(162,167).trim()) / 100;
        this.angle = Float.parseFloat(totalParam.substring(169,174).trim()) / 100;
        this.timeStamp = totalParam.substring(176,195).trim();
        this.parameterSetLastChangeTime = totalParam.substring(197,216).trim();
        this.batchStatus = Long.parseLong(totalParam.substring(218,219).trim());
        this.tighteningID = Long.parseLong(totalParam.substring(221,231).trim());
    }

    @Override
    public String toString() {
        return "TorqueParam{" +
                "paramLength=" + paramLength +
                ", commandCode='" + commandCode + '\'' +
                ", isSuccess=" + isSuccess +
                ",\n cellId=" + cellId +
                ", channelId=" + channelId +
                ", torqueControllerName（控制箱名称）='" + torqueControllerName + '\'' +
                ",\n serialNumber（采集扭矩时扫描的条码）='" + serialNumber + '\'' +
                ", jobID=" + jobID +
                ", parameterSetID（参数集）=" + parameterSetID +
                ", batchSize（本次扭矩需采集的总数量（枪数））=" + batchSize +
                ", batchCounter（当前是第几枪）=" + batchCounter +
                ",\n tighteningStatus（扭矩状态 0=NG,  1=OK）=" + tighteningStatus +
                ", torqueStatus（扭矩详细状态 0=Low,1=OK,2=High）=" + torqueStatus +
                ", angleStatus（扭角详细状态 0=Low,1=OK,2=High）=" + angleStatus +
                ", torqueMinLimit（扭矩下限，除以 100）=" + torqueMinLimit +
                ", torqueMaxLimit（扭矩上限，除以 100）=" + torqueMaxLimit +
                ",\n torqueFinalTarget（扭矩目标值，除以 100）=" + torqueFinalTarget +
                ", torque（扭矩值（需要采集的值），除以 100）=" + torque +
                ", angleMinLimit（扭角下限，除以 100）=" + angleMinLimit +
                ", angleMaxLimit（扭角上限，除以 100）=" + angleMaxLimit +
                ", angleFinalTarget（扭角目标值，除以 100）=" + angleFinalTarget +
                ",\n angle（扭角（需要采集的值），除以 100）=" + angle +
                ", timeStamp（时间戳）='" + timeStamp + '\'' +
                ", parameterSetLastChangeTime（参数集最后一次调整的时间）='" + parameterSetLastChangeTime + '\'' +
                ", batchStatus（整组扭矩的状态 0=未打完, 1=OK, 2=not used）=" + batchStatus +
                ", tighteningID（每个扭矩的唯一编号，10位数字，递增）=" + tighteningID +
                '}';
    }

    public static void main(String[] args){
        /*String s = "02310061001 0000    010000020103 CVI3 Vision             04                         050106001070002080000090101110120000201300005014000030150000321600100170020018000001900000202018-08-01:08:55:15212018-07-19:07:48:10220230000000235 ";
        TorqueParam param = new TorqueParam(s);

        System.out.println(param);
        System.out.println(s.substring(0,4));
        System.out.println(s.substring(4,8));
        System.out.println(s.substring(8,11));
        System.out.println(s.substring(11,12));
        System.out.println(s.substring(12,14));*/
        //System.out.println("==============");
        /*System.out.println(s.substring(20,22) + " : " + s.substring(22,26));
        System.out.println(s.substring(105,107) + " : " + s.substring(107,108));*/
    }
}
