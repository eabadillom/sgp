package mx.com.ferbo.business;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.CuotaIMSSDAO;
import mx.com.ferbo.dao.n.DiaNoLaboralDAO;
import mx.com.ferbo.dao.n.PrestamoDAO;
import mx.com.ferbo.dao.n.RegistroDAO;
import mx.com.ferbo.dao.n.TipoDeduccionDAO;
import mx.com.ferbo.dao.n.TipoOtroPagoDAO;
import mx.com.ferbo.dao.n.TipoPercepcionDAO;
import mx.com.ferbo.dto.CuotaIMSSDTO;
import mx.com.ferbo.dto.PrestamoDTO;
import mx.com.ferbo.model.CatDiaNoLaboral;
import mx.com.ferbo.model.CatPercepciones;
import mx.com.ferbo.model.CatPeriodicidadPago;
import mx.com.ferbo.model.CatSubsidio;
import mx.com.ferbo.model.CatTarifaISR;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaConcepto;
import mx.com.ferbo.model.DetNominaConceptoPK;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.DetNominaEmisor;
import mx.com.ferbo.model.DetNominaOtroPago;
import mx.com.ferbo.model.DetNominaOtroPagoPK;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPercepcionPK;
import mx.com.ferbo.model.DetNominaReceptor;
import mx.com.ferbo.model.DetPrestamo;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.model.sat.CatConcepto;
import mx.com.ferbo.model.sat.CatMetodoPago;
import mx.com.ferbo.model.sat.CatRegimenFiscal;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.model.sat.CatTipoOtroPago;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import mx.com.ferbo.model.sat.CatUnidadSAT;
import mx.com.ferbo.model.sat.CatUsoCFDI;
import mx.com.ferbo.util.DateUtils;
import mx.com.ferbo.util.SGPException;

public class NominaSemanalBL {
	private static Logger log = LogManager.getLogger(NominaSemanalBL.class);
	
	private DetEmpleado empleado = null;
	private Date periodoInicio = null;
	private Date periodoFin = null;
	private Date periodoSiguienteInicio = null;
	private Date periodoSiguienteFin = null;
	private Date fechaInicioAnio = null;
	private Date fechafinAnio = null;
	private Integer semanaAnio = null;
	private Boolean ultimaSemanaMes = null;
	
	private BigDecimal diasPeriodo = null;
	private BigDecimal diasTrabajados = null;
	private BigDecimal diasAsueto = null;
	private BigDecimal diasAusencia = null;
	private Map<String, DetRegistro> mapAsistencias = null;
	
	//PERCEPCIONES
	private BigDecimal salarioDiario = null;
	private BigDecimal salarioDiarioIntegrado = null;
	private BigDecimal proporcionalSeptimoDia = null;
	private BigDecimal septimoDia = null;
	private BigDecimal salarioSemanal = null;
	private BigDecimal bonoPuntualidad = null;
	private BigDecimal valesDespensa = null;
	private BigDecimal totalPercepciones = null;
	
	//DEDUCCIONES ISR
	private BigDecimal baseISR = null;
	private BigDecimal excedente = null;
	private BigDecimal isrPrevio = null;
	private BigDecimal impuestoAntesSubsidio = null;
	private BigDecimal subsidioEmpleo = null;
	private BigDecimal isr = null;
	//CUOTAS DEL IMSS
	private BigDecimal enfermedadYmaternidad = null;
	private BigDecimal gastosMedicosPensionadosBeneficiarios = null;
	private BigDecimal enDinero = null;
	private BigDecimal invalidezVida = null;
	private BigDecimal cesantiaEdadAvanzadaVejez = null;
	private BigDecimal imss = null;
	//PRESTAMOS
	private BigDecimal totalPrestamos = null;
	//TOTAL DEDUCCIONES
	private BigDecimal totalDeducciones;
	
	//NETO A PAGAR
	private BigDecimal neto;
	
    private static final int SEPTIMO_DIA = 1;
    private static final int DIAS_ANIO = 365;
    private static final int DIAS_POR_PERIODO = 6;
    
    private CatPercepciones parametrosPercepciones = null;
    private List<CatTarifaISR> tablaISRSemanal = null;
    private List<CatSubsidio> tablaSubsidioSemanal = null;
    private BigDecimal tasaValesDespensa = null;
	private BigDecimal uma = null;
	
	private CuotaIMSSDAO tarifaIMSSDAO = null;
	private TipoPercepcionDAO tipoPercepcionDAO = null;
	private TipoOtroPagoDAO tipoOtroPagoDAO = null;
	private TipoDeduccionDAO tipoDeduccionDAO = null;
	private PrestamoDAO prestamoDAO = null;
	private CatMetodoPago metodoPago = null;
	private CatConcepto concepto = null;
	private CatUnidadSAT unidadSAT = null;
	private CatPeriodicidadPago periodicidad = null;
	private CatRegimenFiscal regimenFiscalReceptor = null;
	private CatUsoCFDI usoCFDI = null;
	private Integer anio = null;
	
	
	//OBJETOS RELACIONADOS A LA NOMINA Y CFDI
	public NominaSemanalBL(DetEmpleado empleado, Date periodoInicio, Date periodoFin) {
		Integer anioActual = null;
		
		this.empleado = empleado;
		this.periodoInicio = periodoInicio;
		this.periodoFin = periodoFin;
		this.tarifaIMSSDAO = new CuotaIMSSDAO();
		this.tipoPercepcionDAO = new TipoPercepcionDAO(CatTipoPercepcion.class);
		this.tipoOtroPagoDAO = new TipoOtroPagoDAO(CatTipoOtroPago.class);
		this.tipoDeduccionDAO = new TipoDeduccionDAO(CatTipoDeduccion.class);
		this.prestamoDAO = new PrestamoDAO(DetPrestamo.class);
		
		anioActual = DateUtils.getAnio(periodoInicio);
		this.fechaInicioAnio = DateUtils.getDate(anioActual, DateUtils.ENERO, 1);
		DateUtils.setTime(this.fechaInicioAnio, 0, 0, 0, 0);
		
		this.fechafinAnio = DateUtils.getDate(anioActual, DateUtils.DICIEMBRE, 31);
		DateUtils.setTime(this.fechafinAnio, 23, 59, 59, 000);
		
		this.semanaAnio = DateUtils.getSemanaAnio(this.periodoInicio);
		
		this.periodoSiguienteInicio = DateUtils.addDay(this.periodoInicio, 7);
		this.periodoSiguienteFin = DateUtils.addDay(this.periodoFin, 7);
	}
	
