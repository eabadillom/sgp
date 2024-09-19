package mx.com.ferbo.business.deduccion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.CatSubsidio;
import mx.com.ferbo.model.CatTarifaISR;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.DetNominaOtroPago;
import mx.com.ferbo.model.DetNominaOtroPagoPK;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.model.sat.CatTipoOtroPago;

public class ISRDeduccion extends AbstractDeduccion implements IDeducciones {
	
	private static Logger log = LogManager.getLogger(ISRDeduccion.class);
	
	private List<CatTarifaISR> tablaISR;
	private List<CatSubsidio> tablaSubsidio;
	private List<DetNominaPercepcion> percepciones;
	
	private DetNominaOtroPago opSubsidioEmpleo;
	private List<CatTipoOtroPago> tiposOtroPago;
	
	public ISRDeduccion(List<CatTipoDeduccion> tiposDeduccion, List<CatTipoOtroPago> tiposOtroPago, List<DetNominaPercepcion> percepciones, List<CatTarifaISR> tablaISR, List<CatSubsidio> tablaSubsidio) {
		this.tiposDeduccion = tiposDeduccion;
		this.tiposOtroPago = tiposOtroPago;
		this.percepciones = percepciones;
		this.tablaISR = tablaISR;
		this.tablaSubsidio = tablaSubsidio;
	}
	
	@Override
	public List<DetNominaDeduccion> calcular(DetNomina nomina, Integer index) {
		List<DetNominaDeduccion> deduccionesISR = null;
		ISRBaseDeduccion baseISRBO = null;
		TarifaISRDeduccion tarifaISRBO = null;
		
		DetNominaDeduccion dBaseISR = null;
		CatTarifaISR tarifaISR = null;
		
		TarifaSubsidioDeduccion tarifaSubsidioBO = null;
		CatSubsidio tarifaSubsidio = null;
		
		ISRAntesSubsidioDeduccion isrPreSubsidioBO = null;
		DetNominaDeduccion dISRAntesSubsidio = null;
		
		DetNominaDeduccion dISR = null;
		
		CatTipoDeduccion tdISR = null;
		
		BigDecimal isrAntesDeSubsidio = null;
		BigDecimal isr = null;
		
		try {
			deduccionesISR = new ArrayList<>();
			
			//1. Obtener la base para el ISR.
			//Para el cálculo del ISR a retener al empleado se deben sumar las percepciones que gravan para ISR
			baseISRBO = new ISRBaseDeduccion(percepciones);
			baseISRBO.setTiposDeduccion(tiposDeduccion);
			dBaseISR = baseISRBO.calcular(nomina, index++);
			
			tarifaISRBO = new TarifaISRDeduccion(tablaISR, dBaseISR.getImporte());
			tarifaISR = tarifaISRBO.calcular();
			
			isrPreSubsidioBO = new ISRAntesSubsidioDeduccion(dBaseISR.getImporte(), tarifaISR);
			dISRAntesSubsidio = isrPreSubsidioBO.calcular(nomina, index++);
			
			isrAntesDeSubsidio = dISRAntesSubsidio.getImporte();
			deduccionesISR.add(dISRAntesSubsidio);
			
			tarifaSubsidioBO = new TarifaSubsidioDeduccion(this.tablaSubsidio, dBaseISR.getImporte());
			tarifaSubsidio = tarifaSubsidioBO.calcular();
			
			isr = isrAntesDeSubsidio.subtract(tarifaSubsidio.getCantidadSubsidio());
			
			dISR = new DetNominaDeduccion();
			dISR.setKey(new DetNominaDeduccionPK(nomina, index++));
			tdISR = this.getTipoDeduccion("002");
			dISR.setTipoDeduccion(tdISR);
			dISR.setClave("045");
			dISR.setNombre("I.S.R.");
			dISR.setImporte(isr);
			dISR.setProcesar(true);
			deduccionesISR.add(dISR);
			
			this.procesaSubsidioAlEmpleo(nomina, tarifaSubsidio);
			
		} catch(Exception ex) {
			log.error("Problema para calcular el ISR...", ex);
		} finally {
			this.tiposDeduccion = null;
			this.tablaISR = null;
			this.tablaSubsidio = null;
			this.percepciones = null;
		}
		
		return deduccionesISR;
	}
	
	public void procesaSubsidioAlEmpleo(DetNomina nomina, CatSubsidio tarifaSubsidio) {
		CatTipoOtroPago topSubsidioEmpleo = null;
		int indexOP = -1;
		
		if(nomina.getOtrosPagos() == null)
			nomina.setOtrosPagos(new ArrayList<>());
		
		for(DetNominaOtroPago o : nomina.getOtrosPagos()) {
			if(indexOP >= o.getKey().getId())
				continue;
			indexOP = o.getKey().getId();
		}
		indexOP++;
		
		opSubsidioEmpleo = new DetNominaOtroPago();
		opSubsidioEmpleo.setKey(new DetNominaOtroPagoPK(nomina, null));
		
		
		topSubsidioEmpleo = this.getTipoOtroPago("002");
		opSubsidioEmpleo.setTipoOtroPago(topSubsidioEmpleo);
		opSubsidioEmpleo.setClave("FRB-035");
		opSubsidioEmpleo.setNombre("Subs. al empleo mes");
		opSubsidioEmpleo.setImporte(tarifaSubsidio.getCantidadSubsidio());
		opSubsidioEmpleo.setProcesar(false);
		nomina.getOtrosPagos().add(opSubsidioEmpleo);
	}
	
	public CatTipoOtroPago getTipoOtroPago(String clave) {
		CatTipoOtroPago tipoOtroPago = null;
		List<CatTipoOtroPago> collect = null;
		
		try {
			collect = this.tiposOtroPago.stream()
					.filter(t -> clave.equals(t.getClave()))
					.collect(Collectors.toList())
					;
			
			if(collect.size() > 0)
				tipoOtroPago = collect.get(0);
		} catch(Exception ex) {
			log.warn("No es posible determinar el tipo de otro pago: " + clave, ex);
		}
		
		return tipoOtroPago;
	}

	public DetNominaOtroPago getOpSubsidioEmpleo() {
		return opSubsidioEmpleo;
	}

	public void setTiposOtroPago(List<CatTipoOtroPago> tiposOtroPago) {
		this.tiposOtroPago = tiposOtroPago;
	}
}
