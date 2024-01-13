create database agregator;

create table post (
	id serial primary key unique,
	name varchar(255), 
	text text, 
	link text unique,
	created timestamp
);