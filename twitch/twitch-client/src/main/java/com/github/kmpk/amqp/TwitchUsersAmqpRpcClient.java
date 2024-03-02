package com.github.kmpk.amqp;

import com.github.kmpk.config.AmqpProperties;
import com.github.kmpk.twitch.TwitchUsersResource;
import com.github.kmpk.twitch.amqp.message.UsersRequestMessage;
import com.github.kmpk.twitch.amqp.message.UsersResponseMessage;
import com.github.kmpk.twitch.to.TwitchUserTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class TwitchUsersAmqpRpcClient implements TwitchUsersResource {
    private final AmqpProperties properties;

    private final RabbitTemplate rabbitTemplate; //TODO: timeout

    @Override
    public Optional<TwitchUserTo> findById(String id) {
        UsersRequestMessage message = new UsersRequestMessage(List.of(id), Collections.emptyList());
        return requestSingle(message);
    }

    @Override
    public Optional<TwitchUserTo> findByLogin(String login) {
        UsersRequestMessage message = new UsersRequestMessage(Collections.emptyList(), List.of(login));
        return requestSingle(message);
    }

    private Optional<TwitchUserTo> requestSingle(UsersRequestMessage message) {
        try {
            UsersResponseMessage response = rabbitTemplate.convertSendAndReceiveAsType(properties.getExchange(),
                    properties.getGamesQueue(), message, ParameterizedTypeReference.forType(UsersResponseMessage.class));
            if (response != null && !response.users().isEmpty()) {
                return Optional.of(response.users().getFirst());
            }
        } catch (AmqpException e) {
            log.error("", e);
        }
        return Optional.empty();
    }

    @Override
    public List<TwitchUserTo> findByIds(List<String> ids) {
        UsersRequestMessage message = new UsersRequestMessage(ids, Collections.emptyList());
        return request(message);
    }

    @Override
    public List<TwitchUserTo> findByLogins(List<String> logins) {
        UsersRequestMessage message = new UsersRequestMessage(Collections.emptyList(), logins);
        return request(message);
    }

    private List<TwitchUserTo> request(UsersRequestMessage message) {
        try {
            UsersResponseMessage response = rabbitTemplate.convertSendAndReceiveAsType(properties.getExchange(),
                    properties.getGamesQueue(), message, ParameterizedTypeReference.forType(UsersResponseMessage.class));
            if (response != null && !response.users().isEmpty()) {
                return response.users();
            }
        } catch (AmqpException e) {
            log.error("", e);
        }
        return Collections.emptyList();
    }
}
