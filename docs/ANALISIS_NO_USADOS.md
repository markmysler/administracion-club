# Clases, métodos y atributos sin uso — análisis y propuestas

Análisis del 02/11/2026 sobre `src/main/java` (471 archivos) con `herramientas/buscar_no_usados.py` y revisión manual
de cada candidato contra el diseño 6.0. El script busca por nombre (clases sin referencias, métodos cuyo nombre no se
invoca en ningún lado, atributos privados que se asignan y no se leen); lo que usa el framework por anotación
(`@Route`, `@Component`, `@Scheduled`, `@EventListener`, `@Override`, Binder de Vaadin, Jackson) se descartó como uso
legítimo después de revisarlo. Límite conocido: un método que se llama igual que otro que sí se usa no aparece.

Resultado: **0 atributos sin leer**, **9 objetos consulta del diseño sin uso**, **6 métodos del diseño sin uso** y
**6 miembros agregados en la implementación sin uso**. Ninguna clase de dominio, fachada, broker, vista ni
presentador está sin usar.

Propuestas: **Eliminar** (del código y del diseño), **Usar** (conectar el código como dice el diseño),
**Conservar** (queda en código y diseño; motivo indicado).

## 1. Objetos consulta del diseño (MODELO §11.8.4 / 07C) que el código no usa

| # | Consulta | Para qué la preveía el diseño | Qué hace el código en su lugar | Propuesta |
|---|---|---|---|---|
| 1 | `ReservasQueSeSuperponen` (reservas) | `ValidadorTurnoLibre`: `contar(...) > 0` | `FabricaCadenaValidacion` le pasa a `ValidadorTurnoLibre` los turnos ocupados de la cancha (`ConsultaTurnosOcupados`, consulta invertida ya diseñada) y la base revalida con la restricción de exclusión | **Eliminar**: duplicaría lo que ya resuelve `TurnosOcupadosDeCancha` |
| 2 | `ReservasVigentesDeSocio` (reservas) | `ValidadorLimiteReservas`: `contar(...)` | La fábrica le pasa la lista de `ReservasFuturasDeSocio` (sin la reserva que se modifica) | **Eliminar**: misma información que `ReservasFuturasDeSocio` |
| 3 | `CategoriasActivas` (socios) | Combo de categorías de P-07 | `listarCategorias()` (`CategoriasSocio`) y el presentador filtra las activas en memoria | **Usar**: agregar `ServicioSocios.listarCategoriasActivas()` con esta consulta para P-07 (lo pide el diseño y evita traer las inactivas) |
| 4 | `DeportesActivos` (recursos) | Combo de deportes activos | Las pantallas usan `Deportes` y filtran | **Eliminar**: ningún diagrama la usa y `sec_cu26_p2` dibuja `CanchasDeDeporte` + filtro |
| 5 | `CanchasActivasDeDeporte` (recursos) | Grilla de CU-26 | `CanchasDeDeporte` + `estaDisponibleParaReservar()`, como `sec_cu26_p2` | **Eliminar**: el diagrama de secuencia usa la otra |
| 6 | `NotificacionesFallidas` (notificaciones) | Reenvío de notificaciones fallidas | Ningún caso de uso reenvía notificaciones | **Eliminar** |
| 7 | `CuotaDeGrupoYPeriodo` (cuotas) | Emisión idempotente | `GruposVigentesSinCuota(periodo)` ya excluye los grupos con cuota del período y `uk_cuota_grupo_periodo` lo garantiza | **Eliminar** |
| 8 | `PagoPorIdPasarela` (pagos) | Idempotencia del webhook por id de la pasarela | El webhook busca por `PagoPorReferenciaExterna` (FOR UPDATE) y `uk_pago_online_id_pasarela` impide duplicados | **Eliminar** |
| 9 | `MediosPagoHabilitados` (pagos) | Combo de medios de P-30 (CU-36) | `consultarMediosYPasarela()` trae todos y el presentador filtra los habilitados | **Usar**: `ServicioPagos.listarMediosHabilitados()` con esta consulta para P-30 |

## 2. Métodos del diseño que el código no llama

| # | Método | Propuesta |
|---|---|---|
| 10 | `TablaDatos.mostrarPaginado(proveedor)` | **Usar** en P-22 «Reservas del club» y P-28 «Pagos» (el diseño los describe con carga perezosa por páginas). Alternativa: Eliminar y dejar carga completa |
| 11 | `TablaDatos.filaSeleccionada()` | **Eliminar**: todas las tablas usan columnas de acción o `alSeleccionar` |
| 12 | `FiltroFechas.alCambiar(oyente)` | **Eliminar**: los filtros se aplican con el botón «Buscar/Generar» |
| 13 | `FilaResultado.esNulo(columna)` | **Conservar**: es parte de la API de lectura tipada (A-3); los brokers comparan `valor(...) == null` |
| 14 | `BrokerBaseDatos.aParametro(valor)` (gancho) | **Usar**: que `vincularParametros` de cada broker pase por `aParametro` (hoy llama directo a la conversión estática), así el gancho del Template Method queda activo |
| 15 | `SesionUsuario.getNombreVisible()` (`sec_cu11_p3`) | **Usar**: mostrar «Hola, {nombre}» en el encabezado de `DisposicionPrincipal` |

## 3. Miembros agregados por la implementación que no se usan

| # | Miembro | Propuesta |
|---|---|---|
| 16 | `ServicioAuditoria.consultarHistorial(...)` | **Eliminar**: ninguna pantalla muestra el historial |
| 17 | `Comprobante.esConstanciaAcreditacion()` | **Usar** en `DetalleComprobante.desde` (hoy repite la condición) |
| 18 | `PasarelaSimuladaAdapter.getUrlWebhook()` | **Eliminar**: el simulador confirma en el mismo proceso |
| 19 | `TokenSesionPersistente.getPersistido()` | **Eliminar** (solo lo usa `esNuevo()` por el atributo) |
| 20 | `PresentadorResultadoPago.getIntentos()` / `getUltimoEstado()` | **Conservar**: atributos del diagrama de estados de P-26; los usan las pruebas |
| 21 | `Dinero.getMoneda()`, `ConcurrenciaException.getClaseObjeto()` | **Conservar**: accesores de objetos de valor/excepción (documentan el dato) |

Los setters de los objetos `Datos*`/`Filtro*` que aparecen sin llamadas los usa el `Binder` de Vaadin por reflexión
y los de `NotificacionPasarelaRequest` los usa Jackson: se conservan.

## Resumen

- **Eliminar (12)**: #1, #2, #4, #5, #6, #7, #8, #11, #12, #16, #18, #19.
- **Usar (6)**: #3, #9, #10, #14, #15, #17.
- **Conservar (4)**: #13, #20, #21 y los accesores usados por reflexión.

**Estado (02/11/2026): todas las propuestas aplicadas.** Además se eliminó `RegistrosDeEntidad` (consulta 46), que
quedó sin uso al eliminar `ServicioAuditoria.consultarHistorial`, y el atributo `PasarelaSimuladaAdapter.urlWebhook`,
que solo leía el getter eliminado. Los cambios están registrados como G-28 a G-38 en `docs/CAMBIOS_AL_DISENO.md`.

Cada eliminación o cambio que se aplique se registra en `docs/CAMBIOS_AL_DISENO.md` para que la sesión de
documentación lo refleje en MODELO §11, 07B, 07C, 08 y el modelo global.
