-- Entrega 7C, sección 2: índices adicionales.
CREATE INDEX ix_reserva_cancha_fecha ON reserva (cancha_id, fecha);
CREATE INDEX ix_reserva_socio_estado ON reserva (socio_id, estado);
CREATE INDEX ix_cuota_estado_vencimiento ON cuota (estado, fecha_vencimiento);
CREATE INDEX ix_auditoria_entidad ON registro_auditoria (entidad, entidad_id);
CREATE INDEX ix_socio_grupo ON socio (grupo_familiar_id);
CREATE INDEX ix_item_cuota_cuota ON item_cuota (cuota_id);
CREATE INDEX ix_pago_cuota ON pago (cuota_id);
CREATE INDEX ix_notificacion_reserva ON notificacion (reserva_id);
CREATE INDEX ix_token_usuario ON token_recuperacion (usuario_id);
CREATE INDEX ix_token_sesion_usuario ON token_sesion_persistente (usuario_id);
CREATE INDEX ix_movimiento_saldo_grupo ON movimiento_saldo (grupo_familiar_id);
CREATE INDEX ix_pago_estado_fecha ON pago (estado, fecha);
