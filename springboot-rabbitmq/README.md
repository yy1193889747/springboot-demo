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

# ico
将ico命名为favicon.ico 放到resources下即可
1. `spring.mvc.favicon.enabled = false`
2. `<link rel="icon" type="image/x-icon" href="/static/favicon.ico">`

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
/var/lib/docker/ docker默认路径
1. docker网络
 * `apt-get install bridge-utils` 安装桥接查看工具
 * `brctl show` 查看桥接信息
 * `ifconfig docker0 192.168.200.1 netmask 255.255.255.0` 修改默认ip段
 * `service docker restart` 修改完重启配置
 * `brctl addbr br0` 添加虚拟网桥
 * `同上` 设置br0ip段
 * `vi /etc/default/docker `  DOCKER_OPTS= "b=br0"
 *  DOCKER_OPTS= " -icc=false -iptables=true" 允许link互联
 * `iptables -I DOCKER -s 172.17.0.5 -d 172.17.0.4 -p tcp --dport 80 -j DROP` 添加规则拒绝访问端口
 * `iptables -nL --line-numbers`查看行号
 * `iptables -D DOCKER 1` 删除iptables规则
2. 数据卷
 * `docker inspect 9d` 查看容器信息
 * `VOLUME ["/volume1","/volume2"]` Dockerfile指定数据卷
 * `docker run --volumes-from 55 -it alpine` 数据卷容器
 * `docker rm 55 -v` 删除容器及数据卷，只要被别的容器引用就删不了，即便容器停止
 * `docker run --volumes-from 55 -it -v /backup:/backup alpine tar cvf /backup/data.tar /volume1` 备份数据
 * `docker stata 9d` 监控容器状态
3. docker命令
 * `docker rm 55 -f` 删除正在运行的容器
 * `docker rm -f $(docker ps -a -q)` 删除正在运行的所有容器
 * `-v /etc/timezone:/etc/timezone` 直接挂在时区即可
 
 # linux 命令
 * `cat /proc/cpuinfo | grep "physical id" | uniq | wc -l` 查看CPU个数
 * `cat /proc/cpuinfo | grep "cpu cores" | uniq` 查看CPU核数
 * `cat /proc/cpuinfo | grep 'model name' |uniq` 查看CPU型号
 * `cat /proc/meminfo | grep MemTotal` 查看内存大小
 * `fdisk -l | grep Disk` 查看硬盘大小
 
 
 