create table payment (
                         id bigint primary key,
                         amount decimal(19, 2) not null,
                         method varchar(30) not null
);