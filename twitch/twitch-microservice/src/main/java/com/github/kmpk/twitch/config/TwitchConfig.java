package com.github.kmpk.twitch.config;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.common.util.ThreadUtils;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@EnableScheduling
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@Configuration
@Profile("!test")
public class TwitchConfig {
    private static final int REQUEST_QUEUE_SIZE = 500;
    private final OAuth2Credential credential;
    private final ScheduledThreadPoolExecutor executor;
    private final TwitchProperties twitchProperties;

    public TwitchConfig(TwitchProperties twitchProperties) {
        this.twitchProperties = twitchProperties;
        this.credential = new TwitchIdentityProvider(twitchProperties.getId(), twitchProperties.getSecret(), null)
                .getAppAccessToken();
        this.executor = ThreadUtils.getDefaultScheduledThreadPoolExecutor("TwitchScheduled-", 1);
        executor.scheduleAtFixedRate(
                () -> credential.updateCredential(credential),
                6L,
                6L,
                TimeUnit.HOURS
        );
    }

    @Bean
    public TwitchHelix getTwitchHelix() {
        return TwitchHelixBuilder.builder()
                .withClientId(twitchProperties.getId())
                .withClientSecret(twitchProperties.getSecret())
                .withDefaultAuthToken(credential)
                .withScheduledThreadPoolExecutor(executor)
                .withRequestQueueSize(REQUEST_QUEUE_SIZE)
                .build();
    }
}
