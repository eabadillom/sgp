package mx.com.ferbo.business.nomina;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AbstractDBL;
import mx.com.ferbo.business.deduccion.AjusteAlNetoDBL;
import mx.com.ferbo.business.otropago.AbstractOtroPago;
import mx.com.ferbo.business.otropago.AjusteAlNetoOtroPago;
import mx.com.ferbo.dao.n.VacacionesDAO;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.CatEmpresa;
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
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.model.DetVacaciones;
import mx.com.ferbo.model.sat.CatConcepto;
import mx.com.ferbo.model.sat.CatUnidadSAT;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

public abstract class NominaBL {
	private static Logger log = LogManager.getLogger(NominaBL.class);
	
	public static final String TP_NOMINA_ORDINARIA      = "O";
	public static final String TP_NOMINA_EXTRAORDINARIA = "E";
	public static final String TP_COMPROBANTE_CFDI      = "N";
	
	public static final int DIAS_ANIO = 365;
	public static final BigDecimal cien = new BigDecimal(100).setScale(2, RoundingMode.HALF_UP);
	
	protected DetEmpleado empleado = null;
	protected ParametrosNomina parametros = null;
	protected Map<String, DetRegistro> mapAsistencias = null;
	
	public NominaBL() {
	}
	
	public NominaBL(DetEmpleado empleado, ParametrosNomina parametros, Map<String, DetRegistro> mapAsistencias) {
		this.empleado = empleado;
		this.parametros = parametros;
		this.mapAsistencias = mapAsistencias;
	}
	
	public static synchronized DetNomina build(String tipoNomina, ParametrosNomina parametros, DetEmpleado empleado)
	throws SGPException {
		DetNomina                 nomina       = null;
		DetNominaEmisor           emisor       = null;
		DetNominaReceptor         receptor     = null;
		DetNominaConcepto         concepto     = null;
		CatConcepto               conceptoSAT  = null;
		CatUnidadSAT              unidadSAT    = null;
		
		List<DetNominaConcepto>   conceptos    = null;
		List<DetNominaPercepcion> percepciones = null;
		List<DetNominaOtroPago>   otrosPagos   = null;
		List<DetNominaDeduccion>  deducciones  = null;
		List<DetVacaciones>       vacaciones   = null;
		
		if(parametros == null)
			throw new SGPException("Los parámetros de nómina no están definidos.");
		
		try {
			nomina = new DetNomina();
			
			if(empleado == null) {
				receptor = new DetNominaReceptor();
				emisor   = new DetNominaEmisor();
			} else {
				receptor = getReceptor(nomina, parametros, empleado);
				emisor   = getEmisor(nomina, empleado.getDatoEmpresa().getEmpresa());
			}
			
			conceptos = new ArrayList<DetNominaConcepto>();
			percepciones = new ArrayList<DetNominaPercepcion>();
			otrosPagos = new ArrayList<DetNominaOtroPago>();
			deducciones = new ArrayList<DetNominaDeduccion>();
			vacaciones = new ArrayList<DetVacaciones>();
			
			conceptoSAT = parametros.getConcepto();
			unidadSAT = parametros.getUnidadSAT();
			
			concepto = new DetNominaConcepto();
			concepto.setKey(new DetNominaConceptoPK(nomina, 0));
			concepto.setConcepto(conceptoSAT);
			concepto.setCantidad(ValoresBD._1.get());
			concepto.setUnidad(unidadSAT);
			concepto.setNombreConcepto("Pago de nómina");
			concepto.setObjetoImpuesto("01");
			conceptos.add(concepto);
			
			if(TP_NOMINA_ORDINARIA.equalsIgnoreCase(tipoNomina)) {
				nomina.setTipoNomina(TP_NOMINA_ORDINARIA);
			} else if(TP_NOMINA_EXTRAORDINARIA.equalsIgnoreCase(tipoNomina)) {
				nomina.setTipoNomina(TP_NOMINA_EXTRAORDINARIA);
			} else
				throw new SGPException("El tipo de nómina no es válido: " + tipoNomina);
			
			nomina.setEmisor(emisor);
			nomina.setReceptor(receptor);
			nomina.setConceptos(conceptos);
			nomina.setPercepciones(percepciones);
			nomina.setOtrosPagos(otrosPagos);
			nomina.setDeducciones(deducciones);
			nomina.setVacaciones(vacaciones);
			nomina.setTipoComprobante(TP_COMPROBANTE_CFDI);
			nomina.setClaveExportacion("01");
			nomina.setMoneda("MXN");
			nomina.setMetodoPago(parametros.getMetodoPago());
			nomina.setSerie(String.format("%d", parametros.getAnio()));
			nomina.setFolio(String.format("%d", parametros.getPeriodo()));
			nomina.setLugarExpedicion(parametros.getEmpresa().getCodigoPostal());
			nomina.setFechaEmision(parametros.getFechaEmision());
			nomina.setEjercicio(parametros.getAnio());
			nomina.setPeriodo(parametros.getPeriodo());
			nomina.setPeriodoInicio(parametros.getPeriodoInicio().toInstant().atZone(ZoneId.of("GMT-6")).toLocalDate());
			nomina.setPeriodoFin(parametros.getPeriodoFin().toInstant().atZone(ZoneId.of("GMT-6")).toLocalDate());
			nomina.setDiasLaborados(0);
			nomina.setDiasNoLaborados(0);
			nomina.setDiasAsueto(0);
			nomina.setDiasPagados(ValoresBD._CERO.get());
			nomina.setSubtotal(ValoresBD._CERO.get());
			nomina.setDescuento(ValoresBD._CERO.get());
			nomina.setTotal(ValoresBD._CERO.get());
		
		} catch(SGPException ex){
			log.warn("Problema para generar el objeto nómina del empleado...", ex);
			throw ex;
		} catch(Exception ex) {
			log.error("Problema para generar la estructura básica de la nómina del empleado.");
		}
		
		return nomina;
	}
	
