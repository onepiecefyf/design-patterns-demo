## 住建委部署-统一认证相关地址
http://192.169.226.16:31008/am  

http://192.169.226.16:30801/idm/#/user/login   

http://192.169.226.16:31802/portal  

## 进入pod命令
kubectl get pod | grep uams

kubectl exec -it  uams-idm-0  -c uams-idm bash

kubectl exec -it  uams-am-0  -c uams-am bash

kubectl exec -it uams-consumer-0  -c uams-consumer bash

kubectl exec -it uams-portal-0  -c uams-portal bash

kubectl exec -it  uams-opendj-0  -c uams-opendj bash

## 查看pod网络相关信息
kubectl get pod uams-am-75988864c4-qcfvl -owide

kubectl get pod uams-consumer-f67cc5dcd-cxn6t -owide

kubectl get pod uams-idm-6df69b5f99-mk84p -owide

kubectl get pod uams-opendj-5568b99ddd-dmlwb -owide

kubectl get pod uams-portal-84965fb9b-44tjq  -owide

## 统一认证镜像
docker load -i uams-docker-3.6.3.tar.gz

docker tag uams:3.6.3 tpaas.registry.hub/test/uams:3.6.3

docker push tpaas.registry.hub/test/uams:3.6.3

## 查看镜像信息
docker image ls | grep uams

docker rmi -f tpaas.registry.hub/test/uams:3.6.3

## 部署统一认证
kubectl apply -f uams-config-map.yml

kubectl apply -f service.yml

kubectl apply -f deployment.yml