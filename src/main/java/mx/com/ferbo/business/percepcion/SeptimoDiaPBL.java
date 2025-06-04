package mx.com.ferbo.business.percepcion;

import static mx.com.ferbo.enums.ValoresBD._CERO;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.util.SGPException;

public class SeptimoDiaPBL extends AbstractPBL {
	
	private static Logger log = LogManager.getLogger(SeptimoDiaPBL.class);
	
	private BigDecimal diasNoLaborales  = null;
	private BigDecimal diasVacaciones   = null;
	private BigDecimal diasTrabajados   = null;
	
	public SeptimoDiaPBL(ParametrosNomina parametros, DetNomina nomina, BigDecimal diasLaborales, BigDecimal diasNoLaborales, BigDecimal diasTrabajados, BigDecimal diasVacaciones) {
		super(parametros, nomina);
		this.baseCalculo = this.nomina.getReceptor().getSalarioDiario();
		
		this.diasNoLaborales = diasNoLaborales;
		this.diasTrabajados = diasTrabajados;
		this.diasVacaciones = diasVacaciones;
	}
	
	public SeptimoDiaPBL(ParametrosNomina parametros, DetNomina nomina) {
		super(parametros, nomina);
		this.baseCalculo = this.nomina.getReceptor().getSalarioDiario();
		this.diasTrabajados = nomina.getDiasLaborados();
		this.diasVacaciones = nomina.getDiasVacaciones();
		this.diasNoLaborales = nomina.getDiasNoLaborales();
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
			
			this.cantidad = cantidad;
			this.importe = this.calcularImporte(this.cantidad, baseCalculo);
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
			percepcion = this.build(nomina, CVE_SEPTIMO_DIA, cantidad, importeExento, importeGravado);
		}
		
		return percepcion;
	}
	
	@Override
	protected BigDecimal calcularCantidad(DetNomina nomina) throws SGPException {
		BigDecimal cantidad = null;
		BigDecimal t = null;
		BigDecimal proporcionalSemanal = null;
		
		t = new BigDecimal(parametros.getDiasPeriodo())
				.setScale(2, RoundingMode.HALF_UP)
				.subtract(diasNoLaborales)
				.setScale(2, RoundingMode.HALF_UP);
		
		proporcionalSemanal = diasTrabajados
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
}
