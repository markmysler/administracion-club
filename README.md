# Sistema de Administración de Club — implementación del diseño 6.0

Ingeniería de Software II — Universidad de Belgrano. El código implementa el diseño de la versión 6.0 (documentos
TP1 a TP9): mismos paquetes, clases, atributos, métodos y patrones. Cada clase y cada método tiene Javadoc en
español con la línea `Casos de uso:` que indica en qué casos de uso participa. Para seguir un caso de uso de punta a
punta (vista → presentador → fachada → dominio → fachada de persistencia → broker → PostgreSQL) ver
[`docs/TRAZABILIDAD.md`](docs/TRAZABILIDAD.md).

## Cómo ejecutar

Requisitos: Docker Desktop. Para ejecutar fuera de Docker: Java 21 o superior (el proyecto compila con `release 21`).

### Opción 1: todo con Docker Compose

```bash
docker compose up --build
```

Abrir <http://localhost:8080>. La primera construcción tarda varios minutos (descarga dependencias y empaqueta el
frontend de Vaadin). Servicios: `nginx` (puerto 8080 → proxy inverso, también del WebSocket de Vaadin), `club-app`
(Spring Boot + Vaadin) y `postgres` (PostgreSQL 16 con volumen `datos-club`).

### Opción 2: aplicación local contra el PostgreSQL de Compose (recomendada para la demostración)

```bash
docker compose up -d postgres
```

```bash
./mvnw spring-boot:run
```

En Windows: `mvnw.cmd spring-boot:run`. Abrir <http://localhost:8080>. La primera vez Vaadin prepara el frontend en
modo desarrollo (uno o dos minutos). La consola muestra el SQL que ejecutan los brokers y los objetos consulta
(logger `ar.edu.ub.club.compartido.persistencia` en DEBUG), útil para mostrar la traza de un caso de uso.

Flyway crea el esquema `club` y carga los datos de demostración al arrancar. Para volver a los datos iniciales:

```bash
docker compose down -v
```

### Pruebas y verificación de la documentación

```bash
./mvnw test
```

```bash
python herramientas/verificar_javadoc.py
```

El verificador falla si alguna clase, interfaz, enumeración, record, método o constructor (incluidos los privados)
no tiene Javadoc, no tiene la línea `Casos de uso:` o conserva una marca `PENDIENTE`. Con `--stubs` lista además los
métodos sin implementar. Las pruebas de persistencia usan Testcontainers (necesitan Docker; si no hay Docker se omiten).

## Usuarios de demostración

| Usuario | Clave | Tipo de cuenta | Datos |
|---|---|---|---|
| `admin` | `ClubAdmin2026Demo` | Administrador | Administrador inicial |
| `laura` | `ClubAdmin2026Demo` | Administrador | Laura Gómez |
| `juanperez` | `ClubSocio2026Demo` | Socio | Titular del grupo 1 (con María y Tomás Pérez); tiene reservas y cuotas |
| `mariaperez` | `ClubSocio2026Demo` | Socio | Integrante del grupo 1 |
| `tomasperez` | `ClubSocio2026Demo` | Socio | Integrante del grupo 1 (categoría Cadete) |
| `anagarcia` | `ClubSocio2026Demo` | Socio | Titular del grupo 2 (cuota del mes anterior vencida) |
| `carloslopez` | `ClubSocio2026Demo` | Socio | Titular del grupo 3 (con Lucía López, Infantil) |
| `lucialopez` | `ClubSocio2026Demo` | Socio | Integrante del grupo 3 |
| `pedroruiz` | `ClubSocio2026Demo` | Socio | Cuenta INACTIVA (para probar el rechazo del ingreso) |

Otros datos cargados (migración `V8__datos_demo.sql`, fechas relativas al día en que se crea la base): categorías
Activo, Cadete, Infantil, Vitalicio y Transitorio (inactiva); deportes Tenis, Pádel y Fútbol 5; canchas Tenis 1,
Tenis 2, Pádel 1 y Fútbol 5 A; horario de 08:00 a 22:00 todos los días; feriado de Navidad; reglas de reserva (2 h de
anticipación mínima, 14 días máxima, turnos de 60 minutos, 3 reservas vigentes, 4 h para cancelar o modificar);
reservas futuras, una finalizada y una cancelada; cuotas del mes anterior (pagadas y una vencida) y del mes actual
(pendientes); un pago manual y uno online aprobados con comprobante y uno online rechazado; medios de pago Efectivo,
Transferencia bancaria y Tarjeta de débito.

