
### rsyslog 简介

> Rsyslog是高速的日志收集处理服务，它具有高性能、安全可靠和模块化设计的特点，能够接收来自各种来源的日志输入（例如：file，tcp，udp，uxsock等），并通过处理后将结果输出的不同的目的地（例如：mysql，mongodb，elasticsearch，kafka等），每秒处理日志量能够超过百万条。  
> Rsyslog作为syslog的增强升级版本已经在各linux发行版默认安装了，无需额外安装

> rsyslog是syslog的升级版，是一种日志管理服务，应用，操作系统一般有以上三种方法来使用rsyslog服务。  

> rsyslog可以理解为增强版的syslog，在syslog的基础上扩展了很多其他功能，如数据库支持(Mysql,PostgreSQL、Oracle等)、日志内容筛选、定义日志格式模板等。除了默认的udp协议外，rsyslog还支持tcp协议来接收日志，可以yum安装，也可以源码安装。  

### rsyslog 版本
```shell
[root@10_0_4_2 systemd]# rsyslogd -version
rsyslogd 8.24.0-57.el7_9.1, compiled with:
	PLATFORM:				x86_64-redhat-linux-gnu
	PLATFORM (lsb_release -d):
	FEATURE_REGEXP:				Yes
	GSSAPI Kerberos 5 support:		Yes
	FEATURE_DEBUG (debug build, slow code):	No
	32bit Atomic operations supported:	Yes
	64bit Atomic operations supported:	Yes
	memory allocator:			system default
	Runtime Instrumentation (slow code):	No
	uuid support:				Yes
	Number of Bits in RainerScript integers: 64

See http://www.rsyslog.com for more information.
```
### rsyslog 优点

> 防止系统崩溃无法获取系统日志分享崩溃原因，用rsyslog可以把日志传输到远程的日志服务器上。  
> 使用rsyslog日志可以减轻系统压力，因为使用rsyslog可以有效减轻系统的磁盘IO。  
> rsyslog使用tcp传输非常可靠，可以对日志进行过滤，提取出有效的日志,rsyslog是轻量级的日志软件，在大量日志写的情况下，系统负载基本上在0.1以下。  

### rsyslog 配置

#### 执行文件:   /sbin/rsyslogd
主要是执行rsyslog的一些命令

#### 主配置文件: /etc/rsyslog.conf
```java
---------------------------- rsyslog 加载模块 ----------------------------
$ModLoad imuxsock # 支持系统日志采集
$ModLoad imklog # 支持内核日志采集
$ModLoad immark # provides --MARK-- message capability
        
#$ModLoad imudp # 传统方式的UDP传输，有损耗
#$UDPServerRun 514 # 允许通过514端口接收使用udp协议的远程日志

# 接收端需要加载这个模块，发送端不需要
#$ModLoad imtcp # 基于TCP明文的传输，只在特定情况下丢失信息，并被广泛使用
#$InputTCPServerRun 514 # 允许通过514端口接收使用tcp协议的远程日志

---------------------------- rsyslog 全局设置 ----------------------------
$ActionFileDefaultTemplate RSYSLOG_TraditionalFileFormat # 默认日志模版,可以子自定义模版
#$ActionFileEnableSync on # 文件同步功能，很少用，默认禁止
$IncludeConfig /etc/rsyslog.d/*.conf # 这里会自动加载自定义的*.conf配置文件，可以覆盖默认参数
$WorkDirectory /var/lib/rsyslog # 工作文件目录，主要记录当前日志采集标志（有点像断点续传）

---------------------------- 用来指定哪种类型、级别的log，发送给谁处理 ----------------------------
# 内核消息，默认不启用
# kern.* /dev/console

# 记录所有日志类型的，信息等级大于等于info级别的信息到messages文件（mail邮件信息，authpriv验证信息和corn时间和任务信息除外）
*.info;mail.none;authpriv.none;cron.none /var/log/messages

# authpriv验证相关的所有信息存放在/var/log/secure
authpriv.* /var/log/secure

# 邮件的所有信息存在/var/log/maillog；这里有一个“-”符号表示是使用异步的方式记录
mail.* -/var/log/maillog # '-' 表示异步

# 任务计划有关的信息存放在/var/log/cron
cron.* /var/log/cron

# 记录所有的≥emerg级别信息，发送给每个登录到系统的日志
*.emerg  :omusrmsg:*

# 记录uucp，news.crit等存放在/var/log/spooler
uucp,news.crit /var/log/spooler

# 本地服务器的启动的所有日志存放在/var/log/boot.log
local7.* /var/log/boot.log # local开头的是自定义的日志类型


---------------------------- begin forwarding rule ----------------------------
# 远程转发的配置，只要去除转发配置前面的注释就可使用。不用去除modules部分imtcp/imudp的注释，不必修改上面的任何配置。
#$ActionQueueFileName fwdRule1 # unique name prefix for spool files
#$ActionQueueMaxDiskSpace 1g # 1gb space limit (use as much as possible)
#$ActionQueueSaveOnShutdown on # save messages to disk on shutdown
#$ActionQueueType LinkedList # run asynchronously
#$ActionResumeRetryCount -1 # infinite retries if host is down

# 日志发送的配置，@表示传输协议（@表示udp，@@表示tcp），后面是ip和端口，格式可配置
# remote host is: name/ip:port, e.g. 192.168.0.1:514, port optional
#*.* @@remote-host:514

# 排除本地主机IP日志记录，只记录远程主机日志
#:fromhost-ip, !isequal, "127.0.0.1" ?RemoteLogs

# 开启采集系统相关日志到本机 TCP传输
*.*  @@10.0.4.2:514
```

