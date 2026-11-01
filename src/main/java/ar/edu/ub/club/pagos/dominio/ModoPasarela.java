package ar.edu.ub.club.pagos.dominio;

/**
 * Adaptador de pasarela activo (Adapter): SIMULADA usa {@code PasarelaSimuladaAdapter} (entorno académico, sin
 * dinero real, valor por omisión) y MERCADO_PAGO usa {@code MercadoPagoAdapter} (requiere credenciales). Se guarda
 * por nombre en {@code configuracion_pasarela.modo}.
 *
 * <p>Casos de uso: CU-34, CU-35
 */
public enum ModoPasarela {

    /** Valor SIMULADA. */
    SIMULADA,
    /** Valor MERCADO_PAGO. */
    MERCADO_PAGO;
}
