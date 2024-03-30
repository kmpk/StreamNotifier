package com.github.kmpk.twitch.client;

import com.github.kmpk.twitch.TestMappersConfig;
import com.github.kmpk.twitch.to.TwitchGameTo;
import com.github.kmpk.twitch.to.TwitchLiveStreamTo;
import com.github.kmpk.twitch.to.TwitchUserTo;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.domain.Game;
import com.github.twitch4j.helix.domain.GameList;
import com.github.twitch4j.helix.domain.Stream;
import com.github.twitch4j.helix.domain.StreamList;
import com.github.twitch4j.helix.domain.User;
import com.github.twitch4j.helix.domain.UserList;
import com.netflix.hystrix.HystrixCommand;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static com.github.kmpk.twitch.TestData.NEW_GAME_TO;
import static com.github.kmpk.twitch.TestData.NEW_STREAM_TO;
import static com.github.kmpk.twitch.TestData.NEW_USER_TO;
import static com.github.kmpk.twitch.client.TwitchClientAdapter.BATCH_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {TwitchClientAdapter.class, TestMappersConfig.class})
class TwitchClientAdapterTest {
    @Autowired
    private TwitchClient twitch;
    @MockBean
    private TwitchHelix helix;

    @Mock
    private Game game;
    @Mock
    private GameList gameList;
    @Mock
    private HystrixCommand<GameList> gameListCommand;

    @Test
    void loadGamesByName() {
        Mockito.when(helix.getGames(null, null, List.of(NEW_GAME_TO.name()), null))
                .thenReturn(gameListCommand);

        gameMock();

        List<TwitchGameTo> result = twitch.findGamesByName(List.of(NEW_GAME_TO.name()));
        assertEquals(1, result.size());
        assertEquals(NEW_GAME_TO, result.getFirst());
    }

    @Test
    void loadGamesById() {
        Mockito.when(helix.getGames(null, List.of(NEW_GAME_TO.id()), null, null))
                .thenReturn(gameListCommand);

        gameMock();

        List<TwitchGameTo> result = twitch.findGamesById(List.of(NEW_GAME_TO.id()));
        assertEquals(1, result.size());
        assertEquals(NEW_GAME_TO, result.getFirst());
    }

    private void gameMock() {
        Mockito.when(gameListCommand.execute()).thenReturn(gameList);

        Mockito.when(gameList.getGames()).thenReturn(List.of(game));

        Mockito.when(game.getId()).thenReturn(NEW_GAME_TO.id());
        Mockito.when(game.getName()).thenReturn(NEW_GAME_TO.name());
        Mockito.when(game.getBoxArtUrl()).thenReturn(NEW_GAME_TO.artUrl());
    }

    @Mock
    private User user;
    @Mock
    private UserList userList;
    @Mock
    private HystrixCommand<UserList> userListCommand;

    @Test
    void loadUsersByLogin() {
        Mockito.when(helix.getUsers(null, null, List.of(NEW_USER_TO.login())))
                .thenReturn(userListCommand);

        userMock();

        List<TwitchUserTo> result = twitch.findUsersByLogin(List.of(NEW_USER_TO.login()));
        assertEquals(1, result.size());
        assertEquals(NEW_USER_TO, result.getFirst());
    }

    @Test
    void loadUsersById() {
        Mockito.when(helix.getUsers(null, List.of(NEW_USER_TO.id()), null))
                .thenReturn(userListCommand);

        userMock();

        List<TwitchUserTo> result = twitch.findUsersById(List.of(NEW_USER_TO.id()));
        assertEquals(1, result.size());
        assertEquals(NEW_USER_TO, result.getFirst());
    }

    private void userMock() {
        Mockito.when(userListCommand.execute()).thenReturn(userList);

        Mockito.when(userList.getUsers()).thenReturn(List.of(user));

        Mockito.when(user.getId()).thenReturn(NEW_USER_TO.id());
        Mockito.when(user.getLogin()).thenReturn(NEW_USER_TO.login());
        Mockito.when(user.getDisplayName()).thenReturn(NEW_USER_TO.name());
        Mockito.when(user.getProfileImageUrl()).thenReturn(NEW_USER_TO.avatarUrl());
    }

    @Mock
    private Stream stream;
    @Mock
    private StreamList streamList;
    @Mock
    private HystrixCommand<StreamList> streamListCommand;

    @Test
    void loadLiveStreamsByUserIds() {
        Mockito.when(helix.getStreams(null, null, null, BATCH_SIZE, null, null, List.of(NEW_STREAM_TO.userId()), null))
                .thenReturn(streamListCommand);

        Mockito.when(streamListCommand.execute()).thenReturn(streamList);

        Mockito.when(streamList.getStreams()).thenReturn(List.of(stream));

        Mockito.when(stream.getUserId()).thenReturn(NEW_STREAM_TO.userId());
        Mockito.when(stream.getUserName()).thenReturn(NEW_STREAM_TO.userName());
        Mockito.when(stream.getUserLogin()).thenReturn(NEW_STREAM_TO.userLogin());
        Mockito.when(stream.getGameId()).thenReturn(NEW_STREAM_TO.gameId());
        Mockito.when(stream.getGameName()).thenReturn(NEW_STREAM_TO.gameName());
        Mockito.when(stream.getThumbnailUrl()).thenReturn(NEW_STREAM_TO.thumbnailUrl());
        Mockito.when(stream.getTitle()).thenReturn(NEW_STREAM_TO.title());
        Mockito.when(stream.getStartedAtInstant()).thenReturn(NEW_STREAM_TO.startedAt());
        Mockito.when(stream.getType()).thenReturn("live");

        List<TwitchLiveStreamTo> result = twitch.findLiveStreamsByUserIds(List.of(NEW_STREAM_TO.userId()));
        assertEquals(1, result.size());
        assertEquals(NEW_STREAM_TO, result.getFirst());
    }
}