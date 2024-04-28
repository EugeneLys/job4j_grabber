CREATE TABLE company
(
    id integer NOT NULL,
    name character varying,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE person
(
    id integer NOT NULL,
    name character varying,
    company_id integer references company(id),
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

insert into company values (1, 'Mersedes'), (2, 'BMW'), (3, 'Toyota'), (4, 'Nissan'), (5, 'Ford');

insert into person values 
(1, 'Person1', 1), (2, 'Person2', 1), (3, 'Person3', 1), (4, 'Person4', 1),
(5, 'Person5', 2), (6, 'Person6', 2), (7, 'Person7', 2), (8, 'Person8', 2),
(9, 'Person9', 3), (10, 'Person10', 3), (11, 'Person11', 3), (12, 'Person12', 3),
(13, 'Person13', 4), (14, 'Person14', 4),
(15, 'Person15', 5);

select person.name as person, company.name as company from person join company on company.id = person.company_id and company.id != 5;

create view result as select count(*), company.name from person join company on company_id = company.id group by company.name;

select distinct rsl.name, rsl.count from result join result as rsl on rsl.count = (select MAX(count) from result);