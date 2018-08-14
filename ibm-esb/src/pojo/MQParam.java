package pojo;

/**
 * @Description : 队列配置信息
 * @Reference :
 * @Author :    frin
 * @Date : 2018-08-08 09:30
 * @Modify :
 **/
public class MQParam {
    private String host;
    private int port;
    private int ccsid;
    private String channel;
    private String queueManager;
    private String queueName;   //队列名称
    private String replyToQueueName;    //回复队列名称
    private String userId;
    private String password;

    public MQParam() {
    }

    public MQParam(String host, int port, int ccsid, String channel, String queueManager, String queueName) {
        this.host = host;
        this.port = port;
        this.ccsid = ccsid;
        this.channel = channel;
        this.queueManager = queueManager;
        this.queueName = queueName;
    }

    public MQParam(String host, int port, int ccsid, String channel, String queueManager, String queueName, String replyToQueueName) {
        this.host = host;
        this.port = port;
        this.ccsid = ccsid;
        this.channel = channel;
        this.queueManager = queueManager;
        this.queueName = queueName;
        this.replyToQueueName = replyToQueueName;
    }

    public MQParam(String host, int port, int ccsid, String channel, String queueManager, String queueName, String replyToQueueName, String userId, String password) {
        this.host = host;
        this.port = port;
        this.ccsid = ccsid;
        this.channel = channel;
        this.queueManager = queueManager;
        this.queueName = queueName;
        this.replyToQueueName = replyToQueueName;
        this.userId = userId;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getCcsid() {
        return ccsid;
    }

    public void setCcsid(int ccsid) {
        this.ccsid = ccsid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getQueueManager() {
        return queueManager;
    }

    public void setQueueManager(String queueManager) {
        this.queueManager = queueManager;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getReplyToQueueName() {
        return replyToQueueName;
    }

    public void setReplyToQueueName(String replyToQueueName) {
        this.replyToQueueName = replyToQueueName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
