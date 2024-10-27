-- 1. Obtener todos los alumnos y el socio asociado a cada uno
SELECT a.nombre AS alumno, s.nombre AS socio
FROM Alumnos a
JOIN Socios s ON a.socio_id = s.id_Socio;

-- 2. Ver todas las cuotas pendientes de pago
SELECT c.id_cuota, c.monto, s.nombre AS socio, c.fechaVencimiento
FROM Cuotas c
JOIN Socios s ON c.socio_id = s.id_Socio
WHERE c.estado = 'pendiente';

-- 3. Listar todos los alumnos de un socio específico
SELECT a.nombre, a.curso
FROM Alumnos a
JOIN Socios s ON a.socio_id = s.id_Socio
WHERE s.nombre = 'Pedro Sanchez';

-- 4. Obtener el total de cuotas pagadas y pendientes de un socio
SELECT s.nombre, 
  SUM(CASE WHEN c.estado = 'pagada' THEN c.monto ELSE 0 END) AS total_pagado,
  SUM(CASE WHEN c.estado = 'pendiente' THEN c.monto ELSE 0 END) AS total_pendiente
FROM Cuotas c
JOIN Socios s ON c.socio_id = s.id_Socio
GROUP BY s.nombre;

-- 5. Buscar alumnos por DNI
SELECT * 
FROM Alumnos 
WHERE dni = 22222222;

-- 6. Ver las notificaciones enviadas a un socio específico
SELECT n.tipo, n.fechaEnvio, n.mensaje
FROM Notificaciones n
JOIN Socios s ON n.destinatario = s.id_Socio
WHERE s.nombre = 'Marta Herrera';

-- 7. Listar todos los socios que tienen cuotas vencidas
SELECT s.nombre, c.fechaVencimiento
FROM Cuotas c
JOIN Socios s ON c.socio_id = s.id_Socio
WHERE c.estado = 'vencida';

-- 8. Consultar todas las cuotas generadas para un alumno en particular
SELECT c.id_cuota, c.monto, c.estado, c.fechaVencimiento
FROM Cuotas c
JOIN Alumnos a ON c.socio_id = a.socio_id
WHERE a.nombre = 'Juanito Sánchez';

-- 9. Ver todas las notificaciones enviadas entre dos fechas específicas
SELECT n.id_noti, n.tipo, s.nombre AS destinatario, n.fechaEnvio
FROM Notificaciones n
JOIN Socios s ON n.destinatario = s.id_Socio
WHERE n.fechaEnvio BETWEEN '2024-01-01' AND '2024-02-01';

-- 10. Listar todos los administradores del sistema
SELECT * 
FROM Admin;
