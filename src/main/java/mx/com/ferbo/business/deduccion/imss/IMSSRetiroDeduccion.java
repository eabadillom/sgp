package mx.com.ferbo.business.deduccion.imss;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.IDeduccion;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;

/**Cálculo de Retiro, Cesantía en Edad Avanzada y Vejez (Retiro). Fund. Art. 168 Fracción I LSS.
 * 
 */
public class IMSSRetiroDeduccion extends AbstractIMSSDeduccion implements IDeduccion {
	
	private static Logger log = LogManager.getLogger(IMSSRetiroDeduccion.class);

	@Override
	public DetNominaDeduccion calcular(DetNomina nomina, Integer index) {
		// TODO Cálculo: SBC x Cuota (2% Patrón, 0% Trabajador) x (Dias trabajados - Ausencias)
		throw new UnsupportedOperationException("El cálculo para las cuotas de Retiro aún no está disponible.");
	}

}
