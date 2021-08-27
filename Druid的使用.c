# DAO_JDBC

maven管理  添加druid的依赖、数据库驱动   
       <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.8</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.19</version>
        </dependency>


纯代码方式

        //数据源配置
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://127.0.0.1/db_student?serverTimezone=UTC");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");    //这个可以缺省的，会根据url自动识别
        dataSource.setUsername("root");
        dataSource.setPassword("abcd");
        
        //下面都是可选的配置
        dataSource.setInitialSize(10);  //初始连接数，默认0
        dataSource.setMaxActive(30);  //最大连接数，默认8
        dataSource.setMinIdle(10);  //最小闲置数
        dataSource.setMaxWait(2000);  //获取连接的最大等待时间，单位毫秒
        dataSource.setPoolPreparedStatements(true); //缓存PreparedStatement，默认false
        dataSource.setMaxOpenPreparedStatements(20); //缓存PreparedStatement的最大数量，默认-1（不缓存）。大于0时会自动开启缓存PreparedStatement，所以可以省略上一句代码
        Connection connection = dataSource.getConnection();//获取连接   
        connection.close();//关闭连接





配置文件方式

druid.properties
url=jdbc:mysql://127.0.0.1/db_student?serverTimezone=UTC
#这个可以缺省的，会根据url自动识别
driverClassName=com.mysql.cj.jdbc.Driver
username=root
password=abcd

##初始连接数，默认0
initialSize=10
#最大连接数，默认8
maxActive=30
#最小闲置数
minIdle=10
#获取连接的最大等待时间，单位毫秒
maxWait=2000
#缓存PreparedStatement，默认false
poolPreparedStatements=true
#缓存PreparedStatement的最大数量，默认-1（不缓存）。大于0时会自动开启缓存PreparedStatement，所以可以省略上一句设置
maxOpenPreparedStatements=20

Java
public class Test {

     public static void main(String[] args) throws Exception {
        //数据源配置
        Properties properties=new Properties();
        //通过当前类的class对象获取资源文件
        InputStream is = Test.class.getResourceAsStream("/druid.properties"); 
        properties.load(is);
        //返回的是DataSource，不是DruidDataSource
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        //获取连接
        Connection connection = dataSource.getConnection();
        //关闭连接
        connection.close();
    }
    
}





在Spring中使用Druid

druid.properties
druid.url=jdbc:mysql://127.0.0.1/db_student?serverTimezone=UTC
#这个可以缺省的，会根据url自动识别
druid.driverClassName=com.mysql.cj.jdbc.Driver
druid.username=root
druid.password=abcd

##初始连接数，默认0
druid.initialSize=10
#最大连接数，默认8
druid.maxActive=30
#最小闲置数
druid.minIdle=10
#获取连接的最大等待时间，单位毫秒
druid.maxWait=2000
#缓存PreparedStatement，默认false
druid.poolPreparedStatements=true
#缓存PreparedStatement的最大数量，默认-1（不缓存）。大于0时会自动开启缓存PreparedStatement，所以可以省略上一句设置
druid.maxOpenPreparedStatements=20


spring配置文件
<!--引入druid配置文件-->
    <context:property-placeholder location="classpath:druid.properties" />

    <!--druid连接池-->
    <bean name="druidDataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="url" value="${druid.url}" />
        <property name="driverClassName" value="${druid.driverClassName}" />
        <property name="username" value="${druid.username}" />
        <property name="password" value="${druid.password}" />
        <property name="initialSize" value="${druid.initialSize}"/>
        <property name="maxActive" value="${druid.maxActive}" />
        <property name="minIdle" value="${druid.minIdle}" />
        <property name="maxWait" value="${druid.maxWait}" />
        <property name="poolPreparedStatements" value="${druid.poolPreparedStatements}" />
        <property name="maxOpenPreparedStatements" value="${druid.maxOpenPreparedStatements}" />
    </bean>



使用
@Repository
public class Xxx {
    @Resource
    private DruidDataSource dataSource;

    public void xxx() throws SQLException {
        //获取连接
        Connection connection = dataSource.getConnection();

        //关闭连接
        connection.close();
    }
}
