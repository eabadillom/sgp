package mx.com.ferbo.business.deduccion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import mx.com.ferbo.util.DateUtils;

public class ISRSemanalDeduccion extends AbstractDeduccion implements IDeducciones {
	
	private static Logger log = LogManager.getLogger(ISRSemanalDeduccion.class);
	
	private List<CatTarifaISR> tablaISR = null;
	private List<CatSubsidio> tablaSubsidio = null;
	private List<DetNominaPercepcion> percepciones = null;
	
	private DetNominaOtroPago opSubsidioEmpleo = null;
	private List<CatTipoOtroPago> tiposOtroPago = null;
	
	private Date periodoInicio = null;
	private Date periodoFin = null;
	private Boolean ultimaSemanaMes = null;
	
	private List<DetNomina> listaNominaMes = null;
	private List<CatTarifaISR> tablaISRMensual = null;
	private List<CatSubsidio> tablaSubsidioMensual = null;
	
	public ISRSemanalDeduccion(List<CatTipoDeduccion> tiposDeduccion, List<CatTipoOtroPago> tiposOtroPago, List<DetNominaPercepcion> percepciones, List<CatTarifaISR> tablaISR, List<CatSubsidio> tablaSubsidio) {
		this.tiposDeduccion = tiposDeduccion;
		this.tiposOtroPago = tiposOtroPago;
		this.percepciones = percepciones;
		this.tablaISR = tablaISR;
		this.tablaSubsidio = tablaSubsidio;
	}
	
