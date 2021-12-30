## install k8s
```
//설치
dapr init -k --wait      # default(300)
dapr init -k --wait --timeout 600
dapr status -k
dapr init -k --enable-mtls=false
dapr uninstall -k

kubectl get pods --namespace dapr-system

```
## simple-api(brach: dapr)

### 1. profile-local

- dapr-simple-api 기동 
```
dapr run --dapr-http-port 4320 --app-id simple-api --app-port 9320 mvn spring-boot:run
```

### 2. profile-dev
```
docker run --rm -p 9320:8080 saturn203/simple-api:dapr-v1.0 
dapr run --dapr-http-port 4320 --app-id simple-api --app-port 9320
```
### 3. url 확인
```
curl http://localhost:4320/v1.0/invoke/simple-api/method/api/hello
curl http://localhost/v1.0/invoke/simple-api/method/api/hello
```




## dashboard 
```
//default  (port: 8080 )
dapr dashboard   

// port는 자유롭게 지정가능
dapr dashboard -p 9991
```