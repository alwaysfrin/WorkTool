import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
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
public class DBFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(DBFactory.class);
    private Connection con = null;

    public interface IResultSetCall<T>{
        public T invoke(ResultSet rs) throws SQLException;
    }

    public Connection openConnection(int dbType, String ip, String dbInstance, String username, String pwd)
                                                            throws SQLException, IOException, ClassNotFoundException {
        return openConnection(new JdbcConnector(dbType,ip,dbInstance,username,pwd));
    }

    public Connection openConnection(int dbType, String ip,int port, String dbInstance, String username, String pwd)
                                                            throws SQLException, ClassNotFoundException, IOException {
        return openConnection(new JdbcConnector(dbType,ip,port,dbInstance,username,pwd));
    }

    public Connection openConnection(JdbcConnector connector) throws SQLException, ClassNotFoundException, IOException {
        if(con == null || con.isClosed()) {
            Class.forName(connector.getDriver());
            con = DriverManager.getConnection(connector.getUrl(), connector.getUsername(), connector.getPwd());
        }
        return con;
    }

    public void closeConnection(Connection con) throws SQLException {
        if(con != null && !con.isClosed()){
            con.close();
            System.gc();
        }
    }

    public List<Map<String, Object>> queryMapList(Connection con, String sql) throws SQLException {
        con.setAutoCommit(false);
        con.setReadOnly(true);

        List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
        Statement preStmt = null;
        ResultSet rs = null;
        try {
            preStmt = con.createStatement();
            rs = preStmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (null != rs && rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < columnCount; i++) {
                    String name = rsmd.getColumnName(i + 1);
                    Object value = rs.getObject(name);
                    map.put(name, value);
                }
                lists.add(map);
            }
        }catch(Exception e){
            LOGGER.error(e.getMessage(),e);
        } finally {
            if (null != rs)
                rs.close();
            if (null != preStmt)
                preStmt.close();
        }
        return lists;
    }

    public List<Map<String, Object>> queryMapList(Connection con, String sql, Object... params)
            throws SQLException, InstantiationException, IllegalAccessException {
        List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        try {
            preStmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preStmt.setObject(i + 1, params[i]);// 下标从1开始
            }
            rs = preStmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (null != rs && rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < columnCount; i++) {
                    String name = rsmd.getColumnName(i + 1);
                    Object value = rs.getObject(name);
                    map.put(name, value);
                }
                lists.add(map);
            }
        }catch(Exception e){
            LOGGER.error(e.getMessage(),e);
        } finally {
            if (null != rs)
                rs.close();
            if (null != preStmt)
                preStmt.close();
        }
        return lists;
    }

    public <T> List<T> queryBeanList(Connection con, String sql, Class<T> beanClass) throws SQLException,
            InstantiationException, IllegalAccessException {
        List<T> lists = new ArrayList<T>();
        Statement stmt = null;
        ResultSet rs = null;
        Field[] fields = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            fields = beanClass.getDeclaredFields();
            for (Field f : fields)
                f.setAccessible(true);
            while (null != rs && rs.next()) {
                T t = beanClass.newInstance();
                for (Field f : fields) {
                    String name = f.getName();
                    try {
                        Object value = rs.getObject(name);
                        setValue(t, f, value);
                    } catch (Exception e) {
                    }
                }
                lists.add(t);
            }
        }catch(Exception e){
            LOGGER.error(e.getMessage(),e);
        } finally {
            if (null != rs)
                rs.close();
            if (null != stmt)
                stmt.close();
        }
        return lists;
    }

    public <T> List<T> queryBeanList(Connection con, String sql, Class<T> beanClass, Object... params)
            throws SQLException, InstantiationException, IllegalAccessException {
        List<T> lists = new ArrayList<T>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Field[] fields = null;
        try {
            preStmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++)
                preStmt.setObject(i + 1, params[i]);// 下标从1开始
            rs = preStmt.executeQuery();
            fields = beanClass.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
            }
            while (null != rs && rs.next()) {
                T t = beanClass.newInstance();
                for (Field f : fields) {
                    String name = f.getName();
                    try {
                        Object value = rs.getObject(name);
                        setValue(t, f, value);
                    } catch (Exception e) {
                    }
                }
                lists.add(t);
            }
        }catch(Exception e){
            LOGGER.error(e.getMessage(),e);
        } finally {
            if (null != rs)
                rs.close();
            if (null != preStmt)
                preStmt.close();
        }
        return lists;
    }

    public <T> List<T> queryBeanList(Connection con, String sql, IResultSetCall<T> qdi) throws SQLException {
        List<T> lists = new ArrayList<T>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (null != rs && rs.next())
                lists.add(qdi.invoke(rs));
        }catch(Exception e){
            LOGGER.error(e.getMessage(),e);
        } finally {
            if (null != rs)
                rs.close();
            if (null != stmt)
                stmt.close();
        }
        return lists;
    }

    public <T> List<T> queryBeanList(Connection con, String sql, IResultSetCall<T> qdi, Object... params)
            throws SQLException {
        List<T> lists = new ArrayList<T>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        try {
            preStmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++)
                preStmt.setObject(i + 1, params[i]);
            rs = preStmt.executeQuery();
            while (null != rs && rs.next())
                lists.add(qdi.invoke(rs));
        }catch(Exception e){
            LOGGER.error(e.getMessage(),e);
        } finally {
            if (null != rs)
                rs.close();
            if (null != preStmt)
                preStmt.close();
        }
        return lists;
    }

    public <T> T queryBean(Connection con, String sql, Class<T> beanClass) throws SQLException,
            InstantiationException, IllegalAccessException {
        List<T> lists = queryBeanList(con, sql, beanClass);
        if (lists.size() != 1)
            throw new SQLException("SqlError：期待一行返回值，却返回了太多行！");
        return lists.get(0);
    }

    public <T> T queryBean(Connection con, String sql, Class<T> beanClass, Object... params)
            throws SQLException, InstantiationException, IllegalAccessException {
        List<T> lists = queryBeanList(con, sql, beanClass, params);
        if (lists.size() != 1)
            throw new SQLException("SqlError：期待一行返回值，却返回了太多行！");
        return lists.get(0);
    }

    public <T> List<T> queryObjectList(Connection con, String sql, Class<T> objClass) throws SQLException,
            InstantiationException, IllegalAccessException {
        List<T> lists = new ArrayList<T>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            label: while (null != rs && rs.next()) {
                Constructor<?>[] constor = objClass.getConstructors();
                for (Constructor<?> c : constor) {
                    Object value = rs.getObject(1);
                    try {
                        lists.add((T) c.newInstance(value));
                        continue label;
                    } catch (Exception e) {
                    }
                }
            }
        }catch(Exception e){
            LOGGER.error(e.getMessage(),e);
        } finally {
            if (null != rs)
                rs.close();
            if (null != stmt)
                stmt.close();
        }
        return lists;
    }

    public <T> List<T> queryObjectList(Connection con, String sql, Class<T> objClass, Object... params)
            throws SQLException, InstantiationException, IllegalAccessException {
        List<T> lists = new ArrayList<T>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        try {
            preStmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++)
                preStmt.setObject(i + 1, params[i]);
            rs = preStmt.executeQuery();
            label: while (null != rs && rs.next()) {
                Constructor<?>[] constor = objClass.getConstructors();
                for (Constructor<?> c : constor) {
                    String value = rs.getObject(1).toString();
                    try {
                        T t = (T) c.newInstance(value);
                        lists.add(t);
                        continue label;
                    } catch(Exception e){
                        LOGGER.error(e.getMessage(),e);
                    }
                }
            }
        }catch(Exception e){
            LOGGER.error(e.getMessage(),e);
        } finally {
            if (null != rs)
                rs.close();
            if (null != preStmt)
                preStmt.close();
        }
        return lists;
    }

    public <T> T queryObject(Connection con, String sql, Class<T> objClass) throws SQLException,
            InstantiationException, IllegalAccessException {
        List<T> lists = queryObjectList(con, sql, objClass);
        if (lists.size() != 1)
            throw new SQLException("SqlError：期待一行返回值，却返回了太多行！");
        return lists.get(0);
    }

    public <T> T queryObject(Connection con, String sql, Class<T> objClass, Object... params)
            throws SQLException, InstantiationException, IllegalAccessException {
        List<T> lists = queryObjectList(con, sql, objClass, params);
        if (lists.size() != 1)
            throw new SQLException("SqlError：期待一行返回值，却返回了太多行！");
        return lists.get(0);
    }

    public <T> List<List<T>> listLimit(List<T> lists, int pageSize) {
        List<List<T>> llists = new ArrayList<List<T>>();
        for (int i = 0; i < lists.size(); i = i + pageSize) {
            try {
                List<T> list = lists.subList(i, i + pageSize);
                llists.add(list);
            } catch (IndexOutOfBoundsException e) {
                List<T> list = lists.subList(i, i + (lists.size() % pageSize));
                llists.add(list);
            }
        }
        return llists;
    }

    private <T> void setValue(T t, Field f, Object value) throws IllegalAccessException {
        if (value == null) {
            return;
        }
        String v = value.toString();
        String n = f.getType().getName();
        if ("java.lang.Byte".equals(n) || "byte".equals(n)) {
            f.set(t, Byte.parseByte(v));
        } else if ("java.lang.Short".equals(n) || "short".equals(n)) {
            f.set(t, Short.parseShort(v));
        } else if ("java.lang.Integer".equals(n) || "int".equals(n)) {
            f.set(t, Integer.parseInt(v));
        } else if ("java.lang.Long".equals(n) || "long".equals(n)) {
            f.set(t, Long.parseLong(v));
        } else if ("java.lang.Float".equals(n) || "float".equals(n)) {
            f.set(t, Float.parseFloat(v));
        } else if ("java.lang.Double".equals(n) || "double".equals(n)) {
            f.set(t, Double.parseDouble(v));
        } else if ("java.lang.String".equals(n)) {
            f.set(t, value.toString());
        } else if ("java.lang.Character".equals(n) || "char".equals(n)) {
            f.set(t, (Character) value);
        } else if ("java.lang.Date".equals(n)) {
            f.set(t, new Date(((java.sql.Date) value).getTime()));
        } else if ("java.lang.Timer".equals(n)) {
            f.set(t, new Time(((java.sql.Time) value).getTime()));
        } else if ("java.sql.Timestamp".equals(n)) {
            f.set(t, (java.sql.Timestamp) value);
        } else if("oracle.sql.CLOB".equals(n)){
            f.set(t,(Clob)value);
            //clob需要单独转换成String  parseClobToString
        } else {
            System.out.println("SqlError：暂时不支持此数据类型，请使用其他类型代替此类型！");
        }
    }

    /**
     * Clob转换成string
     * @param clob
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public String parseClobToString(Clob clob) throws SQLException, IOException {
        BufferedReader br = new BufferedReader(clob.getCharacterStream());
        String s = null;

        StringBuffer sb = new StringBuffer();
        while ((s = br.readLine()) != null){
            sb.append(s);
        }
        return sb.toString();
    }

    /******************** 事务性操作 ********************/

    public int execute(Connection con, String sql) throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            return stmt.executeUpdate(sql);
        } catch(Exception e) {
            con.rollback();
            LOGGER.error(e.getMessage(),e);
        } finally {
            if (null != stmt)
                stmt.close();
        }
        return 0;
    }

    public int execute(Connection con, String sql, Object... params) throws SQLException {
        PreparedStatement preStmt = null;
        try {
            preStmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preStmt.setObject(i + 1, params[i]);// 下标从1开始
            }
            return preStmt.executeUpdate();
        } catch(Exception e) {
            con.rollback();
            LOGGER.error(e.getMessage(),e);
        }finally {
            if (null != preStmt)
                preStmt.close();
        }
        return 0;
    }

    public int[] executeAsBatch(Connection con, List<String> sqlList) throws SQLException {
        return executeAsBatch(con, sqlList.toArray(new String[] {}));
    }

    public int[] executeAsBatch(Connection con, String[] sqlArray) throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            for (String sql : sqlArray) {
                stmt.addBatch(sql);
            }
            return stmt.executeBatch();
        } catch(Exception e) {
            con.rollback();
            LOGGER.error(e.getMessage(),e);
        }finally {
            if (null != stmt) {
                stmt.close();
            }
        }
        return null;
    }

    public int[] executeAsBatch(Connection con, String sql, Object[][] params) throws SQLException {
        PreparedStatement preStmt = null;
        try {
            preStmt = con.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                Object[] rowParams = params[i];
                for (int k = 0; k < rowParams.length; k++) {
                    Object obj = rowParams[k];
                    preStmt.setObject(k + 1, obj);
                }
                preStmt.addBatch();
            }
            return preStmt.executeBatch();
        } catch(Exception e) {
            con.rollback();
            LOGGER.error(e.getMessage(),e);
        } finally {
            if (null != preStmt) {
                preStmt.close();
            }
        }
        return null;
    }

    public void executeProcedure(Connection con, String procedureName, Object... params) throws SQLException {
        CallableStatement proc = null;
        try {
            proc = con.prepareCall(procedureName);
            for (int i = 0; i < params.length; i++) {
                proc.setObject(i + 1, params[i]);
            }
            proc.execute();
        } catch(Exception e) {
            con.rollback();
            LOGGER.error(e.getMessage(),e);
        }finally {
            if (null != proc)
                proc.close();
        }
    }

    public boolean executeProcedureReturnErrorMsg(Connection con, String procedureName, StringBuffer errorMsg,
                                                         Object... params) throws SQLException {
        CallableStatement proc = null;
        try {
            proc = con.prepareCall(procedureName);
            proc.registerOutParameter(1, OracleTypes.VARCHAR);
            for (int i = 0; i < params.length; i++) {
                proc.setObject(i + 2, params[i]);
            }
            boolean b = proc.execute();
            errorMsg.append(proc.getString(1));
            return b;
        } catch(Exception e) {
            con.rollback();
            LOGGER.error(e.getMessage(),e);
        }finally {
            if (null != proc)
                proc.close();
        }
        return false;
    }

    public <T> List<T> executeProcedureReturnCursor(Connection con, String procedureName, Class<T> beanClass,
                                                           Object... params) throws SQLException, InstantiationException, IllegalAccessException {
        List<T> lists = new ArrayList<T>();
        CallableStatement proc = null;
        ResultSet rs = null;
        try {
            proc = con.prepareCall(procedureName);
            proc.registerOutParameter(1, OracleTypes.CURSOR);
            for (int i = 0; i < params.length; i++) {
                proc.setObject(i + 2, params[i]);
            }
            boolean b = proc.execute();
            if (b) {
                rs = (ResultSet) proc.getObject(1);
                while (null != rs && rs.next()) {
                    T t = beanClass.newInstance();
                    Field[] fields = beanClass.getDeclaredFields();
                    for (Field f : fields) {
                        f.setAccessible(true);
                        String name = f.getName();
                        try {
                            Object value = rs.getObject(name);
                            setValue(t, f, value);
                        } catch (Exception e) {
                        }
                    }
                    lists.add(t);
                }
            }
        } catch(Exception e) {
            con.rollback();
            LOGGER.error(e.getMessage(),e);
        }finally {
            if (null != rs)
                rs.close();
            if (null != proc)
                proc.close();
        }
        return lists;
    }

}
