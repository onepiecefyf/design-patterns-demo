## 记一次ansible变量优化设计

### 1、背景
主要还是实施在部署过程中需要更改剧本的变量（大多数是IP和端口）问题，小剧本涉及的变量较少，更改起来很简单，但是，一般部署高可用剧本涉及的变量很多写死在剧本导致实施部署的时候更改起起来很是费劲，还容易改错。  

### 2、方案
查阅网上资料发现ansible支持一种内置变量groups。groups内置变量就是可以获取到主机清单中的所有分组的意思。  
举个例子，现有主机清单inventory如下：
```java
[middleware]
192.168.56.111

[background]
192.168.56.111

[webui]
192.168.56.111
```
该主机清单包含三个组middleware、background、webui。  

编写测试剧本test.yml查看组信息：
```java
---
- host: all
  remote_user: root
  task:
    - debug:
        msg: "{{ groups }}"
```
执行命令查看打印的组信息:
```java
ansible-playbook -i inventory test.yml
```
打印的组信息，如下：
```java
ok: [10.0.0.2] => {
    "msg": {
        "all": [
            "10.0.0.1",
            "10.0.0.2",
            "10.0.0.3"
        ],
        "background": [
            "10.0.0.2"
        ],
        "middleware": [
            "10.0.0.1"
        ],
        "ungrouped": [],
        "webui": [
            "10.0.0.3"
        ]
    }
}
```
从打印信息可以看出，所有主机默认被分成了组名为”all”的组，background、middleware、webui组中都只有一台主机，ansible自动将没有分组的主机分到了名为”ungrouped”的组中，即组名为”未分组”的组。  
可以获取主机清单的组信息，那就代表它其实是一个个数组组成的，也就是可以获取到组中的主机信息，获取组中信息的格式如下：
```java
- host: all
  remote_user: root
  task:
    - debug:
        msg: "{{ groups.background }}"
```
执行命令，查看打印信息
```java
ansible-playbook -i inventory test.yml
```
打印信息如下：
```java
ok: [10.0.0.2] => {
        "msg": [
        "10.0.0.2"
        ]
        }
```
得到的就是单个组中的主机清单，但是，单个组可以包含多个主机清单，要想获取某个主机也很简单，就像是获取Java数组中的信息一样，格式如下：
```java
# 获取组background下第一个主机清单
- host: all
  remote_user: root
  task:
    - debug:
        msg: "{{ groups.background[0] }}"
```
### 3、结论
剧本优化前：
```java
# 安装 Java Kafka Zookeeper
- hosts: middleware
  vars:
    - local_ip: "{{ inventory_hostname }}"
    - java_version: "1.8"
    - java_home: "/usr/java/jdk1.8.0_191"
    - java_file: "jdk-8u191-linux-x64.tar.gz"
    - java_install_path: "{{ software_install_path }}/jdk1.8.0_191"
    - zookeeper_auth_ips:
      - "192.168.33.21"
      - "192.168.33.22"
      - "192.168.33.23"
```
剧本优化后：
```java
# 安装 Java Kafka Zookeeper
- hosts: middleware
  vars:
    - local_ip: "{{ inventory_hostname }}"
    - java_version: "1.8"
    - java_home: "/usr/java/jdk1.8.0_191"
    - java_file: "jdk-8u191-linux-x64.tar.gz"
    - java_install_path: "{{ software_install_path }}/jdk1.8.0_191"
    - zookeeper_auth_ips:
      - "{{ groups.middleware[0] }}"
      - "{{ groups.background[0] }}"
      - "{{ groups.webui[0] }}"
```

优化之前每次实施去部署项目只要主机清单IP变更，那么剧本涉及的IP都需要手动更改，稍一不慎就会部署出错，并且每次改动也很麻烦。  
优化之后每次部署只需要更改外部的inventory中涉及的主机清单即可，不需要更改剧本，省时省力。
