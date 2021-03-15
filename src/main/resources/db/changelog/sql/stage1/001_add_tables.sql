create table playlists
(
    id   bigserial
        constraint playlists_pkey primary key,
    name varchar(63) not null unique
);

create table raw_playlists
(
    id          bigserial
        constraint raw_playlists_pkey primary key,
    playlist_id bigint not null
        constraint raw_playlists_playlist_fkey references playlists on delete cascade,
    content     text
);

create table categories
(
    id          bigserial
        constraint categories_pkey primary key,
    playlist_id bigint      not null
        constraint categories_playlist_fkey references playlists on delete cascade,
    name        varchar(63) not null
);

create table channels
(
    id          bigserial
        constraint channels_pkey primary key,
    category_id bigint       not null
        constraint channels_categories_fkey references categories on delete cascade,
    name        varchar(63)  not null,
    history     int          not null default 0,
    url         varchar(255) not null
);