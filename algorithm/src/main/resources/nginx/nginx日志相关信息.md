nginx的日志包含了两类，一类是error.log,一类是access.log。

1.error.log的设置：

error_log的默认值：

#error_log  logs/error.log  error;
error_log的语法格式及参数语法说明如下:

    error_log    <FILE>    <LEVEL>;

    关键字        日志文件   错误日志级别

    关键字：其中关键字error_log不能改变

    日志文件：可以指定任意存放日志的目录

    错误日志级别：常见的错误日志级别有[debug | info | notice | warn | error | crit | alert | emerg]，级别越高记录的信息越少。

    生产场景一般是 warn | error | crit 这三个级别之一

注意：不要配置info等级较低的级别，会带来大量的磁盘I/O消耗。

error_log参数的标签段位置：

main, http, server, location

比如：我的nginx中记录的位置为：



就会在我设置的位置产生log文件：



2.access.log文件的设置：

Nginx访问日志主要有两个参数控制

log_format   #用来定义记录日志的格式（可以定义多种日志格式，取不同名字即可）

access_log  #用来指定日至文件的路径及使用的何种日志格式记录日志

其实这里我们主要讲述的是log_format的设置，以及各个字段的含义：

log_format格式变量：

    $remote_addr  #记录访问网站的客户端地址

    $remote_user  #远程客户端用户名

    $time_local  #记录访问时间与时区

    $request  #用户的http请求起始行信息

    $status  #http状态码，记录请求返回的状态码，例如：200、301、404等

    $body_bytes_sent  #服务器发送给客户端的响应body字节数

    $http_referer  #记录此次请求是从哪个连接访问过来的，可以根据该参数进行防盗链设置。

    $http_user_agent  #记录客户端访问信息，例如：浏览器、手机客户端等

    $http_x_forwarded_for  #当前端有代理服务器时，设置web节点记录客户端地址的配置，此参数生效的前提是代理服务器也要进行相关的x_forwarded_for设置

	log_format  access  '$remote_addr - $remote_user [$time_local] "$request" '
	        '$status $body_bytes_sent "$http_referer" '
	        '"$http_user_agent" $http_x_forwarded_for '
	 '"$upstream_addr" "$upstream_status" "$upstream_response_time" "$request_time"';
其实我们设置这些，主要是记录下来访问以及出错的情况，这样方便我们定位问题和分析。






Nginx日志相关指令主要有两条，一条是log_format，用来设置日志格式，另外一条是access_log，用来指定日志文件的存放路径、类型、缓存大小等，一般放在Nginx的默认主配置文件/etc/nginx/nginx.conf 

Nginx的log_format有很多可选的参数用于标示服务器的活动状态，默认的是：
‘$remote_addr – $remote_user [$time_local] “$request” ‘
‘$status $body_bytes_sent “$http_referer” ‘
‘”$http_user_agent” “$http_x_forwarded_for”‘;



参数	说明	示例
$remote_addr	客户端地址	219.227.111.255
$remote_user	客户端用户名称	—
$time_local	访问时间和时区	18/Jul/2014:17:00:01 +0800
$request	请求的URI和HTTP协议	“GET /article-10000.html HTTP/1.1”
$http_host	请求地址，即浏览器中你输入的地址（IP或域名）	www.ha97.com
$status	HTTP请求状态	200
$upstream_status	upstream状态	200
$body_bytes_sent	发送给客户端文件内容大小	1547
$http_referer	url跳转来源	https://www.google.com/
$http_user_agent	用户终端浏览器等信息	“Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; SV1; GTB7.0; .NET4.0C;
$ssl_protocol	SSL协议版本	TLSv1
$ssl_cipher	交换数据中的算法	RC4-SHA
$upstream_addr	后台upstream的地址，即真正提供服务的主机地址	10.36.10.80:80
$request_time	整个请求的总时间	0.165
$upstream_response_time	请求过程中，upstream响应时间	0.002

log_format  main  '$clientRealIp -_- $remote_addr - $remote_user [$time_local] "$request" '
'$status $body_bytes_sent "$http_referer" '
'"$http_user_agent" "$http_x_forwarded_for" '
'"$upstream_addr" "$upstream_status" "$upstream_response_time" '
'$request_time -- $http_cookie -- $cookie_pin';

已经设置了两个响应时间的参数，且nginx.conf配置已重载生效，但是打开assess.log尾部一直没出现这两个参数。原因是项目的虚拟配置。在虚拟配置assess_log后面加个main,启用main配置



input {
file {
path => [ "/data/nginx-logs/access.log" ]
start_position => "beginning"
ignore_older => 0
}
}

filter {
grok {
match => { "message" => "%{NGINXACCESS}" }

    }
    geoip {
      source => "http_x_forwarded_for"
      target => "geoip"
      database => "/etc/logstash/GeoLiteCity.dat"
      add_field => [ "[geoip][coordinates]", "%{[geoip][longitude]}" ]
      add_field => [ "[geoip][coordinates]", "%{[geoip][latitude]}" ]
    }

    mutate {
      convert => [ "[geoip][coordinates]", "float" ]
      convert => [ "response","integer" ]
      convert => [ "bytes","integer" ]
      replace => { "type" => "nginx_access" }
      remove_field => "message"
    }

    date {
      match => [ "timestamp","dd/MMM/yyyy:HH:mm:ss Z"]

    }
    mutate {
      remove_field => "timestamp"

    }

}
output {
elasticsearch {
hosts => ["127.0.0.1:9200"]
index => "logstash-nginx-access-%{+YYYY.MM.dd}"
}
stdout {codec => rubydebug}
}

