# dynamic-datasource integration with spring-boot
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/9d1b582e9e26439d9b24b7cd620422b2)](https://app.codacy.com/app/zhuangjinjin/dynamic-datasource-boot-starter?utm_source=github.com&utm_medium=referral&utm_content=zhuangjinjin/dynamic-datasource-boot-starter&utm_campaign=Badge_Grade_Dashboard)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Build Status](https://travis-ci.org/zhuangjinjin/dynamic-datasource-boot-starter.svg?branch=)](https://travis-ci.org/zhuangjinjin/dynamic-datasource-boot-starter)
[![codecov](https://codecov.io/gh/zhuangjinjin/dynamic-datasource-boot-starter/branch/dev/graph/badge.svg)](https://codecov.io/gh/zhuangjinjin/dynamic-datasource-boot-starter)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/dd13a7424bff471680ab06a60a1f93c4)](https://www.codacy.com/app/zhuangjinjin/dynamic-datasource-boot-starter?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=zhuangjinjin/dynamic-datasource-boot-starter&amp;utm_campaign=Badge_Grade)

dynamic-datasource-spring-boot-starter 是一个动态数据源切换的实现(可用于切换主从数据源)，目前支持`Mybtatis`和`spring-data-jpa`等ORM框架，并且支持多数据源自动创建Schema。

## 使用

### Maven

在pom.xml中加入nexus资源库

```xml
<repositories>
    <repository>
        <id>nexus</id>
        <name>nexus</name>
        <url>http://maven.zhuangjinjin.cn/repository/public</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
</repositories>
```

在pom.xml中加入依赖

```xml
<dependency>
   <groupId>io.github.ukuz</groupId>
   <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
   <version>1.2.1</version>
</dependency>
```

### Gradle

在build.gradle中加入nexus资源库

```groovy
repositories {
    mavenLocal()
    maven {url 'http://maven.zhuangjinjin.cn/repository/public'}
    mavenCentral()
}
```

在build.gradle加入依赖

```groovy
dependencies {
    ...
    compile 'io.github.ukuz:dynamic-datasource-spring-boot-starter:1+'
}
```

### Springboot 注解

在Application类上添加`@EnableDynamicRoutingDataSource`注解

```java
@SpringBootApplication
@EnableDynamicRoutingDataSource
public class FooApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(FooApplication.class).run(args);
    }

}
```

### Springboot 外部化配置

在`application.yml`中设置相关信息

```yaml
dynamic:
  datasource:
    enable: true
    routing-strategy: dboperation
    loadbalance: random
    properties:
    - name: master
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost/test
      username: root
      password: 123456
      weight: 5 #负载均衡的权重值
      crud-types: #该数据源的读写类型
      - WRITE
      - READ
    - name: slave
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost/test_2
      username: root
      password: 123456
      weight: 5
      crud-types:
      - READ
```

或者，在`application.properties`设置相关信息

```properties
dynamic.datasource.enable=true
dynamic.datasource.routing-strategy=dboperation
dynamic.datasource.loadbalance=random

dynamic.datasource.properties[0].name=master
dynamic.datasource.properties[0].driver-class-name=com.mysql.cj.jdbc.Driver
dynamic.datasource.properties[0].url=jdbc:mysql://localhost/test
dynamic.datasource.properties[0].username=root
dynamic.datasource.properties[0].password=123456
dynamic.datasource.properties[0].weight=5
dynamic.datasource.properties[0].crud-types=WRITE,READ

dynamic.datasource.properties[1].name=slave
dynamic.datasource.properties[1].driver-class-name=com.mysql.cj.jdbc.Driver
dynamic.datasource.properties[1].url=jdbc:mysql://localhost/test_2
dynamic.datasource.properties[1].username=root
dynamic.datasource.properties[1].password=123456
dynamic.datasource.properties[1].weight=5
dynamic.datasource.properties[1].crud-types=READ
```



## 扩展

### 数据源切换策略扩展

如果不想采用读写切换数据源策略（默认），可以自定义。需要如下步骤

* 自定义一个类实现`io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.strategy.RoutingStrategy`接口。
* 在`META-INF/ukuz`目录下创建一个`io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.strategy.RoutingStrategy`文件，内容格式`${key}=${value}`，其中`${value}`为实现类的全路径。
* 并且在`application.yml`中加入`dynamic.datasource.routing-strategy=${key}`，其中`${key}`是上一步中自定义的`${key}`

### 负载均衡算法扩展

如果不想采用随机负载均衡（默认），可以自定义，需要如下步骤

- 自定义一个类实现`io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.loadbalance.LoadBalance`接口。
- 在`META-INF/ukuz`目录下创建一个`io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.loadbalance.LoadBalance`文件，内容格式`${key}=${value}`，其中`${value}`为实现类的全路径。
- 并且在`application.yml`中加入`dynamic.datasource.loadbalance=${key}`，其中`${key}`是上一步中自定义的`${key}`

