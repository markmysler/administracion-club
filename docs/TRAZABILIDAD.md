# Trazabilidad: caso de uso → diagramas de secuencia → código

Para cada caso de uso se listan los diagramas de secuencia de la Entrega 8 (`v6/tp8/diagramas/*.puml`) y, para cada diagrama, la secuencia de llamadas que dibuja (clase destino y mensaje), en orden. Las clases y los métodos tienen el mismo nombre en el código; la columna «Archivo» lleva a la clase. En el Javadoc de cada método la línea `Casos de uso:` confirma su participación. El SQL que ejecuta cada broker u objeto consulta se ve en la consola al ejecutar la aplicación (logger `ar.edu.ub.club.compartido.persistencia`).

Cadena general (MVP + Facade + Database Broker): `Vista*` (evento de Vaadin) → `Presentador*.al…()` → `PresentadorBase.ejecutar/consultar` → `Servicio*` (fachada, `@Transactional`, `ControlAcceso.verificar`) → dominio (entidades, estados, validadores) → `FachadaPersistencia.get/put/buscar` → `Broker*` (`BrokerBaseDatos.materializar/insertar/actualizar`) u objeto consulta → JDBC → PostgreSQL. Ver también las interacciones comunes al final.

## CU-01 Administrador registra socio

<!-- NOTAS:CU-01 -->

### `sec_cu01_p1.puml` — CU-01 Administrador registra socio (parte 1: de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaListadoSocios | `elige Nuevo socio (paso 1)` | [VistaListadoSocios.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaListadoSocios.java) |
| 2 | VistaListadoSocios | PresentadorListadoSocios | `alNuevo()` | [PresentadorListadoSocios.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorListadoSocios.java) |
| 3 | PresentadorListadoSocios | VistaListadoSocios | `navegarA("socios/formulario")` | [VistaListadoSocios.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaListadoSocios.java) |
| 4 | VistaListadoSocios | VistaFormularioSocio | `navega a P-07 (alta)` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |
| 5 | VistaFormularioSocio | PresentadorFormularioSocio | `cargar(null)` | [PresentadorFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorFormularioSocio.java) |
| 6 | PresentadorFormularioSocio | ServicioSocios | `listarCategorias()` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 7 | PresentadorFormularioSocio | VistaFormularioSocio | `mostrarCategorias(opciones: List<OpcionSeleccion>) (paso 2)` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |
| 8 | PresentadorFormularioSocio | VistaFormularioSocio | `mostrarFormulario(new DatosSocio(), true)` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |
| 9 | Administrador | VistaFormularioSocio | `elige Cancelar` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |
| 10 | VistaFormularioSocio | PresentadorFormularioSocio | `alCancelar()` | [PresentadorFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorFormularioSocio.java) |
| 11 | PresentadorFormularioSocio | VistaFormularioSocio | `navegarA("socios")` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |
| 12 | Administrador | VistaFormularioSocio | `elige Guardar` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |
| 13 | VistaFormularioSocio | VistaFormularioSocio | `FormularioEntidad .leer() (paso 4)` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |
| 14 | VistaFormularioSocio | PresentadorFormularioSocio | `alGuardar(datos: DatosSocio)` | [PresentadorFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorFormularioSocio.java) |
| 15 | PresentadorFormularioSocio | ServicioSocios | `registrarSocio(datos) (dentro de ejecutar)` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 16 | PresentadorFormularioSocio | VistaFormularioSocio | `mostrarMensaje("Socio registrado")` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |
| 17 | PresentadorFormularioSocio | VistaFormularioSocio | `navegarA("socios/ficha/" + s.id)` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |
| 18 | PresentadorFormularioSocio | VistaFormularioSocio | `marcarCampo(campo, mensaje)` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |
| 19 | PresentadorFormularioSocio | VistaFormularioSocio | `mostrarError(mensaje genérico con código)` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |

### `sec_cu01_p2.puml` — CU-01 Administrador registra socio (parte 2: registrarSocio, validaciones y CU-16)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioSocios | PoliticaContrasenas | `validar(clave, username) (paso 5)` | [PoliticaContrasenas.java](../src/main/java/ar/edu/ub/club/accesos/dominio/PoliticaContrasenas.java) |
| 2 | ServicioSocios | FachadaPersistencia«Singleton» | `contar(SocioPorDni(dni)) (paso 6)` |  |
| 3 | ServicioSocios | FachadaPersistencia«Singleton» | `get(idCategoria, CategoriaSocio) (paso 7)` |  |
| 4 | ServicioSocios | FachadaPersistencia«Singleton» | `contar(UsuarioPorUsername( username))` |  |
| 5 | ServicioSocios | FachadaPersistencia«Singleton» | `contar(UsuarioPorEmail(email))` |  |
| 6 | ServicioSocios | CodificadorClave | `codificar(clave)` | [CodificadorClave.java](../src/main/java/ar/edu/ub/club/accesos/dominio/CodificadorClave.java) |
| 7 | ServicioSocios | Socio | `«create» (datos, categoria, hash): estado ACTIVA, intentosFallidos 0` | [Socio.java](../src/main/java/ar/edu/ub/club/socios/dominio/Socio.java) |

### `sec_cu01_p3.puml` — CU-01 Administrador registra socio (parte 3: registrarSocio, grupo y auditoría)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioSocios | FachadaPersistencia«Singleton» | `put(s)` |  |
| 2 | ServicioSocios | ServicioGruposFamiliares | `crearGrupoUnipersonal(s) (paso 9)` | [ServicioGruposFamiliares.java](../src/main/java/ar/edu/ub/club/grupos/aplicacion/ServicioGruposFamiliares.java) |
| 3 | ServicioSocios | ServicioAuditoria | `registrar(usuario, "ALTA", "Socio", s.id, detalle) (paso 11)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

## CU-02 Administrador modifica datos de socio

<!-- NOTAS:CU-02 -->

### `sec_cu02_p1.puml` — CU-02 Administrador modifica datos de socio (parte 1: de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaFichaSocio | `elige Modificar` | [VistaFichaSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFichaSocio.java) |
| 2 | VistaFichaSocio | PresentadorFichaSocio | `alModificar()` | [PresentadorFichaSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorFichaSocio.java) |
| 3 | PresentadorFichaSocio | VistaFichaSocio | `navegarA("socios/ formulario/" + id)` | [VistaFichaSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFichaSocio.java) |
| 4 | VistaFichaSocio | VistaFormularioSocio | `navega a P-07 (modificación)` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |
| 5 | VistaFormularioSocio | PresentadorFormularioSocio | `cargar(idSocio) (paso 4)` | [PresentadorFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorFormularioSocio.java) |
| 6 | PresentadorFormularioSocio | ServicioSocios | `listarCategorias()` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 7 | PresentadorFormularioSocio | ServicioSocios | `consultarSocio(idSocio)` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 8 | PresentadorFormularioSocio | VistaFormularioSocio | `mostrarCategorias(opciones)` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |
| 9 | PresentadorFormularioSocio | VistaFormularioSocio | `mostrarFormulario(DatosSocio .desde(ficha.socio), false)` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |
| 10 | Administrador | VistaFormularioSocio | `elige Cancelar` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |
| 11 | VistaFormularioSocio | PresentadorFormularioSocio | `alCancelar()` | [PresentadorFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorFormularioSocio.java) |
| 12 | PresentadorFormularioSocio | VistaFormularioSocio | `navegarA("socios/ficha/" + idSocio)` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |
| 13 | Administrador | VistaFormularioSocio | `elige Guardar` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |
| 14 | VistaFormularioSocio | VistaFormularioSocio | `FormularioEntidad .leer() (paso 6)` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |
| 15 | VistaFormularioSocio | PresentadorFormularioSocio | `alGuardar(datos: DatosSocio)` | [PresentadorFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorFormularioSocio.java) |
| 16 | PresentadorFormularioSocio | ServicioSocios | `modificarSocio(idSocio, datos) (dentro de ejecutar)` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 17 | PresentadorFormularioSocio | VistaFormularioSocio | `mostrarMensaje(...) y navegarA("socios/ficha/" + s.id)` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |
| 18 | PresentadorFormularioSocio | VistaFormularioSocio | `marcarCampo(campo, mensaje)` | [VistaFormularioSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFormularioSocio.java) |

### `sec_cu02_p2.puml` — CU-02 Administrador modifica datos de socio (parte 2: ServicioSocios.modificarSocio)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorFormularioSocio | ServicioSocios | `modificarSocio(id, datos)` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 2 | ServicioSocios | FachadaPersistencia«Singleton» | `get(id, Socio)` |  |
| 3 | ServicioSocios | FachadaPersistencia«Singleton» | `contar(SocioPorDni(nuevoDni))` |  |
| 4 | ServicioSocios | FachadaPersistencia«Singleton» | `get(idCategoria, CategoriaSocio)` |  |
| 5 | ServicioSocios | Socio | `actualizarDatos(datos, categoria) (paso 9)` | [Socio.java](../src/main/java/ar/edu/ub/club/socios/dominio/Socio.java) |
| 6 | ServicioSocios | FachadaPersistencia«Singleton» | `put(s)` |  |
| 7 | ServicioSocios | ServicioAuditoria | `registrar(usuario, "MODIFICACION", "Socio", s.id, anterior y nuevo) (paso 10)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

## CU-03 Administrador desactiva socio

<!-- NOTAS:CU-03 -->

**Implementación (reservas y notificaciones) — Cancelación por bajas (sec_comun_cancelar_baja; CU-03, CU-20, CU-23)**

`ServicioSocios.desactivarSocio` / `ServicioRecursos.desactivarCancha/desactivarDeporte` → `ApplicationEventPublisher.publishEvent` →
`CanceladorReservasPorBaja.alDesactivarSocio/alDesactivarCancha/alDesactivarDeporte` → `buscar(new ReservasFuturasDeSocio/DeCancha/DeDeporte(id, ahora))`
→ `cancelarTodas` → `Reserva.cancelar(motivo, evento.usuario, ahora)` → `put` → `publicar(new ReservaCanceladaEvento)`.
Antes de confirmar la baja, socios/recursos/agenda usan `ConsultaReservasFuturasReservas.buscarFuturasDe*` / `buscarDeFecha` / `buscarFuturasDesde`
→ `ResumenesFuturosDe*` / `ResumenesDeFecha` / `ResumenesFuturosDesde` (proyección `ResumenReserva`).

**Diagramas de secuencia del diseño:**

### `sec_cu03_p1.puml` — CU-03 Administrador desactiva socio (parte 1: aviso previo, de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaFichaSocio | `elige Desactivar (paso 1)` | [VistaFichaSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFichaSocio.java) |
| 2 | VistaFichaSocio | PresentadorFichaSocio | `alDesactivar()` | [PresentadorFichaSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorFichaSocio.java) |
| 3 | PresentadorFichaSocio | VistaFichaSocio | `abrirDesactivacion(idSocio, nombre, dni, candidatosTitular)` | [VistaFichaSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFichaSocio.java) |
| 4 | VistaFichaSocio | DialogoDesactivarSocio | `«create» y abrir() (P-09)` | [DialogoDesactivarSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/DialogoDesactivarSocio.java) |
| 5 | DialogoDesactivarSocio | PresentadorDesactivarSocio | `alAbrir(idSocio, nombre, dni, candidatosTitular)` | [PresentadorDesactivarSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorDesactivarSocio.java) |
| 6 | PresentadorDesactivarSocio | DialogoDesactivarSocio | `mostrarSocio(nombre, dni)` | [DialogoDesactivarSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/DialogoDesactivarSocio.java) |
| 7 | PresentadorDesactivarSocio | ServicioSocios | `desactivarSocio(idSocio, null, false)` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 8 | PresentadorDesactivarSocio | DialogoDesactivarSocio | `mostrarError(mensaje)` | [DialogoDesactivarSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/DialogoDesactivarSocio.java) |
| 9 | PresentadorDesactivarSocio | DialogoDesactivarSocio | `mostrarAfectadas(reservas: List<FilaResumenReserva>, candidatosTitular)` | [DialogoDesactivarSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/DialogoDesactivarSocio.java) |
| 10 | Administrador | DialogoDesactivarSocio | `elige Cancelar` | [DialogoDesactivarSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/DialogoDesactivarSocio.java) |

### `sec_cu03_p2.puml` — CU-03 Administrador desactiva socio (parte 2: confirmación, de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | DialogoDesactivarSocio | `elige Confirmar, con el nuevo titular si corresponde (paso 6)` | [DialogoDesactivarSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/DialogoDesactivarSocio.java) |
| 2 | DialogoDesactivarSocio | PresentadorDesactivarSocio | `alConfirmar(idNuevoTitular)` | [PresentadorDesactivarSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorDesactivarSocio.java) |
| 3 | PresentadorDesactivarSocio | ServicioSocios | `desactivarSocio(idSocio, idNuevoTitular, true) (dentro de ejecutar)` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 4 | PresentadorDesactivarSocio | DialogoDesactivarSocio | `mostrarMensaje(baja y cantidad de reservas canceladas)` | [DialogoDesactivarSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/DialogoDesactivarSocio.java) |
| 5 | PresentadorDesactivarSocio | DialogoDesactivarSocio | `cerrarConExito() (dispara DialogoResueltoEvento)` | [DialogoDesactivarSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/DialogoDesactivarSocio.java) |
| 6 | DialogoDesactivarSocio | VistaFichaSocio | `DialogoResueltoEvento` | [VistaFichaSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFichaSocio.java) |
| 7 | VistaFichaSocio | PresentadorFichaSocio | `cargar(idSocio)` | [PresentadorFichaSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorFichaSocio.java) |
| 8 | PresentadorDesactivarSocio | DialogoDesactivarSocio | `mostrarError(mensaje)` | [DialogoDesactivarSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/DialogoDesactivarSocio.java) |
| 9 | PresentadorDesactivarSocio | DialogoDesactivarSocio | `mostrarError(mensaje genérico con código)` | [DialogoDesactivarSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/DialogoDesactivarSocio.java) |

### `sec_cu03_p3.puml` — CU-03 Administrador desactiva socio (parte 3: ServicioSocios.desactivarSocio, primer paso)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorDesactivarSocio | ServicioSocios | `desactivarSocio(id, idNuevoTitular, confirmar)` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 2 | ServicioSocios | FachadaPersistencia«Singleton» | `get(id, Socio)` |  |
| 3 | ServicioSocios | Socio | `estaActiva() (paso 2)` | [Socio.java](../src/main/java/ar/edu/ub/club/socios/dominio/Socio.java) |
| 4 | ServicioSocios | FachadaPersistencia«Singleton» | `buscarUno(GrupoVigente DeSocio(id))` |  |
| 5 | ServicioSocios | GrupoFamiliar | `integrantesActivos() (paso 3)` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 6 | ServicioSocios | ConsultaReservasFuturas | `buscarFuturasDeSocio(id) (paso 4)` | [ConsultaReservasFuturas.java](../src/main/java/ar/edu/ub/club/compartido/dominio/ConsultaReservasFuturas.java) |

### `sec_cu03_p4.puml` — CU-03 Administrador desactiva socio (parte 4: ServicioSocios.desactivarSocio, confirmación)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioSocios | FachadaPersistencia«Singleton» | `get(idNuevoTitular, Socio)` |  |
| 2 | ServicioSocios | GrupoFamiliar | `cambiarTitular(nuevo)` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 3 | ServicioSocios | FachadaPersistencia«Singleton» | `put(g)` |  |
| 4 | ServicioSocios | Socio | `desactivar() (paso 9)` | [Socio.java](../src/main/java/ar/edu/ub/club/socios/dominio/Socio.java) |
| 5 | ServicioSocios | ApplicationEventPublisher | `publishEvent(SocioDesactivado Evento(id, usuario, ahora)) (paso 10)` |  |
| 6 | ServicioSocios | FachadaPersistencia«Singleton» | `put(s) (paso 11)` |  |
| 7 | ServicioSocios | GestorSesiones | `revocarTokensDe(s) (paso 12)` | [GestorSesiones.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/GestorSesiones.java) |
| 8 | ServicioSocios | GestorSesiones | `invalidarSesionesDe(s)` | [GestorSesiones.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/GestorSesiones.java) |
| 9 | ServicioSocios | ServicioAuditoria | `registrar(usuario, "BAJA", "Socio", id, detalle) (paso 13)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

## CU-04 Administrador consulta socio

<!-- NOTAS:CU-04 -->

### `sec_cu04_p1.puml` — CU-04 Administrador consulta socio (parte 1: búsqueda y ficha, de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaListadoSocios | `ingresa el criterio y elige Buscar (paso 1)` | [VistaListadoSocios.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaListadoSocios.java) |
| 2 | VistaListadoSocios | PresentadorListadoSocios | `alBuscar(criterio: CriterioBusquedaSocio)` | [PresentadorListadoSocios.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorListadoSocios.java) |
| 3 | PresentadorListadoSocios | ServicioSocios | `buscarSocios(criterio)` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 4 | ServicioSocios | FachadaPersistencia«Singleton» | `buscar(SociosPorCriterio( texto, estado)) (paso 2)` |  |
| 5 | PresentadorListadoSocios | PresentadorListadoSocios | `FilaSocio.desde(socio)` | [PresentadorListadoSocios.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorListadoSocios.java) |
| 6 | PresentadorListadoSocios | VistaListadoSocios | `mostrarSocios(filas)` | [VistaListadoSocios.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaListadoSocios.java) |
| 7 | Administrador | VistaListadoSocios | `selecciona un socio (paso 4)` | [VistaListadoSocios.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaListadoSocios.java) |
| 8 | VistaListadoSocios | PresentadorListadoSocios | `alSeleccionar(idSocio)` | [PresentadorListadoSocios.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorListadoSocios.java) |
| 9 | PresentadorListadoSocios | VistaListadoSocios | `navegarA("socios/ficha/" + idSocio)` | [VistaListadoSocios.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaListadoSocios.java) |
| 10 | VistaListadoSocios | VistaFichaSocio | `navega a P-08` | [VistaFichaSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFichaSocio.java) |
| 11 | VistaFichaSocio | PresentadorFichaSocio | `cargar(idSocio)` | [PresentadorFichaSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorFichaSocio.java) |
| 12 | PresentadorFichaSocio | ServicioSocios | `consultarSocio(idSocio) (dentro de consultar)` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 13 | PresentadorFichaSocio | VistaFichaSocio | `mostrarFicha(DetalleSocio .desde(ficha))` | [VistaFichaSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFichaSocio.java) |
| 14 | PresentadorFichaSocio | VistaFichaSocio | `mostrarError("El elemento ya no existe.")` | [VistaFichaSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFichaSocio.java) |

### `sec_cu04_p2.puml` — CU-04 Administrador consulta socio (parte 2: ServicioSocios.consultarSocio)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorFichaSocio | ServicioSocios | `consultarSocio(id)` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 2 | ServicioSocios | FachadaPersistencia«Singleton» | `get(id, Socio) (paso 5)` |  |
| 3 | ServicioSocios | FachadaPersistencia«Singleton» | `buscarUno(GrupoVigente DeSocio(id)) (paso 6)` |  |
| 4 | ServicioSocios | GrupoFamiliar | `integrantesActivos()` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 5 | ServicioSocios | ConsultaReservasFuturas | `buscarFuturasDeSocio(id) (paso 7)` | [ConsultaReservasFuturas.java](../src/main/java/ar/edu/ub/club/compartido/dominio/ConsultaReservasFuturas.java) |
| 6 | ServicioSocios | GrupoFamiliar | `tieneCuotaVencida() (paso 8)` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 7 | ServicioSocios | FichaSocio | `«create» (s, g, reservas, estadoCuenta)` | [FichaSocio.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/FichaSocio.java) |

## CU-05 Socio consulta perfil e historial propio

<!-- NOTAS:CU-05 -->

### `sec_cu05_p1.puml` — CU-05 Socio consulta perfil e historial propio (parte 1: perfil)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Socio | VistaMiPerfil | `selecciona Mi perfil (paso 1)` | [VistaMiPerfil.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaMiPerfil.java) |
| 2 | VistaMiPerfil | PresentadorMiPerfil | `cargar()` | [PresentadorMiPerfil.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorMiPerfil.java) |
| 3 | PresentadorMiPerfil | SocioActual | `obtener() (paso 2)` | [SocioActual.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/SocioActual.java) |
| 4 | PresentadorMiPerfil | ServicioSocios | `consultarPerfil Propio(socio)` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 5 | ServicioSocios | FachadaPersistencia«Singleton» | `buscarUno(GrupoVigente DeSocio(socio.id)) (pasos 3-4)` |  |
| 6 | ServicioSocios | GrupoFamiliar | `integrantesActivos()` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 7 | PresentadorMiPerfil | PresentadorMiPerfil | `ResumenPerfil .desde(perfil)` | [PresentadorMiPerfil.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorMiPerfil.java) |
| 8 | PresentadorMiPerfil | VistaMiPerfil | `mostrarPerfil(resumen)` | [VistaMiPerfil.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaMiPerfil.java) |
| 9 | PresentadorMiPerfil | VistaMiPerfil | `mostrarSaldo(resumenSaldo)` | [VistaMiPerfil.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaMiPerfil.java) |

### `sec_cu05_p2.puml` — CU-05 Socio consulta perfil e historial propio (parte 2: cuota y saldo a favor)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorMiPerfil | PresentadorMiPerfil | `resumen sin cuota: «Sin cuotas emitidas»` | [PresentadorMiPerfil.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorMiPerfil.java) |
| 2 | PresentadorMiPerfil | ServicioCuotas | `consultarSaldo( perfil.grupo.id) (paso 6)` | [ServicioCuotas.java](../src/main/java/ar/edu/ub/club/cuotas/aplicacion/ServicioCuotas.java) |
| 3 | ServicioCuotas | FachadaPersistencia«Singleton» | `get(idGrupo, GrupoFamiliar)` |  |
| 4 | ServicioCuotas | GrupoFamiliar | `saldoAFavor()` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 5 | ServicioCuotas | FachadaPersistencia«Singleton» | `buscar(Movimientos DeGrupo(idGrupo))` |  |
| 6 | PresentadorMiPerfil | PresentadorMiPerfil | `ResumenSaldo .desde(saldo)` | [PresentadorMiPerfil.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorMiPerfil.java) |

### `sec_cu05_p3.puml` — CU-05 Socio consulta perfil e historial propio (parte 3: historial y detalle)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Socio | VistaMiPerfil | `Mis reservas` | [VistaMiPerfil.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaMiPerfil.java) |
| 2 | Socio | VistaMiPerfil | `Comprobantes del grupo` | [VistaMiPerfil.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaMiPerfil.java) |
| 3 | Socio | VistaMiPerfil | `Movimientos de saldo` | [VistaMiPerfil.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaMiPerfil.java) |
| 4 | Socio | VistaComprobante | `abre un comprobante (paso 11)` | [VistaComprobante.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaComprobante.java) |
| 5 | VistaComprobante | PresentadorComprobante | `cargar(idComprobante)` | [PresentadorComprobante.java](../src/main/java/ar/edu/ub/club/pagos/ui/PresentadorComprobante.java) |
| 6 | PresentadorComprobante | UsuarioActual | `obtener()` | [UsuarioActual.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/UsuarioActual.java) |
| 7 | PresentadorComprobante | ServicioPagos | `obtenerComprobante (usuario, idComprobante)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 8 | ServicioPagos | FachadaPersistencia«Singleton» | `get(idComprobante, Comprobante)` |  |
| 9 | ServicioPagos | ServicioPagos | `pago del grupo del socio (paso 12, RN-20)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 10 | PresentadorComprobante | ServicioConfiguracionClub | `consultar()` | [ServicioConfiguracionClub.java](../src/main/java/ar/edu/ub/club/configuracion/aplicacion/ServicioConfiguracionClub.java) |
| 11 | PresentadorComprobante | VistaComprobante | `mostrarComprobante(Detalle Comprobante.desde( comprobante, club))` | [VistaComprobante.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaComprobante.java) |
| 12 | PresentadorComprobante | VistaComprobante | `mostrarError("No tiene permiso para realizar esta operación.")` | [VistaComprobante.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaComprobante.java) |

## CU-06 Administrador configura categorías de socio

<!-- NOTAS:CU-06 -->

### `sec_cu06_p1.puml` — CU-06 Administrador configura categorías de socio (parte 1: listado y formulario)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaCategorias | `ingresa a P-10 (paso 1)` | [VistaCategorias.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaCategorias.java) |
| 2 | VistaCategorias | PresentadorCategorias | `cargar()` | [PresentadorCategorias.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorCategorias.java) |
| 3 | PresentadorCategorias | ServicioSocios | `listarCategorias()` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 4 | PresentadorCategorias | PresentadorCategorias | `FilaCategoria.desde(c)` | [PresentadorCategorias.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorCategorias.java) |
| 5 | PresentadorCategorias | VistaCategorias | `mostrarCategorias(filas)` | [VistaCategorias.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaCategorias.java) |
| 6 | Administrador | VistaCategorias | `Nueva categoría o una existente (paso 3)` | [VistaCategorias.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaCategorias.java) |
| 7 | VistaCategorias | PresentadorCategorias | `alEditar(fila o null)` | [PresentadorCategorias.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorCategorias.java) |
| 8 | PresentadorCategorias | VistaCategorias | `editar(new DatosCategoria(nombre, importe, activa) o vacío, idCategoria)` | [VistaCategorias.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaCategorias.java) |
| 9 | Administrador | VistaCategorias | `elige Cancelar` | [VistaCategorias.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaCategorias.java) |

### `sec_cu06_p2.puml` — CU-06 Administrador configura categorías de socio (parte 2: envío, de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaCategorias | `elige Guardar` | [VistaCategorias.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaCategorias.java) |
| 2 | VistaCategorias | VistaCategorias | `FormularioEntidad.leer(): nombre completo, importe mayor o igual a 0 (paso 6)` | [VistaCategorias.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaCategorias.java) |
| 3 | VistaCategorias | PresentadorCategorias | `alGuardar(idCategoria, datos: DatosCategoria)` | [PresentadorCategorias.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorCategorias.java) |
| 4 | PresentadorCategorias | ServicioSocios | `registrarCategoria(datos)` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 5 | PresentadorCategorias | ServicioSocios | `modificarCategoria( idCategoria, datos)` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 6 | PresentadorCategorias | PresentadorCategorias | `cargar()` | [PresentadorCategorias.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorCategorias.java) |
| 7 | PresentadorCategorias | VistaCategorias | `mostrarCategorias(filas) y mostrarMensaje(rige desde la próxima cuota)` | [VistaCategorias.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaCategorias.java) |
| 8 | PresentadorCategorias | VistaCategorias | `marcarCampo(campo, mensaje) o mostrarError(mensaje)` | [VistaCategorias.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaCategorias.java) |

### `sec_cu06_p3.puml` — CU-06 Administrador configura categorías de socio (parte 3: registrarCategoria y modificarCategoria)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorCategorias | ServicioSocios | `registrarCategoria(datos) o modificarCategoria(id, datos)` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 2 | ServicioSocios | FachadaPersistencia«Singleton» | `contar(CategoriaPor Nombre(nombre)) (paso 7)` |  |
| 3 | ServicioSocios | CategoriaSocio | `«create» (nombre, importe, activa)` | [CategoriaSocio.java](../src/main/java/ar/edu/ub/club/socios/dominio/CategoriaSocio.java) |
| 4 | ServicioSocios | FachadaPersistencia«Singleton» | `get(id, CategoriaSocio)` |  |
| 5 | ServicioSocios | FachadaPersistencia«Singleton» | `contar(SociosActivos DeCategoria(id))` |  |
| 6 | ServicioSocios | CategoriaSocio | `modificar(nombre, importe, activa)` | [CategoriaSocio.java](../src/main/java/ar/edu/ub/club/socios/dominio/CategoriaSocio.java) |
| 7 | ServicioSocios | FachadaPersistencia«Singleton» | `put(c) (paso 9)` |  |
| 8 | ServicioSocios | ServicioAuditoria | `registrar(usuario, "ALTA" o "MODIFICACION", "CategoriaSocio", c.id, detalle) (paso 10)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

## CU-07 Administrador asocia socio a grupo familiar

<!-- NOTAS:CU-07 -->

### `sec_cu07_p1.puml` — CU-07 Administrador asocia socio a grupo familiar (parte 1: búsqueda del grupo destino)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaFichaSocio | `Asociar a grupo familiar (paso 1)` | [VistaFichaSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFichaSocio.java) |
| 2 | VistaFichaSocio | PresentadorFichaSocio | `alAsociarAGrupo()` | [PresentadorFichaSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/PresentadorFichaSocio.java) |
| 3 | PresentadorFichaSocio | VistaFichaSocio | `abrirAsociacion( idSocio, nombre, dni, idGrupo, grupoActual, null)` | [VistaFichaSocio.java](../src/main/java/ar/edu/ub/club/socios/ui/VistaFichaSocio.java) |
| 4 | VistaFichaSocio | DialogoAsociarSocio | `«create» y abrir() (P-12, paso 2)` | [DialogoAsociarSocio.java](../src/main/java/ar/edu/ub/club/grupos/ui/DialogoAsociarSocio.java) |
| 5 | DialogoAsociarSocio | PresentadorAsociarSocio | `alAbrir(idSocio, nombre, dni, idGrupoActual, grupoActual, null)` | [PresentadorAsociarSocio.java](../src/main/java/ar/edu/ub/club/grupos/ui/PresentadorAsociarSocio.java) |
| 6 | PresentadorAsociarSocio | DialogoAsociarSocio | `mostrarSocio(nombre, dni, grupoActual)` | [DialogoAsociarSocio.java](../src/main/java/ar/edu/ub/club/grupos/ui/DialogoAsociarSocio.java) |
| 7 | Administrador | DialogoAsociarSocio | `ingresa el texto y elige Buscar (paso 3)` | [DialogoAsociarSocio.java](../src/main/java/ar/edu/ub/club/grupos/ui/DialogoAsociarSocio.java) |
| 8 | DialogoAsociarSocio | PresentadorAsociarSocio | `alBuscar(texto)` | [PresentadorAsociarSocio.java](../src/main/java/ar/edu/ub/club/grupos/ui/PresentadorAsociarSocio.java) |
| 9 | PresentadorAsociarSocio | ServicioGruposFamiliares | `buscarGrupos(new Criterio BusquedaGrupo(texto, true))` | [ServicioGruposFamiliares.java](../src/main/java/ar/edu/ub/club/grupos/aplicacion/ServicioGruposFamiliares.java) |
| 10 | ServicioGruposFamiliares | FachadaPersistencia«Singleton» | `buscar(GruposPorCriterio( texto, vigente))` |  |
| 11 | PresentadorAsociarSocio | PresentadorAsociarSocio | `FilaGrupo.desde(r)` | [PresentadorAsociarSocio.java](../src/main/java/ar/edu/ub/club/grupos/ui/PresentadorAsociarSocio.java) |
| 12 | PresentadorAsociarSocio | DialogoAsociarSocio | `mostrarGrupos(filas)` | [DialogoAsociarSocio.java](../src/main/java/ar/edu/ub/club/grupos/ui/DialogoAsociarSocio.java) |
| 13 | Administrador | DialogoAsociarSocio | `elige una fila: grupo destino (paso 5)` | [DialogoAsociarSocio.java](../src/main/java/ar/edu/ub/club/grupos/ui/DialogoAsociarSocio.java) |

