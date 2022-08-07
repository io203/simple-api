## tracing-zipkin-k8s

##  zipkin 기동 
- `dapr init` 명령어를 하면 기본적으로 dapr, redis, zipkin docker container가 로컬에서 실행된다  
- 수동으로 zipkin을  기동 해도 된다 
```
docker run -d -p 9411:9411 openzipkin/zipkin

```
## simple-api(branch: dapr-tracing-zipkin-k8s)
### config
- components는 필요 없다 
- config 설정으로만 가능하다 
- dapr-config >  zipkin-config.yaml

`zipkin-config.yaml`
```
apiVersion: dapr.io/v1alpha1
kind: Configuration
metadata:
  name: tracing
  namespace: api
spec:
  tracing:
    samplingRate: "1"
    zipkin:
      endpointAddress: "http://host.docker.internal:9411/api/v2/spans"
  
```

### deployment
```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: simple-api-deploy
  namespace: api    
  
  labels:
    app: simple-api
    
   
spec:
  replicas: 2
  selector:
    matchLabels:
      app: simple-api
  template:
    metadata:
      labels:
        app: simple-api
      annotations:
        dapr.io/enabled: "true"
        dapr.io/app-id: "simple-api"
        dapr.io/app-port: "8080"
        dapr.io/config: "tracing"
        
    spec:
      containers:
        - name: simple-api
          image: simple-api
          ports:
          - name: http
            containerPort: 8080
```
- dapr.io/config: "tracing" : config의 name을 적는다 



### zipkin 화면 
- http://localhost:9411




## dashboard 
```
//default  (port: 8080 )
dapr dashboard   

// port는 자유롭게 지정가능
dapr dashboard -p 9991
```