package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;

public class VacacionesPercepcion extends AbstractPercepcion implements IPercepcion {
	
	private static Logger log = LogManager.getLogger(VacacionesPercepcion.class);
	
	private BigDecimal diasVacaciones = null;
	
	public VacacionesPercepcion(ParametrosNomina parametros, BigDecimal diasVacaciones) {
		this.tiposPercepcion = parametros.getTiposPercepcion();
		this.diasVacaciones = diasVacaciones;
	}

	@Override
	public DetNominaPercepcion calcular(DetNomina nomina) {
		DetNominaPercepcion percepcion = null;
		BigDecimal salarioDiario = null;
		BigDecimal cantidad = null;
		BigDecimal salarioVacaciones = null;
		List<DetNominaPercepcion> percepciones = null;
		
		try {
			percepciones = nomina.getPercepciones();
			salarioDiario = nomina.getReceptor().getSalarioDiario();
			
			boolean removedPercepciones = percepciones.removeIf(d -> CVE_VACACIONES_EN_TIEMPO.equalsIgnoreCase(d.getClave()));
			if(removedPercepciones)
				log.info("Se encontraron conceptos {}, los cuales fueron eliminados para el reproceso de SUELDO.", AbstractPercepcion.CVE_SUELDO);
			
			salarioVacaciones = salarioDiario
					.multiply(diasVacaciones)
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			
			cantidad = diasVacaciones.setScale(2, BigDecimal.ROUND_HALF_UP);
			
		} catch(Exception ex) {
			salarioVacaciones = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
			cantidad = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		} finally {
			percepcion = this.build(nomina, CVE_VACACIONES_EN_TIEMPO, cantidad, ValoresBD.CERO.getValor(), salarioVacaciones);
		}
		
		return percepcion;

	}

}
