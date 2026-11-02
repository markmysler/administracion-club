-- Entrega 7C: cuota, item_cuota, movimiento_saldo, pago (con ux_pago_aprobado_por_cuota, RN-18), pago_online,
-- pago_manual, pago_con_saldo, comprobante, medio_pago, configuracion_pasarela.

CREATE TABLE cuota (
    id                BIGSERIAL     PRIMARY KEY,
    grupo_familiar_id BIGINT        NOT NULL,
    periodo           CHAR(7)       NOT NULL,
    importe           NUMERIC(12,2) NOT NULL,
    credito_aplicado  NUMERIC(12,2) NOT NULL DEFAULT 0,
    fecha_emision     DATE          NOT NULL,
    fecha_vencimiento DATE          NOT NULL,
    estado            VARCHAR(10)   NOT NULL DEFAULT 'PENDIENTE',
    CONSTRAINT uk_cuota_grupo_periodo UNIQUE (grupo_familiar_id, periodo),
    CONSTRAINT ck_cuota_periodo CHECK (periodo ~ '^[0-9]{4}-(0[1-9]|1[0-2])$'),
    CONSTRAINT ck_cuota_estado CHECK (estado IN ('PENDIENTE', 'VENCIDA', 'PAGADA')),
    CONSTRAINT ck_cuota_fechas CHECK (fecha_vencimiento >= fecha_emision),
    CONSTRAINT ck_cuota_importe CHECK (importe >= 0),
    CONSTRAINT ck_cuota_credito CHECK (credito_aplicado BETWEEN 0 AND importe),
    CONSTRAINT fk_cuota_grupo FOREIGN KEY (grupo_familiar_id) REFERENCES grupo_familiar (id)
);

CREATE TABLE item_cuota (
    id           BIGSERIAL     PRIMARY KEY,
    cuota_id     BIGINT        NOT NULL,
    socio_id     BIGINT        NOT NULL,
    categoria_id BIGINT        NOT NULL,
    importe      NUMERIC(12,2) NOT NULL,
    CONSTRAINT uk_item_cuota_socio UNIQUE (cuota_id, socio_id),
    CONSTRAINT ck_item_importe CHECK (importe >= 0),
    CONSTRAINT fk_item_cuota_cuota FOREIGN KEY (cuota_id) REFERENCES cuota (id),
    CONSTRAINT fk_item_cuota_socio FOREIGN KEY (socio_id) REFERENCES socio (id),
    CONSTRAINT fk_item_cuota_categoria FOREIGN KEY (categoria_id) REFERENCES categoria_socio (id)
);

CREATE TABLE medio_pago (
    id         BIGSERIAL   PRIMARY KEY,
    nombre     VARCHAR(40) NOT NULL,
    habilitado BOOLEAN     NOT NULL DEFAULT true,
    CONSTRAINT uk_medio_pago_nombre UNIQUE (nombre)
);

CREATE TABLE pago (
    id             BIGSERIAL     PRIMARY KEY,
    cuota_id       BIGINT        NOT NULL,
    importe        NUMERIC(12,2) NOT NULL,
    fecha          TIMESTAMPTZ   NOT NULL DEFAULT now(),
    estado         VARCHAR(10)   NOT NULL,
    motivo_rechazo VARCHAR(100),
    CONSTRAINT ck_pago_estado CHECK (estado IN ('PENDIENTE', 'APROBADO', 'RECHAZADO', 'ACREDITADO', 'VENCIDO')),
    CONSTRAINT ck_pago_importe CHECK (importe > 0),
    CONSTRAINT fk_pago_cuota FOREIGN KEY (cuota_id) REFERENCES cuota (id)
);

CREATE UNIQUE INDEX ux_pago_aprobado_por_cuota ON pago (cuota_id) WHERE estado = 'APROBADO';

