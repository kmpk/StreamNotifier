package com.github.kmpk.twitch;

import com.github.kmpk.twitch.to.TwitchGameTo;

import java.util.List;
import java.util.Optional;

public interface TwitchGamesResource {
    Optional<TwitchGameTo> findById(String id);

    Optional<TwitchGameTo> findByName(String name);

    List<TwitchGameTo> findByIds(List<String> ids);

    List<TwitchGameTo> findByNames(List<String> names);
}
