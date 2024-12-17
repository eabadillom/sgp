package mx.com.ferbo.business.nomina;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.IDeducciones;
import mx.com.ferbo.business.deduccion.isr.ISRExecutor;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaConcepto;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.DetNominaEmisor;
import mx.com.ferbo.model.DetNominaOtroPago;
import mx.com.ferbo.model.DetNominaOtroPagoPK;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPercepcionPK;
import mx.com.ferbo.model.DetNominaReceptor;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

public abstract class NominaBL {
	private static Logger log = LogManager.getLogger(NominaBL.class);
	
	public static synchronized DetNomina build() {
		DetNomina nomina = null;
		
		DetNominaEmisor emisor = null;
		DetNominaReceptor receptor = null;
		
		List<DetNominaConcepto> conceptos = null;
		List<DetNominaPercepcion> percepciones = null;
		List<DetNominaOtroPago> otrosPagos = null;
		List<DetNominaDeduccion> deducciones = null;
		
		try {
			nomina = new DetNomina();
			emisor = new DetNominaEmisor();
			receptor = new DetNominaReceptor();
			conceptos = new ArrayList<>();
			percepciones = new ArrayList<>();
			otrosPagos = new ArrayList<>();
			deducciones = new ArrayList<>();
			
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
	
	public static synchronized void actualizar(DetNomina nomina) {
		BigDecimal totalPercepciones = null;
    	BigDecimal totalOtrosPagos = null;
    	BigDecimal totalDeducciones = null;
    	BigDecimal subtotal = null;
    	BigDecimal descuentos = null;
    	BigDecimal total = null;
    	
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
		
		nomina.setSubtotal(subtotal);
		nomina.setDescuento(descuentos);
		nomina.setTotal(total);
	}
	
	
	public static synchronized void procesarISR(DetNomina nomina, Date periodoInicio, Date periodoFin, ParametrosNomina parametros, List<DetNomina> nominaSemanal) {
		
		//Primero se debe buscar en "nomina" si ya existen registros de ISR y Subsidio al salario.
		
		
		
		
		ISRExecutor isrExecutor = new ISRExecutor(periodoInicio, periodoFin, parametros.getTiposDeduccion(), parametros.getTiposOtroPago(), parametros.getTablaISR(), nominaSemanal);
		IDeducciones isrBO = isrExecutor.loadClass("ISRS", DateUtil.toLocalDate(periodoFin));
		isrBO.procesar(nomina);
	}
}
