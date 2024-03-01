package com.github.kmpk.twitch.service;

import com.github.kmpk.twitch.client.TwitchClient;
import com.github.kmpk.twitch.mapper.UserMapper;
import com.github.kmpk.twitch.model.TwitchUserEntity;
import com.github.kmpk.twitch.repository.TwitchUserEntityRepository;
import com.github.kmpk.twitch.to.TwitchUserTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TwitchUserServiceImpl implements TwitchUserService {
    private static final Duration UPDATE_DURATION = Duration.of(1, ChronoUnit.DAYS);

    private final TwitchUserEntityRepository repository;
    private final TwitchClient twitch;
    private final UserMapper mapper;

    @Override
    public List<TwitchUserTo> getByLogins(List<String> logins) {
        List<String> notFoundLogins = new ArrayList<>();
        List<TwitchUserTo> foundUsers = new ArrayList<>();

        for (String name : logins) {
            repository.findByLogin(name)
                    .map(mapper::toTo)
                    .ifPresentOrElse(foundUsers::add, () -> notFoundLogins.add(name));
        }
        if (!notFoundLogins.isEmpty()) {
            List<TwitchUserTo> usersById = twitch.findUsersByLogin(notFoundLogins);
            foundUsers.addAll(usersById);
            repository.saveAll(usersById.stream().map(mapper::toEntity).toList());
        }

        return foundUsers;
    }

    @Override
    public List<TwitchUserTo> getByIds(List<String> ids) {
        List<String> notFoundIds = new ArrayList<>();
        List<TwitchUserTo> foundUsers = new ArrayList<>();

        for (String id : ids) {
            repository.findById(id)
                    .map(mapper::toTo)
                    .ifPresentOrElse(foundUsers::add, () -> notFoundIds.add(id));
        }
        if (!notFoundIds.isEmpty()) {
            List<TwitchUserTo> usersById = twitch.findUsersById(notFoundIds);
            foundUsers.addAll(usersById);
            repository.saveAll(usersById.stream().map(mapper::toEntity).toList());
        }

        return foundUsers;
    }

    @Scheduled(cron = "0 0 1 * * *")
    @Override
    public void update() {
        Instant updateBeforeInstant = Instant.now().minus(UPDATE_DURATION);
        List<String> idsToUpdate = repository.findAllByUpdatedAtBefore(updateBeforeInstant).stream()
                .map(TwitchUserEntity::getId)
                .toList();
        List<TwitchUserEntity> updated = twitch.findUsersById(idsToUpdate).stream().map(mapper::toEntity).toList();
        repository.saveAll(updated);
    }
}
