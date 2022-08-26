### 背景
ELK日志系统每天采集日志存储到ES中，我们日志是按照天来分割，需要每天创建新的索引存储日志数据。  
一方面是为了提高效率，当天创建第二天的索引，清除前一天的索引，这样存储明天数据不用再创建索引；  
另一方面定时任务清理数据，创建索引，省心省事。  

### crontab 使用

1、命令了解
```shell
[root@10_0_4_2 systemd]# crontab -h
```
```java
Usage:
 crontab [options] file
 crontab [options]
 crontab -n [hostname]

Options:
 -u <user>  define user 指定用户
 -e         edit user's crontab 编辑某个用户的定时任务
 -l         list user's crontab 列出某个用户的定时任务，默认root用户
 -r         delete user's crontab 删除某个用户下的所有定时任务
```

2、常用命令
```java
# 查看某个用户下的定时任务
crontab -u root -l

# 删除某个用户下的定时任务
crontab -u root -r 

# 编辑某个用户下的定时任务
crontab -u root -e
```

### 一键部署（ansible）实现

#### 1、脚本实现
```shell
#!/bin/bash
searchIndex=log
es_url={{ es_url }}

date2stamp () {
    date --utc --date "$1" +%s
}

dateDiff (){
    case $1 in
        -s)   sec=1;      shift;;
        -m)   sec=60;     shift;;
        -h)   sec=3600;   shift;;
        -d)   sec=86400;  shift;;
        *)    sec=86400;;
    esac
    dte1=$(date2stamp $1)
    dte2=$(date2stamp $2)
    diffSec=$((dte2-dte1))
    if ((diffSec < 0)); then abs=-1; else abs=1; fi
    echo $((diffSec/sec*abs))
}

echo "-> -> -> begin clean old index `date '+%Y-%m-%d %H:%M:%S'`"

for index in $(curl -s "${es_url}_cat/indices?v" |     grep -E " ${searchIndex}-20[0-9][0-9][0-1][0-9][0-3][0-9]" | awk '{     print $3 }'); do
  # echo "------->${index}<---------"
  date=$(echo ${index: -8}) 
  cond=$(date +%Y%m%d)
  # echo "------->${cond}<--------"
  diff=$(dateDiff -d $date $cond)
  # echo -n "${index} (${diff})"
  if [ $diff -gt {{ clean_days }} ]; then
    echo " ${index} (${diff}) / DELETE"
    curl -XDELETE "${es_url}${index}?pretty"
  fi
done

echo "--> start forcemerge <--"
curl -XPOST "${es_url}_forcemerge?only_expunge_deletes=true&pretty=true"
echo "--> end forcemerge <--"

echo "--> start add tomorrow index <--"
tomorrow=$(date -d "1 day" +"%Y%m%d")
curl -XPUT "${es_url}log-${tomorrow}"  -H "Content-Type: application/json" -d '{"settings":{"number_of_shards":5,"number_of_replicas":1},"mappings":{"log":{"properties":{"body":{"type":"text","fields":{"keyword":{"type":"keyword","ignore_above":256}}},"file":{"type":"text","fields":{"keyword":{"type":"keyword","ignore_above":256}}},"hostname":{"type":"text","fields":{"keyword":{"type":"keyword","ignore_above":256}}},"level":{"type":"text","fields":{"keyword":{"type":"keyword","ignore_above":256}}},"project":{"type":"text","fields":{"keyword":{"type":"keyword","ignore_above":256}}},"timestamp":{"type":"date"}}}}}'
echo "--> end add tomorrow index <--"

curl -H "Content-Type:application/json" -XPOST '${es_url}_cache/clear' -d '{ "fielddata": "true" }'

echo "end <- <- <-"

```

#### 2、创建任务
```java
---

- name: Template clean crontab script
  template:
    src: clean.sh
    dest: "/home/footstone/"
    owner: "footstone"
    group: "footstone"

- name: edit crontab to clean es log index, 5 1 * * *
  cron:
    user: footstone
    name: "clean es"
    minute: 5
    hour: 1
    job: "/bin/sh /home/footstone/clean.sh >> /home/footstone/es_clean_cron.log 2>&1"

```

#### 3、查看定时任务日志
```shell
[root@10_0_4_3 footstone]# less es_clean_cron.log
```

```java
-> -> -> begin clean old index 2022-03-17 01:05:01
--> start forcemerge <--
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
^M  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0^M100    82  100    82    0     0    243      0 --:--:-- --:--:-- --:--:--   244
{
  "_shards" : {
    "total" : 87,
    "successful" : 47,
    "failed" : 0
  }
}
--> end forcemerge <--
--> start add tomorrow index <--
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
^M  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0^M100   611  100    71  100   540    359   2733 --:--:-- --:--:-- --:--:--  2741
{"acknowledged":true,"shards_acknowledged":true,"index":"log-20220318"}--> end add tomorrow index <--
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
^M  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0
end <- <- <-
```
