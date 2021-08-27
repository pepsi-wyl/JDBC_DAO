# JDBC

package JDBC.DAO;


导入 com.alibaba.druid.pool.DruidDataSourceFactory；

导入 java.io.FileInputStream ;
导入 java.lang.reflect.Field ;
导入 java.sql.* ;
导入 java.util.ArrayList；
导入 java.util.List ;
import java.util.Properties;

/**
 * BaseDAO
 * 节日 2021-5
 **/
@SuppressWarnings（“全部”）
公共 抽象 类 DAO {

    /*
    连接
     */
    公共 连接 getConnection ()抛出 异常{
//        属性 properties = new Properties();
//        properties.load(new FileInputStream("G:\\idea\\JDBC\\properties\\mysql.properties"));                 //获取连接信息
//         Class.forName(properties.getProperty("driver"));
//         return DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));

        属性properties =  new  Properties ();
        属性。负载（新 的FileInputStream（“ G：\\想法\\ JDBC \\属性\\ druid.properties ”））;
        返回 DruidDataSourceFactory 。createDataSource(properties) 。获取连接（）；
    }
    /*
    断开连接
     */
    公共 无效 closeResourse（连接 连接，声明 语句，结果集 的resultSet）{
        如果（结果集！=  null）{
            试试{
                结果集。关闭（）;
            } catch ( SQLException throwables) {
                投掷物。打印堆栈跟踪（）；
            }
        }
        如果（语句！=  null）{
            试试{
                声明。关闭（）;
            } catch ( SQLException throwables) {
                投掷物。打印堆栈跟踪（）；
            }
        }
        如果（连接！=  null）{
            试试{
                连接。关闭（）;
            } catch ( SQLException throwables) {
                投掷物。打印堆栈跟踪（）；
            }
        }
    }

    public  void  closeResourse（连接 连接，声明 语句）{
        如果（语句！=  null）{
            试试{
                声明。关闭（）;
            } catch ( SQLException throwables) {
                投掷物。打印堆栈跟踪（）；
            }
        }
        如果（连接！=  null）{
            试试{
                连接。关闭（）;
            } catch ( SQLException throwables) {
                投掷物。打印堆栈跟踪（）；
            }
        }
    }

    public  void  closeResourse（连接 连接）{
        如果（连接！=  null）{
            试试{
                连接。关闭（）;
            } catch ( SQLException throwables) {
                投掷物。打印堆栈跟踪（）；
            }
        }
    }

    /*
    更新成功 返回1 失败返回0
     */
    @已弃用
    public  int  updateStatement（连接 连接，字符串 sql）{
        语句语句=  null ;
        试试{
            //得到语句对象
            声明=连接。创建语句（）；
            //执行sql语句
            return statement.executeUpdate(sql);    //影响行数
        }捕获（异常e）{
            电子。打印堆栈跟踪（）；
        }最后{
            //断开连接
            closeResource( null , 语句);
        }
        返回 0 ;
    }

    /*
    更新成功 返回1 失败返回0
     */
    public int UpdatePreparedStatement(Connection connection, String sql, Object... args) {   //sql中占位符的个数于可变性惨的长度相同
        的PreparedStatement preparedStatement时= 空;
        试试{
            //预编译
            PreparedStatement =连接。准备语句（sql）；
            //填充占位符
            对（INT我=  0 ;我< ARGS 。长度;我++）{
                准备好的语句。setObject(i +  1 , args[i]);
            }
            //执行操作
            //preparedStatement.execute();    执行语句<查询>有返回结果为true   <增删改>没有有返回结果false
            返回preparedStatement 。执行更新（）；
        }捕获（异常e）{
            电子。打印堆栈跟踪（）；
        }最后{
            //断开连接
            关闭资源（空，preparedStatement）；
        }
        返回 0 ;
    }

    /*
    查询一个值
     */
    @已弃用
    public  < T >  T  getInstanceStatement（连接 连接，Class< T >  clazz，String  sql）{
        语句语句=  null ;
        结果集的resultSet = 空;
        试试{
            //得到语句对象
            声明=连接。创建语句（）；
            //执行语句获取结果集
            结果集=语句。执行查询（sql）；
            //获取结果集元数据
            ResultSetMetaData的元数据=的resultSet 。获取元数据（）；
            //得到结果集列数
            INT信息columnCount =元数据。getColumnCount();
            如果（结果集。下一个（））{
                T t =克拉兹。新实例（）；
                //处理数据
                for ( int i =  0 ; i < columnCount; i ++ ) {
                    //获取列值
                    对象columnValue = resultSet 。getObject(i +  1 );
                    //获取列别名          有别名用别名  没有则使用列名
                    字符串columnLabel = metaData 。getColumnLabel(i +  1 );
                    //反射
                    字段字段= clazz 。getDeclaredField(columnLabel);
                    领域。setAccessible( true );
                    领域。设置（t，列值）；
                }
                返回t;
            }
        }捕获（异常e）{
            电子。打印堆栈跟踪（）；
        }最后{
            closeResource( null , statement, resultSet);
        }
        返回 空；
    }

    /*
    查询多个值
     */
    @已弃用
    public  < T >  List< T >  getForListStatement（连接 连接，Class< T >  clazz，String  sql）{
        语句语句=  null ;
        结果集的resultSet = 空;
        试试{
            //得到语句对象
            声明=连接。创建语句（）；
            //执行语句获取结果集
            结果集=语句。执行查询（sql）；
            //获取结果集元数据
            ResultSetMetaData的元数据=的resultSet 。获取元数据（）；
            //得到结果集列数
            INT信息columnCount =元数据。getColumnCount();
            //创建集合
            ArrayList< T > list =  new  ArrayList< T > ();
            而（结果集。下一个（））{
                T t =克拉兹。新实例（）；
                //处理数据  给每一个t对象肤质，并且加入集合中
                for ( int i =  0 ; i < columnCount; i ++ ) {
                    //获取列值
                    对象columnValue = resultSet 。getObject(i +  1 );
                    //获取列别名    有别名用别名  没有则使用列名
                    字符串columnLabel = metaData 。getColumnLabel(i +  1 );
                    //反射
                    字段字段= clazz 。getDeclaredField(columnLabel);
                    领域。setAccessible( true );
                    领域。设置（t，列值）；
                }
                列表。添加（t）；
            }
            退货清单；
        }捕获（异常e）{
            电子。打印堆栈跟踪（）；
        }最后{
            closeResource( null , statement, resultSet);
        }
        返回 空；
    }

    /*
    查询一个值  查到返回该对象
     */
    public  < T >  T  getInstancePreparedStatement（连接 连接，Class< T >  clazz，String  sql，Object ... args）{
        的PreparedStatement preparedStatement时= 空;
        结果集的resultSet = 空;
        试试{
            //预编译sql语句
            PreparedStatement =连接。准备语句（sql）；
            //填充占位符
            对（INT我=  0 ;我< ARGS 。长度;我++）{
                准备好的语句。setObject(i +  1 , args[i]);
            }
            //执行语句获取结果集
            的resultSet = preparedStatement时。执行查询（）；
            //获取结果集元数据
            ResultSetMetaData的元数据=的resultSet 。获取元数据（）；
            //得到结果集列数
            INT信息columnCount =元数据。getColumnCount();
            如果（结果集。下一个（））{
                T t =克拉兹。新实例（）；
                //处理数据
                for ( int i =  0 ; i < columnCount; i ++ ) {
                    //获取列值
                    对象columnValue = resultSet 。getObject(i +  1 );
                    //获取列别名      有别名用别名  没有则使用列名
                    字符串columnLabel = metaData 。getColumnLabel(i +  1 );
                    //反射
                    字段字段= clazz 。getDeclaredField(columnLabel);
                    领域。setAccessible( true );
                    领域。设置（t，列值）；
                }
                返回t;
            }
        }捕获（异常e）{
            电子。打印堆栈跟踪（）；
        }最后{
            closeResourse( null , prepareStatement , resultSet);
        }
        返回 空；
    }

    /*
    查询多个值
     */
    public  < T >  List< T >  getForListPreparedStatement（连接 连接，Class< T >  clazz，String  sql，Object ... args）{
        的PreparedStatement preparedStatement时= 空;
        结果集的resultSet = 空;
        试试{
            //预编译sql语句
            PreparedStatement =连接。准备语句（sql）；
            //填充占位符
            对（INT我=  0 ;我< ARGS 。长度;我++）{
                准备好的语句。setObject(i +  1 , args[i]);
            }
            //执行语句获取结果集
            的resultSet = preparedStatement时。执行查询（）；
            //获取结果集元数据
            ResultSetMetaData的元数据=的resultSet 。获取元数据（）；
            //得到结果集列数
            INT信息columnCount =元数据。getColumnCount();
            //创建集合
            ArrayList< T > list =  new  ArrayList< T > ();
            而（结果集。下一个（））{
                T t =克拉兹。新实例（）；
                //处理数据  给每一个t对象肤质，并且加入集合中
                for ( int i =  0 ; i < columnCount; i ++ ) {
                    //获取列值
                    对象columnValue = resultSet 。getObject(i +  1 );
                    //获取列别名    有别名用别名  没有则使用列名
                    字符串columnLabel = metaData 。getColumnLabel(i +  1 );
                    //反射
                    字段字段= clazz 。getDeclaredField(columnLabel);
                    领域。setAccessible( true );
                    领域。设置（t，列值）；
                }
                列表。添加（t）；
            }
            退货清单；
        }捕获（异常e）{
            电子。打印堆栈跟踪（）；
        }最后{
            closeResourse( null , prepareStatement , resultSet);
        }
        返回 空；
    }

    //查询特殊值
    public  < E >  E  getValue ( Connection  connection , String  sql , Object ... args ) {
        的PreparedStatement preparedStatement时= 空;
        结果集的resultSet = 空;
        试试{
            PreparedStatement =连接。准备语句（sql）；
            对（INT我=  0 ;我< ARGS 。长度;我++）{
                准备好的语句。setObject(i +  1 , args[i]);
            }
            的resultSet = preparedStatement时。执行查询（）；
            如果（结果集。下一个（））{
                返回( E ) 结果集。获取对象（1）；
            }
        } catch ( SQLException throwables) {
            投掷物。打印堆栈跟踪（）；
        }最后{
            closeResourse( null , prepareStatement , resultSet);
        }
        返回 空；
    }
}
