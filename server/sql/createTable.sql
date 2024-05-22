begin transaction;

create schema hotelhub;

create table hotelhub.user
(
    id                  int generated always as identity primary key,
    username            varchar(24) unique                                                                                                                                   not null,
    email               text check (email ~
                                    '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$') not null not null not null,
    password_validation varchar(256)                                                                                                                                         not null
    /* phone_number varchar(20) */
);

create table hotelhub.token
(
    token_validation varchar(256) primary key,
    user_id          int,
    created_at       bigint not null,
    last_used_at     bigint not null,
    foreign key (user_id) references hotelhub.user (id)
);

create table hotelhub.hotel
(
    id        serial primary key,
    name      varchar(32)                       not null,
    address   varchar(64)                       not null,
    stars     int check (stars between 1 and 5) not null,
    latitude  double precision                  not null,
    longitude double precision                  not null
);

create table hotelhub.features
(
    id      serial primary key,
    feature varchar(48) not null
);

create table hotelhub.hotel_features
(
    hotel   int not null,
    feature int not null,
    foreign key (hotel) references hotelhub.hotel (id),
    foreign key (feature) references hotelhub.features (id)
);

create table hotelhub.critique
(
    id          serial primary key,
    user_id     int                               not null,
    hotel_id    int                               not null,
    edited      boolean   default false,
    created_at  timestamp                         not null,
    edited_at   timestamp default null,
    stars       int check (stars between 1 and 5) not null,
    description varchar(512)                      not null,
    foreign key (user_id) references hotelhub.user (id),
    foreign key (hotel_id) references hotelhub.hotel (id)
);

end transaction;