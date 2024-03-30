package com.github.kmpk.twitch.client;

import com.github.kmpk.twitch.to.TwitchGameTo;
import com.github.kmpk.twitch.to.TwitchLiveStreamTo;
import com.github.kmpk.twitch.to.TwitchUserTo;

import java.util.List;

public interface TwitchClient {
    List<TwitchGameTo> findGamesByName(List<String> names);

    List<TwitchGameTo> findGamesById(List<String> ids);

    List<TwitchUserTo> findUsersByLogin(List<String> logins);

    List<TwitchUserTo> findUsersById(List<String> ids);

    List<TwitchLiveStreamTo> findLiveStreamsByUserIds(List<String> ids);
}