### `sec_cu07_p2.puml` — CU-07 Administrador asocia socio a grupo familiar (parte 2: confirmación, de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | DialogoAsociarSocio | `elige Cancelar` | [DialogoAsociarSocio.java](../src/main/java/ar/edu/ub/club/grupos/ui/DialogoAsociarSocio.java) |
| 2 | Administrador | DialogoAsociarSocio | `elige Confirmar` | [DialogoAsociarSocio.java](../src/main/java/ar/edu/ub/club/grupos/ui/DialogoAsociarSocio.java) |
| 3 | DialogoAsociarSocio | PresentadorAsociarSocio | `alConfirmar( idGrupoDestino)` | [PresentadorAsociarSocio.java](../src/main/java/ar/edu/ub/club/grupos/ui/PresentadorAsociarSocio.java) |
| 4 | PresentadorAsociarSocio | ServicioGruposFamiliares | `asociarSocio(idSocio, idGrupoDestino) (dentro de ejecutar)` | [ServicioGruposFamiliares.java](../src/main/java/ar/edu/ub/club/grupos/aplicacion/ServicioGruposFamiliares.java) |
| 5 | PresentadorAsociarSocio | DialogoAsociarSocio | `navegarA("grupos/" + idGrupoDestino)` | [DialogoAsociarSocio.java](../src/main/java/ar/edu/ub/club/grupos/ui/DialogoAsociarSocio.java) |
| 6 | DialogoAsociarSocio | VistaGrupoFamiliar | `navega a P-11 del grupo destino` | [VistaGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/VistaGrupoFamiliar.java) |
| 7 | PresentadorAsociarSocio | DialogoAsociarSocio | `mostrarError(mensaje)` | [DialogoAsociarSocio.java](../src/main/java/ar/edu/ub/club/grupos/ui/DialogoAsociarSocio.java) |

### `sec_cu07_p3.puml` — CU-07 Administrador asocia socio a grupo familiar (parte 3: ServicioGruposFamiliares.asociarSocio)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorAsociarSocio | ServicioGruposFamiliares | `asociarSocio(idSocio, idGrupoDestino)` | [ServicioGruposFamiliares.java](../src/main/java/ar/edu/ub/club/grupos/aplicacion/ServicioGruposFamiliares.java) |
| 2 | ServicioGruposFamiliares | FachadaPersistencia«Singleton» | `get(idSocio, Socio)` |  |
| 3 | ServicioGruposFamiliares | FachadaPersistencia«Singleton» | `buscarUno(GrupoVigente DeSocio(idSocio))` |  |
| 4 | ServicioGruposFamiliares | FachadaPersistencia«Singleton» | `get(idGrupoDestino, GrupoFamiliar)` |  |
| 5 | ServicioGruposFamiliares | GrupoFamiliar | `integrantesActivos() (paso 7)` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 6 | ServicioGruposFamiliares | GrupoFamiliar | `quitarIntegrante(s) (paso 10)` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 7 | ServicioGruposFamiliares | GrupoFamiliar | `agregarIntegrante(s) (paso 11)` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 8 | ServicioGruposFamiliares | FachadaPersistencia«Singleton» | `put(origen)` |  |
| 9 | ServicioGruposFamiliares | FachadaPersistencia«Singleton» | `put(destino) (paso 12)` |  |
| 10 | ServicioGruposFamiliares | ServicioAuditoria | `registrar(usuario, "MODIFICACION", "GrupoFamiliar", destino.id, detalle) (paso 13)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

## CU-08 Administrador cambia titular de grupo familiar

<!-- NOTAS:CU-08 -->

### `sec_cu08_p1.puml` — CU-08 Administrador cambia titular de grupo familiar (parte 1: de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaGrupoFamiliar | `Cambiar titular (paso 1)` | [VistaGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/VistaGrupoFamiliar.java) |
| 2 | Administrador | VistaGrupoFamiliar | `elige al nuevo titular (paso 3)` | [VistaGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/VistaGrupoFamiliar.java) |
| 3 | VistaGrupoFamiliar | DialogoConfirmacion | `«create» y abrir(): el nuevo titular paga la cuota (paso 5)` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 4 | Administrador | DialogoConfirmacion | `elige Cancelar` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 5 | Administrador | DialogoConfirmacion | `elige Confirmar` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 6 | DialogoConfirmacion | VistaGrupoFamiliar | `oyente de alConfirmar` | [VistaGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/VistaGrupoFamiliar.java) |
| 7 | VistaGrupoFamiliar | PresentadorGrupoFamiliar | `alCambiarTitular( idNuevoTitular)` | [PresentadorGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/PresentadorGrupoFamiliar.java) |
| 8 | PresentadorGrupoFamiliar | ServicioGruposFamiliares | `cambiarTitular( idGrupo, idNuevoTitular) (dentro de ejecutar)` | [ServicioGruposFamiliares.java](../src/main/java/ar/edu/ub/club/grupos/aplicacion/ServicioGruposFamiliares.java) |
| 9 | PresentadorGrupoFamiliar | VistaGrupoFamiliar | `mostrarGrupo(FichaGrupo .desde(detalle))` | [VistaGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/VistaGrupoFamiliar.java) |
| 10 | PresentadorGrupoFamiliar | VistaGrupoFamiliar | `mostrarError(mensaje)` | [VistaGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/VistaGrupoFamiliar.java) |

### `sec_cu08_p2.puml` — CU-08 Administrador cambia titular de grupo familiar (parte 2: ServicioGruposFamiliares.cambiarTitular)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorGrupoFamiliar | ServicioGruposFamiliares | `cambiarTitular(idGrupo, idNuevoTitular)` | [ServicioGruposFamiliares.java](../src/main/java/ar/edu/ub/club/grupos/aplicacion/ServicioGruposFamiliares.java) |
| 2 | ServicioGruposFamiliares | FachadaPersistencia«Singleton» | `get(idGrupo, GrupoFamiliar)` |  |
| 3 | ServicioGruposFamiliares | FachadaPersistencia«Singleton» | `get(idNuevoTitular, Socio)` |  |
| 4 | ServicioGruposFamiliares | GrupoFamiliar | `cambiarTitular(nuevo) (pasos 4 y 7)` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 5 | GrupoFamiliar | GrupoFamiliar | `integrantesActivos()` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 6 | GrupoFamiliar | GrupoFamiliar | `titular = nuevo` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 7 | ServicioGruposFamiliares | FachadaPersistencia«Singleton» | `put(g)` |  |
| 8 | ServicioGruposFamiliares | ServicioAuditoria | `registrar(usuario, "MODIFICACION", "GrupoFamiliar", g.id, titular anterior y nuevo) (paso 8)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

## CU-09 Administrador quita socio de grupo familiar

<!-- NOTAS:CU-09 -->

### `sec_cu09_p1.puml` — CU-09 Administrador quita socio de grupo familiar (parte 1: de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaGrupoFamiliar | `Quitar del grupo (paso 1)` | [VistaGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/VistaGrupoFamiliar.java) |
| 2 | VistaGrupoFamiliar | DialogoConfirmacion | `«create» y abrir(): pasará a un grupo propio (paso 4)` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 3 | Administrador | DialogoConfirmacion | `elige Cancelar` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 4 | Administrador | DialogoConfirmacion | `elige Confirmar` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 5 | DialogoConfirmacion | VistaGrupoFamiliar | `oyente de alConfirmar` | [VistaGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/VistaGrupoFamiliar.java) |
| 6 | VistaGrupoFamiliar | PresentadorGrupoFamiliar | `alQuitar(idSocio)` | [PresentadorGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/PresentadorGrupoFamiliar.java) |
| 7 | PresentadorGrupoFamiliar | ServicioGruposFamiliares | `quitarSocio(idGrupo, idSocio) (dentro de ejecutar)` | [ServicioGruposFamiliares.java](../src/main/java/ar/edu/ub/club/grupos/aplicacion/ServicioGruposFamiliares.java) |
| 8 | PresentadorGrupoFamiliar | VistaGrupoFamiliar | `mostrarGrupo(FichaGrupo .desde(detalle))` | [VistaGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/VistaGrupoFamiliar.java) |
| 9 | PresentadorGrupoFamiliar | VistaGrupoFamiliar | `mostrarError(mensaje)` | [VistaGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/VistaGrupoFamiliar.java) |

### `sec_cu09_p2.puml` — CU-09 Administrador quita socio de grupo familiar (parte 2: quitarSocio y crearGrupoUnipersonal)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorGrupoFamiliar | ServicioGruposFamiliares | `quitarSocio(idGrupo, idSocio)` | [ServicioGruposFamiliares.java](../src/main/java/ar/edu/ub/club/grupos/aplicacion/ServicioGruposFamiliares.java) |
| 2 | ServicioGruposFamiliares | FachadaPersistencia«Singleton» | `get(idGrupo, GrupoFamiliar)` |  |
| 3 | ServicioGruposFamiliares | FachadaPersistencia«Singleton» | `get(idSocio, Socio)` |  |
| 4 | ServicioGruposFamiliares | GrupoFamiliar | `integrantesActivos() (paso 2)` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 5 | ServicioGruposFamiliares | GrupoFamiliar | `quitarIntegrante(s) (paso 6)` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 6 | ServicioGruposFamiliares | GrupoFamiliar | `«create» (fechaCreacion = hoy, vigente = true)` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 7 | ServicioGruposFamiliares | GrupoFamiliar | `agregarIntegrante(s)` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 8 | ServicioGruposFamiliares | GrupoFamiliar | `cambiarTitular(s)` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 9 | ServicioGruposFamiliares | FachadaPersistencia«Singleton» | `put(nuevo)` |  |
| 10 | ServicioGruposFamiliares | FachadaPersistencia«Singleton» | `put(g) (paso 8)` |  |
| 11 | ServicioGruposFamiliares | ServicioAuditoria | `registrar(usuario, "MODIFICACION", "GrupoFamiliar", g.id, detalle) (paso 9)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

## CU-10 Administrador consulta grupo familiar

<!-- NOTAS:CU-10 -->

### `sec_cu10_p1.puml` — CU-10 Administrador consulta grupo familiar (parte 1: búsqueda)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaGrupoFamiliar | `ingresa DNI o nombre y elige Buscar (paso 1)` | [VistaGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/VistaGrupoFamiliar.java) |
| 2 | VistaGrupoFamiliar | PresentadorGrupoFamiliar | `alBuscarGrupo(texto)` | [PresentadorGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/PresentadorGrupoFamiliar.java) |
| 3 | PresentadorGrupoFamiliar | ServicioGruposFamiliares | `buscarGrupos(new Criterio BusquedaGrupo(texto, null))` | [ServicioGruposFamiliares.java](../src/main/java/ar/edu/ub/club/grupos/aplicacion/ServicioGruposFamiliares.java) |
| 4 | ServicioGruposFamiliares | FachadaPersistencia«Singleton» | `buscar(GruposPorCriterio( texto, vigente))` |  |
| 5 | PresentadorGrupoFamiliar | PresentadorGrupoFamiliar | `FilaGrupo.desde(r)` | [PresentadorGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/PresentadorGrupoFamiliar.java) |
| 6 | PresentadorGrupoFamiliar | VistaGrupoFamiliar | `mostrarGrupos(filas)` | [VistaGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/VistaGrupoFamiliar.java) |

### `sec_cu10_p2.puml` — CU-10 Administrador consulta grupo familiar (parte 2: detalle del grupo)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaGrupoFamiliar | `selecciona un grupo (paso 3)` | [VistaGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/VistaGrupoFamiliar.java) |
| 2 | VistaGrupoFamiliar | PresentadorGrupoFamiliar | `cargar(idGrupo)` | [PresentadorGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/PresentadorGrupoFamiliar.java) |
| 3 | PresentadorGrupoFamiliar | ServicioGruposFamiliares | `consultarGrupo( idGrupo) (dentro de consultar)` | [ServicioGruposFamiliares.java](../src/main/java/ar/edu/ub/club/grupos/aplicacion/ServicioGruposFamiliares.java) |
| 4 | ServicioGruposFamiliares | FachadaPersistencia«Singleton» | `get(idGrupo, GrupoFamiliar) (paso 4)` |  |
| 5 | ServicioGruposFamiliares | GrupoFamiliar | `integrantes Activos() (paso 5)` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 6 | ServicioGruposFamiliares | GrupoFamiliar | `calcularImporte Cuota()` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 7 | PresentadorGrupoFamiliar | VistaGrupoFamiliar | `mostrarGrupo(FichaGrupo .desde(detalle))` | [VistaGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/VistaGrupoFamiliar.java) |
| 8 | Administrador | VistaGrupoFamiliar | `selecciona un integrante en TablaDatos (paso 7)` | [VistaGrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/ui/VistaGrupoFamiliar.java) |

## CU-11 Usuario inicia sesión

<!-- NOTAS:CU-11 -->

**Implementación (accesos, auditoría y configuración) — CU-11 Iniciar sesión — `sec_cu11_p1.puml` … `sec_cu11_p4.puml`**

1. `VistaInicioSesion` (LoginForm, oyente de login) → `PresentadorInicioSesion.alIngresar(username, clave, mantenerSesion)` (p1)
2. `PresentadorBase.consultar` → `ServicioAccesos.iniciarSesion` (p1, p2) → `Reloj.ahora`
3. `FachadaPersistencia.buscarUno(new UsuarioPorUsername(username))` → `BrokerAdministrador`/`BrokerSocio.materializarTodos` (`BrokerUsuario.consultaBase`: `usuario JOIN administrador|socio`) → tablas `usuario`, `administrador`, `socio` (p2)
4. `Usuario.estaBloqueada` → `Usuario.autenticar` → `CodificadorClaveBCrypt.coincide`; si falla `Usuario.registrarIntentoFallido` + `FachadaPersistencia.put` → `BrokerUsuario.valoresActualizacion` (UPDATE `usuario`) (p2)
5. `Usuario.estaActiva` → `Usuario.reiniciarIntentos` + `put` → `Usuario.obtenerPermisos` (polimórfico) → `new SesionUsuario` (p3)
6. si `mantenerSesion && admiteSesionPersistente()`: `GestorSesiones.emitirTokenPersistente` → `new TokenSesionPersistente` → `put` → `BrokerTokenSesionPersistente.insertar` (tabla `token_sesion_persistente`) → `SesionUsuario.agregarTokenPersistente` (p3)
7. `RegistroSesionVaadin.iniciar(sesion)` (changeSessionId, `HttpSessionSecurityContextRepository.saveContext`, `SessionRegistry.registerNewSession`, `ServicioRecordarmeClub.escribirCookie`) → `pantalla.navegarA("panel"|"inicio")` (p1)
8. FA1/FA2 (p4): `ServicioRecordarmeClub.processAutoLoginCookie` → `AlmacenTokensSesionPersistente.getTokenForSeries` → `buscarPorSerie` → `buscarUno(new TokenSesionPorSerie)` → `BrokerTokenSesionPersistente` → `CuentasParaRecordarme.loadUserByUsername` → `updateToken` → `actualizarToken` → `TokenSesionPersistente.rotar` → `put`; robo: `removeUserTokens` → `eliminarDeUsuario` → `buscar(new TokensSesionDeUsuario)` → `eliminar`. Luego `VistaRaiz.beforeEnter` → `UsuarioActualSesion.autenticada/cuenta` → `ControlAcceso.tienePermiso`.
9. FA3: `PresentadorInicioSesion.alOlvidarClave` → `navegarA("recuperar-clave")`.

**Diagramas de secuencia del diseño:**

### `sec_cu11_p1.puml` — CU-11 Usuario inicia sesión (parte 1: de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Usuario(SociooAdministrador) | VistaInicioSesion | `completa usuario y clave, marca o no la casilla y elige Ingresar (paso 3)` | [VistaInicioSesion.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaInicioSesion.java) |
| 2 | VistaInicioSesion | PresentadorInicioSesion | `alIngresar(username, clave, mantenerSesion)` | [PresentadorInicioSesion.java](../src/main/java/ar/edu/ub/club/accesos/ui/PresentadorInicioSesion.java) |
| 3 | PresentadorInicioSesion | ServicioAccesos | `iniciarSesion( username, clave, mantenerSesion) (dentro de ejecutar)` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 4 | PresentadorInicioSesion | RegistroSesionVaadin«RegistroSesionWeb» | `iniciar(s)` |  |
| 5 | RegistroSesionVaadin«RegistroSesionWeb» | RegistroSesionVaadin«RegistroSesionWeb» | `cambia el id de sesión (changeSessionId)` |  |
| 6 | RegistroSesionVaadin«RegistroSesionWeb» | RegistroSesionVaadin«RegistroSesionWeb» | `guarda el SecurityContext en la sesión HTTP (en memoria)` |  |
| 7 | RegistroSesionVaadin«RegistroSesionWeb» | SessionRegistry«SpringSecurity» | `registerNewSession( idSesion, principal)` |  |
| 8 | RegistroSesionVaadin«RegistroSesionWeb» | RegistroSesionVaadin«RegistroSesionWeb» | `escribe la cookie remember-me de 30 días (HttpOnly, Secure, SameSite=Strict)` |  |
| 9 | PresentadorInicioSesion | VistaInicioSesion | `navegarA("panel")` | [VistaInicioSesion.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaInicioSesion.java) |
| 10 | PresentadorInicioSesion | VistaInicioSesion | `navegarA("inicio")` | [VistaInicioSesion.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaInicioSesion.java) |
| 11 | PresentadorInicioSesion | VistaInicioSesion | `mostrarError(mensaje)` | [VistaInicioSesion.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaInicioSesion.java) |
| 12 | Usuario(SociooAdministrador) | VistaInicioSesion | `elige «Olvidé mi contraseña»` | [VistaInicioSesion.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaInicioSesion.java) |
| 13 | VistaInicioSesion | PresentadorInicioSesion | `alOlvidarClave()` | [PresentadorInicioSesion.java](../src/main/java/ar/edu/ub/club/accesos/ui/PresentadorInicioSesion.java) |
| 14 | PresentadorInicioSesion | VistaInicioSesion | `navegarA("recuperar-clave")` | [VistaInicioSesion.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaInicioSesion.java) |

### `sec_cu11_p2.puml` — CU-11 Usuario inicia sesión (parte 2: ServicioAccesos.iniciarSesion, pasos 4 a 6)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorInicioSesion | ServicioAccesos | `iniciarSesion(username, clave, mantenerSesion)` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 2 | ServicioAccesos | Reloj | `ahora()` | [Reloj.java](../src/main/java/ar/edu/ub/club/compartido/dominio/Reloj.java) |
| 3 | ServicioAccesos | FachadaPersistencia«Singleton» | `buscarUno(UsuarioPor Username(username)) (paso 4)` |  |
| 4 | ServicioAccesos | Usuario{SociooAdministrador} | `estaBloqueada(ahora) (paso 5)` |  |
| 5 | ServicioAccesos | Usuario{SociooAdministrador} | `autenticar(clave, codificadorClave) (paso 6)` |  |
| 6 | Usuario{SociooAdministrador} | CodificadorClave | `coincide(clave, passwordHash)` | [CodificadorClave.java](../src/main/java/ar/edu/ub/club/accesos/dominio/CodificadorClave.java) |
| 7 | ServicioAccesos | Usuario{SociooAdministrador} | `registrarIntentoFallido( ahora)` |  |
| 8 | ServicioAccesos | FachadaPersistencia«Singleton» | `put(u)` |  |

### `sec_cu11_p3.puml` — CU-11 Usuario inicia sesión (parte 3: ServicioAccesos.iniciarSesion, pasos 7 a 10)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioAccesos | Usuario{SociooAdministrador} | `estaActiva() (paso 7)` |  |
| 2 | ServicioAccesos | Usuario{SociooAdministrador} | `reiniciarIntentos() (paso 8)` |  |
| 3 | ServicioAccesos | FachadaPersistencia«Singleton» | `put(u)` |  |
| 4 | ServicioAccesos | Usuario{SociooAdministrador} | `obtenerPermisos() (paso 9)` |  |
| 5 | ServicioAccesos | SesionUsuario | `«create» (u.id, nombreVisible, permisos)` | [SesionUsuario.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/SesionUsuario.java) |
| 6 | ServicioAccesos | Usuario{SociooAdministrador} | `admiteSesionPersistente() (Socio: true; Admin.: false)` |  |
| 7 | ServicioAccesos | GestorSesiones | `emitirTokenPersistente(u)` | [GestorSesiones.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/GestorSesiones.java) |
| 8 | GestorSesiones | FachadaPersistencia«Singleton» | `put(token)` |  |
| 9 | ServicioAccesos | SesionUsuario | `agrega el token persistente` | [SesionUsuario.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/SesionUsuario.java) |

### `sec_cu11_p4.puml` — CU-11 Usuario inicia sesión (parte 4: ingreso con sesión persistente, FA1 y FA2)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Socio | PersistentTokenBasedRememberMeServices«SpringSecurity» | `abre la aplicación sin sesión del servidor, con la cookie remember-me: autoLogin(...)` |  |
| 2 | PersistentTokenBasedRememberMeServices«SpringSecurity» | AlmacenTokensSesionPersistente | `buscarPorSerie(serie)` | [AlmacenTokensSesionPersistente.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/AlmacenTokensSesionPersistente.java) |
| 3 | AlmacenTokensSesionPersistente | FachadaPersistencia«Singleton» | `buscarUno(TokenSesion PorSerie(serie))` |  |
| 4 | PersistentTokenBasedRememberMeServices«SpringSecurity» | AlmacenTokensSesionPersistente | `actualizarToken( serie, nuevoHash, ahora)` | [AlmacenTokensSesionPersistente.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/AlmacenTokensSesionPersistente.java) |
| 5 | AlmacenTokensSesionPersistente | FachadaPersistencia«Singleton» | `buscarUno(TokenSesion PorSerie(serie))` |  |
| 6 | AlmacenTokensSesionPersistente | AlmacenTokensSesionPersistente | `token.rotar(nuevoHash, ahora) (1.2)` | [AlmacenTokensSesionPersistente.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/AlmacenTokensSesionPersistente.java) |
| 7 | AlmacenTokensSesionPersistente | FachadaPersistencia«Singleton» | `put(token)` |  |
| 8 | PersistentTokenBasedRememberMeServices«SpringSecurity» | PersistentTokenBasedRememberMeServices«SpringSecurity» | `crea la sesión del servidor en memoria, registrada en el SessionRegistry (1.3)` |  |
| 9 | PersistentTokenBasedRememberMeServices«SpringSecurity» | VistaRaiz | `la petición sigue: beforeEnter(evento)` | [VistaRaiz.java](../src/main/java/ar/edu/ub/club/compartido/ui/VistaRaiz.java) |
| 10 | VistaRaiz | SesionActual | `autenticada()` | [SesionActual.java](../src/main/java/ar/edu/ub/club/compartido/seguridad/SesionActual.java) |
| 11 | VistaRaiz | SesionActual | `cuenta()` | [SesionActual.java](../src/main/java/ar/edu/ub/club/compartido/seguridad/SesionActual.java) |
| 12 | VistaRaiz | ControlAcceso | `tienePermiso(u, CONSULTAR_DASHBOARD)` | [ControlAcceso.java](../src/main/java/ar/edu/ub/club/compartido/seguridad/ControlAcceso.java) |
| 13 | PersistentTokenBasedRememberMeServices«SpringSecurity» | PersistentTokenBasedRememberMeServices«SpringSecurity» | `borra la cookie remember-me` |  |
| 14 | PersistentTokenBasedRememberMeServices«SpringSecurity» | VistaRaiz | `la petición sigue sin sesión: beforeEnter(evento)` | [VistaRaiz.java](../src/main/java/ar/edu/ub/club/compartido/ui/VistaRaiz.java) |
| 15 | VistaRaiz | SesionActual | `autenticada()` | [SesionActual.java](../src/main/java/ar/edu/ub/club/compartido/seguridad/SesionActual.java) |
| 16 | PersistentTokenBasedRememberMeServices«SpringSecurity» | AlmacenTokensSesionPersistente | `eliminarDeUsuario( idUsuario)` | [AlmacenTokensSesionPersistente.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/AlmacenTokensSesionPersistente.java) |
| 17 | AlmacenTokensSesionPersistente | FachadaPersistencia«Singleton» | `buscar(TokensSesion DeUsuario(idUsuario))` |  |
| 18 | AlmacenTokensSesionPersistente | FachadaPersistencia«Singleton» | `eliminar(t)` |  |
| 19 | PersistentTokenBasedRememberMeServices«SpringSecurity» | PersistentTokenBasedRememberMeServices«SpringSecurity» | `borra la cookie remember-me` |  |
| 20 | PersistentTokenBasedRememberMeServices«SpringSecurity» | VistaRaiz | `beforeEnter(evento) sin sesión` | [VistaRaiz.java](../src/main/java/ar/edu/ub/club/compartido/ui/VistaRaiz.java) |

## CU-12 Usuario cierra sesión

<!-- NOTAS:CU-12 -->

**Implementación (accesos, auditoría y configuración) — CU-12 Cerrar sesión — `sec_cu12.puml`**

`MenuNavegacion.alCerrarSesion` → `PresentadorCierreSesion.cerrarSesion` → `ServicioAccesos.consultarSesionActual` (`UsuarioActualSesion.obtener` → `FachadaPersistencia.get(id, Usuario)`) → `ServicioAccesos.cerrarSesion` → `GestorSesiones.revocarTokensDe` → `AlmacenTokensSesionPersistente.eliminarDeUsuario` → `buscar(new TokensSesionDeUsuario)` → `FachadaPersistencia.eliminar` → `BrokerTokenSesionPersistente.borrar` (tabla `token_sesion_persistente`) → `RegistroSesionVaadin.cerrar` (`ServicioRecordarmeClub.borrarCookie`, `SecurityContextLogoutHandler.logout`, `VaadinSession.close`) → `MenuNavegacion`: `setLocation("inicio-sesion")`.

**Diagramas de secuencia del diseño:**

### `sec_cu12.puml` — CU-12 Usuario cierra sesión

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Usuario(SociooAdministrador) | MenuNavegacion | `elige «Cerrar sesión» (P-35, paso 1)` | [MenuNavegacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/MenuNavegacion.java) |
| 2 | MenuNavegacion | MenuNavegacion | `alCerrarSesion()` | [MenuNavegacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/MenuNavegacion.java) |
| 3 | MenuNavegacion | PresentadorCierreSesion«AccionCierreSesion» | `cerrarSesion()` |  |
| 4 | PresentadorCierreSesion«AccionCierreSesion» | ServicioAccesos | `consultarSesionActual()` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 5 | PresentadorCierreSesion«AccionCierreSesion» | ServicioAccesos | `cerrarSesion(sesion)` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 6 | ServicioAccesos | GestorSesiones | `revocarTokensDe( usuario) (paso 3)` | [GestorSesiones.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/GestorSesiones.java) |
| 7 | ServicioAccesos | ServicioAccesos | `registra el cierre en la bitácora técnica` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 8 | PresentadorCierreSesion«AccionCierreSesion» | RegistroSesionVaadin«RegistroSesionWeb» | `cerrar() (pasos 2 y 4)` |  |
| 9 | RegistroSesionVaadin«RegistroSesionWeb» | RegistroSesionVaadin«RegistroSesionWeb» | `SecurityContextLogoutHandler .logout(...)` |  |
| 10 | RegistroSesionVaadin«RegistroSesionWeb» | RegistroSesionVaadin«RegistroSesionWeb» | `invalida la sesión HTTP y la VaadinSession (sale del SessionRegistry)` |  |
| 11 | RegistroSesionVaadin«RegistroSesionWeb» | RegistroSesionVaadin«RegistroSesionWeb» | `borra la cookie remember-me` |  |
| 12 | MenuNavegacion | MenuNavegacion | `UI.getPage().setLocation( "inicio-sesion")` | [MenuNavegacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/MenuNavegacion.java) |

## CU-13 Usuario recupera contraseña

<!-- NOTAS:CU-13 -->

**Implementación (accesos, auditoría y configuración) — CU-13 Recuperar contraseña — `sec_cu13_p1.puml` … `sec_cu13_p4.puml`**

1. `VistaRecuperarClave` (EmailField; FA1 sin presentador) → `PresentadorRecuperarClave.alSolicitar(email)` → `ServicioAccesos.solicitarRecuperacion` (p1, p2)
2. `buscarUno(new UsuarioPorEmail)` → brokers de `Usuario` → `usuario` (p2); `buscar(new TokensRecuperacionVigentes(id, ahora))` → `BrokerTokenRecuperacion` → `TokenRecuperacion.marcarUsado` + `put` (tabla `token_recuperacion`)
3. `GestorSesiones.hashSha256(token)` → `new TokenRecuperacion(u, hash, ahora+30min)` → `put` → `BrokerTokenRecuperacion.insertar` → `ServicioAccesos.enviarEnlace` → `ServicioCorreo.enviar(new MensajeCorreo(...))` (p2) → `pantalla.mostrarMensaje` (mismo mensaje siempre)
4. `VistaRestablecerClave.beforeEnter` → `PresentadorRestablecerClave.alEntrar(token)` → `alRestablecer(nueva, repeticion)` (p3) → `ServicioAccesos.restablecerClave` (p4)
5. `buscarUno(new TokenRecuperacionPorHash(sha256))` → `BrokerTokenRecuperacion.cargarAsociaciones` (`get(usuarioId, Usuario)`) → `TokenRecuperacion.esValido` → `PoliticaContrasenas.validar` (`ListaContrasenasProhibidas.contiene`) → `CodificadorClaveBCrypt.codificar` → `Usuario.cambiarClave` → `TokenRecuperacion.marcarUsado` → `Usuario.reiniciarIntentos` → `put(u)`, `put(t)` → `GestorSesiones.revocarTokensDe` → `GestorSesiones.invalidarSesionesDe` (`SessionInformation.expireNow`) (p4) → `navegarA("inicio-sesion")`.

**Diagramas de secuencia del diseño:**

### `sec_cu13_p1.puml` — CU-13 Usuario recupera contraseña (parte 1: solicitud del enlace, de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Usuario | VistaInicioSesion | `elige «Olvidé mi contraseña» (paso 1)` | [VistaInicioSesion.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaInicioSesion.java) |
| 2 | VistaInicioSesion | PresentadorInicioSesion | `alOlvidarClave()` | [PresentadorInicioSesion.java](../src/main/java/ar/edu/ub/club/accesos/ui/PresentadorInicioSesion.java) |
| 3 | PresentadorInicioSesion | VistaInicioSesion | `navegarA("recuperar-clave")` | [VistaInicioSesion.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaInicioSesion.java) |
| 4 | VistaInicioSesion | VistaRecuperarClave | `navega a P-02 (pública) (paso 2)` | [VistaRecuperarClave.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaRecuperarClave.java) |
| 5 | Usuario | VistaRecuperarClave | `ingresa el correo y elige Enviar (paso 3)` | [VistaRecuperarClave.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaRecuperarClave.java) |
| 6 | VistaRecuperarClave | PresentadorRecuperarClave | `alSolicitar(email)` | [PresentadorRecuperarClave.java](../src/main/java/ar/edu/ub/club/accesos/ui/PresentadorRecuperarClave.java) |
| 7 | PresentadorRecuperarClave | ServicioAccesos | `solicitarRecuperacion( email) (dentro de ejecutar)` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 8 | PresentadorRecuperarClave | VistaRecuperarClave | `mostrarMensaje(siempre el mismo mensaje)` | [VistaRecuperarClave.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaRecuperarClave.java) |

### `sec_cu13_p2.puml` — CU-13 Usuario recupera contraseña (parte 2: ServicioAccesos.solicitarRecuperacion)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorRecuperarClave | ServicioAccesos | `solicitarRecuperacion( email)` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 2 | ServicioAccesos | FachadaPersistencia«Singleton» | `buscarUno(UsuarioPor Email(email)) (paso 5)` |  |
| 3 | ServicioAccesos | FachadaPersistencia«Singleton» | `buscar(TokensRecuperacion Vigentes(u.id, ahora))` |  |
| 4 | ServicioAccesos | FachadaPersistencia«Singleton» | `put(v) tras v.marcarUsado()` |  |
| 5 | ServicioAccesos | ServicioAccesos | `token = valor aleatorio; hash = SHA-256(token) (paso 6)` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 6 | ServicioAccesos | TokenRecuperacion | `«create» (u, hash, expiraEn = ahora + 30 min, usado = false)` | [TokenRecuperacion.java](../src/main/java/ar/edu/ub/club/accesos/dominio/TokenRecuperacion.java) |
| 7 | ServicioAccesos | FachadaPersistencia«Singleton» | `put(t)` |  |
| 8 | ServicioAccesos | ServicioCorreo | `enviar(MensajeCorreo( u.email, enlace P-03 con el token)) (paso 7)` | [ServicioCorreo.java](../src/main/java/ar/edu/ub/club/notificaciones/integracion/ServicioCorreo.java) |
| 9 | ServicioAccesos | ServicioAccesos | `registra la falla en la bitácora técnica, sin informarla al usuario` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 10 | ServicioAccesos | ServicioAccesos | `no genera token ni envía mensajes` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |

### `sec_cu13_p3.puml` — CU-13 Usuario recupera contraseña (parte 3: restablecimiento, de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Usuario | VistaRestablecerClave | `abre el enlace restablecer-clave?token=… (paso 10)` | [VistaRestablecerClave.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaRestablecerClave.java) |
| 2 | VistaRestablecerClave | PresentadorRestablecerClave | `alEntrar(token)` | [PresentadorRestablecerClave.java](../src/main/java/ar/edu/ub/club/accesos/ui/PresentadorRestablecerClave.java) |
| 3 | Usuario | VistaRestablecerClave | `ingresa la clave dos veces y elige Guardar (paso 13)` | [VistaRestablecerClave.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaRestablecerClave.java) |
| 4 | VistaRestablecerClave | PresentadorRestablecerClave | `alRestablecer(nuevaClave, repeticion)` | [PresentadorRestablecerClave.java](../src/main/java/ar/edu/ub/club/accesos/ui/PresentadorRestablecerClave.java) |
| 5 | PresentadorRestablecerClave | VistaRestablecerClave | `marcarCampo("repeticion", "las claves no coinciden")` | [VistaRestablecerClave.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaRestablecerClave.java) |
| 6 | PresentadorRestablecerClave | ServicioAccesos | `restablecerClave( token, nuevaClave) (dentro de ejecutar)` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 7 | PresentadorRestablecerClave | VistaRestablecerClave | `mostrarMensaje(...) y navegarA("inicio-sesion")` | [VistaRestablecerClave.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaRestablecerClave.java) |
| 8 | PresentadorRestablecerClave | VistaRestablecerClave | `mostrarError("enlace no válido")` | [VistaRestablecerClave.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaRestablecerClave.java) |
| 9 | PresentadorRestablecerClave | VistaRestablecerClave | `marcarCampo(campo, requisito)` | [VistaRestablecerClave.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaRestablecerClave.java) |

### `sec_cu13_p4.puml` — CU-13 Usuario recupera contraseña (parte 4: ServicioAccesos.restablecerClave)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioAccesos | FachadaPersistencia«Singleton» | `buscarUno(TokenRecuperacion PorHash(SHA-256(token)))` |  |
| 2 | ServicioAccesos | TokenRecuperacion | `esValido(ahora) (paso 11)` | [TokenRecuperacion.java](../src/main/java/ar/edu/ub/club/accesos/dominio/TokenRecuperacion.java) |
| 3 | ServicioAccesos | PoliticaContrasenas | `validar(nuevaClave, u.username) (paso 14)` | [PoliticaContrasenas.java](../src/main/java/ar/edu/ub/club/accesos/dominio/PoliticaContrasenas.java) |
| 4 | ServicioAccesos | CodificadorClave | `codificar(nuevaClave)` | [CodificadorClave.java](../src/main/java/ar/edu/ub/club/accesos/dominio/CodificadorClave.java) |
| 5 | ServicioAccesos | Usuario | `cambiarClave(hash) (paso 15)` | [Usuario.java](../src/main/java/ar/edu/ub/club/accesos/dominio/Usuario.java) |
| 6 | ServicioAccesos | TokenRecuperacion | `marcarUsado()` | [TokenRecuperacion.java](../src/main/java/ar/edu/ub/club/accesos/dominio/TokenRecuperacion.java) |
| 7 | ServicioAccesos | Usuario | `reiniciarIntentos()` | [Usuario.java](../src/main/java/ar/edu/ub/club/accesos/dominio/Usuario.java) |
| 8 | ServicioAccesos | FachadaPersistencia«Singleton» | `put(u)` |  |
| 9 | ServicioAccesos | FachadaPersistencia«Singleton» | `put(t)` |  |
| 10 | ServicioAccesos | GestorSesiones | `revocarTokensDe(u) (paso 16)` | [GestorSesiones.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/GestorSesiones.java) |
| 11 | ServicioAccesos | GestorSesiones | `invalidarSesionesDe(u)` | [GestorSesiones.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/GestorSesiones.java) |

## CU-14 Administrador crea usuario administrador

<!-- NOTAS:CU-14 -->

**Implementación (accesos, auditoría y configuración) — CU-14 Crear administrador — `sec_cu14_p1.puml`, `sec_cu14_p2.puml` (+ `sec_comun_autorizar.puml`)**

`VistaCuentasAdministrador` (botón Nueva) → `PresentadorCuentasAdministrador.alNueva` → `navegarA("cuentas/formulario")` → `VistaFormularioCuenta.alEntrar` → `PresentadorFormularioCuenta.cargar(null)` → `pantalla.mostrarAlta(new DatosAdministrador())` → `FormularioEntidad.alEnviar` → `PresentadorFormularioCuenta.alCrear(datos)` → `ServicioAccesos.crearAdministrador` → `UsuarioActualSesion.obtener` + `ControlAcceso.verificar(GESTIONAR_CUENTAS)` → `PoliticaContrasenas.validar` → `contar(new UsuarioPorUsername)` / `contar(new UsuarioPorEmail)` → `CodificadorClaveBCrypt.codificar` → `new Administrador(...)` → `FachadaPersistencia.put` → `BrokerAdministrador.insertar` (`BrokerUsuario.valoresInsercion` → `usuario`; `guardarPartes` → `administrador`) → `ServicioAuditoria.registrar("ALTA")` → `BrokerRegistroAuditoria.insertar` (`registro_auditoria`) → `navegarA("cuentas")`.

**Diagramas de secuencia del diseño:**

### `sec_cu14_p1.puml` — CU-14 Administrador crea usuario administrador (parte 1: de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaCuentasAdministrador | `«Nueva cuenta de administrador» (paso 1)` | [VistaCuentasAdministrador.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaCuentasAdministrador.java) |
| 2 | VistaCuentasAdministrador | PresentadorCuentasAdministrador | `alNueva()` | [PresentadorCuentasAdministrador.java](../src/main/java/ar/edu/ub/club/accesos/ui/PresentadorCuentasAdministrador.java) |
| 3 | PresentadorCuentasAdministrador | VistaCuentasAdministrador | `navegarA("cuentas/formulario")` | [VistaCuentasAdministrador.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaCuentasAdministrador.java) |
| 4 | VistaCuentasAdministrador | VistaFormularioCuenta | `navega a P-14` | [VistaFormularioCuenta.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaFormularioCuenta.java) |
| 5 | VistaFormularioCuenta | PresentadorFormularioCuenta | `cargar(null)` | [PresentadorFormularioCuenta.java](../src/main/java/ar/edu/ub/club/accesos/ui/PresentadorFormularioCuenta.java) |
| 6 | PresentadorFormularioCuenta | VistaFormularioCuenta | `mostrarAlta( new DatosAdministrador())` | [VistaFormularioCuenta.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaFormularioCuenta.java) |
| 7 | Administrador | VistaFormularioCuenta | `elige Guardar (paso 3)` | [VistaFormularioCuenta.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaFormularioCuenta.java) |
| 8 | VistaFormularioCuenta | PresentadorFormularioCuenta | `alCrear(datos: DatosAdministrador)` | [PresentadorFormularioCuenta.java](../src/main/java/ar/edu/ub/club/accesos/ui/PresentadorFormularioCuenta.java) |
| 9 | PresentadorFormularioCuenta | ServicioAccesos | `crearAdministrador( datos) (dentro de ejecutar)` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 10 | PresentadorFormularioCuenta | VistaFormularioCuenta | `mostrarMensaje(...) y navegarA("cuentas")` | [VistaFormularioCuenta.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaFormularioCuenta.java) |
| 11 | VistaFormularioCuenta | VistaCuentasAdministrador | `navega a P-13` | [VistaCuentasAdministrador.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaCuentasAdministrador.java) |
| 12 | VistaCuentasAdministrador | PresentadorCuentasAdministrador | `cargar()` | [PresentadorCuentasAdministrador.java](../src/main/java/ar/edu/ub/club/accesos/ui/PresentadorCuentasAdministrador.java) |
| 13 | PresentadorCuentasAdministrador | ServicioAccesos | `listarAdministradores()` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 14 | PresentadorCuentasAdministrador | VistaCuentasAdministrador | `mostrarCuentas(FilaCuenta .desde(admin, reloj.ahora()))` | [VistaCuentasAdministrador.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaCuentasAdministrador.java) |
| 15 | PresentadorFormularioCuenta | VistaFormularioCuenta | `marcarCampo(campo, mensaje)` | [VistaFormularioCuenta.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaFormularioCuenta.java) |
| 16 | Administrador | VistaFormularioCuenta | `elige Cancelar` | [VistaFormularioCuenta.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaFormularioCuenta.java) |

### `sec_cu14_p2.puml` — CU-14 Administrador crea usuario administrador (parte 2: ServicioAccesos.crearAdministrador)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorFormularioCuenta | ServicioAccesos | `crearAdministrador(datos)` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 2 | ServicioAccesos | PoliticaContrasenas | `validar(datos.clave, datos.username) (paso 5)` | [PoliticaContrasenas.java](../src/main/java/ar/edu/ub/club/accesos/dominio/PoliticaContrasenas.java) |
| 3 | ServicioAccesos | FachadaPersistencia«Singleton» | `contar(UsuarioPorUsername( datos.username))` |  |
| 4 | ServicioAccesos | FachadaPersistencia«Singleton» | `contar(UsuarioPorEmail( datos.email))` |  |
| 5 | ServicioAccesos | CodificadorClave | `codificar(datos.clave)` | [CodificadorClave.java](../src/main/java/ar/edu/ub/club/accesos/dominio/CodificadorClave.java) |
| 6 | ServicioAccesos | Administrador | `«create» Administrador( nombre, apellido, username, email, hash)` | [Administrador.java](../src/main/java/ar/edu/ub/club/accesos/dominio/Administrador.java) |
| 7 | ServicioAccesos | FachadaPersistencia«Singleton» | `put(a) (paso 7)` |  |
| 8 | ServicioAccesos | ServicioAuditoria | `registrar(usuario, "ALTA", "Administrador", a.id, detalle sin clave) (paso 8)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

