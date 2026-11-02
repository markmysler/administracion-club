package ar.edu.ub.club.accesos.persistencia;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Broker abstracto de la jerarquía {@code Usuario} con herencia por tabla de subclase (Class Table Inheritance;
 * JOINED): la tabla padre {@code usuario} guarda las columnas comunes y cada subclase su tabla hija
 * ({@code administrador}, {@code socio}) con la misma {@code id} como PK/FK. No hay columna de rol ni
 * discriminador: la subclase se conoce por la tabla hija con la que hace el INNER JOIN.
 *
 * <p>Patrones: Database Broker (Larman, cap. 34) y Template Method: completa los ganchos de {@code BrokerBaseDatos}
 * para la tabla padre y define dos ganchos nuevos ({@code tablaHija()} y {@code valoresHija(T)}) que completa
 * cada broker concreto ({@code BrokerAdministrador}, {@code BrokerSocio} del módulo socios). {@code claseRaiz()}
 * = {@code Usuario}: el mapa de identidad indexa todas las cuentas por la misma clase y {@code FachadaPersistencia}
 * resuelve {@code get(id, Usuario)} y las consultas polimórficas probando cada broker de la jerarquía.
 *
 * <p>Para escribir un broker hijo: {@code claseObjeto()}, {@code tablaHija()}, {@code valoresHija(T)} (columnas
 * de la tabla hija SIN {@code id}) y {@code crearObjeto(fila)} (la fila trae {@code usuario.*} y las columnas de
 * la hija juntas); si tiene asociaciones, redefine {@code cargarAsociaciones}.
 *
 * @param <T> subclase concreta de {@code Usuario}
 *
 * <p>Casos de uso: todos
 */
public abstract class BrokerUsuario<T extends Usuario> extends BrokerBaseDatos<T> {

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: todos
     * @param proveedor proveedor de conexiones
     */
    public BrokerUsuario(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Clase raíz de la jerarquía, clave del mapa de identidad: {@code Usuario}.
     *
     * <p>Casos de uso: todos
     * @return {@code Usuario.class}
     */
    @Override
    public Class<?> claseRaiz() {
        return Usuario.class;
    }

    /**
     * Tabla padre de la jerarquía: {@code usuario}.
     *
     * <p>Casos de uso: todos
     * @return {@code "usuario"}
     */
    @Override
    protected String tabla() {
        return "usuario";
    }

    /**
     * SELECT con INNER JOIN de la tabla hija: {@code SELECT usuario.*, <hija>.* FROM usuario JOIN <hija> ON
     * <hija>.id = usuario.id}. Solo devuelve las cuentas de la subclase del broker.
     *
     * <p>Casos de uso: todos
     * @return SELECT sin WHERE
     */
    @Override
    protected String consultaBase() {
        String hija = tablaHija();
        return "SELECT usuario.*, " + hija + ".* FROM usuario JOIN " + hija + " ON " + hija + ".id = usuario.id";
    }

    /**
     * Columnas comunes del INSERT en {@code usuario} (el id lo genera la secuencia).
     *
     * <p>Casos de uso: CU-01, CU-14, CU-16
     * @param objeto cuenta nueva
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(T objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("username", objeto.getUsername());
        valores.put("email", objeto.getEmail());
        valores.put("password_hash", objeto.getPasswordHash());
        valores.put("estado", objeto.getEstado());
        valores.put("intentos_fallidos", objeto.getIntentosFallidos());
        valores.put("bloqueado_hasta", objeto.getBloqueadoHasta());
        valores.put("fecha_creacion", objeto.getFechaCreacion());
        return valores;
    }

    /**
     * Columnas comunes del UPDATE de {@code usuario} (la fecha de creación no cambia).
     *
     * <p>Casos de uso: CU-03, CU-11, CU-13, CU-15, CU-17
     * @param objeto cuenta persistida
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresActualizacion(T objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("username", objeto.getUsername());
        valores.put("email", objeto.getEmail());
        valores.put("password_hash", objeto.getPasswordHash());
        valores.put("estado", objeto.getEstado());
        valores.put("intentos_fallidos", objeto.getIntentosFallidos());
        valores.put("bloqueado_hasta", objeto.getBloqueadoHasta());
        return valores;
    }

    /**
     * Guarda la fila de la tabla hija en la misma transacción: en el alta {@code INSERT INTO <hija> (id, ...)} con
     * el id recién generado; en la modificación {@code UPDATE <hija> SET ... WHERE id = ?}.
     *
     * <p>Casos de uso: todos
     * @param objeto cuenta guardada
     * @param alta true si se acaba de insertar la fila de {@code usuario}
     */
    @Override
    protected void guardarPartes(T objeto, boolean alta) {
        Map<String, Object> valores = valoresHija(objeto);
        String hija = tablaHija();
        List<Object> parametros = new ArrayList<>();
        if (alta) {
            List<String> columnas = new ArrayList<>();
            columnas.add("id");
            parametros.add(objeto.getId());
            columnas.addAll(valores.keySet());
            parametros.addAll(valores.values());
            String marcas = String.join(", ", java.util.Collections.nCopies(columnas.size(), "?"));
            ejecutarActualizacion("INSERT INTO " + hija + " (" + String.join(", ", columnas) + ") VALUES (" + marcas + ")",
                    parametros);
        } else if (!valores.isEmpty()) {
            List<String> asignaciones = new ArrayList<>();
            for (String columna : valores.keySet()) {
                asignaciones.add(columna + " = ?");
            }
            parametros.addAll(valores.values());
            parametros.add(objeto.getId());
            ejecutarActualizacion("UPDATE " + hija + " SET " + String.join(", ", asignaciones) + " WHERE id = ?",
                    parametros);
        }
    }

    /**
     * Gancho: tabla hija de la subclase ({@code "administrador"}, {@code "socio"}).
     *
     * <p>Casos de uso: todos
     * @return nombre de la tabla hija
     */
    protected abstract String tablaHija();

    /**
     * Gancho: columnas y valores propios de la tabla hija, sin {@code id} (en orden).
     *
     * <p>Casos de uso: todos
     * @param objeto cuenta
     * @return mapa ordenado columna → valor
     */
    protected abstract Map<String, Object> valoresHija(T objeto);
}
