package ar.edu.ub.club.compartido.persistencia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Proxy virtual (GoF Proxy, variante Virtual Proxy) de una colección: el broker lo crea en {@code crearObjeto}
 * sin tocar la base y la consulta se ejecuta en el primer acceso. Se usa en {@code GrupoFamiliar.integrantes}
 * ({@code SociosDeGrupo}), {@code GrupoFamiliar.cuotas} ({@code CuotasDeGrupo}), {@code GrupoFamiliar.movimientos}
 * ({@code MovimientosDeGrupo}) y {@code Cuota.items} ({@code ItemsDeCuota}). Todas las operaciones de
 * {@code List} delegan en {@code real()}.
 *
 * @param <T> elemento de la lista
 *
 * <p>Casos de uso: CU-04, CU-05, CU-07, CU-08, CU-09, CU-10, CU-35, CU-36, CU-37; tareas programadas: emisión
 * mensual de cuotas
 */
public class ProxyListaVirtual<T> implements List<T> {

    /** Fachada con la que se ejecuta la consulta. */
    private final FachadaPersistencia fachada;

    /** Consulta que carga la colección. */
    private final Consulta<T> consulta;

    /** Lista real, {@code null} hasta el primer acceso. */
    private List<T> real;

    /**
     * Crea el proxy sin ejecutar la consulta.
     *
     * <p>Casos de uso: todos
     * @param fachada fachada de persistencia
     * @param consulta consulta que carga la colección
     */
    public ProxyListaVirtual(FachadaPersistencia fachada, Consulta<T> consulta) {
        this.fachada = fachada;
        this.consulta = consulta;
    }

    /**
     * Indica si la colección ya se cargó ({@code BrokerGrupoFamiliar.guardarPartes} solo guarda integrantes
     * cargados).
     *
     * <p>Casos de uso: CU-07, CU-08, CU-09
     * @return true si ya se ejecutó la consulta
     */
    public boolean estaCargada() {
        return real != null;
    }

    /**
     * Carga la lista en el primer acceso: {@code real = new ArrayList<>(fachada.buscar(consulta))}.
     *
     * <p>Casos de uso: todos
     * @return lista real
     */
    private List<T> real() {
        if (real == null) {
            real = new ArrayList<>(fachada.buscar(consulta));
        }
        return real;
    }

    /**
     * Cantidad de elementos.
     *
     * <p>Casos de uso: todos
     * @return tamaño
     */
    @Override
    public int size() {
        return real().size();
    }

    /**
     * Indica si está vacía.
     *
     * <p>Casos de uso: todos
     * @return true si no tiene elementos
     */
    @Override
    public boolean isEmpty() {
        return real().isEmpty();
    }

    /**
     * Indica si contiene el objeto.
     *
     * <p>Casos de uso: todos
     * @param o objeto buscado
     * @return true si lo contiene
     */
    @Override
    public boolean contains(Object o) {
        return real().contains(o);
    }

    /**
     * Iterador de la lista real.
     *
     * <p>Casos de uso: todos
     * @return iterador
     */
    @Override
    public Iterator<T> iterator() {
        return real().iterator();
    }

    /**
     * Copia a un arreglo.
     *
     * <p>Casos de uso: todos
     * @return arreglo
     */
    @Override
    public Object[] toArray() {
        return real().toArray();
    }

    /**
     * Copia a un arreglo tipado.
     *
     * <p>Casos de uso: todos
     * @param a arreglo destino
     * @param <A> tipo del arreglo
     * @return arreglo
     */
    @Override
    public <A> A[] toArray(A[] a) {
        return real().toArray(a);
    }

    /**
     * Agrega un elemento.
     *
     * <p>Casos de uso: CU-01, CU-07
     * @param e elemento
     * @return true
     */
    @Override
    public boolean add(T e) {
        return real().add(e);
    }

    /**
     * Quita un elemento.
     *
     * <p>Casos de uso: CU-09
     * @param o elemento
     * @return true si estaba
     */
    @Override
    public boolean remove(Object o) {
        return real().remove(o);
    }

    /**
     * Indica si contiene todos los elementos.
     *
     * <p>Casos de uso: todos
     * @param c colección
     * @return true si los contiene
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        return real().containsAll(c);
    }

    /**
     * Agrega todos los elementos.
     *
     * <p>Casos de uso: todos
     * @param c colección
     * @return true si cambió
     */
    @Override
    public boolean addAll(Collection<? extends T> c) {
        return real().addAll(c);
    }

    /**
     * Inserta todos los elementos en una posición.
     *
     * <p>Casos de uso: todos
     * @param index posición
     * @param c colección
     * @return true si cambió
     */
    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return real().addAll(index, c);
    }

    /**
     * Quita todos los elementos de la colección.
     *
     * <p>Casos de uso: todos
     * @param c colección
     * @return true si cambió
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        return real().removeAll(c);
    }

    /**
     * Conserva solo los elementos de la colección.
     *
     * <p>Casos de uso: todos
     * @param c colección
     * @return true si cambió
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        return real().retainAll(c);
    }

    /**
     * Vacía la lista.
     *
     * <p>Casos de uso: todos
     */
    @Override
    public void clear() {
        real().clear();
    }

    /**
     * Elemento en una posición.
     *
     * <p>Casos de uso: todos
     * @param index posición
     * @return elemento
     */
    @Override
    public T get(int index) {
        return real().get(index);
    }

    /**
     * Reemplaza el elemento de una posición.
     *
     * <p>Casos de uso: todos
     * @param index posición
     * @param element elemento nuevo
     * @return elemento anterior
     */
    @Override
    public T set(int index, T element) {
        return real().set(index, element);
    }

    /**
     * Inserta un elemento en una posición.
     *
     * <p>Casos de uso: todos
     * @param index posición
     * @param element elemento
     */
    @Override
    public void add(int index, T element) {
        real().add(index, element);
    }

    /**
     * Quita el elemento de una posición.
     *
     * <p>Casos de uso: todos
     * @param index posición
     * @return elemento quitado
     */
    @Override
    public T remove(int index) {
        return real().remove(index);
    }

    /**
     * Posición de un elemento.
     *
     * <p>Casos de uso: todos
     * @param o elemento
     * @return posición o -1
     */
    @Override
    public int indexOf(Object o) {
        return real().indexOf(o);
    }

    /**
     * Última posición de un elemento.
     *
     * <p>Casos de uso: todos
     * @param o elemento
     * @return posición o -1
     */
    @Override
    public int lastIndexOf(Object o) {
        return real().lastIndexOf(o);
    }

    /**
     * Iterador de lista.
     *
     * <p>Casos de uso: todos
     * @return iterador
     */
    @Override
    public ListIterator<T> listIterator() {
        return real().listIterator();
    }

    /**
     * Iterador de lista desde una posición.
     *
     * <p>Casos de uso: todos
     * @param index posición inicial
     * @return iterador
     */
    @Override
    public ListIterator<T> listIterator(int index) {
        return real().listIterator(index);
    }

    /**
     * Sublista de la lista real.
     *
     * <p>Casos de uso: todos
     * @param fromIndex desde (incluido)
     * @param toIndex hasta (excluido)
     * @return sublista
     */
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return real().subList(fromIndex, toIndex);
    }

    /**
     * Representación de la lista (no fuerza la carga).
     *
     * <p>Casos de uso: todos
     * @return texto
     */
    @Override
    public String toString() {
        return real == null ? "ProxyListaVirtual(sin cargar)" : real.toString();
    }
}
