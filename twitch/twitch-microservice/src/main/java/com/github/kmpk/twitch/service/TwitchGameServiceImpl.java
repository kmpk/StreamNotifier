package com.github.kmpk.twitch.service;

import com.github.kmpk.twitch.client.TwitchClient;
import com.github.kmpk.twitch.mapper.GameMapper;
import com.github.kmpk.twitch.model.TwitchGameEntity;
import com.github.kmpk.twitch.repository.TwitchGameEntityRepository;
import com.github.kmpk.twitch.to.TwitchGameTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TwitchGameServiceImpl implements TwitchGameService {
    private static final Duration UPDATE_DURATION = Duration.of(1, ChronoUnit.DAYS);

    private final TwitchGameEntityRepository repository;
    private final TwitchClient twitch;
    private final GameMapper mapper;

    @Override
    public List<TwitchGameTo> getByNames(List<String> names) {
        List<String> notFoundNames = new ArrayList<>();
        List<TwitchGameTo> foundGames = new ArrayList<>();

        for (String name : names) {
            repository.findByNameIgnoreCase(name)
                    .map(mapper::toTo)
                    .ifPresentOrElse(foundGames::add, () -> notFoundNames.add(name));
        }
        if (!notFoundNames.isEmpty()) {
            List<TwitchGameTo> gamesById = twitch.findGamesByName(notFoundNames);
            foundGames.addAll(gamesById);
            repository.saveAll(gamesById.stream().map(mapper::toEntity).toList());
        }

        return foundGames;
    }

    @Override
    public List<TwitchGameTo> getByIds(List<String> ids) {
        List<String> notFoundIds = new ArrayList<>();
        List<TwitchGameTo> foundGames = new ArrayList<>();

        for (String id : ids) {
            repository.findById(id)
                    .map(mapper::toTo)
                    .ifPresentOrElse(foundGames::add, () -> notFoundIds.add(id));
        }
        if (!notFoundIds.isEmpty()) {
            List<TwitchGameTo> gamesById = twitch.findGamesById(notFoundIds);
            foundGames.addAll(gamesById);
            repository.saveAll(gamesById.stream().map(mapper::toEntity).toList());
        }

        return foundGames;
    }

    @Scheduled(cron = "0 0 1 * * *")
    @Override
    public void update() {
        Instant updateBeforeInstant = Instant.now().minus(UPDATE_DURATION);
        List<String> idsToUpdate = repository.findAllByUpdatedAtBefore(updateBeforeInstant).stream()
                .map(TwitchGameEntity::getId)
                .toList();
        List<TwitchGameEntity> updated = twitch.findGamesById(idsToUpdate).stream().map(mapper::toEntity).toList();
        repository.saveAll(updated);
    }
}
