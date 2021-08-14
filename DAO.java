package JDBC.DAO;


import JDBC.Exception.conException;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * BaseDAO
 * wyl 2021-5
 **/
@SuppressWarnings("all")
public abstract class DAO {

    /*
    连接
     */
    public Connection getConnection() throws Exception {
        //读取配置文件文件
        FileInputStream fileInputStream = new FileInputStream("G:\\idea\\JDBC\\properties\\mysql.properties");
        //加载配置文件
        Properties properties = new Properties();
        properties.load(fileInputStream);                 //获取连接信息
        String driver = properties.getProperty("driver");
        String url = properties.getProperty("url");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, user, password);
        if (connection == null) {
            throw new conException("连接失败!!!!!!");
        }
        return connection;
    }

    /*
    断开连接
     */
    public void closeResourse(Connection connection, Statement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void closeResourse(Connection connection, Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void closeResourse(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /*
    更新成功 返回1 失败返回0
     */
    @Deprecated
    public int updateStatement(Connection connection, String sql) {
        Statement statement = null;
        try {
            //得到语句对象
            statement = connection.createStatement();
            //执行sql语句
            return statement.executeUpdate(sql);    //影响行数
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //断开连接
            closeResourse(null, statement);
        }
        return 0;
    }

    /*
    更新成功 返回1 失败返回0
     */
    public int UpdatePreparedStatement(Connection connection, String sql, Object... args) {   //sql中占位符的个数于可变性惨的长度相同
        PreparedStatement preparedStatement = null;
        try {
            //预编译
            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            //执行操作
            //preparedStatement.execute();    执行语句<查询>有返回结果为true   <增删改>没有有返回结果false
            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //断开连接
            closeResourse(null, preparedStatement);
        }
        return 0;
    }

    /*
    查询一个值
     */
    @Deprecated
    public <T> T getInstanceStatement(Connection connection, Class<T> clazz, String sql) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            //得到语句对象
            statement = connection.createStatement();
            //执行语句获取结果集
            resultSet = statement.executeQuery(sql);
            //获取结果集元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            //得到结果集列数
            int columnCount = metaData.getColumnCount();
            if (resultSet.next()) {
                T t = clazz.newInstance();
                //处理数据
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = resultSet.getObject(i + 1);
                    //获取列别名          有别名用别名  没有则使用列名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResourse(null, statement, resultSet);
        }
        return null;
    }

    /*
    查询多个值
     */
    @Deprecated
    public <T> List<T> getForListStatement(Connection connection, Class<T> clazz, String sql) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            //得到语句对象
            statement = connection.createStatement();
            //执行语句获取结果集
            resultSet = statement.executeQuery(sql);
            //获取结果集元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            //得到结果集列数
            int columnCount = metaData.getColumnCount();
            //创建集合
            ArrayList<T> list = new ArrayList<T>();
            while (resultSet.next()) {
                T t = clazz.newInstance();
                //处理数据  给每一个t对象肤质，并且加入集合中
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = resultSet.getObject(i + 1);
                    //获取列别名    有别名用别名  没有则使用列名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResourse(null, statement, resultSet);
        }
        return null;
    }

    /*
    查询一个值  查到返回该对象
     */
    public <T> T getInstancePreparedStatement(Connection connection, Class<T> clazz, String sql, Object... args) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            //预编译sql语句
            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            //执行语句获取结果集
            resultSet = preparedStatement.executeQuery();
            //获取结果集元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            //得到结果集列数
            int columnCount = metaData.getColumnCount();
            if (resultSet.next()) {
                T t = clazz.newInstance();
                //处理数据
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = resultSet.getObject(i + 1);
                    //获取列别名      有别名用别名  没有则使用列名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResourse(null, preparedStatement, resultSet);
        }
        return null;
    }

    /*
    查询多个值
     */
    public <T> List<T> getForListPreparedStatement(Connection connection, Class<T> clazz, String sql, Object... args) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            //预编译sql语句
            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            //执行语句获取结果集
            resultSet = preparedStatement.executeQuery();
            //获取结果集元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            //得到结果集列数
            int columnCount = metaData.getColumnCount();
            //创建集合
            ArrayList<T> list = new ArrayList<T>();
            while (resultSet.next()) {
                T t = clazz.newInstance();
                //处理数据  给每一个t对象肤质，并且加入集合中
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = resultSet.getObject(i + 1);
                    //获取列别名    有别名用别名  没有则使用列名
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResourse(null, preparedStatement, resultSet);
        }
        return null;
    }

    //查询特殊值
    public <E> E getValue(Connection connection, String sql, Object... args) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return (E) resultSet.getObject(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeResourse(null, preparedStatement, resultSet);
        }
        return null;
    }
}
