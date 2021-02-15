# VueBlog 练习项目 后端

` + Vue开发的前后端分离博客项目(https://github.com/MarkerHub/vueblog) 
`

### 技术栈依赖：
* spring framework
  * spring-boot 
  `2.4.2`
  * spring-boot-starter-web 
  `Spring Web MVC`
  * spring-boot-starter-validation 
  `从Springboot 2.3开始需要显示添加验证依赖`
  * spring-boot-devtools

* 数据数据源
    * mysql-connector-java

* [mybatis-plus](https://baomidou.com/guide)
`根据Entity自动生成访问数据库的Service`
  * mybatis-plus-boot-starter
  * [mybatis-plus-generator](https://baomidou.com/guide/generator.html)
  `代码生成器 配合下面的模板引擎`
  * spring-boot-starter-freemarker

* Shiro验证框架
  * [shiro-redis-spring-boot-starter](https://alexxiyang.github.io/shiro-redis)
  `Shiro和Redis的整合版，方便Shiro设置Redis作为缓存`
  * [java-jwt](https://github.com/auth0/java-jwt)
  `生成JSON Web Token`
  
* 其它
  * [lombok](https://projectlombok.org/setup/maven)
  `注释简化POJO工具包`
  *[Hutool](https://github.com/looly/hutool)
  `Java工具类库`