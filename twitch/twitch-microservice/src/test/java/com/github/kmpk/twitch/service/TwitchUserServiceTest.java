package com.github.kmpk.twitch.service;

import com.github.kmpk.twitch.AbstractSpringDataJpaTest;
import com.github.kmpk.twitch.TestData;
import com.github.kmpk.twitch.TestMappersConfig;
import com.github.kmpk.twitch.client.TwitchClient;
import com.github.kmpk.twitch.model.TwitchUserEntity;
import com.github.kmpk.twitch.repository.TwitchUserEntityRepository;
import com.github.kmpk.twitch.to.TwitchUserTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {TwitchUserServiceImpl.class, TestMappersConfig.class})
class TwitchUserServiceTest extends AbstractSpringDataJpaTest {
    @Autowired
    private TwitchUserService service;
    @MockBean
    private TwitchClient twitchClient;
    @SpyBean
    private TwitchUserEntityRepository repository;

    @Test
    void getExistedByLogin() {
        List<TwitchUserTo> result = service.getByLogins(List.of(TestData.EXISTED_USER_TO.login()));
        assertEquals(1, result.size());
        assertEquals(TestData.EXISTED_USER_TO, result.getFirst());
        verify(repository, times(1)).findByLogin(TestData.EXISTED_USER_TO.login());
        verify(twitchClient, never()).findUsersByLogin(anyList());
    }

    @Test
    void getNewByLogin() {
        when(twitchClient.findUsersByLogin(anyList())).thenReturn(List.of(TestData.NEW_USER_TO));
        List<TwitchUserTo> result = service.getByLogins(List.of(TestData.NEW_USER_TO.login()));
        assertEquals(1, result.size());
        assertEquals(TestData.NEW_USER_TO, result.getFirst());
        verify(repository).saveAll(any());
        verify(repository, times(1)).findByLogin(TestData.NEW_USER_TO.login());
    }

    @Test
    void getExistedById() {
        List<TwitchUserTo> result = service.getByIds(List.of(TestData.EXISTED_USER_TO.id()));
        assertEquals(1, result.size());
        assertEquals(TestData.EXISTED_USER_TO, result.getFirst());
        verify(repository, times(1)).findById(TestData.EXISTED_USER_TO.id());
        verify(twitchClient, never()).findUsersById(anyList());

    }

    @Test
    void getNewById() {
        when(twitchClient.findUsersById(anyList())).thenReturn(List.of(TestData.NEW_USER_TO));
        List<TwitchUserTo> result = service.getByIds(List.of(TestData.NEW_USER_TO.id()));
        assertEquals(1, result.size());
        assertEquals(TestData.NEW_USER_TO, result.getFirst());
        verify(repository).saveAll(any());
        verify(repository, times(1)).findById(TestData.NEW_USER_TO.id());
    }

    @Test
    void updateUsersInfoUsers() {
        when(twitchClient.findUsersById(List.of(TestData.EXISTED_OLD_USER_TO.id()))).thenReturn(List.of(TestData.EXISTED_OLD_USER_TO));
        service.update();
        Optional<TwitchUserEntity> byId = repository.findById(TestData.EXISTED_OLD_USER_TO.id());
        assertTrue(byId.isPresent());
        assertTrue(Duration.between(byId.get().getUpdatedAt(), Instant.now()).toMinutes() < 1);
        verify(twitchClient).findUsersById(List.of(TestData.EXISTED_OLD_USER_TO.id()));
    }
}