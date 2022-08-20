## consul-zipkin


##  consul 기동 
- 로컬 dev로 기동 
```
consul agent -dev

```
##  zipkin 기동 
- `dapr init` 명령어를 하면 기본적으로 dapr, redis, zipkin docker container가 로컬에서 실행된다  
- 수동으로 zipkin을  기동 해도 된다 
```
docker run -d -p 9411:9411 openzipkin/zipkin

```
## simple-api(brach: dapr-consul-zipkin)
### config
dapr-config > consul-zipkin-config.yaml

`consul-zipkin-config.yaml`
```
apiVersion: dapr.io/v1alpha1
kind: Configuration
metadata:
  name: daprConfig
spec:
  nameResolution:
    component: "consul"
    configuration:
      selfRegister: true
  tracing:
    samplingRate: "1"
    zipkin:
      endpointAddress: "http://localhost:9411/api/v2/spans"
  
```


## Run Dapr sidecar 
- consul/zipkin은 `config`로 sidecar 설정해야 한다 기존처럼 components로 하면 안된다 
- --config flag로 설정한다 
```
dapr run --dapr-http-port 4320  --app-id simple-api --app-port 9320 --config ./dapr-config/consul-zipkin-config.yaml ./mvnw spring-boot:run

```


## ui
- consul ui :http://localhost:8500
- zipkin ui: http://localhost:9411

## dapr로 app 호출 
```
curl http://localhost:4320/v1.0/invoke/simple-api/method/api/hello
curl http://localhost:4320/v1.0/invoke/simple-api/method/api/simple
curl http://localhost:4320/v1.0/invoke/simple-api/method/api/version
```

## consul  service 삭제 필요시
```
consul services deregister -id=gateway
```

## dashboard 
```
//default  (port: 8080 )
dapr dashboard   

// port는 자유롭게 지정가능
dapr dashboard -p 9991
```