create table if not exists practice.user
(
    id       int auto_increment primary key,
    username varchar(45) not null,
    password varchar(45) not null,
    age      int         not null,
    address  varchar(45) not null
);