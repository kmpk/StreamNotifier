create table if not exists twitch.game
(
    id         varchar(255)                not null
        primary key,
    art_url    varchar(255)                not null,
    name       varchar(255)                not null,
    updated_at timestamp(6) with time zone not null
);

create index game_name_idx on twitch.game (name);

create table if not exists twitch.user
(
    id         varchar(255)                not null
        primary key,
    avatar_url varchar(255)                not null,
    login      varchar(255)                not null,
    name       varchar(255)                not null,
    updated_at timestamp(6) with time zone not null
);

create index user_login_idx on twitch.user (login);