## CU-15 Administrador modifica datos de acceso de usuario

<!-- NOTAS:CU-15 -->

**Implementación (accesos, auditoría y configuración) — CU-15 Modificar datos de acceso — `sec_cu15_p1.puml` … `sec_cu15_p3.puml`**

`VistaCuentasAdministrador` (Buscar) → `PresentadorCuentasAdministrador.alBuscar(texto)` → `ServicioAccesos.buscarCuentas(new CriterioBusquedaCuenta)` → `buscar(new CuentasPorCriterio)` (BrokerAdministrador + BrokerSocio, `comparador`) → `ResumenCuenta.desde` → `FilaCuenta.desde(resumen)` → `mostrarCuentas` (p1); acción «Modificar acceso» → `alModificarAcceso(id)` → `VistaFormularioCuenta.alEntrar` → `PresentadorFormularioCuenta.cargar(id)` → `ServicioAccesos.consultarCuenta` → `get(id, Usuario)` → `DatosAcceso.desde` + `DetalleCuenta.desde` → `mostrarModificacion` (p1); Guardar → `alModificar(datos)` (p2) → `ServicioAccesos.modificarDatosAcceso` → `verificar` → `get(id, Usuario)` → `estaActiva` → `PoliticaContrasenas.validar` → `contar(UsuarioPorUsername/UsuarioPorEmail)` (solo si cambió) → `Usuario.actualizarDatosAcceso` → `CodificadorClaveBCrypt.codificar` + `Usuario.cambiarClave` → `put` (UPDATE `usuario` + hija) → `GestorSesiones.revocarTokensDe` → `invalidarSesionesDe` (si cambió la clave) → `ServicioAuditoria.registrar("MODIFICACION","Usuario")` (p3).

**Diagramas de secuencia del diseño:**

### `sec_cu15_p1.puml` — CU-15 Administrador modifica datos de acceso (parte 1: búsqueda y carga de la cuenta)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | VistaCuentasAdministrador | PresentadorCuentasAdministrador | `cargar() (paso 1)` | [PresentadorCuentasAdministrador.java](../src/main/java/ar/edu/ub/club/accesos/ui/PresentadorCuentasAdministrador.java) |
| 2 | PresentadorCuentasAdministrador | ServicioAccesos | `listarAdministradores()` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 3 | ServicioAccesos | FachadaPersistencia«Singleton» | `buscar(Administradores())` |  |
| 4 | PresentadorCuentasAdministrador | VistaCuentasAdministrador | `mostrarCuentas(FilaCuenta .desde(admin, reloj.ahora()))` | [VistaCuentasAdministrador.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaCuentasAdministrador.java) |
| 5 | Administrador | VistaCuentasAdministrador | `elige Modificar acceso` | [VistaCuentasAdministrador.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaCuentasAdministrador.java) |
| 6 | VistaCuentasAdministrador | PresentadorCuentasAdministrador | `alModificarAcceso(idUsuario)` | [PresentadorCuentasAdministrador.java](../src/main/java/ar/edu/ub/club/accesos/ui/PresentadorCuentasAdministrador.java) |
| 7 | PresentadorCuentasAdministrador | VistaCuentasAdministrador | `navegarA("cuentas/ formulario/" + idUsuario)` | [VistaCuentasAdministrador.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaCuentasAdministrador.java) |
| 8 | VistaCuentasAdministrador | VistaFormularioCuenta | `navega a P-14 (paso 3)` | [VistaFormularioCuenta.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaFormularioCuenta.java) |
| 9 | VistaFormularioCuenta | PresentadorFormularioCuenta | `cargar(idUsuario)` | [PresentadorFormularioCuenta.java](../src/main/java/ar/edu/ub/club/accesos/ui/PresentadorFormularioCuenta.java) |
| 10 | PresentadorFormularioCuenta | ServicioAccesos | `consultarCuenta(idUsuario)` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 11 | ServicioAccesos | FachadaPersistencia«Singleton» | `get(idUsuario, Usuario)` |  |
| 12 | PresentadorFormularioCuenta | VistaFormularioCuenta | `mostrarModificacion(DatosAcceso .desde(u), DetalleCuenta.desde(u))` | [VistaFormularioCuenta.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaFormularioCuenta.java) |

### `sec_cu15_p2.puml` — CU-15 Administrador modifica datos de acceso (parte 2: guardar, de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaFormularioCuenta | `elige Guardar (paso 6)` | [VistaFormularioCuenta.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaFormularioCuenta.java) |
| 2 | VistaFormularioCuenta | PresentadorFormularioCuenta | `alModificar(datos: DatosAcceso)` | [PresentadorFormularioCuenta.java](../src/main/java/ar/edu/ub/club/accesos/ui/PresentadorFormularioCuenta.java) |
| 3 | PresentadorFormularioCuenta | ServicioAccesos | `modificarDatosAcceso( idUsuario, datos) (dentro de ejecutar)` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 4 | PresentadorFormularioCuenta | VistaFormularioCuenta | `mostrarMensaje(...) y navegarA("cuentas") o a la ficha` | [VistaFormularioCuenta.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaFormularioCuenta.java) |
| 5 | PresentadorFormularioCuenta | VistaFormularioCuenta | `marcarCampo(campo, mensaje) o mostrarError(mensaje)` | [VistaFormularioCuenta.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaFormularioCuenta.java) |
| 6 | Administrador | VistaFormularioCuenta | `elige Cancelar` | [VistaFormularioCuenta.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaFormularioCuenta.java) |

### `sec_cu15_p3.puml` — CU-15 Administrador modifica datos de acceso (parte 3: ServicioAccesos.modificarDatosAcceso)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorFormularioCuenta | ServicioAccesos | `modificarDatosAcceso( idUsuario, datos)` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 2 | ServicioAccesos | FachadaPersistencia«Singleton» | `get(idUsuario, Usuario)` |  |
| 3 | ServicioAccesos | Usuario | `estaActiva()` | [Usuario.java](../src/main/java/ar/edu/ub/club/accesos/dominio/Usuario.java) |
| 4 | ServicioAccesos | PoliticaContrasenas | `validar(nuevaClave, username)` | [PoliticaContrasenas.java](../src/main/java/ar/edu/ub/club/accesos/dominio/PoliticaContrasenas.java) |
| 5 | ServicioAccesos | FachadaPersistencia«Singleton» | `contar(UsuarioPorUsername( username)) / contar( UsuarioPorEmail(email))` |  |
| 6 | ServicioAccesos | Usuario | `actualizarDatosAcceso( username, email)` | [Usuario.java](../src/main/java/ar/edu/ub/club/accesos/dominio/Usuario.java) |
| 7 | ServicioAccesos | CodificadorClave | `codificar(nuevaClave)` | [CodificadorClave.java](../src/main/java/ar/edu/ub/club/accesos/dominio/CodificadorClave.java) |
| 8 | ServicioAccesos | Usuario | `cambiarClave(hash)` | [Usuario.java](../src/main/java/ar/edu/ub/club/accesos/dominio/Usuario.java) |
| 9 | ServicioAccesos | FachadaPersistencia«Singleton» | `put(u)` |  |
| 10 | ServicioAccesos | GestorSesiones | `revocarTokensDe(u) (paso 10)` | [GestorSesiones.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/GestorSesiones.java) |
| 11 | ServicioAccesos | GestorSesiones | `invalidarSesionesDe(u)` | [GestorSesiones.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/GestorSesiones.java) |
| 12 | ServicioAccesos | ServicioAuditoria | `registrar(usuario, "MODIFICACION", "Usuario", u.id, detalle sin clave) (paso 11)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

## CU-16 Administrador asigna rol a usuario

<!-- NOTAS:CU-16 -->

**Implementación (accesos, auditoría y configuración) — CU-16 Asignar tipo de cuenta — `sec_cu16.puml`, `pat_polimorfismo.puml`**

Incluido en CU-14: `ServicioAccesos.crearAdministrador` → `contar(UsuarioPorUsername/UsuarioPorEmail)` → `CodificadorClaveBCrypt.codificar` → `new Administrador(nombre, apellido, username, email, hash)` (constructor protegido `Usuario(username, email, hash)`: ACTIVA, 0 intentos) → `put`. El tipo se lee con `Usuario.tipoCuenta()` (`DetalleCuenta`, `FilaCuenta`, `ResumenCuenta`) y los permisos con `obtenerPermisos()`. Rama CU-01: `ServicioSocios.registrarSocio` → `new Socio(...)` (módulo socios).

**Diagramas de secuencia del diseño:**

### `sec_cu16.puml` — CU-16 Administrador asigna rol a usuario (incluido en CU-01 y CU-14)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioSocios | FachadaPersistencia«Singleton» | `contar(UsuarioPorUsername(username)) / contar(UsuarioPorEmail(email)) (paso 3)` |  |
| 2 | ServicioSocios | ServicioSocios | `lanza ErrorNegocio al caso base` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 3 | ServicioSocios | CodificadorClave | `codificar(clave) (paso 4)` | [CodificadorClave.java](../src/main/java/ar/edu/ub/club/accesos/dominio/CodificadorClave.java) |
| 4 | ServicioSocios | Socio | `«create» Socio(datos, categoria, hash) (paso 5)` | [Socio.java](../src/main/java/ar/edu/ub/club/socios/dominio/Socio.java) |
| 5 | ServicioAccesos | FachadaPersistencia«Singleton» | `contar(UsuarioPorUsername(username)) / contar(UsuarioPorEmail(email)) (paso 3)` |  |
| 6 | ServicioAccesos | ServicioAccesos | `lanza ErrorNegocio al caso base` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 7 | ServicioAccesos | CodificadorClave | `codificar(clave) (paso 4)` | [CodificadorClave.java](../src/main/java/ar/edu/ub/club/accesos/dominio/CodificadorClave.java) |
| 8 | ServicioAccesos | Administrador | `«create» Administrador(nombre, apellido, username, email, hash) (paso 5)` | [Administrador.java](../src/main/java/ar/edu/ub/club/accesos/dominio/Administrador.java) |

## CU-17 Administrador desactiva cuenta de usuario

<!-- NOTAS:CU-17 -->

**Implementación (accesos, auditoría y configuración) — CU-17 Desactivar cuenta — `sec_cu17_p1.puml`, `sec_cu17_p2.puml`**

`VistaCuentasAdministrador` (acción Desactivar → `DialogoConfirmacion`) → `PresentadorCuentasAdministrador.alDesactivar(id)` → `ServicioAccesos.desactivarCuenta` → `verificar(GESTIONAR_CUENTAS)` → `get(id, Administrador)` (`BrokerAdministrador.materializar`) → `estaActiva` → `contar(new AdministradoresActivos())` (`FOR UPDATE OF usuario`) → `Administrador.desactivar` → `put` (UPDATE `usuario`) → `GestorSesiones.revocarTokensDe` → `invalidarSesionesDe` → `ServicioAuditoria.registrar("BAJA","Administrador")` → `PresentadorCuentasAdministrador.cargar` → `listarAdministradores` → `buscar(new Administradores())` → `FilaCuenta.desde(admin, ahora)`.

**Diagramas de secuencia del diseño:**

### `sec_cu17_p1.puml` — CU-17 Administrador desactiva cuenta de usuario (parte 1: de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaCuentasAdministrador | `«Desactivar» (paso 1)` | [VistaCuentasAdministrador.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaCuentasAdministrador.java) |
| 2 | VistaCuentasAdministrador | DialogoConfirmacion | `«create» y abrir() (paso 5)` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 3 | Administrador | DialogoConfirmacion | `elige Confirmar` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 4 | DialogoConfirmacion | VistaCuentasAdministrador | `oyente de alConfirmar` | [VistaCuentasAdministrador.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaCuentasAdministrador.java) |
| 5 | VistaCuentasAdministrador | PresentadorCuentasAdministrador | `alDesactivar(idUsuario)` | [PresentadorCuentasAdministrador.java](../src/main/java/ar/edu/ub/club/accesos/ui/PresentadorCuentasAdministrador.java) |
| 6 | PresentadorCuentasAdministrador | ServicioAccesos | `desactivarCuenta(idUsuario) (dentro de ejecutar)` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 7 | PresentadorCuentasAdministrador | PresentadorCuentasAdministrador | `cargar()` | [PresentadorCuentasAdministrador.java](../src/main/java/ar/edu/ub/club/accesos/ui/PresentadorCuentasAdministrador.java) |
| 8 | PresentadorCuentasAdministrador | VistaCuentasAdministrador | `mostrarCuentas(filas) y mostrarMensaje(...)` | [VistaCuentasAdministrador.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaCuentasAdministrador.java) |
| 9 | PresentadorCuentasAdministrador | VistaCuentasAdministrador | `mostrarError(mensaje)` | [VistaCuentasAdministrador.java](../src/main/java/ar/edu/ub/club/accesos/ui/VistaCuentasAdministrador.java) |
| 10 | Administrador | DialogoConfirmacion | `elige Cancelar` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |

### `sec_cu17_p2.puml` — CU-17 Administrador desactiva cuenta de usuario (parte 2: ServicioAccesos.desactivarCuenta)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorCuentasAdministrador | ServicioAccesos | `desactivarCuenta( idUsuario)` | [ServicioAccesos.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/ServicioAccesos.java) |
| 2 | ServicioAccesos | FachadaPersistencia«Singleton» | `get(idUsuario, Administrador) (pasos 2-3)` |  |
| 3 | ServicioAccesos | Administrador | `estaActiva()` | [Administrador.java](../src/main/java/ar/edu/ub/club/accesos/dominio/Administrador.java) |
| 4 | ServicioAccesos | FachadaPersistencia«Singleton» | `contar(Administradores Activos()) con bloqueo (paso 4)` |  |
| 5 | ServicioAccesos | Administrador | `desactivar() (paso 7)` | [Administrador.java](../src/main/java/ar/edu/ub/club/accesos/dominio/Administrador.java) |
| 6 | ServicioAccesos | FachadaPersistencia«Singleton» | `put(a)` |  |
| 7 | ServicioAccesos | GestorSesiones | `revocarTokensDe(a)` | [GestorSesiones.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/GestorSesiones.java) |
| 8 | ServicioAccesos | GestorSesiones | `invalidarSesionesDe(a) (paso 8)` | [GestorSesiones.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/GestorSesiones.java) |
| 9 | ServicioAccesos | ServicioAuditoria | `registrar(usuario, "BAJA", "Administrador", a.id, detalle) (paso 9)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

## CU-18 Administrador registra deporte

<!-- NOTAS:CU-18 -->

### `sec_cu18.puml` — CU-18 Administrador registra deporte

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaDeportes | `Nuevo deporte (paso 1)` | [VistaDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaDeportes.java) |
| 2 | VistaDeportes | PresentadorDeportes | `alEditar(null)` | [PresentadorDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorDeportes.java) |
| 3 | PresentadorDeportes | VistaDeportes | `editar(new DatosDeporte(), null)` | [VistaDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaDeportes.java) |
| 4 | Administrador | VistaDeportes | `confirma (paso 3)` | [VistaDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaDeportes.java) |
| 5 | VistaDeportes | PresentadorDeportes | `alGuardar(null, datos)` | [PresentadorDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorDeportes.java) |
| 6 | PresentadorDeportes | ServicioRecursos | `registrarDeporte( datos)` | [ServicioRecursos.java](../src/main/java/ar/edu/ub/club/recursos/aplicacion/ServicioRecursos.java) |
| 7 | ServicioRecursos | FachadaPersistencia«Singleton» | `contar(DeportePorNombre( nombre, null)) (paso 5)` |  |
| 8 | ServicioRecursos | Deporte | `«create» (nombre, descripcion) activo (paso 6)` | [Deporte.java](../src/main/java/ar/edu/ub/club/recursos/dominio/Deporte.java) |
| 9 | ServicioRecursos | FachadaPersistencia«Singleton» | `put(d)` |  |
| 10 | ServicioRecursos | ServicioAuditoria | `registrar(usuario, ALTA, "Deporte", d.id, detalle) (paso 7)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |
| 11 | PresentadorDeportes | PresentadorDeportes | `cargar()` | [PresentadorDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorDeportes.java) |
| 12 | PresentadorDeportes | ServicioRecursos | `listarDeportes()` | [ServicioRecursos.java](../src/main/java/ar/edu/ub/club/recursos/aplicacion/ServicioRecursos.java) |
| 13 | PresentadorDeportes | VistaDeportes | `mostrarDeportes(filas) y mostrarMensaje("…")` | [VistaDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaDeportes.java) |
| 14 | PresentadorDeportes | VistaDeportes | `marcarCampo("nombre", mensaje)` | [VistaDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaDeportes.java) |

## CU-19 Administrador modifica deporte

<!-- NOTAS:CU-19 -->

### `sec_cu19.puml` — CU-19 Administrador modifica deporte

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaDeportes | `Modificar (paso 1)` | [VistaDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaDeportes.java) |
| 2 | VistaDeportes | PresentadorDeportes | `alEditar(fila)` | [PresentadorDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorDeportes.java) |
| 3 | PresentadorDeportes | VistaDeportes | `editar(new DatosDeporte( fila.nombre(), fila.descripcion(), fila.activo()), fila.id())` | [VistaDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaDeportes.java) |
| 4 | Administrador | VistaDeportes | `Cancelar` | [VistaDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaDeportes.java) |
| 5 | Administrador | VistaDeportes | `confirma` | [VistaDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaDeportes.java) |
| 6 | Administrador | VistaDeportes | `confirma` | [VistaDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaDeportes.java) |
| 7 | VistaDeportes | PresentadorDeportes | `alGuardar(idDeporte, datos)` | [PresentadorDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorDeportes.java) |
| 8 | PresentadorDeportes | ServicioRecursos | `modificarDeporte( id, datos)` | [ServicioRecursos.java](../src/main/java/ar/edu/ub/club/recursos/aplicacion/ServicioRecursos.java) |
| 9 | ServicioRecursos | FachadaPersistencia«Singleton» | `get(id, Deporte)` |  |
| 10 | ServicioRecursos | FachadaPersistencia«Singleton» | `contar(DeportePorNombre( nombre, id))` |  |
| 11 | ServicioRecursos | Deporte | `activar()` | [Deporte.java](../src/main/java/ar/edu/ub/club/recursos/dominio/Deporte.java) |
| 12 | ServicioRecursos | Deporte | `modificar(nombre, descripcion)` | [Deporte.java](../src/main/java/ar/edu/ub/club/recursos/dominio/Deporte.java) |
| 13 | PresentadorDeportes | VistaDeportes | `marcarCampo("nombre", mensaje)` | [VistaDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaDeportes.java) |
| 14 | ServicioRecursos | FachadaPersistencia«Singleton» | `put(d)` |  |
| 15 | ServicioRecursos | ServicioAuditoria | `registrar(usuario, MODIFICACION, "Deporte", id, detalle) (paso 7)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |
| 16 | PresentadorDeportes | PresentadorDeportes | `cargar()` | [PresentadorDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorDeportes.java) |
| 17 | PresentadorDeportes | VistaDeportes | `mostrarDeportes(filas)` | [VistaDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaDeportes.java) |

## CU-20 Administrador desactiva deporte

<!-- NOTAS:CU-20 -->

**Implementación (reservas y notificaciones) — Cancelación por bajas (sec_comun_cancelar_baja; CU-03, CU-20, CU-23)**

`ServicioSocios.desactivarSocio` / `ServicioRecursos.desactivarCancha/desactivarDeporte` → `ApplicationEventPublisher.publishEvent` →
`CanceladorReservasPorBaja.alDesactivarSocio/alDesactivarCancha/alDesactivarDeporte` → `buscar(new ReservasFuturasDeSocio/DeCancha/DeDeporte(id, ahora))`
→ `cancelarTodas` → `Reserva.cancelar(motivo, evento.usuario, ahora)` → `put` → `publicar(new ReservaCanceladaEvento)`.
Antes de confirmar la baja, socios/recursos/agenda usan `ConsultaReservasFuturasReservas.buscarFuturasDe*` / `buscarDeFecha` / `buscarFuturasDesde`
→ `ResumenesFuturosDe*` / `ResumenesDeFecha` / `ResumenesFuturosDesde` (proyección `ResumenReserva`).

**Diagramas de secuencia del diseño:**

### `sec_cu20_p1.puml` — CU-20 Administrador desactiva deporte (parte 1: aviso de reservas afectadas)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaDeportes | `Desactivar en un deporte (paso 1)` | [VistaDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaDeportes.java) |
| 2 | VistaDeportes | PresentadorDeportes | `alDesactivar(idDeporte)` | [PresentadorDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorDeportes.java) |
| 3 | PresentadorDeportes | ServicioRecursos | `desactivarDeporte( id, false)` | [ServicioRecursos.java](../src/main/java/ar/edu/ub/club/recursos/aplicacion/ServicioRecursos.java) |
| 4 | PresentadorDeportes | VistaDeportes | `mostrarError(mensaje)` | [VistaDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaDeportes.java) |
| 5 | PresentadorDeportes | PresentadorDeportes | `FilaResumenReserva. desde(r) por cada una` | [PresentadorDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorDeportes.java) |
| 6 | PresentadorDeportes | VistaDeportes | `pedirConfirmacionBaja( idDeporte, afectadas)` | [VistaDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaDeportes.java) |
| 7 | VistaDeportes | DialogoConfirmacion | `«create» con las reservas a cancelar (paso 4)` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 8 | VistaDeportes | DialogoConfirmacion | `mostrarAdvertencias( lineas); abrir()` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |

