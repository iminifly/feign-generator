# Feign-Generator

## 介绍

项目fork自https://gitee.com/xuguofeng2020/feign-generator

最近发现开发spring cloud时，编写feign接口是一件痛苦的事，不仅要编写feign接口，还有fallback、请求参数和返回值等，大量重复工作，很浪费时间。

于是便想到可以编写工具自动生成feign接口。实现起来并不复杂，就是把提供方工程的类加载进来，扫描controller和model生成meta信息，使用模板生成源代码，保存到本地或集中管理feign接口源代码的服务。



## 下载编译

```shell
git clone https://github.com/iminifly/feign-generator.git

cd feign-generator

mvn install
```

