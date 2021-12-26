## binding

## 1. kafka 기동 
 - cd /Users/blackstar/dev/workspace/vscode/tools-work/docker
```
docker-compose up broker
```
## simple-api(brach: dapr-binding)
### components
### components
components> binding> kafka> `kafka.yaml`
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
components> binding>rabbitmq>`rabbitmq.yaml`
```
apiVersion: dapr.io/v1alpha1
kind: Component
metadata:
  name: my-simple   
spec:
  type: bindings.rabbitmq
  version: v1
  metadata:
  - name: queueName
    value: simple
  - name: host
    value: amqp://admin:1234@localhost:5672
  - name: durable
    value: true
  - name: deleteWhenUnused
    value: false
  - name: ttlInSeconds
    value: 60
  - name: prefetchCount
    value: 0
  - name: exclusive
    value: false
  - name: maxPriority
    value: 5
  - name: contentType
    value: "text/plain"
  # route 재정의 
  - name: route
    value: /onevent

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
### kafka binding
```
dapr run --dapr-http-port 4320  --app-id simple-api --app-port 9320 --components-path ./components/binding/kafka mvn spring-boot:run
```
### Rabbitmq binding
```
dapr run --dapr-http-port 4320  --app-id simple-api --app-port 9320 --components-path ./components/binding/rabbitmq mvn spring-boot:run
```

##### rabbitmq admin
- http://localhost:8901

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
dapr run --app-id simple-publisher  --dapr-http-port 3500 --components-path ./components/binding/rabbitmq

curl -X POST http://localhost:3500/v1.0/bindings/simple \
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