	public static BigDecimal calculoVacacionesSDI(ParametrosNomina parametros, DetEmpleado empleado) {
		BigDecimal diasVacaciones = null;
		
		VacacionesDAO vacacionesDAO = null;
    	DetVacaciones periodoVacacional = null;
    	
    	try {
    		vacacionesDAO     = new VacacionesDAO();
    		periodoVacacional = vacacionesDAO.buscarPeriodoPorFecha(empleado.getIdEmpleado(), parametros.getPeriodoFin());
    		diasVacaciones    = new BigDecimal(periodoVacacional.getDiasTotales())
    				.setScale(2, RoundingMode.HALF_UP)
    				;
    	} catch(Exception ex) {
    		log.error("Problema para obtener los días de vacaciones para el Salario Diario Integrado...", ex);
    	}
		
		return diasVacaciones;
	}
		
	
	public static BigDecimal calculoSDI(BigDecimal salarioDiario, BigDecimal diasAguinaldo, BigDecimal diasVacaciones, BigDecimal primaVacacional) {
		BigDecimal salarioDiarioIntegrado = null;
    	BigDecimal factorSDI = null;
    	
    	try {
    		log.info("Calculando Salario Diario Integrado...");
    		
    		log.info("Salario diario: {}, Dias aguinaldo: {}, Dias de vacaciones: {}, Prima vacacional: {}", salarioDiario, diasAguinaldo, diasVacaciones, primaVacacional);
    		
    		factorSDI = primaVacacional
    				.multiply(diasVacaciones).setScale(4, RoundingMode.HALF_UP)
    				.add(diasAguinaldo)
    				.add(ValoresBD._DIAS_ANIO.get())
    				.divide(ValoresBD._DIAS_ANIO.get(), 5, RoundingMode.HALF_UP)
    				;
    		log.info("Factor de integración: {}", factorSDI);
    		
    		salarioDiarioIntegrado = salarioDiario.multiply(factorSDI).setScale(2, RoundingMode.HALF_UP);
    		log.info("Salario Diario Integrado: {}", salarioDiarioIntegrado);
    		
    	} catch(Exception ex) {
    		log.warn("No es posible calcular el Salario Diario Integrado: {}", ex.getMessage());
    		salarioDiarioIntegrado = ValoresBD._CERO.get();
    	}
    	
        return salarioDiarioIntegrado;
	}
	
