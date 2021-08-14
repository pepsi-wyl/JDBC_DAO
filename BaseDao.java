package Dormitory_M.DAO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * 操作数据库的基类--静态类
 */
public class BaseDao {

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("G:\\idea\\Dormitory_M\\src\\main\\resources\\mysql.properties"));                 //获取连接信息
            Class.forName(properties.getProperty("driver"));
            connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return connection;
        }
    }

    /**
     * 查询操作
     */
    public static ResultSet query(Connection connection, String sql, Object... params) throws Exception {
        PreparedStatement pstm = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            pstm.setObject(i + 1, params[i]);
        }
        return pstm.executeQuery();
    }

    /**
     * 更新操作
     */
    public static int update(Connection connection, String sql, Object... params) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++)
                preparedStatement.setObject(i + 1, params[i]);
            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResource(null, preparedStatement, null);
        }
        return 0;
    }

    /**
     * 释放资源
     */
    public static boolean closeResource(Connection connection, PreparedStatement pstm, ResultSet rs) {
        boolean flag = true;
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (pstm != null) {
            try {
                pstm.close();
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }

}
