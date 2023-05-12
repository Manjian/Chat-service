create table t_channel
(
    id      varchar(36) primary key,
    name    varchar(64) not null,

    created timestamp   not null default current_timestamp,
    updated timestamp   not null default current_timestamp,
    deleted timestamp null,


    constraint uk_channel_name unique (name)

);

create table t_message
(
    id            varchar(36) primary key,
    content       varchar(255) not null,
    message_type  varchar(36)  not null,
    message_owner varchar(36)  not null,

    channel_id    varchar(36) references t_channel (id) on update cascade on delete cascade,

    created       timestamp    not null default current_timestamp,
    updated       timestamp    not null default current_timestamp,
    deleted       timestamp null,

    constraint uk_messages_id_deleted unique (id, deleted),
    constraint fk_messages_channel_id foreign key (channel_id) references t_channel (id)
);



create table t_users
(
    id         varchar(36) primary key,
    name       varchar(64) not null,
    password   varchar(64) not null,
    session_id varchar(36) not null,

    channel_id varchar(36) references t_channel (id) on update cascade on delete cascade,

    created    timestamp   not null default current_timestamp,
    updated    timestamp   not null default current_timestamp,
    deleted    timestamp null,


    constraint fk_users_channel_id foreign key (channel_id) references t_channel (id),
    constraint uk_users_name unique (name)

);

create table t_devices
(
    id      varchar(36) primary key,
    address varchar(64) not null,

    user_id varchar(36) references t_users (id) on update cascade on delete cascade,

    created timestamp   not null default current_timestamp,
    updated timestamp   not null default current_timestamp,
    deleted timestamp null,

    constraint uk_devices_id_deleted unique (id, deleted),
    constraint fk_devices_user_id foreign key (user_id) references t_users (id)
);





