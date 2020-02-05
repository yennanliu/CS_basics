-- Postgre 
drop table buses;
drop table  passengers;
create table buses (
    id integer primary key,
    origin varchar not null,
    destination varchar not null,
    time varchar not null,
    unique (origin, destination, time)
);

create table  passengers (
    id integer primary key,
    origin varchar not null,
    destination varchar not null,
    time varchar not null
);


-- buses table 
insert into buses values (10, 'Warsaw', 'Berlin', '10:55');
insert into buses values (20, 'Berlin', 'Paris', '06:20');
insert into buses values (21, 'Berlin', 'Paris', '14:00');
insert into buses values (22, 'Berlin', 'Paris', '21:40');
insert into buses values (30, 'Paris', 'Madrid', '13:30');

-- passengers table 
insert into passengers values (1, 'Paris', 'Madrid', '13:30');
insert into passengers values (2, 'Paris', 'Madrid', '13:31');
insert into passengers values (10, 'Warsaw', 'Paris', '10:00');
insert into passengers values (11, 'Warsaw', 'Berlin', '22:31');
insert into passengers values (40, 'Berlin', 'Paris', '06:15');
insert into passengers values (41, 'Berlin', 'Paris', '06:50');
insert into passengers values (42, 'Berlin', 'Paris', '07:12');
insert into passengers values (43, 'Berlin', 'Paris', '12:03');
insert into passengers values (44, 'Berlin', 'Paris', '20:00');
