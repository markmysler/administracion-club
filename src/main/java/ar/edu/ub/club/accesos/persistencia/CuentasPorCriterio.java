package ar.edu.ub.club.accesos.persistencia;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Cuentas de cualquier tipo cuyo usuario, correo, nombre o apellido contienen el texto (sin distinguir mayúsculas); sin texto, todas. Consulta polimórfica: se ejecuta en {@code BrokerAdministrador} y en {@code BrokerSocio} y se ordena el total por nombre de usuario con {@code comparador()}. Las columnas {@code nombre} y {@code apellido} van sin calificar a propósito: existen con el mismo nombre en ambas tablas hijas ({@code administrador}, {@code socio}) y en {@code usuario} no, así la misma condición sirve para los dos brokers.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado (no es un bean: se crea con {@code new}).
 *
 * <p>Casos de uso: CU-15, CU-17
 */
public class CuentasPorCriterio extends ConsultaEntidades<Usuario> {

    /** Texto de búsqueda (puede ser nulo o vacío). */
    private final String texto;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Agregado en la implementación: el esqueleto no tenía constructor.
     *
     * <p>Casos de uso: CU-15, CU-17
     * @param texto texto de búsqueda (puede ser nulo o vacío).
     */
    public CuentasPorCriterio(String texto) {
        this.texto = texto;
    }

    /**
     * Clase de resultado; elige el broker (o los brokers de la jerarquía) que la resuelve.
     *
     * <p>Casos de uso: CU-15, CU-17
     * @return {@code Usuario.class}
     */
    @Override
    public Class<Usuario> claseResultado() {
        return Usuario.class;
    }

    /**
     * Condición y orden que se agregan a {@code consultaBase()} del broker.
     *
     * <p>Casos de uso: CU-15, CU-17
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        if (texto == null || texto.isBlank()) {
            return "ORDER BY usuario.username";
        }
        return "WHERE (usuario.username ILIKE ? OR usuario.email ILIKE ? OR nombre ILIKE ? OR apellido ILIKE ?)"
                + " ORDER BY usuario.username";
    }

    /**
     * Valores de los {@code ?} en el orden del SQL.
     *
     * <p>Casos de uso: CU-15, CU-17
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        List<Object> parametros = new ArrayList<>();
        if (texto != null && !texto.isBlank()) {
            String patron = "%" + texto.strip() + "%";
            for (int i = 0; i < 4; i++) {
                parametros.add(patron);
            }
        }
        return parametros;
    }

    /**
     * Orden del resultado unido de los brokers de la jerarquía: por nombre de usuario.
     *
     * <p>Casos de uso: CU-15, CU-17
     * @return comparador
     */
    @Override
    public Comparator<Usuario> comparador() {
        return Comparator.comparing(Usuario::getUsername);
    }
}
