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
3. @NotEmpty、@NotBlank、@NotNull的区别 :lollipop:
```   
   @NotEmpty 用在集合类上面 
   @NotBlank 用在String上面 
   @NotNull 用在基本类型上
```
4. 
# Thymeleaf知识点分享
[官方文档----五分钟学会使用Thymeleaf](http://www.thymeleaf.org/doc/articles/standarddialect5minutes.html "官方文档") 
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
# jpa知识点分享
1. hibernate.hbm2ddl.aut参数的作用:lollipop:
````
    validate        加载hibernate时，验证创建数据库表结构
    create          每次加载hibernate，重新创建数据库表结构，这就是导致数据库表数据丢失的原因。
    create-drop     加载hibernate时创建，退出是删除表结构
    update          加载hibernate自动更新数据库结构（一般使用）
````   
    