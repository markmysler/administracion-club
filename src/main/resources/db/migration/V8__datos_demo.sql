-- Datos de demostración (no forman parte del plan de 7C): un segundo administrador, categorías, tres grupos
-- familiares con sus socios, deportes, canchas, un feriado, cuotas del mes anterior y del actual, pagos y reservas.
-- Todas las cuentas de socio usan la clave ClubSocio2026Demo; el administrador "laura" usa ClubAdmin2026Demo.
-- Las fechas son relativas a la fecha en que se aplica la migración (CURRENT_DATE).

-- Categorías de socio (CU-06)
INSERT INTO categoria_socio (id, nombre, importe, activa) VALUES
    (1, 'Activo', 25000.00, true),
    (2, 'Cadete', 15000.00, true),
    (3, 'Infantil', 10000.00, true),
    (4, 'Vitalicio', 0.00, true),
    (5, 'Transitorio', 18000.00, false);

-- Segundo administrador
INSERT INTO usuario (id, username, email, password_hash, estado, intentos_fallidos) VALUES
    (2, 'laura', 'laura.gomez@club.local', '$2a$10$e0neVXWFYUvnryj8H7IG5uJmJakSAcGpJihRBCgq/iOKXyTVpAjD6', 'ACTIVA', 0);
INSERT INTO administrador (id, nombre, apellido) VALUES (2, 'Laura', 'Gómez');

-- Grupos familiares: se insertan no vigentes y se completan después de insertar los socios (ck_grupo_titular_vigente)
INSERT INTO grupo_familiar (id, titular_id, fecha_creacion, vigente) VALUES
    (1, NULL, CURRENT_DATE - 400, false),
    (2, NULL, CURRENT_DATE - 200, false),
    (3, NULL, CURRENT_DATE - 100, false);

-- Cuentas de socio (usuario + socio, herencia por tabla de subclase)
INSERT INTO usuario (id, username, email, password_hash, estado, intentos_fallidos) VALUES
    (10, 'juanperez',   'juan.perez@correo.local',   '$2a$10$ze7q4FYiw5MZm649qsSTweSH9bZQ2a3q05loioNePBsoXf4h.zz.G', 'ACTIVA', 0),
    (11, 'mariaperez',  'maria.perez@correo.local',  '$2a$10$ze7q4FYiw5MZm649qsSTweSH9bZQ2a3q05loioNePBsoXf4h.zz.G', 'ACTIVA', 0),
    (12, 'tomasperez',  'tomas.perez@correo.local',  '$2a$10$ze7q4FYiw5MZm649qsSTweSH9bZQ2a3q05loioNePBsoXf4h.zz.G', 'ACTIVA', 0),
    (13, 'anagarcia',   'ana.garcia@correo.local',   '$2a$10$ze7q4FYiw5MZm649qsSTweSH9bZQ2a3q05loioNePBsoXf4h.zz.G', 'ACTIVA', 0),
    (14, 'carloslopez', 'carlos.lopez@correo.local', '$2a$10$ze7q4FYiw5MZm649qsSTweSH9bZQ2a3q05loioNePBsoXf4h.zz.G', 'ACTIVA', 0),
    (15, 'lucialopez',  'lucia.lopez@correo.local',  '$2a$10$ze7q4FYiw5MZm649qsSTweSH9bZQ2a3q05loioNePBsoXf4h.zz.G', 'ACTIVA', 0),
    (16, 'pedroruiz',   'pedro.ruiz@correo.local',   '$2a$10$ze7q4FYiw5MZm649qsSTweSH9bZQ2a3q05loioNePBsoXf4h.zz.G', 'INACTIVA', 0);

