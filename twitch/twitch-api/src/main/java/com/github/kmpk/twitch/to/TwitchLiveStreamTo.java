package com.github.kmpk.twitch.to;

import java.time.Instant;

public record TwitchLiveStreamTo(String userId, String userLogin, String userName, String gameId, String gameName,
                                 Instant startedAt, String title, String thumbnailUrl) {
}