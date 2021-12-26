## tracing-zipkin

##  zipkin 기동 
- `dapr init` 명령어를 하면 기본적으로 dapr, redis, zipkin docker container가 로컬에서 실행된다  
- 수동으로 zipkin을  기동 해도 된다 
```
docker run -d -p 9411:9411 openzipkin/zipkin

```
## simple-api(brach: dapr-tracing-zipkin)
### config
- components는 필요 없다 
- config 설정으로만 가능하다 
- dapr-config >  zipkin-config.yaml

`zipkin-config.yaml`
```
apiVersion: dapr.io/v1alpha1
kind: Component
metadata:
  name: zipkin
spec:
  type: exporters.zipkin
  metadata:
  - name: enabled
    value: "true"
  - name: exporterAddress
    value: http://localhost:9411/api/v2/spans
  
```


### Run Dapr sidecar 
- zipkin은 `config`로 sidecar 설정해야 한다 기존처럼 components로 하면 안된다 
- --config flag로 설정한다 
```
dapr run --dapr-http-port 4320  --app-id simple-api --app-port 9320 --config ./dapr-config/zipkin-config.yaml mvn spring-boot:run

```

### zipkin 화면 
- http://localhost:9411


## dapr로 app 호출 
```
curl http://localhost:4320/v1.0/invoke/simple-api/method/api/hello
```

## dashboard 
```
//default  (port: 8080 )
dapr dashboard   

// port는 자유롭게 지정가능
dapr dashboard -p 9991
```