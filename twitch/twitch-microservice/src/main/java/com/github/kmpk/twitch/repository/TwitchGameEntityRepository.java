package com.github.kmpk.twitch.repository;

import com.github.kmpk.twitch.model.TwitchGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TwitchGameEntityRepository extends JpaRepository<TwitchGameEntity, String> {
    Optional<TwitchGameEntity> findByNameIgnoreCase(String name);

    List<TwitchGameEntity> findAllByUpdatedAtBefore(Instant instant);
}