INSERT INTO socio (id, dni, nombre, apellido, fecha_nacimiento, telefono, direccion, fecha_alta, categoria_id, grupo_familiar_id) VALUES
    (10, '20111222', 'Juan',   'Pérez',  '1980-03-15', '11 5555-1001', 'Calle 1 100, CABA', CURRENT_DATE - 400, 1, 1),
    (11, '22333444', 'María',  'Pérez',  '1982-07-20', '11 5555-1002', 'Calle 1 100, CABA', CURRENT_DATE - 400, 1, 1),
    (12, '45666777', 'Tomás',  'Pérez',  '2010-01-10', NULL,           'Calle 1 100, CABA', CURRENT_DATE - 400, 2, 1),
    (13, '30444555', 'Ana',    'García', '1990-11-02', '11 5555-2001', 'Av. 2 200, CABA',   CURRENT_DATE - 200, 1, 2),
    (14, '25777888', 'Carlos', 'López',  '1975-05-30', '11 5555-3001', 'Pasaje 3 300, CABA', CURRENT_DATE - 100, 1, 3),
    (15, '50888999', 'Lucía',  'López',  '2015-09-12', NULL,           'Pasaje 3 300, CABA', CURRENT_DATE - 100, 3, 3),
    (16, '28999000', 'Pedro',  'Ruiz',   '1985-02-14', '11 5555-4001', 'Calle 4 400, CABA', CURRENT_DATE - 300, 1, 2);

UPDATE grupo_familiar SET titular_id = 10, vigente = true WHERE id = 1;
UPDATE grupo_familiar SET titular_id = 13, vigente = true WHERE id = 2;
UPDATE grupo_familiar SET titular_id = 14, vigente = true WHERE id = 3;

-- Deportes y canchas (CU-18, CU-21)
INSERT INTO deporte (id, nombre, descripcion, activo) VALUES
    (1, 'Tenis',   'Tenis single y dobles', true),
    (2, 'Pádel',   'Pádel en parejas', true),
    (3, 'Fútbol 5', 'Fútbol de cinco jugadores', true);

INSERT INTO cancha (id, nombre, deporte_id, ubicacion, caracteristicas, activa) VALUES
    (1, 'Tenis 1',    1, 'Sector norte', 'Polvo de ladrillo, iluminación', true),
    (2, 'Tenis 2',    1, 'Sector norte', 'Cemento', true),
    (3, 'Pádel 1',    2, 'Gimnasio', 'Cristal, techada', true),
    (4, 'Fútbol 5 A', 3, 'Sector sur', 'Césped sintético, iluminación', true);

-- Feriado (CU-24)
INSERT INTO feriado (fecha, descripcion) VALUES (make_date(EXTRACT(YEAR FROM CURRENT_DATE)::int, 12, 25), 'Navidad');

-- Reservas (CU-27 a CU-33)
INSERT INTO reserva (id, socio_id, cancha_id, fecha, hora_inicio, hora_fin, estado, fecha_creacion, registrada_por_id, cancelada_por_id, motivo_cancelacion, version) VALUES
    (1, 10, 1, CURRENT_DATE + 2, '10:00', '11:00', 'CONFIRMADA', now() - interval '1 day', 10, NULL, NULL, 0),
    (2, 13, 3, CURRENT_DATE + 3, '18:00', '19:00', 'CONFIRMADA', now() - interval '2 days', 13, NULL, NULL, 0),
    (3, 14, 4, CURRENT_DATE + 1, '20:00', '21:00', 'CONFIRMADA', now() - interval '1 day', 1, NULL, NULL, 0),
    (4, 11, 2, CURRENT_DATE + 5, '09:00', '10:00', 'CONFIRMADA', now(), 11, NULL, NULL, 0),
    (5, 10, 2, CURRENT_DATE - 3, '17:00', '18:00', 'FINALIZADA', now() - interval '6 days', 10, NULL, NULL, 1),
    (6, 13, 1, CURRENT_DATE + 4, '12:00', '13:00', 'CANCELADA', now() - interval '3 days', 13, 13, 'No puedo asistir', 1);

