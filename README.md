# Feign-Generator

## 介绍
最近发现开发spring cloud时，编写feign接口是一件痛苦的事，不仅要编写feign接口，还有fallback、请求参数和返回值等，大量重复工作，很浪费时间。

于是便想到可以编写工具自动生成feign接口。

实现起来并不复杂，就是把提供方工程的类加载进来，扫描controller和model生成meta信息，使用模板生成源代码，保存到本地或集中管理feign接口源代码的服务。

本文就简单介绍一下这个自动化工具的使用和核心技术。



spring boot工程feign接口自动工具，含maven插件和cli工具。



## 下载编译

```shell
git clone https://gitee.com/xuguofeng2020/feign-generator.git

cd feign-generator

mvn install
```



## Maven插件版

您可以把插件引入到工程中，插件会在编译阶段扫描controller类，生成feign接口元数据并将其发送给中央管理server服务进行集中管理，或在本地保存feign源代码。



### 插件依赖

```xml
<plugin>
  <groupId>org.net5ijy.cloud</groupId>
  <artifactId>feign-generator-plugin</artifactId>
  <version>1.0.0</version>
  <executions>
    <execution>
      <phase>compile</phase>
      <goals>
        <goal>generate</goal>
      </goals>
      <configuration>
        <scanPackage>org.net5ijy.mall.account.controller</scanPackage>
        <modelScanPackage>org.net5ijy.mall.account</modelScanPackage>
        <manageServerUrl>http://localhost:10001/api/v1/feign/generate</manageServerUrl>
        <!-- <local>true</local> -->
      </configuration>
    </execution>
  </executions>
</plugin>
```





### 参数说明

| 参数             | 说明                                                         | 默认值            |
| ---------------- | ------------------------------------------------------------ | ----------------- |
| scanPackage      | 配置扫描controller的基础包名                                 | org.net5ijy.cloud |
| modelScanPackage | 配置扫描参数、返回值的基础包名。如果接口参数、返回值类型不在这个包下面，不会加入到model元数据中 | org.net5ijy.cloud |
| manageServerUrl  | 集中管理服务接收元数据的接口地址，如果local配置为true，该参数就不会起作用 | -                 |
| local            | 本地保存                                                     | false             |



### Maven编译工程

```shell
mvn clean compile
```



## Cli命令行版

### 命令示例

```shell
cd feign-generator-cli

copy scripts\run.bat target

cd target

## 本地保存feign接口源代码
run.bat D:\workspace\net5ijy-mall-accountservice\target\net5ijy-mall-accountservice.jar org.net5ijy.mall.account.controller org.net5ijy.mall.account

# 或者将feign接口元数据发送给集中管理服务
run.bat D:\workspace\net5ijy-mall-accountservice\target\net5ijy-mall-accountservice.jar org.net5ijy.mall.account.controller org.net5ijy.mall.account remote http://localhost:10001/api/v1/feign/generate
```



### 参数说明

| 参数 | 说明                                                         | 默认值                                       |
| ---- | ------------------------------------------------------------ | -------------------------------------------- |
| 1    | 工程jar路径                                                  | -                                            |
| 2    | 配置扫描controller的基础包名                                 | -                                            |
| 3    | 配置扫描参数、返回值的基础包名。如果接口参数、返回值类型不在这个包下面，不会加入到model元数据中 | -                                            |
| 4    | 如何保存feign接口元数据，local - 本地保存，remote - 发送给中央管理server服务 | local                                        |
| 5    | 集中管理服务接收元数据的接口地址                             | http://localhost:10001/api/v1/feign/generate |



## 集中管理服务

用于集中管理工程、feign接口源代码、model源代码，提供工程查看、feign接口源代码下载、feign接口及fallback类源代码在线查看、model源代码在线查看等功能。



### 示例服务

#### 数据库配置

编辑application.yml文件，修改datasource相关配置：

```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://mysql-dba-1:3306/feign
    username: system
    password: ******
    druid:
      filters: stat
      maxActive: 5
      initialSize: 5
      maxWait: 60000
      minIdle: 1
      timeBetweenEvictionRunsMillis: 60000
      minEvictableIdleTimeMillis: 300000
      validationQuery: SELECT 1 FROM DUAL
      testWhileIdle: true
      testOnBorrow: false
      testOnReturn: false
      poolPreparedStatements: true
      maxOpenPreparedStatements: 5
```



#### 打包及运行

```
cd feign-generator-server-demo

mvn clean package

cd target

java -jar feign-generator-server-demo.jar
```



#### 工程管理页面

![](image/101.jpg)



![](image/102.jpg)



#### FeignClient管理页面

![](image/103.jpg)



![](image/104.jpg)



#### Model管理页面

![](image/105.jpg)



![](image/106.jpg)



### 集成

#### 引入依赖

引入feign-generator-server依赖：

```xml
<!-- feign-generator-server -->
<dependency>
  <groupId>org.net5ijy.cloud</groupId>
  <artifactId>feign-generator-server</artifactId>
  <version>1.0.0</version>
</dependency>
```



在启动类配置组件扫描：

```java
@SpringBootApplication(scanBasePackages = {"org.net5ijy.cloud.feign.demo", "org.net5ijy.cloud.plugin.feign.server"})
@MapperScan({"org.net5ijy.cloud.plugin.feign.server.mapper"})
```



#### 接口列表

feign-generator-server会提供查看工程、feign接口、model类的相关接口，如下：

| 接口                        | 请求方式 | 作用                                 | 参数                             |
| --------------------------- | -------- | ------------------------------------ | -------------------------------- |
| /api/v1/feign/{id}          | GET      | 根据ID查询工程信息                   | -                                |
| /api/v1/feign/search        | GET      | 根据groupId、projectName分页查询工程 | groupId、projectName、page、size |
| /api/v1/feign/clients       | GET      | 根据工程ID分页查询feign client       | projectId、page、size            |
| /api/v1/feign/client/{id}   | GET      | 根据feign client id查询client        | -                                |
| /api/v1/feign/models        | GET      | 根据工程ID分页查询model              | projectId、page、size            |
| /api/v1/feign/model/{id}    | GET      | 根据ID查询model                      | -                                |
| /api/v1/feign/download/{id} | POST     | 下载工程的feign client源代码zip文件  | -                                |
| /api/v1/feign/project/all   | GET      | 获取全部工程，用于下拉列表等功能     | -                                |



您可以在已有的管理平台开发前端，使用以上接口获取后台数据进行展示。



## 核心组件

- feign-generator-core - core组件提供封装工程、feign client、model元数据的类，free marker模板解析器，controller扫描器，以及用于生成源代码的free marker模板。
- feign-generator-plugin - Maven插件。
- feign-generator-server - 为集中管理服务提供接口能力。
- feign-generator-cli - 命令行工具，提供的能力和maven插件基本相同。
- feign-generator-server-demo - 集中管理服务示例。



## 核心技术

- maven插件开发
- 动态类加载
- java反射技术
- free marker模板技术







