package mx.com.ferbo.business.nomina;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AbstractDeduccion;
import mx.com.ferbo.business.deduccion.AjusteAlNetoDeduccion;
import mx.com.ferbo.business.deduccion.IDeducciones;
import mx.com.ferbo.business.deduccion.PrestamoDeduccion;
import mx.com.ferbo.business.deduccion.imss.IMSSDeduccion;
import mx.com.ferbo.business.deduccion.isr.ISRExecutor;
import mx.com.ferbo.business.otropago.AbstractOtroPago;
import mx.com.ferbo.business.otropago.AjusteAlNetoOtroPago;
import mx.com.ferbo.business.percepcion.BonoPuntualidadPercepcion;
import mx.com.ferbo.business.percepcion.SeptimoDiaPercepcion;
import mx.com.ferbo.business.percepcion.SueldoPercepcion;
import mx.com.ferbo.business.percepcion.ValesDespensaPercepcion;
import mx.com.ferbo.dao.n.NominaDAO;
import mx.com.ferbo.dao.n.PercepcionEmpleadoDAO;
import mx.com.ferbo.dao.n.RegistroDAO;
import mx.com.ferbo.model.CatDiaNoLaboral;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaConcepto;
import mx.com.ferbo.model.DetNominaConceptoPK;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaEmisor;
import mx.com.ferbo.model.DetNominaOtroPago;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaReceptor;
import mx.com.ferbo.model.DetPercepcionEmpleado;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

public class NominaSemanalBL extends NominaBL {
	private static Logger log = LogManager.getLogger(NominaSemanalBL.class);
	
	private DetEmpleado empleado = null;
	private Date periodoInicio = null;
	private Date periodoFin = null;
	private Date fechaInicioAnio = null;
	private Date fechafinAnio = null;
	private Integer semanaAnio = null;
	
	private BigDecimal diasTrabajados = null;
	private BigDecimal diasAsueto = null;
	private BigDecimal diasAusencia = null;
	private Map<String, DetRegistro> mapAsistencias = null;
	
	//PERCEPCIONES
	private BigDecimal proporcionalSeptimoDia = null;
	private BigDecimal valesDespensa = null;
	
	private BigDecimal totalPercepciones = null;
	private BigDecimal totalOtrosPagos = null;
	private BigDecimal totalDeducciones = null;
	
    private static final int SEPTIMO_DIA = 1;
    private static final int DIAS_ANIO = 365;
    private static final int DIAS_POR_PERIODO = 6;
    
    private ParametrosNomina parametros = null;
	private BigDecimal uma = null;
	
	private Integer anio = null;
	
