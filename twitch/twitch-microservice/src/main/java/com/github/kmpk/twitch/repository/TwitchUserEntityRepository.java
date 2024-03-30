package com.github.kmpk.twitch.repository;

import com.github.kmpk.twitch.model.TwitchUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TwitchUserEntityRepository extends JpaRepository<TwitchUserEntity, String> {
    Optional<TwitchUserEntity> findByLogin(String login);

    List<TwitchUserEntity> findAllByUpdatedAtBefore(Instant instant);
}