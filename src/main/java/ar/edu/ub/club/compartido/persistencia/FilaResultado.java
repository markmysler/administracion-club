package ar.edu.ub.club.compartido.persistencia;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.dominio.RelojSistema;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Envoltorio del {@code ResultSet} con prefijo de columnas y lectura tipada de {@code Dinero}, {@code Date},
 * {@code Time}, {@code DateTime} y {@code UUID}. Así un mismo broker materializa su clase desde una fila propia o
 * desde columnas con alias de un JOIN ({@code conPrefijo("s_")} en {@code BrokerReserva}), y desaparece
 * {@code ConversorDinero}.
 *
 * <p>Casos de uso: todos
 */
public class FilaResultado {

    /** Fila actual del resultado JDBC. */
    private final ResultSet fila;

    /** Prefijo que se antepone a cada nombre de columna. */
    private final String prefijo;

    /**
     * Crea el envoltorio sobre la fila actual.
     *
     * <p>Casos de uso: todos
     * @param fila resultado posicionado en una fila
     * @param prefijo prefijo de columnas ({@code ""} si no hay)
     */
    public FilaResultado(ResultSet fila, String prefijo) {
        this.fila = fila;
        this.prefijo = prefijo == null ? "" : prefijo;
    }

    /**
     * Devuelve una vista de la misma fila con un prefijo adicional (columnas con alias de un JOIN).
     *
     * <p>Casos de uso: CU-27, CU-28, CU-29, CU-30, CU-31, CU-32, CU-33
     * @param prefijo prefijo, por ejemplo {@code "s_"}
     * @return nueva fila con prefijo
     */
    public FilaResultado conPrefijo(String prefijo) {
        return new FilaResultado(fila, this.prefijo + prefijo);
    }

    /**
     * Valor sin conversión de la columna.
     *
     * <p>Casos de uso: todos
     * @param columna nombre de la columna (sin prefijo)
     * @return valor o {@code null}
     */
    public Object valor(String columna) {
        try {
            return fila.getObject(prefijo + columna);
        } catch (SQLException e) {
            throw error(columna, e);
        }
    }

    /**
     * Indica si la columna es NULL.
     *
     * <p>Casos de uso: todos
     * @param columna nombre de la columna
     * @return true si es NULL
     */
    public boolean esNulo(String columna) {
        return valor(columna) == null;
    }

    /**
     * Lee un texto.
     *
     * <p>Casos de uso: todos
     * @param columna nombre de la columna
     * @return texto o {@code null}
     */
    public String texto(String columna) {
        Object v = valor(columna);
        return v == null ? null : v.toString();
    }

    /**
     * Lee un entero.
     *
     * <p>Casos de uso: todos
     * @param columna nombre de la columna
     * @return entero o {@code null}
     */
    public Integer entero(String columna) {
        Object v = valor(columna);
        return v == null ? null : ((Number) v).intValue();
    }

    /**
     * Lee un entero largo.
     *
     * <p>Casos de uso: todos
     * @param columna nombre de la columna
     * @return largo o {@code null}
     */
    public Long largo(String columna) {
        Object v = valor(columna);
        return v == null ? null : ((Number) v).longValue();
    }

    /**
     * Lee un booleano.
     *
     * <p>Casos de uso: todos
     * @param columna nombre de la columna
     * @return booleano o {@code null}
     */
    public Boolean booleano(String columna) {
        Object v = valor(columna);
        return v == null ? null : (Boolean) v;
    }

    /**
     * Lee un decimal.
     *
     * <p>Casos de uso: todos
     * @param columna nombre de la columna
     * @return decimal o {@code null}
     */
    public BigDecimal decimal(String columna) {
        Object v = valor(columna);
        if (v == null) {
            return null;
        }
        return v instanceof BigDecimal b ? b : new BigDecimal(v.toString());
    }

    /**
     * Lee un importe {@code NUMERIC(12,2)} como {@code Dinero} (moneda ARS).
     *
     * <p>Casos de uso: todos
     * @param columna nombre de la columna
     * @return importe o {@code null}
     */
    public Dinero dinero(String columna) {
        BigDecimal d = decimal(columna);
        return d == null ? null : Dinero.de(d);
    }

    /**
     * Lee una fecha de calendario ({@code DATE}).
     *
     * <p>Casos de uso: todos
     * @param columna nombre de la columna
     * @return fecha o {@code null}
     */
    public LocalDate fecha(String columna) {
        try {
            return fila.getObject(prefijo + columna, LocalDate.class);
        } catch (SQLException e) {
            throw error(columna, e);
        }
    }

    /**
     * Lee una hora ({@code TIME}).
     *
     * <p>Casos de uso: todos
     * @param columna nombre de la columna
     * @return hora o {@code null}
     */
    public LocalTime hora(String columna) {
        try {
            return fila.getObject(prefijo + columna, LocalTime.class);
        } catch (SQLException e) {
            throw error(columna, e);
        }
    }

    /**
     * Lee un momento ({@code TIMESTAMPTZ}) y lo expresa en la hora local del club.
     *
     * <p>Casos de uso: todos
     * @param columna nombre de la columna
     * @return fecha y hora local o {@code null}
     */
    public LocalDateTime fechaHora(String columna) {
        try {
            OffsetDateTime v = fila.getObject(prefijo + columna, OffsetDateTime.class);
            return v == null ? null : v.atZoneSameInstant(RelojSistema.ZONA_CLUB).toLocalDateTime();
        } catch (SQLException e) {
            throw error(columna, e);
        }
    }

    /**
     * Lee un {@code UUID}.
     *
     * <p>Casos de uso: CU-35
     * @param columna nombre de la columna
     * @return UUID o {@code null}
     */
    public UUID uuid(String columna) {
        try {
            return fila.getObject(prefijo + columna, UUID.class);
        } catch (SQLException e) {
            throw error(columna, e);
        }
    }

    /**
     * Arma la excepción de lectura de una columna.
     *
     * <p>Casos de uso: todos
     * @param columna columna que falló
     * @param e causa
     * @return excepción no comprobada
     */
    private IllegalStateException error(String columna, SQLException e) {
        return new IllegalStateException("No se pudo leer la columna " + prefijo + columna, e);
    }
}