	public static String antiguedadPeriodo(Date fechaInicio, Date fechaFin) {
		
		Period diferencia = Period.between(DateUtil.toLocalDate(fechaInicio), DateUtil.toLocalDate(fechaFin));

        String formatoDiferencia = String.format("P%dY%dM%dD", 
            diferencia.getYears(), 
            diferencia.getMonths(), 
            diferencia.getDays()
        );
        
		return formatoDiferencia;
	}
	
	public static String antiguedadSemanas(Date fechaInicio, Date fechaFin) {
		long diasDiferencia = ChronoUnit.DAYS.between(DateUtil.toLocalDate(fechaInicio), DateUtil.toLocalDate(fechaFin));

        // Calcular la diferencia en semanas
        long semanasDiferencia = diasDiferencia / 7;

        // Formatear el resultado
        String formatoDiferencia = String.format("P%dW", semanasDiferencia);
        
        return formatoDiferencia;
	}
	
	public static DetNominaEmisor getEmisor(DetNomina nomina, CatEmpresa empresa) {
		DetNominaEmisor emisor = null;
		
		try {
			if(nomina.getEmisor() == null)
				nomina.setEmisor(new DetNominaEmisor());
			
			emisor = nomina.getEmisor();
			emisor.setNomina(nomina);
			emisor.setNombre(empresa.getRazonSocial());
			emisor.setRfc(empresa.getRfc());
			emisor.setCodigoPostal(empresa.getCodigoPostal());
			emisor.setRegistroPatronal(empresa.getRegistroPatronal());
			emisor.setRegimenFiscal(empresa.getRegimenFiscal());
			
			log.info("Emisor: {}", emisor.getNombre());
		} catch(Exception ex) {
			emisor = new DetNominaEmisor();
			emisor.setNomina(nomina);
		}
		
		return emisor;
	}
	
