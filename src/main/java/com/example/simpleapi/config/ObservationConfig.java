package com.example.simpleapi.config;

import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.ObservationTextPublisher;

@Configuration
public class ObservationConfig {
    @Bean
	public ObservationTextPublisher printingObservationHandler() {
		return new ObservationTextPublisher();
	}
    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
    return registry -> registry.config().commonTags("application", "simple-api");
    }

    
}
