package jdbc;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class myJDBC {

	// 锟斤拷url为缺省锟斤拷式锟斤拷省锟斤拷锟斤拷锟斤拷锟斤拷锟剿口ｏ拷
    // 锟斤拷锟斤拷为锟斤拷jdbc:mysql//localhost:3306/test
	
	
	
    static String url = "jdbc:mysql://localhost:3306/test";
    static String name = "root";
    static String password = "root";
    static Connection conn = null;
    private static myJDBC jdbcUtilSingle = null;
 
    public static myJDBC getInitJDBCUtil() {
        if (jdbcUtilSingle == null) {
            // 锟斤拷锟斤拷锟斤拷锟� 锟斤拷止锟竭程诧拷锟斤拷
            synchronized (myJDBC.class) {
                if (jdbcUtilSingle == null) {
                    jdbcUtilSingle = new myJDBC();
                }
            }
        }
        return jdbcUtilSingle;
    }
 
    private myJDBC() {
    }
 
    // 通锟斤拷锟斤拷态锟斤拷锟斤拷锟阶拷锟斤拷锟斤拷菘锟斤拷锟斤拷锟斤拷锟斤拷锟街ぷ拷锟街恢达拷锟揭伙拷锟�
    static {
        try {
            // 注锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟铰凤拷式锟斤拷
            // 1.通锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷注锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷注锟斤拷锟斤拷锟轿ｏ拷锟斤拷锟揭伙拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷嗖伙拷锟斤拷锟斤拷蔷捅锟斤拷锟斤拷恕锟�
            // DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            // 2.锟斤拷3锟斤拷锟斤拷
            // System.setProperty("jdbc.drivers","com.mysql.jdbc.Driver");
            Class.forName("com.mysql.jdbc.Driver");// 锟狡硷拷使锟矫凤拷式
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
 
    // 锟斤拷锟斤拷锟斤拷锟�
    public Connection getConnection() {
        try {
            conn = DriverManager.getConnection(url,name,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
 
    }
 
    // 锟截憋拷锟斤拷锟斤拷
    public void closeConnection(ResultSet rs, Statement statement, Connection con) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
    public void closeResultset(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }
	
	
}
