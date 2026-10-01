create database if  not exists WorkApp;

use WorkApp;

create table Usuario (
	IdEmpl varchar(20) primary key,
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
	Rol varchar (30) not null check(Rol regexp '^(Empleado|Encargado|Gerente|Jefe)$'),
	IdSuperior varchar (20) default null 
	references Usuario(idEmpl) on delete set null on update cascade
);

create table Horarios (
	IdHorario int auto_increment primary key,
	IdEmpl varchar (20) not null 
	references Usuario (idEmpl) on delete cascade on update cascade,
	Fecha date not null,
	HoraInicio time not null,
	HoraFin time not null,
	Turno varchar(20) not null
);

INSERT INTO Usuario (idEmpl, Usuario, Loggin, Nombre, Apellido, Email, Dni, Direccion, Tlfn, Puesto, Rol, IdSuperior) VALUES
('EMP001', '1001', 'usr01', 'Laura', 'García', 'laura.jefe@empresa.com', '11111111A', 'Gran Vía 1, Bilbao', '600111001', 'Director de Centro', 'Jefe', NULL);

-- ============================================================================
-- 2. GERENTES / JEFES DE PLANTA (2 registros - Pertenecen al Director EMP001)
-- Logins: usr02, usr03 | Passwords: 1002, 1003
-- ============================================================================
INSERT INTO Usuario (idEmpl, Usuario, Loggin, Nombre, Apellido, Email, Dni, Direccion, Tlfn, Puesto, Rol, IdSuperior) VALUES
('EMP002', '1002', 'usr02', 'Carlos', 'Martínez', 'carlos.gerente@empresa.com', '22222222B', 'Alameda Urquijo 12, Bilbao', '600222001', 'Jefe Planta Electrónica', 'Gerente', 'EMP001'),
('EMP003', '1003', 'usr03', 'Elena', 'Sánchez', 'elena.gerente@empresa.com', '33333333C', 'Autonomía 45, Bilbao', '600222002', 'Jefa Planta Moda', 'Gerente', 'EMP001');

-- ============================================================================
-- 3. ENCARGADOS / SUPERVISORES DE SECCIÓN (4 registros - 2 por Planta)
-- Logins: usr04 al usr07 | Passwords: 1004 al 1007
-- ============================================================================
-- Encargados de Electrónica (Gerente EMP002)
INSERT INTO Usuario (idEmpl, Usuario, Loggin, Nombre, Apellido, Email, Dni, Direccion, Tlfn, Puesto, Rol, IdSuperior) VALUES
('EMP004', '1004', 'usr04', 'Marta', 'López', 'marta.encargada@empresa.com', '44444444D', 'Calle Mayor 8, Erandio', '600333001', 'Encargada Telefonía', 'Encargado', 'EMP002'),
('EMP005', '1005', 'usr05', 'David', 'Gómez', 'david.encargado@empresa.com', '55555555E', 'Sabino Arana 14, Bilbao', '600333002', 'Encargado Informática', 'Encargado', 'EMP002');

-- Encargados de Moda (Gerente EMP003)
INSERT INTO Usuario (idEmpl, Usuario, Loggin, Nombre, Apellido, Email, Dni, Direccion, Tlfn, Puesto, Rol, IdSuperior) VALUES
('EMP006', '1006', 'usr06', 'Ana', 'Fernández', 'ana.encargada@empresa.com', '66666666F', 'Bizkaia Kalea 3, Getxo', '600333003', 'Encargada Moda Mujer', 'Encargado', 'EMP003'),
('EMP007', '1007', 'usr07', 'Javier', 'Ruiz', 'javier.encargado@empresa.com', '77777777G', 'Las Arenas 22, Getxo', '600333004', 'Encargado Moda Hombre', 'Encargado', 'EMP003');

