## 部署系统信息
系统
> UnionTech OS Server 20 1020e

版本
> Linux host-192-16-2-64 4.19.90-2106.3.0.0095.up2.uel20.aarch64


## 错误信息
Fail to start LVS and VRRP High Availability Monitor
![](https://www.onepiese.top/admin/img/keealived.png)
执行命令journalctl -xe，查看详细的错误信息  
> libssl.so.10缺失库文件  

## 解决思路
一顿谷歌，找到以下几种别人解决的方案  
1、给keepalived.conf添加权限  
```shell
chmod 644 keepalived.conf
```
试了，没能解决问题。    

2、丢失库文件对应的软链接导致  
解决这个问题的解决方法网上很多，不再重复，尝试之后还是不能解决我的问题。  

3、keepalived包的问题
因为适配的是新的系统，之前适配过arm系统，以为这个包也可以使用，执行命令查看系统可以适配的包：
```shell
[root@host-192-16-2-64 ~]# yum list | grep keepalived
keepalived.aarch64           2.0.20-18.uel20                   @@System
keepalived-help.noarch       2.0.20-18.uel20                   everything
puppet-keepalived.noarch     0.0.1-0.11.bbca37agit.uel20       openstack
```
对比自己本地的包，果然不一样啊，本地包keepalived-1.3.5-19.el7.aarch64.rpm  

## 解决方法
1、执行命令查找适配系统包
```shell
[root@host-192-16-2-64 ~]# yum list | grep keepalived
keepalived.aarch64           2.0.20-18.uel20                   @@System
keepalived-help.noarch       2.0.20-18.uel20                   everything
puppet-keepalived.noarch     0.0.1-0.11.bbca37agit.uel20       openstack
```

2、下载包到系统
```shell
# 执行命令会把相关依赖下载下来
yum install --downloadonly --downloaddir=./  keepalived
```

3、开始编写剧本
```yaml
# 将下载的依赖包复制到指定安装目录
- name: copy | Copy rpm file to agent
  copy: src={{ item.src }} dest={{ item.dest }}
  with_items:
    - { src: 'libsemanage-python-2.5-14.el7.aarch64.rpm', dest: '{{ software_install_path }}' }
    - { src: 'libselinux-python3-2.5-15.el7.aarch64.rpm', dest: '{{ software_install_path }}' }
    - { src: 'keepalived-2.0.20-18.uel20.aarch64.rpm', dest: '{{ software_install_path }}' }
    - { src: 'lm_sensors-3.6.0-4.uel20.aarch64.rpm', dest: '{{ software_install_path }}' }
    - { src: 'lm_sensors-help-3.6.0-4.uel20.aarch64.rpm', dest: '{{ software_install_path }}' }
    - { src: 'net-snmp-5.9-3.up1.uel20.aarch64.rpm', dest: '{{ software_install_path }}' }
    - { src: 'net-snmp-help-5.9-3.up1.uel20.noarch.rpm', dest: '{{ software_install_path }}' }

# 开始安装依赖包
- name: install | Istall libsemanage file to agent.
  shell: "rpm -Uvh --force --nodeps {{ item }}"
  args:
    chdir: '{{ software_install_path }}'
  with_items:
    - libsemanage-python-2.5-14.el7.aarch64.rpm
    - libselinux-python3-2.5-15.el7.aarch64.rpm
    - lm_sensors-3.6.0-4.uel20.aarch64.rpm
    - lm_sensors-help-3.6.0-4.uel20.aarch64.rpm
    - net-snmp-5.9-3.up1.uel20.aarch64.rpm
    - net-snmp-help-5.9-3.up1.uel20.noarch.rpm
```

4、问题解决
```shell
[root@host-192-16-2-64 ~]# systemctl status keepalived
● keepalived.service - LVS and VRRP High Availability Monitor
   Loaded: loaded (/usr/lib/systemd/system/keepalived.service; disabled; vendor preset: disabled)
   Active: active (running) since Wed 2021-11-03 18:23:10 CST; 19h ago
 Main PID: 2170507 (keepalived)
    Tasks: 1
   Memory: 3.1M
   CGroup: /system.slice/keepalived.service
           └─2170507 /usr/sbin/keepalived -D

11月 03 18:23:10 host-192-16-2-64 systemd[1]: Starting LVS and VRRP High Availability Monitor...
11月 03 18:23:10 host-192-16-2-64 Keepalived[2170506]: Starting Keepalived v2.0.20 (01/22,2020)
11月 03 18:23:10 host-192-16-2-64 Keepalived[2170506]: Running on Linux 4.19.90-2106.3.0.0095.up2.uel20.aarch64 #1 SMP Thu Jul 8 11:55:51 UTC 2021 (built for Linux 4.19.90)
11月 03 18:23:10 host-192-16-2-64 Keepalived[2170506]: Command line: '/usr/sbin/keepalived' '-D'
11月 03 18:23:10 host-192-16-2-64 Keepalived[2170506]: Opening file '/etc/keepalived/keepalived.conf'.
11月 03 18:23:10 host-192-16-2-64 Keepalived[2170507]: Warning - keepalived has no configuration to run
11月 03 18:23:10 host-192-16-2-64 systemd[1]: Started LVS and VRRP High Availability Monitor.
```
