package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.math.RoundingMode;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.util.SGPException;

public class PtuPBL extends AbstractPBL {

	public PtuPBL(ParametrosNomina parametros, DetNomina nomina) {
		super(parametros, nomina);
	}

	@Override
	protected BigDecimal calcularCantidad(DetNomina nomina) throws SGPException {
		throw new UnsupportedOperationException("El cálculo automático de la PTU no está implementado.");
	}

	@Override
	protected BigDecimal calcularLimiteExento() {
		BigDecimal limiteExento = null;
		BigDecimal uma = this.parametros.getUma().getImporteDiario();
		
		limiteExento = ValoresBD._15.get().multiply(uma).setScale(2, RoundingMode.HALF_UP);
		
		return limiteExento;
	}

	@Override
	public DetNominaPercepcion procesar(DetNomina nomina) throws SGPException {
		DetNominaPercepcion percepcion = null;
		
		this.cantidad = ValoresBD._CERO.get();
		percepcion = this.procesar(nomina, this.cantidad);
		
		return percepcion;
	}

	@Override
	public DetNominaPercepcion procesar(DetNomina nomina, BigDecimal cantidad) throws SGPException {
		DetNominaPercepcion percepcion = null;
		
		try {
			
		} catch(Exception ex) {
			this.cantidad = ValoresBD._CERO.get();
			this.importe = ValoresBD._CERO.get();
			this.importeExento = ValoresBD._CERO.get();
			this.importeGravado = ValoresBD._CERO.get();
		} finally {
			percepcion = build(nomina, CVE_PTU, cantidad, importeExento, importeGravado);
		}
		
		return percepcion;
	}

}
