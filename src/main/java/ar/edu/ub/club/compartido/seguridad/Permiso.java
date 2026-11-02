package ar.edu.ub.club.compartido.seguridad;

/**
 * Enumeración de las operaciones del sistema que requieren autorización (no es un rol). Valores para Socio:
 * CONSULTAR_PERFIL_PROPIO, CONSULTAR_DISPONIBILIDAD, RESERVAR_ONLINE, GESTIONAR_RESERVAS_PROPIAS,
 * PAGAR_CUOTA_ONLINE, CONSULTAR_PAGOS_PROPIOS. Valores para Administrador: GESTIONAR_SOCIOS,
 * GESTIONAR_CATEGORIAS, GESTIONAR_GRUPOS, GESTIONAR_CUENTAS, GESTIONAR_RECURSOS, CONFIGURAR_AGENDA,
 * CONSULTAR_DISPONIBILIDAD, GESTIONAR_RESERVAS_CLUB, CONFIGURAR_PAGOS, REGISTRAR_PAGO_MANUAL,
 * CONSULTAR_PAGOS_CLUB, GENERAR_REPORTES, CONSULTAR_DASHBOARD, CONFIGURAR_CLUB.
 *
 * <p>Los conjuntos los devuelven {@code Socio.obtenerPermisos()} y {@code Administrador.obtenerPermisos()}
 * (polimorfismo): ninguna clase pregunta por el tipo de cuenta.
 *
 * <p>Casos de uso: todos
 */
public enum Permiso {
    /** Ver el perfil, el grupo y el estado de cuenta propios (CU-05). */
    CONSULTAR_PERFIL_PROPIO,
    /** Ver la grilla de turnos (CU-26). */
    CONSULTAR_DISPONIBILIDAD,
    /** Reservar una cancha online (CU-27). */
    RESERVAR_ONLINE,
    /** Ver y cancelar las reservas propias (CU-29, CU-30). */
    GESTIONAR_RESERVAS_PROPIAS,
    /** Pagar la cuota del grupo online (CU-35). */
    PAGAR_CUOTA_ONLINE,
    /** Ver los pagos y comprobantes del grupo propio (CU-37). */
    CONSULTAR_PAGOS_PROPIOS,
    /** Alta, modificación, baja y consulta de socios (CU-01 a CU-04). */
    GESTIONAR_SOCIOS,
    /** Configurar categorías de socio (CU-06). */
    GESTIONAR_CATEGORIAS,
    /** Gestionar grupos familiares (CU-07 a CU-10). */
    GESTIONAR_GRUPOS,
    /** Gestionar cuentas de acceso (CU-14 a CU-17). */
    GESTIONAR_CUENTAS,
    /** Gestionar deportes y canchas (CU-18 a CU-23). */
    GESTIONAR_RECURSOS,
    /** Configurar disponibilidad y reglas de reserva (CU-24, CU-25). */
    CONFIGURAR_AGENDA,
    /** Registrar, consultar, modificar y cancelar reservas del club (CU-28, CU-31 a CU-33). */
    GESTIONAR_RESERVAS_CLUB,
    /** Configurar medios de pago y pasarela (CU-34). */
    CONFIGURAR_PAGOS,
    /** Registrar pagos manuales (CU-36). */
    REGISTRAR_PAGO_MANUAL,
    /** Ver pagos y comprobantes de todo el club (CU-37). */
    CONSULTAR_PAGOS_CLUB,
    /** Generar reportes (CU-38, CU-39). */
    GENERAR_REPORTES,
    /** Ver el panel de administración (CU-40). */
    CONSULTAR_DASHBOARD,
    /** Configurar los datos del club (CU-41). */
    CONFIGURAR_CLUB
}
