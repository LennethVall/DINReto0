drop database WorkApp;

create database WorkApp;

use WorkApp;

create table Usuario (
	idEmpl varchar(20) primary key,
	Usuario varchar (100) not null,
    Loggin varchar (50) not null,
    Nombre varchar (50) not null,
    Apellido varchar (50) not null,
    Email varchar (50) not null
    check (email regexp '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$'),
    Dni VARCHAR(9) not null unique
    check (dni regexp '^[0-9]{8}[A-Za-z]$'),
    Direccion varchar (150) not null,
    Tlfn varchar (15) not null check(Tlfn regexp '^\\+?[0-9]{8,15}$'),
    Puesto varchar (50) not null,
    IdSuperior varchar (20) default null 
    references Usuarios(idEmpl) on delete set null on update cascade
    
);

create table Horarios (
	IdHorario int auto_increment primary key,
    IdEmpl varchar (20) not null 
    references Usuarios (IdEmpl) on delete cascade on update cascade,
    Fecha date not null,
    HoraInicio time not null,
    HoraFin time not null, Turno varchar(20) not null
    );