package mx.com.ferbo.business.percepcion;

import static mx.com.ferbo.enums.ValoresBD._CERO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaIncidencia;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

public class SeptimoDiaPBL extends PercepcionBL {
	
	private static Logger log = LogManager.getLogger(SeptimoDiaPBL.class);
	
	private BigDecimal diasNoLaborales  = null;
	private BigDecimal diasVacaciones   = null;
	private BigDecimal diasTrabajados   = null;
	private Date       incidenciaInicio = null;
	
	public SeptimoDiaPBL(ParametrosNomina parametros, DetNomina nomina) {
		super(parametros, nomina);
		this.baseCalculo = this.nomina.getReceptor().getSalarioDiario();
		this.diasTrabajados = nomina.getDiasLaborados();
		this.diasVacaciones = nomina.getDiasVacaciones();
		this.diasNoLaborales = nomina.getDiasNoLaborales();
		this.incidenciaInicio = parametros.getIncidenciaInicio();
	}

	@Override
	public DetNominaPercepcion procesar(DetNomina nomina) throws SGPException {
		DetNominaPercepcion percepcion = null;
		this.cantidad = this.calcularCantidad(nomina);
		percepcion = this.procesar(nomina, this.cantidad);
		return percepcion;
	}
	
	@Override
	public DetNominaPercepcion procesar(DetNomina nomina, BigDecimal cantidad) throws SGPException {
		DetNominaPercepcion percepcion = null;
		
		try {
			if(cantidad == null)
				throw new SGPException("No se indicó el valor (cantidad).");
			
			List<BigDecimal> salarios = nomina.getIncidencias().stream()
					.map(DetNominaIncidencia::getSalario)
					.filter(Objects::nonNull)
					.distinct()
					.collect(Collectors.toList())
					;
			
			if(salarios.size() > 1) {
				this.importe = this.calcularSeptimoDia(nomina.getDiasLaborales(), nomina.getIncidencias());
			} else {
				this.cantidad = cantidad;
				this.importe = this.calcularImporte(this.cantidad, baseCalculo);
			}
			
			this.calcularExentoGravado();
			
			log.info("Sexto y septimo día se agregaron directamente al objeto nomina.percepciones");
		} catch(SGPException ex) {
			log.warn("[UI] {}", ex.getMessage());
			this.cantidad       = ValoresBD._CERO.get();
			this.importe        = ValoresBD._CERO.get();
			this.importeExento  = ValoresBD._CERO.get();
			this.importeGravado = ValoresBD._CERO.get();
			throw ex;
		} catch(Exception ex) {
			log.error("Problema para generar la percepción...", ex);
			this.cantidad       = ValoresBD._CERO.get();
			this.importe        = ValoresBD._CERO.get();
			this.importeExento  = ValoresBD._CERO.get();
			this.importeGravado = ValoresBD._CERO.get();
		} finally {
			percepcion = this.build(nomina, CVE_SEPTIMO_DIA, cantidad, this.importe, this.importeExento, this.importeGravado);
		}
		
		return percepcion;
	}
	
	@Override
	protected BigDecimal calcularCantidad(DetNomina nomina) throws SGPException {
		BigDecimal cantidad = null;
		BigDecimal t = null;
		BigDecimal proporcionalSemanal = null;
		Boolean inicioEntrePeriodo = null;
		Integer iDiasSinRelacionLaboral = 0;
		BigDecimal diasSinRelacionLaboral = null;
		BigDecimal diasIncapacidad = null;
		
		inicioEntrePeriodo     = DateUtil.isDateBetween(nomina.getReceptor().getInicioRelacionLaboral(), DateUtil.toDate(nomina.getPeriodoInicio()), DateUtil.toDate(nomina.getPeriodoFin()));
		log.info("Inicio periodo: {}, Inicio relación laboral: {}, Fin periodo: {}", nomina.getPeriodoInicio(), nomina.getReceptor().getInicioRelacionLaboral(), nomina.getPeriodoFin());
		
		if(inicioEntrePeriodo) {
			iDiasSinRelacionLaboral = DateUtil.daysDiffNonInclusive(this.incidenciaInicio, nomina.getReceptor().getInicioRelacionLaboral());
			iDiasSinRelacionLaboral = iDiasSinRelacionLaboral - this.diasNoLaborales.intValue();
		}
		
		diasSinRelacionLaboral = new BigDecimal(iDiasSinRelacionLaboral).setScale(0, RoundingMode.HALF_UP);
		diasIncapacidad = nomina.getDiasIncapacidad();
		
		log.info("El empleado tiene inicio de relación laboral dentro del periodo de nómina: Fecha Inicio relación laboral: {}", nomina.getReceptor().getInicioRelacionLaboral());
		
		t = new BigDecimal(parametros.getDiasPeriodo())
				.setScale(2, RoundingMode.HALF_UP)
				.subtract(diasNoLaborales)
				.setScale(2, RoundingMode.HALF_UP);
		
		proporcionalSemanal = diasTrabajados
				.add(diasIncapacidad)
				.add(diasSinRelacionLaboral)
				.add(diasVacaciones)
				.divide(t, 4, RoundingMode.HALF_UP)
				;
		
		cantidad = proporcionalSemanal.multiply(this.diasNoLaborales);
		
		return cantidad;
	}

	/**El septimo dia está gravado al 100% con ISR.
	 */
	@Override
	public BigDecimal calcularLimiteExento() {
		log.info("[UI] Limite exento septimo dia = {}", _CERO.get());
		return _CERO.get();
	}
	
	private BigDecimal calcularSeptimoDia(BigDecimal diasLaborables, List<DetNominaIncidencia> incidencias)
	throws SGPException {
		BigDecimal septimoDia = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		BigDecimal proporcionDiaria = null;
		BigDecimal importeDiario = null;
		
		if(diasLaborables == null)
			throw new SGPException("El empleado no tiene configurados sus días laborables.");
			
		if(diasLaborables.compareTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) <= 0)
			throw new SGPException("El empleado no tiene configurados sus días laborables.");
		
		proporcionDiaria = BigDecimal.ONE.setScale(4, RoundingMode.HALF_UP)
				.divide(diasLaborables, RoundingMode.HALF_UP)
				;
		
		for(DetNominaIncidencia incidencia : incidencias) {
			
			switch (incidencia.getClave()) {
			case "T":
			case "R":
			case "J":
			case "V":
			case "O":
			case "N":
				importeDiario = incidencia.getSalario().multiply(proporcionDiaria).setScale(2, RoundingMode.HALF_UP);
				septimoDia = septimoDia.add(importeDiario);
				log.info("Agregando proporción del septimo día ({}): {}", proporcionDiaria, importeDiario);
				break;
			default:
				break;
			}
		}
		
		return septimoDia;
	}
}