### `sec_cu20_p2.puml` — CU-20 Administrador desactiva deporte (parte 2: confirmación)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | DialogoConfirmacion | `Confirmar` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 2 | DialogoConfirmacion | VistaDeportes | `oyente de alConfirmar` | [VistaDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaDeportes.java) |
| 3 | VistaDeportes | PresentadorDeportes | `alConfirmarDesactivacion( idDeporte)` | [PresentadorDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorDeportes.java) |
| 4 | PresentadorDeportes | ServicioRecursos | `desactivarDeporte( id, true)` | [ServicioRecursos.java](../src/main/java/ar/edu/ub/club/recursos/aplicacion/ServicioRecursos.java) |
| 5 | PresentadorDeportes | PresentadorDeportes | `cargar()` | [PresentadorDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorDeportes.java) |
| 6 | PresentadorDeportes | VistaDeportes | `mostrarDeportes(filas); mostrarMensaje("…")` | [VistaDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaDeportes.java) |
| 7 | PresentadorDeportes | VistaDeportes | `mostrarError(mensaje) (genérico con código de error, RNF-16)` | [VistaDeportes.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaDeportes.java) |
| 8 | Administrador | DialogoConfirmacion | `Cancelar` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |

### `sec_cu20_p3.puml` — CU-20 Administrador desactiva deporte (parte 3: ServicioRecursos.desactivarDeporte)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorDeportes | ServicioRecursos | `desactivarDeporte( id, confirmar)` | [ServicioRecursos.java](../src/main/java/ar/edu/ub/club/recursos/aplicacion/ServicioRecursos.java) |
| 2 | ServicioRecursos | FachadaPersistencia«Singleton» | `get(id, Deporte)` |  |
| 3 | ServicioRecursos | Deporte | `estaActivo() (paso 2)` | [Deporte.java](../src/main/java/ar/edu/ub/club/recursos/dominio/Deporte.java) |
| 4 | ServicioRecursos | ConsultaReservasFuturas | `buscarFuturasDe Deporte(id) (paso 3)` | [ConsultaReservasFuturas.java](../src/main/java/ar/edu/ub/club/compartido/dominio/ConsultaReservasFuturas.java) |
| 5 | ServicioRecursos | Deporte | `desactivar() (paso 6)` | [Deporte.java](../src/main/java/ar/edu/ub/club/recursos/dominio/Deporte.java) |
| 6 | ServicioRecursos | FachadaPersistencia«Singleton» | `put(d)` |  |
| 7 | ServicioRecursos | ApplicationEventPublisher | `publishEvent(Deporte DesactivadoEvento(id, admin, ahora))` |  |
| 8 | ServicioRecursos | ServicioAuditoria | `registrar(admin, BAJA, "Deporte", id, detalle) (paso 9)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

## CU-21 Administrador registra cancha

<!-- NOTAS:CU-21 -->

### `sec_cu21_p1.puml` — CU-21 Administrador registra cancha (parte 1: de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaCanchas | `Nueva cancha (paso 1)` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |
| 2 | VistaCanchas | PresentadorCanchas | `alEditar(null)` | [PresentadorCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorCanchas.java) |
| 3 | PresentadorCanchas | VistaCanchas | `mostrarMensaje("primero debe registrarse o reactivarse un deporte")` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |
| 4 | PresentadorCanchas | VistaCanchas | `editar(new DatosCancha(), null)` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |
| 5 | Administrador | VistaCanchas | `confirma (paso 3)` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |
| 6 | VistaCanchas | PresentadorCanchas | `alGuardar(null, datos)` | [PresentadorCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorCanchas.java) |
| 7 | PresentadorCanchas | ServicioRecursos | `registrarCancha( datos)` | [ServicioRecursos.java](../src/main/java/ar/edu/ub/club/recursos/aplicacion/ServicioRecursos.java) |
| 8 | PresentadorCanchas | PresentadorCanchas | `cargar(null)` | [PresentadorCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorCanchas.java) |
| 9 | PresentadorCanchas | VistaCanchas | `mostrarCanchas(filas); mostrarMensaje("…")` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |
| 10 | PresentadorCanchas | VistaCanchas | `marcarCampo(campo, motivo)` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |
| 11 | PresentadorCanchas | ServicioRecursos | `listarDeportes() (solo FA5)` | [ServicioRecursos.java](../src/main/java/ar/edu/ub/club/recursos/aplicacion/ServicioRecursos.java) |
| 12 | PresentadorCanchas | VistaCanchas | `mostrarDeportes(opciones)` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |

### `sec_cu21_p2.puml` — CU-21 Administrador registra cancha (parte 2: ServicioRecursos.registrarCancha)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorCanchas | ServicioRecursos | `registrarCancha( datos)` | [ServicioRecursos.java](../src/main/java/ar/edu/ub/club/recursos/aplicacion/ServicioRecursos.java) |
| 2 | ServicioRecursos | FachadaPersistencia«Singleton» | `contar(CanchaPorNombre( nombre, null)) (paso 5)` |  |
| 3 | ServicioRecursos | FachadaPersistencia«Singleton» | `get(idDeporte, Deporte)` |  |
| 4 | ServicioRecursos | Deporte | `estaActivo() (paso 6)` | [Deporte.java](../src/main/java/ar/edu/ub/club/recursos/dominio/Deporte.java) |
| 5 | ServicioRecursos | Cancha | `«create» (nombre, d, ubicacion, caracteristicas) activa (paso 7)` | [Cancha.java](../src/main/java/ar/edu/ub/club/recursos/dominio/Cancha.java) |
| 6 | ServicioRecursos | FachadaPersistencia«Singleton» | `put(c)` |  |
| 7 | ServicioRecursos | ServicioAuditoria | `registrar(usuario, ALTA, "Cancha", c.id, detalle) (paso 8)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

## CU-22 Administrador modifica cancha

<!-- NOTAS:CU-22 -->

### `sec_cu22_p1.puml` — CU-22 Administrador modifica cancha (parte 1: de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaCanchas | `Modificar (paso 1)` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |
| 2 | VistaCanchas | PresentadorCanchas | `alEditar(fila)` | [PresentadorCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorCanchas.java) |
| 3 | PresentadorCanchas | VistaCanchas | `editar(new DatosCancha(fila.nombre(), fila.idDeporte(), fila.ubicacion(), fila.caracteristicas(), fila.activa(), false), fila.id())` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |
| 4 | Administrador | VistaCanchas | `confirma (paso 3 o Activar, FA1)` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |
| 5 | VistaCanchas | PresentadorCanchas | `alGuardar(idCancha, datos)` | [PresentadorCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorCanchas.java) |
| 6 | PresentadorCanchas | ServicioRecursos | `modificarCancha( id, datos)` | [ServicioRecursos.java](../src/main/java/ar/edu/ub/club/recursos/aplicacion/ServicioRecursos.java) |
| 7 | PresentadorCanchas | PresentadorCanchas | `cargar(null)` | [PresentadorCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorCanchas.java) |
| 8 | PresentadorCanchas | VistaCanchas | `mostrarCanchas(filas)` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |
| 9 | PresentadorCanchas | VistaCanchas | `marcarCampo(campo, motivo)` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |
| 10 | PresentadorCanchas | PresentadorCanchas | `alRequerirConfirmacion(ex)` | [PresentadorCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorCanchas.java) |
| 11 | PresentadorCanchas | VistaCanchas | `pedirConfirmacionCambio( afectadas)` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |
| 12 | VistaCanchas | DialogoConfirmacion | `«create» con las reservas, que se conservan` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 13 | Administrador | DialogoConfirmacion | `Confirmar (7.2)` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 14 | DialogoConfirmacion | VistaCanchas | `oyente de alConfirmar` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |
| 15 | VistaCanchas | PresentadorCanchas | `reenvía alGuardar con datos.confirmar = true` | [PresentadorCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorCanchas.java) |

### `sec_cu22_p2.puml` — CU-22 Administrador modifica cancha (parte 2: ServicioRecursos.modificarCancha)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorCanchas | ServicioRecursos | `modificarCancha( id, datos)` | [ServicioRecursos.java](../src/main/java/ar/edu/ub/club/recursos/aplicacion/ServicioRecursos.java) |
| 2 | ServicioRecursos | FachadaPersistencia«Singleton» | `get(id, Cancha)` |  |
| 3 | ServicioRecursos | Cancha | `activar()` | [Cancha.java](../src/main/java/ar/edu/ub/club/recursos/dominio/Cancha.java) |
| 4 | ServicioRecursos | FachadaPersistencia«Singleton» | `contar(CanchaPorNombre( nombre, id))` |  |
| 5 | ServicioRecursos | FachadaPersistencia«Singleton» | `get(idDeporte, Deporte)` |  |
| 6 | ServicioRecursos | Deporte | `estaActivo() (paso 6)` | [Deporte.java](../src/main/java/ar/edu/ub/club/recursos/dominio/Deporte.java) |
| 7 | ServicioRecursos | ConsultaReservasFuturas | `buscarFuturasDe Cancha(id) (paso 7)` | [ConsultaReservasFuturas.java](../src/main/java/ar/edu/ub/club/compartido/dominio/ConsultaReservasFuturas.java) |
| 8 | ServicioRecursos | Cancha | `modificar(nombre, d, ubicacion, caracteristicas)` | [Cancha.java](../src/main/java/ar/edu/ub/club/recursos/dominio/Cancha.java) |
| 9 | ServicioRecursos | FachadaPersistencia«Singleton» | `put(c) (paso 8)` |  |
| 10 | ServicioRecursos | ServicioAuditoria | `registrar(usuario, MODIFICACION, "Cancha", id, detalle) (paso 9)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

## CU-23 Administrador desactiva cancha

<!-- NOTAS:CU-23 -->

**Implementación (reservas y notificaciones) — Cancelación por bajas (sec_comun_cancelar_baja; CU-03, CU-20, CU-23)**

`ServicioSocios.desactivarSocio` / `ServicioRecursos.desactivarCancha/desactivarDeporte` → `ApplicationEventPublisher.publishEvent` →
`CanceladorReservasPorBaja.alDesactivarSocio/alDesactivarCancha/alDesactivarDeporte` → `buscar(new ReservasFuturasDeSocio/DeCancha/DeDeporte(id, ahora))`
→ `cancelarTodas` → `Reserva.cancelar(motivo, evento.usuario, ahora)` → `put` → `publicar(new ReservaCanceladaEvento)`.
Antes de confirmar la baja, socios/recursos/agenda usan `ConsultaReservasFuturasReservas.buscarFuturasDe*` / `buscarDeFecha` / `buscarFuturasDesde`
→ `ResumenesFuturosDe*` / `ResumenesDeFecha` / `ResumenesFuturosDesde` (proyección `ResumenReserva`).

**Diagramas de secuencia del diseño:**

### `sec_cu23_p1.puml` — CU-23 Administrador desactiva cancha (parte 1: aviso de reservas afectadas)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaCanchas | `Desactivar en una cancha (paso 1)` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |
| 2 | VistaCanchas | PresentadorCanchas | `alDesactivar(idCancha)` | [PresentadorCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorCanchas.java) |
| 3 | PresentadorCanchas | ServicioRecursos | `desactivarCancha( id, false)` | [ServicioRecursos.java](../src/main/java/ar/edu/ub/club/recursos/aplicacion/ServicioRecursos.java) |
| 4 | PresentadorCanchas | VistaCanchas | `mostrarError(mensaje)` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |
| 5 | PresentadorCanchas | PresentadorCanchas | `FilaResumenReserva. desde(r) por cada una` | [PresentadorCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorCanchas.java) |
| 6 | PresentadorCanchas | VistaCanchas | `pedirConfirmacionBaja( idCancha, afectadas)` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |
| 7 | VistaCanchas | DialogoConfirmacion | `«create» con las reservas a cancelar (paso 4)` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 8 | VistaCanchas | DialogoConfirmacion | `mostrarAdvertencias( lineas); abrir()` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |

### `sec_cu23_p2.puml` — CU-23 Administrador desactiva cancha (parte 2: confirmación)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | DialogoConfirmacion | `Confirmar` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 2 | DialogoConfirmacion | VistaCanchas | `oyente de alConfirmar` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |
| 3 | VistaCanchas | PresentadorCanchas | `alConfirmarDesactivacion( idCancha)` | [PresentadorCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorCanchas.java) |
| 4 | PresentadorCanchas | ServicioRecursos | `desactivarCancha( id, true)` | [ServicioRecursos.java](../src/main/java/ar/edu/ub/club/recursos/aplicacion/ServicioRecursos.java) |
| 5 | PresentadorCanchas | PresentadorCanchas | `cargar(null)` | [PresentadorCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/PresentadorCanchas.java) |
| 6 | PresentadorCanchas | VistaCanchas | `mostrarCanchas(filas); mostrarMensaje("…")` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |
| 7 | PresentadorCanchas | VistaCanchas | `mostrarError(mensaje) (genérico con código de error, RNF-16)` | [VistaCanchas.java](../src/main/java/ar/edu/ub/club/recursos/ui/VistaCanchas.java) |
| 8 | Administrador | DialogoConfirmacion | `Cancelar` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |

### `sec_cu23_p3.puml` — CU-23 Administrador desactiva cancha (parte 3: ServicioRecursos.desactivarCancha)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorCanchas | ServicioRecursos | `desactivarCancha( id, confirmar)` | [ServicioRecursos.java](../src/main/java/ar/edu/ub/club/recursos/aplicacion/ServicioRecursos.java) |
| 2 | ServicioRecursos | FachadaPersistencia«Singleton» | `get(id, Cancha)` |  |
| 3 | ServicioRecursos | Cancha | `estaActivo() (paso 2)` | [Cancha.java](../src/main/java/ar/edu/ub/club/recursos/dominio/Cancha.java) |
| 4 | ServicioRecursos | ConsultaReservasFuturas | `buscarFuturasDe Cancha(id) (paso 3)` | [ConsultaReservasFuturas.java](../src/main/java/ar/edu/ub/club/compartido/dominio/ConsultaReservasFuturas.java) |
| 5 | ServicioRecursos | Cancha | `desactivar() (paso 6)` | [Cancha.java](../src/main/java/ar/edu/ub/club/recursos/dominio/Cancha.java) |
| 6 | ServicioRecursos | FachadaPersistencia«Singleton» | `put(c)` |  |
| 7 | ServicioRecursos | ApplicationEventPublisher | `publishEvent(Cancha DesactivadaEvento(id, admin, ahora))` |  |
| 8 | ServicioRecursos | ServicioAuditoria | `registrar(admin, BAJA, "Cancha", id, detalle) (paso 9)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

## CU-24 Administrador configura disponibilidad del club

<!-- NOTAS:CU-24 -->

### `sec_cu24_p1.puml` — CU-24 Administrador configura disponibilidad del club (parte 1: días y horario)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaDisponibilidadClub | `guarda días y horario (paso 3)` | [VistaDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/VistaDisponibilidadClub.java) |
| 2 | VistaDisponibilidadClub | PresentadorDisponibilidadClub | `alGuardarHorario( datos)` | [PresentadorDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/PresentadorDisponibilidadClub.java) |
| 3 | PresentadorDisponibilidadClub | ServicioAgenda | `configurarDisponibilidad( datos)` | [ServicioAgenda.java](../src/main/java/ar/edu/ub/club/agenda/aplicacion/ServicioAgenda.java) |
| 4 | PresentadorDisponibilidadClub | PresentadorDisponibilidadClub | `cargar()` | [PresentadorDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/PresentadorDisponibilidadClub.java) |
| 5 | PresentadorDisponibilidadClub | VistaDisponibilidadClub | `mostrarDisponibilidad( datos, feriados)` | [VistaDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/VistaDisponibilidadClub.java) |
| 6 | PresentadorDisponibilidadClub | VistaDisponibilidadClub | `mostrarError(motivo)` | [VistaDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/VistaDisponibilidadClub.java) |
| 7 | PresentadorDisponibilidadClub | PresentadorDisponibilidadClub | `alRequerirConfirmacion(ex)` | [PresentadorDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/PresentadorDisponibilidadClub.java) |
| 8 | PresentadorDisponibilidadClub | VistaDisponibilidadClub | `pedirConfirmacion( afectadas)` | [VistaDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/VistaDisponibilidadClub.java) |
| 9 | VistaDisponibilidadClub | DialogoConfirmacion | `«create» con las reservas, que se conservan (6.1)` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 10 | Administrador | DialogoConfirmacion | `Confirmar (6.2)` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 11 | DialogoConfirmacion | VistaDisponibilidadClub | `oyente de alConfirmar` | [VistaDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/VistaDisponibilidadClub.java) |
| 12 | VistaDisponibilidadClub | PresentadorDisponibilidadClub | `reenvía alGuardarHorario con datos.confirmar = true` | [PresentadorDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/PresentadorDisponibilidadClub.java) |

### `sec_cu24_p2.puml` — CU-24 Administrador configura disponibilidad del club (parte 2: ServicioAgenda.configurarDisponibilidad)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorDisponibilidadClub | ServicioAgenda | `configurarDisponibilidad( datos)` | [ServicioAgenda.java](../src/main/java/ar/edu/ub/club/agenda/aplicacion/ServicioAgenda.java) |
| 2 | ServicioAgenda | FachadaPersistencia«Singleton» | `get(ReglaReserva.ID_UNICO, ReglaReserva)` |  |
| 3 | ServicioAgenda | FachadaPersistencia«Singleton» | `get(DisponibilidadClub. ID_UNICO, DisponibilidadClub)` |  |
| 4 | ServicioAgenda | DisponibilidadClub | `modificar(dias, apertura, cierre, regla.duracion TurnoMinutos)` | [DisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/dominio/DisponibilidadClub.java) |
| 5 | ServicioAgenda | ConsultaReservasFuturas | `buscarFuturasDesde( ahora) (paso 6)` | [ConsultaReservasFuturas.java](../src/main/java/ar/edu/ub/club/compartido/dominio/ConsultaReservasFuturas.java) |
| 6 | ServicioAgenda | DisponibilidadClub | `atiende(r.turno.fecha)` | [DisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/dominio/DisponibilidadClub.java) |
| 7 | ServicioAgenda | DisponibilidadClub | `dentroDelHorario( r.turno)` | [DisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/dominio/DisponibilidadClub.java) |
| 8 | ServicioAgenda | FachadaPersistencia«Singleton» | `put(d) (paso 7)` |  |
| 9 | ServicioAgenda | ServicioAuditoria | `registrar(usuario, MODIFICACION, "DisponibilidadClub", d.id, detalle) (paso 8)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

### `sec_cu24_p3.puml` — CU-24 Administrador configura disponibilidad del club (parte 3: registrar feriado, FA1)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaDisponibilidadClub | `agrega un feriado (3.1)` | [VistaDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/VistaDisponibilidadClub.java) |
| 2 | VistaDisponibilidadClub | PresentadorDisponibilidadClub | `alAgregarFeriado( datos)` | [PresentadorDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/PresentadorDisponibilidadClub.java) |
| 3 | PresentadorDisponibilidadClub | ServicioAgenda | `registrarFeriado( datos)` | [ServicioAgenda.java](../src/main/java/ar/edu/ub/club/agenda/aplicacion/ServicioAgenda.java) |
| 4 | PresentadorDisponibilidadClub | PresentadorDisponibilidadClub | `cargar()` | [PresentadorDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/PresentadorDisponibilidadClub.java) |
| 5 | PresentadorDisponibilidadClub | VistaDisponibilidadClub | `mostrarDisponibilidad( datos, feriados)` | [VistaDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/VistaDisponibilidadClub.java) |
| 6 | PresentadorDisponibilidadClub | VistaDisponibilidadClub | `marcarCampo("fecha", motivo)` | [VistaDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/VistaDisponibilidadClub.java) |
| 7 | PresentadorDisponibilidadClub | VistaDisponibilidadClub | `pedirConfirmacion( afectadas)` | [VistaDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/VistaDisponibilidadClub.java) |
| 8 | VistaDisponibilidadClub | DialogoConfirmacion | `«create» con las reservas de la fecha` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 9 | Administrador | DialogoConfirmacion | `Confirmar (3.4)` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 10 | DialogoConfirmacion | VistaDisponibilidadClub | `oyente de alConfirmar` | [VistaDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/VistaDisponibilidadClub.java) |
| 11 | VistaDisponibilidadClub | PresentadorDisponibilidadClub | `reenvía alAgregarFeriado con datos.confirmar = true` | [PresentadorDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/PresentadorDisponibilidadClub.java) |

### `sec_cu24_p4.puml` — CU-24 Administrador configura disponibilidad del club (parte 4: ServicioAgenda.registrarFeriado)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorDisponibilidadClub | ServicioAgenda | `registrarFeriado( datos)` | [ServicioAgenda.java](../src/main/java/ar/edu/ub/club/agenda/aplicacion/ServicioAgenda.java) |
| 2 | ServicioAgenda | FachadaPersistencia«Singleton» | `contar(FeriadosEntre( fecha, fecha))` |  |
| 3 | ServicioAgenda | ConsultaReservasFuturas | `buscarDeFecha( fecha) (3.3)` | [ConsultaReservasFuturas.java](../src/main/java/ar/edu/ub/club/compartido/dominio/ConsultaReservasFuturas.java) |
| 4 | ServicioAgenda | Feriado | `«create» (fecha, descripcion)` | [Feriado.java](../src/main/java/ar/edu/ub/club/agenda/dominio/Feriado.java) |
| 5 | ServicioAgenda | FachadaPersistencia«Singleton» | `put(f) (3.5)` |  |
| 6 | ServicioAgenda | ServicioAuditoria | `registrar(usuario, ALTA, "Feriado", f.id, detalle)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

### `sec_cu24_p5.puml` — CU-24 Administrador configura disponibilidad del club (parte 5: eliminar feriado, FA2)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaDisponibilidadClub | `Eliminar feriado (3.1)` | [VistaDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/VistaDisponibilidadClub.java) |
| 2 | VistaDisponibilidadClub | DialogoConfirmacion | `«create» pide confirmación (3.2)` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 3 | Administrador | DialogoConfirmacion | `Confirmar` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 4 | DialogoConfirmacion | VistaDisponibilidadClub | `oyente de alConfirmar` | [VistaDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/VistaDisponibilidadClub.java) |
| 5 | VistaDisponibilidadClub | PresentadorDisponibilidadClub | `alEliminarFeriado( idFeriado)` | [PresentadorDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/PresentadorDisponibilidadClub.java) |
| 6 | PresentadorDisponibilidadClub | ServicioAgenda | `eliminarFeriado(id)` | [ServicioAgenda.java](../src/main/java/ar/edu/ub/club/agenda/aplicacion/ServicioAgenda.java) |
| 7 | ServicioAgenda | FachadaPersistencia«Singleton» | `get(id, Feriado)` |  |
| 8 | ServicioAgenda | FachadaPersistencia«Singleton» | `eliminar(f) (3.4)` |  |
| 9 | ServicioAgenda | ServicioAuditoria | `registrar(usuario, BAJA, "Feriado", id, detalle)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |
| 10 | PresentadorDisponibilidadClub | PresentadorDisponibilidadClub | `cargar()` | [PresentadorDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/PresentadorDisponibilidadClub.java) |
| 11 | PresentadorDisponibilidadClub | VistaDisponibilidadClub | `mostrarDisponibilidad( datos, feriados)` | [VistaDisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/ui/VistaDisponibilidadClub.java) |
| 12 | Administrador | DialogoConfirmacion | `Cancelar` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |

## CU-25 Administrador configura reglas de reserva

<!-- NOTAS:CU-25 -->

### `sec_cu25_p1.puml` — CU-25 Administrador configura reglas de reserva (parte 1: de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaReglasReserva | `guarda las reglas (paso 3)` | [VistaReglasReserva.java](../src/main/java/ar/edu/ub/club/agenda/ui/VistaReglasReserva.java) |
| 2 | VistaReglasReserva | PresentadorReglasReserva | `alGuardar(datos)` | [PresentadorReglasReserva.java](../src/main/java/ar/edu/ub/club/agenda/ui/PresentadorReglasReserva.java) |
| 3 | PresentadorReglasReserva | ServicioAgenda | `configurarReglas( datos)` | [ServicioAgenda.java](../src/main/java/ar/edu/ub/club/agenda/aplicacion/ServicioAgenda.java) |
| 4 | PresentadorReglasReserva | VistaReglasReserva | `mostrarReglas(datos); mostrarMensaje("…")` | [VistaReglasReserva.java](../src/main/java/ar/edu/ub/club/agenda/ui/VistaReglasReserva.java) |
| 5 | PresentadorReglasReserva | VistaReglasReserva | `mostrarError(motivo) o marcarCampo(campo, motivo)` | [VistaReglasReserva.java](../src/main/java/ar/edu/ub/club/agenda/ui/VistaReglasReserva.java) |

### `sec_cu25_p2.puml` — CU-25 Administrador configura reglas de reserva (parte 2: ServicioAgenda.configurarReglas)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorReglasReserva | ServicioAgenda | `configurarReglas( datos)` | [ServicioAgenda.java](../src/main/java/ar/edu/ub/club/agenda/aplicacion/ServicioAgenda.java) |
| 2 | ServicioAgenda | FachadaPersistencia«Singleton» | `get(ReglaReserva.ID_UNICO, ReglaReserva)` |  |
| 3 | ServicioAgenda | FachadaPersistencia«Singleton» | `get(DisponibilidadClub. ID_UNICO, DisponibilidadClub)` |  |
| 4 | ServicioAgenda | ReglaReserva | `modificar(valores, d)` | [ReglaReserva.java](../src/main/java/ar/edu/ub/club/agenda/dominio/ReglaReserva.java) |
| 5 | ServicioAgenda | FachadaPersistencia«Singleton» | `put(regla) (paso 6)` |  |
| 6 | ServicioAgenda | ServicioAuditoria | `registrar(usuario, MODIFICACION, "ReglaReserva", id, detalle) (paso 7)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

## CU-26 Socio o Administrador consulta disponibilidad de canchas

<!-- NOTAS:CU-26 -->

**Implementación (reservas y notificaciones) — CU-26 Consultar disponibilidad (sec_cu26_p1, sec_cu26_p2, sec_cu26_p3)**

`VistaDisponibilidadCanchas.alEntrar` → `PresentadorDisponibilidadCanchas.cargar` → `ServicioRecursos.listarDeportes` /
`listarCanchas(null)` → `pantalla.mostrarFiltros`; `ServicioAgenda.consultarReglas` → `mostrarRangoCalendario`;
`ControlAcceso.tienePermiso(usuarioActual.obtener(), GESTIONAR_RESERVAS_CLUB)` → `mostrarEleccionSocio`.
Botón «Consultar» → `VistaDisponibilidadCanchas.filtro()` → `PresentadorDisponibilidadCanchas.alConsultar(filtro)` →
`ServicioAgenda.consultarDisponibilidad` → (agenda) `ConsultaTurnosOcupados.turnosOcupados` = `ConsultaTurnosOcupadosReservas.turnosOcupados`
→ `FachadaPersistencia.buscar(new TurnosOcupadosDeCancha(idCancha, fecha))` → tabla `reserva` (CONFIRMADAS) →
`FilaTurno.desde(t, esAdministrador)` → `VistaDisponibilidadCanchas.mostrarTurnos`.

**Diagramas de secuencia del diseño:**

### `sec_cu26_p1.puml` — CU-26 Socio o Administrador consulta disponibilidad de canchas (parte 1: de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | SociooAdministrador | VistaDisponibilidadCanchas | `abre P-19 (paso 1)` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |
| 2 | VistaDisponibilidadCanchas | PresentadorDisponibilidadCanchas | `cargar()` | [PresentadorDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorDisponibilidadCanchas.java) |
| 3 | PresentadorDisponibilidadCanchas | ServicioRecursos | `listarDeportes()` | [ServicioRecursos.java](../src/main/java/ar/edu/ub/club/recursos/aplicacion/ServicioRecursos.java) |
| 4 | PresentadorDisponibilidadCanchas | ServicioRecursos | `listarCanchas(null)` | [ServicioRecursos.java](../src/main/java/ar/edu/ub/club/recursos/aplicacion/ServicioRecursos.java) |
| 5 | PresentadorDisponibilidadCanchas | VistaDisponibilidadCanchas | `mostrarMensaje("no hay canchas disponibles para reservar")` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |
| 6 | PresentadorDisponibilidadCanchas | VistaDisponibilidadCanchas | `mostrarFiltros(deportes, canchas) (OpcionSeleccion)` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |
| 7 | PresentadorDisponibilidadCanchas | UsuarioActual | `obtener()` | [UsuarioActual.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/UsuarioActual.java) |
| 8 | PresentadorDisponibilidadCanchas | ControlAcceso | `tienePermiso(u, GESTIONAR_RESERVAS_CLUB)` | [ControlAcceso.java](../src/main/java/ar/edu/ub/club/compartido/seguridad/ControlAcceso.java) |
| 9 | PresentadorDisponibilidadCanchas | VistaDisponibilidadCanchas | `mostrarEleccionSocio( esAdministrativo)` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |
| 10 | SociooAdministrador | VistaDisponibilidadCanchas | `consulta (paso 3)` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |
| 11 | VistaDisponibilidadCanchas | PresentadorDisponibilidadCanchas | `alConsultar(filtro)` | [PresentadorDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorDisponibilidadCanchas.java) |
| 12 | PresentadorDisponibilidadCanchas | ServicioAgenda | `consultarDisponibilidad( filtro)` | [ServicioAgenda.java](../src/main/java/ar/edu/ub/club/agenda/aplicacion/ServicioAgenda.java) |
| 13 | PresentadorDisponibilidadCanchas | PresentadorDisponibilidadCanchas | `FilaTurno.desde(t, esAdministrativo)` | [PresentadorDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorDisponibilidadCanchas.java) |
| 14 | PresentadorDisponibilidadCanchas | VistaDisponibilidadCanchas | `mostrarTurnos(filas)` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |
| 15 | PresentadorDisponibilidadCanchas | VistaDisponibilidadCanchas | `mostrarError(motivo)` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |

### `sec_cu26_p2.puml` — CU-26 Consulta de disponibilidad (parte 2: ServicioAgenda.consultarDisponibilidad)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorDisponibilidadCanchas | ServicioAgenda | `consultarDisponibilidad( filtro)` | [ServicioAgenda.java](../src/main/java/ar/edu/ub/club/agenda/aplicacion/ServicioAgenda.java) |
| 2 | ServicioAgenda | FachadaPersistencia«Singleton» | `get(ReglaReserva.ID_UNICO, ReglaReserva)` |  |
| 3 | ServicioAgenda | ReglaReserva | `fechaConsultable( fecha, hoy) (paso 4)` | [ReglaReserva.java](../src/main/java/ar/edu/ub/club/agenda/dominio/ReglaReserva.java) |
| 4 | ServicioAgenda | FachadaPersistencia«Singleton» | `get(DisponibilidadClub. ID_UNICO, DisponibilidadClub)` |  |
| 5 | ServicioAgenda | DisponibilidadClub | `atiende(fecha) (paso 5)` | [DisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/dominio/DisponibilidadClub.java) |
| 6 | ServicioAgenda | FachadaPersistencia«Singleton» | `buscar(CanchasDeDeporte( idDeporte)) (paso 6)` |  |
| 7 | ServicioAgenda | ConsultaTurnosOcupados | `turnosOcupados( cancha.id, fecha) (paso 7)` | [ConsultaTurnosOcupados.java](../src/main/java/ar/edu/ub/club/agenda/dominio/ConsultaTurnosOcupados.java) |
| 8 | ServicioAgenda | CalendarioTurnos | `generarTurnos(cancha, fecha, d, regla, ocupados)` | [CalendarioTurnos.java](../src/main/java/ar/edu/ub/club/agenda/dominio/CalendarioTurnos.java) |

