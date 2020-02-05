-- Postgre 

-- line table 
create table line (
      id int not null PRIMARY KEY,
      name varchar(255) not null,
      weight int not null,
      turn int unique not null,
      check (weight > 0)
  );

insert into line values (5, 'George Washington', 250, 1);
insert into line values (4, 'Thomas Jefferson', 175, 5);
insert into line values (3, 'John Adams', 350, 2);
insert into line values (6, 'Thomas Jefferson', 400, 3);
insert into line values (1, 'James Elephant', 500, 6);
insert into line values (2, 'Will Johnliams', 200, 4);

-- plays table 
create table plays (
      id integer not null,
      title varchar(40) not null,
      writer varchar(40) not null,
      unique(id)
  );

insert into plays values (109, 'Queens and Kings of Madagascar', 'Paul Sat');
insert into plays values (123, 'Merlin', 'Lee Roy');
insert into plays values (142, 'Key of the tea', 'Max Rogers');
insert into plays values (144, 'ROMEance Comedy', 'Bohring Ashell');
insert into plays values (145, 'Nameless.', 'Note Nul');

-- reservations table 
create table reservations (
  id integer not null,
  play_id integer not null,
  number_of_tickets integer not null,
  theater varchar(40) not null,
  unique(id)
);

insert into reservations values (13, 109, 12, 'Mc Rayleigh Theater');
insert into reservations values (24, 109, 34, 'Mc Rayleigh Theater');
insert into reservations values (37, 145, 84, 'Mc Rayleigh Theater');
insert into reservations values (49, 145, 45, 'Mc Rayleigh Theater');
insert into reservations values (51, 145, 41, 'Mc Rayleigh Theater');
insert into reservations values (68, 123, 3, 'Mc Rayleigh Theater');
insert into reservations values (83, 142, 46, 'Mc Rayleigh Theater');

-- recipes table 
 create table recipes (
      page_no integer not null,
      title varchar(30) not null,
      unique(page_no)
  );

insert into recipes values (1, 'Scrambled eggs');
insert into recipes values (2, 'Fondue');
insert into recipes values (3, 'Sandwich');
insert into recipes values (4, 'Tomato soup');
insert into recipes values (6, 'Liver');
