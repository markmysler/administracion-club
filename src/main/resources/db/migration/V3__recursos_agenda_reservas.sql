-- Entrega 7C: deporte, cancha, disponibilidad_club, dia_atencion, feriado, regla_reserva, reserva (con la
-- restricción de exclusión contra la doble reserva) y notificacion.

CREATE TABLE deporte (
    id          BIGSERIAL    PRIMARY KEY,
    nombre      VARCHAR(40)  NOT NULL,
    descripcion VARCHAR(200),
    activo      BOOLEAN      NOT NULL DEFAULT true,
    CONSTRAINT uk_deporte_nombre UNIQUE (nombre)
);

CREATE TABLE cancha (
    id              BIGSERIAL    PRIMARY KEY,
    nombre          VARCHAR(40)  NOT NULL,
    deporte_id      BIGINT       NOT NULL,
    ubicacion       VARCHAR(100),
    caracteristicas VARCHAR(200),
    activa          BOOLEAN      NOT NULL DEFAULT true,
    CONSTRAINT uk_cancha_nombre UNIQUE (nombre),
    CONSTRAINT fk_cancha_deporte FOREIGN KEY (deporte_id) REFERENCES deporte (id)
);

CREATE TABLE disponibilidad_club (
    id            SMALLINT PRIMARY KEY DEFAULT 1,
    hora_apertura TIME     NOT NULL,
    hora_cierre   TIME     NOT NULL,
    CONSTRAINT ck_disponibilidad_fila CHECK (id = 1),
    CONSTRAINT ck_disponibilidad_horario CHECK (hora_cierre > hora_apertura)
);

CREATE TABLE dia_atencion (
    disponibilidad_id SMALLINT NOT NULL,
    dia               SMALLINT NOT NULL,
    CONSTRAINT pk_dia_atencion PRIMARY KEY (disponibilidad_id, dia),
    CONSTRAINT ck_dia_atencion_dia CHECK (dia BETWEEN 1 AND 7),
    CONSTRAINT fk_dia_atencion_disponibilidad FOREIGN KEY (disponibilidad_id) REFERENCES disponibilidad_club (id)
);

CREATE TABLE feriado (
    id          BIGSERIAL    PRIMARY KEY,
    fecha       DATE         NOT NULL,
    descripcion VARCHAR(100) NOT NULL,
    CONSTRAINT uk_feriado_fecha UNIQUE (fecha)
);

CREATE TABLE regla_reserva (
    id                                  SMALLINT PRIMARY KEY DEFAULT 1,
    anticipacion_minima_horas           SMALLINT NOT NULL,
    anticipacion_maxima_dias            SMALLINT NOT NULL,
    duracion_turno_minutos              SMALLINT NOT NULL,
    limite_reservas_vigentes            SMALLINT NOT NULL,
    anticipacion_min_cancelacion_horas  SMALLINT NOT NULL,
    anticipacion_min_modificacion_horas SMALLINT NOT NULL,
    CONSTRAINT ck_regla_fila CHECK (id = 1),
    CONSTRAINT ck_regla_positivos CHECK (anticipacion_minima_horas > 0 AND anticipacion_maxima_dias > 0
        AND duracion_turno_minutos > 0 AND limite_reservas_vigentes > 0
        AND anticipacion_min_cancelacion_horas > 0 AND anticipacion_min_modificacion_horas > 0)
);

CREATE TABLE reserva (
    id                 BIGSERIAL    PRIMARY KEY,
    socio_id           BIGINT       NOT NULL,
    cancha_id          BIGINT       NOT NULL,
    fecha              DATE         NOT NULL,
    hora_inicio        TIME         NOT NULL,
    hora_fin           TIME         NOT NULL,
    estado             VARCHAR(12)  NOT NULL DEFAULT 'CONFIRMADA',
    fecha_creacion     TIMESTAMPTZ  NOT NULL DEFAULT now(),
    registrada_por_id  BIGINT       NOT NULL,
    cancelada_por_id   BIGINT,
    motivo_cancelacion VARCHAR(200),
    version            INTEGER      NOT NULL DEFAULT 0,
    CONSTRAINT ck_reserva_horario CHECK (hora_fin > hora_inicio),
    CONSTRAINT ck_reserva_estado CHECK (estado IN ('CONFIRMADA', 'CANCELADA', 'FINALIZADA')),
    CONSTRAINT ck_reserva_cancelada CHECK (estado <> 'CANCELADA' OR cancelada_por_id IS NOT NULL),
    CONSTRAINT ck_reserva_version CHECK (version >= 0),
    CONSTRAINT fk_reserva_socio FOREIGN KEY (socio_id) REFERENCES socio (id),
    CONSTRAINT fk_reserva_cancha FOREIGN KEY (cancha_id) REFERENCES cancha (id),
    CONSTRAINT fk_reserva_registrada_por FOREIGN KEY (registrada_por_id) REFERENCES usuario (id),
    CONSTRAINT fk_reserva_cancelada_por FOREIGN KEY (cancelada_por_id) REFERENCES usuario (id),
    CONSTRAINT ex_reserva_sin_superposicion EXCLUDE USING gist (
        cancha_id WITH =,
        tsrange(fecha + hora_inicio, fecha + hora_fin, '[)') WITH &&
    ) WHERE (estado = 'CONFIRMADA')
);

CREATE TABLE notificacion (
    id            BIGSERIAL    PRIMARY KEY,
    socio_id      BIGINT       NOT NULL,
    reserva_id    BIGINT       NOT NULL,
    tipo          VARCHAR(12)  NOT NULL,
    mensaje       TEXT         NOT NULL,
    fecha         TIMESTAMPTZ  NOT NULL DEFAULT now(),
    estado_envio  VARCHAR(10)  NOT NULL DEFAULT 'PENDIENTE',
    detalle_error VARCHAR(500),
    CONSTRAINT ck_notificacion_tipo CHECK (tipo IN ('CONFIRMACION', 'MODIFICACION', 'CANCELACION')),
    CONSTRAINT ck_notificacion_envio CHECK (estado_envio IN ('PENDIENTE', 'ENVIADA', 'FALLIDA')),
    CONSTRAINT fk_notificacion_socio FOREIGN KEY (socio_id) REFERENCES socio (id),
    CONSTRAINT fk_notificacion_reserva FOREIGN KEY (reserva_id) REFERENCES reserva (id)
);
