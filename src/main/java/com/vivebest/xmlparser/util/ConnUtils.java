package com.vivebest.xmlparser.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class ConnUtils {

	private static Connection conn = null;
	
    /**
     * 获取数据库连接
     * @return
     */
    public static Connection getConnection() {
    	
    	try {
			if(conn != null && !conn.isClosed()) {
				return conn;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
    	
    	String fileName = "config/db.properties";
		Properties prop = null;
		try {
			prop = FileUtil.loadResourceFile(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String driverClassName = prop.getProperty("driverClass");
		String url = prop.getProperty("url");
		String username = prop.getProperty("username");
		String password = prop.getProperty("password");
    	
        try {
            Class.forName(driverClassName);
            conn = DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException e) {
            //没有找到数据库驱动
            e.printStackTrace();
        } catch (Exception e) {
            //获取数据库连接异常
            e.printStackTrace();
        }
        return conn;
    }
    
    public static int manipulateSql (Connection conn, String sql) {
        int errNum = 0;
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();
        }catch(SQLException e) {
            errNum = -1;
            e.printStackTrace();
        }finally {
            try { if (pst != null) pst.close(); } catch(Exception e) { }
        }
        return errNum;
    } 
    
    public static void release(Connection conn, Statement stmt, ResultSet rs) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