	public void setPeriodo(Date periodoInicio, Date periodoFin) {
		Date periodoSiguienteFin = null;
		
		this.periodoInicio = periodoInicio;
		this.periodoFin = periodoFin;
		
		periodoSiguienteFin = DateUtils.addDay(periodoInicio, 7);
		
		Integer mesActual = DateUtils.getMes(this.periodoFin);
		Integer mesSiguiente = DateUtils.getMes(periodoSiguienteFin);
		
		if(mesSiguiente > mesActual) {
			this.ultimaSemanaMes = new Boolean(true);
			log.info("ULTIMA SEMANA DEL MES: {} - {}", this.periodoInicio, this.periodoFin);
		} else {
			this.ultimaSemanaMes = new Boolean(false);
		}
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
		
		BigDecimal isrAcumulado = null;
		
		Integer idx = null;
		
		try {
			idx = this.nuevoIndiceDe(nomina.getDeducciones());
			
			deduccionesISR = new ArrayList<>();
			
			//1. Obtener la base para el ISR.
			//Para el cálculo del ISR a retener al empleado se deben sumar las percepciones que gravan para ISR
			baseISRBO = new ISRBaseDeduccion(percepciones);
			baseISRBO.setTiposDeduccion(tiposDeduccion);
			
			dBaseISR = baseISRBO.calcular(nomina, idx++);
			
			//CALCULO DE ISR MENSUAL
			if(this.ultimaSemanaMes) {
				isrAcumulado = this.calcularBaseISRAcumulado(dBaseISR.getImporte(), isr);
				log.info("Base ISR (mensual): {}", isrAcumulado);
				
				tarifaISRBO = new TarifaISRDeduccion(tablaISRMensual, isrAcumulado);
				tarifaISR = tarifaISRBO.calcular();
				
				isrPreSubsidioBO = new ISRAntesSubsidioDeduccion(dBaseISR.getImporte(), tarifaISR);
				dISRAntesSubsidio = isrPreSubsidioBO.calcular(nomina, idx++);
				isrAntesDeSubsidio = dISRAntesSubsidio.getImporte();
				
				log.info("ISR antes de subsidio al empleo: {}", isrAntesDeSubsidio);
				
				tarifaSubsidioBO = new TarifaSubsidioDeduccion(this.tablaSubsidioMensual, dBaseISR.getImporte());
				tarifaSubsidio = tarifaSubsidioBO.calcular();
				
				isrAcumulado = isrAntesDeSubsidio.subtract(tarifaSubsidio.getCantidadSubsidio());
				isr = isrAcumulado;
				
			} else {
				tarifaISRBO = new TarifaISRDeduccion(tablaISR, dBaseISR.getImporte());
				tarifaISR = tarifaISRBO.calcular();
				
				isrPreSubsidioBO = new ISRAntesSubsidioDeduccion(dBaseISR.getImporte(), tarifaISR);
				dISRAntesSubsidio = isrPreSubsidioBO.calcular(nomina, idx++);
				isrAntesDeSubsidio = dISRAntesSubsidio.getImporte();
				
				//TODO convertir a patrón strategy la tarifa de subsidio al empleo
				tarifaSubsidioBO = new TarifaSubsidioDeduccion(this.tablaSubsidio, dBaseISR.getImporte());
				tarifaSubsidio = tarifaSubsidioBO.calcular();
				
				isr = isrAntesDeSubsidio.subtract(tarifaSubsidio.getCantidadSubsidio());
				
			}
			
			log.info("ISR NETO Semanal: {}", isr);
			dISR = new DetNominaDeduccion();
			dISR.setKey(new DetNominaDeduccionPK(nomina, idx++));
			tdISR = this.getTipoDeduccion(D_ISR);
			dISR.setTipoDeduccion(tdISR);
			dISR.setClave("FRB-" + D_ISR);
			dISR.setNombre("I.S.R.");
			dISR.setImporte(isr);
			dISR.setProcesar(true);
			
			deduccionesISR.add(dISRAntesSubsidio);
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
		
		topSubsidioEmpleo = this.getTipoOtroPago(AbstractOtroPago.OP_SUBSIDIO_AL_SALARIO);
		
		opSubsidioEmpleo = new DetNominaOtroPago();
		opSubsidioEmpleo.setKey(new DetNominaOtroPagoPK(nomina, indexOP));
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
	
	public BigDecimal calcularBaseISRAcumulado(BigDecimal baseISRSemanal, BigDecimal isrAntesDeSubsidioSemanal) {
		BigDecimal baseISRAcumulado = null;
		List<BigDecimal> listaBaseISR = null;
		final BigDecimal cero = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal baseISR = null;
		
		try {
			listaBaseISR = new ArrayList<>();
			
			//Se evalúa la Base de ISR y el ISR pagado por cada semana.
			for(DetNomina nomina : this.listaNominaMes) {
				
				//Se obtiene la base de ISR pagado por cada semana.
				List<DetNominaPercepcion> collectPercepciones = nomina.getPercepciones()
						.stream()
						.filter(p -> (p.getImporteGravado().compareTo(cero) > 0 ))
						.collect(Collectors.toList());
				
				
				baseISR = collectPercepciones
						.stream()
						.map(item -> item.getImporteGravado())
						.reduce(cero, BigDecimal::add)
						;
				
				listaBaseISR.add(baseISR);
			}
			
			baseISRAcumulado = listaBaseISR
					.stream()
					.reduce(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal::add)
					;
			baseISRAcumulado = baseISRAcumulado.add(baseISRSemanal);
			
			log.info("Base ISR mensual: {}", baseISRAcumulado);
			
		} catch(Exception ex) {
			baseISRAcumulado = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		return baseISRAcumulado;
	}

	public DetNominaOtroPago getOpSubsidioEmpleo() {
		return opSubsidioEmpleo;
	}

	public void setTiposOtroPago(List<CatTipoOtroPago> tiposOtroPago) {
		this.tiposOtroPago = tiposOtroPago;
	}

	public void setListaNominaMes(List<DetNomina> listaNominaMes) {
		this.listaNominaMes = listaNominaMes;
	}

	public void setTablaISRMensual(List<CatTarifaISR> tablaISRMensual) {
		this.tablaISRMensual = tablaISRMensual;
	}

	public void setTablaSubsidioMensual(List<CatSubsidio> tablaSubsidioMensual) {
		this.tablaSubsidioMensual = tablaSubsidioMensual;
	}
}
