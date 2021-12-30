## State(dapr-state-k8s)

## 1. Redis 기동 
- host에서 기동 시킨다 
## simple-api(brach: dapr-state-k8s)
### components
- components > state> statestore.yaml 

`statestore.yaml`
```
apiVersion: dapr.io/v1alpha1
kind: Component
metadata:
  name: my-statestore
  namespace: api
spec:
  type: state.redis
  version: v1
  metadata:
  - name: redisHost
    value: host.docker.internal:6379
  - name: redisPassword
    value: ""
  - name: actorStateStore
    value: "true"

```

### k8s에 생성 (namspace:api)
```
kubectl apply -f statestore.yaml 
``` 

## simple-api.yaml
```
apiVersion: v1
kind: Service
metadata:
  name: simple-api-svc
  namespace: api  
spec:
  selector:
    app: simple-api  
  ports:
  - protocol: TCP
    port: 8080 
    targetPort: http
  #   nodePort: 32180
  # type: NodePort
---
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
    spec:
      containers:
        - name: simple-api
          image: simple-api
          ports:
          - name: http
            containerPort: 8080
```
## application.yml
```
server:
  port: 8080

dapr:
  state:
    url: http://localhost:3500/v1.0/state/my-statestore
```

## SimpleService.java
```
@Service
public class SimpleService {
	private  WebClient client;	
	private String stateUrl;

	public WebClient getWebClient(){
		return WebClient
			.builder()
			.baseUrl(this.stateUrl)
			// .defaultCookie("쿠키키","쿠키값")
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			// .defaultHeader("apiKey", apiKey)
			//Memory 조정: 2M (default 256KB)
			.exchangeStrategies(ExchangeStrategies.builder()
            	.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2*1024*1024)) 
            	.build())
			.build();
	}
	
	public SimpleService(@Value("${dapr.state.url}") String stateUrl) {
		this.stateUrl = stateUrl;	
		this.client = getWebClient();
	}

    public String redisHello(String key) {
		
		return client
				.get()
				.uri("/"+key)
                .retrieve()
				.bodyToMono(String.class).block();
	}

	public void  insertState(String data)  {		 
		 client
				.post()
				.body(Mono.just(data), String.class)			
				.exchangeToMono(s -> s.toBodilessEntity())
				.block();
	}
	

}

```
## StateController.java
```

@RestController
@Slf4j
@RequiredArgsConstructor
public class StateController {
	private final SimpleService service;



	@GetMapping("/redis/{key}")
	public String redis(@PathVariable String key){

		return service.redisHello(key);
	}

	@PostMapping("redis")
	public String add(@RequestBody String data){

		service.insertState(data);
		return "정상 저장";
	}
}
```
### k8s 기동 
```
skaffold dev -p kind -t RB0.0.3

```

### put/ get
```
GET http://localhost:9320/redis/hello4

###
POST http://localhost:9320/redis
Content-Type: application/json

[
    { "key": "hello4",
        "value": "hello redis world4"
    }
]

```

