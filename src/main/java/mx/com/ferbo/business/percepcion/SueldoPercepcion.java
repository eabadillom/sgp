package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPercepcionPK;
import mx.com.ferbo.model.sat.CatTipoPercepcion;

public class SueldoPercepcion extends AbstractPercepcion implements IPercepcion {
	
	private static Logger log = LogManager.getLogger(SueldoPercepcion.class);
	
	private BigDecimal diasTrabajados = null;
	
	public SueldoPercepcion(List<CatTipoPercepcion> tiposPercepcion, BigDecimal diasTrabajados) {
		this.tiposPercepcion = tiposPercepcion;
		this.diasTrabajados = diasTrabajados;
	}

	@Override
	public DetNominaPercepcion calcular(DetNomina nomina) {
		DetNominaPercepcion percepcion = null;
		BigDecimal salarioDiario = null;
		BigDecimal cantidad = null;
		BigDecimal salarioSemanal = null;
		CatTipoPercepcion tpSueldo = null;
		List<DetNominaPercepcion> percepciones = null;
		
		Integer index = null;
		
		try {
			percepciones = nomina.getPercepciones();
			salarioDiario = nomina.getReceptor().getSalarioDiario();
			
			boolean removedPercepciones = percepciones.removeIf(d -> AbstractPercepcion.CVE_SUELDO.equalsIgnoreCase(d.getClave()));
			if(removedPercepciones)
				log.info("Se encontraron conceptos {}, los cuales fueron eliminados para el reproceso de SUELDO.", AbstractPercepcion.CVE_SUELDO);
			
			index = this.nuevoIndiceDe(nomina.getPercepciones());
			
			tpSueldo = this.getTipoPercepcion(P_SUELDO);
			
			salarioSemanal = salarioDiario
					.multiply(diasTrabajados)
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			
			cantidad = diasTrabajados.setScale(2, BigDecimal.ROUND_HALF_UP);
			
		} catch(Exception ex) {
			salarioSemanal = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
			cantidad = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		} finally {
			percepcion = new DetNominaPercepcion();
			percepcion.setKey(new DetNominaPercepcionPK(nomina, index));
			percepcion.setClave(CVE_SUELDO);
			percepcion.setNombre("Sueldo");
			percepcion.setCantidad(cantidad);
			percepcion.setTipoPercepcion(tpSueldo);
			percepcion.setImporteGravado(salarioSemanal);
			percepcion.setImporteExcento(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
			
			if(salarioSemanal != null && salarioSemanal.compareTo(BigDecimal.ZERO) > 0)
				nomina.getPercepciones().add(percepcion);
			
			this.tiposPercepcion = null;
			this.diasTrabajados = null;
		}
		return percepcion;
	}

	public void setDiasTrabajados(BigDecimal diasTrabajados) {
		this.diasTrabajados = diasTrabajados;
	}
}
