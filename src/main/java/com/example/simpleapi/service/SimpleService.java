package com.example.simpleapi.service;

 

import java.util.ArrayList;
import java.util.List;

import com.example.simple.proto.HelloRequest;
import com.example.simple.proto.HelloResponse;
import com.example.simple.proto.HelloServiceGrpc.HelloServiceImplBase;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@Slf4j
public class SimpleService extends HelloServiceImplBase {
    
    // unary
  @Override
  public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {

    String greeting = request.getFirstName() + "," + request.getLastName();

    HelloResponse response = HelloResponse.newBuilder()
        .setGreeting(greeting)
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  // server stream
  @Override
  public void helloServerStream(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {

    List<String> greetingList = new ArrayList<>();
    for (int i = 1; i <= 3; i++) {
      greetingList.add(request.getFirstName() + "," + request.getLastName() + ":" + i);
    }

    for (String greeting : greetingList) {
      HelloResponse response = HelloResponse.newBuilder()
          .setGreeting(greeting)
          .build();
      responseObserver.onNext(response);
    }

    responseObserver.onCompleted();
  }

  // client stream
  @Override
  public StreamObserver<HelloRequest> helloClientStream( StreamObserver<HelloResponse> responseObserver) {
    return new StreamObserver<HelloRequest>() {
        @Override
        public void onNext(HelloRequest helloRequest) {
            System.out.println(helloRequest.getFirstName() + "," + helloRequest.getLastName());
            // 주의 : 여기서 responseObserver.onNext를 쓰면 biStream이 되버림
        }

        @Override
        public void onError(Throwable throwable) {
            System.out.println("error");
        }

        @Override
        public void onCompleted() {
            responseObserver.onNext(HelloResponse.newBuilder().setGreeting("success").build());
            responseObserver.onCompleted();
        }
    };
  }

  // bi stream
  @Override
  public StreamObserver<HelloRequest> helloBiStream( StreamObserver<HelloResponse> responseObserver) {
    return new StreamObserver<HelloRequest>() {
        @Override
        public void onNext(HelloRequest helloRequest) {
            String greeting = helloRequest.getFirstName() + "," + helloRequest.getLastName();
            System.out.println(greeting);

            responseObserver.onNext(HelloResponse.newBuilder().setGreeting(greeting+"1").build());
            responseObserver.onNext(HelloResponse.newBuilder().setGreeting(greeting+"2").build());
        }

        @Override
        public void onError(Throwable throwable) {
            System.out.println("error");
        }

        @Override
        public void onCompleted() {
            responseObserver.onCompleted();
        }
    };
  }
}