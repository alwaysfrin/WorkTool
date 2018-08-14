package thread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.TorqueParam;
import pojo.TorqueSocket;
import util.TorqueConstWords;
import util.TorqueTool;

/**
 * @Description : 接受扭矩枪线程
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class TorqueListenerThread extends Thread {
    public static final Logger LOGGER = LoggerFactory.getLogger(TorqueListenerThread.class);

    private TorqueSocket torqueSocket;  //端口
    private int collectTimes;   //需要采集成功的次数
    private String vin; // 过程条码

    public TorqueListenerThread(TorqueSocket s,String vin){
        this.torqueSocket = s;
        this.torqueSocket.setListenerThread(this);
        this.torqueSocket.setListenerThreadStatus(true);

        this.collectTimes = 9999;    //接受9999此数据，由扭矩枪判断是否结束
        this.vin = vin;

        TorqueTool.TORQUE_THREAD_LIST.add(this);
    }

    public TorqueListenerThread(TorqueSocket s,int t,String vin){
        this.torqueSocket = s;
        this.torqueSocket.setListenerThread(this);
        this.torqueSocket.setListenerThreadStatus(true);

        this.collectTimes = t;
        this.vin = vin;

        TorqueTool.TORQUE_THREAD_LIST.add(this);
    }

    @Override
    public void run() {
        synchronized (torqueSocket){
            try {
                /*************** 确认开始通信 ***************/
                TorqueTool.sendCommand(torqueSocket.getSocket(), TorqueConstWords.START_COMMAND);
                TorqueParam param = TorqueTool.packTorqueParam(torqueSocket.getSocket());
                if (param.isSuccess()) {
                    //发送过程条码
                    //TorqueTool.sendCommand(socket,TorqueConstWords.getSNCommand(vin));
                    //param = TorqueTool.packTorqueParam(socket);

                    /*************** 确认订阅数据 ***************/
                    LOGGER.info("==开始订阅==" + TorqueConstWords.SUBSCRIBTE_COMMAND);
                    TorqueTool.sendCommand(torqueSocket.getSocket(), TorqueConstWords.SUBSCRIBTE_COMMAND);
                    param = TorqueTool.packTorqueParam(torqueSocket.getSocket());
                    if (param.isSuccess()) {
                        /*************** 开始获取数据 ***************/
                        //开始心跳
                        HeartBeatThread heartBeat = new HeartBeatThread(torqueSocket);
                        heartBeat.start();

                        int timeCount = 0;  //扭枪操作成功计数

                        //开始接受数据
                        boolean isContinue = true;
                        while (isContinue) {
                            param = TorqueTool.getTorqueRequest(torqueSocket.getSocket());
                            LOGGER.info("返回参数：" + param.getCommandCode() + " " + param.isSuccess() + "，获取扭矩：" + param.getTorque()
                                    + "，全部次数：" + param.getBatchSize() + "，成功次数：" + param.getBatchCounter() + "，本批成功状态：" + param.getBatchStatus()
                                    + "，扭矩状态：" + param.getBatchSize() + "，扭角状态：" + param.getBatchCounter() + "，过程条码：" + param.getSerialNumber());

                            //添加到数据采集
                            TorqueTool.addTORQUE_COUNT_MAP(vin,param);

                            //重置心跳
                            heartBeat.resetTime();

                            //if (param.getTorqueStatus() == 1 && param.getAngleStatus() == 1) {
                                //扭矩和扭角同时成功次数
                                timeCount++;
                            //}

                            if (collectTimes == timeCount ||
                                    (param.getBatchSize() == param.getBatchCounter() && param.getBatchStatus() == 1)) {
                                // 本次扭矩获取结束
                                isContinue = false;
                                heartBeat.stopWork();
                            }
                        }
                    }
                }
            }catch(Exception e){
                //操作失败处理
                LOGGER.error("线程执行异常：" + e.getMessage());
            }
        }
        LOGGER.info("扭矩枪获取数据监听线程已结束...");
    }
}
