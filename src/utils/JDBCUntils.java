package utils;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

//JDBC工具类
public class JDBCUntils {

    private JDBCUntils(){
    }

    private static String driverClass;
    private static String url;
    private static String username;
    private static String password;
    private static Connection con;

    static {
        try {
            InputStream is = JDBCUntils.class.getClassLoader().getResourceAsStream("jd.properties");
            Properties prop=new Properties();
            prop.load(is);
            driverClass=prop.getProperty("driverClass");
            url=prop.getProperty("url");
            username=prop.getProperty("username");
            password=prop.getProperty("password");
            Class.forName(driverClass);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        try {
            con= DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }


    public  static void close(Connection con, Statement stat, ResultSet rs){
        if(con!=null)
        {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(stat!=null)
        {
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(rs!=null)
        {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public  static void close(Connection con, Statement stat){
        if(con!=null)
        {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(stat!=null)
        {
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
