package com.github.kmpk.twitch;

import com.github.kmpk.twitch.to.TwitchUserTo;

import java.util.List;
import java.util.Optional;

public interface TwitchUsersResource {
    Optional<TwitchUserTo> findById(String id);

    Optional<TwitchUserTo> findByLogin(String login);

    List<TwitchUserTo> findByIds(List<String> ids);

    List<TwitchUserTo> findByLogins(List<String> logins);
}
