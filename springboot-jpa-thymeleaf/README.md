#Thymeleaf知识点分享
http://www.thymeleaf.org/doc/articles/standarddialect5minutes.html
1. pom.xml中加入依赖

    `<dependency>
     		<groupId>org.springframework.boot</groupId>
     		<artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>`
2. Html页面需添加th的命名空间`<html xmlns:th="http://www.thymeleaf.org">`
3. 常用的表达式
    #####`@{...} : 链接表达式.`
    #####`${...} : 变量表达式.`
    #####`*{...} : 选择表达式.`