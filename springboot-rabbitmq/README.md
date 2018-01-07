# springboot整合RabbitMq
1. 下载并安装RabbitMq（[下载地址](https://www.rabbitmq.com/download.html)）
2. 下载并安装Erland（[下载地址](https://www.rabbitmq.com/download.html)）
3. 命令行进入RabbitMq目录的sbin目录
4. 执行`rabbitmq-plugins enable rabbitmq_management`开启web管理插件
5. 打开地址 http://localhost:15672/ guest guest
6. 添加新用户 并 Set permission
7. maven引入依赖包
    ```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-amqp</artifactId>
    </dependency>
    ```
8. 请求响应模式, **注:(** 一对多是平均消费
9. 发送对象时, **注:(** 对象实现序列化
10. 主题模式
11. 发布订阅模式（广播模式）

# javaMail的使用
1. 先来展示下收到邮件的效果图：
    
    ![](/springboot-rabbitmq/src/main/resources/static/img/QQ图片20171214160014.png "邮件效果图")
2. 准备工作
    * 邮箱服务器地址：这里使用新浪邮箱，登录你的新浪邮箱，开启SMTP
    * 目标邮箱地址：可以是自己的qq邮箱，能接到邮件就行
3. maven引入依赖包，thymeleaf有提供htmlmail模板
   ```
      <!--mail-->
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-mail</artifactId>
      </dependency>

      <!--thymeleaf-->
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-thymeleaf</artifactId>
      </dependency>
   ```
4. 配置相关信息
    ```
   spring:  
     thymeleaf:
       cache: false
       mode: LEGACYHTML5
     mail:
         # 邮箱设置里可以找到
         host: smtp.sina.com
         # 去页面开启SMTP
         username:
         password:
         default-encoding: UTF-8
    ```
5. 编写相关[代码](https://github.com/yy1193889747/springboot-demo/blob/master/springboot-rabbitmq/src/main/java/com/ocly/util/SendMail.java)

# mvn命令
`mvn install:install-file -Dfile=D:/jar/xxx.jar -DgroupId=org.csource -DartifactId=fastdfs-client-java -Dversion=1.25 -Dpackaging=jar`

# Docker study
1. docker网络
 * `apt-get install bridge-utils` 安装桥接查看工具