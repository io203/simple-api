
k create ns api

istioctl kube-inject -f simple-api.yaml | kubectl apply -f -

kaf ingress-simple-api.yaml 

curl dev.simple-api.192.168.41.10.sslip.io/api/simple

kdelf simple-api.yaml

kaf simple-api.yaml
istioctl kube-inject -f simple-api.yaml | kubectl apply -f -
curl dev.simple-api.192.168.41.10.sslip.io/api/simple

curl simple-api-svc.api:8080/api/simple