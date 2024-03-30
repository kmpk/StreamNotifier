package com.github.kmpk.twitch.service;

import com.github.kmpk.twitch.client.TwitchClient;
import com.github.kmpk.twitch.to.TwitchLiveStreamTo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class TwitchStreamServiceImpl implements TwitchStreamService {
    private final TwitchClient twitch;
    private final Map<String, Instant> usersToCheck = new ConcurrentHashMap<>();

    @Override
    public void addUserIds(Collection<String> ids) {
        ids.forEach(id -> usersToCheck.put(id, Instant.now()));
    }

    @Override
    public void cleanUserIds() {
        usersToCheck.clear();
    }

    @Override
    public List<TwitchLiveStreamTo> findLiveStreams() {
        removeOldUsers();
        List<String> users = new ArrayList<>(usersToCheck.keySet());
        return twitch.findLiveStreamsByUserIds(users);
    }

    private void removeOldUsers() {
        Instant hourAgo = Instant.now().minus(1, ChronoUnit.HOURS);
        usersToCheck.entrySet().removeIf(e -> e.getValue().isBefore(hourAgo));
    }
}
