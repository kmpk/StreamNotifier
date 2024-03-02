package com.github.kmpk.twitch.amqp.message;

import com.github.kmpk.twitch.to.TwitchUserTo;

import java.util.List;

public record UsersResponseMessage(List<TwitchUserTo> users) {
}