### `sec_cu26_p3.puml` — CU-26 Consulta de disponibilidad (parte 3: CalendarioTurnos.generarTurnos)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioAgenda | CalendarioTurnos | `generarTurnos(cancha, fecha, d, regla, ocupados)` | [CalendarioTurnos.java](../src/main/java/ar/edu/ub/club/agenda/dominio/CalendarioTurnos.java) |
| 2 | CalendarioTurnos | DisponibilidadClub | `getHoraApertura(), getHoraCierre()` | [DisponibilidadClub.java](../src/main/java/ar/edu/ub/club/agenda/dominio/DisponibilidadClub.java) |
| 3 | CalendarioTurnos | ReglaReserva | `getDuracionTurno Minutos()` | [ReglaReserva.java](../src/main/java/ar/edu/ub/club/agenda/dominio/ReglaReserva.java) |
| 4 | CalendarioTurnos | Reloj | `ahora()` | [Reloj.java](../src/main/java/ar/edu/ub/club/compartido/dominio/Reloj.java) |
| 5 | CalendarioTurnos | Turno | `«create» (fecha, inicio, inicio + duracion)` | [Turno.java](../src/main/java/ar/edu/ub/club/agenda/dominio/Turno.java) |
| 6 | CalendarioTurnos | Turno | `seSuperponeCon(o) para cada o de ocupados` | [Turno.java](../src/main/java/ar/edu/ub/club/agenda/dominio/Turno.java) |
| 7 | CalendarioTurnos | ReglaReserva | `cumpleAnticipacion Minima(t, ahora) (paso 9)` | [ReglaReserva.java](../src/main/java/ar/edu/ub/club/agenda/dominio/ReglaReserva.java) |
| 8 | CalendarioTurnos | TurnoDisponible | `«create» (cancha, t, estado)` | [TurnoDisponible.java](../src/main/java/ar/edu/ub/club/agenda/dominio/TurnoDisponible.java) |

## CU-27 Socio reserva cancha online

<!-- NOTAS:CU-27 -->

**Implementación (accesos, auditoría y configuración) — Auditoría de reservas — `4b_reservas_eventos` / diagramas de CU-27..33**

`PublicadorEventosReserva` (reservas) → `AuditorReservas.alOcurrir(evento)` → `ServicioAuditoria.registrar(e.getUsuario(), e.accion(), "Reserva", id, detalle)` → `FachadaPersistencia.put(new RegistroAuditoria)` → `BrokerRegistroAuditoria.insertar` (`registro_auditoria`).

**Implementación (reservas y notificaciones) — CU-27 Reservar online (sec_cu27_p1 a p4, col_cu27, pat_cadena, pat_observer_reserva, sec_comun_notificar)**

Acción «Reservar» → `PresentadorDisponibilidadCanchas.alElegirTurno` → `VistaDisponibilidadCanchas.abrirConfirmacion` →
`new DialogoConfirmarReserva(...)`.`abrir()` → `PresentadorConfirmarReserva.alAbrir` → `DialogoConfirmarReserva.mostrarTurno`.
«Confirmar» → `PresentadorConfirmarReserva.alConfirmar` → `SocioActual.obtener` → `FilaTurno.aTurno` →
`ServicioReservas.reservarOnline` → `ControlAcceso.verificar(RESERVAR_ONLINE)` → `ServicioReservas.registrarAlta` →
`FachadaPersistencia.buscarUno(new SocioParaReserva(id))` (FOR UPDATE, `usuario`/`socio`) → `buscarCancha` (`get(id, Cancha)`) →
`new SolicitudReserva(..., ALTA, SOCIO, ahora)` → `FabricaCadenaValidacion.crearCadena` (`buscarUno(GrupoVigenteDeSocio)`,
`get(DisponibilidadClub.ID_UNICO)`, `buscar(FeriadosEntre)`, `get(ReglaReserva.ID_UNICO)`, `buscar(ReservasFuturasDeSocio)`,
`ConsultaTurnosOcupadosReservas.turnosOcupados`, `ocupadosSinOriginal`, 7 × `new Validador...`, `enlazar`) →
`ValidadorReservaEncadenado.validar` → `verificar` de `ValidadorSocioHabilitado`, `ValidadorRecursoActivo`, `ValidadorHorarioClub`,
`ValidadorDuracionTurno`, `ValidadorAnticipacion`, `ValidadorLimiteReservas`, `ValidadorTurnoLibre` → `new Reserva(...)` →
`Reserva.confirmar` (`EstadoReserva.desdeCodigo("CONFIRMADA")`) → `FachadaPersistencia.put` → `BrokerReserva.valoresInsercion`
(`BrokerBaseDatos.insertar`, tabla `reserva`; 23P01 → `ErrorNegocio`) → `PublicadorEventosReserva.publicar(new ReservaConfirmadaEvento)` →
`AuditorReservas.alOcurrir` (auditoría) y `NotificadorReservas.alOcurrir` → (after commit) `notificarDespuesDelCommit` → `enviar` →
`new Notificacion` → `put` (`BrokerNotificacion`, tabla `notificacion`) → `ServicioCorreo.enviar` (`CorreoSimuladoAdapter`/`CorreoSmtpAdapter`)
→ `Notificacion.marcarEnviada/marcarFallida` → `put`. Vuelta: `PresentadorConfirmarReserva` → `DialogoConfirmarReserva.cerrarConExito`
→ `DialogoBase.notificarResuelto` (`DialogoResueltoEvento`) → `VistaDisponibilidadCanchas` (mensaje) y `alCerrar` → `alConsultar`.

**Diagramas de secuencia del diseño:**

### `sec_cu27_p1.puml` — CU-27 Socio reserva cancha online (parte 1: de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Socio | VistaDisponibilidadCanchas | `elige un turno libre` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |
| 2 | VistaDisponibilidadCanchas | PresentadorDisponibilidadCanchas | `alElegirTurno(turno)` | [PresentadorDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorDisponibilidadCanchas.java) |
| 3 | PresentadorDisponibilidadCanchas | VistaDisponibilidadCanchas | `abrirConfirmacion( turno, null)` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |
| 4 | VistaDisponibilidadCanchas | DialogoConfirmarReserva | `«create»; abrir()` | [DialogoConfirmarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/DialogoConfirmarReserva.java) |
| 5 | DialogoConfirmarReserva | PresentadorConfirmarReserva | `alAbrir(turno, null)` | [PresentadorConfirmarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorConfirmarReserva.java) |
| 6 | PresentadorConfirmarReserva | DialogoConfirmarReserva | `mostrarTurno(turno, null)` | [DialogoConfirmarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/DialogoConfirmarReserva.java) |
| 7 | Socio | DialogoConfirmarReserva | `Confirmar (paso 3)` | [DialogoConfirmarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/DialogoConfirmarReserva.java) |
| 8 | DialogoConfirmarReserva | PresentadorConfirmarReserva | `alConfirmar()` | [PresentadorConfirmarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorConfirmarReserva.java) |
| 9 | PresentadorConfirmarReserva | SocioActual | `obtener()` | [SocioActual.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/SocioActual.java) |
| 10 | PresentadorConfirmarReserva | ServicioReservas | `reservarOnline(socio, turno.idCancha, turno.aTurno())` | [ServicioReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/ServicioReservas.java) |
| 11 | PresentadorConfirmarReserva | DialogoConfirmarReserva | `cerrarConExito()` | [DialogoConfirmarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/DialogoConfirmarReserva.java) |
| 12 | DialogoConfirmarReserva | VistaDisponibilidadCanchas | `DialogoResueltoEvento` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |
| 13 | VistaDisponibilidadCanchas | PresentadorDisponibilidadCanchas | `alConsultar(filtro)` | [PresentadorDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorDisponibilidadCanchas.java) |
| 14 | PresentadorDisponibilidadCanchas | VistaDisponibilidadCanchas | `mostrarTurnos(filas); mostrarMensaje("…")` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |
| 15 | PresentadorConfirmarReserva | DialogoConfirmarReserva | `mostrarError(motivo)` | [DialogoConfirmarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/DialogoConfirmarReserva.java) |
| 16 | PresentadorConfirmarReserva | DialogoConfirmarReserva | `mostrarError(mensaje)` | [DialogoConfirmarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/DialogoConfirmarReserva.java) |
| 17 | DialogoConfirmarReserva | VistaDisponibilidadCanchas | `al cerrar, la vista anfitriona recarga` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |
| 18 | VistaDisponibilidadCanchas | PresentadorDisponibilidadCanchas | `alConsultar(filtro)` | [PresentadorDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorDisponibilidadCanchas.java) |
| 19 | Socio | DialogoConfirmarReserva | `cierra el diálogo` | [DialogoConfirmarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/DialogoConfirmarReserva.java) |

### `sec_cu27_p2.puml` — CU-27 Socio reserva cancha online (parte 2: ServicioReservas.reservarOnline)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorConfirmarReserva | ServicioReservas | `reservarOnline(socio, idCancha, turno)` | [ServicioReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/ServicioReservas.java) |
| 2 | ServicioReservas | FachadaPersistencia«Singleton» | `get(idCancha, Cancha)` |  |
| 3 | ServicioReservas | ServicioReservas | `s = new SolicitudReserva( socio, cancha, turno, null, ALTA, SOCIO, reloj.ahora()) (paso 4)` | [ServicioReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/ServicioReservas.java) |
| 4 | ServicioReservas | FabricaCadenaValidacion | `crearCadena(s)` | [FabricaCadenaValidacion.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/FabricaCadenaValidacion.java) |
| 5 | ServicioReservas | ValidadorReserva | `validar(s)` | [ValidadorReserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/ValidadorReserva.java) |
| 6 | ServicioReservas | Reserva | `«create» (socio, cancha, turno, registradaPor: socio, ahora)` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |
| 7 | ServicioReservas | Reserva | `confirmar()` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |
| 8 | ServicioReservas | FachadaPersistencia«Singleton» | `put(r) (paso 12)` |  |
| 9 | ServicioReservas | PublicadorEventosReserva | `publicar(ReservaConfirmada Evento(r, socio, ahora))` | [PublicadorEventosReserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/PublicadorEventosReserva.java) |

### `sec_cu27_p3.puml` — CU-27 (parte 3: FabricaCadenaValidacion.crearCadena arma la cadena)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioReservas | FabricaCadenaValidacion | `crearCadena(s)` | [FabricaCadenaValidacion.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/FabricaCadenaValidacion.java) |
| 2 | FabricaCadenaValidacion | FachadaPersistencia«Singleton» | `buscarUno(GrupoVigente DeSocio(s.socio.id))` |  |
| 3 | FabricaCadenaValidacion | FachadaPersistencia«Singleton» | `get(DisponibilidadClub.ID_UNICO, DisponibilidadClub)` |  |
| 4 | FabricaCadenaValidacion | FachadaPersistencia«Singleton» | `buscar(FeriadosEntre( fecha, fecha))` |  |
| 5 | FabricaCadenaValidacion | FachadaPersistencia«Singleton» | `get(ReglaReserva.ID_UNICO, ReglaReserva)` |  |
| 6 | FabricaCadenaValidacion | FachadaPersistencia«Singleton» | `buscar(ReservasFuturasDeSocio( s.socio.id, s.ahora))` |  |
| 7 | FabricaCadenaValidacion | ConsultaTurnosOcupados | `turnosOcupados( s.cancha.id, s.turno.fecha)` | [ConsultaTurnosOcupados.java](../src/main/java/ar/edu/ub/club/agenda/dominio/ConsultaTurnosOcupados.java) |
| 8 | FabricaCadenaValidacion | FabricaCadenaValidacion | `excluye s.reservaOriginal de vigentes y su turno de ocupados` | [FabricaCadenaValidacion.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/FabricaCadenaValidacion.java) |
| 9 | FabricaCadenaValidacion | FabricaCadenaValidacion | `v1 = new ValidadorSocioHabilitado(grupo) v2 = new ValidadorRecursoActivo() v3 = new ValidadorHorarioClub( disponibilidad, feriados) v4 = new ValidadorDuracionTurno( regla, disponibilidad) v5 = new ValidadorAnticipacion(regla) v6 = new ValidadorLimiteReservas( regla, vigentes) v7 = new ValidadorTurnoLibre(ocupados)` | [FabricaCadenaValidacion.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/FabricaCadenaValidacion.java) |
| 10 | FabricaCadenaValidacion | FabricaCadenaValidacion | `v1.enlazar(v2).enlazar(v3) .enlazar(v4).enlazar(v5) .enlazar(v6).enlazar(v7)` | [FabricaCadenaValidacion.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/FabricaCadenaValidacion.java) |

### `sec_cu27_p4.puml` — CU-27 (parte 4: recorrido de la cadena de validadores)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioReservas | ValidadorSocioHabilitado | `validar(s)` | [ValidadorSocioHabilitado.java](../src/main/java/ar/edu/ub/club/reservas/dominio/ValidadorSocioHabilitado.java) |
| 2 | ValidadorSocioHabilitado | ValidadorSocioHabilitado | `verificar(s)` | [ValidadorSocioHabilitado.java](../src/main/java/ar/edu/ub/club/reservas/dominio/ValidadorSocioHabilitado.java) |
| 3 | ValidadorSocioHabilitado | ValidadorRecursoActivo | `validar(s)` | [ValidadorRecursoActivo.java](../src/main/java/ar/edu/ub/club/reservas/dominio/ValidadorRecursoActivo.java) |
| 4 | ValidadorRecursoActivo | ValidadorRecursoActivo | `verificar(s)` | [ValidadorRecursoActivo.java](../src/main/java/ar/edu/ub/club/reservas/dominio/ValidadorRecursoActivo.java) |
| 5 | ValidadorRecursoActivo | ValidadorHorarioClub | `validar(s)` | [ValidadorHorarioClub.java](../src/main/java/ar/edu/ub/club/reservas/dominio/ValidadorHorarioClub.java) |
| 6 | ValidadorHorarioClub | ValidadorHorarioClub | `verificar(s)` | [ValidadorHorarioClub.java](../src/main/java/ar/edu/ub/club/reservas/dominio/ValidadorHorarioClub.java) |
| 7 | ValidadorHorarioClub | ValidadorDuracionTurno | `validar(s)` | [ValidadorDuracionTurno.java](../src/main/java/ar/edu/ub/club/reservas/dominio/ValidadorDuracionTurno.java) |
| 8 | ValidadorDuracionTurno | ValidadorDuracionTurno | `verificar(s)` | [ValidadorDuracionTurno.java](../src/main/java/ar/edu/ub/club/reservas/dominio/ValidadorDuracionTurno.java) |
| 9 | ValidadorDuracionTurno | ValidadorAnticipacion | `validar(s)` | [ValidadorAnticipacion.java](../src/main/java/ar/edu/ub/club/reservas/dominio/ValidadorAnticipacion.java) |
| 10 | ValidadorAnticipacion | ValidadorAnticipacion | `verificar(s)` | [ValidadorAnticipacion.java](../src/main/java/ar/edu/ub/club/reservas/dominio/ValidadorAnticipacion.java) |
| 11 | ValidadorAnticipacion | ValidadorLimiteReservas | `validar(s)` | [ValidadorLimiteReservas.java](../src/main/java/ar/edu/ub/club/reservas/dominio/ValidadorLimiteReservas.java) |
| 12 | ValidadorLimiteReservas | ValidadorLimiteReservas | `verificar(s)` | [ValidadorLimiteReservas.java](../src/main/java/ar/edu/ub/club/reservas/dominio/ValidadorLimiteReservas.java) |
| 13 | ValidadorLimiteReservas | ValidadorTurnoLibre | `validar(s)` | [ValidadorTurnoLibre.java](../src/main/java/ar/edu/ub/club/reservas/dominio/ValidadorTurnoLibre.java) |
| 14 | ValidadorTurnoLibre | ValidadorTurnoLibre | `verificar(s)` | [ValidadorTurnoLibre.java](../src/main/java/ar/edu/ub/club/reservas/dominio/ValidadorTurnoLibre.java) |

## CU-28 Administrador registra reserva manual

<!-- NOTAS:CU-28 -->

**Implementación (reservas y notificaciones) — CU-28 Reserva manual (sec_cu28_p1, p2, p3)**

`VistaDisponibilidadCanchas` «Buscar socio» → `PresentadorDisponibilidadCanchas.alBuscarSocio` → `ServicioSocios.buscarSocios` →
`FilaSocio.desde` → `mostrarSocios`; «Elegir» → `alElegirSocio` → `ServicioSocios.consultarSocio` → `FilaSocio.desde`,
`FilaCuota.desde(cuota más reciente, false)` → `mostrarSocioElegido`. Turno → `alElegirTurno` → `abrirConfirmacion(turno, idSocioElegido)`
→ `PresentadorConfirmarReserva.alConfirmar` → `UsuarioActual.obtenerAdministrador` → `ServicioReservas.registrarReservaManual` →
`verificar(GESTIONAR_RESERVAS_CLUB)` → `get(idSocio, Socio)` → `registrarAlta(..., admin, ADMINISTRADOR)` → (igual que CU-27) →
`ReservaConfirmadaEvento(r, admin, ahora)`.

**Diagramas de secuencia del diseño:**

### `sec_cu28_p1.puml` — CU-28 Administrador registra reserva manual (parte 1: elección del socio)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaDisponibilidadCanchas | `selecciona Registrar reserva manual (paso 1)` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |
| 2 | Administrador | VistaDisponibilidadCanchas | `busca el socio (paso 3)` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |
| 3 | VistaDisponibilidadCanchas | PresentadorDisponibilidadCanchas | `alBuscarSocio(criterio)` | [PresentadorDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorDisponibilidadCanchas.java) |
| 4 | PresentadorDisponibilidadCanchas | ServicioSocios | `buscarSocios(criterio)` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 5 | ServicioSocios | FachadaPersistencia«Singleton» | `buscar(SociosPorCriterio( texto, estado))` |  |
| 6 | PresentadorDisponibilidadCanchas | PresentadorDisponibilidadCanchas | `FilaSocio.desde(s)` | [PresentadorDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorDisponibilidadCanchas.java) |
| 7 | PresentadorDisponibilidadCanchas | VistaDisponibilidadCanchas | `mostrarSocios(filas)` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |
| 8 | Administrador | VistaDisponibilidadCanchas | `elige un socio (paso 5)` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |
| 9 | VistaDisponibilidadCanchas | PresentadorDisponibilidadCanchas | `alElegirSocio(idSocio)` | [PresentadorDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorDisponibilidadCanchas.java) |
| 10 | PresentadorDisponibilidadCanchas | ServicioSocios | `consultarSocio(idSocio)` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 11 | ServicioSocios | FachadaPersistencia«Singleton» | `get(idSocio, Socio)` |  |
| 12 | PresentadorDisponibilidadCanchas | VistaDisponibilidadCanchas | `mostrarSocioElegido( FilaSocio.desde(ficha.socio), FilaCuota.desde( cuotaVigente, false))` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |

### `sec_cu28_p2.puml` — CU-28 Administrador registra reserva manual (parte 2: de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaDisponibilidadCanchas | `elige un turno libre` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |
| 2 | VistaDisponibilidadCanchas | PresentadorDisponibilidadCanchas | `alElegirTurno(turno)` | [PresentadorDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorDisponibilidadCanchas.java) |
| 3 | PresentadorDisponibilidadCanchas | VistaDisponibilidadCanchas | `abrirConfirmacion( turno, idSocioElegido)` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |
| 4 | VistaDisponibilidadCanchas | DialogoConfirmarReserva | `«create»; abrir()` | [DialogoConfirmarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/DialogoConfirmarReserva.java) |
| 5 | DialogoConfirmarReserva | PresentadorConfirmarReserva | `alAbrir(turno, idSocio)` | [PresentadorConfirmarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorConfirmarReserva.java) |
| 6 | PresentadorConfirmarReserva | DialogoConfirmarReserva | `mostrarTurno(turno, socio)` | [DialogoConfirmarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/DialogoConfirmarReserva.java) |
| 7 | Administrador | DialogoConfirmarReserva | `Confirmar (paso 9)` | [DialogoConfirmarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/DialogoConfirmarReserva.java) |
| 8 | DialogoConfirmarReserva | PresentadorConfirmarReserva | `alConfirmar()` | [PresentadorConfirmarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorConfirmarReserva.java) |
| 9 | PresentadorConfirmarReserva | UsuarioActual | `obtener Administrador()` | [UsuarioActual.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/UsuarioActual.java) |
| 10 | PresentadorConfirmarReserva | ServicioReservas | `registrarReserva Manual(admin, idSocio, turno.idCancha, turno.aTurno())` | [ServicioReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/ServicioReservas.java) |
| 11 | PresentadorConfirmarReserva | DialogoConfirmarReserva | `cerrarConExito()` | [DialogoConfirmarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/DialogoConfirmarReserva.java) |
| 12 | DialogoConfirmarReserva | VistaDisponibilidadCanchas | `DialogoResueltoEvento` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |
| 13 | VistaDisponibilidadCanchas | PresentadorDisponibilidadCanchas | `alConsultar(filtro)` | [PresentadorDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorDisponibilidadCanchas.java) |
| 14 | PresentadorConfirmarReserva | DialogoConfirmarReserva | `mostrarError(motivo)` | [DialogoConfirmarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/DialogoConfirmarReserva.java) |
| 15 | PresentadorConfirmarReserva | DialogoConfirmarReserva | `mostrarError(mensaje)` | [DialogoConfirmarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/DialogoConfirmarReserva.java) |
| 16 | DialogoConfirmarReserva | VistaDisponibilidadCanchas | `al cerrar, la vista anfitriona recarga` | [VistaDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaDisponibilidadCanchas.java) |
| 17 | VistaDisponibilidadCanchas | PresentadorDisponibilidadCanchas | `alConsultar(filtro)` | [PresentadorDisponibilidadCanchas.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorDisponibilidadCanchas.java) |
| 18 | Administrador | DialogoConfirmarReserva | `cierra el diálogo` | [DialogoConfirmarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/DialogoConfirmarReserva.java) |

### `sec_cu28_p3.puml` — CU-28 Administrador registra reserva manual (parte 3: ServicioReservas.registrarReservaManual)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioReservas | FachadaPersistencia«Singleton» | `get(idSocio, Socio)` |  |
| 2 | ServicioReservas | FachadaPersistencia«Singleton» | `get(idCancha, Cancha)` |  |
| 3 | ServicioReservas | ServicioReservas | `s = new Solicitud Reserva(socio, cancha, turno, null, ALTA, ADMINISTRADOR, reloj.ahora()) (paso 10)` | [ServicioReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/ServicioReservas.java) |
| 4 | ServicioReservas | FabricaCadenaValidacion | `crearCadena(s)` | [FabricaCadenaValidacion.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/FabricaCadenaValidacion.java) |
| 5 | ServicioReservas | ValidadorReserva | `validar(s)` | [ValidadorReserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/ValidadorReserva.java) |
| 6 | ServicioReservas | Reserva | `«create» (socio, cancha, turno, registradaPor: admin, ahora)` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |
| 7 | ServicioReservas | Reserva | `confirmar()` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |
| 8 | ServicioReservas | FachadaPersistencia«Singleton» | `put(r) (paso 18)` |  |
| 9 | ServicioReservas | PublicadorEventosReserva | `publicar(ReservaConfirmada Evento(r, admin, ahora))` | [PublicadorEventosReserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/PublicadorEventosReserva.java) |

## CU-29 Socio consulta reservas propias

<!-- NOTAS:CU-29 -->

**Implementación (reservas y notificaciones) — CU-29 Reservas propias (sec_cu29)**

`VistaMisReservas.alEntrar`/`buscar` (`FiltroFechas.valor`) → `PresentadorMisReservas.cargar` → `SocioActual.obtener` →
`ServicioReservas.consultarReservasPropias` → `verificar(GESTIONAR_RESERVAS_PROPIAS)` → `FiltroReservas.validar` →
`FiltroReservas.paraSocio` → `FachadaPersistencia.buscar(new ReservasPorFiltro(filtro))` → `BrokerReserva.consultaBase` + `sql()`
(tablas `reserva`, `usuario`, `socio`, `cancha`) → `BrokerReserva.crearObjeto`/`cargarAsociaciones` → `FilaReserva.desde` →
`VistaMisReservas.mostrarReservas`. «Ver detalle» → `PresentadorMisReservas.alVerDetalle` → `ServicioReservas.consultarReserva`
(`perteneceA`) + `ServicioNotificaciones.consultarPorReserva` (`buscar(NotificacionesDeReserva)`, `BrokerNotificacion`) +
`ServicioAgenda.consultarReglas` → `DetalleReserva.desde` (`FilaNotificacion.desde`) → `VistaMisReservas.mostrarDetalle`.

**Diagramas de secuencia del diseño:**

### `sec_cu29.puml` — CU-29 Socio consulta reservas propias

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Socio | VistaMisReservas | `ingresa a P-21 (paso 1)` | [VistaMisReservas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaMisReservas.java) |
| 2 | VistaMisReservas | PresentadorMisReservas | `cargar(filtro)` | [PresentadorMisReservas.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorMisReservas.java) |
| 3 | PresentadorMisReservas | SocioActual | `obtener()` | [SocioActual.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/SocioActual.java) |
| 4 | PresentadorMisReservas | ServicioReservas | `consultarReservas Propias(socio, filtro)` | [ServicioReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/ServicioReservas.java) |
| 5 | ServicioReservas | ServicioReservas | `filtro.socio = socio (RN-20: se ignora otro socio en el filtro)` | [ServicioReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/ServicioReservas.java) |
| 6 | ServicioReservas | FachadaPersistencia«Singleton» | `buscar(ReservasPorFiltro( filtro))` |  |
| 7 | PresentadorMisReservas | PresentadorMisReservas | `FilaReserva.desde(r)` | [PresentadorMisReservas.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorMisReservas.java) |
| 8 | PresentadorMisReservas | VistaMisReservas | `mostrarReservas(filas)` | [VistaMisReservas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaMisReservas.java) |
| 9 | Socio | VistaMisReservas | `selecciona una fila (paso 7)` | [VistaMisReservas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaMisReservas.java) |
| 10 | VistaMisReservas | PresentadorMisReservas | `alVerDetalle(idReserva)` | [PresentadorMisReservas.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorMisReservas.java) |
| 11 | PresentadorMisReservas | ServicioReservas | `consultarReserva(idReserva)` | [ServicioReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/ServicioReservas.java) |
| 12 | PresentadorMisReservas | ServicioNotificaciones | `consultarPorReserva( usuario, idReserva)` | [ServicioNotificaciones.java](../src/main/java/ar/edu/ub/club/notificaciones/aplicacion/ServicioNotificaciones.java) |
| 13 | PresentadorMisReservas | ServicioAgenda | `consultarReglas()` | [ServicioAgenda.java](../src/main/java/ar/edu/ub/club/agenda/aplicacion/ServicioAgenda.java) |
| 14 | PresentadorMisReservas | VistaMisReservas | `mostrarDetalle(DetalleReserva. desde(r, notificaciones, regla))` | [VistaMisReservas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaMisReservas.java) |

## CU-30 Socio cancela reserva propia

<!-- NOTAS:CU-30 -->

**Implementación (reservas y notificaciones) — CU-30 Cancelar reserva propia (sec_cu30_p1, sec_cu30_p2, est_reserva, pat_state_reserva)**

`VistaMisReservas` «Cancelar reserva» → `confirmarCancelacion` (`DialogoConfirmacion`) → `PresentadorMisReservas.alCancelar` →
`SocioActual.obtener` → `ServicioReservas.cancelarReservaPropia` → `verificar` → `buscarReserva` (`get`; inexistente → `AccesoDenegadoException`)
→ `Reserva.perteneceA` → `Reserva.esVigente` → `get(ReglaReserva.ID_UNICO)` → `ReglaReserva.admiteCancelacion` →
`Reserva.cancelar` → `EstadoReservaConfirmada.cancelar` → `Reserva.registrarCancelacion` + `Reserva.cambiarEstado(CANCELADA)` →
`FachadaPersistencia.put` → `BrokerReserva.valoresActualizacion` (`UPDATE reserva ... AND version = ?`; 0 filas → `ConcurrenciaException`
→ `PresentadorMisReservas.alConflicto`) → `PublicadorEventosReserva.publicar(new ReservaCanceladaEvento)` → auditoría y notificación →
`PresentadorMisReservas.cargar`.

**Diagramas de secuencia del diseño:**

### `sec_cu30_p1.puml` — CU-30 Socio cancela reserva propia (parte 1: de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Socio | VistaMisReservas | `elige Cancelar en una reserva vigente (paso 1)` | [VistaMisReservas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaMisReservas.java) |
| 2 | VistaMisReservas | DialogoConfirmacion | `«create»; abrir() (paso 5, RN-10)` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 3 | Socio | DialogoConfirmacion | `Confirmar` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 4 | DialogoConfirmacion | VistaMisReservas | `oyente de alConfirmar` | [VistaMisReservas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaMisReservas.java) |
| 5 | VistaMisReservas | PresentadorMisReservas | `alCancelar(idReserva)` | [PresentadorMisReservas.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorMisReservas.java) |
| 6 | PresentadorMisReservas | SocioActual | `obtener()` | [SocioActual.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/SocioActual.java) |
| 7 | PresentadorMisReservas | ServicioReservas | `cancelarReserva Propia(socio, idReserva)` | [ServicioReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/ServicioReservas.java) |
| 8 | PresentadorMisReservas | PresentadorMisReservas | `cargar(filtro)` | [PresentadorMisReservas.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorMisReservas.java) |
| 9 | PresentadorMisReservas | VistaMisReservas | `mostrarReservas(filas)` | [VistaMisReservas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaMisReservas.java) |
| 10 | PresentadorMisReservas | VistaMisReservas | `mostrarError("No tiene permiso para realizar esta operación.")` | [VistaMisReservas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaMisReservas.java) |
| 11 | PresentadorMisReservas | VistaMisReservas | `mostrarError(motivo)` | [VistaMisReservas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaMisReservas.java) |
| 12 | PresentadorMisReservas | PresentadorMisReservas | `alConflicto(ex)` | [PresentadorMisReservas.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorMisReservas.java) |
| 13 | PresentadorMisReservas | VistaMisReservas | `mostrarError("la reserva cambió"); cargar(filtro)` | [VistaMisReservas.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaMisReservas.java) |
| 14 | Socio | DialogoConfirmacion | `Cancelar` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |

### `sec_cu30_p2.puml` — CU-30 Socio cancela reserva propia (parte 2: ServicioReservas.cancelarReservaPropia)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioReservas | FachadaPersistencia«Singleton» | `get(idReserva, Reserva)` |  |
| 2 | ServicioReservas | Reserva | `perteneceA(socio) (paso 2)` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |
| 3 | ServicioReservas | Reserva | `esVigente(ahora) (paso 3)` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |
| 4 | ServicioReservas | FachadaPersistencia«Singleton» | `get(ReglaReserva.ID_UNICO, ReglaReserva)` |  |
| 5 | ServicioReservas | ReglaReserva | `admiteCancelacion(r.turno, ahora) (paso 4, RN-14)` | [ReglaReserva.java](../src/main/java/ar/edu/ub/club/agenda/dominio/ReglaReserva.java) |
| 6 | ServicioReservas | Reserva | `cancelar("Cancelada por el socio", socio, ahora) (paso 7)` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |
| 7 | Reserva | EstadoReservaConfirmada | `cancelar(r, motivo, socio, ahora)` | [EstadoReservaConfirmada.java](../src/main/java/ar/edu/ub/club/reservas/dominio/EstadoReservaConfirmada.java) |
| 8 | EstadoReservaConfirmada | Reserva | `cambiarEstado(Estado ReservaCancelada)` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |
| 9 | ServicioReservas | FachadaPersistencia«Singleton» | `put(r)` |  |
| 10 | ServicioReservas | PublicadorEventosReserva | `publicar(ReservaCancelada Evento(r, socio, ahora))` | [PublicadorEventosReserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/PublicadorEventosReserva.java) |