	public static DetNominaReceptor getReceptor(DetNomina nomina, ParametrosNomina parametros, DetEmpleado empleado) {
		DetNominaReceptor receptor = null;
		BigDecimal salarioDiario = null;
		BigDecimal diasAguinaldo = null;
		BigDecimal diasVacaciones = null;
		BigDecimal primaVacacional = null;
		BigDecimal sdi = null;
		
		try {
			if(nomina == null)
				throw new SGPException("El objeto DetNomina no esta definido.");
			
			if(nomina.getReceptor() == null)
				nomina.setReceptor(new DetNominaReceptor());
			
			receptor = nomina.getReceptor();
			receptor.setNomina(nomina);
			
			if(empleado == null)
				throw new SGPException("El objeto DetEmpleado no esta definido.");
			receptor.setNombre(String.format("%s %s %s", empleado.getNombre(), empleado.getPrimerAp(), empleado.getSegundoAp()).trim());
			
			log.info("[UI] Generando información del receptor {}", receptor.getNombre());
			
			salarioDiario = empleado.getDatoEmpresa().getSalarioDiario();
			diasAguinaldo = empleado.getDatoEmpresa().getDiasAguinaldo();
			diasVacaciones = calculoVacacionesSDI(parametros, empleado);
			primaVacacional = empleado.getDatoEmpresa().getPrimaVacacional()
					.divide(ValoresBD._100.get())
					.setScale(2, RoundingMode.HALF_UP);
			
			if(empleado.getDatoEmpresa() == null)
				throw new SGPException("El objeto DatoEmpresa de DetEmpleado no esta definido.");
			receptor.setRfc(empleado.getDatoEmpresa().getRfc());
			
			if(empleado.getNumEmpleado() == null)
				throw new SGPException("El número de empleado no está definido.");
			receptor.setNumeroEmpleado(empleado.getNumEmpleado());
			
			if(empleado.getDomicilio() == null)
				throw new SGPException("El objeto DetDomicilio de DetEmpleado no esta definido.");
			
			if(empleado.getDomicilio().getAsentamiento() == null)
				throw new SGPException("El objeto DetAsentamiento de DetEmpleado.domicilio no esta definido.");
			receptor.setCodigoPostal(empleado.getDomicilio().getAsentamiento().getCp());
			
			if(parametros.getRegimenFiscalReceptor() == null)
				throw new SGPException("El regimen fiscal del receptor no esta definido.");
			receptor.setRegimenFiscal(parametros.getRegimenFiscalReceptor());
			
			if(parametros.getUsoCFDI() == null)
				throw new SGPException("El uso del CFDI del recpetor no esta definido.");
			receptor.setUsoCfdi(parametros.getUsoCFDI());
			
			if(empleado.getCurp() == null)
				throw new SGPException("La CURP del empleado no está definida.");
			receptor.setCurp(empleado.getCurp());
			
			if(empleado.getDatoEmpresa().getNss() == null)
				throw new SGPException("El número de seguridad social del empleado no está definido.");
			receptor.setNss(empleado.getDatoEmpresa().getNss());
			
			if(empleado.getDatoEmpresa().getFechaIngreso() == null)
				throw new SGPException("La fecha de ingreso del empleado no está definida.");
			receptor.setInicioRelacionLaboral(empleado.getDatoEmpresa().getFechaIngreso());
			
			if(empleado.getDatoEmpresa().getTipoContrato() == null)
				throw new SGPException("El tipo de contrato del empleado no está definido.");
			receptor.setTipoContrato(empleado.getDatoEmpresa().getTipoContrato());
			
			receptor.setSindicalizado(empleado.getDatoEmpresa().getSindicalizado());
			
			if(empleado.getDatoEmpresa().getTipoJornada() == null)
				throw new SGPException("El tipo de jornada del empleado no está definido.");
			receptor.setTipoJornada(empleado.getDatoEmpresa().getTipoJornada());
			
			if(empleado.getDatoEmpresa().getTipoRegimen() == null)
				throw new SGPException("El tipo de régimen del empleado no está definido.");
			receptor.setTipoRegimen(empleado.getDatoEmpresa().getTipoRegimen());
			
			if(empleado.getDatoEmpresa().getArea() == null)
				throw new SGPException("El área donde labora el empleado no está definida.");
			receptor.setDepartamento(empleado.getDatoEmpresa().getArea().getDescripcion());
			
			if(empleado.getDatoEmpresa().getPuesto() == null)
				throw new SGPException("El puesto donde labora el empleado no está definido.");
			receptor.setPuesto(empleado.getDatoEmpresa().getPuesto().getDescripcion());
			
			if(empleado.getDatoEmpresa().getRiesgoPuesto() == null)
				throw new SGPException("El riesgo laboral del puesto del empleado no está definido.");
			receptor.setRiesgoPuesto(empleado.getDatoEmpresa().getRiesgoPuesto());
			
			if(empleado.getDatoEmpresa().getPeriodicidadPago() == null)
				throw new SGPException("La periodicidad de pago del empleado no está definida.");
			receptor.setPeriodicidadPago(empleado.getDatoEmpresa().getPeriodicidadPago());
			
			if(empleado.getDatoEmpresa().getSalarioDiario() == null)
				throw new SGPException("El salario diario del empleado no está definido.");
			receptor.setSalarioDiario(salarioDiario);
			
			if(empleado.getDatoEmpresa().getEntidadFederativa() == null)
				throw new SGPException("La entidad federativa del empleado no está definida");
			receptor.setEntidadFederativa(empleado.getDatoEmpresa().getEntidadFederativa());
			
			if(empleado.getDatoEmpresa().getDiasAguinaldo() == null)
				throw new  SGPException("Los días de aguianldo del empleado no están definidos.");
			receptor.setDiasAguinaldo(empleado.getDatoEmpresa().getDiasAguinaldo());
			
			receptor.setDiasVacaciones(diasVacaciones);
			
			receptor.setPrimaVacacional(empleado.getDatoEmpresa().getPrimaVacacional());
			
			sdi = calculoSDI(salarioDiario, diasAguinaldo, diasVacaciones, primaVacacional);
			receptor.setSalarioDiarioIntegrado(sdi);
			log.info("[UI] SDI = (({} + {} + ({} * {})) / {}) * {}",
					ValoresBD._DIAS_ANIO.get(), diasAguinaldo, diasVacaciones, primaVacacional, ValoresBD._DIAS_ANIO.get(), salarioDiario);
			
			
			//TODO pendiente revisar antiguedad
			String sAntiguedad = antiguedadSemanas(empleado.getDatoEmpresa().getFechaIngreso(), parametros.getPeriodoFin());
			log.info("[UI] Antiguedad: {}", sAntiguedad);
			receptor.setAntiguedad(sAntiguedad);
			
			//TODO pendiente revisar días de vacaciones.
			
		} catch(Exception ex) {
			log.error("Problema para generar el receptor...", ex);
			receptor = new DetNominaReceptor();
			receptor.setNomina(nomina);
		}
		return receptor;
	}
	
