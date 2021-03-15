create table templates
(
    id   bigserial
        constraint templates_pkey primary key,
    name varchar(63) not null unique
);

create table template_channels
(
    id          bigserial
        constraint template_channels_pkey primary key,
    name        varchar(63) not null,
    template_id bigint      not null
        constraint template_channels_template_fkey references templates on delete cascade
);