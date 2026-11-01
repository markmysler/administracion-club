-- Entrega 7C: filas únicas de configuración, medios de pago iniciales y la cuenta del primer administrador.
-- Clave del administrador inicial: ClubAdmin2026Demo (cambiarla en producción).

INSERT INTO configuracion_club (id, nombre, direccion, telefono, email)
VALUES (1, 'Club Social y Deportivo Ejemplo', 'Av. Siempre Viva 742, CABA', '+54 11 4000-0000', 'contacto@club.local');

INSERT INTO configuracion_pasarela (id, proveedor, modo, habilitada, credenciales_configuradas)
VALUES (1, 'Mercado Pago', 'SIMULADA', true, false);

INSERT INTO disponibilidad_club (id, hora_apertura, hora_cierre) VALUES (1, '08:00', '22:00');
INSERT INTO dia_atencion (disponibilidad_id, dia) VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7);

INSERT INTO regla_reserva (id, anticipacion_minima_horas, anticipacion_maxima_dias, duracion_turno_minutos,
                           limite_reservas_vigentes, anticipacion_min_cancelacion_horas,
                           anticipacion_min_modificacion_horas)
VALUES (1, 2, 14, 60, 3, 4, 4);

INSERT INTO medio_pago (nombre, habilitado) VALUES ('Efectivo', true), ('Transferencia bancaria', true),
                                                   ('Tarjeta de débito', true);

INSERT INTO usuario (id, username, email, password_hash, estado, intentos_fallidos)
VALUES (1, 'admin', 'admin@club.local', '$2a$10$e0neVXWFYUvnryj8H7IG5uJmJakSAcGpJihRBCgq/iOKXyTVpAjD6', 'ACTIVA', 0);
INSERT INTO administrador (id, nombre, apellido) VALUES (1, 'Administrador', 'Inicial');

SELECT setval('usuario_id_seq', 1);
