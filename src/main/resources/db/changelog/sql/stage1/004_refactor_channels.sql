drop table template_channels;
drop table channels;

create table channels
(
    id      bigserial
        constraint channels_pkey primary key,
    name    varchar(63)  not null,
    history int          not null default 0,
    url     varchar(255) not null
);

create table categories_to_channels
(
    category_id bigserial
        constraint categories_to_channels_category_fkey references categories on delete cascade,
    channel_id  bigserial
        constraint categories_to_channels_channel_fkey references channels on delete cascade,
    constraint categories_to_channels_pkey primary key (category_id, channel_id)
);

create table templates_to_channels
(
    template_id bigserial
        constraint templates_to_channels_template_fkey references templates on delete cascade,
    channel_id  bigserial
        constraint templates_to_channels_channel_fkey references channels on delete cascade,
    constraint templates_to_channels_pkey primary key (template_id, channel_id)
);