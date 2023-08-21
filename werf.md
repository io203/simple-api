# werf

```
curl -sSL https://werf.io/install.sh | bash -s -- --version 1.2 --channel stable
```

example
```
# Clone the example repository to ~/werf-guide/guides (if you have not cloned it yet).
git clone https://github.com/werf/website 


// local build
werf build

// docker hub에 push까지 
werf build  --repo saturn203/simple-api --add-custom-tag werf-v1.0
werf converge --repo saturn203/simple-api
werf converge --use-custom-tag 'werf-v1.0' --repo saturn203/simple-api
werf render  --output manifests.yaml --repo saturn203/simple-api 
werf cleanup --repo saturn203/simple-api 

```