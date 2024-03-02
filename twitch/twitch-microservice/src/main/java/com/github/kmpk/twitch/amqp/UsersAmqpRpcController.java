package com.github.kmpk.twitch.amqp;

import com.github.kmpk.twitch.amqp.message.UsersRequestMessage;
import com.github.kmpk.twitch.amqp.message.UsersResponseMessage;
import com.github.kmpk.twitch.service.TwitchUserService;
import com.github.kmpk.twitch.to.TwitchUserTo;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UsersAmqpRpcController {
    private final TwitchUserService service;

    @RabbitListener(queues = "${notifier.amqp.users-queue}")
    public UsersResponseMessage process(@Payload UsersRequestMessage message) {
        List<TwitchUserTo> result = new ArrayList<>();
        if (!message.ids().isEmpty()) {
            result.addAll(service.getByIds(message.ids()));
        }
        if (!message.logins().isEmpty()) {
            result.addAll(service.getByLogins(message.logins()));
        }
        return new UsersResponseMessage(result);
    }
}
