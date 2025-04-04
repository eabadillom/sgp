package mx.com.ferbo.business.deduccion.isr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AbstractDBL;
import mx.com.ferbo.business.deduccion.IDeducciones;
import mx.com.ferbo.business.deduccion.subsidio.ISubsidioEmpleo;
import mx.com.ferbo.business.deduccion.subsidio.SubsidioEmpleoExecutor;
import mx.com.ferbo.business.otropago.AbstractOtroPago;
import mx.com.ferbo.enums.ValoresBD;
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

/**Cálculo del ISR, conforme al Decreto del DOF 1 de mayo 2024.
 */
public class ISRSemanalDBL2 extends AbstractDBL implements IDeducciones {
	
	private static Logger log = LogManager.getLogger(ISRSemanalDBL2.class);
	
	private SubsidioEmpleoExecutor subsidioExecutor = null;
	private ISubsidioEmpleo        tarifaSubsidioBO = null;
	private List<CatTarifaISR>     tablaISR = null;
	
	private DetNominaOtroPago      opSubsidioEmpleo = null;
	private List<CatTipoOtroPago>  tiposOtroPago = null;
	
	private Date                   periodoInicio = null;
	private Date                   periodoFin = null;
	private Boolean                ultimaSemanaMes = null;
	
	private List<DetNomina>        listaNominaMes = null;
	private List<CatTarifaISR>     tablaISRSemanal = null;
	private List<CatTarifaISR>     tablaISRMensual = null;
	
