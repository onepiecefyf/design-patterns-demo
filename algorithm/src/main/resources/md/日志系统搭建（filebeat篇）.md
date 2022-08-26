## Filebeat 输出kafka遇到的问题

### 1、filebeat与kafka版本问题
错误信息：
```java
2022-03-20T14:55:02.198+0800    INFO    kafka/log.go:53 producer/broker/544 starting up
2022-03-20T14:55:02.198+0800    INFO    kafka/log.go:53 producer/broker/544 state change to [open] on wp-news-filebeat/4
2022-03-20T14:55:02.198+0800    INFO    kafka/log.go:53 producer/leader/wp-news-filebeat/4 selected broker 544
2022-03-20T14:55:02.198+0800    INFO    kafka/log.go:53 producer/broker/478 state change to [closing] because EOF
2022-03-20T14:55:02.199+0800    INFO    kafka/log.go:53 Closed connection to broker bitar1d12:9092
2022-03-20T14:55:02.199+0800    INFO    kafka/log.go:53 producer/leader/wp-news-filebeat/5 state change to [retrying-3]
2022-03-20T14:55:02.199+0800    INFO    kafka/log.go:53 producer/leader/wp-news-filebeat/4 state change to [flushing-3]
2022-03-20T14:55:02.199+0800    INFO    kafka/log.go:53 producer/leader/wp-news-filebeat/5 abandoning broker 478
2022-03-20T14:55:02.199+0800    INFO    kafka/log.go:53 producer/leader/wp-news-filebeat/2 state change to [retrying-2]
2022-03-20T14:55:02.199+0800    INFO    kafka/log.go:53 producer/leader/wp-news-filebeat/2 abandoning broker 541
2022-03-20T14:55:02.199+0800    INFO    kafka/log.go:53 producer/leader/wp-news-filebeat/3 state change to [retrying-2]
2022-03-20T14:55:02.199+0800    INFO    kafka/log.go:53 producer/broker/478 shut down
```
看日志错误信息，kafka一直在创建和关闭连接。如果使用filebeat遇到这种问题，很大情况就是kafka和filebeat版本不兼容导致的。

### 2、自定义采集日志字段问题
安装配置好filebeat之后，到kibana查看日志会发现，存在很多多余的字段，如下所示：
```java
{
    "@timestamp": "2020-12-01T07:49:19.035Z",
    "@metadata": {
        "beat": "filebeat",
        "type": "_doc",
        "version": "7.10.0"
    },
    "fields": {
        "kafka_topic": "first"
    },
    "log": {
        "file": {
            "path": "xxx.log"
        },
        "offset": 299
    },
    "message": "60.247.93.190 - - [2022-03-30T14:22:10+08:00] 124.223.219.45:10005 GET "/compApia/api/v1/customer/page" "customerName=&current=1&size=10&time=1648621329842" 200 318 "http://124.223.219.45:10005/msp/" 200 10.0.4.2:11000 0.008 0.008 "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.83 Safari/537.36" "-"",
    "input": {
        "type": "log"
    }
}



```
调整filebeat.yml配置文件，在output配置项中添加如下配置：
```java
output.kafka:
  codec.format:
    string: '%{[@timestamp]} %{[message]}'
```
表示我的日志输出是kafka，输出的日志格式之后时间和原日志信息(message字段表示的信息)。

### 3、采集日志多行合并问题

假如日志格式如下：
```java
2022-03-30 15:10:10,030  INFO 31609 --- [pool-3-thread-1] [] c.o.b.m.p.b.a.s.d.t.DashBoardMonitorTask : 监控大屏定时任务配置：{"createTime":{"hour":14,"minute":6,"second":58,"nano":0,"dayOfYear":75,"dayOfWeek":"WEDNESDAY","month":"MARCH","dayOfMonth":16,"year":2022,"monthValue":3,"chronology":{"calendarType":"iso8601","id":"ISO"}},"updateTime":{"hour":14,"minute":6,"second":58,"nano":0,"dayOfYear":75,"dayOfWeek":"WEDNESDAY","month":"MARCH","dayOfMonth":16,"year":2022,"monthValue":3,"chronology":{"calendarType":"iso8601","id":"ISO"}},"createBy":"1","updateBy":"1","id":"600","configGroup":"DASHBOARD_MONITOR","configKey":"dashboard_monitor_cron","configValue":"10 */1 * * * ?","description":"监控大屏数据统计定时任务"}
...skipping...
        at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)
        at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)
        at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:367)
        at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)
        at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:868)
        at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1639)
        at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
        at java.lang.Thread.run(Thread.java:748)
Caused by: com.fasterxml.jackson.core.JsonParseException: Unexpected character ('<' (code 60)): expected a valid value (JSON String, Number, Array, Object or token 'null', 'true' or 'false')
 at [Source: (String)"<html>
<head><title>403 Forbidden</title></head>
<body bgcolor="white">
<center><h1>403 Forbidden</h1></center>
<hr><center>openresty</center>
</body>
</html>
"; line: 1, column: 2]
```

这种日志被采集的时候如果不配置多行合并采集，就会一行一行的去采集，导致后续很难分析日志。解决办法就是使用filebeat自带配置解决，如下：
```java
# 匹配采集日志的开始行 正则表达式
multiline.pattern: '^[0-9]{4}-[0-9]{2}-[0-9]{2}'
# 默认为false，暗示匹配pattern的行归并到上一行；true暗示不匹配pattern的行归并到上一行
multiline.negate: true
# after暗示归并到上一行的末端，before暗示归并到上一行的行首    
multiline.match: after
# 匹配最大行数    
multiline.max_lines: 1000
```
针对不同的日志格式（业务日志，nginx日志，系统日志，操作日志，MySQL日志等），需要手动写正则去匹配。


