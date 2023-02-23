# boot3-observability

## actuator + prometheus 
pom.xml
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>


<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>		
    <scope>runtime</scope>
</dependency>

```

## application.yml
```
management: 
  endpoints:
    web.exposure.include: prometheus # only exposure /actuator/prometheus endpoint
  metrics:
    tags:
      application: ${spring.application.name} #  필히 해주는 것이 좋다

```

## docker-compose.yaml
```
version: "3.3" 
networks:
  default:
    name: operations-dc

services:    
    prometheus:
        image: prom/prometheus       
        command:
            - --enable-feature=exemplar-storage
            - --config.file=/etc/prometheus/prometheus.yml
        volumes:
            - ./docker/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml:ro
        ports:
            - "9090:9090"

    grafana:
        image: grafana/grafana    
        volumes:
          - ./docker/grafana/provisioning/datasources:/etc/grafana/provisioning/datasources:ro
          - ./docker/grafana/provisioning/dashboards:/etc/grafana/provisioning/dashboards:ro        
        ports:
            - "3000:3000"
# Prometheus: http://localhost:9090/
# Grafana: http://localhost:3000/

```
## prometheus

prometheus.yml
```
global:
    scrape_interval: 2s
    evaluation_interval: 2s

scrape_configs:
    - job_name: 'prometheus'
      static_configs:
          - targets: ['host.docker.internal:9090']
          
    - job_name: 'simple-api'
      metrics_path: '/actuator/prometheus'
      static_configs:
        - targets: ['host.docker.internal:9320']

```

## grafana
- 초기: admin/admin 
- datasource: http://host.docker.internal:9090
## spring boot dashboard
- https://grafana.com/grafana/dashboards/
- JVM (Micrometer)
    - dashboard id: 4701

### dashboard.yml
```
apiVersion: 1
providers:
    - name: dashboards
      type: file
      disableDeletion: false
      editable: true
      options:
          path: /etc/grafana/provisioning/dashboards
          foldersFromFilesStructure: true
```
### JVM (Micrometer)
- jvm-micrometer_rev9.json

### datasource.yml
```
apiVersion: 1
datasources:
    - name: Prometheus
      type: prometheus
      access: proxy
      url: http://host.docker.internal:9090

```


# loki
