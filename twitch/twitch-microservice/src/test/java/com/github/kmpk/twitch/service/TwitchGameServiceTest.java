package com.github.kmpk.twitch.service;

import com.github.kmpk.twitch.AbstractSpringDataJpaTest;
import com.github.kmpk.twitch.TestMappersConfig;
import com.github.kmpk.twitch.client.TwitchClient;
import com.github.kmpk.twitch.model.TwitchGameEntity;
import com.github.kmpk.twitch.repository.TwitchGameEntityRepository;
import com.github.kmpk.twitch.to.TwitchGameTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.github.kmpk.twitch.TestData.EXISTED_GAME_TO;
import static com.github.kmpk.twitch.TestData.EXISTED_OLD_GAME_TO;
import static com.github.kmpk.twitch.TestData.NEW_GAME_TO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {TwitchGameServiceImpl.class, TestMappersConfig.class})
class TwitchGameServiceTest extends AbstractSpringDataJpaTest {
    @Autowired
    private TwitchGameService service;
    @MockBean
    private TwitchClient twitchClient;
    @SpyBean
    private TwitchGameEntityRepository repository;

    @Test
    void getExistedByName() {
        List<TwitchGameTo> result = service.getByNames(List.of(EXISTED_GAME_TO.name()));
        assertEquals(1, result.size());
        assertEquals(EXISTED_GAME_TO, result.getFirst());
        verify(repository, times(1)).findByNameIgnoreCase(EXISTED_GAME_TO.name());
        verify(twitchClient, never()).findGamesByName(anyList());
    }

    @Test
    void getNewByName() {
        when(twitchClient.findGamesByName(anyList())).thenReturn(List.of(NEW_GAME_TO));
        List<TwitchGameTo> result = service.getByNames(List.of(NEW_GAME_TO.name()));
        assertEquals(1, result.size());
        assertEquals(NEW_GAME_TO, result.getFirst());
        verify(repository, times(1)).findByNameIgnoreCase(NEW_GAME_TO.name());
        verify(repository).saveAll(any());
    }

    @Test
    void getExistedById() {
        List<TwitchGameTo> result = service.getByIds(List.of(EXISTED_GAME_TO.id()));
        assertEquals(1, result.size());
        assertEquals(EXISTED_GAME_TO, result.getFirst());
        verify(repository, times(1)).findById(EXISTED_GAME_TO.id());
        verify(twitchClient, never()).findGamesById(anyList());
    }

    @Test
    void getNewById() {
        when(twitchClient.findGamesById(anyList())).thenReturn(List.of(NEW_GAME_TO));
        List<TwitchGameTo> result = service.getByIds(List.of(NEW_GAME_TO.id()));
        assertEquals(1, result.size());
        assertEquals(NEW_GAME_TO, result.getFirst());
        verify(repository, times(1)).findById(NEW_GAME_TO.id());
        verify(repository).saveAll(any());
    }

    @Test
    void updateGames() {
        when(twitchClient.findGamesById(List.of(EXISTED_OLD_GAME_TO.id()))).thenReturn(List.of(EXISTED_OLD_GAME_TO));
        service.update();
        Optional<TwitchGameEntity> byId = repository.findById(EXISTED_OLD_GAME_TO.id());
        assertTrue(byId.isPresent());
        assertTrue(Duration.between(byId.get().getUpdatedAt(), Instant.now()).toMinutes() < 1);
        verify(twitchClient).findGamesById(List.of(EXISTED_OLD_GAME_TO.id()));
    }
}