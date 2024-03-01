package com.github.kmpk.twitch.service;

import com.github.kmpk.twitch.AbstractSpringDataJpaTest;
import com.github.kmpk.twitch.TestData;
import com.github.kmpk.twitch.TestMappersConfig;
import com.github.kmpk.twitch.client.TwitchClient;
import com.github.kmpk.twitch.to.TwitchLiveStreamTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {TwitchStreamServiceImpl.class, TestMappersConfig.class})
class TwitchStreamServiceTest extends AbstractSpringDataJpaTest {

    @Autowired
    private TwitchStreamService service;
    @MockBean
    private TwitchClient twitchClient;

    @BeforeEach
    void setup() {
        service.cleanUserIds();
        Mockito.when(twitchClient.findLiveStreamsByUserIds(List.of(TestData.EXISTED_USER_TO.id())))
                .thenReturn(List.of(TestData.NEW_STREAM_TO));
    }

    @Test
    void getExistedStream() {
        service.addUserIds(List.of(TestData.EXISTED_USER_TO.id()));
        List<TwitchLiveStreamTo> result = service.findLiveStreams();
        assertEquals(1, result.size());
        assertTrue(isEqual(TestData.NEW_STREAM_TO, result.getFirst()));
    }

    @Test
    void getNonExistedStream() {
        service.addUserIds(List.of(TestData.NEW_USER_TO.id()));
        List<TwitchLiveStreamTo> result = service.findLiveStreams();
        assertTrue(result.isEmpty());
    }

    private boolean isEqual(TwitchLiveStreamTo first, TwitchLiveStreamTo second) {
        return first.userId().equals(second.userId()) &&
                first.userName().equals(second.userName()) &&
                first.title().equals(second.title()) &&
                first.thumbnailUrl().equals(second.thumbnailUrl()) &&
                first.gameId().equals(second.gameId()) &&
                first.gameName().equals(second.gameName());
    }
}