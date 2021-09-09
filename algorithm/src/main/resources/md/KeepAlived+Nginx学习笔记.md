## 1、keepalived配置文件说明(主备模式)
```java
global_defs {
 # keepalived 自带的邮件提醒需要开启 sendmail 服务。建议用独立的监控或第三方 SMTP 
 router_id keep130 ## 标识本节点的字条串，通常为 hostname
}
# keepalived 会定时执行脚本并对脚本执行的结果进行分析，动态调整 vrrp_instance 的优先级。如果脚本执行结果为 0，并且 weight 配置的值大于 0，则优先级相应的增加。如果脚本执行结果非 0，并且 weight配置的值小于 0，则优先级相应的减少。其他情况，维持原本配置的优先级，即配置文件中 priority 对应的值。
vrrp_script chk_nginx {
 script "/etc/keepalived/nginx_check.sh" ## 检测 nginx 状态的脚本路径
 interval 2 ## 检测时间间隔
 weight -20 ## 如果条件成立，权重-20
}
## 定义虚拟路由，VI_1 为虚拟路由的标示符，自己定义名称
vrrp_instance VI_1 {
 state MASTER ## 主节点为 MASTER，对应的备份节点为 BACKUP
 interface eth1 ## 绑定虚拟 IP 的网络接口，与本机 IP 地址所在的网络接口相同，我的是 eth1
 virtual_router_id 130 ## 虚拟路由的 ID 号，两个节点设置必须一样，可选 IP 最后一段使用, 相同的 VRID 为一个组，他将决定多播的 MAC 地址
 mcast_src_ip 192.168.211.130 ## 本机 IP 地址
 priority 100 ## 节点优先级，值范围 0-254，MASTER 要比 BACKUP 高
nopreempt ## 优先级高的设置 nopreempt 解决异常恢复后再次抢占的问题
advert_int 1 ## 组播信息发送间隔，两个节点设置必须一样，默认 1s
## 设置验证信息，两个节点必须一致
authentication {
 auth_type PASS ## 密码验证的方式
 auth_pass 1111 ## 密码（真实生产，按需求对应该过来）
}
## 将 track_script 块加入 instance 配置块
 track_script {
 chk_nginx ## 执行 Nginx 监控的服务
}
## 虚拟 IP 池, 两个节点设置必须一样
 virtual_ipaddress {
    192.168.199.131 ## 虚拟 ip，可以定义多个
 }
}
```

## 2、Nginx检查脚本 nginx_check.sh
```shell
#!/bin/bash
A=`ps -C nginx –no-header |wc -l`
if [ $A -eq 0 ];then
/usr/local/nginx/sbin/nginx ## nginx安装地址
sleep 2
if [ `ps -C nginx --no-header |wc -l` -eq 0 ];then
killall keepalived
fi
fi
```

## 3、Nginx参数优化
**nginx内部原理**
![](https://www.onepiese.top/admin/img/WechatIMG21.png)
![](https://www.onepiese.top/admin/img/WechatIMG22.png)
**master-workers 的机制的好处**
> 首先对于每个 worker 进程来说，独立的进程，不需要加锁，所以省掉了锁带来的开销，同时在编程以及问题查找时，也会方便很多。  
> 其次，采用独立的进程，可以让互相之间不会影响，一个进程退出后，其它进程还在工作，服务不会中断，master进程则很快启动新的worker 进程。当然，worker 进程的异常退出，肯定是程序有 bug 了，异常退出，会导致当前 worker 上的所有请求失败，不过不会影响到所有请求，所以降低了风险。  

**需要设置多少个 worker**  
> Nginx 同 redis 类似都采用了 io 多路复用机制，每个 worker 都是一个独立的进程，但每个进程里只有一个主线程，通过异步非阻塞的方式来处理请求， 即使是千上万个请求也不在话下。每个 worker 的线程可以把一个 cpu 的性能发挥到极致。所以 worker 数和服务器的 cpu数相等是最为适宜的。设少了会浪费 cpu，设多了会造成 cpu 频繁切换上下文带来的损耗。
```java
#设置 worker 数量。
worker_processes 4
#work 绑定 cpu(4 work 绑定 4cpu)。
worker_cpu_affinity 0001 0010 0100 1000
#work 绑定 cpu (4 work 绑定 8cpu 中的 4 个) 。
worker_cpu_affinity 0000001 00000010 00000100 00001000
```
**连接数 worker_connection**  
> 这个值是表示每个worker进程所能建立连接的最大值，所以，一个nginx能建立的最大连接数，应该是worker_connections * worker_processes。当然，这里说的是最大连接数，对于HTTP请求本地资源来说,能够支持的最大并发数量是worker_connections*worker_processes，如果是支持 http1.1 的浏览器每次访问要占两个连接，所以普通的静态访问最大并发数是：worker_connections * worker_processes /2，而如果是HTTP作为反向代理来说，最大并发数量应该是worker_connections * worker_processes/4。因为作为反向代理服务器，每个并发会建立与客户端的连接和与后端服务的连接，会占用两个连接。

## keepalived+nginx双主模式
在keepalived.conf配置中多添加vrrp_instance（定义虚拟路由）
```java
vrrp_instance VI_1 {
    state BACKUP
    interface ens33
    virtual_router_id 51
    priority 100
    advert_int 1
    authentication {
    auth_type PASS
    auth_pass 1111
  }
  virtual_ipaddress {
      192.168.1.110/24 dev ens33 label ens33:1 
  } 

}
vrrp_instance VI_2 {
    state MASTER
    interface ens33
    virtual_router_id 52
    priority 150
    advert_int 1
    authentication {
    auth_type PASS
    auth_pass 2222
  }
    virtual_ipaddress {
      192.168.1.210/24 dev ens33 label ens33:2
    } 
}
```