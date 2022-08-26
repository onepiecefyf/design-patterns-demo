## Ansible发展史

[scode type="green"]
Ansible  
Michael DeHaan（ Cobbler 与 Func 作者）  
名称来自《安德的游戏》中跨越时空的即时通信工具  
2012-03-09，发布0.0.1版，2015-10-17，Red Hat宣布收购  
官网：https://www.ansible.com/  
官方文档：https://docs.ansible.com/  
[/scode]

## ansible的作用以及工作结构
### 1、ansible简介：
ansible是新出现的自动化运维工具，基于Python开发，
集合了众多运维工具（puppet、cfengine、chef、func、fabric）的优点，
实现了批量系统配置、批量程序部署、批量运行命令等功能。  
ansible是基于模块工作的，本身没有批量部署的能力。  
真正具有批量部署的是ansible所运行的模块，ansible只是提供一种框架。  
主要包括：  
(1)、连接插件connection plugins：负责和被监控端实现通信；  
(2)、host inventory：指定操作的主机，是一个配置文件里面定义监控的主机；  
(3)、各种模块核心模块、command模块、自定义模块；  
(4)、借助于插件完成记录日志邮件等功能；  
(5)、playbook：剧本执行多个任务时，非必需可以让节点一次性运行多个任务。

### 2、ansible的架构

连接其他主机默认使用ssh协议

## Ansible主要组成部分

[scode type="blue"]
ANSIBLE PLAYBOOKS：任务剧本（任务集），编排定义Ansible任务集的配置文件，由Ansible顺序依次执行，通常是JSON格式的YML文件  
INVENTORY：Ansible管理主机的清单  /etc/anaible/hosts  
MODULES：  Ansible执行命令的功能模块，多数为内置核心模块，也可自定义  
PLUGINS：  模块功能的补充，如连接类型插件、循环插件、变量插件、过滤插件等，该功能不常用    
API：      供第三方程序调用的应用程序编程接口  
ANSIBLE：  组合INVENTORY、API、MODULES、PLUGINS的绿框，可以理解为是ansible命令工具，其为核心执行工具
[/scode]

**Ansible命令执行来源:**  
1、USER，普通用户，即SYSTEM ADMINISTRATOR  
2、CMDB（配置管理数据库） API 调用  
3、PUBLIC/PRIVATE CLOUD API调用  (公有私有云的API接口调用)  
4、USER-> Ansible Playbook -> Ansibile

**利用ansible实现管理的方式**   
1、Ad-Hoc 即ansible单条命令，主要用于临时命令使用场景  
2、Ansible-playbook 主要用于长期规划好的，大型项目的场景，需要有前期的规划过程    

**Ansible-playbook（剧本）执行过程**  
1、将已有编排好的任务集写入Ansible-Playbook  
2、通过ansible-playbook命令分拆任务集至逐条ansible命令，按预定规则逐条执行  

**Ansible主要操作对象**   
1、HOSTS主机    
2、NETWORKING网络设备  

[scode type="yellow"]
**注意事项:**   
执行ansible的主机一般称为主控端，中控，master或堡垒机  
主控端Python版本需要2.6或以上  
被控端Python版本小于2.4需要安装python-simplejson  
被控端如开启SELinux需要安装libselinux-python  
windows不能做为主控端  
ansible不是服务,不会一直启动,只是需要的时候启动
[/scode]

## 安装
```shell
rpm包安装: EPEL源
    yum install ansible

编译安装:
    yum -y install python-jinja2 PyYAML python-paramiko python-babel
    python-crypto
    tar xf ansible-1.5.4.tar.gz
    cd ansible-1.5.4
    python setup.py build
    python setup.py install
    mkdir /etc/ansible
    cp -r examples/* /etc/ansible


Git方式:
    git clone git://github.com/ansible/ansible.git --recursive
    cd ./ansible
    source ./hacking/env-setup

pip安装： pip是安装Python包的管理器，类似yum
    yum install python-pip python-devel
    yum install gcc glibc-devel zibl-devel rpm-bulid openssl-devel
    pip install --upgrade pip
    pip install ansible --upgrade

确认安装：
    ansible --version
```

## 相关文件
```java
配置文件  
/etc/ansible/ansible.cfg  主配置文件,配置ansible工作特性(一般无需修改)  
/etc/ansible/hosts        主机清单(将被管理的主机放到此文件)  
/etc/ansible/roles/       存放角色的目录

程序  
/usr/bin/ansible          主程序，临时命令执行工具  
/usr/bin/ansible-doc      查看配置文档，模块功能查看工具  
/usr/bin/ansible-galaxy   下载/上传优秀代码或Roles模块的官网平台  
/usr/bin/ansible-playbook 定制自动化任务，编排剧本工具  
/usr/bin/ansible-pull     远程执行命令的工具  
/usr/bin/ansible-vault    文件加密工具  
/usr/bin/ansible-console  基于Console界面与用户交互的执行工具  
```