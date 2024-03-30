package com.github.kmpk.twitch.amqp;

import com.github.kmpk.twitch.amqp.message.GamesRequestMessage;
import com.github.kmpk.twitch.amqp.message.GamesResponseMessage;
import com.github.kmpk.twitch.service.TwitchGameService;
import com.github.kmpk.twitch.to.TwitchGameTo;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GamesAmqpRpcController {
    private final TwitchGameService service;

    @RabbitListener(queues = "${notifier.amqp.games-queue}")
    public GamesResponseMessage process(@Payload GamesRequestMessage message) {
        List<TwitchGameTo> result = new ArrayList<>();
        if (!message.ids().isEmpty()) {
            result.addAll(service.getByIds(message.ids()));
        }
        if (!message.names().isEmpty()) {
            result.addAll(service.getByNames(message.names()));
        }
        return new GamesResponseMessage(result);
    }
}
