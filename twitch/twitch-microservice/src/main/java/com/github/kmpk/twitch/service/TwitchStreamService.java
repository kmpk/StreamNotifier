package com.github.kmpk.twitch.service;

import com.github.kmpk.twitch.to.TwitchLiveStreamTo;

import java.util.Collection;
import java.util.List;

public interface TwitchStreamService {

    void addUserIds(Collection<String> ids);

    void cleanUserIds();

    List<TwitchLiveStreamTo> findLiveStreams();
}