geoip {
database => "/usr/share/logstash/geodb/GeoLite2-City.mmdb"
source => "Client_IP"
target => "geoip"
fields => ["country_name","region_name", "city_name", "ip", "longitude", "latitude", "location"]
add_field => [ "[geoip][coordinates]", "%{[geoip][longitude]}" ]
add_field => [ "[geoip][coordinates]", "%{[geoip][latitude]}"  ]
}
useragent {
source => "User_Agent"
target => "ua"
}
mutate {
convert => [ "[geoip][coordinates]", "float" ]
convert => [ "Http_Status_Code", "integer" ]
convert => [ "Http_Bytes", "integer" ]
convert => [ "Request_Time", "float" ]
convert => [ "Upstream_Response_Time", "float" ]
split => ["Http_Request", "?"]
add_field => { "Http_URI" => "%{Http_Request[0]}" }
remove_field => [ "message", "beat", "@version", "auth", "prospector", "source", "offset"]
}


参数	说明	示例
$remote_addr	客户端地址	219.227.111.255
$remote_user	客户端用户名称	—
$time_local	访问时间和时区	18/Jul/2014:17:00:01 +0800
$request	请求的URI和HTTP协议	“GET /article-10000.html HTTP/1.1”
$http_host	请求地址，即浏览器中你输入的地址（IP或域名）	www.ha97.com
$status	HTTP请求状态	200
$upstream_status	upstream状态	200
$body_bytes_sent	发送给客户端文件内容大小	1547
$http_referer	url跳转来源	https://www.google.com/
$http_user_agent	用户终端浏览器等信息	“Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; SV1; GTB7.0; .NET4.0C;
$ssl_protocol	SSL协议版本	TLSv1
$ssl_cipher	交换数据中的算法	RC4-SHA
$upstream_addr	后台upstream的地址，即真正提供服务的主机地址	10.36.10.80:80
$request_time	整个请求的总时间	0.165
$upstream_response_time	请求过程中，upstream响应时间	0.002

log_format  main  '$time_local $remote_addr $remote_user $http_host $request_method "$request" '
                  '$status $body_bytes_sent "$http_referer" '
                  '"$http_user_agent" "$http_x_forwarded_for" '
                  '"$upstream_addr" "$upstream_status" "$upstream_response_time" '
                  '$request_time';



URIPARM1 [A-Za-z0-9$.+!*'|(){},~@#%&/=:;^\\_<>`?\-\[\]]*
URIPATH1 (?:/[\\A-Za-z0-9$.+!*'(){},~:;=@#% \[\]_<>^\-&?]*)+
HOSTNAME1 \b(?:[0-9A-Za-z_\-][0-9A-Za-z-_\-]{0,62})(?:\.(?:[0-9A-Za-z_\-][0-9A-Za-z-:\-_]{0,62}))*(\.?|\b)
STATUS ([0-9.]{0,3}[, ]{0,2})+
HOSTPORT1 (%{IPV4}:%{POSINT}[, ]{0,2})+
FORWORD (?:%{IPV4}[,]?[ ]?)+|%{WORD}



URIPARM1 [A-Za-z0-9$.+!*'|(){},~@#%&/=:;^\\_<>`?\-\[\]]*

URIPATH1 (?:/[\\A-Za-z0-9$.+!*'(){},~:;=@#% \[\]_<>^\-&?]*)+

HOSTNAME1 \b(?:[0-9A-Za-z_\-][0-9A-Za-z-_\-]{0,62})(?:\.(?:[0-9A-Za-z_\-][0-9A-Za-z-:\-_]{0,62}))*(\.?|\b)

STATUS ([0-9.]{0,3}[, ]{0,2})+

HOSTPORT1 (%{IPV4}:%{POSINT}[, ]{0,2})+

FORWORD (?:%{IPV4}[,]?[ ]?)+|%{WORD}

URIPARM [A-Za-z0-9$.+!*'|(){},~@#%&/=:;_?\-\[\]]*

URIPATH (?:/[A-Za-z0-9$.+!*'(){},~:;=@#%&_\- ]*)+

URI1 (%{URIPROTO}://)?(?:%{USER}(?::[^@]*)?@)?(?:%{URIHOST})?(?:%{URIPATHPARAM})?

NGINXACCESS %{IPORHOST:remote_addr} - (%{USERNAME:user}|-) \[%{HTTPDATE:log_timestamp}\]  \"{WORD:request_method} %{URIPATH1:uri}\" %{BASE10NUM:http_status} (?:%{BASE10NUM:body_bytes_sent}|-) \"(?:%{GREEDYDATA:http_referrer}|-)\" \"(%{GREEDYDATA:user_agent}|-)\" (%{FORWORD:x_forword_for}|-) (?:%{HOSTPORT1:upstream_addr}|-) ({BASE16FLOAT:upstream_response_time}|-) (%{STATUS:request_time}|-)











\[%{HTTPDATE:log_timestamp}\] (?:%{HOSTPORT1:request_uri}|-) %{WORD:request_method} \"%{URIPATH1:uri}\" \"%{URIPARM1:param}\" %{BASE10NUM:http_status} (?:%{BASE10NUM:body_bytes_sent}|-) \"(?:%{URIPATH:http_referrer}|-)\" %{BASE10NUM:upstream_status} (?:%{HOSTPORT1:upstream_addr|-) (%{STATUS:request_time}|-) ({BASE16FLOAT:upstream_response_time}|-) \"(%{GREEDYDATA:user_agent}|-)\" \(%{FORWORD:x_forword_for}|-)





