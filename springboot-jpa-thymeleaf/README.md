# 问题与思考
1. @Controller和@RestController的区别 :lollipop:
```
    @RestController相当于@ResponseBody and @Controller.
    当要返回前台页面时只能用@Controller
```
2. 为什么新增用户页面不是直接打开，而是通过controller返回
```   
   实测发现在templates下的页面不能直接访问，而在static下的页面时可以访问到
```


# Thymeleaf知识点分享
[五分钟学会使用Thymeleaf](http://www.thymeleaf.org/doc/articles/standarddialect5minutes.html "悬停显示") 
1. pom.xml中加入依赖
```
    <dependency>
     	<groupId>org.springframework.boot</groupId>
     	<artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
```
2. Html页面需添加th的命名空间`<html xmlns:th="http://www.thymeleaf.org">`
3. 常用的表达式
    ##### `@{...} : 链接表达式.`
    ##### `${...} : 变量表达式.`
    ##### `*{...} : 选择表达式.`
    
    