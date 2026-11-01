-- Entrega 7C: configuracion_club (las sesiones del servidor viven en memoria y no tienen tablas).
CREATE TABLE configuracion_club (
    id        SMALLINT     PRIMARY KEY DEFAULT 1,
    nombre    VARCHAR(100) NOT NULL,
    direccion VARCHAR(150),
    telefono  VARCHAR(30),
    email     VARCHAR(120),
    CONSTRAINT ck_club_fila CHECK (id = 1)
);
