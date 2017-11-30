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