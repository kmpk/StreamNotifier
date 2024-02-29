package com.github.kmpk.twitch.mapper;

import com.github.kmpk.twitch.model.TwitchUserEntity;
import com.github.kmpk.twitch.to.TwitchUserTo;
import com.github.twitch4j.helix.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {
    @Mapping(target = "avatarUrl", source = "profileImageUrl")
    @Mapping(target = "name", source = "displayName")
    TwitchUserTo toTo(User user);

    TwitchUserTo toTo(TwitchUserEntity user);

    @Mapping(target = "updatedAt", expression = "java(java.time.Instant.now())")
    TwitchUserEntity toEntity(TwitchUserTo to);
}
