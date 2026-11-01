-- Entrega 7C: esquema, extensión btree_gist (restricción de exclusión de reservas) y numeración de comprobantes.
CREATE SCHEMA IF NOT EXISTS club;
CREATE EXTENSION IF NOT EXISTS btree_gist;
CREATE SEQUENCE club.comprobante_numero_seq START WITH 1 INCREMENT BY 1;
