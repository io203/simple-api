package com.example.simpleapi.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import com.example.simpleapi.config.AppKafkaProperties.Backoff;
import com.example.simpleapi.config.AppKafkaProperties.DeadLetter;
import com.example.simpleapi.config.AppKafkaProperties.Topic;

@ConfigurationProperties(prefix = "app.kafka")
@Validated
public record AppKafkaProperties(Topic topic, DeadLetter deadletter, Backoff backoff) {
	public record Topic(
			String name,
			String retention) {
	}

	public record DeadLetter(
			Duration retention,
			String suffix) {
	}

	public record Backoff(
			long initialInterval,
			long maxInterval) {
	}
}

// @NotNull @Valid DeadLetter deadletter,
// @NotNull @Valid Backoff backoff) {
// }

// record Topic(
// @NotNull String name,
// @NotNull String retention) {
// }

// record DeadLetter(
// @NotNull Duration retention,
// @Nullable String suffix) {
// }

// record Backoff(
// @NotNull long initialInterval,
// @NotNull long maxInterval) {
// }