-- ============================================================================
-- 4. EMPLEADOS / VENDEDORES Y CAJEROS (16 registros - 4 por Encargado)
-- Logins: usr08 al usr23 | Passwords: 1008 al 1023
-- ============================================================================
-- Subordinados de Encargada Telefonía (EMP004)
INSERT INTO Usuario (idEmpl, Usuario, Loggin, Nombre, Apellido, Email, Dni, Direccion, Tlfn, Puesto, Rol, IdSuperior) VALUES
('EMP008', '1008', 'usr08', 'Aitor', 'Bilbao', 'aitor.emp@empresa.com', '10000001A', 'Altzaga 5, Erandio', '600444001', 'Vendedor Telefonía', 'Empleado', 'EMP004'),
('EMP009', '1009', 'usr09', 'Lucía', 'Díaz', 'lucia.emp@empresa.com', '10000002B', 'Tartanga 10, Erandio', '600444002', 'Vendedora Telefonía', 'Empleado', 'EMP004'),
('EMP010', '1010', 'usr10', 'Pablo', 'Moreno', 'pablo.emp@empresa.com', '10000003C', 'Indautxu 3, Bilbao', '600444003', 'Técnico Postventa', 'Empleado', 'EMP004'),
('EMP011', '1011', 'usr11', 'Sara', 'Álvarez', 'sara.emp@empresa.com', '10000004D', 'Ercilla 18, Bilbao', '600444004', 'Cajera Telefonía', 'Empleado', 'EMP004');

-- Subordinados de Encargado Informática (EMP005)
INSERT INTO Usuario (idEmpl, Usuario, Loggin, Nombre, Apellido, Email, Dni, Direccion, Tlfn, Puesto, Rol, IdSuperior) VALUES
('EMP012', '1012', 'usr12', 'Iker', 'Romero', 'iker.emp@empresa.com', '10000005E', 'Zabalburu 4, Bilbao', '600444005', 'Vendedor Informática', 'Empleado', 'EMP005'),
('EMP013', '1013', 'usr13', 'Marina', 'Navarro', 'marina.emp@empresa.com', '10000006F', 'Deusto 12, Bilbao', '600444006', 'Vendedora Gaming', 'Empleado', 'EMP005'),
('EMP014', '1014', 'usr14', 'Jon', 'Torres', 'jon.emp@empresa.com', '10000007G', 'San Mamés 2, Bilbao', '600444007', 'Reponedor Almacén', 'Empleado', 'EMP005'),
('EMP015', '1015', 'usr15', 'Nerea', 'Gil', 'nerea.emp@empresa.com', '10000008H', 'Casco Viejo 9, Bilbao', '600444008', 'Cajera Informática', 'Empleado', 'EMP005');

-- Subordinados de Encargada Moda Mujer (EMP006)
INSERT INTO Usuario (idEmpl, Usuario, Loggin, Nombre, Apellido, Email, Dni, Direccion, Tlfn, Puesto, Rol, IdSuperior) VALUES
('EMP016', '1016', 'usr16', 'Mikel', 'Vázquez', 'mikel.emp@empresa.com', '10000009I', 'Algorta 15, Getxo', '600444009', 'Vendedor Moda Mujer', 'Empleado', 'EMP006'),
('EMP017', '1017', 'usr17', 'Paula', 'Serrano', 'paula.emp@empresa.com', '10000010J', 'Gobela 8, Getxo', '600444010', 'Vendedora Moda Mujer', 'Empleado', 'EMP006'),
('EMP018', '1018', 'usr18', 'Unai', 'Blanco', 'unai.emp@empresa.com', '10000011K', 'Romo 6, Getxo', '600444011', 'Visual Merchandiser', 'Empleado', 'EMP006'),
('EMP019', '1019', 'usr19', 'Carmen', 'Molina', 'carmen.emp@empresa.com', '10000012L', 'Neguri 21, Getxo', '600444012', 'Cajera Moda', 'Empleado', 'EMP006');

