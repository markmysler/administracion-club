package ar.edu.ub.club.compartido.persistencia;

/**
 * Superclase abstracta de las entidades del dominio (reemplaza las anotaciones JPA de 5.0): guarda el
 * identificador y la versión que maneja el broker. Las entidades no conocen SQL ni JDBC.
 *
 * <p>Casos de uso: todos
 */
public abstract class EntidadPersistente implements ObjetoPersistente {

    /** Identificador generado por la base; {@code null} mientras el objeto es nuevo. */
    private Long id;

    /** Versión del control optimista; {@code null} si la clase no lo usa. */
    private Integer version;

    /**
     * Devuelve el identificador.
     *
     * <p>Casos de uso: todos
     * @return identificador o {@code null}
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Asigna el identificador (lo usa el broker al insertar o al reconstituir).
     *
     * <p>Casos de uso: todos
     * @param id identificador
     */
    @Override
    public void asignarId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve la versión del control optimista.
     *
     * <p>Casos de uso: todos
     * @return versión o {@code null}
     */
    @Override
    public Integer getVersion() {
        return version;
    }

    /**
     * Asigna la versión (lo usa el broker).
     *
     * <p>Casos de uso: todos
     * @param version versión
     */
    @Override
    public void asignarVersion(Integer version) {
        this.version = version;
    }

    /**
     * Un objeto es nuevo si todavía no tiene identificador ({@code TokenSesionPersistente} lo redefine porque su
     * clave es la serie).
     *
     * <p>Casos de uso: todos
     * @return true si {@code id == null}
     */
    @Override
    public boolean esNuevo() {
        return id == null;
    }

    /**
     * Dos entidades son iguales si son de la misma clase y tienen el mismo identificador (las nuevas solo son
     * iguales a sí mismas).
     *
     * <p>Casos de uso: todos
     * @param o otro objeto
     * @return true si representan la misma fila
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass() || id == null) {
            return false;
        }
        return id.equals(((EntidadPersistente) o).id);
    }

    /**
     * Código hash coherente con {@code equals}.
     *
     * <p>Casos de uso: todos
     * @return hash del identificador
     */
    @Override
    public int hashCode() {
        return id == null ? System.identityHashCode(this) : id.hashCode();
    }
}