## CU-31 Administrador consulta reservas del club

<!-- NOTAS:CU-31 -->

**Implementación (reservas y notificaciones) — CU-31 Reservas del club (sec_cu31_p1, p2, p3)**

`VistaReservasClub.alEntrar` (filtro del día) / `buscar` → `PresentadorReservasClub.cargar` → (primera vez) `ServicioRecursos.listarDeportes`
→ `mostrarFiltros`; `ServicioReservas.consultarReservasClub` → `verificar(usuarioActual.obtener(), GESTIONAR_RESERVAS_CLUB)` →
`FiltroReservas.validar` → `buscar(new ReservasPorFiltro(filtro))` → `BrokerReserva` → `FilaReserva.desde` → `mostrarReservas`.
«Detalle» → `PresentadorReservasClub.alSeleccionar` → `ServicioReservas.consultarReserva` (`get(id, Reserva)`) →
`ServicioNotificaciones.consultarPorReserva` → `ServicioAgenda.consultarReglas` → `DetalleReserva.desde` → `VistaReservasClub.mostrarDetalle`.

**Diagramas de secuencia del diseño:**

### `sec_cu31_p1.puml` — CU-31 Administrador consulta reservas del club (parte 1: listado)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaReservasClub | `ingresa a P-22 (paso 1)` | [VistaReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaReservasClub.java) |
| 2 | VistaReservasClub | PresentadorReservasClub | `cargar(filtro)` | [PresentadorReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorReservasClub.java) |
| 3 | PresentadorReservasClub | ServicioReservas | `consultarReservas Club(filtro)` | [ServicioReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/ServicioReservas.java) |
| 4 | ServicioReservas | FachadaPersistencia«Singleton» | `buscar(ReservasPorFiltro( filtro)) (paso 5)` |  |
| 5 | PresentadorReservasClub | PresentadorReservasClub | `FilaReserva.desde(r)` | [PresentadorReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorReservasClub.java) |
| 6 | PresentadorReservasClub | VistaReservasClub | `mostrarReservas(filas)` | [VistaReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaReservasClub.java) |

### `sec_cu31_p2.puml` — CU-31 Administrador consulta reservas del club (parte 2: detalle de la reserva)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaReservasClub | `selecciona una fila (paso 7)` | [VistaReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaReservasClub.java) |
| 2 | VistaReservasClub | PresentadorReservasClub | `alSeleccionar( idReserva)` | [PresentadorReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorReservasClub.java) |
| 3 | PresentadorReservasClub | ServicioReservas | `consultar Reserva(idReserva)` | [ServicioReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/ServicioReservas.java) |
| 4 | ServicioReservas | FachadaPersistencia«Singleton» | `get(idReserva, Reserva)` |  |
| 5 | PresentadorReservasClub | VistaReservasClub | `mostrarDetalle(Detalle Reserva.desde(r, notificaciones, regla))` | [VistaReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaReservasClub.java) |
| 6 | PresentadorReservasClub | VistaReservasClub | `mostrarError("El elemento ya no existe.")` | [VistaReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaReservasClub.java) |

### `sec_cu31_p3.puml` — CU-31 Administrador consulta reservas del club (parte 3: estado de las notificaciones)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorReservasClub | UsuarioActual | `obtener()` | [UsuarioActual.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/UsuarioActual.java) |
| 2 | PresentadorReservasClub | ServicioNotificaciones | `consultarPorReserva( usuario, idReserva)` | [ServicioNotificaciones.java](../src/main/java/ar/edu/ub/club/notificaciones/aplicacion/ServicioNotificaciones.java) |
| 3 | ServicioNotificaciones | FachadaPersistencia«Singleton» | `buscar(Notificaciones DeReserva(idReserva))` |  |
| 4 | PresentadorReservasClub | ServicioAgenda | `consultarReglas()` | [ServicioAgenda.java](../src/main/java/ar/edu/ub/club/agenda/aplicacion/ServicioAgenda.java) |
| 5 | PresentadorReservasClub | PresentadorReservasClub | `DetalleReserva.desde(r, notificaciones, regla) (FilaNotificacion.desde(n))` | [PresentadorReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorReservasClub.java) |
| 6 | PresentadorReservasClub | VistaReservasClub | `mostrarDetalle(detalle)` | [VistaReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaReservasClub.java) |

## CU-32 Administrador modifica reserva

<!-- NOTAS:CU-32 -->

**Implementación (reservas y notificaciones) — CU-32 Modificar reserva (sec_cu32_p1, p2, p3, col_cu32)**

`VistaReservasClub` «Modificar» → `PresentadorReservasClub.alModificar` → `navegarA("reservas-club/modificar/" + id)` →
`VistaModificarReserva.alEntrar` → `PresentadorModificarReserva.cargar` → `ServicioReservas.consultarReserva` + `ServicioAgenda.consultarReglas`
→ `DetalleReserva.desde(r, List.of(), regla)` (guarda `version`) → `ServicioRecursos.listarCanchas(idDeporte)` → `mostrarReserva`.
«Consultar turnos» → `alConsultarTurnos` → `ServicioAgenda.consultarDisponibilidad` → `FilaTurno.desde(t, true)` → `mostrarTurnos`.
«Elegir» + «Guardar» → `PresentadorModificarReserva.alGuardar` → `UsuarioActual.obtenerAdministrador` → `ServicioReservas.modificarReserva`
→ `verificar` → `buscarReserva` → comparación de `version` (`ConcurrenciaException`) → `esVigente` → `get(ReglaReserva.ID_UNICO)` →
`verificarPlazo` → `buscarUno(SocioParaReserva)` → `buscarCancha` → `new SolicitudReserva(..., MODIFICACION, ADMINISTRADOR)` →
`FabricaCadenaValidacion.crearCadena` (+ `ocupadosSinOriginal`) → `validar` → `Reserva.modificar` → `EstadoReservaConfirmada.modificar` →
`Reserva.cambiarTurno` → `put` (`UPDATE ... AND version = ?`, exclusión 23P01) → `publicar(new ReservaModificadaEvento(..., turnoAnterior,
canchaAnterior))` → `navegarA("reservas-club")`; conflicto → `PresentadorModificarReserva.alConflicto` → `cargar`.

**Diagramas de secuencia del diseño:**

### `sec_cu32_p1.puml` — CU-32 Administrador modifica reserva (parte 1: carga de la reserva)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaReservasClub | `elige Modificar (paso 1)` | [VistaReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaReservasClub.java) |
| 2 | VistaReservasClub | PresentadorReservasClub | `alModificar(idReserva)` | [PresentadorReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorReservasClub.java) |
| 3 | PresentadorReservasClub | VistaReservasClub | `navegarA("reservas-club/ modificar/" + idReserva)` | [VistaReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaReservasClub.java) |
| 4 | VistaReservasClub | VistaModificarReserva | `navega a P-23` | [VistaModificarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaModificarReserva.java) |
| 5 | VistaModificarReserva | PresentadorModificarReserva | `cargar(idReserva)` | [PresentadorModificarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorModificarReserva.java) |
| 6 | PresentadorModificarReserva | ServicioReservas | `consultarReserva( idReserva)` | [ServicioReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/ServicioReservas.java) |
| 7 | ServicioReservas | FachadaPersistencia«Singleton» | `get(idReserva, Reserva)` |  |
| 8 | PresentadorModificarReserva | ServicioAgenda | `consultarReglas()` | [ServicioAgenda.java](../src/main/java/ar/edu/ub/club/agenda/aplicacion/ServicioAgenda.java) |
| 9 | PresentadorModificarReserva | PresentadorModificarReserva | `detalle = DetalleReserva. desde(r, List.of(), regla); guarda version` | [PresentadorModificarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorModificarReserva.java) |
| 10 | PresentadorModificarReserva | ServicioRecursos | `listarCanchas( detalle.idDeporte)` | [ServicioRecursos.java](../src/main/java/ar/edu/ub/club/recursos/aplicacion/ServicioRecursos.java) |
| 11 | PresentadorModificarReserva | VistaModificarReserva | `mostrarReserva(detalle, canchas)` | [VistaModificarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaModificarReserva.java) |

### `sec_cu32_p2.puml` — CU-32 Administrador modifica reserva (parte 2: de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaModificarReserva | `elige el nuevo turno y guarda` | [VistaModificarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaModificarReserva.java) |
| 2 | VistaModificarReserva | PresentadorModificarReserva | `alGuardar(nuevoTurno)` | [PresentadorModificarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorModificarReserva.java) |
| 3 | PresentadorModificarReserva | UsuarioActual | `obtenerAdministrador()` | [UsuarioActual.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/UsuarioActual.java) |
| 4 | PresentadorModificarReserva | ServicioReservas | `modificarReserva(admin, idReserva, nuevoTurno.aTurno(), nuevoTurno.idCancha, version)` | [ServicioReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/ServicioReservas.java) |
| 5 | PresentadorModificarReserva | VistaModificarReserva | `navegarA("reservas-club")` | [VistaModificarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaModificarReserva.java) |
| 6 | PresentadorModificarReserva | VistaModificarReserva | `mostrarError(motivo)` | [VistaModificarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaModificarReserva.java) |
| 7 | PresentadorModificarReserva | PresentadorModificarReserva | `alConflicto(ex)` | [PresentadorModificarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorModificarReserva.java) |
| 8 | PresentadorModificarReserva | VistaModificarReserva | `mostrarError("la reserva cambió"); cargar(idReserva)` | [VistaModificarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaModificarReserva.java) |
| 9 | PresentadorModificarReserva | VistaModificarReserva | `mostrarError(mensaje)` | [VistaModificarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaModificarReserva.java) |
| 10 | Administrador | VistaModificarReserva | `Cancelar` | [VistaModificarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaModificarReserva.java) |

### `sec_cu32_p3.puml` — CU-32 Administrador modifica reserva (parte 3: ServicioReservas.modificarReserva)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioReservas | FachadaPersistencia«Singleton» | `get(idReserva, Reserva)` |  |
| 2 | ServicioReservas | FachadaPersistencia«Singleton» | `get(idNuevaCancha, Cancha)` |  |
| 3 | ServicioReservas | ServicioReservas | `s = new Solicitud Reserva(r.socio, nuevaCancha, nuevoTurno, r, MODIFICACION, ADMINISTRADOR, ahora) (paso 6)` | [ServicioReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/ServicioReservas.java) |
| 4 | ServicioReservas | FabricaCadenaValidacion | `crearCadena(s)` | [FabricaCadenaValidacion.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/FabricaCadenaValidacion.java) |
| 5 | ServicioReservas | ValidadorReserva | `validar(s)` | [ValidadorReserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/ValidadorReserva.java) |
| 6 | ServicioReservas | ServicioReservas | `turnoAnterior = r.turno; canchaAnterior = r.cancha` | [ServicioReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/ServicioReservas.java) |
| 7 | ServicioReservas | Reserva | `modificar(nuevoTurno, nuevaCancha, ahora) (paso 14)` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |
| 8 | Reserva | EstadoReservaConfirmada | `modificar(r, nuevoTurno, nuevaCancha, ahora)` | [EstadoReservaConfirmada.java](../src/main/java/ar/edu/ub/club/reservas/dominio/EstadoReservaConfirmada.java) |
| 9 | ServicioReservas | FachadaPersistencia«Singleton» | `put(r)` |  |
| 10 | ServicioReservas | PublicadorEventosReserva | `publicar(Reserva ModificadaEvento (r, admin, ahora, turnoAnterior, canchaAnterior))` | [PublicadorEventosReserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/PublicadorEventosReserva.java) |

## CU-33 Administrador cancela reserva

<!-- NOTAS:CU-33 -->

**Implementación (reservas y notificaciones) — CU-33 Cancelar reserva administrativa (sec_cu33_p1, p2, col_cu33)**

`VistaReservasClub` «Cancelar» → `pedirMotivoCancelacion` (`DialogoConfirmacion.pedirMotivo(true)`) → `PresentadorReservasClub.alCancelar(id, motivo)`
→ `UsuarioActual.obtenerAdministrador` → `ServicioReservas.cancelarReservaAdministrativa` → `verificar` → motivo no vacío →
`buscarReserva` → `esVigente` → `Reserva.cancelar(motivo, admin, ahora)` → `EstadoReservaConfirmada.cancelar` → `put` →
`publicar(new ReservaCanceladaEvento)` → `PresentadorReservasClub.cargar`.

**Diagramas de secuencia del diseño:**

### `sec_cu33_p1.puml` — CU-33 Administrador cancela reserva (parte 1: de la vista a la fachada)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaReservasClub | `elige Cancelar (paso 1)` | [VistaReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaReservasClub.java) |
| 2 | VistaReservasClub | DialogoConfirmacion | `«create»; pedirMotivo(true); abrir() (paso 3)` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 3 | Administrador | DialogoConfirmacion | `Confirmar(motivo)` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 4 | DialogoConfirmacion | VistaReservasClub | `oyente de alConfirmar(motivo)` | [VistaReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaReservasClub.java) |
| 5 | VistaReservasClub | PresentadorReservasClub | `alCancelar(idReserva, motivo)` | [PresentadorReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorReservasClub.java) |
| 6 | PresentadorReservasClub | UsuarioActual | `obtener Administrador()` | [UsuarioActual.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/UsuarioActual.java) |
| 7 | PresentadorReservasClub | ServicioReservas | `cancelarReserva Administrativa(admin, idReserva, motivo)` | [ServicioReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/ServicioReservas.java) |
| 8 | PresentadorReservasClub | PresentadorReservasClub | `cargar(filtro)` | [PresentadorReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorReservasClub.java) |
| 9 | PresentadorReservasClub | VistaReservasClub | `mostrarReservas(filas)` | [VistaReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaReservasClub.java) |
| 10 | PresentadorReservasClub | VistaReservasClub | `mostrarError(motivo)` | [VistaReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaReservasClub.java) |
| 11 | PresentadorReservasClub | PresentadorReservasClub | `alConflicto(ex)` | [PresentadorReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorReservasClub.java) |
| 12 | PresentadorReservasClub | VistaReservasClub | `mostrarError("la reserva cambió"); cargar(filtro)` | [VistaReservasClub.java](../src/main/java/ar/edu/ub/club/reservas/ui/VistaReservasClub.java) |
| 13 | Administrador | DialogoConfirmacion | `Cancelar` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |

### `sec_cu33_p2.puml` — CU-33 Administrador cancela reserva (parte 2: ServicioReservas.cancelarReservaAdministrativa)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioReservas | ServicioReservas | `verifica motivo no vacío (paso 5)` | [ServicioReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/ServicioReservas.java) |
| 2 | ServicioReservas | FachadaPersistencia«Singleton» | `get(idReserva, Reserva)` |  |
| 3 | ServicioReservas | Reserva | `esVigente(ahora) (paso 2)` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |
| 4 | ServicioReservas | Reserva | `cancelar(motivo, admin, ahora) (paso 6)` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |
| 5 | Reserva | EstadoReservaConfirmada | `cancelar(r, motivo, admin, ahora)` | [EstadoReservaConfirmada.java](../src/main/java/ar/edu/ub/club/reservas/dominio/EstadoReservaConfirmada.java) |
| 6 | EstadoReservaConfirmada | Reserva | `cambiarEstado(Estado ReservaCancelada)` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |
| 7 | ServicioReservas | FachadaPersistencia«Singleton» | `put(r)` |  |
| 8 | ServicioReservas | PublicadorEventosReserva | `publicar(ReservaCancelada Evento(r, admin, ahora))` | [PublicadorEventosReserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/PublicadorEventosReserva.java) |

## CU-34 Administrador configura medios y pasarela de pago

<!-- NOTAS:CU-34 -->

### `sec_cu34_p1.puml` — CU-34 Configura medios y pasarela de pago\n(parte 1: consulta de la configuración, pasos 1-2)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaMediosPago | `abre P-31 (paso 1)` | [VistaMediosPago.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaMediosPago.java) |
| 2 | VistaMediosPago | PresentadorMediosPago | `cargar()` | [PresentadorMediosPago.java](../src/main/java/ar/edu/ub/club/pagos/ui/PresentadorMediosPago.java) |
| 3 | PresentadorMediosPago | ServicioPagos | `consultarMedios YPasarela()` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 4 | ServicioPagos | FachadaPersistencia«Singleton» | `buscar(MediosPago())` |  |
| 5 | ServicioPagos | FachadaPersistencia«Singleton» | `get(ConfiguracionPasarela.ID_UNICO, ConfiguracionPasarela)` |  |
| 6 | ServicioPagos | ServicioPagos | `new Configuracion Pagos(medios, c)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 7 | PresentadorMediosPago | PresentadorMediosPago | `DatosConfiguracionPagos.desde(config) ResumenPasarela.desde(config.pasarela)` | [PresentadorMediosPago.java](../src/main/java/ar/edu/ub/club/pagos/ui/PresentadorMediosPago.java) |
| 8 | PresentadorMediosPago | VistaMediosPago | `mostrarConfiguracion( datos, pasarela)` | [VistaMediosPago.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaMediosPago.java) |

### `sec_cu34_p2.puml` — CU-34 Configura medios y pasarela de pago\n(parte 2: de la vista a la fachada, pasos 3-11)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaMediosPago | `cambia medios o pasarela y elige Guardar (paso 3; FA1 agrega un medio)` | [VistaMediosPago.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaMediosPago.java) |
| 2 | VistaMediosPago | FormularioEntidad | `leer() (paso 4)` | [FormularioEntidad.java](../src/main/java/ar/edu/ub/club/compartido/ui/FormularioEntidad.java) |
| 3 | VistaMediosPago | PresentadorMediosPago | `alGuardar(datos)` | [PresentadorMediosPago.java](../src/main/java/ar/edu/ub/club/pagos/ui/PresentadorMediosPago.java) |
| 4 | PresentadorMediosPago | ServicioPagos | `configurarMedios YPasarela(datos)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 5 | PresentadorMediosPago | VistaMediosPago | `mostrarConfiguracion( datos, pasarela)` | [VistaMediosPago.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaMediosPago.java) |
| 6 | PresentadorMediosPago | VistaMediosPago | `mostrarMensaje( "Configuración guardada")` | [VistaMediosPago.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaMediosPago.java) |
| 7 | PresentadorMediosPago | VistaMediosPago | `marcarCampo( campo, mensaje)` | [VistaMediosPago.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaMediosPago.java) |
| 8 | VistaMediosPago | FormularioEntidad | `marcarError( campo, mensaje)` | [FormularioEntidad.java](../src/main/java/ar/edu/ub/club/compartido/ui/FormularioEntidad.java) |

### `sec_cu34_p3.puml` — CU-34 (parte 3:\nServicioPagos.configurarMediosYPasarela)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorMediosPago | ServicioPagos | `configurarMedios YPasarela(datos)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 2 | ServicioPagos | FachadaPersistencia«Singleton» | `contar(MedioPago PorNombre(nombre))` |  |
| 3 | ServicioPagos | FachadaPersistencia«Singleton» | `put(«create» MedioPago(nombre))` |  |
| 4 | ServicioPagos | FachadaPersistencia«Singleton» | `get(id, MedioPago)` |  |
| 5 | ServicioPagos | FachadaPersistencia«Singleton» | `put(medio) (paso 5)` |  |
| 6 | ServicioPagos | FachadaPersistencia«Singleton» | `get(ConfiguracionPasarela.ID_UNICO, ConfiguracionPasarela)` |  |
| 7 | ServicioPagos | ConfiguracionPasarela | `habilitada = false` | [ConfiguracionPasarela.java](../src/main/java/ar/edu/ub/club/pagos/dominio/ConfiguracionPasarela.java) |
| 8 | ServicioPagos | ConfiguracionPasarela | `habilitada = false` | [ConfiguracionPasarela.java](../src/main/java/ar/edu/ub/club/pagos/dominio/ConfiguracionPasarela.java) |
| 9 | ServicioPagos | PasarelaPago | `validarConfiguracion(c) (paso 7)` | [PasarelaPago.java](../src/main/java/ar/edu/ub/club/pagos/integracion/PasarelaPago.java) |
| 10 | ServicioPagos | ConfiguracionPasarela | `habilitada = resultado (paso 9; false en FA5)` | [ConfiguracionPasarela.java](../src/main/java/ar/edu/ub/club/pagos/dominio/ConfiguracionPasarela.java) |
| 11 | ServicioPagos | FachadaPersistencia«Singleton» | `put(c)` |  |
| 12 | ServicioPagos | ServicioAuditoria | `registrar(admin, "MODIFICACION", "ConfiguracionPasarela", id, detalle) (paso 10)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

## CU-35 Socio (titular) paga cuota societaria online

<!-- NOTAS:CU-35 -->

### `sec_cu35_p1.puml` — CU-35 Socio (titular) paga cuota online\n(parte 1: estado de cuenta del grupo, pasos 1-6)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Socio(titular) | VistaEstadoCuenta | `abre P-25 (paso 1)` | [VistaEstadoCuenta.java](../src/main/java/ar/edu/ub/club/cuotas/ui/VistaEstadoCuenta.java) |
| 2 | VistaEstadoCuenta | PresentadorEstadoCuenta | `cargar()` | [PresentadorEstadoCuenta.java](../src/main/java/ar/edu/ub/club/cuotas/ui/PresentadorEstadoCuenta.java) |
| 3 | PresentadorEstadoCuenta | SocioActual | `obtener()` | [SocioActual.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/SocioActual.java) |
| 4 | PresentadorEstadoCuenta | ServicioSocios | `consultarPerfil Propio(socio)` | [ServicioSocios.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/ServicioSocios.java) |
| 5 | PresentadorEstadoCuenta | ServicioCuotas | `consultarCuotas DeGrupo(idGrupo)` | [ServicioCuotas.java](../src/main/java/ar/edu/ub/club/cuotas/aplicacion/ServicioCuotas.java) |
| 6 | ServicioCuotas | FachadaPersistencia«Singleton» | `buscar(Cuotas DeGrupo(idGrupo))` |  |
| 7 | PresentadorEstadoCuenta | ServicioCuotas | `pagoOnline Habilitado()` | [ServicioCuotas.java](../src/main/java/ar/edu/ub/club/cuotas/aplicacion/ServicioCuotas.java) |
| 8 | ServicioCuotas | FachadaPersistencia«Singleton» | `get(ConfiguracionPasarela.ID_UNICO, ConfiguracionPasarela)` |  |
| 9 | PresentadorEstadoCuenta | PresentadorEstadoCuenta | `FilaCuota.desde(c, esTitular && habilitada)` | [PresentadorEstadoCuenta.java](../src/main/java/ar/edu/ub/club/cuotas/ui/PresentadorEstadoCuenta.java) |
| 10 | PresentadorEstadoCuenta | Cuota | `admitePago()` | [Cuota.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/Cuota.java) |
| 11 | PresentadorEstadoCuenta | Cuota | `importeAPagar()` | [Cuota.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/Cuota.java) |
| 12 | PresentadorEstadoCuenta | VistaEstadoCuenta | `mostrarCuotas( filas, esTitular)` | [VistaEstadoCuenta.java](../src/main/java/ar/edu/ub/club/cuotas/ui/VistaEstadoCuenta.java) |

### `sec_cu35_p2.puml` — CU-35 Socio (titular) paga cuota online\n(parte 2: inicio del pago, de la vista a la fachada, pasos 7-12)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Socio(titular) | VistaEstadoCuenta | `elige «Pagar online» en una cuota (paso 7)` | [VistaEstadoCuenta.java](../src/main/java/ar/edu/ub/club/cuotas/ui/VistaEstadoCuenta.java) |
| 2 | VistaEstadoCuenta | PresentadorEstadoCuenta | `alPagar(idCuota)` | [PresentadorEstadoCuenta.java](../src/main/java/ar/edu/ub/club/cuotas/ui/PresentadorEstadoCuenta.java) |
| 3 | PresentadorEstadoCuenta | SocioActual | `obtener()` | [SocioActual.java](../src/main/java/ar/edu/ub/club/socios/aplicacion/SocioActual.java) |
| 4 | PresentadorEstadoCuenta | ServicioPagos | `iniciarPagoOnline( titular, idCuota)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 5 | PresentadorEstadoCuenta | VistaEstadoCuenta | `redirigirA( intencion.urlCheckout)` | [VistaEstadoCuenta.java](../src/main/java/ar/edu/ub/club/cuotas/ui/VistaEstadoCuenta.java) |
| 6 | VistaEstadoCuenta | navegador | `getPage().setLocation( urlCheckout) (paso 12)` |  |
| 7 | PresentadorEstadoCuenta | VistaEstadoCuenta | `mostrarError("No tiene permiso para realizar esta operación.")` | [VistaEstadoCuenta.java](../src/main/java/ar/edu/ub/club/cuotas/ui/VistaEstadoCuenta.java) |
| 8 | PresentadorEstadoCuenta | VistaEstadoCuenta | `mostrarError(motivo)` | [VistaEstadoCuenta.java](../src/main/java/ar/edu/ub/club/cuotas/ui/VistaEstadoCuenta.java) |
| 9 | PresentadorEstadoCuenta | PresentadorEstadoCuenta | `cargar()` | [PresentadorEstadoCuenta.java](../src/main/java/ar/edu/ub/club/cuotas/ui/PresentadorEstadoCuenta.java) |

### `sec_cu35_p3.puml` — CU-35 (parte 3: ServicioPagos.iniciarPagoOnline, pasos 8-11)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorEstadoCuenta | ServicioPagos | `iniciarPagoOnline( titular, idCuota)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 2 | ServicioPagos | FachadaPersistencia«Singleton» | `get(ConfiguracionPasarela.ID_UNICO, ConfiguracionPasarela)` |  |
| 3 | ServicioPagos | FachadaPersistencia«Singleton» | `get(idCuota, Cuota)` |  |
| 4 | ServicioPagos | ServicioPagos | `verifica que titular sea el titular del grupo de c (paso 8, RN-09, RN-20)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 5 | ServicioPagos | Cuota | `admitePago()` | [Cuota.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/Cuota.java) |
| 6 | ServicioPagos | ProcesadorPagoOnline | `registrar(c, datos) (paso 9)` | [ProcesadorPagoOnline.java](../src/main/java/ar/edu/ub/club/pagos/dominio/ProcesadorPagoOnline.java) |
| 7 | ServicioPagos | PasarelaPago | `crearIntencionPago( SolicitudPagoOnline(ref, concepto, importe, urlRetorno = /pagos/retorno ?referencia=ref, vence = ahora + 24 h)) (paso 10)` | [PasarelaPago.java](../src/main/java/ar/edu/ub/club/pagos/integracion/PasarelaPago.java) |
| 8 | ServicioPagos | PagoOnline | `rechazar("falla al crear la intención")` | [PagoOnline.java](../src/main/java/ar/edu/ub/club/pagos/dominio/PagoOnline.java) |

### `sec_cu35_p4.puml` — CU-35 (parte 4: ProcesadorPago.registrar con Factory Method, paso 9)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioPagos | ProcesadorPagoOnline | `registrar(c, datos)` | [ProcesadorPagoOnline.java](../src/main/java/ar/edu/ub/club/pagos/dominio/ProcesadorPagoOnline.java) |
| 2 | ProcesadorPagoOnline | Cuota | `admitePago()` | [Cuota.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/Cuota.java) |
| 3 | ProcesadorPagoOnline | ProcesadorPagoOnline | `crearPago(c, datos) (método fábrica redefinido)` | [ProcesadorPagoOnline.java](../src/main/java/ar/edu/ub/club/pagos/dominio/ProcesadorPagoOnline.java) |
| 4 | ProcesadorPagoOnline | Cuota | `importeAPagar()` | [Cuota.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/Cuota.java) |
| 5 | ProcesadorPagoOnline | PagoOnline | `«create» PagoOnline(c, importe, referenciaExterna: UUID nuevo, fecha: ahora)` | [PagoOnline.java](../src/main/java/ar/edu/ub/club/pagos/dominio/PagoOnline.java) |
| 6 | ProcesadorPagoOnline | FachadaPersistencia«Singleton» | `put(p)` |  |

### `sec_cu35_p5.puml` — CU-35 (parte 5: pago en el simulador P-27 y webhook,\nmodo SIMULADA, pasos 13-14)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Socio(titular) | VistaSimuladorPasarela | `llega a P-27 desde urlCheckout (simulador-pasarela/{idPreferencia})` | [VistaSimuladorPasarela.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaSimuladorPasarela.java) |
| 2 | VistaSimuladorPasarela | PresentadorSimuladorPasarela | `cargar(idPreferencia)` | [PresentadorSimuladorPasarela.java](../src/main/java/ar/edu/ub/club/pagos/ui/PresentadorSimuladorPasarela.java) |
| 3 | PresentadorSimuladorPasarela | PasarelaSimuladaAdapter | `obtenerIntencion( idPreferencia)` | [PasarelaSimuladaAdapter.java](../src/main/java/ar/edu/ub/club/pagos/integracion/PasarelaSimuladaAdapter.java) |
| 4 | PresentadorSimuladorPasarela | VistaSimuladorPasarela | `mostrarIntencion( ResumenIntencion Simulada.desde(i))` | [VistaSimuladorPasarela.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaSimuladorPasarela.java) |
| 5 | Socio(titular) | VistaSimuladorPasarela | `elige Aprobar o Rechazar (paso 13)` | [VistaSimuladorPasarela.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaSimuladorPasarela.java) |
| 6 | VistaSimuladorPasarela | PresentadorSimuladorPasarela | `alResolver(aprobar)` | [PresentadorSimuladorPasarela.java](../src/main/java/ar/edu/ub/club/pagos/ui/PresentadorSimuladorPasarela.java) |
| 7 | PresentadorSimuladorPasarela | PasarelaSimuladaAdapter | `resolverPago( idPreferencia, aprobar)` | [PasarelaSimuladaAdapter.java](../src/main/java/ar/edu/ub/club/pagos/integracion/PasarelaSimuladaAdapter.java) |
| 8 | PasarelaSimuladaAdapter | PasarelaSimuladaAdapter | `registra el resultado y genera idPagoPasarela` | [PasarelaSimuladaAdapter.java](../src/main/java/ar/edu/ub/club/pagos/integracion/PasarelaSimuladaAdapter.java) |
| 9 | PasarelaSimuladaAdapter | WebhookPasarelaController | `POST /api/pasarela/ notificaciones (paso 14)` | [WebhookPasarelaController.java](../src/main/java/ar/edu/ub/club/pagos/web/WebhookPasarelaController.java) |
| 10 | WebhookPasarelaController | ServicioPagos | `confirmarResultado Pasarela( idPagoPasarela)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 11 | PresentadorSimuladorPasarela | VistaSimuladorPasarela | `redirigirA(urlRetorno)` | [VistaSimuladorPasarela.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaSimuladorPasarela.java) |

### `sec_cu35_p6.puml` — CU-35 (parte 6: pago en Mercado Pago, webhook y abandono,\npasos 13-14 y FA7)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Pasareladepago(AC-03) | WebhookPasarelaController | `POST /api/pasarela/ notificaciones (NotificacionPasarela Request) (paso 14)` | [WebhookPasarelaController.java](../src/main/java/ar/edu/ub/club/pagos/web/WebhookPasarelaController.java) |
| 2 | WebhookPasarelaController | ServicioPagos | `confirmarResultado Pasarela( idPagoPasarela)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 3 | PlanificadorPagos | ServicioPagos | `vencerPagosPendientes( ahora) (cada hora)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |

