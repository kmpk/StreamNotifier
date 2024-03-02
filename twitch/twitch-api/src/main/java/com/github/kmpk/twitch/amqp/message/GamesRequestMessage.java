package com.github.kmpk.twitch.amqp.message;

import java.util.List;

public record GamesRequestMessage(List<String> ids, List<String> names) {
}