-- Cuotas: mes anterior (grupo 1 pagada, grupo 2 vencida, grupo 3 pagada) y mes actual (pendientes)
INSERT INTO cuota (id, grupo_familiar_id, periodo, importe, credito_aplicado, fecha_emision, fecha_vencimiento, estado) VALUES
    (1, 1, to_char(CURRENT_DATE - interval '1 month', 'YYYY-MM'), 65000.00, 0, date_trunc('month', CURRENT_DATE - interval '1 month')::date, (date_trunc('month', CURRENT_DATE - interval '1 month') + interval '9 days')::date, 'PAGADA'),
    (2, 2, to_char(CURRENT_DATE - interval '1 month', 'YYYY-MM'), 50000.00, 0, date_trunc('month', CURRENT_DATE - interval '1 month')::date, (date_trunc('month', CURRENT_DATE - interval '1 month') + interval '9 days')::date, 'VENCIDA'),
    (3, 3, to_char(CURRENT_DATE - interval '1 month', 'YYYY-MM'), 35000.00, 0, date_trunc('month', CURRENT_DATE - interval '1 month')::date, (date_trunc('month', CURRENT_DATE - interval '1 month') + interval '9 days')::date, 'PAGADA'),
    (4, 1, to_char(CURRENT_DATE, 'YYYY-MM'), 65000.00, 0, date_trunc('month', CURRENT_DATE)::date, (date_trunc('month', CURRENT_DATE) + interval '9 days')::date, 'PENDIENTE'),
    (5, 2, to_char(CURRENT_DATE, 'YYYY-MM'), 50000.00, 0, date_trunc('month', CURRENT_DATE)::date, (date_trunc('month', CURRENT_DATE) + interval '9 days')::date, 'PENDIENTE'),
    (6, 3, to_char(CURRENT_DATE, 'YYYY-MM'), 35000.00, 0, date_trunc('month', CURRENT_DATE)::date, (date_trunc('month', CURRENT_DATE) + interval '9 days')::date, 'PENDIENTE');

INSERT INTO item_cuota (cuota_id, socio_id, categoria_id, importe) VALUES
    (1, 10, 1, 25000.00), (1, 11, 1, 25000.00), (1, 12, 2, 15000.00),
    (2, 13, 1, 25000.00), (2, 16, 1, 25000.00),
    (3, 14, 1, 25000.00), (3, 15, 3, 10000.00),
    (4, 10, 1, 25000.00), (4, 11, 1, 25000.00), (4, 12, 2, 15000.00),
    (5, 13, 1, 25000.00), (5, 16, 1, 25000.00),
    (6, 14, 1, 25000.00), (6, 15, 3, 10000.00);

-- Pagos del mes anterior: manual (grupo 1) y online simulado (grupo 3), con sus comprobantes (CU-36, CU-35, CU-37)
INSERT INTO pago (id, cuota_id, importe, fecha, estado, motivo_rechazo) VALUES
    (1, 1, 65000.00, now() - interval '25 days', 'APROBADO', NULL),
    (2, 3, 35000.00, now() - interval '24 days', 'APROBADO', NULL),
    (3, 2, 50000.00, now() - interval '20 days', 'RECHAZADO', 'Fondos insuficientes');
INSERT INTO pago_manual (id, medio_pago_id, registrado_por_id) VALUES (1, 1, 1);
INSERT INTO pago_online (id, referencia_externa, id_pago_pasarela, respuesta_pasarela, iniciado_por_id) VALUES
    (2, '7f8a1c2e-1111-4a2b-9c3d-000000000002', 'SIM-000002', '{"estado": "approved"}', 14),
    (3, '7f8a1c2e-1111-4a2b-9c3d-000000000003', 'SIM-000003', '{"estado": "rejected"}', 13);
INSERT INTO comprobante (pago_id, numero, fecha_emision, importe) VALUES
    (1, 'C-' || lpad(nextval('comprobante_numero_seq')::text, 8, '0'), now() - interval '25 days', 65000.00),
    (2, 'C-' || lpad(nextval('comprobante_numero_seq')::text, 8, '0'), now() - interval '24 days', 35000.00);

-- Auditoría de ejemplo
INSERT INTO registro_auditoria (fecha_hora, usuario_id, accion, entidad, entidad_id, detalle) VALUES
    (now() - interval '25 days', 1, 'PAGO', 'Pago', 1, '{"medio": "Efectivo"}');

SELECT setval('usuario_id_seq', 100);
SELECT setval('categoria_socio_id_seq', 100);
SELECT setval('grupo_familiar_id_seq', 100);
SELECT setval('deporte_id_seq', 100);
SELECT setval('cancha_id_seq', 100);
SELECT setval('reserva_id_seq', 100);
SELECT setval('cuota_id_seq', 100);
SELECT setval('pago_id_seq', 100);