### `sec_cu35_p7.puml` — CU-35 (parte 7: ServicioPagos.confirmarResultadoPasarela,\npasos 15-17, FA9 a FA11)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | WebhookPasarelaController | ServicioPagos | `confirmarResultado Pasarela( idPagoPasarela)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 2 | ServicioPagos | PasarelaPago | `obtenerResultado( idPagoPasarela) (paso 15)` | [PasarelaPago.java](../src/main/java/ar/edu/ub/club/pagos/integracion/PasarelaPago.java) |
| 3 | ServicioPagos | FachadaPersistencia«Singleton» | `buscarUno(PagoPor ReferenciaExterna( referencia)) (FOR UPDATE)` |  |
| 4 | ServicioPagos | ServicioAuditoria | `registrar(null, "ANOMALIA_PAGO", "Pago", null, detalle)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

### `sec_cu35_p8.puml` — CU-35 (parte 8: aplicación del resultado: State del pago,\ncomprobante y saldo a favor, pasos 18-22)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioPagos | PagoOnline | `registrarRespuesta Pasarela(resultado)` | [PagoOnline.java](../src/main/java/ar/edu/ub/club/pagos/dominio/PagoOnline.java) |
| 2 | ServicioPagos | Cuota | `admitePago() (paso 18)` | [Cuota.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/Cuota.java) |
| 3 | ServicioPagos | PagoOnline | `aprobar(ahora) (paso 19)` | [PagoOnline.java](../src/main/java/ar/edu/ub/club/pagos/dominio/PagoOnline.java) |
| 4 | PagoOnline | EstadoPago(PendienteoVencido) | `aprobar(p, ahora)` |  |
| 5 | EstadoPago(PendienteoVencido) | PagoOnline | `cambiarEstado( EstadoPagoAprobado)` | [PagoOnline.java](../src/main/java/ar/edu/ub/club/pagos/dominio/PagoOnline.java) |
| 6 | ServicioPagos | Cuota | `registrarPagoAprobado(p) (paso 20)` | [Cuota.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/Cuota.java) |
| 7 | ServicioPagos | GeneradorComprobante | `generar(p) (paso 21)` | [GeneradorComprobante.java](../src/main/java/ar/edu/ub/club/pagos/dominio/GeneradorComprobante.java) |
| 8 | ServicioPagos | ServicioAuditoria | `registrar(null, "PAGO_APROBADO", "Pago", id, detalle) (paso 22)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |
| 9 | ServicioPagos | PagoOnline | `acreditar(ahora) (18.1)` | [PagoOnline.java](../src/main/java/ar/edu/ub/club/pagos/dominio/PagoOnline.java) |
| 10 | PagoOnline | EstadoPago(PendienteoVencido) | `acreditar(p, ahora)` |  |
| 11 | EstadoPago(PendienteoVencido) | PagoOnline | `cambiarEstado( EstadoPagoAcreditado)` | [PagoOnline.java](../src/main/java/ar/edu/ub/club/pagos/dominio/PagoOnline.java) |
| 12 | ServicioPagos | GeneradorComprobante | `generar(p) (18.2)` | [GeneradorComprobante.java](../src/main/java/ar/edu/ub/club/pagos/dominio/GeneradorComprobante.java) |
| 13 | ServicioPagos | ServicioCuotas | `registrarCredito(idGrupo de c, p.importe, p.id) (18.3, RN-21)` | [ServicioCuotas.java](../src/main/java/ar/edu/ub/club/cuotas/aplicacion/ServicioCuotas.java) |
| 14 | ServicioPagos | ServicioAuditoria | `registrar(null, "PAGO_ACREDITADO", "Pago", id, detalle) (18.4)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |
| 15 | ServicioPagos | PagoOnline | `rechazar(motivo de la pasarela) (16.1)` | [PagoOnline.java](../src/main/java/ar/edu/ub/club/pagos/dominio/PagoOnline.java) |
| 16 | PagoOnline | EstadoPago(PendienteoVencido) | `rechazar(p, motivo)` |  |
| 17 | EstadoPago(PendienteoVencido) | PagoOnline | `cambiarEstado( EstadoPagoRechazado)` | [PagoOnline.java](../src/main/java/ar/edu/ub/club/pagos/dominio/PagoOnline.java) |

### `sec_cu35_p9.puml` — CU-35 (parte 9: retorno a P-26 y consulta del resultado,\npasos 23-24, 16.3-16.4, 18.5-18.6 y FA13)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Socio(titular) | navegador | `vuelve a urlRetorno (paso 23)` |  |
| 2 | navegador | RetornoPasarelaController | `GET /pagos/retorno ?referencia={uuid}` | [RetornoPasarelaController.java](../src/main/java/ar/edu/ub/club/pagos/web/RetornoPasarelaController.java) |
| 3 | RetornoPasarelaController | RetornoPasarelaController | `retornar( referencia)` | [RetornoPasarelaController.java](../src/main/java/ar/edu/ub/club/pagos/web/RetornoPasarelaController.java) |
| 4 | navegador | VistaResultadoPago | `/pagos/resultado ?referencia={uuid}` | [VistaResultadoPago.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaResultadoPago.java) |
| 5 | VistaResultadoPago | PresentadorResultadoPago | `alEntrar(referencia)` | [PresentadorResultadoPago.java](../src/main/java/ar/edu/ub/club/pagos/ui/PresentadorResultadoPago.java) |
| 6 | PresentadorResultadoPago | UsuarioActual | `obtener()` | [UsuarioActual.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/UsuarioActual.java) |
| 7 | PresentadorResultadoPago | ServicioPagos | `consultarPagoOnline( usuario, referencia)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 8 | ServicioPagos | FachadaPersistencia«Singleton» | `buscarUno(PagoPor ReferenciaExterna( referencia))` |  |
| 9 | ServicioPagos | ServicioPagos | `p del grupo del usuario (RN-20)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 10 | PresentadorResultadoPago | VistaResultadoPago | `mostrarEstado( ResumenPagoOnline .desde(p))` | [VistaResultadoPago.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaResultadoPago.java) |
| 11 | PresentadorResultadoPago | PresentadorResultadoPago | `planificador .schedule(this:: consultar, 3 s)` | [PresentadorResultadoPago.java](../src/main/java/ar/edu/ub/club/pagos/ui/PresentadorResultadoPago.java) |
| 12 | PresentadorResultadoPago | VistaResultadoPago | `mostrarEspera Confirmacion()` | [VistaResultadoPago.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaResultadoPago.java) |

## CU-36 Administrador registra pago manual

<!-- NOTAS:CU-36 -->

### `sec_cu36_p1.puml` — CU-36 Administrador registra pago manual\n(parte 1: medios, búsqueda del grupo y cuotas impagas, pasos 1-4)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaPagoManual | `abre P-30` | [VistaPagoManual.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaPagoManual.java) |
| 2 | VistaPagoManual | PresentadorPagoManual | `cargar()` | [PresentadorPagoManual.java](../src/main/java/ar/edu/ub/club/pagos/ui/PresentadorPagoManual.java) |
| 3 | PresentadorPagoManual | ServicioPagos | `consultarMedios YPasarela()` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 4 | PresentadorPagoManual | VistaPagoManual | `mostrarMedios(opciones) (solo medios habilitados)` | [VistaPagoManual.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaPagoManual.java) |
| 5 | Administrador | VistaPagoManual | `ingresa DNI o nombre (paso 1)` | [VistaPagoManual.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaPagoManual.java) |
| 6 | VistaPagoManual | PresentadorPagoManual | `alBuscarGrupo(texto)` | [PresentadorPagoManual.java](../src/main/java/ar/edu/ub/club/pagos/ui/PresentadorPagoManual.java) |
| 7 | PresentadorPagoManual | ServicioGruposFamiliares | `buscarGrupos(new Criterio BusquedaGrupo(texto, true))` | [ServicioGruposFamiliares.java](../src/main/java/ar/edu/ub/club/grupos/aplicacion/ServicioGruposFamiliares.java) |
| 8 | PresentadorPagoManual | VistaPagoManual | `mostrarGrupos(filas) (FilaGrupo.desde(r))` | [VistaPagoManual.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaPagoManual.java) |
| 9 | Administrador | VistaPagoManual | `elige un grupo (paso 3)` | [VistaPagoManual.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaPagoManual.java) |
| 10 | VistaPagoManual | PresentadorPagoManual | `alElegirGrupo(idGrupo)` | [PresentadorPagoManual.java](../src/main/java/ar/edu/ub/club/pagos/ui/PresentadorPagoManual.java) |
| 11 | PresentadorPagoManual | ServicioCuotas | `consultarCuotas DeGrupo(idGrupo)` | [ServicioCuotas.java](../src/main/java/ar/edu/ub/club/cuotas/aplicacion/ServicioCuotas.java) |
| 12 | PresentadorPagoManual | VistaPagoManual | `mostrarCuotas(filas) (FilaCuota.desde(c, false), solo impagas)` | [VistaPagoManual.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaPagoManual.java) |

### `sec_cu36_p2.puml` — CU-36 Administrador registra pago manual\n(parte 2: de la vista a la fachada, pasos 5-14)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaPagoManual | `elige cuota y medio; el importe es el importe a pagar, no editable (paso 5)` | [VistaPagoManual.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaPagoManual.java) |
| 2 | VistaPagoManual | DialogoConfirmacion | `abrir()` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 3 | Administrador | DialogoConfirmacion | `Confirmar` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |
| 4 | DialogoConfirmacion | VistaPagoManual | `alConfirmar` | [VistaPagoManual.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaPagoManual.java) |
| 5 | VistaPagoManual | PresentadorPagoManual | `alRegistrar(idCuota, idMedio, importe)` | [PresentadorPagoManual.java](../src/main/java/ar/edu/ub/club/pagos/ui/PresentadorPagoManual.java) |
| 6 | PresentadorPagoManual | UsuarioActual | `obtener Administrador()` | [UsuarioActual.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/UsuarioActual.java) |
| 7 | PresentadorPagoManual | ServicioPagos | `registrarPagoManual( admin, idCuota, idMedio, importe)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 8 | PresentadorPagoManual | VistaPagoManual | `navegarA("pagos/comprobante/" + idComprobante) (paso 14)` | [VistaPagoManual.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaPagoManual.java) |
| 9 | PresentadorPagoManual | VistaPagoManual | `mostrarError(motivo)` | [VistaPagoManual.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaPagoManual.java) |
| 10 | PresentadorPagoManual | PresentadorPagoManual | `alElegirGrupo(idGrupo)` | [PresentadorPagoManual.java](../src/main/java/ar/edu/ub/club/pagos/ui/PresentadorPagoManual.java) |
| 11 | PresentadorPagoManual | VistaPagoManual | `mostrarError(mensaje)` | [VistaPagoManual.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaPagoManual.java) |
| 12 | Administrador | DialogoConfirmacion | `Cancelar` | [DialogoConfirmacion.java](../src/main/java/ar/edu/ub/club/compartido/ui/DialogoConfirmacion.java) |

### `sec_cu36_p3.puml` — CU-36 (parte 3: ServicioPagos.registrarPagoManual, pasos 6-13)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorPagoManual | ServicioPagos | `registrarPagoManual( admin, idCuota, idMedio, importe)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 2 | ServicioPagos | FachadaPersistencia«Singleton» | `get(idMedio, MedioPago)` |  |
| 3 | ServicioPagos | FachadaPersistencia«Singleton» | `get(idCuota, Cuota)` |  |
| 4 | ServicioPagos | Cuota | `importeAPagar()` | [Cuota.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/Cuota.java) |
| 5 | ServicioPagos | Cuota | `admitePago() (paso 8)` | [Cuota.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/Cuota.java) |
| 6 | ServicioPagos | ProcesadorPagoManual | `registrar(c, datos(medio, admin, importe, ahora)) (paso 9)` | [ProcesadorPagoManual.java](../src/main/java/ar/edu/ub/club/pagos/dominio/ProcesadorPagoManual.java) |
| 7 | ServicioPagos | Cuota | `registrarPagoAprobado(p) (paso 10)` | [Cuota.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/Cuota.java) |
| 8 | ServicioPagos | GeneradorComprobante | `generar(p) (paso 11)` | [GeneradorComprobante.java](../src/main/java/ar/edu/ub/club/pagos/dominio/GeneradorComprobante.java) |
| 9 | ServicioPagos | ServicioAuditoria | `registrar(admin, "PAGO", "Pago", id, detalle) (paso 13)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

## CU-37 Socio o Administrador consulta pagos y comprobantes

<!-- NOTAS:CU-37 -->

### `sec_cu37_p1.puml` — CU-37 Consulta pagos y comprobantes\n(parte 1: listado de pagos, pasos 1-4 y 6-7)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | SociooAdministrador | VistaPagosComprobantes | `abre P-28 (1) o filtra (6)` | [VistaPagosComprobantes.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaPagosComprobantes.java) |
| 2 | VistaPagosComprobantes | FiltroFechas | `valor()` | [FiltroFechas.java](../src/main/java/ar/edu/ub/club/compartido/ui/FiltroFechas.java) |
| 3 | VistaPagosComprobantes | PresentadorPagosComprobantes | `cargar(filtro)` | [PresentadorPagosComprobantes.java](../src/main/java/ar/edu/ub/club/pagos/ui/PresentadorPagosComprobantes.java) |
| 4 | PresentadorPagosComprobantes | UsuarioActual | `obtener()` | [UsuarioActual.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/UsuarioActual.java) |
| 5 | PresentadorPagosComprobantes | ServicioPagos | `consultarPagos( usuario, filtro)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 6 | ServicioPagos | ServicioPagos | `alcance según usuario.obtener Permisos() (paso 2)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 7 | ServicioPagos | FachadaPersistencia«Singleton» | `buscar(PagosPor Filtro(filtro))` |  |
| 8 | ServicioPagos | FachadaPersistencia«Singleton» | `buscarUno(GrupoVigente DeSocio(idSocio))` |  |
| 9 | ServicioPagos | FachadaPersistencia«Singleton» | `buscar(PagosDeGrupo( grupo.id, filtro))` |  |
| 10 | PresentadorPagosComprobantes | VistaPagosComprobantes | `mostrarPagos(filas) (FilaPago.desde(p): origen() y descripcionMedio())` | [VistaPagosComprobantes.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaPagosComprobantes.java) |

### `sec_cu37_p2.puml` — CU-37 (parte 2: saldo a favor del grupo, paso 5)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorPagosComprobantes | PresentadorPagosComprobantes | `cargarSaldo(idGrupo)` | [PresentadorPagosComprobantes.java](../src/main/java/ar/edu/ub/club/pagos/ui/PresentadorPagosComprobantes.java) |
| 2 | PresentadorPagosComprobantes | ServicioCuotas | `consultarSaldo( idGrupo)` | [ServicioCuotas.java](../src/main/java/ar/edu/ub/club/cuotas/aplicacion/ServicioCuotas.java) |
| 3 | ServicioCuotas | FachadaPersistencia«Singleton» | `get(idGrupo, GrupoFamiliar)` |  |
| 4 | ServicioCuotas | GrupoFamiliar | `saldoAFavor()` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 5 | ServicioCuotas | FachadaPersistencia«Singleton» | `buscar(Movimientos DeGrupo(idGrupo))` |  |
| 6 | ServicioCuotas | ServicioCuotas | `new Saldo Grupo(saldo, movimientos)` | [ServicioCuotas.java](../src/main/java/ar/edu/ub/club/cuotas/aplicacion/ServicioCuotas.java) |
| 7 | PresentadorPagosComprobantes | VistaPagosComprobantes | `mostrarSaldo( ResumenSaldo .desde(saldo))` | [VistaPagosComprobantes.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaPagosComprobantes.java) |

### `sec_cu37_p3.puml` — CU-37 (parte 3: comprobante o constancia de acreditación,\npasos 8-11)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | SociooAdministrador | VistaPagosComprobantes | `Ver comprobante (paso 8)` | [VistaPagosComprobantes.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaPagosComprobantes.java) |
| 2 | VistaPagosComprobantes | VistaComprobante | `abre P-29: el presentador de P-28 atiende alVer Comprobante(id) con navegarA("pagos/ comprobante/" + id)` | [VistaComprobante.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaComprobante.java) |
| 3 | VistaComprobante | PresentadorComprobante | `cargar(id)` | [PresentadorComprobante.java](../src/main/java/ar/edu/ub/club/pagos/ui/PresentadorComprobante.java) |
| 4 | PresentadorComprobante | UsuarioActual | `obtener()` | [UsuarioActual.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/UsuarioActual.java) |
| 5 | PresentadorComprobante | ServicioPagos | `obtenerComprobante( usuario, id)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 6 | ServicioPagos | FachadaPersistencia«Singleton» | `get(id, Comprobante)` |  |
| 7 | ServicioPagos | ServicioPagos | `verifica el alcance (paso 9, RN-20)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 8 | PresentadorComprobante | ServicioConfiguracionClub | `consultar()` | [ServicioConfiguracionClub.java](../src/main/java/ar/edu/ub/club/configuracion/aplicacion/ServicioConfiguracionClub.java) |
| 9 | PresentadorComprobante | VistaComprobante | `mostrarComprobante( DetalleComprobante .desde(comprobante, club))` | [VistaComprobante.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaComprobante.java) |
| 10 | PresentadorComprobante | VistaComprobante | `mostrarError("No tiene permiso para realizar esta operación.")` | [VistaComprobante.java](../src/main/java/ar/edu/ub/club/pagos/ui/VistaComprobante.java) |

## CU-38 Administrador genera reporte de uso de canchas

<!-- NOTAS:CU-38 -->

### `sec_cu38_p1.puml` — CU-38 Administrador genera reporte de uso de canchas\n(parte 1: generación, de la vista a la fachada, pasos 1-7)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaReporteUsoCanchas | `abre P-32 (paso 1)` | [VistaReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/ui/VistaReporteUsoCanchas.java) |
| 2 | VistaReporteUsoCanchas | PresentadorReporteUsoCanchas | `cargar()` | [PresentadorReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/ui/PresentadorReporteUsoCanchas.java) |
| 3 | PresentadorReporteUsoCanchas | ServicioRecursos | `listarDeportes(), listarCanchas(null)` | [ServicioRecursos.java](../src/main/java/ar/edu/ub/club/recursos/aplicacion/ServicioRecursos.java) |
| 4 | PresentadorReporteUsoCanchas | VistaReporteUsoCanchas | `mostrarFiltros( deportes, canchas)` | [VistaReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/ui/VistaReporteUsoCanchas.java) |
| 5 | Administrador | VistaReporteUsoCanchas | `elige período, deporte y cancha y Generar (paso 3)` | [VistaReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/ui/VistaReporteUsoCanchas.java) |
| 6 | VistaReporteUsoCanchas | FiltroFechas | `valor() (paso 4)` | [FiltroFechas.java](../src/main/java/ar/edu/ub/club/compartido/ui/FiltroFechas.java) |
| 7 | VistaReporteUsoCanchas | PresentadorReporteUsoCanchas | `alGenerar(filtro)` | [PresentadorReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/ui/PresentadorReporteUsoCanchas.java) |
| 8 | PresentadorReporteUsoCanchas | ServicioReportes | `generarReporte( USO_CANCHAS, filtro)` | [ServicioReportes.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ServicioReportes.java) |
| 9 | PresentadorReporteUsoCanchas | PresentadorReporteUsoCanchas | `TablaReporte.desde( resultado)` | [PresentadorReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/ui/PresentadorReporteUsoCanchas.java) |
| 10 | PresentadorReporteUsoCanchas | VistaReporteUsoCanchas | `mostrarResultado(tabla)` | [VistaReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/ui/VistaReporteUsoCanchas.java) |
| 11 | PresentadorReporteUsoCanchas | VistaReporteUsoCanchas | `mostrarError(mensaje)` | [VistaReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/ui/VistaReporteUsoCanchas.java) |

### `sec_cu38_p2.puml` — CU-38 Administrador genera reporte de uso de canchas\n(parte 2: exportación, de la vista a la fachada, pasos 8-10)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaReporteUsoCanchas | `elige Exportar en EXCEL o PDF (paso 8)` | [VistaReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/ui/VistaReporteUsoCanchas.java) |
| 2 | VistaReporteUsoCanchas | PresentadorReporteUsoCanchas | `alExportar(formato)` | [PresentadorReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/ui/PresentadorReporteUsoCanchas.java) |
| 3 | PresentadorReporteUsoCanchas | ServicioReportes | `exportarReporte(USO_CANCHAS, ultimoFiltro, formato)` | [ServicioReportes.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ServicioReportes.java) |
| 4 | PresentadorReporteUsoCanchas | VistaReporteUsoCanchas | `descargar(nombre, tipoContenido, contenido)` | [VistaReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/ui/VistaReporteUsoCanchas.java) |
| 5 | VistaReporteUsoCanchas | VistaReporteUsoCanchas | `new StreamResource(nombre, () -> new ByteArrayInputStream( contenido)) y Anchor de descarga` | [VistaReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/ui/VistaReporteUsoCanchas.java) |
| 6 | VistaReporteUsoCanchas | navegador | `activa la descarga del archivo (paso 10)` |  |
| 7 | PresentadorReporteUsoCanchas | PresentadorReporteUsoCanchas | `ejecutar: registra el idError` | [PresentadorReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/ui/PresentadorReporteUsoCanchas.java) |
| 8 | PresentadorReporteUsoCanchas | VistaReporteUsoCanchas | `mostrarError(mensaje) (genérico con código de error, RNF-16)` | [VistaReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/ui/VistaReporteUsoCanchas.java) |

### `sec_cu38_p3.puml` — CU-38 (parte 3: ServicioReportes.generarReporte\ncon Template Method, pasos 4-7)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorReporteUsoCanchas | ServicioReportes | `generarReporte( USO_CANCHAS, filtro)` | [ServicioReportes.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ServicioReportes.java) |
| 2 | ServicioReportes | ServicioReportes | `r = reportes.get(USO_CANCHAS)` | [ServicioReportes.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ServicioReportes.java) |
| 3 | ServicioReportes | ReporteUsoCanchas | `generar(filtro)` | [ReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ReporteUsoCanchas.java) |
| 4 | ReporteUsoCanchas | ReporteUsoCanchas | `validarFiltro(filtro) (paso 4)` | [ReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ReporteUsoCanchas.java) |
| 5 | ReporteUsoCanchas | ReporteUsoCanchas | `obtenerDatos(filtro)` | [ReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ReporteUsoCanchas.java) |
| 6 | ReporteUsoCanchas | ConsultasReportes | `usoCanchas(filtro)` | [ConsultasReportes.java](../src/main/java/ar/edu/ub/club/reportes/persistencia/ConsultasReportes.java) |
| 7 | ReporteUsoCanchas | ReporteUsoCanchas | `calcularTotales(filas) (paso 6: reservas, horas y % de ocupación sobre las horas de atención)` | [ReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ReporteUsoCanchas.java) |
| 8 | ReporteUsoCanchas | ReporteUsoCanchas | `armarResultado(filas, totales) (paso 7; totales en cero si no hay filas, FA2)` | [ReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ReporteUsoCanchas.java) |

### `sec_cu38_p4.puml` — CU-38 (parte 4: ServicioReportes.exportarReporte\ncon Strategy de exportación, paso 9)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorReporteUsoCanchas | ServicioReportes | `exportarReporte( USO_CANCHAS, ultimoFiltro, formato)` | [ServicioReportes.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ServicioReportes.java) |
| 2 | ServicioReportes | ReporteUsoCanchas | `generar(filtro)` | [ReporteUsoCanchas.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ReporteUsoCanchas.java) |
| 3 | ServicioReportes | ServicioReportes | `e = exportadores.get( formato)` | [ServicioReportes.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ServicioReportes.java) |
| 4 | ServicioReportes | ExportadorReporte | `exportar(resultado) (paso 9)` | [ExportadorReporte.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ExportadorReporte.java) |
| 5 | ExportadorReporte | ExportadorReporte | `arma el libro .xlsx (Apache POI)` | [ExportadorReporte.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ExportadorReporte.java) |
| 6 | ExportadorReporte | ExportadorReporte | `arma el documento .pdf (OpenPDF)` | [ExportadorReporte.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ExportadorReporte.java) |

## CU-39 Administrador genera reporte de cuotas y deudores

<!-- NOTAS:CU-39 -->

### `sec_cu39_p1.puml` — CU-39 Administrador genera reporte de cuotas y deudores\n(parte 1: de la vista a la fachada, pasos 1-11)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaReporteCuotas | `abre P-33 (paso 1)` | [VistaReporteCuotas.java](../src/main/java/ar/edu/ub/club/reportes/ui/VistaReporteCuotas.java) |
| 2 | Administrador | VistaReporteCuotas | `elige el período y Generar (paso 3)` | [VistaReporteCuotas.java](../src/main/java/ar/edu/ub/club/reportes/ui/VistaReporteCuotas.java) |
| 3 | VistaReporteCuotas | FiltroFechas | `valor() (paso 4)` | [FiltroFechas.java](../src/main/java/ar/edu/ub/club/compartido/ui/FiltroFechas.java) |
| 4 | VistaReporteCuotas | PresentadorReporteCuotas | `alGenerar(filtro)` | [PresentadorReporteCuotas.java](../src/main/java/ar/edu/ub/club/reportes/ui/PresentadorReporteCuotas.java) |
| 5 | PresentadorReporteCuotas | ServicioReportes | `generarReporte( CUOTAS_DEUDORES, filtro)` | [ServicioReportes.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ServicioReportes.java) |
| 6 | PresentadorReporteCuotas | PresentadorReporteCuotas | `TablaReporte.desde(resultado)` | [PresentadorReporteCuotas.java](../src/main/java/ar/edu/ub/club/reportes/ui/PresentadorReporteCuotas.java) |
| 7 | PresentadorReporteCuotas | VistaReporteCuotas | `mostrarResultado(tabla)` | [VistaReporteCuotas.java](../src/main/java/ar/edu/ub/club/reportes/ui/VistaReporteCuotas.java) |
| 8 | PresentadorReporteCuotas | VistaReporteCuotas | `mostrarError(mensaje)` | [VistaReporteCuotas.java](../src/main/java/ar/edu/ub/club/reportes/ui/VistaReporteCuotas.java) |
| 9 | Administrador | VistaReporteCuotas | `elige Exportar` | [VistaReporteCuotas.java](../src/main/java/ar/edu/ub/club/reportes/ui/VistaReporteCuotas.java) |

### `sec_cu39_p2.puml` — CU-39 (parte 2: ServicioReportes.generarReporte\ncon Template Method, pasos 4-8)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorReporteCuotas | ServicioReportes | `generarReporte( CUOTAS_DEUDORES, filtro)` | [ServicioReportes.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ServicioReportes.java) |
| 2 | ServicioReportes | ServicioReportes | `r = reportes.get(CUOTAS_DEUDORES)` | [ServicioReportes.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ServicioReportes.java) |
| 3 | ServicioReportes | ReporteCuotasDeudores | `generar(filtro)` | [ReporteCuotasDeudores.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ReporteCuotasDeudores.java) |
| 4 | ReporteCuotasDeudores | ReporteCuotasDeudores | `validarFiltro(filtro) (paso 4)` | [ReporteCuotasDeudores.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ReporteCuotasDeudores.java) |
| 5 | ReporteCuotasDeudores | ReporteCuotasDeudores | `obtenerDatos(filtro)` | [ReporteCuotasDeudores.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ReporteCuotasDeudores.java) |
| 6 | ReporteCuotasDeudores | ConsultasReportes | `cuotasYDeudores( filtro)` | [ConsultasReportes.java](../src/main/java/ar/edu/ub/club/reportes/persistencia/ConsultasReportes.java) |
| 7 | ReporteCuotasDeudores | ReporteCuotasDeudores | `calcularTotales(filas) (paso 6: cantidad e importe de cuotas cobradas, pendientes y vencidas)` | [ReporteCuotasDeudores.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ReporteCuotasDeudores.java) |
| 8 | ReporteCuotasDeudores | ReporteCuotasDeudores | `armarResultado(filas, totales) (paso 7: grupos deudores con titular, cuotas adeudadas y deuda; vacío en FA2)` | [ReporteCuotasDeudores.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ReporteCuotasDeudores.java) |

## CU-40 Administrador consulta panel de administración

<!-- NOTAS:CU-40 -->

### `sec_cu40.puml` — CU-40 Administrador consulta panel de administración\n(ServicioDashboard.obtenerIndicadores)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaPanelAdministracion | `ingresa a P-04 (paso 1)` | [VistaPanelAdministracion.java](../src/main/java/ar/edu/ub/club/reportes/ui/VistaPanelAdministracion.java) |
| 2 | VistaPanelAdministracion | PresentadorPanelAdministracion | `cargar()` | [PresentadorPanelAdministracion.java](../src/main/java/ar/edu/ub/club/reportes/ui/PresentadorPanelAdministracion.java) |
| 3 | PresentadorPanelAdministracion | Reloj | `hoy()` | [Reloj.java](../src/main/java/ar/edu/ub/club/compartido/dominio/Reloj.java) |
| 4 | PresentadorPanelAdministracion | ServicioDashboard | `obtenerIndicadores( hoy)` | [ServicioDashboard.java](../src/main/java/ar/edu/ub/club/reportes/aplicacion/ServicioDashboard.java) |
| 5 | ServicioDashboard | ConsultasReportes | `indicadores(hoy)` | [ConsultasReportes.java](../src/main/java/ar/edu/ub/club/reportes/persistencia/ConsultasReportes.java) |
| 6 | PresentadorPanelAdministracion | PresentadorPanelAdministracion | `ResumenIndicadores .desde(indicadores)` | [PresentadorPanelAdministracion.java](../src/main/java/ar/edu/ub/club/reportes/ui/PresentadorPanelAdministracion.java) |
| 7 | PresentadorPanelAdministracion | VistaPanelAdministracion | `mostrarIndicadores( resumen)` | [VistaPanelAdministracion.java](../src/main/java/ar/edu/ub/club/reportes/ui/VistaPanelAdministracion.java) |
| 8 | Administrador | VistaPanelAdministracion | `selecciona un indicador (paso 9)` | [VistaPanelAdministracion.java](../src/main/java/ar/edu/ub/club/reportes/ui/VistaPanelAdministracion.java) |
| 9 | VistaPanelAdministracion | PresentadorPanelAdministracion | `alIrAReporte(tipo)` | [PresentadorPanelAdministracion.java](../src/main/java/ar/edu/ub/club/reportes/ui/PresentadorPanelAdministracion.java) |
| 10 | PresentadorPanelAdministracion | VistaPanelAdministracion | `navegarA("reportes/uso-canchas") o navegarA("reportes/cuotas")` | [VistaPanelAdministracion.java](../src/main/java/ar/edu/ub/club/reportes/ui/VistaPanelAdministracion.java) |

## CU-41 Administrador configura datos del club

<!-- NOTAS:CU-41 -->

**Implementación (accesos, auditoría y configuración) — CU-41 Configurar datos del club — `sec_cu41_p1.puml` … `sec_cu41_p3.puml`**

`VistaConfiguracionClub.alEntrar` → `PresentadorConfiguracionClub.cargar` → `ServicioConfiguracionClub.consultar` → `FachadaPersistencia.get(ConfiguracionClub.ID_UNICO, ConfiguracionClub)` → `BrokerConfiguracionClub.materializar` (tabla `configuracion_club`) → `DatosConfiguracionClub.desde` → `mostrarDatos` (p1); Guardar (`FormularioEntidad.leer`) → `PresentadorConfiguracionClub.alGuardar(datos)` (p2) → `ServicioConfiguracionClub.modificar` → `UsuarioActualSesion.obtener` + `ControlAcceso.verificar(CONFIGURAR_CLUB)` → validación → `get(ID_UNICO)` → `ConfiguracionClub.actualizar` → `put` → `BrokerConfiguracionClub.actualizar` → `ServicioAuditoria.registrar("MODIFICACION","ConfiguracionClub", 1)` (p3) → `mostrarDatos` + `mostrarMensaje`. Encabezado: `DisposicionPrincipal` → `InformacionClubConfigurada.nombreClub` → `ServicioConfiguracionClub.consultar`.

**Diagramas de secuencia del diseño:**

### `sec_cu41_p1.puml` — CU-41 Administrador configura datos del club\n(parte 1: consulta, pasos 1-2)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaConfiguracionClub | `abre P-34 (paso 1)` | [VistaConfiguracionClub.java](../src/main/java/ar/edu/ub/club/configuracion/ui/VistaConfiguracionClub.java) |
| 2 | VistaConfiguracionClub | PresentadorConfiguracionClub | `cargar()` | [PresentadorConfiguracionClub.java](../src/main/java/ar/edu/ub/club/configuracion/ui/PresentadorConfiguracionClub.java) |
| 3 | PresentadorConfiguracionClub | ServicioConfiguracionClub | `consultar()` | [ServicioConfiguracionClub.java](../src/main/java/ar/edu/ub/club/configuracion/aplicacion/ServicioConfiguracionClub.java) |
| 4 | PresentadorConfiguracionClub | PresentadorConfiguracionClub | `DatosConfiguracion Club.desde(c)` | [PresentadorConfiguracionClub.java](../src/main/java/ar/edu/ub/club/configuracion/ui/PresentadorConfiguracionClub.java) |
| 5 | PresentadorConfiguracionClub | VistaConfiguracionClub | `mostrarDatos(datos)` | [VistaConfiguracionClub.java](../src/main/java/ar/edu/ub/club/configuracion/ui/VistaConfiguracionClub.java) |

### `sec_cu41_p2.puml` — CU-41 Administrador configura datos del club\n(parte 2: modificación, de la vista a la fachada, pasos 3-7)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Administrador | VistaConfiguracionClub | `modifica los datos y elige Guardar (paso 3)` | [VistaConfiguracionClub.java](../src/main/java/ar/edu/ub/club/configuracion/ui/VistaConfiguracionClub.java) |
| 2 | VistaConfiguracionClub | FormularioEntidad | `leer() (paso 4)` | [FormularioEntidad.java](../src/main/java/ar/edu/ub/club/compartido/ui/FormularioEntidad.java) |
| 3 | VistaConfiguracionClub | PresentadorConfiguracionClub | `alGuardar(datos)` | [PresentadorConfiguracionClub.java](../src/main/java/ar/edu/ub/club/configuracion/ui/PresentadorConfiguracionClub.java) |
| 4 | PresentadorConfiguracionClub | ServicioConfiguracionClub | `modificar(datos)` | [ServicioConfiguracionClub.java](../src/main/java/ar/edu/ub/club/configuracion/aplicacion/ServicioConfiguracionClub.java) |
| 5 | PresentadorConfiguracionClub | VistaConfiguracionClub | `mostrarDatos(datos)` | [VistaConfiguracionClub.java](../src/main/java/ar/edu/ub/club/configuracion/ui/VistaConfiguracionClub.java) |
| 6 | PresentadorConfiguracionClub | VistaConfiguracionClub | `mostrarMensaje("Datos guardados")` | [VistaConfiguracionClub.java](../src/main/java/ar/edu/ub/club/configuracion/ui/VistaConfiguracionClub.java) |
| 7 | PresentadorConfiguracionClub | VistaConfiguracionClub | `marcarCampo( campo, mensaje)` | [VistaConfiguracionClub.java](../src/main/java/ar/edu/ub/club/configuracion/ui/VistaConfiguracionClub.java) |
| 8 | VistaConfiguracionClub | FormularioEntidad | `marcarError( campo, mensaje)` | [FormularioEntidad.java](../src/main/java/ar/edu/ub/club/compartido/ui/FormularioEntidad.java) |

### `sec_cu41_p3.puml` — CU-41 (parte 3: ServicioConfiguracionClub.consultar y modificar)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorConfiguracionClub | ServicioConfiguracionClub | `consultar()` | [ServicioConfiguracionClub.java](../src/main/java/ar/edu/ub/club/configuracion/aplicacion/ServicioConfiguracionClub.java) |
| 2 | ServicioConfiguracionClub | FachadaPersistencia«Singleton» | `get(ConfiguracionClub.ID_UNICO, ConfiguracionClub)` |  |
| 3 | PresentadorConfiguracionClub | ServicioConfiguracionClub | `modificar(datos)` | [ServicioConfiguracionClub.java](../src/main/java/ar/edu/ub/club/configuracion/aplicacion/ServicioConfiguracionClub.java) |
| 4 | ServicioConfiguracionClub | ServicioConfiguracionClub | `valida nombre completo y formato de correo y teléfono (paso 4)` | [ServicioConfiguracionClub.java](../src/main/java/ar/edu/ub/club/configuracion/aplicacion/ServicioConfiguracionClub.java) |
| 5 | ServicioConfiguracionClub | FachadaPersistencia«Singleton» | `get(ConfiguracionClub.ID_UNICO, ConfiguracionClub)` |  |
| 6 | ServicioConfiguracionClub | ConfiguracionClub | `actualiza nombre, dirección, teléfono y correo` | [ConfiguracionClub.java](../src/main/java/ar/edu/ub/club/configuracion/dominio/ConfiguracionClub.java) |
| 7 | ServicioConfiguracionClub | FachadaPersistencia«Singleton» | `put(c) (paso 5)` |  |
| 8 | ServicioConfiguracionClub | ServicioAuditoria | `registrar(admin, "MODIFICACION", "ConfiguracionClub", 1, detalle) (paso 6)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

