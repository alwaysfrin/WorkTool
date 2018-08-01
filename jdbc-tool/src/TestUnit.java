import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.fail;

/**
 * @Description :
 * @Reference :
 * @Author :
 * @Date :
 * @Modify :
 **/
public class TestUnit {

    private DBFactory dbFactory = null;
    private Connection con = null;

    @Before
    public void setUp() throws Exception {
        dbFactory = new DBFactory();
        con = dbFactory.openConnection(JdbcConnector.DB_TYPE_SUPPORT.ORACLE.getValue(),"localhost","dbname","username","pwd");
    }

    @After
    public void tearDown() throws Exception {
        dbFactory.closeConnection(con);
    }

    @Test
    public void testQueryBeanListConnectionStringClassOfT() {
        String sql = "SELECT * FROM USERS";
        try {
            List<Users> emList = dbFactory.queryBeanList(con, sql, Users.class);
            print(emList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testQueryBeanListConnectionStringClassOfTObjectArray() {
        String sql = "SELECT * FROM USERS t WHERE t.salary > ? and T.JOB_ID = ?";
        try {
            List<Users> userList = dbFactory.queryBeanList(con, sql, Users.class, 5000, "ST_MAN");
            print(userList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testQueryBeanListConnectionStringIResultSetCallOfTObjectArray() {
        String sql = "SELECT first_name, last_name, salary FROM USERS t WHERE t.salary > ? and T.JOB_ID = ?";
        try {
            List<Users> emList = dbFactory.queryBeanList(con, sql, new DBFactory.IResultSetCall<Users>() {
                public Users invoke(ResultSet rs) throws SQLException {
                    Users e = new Users();
                    e.setUsername(rs.getString("username"));
                    e.setPwd(rs.getString("pwd"));
                    e.setSalary(rs.getDouble("salary"));
                    return e;
                }
            }, 5000, "ST_MAN");
            print(emList);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testQueryObjectListConnectionStringClassOfT() {
        String sql = "SELECT username FROM USERS u";
        try {
            List<String> lists = dbFactory.queryObjectList(con, sql, String.class);
            print(lists);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testQueryObjectListConnectionStringClassOfTObjectArray() {
        String sql = "SELECT salary FROM USERS t WHERE t.salary > ? and t.pwd = ?";
        try {
            List<Double> lists = dbFactory.queryObjectList(con, sql, Double.class, 2000, "yourpwd");
            print(lists);
        } catch (Exception e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testQueryBeanConnectionStringClassOfT() {
        String sql = "SELECT * FROM USERS t WHERE t.username in (120)";
        try {
            Users emp = dbFactory.queryBean(con, sql, Users.class);
            print(emp);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testQueryBeanConnectionStringClassOfTObjectArray() {
        String sql = "SELECT * FROM USERS t WHERE t.username = ?";
        try {
            Users emp = dbFactory.queryBean(con, sql, Users.class, 120);
            print(emp);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testQueryObjectConnectionStringClassOfT() {
        String sql = "SELECT email FROM USERS t WHERE t.username in (120)";
        try {
            String s = dbFactory.queryObject(con, sql, String.class);
            print(s);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testQueryObjectConnectionStringClassOfTObjectArray() {
        String sql = "SELECT salary FROM USERS t WHERE t.username in (?)";
        try {
            Double d = dbFactory.queryObject(con, sql, Double.class, 12);
            print(d);
        } catch (Exception e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testExecuteConnectionStringObjectArray() {
        String sql = "UPDATE USERS t SET t.salary =? WHERE t.username =?";
        try {
            int count = dbFactory.execute(con, sql, 20000, 120);
            Assert.assertTrue(count == 1);
            sql = "SELECT t.salary FROM USERS t WHERE t.username =?";
            Double d = dbFactory.queryObject(con, sql, Double.class, 120);
            Assert.assertTrue(d - 20000 == 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testQueryMapList() {
        String sql = "SELECT first_name, last_name, salary FROM USERS t WHERE t.salary > ? and T.JOB_ID = ?";
        try {
            List<Map<String, Object>> lists = dbFactory.queryMapList(con, sql, 3000, "ST_MAN");
            print(lists);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testExecuteProcedure() {
        try {
            dbFactory.executeProcedure(con, "{CALL procedure(?,?)}", "param1","param2");
            System.out.println("执行存储过程成功");
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testExecuteAsBatch() {
        try {
            List<String> sqlList = new ArrayList<String>();
            sqlList.add("UPDATE Users t SET t.pwd = 'ok' WHERE t.username = '232s43' ");
            sqlList.add("UPDATE Users t SET t.pwd = 'ok' WHERE t.username = '232f42' ");
            sqlList.add("UPDATE Users t SET t.pwd = 'ok' WHERE t.username = '23g2423' ");
            sqlList.add("UPDATE Users t SET t.pwd = 'ok' WHERE t.username = '232434s' ");
            dbFactory.executeAsBatch(con, sqlList);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testExecuteAsBatchForPre() {
        try {
            dbFactory.executeAsBatch(con, "UPDATE USERS t SET t.first_name = ? WHERE t.last_name = ? ",
                    new Object[][] {{ "ok", "235jklsd" }, { "no", "jg4ti324" }, { "no1", "111" }, { "no2", "32423" } });
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    private void print(Object obj) {
        if (obj instanceof List) {
            List list = (List) obj;
            for (Object o : list) {
                if (o instanceof Map) {
                    Map<String, Object> map = (Map<String, Object>) o;
                    Set<String> set = map.keySet();
                    for (String key : set) {
                        Object value = map.get(key);
                        System.out.print(key + ":" + value + "\t");
                    }
                    System.out.println();
                } else {
                    System.out.println(o);
                }
            }
            System.out.println("总共查询出数据数量是：" + list.size());
        } else {
            System.out.println(obj);
        }
    }

}
