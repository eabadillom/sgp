package mx.com.ferbo.business.nomina;

import java.math.BigDecimal;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AbstractDeduccion;
import mx.com.ferbo.business.deduccion.AjusteAlNetoDeduccion;
import mx.com.ferbo.business.otropago.AbstractOtroPago;
import mx.com.ferbo.business.otropago.AjusteAlNetoOtroPago;
import mx.com.ferbo.dao.n.ConceptoDAO;
import mx.com.ferbo.dao.n.UnidadSATDAO;
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
import mx.com.ferbo.model.sat.CatConcepto;
import mx.com.ferbo.model.sat.CatUnidadSAT;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

public abstract class NominaBL {
	private static Logger log = LogManager.getLogger(NominaBL.class);
	
	public static final String TP_NOMINA_ORDINARIA = "O";
	
	public static synchronized DetNomina build(String tipoNomina, ParametrosNomina parametros, DetEmpleado empleado) {
		DetNomina nomina = null;
		
		DetNominaEmisor emisor = null;
		DetNominaReceptor receptor = null;
		DetNominaConcepto concepto = null;
		
		CatConcepto conceptoSAT = null;
		ConceptoDAO conceptoDAO = null;
		
		CatUnidadSAT unidadSAT = null;
		UnidadSATDAO unidadSATDAO = null;
		
		List<DetNominaConcepto> conceptos = null;
		List<DetNominaPercepcion> percepciones = null;
		List<DetNominaOtroPago> otrosPagos = null;
		List<DetNominaDeduccion> deducciones = null;
		
		try {
			nomina = new DetNomina();
			
			if(empleado == null) {
				emisor = new DetNominaEmisor();
				receptor = new DetNominaReceptor();
			} else {
				emisor = getEmisor(nomina, empleado.getDatoEmpresa().getEmpresa());
				receptor = getReceptor(nomina, parametros, empleado);
			}
			
			conceptos = new ArrayList<>();
			percepciones = new ArrayList<>();
			otrosPagos = new ArrayList<>();
			deducciones = new ArrayList<>();
			
			
			if(parametros == null) {
				conceptoDAO = new ConceptoDAO();
				conceptoSAT = conceptoDAO.buscarPorId("84111505");
				
				unidadSATDAO = new UnidadSATDAO();
				unidadSAT = unidadSATDAO.buscarPorId("ACT");
			} else {
				conceptoSAT = parametros.getConcepto();
				unidadSAT = parametros.getUnidadSAT();
			}
			
			if(TP_NOMINA_ORDINARIA.equalsIgnoreCase(tipoNomina)) {
				concepto = new DetNominaConcepto();
				concepto.setKey(new DetNominaConceptoPK(nomina, 0));
				concepto.setConcepto(conceptoSAT);
				concepto.setCantidad(BigDecimal.ONE.setScale(2, BigDecimal.ROUND_HALF_UP));
				concepto.setUnidad(unidadSAT);
				concepto.setNombreConcepto("Pago de nómina");
				concepto.setObjetoImpuesto("01");
				conceptos.add(concepto);
			}
			
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
		} catch(Exception ex) {
			emisor = new DetNominaEmisor();
			emisor.setNomina(nomina);
		}
		
		return emisor;
	}
	
