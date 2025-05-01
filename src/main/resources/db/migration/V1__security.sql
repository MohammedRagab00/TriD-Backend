create table id_generator
(
    id_value bigint,
    id_name  varchar(30) not null
        primary key
);

create table users
(
    account_locked     boolean      not null,
    date_of_birth      date,
    enabled            boolean      not null,
    id                 integer      not null
        primary key,
    created_date       timestamp(6) not null,
    last_modified_date timestamp(6),
    firstname          varchar(50)  not null,
    lastname           varchar(50)  not null,
    email              varchar(100) not null
        unique,
    password           varchar(255)
);

create table role
(
    id                 integer      not null
        primary key,
    created_date       timestamp(6) not null,
    last_modified_date timestamp(6),
    name               varchar(20)
        unique
);

create table user_role
(
    role_id integer not null
        constraint role_foreign_key
            references role,
    user_id integer not null
        constraint user_foreign_key
            references users,
    primary key (role_id, user_id)
);

create table token
(
    id           integer     not null
        primary key,
    user_id      integer     not null
        constraint user_foreign_key
            references users
            on delete cascade,
    created_at   timestamp(6),
    expires_at   timestamp(6),
    validated_at timestamp(6),
    token        varchar(20) not null
);

create table refresh_token
(
    id                 integer      not null
        primary key,
    revoked            boolean      not null,
    user_id            integer      not null
        constraint user_foreign_key
            references users
            on delete cascade,
    created_date       timestamp(6) not null,
    last_modified_date timestamp(6),
    token              varchar(255) not null
        unique
);