### 1、 logstash的两种启动方式：
> 1、logstash -f logstash.conf  
> 2、配置以服务的方式启动  

第一种方式一般在测试环境选择使用，因为直接指定执行的配置，操作简单；缺点也是显而易见，不会随着系统的重启而启动，不适合线上环境操作。  
第二种方式配置以服务的方式启动，会随着系统的启动而启动，比较适合线上环境。

### 2、Service特性简介
> service命令用于对系统服务进行管理，比如启动（start）、停止（stop）、重启（restart）、查看状态（status）等。相关的命令还包括chkconfig、ntsysv等，chkconfig用于查看、设置服务的运行级别，ntsysv用于直观方便的设置各个服务是否自动启动。service命令本身是一个shell脚本，它在/etc/init.d/目录查找指定的服务脚本，然后调用该服务脚本来完成任务。这个命令不是在所有的linux发行版本中都有。主要是在redhat、fedora、mandriva和centos中。

### 2、更改logstash配置

找到logstash的安装目录，修改文件**startup.optins**，我只是列出配置需要修改部分：
```text
# logstash的安装目录
LS_HOME={{ logstash_home }}

# logstash的配置目录${LS_HOM}/config
LS_SETTINGS_DIR={{ logstash_config_path }}

# 为logstash指定配置参数 指定启动文件
LS_OPTS="--path.settings ${LS_SETTINGS_DIR} -f ${LS_SETTINGS_DIR}/logstash.conf"
```

### 3、执行命令
```shell
bin/system-install
```

这条logstash命令执行之后，会自动生成系统服务，如下：
```shell
/etc/systemd/system/logstash.service
```

查看系统服务logstash.service配置：
```text
[Unit]
Description=logstash

[Service]
Type=simple
User=root
Group=root
# Load env vars from /etc/default/ and /etc/sysconfig/ if they exist.
# Prefixing the path with '-' makes it try to load, but if the file doesn't
# exist, it continues onward.
EnvironmentFile=-/etc/default/logstash
EnvironmentFile=-/etc/sysconfig/logstash
ExecStart=/usr/local/logstash/bin/logstash "--path.settings" "/usr/local/logstash/config" "-f" "/usr/local/logstash/config/logstash.conf"
Restart=always
WorkingDirectory=/
Nice=19
LimitNOFILE=16384

[Install]
WantedBy=multi-user.target
```

其中配置/etc/default/logstash是logstash的默认配置，是执行命令bin/system-install之后在startup.options的配置，所以这也就是为什么要修改startup.options的原因。

### 4、服务启动
```text
设置服务自启动：systemctl enable logstash  
启动服务：systemctl start logstash  
停止服务：systemctl stop logstash  
重启服务：systemctl restart logstash  
查看服务状态：systemctl status logstash  
```