	private PercepcionEmpleadoDAO percepcionEmpleadoDAO = null;
	
	
	public NominaSemanalBL(DetEmpleado empleado, Date periodoInicio, Date periodoFin) {
		Integer anioActual = null;
		
		this.empleado = empleado;
		this.periodoInicio = periodoInicio;
		this.periodoFin = periodoFin;
		this.percepcionEmpleadoDAO = new PercepcionEmpleadoDAO();
		
		anioActual = DateUtil.getAnio(periodoInicio);
		this.fechaInicioAnio = DateUtil.getDate(anioActual, DateUtil.ENERO, 1);
		DateUtil.setTime(this.fechaInicioAnio, 0, 0, 0, 0);
		
		this.fechafinAnio = DateUtil.getDate(anioActual, DateUtil.DICIEMBRE, 31);
		DateUtil.setTime(this.fechafinAnio, 23, 59, 59, 000);
		
		this.semanaAnio = DateUtil.getSemanaAnio(this.periodoInicio);
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
		
		List<DetPercepcionEmpleado> percepcionesEmpleado = null;
		
		List<DetNominaPercepcion> percepciones = null;
		List<DetNominaOtroPago> otrosPagos = null;
		List<DetNominaDeduccion> deducciones = null;
		
		SueldoPercepcion sueldoBO = null;
		SeptimoDiaPercepcion septimoDiaBO = null;
		BonoPuntualidadPercepcion bonoPuntualidadBO = null;
		ValesDespensaPercepcion valesDespensaBO = null;
		
		IMSSDeduccion imssBO = null;
		PrestamoDeduccion prestamosBO = null;
		
		Integer idxP = 0;
		
		BigDecimal tasaBonoPuntualidad = null;
		
		try {
			log.info("#############################################################################");
			log.info("Empleado: {} {} {}, Salario diario: {}", empleado.getNombre(), empleado.getPrimerAp(), empleado.getSegundoAp(), empleado.getDatoEmpresa().getSalarioDiario());
			log.info("Ejecutando la nomina de la semana {} del año en curso...", this.semanaAnio);
			nomina =  NominaBL.build();
			
			//TODO Cambiar UMA por CAT_UMA;
			this.uma = this.parametros.getParametrosPercepciones().getUma();
			percepciones = nomina.getPercepciones();
			otrosPagos = nomina.getOtrosPagos();
			deducciones = nomina.getDeducciones();
			
			
			
			log.debug("Buscando información empresarial del empleado.");
			
			mapAsistencias = this.getAsistencias(this.empleado, this.periodoInicio, this.periodoFin);
			diasPeriodo = new BigDecimal(DateUtil.daysDiff(periodoInicio, periodoFin)).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			//Para los días trabajados, se debe considerar el periodo inicio y fin de cálculo de la nómina y validar si de los 6 días que
			//al trabajador le corresponde laborar, tuvo alguna falta.
			diasTrabajados = this.getDiasTrabajados(mapAsistencias, DIAS_POR_PERIODO); //EL SEGUNDO PARAMETRO (6) CORRESPONDE A LOS DÍAS QUE DEBE LABORAR UN TRABAJADOR POR SEMANA.
			diasLaboralesPeriodo = new BigDecimal(DIAS_POR_PERIODO).setScale(2, BigDecimal.ROUND_HALF_UP);
			
    		salarioDiarioIntegrado = this.calculoSDI(this.empleado);
    		
    		/*---------------------------PERCEPCIONES------------------------------*/
    		sueldoBO = new SueldoPercepcion(this.parametros.getTiposPercepcion(), this.empleado.getDatoEmpresa().getSalarioDiario(), diasTrabajados);
    		pSueldo = sueldoBO.calcular(nomina, idxP++);
    		salarioSemanal = pSueldo.getImporteExcento()
    				.add(pSueldo.getImporteGravado());
    		if(salarioSemanal.compareTo(BigDecimal.ZERO) > 0)
    			percepciones.add(pSueldo);
    		
    		//Para el séptimo día, se considera el salario diario (sin SDI), dividiendolo entre los días de la semana que se deben laborar,
    		//multiplicado por los días que si laboró el trabajador (parte proporcional de los días trabajados).
    		septimoDiaBO = new SeptimoDiaPercepcion(this.parametros.getTiposPercepcion(), this.empleado.getDatoEmpresa().getSalarioDiario(), diasLaboralesPeriodo, diasTrabajados);
    		pSeptimoDia  = septimoDiaBO.calcular(nomina, idxP++);
    		septimoDia = pSeptimoDia.getImporteExcento()
    				.add(pSeptimoDia.getImporteGravado());
    		if(septimoDia.compareTo(BigDecimal.ZERO) > 0)
    			percepciones.add(pSeptimoDia);
    		
    		percepcionesEmpleado = percepcionEmpleadoDAO.buscarPorEmpleado(this.empleado.getIdEmpleado());
    		
    		Optional<DetPercepcionEmpleado> optPercepcion010 = percepcionesEmpleado.stream().filter(p -> p.getTipoPercepcion().getClave().equalsIgnoreCase("010")).findFirst();
    		
    		if(optPercepcion010.isPresent() && optPercepcion010.get().getActivo()) {
    			tasaBonoPuntualidad = this.parametros.getParametrosPercepciones().getBonoPuntualidad();
    			bonoPuntualidadBO = new BonoPuntualidadPercepcion(this.parametros.getTiposPercepcion(), tasaBonoPuntualidad, diasTrabajados, mapAsistencias, DIAS_POR_PERIODO, salarioDiarioIntegrado, proporcionalSeptimoDia);
    			bonoPuntualidadBO.setPercepcionesEmpleado(this.empleado.getPercepcionesEmpleado());
    			pBonoPuntualidad = bonoPuntualidadBO.calcular(nomina, idxP++);
    			if(pBonoPuntualidad.getImporteExcento().add(pBonoPuntualidad.getImporteGravado()).compareTo(BigDecimal.ZERO) > 0) //Si el hay bono de puntualidad, se agrega a la lista de percepciones.
    				percepciones.add(pBonoPuntualidad);
    		}
    			
    		Optional<DetPercepcionEmpleado> optPercepcion029 = percepcionesEmpleado.stream().filter(p -> p.getTipoPercepcion().getClave().equalsIgnoreCase("029")).findFirst();
    		
			if(optPercepcion029.isPresent() && optPercepcion029.get().getActivo()) {
				valesDespensaBO = new ValesDespensaPercepcion(this.parametros.getTiposPercepcion(), diasTrabajados, this.parametros.getParametrosPercepciones().getUma(), this.parametros.getParametrosPercepciones().getValeDespensa(), diasPeriodo);
				valesDespensaBO.setPercepcionesEmpleado(this.empleado.getPercepcionesEmpleado());
				pValeDespensa = valesDespensaBO.calcular(nomina, idxP++);
				if(pValeDespensa.getImporteExcento().add(pValeDespensa.getImporteGravado()).compareTo(BigDecimal.ZERO) > 0) //Si hay vales de desapensa, se agregan a la lista de percepciones.
					percepciones.add(pValeDespensa);
			}
			
			/*-------------------------DEDUCCIONES-----------------------*/
			if(salarioSemanal.compareTo(BigDecimal.ZERO) > 0) {
				
				NominaSemanalBL.procesarISR(nomina, this.periodoInicio, this.periodoFin, this.parametros);

				imssBO = new IMSSDeduccion(this.parametros.getTiposDeduccion(), this.parametros.getCuotasIMSS(), this.fechaInicioAnio, this.fechafinAnio, new BigDecimal(DIAS_POR_PERIODO + SEPTIMO_DIA), this.uma, salarioDiarioIntegrado);
				imssBO.procesar(nomina);
				
				prestamosBO = new PrestamoDeduccion(this.empleado);
				prestamosBO.setFecha(this.periodoFin);
				prestamosBO.procesar(nomina);
			}
			
			for(DetNominaPercepcion p : percepciones) {
				log.info("Percepcion: {} - {} - {}", p.getNombre(), p.getImporteExcento(), p.getImporteGravado());
			}
			
			for(DetNominaOtroPago o : otrosPagos) {
				log.info("Otro pago: {} - {}", o.getNombre(), o.getImporte());
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
			nomina.setMetodoPago(this.parametros.getMetodoPago());
			nomina.setSerie(String.format("%d", this.anio));
			nomina.setFolio(String.format("%d", this.semanaAnio));
			nomina.setLugarExpedicion(this.empleado.getDatoEmpresa().getEmpresa().getCodigoPostal());
			nomina.setEjercicio(DateUtil.getAnio(this.fechaInicioAnio));
			nomina.setDiasLaborados(diasTrabajados.intValue());
			nomina.setDiasNoLaborados(diasLaboralesPeriodo.subtract(diasTrabajados).intValue());
			nomina.setPeriodo(this.semanaAnio);
			nomina.setPeriodoInicio(this.periodoInicio.toInstant().atZone(ZoneId.of("GMT-6")).toLocalDate());
			nomina.setPeriodoFin(this.periodoFin.toInstant().atZone(ZoneId.of("GMT-6")).toLocalDate());
			nomina.setSubtotal(BigDecimal.ZERO.add(this.totalPercepciones).add(this.totalOtrosPagos));
			nomina.setDescuento(this.totalDeducciones);
			nomina.setTotal(neto);
			
			DetNominaEmisor emisor = this.getEmisor(nomina);
			log.info("Emisor: {}", emisor.getNombre());
			
			DetNominaReceptor receptor = this.getNominaReceptor(nomina, salarioDiarioIntegrado);
			log.info("Receptor: {}", receptor.getNombre());
					
			DetNominaConcepto concepto = new DetNominaConcepto();
			concepto.setKey(new DetNominaConceptoPK(nomina, 0));
			concepto.setConcepto(this.parametros.getConcepto());
			concepto.setCantidad(new BigDecimal("1").setScale(2, BigDecimal.ROUND_HALF_UP));
			concepto.setUnidad(this.parametros.getUnidadSAT());
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
		BigDecimal totalOtrosPagos = null;
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
			
			totalOtrosPagos = otrosPagos.stream()
					.filter(o -> o.getProcesar())
					.map(item -> item.getImporte())
					.reduce(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal :: add)
					;
			
			this.totalOtrosPagos = totalOtrosPagos;
			
			totalDeducciones = deducciones.stream()
					.filter(d -> d.getProcesar())
					.map(item -> item.getImporte())
					.reduce(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal::add)
					;
			
			this.totalDeducciones = totalDeducciones;
			
			previoNeto = totalPercepciones.add(totalOtrosPagos).subtract(totalDeducciones).setScale(1, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
			neto = totalPercepciones.add(totalOtrosPagos).subtract(totalDeducciones).setScale(2, BigDecimal.ROUND_HALF_UP);
			ajusteAlNeto = previoNeto.subtract(neto);
			
			total = neto.add(ajusteAlNeto);
			
			if(ajusteAlNeto.compareTo(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP)) < 0 ) {
				DetNominaDeduccion maxD = Collections.max(deducciones, Comparator.comparing(d -> d.getKey().getId()));
				dAjusteNetoBO = new AjusteAlNetoDeduccion(ajusteAlNeto.abs());
				dAjusteNetoBO.setTiposDeduccion(this.parametros.getTiposDeduccion());
				dAjusteAlNeto = dAjusteNetoBO.calcular(nomina, maxD.getKey().getId() + 1);
				deducciones.add(dAjusteAlNeto);
				log.info("Aplicando ajuste al neto como deduccion: {}", dAjusteAlNeto);
			} else if(ajusteAlNeto.compareTo(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP)) > 0) {
				Integer index = null;
				opAjusteNetoBO = new AjusteAlNetoOtroPago(ajusteAlNeto);
				opAjusteNetoBO.setTiposOtroPago(this.parametros.getTiposOtroPago());
				index = opAjusteNetoBO.nuevoIndiceDe(otrosPagos);
				opAjusteAlNeto = opAjusteNetoBO.calcular(nomina, index);
				otrosPagos.add(opAjusteAlNeto);
				log.info("Aplicando ajuste al neto como otro pago: {}", opAjusteAlNeto);
			}
			
			//Recalculamos los importes para presentar el ajuste al neto en el resumen del recibo de nomina.
			this.totalPercepciones = percepciones.stream()
					.map(item -> item.getImporteExcento().add(item.getImporteGravado()))
					.reduce(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal :: add)
			;
			
			this.totalOtrosPagos = otrosPagos.stream()
					.filter(o -> o.getProcesar())
					.map(item -> item.getImporte())
					.reduce(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal :: add)
					;
			
			this.totalDeducciones = deducciones.stream()
					.filter(d -> d.getProcesar())
					.map(item -> item.getImporte())
					.reduce(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal::add)
					;
			
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
			if(nomina == null)
				throw new SGPException("El objeto DetNomina no esta definido.");
			
			if(nomina.getReceptor() == null)
				nomina.setReceptor(new DetNominaReceptor());
			
			receptor = nomina.getReceptor();
			receptor.setNomina(nomina);
			
			if(this.empleado == null)
				throw new SGPException("El objeto DetEmpleado no esta definido.");
			receptor.setNombre(String.format("%s %s %s", this.empleado.getNombre(), this.empleado.getPrimerAp(), this.empleado.getSegundoAp()).trim());
			
			if(this.empleado.getDatoEmpresa() == null)
				throw new SGPException("El objeto DatoEmpresa de DetEmpleado no esta definido.");
			receptor.setRfc(this.empleado.getDatoEmpresa().getRfc());
			
			if(this.empleado.getDomicilio() == null)
				throw new SGPException("El objeto DetDomicilio de DetEmpleado no esta definido.");
			
			if(this.empleado.getDomicilio().getAsentamiento() == null)
				throw new SGPException("El objeto DetAsentamiento de DetEmpleado.domicilio no esta definido.");
			receptor.setCodigoPostal(this.empleado.getDomicilio().getAsentamiento().getCp());
			
			if(this.parametros.getRegimenFiscalReceptor() == null)
				throw new SGPException("El regimen fiscal del receptor no esta definido.");
			receptor.setRegimenFiscal(this.parametros.getRegimenFiscalReceptor());
			
			if(this.parametros.getUsoCFDI() == null)
				throw new SGPException("El uso del CFDI del recpetor no esta definido.");
			receptor.setUsoCfdi(this.parametros.getUsoCFDI());
			
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
			log.error("Problema para generar el receptor...", ex);
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
			diaInicioMes = DateUtil.getDia(periodoInicio);
			
			if(diaInicioMes.compareTo(new Integer(1)) == 0)
				throw new SGPException("La semana en curso es la primera del mes.");
			
			//SI NO, SE VERIFICA SI EL MES DEL PRIMER DIA DE LA SEMANA EN CURSO ES DIFERENTE AL MES DEL ÚLTIMO DIA DE LA SEMANA EN CURSO.
			//SI EL PRIMER DIA DEL PERIODO ES DE UN MES DIFERENTE AL ÚLTIMO DIA DEL PERIODO, ENTONCES, LA SEMANA NO ES LA ÚLTIMA.
			mesActualInicio = DateUtil.getMes(periodoInicio);
			mesActualFin = DateUtil.getMes(periodoFin);
			
			if(mesActualInicio.compareTo(mesActualFin) != 0)
				throw new SGPException("La semana actual termina en un mes diferente al inicial.");
			
			//SI NO, CALCULAMOS LAS FECHAS DE INICIO Y FIN DE LA SIGUIENTE SEMANA Y REPETIMOS LA EVALUACIÓN ANTERIOR. 
			periodoSiguienteInicio = DateUtil.addDay(periodoInicio, 7);
			periodoSiguienteFin = DateUtil.addDay(periodoFin, 7);
			
			mesSiguienteInicio = DateUtil.getMes(periodoSiguienteInicio);
			mesSiguienteFin = DateUtil.getMes(periodoSiguienteFin);
			
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
	
	public static List<DetNomina> procesaNominaDelMes(Date periodoInicio, String rfc) {
		List<DetNomina> listaNominaDelMes = null;
		Date dPeriodoInicio = new Date(periodoInicio.getTime());
		Date dPeriodoAnteriorFin = null;
		Date dPeriodoAnteriorInicio = null;
		
		Integer mesActual = null;
		Integer mesAnterior = null;
		
		Integer semanaInicio = null;
		Integer semanaFin = null;
		
		NominaDAO nominaDAO = new NominaDAO();
		
		//Obtener la fecha inicio de la primera semana del mes
		for(int i = 0; i < 6; i++) {
			dPeriodoAnteriorInicio = DateUtil.addDay(dPeriodoInicio, (-7 * i));
			
			if(DateUtil.getDia(dPeriodoAnteriorInicio) == 1)
				break;
			
			mesActual = DateUtil.getMes(dPeriodoInicio);
			mesAnterior = DateUtil.getMes(dPeriodoAnteriorInicio);
			
			if(mesAnterior.equals(mesActual) == false)
				break;
		}
		
		dPeriodoAnteriorFin = DateUtil.addDay(dPeriodoInicio, -1);
		
		try {
			log.info("Buscando pagos semanales de nómina del {} al {}", 
					DateUtil.getString(dPeriodoAnteriorInicio, DateUtil.FORMATO_DD_MM_YYYY),
					DateUtil.getString(dPeriodoAnteriorFin, DateUtil.FORMATO_DD_MM_YYYY));
		} catch (SGPException e) {
			log.warn(e.getMessage());
		}
		
		semanaInicio = DateUtil.getSemanaAnio(dPeriodoAnteriorInicio);
		semanaFin = DateUtil.getSemanaAnio(dPeriodoAnteriorFin);
		
		log.info("Búsqueda de la semana {} a {}", semanaInicio, semanaFin);
		
		listaNominaDelMes = nominaDAO.buscarPorSemanaRfc(semanaInicio, semanaFin, rfc);
		
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
			diaSemana = DateUtil.getDiaSemana(registro.getFechaEntrada());
			if(mapAsistencias.containsKey(diaSemana))
				continue;
			mapAsistencias.put(diaSemana, registro);
		}
		
		for(CatDiaNoLaboral dia : this.parametros.getDiasNoLaborales()) {
			log.info("Dia no laboral encontrado: {}", dia);
			diaSemana = DateUtil.getDiaSemana(dia.getFecha());
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
    		diasAguinaldo = new BigDecimal(this.parametros.getParametrosPercepciones().getDiasAguinaldo().intValue());
    		
    		//TODO Dias de vacaciones debe ser un dato calculado, conforme a la ley federal del trabajo
    		diasVacaciones = new BigDecimal(this.parametros.getParametrosPercepciones().getDiasVacaciones().intValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
    		primaVacacional = new BigDecimal(this.parametros.getParametrosPercepciones().getPrimaVacacional().floatValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
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
	
	public static synchronized void procesarISR(DetNomina nomina, Date periodoInicio, Date periodoFin, ParametrosNomina parametros) {
		Boolean esUltimaSemanaMes = false;
		List<DetNomina> nominaMensual = null;
		
		esUltimaSemanaMes = NominaSemanalBL.esUltimaSemanaMes(periodoInicio, periodoFin);
		if(esUltimaSemanaMes)
			nominaMensual = NominaSemanalBL.procesaNominaDelMes(periodoInicio, nomina.getReceptor().getRfc());
		
		log.info("Procesando cálculo de ISR...");
		//Primero se debe buscar en "nomina" si ya existen registros de ISR y Subsidio al salario y eliminarlos.
		List<DetNominaDeduccion> deducciones = nomina.getDeducciones();
		boolean removedDeducciones = deducciones.removeIf(d -> AbstractDeduccion.D_ISR.equalsIgnoreCase(d.getTipoDeduccion().getClave()));
		if(removedDeducciones)
			log.info("Se encontraron conceptos {}, los cuales fueron eliminados para el reproceso de ISR", AbstractDeduccion.D_ISR);
		
		List<DetNominaOtroPago> otrosPagos = nomina.getOtrosPagos();
		boolean removedOtrosPagos = otrosPagos.removeIf(o -> AbstractOtroPago.OP_SUBSIDIO_AL_SALARIO.equalsIgnoreCase(o.getTipoOtroPago().getClave()));
		if(removedOtrosPagos)
			log.info("Se encontraron conceptos {}, los cuales fueron eliminados para el reproceso de Subsidio al empleo.", AbstractOtroPago.OP_SUBSIDIO_AL_SALARIO);
		
		ISRExecutor isrExecutor = new ISRExecutor(periodoInicio, periodoFin, parametros.getTiposDeduccion(), parametros.getTiposOtroPago(), parametros.getTablaISR(), nominaMensual);
		IDeducciones isrBO = isrExecutor.loadClass("ISRS", DateUtil.toLocalDate(periodoFin));
		isrBO.procesar(nomina);
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

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public void setParametros(ParametrosNomina parametros) {
		this.parametros = parametros;
	}
}
