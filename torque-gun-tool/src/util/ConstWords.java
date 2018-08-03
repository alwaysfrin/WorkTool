package util;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class ConstWords {

    //报文头
    public static final String COMMAND_HEADER = "0020"; //长度20
    //指令版本号，报文结束（\0作为结束符）
    public static final String COMMAND_VERSION = "001         \0";

    //开始连接
    public static final String START_COMMAND = COMMAND_HEADER + "0001" + COMMAND_VERSION;
    //结束连接
    public static final String END_COMMAND = COMMAND_HEADER + "0003" + COMMAND_VERSION;
    //开始订阅扭矩数据
    public static final String SUBSCRIBTE_COMMAND = COMMAND_HEADER + "0060" + COMMAND_VERSION;
    //订阅者返回已收到信息ACK，数据接收确认(表明已收到数据，上条数据不需要再次发送)
    public static final String SUBSCRIBTE_SUBMIT_COMMAND = COMMAND_HEADER + "0062" + COMMAND_VERSION;
    //订阅心跳
    public static final String HEART_BEAT_COMMAND = COMMAND_HEADER + "9999" + COMMAND_VERSION;

    //最多保存多少个过程条码的扭矩
    public static final int TORQUE_MODEL_MAX_COUNT = 50;

    // 生成vin码指令
    public static String getSNCommand(String sn){
        return String.format("%04d", sn.length() + 20) + "0150001         " + sn + "\0";
    }

    /**
     * String 转换成ascⅡ码
     * @param value
     * @return
     */
    public static String stringToAscii(String value){
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(i != chars.length - 1) {
                sbu.append((int)chars[i]);
            }
            else {
                sbu.append((int)chars[i]);
            }
        }
        return sbu.toString();
    }

    /**
     * asc码转换成String
     * @param value
     * @return
     */
    public static String asciiToString(String value){
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }

    public static void main(String[] args){
        //int c = 365;
        //String s = String.format("%04d", c); //0代表前面要补的字符 10代表字符串长度,d表示参数为整数类型
        //System.out.println(s);
    }
}