	private DetNomina newNomina() {
		DetNomina nomina = null;
		
		DetNominaEmisor emisor = null;
		DetNominaReceptor receptor = null;
		
		List<DetNominaConcepto> conceptos = null;
		List<DetNominaPercepcion> percepciones = null;
		List<DetNominaOtroPago> otrosPagos = null;
		List<DetNominaDeduccion> deducciones = null;
		
		try {
			
			
			nomina = new DetNomina();
			conceptos = new ArrayList<>();
			percepciones = new ArrayList<>();
			otrosPagos = new ArrayList<>();
			deducciones = new ArrayList<>();
			
			emisor = new DetNominaEmisor();
			receptor = new DetNominaReceptor();
			
			nomina.setEmisor(emisor);
			nomina.setReceptor(receptor);
			nomina.setConceptos(conceptos);
			nomina.setPercepciones(percepciones);
			nomina.setOtrosPagos(otrosPagos);
			nomina.setDeducciones(deducciones);
			
		} catch(Exception ex) {
			log.error("Problema para generar la estructura básica de la nómina del empleado.");
		}
		
		return nomina;
	}
	
	public DetNomina calculoNomina() {
		BigDecimal diasTrabajados = null;
		BigDecimal diasPeriodo = null;
		
		CatSubsidio tarifaSubsidio = null;
		CatTarifaISR tarifaISR = null;
		
		List<PrestamoDTO> prestamos = null;
		
		DetNomina nominaNew = null;
		DetNominaPercepcion pSueldo = null;
		DetNominaPercepcion pSeptimoDia = null;
		DetNominaPercepcion pBonoPuntualidad = null;
		DetNominaPercepcion pValeDespensa = null;
		
		DetNominaOtroPago opAjusteAlNeto = null;
		DetNominaOtroPago opSubsidioEmpleo = null;
		
		DetNominaDeduccion dISR = null;
		DetNominaDeduccion dIMSS = null;
		List<DetNominaDeduccion> listaPrestamos = null;
		
		List<DetNominaPercepcion> percepciones = null;
		List<DetNominaOtroPago> otrosPagos = null;
		List<DetNominaDeduccion> deducciones = null;
		
		try {
			log.info("Ejecutando la nomina de la semana {} del año en curso...", this.semanaAnio);
			nominaNew = this.newNomina();
			
			this.esUltimaSemanaMes();
			
			log.debug("Buscando información empresarial del empleado.");
			
			mapAsistencias = this.getAsistencias(this.empleado, this.periodoInicio, this.periodoFin);
			diasPeriodo = new BigDecimal(DateUtils.daysDiff(periodoInicio, periodoFin)).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			//Para los días trabajados, se debe considerar el periodo inicio y fin de cálculo de la nómina y validar si de los 6 días que
			//al trabajador le corresponde laborar, tuvo alguna falta.
			diasTrabajados = this.getDiasTrabajados(mapAsistencias, DIAS_POR_PERIODO); //EL SEGUNDO PARAMETRO (6) CORRESPONDE A LOS DÍAS QUE DEBE LABORAR UN TRABAJADOR POR SEMANA.
			this.diasTrabajados = diasTrabajados;
			this.diasPeriodo = new BigDecimal(DIAS_POR_PERIODO).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			//Para el séptimo día, se considera el salario diario (sin SDI), dividiendolo entre los días de la semana que se deben laborar,
    		//multiplicado por los días que si laboró el trabajador (parte proporcional de los días trabajados).
    		
			
    		this.salarioDiario = empleado.getSueldoDiario();
    		this.salarioDiarioIntegrado = this.calculoSDI(this.empleado);
    		
    		this.salarioSemanal = this.salarioDiario.multiply(diasTrabajados).setScale(2, BigDecimal.ROUND_HALF_UP);
			this.septimoDia = this.calculoSeptimoDia(this.empleado);
			this.bonoPuntualidad = this.calculoBonoPuntualidad(DIAS_POR_PERIODO, empleado, mapAsistencias, this.salarioDiarioIntegrado, proporcionalSeptimoDia);
			this.valesDespensa = this.calculoValesDespensa(diasPeriodo);
			
			this.totalPercepciones = this.salarioSemanal
					.add(this.septimoDia)
					.add(this.bonoPuntualidad)
					.add(this.valesDespensa)
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			
			//deducciones
			//Para el cálculo del ISR a retener al empleado se deben sumar las percepciones 
			//que gravan para ISR, en este caso son:
			if(this.salarioSemanal.compareTo(BigDecimal.ZERO) > 0) {
				this.baseISR = this.salarioSemanal
						.add(this.septimoDia)
						.add(this.bonoPuntualidad)
						.setScale(2, BigDecimal.ROUND_HALF_UP);
				
				tarifaISR = this.getTarifaISR(this.baseISR);
				tarifaSubsidio = this.getTarifaSubsidio(this.baseISR);
				
				this.excedente = this.calculoExcedente(tarifaISR);
				this.isrPrevio = this.calculoISRPrevio(tarifaISR);
				this.impuestoAntesSubsidio = calculoImpuestoAntesSubsidio(tarifaISR);
				this.subsidioEmpleo = calculoSubsidioEmpleo(tarifaSubsidio);
				this.isr = impuestoAntesSubsidio.subtract(subsidioEmpleo);
				
				//Cálculo del IMSS
				this.enfermedadYmaternidad = this.calculoEnfermedadMaternidad();
				this.gastosMedicosPensionadosBeneficiarios = this.calculoGastosMedicosPensionadosBeneficiarios();
				this.enDinero = this.calculoEnDinero();
				this.invalidezVida = this.calculoInvalidezVida();
				this.cesantiaEdadAvanzadaVejez = this.calculoCesantiaEdadAvanzadaVejez();
				this.imss = enfermedadYmaternidad
						.add(this.gastosMedicosPensionadosBeneficiarios)
						.add(this.enDinero)
						.add(this.invalidezVida)
						.add(this.cesantiaEdadAvanzadaVejez)
						;
				
				
				listaPrestamos = this.procesaPrestamos(this.empleado);
				this.totalPrestamos = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
				
				for(DetNominaDeduccion p : listaPrestamos) {
					this.totalPrestamos = this.totalPrestamos.add(p.getImporte());
				}
				
			} else {
				this.baseISR = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
				this.excedente = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
				this.isrPrevio = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
				this.impuestoAntesSubsidio = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
				this.subsidioEmpleo = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
				this.isr = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
				
				//Cálculo del IMSS
				this.enfermedadYmaternidad = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
				this.gastosMedicosPensionadosBeneficiarios = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
				this.enDinero = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
				this.invalidezVida = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
				this.cesantiaEdadAvanzadaVejez = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
				this.imss = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
				this.totalPrestamos = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
				listaPrestamos = new ArrayList<>();
			}
			
			this.totalDeducciones = this.isr.add(this.imss).add(this.totalPrestamos);
			this.neto = this.totalPercepciones.subtract(totalDeducciones);
			
			
			//NUEVA NOMINA...........................................
			nominaNew.setFechaEmision(new Date());
			nominaNew.setClaveExportacion("01");
			nominaNew.setTipoComprobante("N");
			nominaNew.setMoneda("MXN");
			nominaNew.setMetodoPago(this.metodoPago);
			nominaNew.setSerie(String.format("%d", this.anio));
			nominaNew.setFolio(String.format("%d", this.semanaAnio));
			nominaNew.setLugarExpedicion(this.empleado.getDatoEmpresa().getEmpresa().getCodigoPostal());
			nominaNew.setEjercicio(DateUtils.getAnio(this.fechaInicioAnio));
			nominaNew.setDiasLaborados(this.diasTrabajados.intValue());
			nominaNew.setDiasNoLaborados(this.diasPeriodo.subtract(diasTrabajados).intValue());
			nominaNew.setPeriodo(DateUtils.getSemanaAnio(this.fechaInicioAnio));
			nominaNew.setSubtotal(this.totalPercepciones);
			nominaNew.setDescuento(this.totalDeducciones);
			nominaNew.setTotal(this.neto);
			nominaNew.setPeriodoInicio(this.periodoInicio.toInstant().atZone(ZoneId.of("GMT-6")).toLocalDate());
			nominaNew.setPeriodoFin(this.periodoFin.toInstant().atZone(ZoneId.of("GMT-6")).toLocalDate());
			
			DetNominaEmisor emisor = nominaNew.getEmisor();
			emisor.setNomina(nominaNew);
			emisor.setNombre(this.empleado.getDatoEmpresa().getEmpresa().getRazonSocial());
			emisor.setRfc(this.empleado.getDatoEmpresa().getEmpresa().getRfc());
			emisor.setCodigoPostal(this.empleado.getDatoEmpresa().getEmpresa().getCodigoPostal());
			emisor.setRegistroPatronal(this.empleado.getDatoEmpresa().getEmpresa().getRegistroPatronal());
			emisor.setRegimenFiscal(this.empleado.getDatoEmpresa().getEmpresa().getRegimenFiscal());
			
			
			DetNominaReceptor receptor = nominaNew.getReceptor();
			receptor.setNomina(nominaNew);
			receptor.setNombre(String.format("%s %s %s", this.empleado.getNombre(), this.empleado.getPrimerAp(), this.empleado.getSegundoAp()));
			receptor.setRfc(this.empleado.getRfc());
			receptor.setCodigoPostal(this.empleado.getDatoEmpresa().getCodigoPostal());
			receptor.setRegimenFiscal(this.regimenFiscalReceptor);//TODO pendiente revisar el regimen fiscal del receptor.
			receptor.setUsoCfdi(this.usoCFDI);
			receptor.setCurp(this.empleado.getCurp());
			receptor.setNss(this.empleado.getDatoEmpresa().getNss());
			receptor.setInicioRelacionLaboral(this.empleado.getDatoEmpresa().getFechaIngreso());
			receptor.setAntiguedad("P----");//TODO pendiente implementar antiguedad
			receptor.setTipoContrato(this.empleado.getDatoEmpresa().getTipoContrato());
			receptor.setSindicalizado(this.empleado.getDatoEmpresa().getSindicalizado());
			receptor.setTipoJornada(this.empleado.getDatoEmpresa().getTipoJornada());
			receptor.setTipoRegimen(this.empleado.getDatoEmpresa().getTipoRegimen());
			receptor.setNumeroEmpleado(this.empleado.getNumEmpleado());
			receptor.setDepartamento(this.empleado.getDatoEmpresa().getArea().getDescripcion());
			receptor.setPuesto(this.empleado.getDatoEmpresa().getPuesto().getDescripcion());
			receptor.setRiesgoPuesto(this.empleado.getDatoEmpresa().getRiesgoPuesto());
			receptor.setPeriodicidadPago(this.empleado.getDatoEmpresa().getPeriodicidadPago());
			receptor.setSalarioDiario(this.salarioDiario);
			receptor.setSalarioDiarioIntegrado(this.salarioDiarioIntegrado);
			receptor.setEntidadFederativa(this.empleado.getDatoEmpresa().getEntidadFederativa());
			
			percepciones = nominaNew.getPercepciones();
			otrosPagos = nominaNew.getOtrosPagos();
			deducciones = nominaNew.getDeducciones();
			
			DetNominaConcepto concepto = new DetNominaConcepto();
			concepto.setKey(new DetNominaConceptoPK(nominaNew, 0));
			concepto.setConcepto(this.concepto);
			concepto.setCantidad(new BigDecimal("1").setScale(2, BigDecimal.ROUND_HALF_UP));
			concepto.setUnidad(this.unidadSAT);
			concepto.setNombreConcepto("Pago de nómina");
			concepto.setObjetoImpuesto("01");
			concepto.setValorUnitario(this.totalPercepciones);
			concepto.setImporte(this.totalPercepciones);
			concepto.setDescuento(this.totalDeducciones);
			nominaNew.getConceptos().add(concepto);
			
			int idxP = 0;
			int idxOP = 0;
			int idxD = 0;
			
			//PERCEPCIONES...
			pSueldo = new DetNominaPercepcion();
			pSeptimoDia = new DetNominaPercepcion();
			pBonoPuntualidad = new DetNominaPercepcion();
			pValeDespensa = new DetNominaPercepcion();
			if(this.isr.compareTo(BigDecimal.ZERO) > 0) {
				
				pSueldo.setKey(new DetNominaPercepcionPK(nominaNew, idxP++));
				pSueldo.setClave("001");
				pSueldo.setNombre("Sueldo");
				CatTipoPercepcion tpSueldo = tipoPercepcionDAO.buscarPorId("001");
				pSueldo.setTipoPercepcion(tpSueldo);
				pSueldo.setImporteGravado(this.salarioSemanal);
				pSueldo.setImporteExcento(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
				pSueldo.setClave("FRB-" + tpSueldo.getClave());
				percepciones.add(pSueldo);
				
				pSeptimoDia.setKey(new DetNominaPercepcionPK(nominaNew, idxP++));
				pSeptimoDia.setClave("003");
				pSeptimoDia.setNombre("Séptimo día");
				CatTipoPercepcion tpSeptimoDia = tipoPercepcionDAO.buscarPorId("001");
				pSeptimoDia.setTipoPercepcion(tpSeptimoDia);
				pSeptimoDia.setImporteGravado(this.septimoDia);
				pSeptimoDia.setImporteExcento(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
				pSeptimoDia.setClave("FRB-" + tpSeptimoDia.getClave());
				percepciones.add(pSeptimoDia);
				
				pBonoPuntualidad.setKey(new DetNominaPercepcionPK(nominaNew, idxP++));
				pBonoPuntualidad.setClave("015");
				pBonoPuntualidad.setNombre("Bono puntualidad");
				CatTipoPercepcion tpBonoPuntualidad = tipoPercepcionDAO.buscarPorId("010");
				pBonoPuntualidad.setTipoPercepcion(tpBonoPuntualidad);
				pBonoPuntualidad.setImporteGravado(this.bonoPuntualidad);
				pBonoPuntualidad.setImporteExcento(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
				pBonoPuntualidad.setClave("FRB-" + tpBonoPuntualidad.getClave());
				percepciones.add(pBonoPuntualidad);
			}
			
			pValeDespensa.setKey(new DetNominaPercepcionPK(nominaNew, idxP++));
			pValeDespensa.setClave("032");
			pValeDespensa.setNombre("Despensa");
			CatTipoPercepcion tpValeDespensa = tipoPercepcionDAO.buscarPorId("029");
			pValeDespensa.setTipoPercepcion(tpValeDespensa);
			pValeDespensa.setImporteGravado(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
			pValeDespensa.setImporteExcento(this.valesDespensa);
			pValeDespensa.setClave("FRB-" + tpValeDespensa.getClave());
			percepciones.add(pValeDespensa);
			
			
			//OTROS PAGOS...
			opAjusteAlNeto = new DetNominaOtroPago();
			opAjusteAlNeto.setKey(new DetNominaOtroPagoPK(nominaNew, idxOP++));
			CatTipoOtroPago tpAjusteAlNeto = this.tipoOtroPagoDAO.buscarPorId("999");
			opAjusteAlNeto.setTipoOtroPago(tpAjusteAlNeto);
			opAjusteAlNeto.setClave("FRB-099");
			opAjusteAlNeto.setNombre("Ajuste al neto");
			opAjusteAlNeto.setImporte(new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP));
			otrosPagos.add(opAjusteAlNeto);
			
			opSubsidioEmpleo = new DetNominaOtroPago();
			opSubsidioEmpleo.setKey(new DetNominaOtroPagoPK(nominaNew, idxOP++));
			CatTipoOtroPago topSubsidioEmpleo = this.tipoOtroPagoDAO.buscarPorId("002");
			opSubsidioEmpleo.setTipoOtroPago(topSubsidioEmpleo);
			opSubsidioEmpleo.setClave("FRB-035");
			opSubsidioEmpleo.setNombre("Subs. al empleo mes");
			opSubsidioEmpleo.setImporte(this.subsidioEmpleo);
			otrosPagos.add(opSubsidioEmpleo);
			
			//DEDUCCIONES...
			dISR = new DetNominaDeduccion();
			dISR.setKey(new DetNominaDeduccionPK(nominaNew, idxD++));
			CatTipoDeduccion tdISR = this.tipoDeduccionDAO.buscarPorId("002");
			dISR.setTipoDeduccion(tdISR);
			dISR.setClave("045");
			dISR.setNombre("I.S.R.");
			dISR.setImporte(this.isr);
			deducciones.add(dISR);
			
			dIMSS = new DetNominaDeduccion();
			dIMSS.setKey(new DetNominaDeduccionPK(nominaNew, idxD++));
			CatTipoDeduccion tdIMSS = this.tipoDeduccionDAO.buscarPorId("001");
			dIMSS.setTipoDeduccion(tdIMSS);
			dIMSS.setClave("FRB-052");
			dIMSS.setNombre("I.M.S.S.");
			dIMSS.setImporte(this.imss);
			deducciones.add(dIMSS);
			
			deducciones.addAll(listaPrestamos);
			
		} catch(Exception ex) {
			log.error("Problema para obtener el cálculo de la nómina del empleado {} {} {}", empleado.getNombre(), empleado.getPrimerAp(), empleado.getSegundoAp() );
			log.error(ex);
		}
		
		return nominaNew;
	}
	
	private CatSubsidio getTarifaSubsidio(BigDecimal baseISR) throws SGPException {
		CatSubsidio tarifaSubsidio = null;
		
		List<CatSubsidio> resultList = this.tablaSubsidioSemanal.stream()
				.filter(s -> s.getParaIngresosDe().compareTo(baseISR) <= 0
						&& s.getHastaIngresosDe().compareTo(baseISR) >= 0)
				.collect(Collectors.toList())
				;
		
		if(resultList.size() > 0) {
			tarifaSubsidio = resultList.get(0);
		} else {
			
			throw new SGPException(String.format("No se encontró una tarifa de subsidio para la base calculada: Base ISR = %s", baseISR.toString()));
		}
		
		return tarifaSubsidio;
	}

	private CatTarifaISR getTarifaISR(BigDecimal baseISR) throws SGPException {
		CatTarifaISR tarifaISR = null;
		
		List<CatTarifaISR> resultList = this.tablaISRSemanal.stream()
				.filter(i -> i.getLimiteInferior().compareTo(baseISR) <= 0 
					&& i.getLimiteSuperior().compareTo(baseISR) >= 0)
				.collect(Collectors.toList());
		
		if(resultList.size() > 0)
			tarifaISR = resultList.get(0);
		else {
			throw new SGPException(String.format("No se encontró una tarifa ISR para la base calculada: Base ISR = %s", baseISR.toString()));
		}
		
		return tarifaISR;
	}

	private void esUltimaSemanaMes() {
		Integer mesActual = DateUtils.getMes(this.periodoFin);
		Integer mesSiguiente = DateUtils.getMes(this.periodoSiguienteFin);
		
		if(mesSiguiente > mesActual) {
			this.ultimaSemanaMes = new Boolean(true);
			log.info("ULTIMA SEMANA DEL MES: {} - {}", this.periodoInicio, this.periodoFin);
		}
		else {
			this.ultimaSemanaMes = new Boolean(false);
		}
	}
	
	private List<DetNominaDeduccion> procesaPrestamos(DetEmpleado empleado) {
		BigDecimal totalPrestamos = null;
		List<DetPrestamo> prestamos = null;
		DetNominaDeduccion deduccion = null;
		List<DetNominaDeduccion> deducciones = null;
		try {
			deducciones = new ArrayList<DetNominaDeduccion>();
			prestamos = prestamoDAO.buscar(empleado.getIdEmpleado());
			
			if(prestamos.size() <= 0)
				throw new SGPException("No hay préstamos para el empleado.");
			
			totalPrestamos = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
			for(DetPrestamo prestamo : prestamos) {
				deduccion = new DetNominaDeduccion();
				deduccion.setTipoDeduccion(prestamo.getTipoPrestamo().getTipoDeduccion());
				deduccion.setNombre(prestamo.getTipoPrestamo().getDescripcion());
				deduccion.setClave(prestamo.getTipoPrestamo().getTipoPrestamo());
				deduccion.setImporte(prestamo.getImporte());
				totalPrestamos = totalPrestamos
						.add(prestamo.getImporte())
						;
				
				deducciones.add(deduccion);
			}
			
		} catch(Exception ex) {
			log.error("Problema para procesar los préstamos del empleado");
			totalPrestamos = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		return deducciones;
	}

	/**Con base en las asistencias del trabajador, se determina cuantos días se presentó a laborar.
	 * @param mapAsistencias Registro de asistencias del trabajador
	 * @param diasPorSemana Máximo de días por periodo que un trabajador puede laborar (según contrato).<br>
	 * Por ejemplo, un trabajador, por contrato, puede trabajar 6 días de la semana.
	 * @return
	 */
	private BigDecimal getDiasTrabajados(Map<String, DetRegistro> mapAsistencias, int diasPorSemana) {
		BigDecimal diasTrabajados = null;
		Integer diasRegistrados = null;
		DetRegistro registro = null;
		
		diasRegistrados = mapAsistencias.size();
		
		if(diasRegistrados.compareTo(diasPorSemana) == 0) {
			return new BigDecimal(diasRegistrados).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		Set<String> keySet = mapAsistencias.keySet();
		
		for(String key : keySet) {
			registro = mapAsistencias.get(key);
			log.info("Buscando día de la semana Key en el catálogo de días no laborales: {}, {}...", key, registro.getFechaEntrada());
		}
		diasTrabajados = new BigDecimal(mapAsistencias.size());
		
		return diasTrabajados;
	}
	
	private Map<String, DetRegistro> getAsistencias(DetEmpleado empleado, Date periodoInicio, Date periodoFin) {
		Map<String, DetRegistro> mapAsistencias = null;
		List<DetRegistro> listaAsistencias = null;
		List<CatDiaNoLaboral> listaDiasDescanso = null;
		RegistroDAO registroDAO = null;
		DiaNoLaboralDAO diaNLDAO = null;
		String diaSemana = null;
		Date diaNLEntrada = null;
		Date diaNLSalida = null;
		
		mapAsistencias = new HashMap<String, DetRegistro>();
		registroDAO = new RegistroDAO(DetRegistro.class);
		listaAsistencias = registroDAO.buscar(empleado.getIdEmpleado(), periodoInicio, periodoFin);
		for(DetRegistro registro : listaAsistencias) {
			diaSemana = DateUtils.getDiaSemana(registro.getFechaEntrada());
			if(mapAsistencias.containsKey(diaSemana))
				continue;
			mapAsistencias.put(diaSemana, registro);
		}
		
		diaNLDAO = new DiaNoLaboralDAO(CatDiaNoLaboral.class);
		listaDiasDescanso = diaNLDAO.buscarPorPeriodo("MX", periodoInicio, periodoFin);
		
		for(CatDiaNoLaboral dia : listaDiasDescanso) {
			diaSemana = DateUtils.getDiaSemana(dia.getFecha());
			if(mapAsistencias.containsKey(diaSemana))
				continue;
			
			diaNLEntrada = new Date(dia.getFecha().getTime());
			diaNLSalida = new Date(dia.getFecha().getTime());
			
			mapAsistencias.put(diaSemana, new DetRegistro(null, diaNLEntrada, diaNLSalida, -1, "DIA NO LABORAL"));
		}
		
		this.proporcionalSeptimoDia = new BigDecimal(mapAsistencias.size())
				.setScale(2, BigDecimal.ROUND_HALF_UP)
				.divide(new BigDecimal( DIAS_POR_PERIODO ).setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal.ROUND_HALF_UP)
				.setScale(2, BigDecimal.ROUND_HALF_UP)
				;
		
		return mapAsistencias;
	}

	private BigDecimal calculoSDI(DetEmpleado empleado) {
		BigDecimal sdi = null;
    	BigDecimal diasAguinaldo = null;
    	BigDecimal diasVacaciones = null;
    	BigDecimal primaVacacional = null;
    	BigDecimal diasAnio = null;
    	BigDecimal sueldoDiario = null;
    	
    	BigDecimal factorSDI = null;
    	
    	try {
    		diasAnio = new BigDecimal(DIAS_ANIO).setScale(2, BigDecimal.ROUND_HALF_UP);
    		diasAguinaldo = new BigDecimal(parametrosPercepciones.getDiasAguinaldo().intValue());
    		
    		//TODO Dias de vacaciones debe ser un dato calculado, conforme a la ley federal del trabajo
    		diasVacaciones = new BigDecimal(parametrosPercepciones.getDiasVacaciones().intValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
    		primaVacacional = new BigDecimal(parametrosPercepciones.getPrimaVacacional().floatValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
    		sueldoDiario = empleado.getSueldoDiario();
    		
    		factorSDI = primaVacacional
    				.multiply(diasVacaciones).setScale(4, BigDecimal.ROUND_HALF_UP)
    				.add(diasAguinaldo)
    				.add(diasAnio)
    				.divide(diasAnio, 4, BigDecimal.ROUND_HALF_UP)
    				;
    		
    		sdi = sueldoDiario.multiply(factorSDI).setScale(2, BigDecimal.ROUND_HALF_UP);
    	} catch(Exception ex) {
    		sdi = BigDecimal.ZERO;
    	}
    	
        return sdi;
	}
	
	private BigDecimal calculoSeptimoDia(DetEmpleado empleado) {
		BigDecimal septimoDia = null;
		
		try {
			septimoDia = this.salarioDiario
					.divide(this.diasPeriodo, 4, BigDecimal.ROUND_HALF_UP)
					.multiply(this.diasTrabajados).setScale(2, BigDecimal.ROUND_HALF_UP)
					;
			
		} catch(Exception ex) {
			log.error("Problema para obtener el cálculo del septimo día.",  ex);
			septimoDia = BigDecimal.ZERO;
		}
		return septimoDia;
	}
	
	private BigDecimal calculoBonoPuntualidad(int diasPorPeriodo, DetEmpleado empleado, Map<String, DetRegistro> mapAsistencia, BigDecimal salarioDiarioIntegrado, BigDecimal proporcionalSeptimoDia) {
		BigDecimal bono = null;
    	BigDecimal tasaBonoPuntualidad = null;
    	BigDecimal diasPeriodo = null;
    	
    	try {
    		//TODO VALIDAR PRIMERO SI NO HAY RETARDOS.
    		//En caso de existir retardos en el periodo de calculo, el bono de puntualidad es CERO.
    		if(this.diasTrabajados.intValue() < diasPorPeriodo)
    			return BigDecimal.ZERO;
    		
    		for(Map.Entry<String, DetRegistro> entry :  this.mapAsistencias.entrySet()) {
    			String codigo = entry.getValue().getIdEstatus().getCodigo();
    			if("R".equalsIgnoreCase(codigo) || "F".equalsIgnoreCase(codigo))
    				return BigDecimal.ZERO;
    		}
    		
    		tasaBonoPuntualidad = new BigDecimal(this.parametrosPercepciones.getBonoPuntualidad().floatValue());
    		diasPeriodo = this.diasTrabajados.add(proporcionalSeptimoDia).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
    		bono = salarioDiarioIntegrado.multiply(tasaBonoPuntualidad).setScale(2, BigDecimal.ROUND_HALF_UP);
    		bono = bono.multiply(diasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
    	} catch(Exception ex) {
    		bono = BigDecimal.ZERO;
    	}
    	
        return bono;
	}
	
	private BigDecimal calculoValesDespensa(BigDecimal diasPeriodo) {
		BigDecimal vales = null;
		
    	try {
    		
    		if(this.diasTrabajados.compareTo(BigDecimal.ZERO) == 0)
    			throw new SGPException("No es posible asignar bono de puntualidad.");
    		
    		this.uma = parametrosPercepciones.getUma();
    		this.tasaValesDespensa = parametrosPercepciones.getValeDespensa();
    		
    		vales = uma.multiply(tasaValesDespensa).setScale(4, BigDecimal.ROUND_HALF_UP);
    		vales = vales.multiply(diasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
    	} catch(Exception ex) {
    		vales = BigDecimal.ZERO;
    	}
    	
    	return vales;
	}
	/********************TERMINAN PERCEPCIONES****************/
	
	
	/********************EMPIEZAN DEDUCCIONES****************/
	
	private BigDecimal calculoExcedente(CatTarifaISR tarifaISR) {
		BigDecimal excedente = null;
		excedente = baseISR.subtract(tarifaISR.getLimiteInferior()) ;
		return excedente;
	}
	
	private BigDecimal calculoISRPrevio(CatTarifaISR tarifaISR) {
		BigDecimal isrPrevio = null;
		BigDecimal porcentajeExcedente = null;
		BigDecimal cien = null;
		BigDecimal porcentaje = null;
		
		try {
			porcentajeExcedente = tarifaISR.getPorcAplExceLimInf();
			
			cien = new BigDecimal("100.00").setScale(2, BigDecimal.ROUND_HALF_UP);
			if(porcentajeExcedente.compareTo(cien) >= 0)
				porcentaje = porcentajeExcedente;
			else
				porcentaje = porcentajeExcedente.divide(cien, 4, BigDecimal.ROUND_HALF_UP);
			
			isrPrevio = this.excedente.multiply(porcentaje).setScale(2, BigDecimal.ROUND_HALF_UP);
		} catch(Exception ex) {
			log.error("Problema para obtener el ISR previo");
			isrPrevio = BigDecimal.ZERO;
		}
		
		return isrPrevio;
	}
	
	private BigDecimal calculoImpuestoAntesSubsidio(CatTarifaISR tarifaISR) {
		BigDecimal impuesto = null;
		impuesto = this.isrPrevio.add(tarifaISR.getCuotaFija());
		return impuesto;
	}
	
	private BigDecimal calculoSubsidioEmpleo(CatSubsidio tarifaSubsidio) {
		BigDecimal subsidioEmpleo = null;
		subsidioEmpleo = tarifaSubsidio.getCantidadSubsidio();
		return subsidioEmpleo;
	}
	
	//IMSS
	private BigDecimal calculoEnfermedadMaternidad() {
		BigDecimal cuota = null;
		BigDecimal cero = null;
		BigDecimal tres = null;
		BigDecimal limiteUMAs = null;
		BigDecimal excedente = null;
		BigDecimal tarifa = null;
		BigDecimal totalDiasPeriodo = null;
		CuotaIMSSDTO tarifaIMSS = null;
		
		try {
			//Constantes necesarias para el cálculo de Enfermedad y Maternidad.
			cero = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
			tres = new BigDecimal("3.00").setScale(2, BigDecimal.ROUND_HALF_UP);
			
			totalDiasPeriodo = new BigDecimal(DIAS_POR_PERIODO + SEPTIMO_DIA).setScale(2, BigDecimal.ROUND_HALF_UP);
			cuota = cero;
			
			limiteUMAs = this.uma.multiply(tres).setScale(2, BigDecimal.ROUND_HALF_UP);
			excedente = this.salarioDiarioIntegrado.subtract(limiteUMAs);
			
			if(salarioDiarioIntegrado.compareTo(limiteUMAs) >= 0) {
				
				tarifaIMSS = tarifaIMSSDAO.buscar("O", "EM1", this.fechaInicioAnio, this.fechafinAnio, tres);
				tarifa = tarifaIMSS.getCuota();
				cuota = excedente
						.multiply(tarifa).setScale(2, BigDecimal.ROUND_HALF_UP)
						.multiply(totalDiasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
		} catch(Exception ex) {
			log.warn("No fue posible calcular el excedente por Enfermedad y Maternidad.", ex);
			cuota = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		return cuota;
	}
	
	//IMSS
	private BigDecimal calculoGastosMedicosPensionadosBeneficiarios() {
		BigDecimal cuota = null;
		BigDecimal totalDiasPeriodo = null;
		CuotaIMSSDTO tarifaIMSS = null;
		
		try {
			totalDiasPeriodo = new BigDecimal(DIAS_POR_PERIODO + SEPTIMO_DIA).setScale(2, BigDecimal.ROUND_HALF_UP);
			tarifaIMSS = tarifaIMSSDAO.buscar("O", "EM2", this.fechaInicioAnio, this.fechafinAnio, this.salarioDiarioIntegrado);
			cuota = this.salarioDiarioIntegrado
					.multiply(tarifaIMSS.getCuota()).setScale(2, BigDecimal.ROUND_HALF_UP)
					.multiply(totalDiasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
			
		} catch(Exception ex) {
			log.warn("No fue posible calcular el excedente por Gastos Médicos para pensionados y beneficiarios...", ex);
			cuota = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		return cuota;
	}
	
	//IMSS
	private BigDecimal calculoEnDinero() {
		BigDecimal cuota = null;
		BigDecimal totalDiasPeriodo = null;
		CuotaIMSSDTO tarifaIMSS = null;
		
		try {
			totalDiasPeriodo = new BigDecimal(DIAS_POR_PERIODO + SEPTIMO_DIA).setScale(2, BigDecimal.ROUND_HALF_UP);
			tarifaIMSS = tarifaIMSSDAO.buscar("O", "EM3", this.fechaInicioAnio, this.fechafinAnio, this.salarioDiarioIntegrado);
			cuota = this.salarioDiarioIntegrado
					.multiply(tarifaIMSS.getCuota()).setScale(2, BigDecimal.ROUND_HALF_UP)
					.multiply(totalDiasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
			
		} catch(Exception ex) {
			log.warn("No fue posible calcular el excedente por Gastos Médicos para pensionados y beneficiarios...", ex);
			cuota = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		return cuota;
	}
	
	//IMSS
	private BigDecimal calculoInvalidezVida() {
		BigDecimal cuota = null;
		BigDecimal totalDiasPeriodo = null;
		CuotaIMSSDTO tarifaIMSS = null;
		
		try {
			totalDiasPeriodo = new BigDecimal(DIAS_POR_PERIODO + SEPTIMO_DIA).setScale(2, BigDecimal.ROUND_HALF_UP);
			tarifaIMSS = tarifaIMSSDAO.buscar("O", "IV", this.fechaInicioAnio, this.fechafinAnio, this.salarioDiarioIntegrado);
			cuota = this.salarioDiarioIntegrado
					.multiply(tarifaIMSS.getCuota()).setScale(2, BigDecimal.ROUND_HALF_UP)
					.multiply(totalDiasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
			
		} catch(Exception ex) {
			log.warn("No fue posible calcular el excedente por Gastos Médicos para pensionados y beneficiarios...", ex);
			cuota = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		return cuota;
	}
	
	//IMSS
	private BigDecimal calculoCesantiaEdadAvanzadaVejez() {
		BigDecimal cuota = null;
		BigDecimal totalDiasPeriodo = null;
		CuotaIMSSDTO tarifaIMSS = null;
		
		try {
			totalDiasPeriodo = new BigDecimal(DIAS_POR_PERIODO + SEPTIMO_DIA).setScale(2, BigDecimal.ROUND_HALF_UP);
			tarifaIMSS = tarifaIMSSDAO.buscar("O", "CEAV", this.fechaInicioAnio, this.fechafinAnio, this.salarioDiarioIntegrado);
			cuota = this.salarioDiarioIntegrado
					.multiply(tarifaIMSS.getCuota()).setScale(2, BigDecimal.ROUND_HALF_UP)
					.multiply(totalDiasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
			
		} catch(Exception ex) {
			log.warn("No fue posible calcular el excedente por Gastos Médicos para pensionados y beneficiarios...", ex);
			cuota = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		return cuota;
	}
	
	/********************TERMINAN DEDUCCIONES****************/

	public DetEmpleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(DetEmpleado empleado) {
		this.empleado = empleado;
	}

	public BigDecimal getSalarioDiario() {
		return salarioDiario;
	}

	public void setSalarioDiario(BigDecimal salarioDiario) {
		this.salarioDiario = salarioDiario;
	}

	public BigDecimal getSalarioDiarioIntegrado() {
		return salarioDiarioIntegrado;
	}

	public void setSalarioDiarioIntegrado(BigDecimal salarioDiarioIntegrado) {
		this.salarioDiarioIntegrado = salarioDiarioIntegrado;
	}

	public BigDecimal getSeptimoDia() {
		return septimoDia;
	}

	public void setSeptimoDia(BigDecimal septimoDia) {
		this.septimoDia = septimoDia;
	}

	public BigDecimal getSalarioSemanal() {
		return salarioSemanal;
	}

	public void setSalarioSemanal(BigDecimal salarioSemanal) {
		this.salarioSemanal = salarioSemanal;
	}

	public BigDecimal getBonoPuntualidad() {
		return bonoPuntualidad;
	}

	public void setBonoPuntualidad(BigDecimal bonoPuntualidad) {
		this.bonoPuntualidad = bonoPuntualidad;
	}

	public BigDecimal getValesDespensa() {
		return valesDespensa;
	}

	public void setValesDespensa(BigDecimal valesDespensa) {
		this.valesDespensa = valesDespensa;
	}

	public BigDecimal getTotalPercepciones() {
		return totalPercepciones;
	}

	public void setTotalPercepciones(BigDecimal totalPercepciones) {
		this.totalPercepciones = totalPercepciones;
	}

	public BigDecimal getDiasPeriodo() {
		return diasPeriodo;
	}

	public void setDiasPeriodo(BigDecimal diasPeriodo) {
		this.diasPeriodo = diasPeriodo;
	}

	public BigDecimal getDiasTrabajados() {
		return diasTrabajados;
	}

	public void setDiasTrabajados(BigDecimal diasTrabajados) {
		this.diasTrabajados = diasTrabajados;
	}

	public BigDecimal getDiasAsueto() {
		return diasAsueto;
	}

	public void setDiasAsueto(BigDecimal diasAsueto) {
		this.diasAsueto = diasAsueto;
	}

	public BigDecimal getDiasAusencia() {
		return diasAusencia;
	}

	public void setDiasAusencia(BigDecimal diasAusencia) {
		this.diasAusencia = diasAusencia;
	}

	public Date getPeriodoInicio() {
		return periodoInicio;
	}

	public void setPeriodoInicio(Date periodoInicio) {
		this.periodoInicio = periodoInicio;
	}

	public Date getPeriodoFin() {
		return periodoFin;
	}

	public void setPeriodoFin(Date periodoFin) {
		this.periodoFin = periodoFin;
	}

	public BigDecimal getProporcionalSeptimoDia() {
		return proporcionalSeptimoDia;
	}

	public void setProporcionalSeptimoDia(BigDecimal proporcionalSeptimoDia) {
		this.proporcionalSeptimoDia = proporcionalSeptimoDia;
	}

	public Map<String, DetRegistro> getMapAsistencias() {
		return mapAsistencias;
	}

	public void setMapAsistencias(Map<String, DetRegistro> mapAsistencias) {
		this.mapAsistencias = mapAsistencias;
	}

	public CatPercepciones getParametrosPercepciones() {
		return parametrosPercepciones;
	}

	public void setParametrosPercepciones(CatPercepciones parametrosPercepciones) {
		this.parametrosPercepciones = parametrosPercepciones;
	}

	public List<CatTarifaISR> getTablaISRSemanal() {
		return tablaISRSemanal;
	}

	public void setTablaISRSemanal(List<CatTarifaISR> tablaISRSemanal) {
		this.tablaISRSemanal = tablaISRSemanal;
	}

	public List<CatSubsidio> getTablaSubsidioSemanal() {
		return this.tablaSubsidioSemanal;
	}
	
	public void setTablaSubsidioSemanal(List<CatSubsidio> tablaSubsidioSemanal) {
		this.tablaSubsidioSemanal = tablaSubsidioSemanal;
	}

	public CatMetodoPago getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(CatMetodoPago metodoPago) {
		this.metodoPago = metodoPago;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public CatConcepto getConcepto() {
		return concepto;
	}

	public void setConcepto(CatConcepto concepto) {
		this.concepto = concepto;
	}

	public CatUnidadSAT getUnidadSAT() {
		return unidadSAT;
	}

	public void setUnidadSAT(CatUnidadSAT unidadSAT) {
		this.unidadSAT = unidadSAT;
	}

	public CatPeriodicidadPago getPeriodicidad() {
		return periodicidad;
	}

	public void setPeriodicidad(CatPeriodicidadPago periodicidad) {
		this.periodicidad = periodicidad;
	}

	public CatRegimenFiscal getRegimenFiscalReceptor() {
		return regimenFiscalReceptor;
	}

	public void setRegimenFiscalReceptor(CatRegimenFiscal regimenFiscalReceptor) {
		this.regimenFiscalReceptor = regimenFiscalReceptor;
	}

	public CatUsoCFDI getUsoCFDI() {
		return usoCFDI;
	}

	public void setUsoCFDI(CatUsoCFDI usoCFDI) {
		this.usoCFDI = usoCFDI;
	}
}
