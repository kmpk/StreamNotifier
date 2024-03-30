package com.github.kmpk.twitch.service;

import com.github.kmpk.twitch.to.TwitchGameTo;

import java.util.List;

public interface TwitchGameService {
    List<TwitchGameTo> getByNames(List<String> names);

    List<TwitchGameTo> getByIds(List<String> ids);

    void update();
}