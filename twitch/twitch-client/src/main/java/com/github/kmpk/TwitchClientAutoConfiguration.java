package com.github.kmpk;

import com.github.kmpk.amqp.TwitchGamesAmqpRpcClient;
import com.github.kmpk.amqp.TwitchUsersAmqpRpcClient;
import com.github.kmpk.config.AmqpProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AmqpProperties.class)
@RequiredArgsConstructor
public class TwitchClientAutoConfiguration {
    private final AmqpProperties properties;

    @Bean
    public TwitchGamesAmqpRpcClient twitchGamesClient(RabbitTemplate rabbitTemplate) {
        return new TwitchGamesAmqpRpcClient(properties, rabbitTemplate);
    }

    @Bean
    public TwitchUsersAmqpRpcClient twitchUsersClient(RabbitTemplate rabbitTemplate) {
        return new TwitchUsersAmqpRpcClient(properties, rabbitTemplate);
    }

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

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}