**Pago online sin dinero real**: la pasarela está en modo SIMULADA. Al pagar una cuota online desde «Estado de
cuenta» el navegador va a la página del simulador (`simulador-pasarela/{id}`), que permite aprobar o rechazar; el
simulador notifica al servicio de pagos como lo haría el webhook de Mercado Pago y vuelve por `/pagos/retorno`. El
correo también es simulado (`club.correo.modo=simulado`): los mensajes (confirmaciones de reserva, enlace de
recuperación de contraseña) se escriben en la consola.

## Arquitectura

| Capa (paquete) | Contenido | Patrones |
|---|---|---|
| `<módulo>.ui` | Vistas Vaadin (`Vista*`, `Dialogo*`), interfaces pasivas `Pantalla*`, presentadores `Presentador*`, modelos de vista (records) y `Menu<Módulo>` | MVP Vista Pasiva, Template Method (`VistaProtegida.beforeEnter`, `PresentadorBase.ejecutar/consultar`), Composite (componentes Vaadin) |
| `<módulo>.aplicacion` | Fachadas `Servicio*` (controladores GRASP de los casos de uso, con `@Transactional` y verificación de permisos), objetos `Datos*`/`Filtro*`/`Criterio*`, observadores, planificadores | Facade, Observer, Strategy (exportadores), Template Method (reportes) |
| `<módulo>.dominio` | Entidades (extienden `EntidadPersistente`), estados, validadores, eventos | State (reserva, cuota, pago), Chain of Responsibility (validación de reservas), Factory Method (creadores de pagos), Template Method (`ProcesadorPago`), Observer, polimorfismo de permisos (`Usuario` → `Socio`/`Administrador`) |
| `<módulo>.persistencia` | Un broker por clase persistente y los objetos consulta | Database Broker (Larman), Template Method, Query Object, Virtual Proxy, Identity Map |
| `<módulo>.integracion` | Adaptadores de correo y de pasarela de pago | Adapter |
| `compartido.*` | `FachadaPersistencia` (Singleton de Spring), `BrokerBaseDatos`, `ProveedorConexiones`, `MapaIdentidad`, `ProxyListaVirtual`, `Consulta*`, `FilaResultado`; bases de la UI; `Permiso`, `ControlAcceso`, `SesionActual`; `Dinero`, `Reloj`; excepciones | Facade, Template Method, Indirection, Protected Variations |

Tecnología: Java 21, Spring Boot 3.5 (Security, JDBC sin JPA, Scheduling), Vaadin Flow 24 (solo componentes libres),
PostgreSQL 16 con Flyway (restricción de exclusión `btree_gist` contra la doble reserva), HikariCP, Apache POI y
OpenPDF para exportar reportes, Docker Compose (nginx, app, postgres).

Reglas de dependencia: `ui` es la capa superior (nadie depende de ella); las vistas solo conocen su pantalla, su
presentador y los modelos de vista; los presentadores solo llaman a fachadas; las fachadas usan
`FachadaPersistencia` y objetos consulta (nunca un broker); entre módulos solo por fachada, consulta invertida o
evento (D-10).

## Estructura del repositorio

- `src/main/java/ar/edu/ub/club/...` — código por módulo y capa.
- `src/main/resources/db/migration` — migraciones Flyway V1 a V7 (plan de 07C) y V8 (datos de demostración).
- `src/test/java` — pruebas unitarias de los patrones del dominio y prueba del broker contra PostgreSQL.
- `herramientas/verificar_javadoc.py` — verificador de documentación.
- `docs/TRAZABILIDAD.md` — cadena de clases y métodos por caso de uso.
- `docker-compose.yml`, `Dockerfile`, `despliegue/nginx.conf` — despliegue.
