package com.github.kmpk.twitch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AmqpConfig {
    private final AmqpProperties properties;

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(properties.getExchange());
    }

    @Bean
    public Binding gamesBinding() {
        return BindingBuilder.bind(gamesQueue()).to(exchange()).with(properties.getGamesQueue());
    }

    @Bean
    public Queue gamesQueue() {
        return new Queue(properties.getGamesQueue());
    }

    @Bean
    public Binding usersBinding() {
        return BindingBuilder.bind(usersQueue()).to(exchange()).with(properties.getUsersQueue());
    }

    @Bean
    public Queue usersQueue() {
        return new Queue(properties.getUsersQueue());
    }
}
