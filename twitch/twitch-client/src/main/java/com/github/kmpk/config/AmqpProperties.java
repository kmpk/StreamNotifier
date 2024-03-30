package com.github.kmpk.config;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "notifier.amqp", ignoreUnknownFields = false)
public class AmqpProperties {
    @NotEmpty
    private String usersQueue;
    @NotEmpty
    private String gamesQueue;
    @NotEmpty
    private String exchange;
}
