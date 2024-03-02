package com.github.kmpk.amqp;

import com.github.kmpk.config.AmqpProperties;
import com.github.kmpk.twitch.TwitchGamesResource;
import com.github.kmpk.twitch.amqp.message.GamesRequestMessage;
import com.github.kmpk.twitch.amqp.message.GamesResponseMessage;
import com.github.kmpk.twitch.to.TwitchGameTo;
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
public class TwitchGamesAmqpRpcClient implements TwitchGamesResource {
    private final AmqpProperties properties;

    private final RabbitTemplate rabbitTemplate; //TODO: timeout

    @Override
    public Optional<TwitchGameTo> findById(String id) {
        GamesRequestMessage message = new GamesRequestMessage(List.of(id), Collections.emptyList());
        return requestSingle(message);
    }

    @Override
    public Optional<TwitchGameTo> findByName(String name) {
        GamesRequestMessage message = new GamesRequestMessage(Collections.emptyList(), List.of(name));
        return requestSingle(message);
    }

    private Optional<TwitchGameTo> requestSingle(GamesRequestMessage message) {
        try {
            GamesResponseMessage response = rabbitTemplate.convertSendAndReceiveAsType(properties.getExchange(),
                    properties.getGamesQueue(), message, ParameterizedTypeReference.forType(GamesResponseMessage.class));
            if (response != null && !response.games().isEmpty()) {
                return Optional.of(response.games().getFirst());
            }
        } catch (AmqpException e) {
            log.error("", e);
        }
        return Optional.empty();
    }

    @Override
    public List<TwitchGameTo> findByIds(List<String> ids) {
        GamesRequestMessage message = new GamesRequestMessage(ids, Collections.emptyList());
        return request(message);
    }

    @Override
    public List<TwitchGameTo> findByNames(List<String> names) {
        GamesRequestMessage message = new GamesRequestMessage(Collections.emptyList(), names);
        return request(message);
    }

    private List<TwitchGameTo> request(GamesRequestMessage message) {
        try {
            GamesResponseMessage response = rabbitTemplate.convertSendAndReceiveAsType(properties.getExchange(),
                    properties.getGamesQueue(), message, ParameterizedTypeReference.forType(GamesResponseMessage.class));
            if (response != null && !response.games().isEmpty()) {
                return response.games();
            }
        } catch (AmqpException e) {
            log.error("", e);
        }
        return Collections.emptyList();
    }
}
