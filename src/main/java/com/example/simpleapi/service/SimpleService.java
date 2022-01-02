package com.example.simpleapi.service;


import com.example.simple.model.HelloReply;
import com.example.simple.model.HelloRequest;
import com.example.simple.model.SimpleApiGrpc.SimpleApiImplBase;
import com.example.simple.model.SimpleReply;
import com.example.simple.model.SimpleRequest;
import com.example.simple.model.VersionReply;
import com.example.simple.model.VersionRequest;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@Slf4j
public class SimpleService extends SimpleApiImplBase {
    
    @Override
    public void hello(HelloRequest req, StreamObserver<HelloReply> resObserver){
        HelloReply helloReply = HelloReply.newBuilder().setHello("Hello world").build();
        resObserver.onNext(helloReply);
        resObserver.onCompleted();
    }

    @Override
    public void version(VersionRequest req, StreamObserver<VersionReply> resObserver){
        VersionReply versionReply = VersionReply.newBuilder().setVersion("v-1.0").build();
        resObserver.onNext(versionReply);
        resObserver.onCompleted();
    }

    @Override
    public void simple(SimpleRequest req, StreamObserver<SimpleReply> resObserver){
        log.info("=====simple");
        for(int i=0; i< 10 ;i++){            
            resObserver.onNext(
                SimpleReply.newBuilder()
                    .setNum(i+1)
                    .setTitle("title-"+(i+1))
                    .setContent("content-"+(i+1))
                    .build()
            );            
        }
        resObserver.onCompleted();
        log.info("=====simple end");
    }
}
