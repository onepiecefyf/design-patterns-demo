## 使用logstash遇到的一些问题

### 1、logstash kafka input配置
> 想要使用多个 logstash 端协同消费同一个 topic 的话，那么需要把两个或是多个 logstash 消费端配置成相同的 group_id 和 topic_id， 但是前提是要把相应的 topic 分多个 partitions (区)，多个消费者消费是无法保证消息的消费顺序性的。 
> 保证消息的顺序，那就用一个 partition。 kafka 的每个 partition 只能同时被同一个 group 中的一个 consumer 消费。


### 2、type类型区分不同日志数据源
假如输入数据源包含多个，针对不同的输入源想要使用不同的过滤算法，可以使用type来区分。    

input配置
```java
input{
   kafka{
      type => "nginx"
      topics => ["nginxTopic"]
      bootstrap_servers => ["10.0.4.2:9092"]
    }

   kafka{
      type => "system"
      topics => ["systemTopic"]
      bootstrap_servers => ["10.0.4.2:9092"]
    }

   kafka{
      type => "business"
      topics => ["businessTopic"]
      bootstrap_servers => ["10.0.4.2:9092"]
    }
}
```

filter配置
```java
filter {
   if[type] == "nginx" {
      grok {
         patterns_dir => "/usr/local/logstash/logstash-7.0.0/patterns"
         match => { "message" => "%{NGINX_ACCESS}" }
      }
      mutate {
         remove_field => ["@version"]
         remove_field => ["tags"]
      }
   }
   ........
}
```

### 3、使用自定义的模版
日志采集到kafka，logstash消费kafka日志存储到ES，如果没有配置索引等相关信息，查询是没有效果的。  
使用logstash来管理自定义的模版，使用方式如下：
```java
output{
   if[type] == "system" {
      elasticsearch {
           hosts => ["http://10.0.4.x:9200"]
           user => "xxx"
           password => "xxx"
           index => "system-%{+YYYYMMdd}"
           document_type => "system"
           template => "../config/systemlog.json"
           template_name => "system-log"
           manage_template => true
           template_overwrite => true
      }
   }
}
```

template: 模板的路径  
template_name: 模版的名称  
manage_template: 布尔类型 默认为true 设置为false将关闭logstash自动管理模板功能  
template_overwrite: 设置为true表示如果你有一个自定义的模板叫logstash，那么将会用你自定义模板覆盖默认模板logstash  

> 注意：如果使用自定的模版需要删除已存在的模版，一般情况下启动logstash它就会在ES里面自动创建一个名为logstash的模版，如果不删除，ES里就变成有两个模板，logstash和myname，都匹配logstash-*索引名，
> ES会按照一定的规则来尝试自动merge多个都匹配上了的模板规则，最终运用到索引上。

### 4、logstash日志问题
logstash有自己的日志打印方式，在logstash.conf配置文件中，如下所示：
```java
stdout {
  # 以ruby格式打印日志，也可以配置为jsonline，以json方式打印logstash日志
  codec => rubydebug
}
```

这一配置**强烈建议**不要再生产环境使用，直接去掉即可。在测试环境测试logstash是否正常处理日志即可。  
因为logstash每处理一条日志就会打印这条日志详细信息，一般生产环境都会将logstash作为系统服务来启动，如果每天日志量很大，就会导致logstash日志直接撑爆磁盘，当系统磁盘空间不可用时，ES会自动更新配置为不可写入，导致日志系统不可用。






