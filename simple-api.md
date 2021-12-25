
pom.xml
```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.6.1</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
<properties>
    <java.version>11</java.version>
    <spring-cloud.version>2021.0.0</spring-cloud.version>
</properties>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-function-web</artifactId>
</dependency>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

```

## @Bean으로 등록 하여 사용

`SimpleApiApplication.java`
```
@Bean
public Function<Mono<String>, Mono<String>> hello() {
    return mono -> mono.map(value -> value.toUpperCase());
}

```
## class로 function 사용

`SimpleFn.java`
```
@Slf4j
public class SimpleFn implements Function<String, List<Simple>>{
    @Override
    public List<Simple> apply(String count) {
        List<Simple> list = new ArrayList<>();

        int num = Integer.parseInt(count);
		
		for(int i=0 ; i< num;i++) {
			list.add(new Simple(i+1,"test-"+i, "contents-"+i));
			log.info("for "+i);
		}
		log.info(list.toString());
		return list;
    }
    
}

```

`Version.java`
```
public class Version implements Function<String, String>{

    @Override
    public String apply(String ver) {
       
        return "=====  version "+ ver;
    }
    
}
```

## application.yml
```
spring:
  cloud:
    function:
      web:
        path: /api
      definition: hello,simple, version

      scan.packages: com.example.simpleapi.functions
```

## 호출 
```

curl -X POST -H "Content-Type: text/plain" http://localhost:9320/api/hello -d 'hello world'

### function을 multi로 호출 가능 
curl -X POST -H "Content-Type: text/plain" http://localhost:9320/api/version,hello -d 'hello world'


###
POST http://localhost:9320/api/hello
Content-Type: text/plain

hello world
###
POST http://localhost:9320/api/simpleFn
Content-Type: text/plain

9
###

###
POST http://localhost:9320/api/version
Content-Type: text/plain

v1.9


```