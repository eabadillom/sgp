package mx.com.ferbo.business.deduccion.isr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AbstractDeduccion;
import mx.com.ferbo.business.deduccion.AbstractOtroPago;
import mx.com.ferbo.business.deduccion.IDeducciones;
import mx.com.ferbo.business.deduccion.subsidio.ISubsidioEmpleo;
import mx.com.ferbo.business.deduccion.subsidio.SubsidioEmpleoExecutor;
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
	
	private SubsidioEmpleoExecutor subsidioExecutor = null;
	private ISubsidioEmpleo tarifaSubsidioBO = null;
	
	private List<CatTarifaISR> tablaISR = null;
	private List<DetNominaPercepcion> percepciones = null;
	
	private DetNominaOtroPago opSubsidioEmpleo = null;
	private List<CatTipoOtroPago> tiposOtroPago = null;
	
	private Date periodoInicio = null;
	private Date periodoFin = null;
	private Boolean ultimaSemanaMes = null;
	
	private List<DetNomina> listaNominaMes = null;
	private List<CatTarifaISR> tablaISRMensual = null;
	
	public ISRSemanalDeduccion(List<CatTipoDeduccion> tiposDeduccion, List<CatTipoOtroPago> tiposOtroPago, List<DetNominaPercepcion> percepciones, List<CatTarifaISR> tablaISR) {
		this.tiposDeduccion = tiposDeduccion;
		this.tiposOtroPago = tiposOtroPago;
		this.percepciones = percepciones;
		this.tablaISR = tablaISR;
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
	public List<DetNominaDeduccion> procesar(DetNomina nomina, Integer index) {
		List<DetNominaDeduccion> deduccionesISR = null;
		ISRBaseDeduccion baseISRBO = null;
		TarifaISRDeduccion tarifaISRBO = null;
		
		DetNominaDeduccion dBaseISR = null;
		CatTarifaISR tarifaISR = null;
		
		BigDecimal importeSubsidio = null;
		
		ISRAntesSubsidioDeduccion isrPreSubsidioBO = null;
		
		ISRAntesSubsidioDeduccion isrPreSubsidioMensualBO = null;
		DetNominaDeduccion dISRAntesSubsidio = null;
		DetNominaDeduccion dISR = null;
		
		CatTipoDeduccion tdISR = null;
		
		BigDecimal isrAntesDeSubsidio = null;
		BigDecimal isr = null;
		
		BigDecimal baseISRMensual = null;
		BigDecimal isrRetenidoSemanasAnteriores = null;
		DetNominaDeduccion dISRAntesSubsidioMensual = null;
		
		Integer idx = null;
		
		try {
			idx = this.nuevoIndiceDe(nomina.getDeducciones());
			
			deduccionesISR = new ArrayList<>();
			
			//1. Obtener la base para el ISR (semanal)
			//Para el cálculo del ISR a retener al empleado se deben sumar las percepciones que gravan para ISR
			baseISRBO = new ISRBaseDeduccion(percepciones);
			baseISRBO.setTiposDeduccion(tiposDeduccion);
			dBaseISR = baseISRBO.calcular(nomina, idx++);
			
			
			tarifaISRBO = new TarifaISRDeduccion(tablaISR, dBaseISR.getImporte());
			tarifaISR = tarifaISRBO.calcular();
			
			isrPreSubsidioBO = new ISRAntesSubsidioDeduccion(dBaseISR.getImporte(), tarifaISR);
			dISRAntesSubsidio = isrPreSubsidioBO.calcular(nomina, idx++);
			isrAntesDeSubsidio = dISRAntesSubsidio.getImporte();
			
			if(this.subsidioExecutor == null)
				this.subsidioExecutor = new SubsidioEmpleoExecutor();
			
			if(this.tarifaSubsidioBO == null)
				this.tarifaSubsidioBO = subsidioExecutor.loadClass("SUBEM", DateUtils.toLocalDate(periodoFin));
			
			importeSubsidio = this.tarifaSubsidioBO.calcular(ISubsidioEmpleo.PERIODO_SEMANAL, dBaseISR.getImporte());
			
			//ISR Semanal despues de subsidio al salario.
			isr = isrAntesDeSubsidio.subtract(importeSubsidio);
			
			
			//CALCULO DE ISR MENSUAL
			if(this.ultimaSemanaMes) {
				log.info("Percepciones: {}", this.percepciones);
				
				baseISRMensual = this.calcularBaseISRAcumulado(dBaseISR.getImporte(), isr);
				log.info("Base ISR (mensual): {}", baseISRMensual);
				
				tarifaISRBO = new TarifaISRDeduccion(tablaISRMensual, baseISRMensual);
				tarifaISR = tarifaISRBO.calcular();
				
				isrPreSubsidioMensualBO = new ISRAntesSubsidioDeduccion(baseISRMensual, tarifaISR);
				dISRAntesSubsidioMensual = isrPreSubsidioMensualBO.calcular(nomina, idx++);
				isrAntesDeSubsidio = dISRAntesSubsidioMensual.getImporte();
				log.info("ISR antes de subsidio al empleo: {}", isrAntesDeSubsidio);
				
				importeSubsidio = tarifaSubsidioBO.calcular(ISubsidioEmpleo.PERIODO_MENSUAL, baseISRMensual);
				
				baseISRMensual = isrAntesDeSubsidio.subtract(importeSubsidio);
				log.info("ISR Mensual después de subsidio: {}", baseISRMensual);
				
				isrRetenidoSemanasAnteriores = this.calcularISRSemanasAnteriores(this.listaNominaMes);
				log.info("ISR Retenido en las semanas anteriores: {}", isrRetenidoSemanasAnteriores);
				
				isr = baseISRMensual.subtract(isrRetenidoSemanasAnteriores);
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
			
			this.procesaSubsidioAlEmpleo(nomina, importeSubsidio);
			
			
			nomina.getDeducciones().addAll(deduccionesISR);
		} catch(Exception ex) {
			log.error("Problema para calcular el ISR...", ex);
		} finally {
			this.tiposDeduccion = null;
			this.tablaISR = null;
			this.percepciones = null;
		}
		
		return null;
	}
	
	private BigDecimal calcularISRSemanasAnteriores(List<DetNomina> listaNominaMes) {
		BigDecimal isrSemanasAnteriores = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal isrSemanal = null;
		
		for(DetNomina n : listaNominaMes) {
			List<DetNominaDeduccion> deducciones = n.getDeducciones();
			isrSemanal = deducciones.stream()
					.filter(d -> d.getTipoDeduccion().getClave().equals("002") && d.getProcesar() == true )
					.map(d -> d.getImporte())
					.reduce(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal :: add)
					;
			
			isrSemanasAnteriores = isrSemanasAnteriores.add(isrSemanal);
		}
		
		return isrSemanasAnteriores;
	}

	public void procesaSubsidioAlEmpleo(DetNomina nomina, BigDecimal importeSubsidio) {
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
		opSubsidioEmpleo.setImporte(importeSubsidio);
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

}
