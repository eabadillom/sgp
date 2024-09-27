package mx.com.ferbo.business;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.IMSSDeduccion;
import mx.com.ferbo.business.deduccion.ISRDeduccion;
import mx.com.ferbo.business.otropago.AjusteNetoOtroPago;
import mx.com.ferbo.business.percepcion.BonoPuntualidadPercepcion;
import mx.com.ferbo.business.percepcion.SeptimoDiaPercepcion;
import mx.com.ferbo.business.percepcion.SueldoPercepcion;
import mx.com.ferbo.business.percepcion.ValesDespensaPercepcion;
import mx.com.ferbo.dao.n.DiaNoLaboralDAO;
import mx.com.ferbo.dao.n.PrestamoDAO;
import mx.com.ferbo.dao.n.RegistroDAO;
import mx.com.ferbo.dao.n.TipoPercepcionDAO;
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
import mx.com.ferbo.model.DetNominaEmisor;
import mx.com.ferbo.model.DetNominaOtroPago;
import mx.com.ferbo.model.DetNominaPercepcion;
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
	
	private BigDecimal diasLaboralesPeriodo = null;
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
	
	//PRESTAMOS
//	private BigDecimal totalPrestamos = null;
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
    private List<CatTipoDeduccion> tiposDeduccion = null;
    private List<CatTipoOtroPago> tiposOtroPago = null;
	private BigDecimal uma = null;
	
	private TipoPercepcionDAO tipoPercepcionDAO = null;
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
		this.tipoPercepcionDAO = new TipoPercepcionDAO(CatTipoPercepcion.class);
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
		
		DetNomina nomina = null;
		DetNominaPercepcion pSueldo = null;
		DetNominaPercepcion pSeptimoDia = null;
		DetNominaPercepcion pBonoPuntualidad = null;
		DetNominaPercepcion pValeDespensa = null;
		
		DetNominaOtroPago opAjusteAlNeto = null;
		
		List<DetNominaDeduccion> listaPrestamos = null;
		
		List<DetNominaPercepcion> percepciones = null;
		List<DetNominaOtroPago> otrosPagos = null;
		List<DetNominaDeduccion> deducciones = null;
		
		SueldoPercepcion sueldoBO = null;
		SeptimoDiaPercepcion septimoDiaBO = null;
		BonoPuntualidadPercepcion bonoPuntualidadBO = null;
		ValesDespensaPercepcion valesDespensaBO = null;
		
		
		ISRDeduccion isrBO = null;
		IMSSDeduccion imssBO = null;
		
		AjusteNetoOtroPago ajusteNetoBO = null;
		
		int idxP = 0;
		int idxOP = 0;
		int idxD = 0;
		
		BigDecimal tasaBonoPuntualidad = null;
		
		try {
			log.info("Ejecutando la nomina de la semana {} del año en curso...", this.semanaAnio);
			this.uma = parametrosPercepciones.getUma();
			nomina = this.newNomina();
			percepciones = nomina.getPercepciones();
			otrosPagos = nomina.getOtrosPagos();
			deducciones = nomina.getDeducciones();
			
			this.esUltimaSemanaMes();
			
			log.debug("Buscando información empresarial del empleado.");
			
			mapAsistencias = this.getAsistencias(this.empleado, this.periodoInicio, this.periodoFin);
			diasPeriodo = new BigDecimal(DateUtils.daysDiff(periodoInicio, periodoFin)).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			//Para los días trabajados, se debe considerar el periodo inicio y fin de cálculo de la nómina y validar si de los 6 días que
			//al trabajador le corresponde laborar, tuvo alguna falta.
			diasTrabajados = this.getDiasTrabajados(mapAsistencias, DIAS_POR_PERIODO); //EL SEGUNDO PARAMETRO (6) CORRESPONDE A LOS DÍAS QUE DEBE LABORAR UN TRABAJADOR POR SEMANA.
			this.diasTrabajados = diasTrabajados;
			this.diasLaboralesPeriodo = new BigDecimal(DIAS_POR_PERIODO).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			//Para el séptimo día, se considera el salario diario (sin SDI), dividiendolo entre los días de la semana que se deben laborar,
    		//multiplicado por los días que si laboró el trabajador (parte proporcional de los días trabajados).
    		
			
    		this.salarioDiario = empleado.getSueldoDiario();
    		this.salarioDiarioIntegrado = this.calculoSDI(this.empleado);
    		sueldoBO = new SueldoPercepcion(empleado.getSueldoDiario(), diasTrabajados);
    		sueldoBO.setTipoPercepcionDAO(tipoPercepcionDAO);
    		pSueldo = sueldoBO.calcular(nomina, idxP++);
    		this.salarioSemanal = pSueldo.getImporteExcento()
    				.add(pSueldo.getImporteGravado());
    		if(this.salarioSemanal.compareTo(BigDecimal.ZERO) > 0)
    			percepciones.add(pSueldo);
    		
    		septimoDiaBO = new SeptimoDiaPercepcion(empleado.getSueldoDiario(), this.diasLaboralesPeriodo, diasTrabajados);
    		septimoDiaBO.setTipoPercepcionDAO(tipoPercepcionDAO);
    		pSeptimoDia  = septimoDiaBO.calcular(nomina, idxP++);
    		this.septimoDia = pSeptimoDia.getImporteExcento()
    				.add(pSeptimoDia.getImporteGravado());
    		if(this.septimoDia.compareTo(BigDecimal.ZERO) > 0)
    			percepciones.add(pSeptimoDia);
    		
			tasaBonoPuntualidad = this.parametrosPercepciones.getBonoPuntualidad();
			bonoPuntualidadBO = new BonoPuntualidadPercepcion(tasaBonoPuntualidad, diasTrabajados, mapAsistencias, DIAS_POR_PERIODO, salarioDiarioIntegrado, proporcionalSeptimoDia);
			bonoPuntualidadBO.setTipoPercepcionDAO(tipoPercepcionDAO);
			pBonoPuntualidad = bonoPuntualidadBO.calcular(nomina, idxP++);
			this.bonoPuntualidad = pBonoPuntualidad.getImporteExcento()
					.add(pBonoPuntualidad.getImporteGravado());
			if(this.bonoPuntualidad.compareTo(BigDecimal.ZERO) > 0)
				percepciones.add(pBonoPuntualidad);
			
			valesDespensaBO = new ValesDespensaPercepcion(diasTrabajados, parametrosPercepciones.getUma(), parametrosPercepciones.getValeDespensa(), diasPeriodo);
			pValeDespensa = valesDespensaBO.calcular(nomina, idxP++);
			this.valesDespensa = pValeDespensa.getImporteExcento()
					.add(pValeDespensa.getImporteGravado());
			if(this.valesDespensa.compareTo(BigDecimal.ZERO) > 0)
				percepciones.add(pValeDespensa);
			
			this.totalPercepciones = percepciones.stream()
					.map(item -> item.getImporteExcento().add(item.getImporteGravado()))
					.reduce(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal :: add)
					;
			
			//deducciones
			if(this.salarioSemanal.compareTo(BigDecimal.ZERO) > 0) {
				
				isrBO = new ISRDeduccion(tiposDeduccion, tiposOtroPago, percepciones, tablaISRSemanal, tablaSubsidioSemanal);
				List<DetNominaDeduccion> deduccionesISR = isrBO.calcular(nomina, idxD);
				deducciones.addAll(deduccionesISR);

				imssBO = new IMSSDeduccion(this.tiposDeduccion, this.fechaInicioAnio, this.fechafinAnio, new BigDecimal(DIAS_POR_PERIODO + SEPTIMO_DIA), this.uma, this.salarioDiarioIntegrado);
				List<DetNominaDeduccion> aportacionesIMSS = imssBO.calcular(nomina, idxD);
				deducciones.addAll(aportacionesIMSS) ;
				
				listaPrestamos = this.procesaPrestamos(this.empleado);
//				this.totalPrestamos = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
//				
//				for(DetNominaDeduccion p : listaPrestamos) {
//					this.totalPrestamos = this.totalPrestamos.add(p.getImporte());
//				}
				
			}
			
			this.totalDeducciones = deducciones.stream()
					.filter(d -> d.getProcesar())
					.map(item -> item.getImporte())
					.reduce(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal::add)
					;
			
			this.neto = this.totalPercepciones.subtract(totalDeducciones);
			
			
			//NUEVA NOMINA...........................................
			nomina.setFechaEmision(new Date());
			nomina.setClaveExportacion("01");
			nomina.setTipoComprobante("N");
			nomina.setMoneda("MXN");
			nomina.setMetodoPago(this.metodoPago);
			nomina.setSerie(String.format("%d", this.anio));
			nomina.setFolio(String.format("%d", this.semanaAnio));
			nomina.setLugarExpedicion(this.empleado.getDatoEmpresa().getEmpresa().getCodigoPostal());
			nomina.setEjercicio(DateUtils.getAnio(this.fechaInicioAnio));
			nomina.setDiasLaborados(this.diasTrabajados.intValue());
			nomina.setDiasNoLaborados(this.diasLaboralesPeriodo.subtract(diasTrabajados).intValue());
			nomina.setPeriodo(DateUtils.getSemanaAnio(this.fechaInicioAnio));
			nomina.setSubtotal(this.totalPercepciones);
			nomina.setDescuento(this.totalDeducciones);
			nomina.setTotal(this.neto);
			nomina.setPeriodoInicio(this.periodoInicio.toInstant().atZone(ZoneId.of("GMT-6")).toLocalDate());
			nomina.setPeriodoFin(this.periodoFin.toInstant().atZone(ZoneId.of("GMT-6")).toLocalDate());
			
			DetNominaEmisor emisor = this.getEmisor(nomina);
			log.info("Emisor: {}", emisor.getNombre());
			
			DetNominaReceptor receptor = this.getNominaReceptor(nomina);
			log.info("Receptor: {}", receptor.getNombre());
					
			DetNominaConcepto concepto = new DetNominaConcepto();
			concepto.setKey(new DetNominaConceptoPK(nomina, 0));
			concepto.setConcepto(this.concepto);
			concepto.setCantidad(new BigDecimal("1").setScale(2, BigDecimal.ROUND_HALF_UP));
			concepto.setUnidad(this.unidadSAT);
			concepto.setNombreConcepto("Pago de nómina");
			concepto.setObjetoImpuesto("01");
			concepto.setValorUnitario(this.totalPercepciones);
			concepto.setImporte(this.totalPercepciones);
			concepto.setDescuento(this.totalDeducciones);
			nomina.getConceptos().add(concepto);
			
			//OTROS PAGOS...
			ajusteNetoBO = new AjusteNetoOtroPago();
			opAjusteAlNeto = ajusteNetoBO.calcular(nomina, idxOP++);
			otrosPagos.add(opAjusteAlNeto);
			deducciones.addAll(listaPrestamos);
			
		} catch(Exception ex) {
			log.error("Problema para obtener el cálculo de la nómina del empleado {} {} {}", empleado.getNombre(), empleado.getPrimerAp(), empleado.getSegundoAp() );
			log.error(ex);
		}
		
		return nomina;
	}
	
	private DetNominaReceptor getNominaReceptor(DetNomina nomina) {
		DetNominaReceptor receptor = null;
		try {
			if(nomina.getReceptor() == null)
				nomina.setReceptor(new DetNominaReceptor());
			
			receptor = nomina.getReceptor();
			receptor.setNomina(nomina);
			receptor.setNombre(String.format("%s %s %s", this.empleado.getNombre(), this.empleado.getPrimerAp(), this.empleado.getSegundoAp()).trim());
			receptor.setRfc(this.empleado.getRfc());
			receptor.setCodigoPostal(this.empleado.getDatoEmpresa().getCodigoPostal());
			receptor.setRegimenFiscal(this.regimenFiscalReceptor);
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
			
		} catch(Exception ex) {
			receptor = new DetNominaReceptor();
			receptor.setNomina(nomina);
		}
		return receptor;
	}

	private DetNominaEmisor getEmisor(DetNomina nomina) {
		DetNominaEmisor emisor = null;
		
		try {
			if(nomina.getEmisor() == null)
				nomina.setEmisor(new DetNominaEmisor());
			emisor = nomina.getEmisor();
			emisor.setNomina(nomina);
			emisor.setNombre(this.empleado.getDatoEmpresa().getEmpresa().getRazonSocial());
			emisor.setRfc(this.empleado.getDatoEmpresa().getEmpresa().getRfc());
			emisor.setCodigoPostal(this.empleado.getDatoEmpresa().getEmpresa().getCodigoPostal());
			emisor.setRegistroPatronal(this.empleado.getDatoEmpresa().getEmpresa().getRegistroPatronal());
			emisor.setRegimenFiscal(this.empleado.getDatoEmpresa().getEmpresa().getRegimenFiscal());
		} catch(Exception ex) {
			emisor = new DetNominaEmisor();
			emisor.setNomina(nomina);
		}
		
		return emisor;
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
				deduccion.setProcesar(true);
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
		return diasLaboralesPeriodo;
	}

	public void setDiasPeriodo(BigDecimal diasPeriodo) {
		this.diasLaboralesPeriodo = diasPeriodo;
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

	public void setTiposDeduccion(List<CatTipoDeduccion> tiposDeduccion) {
		this.tiposDeduccion = tiposDeduccion;
	}

	public void setTiposOtroPago(List<CatTipoOtroPago> tiposOtroPago) {
		this.tiposOtroPago = tiposOtroPago;
	}
}
