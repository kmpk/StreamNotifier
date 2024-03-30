package com.github.kmpk.twitch.mapper;

import com.github.kmpk.twitch.to.TwitchLiveStreamTo;
import com.github.twitch4j.helix.domain.Stream;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface StreamMapper {
    @Mapping(target = "startedAt", source = "startedAtInstant")
    TwitchLiveStreamTo toTo(Stream stream);
}
