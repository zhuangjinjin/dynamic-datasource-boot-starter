# dynamic-datasource integration with spring-boot
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

dynamic-datasource-spring-boot-starter 是一个动态数据源切换的实现(可用于切换主从数据源)，目前支持`Mybtatis`和`spring-data-jpa`。

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
   <version>1.0.0</version>
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

在`META-INF/services`目录下创建一个`io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.strategy.RoutingStrategy`文件，内容为`io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.strategy.DbOperationRoutingStrategy`。

### Springboot 外部化配置

在application.yml中设置相关信息

```properties
dynamic:
  datasource:
    enable: true
    properties:
    - name: master
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost/test
      username: root
      password: 123456
      crud-types: #该数据源的读写类型
      - WRITE
      - READ
    - name: slave
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost/test_2
      username: root
      password: 123456
      crud-types:
      - READ
```



## 扩展

### 数据源切换策略扩展(TODO)

如果不想采用读写切换数据源策略，可以自定义。需要如下步骤

* 自定义一个类实现`io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.strategy.RoutingStrategy`接口。
* 在`META-INF/services`目录下创建一个`io.github.ukuz.dynamic.datasource.spring.boot.autoconfigure.strategy.RoutingStrategy`文件，内容格式`${value}`，其中`${value}`为实现类的全路径。