-- Subordinados de Encargado Moda Hombre (EMP007)
INSERT INTO Usuario (idEmpl, Usuario, Loggin, Nombre, Apellido, Email, Dni, Direccion, Tlfn, Puesto, Rol, IdSuperior) VALUES
('EMP020', '1020', 'usr20', 'Kerman', 'Castro', 'kerman.emp@empresa.com', '10000013M', 'Berango 7', '600444013', 'Vendedor Moda Hombre', 'Empleado', 'EMP007'),
('EMP021', '1021', 'usr21', 'Amaia', 'Ortiz', 'amaia.emp@empresa.com', '10000014N', 'Sopela 11', '600444014', 'Vendedora Moda Hombre', 'Empleado', 'EMP007'),
('EMP022', '1022', 'usr22', 'Gorka', 'Rubio', 'gorka.emp@empresa.com', '10000015O', 'Plentzia 3', '600444015', 'Sastre / Arreglos', 'Empleado', 'EMP007'),
('EMP023', '1023', 'usr23', 'Ane', 'Marín', 'ane.emp@empresa.com', '10000016P', 'Urduliz 19', '600444016', 'Reponedora Moda', 'Empleado', 'EMP007');

-- ============================================================================


-- ============================================================================
-- HORARIOS DEL MES DE OCTUBRE DE 2026 (39 horas semanales / 2 días libres)
-- ============================================================================

INSERT INTO Horarios (IdEmpl, Fecha, HoraInicio, HoraFin, Turno) VALUES

-- ----------------------------------------------------------------------------
-- EMPLEADOS CON TURNO DE MAÑANA (EMP008, EMP009, EMP012, EMP013, EMP016, EMP017, EMP020, EMP021)
-- Libre: Domingos y Lunes
-- ----------------------------------------------------------------------------
-- Semana 1 (1 al 3 Oct)
('EMP008', '2026-10-01', '08:00:00', '16:00:00', 'Mañana'),
('EMP008', '2026-10-02', '08:00:00', '16:00:00', 'Mañana'),
('EMP008', '2026-10-03', '08:00:00', '15:00:00', 'Mañana'),
-- Semana 2 (5 al 10 Oct)
('EMP008', '2026-10-06', '08:00:00', '16:00:00', 'Mañana'),
('EMP008', '2026-10-07', '08:00:00', '16:00:00', 'Mañana'),
('EMP008', '2026-10-08', '08:00:00', '16:00:00', 'Mañana'),
('EMP008', '2026-10-09', '08:00:00', '16:00:00', 'Mañana'),
('EMP008', '2026-10-10', '08:00:00', '15:00:00', 'Mañana'),
-- Semana 3 (12 al 17 Oct)
('EMP008', '2026-10-13', '08:00:00', '16:00:00', 'Mañana'),
('EMP008', '2026-10-14', '08:00:00', '16:00:00', 'Mañana'),
('EMP008', '2026-10-15', '08:00:00', '16:00:00', 'Mañana'),
('EMP008', '2026-10-16', '08:00:00', '16:00:00', 'Mañana'),
('EMP008', '2026-10-17', '08:00:00', '15:00:00', 'Mañana'),
-- Semana 4 (19 al 24 Oct)
('EMP008', '2026-10-20', '08:00:00', '16:00:00', 'Mañana'),
('EMP008', '2026-10-21', '08:00:00', '16:00:00', 'Mañana'),
('EMP008', '2026-10-22', '08:00:00', '16:00:00', 'Mañana'),
('EMP008', '2026-10-23', '08:00:00', '16:00:00', 'Mañana'),
('EMP008', '2026-10-24', '08:00:00', '15:00:00', 'Mañana'),
-- Semana 5 (26 al 31 Oct)
('EMP008', '2026-10-27', '08:00:00', '16:00:00', 'Mañana'),
('EMP008', '2026-10-28', '08:00:00', '16:00:00', 'Mañana'),
('EMP008', '2026-10-29', '08:00:00', '16:00:00', 'Mañana'),
('EMP008', '2026-10-30', '08:00:00', '16:00:00', 'Mañana'),
('EMP008', '2026-10-31', '08:00:00', '15:00:00', 'Mañana');

