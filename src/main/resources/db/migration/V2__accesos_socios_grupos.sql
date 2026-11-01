-- Entrega 7C: usuario, administrador, categoria_socio, grupo_familiar, socio, token_recuperacion,
-- token_sesion_persistente, registro_auditoria. Herencia por tabla de subclase (usuario + socio/administrador),
-- sin columna de rol.

CREATE TABLE usuario (
    id                BIGSERIAL    PRIMARY KEY,
    username          VARCHAR(50)  NOT NULL,
    email             VARCHAR(120) NOT NULL,
    password_hash     VARCHAR(100) NOT NULL,
    estado            VARCHAR(10)  NOT NULL DEFAULT 'ACTIVA',
    intentos_fallidos SMALLINT     NOT NULL DEFAULT 0,
    bloqueado_hasta   TIMESTAMPTZ,
    fecha_creacion    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT uk_usuario_username UNIQUE (username),
    CONSTRAINT uk_usuario_email UNIQUE (email),
    CONSTRAINT ck_usuario_estado CHECK (estado IN ('ACTIVA', 'INACTIVA')),
    CONSTRAINT ck_usuario_intentos CHECK (intentos_fallidos BETWEEN 0 AND 5)
);

CREATE TABLE administrador (
    id       BIGINT      PRIMARY KEY,
    nombre   VARCHAR(60) NOT NULL,
    apellido VARCHAR(60) NOT NULL,
    CONSTRAINT fk_administrador_usuario FOREIGN KEY (id) REFERENCES usuario (id)
);

CREATE TABLE categoria_socio (
    id      BIGSERIAL     PRIMARY KEY,
    nombre  VARCHAR(40)   NOT NULL,
    importe NUMERIC(12,2) NOT NULL,
    activa  BOOLEAN       NOT NULL DEFAULT true,
    CONSTRAINT uk_categoria_nombre UNIQUE (nombre),
    CONSTRAINT ck_categoria_importe CHECK (importe >= 0)
);

CREATE TABLE grupo_familiar (
    id             BIGSERIAL PRIMARY KEY,
    titular_id     BIGINT,
    fecha_creacion DATE      NOT NULL DEFAULT CURRENT_DATE,
    vigente        BOOLEAN   NOT NULL DEFAULT true,
    -- PostgreSQL no admite CHECK diferibles: en el alta de un socio el grupo se inserta con vigente = false y
    -- se actualiza (titular y vigente) después de insertar el socio, en la misma transacción.
    CONSTRAINT ck_grupo_titular_vigente CHECK (NOT vigente OR titular_id IS NOT NULL)
);

CREATE TABLE socio (
    id                BIGINT       PRIMARY KEY,
    dni               VARCHAR(10)  NOT NULL,
    nombre            VARCHAR(60)  NOT NULL,
    apellido          VARCHAR(60)  NOT NULL,
    fecha_nacimiento  DATE         NOT NULL,
    telefono          VARCHAR(30),
    direccion         VARCHAR(150),
    fecha_alta        DATE         NOT NULL DEFAULT CURRENT_DATE,
    categoria_id      BIGINT       NOT NULL,
    grupo_familiar_id BIGINT       NOT NULL,
    CONSTRAINT fk_socio_usuario FOREIGN KEY (id) REFERENCES usuario (id),
    CONSTRAINT uk_socio_dni UNIQUE (dni),
    CONSTRAINT ck_socio_dni CHECK (dni ~ '^[0-9]{7,8}$'),
    CONSTRAINT fk_socio_categoria FOREIGN KEY (categoria_id) REFERENCES categoria_socio (id),
    CONSTRAINT fk_socio_grupo FOREIGN KEY (grupo_familiar_id) REFERENCES grupo_familiar (id)
);

ALTER TABLE grupo_familiar
    ADD CONSTRAINT fk_grupo_titular FOREIGN KEY (titular_id) REFERENCES socio (id) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE token_recuperacion (
    id         BIGSERIAL    PRIMARY KEY,
    usuario_id BIGINT       NOT NULL,
    token_hash VARCHAR(100) NOT NULL,
    expira_en  TIMESTAMPTZ  NOT NULL,
    usado      BOOLEAN      NOT NULL DEFAULT false,
    CONSTRAINT uk_token_hash UNIQUE (token_hash),
    CONSTRAINT fk_token_recuperacion_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);

CREATE TABLE token_sesion_persistente (
    serie      VARCHAR(64)  NOT NULL,
    usuario_id BIGINT       NOT NULL,
    token_hash VARCHAR(100) NOT NULL,
    ultimo_uso TIMESTAMPTZ  NOT NULL DEFAULT now(),
    expira_en  TIMESTAMPTZ  NOT NULL,
    CONSTRAINT pk_token_sesion PRIMARY KEY (serie),
    CONSTRAINT fk_token_sesion_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT ck_token_sesion_expira CHECK (expira_en > ultimo_uso)
);

CREATE TABLE registro_auditoria (
    id         BIGSERIAL   PRIMARY KEY,
    fecha_hora TIMESTAMPTZ NOT NULL DEFAULT now(),
    usuario_id BIGINT,
    accion     VARCHAR(30) NOT NULL,
    entidad    VARCHAR(40) NOT NULL,
    entidad_id BIGINT      NOT NULL,
    detalle    JSONB,
    CONSTRAINT fk_auditoria_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);