	public ISRSemanalDBL2(Date periodoInicio, Date periodoFin, List<CatTipoDeduccion> tiposDeduccion, List<CatTipoOtroPago> tiposOtroPago, List<CatTarifaISR> tablaISR, List<DetNomina> nominaMensual)
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
		this.periodoInicio   = periodoInicio;
		this.periodoFin      = periodoFin;
		this.ultimaSemanaMes = this.esUltimaSemanaMes(periodoInicio, periodoFin);
	}
	
	@Override
	public void procesar(DetNomina nomina) {
		List<DetNominaDeduccion>  deduccionesISR = null;
		List<DetNominaPercepcion> percepciones = null;
		ISRBaseDBL          baseISRBO = null;
		TarifaISRBL        tarifaISRBO = null;
		
		DetNominaDeduccion        dBaseISR = null;
		CatTarifaISR              tarifaISR = null;
		CatTipoDeduccion          tdISR = null;
		
		BigDecimal                importeSubsidioSemanal   = null;
		BigDecimal                importeSubsidioMensual   = null;
		BigDecimal                importeSubsidioAcumulado = null;
		BigDecimal                importeSubsidio          = null;
		BigDecimal                importe                  = null;
		
		ISRAntesSubsidioDBL isrPreSubsidioBO         = null;
		ISRAntesSubsidioDBL isrPreSubsidioMensualBO  = null;
		
		DetNominaDeduccion        dISRAntesSubsidio        = null;
		DetNominaDeduccion        dISR                     = null;
		
		BigDecimal                isrAntesDeSubsidioSemanal   = null;
		BigDecimal                isrAntesDeSubsidioMensual   = null;
		BigDecimal                isrDespuesDeSubsidioSemanal = null;
		BigDecimal                isrDespuesDeSubsidioMensual = null;
		
		BigDecimal                baseISRSemanal                      = null;
		BigDecimal                baseISRMensual                      = null;
		BigDecimal                isrAntesDeSubsidioSemanasAnteriores = null;
		BigDecimal                isrRetenidoSemanasAnteriores        = null;
		DetNominaDeduccion        dISRAntesSubsidioMensual            = null;
		
		BigDecimal                ajusteISRMensual          = null;
		DetNominaDeduccion        dAjusteISRMensual         = null;
		BigDecimal                ajusteAlSubsidioCausado   = null;
		DetNominaDeduccion        dAjusteAlSubsidioCausado  = null;
		BigDecimal                isrAjustadoPorSubsidio    = null;
		
		Integer idx = null;
		
		try {
			idx = this.nuevoIndiceDe(nomina.getDeducciones());
			percepciones = nomina.getPercepciones();
			
			deduccionesISR = new ArrayList<>();
			
			//1. Obtener la base para el ISR (semanal)
			//Para el cálculo del ISR a retener al empleado se deben sumar las percepciones que gravan para ISR
			baseISRBO = new ISRBaseDBL(percepciones);
			baseISRBO.setTiposDeduccion(this.tiposDeduccion);
			dBaseISR = baseISRBO.calcular(nomina);
			baseISRSemanal = dBaseISR.getImporte();
			
			tarifaISRBO = new TarifaISRBL(this.tablaISRSemanal, baseISRSemanal);
			tarifaISR = tarifaISRBO.calcular();
			
			isrPreSubsidioBO = new ISRAntesSubsidioDBL(baseISRSemanal, tarifaISR);
			dISRAntesSubsidio = isrPreSubsidioBO.calcular(nomina);
			isrAntesDeSubsidioSemanal = dISRAntesSubsidio.getImporte();
			
			if(this.subsidioExecutor == null)
				this.subsidioExecutor = new SubsidioEmpleoExecutor();
			
			if(this.tarifaSubsidioBO == null) {
				this.tarifaSubsidioBO = subsidioExecutor.loadClass("SUBEM", DateUtil.toLocalDate(periodoFin), isrAntesDeSubsidioSemanal);
			}
			
			importeSubsidioSemanal = this.tarifaSubsidioBO.calcular(ISubsidioEmpleo.PERIODO_SEMANAL, dBaseISR.getImporte());
			
			importeSubsidio = importeSubsidioSemanal;
			
			//ISR Semanal despues de subsidio al salario.
			isrDespuesDeSubsidioSemanal = isrAntesDeSubsidioSemanal.subtract(importeSubsidioSemanal);
			
			
			//CALCULO DE ISR MENSUAL
			if(this.ultimaSemanaMes) {
				log.info("Percepciones: {}", percepciones);
				
				baseISRMensual = this.calcularBaseISRAcumulado(baseISRSemanal, isrDespuesDeSubsidioSemanal);
				log.info("Base ISR (mensual): {}", baseISRMensual);
				
				
				
				tarifaISRBO = new TarifaISRBL(tablaISRMensual, baseISRMensual);
				tarifaISR = tarifaISRBO.calcular();
				
				isrPreSubsidioMensualBO = new ISRAntesSubsidioDBL(baseISRMensual, tarifaISR);
				dISRAntesSubsidioMensual = isrPreSubsidioMensualBO.calcular(nomina);
				isrAntesDeSubsidioMensual = dISRAntesSubsidioMensual.getImporte();
				log.info("ISR mensual antes de subsidio al empleo: {}", isrAntesDeSubsidioMensual);
				
				importeSubsidioMensual = tarifaSubsidioBO.calcular(ISubsidioEmpleo.PERIODO_MENSUAL, baseISRMensual);
				
				isrDespuesDeSubsidioMensual = isrAntesDeSubsidioMensual.subtract(importeSubsidioMensual);
				log.info("ISR Mensual después de subsidio: {}", isrDespuesDeSubsidioMensual);
				
				isrAntesDeSubsidioSemanasAnteriores = this.calcularISRAntesSubsidioSemanasAnteriores(this.listaNominaMes);
				log.info("ISR antes de subsidio mensual en las semanas anteriores: {}", isrAntesDeSubsidioSemanasAnteriores);
				
				isrRetenidoSemanasAnteriores = this.calcularISRSemanasAnteriores(this.listaNominaMes);
				log.info("ISR Retenido en las semanas anteriores: {}", isrRetenidoSemanasAnteriores);
				
				importeSubsidioAcumulado = this.calcularSubsidioSemanasAnteriores(this.listaNominaMes);
				log.info("Subsidio otorgado en las semanas anteriores: {}", importeSubsidioAcumulado);
				
				importeSubsidio = importeSubsidioMensual.subtract(importeSubsidioAcumulado);
				
				if(importeSubsidio.compareTo(BigDecimal.ZERO) < 0)
					importeSubsidio = ValoresBD._CERO.get();
				
				log.info("Subsidio otorgado por ajuste mensual: {}", importeSubsidio);
				
				isrDespuesDeSubsidioSemanal = isrAntesDeSubsidioSemanal.subtract(importeSubsidio);
				
				isrDespuesDeSubsidioSemanal = isrAntesDeSubsidioMensual
						.subtract(isrAntesDeSubsidioSemanasAnteriores)
						.subtract(importeSubsidio)
						;
				
				//Se evalúa que el subsidio mensual sea cero y que se haya otorgado subsidio al empleo en las semanas anteriores.
				if( importeSubsidioMensual
					.subtract(importeSubsidioAcumulado)
					.compareTo(ValoresBD._CERO.get()) < 0) {
					
					ajusteISRMensual = importeSubsidioAcumulado.setScale(2, BigDecimal.ROUND_HALF_UP);
					dAjusteISRMensual = this.getDeduccion(nomina, idx++, TD_AJUSTE_ISR_MENSUAL, CVE_AJUSTE_ISR_MENSUAL, "ISR de ajuste mensual", true, true, ajusteISRMensual);
					
					ajusteAlSubsidioCausado = importeSubsidioAcumulado.setScale(2, BigDecimal.ROUND_HALF_UP);
					dAjusteAlSubsidioCausado = this.getDeduccion(nomina, idx++, TD_AJUSTE_AL_SUBSIDIO, CVE_AJUSTE_AL_SUBSIDIO, "Ajuste al subsidio causado", true, false, ajusteAlSubsidioCausado);
					
					isrAjustadoPorSubsidio = importeSubsidioAcumulado.setScale(2, BigDecimal.ROUND_HALF_UP);
					this.procesaISRAjustadoPorSubsidio(nomina, isrAjustadoPorSubsidio);
				}
			}
			
			log.info("ISR NETO Semanal: {}", isrDespuesDeSubsidioSemanal);
			
			tdISR = this.getTipoDeduccion(TD_ISR);
			if(isrDespuesDeSubsidioSemanal.compareTo(ValoresBD._CERO.get()) < 0) {
				importe = ValoresBD._CERO.get();
			} else {
				importe = isrDespuesDeSubsidioSemanal.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			
			dISR = new DetNominaDeduccion.Builder()
					.key(new DetNominaDeduccionPK(nomina, idx++))
					.tipoDeduccion(tdISR)
					.clave(CVE_ISR)
					.nombre("I.S.R.")
					.importe(importe)
					.informar(true)
					.procesar(true)
					.build();
			
			deduccionesISR.add(dISRAntesSubsidio);
			deduccionesISR.add(dISR);
			
			if(dAjusteISRMensual != null)
				deduccionesISR.add(dAjusteISRMensual);
			
			if(dAjusteAlSubsidioCausado != null)
				deduccionesISR.add(dAjusteAlSubsidioCausado);
			
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
		BigDecimal isrSemanasAnteriores = ValoresBD._CERO.get();
		BigDecimal isrSemanal = null;
		
		for(DetNomina n : listaNominaMes) {
			List<DetNominaDeduccion> deducciones = n.getDeducciones();
			isrSemanal = deducciones.stream()
					.filter(d -> d.getClave().equals(CVE_ISR) && d.getProcesar() == true )
					.map(d -> d.getImporte())
					.reduce(ValoresBD._CERO.get(), BigDecimal :: add)
					;
			log.info("ISR causado en el periodo {}: {}", n.getPeriodo(), isrSemanal);
			isrSemanasAnteriores = isrSemanasAnteriores.add(isrSemanal);
		}
		
		return isrSemanasAnteriores;
	}
	
	private BigDecimal calcularISRAntesSubsidioSemanasAnteriores(List<DetNomina> listaNominaMes) {
		BigDecimal isrSemanasAnteriores = ValoresBD._CERO.get();
		BigDecimal isrSemanal = null;
		
		for(DetNomina n : listaNominaMes) {
			List<DetNominaDeduccion> deducciones = n.getDeducciones();
			isrSemanal = deducciones.stream()
					.filter(d -> d.getClave().equalsIgnoreCase(CVE_ISR_ANTES_DE_SUBSIDIO))
					.map(d -> d.getImporte())
					.reduce(ValoresBD._CERO.get(), BigDecimal :: add)
					;
			log.info("ISR ante de subsidio en el periodo {}: {}", n.getPeriodo(), isrSemanal);
			isrSemanasAnteriores = isrSemanasAnteriores.add(isrSemanal);
		}
		
		return isrSemanasAnteriores;
	}
	
	private BigDecimal calcularSubsidioSemanasAnteriores(List<DetNomina> listaNominaMes) {
		BigDecimal subsidioSemanasAnteriores = ValoresBD._CERO.get();
		BigDecimal subsidioSemanal = null;
		
		for(DetNomina n : listaNominaMes) {
			List<DetNominaOtroPago> otrosPagos = n.getOtrosPagos();
			
			subsidioSemanal = otrosPagos.stream()
					.filter(o -> o.getClave().equals(AbstractOtroPago.CVE_SUBSIDIO_AL_SALARIO))
					.map(o -> o.getImporte())
					.reduce(ValoresBD._CERO.get(), BigDecimal :: add)
					;
			
			log.info("Subsidio otorgado en el periodo {}: {}", n.getPeriodo(), subsidioSemanal);
			subsidioSemanasAnteriores = subsidioSemanasAnteriores.add(subsidioSemanal);
		}
		
		return subsidioSemanasAnteriores;
	}

	public void procesaSubsidioAlEmpleo(DetNomina nomina, BigDecimal importeSubsidio) {
		CatTipoOtroPago topSubsidioEmpleo = null;
		int indexOP = -1;
		
		if(importeSubsidio.compareTo(ValoresBD._CERO.get()) <= 0)
			return;
		
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
		opSubsidioEmpleo.setClave(AbstractOtroPago.CVE_SUBSIDIO_AL_SALARIO);
		opSubsidioEmpleo.setNombre("Subs al empleo acreditado");
		opSubsidioEmpleo.setImporte(importeSubsidio);
		opSubsidioEmpleo.setInformar(true);
		opSubsidioEmpleo.setProcesar(false);
		nomina.getOtrosPagos().add(opSubsidioEmpleo);
	}
	
	public void procesaISRAjustadoPorSubsidio(DetNomina nomina, BigDecimal importeSubsidio) {
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
		
		topSubsidioEmpleo = this.getTipoOtroPago(AbstractOtroPago.OP_ISR_AJUSTADO_POR_SUBSIDIO);
		
		opSubsidioEmpleo = new DetNominaOtroPago();
		opSubsidioEmpleo.setKey(new DetNominaOtroPagoPK(nomina, indexOP));
		opSubsidioEmpleo.setTipoOtroPago(topSubsidioEmpleo);
		opSubsidioEmpleo.setClave(AbstractOtroPago.CVE_ISR_AJUSTADO_POR_SUBSIDIO);
		opSubsidioEmpleo.setNombre("ISR ajustado por subsidio");
		opSubsidioEmpleo.setImporte(importeSubsidio);
		opSubsidioEmpleo.setInformar(true);
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
		BigDecimal baseISR = null;
		
		try {
			listaBaseISR = new ArrayList<>();
			
			//Se evalúa la Base de ISR y el ISR pagado por cada semana.
			for(DetNomina nomina : this.listaNominaMes) {
				
				//Se obtiene la base de ISR pagado por cada semana.
				List<DetNominaPercepcion> collectPercepciones = nomina.getPercepciones()
						.stream()
						.filter(p -> (p.getImporteGravado().compareTo(ValoresBD._CERO.get()) > 0 ))
						.collect(Collectors.toList());
				
				
				baseISR = collectPercepciones
						.stream()
						.map(item -> item.getImporteGravado())
						.reduce(ValoresBD._CERO.get(), BigDecimal::add)
						;
				
				listaBaseISR.add(baseISR);
			}
			
			baseISRAcumulado = listaBaseISR
					.stream()
					.reduce(ValoresBD._CERO.get(), BigDecimal::add)
					;
			
			baseISRAcumulado = baseISRAcumulado.add(baseISRSemanal);
			
			log.info("Base ISR mensual: {}", baseISRAcumulado);
			
		} catch(Exception ex) {
			baseISRAcumulado = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		return baseISRAcumulado;
	}
	
	private Boolean esUltimaSemanaMes(Date periodoInicio, Date periodoFin) {
		Boolean ultimaSemanaMes        = null;
		Integer mesActualInicio        = null;
		Integer mesActualFin           = null;
		Date    periodoSiguienteInicio = null;
		Date    periodoSiguienteFin    = null;
		Integer mesSiguienteInicio     = null;
		Integer mesSiguienteFin        = null;
		Integer diaInicioMes           = null;
		
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
			} else {
				ultimaSemanaMes = new Boolean(false);
			}
			
		} catch(Exception ex) {
			ultimaSemanaMes = new Boolean(false);
		}
		
		return ultimaSemanaMes;
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