-- ----------------------------------------------------------------------------
-- REPLICA PARA EL RESTO DE COMPAÑEROS DE MAÑANA (EMP009, EMP012, EMP013)
-- ----------------------------------------------------------------------------
INSERT INTO Horarios (IdEmpl, Fecha, HoraInicio, HoraFin, Turno)
SELECT 'EMP009', Fecha, HoraInicio, HoraFin, Turno FROM Horarios WHERE IdEmpl = 'EMP008';

INSERT INTO Horarios (IdEmpl, Fecha, HoraInicio, HoraFin, Turno)
SELECT 'EMP012', Fecha, HoraInicio, HoraFin, Turno FROM Horarios WHERE IdEmpl = 'EMP008';

INSERT INTO Horarios (IdEmpl, Fecha, HoraInicio, HoraFin, Turno)
SELECT 'EMP013', Fecha, HoraInicio, HoraFin, Turno FROM Horarios WHERE IdEmpl = 'EMP008';

-- ----------------------------------------------------------------------------
-- EMPLEADOS CON TURNO PARTIDO (EMP010, EMP014, EMP018, EMP022)
-- Libre: Domingos y Miércoles (Día libre como transición)
-- ----------------------------------------------------------------------------
-- Semana 1
INSERT INTO Horarios (IdEmpl, Fecha, HoraInicio, HoraFin, Turno) VALUES
('EMP010', '2026-10-01', '10:00:00', '19:00:00', 'Partido'),
('EMP010', '2026-10-02', '10:00:00', '19:00:00', 'Partido'),
('EMP010', '2026-10-03', '10:00:00', '18:00:00', 'Partido'),
-- Semana 2
('EMP010', '2026-10-05', '10:00:00', '19:00:00', 'Partido'),
('EMP010', '2026-10-06', '10:00:00', '19:00:00', 'Partido'),
('EMP010', '2026-10-08', '10:00:00', '19:00:00', 'Partido'),
('EMP010', '2026-10-09', '10:00:00', '19:00:00', 'Partido'),
('EMP010', '2026-10-10', '10:00:00', '18:00:00', 'Partido'),
-- Semana 3
('EMP010', '2026-10-12', '10:00:00', '19:00:00', 'Partido'),
('EMP010', '2026-10-13', '10:00:00', '19:00:00', 'Partido'),
('EMP010', '2026-10-15', '10:00:00', '19:00:00', 'Partido'),
('EMP010', '2026-10-16', '10:00:00', '19:00:00', 'Partido'),
('EMP010', '2026-10-17', '10:00:00', '18:00:00', 'Partido'),
-- Semana 4
('EMP010', '2026-10-19', '10:00:00', '19:00:00', 'Partido'),
('EMP010', '2026-10-20', '10:00:00', '19:00:00', 'Partido'),
('EMP010', '2026-10-22', '10:00:00', '19:00:00', 'Partido'),
('EMP010', '2026-10-23', '10:00:00', '19:00:00', 'Partido'),
('EMP010', '2026-10-24', '10:00:00', '18:00:00', 'Partido'),
-- Semana 5
('EMP010', '2026-10-26', '10:00:00', '19:00:00', 'Partido'),
('EMP010', '2026-10-27', '10:00:00', '19:00:00', 'Partido'),
('EMP010', '2026-10-29', '10:00:00', '19:00:00', 'Partido'),
('EMP010', '2026-10-30', '10:00:00', '19:00:00', 'Partido'),
('EMP010', '2026-10-31', '10:00:00', '18:00:00', 'Partido');

-- Replicar para resto de grupo Partido
INSERT INTO Horarios (IdEmpl, Fecha, HoraInicio, HoraFin, Turno)
SELECT 'EMP014', Fecha, HoraInicio, HoraFin, Turno FROM Horarios WHERE IdEmpl = 'EMP010';

