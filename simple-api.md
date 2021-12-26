## secretstores

## simple-api(brach: dapr-secretstores)

### mysecrets 파일 생성 
`mysecrets.json`
```
{
   "my-secret" : "id-1234356789"
}

```
### components
- components > secretstores> localSecretStore.yaml 

`localSecretStore.yaml`
```
apiVersion: dapr.io/v1alpha1
kind: Component
metadata:
  name: my-secret-store
  namespace: default
spec:
  type: secretstores.local.file
  version: v1
  metadata:
  - name: secretsFile
    value: /Users/blackstar/dev/data/dapr/myscrets/mysecrets.json
  - name: nestedSeparator
    value: ":"

```

### Run the Dapr sidecar
```
dapr run --dapr-http-port 3500 --dapr-grpc-port 6000 --app-id simple-api  --components-path ./components/secretstores mvn spring-boot:run
```

### Get a secret
```
curl http://localhost:3500/v1.0/secrets/my-secret-store/my-secret

```

## dashboard 
```
//default  (port: 8080 )
dapr dashboard   

// port는 자유롭게 지정가능
dapr dashboard -p 9991
```