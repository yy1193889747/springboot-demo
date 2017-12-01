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