


## PubSub

## 1. Redis 기동 ( redis로 pubsub 구성시) 

##  Rabbitmq (rabbitmq로 pubsub 구성시)

docker-compose.yaml
```
version: '3.3'
services:
 rabbitmq:
    image: rabbitmq:management
    environment:
      RABBITMQ_DEFAULT_USER: "admin"
      RABBITMQ_DEFAULT_PASS: "1234"
    ports:
      - 5672:5672
      - 8901:15672  #admin port
    container_name: rabbitmq
```

## simple-api(brach: dapr-pubsub)
### components
- components > pubsub> redis>redis.yaml , subscription.yaml

`redis.yaml`
```
apiVersion: dapr.io/v1alpha1
kind: Component
metadata:
  name: my-pubsub
spec:
  type: pubsub.redis
  version: v1
  metadata:
    - name: redisHost
      value: localhost:6379
    - name: redisPassword
      value: ""
    - name: ttlInSeconds
      value: 60
```
`subscription.yaml`
```
apiVersion: dapr.io/v1alpha1
kind: Subscription
metadata:
  name: my-subscription
spec:
  topic: simple
  route: /simple-sub
  pubsubname: my-pubsub
scopes:
- simple-api
```


### PubsubController.java
```
@RestController
@Slf4j
public class PubsubController {
    @PostMapping("/simple-sub")
	public Mono<String> getSub(@RequestBody(required = false) byte[] body) {
        return Mono.fromRunnable(() ->
                log.info("Received Message: " + new String(body)));
    }
    
}
```

```
dapr run --dapr-http-port 4320  --app-id simple-api --app-port 9320 --components-path ./components/pubsub/redis mvn spring-boot:run
```
`rabbitmq`
```
dapr run --dapr-http-port 4320  --app-id simple-api --app-port 9320 --components-path ./components/pubsub/rabbitmq mvn spring-boot:run
```

### rabbitmq admin ui
- http://localhost:8901 

### topic 생성 확인 
- iRedis(redis client )로  simple topic 생성 확인한다 

### 메세지 전송 
`dapr sidecar  생성` 
```
dapr run --app-id simple-publisher  --dapr-http-port 3500 --components-path ./components/pubsub/redis
```
```
dapr run --app-id simple-publisher  --dapr-http-port 3500 --components-path ./components/pubsub/rabbitmq
```

`메세지 전송`

```
// http로 전송
curl -X POST http://localhost:3500/v1.0/publish/my-pubsub/simple -H "Content-Type: application/json" -d '{"orderId": "id-100"}'

//dapr cli로 전송
dapr publish --publish-app-id simple-publisher --pubsub my-pubsub --topic simple --data '{"orderId": "simple-900"}'

// dapr cli cloud-event format전송 
dapr publish --publish-app-id simple-publisher --pubsub my-pubsub --topic simple --data '{"specversion" : "1.0", "type" : "com.dapr.cloudevent.sent", "source" : "testcloudeventspubsub", "subject" : "Cloud Events Test", "id" : "someCloudEventId", "time" : "2021-08-02T09:00:00Z", "datacontenttype" : "application/cloudevents+json", "data" : {"orderId": "simple-------100"}}'

```


## dashboard 
```
//default  (port: 8080 )
dapr dashboard   

// port는 자유롭게 지정가능
dapr dashboard -p 9991
```
