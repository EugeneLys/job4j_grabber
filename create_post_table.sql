create database agregator;

create table post (
	table_id serial primary key,
	id int unique,
	name varchar(255), 
	text text, 
	link text unique,
	created timestamp
);