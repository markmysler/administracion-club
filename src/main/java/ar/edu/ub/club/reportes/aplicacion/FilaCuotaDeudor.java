package ar.edu.ub.club.reportes.aplicacion;

import ar.edu.ub.club.compartido.dominio.Dinero;

/**
 * Fila del reporte de cuotas y deudores: un grupo familiar con cuotas emitidas en el período, su titular, las cuotas
 * que adeuda (Pendiente y VENCIDAS) y su deuda total calculada con el importe a pagar (importe menos crédito
 * aplicado, RN-21). La lee el objeto consulta de proyección {@code CuotasYDeudores}.
 *
 * <p>Además de los cinco atributos del diseño lleva las cantidades e importes de cuotas cobradas y vencidas del
 * grupo: el diseño pide que {@code ReporteCuotasDeudores.calcularTotales} obtenga la cantidad y el importe de las
 * cuotas cobradas, pendientes y vencidas a partir de estas filas, y con solo los cinco atributos no es posible.
 *
 * <p>Objeto de datos de salida de la capa de aplicación (inmutable).
 *
 * <p>Casos de uso: CU-39
 */
public class FilaCuotaDeudor {

    /** Identificador del grupo familiar. */
    private Long idGrupo;

    /** Apellido y nombre del titular del grupo. */
    private String titular;

    /** DNI del titular del grupo. */
    private String dniTitular;

    /** Cuotas del período en estado Pendiente o VENCIDA. */
    private Integer cuotasAdeudadas;

    /** Suma del importe a pagar de las cuotas adeudadas (RN-21). */
    private Dinero deudaTotal;

    /** Agregado en la implementación: cuotas del período en estado PAGADA. */
    private Integer cuotasCobradas;

    /** Agregado en la implementación: suma del importe de las cuotas PAGADAS. */
    private Dinero importeCobrado;

    /** Agregado en la implementación: cuotas del período en estado VENCIDA (subconjunto de las adeudadas). */
    private Integer cuotasVencidas;

    /** Agregado en la implementación: suma del importe a pagar de las cuotas VENCIDAS. */
    private Dinero importeVencido;

    /**
     * Crea la fila con todos sus datos.
     *
     * <p>Agregado en la implementación: constructor con los atributos del diseño y los cuatro agregados.
     *
     * <p>Casos de uso: CU-39
     * @param idGrupo id del grupo familiar
     * @param titular apellido y nombre del titular
     * @param dniTitular DNI del titular
     * @param cuotasAdeudadas cuotas pendientes y vencidas
     * @param deudaTotal deuda total del grupo
     * @param cuotasCobradas cuotas pagadas
     * @param importeCobrado importe de las cuotas pagadas
     * @param cuotasVencidas cuotas vencidas
     * @param importeVencido importe a pagar de las cuotas vencidas
     */
    public FilaCuotaDeudor(Long idGrupo, String titular, String dniTitular, Integer cuotasAdeudadas,
                           Dinero deudaTotal, Integer cuotasCobradas, Dinero importeCobrado, Integer cuotasVencidas,
                           Dinero importeVencido) {
        this.idGrupo = idGrupo;
        this.titular = titular;
        this.dniTitular = dniTitular;
        this.cuotasAdeudadas = cuotasAdeudadas;
        this.deudaTotal = deudaTotal;
        this.cuotasCobradas = cuotasCobradas;
        this.importeCobrado = importeCobrado;
        this.cuotasVencidas = cuotasVencidas;
        this.importeVencido = importeVencido;
    }

    /**
     * Devuelve el id del grupo familiar.
     *
     * <p>Casos de uso: CU-39
     * @return id del grupo
     */
    public Long getIdGrupo() {
        return idGrupo;
    }

    /**
     * Devuelve el apellido y nombre del titular.
     *
     * <p>Casos de uso: CU-39
     * @return titular
     */
    public String getTitular() {
        return titular;
    }

    /**
     * Devuelve el DNI del titular.
     *
     * <p>Casos de uso: CU-39
     * @return DNI
     */
    public String getDniTitular() {
        return dniTitular;
    }

    /**
     * Devuelve la cantidad de cuotas adeudadas (pendientes y vencidas).
     *
     * <p>Casos de uso: CU-39
     * @return cuotas adeudadas
     */
    public Integer getCuotasAdeudadas() {
        return cuotasAdeudadas;
    }

    /**
     * Devuelve la deuda total del grupo (importe a pagar de las cuotas adeudadas, RN-21).
     *
     * <p>Casos de uso: CU-39
     * @return deuda total
     */
    public Dinero getDeudaTotal() {
        return deudaTotal;
    }

    /**
     * Devuelve la cantidad de cuotas cobradas. Agregado en la implementación (ver la descripción de la clase).
     *
     * <p>Casos de uso: CU-39
     * @return cuotas pagadas
     */
    public Integer getCuotasCobradas() {
        return cuotasCobradas;
    }

    /**
     * Devuelve el importe de las cuotas cobradas. Agregado en la implementación (ver la descripción de la clase).
     *
     * <p>Casos de uso: CU-39
     * @return importe cobrado
     */
    public Dinero getImporteCobrado() {
        return importeCobrado;
    }

    /**
     * Devuelve la cantidad de cuotas vencidas. Agregado en la implementación (ver la descripción de la clase).
     *
     * <p>Casos de uso: CU-39
     * @return cuotas vencidas
     */
    public Integer getCuotasVencidas() {
        return cuotasVencidas;
    }

    /**
     * Devuelve el importe a pagar de las cuotas vencidas. Agregado en la implementación (ver la descripción de la
     * clase).
     *
     * <p>Casos de uso: CU-39
     * @return importe vencido
     */
    public Dinero getImporteVencido() {
        return importeVencido;
    }
}