####  自定义配置文件: /etc/rsyslog.d/*.conf
1、配置日志消息存储到kafka  
在目录创建配置文件 /etc/rsyslog.d/kafka.conf
```java
# 加载omkafka和imfile模块
module(load="omkafka")
module(load="imfile")

# 打印日志格式模版 回覆盖主配置rsyslog.conf文件中存在的配置
$ActionFileDefaultTemplate RSYSLOG_TraditionalFileFormat # 默认的日志格式
# $template SystemLogTemplate,"%timestamp% %fromhost-ip% %syslogtag% %msg%\n" # 自定义的日志格式
$template SystemLogTemplate,"%msg%\n" # 自定义的日志格式
$ActionFileDefaultTemplate SystemLogTemplate   # 使用自定义的日志格式
        
# 规则
ruleset(name="systemlog-kafka") {

# 摘取$msg(消息)变量内容，判断如果有keyboard关键字就进行远程传送，没有此关键字的就不传送；
# if $msg contains "keyboard"  then
    #日志转发kafka
    action (
        type="omkafka"
        template="SystemLogTemplate"
        confParam=["compression.codec=snappy", "queue.buffering.max.messages=400000"]
        topic="logcenter"
        broker="10.0.4.33:9092"
        queue.filename="system_log_kafka"
        queue.size="360000"
        queue.maxDiskSpace="2G"
        queue.highWatermark="216000"
        queue.type="LinkedList"
        queue.maxFileSize="10M"
        queue.spoolDirectory="/tmp"
        queue.saveOnShutdown="on"
        queue.workerThreads="4"
    )
}

# 输入类型 文件
# File 待监控的文件路径
# Tag 文件唯一标识tag，最好保持唯一，用于接收端区分原始log文件，可以包含特殊字符，如":"、","等
input(type="imfile" Tag="Systemlog" File="/data/rsyslog/*/*.log" Ruleset="systemlog-kafka"
```


### rsyslog 命令
> 重启服务： sudo /etc/init.d/rsyslog restart  
> 关闭服务： sudo /etc/init.d/rsyslog stop  
> 启动服务： sudo /etc/init.d/rsyslog start  
> 查看状态： sudo /etc/init.d/rsyslog status  


### 日志信息等级

| level        | 说明                                                                                                                             |
| ------------ | ---------------------------------------------------------------------------------------------------------------------------------- |
| none         | 不需登录等级                                                                                                                 |
| debug        | 调试信息                                                                                                                       |
| info         | 正常信息，仅是一些基本的信息说明而已                                                                             |
| notice       | 比info还需要被注意到的一些信息内容                                                                                  |
| warning,warn | 警告信息，可能有些问题，但是还不至于影响到某个服务运作的信息                                         |
| err,error    | 一些重大的错误信息                                                                                                        |
| crit         | 临界状态，比error还要严重的错误信息，橙色警报                                                                  |
| alert        | 红色警报，已经很有问题的等级，比crit还要严重                                                                   |
| emerg,panic  | 疼痛等级，意指系统已经要宕机的状态！很严重的错误信息了。通常大概只有硬件出问题，导致整个核心无法顺利工作，就会出现这样的等级信息。 |

### 设施类别

> facility：设施，从功能或程序上对日志进行分类，并由专门的工具记录其日志  

| facility | 说明                                        |
| -------- | --------------------------------------------- |
| auth     | 主要与认证相关的机制，例如login，ssh，su等 |
| authpriv | 主要于授权相关的机制                |
| cron     | 就是例行性工作排程cron/at等产生信息记录的地方 |
| daemon   | 各个daemon相关的的讯息                |
| kern     | 就是核心（kernel）产生的讯息的地方 |
| lpr      | 打印相关的信息                         |
| mail     | 只要与邮件收发有关的信息记录都属于这个 |
| mark     | 于防火墙相关的                         |
| news     | 于新闻组服务器有关的东西          |

### 通配机制

| 通配符 | 说明                                                                                                                                                |
| ------ | ----------------------------------------------------------------------------------------------------------------------------------------------------- |
| .      | 代表【比后面还要高的等级（含该等级）都被记录下来】的意思；例如：mail.info代表只要是mail信息，而且该信息等级高于info（含info）时，就会被记录下来的意思 |
| .=     | 代表所需要的等级就是后面接的等级而已，其他的不要。例如：main.=info代表的只要是mail信息，而且该信息等于info级别，就会被记录下来 |
| .!     | 代表不等于（取反），亦是除了该等级外的其他等级都记录                                                                        |
| *      | 所有；例如：*.info代表所有设施的info级别                                                                                               |
| ,      | 列表；例如：mail.notice,news.info代表mail的notice级别及news的info级别                                                                  |

> mail，news.info代表mail的info及news的info。

### 日志的输出位置

| 输出位置 | 说明                                                                       |
| ------------ | ---------------------------------------------------------------------------- |
| 文件       | 如/var/log/messages                                                         |
| 打印机或其他 | 如/dev/lp0这个打印机装置                                             |
| 使用者名称 | 显示给用户。例如：*代表目前在线的所有人                   |
| 远程主机 | 例如：@172.16.100.1(远程日志服务器。@代表通过UCP协议传，@@代表通过TCP协议传) |
| 管道       | |command                                                                     |

### 日志信息格式

> 时间  主机  进程（PID） 事件

### ryslog升级

RHEL/CENTOS系统默认安装的是V5版本的rsyslog。如需升级。请执行如下代码。
```java
wget -P /etc/yum.repos.d/ http://rpms.adiscon.com/v8-stable/rsyslog.repo
yum -y install rsyslog
或者 yum -y update rsyslog
```
