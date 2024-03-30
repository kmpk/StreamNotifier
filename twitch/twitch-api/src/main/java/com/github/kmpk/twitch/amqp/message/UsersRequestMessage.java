package com.github.kmpk.twitch.amqp.message;

import java.util.List;

public record UsersRequestMessage(List<String> ids, List<String> logins) {
}
