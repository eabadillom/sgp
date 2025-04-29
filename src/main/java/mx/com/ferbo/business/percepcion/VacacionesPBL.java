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

public class VacacionesPBL extends AbstractPBL {
	
	private static Logger log = LogManager.getLogger(VacacionesPBL.class);
	
	private BigDecimal diasVacaciones = null;
	
	public VacacionesPBL(ParametrosNomina parametros, DetNomina nomina, BigDecimal diasVacaciones) {
		super(parametros, nomina);
		this.baseCalculo    = nomina.getReceptor().getSalarioDiario();
		this.diasVacaciones = diasVacaciones;
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
			this.importe = this.calcularImporte(cantidad, baseCalculo);
			this.calcularExentoGravado();
			
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
			percepcion = this.build(nomina, CVE_VACACIONES_EN_TIEMPO, cantidad, ValoresBD._CERO.get(), importe);
		}
		
		return percepcion;
	}
	
	@Override
	protected BigDecimal calcularCantidad(DetNomina nomina) throws SGPException {
		return diasVacaciones.setScale(2, RoundingMode.HALF_UP);
	}

	@Override
	public BigDecimal calcularLimiteExento() {
		return _CERO.get();
	}
}
