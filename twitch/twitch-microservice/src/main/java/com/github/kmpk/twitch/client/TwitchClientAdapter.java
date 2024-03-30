package com.github.kmpk.twitch.client;

import com.github.kmpk.twitch.mapper.GameMapper;
import com.github.kmpk.twitch.mapper.StreamMapper;
import com.github.kmpk.twitch.mapper.UserMapper;
import com.github.kmpk.twitch.to.TwitchGameTo;
import com.github.kmpk.twitch.to.TwitchLiveStreamTo;
import com.github.kmpk.twitch.to.TwitchUserTo;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.domain.GameList;
import com.github.twitch4j.helix.domain.Stream;
import com.github.twitch4j.helix.domain.UserList;
import com.google.common.collect.Lists;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class TwitchClientAdapter implements TwitchClient {
    public static final int BATCH_SIZE = 99;

    private final TwitchHelix client;
    private final GameMapper gameMapper;
    private final UserMapper userMapper;
    private final StreamMapper streamMapper;

    @Override
    public List<TwitchGameTo> findGamesByName(List<String> names) {
        return Lists.partition(names, BATCH_SIZE)
                .stream()
                .map(list -> client.getGames(null, null, list, null))
                .map(this::loadAndMapGame)
                .flatMap(Collection::stream)
                .toList();
    }

    @Override
    public List<TwitchGameTo> findGamesById(List<String> ids) {
        return Lists.partition(ids, BATCH_SIZE)
                .stream()
                .map(list -> client.getGames(null, list, null, null))
                .map(this::loadAndMapGame)
                .flatMap(Collection::stream)
                .toList();
    }

    private Collection<TwitchGameTo> loadAndMapGame(HystrixCommand<GameList> gameListCommand) {
        try {
            return gameListCommand.execute()
                    .getGames()
                    .stream()
                    .map(gameMapper::toTo)
                    .toList();
        } catch (HystrixRuntimeException e) {
            log.error("Error occurred during game retrieval", e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<TwitchUserTo> findUsersByLogin(List<String> logins) {
        return Lists.partition(logins, BATCH_SIZE)
                .stream()
                .map(list -> client.getUsers(null, null, list))
                .map(this::loadAndMapUser)
                .flatMap(Collection::stream)
                .toList();
    }

    @Override
    public List<TwitchUserTo> findUsersById(List<String> ids) {
        return Lists.partition(ids, BATCH_SIZE)
                .stream()
                .map(list -> client.getUsers(null, list, null))
                .map(this::loadAndMapUser)
                .flatMap(Collection::stream)
                .toList();
    }

    private Collection<TwitchUserTo> loadAndMapUser(HystrixCommand<UserList> userListCommand) {
        try {
            return userListCommand.execute()
                    .getUsers()
                    .stream()
                    .map(userMapper::toTo)
                    .toList();
        } catch (HystrixRuntimeException e) {
            log.error("Error occurred during user retrieval", e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<TwitchLiveStreamTo> findLiveStreamsByUserIds(List<String> ids) {
        return Lists.partition(ids, BATCH_SIZE)
                .stream()
                .map(list -> {
                    List<Stream> result;
                    try {
                        result = client.getStreams(null, null, null, BATCH_SIZE, null, null, list, null).execute().getStreams();
                    } catch (HystrixRuntimeException e) {
                        log.error("Error occurred during streams retrieval", e);
                        result = Collections.emptyList();
                    }
                    return result;
                })
                .flatMap(Collection::stream)
                .filter(s -> "live".equalsIgnoreCase(s.getType()))
                .map(streamMapper::toTo)
                .toList();
    }
}