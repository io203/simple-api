package com.example.simpleapi.config;


import com.example.simpleapi.service.GrpcSimpleService;
import com.linecorp.armeria.client.ClientFactory;
import com.linecorp.armeria.client.HttpClient;
import com.linecorp.armeria.client.circuitbreaker.CircuitBreakerClient;
import com.linecorp.armeria.client.circuitbreaker.CircuitBreakerRule;
import com.linecorp.armeria.common.grpc.GrpcSerializationFormats;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.grpc.GrpcService;
import com.linecorp.armeria.server.logging.AccessLogWriter;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import com.linecorp.armeria.spring.web.reactive.ArmeriaClientConfigurator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ArmeriaConfiguration {
    private final GrpcSimpleService grpcSimpleService;
    // private final SimpleService simpleService;
  
    // A user can configure the server by providing an ArmeriaServerConfigurator bean.
    @Bean
    public ArmeriaServerConfigurator armeriaServerConfigurator() {
        // Customize the server using the given ServerBuilder. For example:
        return builder -> {
            // Add DocService that enables you to send Thrift and gRPC requests
            // from web browser.
            builder.serviceUnder("/docs", new DocService());

            // Log every message which the server receives and responds.
            builder.decorator(LoggingService.newDecorator());

            // Write access log after completing a request.
            builder.accessLogWriter(AccessLogWriter.combined(), false);

            // You can also bind asynchronous RPC services such as Thrift and gRPC:
            // builder.service(THttpService.of(...));
            builder.service(
                GrpcService.builder()
                .addService(grpcSimpleService)         
                .supportedSerializationFormats(GrpcSerializationFormats.values())
                .enableUnframedRequests(true)
                .build());
            
        };
    }

    @Bean
    public ClientFactory clientFactory() {
        return ClientFactory.insecure();
    }

    /**
     * A user can configure an {@link HttpClient} by providing an {@link ArmeriaClientConfigurator} bean.
     */
    @Bean
    public ArmeriaClientConfigurator armeriaClientConfigurator(ClientFactory clientFactory) {
        // Customize the client using the given WebClientBuilder. For example:
        return builder -> {
            // Use a circuit breaker for each remote host.
            final CircuitBreakerRule rule = CircuitBreakerRule.builder()
                                                              .onServerErrorStatus()
                                                              .onException()
                                                              .thenFailure();
            builder.decorator(CircuitBreakerClient.builder(rule)
                                                  .newDecorator());

            // Set a custom client factory.
            builder.factory(clientFactory);
        };
    }
}