	public static synchronized DetNominaPercepcion nuevaPercepcion(DetNomina nomina)
	throws SGPException {
		DetNominaPercepcion percepcion = null;
		
		if(nomina == null)
			throw new SGPException("El objeto nómina no está definido");
		
		percepcion = new DetNominaPercepcion();
    	percepcion.setKey(new DetNominaPercepcionPK());
    	percepcion.getKey().setNomina(nomina);
    	
    	return percepcion;
	}
	
	public static synchronized DetNominaOtroPago nuevoOtroPago(DetNomina nomina)
	throws SGPException {
		DetNominaOtroPago otroPago = null;
		
		if(nomina == null)
			throw new SGPException("El objeto nómina no está definido.");
		
		otroPago = new DetNominaOtroPago();
    	otroPago.setKey(new DetNominaOtroPagoPK());
    	otroPago.getKey().setNomina(nomina);
    	
		return otroPago;
	}
	
	public static synchronized DetNominaDeduccion nuevaDeduccion(DetNomina nomina)
	throws SGPException {
		DetNominaDeduccion deduccion = null;
		
		if(nomina == null)
			throw new SGPException("El objeto nómina no está definido.");
		
		deduccion = new DetNominaDeduccion();
    	deduccion.setKey(new DetNominaDeduccionPK());
    	deduccion.getKey().setNomina(nomina);
    	
    	return deduccion;
	}
	
	
	public static synchronized void agregarPercepcion(DetNomina nomina, DetNominaPercepcion percepcion)
	throws SGPException {
//		Integer maxIndex = null;
//		DetNominaPercepcion maxP = null;
//		DetNominaPercepcionPK maxKey = null;
		
		if(nomina == null)
			throw new SGPException("El objeto nómina no está definido.");
		
		if(nomina.getPercepciones() == null)
			throw new SGPException("La lista de percepciones no está definida.");
		
		if(percepcion.getImporteExento() == null && percepcion.getImporteGravado() == null)
			throw new SGPException("Debe indicar un importe (excento o gravado).");
		
		if(percepcion.getImporteExento() == null)
			percepcion.setImporteExento(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
		
		if(percepcion.getImporteGravado() == null)
			percepcion.setImporteGravado(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
		
		if(percepcion.getImporteExento().compareTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) < 0
				&& percepcion.getImporteGravado().compareTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) < 0
				)
			throw new SGPException("Debe indicar un importe (excento o gravado).");
		
		if(percepcion.getNombre() == null)
			throw new SGPException("Debe indicar una descripción para la percepción.");
		
		if(percepcion.getNombre().trim().equalsIgnoreCase(""))
			throw new SGPException("Debe indicar una descripción para la percepción.");
		
		if(percepcion.getClave() == null)
			throw new SGPException("Debe indicar una clave para la percepción.");
		
		if(percepcion.getClave().trim().equalsIgnoreCase(""))
			throw new SGPException("Debe indicar una clave para la percepción.");
		
		final String clave = percepcion.getClave();
		
		boolean removedPercepciones = nomina.getPercepciones().removeIf(p -> p.getClave().equalsIgnoreCase(clave));
		if(removedPercepciones)
			log.info("Se encontraron conceptos {}, los cuales fueron eliminados de la lista de percepciones.", clave);
		
//		try {
//			maxP = Collections.max(nomina.getPercepciones(), Comparator.comparing(p -> p.getKey().getId()));
//			maxKey = maxP.getKey();
//		} catch(NoSuchElementException ex) {
//			maxKey = new DetNominaPercepcionPK(nomina, -1);
//		}
//		
//		if(maxKey.getId() == null)
//			throw new SGPException("Existen elementos de \"Percepciones\" que no tienen asignado un consecutivo");
//		
//		maxIndex = maxKey.getId() + 1;
//		percepcion.getKey().setId(maxIndex);
		
		if(percepcion.getImporteExento().add(percepcion.getImporteGravado()).compareTo(ValoresBD._CERO.get()) > 0)
			nomina.getPercepciones().add(percepcion);
	}
	
