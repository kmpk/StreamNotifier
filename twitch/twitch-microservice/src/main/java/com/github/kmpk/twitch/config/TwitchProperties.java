package com.github.kmpk.twitch.config;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Slf4j
@ConfigurationProperties(prefix = "notifier.twitch", ignoreUnknownFields = false)
public class TwitchProperties {
    @NotEmpty
    private String id;
    @NotEmpty
    private String secret;
}
