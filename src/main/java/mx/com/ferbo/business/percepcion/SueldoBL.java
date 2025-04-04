package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;

public class SueldoBL extends AbstractPBL implements IPercepcion {
	
	private static Logger log = LogManager.getLogger(SueldoBL.class);
	
	private BigDecimal diasTrabajados = null;
	
	public SueldoBL(ParametrosNomina parametros, BigDecimal diasTrabajados) {
		this.tiposPercepcion = parametros.getTiposPercepcion();
		this.diasTrabajados = diasTrabajados;
	}

	@Override
	public DetNominaPercepcion calcular(DetNomina nomina) {
		DetNominaPercepcion percepcion = null;
		BigDecimal salarioDiario = null;
		BigDecimal cantidad = null;
		BigDecimal salarioSemanal = null;
		List<DetNominaPercepcion> percepciones = null;
		
		try {
			percepciones = nomina.getPercepciones();
			salarioDiario = nomina.getReceptor().getSalarioDiario();
			
			boolean removedPercepciones = percepciones.removeIf(d -> CVE_SUELDO.equalsIgnoreCase(d.getClave()));
			if(removedPercepciones)
				log.info("Se encontraron conceptos {}, los cuales fueron eliminados para el reproceso de SUELDO.", AbstractPBL.CVE_SUELDO);
			
			salarioSemanal = salarioDiario
					.multiply(diasTrabajados)
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			
			cantidad = diasTrabajados.setScale(2, BigDecimal.ROUND_HALF_UP);
			
		} catch(Exception ex) {
			salarioSemanal = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
			cantidad = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		} finally {
			percepcion = this.build(nomina, CVE_SUELDO, cantidad, ValoresBD._CERO.get(), salarioSemanal);
		}
		
		return percepcion;
	}
}
