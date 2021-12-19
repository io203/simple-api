## local

### consul 시작
```
consul agent -dev

```
- local일경우  consul agent -server는 정상적으로 되지 않는다 

## ui 
- http://127.0.0.1:8500/


## spring-config로 사용 
- consul ui에서 key/Value를 메뉴 선택 
- config 디렉토리 생성 
```
config/
```
  -  consul의 key/Value에 디렉토리 생성시 `/`를 붙혀주어야 디렉토리가 되면 붙혀주지 않으면 파일이 생성된다 
- config/demo-api 디렉토리를 만들고 환경별 profile을 만들기 위해서 다음과 같이 하면 된다 
  ```
  demo-api,dev
  ```
  하지만 다음과 같이 커스터마이징도 가능하다 
  ```
  demo-api-dev
  ```
  - demo-api-dev로 하자
- demo-api-dev 디렉토리에 application-config를 만든다 
### config > demo-api-dev > application-config
```
server:
  port: 9001

```

## spring boot 설정 
### pom.xml
```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.5.5</version>
    <relativePath/> 
</parent>


<properties>
    <java.version>11</java.version>
    <spring-cloud.version>2020.0.4</spring-cloud.version>
</properties>


<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-discovery</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-config</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
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
- 현재 spring-cloud.version 2020.0.4에서는 호환 spring boot version 2.5.x 이다 

### application.yml (consul-demo-api)
- consul을 위한 설정파일이 필요하며 2.4버전 이전 버전은 application.yml 이전 로딩하기 위해서  bootstrap.yml에 설정하였다 
- springboot 2.4버전 이후부터 bootstrap.yml은 사용하지 않는다 
- 따라서 다음과 같이 설정하면 된다 spring.config.import: "optional:consul:"
```
spring:
  profiles:
    active: local 
  application:
    name: simple-api # apply name
  
  config:
    import: "optional:consul:"
     
  cloud:
    consul:
     
      host: localhost
      port: 8500
     
      config:        
        enabled: true        
        prefixes: config        
        default-context: ${spring.application.name}
        profile-separator: ','       
        format: YAML
        data-key: application-config
        watch:        
          enabled: true        
          delay: 1000
     
      discovery:
        register: true                                
        instance-id: ${spring.application.name}-${random.uuid}   
        service-name: web:${spring.application.name}           
        prefer-ip-address: true                      
        ip-address: ${spring.cloud.client.ip-address}
        

        #### healthCheckPath를 설정하지 않으면 spring-actuator 를 활용한다 (spring-actuator 설치 되어 있어야 한다)        
        # healthCheckPath: /sys/healthz       

        register-health-check: true
        health-check-critical-timeout: "1m"

```