CREATE TABLE pago_online (
    id                 BIGINT      PRIMARY KEY,
    referencia_externa UUID        NOT NULL,
    id_pago_pasarela   VARCHAR(40),
    respuesta_pasarela JSONB,
    iniciado_por_id    BIGINT      NOT NULL,
    CONSTRAINT fk_pago_online_pago FOREIGN KEY (id) REFERENCES pago (id),
    CONSTRAINT uk_pago_online_referencia UNIQUE (referencia_externa),
    CONSTRAINT uk_pago_online_id_pasarela UNIQUE (id_pago_pasarela),
    CONSTRAINT fk_pago_online_socio FOREIGN KEY (iniciado_por_id) REFERENCES socio (id)
);

CREATE TABLE pago_manual (
    id                BIGINT PRIMARY KEY,
    medio_pago_id     BIGINT NOT NULL,
    registrado_por_id BIGINT NOT NULL,
    CONSTRAINT fk_pago_manual_pago FOREIGN KEY (id) REFERENCES pago (id),
    CONSTRAINT fk_pago_manual_medio FOREIGN KEY (medio_pago_id) REFERENCES medio_pago (id),
    CONSTRAINT fk_pago_manual_admin FOREIGN KEY (registrado_por_id) REFERENCES administrador (id)
);

CREATE TABLE pago_con_saldo (
    id BIGINT PRIMARY KEY,
    CONSTRAINT fk_pago_con_saldo_pago FOREIGN KEY (id) REFERENCES pago (id)
);

CREATE TABLE movimiento_saldo (
    id                BIGSERIAL     PRIMARY KEY,
    grupo_familiar_id BIGINT        NOT NULL,
    fecha             TIMESTAMPTZ   NOT NULL DEFAULT now(),
    tipo              VARCHAR(7)    NOT NULL,
    importe           NUMERIC(12,2) NOT NULL,
    motivo            VARCHAR(100)  NOT NULL,
    pago_id           BIGINT,
    cuota_id          BIGINT,
    CONSTRAINT fk_movimiento_grupo FOREIGN KEY (grupo_familiar_id) REFERENCES grupo_familiar (id),
    CONSTRAINT fk_movimiento_pago FOREIGN KEY (pago_id) REFERENCES pago (id),
    CONSTRAINT fk_movimiento_cuota FOREIGN KEY (cuota_id) REFERENCES cuota (id),
    CONSTRAINT ck_movimiento_tipo CHECK (tipo IN ('CREDITO', 'DEBITO')),
    CONSTRAINT ck_movimiento_importe CHECK (importe > 0),
    CONSTRAINT ck_movimiento_origen CHECK ((tipo = 'CREDITO' AND pago_id IS NOT NULL AND cuota_id IS NULL)
        OR (tipo = 'DEBITO' AND cuota_id IS NOT NULL AND pago_id IS NULL))
);

CREATE TABLE comprobante (
    id            BIGSERIAL     PRIMARY KEY,
    pago_id       BIGINT        NOT NULL,
    numero        VARCHAR(20)   NOT NULL,
    fecha_emision TIMESTAMPTZ   NOT NULL DEFAULT now(),
    importe       NUMERIC(12,2) NOT NULL,
    CONSTRAINT uk_comprobante_pago UNIQUE (pago_id),
    CONSTRAINT uk_comprobante_numero UNIQUE (numero),
    CONSTRAINT ck_comprobante_importe CHECK (importe > 0),
    CONSTRAINT fk_comprobante_pago FOREIGN KEY (pago_id) REFERENCES pago (id)
);

CREATE TABLE configuracion_pasarela (
    id                        SMALLINT    PRIMARY KEY DEFAULT 1,
    proveedor                 VARCHAR(40) NOT NULL,
    modo                      VARCHAR(15) NOT NULL DEFAULT 'SIMULADA',
    habilitada                BOOLEAN     NOT NULL DEFAULT false,
    credenciales_configuradas BOOLEAN     NOT NULL DEFAULT false,
    CONSTRAINT ck_pasarela_fila CHECK (id = 1),
    CONSTRAINT ck_pasarela_modo CHECK (modo IN ('SIMULADA', 'MERCADO_PAGO')),
    CONSTRAINT ck_pasarela_credenciales CHECK (modo = 'SIMULADA' OR NOT habilitada OR credenciales_configuradas)
);
