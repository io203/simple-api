# simple-api (grpc-tls)

```
openssl req -x509 -nodes -subj "//CN=localhost" -newkey rsa:4096 -sha256 -keyout server.key -out server.crt -days 3650

openssl req -x509 -nodes \
-subj "/C=KR/ST=Seoul/L=Seoul/O=osc/CN=localhost/emailAddress=info@example.com" \
-newkey rsa:4096 -sha256 -keyout server.key -out server.crt -days 3650
```
