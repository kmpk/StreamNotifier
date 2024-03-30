-- games test

DELETE
FROM twitch.game
WHERE true;

INSERT INTO twitch.game (id, name, art_url, updated_at)
VALUES ('existedId', 'existedName', 'existedUrl', NOW()),
       ('existedOldId', 'existedOldName', 'existedOldUrl', NOW() - INTERVAL '2 DAYS');

-- users test

DELETE
FROM twitch.user
WHERE true;

INSERT INTO twitch.user (id, login, name, avatar_url, updated_at)
VALUES ('existedId', 'existedLogin', 'existedName', 'existedAvatarUrl', NOW()),
       ('existedOldId', 'existedOldLogin', 'existedOldName', 'existedOldAvatarUrl', NOW() - INTERVAL '2 DAYS');