package com.github.kmpk.twitch.service;

import com.github.kmpk.twitch.to.TwitchUserTo;

import java.util.List;

public interface TwitchUserService {
    List<TwitchUserTo> getByLogins(List<String> names);

    List<TwitchUserTo> getByIds(List<String> ids);

    void update();
}