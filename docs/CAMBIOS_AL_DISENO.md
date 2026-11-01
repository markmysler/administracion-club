# Cambios y precisiones respecto del diseño 6.0

Registro de todo lo que la implementación tuvo que cambiar, agregar o precisar respecto de los documentos de la
versión 6.0 (MODELO §11, 07B, 07C, 06, 08 y el modelo global). Sirve para actualizar la documentación y que vuelva a
coincidir con el código. Cada fila indica el documento afectado y el tipo: **Cambio** (el código difiere del diseño),
**Agregado** (el diseño no lo tenía) o **Precisión** (el diseño callaba y se eligió la opción más simple).

## Núcleo compartido, infraestructura y base de datos

| # | Tipo | Elemento | Diseño | Implementación | Motivo | Documentos a actualizar |
|---|---|---|---|---|---|---|
| G-1 | Cambio | Versión de Vaadin | Vaadin Flow 24 | Vaadin 24.9.10, solo `vaadin-core` + `vaadin-spring` | Las versiones 24.9.30+ y 24.10.x son de mantenimiento extendido comercial y piden licencia; el starter completo incluye componentes comerciales | 04B (D-09), 11.2 |
| G-2 | Cambio | `ControlAcceso` | Interfaz | Clase concreta (`@Component`) con `verificar` y `tienePermiso` | El modelo no nombra ninguna implementación; una sola clase es lo más simple | Modelo global, 07B (compartido.seguridad) |
| G-3 | Agregado | `RelojSistema` (compartido.dominio) y bean en `ConfiguracionAplicacion` | Solo la interfaz `Reloj` | Implementación sobre `Clock` en la zona America/Argentina/Buenos_Aires | Hace falta una implementación concreta | Modelo global, 07B |
| G-4 | Agregado | Operaciones de `Dinero` | Solo atributo `moneda` (y monto implícito) | `monto: BigDecimal`, `moneda`; `de`, `cero`, `sumar`, `restar`, `minimo`, `esMayorQue`, `esCero`, `esPositivo`, `esNegativo`, `getMonto`, `compareTo` | El diseño usa importes pero no define sus operaciones | Modelo global, 07B (compartido.dominio) |
| G-5 | Cambio | `ResumenReserva`, `ResultadoBaja` | Clases con atributos | `record` (más `ResultadoBaja.pendiente(...)` y `ResultadoBaja.realizada(...)`) | Objetos de datos inmutables sin comportamiento | Modelo global, 07B |
| G-6 | Cambio | `FachadaPersistencia.brokerDe` | Visibilidad de paquete (`~`) | Pública | `BrokerReserva` (otro paquete) la usa para materializar socio y cancha desde el JOIN, como indica 11.8.3 | 11.8.1, 07C |
| G-7 | Agregado | `BrokerBaseDatos.ejecutarActualizacion(sql, parametros)` (protegido final) | — | Ejecuta un INSERT/UPDATE/DELETE con la conexión de la transacción | Lo usan los ganchos `guardarPartes`/`borrarPartes` (tablas hijas, `dia_atencion`, `item_cuota`) | 11.8.1, 07C |
| G-8 | Precisión | SQL de brokers y consultas | `"WHERE <alias>.clave = ?"` | Las columnas se califican con el nombre de la tabla, sin alias (`WHERE reserva.fecha = ?`); `consultaBase()` por omisión `SELECT * FROM <tabla>` | Los objetos consulta tienen que conocer el calificador de columnas | 11.8.1, 07C |
| G-9 | Precisión | `aParametro` | Dinero, DateTime, enumerado, estado | Además: entidad persistente → su id; los estados se reconocen por su método `codigo()` | Simplifica `valoresInsercion` | 11.8.1 |
| G-10 | Precisión | `MapaIdentidad` | Uno por transacción | La clave del recurso es el `ConnectionHolder` de la transacción (cada `REQUIRES_NEW` tiene su mapa); las claves numéricas se normalizan a `Long` | Filas únicas con `SMALLINT` y transacciones anidadas | 11.8.2, 07C |
| G-11 | Precisión | Traducción de errores SQL | 23P01 → `ErrorNegocio` | 23P01 → `ErrorNegocio("El turno ya fue reservado; elija otro turno.")`; el resto → `IllegalStateException` | — | 07C (5.1) |
| G-12 | Cambio | `ck_grupo_titular_vigente` | Restricción diferible (alta: grupo antes que socio) | CHECK no diferible (PostgreSQL no admite CHECK diferibles): el grupo se inserta no vigente / sin titular, luego el socio y luego se actualiza el grupo | Limitación de PostgreSQL | 07C (grupo_familiar, ciclo de vida), SD de CU-01 |
| G-13 | Cambio | Usuarios de base | Usuario de migración y usuario de aplicación separados | Un solo usuario (`club`) | Simplicidad del entorno de demostración | 07C (Flyway), 04B |
| G-14 | Agregado | `V8__datos_demo.sql` | Plan V1–V7 | Migración de datos de demostración | Requisito de la demostración | 07C (plan de Flyway) |
| G-15 | Agregado | Zona horaria de la JVM | — | `AplicacionClub.main` fija America/Argentina/Buenos_Aires; pruebas con `-Duser.timezone` | Windows informa el alias «America/Buenos_Aires», que PostgreSQL 16 rechaza | 04B / 09 (despliegue) |
| G-16 | Precisión | URL JDBC | — | `stringtype=unspecified` (JSONB y UUID desde texto); las consultas arman el WHERE solo con los filtros no nulos | Evitar conversiones manuales | 07C |
| G-17 | Agregado | `DialogoBase.notificarResuelto()` y `addListener` público | `addListener(tipo, oyente)` | Además `notificarResuelto()` cierra y dispara `DialogoResueltoEvento` | Disparo del evento desde las subclases | 11.4.2, 07B |
| G-18 | Agregado | `FormularioEntidad.tieneCampo(campo)` | — | Indica si existe un campo vinculado a la propiedad | `VistaBase.marcarCampo` decide entre marcar el campo o mostrar el error general | 11.4.3, 07B |
| G-19 | Agregado | `FilaResumenReserva.comoTexto()`, `OpcionSeleccion.toString()` | — | Texto de una línea para advertencias y combos | Presentación | 11.6.1 |
| G-20 | Precisión | `MenuNavegacion` | Componente | Bean de Spring de alcance prototipo; el menú se reconstruye en cada navegación (`DisposicionPrincipal.afterNavigation`) | La cuenta puede cambiar en la misma UI | 11.4.1 |
| G-21 | Precisión | `VistaProtegida` | `sesionActual`, `controlAcceso` protegidos | Inyectados por Spring en los atributos (`@Autowired`) para no obligar a cada vista a pasarlos al constructor | Simplicidad | 11.4.2 |
| G-22 | Agregado | Getters y setters | No figuran en el modelo | Getters en entidades y objetos de datos; getters, setters y constructor vacío en los `Datos*`/`Filtro*`/`Criterio*` | Acceso a los atributos privados; requisito del `Binder` de Vaadin | Modelo global (nota general) |
| G-23 | Cambio | `AlmacenTokensSesionPersistente.updateToken` | `fecha: Date` | `java.util.Date` (no `LocalDate`) | Es la firma de `PersistentTokenRepository` de Spring Security | Modelo global |
| G-24 | Cambio | `ServicioAgenda.consultarReglas()` | Permiso CONFIGURAR_AGENDA (07B) | Sin permiso específico (cuenta autenticada) | Contradicción del diseño: sec_cu26/sec_cu29 la llaman desde pantallas del socio (P-19, P-21), que no tienen ese permiso | 07B (agenda.aplicacion), 06 |

## Cambios de los módulos

Los agentes que implementan cada módulo agregan aquí (al unir las ramas) sus cambios, tomados de las notas
`impl_notas_<modulo>.md`.
