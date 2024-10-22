package mx.com.ferbo.business;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AjusteAlNetoDeduccion;
import mx.com.ferbo.business.deduccion.IMSSDeduccion;
import mx.com.ferbo.business.deduccion.ISRSemanalDeduccion;
import mx.com.ferbo.business.deduccion.PrestamoDeduccion;
import mx.com.ferbo.business.otropago.AjusteAlNetoOtroPago;
import mx.com.ferbo.business.percepcion.BonoPuntualidadPercepcion;
import mx.com.ferbo.business.percepcion.SeptimoDiaPercepcion;
import mx.com.ferbo.business.percepcion.SueldoPercepcion;
import mx.com.ferbo.business.percepcion.ValesDespensaPercepcion;
import mx.com.ferbo.dao.n.NominaDAO;
import mx.com.ferbo.dao.n.PrestamoDAO;
import mx.com.ferbo.dao.n.RegistroDAO;
import mx.com.ferbo.model.CatCuotaIMSS;
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
import mx.com.ferbo.util.DateUtilsException;
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
	
	private BigDecimal diasTrabajados = null;
	private BigDecimal diasAsueto = null;
	private BigDecimal diasAusencia = null;
	private Map<String, DetRegistro> mapAsistencias = null;
	
	//PERCEPCIONES
	private BigDecimal proporcionalSeptimoDia = null;
	private BigDecimal valesDespensa = null;
	
	private BigDecimal totalPercepciones = null;
	private BigDecimal totalDeducciones = null;
	
    private static final int SEPTIMO_DIA = 1;
    private static final int DIAS_ANIO = 365;
    private static final int DIAS_POR_PERIODO = 6;
    
    private CatPercepciones parametrosPercepciones = null;
    private List<CatDiaNoLaboral> diasNoLaborales = null;
    private List<CatTarifaISR> tablaISRSemanal = null;
    private List<CatTarifaISR> tablaISRMensual = null;
    private List<CatSubsidio> tablaSubsidioSemanal = null;
    private List<CatSubsidio> tablaSubsidioMensual = null;
    private List<CatTipoPercepcion> tiposPercepcion = null;
    private List<CatTipoDeduccion> tiposDeduccion = null;
    private List<CatCuotaIMSS> cuotasIMSS = null;
    private List<CatTipoOtroPago> tiposOtroPago = null;
	private BigDecimal uma = null;
	
	private PrestamoDAO prestamoDAO = null;
	private CatMetodoPago metodoPago = null;
	private CatConcepto concepto = null;
	private CatUnidadSAT unidadSAT = null;
	private CatPeriodicidadPago periodicidad = null;
	private CatRegimenFiscal regimenFiscalReceptor = null;
	private CatUsoCFDI usoCFDI = null;
	private Integer anio = null;
	
	private NominaDAO nominaDAO = null;
	
	
	//OBJETOS RELACIONADOS A LA NOMINA Y CFDI
	public NominaSemanalBL(DetEmpleado empleado, Date periodoInicio, Date periodoFin) {
		Integer anioActual = null;
		
		this.empleado = empleado;
		this.periodoInicio = periodoInicio;
		this.periodoFin = periodoFin;
		this.prestamoDAO = new PrestamoDAO();
		this.nominaDAO = new NominaDAO();
		
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
		BigDecimal salarioDiarioIntegrado = null;
		BigDecimal salarioSemanal = null;
		BigDecimal septimoDia = null;
		
		BigDecimal diasLaboralesPeriodo = null;
		BigDecimal neto = null;
		
		DetNomina nomina = null;
		DetNominaPercepcion pSueldo = null;
		DetNominaPercepcion pSeptimoDia = null;
		DetNominaPercepcion pBonoPuntualidad = null;
		DetNominaPercepcion pValeDespensa = null;
		
		List<DetNominaDeduccion> prestamos = null;
		
		List<DetNominaPercepcion> percepciones = null;
		List<DetNominaDeduccion> deducciones = null;
		
		SueldoPercepcion sueldoBO = null;
		SeptimoDiaPercepcion septimoDiaBO = null;
		BonoPuntualidadPercepcion bonoPuntualidadBO = null;
		ValesDespensaPercepcion valesDespensaBO = null;
		
		
		ISRSemanalDeduccion isrBO = null;
		IMSSDeduccion imssBO = null;
		PrestamoDeduccion prestamosBO = null;
		
		Integer idxP = 0;
		Integer idxD = 0;
		
		BigDecimal tasaBonoPuntualidad = null;
		
		try {
			log.info("#############################################################################");
			log.info("Ejecutando la nomina de la semana {} del año en curso...", this.semanaAnio);
			this.uma = parametrosPercepciones.getUma();
			nomina = this.newNomina();
			percepciones = nomina.getPercepciones();
			deducciones = nomina.getDeducciones();
			
			this.ultimaSemanaMes = this.esUltimaSemanaMes();
			
			log.debug("Buscando información empresarial del empleado.");
			
			mapAsistencias = this.getAsistencias(this.empleado, this.periodoInicio, this.periodoFin);
			diasPeriodo = new BigDecimal(DateUtils.daysDiff(periodoInicio, periodoFin)).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			//Para los días trabajados, se debe considerar el periodo inicio y fin de cálculo de la nómina y validar si de los 6 días que
			//al trabajador le corresponde laborar, tuvo alguna falta.
			diasTrabajados = this.getDiasTrabajados(mapAsistencias, DIAS_POR_PERIODO); //EL SEGUNDO PARAMETRO (6) CORRESPONDE A LOS DÍAS QUE DEBE LABORAR UN TRABAJADOR POR SEMANA.
			diasLaboralesPeriodo = new BigDecimal(DIAS_POR_PERIODO).setScale(2, BigDecimal.ROUND_HALF_UP);
			
    		salarioDiarioIntegrado = this.calculoSDI(this.empleado);
    		
    		/*---------------------------PERCEPCIONES------------------------------*/
    		sueldoBO = new SueldoPercepcion(this.tiposPercepcion, this.empleado.getDatoEmpresa().getSalarioDiario(), diasTrabajados);
    		pSueldo = sueldoBO.calcular(nomina, idxP++);
    		salarioSemanal = pSueldo.getImporteExcento()
    				.add(pSueldo.getImporteGravado());
    		if(salarioSemanal.compareTo(BigDecimal.ZERO) > 0)
    			percepciones.add(pSueldo);
    		
    		//Para el séptimo día, se considera el salario diario (sin SDI), dividiendolo entre los días de la semana que se deben laborar,
    		//multiplicado por los días que si laboró el trabajador (parte proporcional de los días trabajados).
    		septimoDiaBO = new SeptimoDiaPercepcion(this.tiposPercepcion, this.empleado.getDatoEmpresa().getSalarioDiario(), diasLaboralesPeriodo, diasTrabajados);
    		pSeptimoDia  = septimoDiaBO.calcular(nomina, idxP++);
    		septimoDia = pSeptimoDia.getImporteExcento()
    				.add(pSeptimoDia.getImporteGravado());
    		if(septimoDia.compareTo(BigDecimal.ZERO) > 0)
    			percepciones.add(pSeptimoDia);
    		
			tasaBonoPuntualidad = this.parametrosPercepciones.getBonoPuntualidad();
			bonoPuntualidadBO = new BonoPuntualidadPercepcion(this.tiposPercepcion, tasaBonoPuntualidad, diasTrabajados, mapAsistencias, DIAS_POR_PERIODO, salarioDiarioIntegrado, proporcionalSeptimoDia);
			bonoPuntualidadBO.setPercepcionesEmpleado(this.empleado.getPercepcionesEmpleado());
			pBonoPuntualidad = bonoPuntualidadBO.calcular(nomina, idxP++);
			if(pBonoPuntualidad.getImporteExcento().add(pBonoPuntualidad.getImporteGravado()).compareTo(BigDecimal.ZERO) > 0) //Si el hay bono de puntualidad, se agrega a la lista de percepciones.
				percepciones.add(pBonoPuntualidad);
			
			valesDespensaBO = new ValesDespensaPercepcion(this.tiposPercepcion, diasTrabajados, parametrosPercepciones.getUma(), parametrosPercepciones.getValeDespensa(), diasPeriodo);
			valesDespensaBO.setPercepcionesEmpleado(this.empleado.getPercepcionesEmpleado());
			pValeDespensa = valesDespensaBO.calcular(nomina, idxP++);
			if(pValeDespensa.getImporteExcento().add(pValeDespensa.getImporteGravado()).compareTo(BigDecimal.ZERO) > 0) //Si hay vales de desapensa, se agregan a la lista de percepciones.
				percepciones.add(pValeDespensa);
			
			
			/*-------------------------DEDUCCIONES-----------------------*/
			if(salarioSemanal.compareTo(BigDecimal.ZERO) > 0) {
				
				isrBO = new ISRSemanalDeduccion(this.tiposDeduccion, this.tiposOtroPago, percepciones, this.tablaISRSemanal, this.tablaSubsidioSemanal);
				isrBO.setPeriodo(this.periodoFin, this.periodoFin);
				isrBO.setListaNominaMes( this.ultimaSemanaMes ? procesaNominaDelMes() : null);
				List<DetNominaDeduccion> deduccionesISR = isrBO.calcular(nomina, idxD);
				deducciones.addAll(deduccionesISR);

				imssBO = new IMSSDeduccion(this.tiposDeduccion, this.cuotasIMSS, this.fechaInicioAnio, this.fechafinAnio, new BigDecimal(DIAS_POR_PERIODO + SEPTIMO_DIA), this.uma, salarioDiarioIntegrado);
				List<DetNominaDeduccion> aportacionesIMSS = imssBO.calcular(nomina, idxD);
				deducciones.addAll(aportacionesIMSS) ;
				
				prestamosBO = new PrestamoDeduccion(this.empleado);
				prestamos = prestamosBO.calcular(nomina, idxD);
				deducciones.addAll(prestamos);
			}
			
			for(DetNominaPercepcion p : percepciones) {
				log.info("Percepcion: {} - {} - {}", p.getNombre(), p.getImporteExcento(), p.getImporteGravado());
			}
			
			for(DetNominaDeduccion d : deducciones) {
				log.info("Deduccion: {} - {}", d.getNombre(), d.getImporte());
			}
			
			neto = this.calcularTotal(nomina);
			
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
			nomina.setDiasLaborados(diasTrabajados.intValue());
			nomina.setDiasNoLaborados(diasLaboralesPeriodo.subtract(diasTrabajados).intValue());
			nomina.setSubtotal(this.totalPercepciones);
			nomina.setDescuento(this.totalDeducciones);
			nomina.setTotal(neto);
			nomina.setPeriodo(this.semanaAnio);
			nomina.setPeriodoInicio(this.periodoInicio.toInstant().atZone(ZoneId.of("GMT-6")).toLocalDate());
			nomina.setPeriodoFin(this.periodoFin.toInstant().atZone(ZoneId.of("GMT-6")).toLocalDate());
			
			DetNominaEmisor emisor = this.getEmisor(nomina);
			log.info("Emisor: {}", emisor.getNombre());
			
			DetNominaReceptor receptor = this.getNominaReceptor(nomina, salarioDiarioIntegrado);
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
		} catch(Exception ex) {
			log.error("Problema para obtener el cálculo de la nómina del empleado {} {} {}", empleado.getNombre(), empleado.getPrimerAp(), empleado.getSegundoAp() );
			log.error(ex);
		} finally {
			log.info("-----------------------------------------------------------------------------");
		}
		
		return nomina;
	}
	
	private BigDecimal calcularTotal(DetNomina nomina) {
		BigDecimal total = null;
		BigDecimal totalPercepciones = null;
		BigDecimal totalDeducciones = null;
		BigDecimal previoNeto = null;
		BigDecimal neto = null;
		BigDecimal ajusteAlNeto = null;
		
		AjusteAlNetoOtroPago opAjusteNetoBO = null;
		DetNominaOtroPago opAjusteAlNeto = null;
		
		AjusteAlNetoDeduccion dAjusteNetoBO = null;
		DetNominaDeduccion dAjusteAlNeto = null;
		
		List<DetNominaPercepcion> percepciones = null;
		List<DetNominaDeduccion> deducciones = null;
		List<DetNominaOtroPago> otrosPagos = null;
		
		try {
			log.info("Calculando subtotal, descuentos y total...");
			percepciones = nomina.getPercepciones();
			deducciones = nomina.getDeducciones();
			otrosPagos = nomina.getOtrosPagos();
			
			totalPercepciones = percepciones.stream()
			.map(item -> item.getImporteExcento().add(item.getImporteGravado()))
			.reduce(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal :: add)
			;
			
			this.totalPercepciones = totalPercepciones;
			
			
			totalDeducciones = deducciones.stream()
					.filter(d -> d.getProcesar())
					.map(item -> item.getImporte())
					.reduce(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal::add)
					;
			
			this.totalDeducciones = totalDeducciones;
			
			previoNeto = totalPercepciones.subtract(totalDeducciones).setScale(1, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
			neto = totalPercepciones.subtract(totalDeducciones);
			ajusteAlNeto = previoNeto.subtract(neto);
			
			total = neto.add(ajusteAlNeto);
			
			if(ajusteAlNeto.compareTo(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP)) < 0 ) {
				DetNominaDeduccion maxD = Collections.max(deducciones, Comparator.comparing(d -> d.getKey().getId()));
				dAjusteNetoBO = new AjusteAlNetoDeduccion(ajusteAlNeto.abs());
				dAjusteNetoBO.setTiposDeduccion(tiposDeduccion);
				dAjusteAlNeto = dAjusteNetoBO.calcular(nomina, maxD.getKey().getId() + 1);
				deducciones.add(dAjusteAlNeto);
				log.info("Aplicando ajuste al neto como deduccion: {}", dAjusteAlNeto);
			} else if(ajusteAlNeto.compareTo(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP)) > 0) {
				Integer index = null;
				opAjusteNetoBO = new AjusteAlNetoOtroPago(ajusteAlNeto);
				opAjusteNetoBO.setTiposOtroPago(tiposOtroPago);
				index = opAjusteNetoBO.nuevoIndiceDe(otrosPagos);
				opAjusteAlNeto = opAjusteNetoBO.calcular(nomina, index);
				otrosPagos.add(opAjusteAlNeto);
				log.info("Aplicando ajuste al neto como otro pago: {}", opAjusteAlNeto);
			}
			
			log.info("Neto previo: {}, neto: {}, ajuste al neto: {}, neto ajustado: {}", previoNeto, neto, ajusteAlNeto, total);
		} catch(Exception ex) {
			log.error("Problema para obtener el neto...", ex);
			totalPercepciones = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		return total;
	}
	
	private DetNominaReceptor getNominaReceptor(DetNomina nomina, BigDecimal sdi) {
		DetNominaReceptor receptor = null;
		try {
			if(nomina.getReceptor() == null)
				nomina.setReceptor(new DetNominaReceptor());
			
			receptor = nomina.getReceptor();
			receptor.setNomina(nomina);
			receptor.setNombre(String.format("%s %s %s", this.empleado.getNombre(), this.empleado.getPrimerAp(), this.empleado.getSegundoAp()).trim());
			receptor.setRfc(this.empleado.getDatoEmpresa().getRfc());
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
			receptor.setSalarioDiario(this.empleado.getDatoEmpresa().getSalarioDiario());
			receptor.setSalarioDiarioIntegrado(sdi);
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
	
	private Boolean esUltimaSemanaMes() {
		Boolean ultimaSemanaMes = null;
		
		Integer mesActual = DateUtils.getMes(this.periodoFin);
		Integer mesSiguiente = DateUtils.getMes(this.periodoSiguienteFin);
		
		if(mesSiguiente > mesActual) {
			ultimaSemanaMes = new Boolean(true);
			log.info("ULTIMA SEMANA DEL MES: {} - {}", this.periodoInicio, this.periodoFin);
		} else {
			ultimaSemanaMes = new Boolean(false);
		}
		
		return ultimaSemanaMes;
	}
	
	public static synchronized Boolean esUltimaSemanaMes(Date periodoInicio, Date periodoFin) {
		Boolean ultimaSemanaMes = null;
		Integer mesActualInicio = null;
		Integer mesActualFin = null;
		Date periodoSiguienteInicio = null;
		Date periodoSiguienteFin;
		Integer mesSiguienteInicio = null;
		Integer mesSiguienteFin = null;
		Integer diaInicioMes = null;
		
		try {
			//PRIMERO SE VERIFICA SI EL INICIO DE LA SEMANA COINCIDE CON EL PRIMER DIA DEL MES. 
			diaInicioMes = DateUtils.getDia(periodoInicio);
			
			if(diaInicioMes.compareTo(new Integer(1)) == 0)
				throw new SGPException("La semana en curso es la primera del mes.");
			
			//SI NO, SE VERIFICA SI EL MES DEL PRIMER DIA DE LA SEMANA EN CURSO ES DIFERENTE AL MES DEL ÚLTIMO DIA DE LA SEMANA EN CURSO.
			//SI EL PRIMER DIA DEL PERIODO ES DE UN MES DIFERENTE AL ÚLTIMO DIA DEL PERIODO, ENTONCES, LA SEMANA NO ES LA ÚLTIMA.
			mesActualInicio = DateUtils.getMes(periodoInicio);
			mesActualFin = DateUtils.getMes(periodoFin);
			
			if(mesActualInicio.compareTo(mesActualFin) != 0)
				throw new SGPException("La semana actual termina en un mes diferente al inicial.");
			
			//SI NO, CALCULAMOS LAS FECHAS DE INICIO Y FIN DE LA SIGUIENTE SEMANA Y REPETIMOS LA EVALUACIÓN ANTERIOR. 
			periodoSiguienteInicio = DateUtils.addDay(periodoInicio, 7);
			periodoSiguienteFin = DateUtils.addDay(periodoFin, 7);
			
			mesSiguienteInicio = DateUtils.getMes(periodoSiguienteInicio);
			mesSiguienteFin = DateUtils.getMes(periodoSiguienteFin);
			
			if(mesSiguienteInicio.compareTo(mesSiguienteFin) != 0) {
				ultimaSemanaMes = new Boolean(true);
				log.info("ULTIMA SEMANA DEL MES: {} - {}", periodoInicio, periodoFin);
			} else {
				ultimaSemanaMes = new Boolean(false);
			}
			
		} catch(Exception ex) {
			ultimaSemanaMes = new Boolean(false);
		}
		
		return ultimaSemanaMes;
	}
	
	private List<DetNomina> procesaNominaDelMes() {
		List<DetNomina> listaNominaDelMes = null;
		Date dPeriodoInicio = new Date(this.periodoInicio.getTime());
		Date dPeriodoFin = new Date(this.periodoFin.getTime());
		Date dPeriodoAnteriorFin = null;
		Date dPeriodoAnteriorInicio = null;
		
		Integer mesActual = null;
		Integer mesAnterior = null;
		
		LocalDate periodoAnteriorInicio = null;
		LocalDate periodoAnteriorFin = null;
		
		Integer semanaInicio = null;
		Integer semanaFin = null;
		
		//Obtener la fecha inicio de la primera semana del mes
		for(int i = 0; i < 6; i++) {
			dPeriodoAnteriorInicio = DateUtils.addDay(dPeriodoInicio, (-7 * i));
			
			if(DateUtils.getDia(dPeriodoAnteriorInicio) == 1)
				break;
			
			mesActual = DateUtils.getMes(dPeriodoInicio);
			mesAnterior = DateUtils.getMes(dPeriodoAnteriorInicio);
			
			if(mesAnterior.equals(mesActual) == false)
				break;
		}
		
		dPeriodoAnteriorFin = DateUtils.addDay(dPeriodoInicio, -1);
		
		log.info("Primera semana del mes: {} - {}", 
				DateUtils.getString(dPeriodoAnteriorInicio, DateUtils.FORMATO_DD_MM_YYYY),
				DateUtils.getString(dPeriodoAnteriorFin, DateUtils.FORMATO_DD_MM_YYYY));
		
		semanaInicio = DateUtils.getSemanaAnio(dPeriodoAnteriorInicio);
		semanaFin = DateUtils.getSemanaAnio(dPeriodoAnteriorFin);
		
		log.info("Búsqueda de la semana {} a {}", semanaInicio, semanaFin);
		
		periodoAnteriorInicio = DateUtils.toLocalDate(dPeriodoAnteriorInicio);
		periodoAnteriorFin = DateUtils.toLocalDate(dPeriodoFin );
		
		listaNominaDelMes = nominaDAO.buscarPorSemanaRfc(semanaInicio, semanaFin, this.empleado.getDatoEmpresa().getRfc());
//		listaNominaDelMes = nominaDAO.buscarPorPeriodoEmpleado(periodoAnteriorInicio, periodoAnteriorFin, this.empleado.getDatoEmpresa().getRfc());
		
		return listaNominaDelMes;
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
		RegistroDAO registroDAO = null;
		String diaSemana = null;
		Date diaNLEntrada = null;
		Date diaNLSalida = null;
		
		mapAsistencias = new HashMap<String, DetRegistro>();
		registroDAO = new RegistroDAO();
		listaAsistencias = registroDAO.buscar(empleado.getIdEmpleado(), periodoInicio, periodoFin);
		for(DetRegistro registro : listaAsistencias) {
			diaSemana = DateUtils.getDiaSemana(registro.getFechaEntrada());
			if(mapAsistencias.containsKey(diaSemana))
				continue;
			mapAsistencias.put(diaSemana, registro);
		}
		
		for(CatDiaNoLaboral dia : this.diasNoLaborales) {
			log.info("Dia no laboral encontrado: {}", dia);
			diaSemana = DateUtils.getDiaSemana(dia.getFecha());
			if(mapAsistencias.containsKey(diaSemana)) {
				//TODO EL TRABAJADOR TIENE ASISTENCIA EN UN DIA NO LABORABLE. SE DEBEN AGREGAR HORAS EXTRAS AL DOBLE O TRIPLE.
				continue;
			}
			
			diaNLEntrada = new Date(dia.getFecha().getTime());
			diaNLSalida = new Date(dia.getFecha().getTime());
			
			mapAsistencias.put(diaSemana, new DetRegistro(null, diaNLEntrada, diaNLSalida, -1, "DIA NO LABORAL"));
		}
		
		log.info("Mapa de asistencias: {}", mapAsistencias);
		
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
    		sueldoDiario = empleado.getDatoEmpresa().getSalarioDiario();
    		
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

	public void setTiposPercepcion(List<CatTipoPercepcion> tiposPercepcion) {
		this.tiposPercepcion = tiposPercepcion;
	}

	public void setDiasNoLaborales(List<CatDiaNoLaboral> diasNoLaborales) {
		this.diasNoLaborales = diasNoLaborales;
	}

	public void setCuotasIMSS(List<CatCuotaIMSS> cuotasIMSS) {
		this.cuotasIMSS = cuotasIMSS;
	}

	public void setTablaISRMensual(List<CatTarifaISR> tablaISRMensual) {
		this.tablaISRMensual = tablaISRMensual;
	}

	public void setTablaSubsidioMensual(List<CatSubsidio> tablaSubsidioMensual) {
		this.tablaSubsidioMensual = tablaSubsidioMensual;
	}
}
