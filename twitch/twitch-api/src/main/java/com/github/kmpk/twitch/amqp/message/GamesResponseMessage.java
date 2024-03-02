package com.github.kmpk.twitch.amqp.message;

import com.github.kmpk.twitch.to.TwitchGameTo;

import java.util.List;

public record GamesResponseMessage(List<TwitchGameTo> games) {
}
