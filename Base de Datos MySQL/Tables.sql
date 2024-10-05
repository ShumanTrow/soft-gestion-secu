CREATE DATABASE DB_IPASOFT;

-- Creación de la tabla Admin
CREATE TABLE Admin (
    id_admin INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    dni INT UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Creación de la tabla Socios
CREATE TABLE Socios (
    id_Socio INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    dni INT UNIQUE NOT NULL,
    telefono INT,
    direccion VARCHAR(255),
    email VARCHAR(255)
);

-- Creación de la tabla Alumnos
CREATE TABLE Alumnos (
    id_alumno INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    dni INT UNIQUE NOT NULL,
    telefono INT,
    direccion VARCHAR(255),
    curso VARCHAR(40), 
    socio_id INT,
    FOREIGN KEY (socio_id) REFERENCES Socios(id_Socio)
);

-- Creación de la tabla Cuotas
CREATE TABLE Cuotas (
    id_cuota INT PRIMARY KEY AUTO_INCREMENT,
    fechaCreacion DATE NOT NULL,
    monto DOUBLE NOT NULL,
    estado ENUM('pendiente', 'pagada', 'vencida') NOT NULL,
    socio_id INT,
    fechaVencimiento DATE NOT NULL,
    FOREIGN KEY (socio_id) REFERENCES Socios(id_Socio)
);

-- Creación de la tabla Notificaciones
CREATE TABLE Notificaciones (
    id_noti INT PRIMARY KEY,
    tipo ENUM('por_pagar', 'pagado', 'vencido') NOT NULL,
    destinatario INT,
    fechaEnvio DATE NOT NULL,
    mensaje TEXT NOT NULL,
    FOREIGN KEY (destinatario) REFERENCES Socios(id_Socio)
);



