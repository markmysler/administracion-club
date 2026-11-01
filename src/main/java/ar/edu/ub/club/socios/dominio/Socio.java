package ar.edu.ub.club.socios.dominio;

import ar.edu.ub.club.accesos.dominio.EstadoCuenta;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import ar.edu.ub.club.socios.aplicacion.DatosSocio;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Subclase de {@code Usuario} con los datos personales, la categoría y el grupo familiar vigente.
 *
 * <p>Entidad del dominio; la persiste su broker.
 *
 * <p>Casos de uso: todos
 */
public class Socio extends Usuario {

    /** Documento. */
    private String dni;

    /** Nombre. */
    private String nombre;

    /** Apellido. */
    private String apellido;

    /** Nacimiento. */
    private LocalDate fechaNacimiento;

    /** Teléfono. */
    private String telefono;

    /** Domicilio. */
    private String direccion;

    /** Alta. */
    private LocalDate fechaAlta;

    /** Categoría vigente. */
    private CategoriaSocio categoria;

    /** Grupo vigente. */
    private GrupoFamiliar grupoFamiliar;

    /**
     * Crea una instancia de {@code Socio}.
     *
     * <p>Casos de uso: CU-01, CU-16
     * @param datos valor de {@code datos}
     * @param categoria valor de {@code categoria}
     * @param passwordHash valor de {@code passwordHash}
     */
    public Socio(DatosSocio datos, CategoriaSocio categoria, String passwordHash) {
        // PENDIENTE
    }

    /**
     * Aplica los datos personales y la categoría modificados (CU-02, Information Expert).
     *
     * <p>Casos de uso: CU-02
     * @param datos Datos personales.
     * @param categoria Categoría elegida.
     */
    public void actualizarDatos(DatosSocio datos, CategoriaSocio categoria) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Permisos del socio.
     *
     * <p>Casos de uso: todos
     * @return Conjunto de permisos de socio.
     */
    public Set<Permiso> obtenerPermisos() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve verdadero.
     *
     * <p>Casos de uso: todos
     * @return true.
     */
    public boolean admiteSesionPersistente() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve "Socio".
     *
     * <p>Casos de uso: todos
     * @return "Socio".
     */
    public String tipoCuenta() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param id valor de {@code id}
     * @param username valor de {@code username}
     * @param email valor de {@code email}
     * @param passwordHash valor de {@code passwordHash}
     * @param estado valor de {@code estado}
     * @param intentosFallidos valor de {@code intentosFallidos}
     * @param bloqueadoHasta valor de {@code bloqueadoHasta}
     * @param fechaCreacion valor de {@code fechaCreacion}
     * @param dni valor de {@code dni}
     * @param nombre valor de {@code nombre}
     * @param apellido valor de {@code apellido}
     * @param fechaNacimiento valor de {@code fechaNacimiento}
     * @param telefono valor de {@code telefono}
     * @param direccion valor de {@code direccion}
     * @param fechaAlta valor de {@code fechaAlta}
     * @return el resultado
     */
    public static Socio reconstituir(Long id, String username, String email, String passwordHash, EstadoCuenta estado, Integer intentosFallidos, LocalDateTime bloqueadoHasta, LocalDateTime fechaCreacion, String dni, String nombre, String apellido, LocalDate fechaNacimiento, String telefono, String direccion, LocalDate fechaAlta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param categoria valor de {@code categoria}
     * @param grupoFamiliar valor de {@code grupoFamiliar}
     */
    public void restaurarAsociaciones(CategoriaSocio categoria, GrupoFamiliar grupoFamiliar) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
