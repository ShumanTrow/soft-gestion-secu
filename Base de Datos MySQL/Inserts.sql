-- Insertar registros en la tabla Admin
INSERT INTO Admin (id_admin, nombre, dni, password) VALUES
(1, 'Juan Perez', 12345678, 'admin123'),
(2, 'Maria Gomez', 87654321, 'admin456');

-- Insertar registros en la tabla Socios
INSERT INTO Socios (id_Socio, nombre, dni, telefono, direccion, email) VALUES
(1, 'Pedro Sanchez', 12312312, 1234567890, 'Calle Falsa 123', 'pedro.sanchez@mail.com'),
(2, 'Laura Fernández', 32132132, 0987654321, 'Avenida Siempreviva 742', 'laura.fernandez@mail.com'),
(3, 'Andrés García', 45645645, 1122334455, 'Boulevard de los Sueños 555', 'andres.garcia@mail.com'),
(4, 'Marta Herrera', 65465465, 2233445566, 'Calle Luna 678', 'marta.herrera@mail.com'),
(5, 'David Jimenez', 78978978, 3344556677, 'Plaza del Sol 999', 'david.jimenez@mail.com');

-- Insertar registros en la tabla Alumnos
INSERT INTO Alumnos (id_alumno, nombre, dni, telefono, direccion, curso, socio_id) VALUES
(1, 'Juanito Sánchez', 22222222, 1111222233, 'Calle Falsa 123', '2A', 1),
(2, 'Anita Fernández', 33333333, 4444555566, 'Avenida Siempreviva 742', '3B', 2),
(3, 'Carlos García', 44444444, 5555666677, 'Boulevard de los Sueños 555', '4C', 3),
(4, 'Marta Jimenez', 55555555, 6666777788, 'Calle Luna 678', '1A', 4),
(5, 'David Herrera', 66666666, 7777888899, 'Plaza del Sol 999', '5A', 5);

-- Insertar registros en la tabla Cuotas
INSERT INTO Cuotas (id_cuota, fechaCreacion, monto, estado, socio_id, fechaVencimiento) VALUES
(1, '2024-01-01', 1000.00, 'pendiente', 1, '2024-02-10'),
(2, '2024-01-01', 1500.00, 'pendiente', 2, '2024-02-10'),
(3, '2024-01-01', 1300.00, 'pagada', 3, '2024-02-10'),
(4, '2024-01-01', 1600.00, 'vencida', 4, '2024-02-10'),
(5, '2024-01-01', 1200.00, 'pendiente', 5, '2024-02-10');

-- Insertar registros en la tabla Notificaciones
INSERT INTO Notificaciones (id_noti, tipo, destinatario, fechaEnvio, mensaje) VALUES
(1, 'Cuota Pendiente', 1, '2024-01-15', 'Estimado Pedro, tiene una cuota pendiente por pagar.'),
(2, 'Cuota Pendiente', 2, '2024-01-15', 'Estimada Laura, tiene una cuota pendiente por pagar.'),
(3, 'Cuota Vencida', 4, '2024-02-11', 'Estimada Marta, su cuota ha vencido, por favor realice el pago.'),
(4, 'Cuota Pagada', 3, '2024-01-05', 'Estimado Andrés, su cuota ha sido pagada. Gracias.'),
(5, 'Cuota Pendiente', 5, '2024-01-15', 'Estimado David, tiene una cuota pendiente por pagar.');
