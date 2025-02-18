package mx.com.ferbo.business.deduccion.isr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AbstractDeduccion;
import mx.com.ferbo.business.deduccion.IDeducciones;
import mx.com.ferbo.business.deduccion.subsidio.ISubsidioEmpleo;
import mx.com.ferbo.business.deduccion.subsidio.SubsidioEmpleoExecutor;
import mx.com.ferbo.business.otropago.AbstractOtroPago;
import mx.com.ferbo.business.otropago.ReintegroISROtroPago;
import mx.com.ferbo.model.CatTarifaISR;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.DetNominaOtroPago;
import mx.com.ferbo.model.DetNominaOtroPagoPK;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.model.sat.CatTipoOtroPago;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

/**Cálculo del ISR, conforme a la reforma de diciembre de 2013.
 */
public class ISRSemanalDeduccion1 extends AbstractDeduccion implements IDeducciones {
	
	private static Logger log = LogManager.getLogger(ISRSemanalDeduccion1.class);
	
	private SubsidioEmpleoExecutor subsidioExecutor = null;
	private ISubsidioEmpleo tarifaSubsidioBO = null;
	
	private List<CatTarifaISR> tablaISR = null;
	
	private DetNominaOtroPago opSubsidioEmpleo = null;
	private List<CatTipoOtroPago> tiposOtroPago = null;
	
	private Date periodoInicio = null;
	private Date periodoFin = null;
	private Boolean ultimaSemanaMes = null;
	
	private List<DetNomina> listaNominaMes = null;
	private List<CatTarifaISR> tablaISRSemanal = null;
	private List<CatTarifaISR> tablaISRMensual = null;
	
	public ISRSemanalDeduccion1(Date periodoInicio, Date periodoFin, List<CatTipoDeduccion> tiposDeduccion, List<CatTipoOtroPago> tiposOtroPago, List<CatTarifaISR> tablaISR, List<DetNomina> nominaMensual)
	throws SGPException {
		try {
			this.setPeriodo(periodoInicio, periodoFin);
			this.tiposDeduccion = tiposDeduccion;
			this.tiposOtroPago = tiposOtroPago;
			this.tablaISR = tablaISR;
			this.listaNominaMes = nominaMensual;
			
			this.tablaISRSemanal = this.tablaISR.stream()
					.filter(t -> "s".equalsIgnoreCase(t.getTipo()))
					.collect(Collectors.toList())
					;
			
			this.tablaISRMensual = this.tablaISR.stream()
					.filter(t -> "m".equalsIgnoreCase(t.getTipo()))
					.collect(Collectors.toList())
					;
		} catch(Exception ex) {
			throw new SGPException("Problema al iniciar el objeto de cálculo de ISR semanal...", ex);
		}
	}
	
	private void setPeriodo(Date periodoInicio, Date periodoFin) {
		Date periodoSiguienteFin = null;
		
		this.periodoInicio = periodoInicio;
		this.periodoFin = periodoFin;
		
		periodoSiguienteFin = DateUtil.addDay(this.periodoFin, 7);
		
		Integer mesActual = DateUtil.getMes(this.periodoFin);
		Integer mesSiguiente = DateUtil.getMes(periodoSiguienteFin);
		
		if(mesSiguiente > mesActual) {
			this.ultimaSemanaMes = new Boolean(true);
			log.info("ULTIMA SEMANA DEL MES: {} - {}", this.periodoInicio, this.periodoFin);
		} else {
			this.ultimaSemanaMes = new Boolean(false);
		}
	}
	
