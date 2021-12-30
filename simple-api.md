## State

## 1. Redis 기동 

## simple-api(brach: dapr-state)
### components
- components > state> statestore.yaml 

`statestore.yaml`
```
apiVersion: dapr.io/v1alpha1
kind: Component
metadata:
  name: my-statestore
spec:
  type: state.redis
  version: v1
  metadata:
  - name: redisHost
    value: localhost:6379
  - name: redisPassword
    value: ""
  - name: actorStateStore
    value: "true"

```

### dapr 기동 
```
dapr run --dapr-http-port 3500 --dapr-grpc-port 6000 --app-id simple-api  --components-path ./components/state mvn spring-boot:run
```

### put/ get
```

//put state
curl -X POST -H "Content-Type: application/json" -d '[{ "key": "hello", "value": "hello redis world"}]' http://localhost:3500/v1.0/state/my-statestore

// iRedis에서 key에 `simple-api/hello`키 생성되었는지 확인한다 

//Get state
curl http://localhost:3500/v1.0/state/my-statestore/hello
```

