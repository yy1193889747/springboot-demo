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
    
    ![](/src/main/resources/static/img/QQ图片20171214160014.png "邮件效果图")
2. maven引入依赖包，应为要用到html模板
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
3. 编写相关类