INSERT INTO Horarios (IdEmpl, Fecha, HoraInicio, HoraFin, Turno)
SELECT 'EMP018', Fecha, HoraInicio, HoraFin, Turno FROM Horarios WHERE IdEmpl = 'EMP010';

INSERT INTO Horarios (IdEmpl, Fecha, HoraInicio, HoraFin, Turno)
SELECT 'EMP022', Fecha, HoraInicio, HoraFin, Turno FROM Horarios WHERE IdEmpl = 'EMP010';

-- ----------------------------------------------------------------------------
-- EMPLEADOS CON TURNO DE TARDE (EMP011, EMP015, EMP019, EMP023)
-- Libre: Domingos y Jueves
-- ----------------------------------------------------------------------------
-- Semana 1
INSERT INTO Horarios (IdEmpl, Fecha, HoraInicio, HoraFin, Turno) VALUES
('EMP011', '2026-10-01', '14:00:00', '22:00:00', 'Tarde'),
('EMP011', '2026-10-02', '14:00:00', '22:00:00', 'Tarde'),
('EMP011', '2026-10-03', '15:00:00', '22:00:00', 'Tarde'),
-- Semana 2
('EMP011', '2026-10-05', '14:00:00', '22:00:00', 'Tarde'),
('EMP011', '2026-10-06', '14:00:00', '22:00:00', 'Tarde'),
('EMP011', '2026-10-07', '14:00:00', '22:00:00', 'Tarde'),
('EMP011', '2026-10-09', '14:00:00', '22:00:00', 'Tarde'),
('EMP011', '2026-10-10', '15:00:00', '22:00:00', 'Tarde'),
-- Semana 3
('EMP011', '2026-10-12', '14:00:00', '22:00:00', 'Tarde'),
('EMP011', '2026-10-13', '14:00:00', '22:00:00', 'Tarde'),
('EMP011', '2026-10-14', '14:00:00', '22:00:00', 'Tarde'),
('EMP011', '2026-10-16', '14:00:00', '22:00:00', 'Tarde'),
('EMP011', '2026-10-17', '15:00:00', '22:00:00', 'Tarde'),
-- Semana 4
('EMP011', '2026-10-19', '14:00:00', '22:00:00', 'Tarde'),
('EMP011', '2026-10-20', '14:00:00', '22:00:00', 'Tarde'),
('EMP011', '2026-10-21', '14:00:00', '22:00:00', 'Tarde'),
('EMP011', '2026-10-23', '14:00:00', '22:00:00', 'Tarde'),
('EMP011', '2026-10-24', '15:00:00', '22:00:00', 'Tarde'),
-- Semana 5
('EMP011', '2026-10-26', '14:00:00', '22:00:00', 'Tarde'),
('EMP011', '2026-10-27', '14:00:00', '22:00:00', 'Tarde'),
('EMP011', '2026-10-28', '14:00:00', '22:00:00', 'Tarde'),
('EMP011', '2026-10-30', '14:00:00', '22:00:00', 'Tarde'),
('EMP011', '2026-10-31', '15:00:00', '22:00:00', 'Tarde');

-- Replicar para resto de grupo Tarde
INSERT INTO Horarios (IdEmpl, Fecha, HoraInicio, HoraFin, Turno)
SELECT 'EMP015', Fecha, HoraInicio, HoraFin, Turno FROM Horarios WHERE IdEmpl = 'EMP011';

INSERT INTO Horarios (IdEmpl, Fecha, HoraInicio, HoraFin, Turno)
SELECT 'EMP019', Fecha, HoraInicio, HoraFin, Turno FROM Horarios WHERE IdEmpl = 'EMP011';

INSERT INTO Horarios (IdEmpl, Fecha, HoraInicio, HoraFin, Turno)
SELECT 'EMP023', Fecha, HoraInicio, HoraFin, Turno FROM Horarios WHERE IdEmpl = 'EMP011';