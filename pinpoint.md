

java -javaagent:/Users/blackstar/dev/workspace/vscode/tools-work/pinpoint/agent/pinpoint-agent/pinpoint-bootstrap-2.3.3.jar -Dpinpoint.profiler.profiles.active=dev -Dpinpoint.agentId=springboot.service01 -Dpinpoint.applicationName=simple-api -jar -Dprofiler.transport.grpc.collector.ip=3.39.19.175 target/simple-api-0.0.1-SNAPSHOT.jar


java -javaagent:/Users/blackstar/dev/workspace/vscode/tools-work/pinpoint/agent/pinpoint-agent/pinpoint-agent-2.4.0/pinpoint-bootstrap-2.4.0.jar  -Dpinpoint.profiler.profiles.active=release -Dpinpoint.agentId=springboot.service01 -Dpinpoint.applicationName=simple-api  -Dprofiler.transport.grpc.collector.ip=3.39.19.175 -jar  target/simple-api-0.0.1-SNAPSHOT.jar