	public static synchronized void agregarOtroPago(DetNomina nomina, DetNominaOtroPago otroPago)
	throws SGPException {
		Integer maxIndex = null;
    	DetNominaOtroPago maxO = null;
    	
    	if(nomina == null)
			throw new SGPException("El objeto nómina no está definido.");
    	
    	if(nomina.getOtrosPagos() == null)
    		throw new SGPException("La lista de otros pagos no está definida.");
    	
		if(otroPago.getImporte() == null)
			throw new SGPException("Debe indicar un importe.");
		
		if(otroPago.getImporte().compareTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) < 0)
			throw new SGPException("El importe indicado es incorrecto");
		
		if(otroPago.getNombre() == null)
			throw new SGPException("Debe indicar una descripción para el pago.");
		
		if(otroPago.getNombre().trim().equalsIgnoreCase(""))
			throw new SGPException("Debe indicar una descripción para el pago.");
		
		otroPago.setClave("FRB-" + otroPago.getTipoOtroPago().getClave());
		otroPago.setInformar(true);
		otroPago.setProcesar(true);
		maxO = Collections.max(nomina.getOtrosPagos(), Comparator.comparing(o -> o.getKey().getId()));
		
		if(maxO.getKey().getId() == null)
			throw new SGPException("Existen elementos de \"Otros pagos\" que no tienen asignado un consecutivo");
		
