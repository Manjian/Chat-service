create table t_users
(
    id         varchar(36) primary key,
    first_name varchar(64) not null,
    last_name  varchar(64) not null,

    active_on  timestamp   null,

    created    timestamp   not null default current_timestamp,
    updated    timestamp   not null default current_timestamp,
    deleted    timestamp   null,

    constraint uk_users_id_deleted unique (id, deleted)
);

create table t_devices
(
    id                      varchar(36) primary key,
    name                    varchar(64) not null,
    type                    varchar(64) not null,

    user_id                 varchar(36) references t_users (id) on update cascade on delete cascade,

    created                 timestamp   not null default current_timestamp,
    updated                 timestamp   not null default current_timestamp,
    deleted                 timestamp   null,

    constraint uk_devices_id_deleted unique (id, deleted),
    constraint fk_devices_user_id foreign key (user_id) references t_users (id)
);