	@Override
	public void procesar(DetNomina nomina) {
		List<DetNominaDeduccion> deduccionesISR = null;
		List<DetNominaPercepcion> percepciones = null;
		ISRBaseDeduccion baseISRBO = null;
		TarifaISRDeduccion tarifaISRBO = null;
		
		DetNominaDeduccion dBaseISR = null;
		CatTarifaISR tarifaISR = null;
		
		BigDecimal importeSubsidio = null;
		
		ISRAntesSubsidioDeduccion isrPreSubsidioBO = null;
		
		ISRAntesSubsidioDeduccion isrPreSubsidioMensualBO = null;
		DetNominaDeduccion dISRAntesSubsidio = null;
		DetNominaDeduccion dISR = null;
		DetNominaOtroPago opISR = null;
		
		CatTipoDeduccion tdISR = null;
		
		BigDecimal isrAntesDeSubsidio = null;
		BigDecimal isrDespuesDeSubsidio = null;
		
		BigDecimal baseISRMensual = null;
		BigDecimal isrRetenidoSemanasAnteriores = null;
		BigDecimal isrMensualDespuesDeSubsidio = null;
		DetNominaDeduccion dISRAntesSubsidioMensual = null;
		
		Integer idxDeduccion = null;
		
		try {
			idxDeduccion = this.nuevoIndiceDe(nomina.getDeducciones());
			percepciones = nomina.getPercepciones();
			
			deduccionesISR = new ArrayList<>();
			
			//1. Obtener la base para el ISR (semanal)
			//Para el cálculo del ISR a retener al empleado se deben sumar las percepciones que gravan para ISR
			baseISRBO = new ISRBaseDeduccion(percepciones);
			baseISRBO.setTiposDeduccion(this.tiposDeduccion);
			dBaseISR = baseISRBO.calcular(nomina, idxDeduccion++);
			
			tarifaISRBO = new TarifaISRDeduccion(this.tablaISRSemanal, dBaseISR.getImporte());
			tarifaISR = tarifaISRBO.calcular();
			
			isrPreSubsidioBO = new ISRAntesSubsidioDeduccion(dBaseISR.getImporte(), tarifaISR);
			dISRAntesSubsidio = isrPreSubsidioBO.calcular(nomina, idxDeduccion++);
			deduccionesISR.add(dISRAntesSubsidio);
			isrAntesDeSubsidio = dISRAntesSubsidio.getImporte();
			
			if(this.subsidioExecutor == null)
				this.subsidioExecutor = new SubsidioEmpleoExecutor();
			
			if(this.tarifaSubsidioBO == null)
				this.tarifaSubsidioBO = subsidioExecutor.loadClass("SUBEM", DateUtil.toLocalDate(periodoFin));
			
			importeSubsidio = this.tarifaSubsidioBO.calcular(ISubsidioEmpleo.PERIODO_SEMANAL, dBaseISR.getImporte());
			
			//ISR Semanal despues de subsidio al salario.
			isrDespuesDeSubsidio = isrAntesDeSubsidio.subtract(importeSubsidio);
			
			
			//CALCULO DE ISR MENSUAL
			if(this.ultimaSemanaMes) {
				log.info("Percepciones: {}", percepciones);
				
				baseISRMensual = this.calcularBaseISRAcumulado(dBaseISR.getImporte(), isrDespuesDeSubsidio);
				log.info("Base ISR (mensual): {}", baseISRMensual);
				
				tarifaISRBO = new TarifaISRDeduccion(tablaISRMensual, baseISRMensual);
				tarifaISR = tarifaISRBO.calcular();
				
				isrPreSubsidioMensualBO = new ISRAntesSubsidioDeduccion(baseISRMensual, tarifaISR);
				dISRAntesSubsidioMensual = isrPreSubsidioMensualBO.calcular(nomina, idxDeduccion++);
				isrAntesDeSubsidio = dISRAntesSubsidioMensual.getImporte();
				log.info("ISR antes de subsidio al empleo: {}", isrAntesDeSubsidio);
				
				importeSubsidio = tarifaSubsidioBO.calcular(ISubsidioEmpleo.PERIODO_MENSUAL, baseISRMensual);
				log.info("Subsidio al empleo: {}", importeSubsidio);
				
				isrMensualDespuesDeSubsidio = isrAntesDeSubsidio.subtract(importeSubsidio);
				log.info("ISR Mensual después de subsidio: {}", isrMensualDespuesDeSubsidio);
				
				isrRetenidoSemanasAnteriores = this.calcularISRSemanasAnteriores(this.listaNominaMes);
				log.info("ISR Retenido en las semanas anteriores: {}", isrRetenidoSemanasAnteriores);
				
				isrDespuesDeSubsidio = isrMensualDespuesDeSubsidio.subtract(isrRetenidoSemanasAnteriores);
			}
			
			log.info("ISR NETO Semanal: {}", isrDespuesDeSubsidio);
			if(isrDespuesDeSubsidio.compareTo(BigDecimal.ZERO) > 0) {
				log.info("Agregando ISR como Deduccion...");
				dISR = new DetNominaDeduccion();
				dISR.setKey(new DetNominaDeduccionPK(nomina, idxDeduccion++));
				tdISR = this.getTipoDeduccion(D_ISR);
				dISR.setTipoDeduccion(tdISR);
				dISR.setClave(AbstractDeduccion.CVE_ISR);
				dISR.setNombre("I.S.R.");
				dISR.setImporte(isrDespuesDeSubsidio);
				dISR.setProcesar(true);
				dISR.setInformar(true);
				
				deduccionesISR.add(dISR);
			} else {
				log.info("Agregando ISR como Otro pago...");
				ReintegroISROtroPago reintegroISROtroPago = new ReintegroISROtroPago(isrDespuesDeSubsidio.abs());
				reintegroISROtroPago.setTiposOtroPago(this.tiposOtroPago);
				opISR = reintegroISROtroPago.calcular(nomina, null);
				nomina.getOtrosPagos().add(opISR);
			}
			
			this.procesaSubsidioAlEmpleo(nomina, importeSubsidio);
			
			
			nomina.getDeducciones().addAll(deduccionesISR);
		} catch(Exception ex) {
			log.error("Problema para calcular el ISR...", ex);
		} finally {
			this.tiposDeduccion = null;
			this.tiposOtroPago = null;
			this.tablaISRSemanal = null;
			this.tablaISRMensual = null;
			this.tablaISR = null;
		}
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
		opSubsidioEmpleo.setClave(AbstractOtroPago.CVE_SUBSIDIO);
		opSubsidioEmpleo.setNombre("Subs. al empleo mes");
		opSubsidioEmpleo.setImporte(importeSubsidio);
		
		if(importeSubsidio.compareTo(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP)) > 0) {
			opSubsidioEmpleo.setInformar(true);
			opSubsidioEmpleo.setProcesar(true);
		} else {
			opSubsidioEmpleo.setInformar(true);
			opSubsidioEmpleo.setProcesar(false);
		}
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
