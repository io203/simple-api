./mvnw spring-boot:build-image -DskipTests -Dspring-boot.build-image.imageName=<이미지명>:<이미지버전>
g
DOCKER_HOST=tcp://127.0.0.1:2375
export DOCKER_HOST=tcp://http.docker.internal:3128
export DOCKER_HOST=http.docker.internal:3128
http.docker.internal:3128


export DOCKER_HOST=unix:///var/run/docker.sock
DOCKER_HOST=tcp://127.0.0.1:2376

DOCKER_HOST=tcp://0.0.0.0:2375
DOCKER_HOST=tcp://host.docker.internal:2375
