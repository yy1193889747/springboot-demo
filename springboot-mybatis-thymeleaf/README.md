# 索引
* [springboot整合mybatis](#springboot整合mybatis)
* [springboot整合druid加监控页面](#springboot整合druid加监控页面)
* [springboot整合druid多数据源](#springboot整合druid多数据源)
* [定制启动页面banner](#定制启动页面banner)
* [开启HTTPS协议](#开启HTTPS协议)
* [log4j2配置文件介绍](#log4j2配置文件介绍)
* [spring-security简单的使用](#spring-security简单的使用)
* [Scheduled定时任务](#scheduled定时任务)
* [Async异步执行](#async异步执行)
* [Swagger接口文档生成](#swagger接口文档生成)

# springboot整合mybatis
1. 首先pom文件引入包
    ````
    <!--mybatis-->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>1.3.0</version>
    </dependency>
    ````
2. 启动类加入注解`@MapperScan("com.cy.dao")`
3. 配置文件加入mybatis的相关配置
    ```
    #mybatis 配置
    mybatis.type-aliases-package=com.cy.dao
    mybatis.mapper-locations=classpath:mybatis/mapper/*.xml
    ```
4. 利用mybatis generator工具生成xml
5. 注意修改namespace，等路径

# springboot整合druid加监控页面
[Alibaba官方整合文档](https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter "官方文档") 
1. maven导入springboot的依赖
    ```
            <!-- Druid 数据连接池依赖 -->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid-spring-boot-starter</artifactId>
                <version>1.1.5</version>
            </dependency>
    ```
2. 配置Druid
    ```
    # druid配置
    # 初始化大小，最小，最大链接数
    spring.datasource.druid.initial-size=3
    spring.datasource.druid.min-idle=3
    spring.datasource.druid.max-active=10
    # 连接超时时间
    spring.datasource.druid.max-wait=60000
    # 后台登录用户名密码
    spring.datasource.druid.stat-view-servlet.login-username=admin
    spring.datasource.druid.stat-view-servlet.login-password=admin
    # 配置StatFilter
    spring.datasource.druid.filter.stat.log-slow-sql=true
    spring.datasource.druid.filter.stat.slow-sql-millis=2000
    ```
3. 启动类加入`@ServletComponentScan`注解，以便访问监控页面
4. 新建两个类，DruidStatViewServlet，DruidStatFilter

# springboot整合druid多数据源
1. maven导入springboot的依赖
    ```
            <!-- Druid 数据连接池依赖 -->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid-spring-boot-starter</artifactId>
                <version>1.1.5</version>
            </dependency>
    ```
2. 配置多数据源Druid
    ```
    #数据库连接1
    spring.datasource.druid.one.url=jdbc:mysql://127.0.0.1/springbootdb?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&useSSL=true
    spring.datasource.druid.one.username=root
    spring.datasource.druid.one.password=0.0001
    spring.datasource.druid.one.driver-class-name=com.mysql.jdbc.Driver
    #数据库连接2
    spring.datasource.druid.tow.url=jdbc:mysql://127.0.0.1/springbootdb2?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&useSSL=true
    spring.datasource.druid.tow.username=root
    spring.datasource.druid.tow.password=0.0001
    spring.datasource.druid.tow.driver-class-name=com.mysql.jdbc.Driver
    # druid配置
    # 初始化大小，最小，最大链接数
    spring.datasource.druid.initial-size=3
    spring.datasource.druid.min-idle=3
    spring.datasource.druid.max-active=10
    # 连接超时时间
    spring.datasource.druid.max-wait=60000
    # 后台登录用户名密码
    spring.datasource.druid.stat-view-servlet.login-username=admin
    spring.datasource.druid.stat-view-servlet.login-password=admin
    # 配置StatFilter
    spring.datasource.druid.filter.stat.log-slow-sql=true
    spring.datasource.druid.filter.stat.slow-sql-millis=2000
    # Druid 数据源 1 配置，继承spring.datasource.druid.* 配置，相同则覆盖
    spring.datasource.druid.one.max-active=20
    spring.datasource.druid.one.max-wait=10000
    # Druid 数据源 2 配置，继承spring.datasource.druid.* 配置，相同则覆盖
    spring.datasource.druid.two.max-active=30
    spring.datasource.druid.two.max-wait=20000
    ```
3. 添加各自配置类对DataSource、DataSourceTransactionManager、SqlSessionFactory 、SqlSessionTemplate四个数据项进行配置
4. 对主数据源使用`@Primary`修饰，必须有且只有一个

# 定制启动页面banner
1. [打开页面](http://patorjk.com/software/taag/)，制作文字图片
2. 在resource目录下添加banner.txt文件
3. 复制制作好的图片到banner文件里面
4. 可以根据喜好，为自己的图案添加控制台输出颜色 [详细配置](/springboot-mybatis-thymeleaf/src/main/resources/banner.txt)

# yml文件的使用注意
1. yml格式比较严格，当你启动就报错，就要细心检查yml格式是否有误

# 开启HTTPS协议
1. 需要一个ssl证书，我们使用keytool生产(记住密码)
```
    keytool -keystore ocly.jks -genkey -alias tomcat -keyalg RSA
```
2. 将ocly.jks文件添加到resource目录下
3. 添加配置文件
    ```
    server:
    port: 8443        # https 端口号，正式 443；测试 8443
    sslPort: 8080     # http 端口号，正式 80；测试 8080
    ssl:
    key-store: classpath:ocly.jks
    key-password: 704739362
    ```
4. 添加http端口的监听sslconfig
5. 实现端口转发http到https （使用的undertow目前没解决）

# log4j2配置文件介绍
[官方文档](http://logging.apache.org/log4j/2.x/manual/configuration.html#YAML)
1. 通用的6种日志记录级别（低到高）
    * trace：追踪，就是程序推进一下，可以写个trace输出
    * **debug**：调试，一般作为最低级别，trace基本不用。
    * **info**：输出重要的信息，使用较多
    * **warn**：警告，有些信息不是错误信息，但也要给程序员一些提示。
    * **error**：错误信息。用的也很多。
    * fatal：致命错误。
2. 输出源：CONSOLE（输出到控制台）、FILE（输出到文件）、RollingFile（循环输出到文件）
3. 布局方式
    * SimpleLayout：以简单的形式显示
    * HTMLLayout：以HTML表格显示
    * PatternLayout：自定义形式显示（首选）

    | 自定义格式|说明          | 
    | ------------- |:-------------:| 
    | %t     | 线程名称 | 
    | %p     | 日志级别      |
    | %c     | 日志消息所在类名      |
    | %m     | 消息内容         |
    | %M     | 输出执行方法    |
    | %d     | 日志生产时间     |
    | %x     | 输出线程上下文堆栈      |
    | %L     | 代码中的行数      |
    | %n     | 换行     |

4. 编写配置文件yml版（注意pom文件引入包）[详细配置](/springboot-mybatis-thymeleaf/src/main/resources/log4j2.yml)
5. 为控制台输出添加代码高亮
6. log记录统一使用占位符形式

# spring-security简单的使用
  实现内存中验证,只有验证通过才能访问/list页面
1. 首先pom文件引入包
    ````
    <!--security-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    ````
2. 添加类SecurityConfig
3. 使用内存中的身份验证
4. 使用数据库验证：
  * 用户类实现UserDetails，并重写方法（没对角色处理）
  * 新建LoginService继承UserDetailsService类，重写方法
  * 对类[SecurityConfig](/springboot-mybatis-thymeleaf/src/main/java/com/cy/config/SecurityConfig.java)进行修改
5. 注意：之前配置的Druid监控页面类会失效
# Scheduled定时任务
1. 启动类加入@EnableScheduling注解
2. 编写定时任务，必须是无返回方法
3. 定时任务参数设置
  * fixedRate = 5 * 1000 上一次开始执行时间点之后5秒再执行
  * fixedDelay = 5 * 1000 上一次执行完毕时间点之后5秒再执行
  * initialDelay = 1000, fixedRate = 5000 第一次延迟1秒后执行，之后按fixedRate的规则每5秒执行一次
  * cron="*/5 * * * * *" 每5秒执行一次 [在线cron表达式生成](http://cron.qqe2.com/)

# Async异步执行
1. 启动类加入@EnableAsync注解，开启异步功能
2. 类方法加入@Async注解：注：（不能定义为static类型
3. 如果需要判断异步是否执行完成，需要使用Future<T>来返回异步调用的结果

# Swagger接口文档生成
1. pom文件引入包（ui版本尽量和swagger版本相同）
   ````
    <dependency>
          <groupId>io.springfox</groupId>
          <artifactId>springfox-swagger2</artifactId>
          <version>2.6.1</version>
      </dependency>
      <dependency>
          <groupId>io.springfox</groupId>
          <artifactId>springfox-swagger-ui</artifactId>
          <version>2.6.1</version>
      </dependency>
   ````
2. 添加[Swagger](/springboot-mybatis-thymeleaf/src/main/java/com/cy/Swagger.java)配置类
3. 给Api接口添加注解 注：（/user/{id} ,参数设置加入paramType = "path"