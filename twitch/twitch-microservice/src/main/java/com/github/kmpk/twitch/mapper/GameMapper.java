package com.github.kmpk.twitch.mapper;

import com.github.kmpk.twitch.model.TwitchGameEntity;
import com.github.kmpk.twitch.to.TwitchGameTo;
import com.github.twitch4j.helix.domain.Game;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface GameMapper {
    @Mapping(target = "artUrl", source = "boxArtUrl")
    TwitchGameTo toTo(Game game);

    TwitchGameTo toTo(TwitchGameEntity game);

    @Mapping(target = "updatedAt", ignore = true)
    TwitchGameEntity toEntity(TwitchGameTo to);
}