		maxIndex = maxO.getKey().getId() + 1;
		otroPago.getKey().setId(maxIndex);
		nomina.getOtrosPagos().add(otroPago);
	}
	
	public static synchronized void agregarDeduccion(DetNomina nomina, DetNominaDeduccion deduccion)
	throws SGPException {
		Integer maxIndex = null;
		DetNominaDeduccion maxD = null;
		
		if(nomina == null)
			throw new SGPException("El objeto nómina no está definido.");
    	
    	if(nomina.getDeducciones() == null)
    		throw new SGPException("La lista de deducciones no está definida.");
		
		if(deduccion.getImporte() == null)
			throw new SGPException("Debe indicar un importe.");
		
		if(deduccion.getImporte().compareTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) < 0)
			throw new SGPException("El importe indicado es incorrecto.");
		
		if(deduccion.getNombre() == null)
			throw new SGPException("Debe indicar una descripción para la deducción.");
		
		if(deduccion.getNombre().trim().equalsIgnoreCase(""))
			throw new SGPException("Debe indicar una descripción para la deducción.");
		
		deduccion.setClave("FRB-" + deduccion.getTipoDeduccion().getClave());
		deduccion.setInformar(true);
		deduccion.setProcesar(true);
		
		maxD = Collections.max(nomina.getDeducciones(), Comparator.comparing(d -> d.getKey().getId()));
		
		if(maxD.getKey().getId() == null)
			throw new SGPException("Existen elementos de \"Deducciones\" que no tienen asignado un consecutivo");
		
		maxIndex = maxD.getKey().getId() + 1;
		deduccion.getKey().setId(maxIndex);
		nomina.getDeducciones().add(deduccion);
	}
	
	public static synchronized void eliminarPercepcion(DetNomina nomina, DetNominaPercepcion percepcion)
	throws SGPException {
		
		if(percepcion == null)
			throw new SGPException("Debe seleccionar una percepción.");
		
		boolean respuesta = nomina.getPercepciones().remove(percepcion);
		
		if(respuesta == false)
			throw new SGPException("Ocurrió un problema para eliminar la percepción.");
	}
	
	public static synchronized void eliminarOtroPago(DetNomina nomina, DetNominaOtroPago otroPago)
	throws SGPException {
		
		if(otroPago == null)
			throw new SGPException("Debe seleccionar un pago.");
		
		boolean respuesta = nomina.getOtrosPagos().remove(otroPago);
		
		if(respuesta == false)
			throw new SGPException("Ocurrió un problema para eliminar el pago.");
	}
	
	public static synchronized void eliminarDeduccion(DetNomina nomina, DetNominaDeduccion deduccion)
	throws SGPException {
		if(deduccion == null)
			throw new SGPException("Debe seleccionar una deducción");
		
		boolean respuesta = nomina.getDeducciones().remove(deduccion);
		
		if(respuesta == false)
			throw new SGPException("Ocurrió un probleam para eliminar la deducción.");
	}
	
	public static synchronized void calcularTotales(DetNomina nomina, ParametrosNomina parametros) {
		BigDecimal totalPercepciones = null;
    	BigDecimal totalOtrosPagos = null;
    	BigDecimal totalDeducciones = null;
    	BigDecimal subtotal = null;
    	BigDecimal descuentos = null;
    	BigDecimal total = null;
    	
    	BigDecimal previoNeto = null;
		BigDecimal neto = null;
		BigDecimal ajusteAlNeto = null;
		
		AjusteAlNetoOtroPago opAjusteNetoBO = null;
		DetNominaOtroPago opAjusteAlNeto = null;
		
		AjusteAlNetoDBL dAjusteNetoBO = null;
		DetNominaDeduccion dAjusteAlNeto = null;
    	
    	//Buscar el ajuste al neto (como Otro pago o Deduccion).
    	Optional<DetNominaDeduccion> dOpt = nomina.getDeducciones().stream()
    			.filter(d -> AbstractDBL.CVE_AJUSTE_AL_NETO.equalsIgnoreCase(d.getClave()))
    			.findFirst()
    			;
    	
    	Optional<DetNominaOtroPago> opOpt = nomina.getOtrosPagos().stream()
    			.filter(o -> AbstractOtroPago.CVE_AJUSTE_AL_NETO.equalsIgnoreCase(o.getClave()))
    			.findFirst()
    			;
    	
    	if(dOpt.isPresent()) {
    		log.info("Ajuste al neto como deduccion encontrado: {}", dOpt.isPresent() ? dOpt.get() : null);
    		nomina.getDeducciones().remove(dOpt.get());
    	}
    	
    	if(opOpt.isPresent()) {
    		log.info("Ajuste al neto como Otro pago: {}", opOpt.isPresent() ? opOpt.get() : null);
    		nomina.getOtrosPagos().remove(opOpt.get());
    	}
    	
    	
    	
    	totalPercepciones = nomina.getPercepciones().stream()
				.map(item -> item.getImporteExento().add(item.getImporteGravado()))
				.reduce(ValoresBD._CERO.get(), BigDecimal :: add)
		;
    	
    	totalOtrosPagos = nomina.getOtrosPagos().stream()
				.filter(o -> o.getProcesar())
				.map(item -> item.getImporte())
				.reduce(ValoresBD._CERO.get(), BigDecimal :: add)
				;
    	
    	totalDeducciones = nomina.getDeducciones().stream()
				.filter(d -> d.getProcesar())
				.map(item -> item.getImporte())
				.reduce(ValoresBD._CERO.get(), BigDecimal::add)
				;
    	
    	previoNeto = totalPercepciones
    			.add(totalOtrosPagos)
    			.subtract(totalDeducciones)
    			//IMPORTANTE REDONDEAR A 1 DECIMAL
    			.setScale(1, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
		
    	neto = totalPercepciones
    			.add(totalOtrosPagos)
    			.subtract(totalDeducciones)
    			//IMPORTANTE REDONDEAR A 2 DECIMALES.
    			.setScale(2, RoundingMode.HALF_UP);
		ajusteAlNeto = previoNeto.subtract(neto);
		
		total = neto.add(ajusteAlNeto);
		
		if(ajusteAlNeto.compareTo(ValoresBD._CERO.get()) < 0 ) {
			
			dAjusteNetoBO = new AjusteAlNetoDBL(ajusteAlNeto.abs());
			dAjusteNetoBO.setTiposDeduccion(parametros.getTiposDeduccion());
			dAjusteAlNeto = dAjusteNetoBO.calcular(nomina);
			nomina.getDeducciones().add(dAjusteAlNeto);
			log.info("Aplicando ajuste al neto como deduccion: {}", dAjusteAlNeto);
			
		} else if(ajusteAlNeto.compareTo(ValoresBD._CERO.get()) > 0) {
			
			Integer index = null;
			opAjusteNetoBO = new AjusteAlNetoOtroPago(ajusteAlNeto);
			opAjusteNetoBO.setTiposOtroPago(parametros.getTiposOtroPago());
			index = opAjusteNetoBO.nuevoIndiceDe(nomina.getOtrosPagos());
			opAjusteAlNeto = opAjusteNetoBO.calcular(nomina, index);
			nomina.getOtrosPagos().add(opAjusteAlNeto);
			log.info("Aplicando ajuste al neto como otro pago: {}", opAjusteAlNeto);
			
		}
    	
    	log.info("Actualizando {}", nomina);
		totalPercepciones = nomina.getPercepciones().stream()
				.map(item -> item.getImporteExento().add(item.getImporteGravado()))
				.reduce(ValoresBD._CERO.get(), BigDecimal :: add);
		
		totalOtrosPagos = nomina.getOtrosPagos().stream()
				.filter(o -> o.getProcesar())
				.map(item -> item.getImporte())
				.reduce(ValoresBD._CERO.get(), BigDecimal :: add);
		
		totalDeducciones = nomina.getDeducciones().stream()
				.filter(d -> d.getProcesar())
				.map(item -> item.getImporte())
				.reduce(ValoresBD._CERO.get(), BigDecimal :: add);
		
		subtotal = ValoresBD._CERO.get().add(totalPercepciones).add(totalOtrosPagos);
		descuentos = ValoresBD._CERO.get().add(totalDeducciones);
		total = subtotal.subtract(descuentos);
		
		log.info("Subtotal recalculado: {}", subtotal);
		log.info("Descuentos: {}", descuentos);
		log.info("Total: {}", total);
		
		nomina.getConceptos().get(0).setValorUnitario(subtotal);
		nomina.getConceptos().get(0).setImporte(subtotal);
		nomina.getConceptos().get(0).setDescuento(totalDeducciones);
		
		nomina.setSubtotal(subtotal);
		nomina.setDescuento(descuentos);
		nomina.setTotal(total);
	}
}
