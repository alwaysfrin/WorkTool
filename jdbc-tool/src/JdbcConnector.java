/**
 * @Description :   数据库连接对象
 * @Reference :
 * @Author :    frin
 * @Date :  2018-07-23 14:31
 * @Modify :
 **/
public class JdbcConnector {

    public static final String MY_SQL_DRIVER = "org.gjt.mm.mysql.Driver" ;
    public static final String SQL_SERVER_DRIVER = "net.sourceforge.jtds.jdbc.Driver" ;
    public static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";

    private int dbType = 1; //数据库类型
    private String dbInstance;  //数据库实例
    private String username;    //用户名
    private String pwd; //密码
    private String ip;  //数据库地址
    private int port;   //数据库端口
    private String url; //完整url连接路径
    private String driver;  // 驱动

    /**
     * 支持的数据库枚举
     */
    public enum DB_TYPE_SUPPORT {
        MY_SQL(1,"MY_SQL"), SQL_SERVER(2, "SQL_SERVER"), ORACLE(3,"ORACLE");

        private int value = 1;
        private String desc = null;

        DB_TYPE_SUPPORT(int type, String desc){
            this.value = type;
            this.desc = desc;
        }

        public int getValue(){
            return value;
        }

        public static String valueOf(int value){
            String tmp = null;
            for(int i = 0; i< values().length;i++) {
                if(values()[i].value == value){
                    tmp = values()[i].desc;
                }
            }
            return tmp;
        }
    }

    public JdbcConnector(int dbType, String ip, String dbInstance, String username, String pwd) {
        this.dbType = dbType;
        this.dbInstance = dbInstance;
        this.username = username;
        this.pwd = pwd;
        this.ip = ip;
        if (dbType == DB_TYPE_SUPPORT.MY_SQL.getValue()){
            port = 3306;
        }else if (dbType == DB_TYPE_SUPPORT.SQL_SERVER.getValue()){
            port = 1433;
        }else {
            port = 1521;
        }

        initDriverAndUrl();
    }

    public JdbcConnector(int dbType, String ip, int port, String dbInstance, String username, String pwd) {
        this.dbType = dbType;
        this.dbInstance = dbInstance;
        this.username = username;
        this.pwd = pwd;
        this.ip = ip;
        this.port = port;

        initDriverAndUrl();
    }

    private void initDriverAndUrl(){
        if (dbType == DB_TYPE_SUPPORT.MY_SQL.getValue()){
            driver = MY_SQL_DRIVER;
            url = "jdbc:mysql://"+ip+":"+port+"/" + dbInstance;
        }else if (dbType == DB_TYPE_SUPPORT.SQL_SERVER.getValue()){
            driver = SQL_SERVER_DRIVER;
            url = "jdbc:jtds:sqlserver://"+ip+":"+port+"/" + dbInstance;
        }else {
            driver = ORACLE_DRIVER;
            url = "jdbc:oracle:thin:"+ip+":"+port+":" + dbInstance;
        }
    }

    public int getDbType() {
        return dbType;
    }

    public void setDbType(int dbType) {
        this.dbType = dbType;
    }

    public String getDbInstance() {
        return dbInstance;
    }

    public void setDbInstance(String dbInstance) {
        this.dbInstance = dbInstance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return url;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
}
