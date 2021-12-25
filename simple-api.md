## binding

## 1. kafka 기동 
 - cd /Users/blackstar/dev/workspace/vscode/tools-work/docker
```
docker-compose up broker
```
## simple-api(brach: dapr-binding)
### components
binding> binding.yaml
```
apiVersion: dapr.io/v1alpha1
kind: Component
metadata:
  name: simple
spec:
  type: bindings.kafka
  version: v1
  metadata:
  # Kafka broker connection setting
  - name: brokers
    value: localhost:29092
  # consumer configuration: topic and consumer group
  - name: topics
    value: simple
  - name: consumerGroup
    value: group1
  # route 재정의 
  - name: route
    value: /onevent
  # publisher configuration: topic
  - name: publishTopic
    value: simple
  - name: authRequired
    value: "false"
 

 
  
```

### BindingController.java
```
@RestController
@Slf4j
public class BindingController {
    @PostMapping("/onevent")
	public Mono<String> onevent(@RequestBody(required = false) byte[] body) {
        return Mono.fromRunnable(() ->
                log.info("Received Message: " + new String(body)));
    }    
}
```
- simple-api 기동 (9320)
```
dapr run --dapr-http-port 4320  --app-id simple-api --app-port 9320 --components-path ./components/binding
```

### kafka topic 확인 / 접속
```
//확인 
kcat -b localhost:29092 -L 

```

### 메세지 전송 

`kcat 전송`

```
kcat -P -b localhost:29092 -t simple
```
`dapr 전송` 
```
dapr run --app-id simple-publisher  --dapr-http-port 3500 --components-path ./components/binding


curl -X POST http://localhost:3500/v1.0/bindings/simple-api \
  -H "Content-Type: application/json" \
  -d '{
        "data": {
          "message": "Hi2"
        },
        "metadata": {
          "key": "key-1"
        },
        "operation": "create"
      }'
```


## dashboard 
```
//default  (port: 8080 )
dapr dashboard   

// port는 자유롭게 지정가능
dapr dashboard -p 9991
```