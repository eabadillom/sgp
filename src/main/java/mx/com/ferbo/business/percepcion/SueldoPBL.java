package mx.com.ferbo.business.percepcion;

import static mx.com.ferbo.enums.ValoresBD._CERO;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.util.SGPException;

public class SueldoPBL extends AbstractPBL {
	
	private static Logger log = LogManager.getLogger(SueldoPBL.class);
	
	private BigDecimal diasTrabajados = null;
	
	public SueldoPBL(ParametrosNomina parametros, BigDecimal diasTrabajados) {
		super(parametros);
		this.diasTrabajados = diasTrabajados;
	}

	@Override
	public DetNominaPercepcion procesar(DetNomina nomina) {
		DetNominaPercepcion percepcion = null;
		
		try {
			this.baseCalculo = nomina.getReceptor().getSalarioDiario();
			this.cantidad = this.calcularCantidad(nomina);
			this.importe = this.calcularImporte(cantidad, baseCalculo);
			
			this.calcularExentoGravado();
			
		} catch(Exception ex) {
			cantidad       = _CERO.get();
			importeExento  = _CERO.get();
			importeGravado = _CERO.get();
		} finally {
			percepcion = this.build(nomina, CVE_SUELDO, cantidad, importeExento, importeGravado);
		}
		
		return percepcion;
	}
	
	@Override
	protected BigDecimal calcularCantidad(DetNomina nomina) throws SGPException {
		BigDecimal cantidad = null;
		cantidad = this.diasTrabajados.setScale(2, BigDecimal.ROUND_HALF_UP);
		log.info("[UI] dias trabajados = {}", this.diasTrabajados);
		return cantidad;
	}

	@Override
	public BigDecimal calcularLimiteExento() {
		log.info("[UI] Limite exento = {}", _CERO.get());
		return _CERO.get();
	}
}
