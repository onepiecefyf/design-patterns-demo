# Jenkins插件介绍以及使用

## 修改Jenkins插件下载地址
Jenkins国外的插件地址下载速度很慢，需要修改为国内的插件地址，
**Jenkins->Manager Jenkins->Jenkins Plugin**插件下载的地方。  
替换为国内的插件下载地址，操作如下：
```shell
cd /var/lib/jenkins/updates
sed -i 's/http:\/\/updates.jenkinsci.org\/download/https:\/\/mirrors.tuna.tsinghua.edu.cn\/jenkins/g' default.json && sed -i
's/http:\/\/www.google.com/https:\/\/www.baidu.com/g' default.json
```
然后，Manage Plugins点击Advanced，把Update Site改为国内插件下载地址  
```java
https://mirrors.tuna.tsinghua.edu.cn/jenkins/updates/update-center.json
```
最后，重启Jenkins，直接在浏览器输入http://IP:端口/restart

## 中文汉化插件
**Jenkins->Manager Jenkins->Jenkins Plugin**，点击available，搜索**Chinese**安装，安装完成之后重启
```java
http://loca:端口/restart
```
## Jenkins用户权限管理插件
安装Role-based Authorization Strategy插件来管理Jenkins用户权限，安装完成之后，进入到Manager Jenkins->Configure Global Securitys，
授权策略切换为Role-Based Strategy。  
系统管理界面进入到Manager Assign Roles，可以看到有Global Roles、Project Roles  
- Global roles（全局角色）：管理员等高级用户可以创建基于全局的角色。  
- Project roles（项目角色）：针对某个或者某些项目的角色。  
- Slave roles（奴隶角色）：节点相关的权限。

## Jenkins凭证管理插件
凭据可以用来存储需要密文保护的数据库密码、Gitlab密码信息、Docker私有仓库密码等，以便
Jenkins可以和这些第三方的应用进行交互。
### Credentials Binding
要在Jenkins使用凭证管理功能，需要安装Credentials Binding插件，安装插件后，左边多了"凭证"菜单，在这里管理所有凭证  
- Username with password：用户名和密码  
- SSH Username with private key： 使用SSH用户和密钥  
- Secret file：需要保密的文本文件，使用时Jenkins会将文件复制到一个临时目录中，再将文件路径
设置到一个变量中，等构建结束后，所复制的Secret file就会被删除。  
- Secret text：需要保存的一个加密的文本串，如钉钉机器人或Github的api token  
- Certificate：通过上传证书文件的方式  
## Jenkins Git插件
为了让Jenkins支持从Gitlab拉取源码，需要安装Git插件以及在Jenkins安装系统上安装Git工具。CentOS安装Git插件
```shell
yum install git -y 
git --version 
```
## Jenkins Maven插件
在Jenkins集成服务器上，我们需要安装Maven来编译和打包项目
### 安装Maven
```shell
tar -xzf apache-maven-3.6.2-bin.tar.gz 解压
mkdir -p /opt/maven 创建目录
mv apache-maven-3.6.2/* /opt/maven 移动文件
```
### 配置环境变量
```shell
vi /etc/profile

# 在文件末尾添加  
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk   
export MAVEN_HOME=/opt/maven   
export PATH=$PATH:$JAVA_HOME/bin:$MAVEN_HOME/bin 

# 使文件生效
source /etc/profile

# 查看maven版本
maven -v
```
### 关联Maven和JDK  
Jenkins->Global Tool Configuration->JDK->新增JDK，配置如下：  
- 别名: JDK1.8  （别名可以自己设置） 
- JAVA_HOME: 安装在Jenkins机器的JDK的目录    

Jenkins->Global Tool Configuration->Maven->新增Maven，配置如下：  
- Name: maven3.6.2 (可以自行设置)  
- MAVEN_HOME: 安装在Jenkins机器的maven目录

Manage Jenkins->Configure System->Global Properties ，添加三个全局变量  
- JAVA_HOME: 安装JDK目录
- M2_HOME: 安装maven目录
- PATH+EXTRA: $M2_HOME/bin

修改Maven的settings配置文件（仓库地址以及阿里镜像）

## Jenkins远程部署插件
Jenkins本身无法实现远程部署到Tomcat的功能，需要安装Deploy to container插件实现