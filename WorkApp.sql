DROP DATABASE IF EXISTS WorkApp;

CREATE DATABASE WorkApp;

USE WorkApp;

CREATE TABLE Usuarios (
    idEmpl VARCHAR(20) PRIMARY KEY,
    Usuario VARCHAR(100) NOT NULL,
    Loggin VARCHAR(50) NOT NULL,
    Nombre VARCHAR(50) NOT NULL,
    Apellido VARCHAR(50) NOT NULL,
    Email VARCHAR(50) NOT NULL CHECK (Email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$'),
    Dni VARCHAR(9) NOT NULL UNIQUE CHECK (Dni REGEXP '^[0-9]{8}[A-Za-z]$'),
    Direccion VARCHAR(150) NOT NULL,
    Tlfn VARCHAR(15) NOT NULL CHECK (Tlfn REGEXP '^\\+?[0-9]{8,15}$'),
    Puesto VARCHAR(50) NOT NULL,
    Rol VARCHAR(50) NOT NULL,
    Password VARCHAR(100) NOT NULL,
    IdSuperior VARCHAR(20) DEFAULT NULL,

    FOREIGN KEY (IdSuperior) REFERENCES Usuarios(idEmpl) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE Horarios (
    IdHorario INT AUTO_INCREMENT PRIMARY KEY,
    IdEmpl VARCHAR(20) NOT NULL,
    Fecha DATE NOT NULL,
    HoraInicio TIME NOT NULL,
    HoraFin TIME NOT NULL,
    Turno VARCHAR(20) NOT NULL,

    FOREIGN KEY (IdEmpl)REFERENCES Usuarios(idEmpl) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO Usuarios (idEmpl, Usuario, Loggin, Nombre, Apellido, Email, Dni, Direccion, Tlfn, Puesto, Rol, Password, IdSuperior) VALUES

('E007', 'marta', 'marta',
 'Marta', 'Gómez',
 'marta.gomez@workapp.com',
 '12345678A',
 'Calle Mayor 1, Bilbao',
 '600111111',
 'Jefa de área',
 'JEFE',
 'marta123',
 NULL),

('E004', 'carlos', 'carlos',
 'Carlos', 'Fernández',
 'carlos.fernandez@workapp.com',
 '23456789B',
 'Calle Autonomía 25, Bilbao',
 '600222222',
 'Gerente',
 'GERENTE',
 'carlos123',
 'E007'),

('E002', 'laura', 'laura',
 'Laura', 'Martínez',
 'laura.martinez@workapp.com',
 '34567890C',
 'Calle Iparraguirre 12, Bilbao',
 '600333333',
 'Encargada',
 'ENCARGADO',
 'laura123',
 'E004'),

('E005', 'ana', 'ana',
 'Ana', 'Rodríguez',
 'ana.rodriguez@workapp.com',
 '45678901D',
 'Calle Ercilla 18, Bilbao',
 '600444444',
 'Encargada',
 'ENCARGADO',
 'ana123',
 'E004'),

('E001', 'iker', 'iker',
 'Iker', 'Bosquez',
 'iker.bosquez@workapp.com',
 '56789012E',
 'Calle San Martín 10, Bilbao',
 '600555555',
 'Empleado',
 'EMPLEADO',
 'iker123',
 'E002'),

('E006', 'javier', 'javier',
 'Javier', 'López',
 'javier.lopez@workapp.com',
 '67890123F',
 'Calle Hurtado de Amézaga 20, Bilbao',
 '600666666',
 'Empleado',
 'EMPLEADO',
 'javier123',
 'E005'),

('E003', 'marcos', 'marcos',
 'Marcos', 'Sánchez',
 'marcos.sanchez@workapp.com',
 '78901234G',
 'Calle Deusto 15, Bilbao',
 '600777777',
 'Empleado',
 'EMPLEADO',
 'marcos123',
 'E002');

INSERT INTO Horarios (IdEmpl, Fecha, HoraInicio, HoraFin, Turno) VALUES

-- Iker
('E001', '2026-10-05', '08:00:00', '16:00:00', 'MAÑANA'),
('E001', '2026-10-06', '08:00:00', '16:00:00', 'MAÑANA'),
('E001', '2026-10-07', '08:00:00', '16:00:00', 'MAÑANA'),

-- Marcos
('E003', '2026-10-05', '09:00:00', '17:00:00', 'MAÑANA'),
('E003', '2026-10-06', '09:00:00', '17:00:00', 'MAÑANA'),
('E003', '2026-10-07', '09:00:00', '17:00:00', 'MAÑANA'),

-- Javier
('E006', '2026-10-05', '14:00:00', '22:00:00', 'TARDE'),
('E006', '2026-10-06', '14:00:00', '22:00:00', 'TARDE'),
('E006', '2026-10-07', '14:00:00', '22:00:00', 'TARDE'),

-- Laura
('E002', '2026-10-05', '08:00:00', '16:00:00', 'MAÑANA'),
('E002', '2026-10-06', '08:00:00', '16:00:00', 'MAÑANA'),

-- Ana
('E005', '2026-10-05', '09:00:00', '17:00:00', 'MAÑANA'),
('E005', '2026-10-06', '09:00:00', '17:00:00', 'MAÑANA'),

-- Carlos
('E004', '2026-10-05', '08:00:00', '17:00:00', 'MAÑANA'),
('E004', '2026-10-06', '08:00:00', '17:00:00', 'MAÑANA'),

-- Marta
('E007', '2026-10-05', '08:00:00', '17:00:00', 'MAÑANA'),
('E007', '2026-10-06', '08:00:00', '17:00:00', 'MAÑANA');