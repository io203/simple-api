package com.example.simpleapi.config;

import java.util.concurrent.TimeUnit;

import com.example.simpleapi.interceptor.SimpleServerInterceptor;
import com.example.simpleapi.service.SimpleService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.grpc.ServerInterceptors;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;

@Configuration
public class GrpcServerConfig {
    @Bean
    public GrpcServerConfigurer serverConfigurer() {
        return serverBuilder -> {
            if (serverBuilder instanceof NettyServerBuilder) {
                ((NettyServerBuilder) serverBuilder)
                        .keepAliveTime(30, TimeUnit.SECONDS)
                        .keepAliveTimeout(5, TimeUnit.SECONDS)
                        .permitKeepAliveWithoutCalls(true);
            }
            serverBuilder.addService(ServerInterceptors.intercept(new SimpleService(), new SimpleServerInterceptor()));
        };
    }

}
