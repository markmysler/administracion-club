package ar.edu.ub.club.compartido.dominio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Objeto de valor (Value Object) que representa un importe en pesos argentinos con dos decimales. Es inmutable:
 * las operaciones devuelven un {@code Dinero} nuevo. Se usa en las categorías, cuotas, ítems, pagos, comprobantes y
 * movimientos de saldo; en la base se guarda como {@code NUMERIC(12,2)} (la moneda es siempre ARS y no se guarda).
 *
 * <p>Casos de uso: CU-01, CU-04, CU-05, CU-06, CU-07, CU-08, CU-09, CU-10, CU-34, CU-35, CU-36, CU-37, CU-38,
 * CU-39, CU-40; tareas programadas: emisión mensual de cuotas
 */
public final class Dinero implements Comparable<Dinero> {

    /** Moneda única del sistema. */
    public static final String ARS = "ARS";

    /** Importe con escala 2. */
    private final BigDecimal monto;

    /** Código de la moneda (siempre ARS). */
    private final String moneda;

    /**
     * Crea un importe en ARS redondeado a dos decimales.
     *
     * <p>Casos de uso: todos
     * @param monto importe decimal
     */
    private Dinero(BigDecimal monto) {
        this.monto = Objects.requireNonNull(monto, "monto").setScale(2, RoundingMode.HALF_UP);
        this.moneda = ARS;
    }

    /**
     * Fábrica a partir de un decimal.
     *
     * <p>Casos de uso: todos
     * @param monto importe
     * @return el importe como {@code Dinero}
     */
    public static Dinero de(BigDecimal monto) {
        return new Dinero(monto);
    }

    /**
     * Fábrica a partir de un texto o número decimal (por ejemplo {@code "1500.50"}).
     *
     * <p>Casos de uso: todos
     * @param monto importe en texto
     * @return el importe como {@code Dinero}
     */
    public static Dinero de(String monto) {
        return new Dinero(new BigDecimal(monto));
    }

    /**
     * Importe cero.
     *
     * <p>Casos de uso: todos
     * @return $ 0,00
     */
    public static Dinero cero() {
        return new Dinero(BigDecimal.ZERO);
    }

    /**
     * Suma dos importes.
     *
     * <p>Casos de uso: CU-07, CU-10, CU-35, CU-36; tareas programadas: emisión mensual de cuotas
     * @param otro importe a sumar
     * @return la suma
     */
    public Dinero sumar(Dinero otro) {
        return new Dinero(monto.add(otro.monto));
    }

    /**
     * Resta dos importes (puede dar negativo).
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas
     * @param otro importe a restar
     * @return la diferencia
     */
    public Dinero restar(Dinero otro) {
        return new Dinero(monto.subtract(otro.monto));
    }

    /**
     * Devuelve el menor de dos importes (se usa para aplicar el saldo a favor hasta el importe de la cuota, RN-21).
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param otro importe a comparar
     * @return el menor
     */
    public Dinero minimo(Dinero otro) {
        return compareTo(otro) <= 0 ? this : otro;
    }

    /**
     * Indica si el importe es mayor que otro.
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas
     * @param otro importe a comparar
     * @return true si es estrictamente mayor
     */
    public boolean esMayorQue(Dinero otro) {
        return compareTo(otro) > 0;
    }

    /**
     * Indica si el importe es cero.
     *
     * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas
     * @return true si vale 0
     */
    public boolean esCero() {
        return monto.signum() == 0;
    }

    /**
     * Indica si el importe es mayor que cero.
     *
     * <p>Casos de uso: CU-06, CU-35, CU-36
     * @return true si es positivo
     */
    public boolean esPositivo() {
        return monto.signum() > 0;
    }

    /**
     * Indica si el importe es menor que cero.
     *
     * <p>Casos de uso: CU-06
     * @return true si es negativo
     */
    public boolean esNegativo() {
        return monto.signum() < 0;
    }

    /**
     * Devuelve el importe decimal (lo usa {@code BrokerBaseDatos.aParametro} para escribirlo en la base).
     *
     * <p>Casos de uso: todos
     * @return monto con dos decimales
     */
    public BigDecimal getMonto() {
        return monto;
    }

    /**
     * Devuelve la moneda.
     *
     * <p>Casos de uso: todos
     * @return "ARS"
     */
    public String getMoneda() {
        return moneda;
    }

    /**
     * Compara importes por su monto.
     *
     * <p>Casos de uso: todos
     * @param otro importe a comparar
     * @return negativo, cero o positivo
     */
    @Override
    public int compareTo(Dinero otro) {
        return monto.compareTo(otro.monto);
    }

    /**
     * Igualdad por valor (monto y moneda).
     *
     * <p>Casos de uso: todos
     * @param o otro objeto
     * @return true si representan el mismo importe
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Dinero d && monto.compareTo(d.monto) == 0 && moneda.equals(d.moneda);
    }

    /**
     * Código hash coherente con {@code equals}.
     *
     * <p>Casos de uso: todos
     * @return hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(monto.stripTrailingZeros(), moneda);
    }

    /**
     * Representación técnica (para bitácoras); la interfaz usa {@code FormatoUI.dinero}.
     *
     * <p>Casos de uso: todos
     * @return por ejemplo "ARS 1500.00"
     */
    @Override
    public String toString() {
        return moneda + " " + monto.toPlainString();
    }
}