	public static DetNominaReceptor getReceptor(DetNomina nomina, ParametrosNomina parametros, DetEmpleado empleado) {
		DetNominaReceptor receptor = null;
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
			
			if(empleado.getDatoEmpresa() == null)
				throw new SGPException("El objeto DatoEmpresa de DetEmpleado no esta definido.");
			receptor.setRfc(empleado.getDatoEmpresa().getRfc());
			
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
			
			receptor.setCurp(empleado.getCurp());
			
			receptor.setNss(empleado.getDatoEmpresa().getNss());
			receptor.setInicioRelacionLaboral(empleado.getDatoEmpresa().getFechaIngreso());
			
			receptor.setTipoContrato(empleado.getDatoEmpresa().getTipoContrato());
			receptor.setSindicalizado(empleado.getDatoEmpresa().getSindicalizado());
			receptor.setTipoJornada(empleado.getDatoEmpresa().getTipoJornada());
			receptor.setTipoRegimen(empleado.getDatoEmpresa().getTipoRegimen());
			receptor.setNumeroEmpleado(empleado.getNumEmpleado());
			receptor.setDepartamento(empleado.getDatoEmpresa().getArea().getDescripcion());
			receptor.setPuesto(empleado.getDatoEmpresa().getPuesto().getDescripcion());
			receptor.setRiesgoPuesto(empleado.getDatoEmpresa().getRiesgoPuesto());
			receptor.setPeriodicidadPago(empleado.getDatoEmpresa().getPeriodicidadPago());
			receptor.setSalarioDiario(empleado.getDatoEmpresa().getSalarioDiario());
			receptor.setEntidadFederativa(empleado.getDatoEmpresa().getEntidadFederativa());
			//TODO pendiente revisar antiguedad
			String sAntiguedad = antiguedadSemanas(empleado.getDatoEmpresa().getFechaIngreso(), parametros.getPeriodoFin());
			log.info("Antiguedad: {}", sAntiguedad);
			receptor.setAntiguedad(sAntiguedad);
			
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
		Integer maxIndex = null;
		DetNominaPercepcion maxP = null;
		
		if(nomina == null)
			throw new SGPException("El objeto nómina no está definido.");
		
		if(nomina.getPercepciones() == null)
			throw new SGPException("La lista de percepciones no está definida.");
		
		if(percepcion.getImporteExcento() == null && percepcion.getImporteGravado() == null)
			throw new SGPException("Debe indicar un importe (excento o gravado).");
		
		if(percepcion.getImporteExcento() == null)
			percepcion.setImporteExcento(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
		
		if(percepcion.getImporteGravado() == null)
			percepcion.setImporteGravado(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
		
		if(percepcion.getImporteExcento().compareTo(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP)) < 0
				&& percepcion.getImporteGravado().compareTo(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP)) < 0
				)
			throw new SGPException("Debe indicar un importe (excento o gravado).");
		
		if(percepcion.getNombre() == null)
			throw new SGPException("Debe indicar una descripción para la percepción.");
		
		if(percepcion.getNombre().trim().equalsIgnoreCase(""))
			throw new SGPException("Debe indicar una descripción para la percepción.");
		
		percepcion.setClave("FRB-" + percepcion.getTipoPercepcion().getClave() );
		
		maxP = Collections.max(nomina.getPercepciones(), Comparator.comparing(d -> d.getKey().getId()));
		
		if(maxP.getKey().getId() == null)
			throw new SGPException("Existen elementos de \"Percepciones\" que no tienen asignado un consecutivo");
		
		maxIndex = maxP.getKey().getId() + 1;
		percepcion.getKey().setId(maxIndex);
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
		
		if(otroPago.getImporte().compareTo(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP)) < 0)
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
		
		if(deduccion.getImporte().compareTo(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP)) < 0)
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
		
		AjusteAlNetoDeduccion dAjusteNetoBO = null;
		DetNominaDeduccion dAjusteAlNeto = null;
    	
    	//Buscar el ajuste al neto (como Otro pago o Deduccion).
    	Optional<DetNominaDeduccion> dOpt = nomina.getDeducciones().stream()
    			.filter(d -> AbstractDeduccion.CVE_AJUSTE_AL_NETO.equalsIgnoreCase(d.getClave()))
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
				.map(item -> item.getImporteExcento().add(item.getImporteGravado()))
				.reduce(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal :: add)
		;
    	
    	totalOtrosPagos = nomina.getOtrosPagos().stream()
				.filter(o -> o.getProcesar())
				.map(item -> item.getImporte())
				.reduce(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal :: add)
				;
    	
    	totalDeducciones = nomina.getDeducciones().stream()
				.filter(d -> d.getProcesar())
				.map(item -> item.getImporte())
				.reduce(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal::add)
				;
    	
    	previoNeto = totalPercepciones.add(totalOtrosPagos).subtract(totalDeducciones).setScale(1, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
		neto = totalPercepciones.add(totalOtrosPagos).subtract(totalDeducciones).setScale(2, BigDecimal.ROUND_HALF_UP);
		ajusteAlNeto = previoNeto.subtract(neto);
		
		total = neto.add(ajusteAlNeto);
		
		if(ajusteAlNeto.compareTo(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP)) < 0 ) {
			
			DetNominaDeduccion maxD = Collections.max(nomina.getDeducciones(), Comparator.comparing(d -> d.getKey().getId()));
			dAjusteNetoBO = new AjusteAlNetoDeduccion(ajusteAlNeto.abs());
			dAjusteNetoBO.setTiposDeduccion(parametros.getTiposDeduccion());
			dAjusteAlNeto = dAjusteNetoBO.calcular(nomina, maxD.getKey().getId() + 1);
			nomina.getDeducciones().add(dAjusteAlNeto);
			log.info("Aplicando ajuste al neto como deduccion: {}", dAjusteAlNeto);
			
		} else if(ajusteAlNeto.compareTo(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP)) > 0) {
			
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
				.map(item -> item.getImporteExcento().add(item.getImporteGravado()))
				.reduce(BigDecimal.ZERO, BigDecimal :: add);
		
		totalOtrosPagos = nomina.getOtrosPagos().stream()
				.filter(o -> o.getProcesar())
				.map(item -> item.getImporte())
				.reduce(BigDecimal.ZERO, BigDecimal :: add);
		
		totalDeducciones = nomina.getDeducciones().stream()
				.filter(d -> d.getProcesar())
				.map(item -> item.getImporte())
				.reduce(BigDecimal.ZERO, BigDecimal :: add);
		
		subtotal = BigDecimal.ZERO.add(totalPercepciones).add(totalOtrosPagos);
		descuentos = BigDecimal.ZERO.add(totalDeducciones);
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
