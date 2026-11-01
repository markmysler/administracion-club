package ar.edu.ub.club.compartido.ui;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException;
import ar.edu.ub.club.compartido.excepciones.ConcurrenciaException;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.excepciones.RecursoNoEncontradoException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/**
 * Prueba del método plantilla {@code PresentadorBase.ejecutar}/{@code consultar} con una pantalla falsa (MVP, Vista
 * Pasiva: el presentador se prueba sin Vaadin): cada excepción de las fachadas se traduce a la operación de pantalla
 * correspondiente.
 *
 * <p>Casos de uso: todos
 */
class PresentadorBaseTest {

    /**
     * Pantalla falsa que registra lo que el presentador le pide.
     *
     * <p>Casos de uso: todos
     */
    static class PantallaFalsa implements PantallaBase {

        /** Registro de llamadas. */
        final List<String> llamadas = new ArrayList<>();

        /**
         * Registra el error.
         *
         * <p>Casos de uso: todos
         * @param mensaje texto
         */
        @Override
        public void mostrarError(String mensaje) {
            llamadas.add("error:" + mensaje);
        }

        /**
         * Registra el mensaje.
         *
         * <p>Casos de uso: todos
         * @param mensaje texto
         */
        @Override
        public void mostrarMensaje(String mensaje) {
            llamadas.add("mensaje:" + mensaje);
        }

        /**
         * Registra el campo marcado.
         *
         * <p>Casos de uso: todos
         * @param campo campo
         * @param mensaje texto
         */
        @Override
        public void marcarCampo(String campo, String mensaje) {
            llamadas.add("campo:" + campo + ":" + mensaje);
        }

        /**
         * Registra la navegación.
         *
         * <p>Casos de uso: todos
         * @param ruta ruta
         */
        @Override
        public void navegarA(String ruta) {
            llamadas.add("ruta:" + ruta);
        }
    }

    /**
     * Presentador concreto mínimo que expone los métodos plantilla.
     *
     * <p>Casos de uso: todos
     */
    static class PresentadorPrueba extends PresentadorBase<PantallaFalsa> {

        /**
         * Ejecuta una acción.
         *
         * <p>Casos de uso: todos
         * @param accion acción
         * @return true si terminó bien
         */
        boolean correr(Runnable accion) {
            return ejecutar(accion);
        }

        /**
         * Ejecuta una consulta.
         *
         * <p>Casos de uso: todos
         * @param valor valor a devolver
         * @return el valor
         */
        Optional<String> leer(String valor) {
            return consultar(() -> valor);
        }
    }

    /**
     * Crea el presentador vinculado a una pantalla falsa nueva.
     *
     * <p>Casos de uso: todos
     * @param pantalla pantalla falsa
     * @return presentador
     */
    private PresentadorPrueba presentador(PantallaFalsa pantalla) {
        PresentadorPrueba p = new PresentadorPrueba();
        p.vincular(pantalla);
        return p;
    }

    /**
     * Una acción sin excepción devuelve true y no muestra nada.
     *
     * <p>Casos de uso: todos
     */
    @Test
    void accionCorrecta() {
        PantallaFalsa pantalla = new PantallaFalsa();
        assertThat(presentador(pantalla).correr(() -> { })).isTrue();
        assertThat(presentador(pantalla).leer("x")).contains("x");
        assertThat(pantalla.llamadas).isEmpty();
    }

    /**
     * {@code ErrorNegocio} con campo marca el campo; sin campo muestra el mensaje.
     *
     * <p>Casos de uso: todos
     */
    @Test
    void errorDeNegocio() {
        PantallaFalsa pantalla = new PantallaFalsa();
        PresentadorPrueba p = presentador(pantalla);
        assertThat(p.correr(() -> {
            throw new ErrorNegocio("dni", "DNI repetido");
        })).isFalse();
        p.correr(() -> {
            throw new ErrorNegocio("Cuota ya pagada");
        });
        assertThat(pantalla.llamadas).containsExactly("campo:dni:DNI repetido", "error:Cuota ya pagada");
    }

    /**
     * Acceso denegado, recurso inexistente, conflicto de versión y error inesperado (con código, RNF-16).
     *
     * <p>Casos de uso: todos
     */
    @Test
    void otrasExcepciones() {
        PantallaFalsa pantalla = new PantallaFalsa();
        PresentadorPrueba p = presentador(pantalla);
        p.correr(() -> {
            throw new AccesoDenegadoException();
        });
        p.correr(() -> {
            throw new RecursoNoEncontradoException("x");
        });
        p.correr(() -> {
            throw new ConcurrenciaException("Reserva", 1L);
        });
        p.correr(() -> {
            throw new IllegalStateException("falla técnica");
        });
        assertThat(pantalla.llamadas.get(0)).isEqualTo("error:No tiene permiso para realizar esta operación.");
        assertThat(pantalla.llamadas.get(1)).isEqualTo("error:El elemento ya no existe.");
        assertThat(pantalla.llamadas.get(2)).startsWith("error:Los datos cambiaron");
        assertThat(pantalla.llamadas.get(3)).startsWith("error:Ocurrió un error inesperado. Código: ");
    }
}
