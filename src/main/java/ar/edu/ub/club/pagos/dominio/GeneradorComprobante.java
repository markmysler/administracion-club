package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.pagos.persistencia.SiguienteNumeroComprobante;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

/**
 * Servicio de dominio (pagos.dominio) que emite el comprobante de un pago aprobado o la constancia de un pago
 * acreditado, en la misma transacción. La numeración correlativa sale de la secuencia {@code
 * comprobante_numero_seq} (sin huecos visibles por concurrencia: la secuencia nunca repite).
 *
 * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
 */
@Component
public class GeneradorComprobante {

    /** Número correlativo y guardado del comprobante. */
    private final FachadaPersistencia fachadaPersistencia;

    /**
     * Crea el generador.
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param fachadaPersistencia fachada de persistencia
     */
    public GeneradorComprobante(FachadaPersistencia fachadaPersistencia) {
        this.fachadaPersistencia = fachadaPersistencia;
    }

    /**
     * Obtiene el número con {@code fachadaPersistencia.buscarUno(SiguienteNumeroComprobante())}, crea el {@code
     * Comprobante}, lo asocia con {@code p.asociarComprobante(c)} y lo guarda con {@code
     * fachadaPersistencia.put(c)}. Si el pago ya tiene comprobante lo devuelve (UNIQUE {@code pago_id}).
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param p Pago aprobado o acreditado.
     * @return Comprobante emitido.
     * @throws IllegalStateException si el pago no está aprobado ni acreditado
     */
    public Comprobante generar(Pago p) {
        if (p.getComprobante() != null) {
            return p.getComprobante();
        }
        if (!(p.getEstado() instanceof EstadoPagoAprobado) && !(p.getEstado() instanceof EstadoPagoAcreditado)) {
            throw new IllegalStateException("Solo un pago aprobado o acreditado tiene comprobante");
        }
        Long siguiente = fachadaPersistencia.buscarUno(new SiguienteNumeroComprobante())
                .orElseThrow(() -> new IllegalStateException("La secuencia de comprobantes no devolvió un número"));
        Comprobante c = new Comprobante(p, formatear(siguiente), LocalDateTime.now(RelojSistema.ZONA_CLUB), p.getImporte());
        p.asociarComprobante(c);
        fachadaPersistencia.put(c);
        return c;
    }

    /**
     * Formato del número: «C-» y ocho dígitos (C-00000001).
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param numero valor de la secuencia
     * @return número con formato
     */
    static String formatear(long numero) {
        return String.format("C-%08d", numero);
    }
}
