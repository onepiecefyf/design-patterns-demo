# 记录安装以及使用Jenkins过程中遇到的问题

## Jenkins持续集成以及介绍
**Jenkins由来**
>  Jenkins的前身是Hudson (软件)项目。Hudson 2004年夏天始创于Sun Microsystems，2005年2月首次发布于java.net。  
> 在2010年11月，关于由谁主导来Hudson[8]，该项目的主要贡献者和Oracle之间展开谈判。尽管在多个方面达成一致，争议集中在是否把Hudson注册为商标[9]，后来Oracle声明对Hudson的名字拥有权利，并在2010年12月申请将其注册为商标。[10]因此，2011年1月11日，社群号召投票将项目名称从“Hudson”变更为“Jenkins”。[11] 2011年1月29日，社群投票以压倒多数批准通过该提案，创建Jenkins项目。

**Jenkins持续集成**
> 持续集成指的是，频繁的将代码集成到主干。  
> 持续集成的目的，就是让产品可以快速的迭代，同时还能保持保质量。  
> 核心措施就是，代码集成到主干之前，必须通过自动化测试，只要存在一个测试用例失败，就不能集成。

**Jenkins介绍**
> Jenkins是一款由Java编写的开源的持续集成工具，广泛用于项目开发，具有自动化构建、测试和部署等功能。

推荐一个Jenkins下载地址[清华大学开源软件镜像站](https://mirrors.tuna.tsinghua.edu.cn/jenkins/redhat-stable/)

## Jenkins安装过程中遇到的问题

### 1、Failed to start LSB: Jenkins Automation Server
这个问题的原因是因为JDK环境问题，我的系统中JDK是用手动下载安装的，需要找到正确的JDK安装路径，执行如下命令查找JDK安装路径
```java
[root@bogon bin]# find / -name java
/usr/bin/java
/usr/local/jdk1.8.0_191/bin/java
/usr/local/jdk1.8.0_191/jre/bin/java
```

我配置的JDK路径是
```java
/usr/local/jdk1.8.0_191/bin/java
```
编辑Jenkins配置
```java
vim /etc/init.d/jenkins
```
添加JDK环境变量到candidates里面
```java
candidates="
/etc/alternatives/java
/usr/lib/jvm/java-1.8.0/bin/java
/usr/lib/jvm/jre-1.8.0/bin/java
/usr/lib/jvm/java-1.7.0/bin/java
/usr/lib/jvm/jre-1.7.0/bin/java
/usr/lib/jvm/java-11.0/bin/java
/usr/lib/jvm/jre-11.0/bin/java
/usr/lib/jvm/java-11-openjdk-amd64
/usr/bin/java
/usr/local/jdk1.8.0_191/bin/java
"
```

使配置生效
```java
# centos7及以上
systemctl daemon-reload 
# centos7以下
sudo chkconfig daemon-reload
```










