package com.github.kmpk.twitch;

import com.github.kmpk.twitch.to.TwitchGameTo;
import com.github.kmpk.twitch.to.TwitchLiveStreamTo;
import com.github.kmpk.twitch.to.TwitchUserTo;

import java.time.Instant;

public class TestData {
    public static final TwitchLiveStreamTo NEW_STREAM_TO = new TwitchLiveStreamTo("newId", "newLogin", "newUser", "newId", "newGame", Instant.now(), "newTitle", "url");

    public static final TwitchUserTo EXISTED_USER_TO = new TwitchUserTo("existedId", "existedLogin", "existedName", "existedAvatarUrl");
    public static final TwitchUserTo EXISTED_OLD_USER_TO = new TwitchUserTo("existedOldId", "existedOldLogin", "existedOldName", "existedOldAvatarUrl");
    public static final TwitchUserTo NEW_USER_TO = new TwitchUserTo("newId", "newLogin", "newName", "newAvatarUrl");

    public static final TwitchGameTo EXISTED_GAME_TO = new TwitchGameTo("existedId", "existedName", "existedUrl");
    public static final TwitchGameTo EXISTED_OLD_GAME_TO = new TwitchGameTo("existedOldId", "existedOldName", "existedOldUrl");
    public static final TwitchGameTo NEW_GAME_TO = new TwitchGameTo("newId", "newName", "newUrl");
}