## Tareas programadas

<!-- NOTAS:TAREAS -->

**Implementación (reservas y notificaciones) — Tarea programada: finalización de reservas cumplidas (sec_tmp_disparo, sec_tmp_finalizar_reservas)**

`@Scheduled` → `PlanificadorReservas.finalizarReservas` → `ServicioReservas.finalizarReservasCumplidas` (readOnly) →
`buscar(new ReservasCumplidas(ahora))` → por reserva `TransactionTemplate.execute` (REQUIRES_NEW) → `finalizarEnTransaccion` →
`get(id, Reserva)` → `Reserva.finalizar` → `EstadoReservaConfirmada.finalizar` → `cambiarEstado(FINALIZADA)` → `put`.

**Diagramas de secuencia del diseño:**

### `sec_tmp_aplicar_saldo.puml` — Aplicar saldo a pendientes (ServicioCuotas.registrarCredito, RN-21)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioPagos | ServicioCuotas | `registrarCredito(idGrupo, importe, idPago)` | [ServicioCuotas.java](../src/main/java/ar/edu/ub/club/cuotas/aplicacion/ServicioCuotas.java) |
| 2 | ServicioCuotas | MovimientoSaldo | `«create» (CREDITO, importe, idPago)` | [MovimientoSaldo.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/MovimientoSaldo.java) |
| 3 | ServicioCuotas | FachadaPersistencia«Singleton» | `put(m)` |  |
| 4 | ServicioCuotas | ServicioCuotas | `aplicarSaldoA Pendientes(grupo)` | [ServicioCuotas.java](../src/main/java/ar/edu/ub/club/cuotas/aplicacion/ServicioCuotas.java) |
| 5 | ServicioCuotas | FachadaPersistencia«Singleton» | `buscarUno( SaldoDeGrupo(idGrupo))` |  |
| 6 | ServicioCuotas | FachadaPersistencia«Singleton» | `buscar(CuotasImpagas DeGrupo(idGrupo)) [más antigua primero]` |  |
| 7 | ServicioCuotas | Cuota | `importeAPagar()` | [Cuota.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/Cuota.java) |
| 8 | ServicioCuotas | FachadaPersistencia«Singleton» | `put(«create» Movimiento Saldo(DEBITO, importe, c))` |  |
| 9 | ServicioCuotas | ApplicationEventPublisher | `publishEvent(CuotaCubierta ConSaldoEvento(c.id, idGrupo, importe, ahora))` |  |

### `sec_tmp_disparo.puml` — Eventos temporales: disparo de las tareas programadas

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PlanificadordeSpring(@Scheduled) | PlanificadorCuotas | `emitirCuotas Mensuales()` | [PlanificadorCuotas.java](../src/main/java/ar/edu/ub/club/cuotas/aplicacion/PlanificadorCuotas.java) |
| 2 | PlanificadorCuotas | ServicioCuotas | `emitirCuotas DelMes(periodo)` | [ServicioCuotas.java](../src/main/java/ar/edu/ub/club/cuotas/aplicacion/ServicioCuotas.java) |
| 3 | PlanificadordeSpring(@Scheduled) | PlanificadorCuotas | `vencerCuotas()` | [PlanificadorCuotas.java](../src/main/java/ar/edu/ub/club/cuotas/aplicacion/PlanificadorCuotas.java) |
| 4 | PlanificadorCuotas | ServicioCuotas | `marcarCuotas Vencidas(hoy)` | [ServicioCuotas.java](../src/main/java/ar/edu/ub/club/cuotas/aplicacion/ServicioCuotas.java) |
| 5 | PlanificadordeSpring(@Scheduled) | PlanificadorReservas | `finalizarReservas()` | [PlanificadorReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/PlanificadorReservas.java) |
| 6 | PlanificadorReservas | ServicioReservas | `finalizarReservas Cumplidas()` | [ServicioReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/ServicioReservas.java) |
| 7 | PlanificadordeSpring(@Scheduled) | PlanificadorPagos | `vencerPagos Pendientes()` | [PlanificadorPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/PlanificadorPagos.java) |
| 8 | PlanificadorPagos | ServicioPagos | `vencerPagos Pendientes(ahora)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |

### `sec_tmp_emision_p1.puml` — Emisión mensual de cuotas (parte 1: ServicioCuotas.emitirCuotasDelMes)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioCuotas | FachadaPersistencia«Singleton» | `buscar(GruposVigentes SinCuota(periodo))` |  |
| 2 | ServicioCuotas | FachadaPersistencia«Singleton» | `buscarUno( SaldoDeGrupo(g.id))` |  |
| 3 | ServicioCuotas | GrupoFamiliar | `emitirCuota(periodo, fechas, saldo)` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 4 | GrupoFamiliar | GrupoFamiliar | `calcularImporte Cuota()` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 5 | GrupoFamiliar | Cuota | `«create» (periodo, importe, fechas)` | [Cuota.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/Cuota.java) |
| 6 | GrupoFamiliar | Cuota | `agrega ItemCuota(socio, categoria, importe)` | [Cuota.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/Cuota.java) |
| 7 | GrupoFamiliar | Cuota | `creditoAplicado = min(saldo, importe)` | [Cuota.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/Cuota.java) |
| 8 | ServicioCuotas | FachadaPersistencia«Singleton» | `put(c)` |  |
| 9 | ServicioCuotas | MovimientoSaldo | `«create» (DEBITO, c.creditoAplicado, c)` | [MovimientoSaldo.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/MovimientoSaldo.java) |
| 10 | ServicioCuotas | FachadaPersistencia«Singleton» | `put(m)` |  |
| 11 | ServicioCuotas | ApplicationEventPublisher | `publishEvent(CuotaCubierta ConSaldoEvento(c.id, g.id, importe, ahora))` |  |

### `sec_tmp_emision_p2.puml` — Emisión mensual de cuotas (parte 2: RegistradorPagoConSaldo)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ApplicationEventPublisher | RegistradorPagoConSaldo | `alCubrirCuota ConSaldo(evento)` | [RegistradorPagoConSaldo.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/RegistradorPagoConSaldo.java) |
| 2 | RegistradorPagoConSaldo | FachadaPersistencia«Singleton» | `get(evento.idCuota, Cuota)` |  |
| 3 | RegistradorPagoConSaldo | ProcesadorPagoConSaldo | `registrar(c, datos)` | [ProcesadorPagoConSaldo.java](../src/main/java/ar/edu/ub/club/pagos/dominio/ProcesadorPagoConSaldo.java) |
| 4 | ProcesadorPagoConSaldo | ProcesadorPagoConSaldo | `crearPago(c, datos)` | [ProcesadorPagoConSaldo.java](../src/main/java/ar/edu/ub/club/pagos/dominio/ProcesadorPagoConSaldo.java) |
| 5 | ProcesadorPagoConSaldo | PagoConSaldo | `«create» (c, importe)` | [PagoConSaldo.java](../src/main/java/ar/edu/ub/club/pagos/dominio/PagoConSaldo.java) |
| 6 | ProcesadorPagoConSaldo | FachadaPersistencia«Singleton» | `put(p)` |  |
| 7 | RegistradorPagoConSaldo | Cuota | `registrarPagoAprobado(p)` | [Cuota.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/Cuota.java) |
| 8 | RegistradorPagoConSaldo | GeneradorComprobante | `generar(p)` | [GeneradorComprobante.java](../src/main/java/ar/edu/ub/club/pagos/dominio/GeneradorComprobante.java) |

### `sec_tmp_finalizar_reservas.puml` — Finalización de reservas cumplidas (ServicioReservas.finalizarReservasCumplidas)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PlanificadorReservas | ServicioReservas | `finalizarReservasCumplidas()` | [ServicioReservas.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/ServicioReservas.java) |
| 2 | ServicioReservas | FachadaPersistencia«Singleton» | `buscar(ReservasCumplidas( reloj.ahora()))` |  |
| 3 | ServicioReservas | Reserva | `finalizar()` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |
| 4 | Reserva | EstadoReservaConfirmada | `finalizar(r)` | [EstadoReservaConfirmada.java](../src/main/java/ar/edu/ub/club/reservas/dominio/EstadoReservaConfirmada.java) |
| 5 | EstadoReservaConfirmada | Reserva | `cambiarEstado(EstadoReservaFinalizada)` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |
| 6 | ServicioReservas | FachadaPersistencia«Singleton» | `put(r)` |  |

### `sec_tmp_vencer_cuotas.puml` — Vencimiento de cuotas (ServicioCuotas.marcarCuotasVencidas, día 11)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PlanificadorCuotas | ServicioCuotas | `marcarCuotasVencidas(hoy)` | [ServicioCuotas.java](../src/main/java/ar/edu/ub/club/cuotas/aplicacion/ServicioCuotas.java) |
| 2 | ServicioCuotas | FachadaPersistencia«Singleton» | `buscar(CuotasPendientes Vencidas(hoy))` |  |
| 3 | ServicioCuotas | Cuota | `vencer(hoy)` | [Cuota.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/Cuota.java) |
| 4 | Cuota | EstadoCuotaPendiente | `vencer(c, hoy)` | [EstadoCuotaPendiente.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/EstadoCuotaPendiente.java) |
| 5 | EstadoCuotaPendiente | Cuota | `cambiarEstado(EstadoCuotaVencida)` | [Cuota.java](../src/main/java/ar/edu/ub/club/cuotas/dominio/Cuota.java) |
| 6 | ServicioCuotas | FachadaPersistencia«Singleton» | `put(c)` |  |

### `sec_tmp_vencer_pagos.puml` — Vencer pagos pendientes (PlanificadorPagos, RN-22)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PlanificadorPagos | ServicioPagos | `vencerPagos Pendientes(ahora)` | [ServicioPagos.java](../src/main/java/ar/edu/ub/club/pagos/aplicacion/ServicioPagos.java) |
| 2 | ServicioPagos | FachadaPersistencia«Singleton» | `buscar(PagosOnline PendientesAnterioresA( ahora − 24 h))` |  |
| 3 | ServicioPagos | PasarelaPago | `obtenerResultado( p.idPagoPasarela)` | [PasarelaPago.java](../src/main/java/ar/edu/ub/club/pagos/integracion/PasarelaPago.java) |
| 4 | ServicioPagos | PagoOnline | `rechazar(motivo)` | [PagoOnline.java](../src/main/java/ar/edu/ub/club/pagos/dominio/PagoOnline.java) |
| 5 | ServicioPagos | PagoOnline | `vencer("Abandonado")` | [PagoOnline.java](../src/main/java/ar/edu/ub/club/pagos/dominio/PagoOnline.java) |
| 6 | PagoOnline | EstadoPagoPendiente | `vencer(p, "Abandonado")` | [EstadoPagoPendiente.java](../src/main/java/ar/edu/ub/club/pagos/dominio/EstadoPagoPendiente.java) |
| 7 | EstadoPagoPendiente | PagoOnline | `cambiarEstado( EstadoPagoVencido)` | [PagoOnline.java](../src/main/java/ar/edu/ub/club/pagos/dominio/PagoOnline.java) |
| 8 | ServicioPagos | FachadaPersistencia«Singleton» | `put(p)` |  |
| 9 | ServicioPagos | ServicioAuditoria | `registrar(null, "VENCIMIENTO", "Pago", p.id, motivo)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |

## Interacciones comunes (usadas por todos los casos de uso)

<!-- NOTAS:COMUNES -->

### `sec_comun_autorizar.puml` — Interacción común: Autorizar(usuario, permiso)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | PresentadorX | ServicioX«fachada» | `metodo(...)` |  |
| 2 | ServicioX«fachada» | UsuarioActual | `obtener()` | [UsuarioActual.java](../src/main/java/ar/edu/ub/club/accesos/aplicacion/UsuarioActual.java) |
| 3 | ServicioX«fachada» | ControlAcceso | `verificar(u, permiso)` | [ControlAcceso.java](../src/main/java/ar/edu/ub/club/compartido/seguridad/ControlAcceso.java) |
| 4 | ControlAcceso | Usuario{SociooAdministrador} | `obtenerPermisos()` |  |
| 5 | ServicioX«fachada» | ServicioX«fachada» | `continúa el método (pertenencia RN-20 con las reglas del dominio)` |  |

### `sec_comun_broker_p1.puml` — Interacción común: persistencia con Database Broker\n(parte 1: lectura con mapa de identidad)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioReservas | FachadaPersistencia«Singleton» | `get(idReserva, Reserva)` |  |
| 2 | FachadaPersistencia«Singleton» | MapaIdentidad | `buscar(Reserva, idReserva)` | [MapaIdentidad.java](../src/main/java/ar/edu/ub/club/compartido/persistencia/MapaIdentidad.java) |
| 3 | FachadaPersistencia«Singleton» | BrokerReserva | `materializar(idReserva) (brokerDe(Reserva))` | [BrokerReserva.java](../src/main/java/ar/edu/ub/club/reservas/persistencia/BrokerReserva.java) |
| 4 | BrokerReserva | ProveedorConexiones | `conexionActual()` | [ProveedorConexiones.java](../src/main/java/ar/edu/ub/club/compartido/persistencia/ProveedorConexiones.java) |
| 5 | BrokerReserva | PostgreSQL | `consultaBase() + " WHERE r.id = ?" (JOIN socio "s_" y cancha "c_")` |  |
| 6 | BrokerReserva | BrokerReserva | `materializarDesdeFila( fila: FilaResultado): clave no registrada en el mapa` | [BrokerReserva.java](../src/main/java/ar/edu/ub/club/reservas/persistencia/BrokerReserva.java) |
| 7 | BrokerReserva | BrokerReserva | `crearObjeto(fila)` | [BrokerReserva.java](../src/main/java/ar/edu/ub/club/reservas/persistencia/BrokerReserva.java) |
| 8 | BrokerReserva | FachadaPersistencia«Singleton» | `brokerDe(Socio) .materializarDesdeFila( fila.conPrefijo("s_")) (idem Cancha, "c_")` |  |
| 9 | BrokerReserva | BrokerReserva | `EstadoReserva .desdeCodigo(fila .texto("estado"))` | [BrokerReserva.java](../src/main/java/ar/edu/ub/club/reservas/persistencia/BrokerReserva.java) |
| 10 | BrokerReserva | Reserva | `Reserva.reconstituir( id, version, turno, estado, fechaCreacion, motivo)` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |
| 11 | BrokerReserva | MapaIdentidad | `registrar(Reserva, id, r) (antes de las asociaciones)` | [MapaIdentidad.java](../src/main/java/ar/edu/ub/club/compartido/persistencia/MapaIdentidad.java) |
| 12 | BrokerReserva | BrokerReserva | `cargarAsociaciones(r)` | [BrokerReserva.java](../src/main/java/ar/edu/ub/club/reservas/persistencia/BrokerReserva.java) |
| 13 | BrokerReserva | FachadaPersistencia«Singleton» | `get(idRegistradaPor, Usuario)` |  |
| 14 | BrokerReserva | Reserva | `restaurarAsociaciones( socio, cancha, registradaPor, canceladaPor)` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |

### `sec_comun_broker_p2.puml` — Interacción común: persistencia con Database Broker\n(parte 2: Virtual Proxy y escritura con control optimista)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioReservas | GrupoFamiliar | `integrantesActivos()` | [GrupoFamiliar.java](../src/main/java/ar/edu/ub/club/grupos/dominio/GrupoFamiliar.java) |
| 2 | GrupoFamiliar | ProxyListaVirtual<Socio> | `stream()` |  |
| 3 | ProxyListaVirtual<Socio> | ProxyListaVirtual<Socio> | `real()` |  |
| 4 | ProxyListaVirtual<Socio> | FachadaPersistencia«Singleton» | `buscar(SociosDeGrupo(idGrupo))` |  |
| 5 | FachadaPersistencia«Singleton» | FachadaPersistencia«Singleton» | `consulta.resolver(this): materializarTodos en el broker de Socio` |  |
| 6 | ServicioReservas | FachadaPersistencia«Singleton» | `put(r)` |  |
| 7 | FachadaPersistencia«Singleton» | Reserva | `esNuevo()` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |
| 8 | FachadaPersistencia«Singleton» | BrokerReserva | `actualizar(r) (brokerDe(Reserva))` | [BrokerReserva.java](../src/main/java/ar/edu/ub/club/reservas/persistencia/BrokerReserva.java) |
| 9 | BrokerReserva | BrokerReserva | `valoresActualizacion(r) (estado.codigo(), …)` | [BrokerReserva.java](../src/main/java/ar/edu/ub/club/reservas/persistencia/BrokerReserva.java) |
| 10 | BrokerReserva | ProveedorConexiones | `conexionActual()` | [ProveedorConexiones.java](../src/main/java/ar/edu/ub/club/compartido/persistencia/ProveedorConexiones.java) |
| 11 | BrokerReserva | PostgreSQL | `UPDATE reserva SET …, version = version + 1 WHERE id = ? AND version = ?` |  |
| 12 | BrokerReserva | Reserva | `asignarVersion(v + 1)` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |
| 13 | BrokerReserva | BrokerReserva | `guardarPartes(r, false)` | [BrokerReserva.java](../src/main/java/ar/edu/ub/club/reservas/persistencia/BrokerReserva.java) |
| 14 | PresentadorModificarReserva | PresentadorModificarReserva | `PresentadorBase.ejecutar: alConflicto(ex)` | [PresentadorModificarReserva.java](../src/main/java/ar/edu/ub/club/reservas/ui/PresentadorModificarReserva.java) |

### `sec_comun_cancelar_baja.puml` — Interacción común: Cancelar reservas por baja (CanceladorReservasPorBaja)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioRecursos | ApplicationEventPublisher | `publishEvent(evento)` |  |
| 2 | ApplicationEventPublisher | CanceladorReservasPorBaja | `alDesactivarSocio(evento)` | [CanceladorReservasPorBaja.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/CanceladorReservasPorBaja.java) |
| 3 | CanceladorReservasPorBaja | FachadaPersistencia«Singleton» | `buscar(ReservasFuturas DeSocio(evento.idSocio, ahora))` |  |
| 4 | ApplicationEventPublisher | CanceladorReservasPorBaja | `alDesactivarCancha(evento)` | [CanceladorReservasPorBaja.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/CanceladorReservasPorBaja.java) |
| 5 | CanceladorReservasPorBaja | FachadaPersistencia«Singleton» | `buscar(ReservasFuturas DeCancha(evento.idCancha, ahora))` |  |
| 6 | ApplicationEventPublisher | CanceladorReservasPorBaja | `alDesactivarDeporte(evento)` | [CanceladorReservasPorBaja.java](../src/main/java/ar/edu/ub/club/reservas/aplicacion/CanceladorReservasPorBaja.java) |
| 7 | CanceladorReservasPorBaja | FachadaPersistencia«Singleton» | `buscar(ReservasFuturas DeDeporte(evento.idDeporte, ahora))` |  |
| 8 | CanceladorReservasPorBaja | Reserva | `cancelar(motivo, evento.usuario, ahora)` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |
| 9 | Reserva | EstadoReservaConfirmada | `cancelar(r, motivo, por, ahora)` | [EstadoReservaConfirmada.java](../src/main/java/ar/edu/ub/club/reservas/dominio/EstadoReservaConfirmada.java) |
| 10 | EstadoReservaConfirmada | Reserva | `cambiarEstado( EstadoReserva Cancelada)` | [Reserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/Reserva.java) |
| 11 | CanceladorReservasPorBaja | FachadaPersistencia«Singleton» | `put(r)` |  |
| 12 | CanceladorReservasPorBaja | PublicadorEventosReserva | `publicar(Reserva CanceladaEvento(r, evento.usuario, ahora))` | [PublicadorEventosReserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/PublicadorEventosReserva.java) |

### `sec_comun_notificar.puml` — Interacción común: Notificar evento de reserva (auditoría y correo)

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | ServicioReservas | PublicadorEventosReserva | `publicar(e)` | [PublicadorEventosReserva.java](../src/main/java/ar/edu/ub/club/reservas/dominio/PublicadorEventosReserva.java) |
| 2 | PublicadorEventosReserva | AuditorReservas | `alOcurrir(e)` | [AuditorReservas.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/AuditorReservas.java) |
| 3 | AuditorReservas | ServicioAuditoria | `registrar(e.usuario, accion, "Reserva", id, detalle)` | [ServicioAuditoria.java](../src/main/java/ar/edu/ub/club/auditoria/aplicacion/ServicioAuditoria.java) |
| 4 | PublicadorEventosReserva | NotificadorReservas | `alOcurrir(e)` | [NotificadorReservas.java](../src/main/java/ar/edu/ub/club/notificaciones/aplicacion/NotificadorReservas.java) |
| 5 | NotificadorReservas | Notificacion | `«create» (socio, reserva, tipo, mensaje)` | [Notificacion.java](../src/main/java/ar/edu/ub/club/notificaciones/dominio/Notificacion.java) |
| 6 | NotificadorReservas | FachadaPersistencia«Singleton» | `put(n)` |  |
| 7 | NotificadorReservas | ServicioCorreo | `enviar(MensajeCorreo)` | [ServicioCorreo.java](../src/main/java/ar/edu/ub/club/notificaciones/integracion/ServicioCorreo.java) |
| 8 | NotificadorReservas | Notificacion | `marcarEnviada()` | [Notificacion.java](../src/main/java/ar/edu/ub/club/notificaciones/dominio/Notificacion.java) |
| 9 | NotificadorReservas | Notificacion | `marcarFallida(detalle)` | [Notificacion.java](../src/main/java/ar/edu/ub/club/notificaciones/dominio/Notificacion.java) |
| 10 | NotificadorReservas | FachadaPersistencia«Singleton» | `put(n)` |  |

### `sec_comun_peticion.puml` — Interacción común: petición de la vista y manejo de errores

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Usuario | FormularioEntidad | `completa y confirma` | [FormularioEntidad.java](../src/main/java/ar/edu/ub/club/compartido/ui/FormularioEntidad.java) |
| 2 | FormularioEntidad | FormularioEntidad | `leer() (Binder, en el servidor)` | [FormularioEntidad.java](../src/main/java/ar/edu/ub/club/compartido/ui/FormularioEntidad.java) |
| 3 | FormularioEntidad | VistaX | `alEnviar(datos)` |  |
| 4 | VistaX | PresentadorX«PresentadorBase» | `alGuardar(datos)` |  |
| 5 | PresentadorX«PresentadorBase» | PresentadorX«PresentadorBase» | `ejecutar(accion) {final, Template Method}` |  |
| 6 | PresentadorX«PresentadorBase» | ServicioX | `metodo(datos)` |  |
| 7 | PresentadorX«PresentadorBase» | PresentadorX«PresentadorBase» | `Modelo.desde(resultado)` |  |
| 8 | PresentadorX«PresentadorBase» | VistaX | `mostrar...(modelo); mostrarMensaje("…")` |  |
| 9 | VistaX | MensajeEstado | `anunciar(mensaje)` | [MensajeEstado.java](../src/main/java/ar/edu/ub/club/compartido/ui/MensajeEstado.java) |
| 10 | PresentadorX«PresentadorBase» | VistaX | `marcarCampo(campo, mensaje)` |  |
| 11 | VistaX | FormularioEntidad | `marcarError(campo, mensaje)` | [FormularioEntidad.java](../src/main/java/ar/edu/ub/club/compartido/ui/FormularioEntidad.java) |
| 12 | PresentadorX«PresentadorBase» | VistaX | `mostrarError(mensaje)` |  |
| 13 | VistaX | MensajeEstado | `anunciarError(mensaje)` | [MensajeEstado.java](../src/main/java/ar/edu/ub/club/compartido/ui/MensajeEstado.java) |
| 14 | PresentadorX«PresentadorBase» | PresentadorX«PresentadorBase» | `alConflicto(ex) / alRequerirConfirmacion(ex) (ganchos)` |  |
| 15 | PresentadorX«PresentadorBase» | VistaX | `mostrarError(...) o pedirConfirmacion(afectadas)` |  |
| 16 | PresentadorX«PresentadorBase» | PresentadorX«PresentadorBase» | `idError = UUID; bitacora.error(idError, ex)` |  |
| 17 | PresentadorX«PresentadorBase» | VistaX | `mostrarError("Ocurrió un error inesperado. Código: " + idError)` |  |
| 18 | VistaX | MensajeEstado | `anunciarError(mensaje)` | [MensajeEstado.java](../src/main/java/ar/edu/ub/club/compartido/ui/MensajeEstado.java) |

### `sec_comun_vista_protegida.puml` — Interacción común: acceso a una vista protegida

| # | Desde | Hacia (clase) | Mensaje | Archivo |
|---|---|---|---|---|
| 1 | Usuario | VistaX«VistaProtegida» | `navega a la ruta de la pantalla` |  |
| 2 | VistaX«VistaProtegida» | VistaX«VistaProtegida» | `beforeEnter(evento) {final, Template Method}` |  |
| 3 | VistaX«VistaProtegida» | SesionActual | `autenticada()` | [SesionActual.java](../src/main/java/ar/edu/ub/club/compartido/seguridad/SesionActual.java) |
| 4 | VistaX«VistaProtegida» | SesionActual | `cuenta()` | [SesionActual.java](../src/main/java/ar/edu/ub/club/compartido/seguridad/SesionActual.java) |
| 5 | VistaX«VistaProtegida» | ControlAcceso | `tienePermiso(u, permisoRequerido())` | [ControlAcceso.java](../src/main/java/ar/edu/ub/club/compartido/seguridad/ControlAcceso.java) |
| 6 | ControlAcceso | Usuario{SociooAdministrador} | `obtenerPermisos()` |  |
| 7 | VistaX«VistaProtegida» | ControlAcceso | `tienePermiso(u, permisoAlternativo())` | [ControlAcceso.java](../src/main/java/ar/edu/ub/club/compartido/seguridad/ControlAcceso.java) |
| 8 | VistaX«VistaProtegida» | VistaX«VistaProtegida» | `alEntrar(parametros, consulta) (gancho)` |  |
| 9 | VistaX«VistaProtegida» | PresentadorX | `método de carga (cargar(...) / alEntrar(...))` |  |
| 10 | PresentadorX | VistaX«VistaProtegida» | `mostrar...(modelo de vista)` |  |

