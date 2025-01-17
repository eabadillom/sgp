package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPercepcionPK;
import mx.com.ferbo.model.sat.CatTipoPercepcion;

public class SeptimoDiaPercepcion extends AbstractPercepcion implements IPercepcion {
	
	private static Logger log = LogManager.getLogger(SeptimoDiaPercepcion.class);
	
	private BigDecimal diasLaborales = null;
	private BigDecimal diasTrabajados = null;
	
	public SeptimoDiaPercepcion(ParametrosNomina parametros, BigDecimal diasLaborales, BigDecimal diasTrabajados) {
		this.tiposPercepcion = parametros.getTiposPercepcion();
		this.diasLaborales = diasLaborales;
		this.diasTrabajados = diasTrabajados;
	}

	@Override
	public DetNominaPercepcion calcular(DetNomina nomina) {
		DetNominaPercepcion percepcion = null;
		BigDecimal          salarioDiario = null;
		BigDecimal          septimoDia = null;
		BigDecimal          proporcionalSemanal = null;
		CatTipoPercepcion   tpSeptimoDia = null;
		List<DetNominaPercepcion> percepciones = null;
		Integer index = null;
		
		try {
			percepciones = nomina.getPercepciones();
			salarioDiario = nomina.getReceptor().getSalarioDiario();
			
			boolean removedPercepciones = percepciones.removeIf(d -> AbstractPercepcion.CVE_SEPTIMO_DIA.equalsIgnoreCase(d.getClave()));
			if(removedPercepciones)
				log.info("Se encontraron conceptos {}, los cuales fueron eliminados para el reproceso de SEPTIMO DIA.", AbstractPercepcion.CVE_SEPTIMO_DIA);
			
			index = this.nuevoIndiceDe(nomina.getPercepciones());
			tpSeptimoDia = this.getTipoPercepcion("001");
			
			proporcionalSemanal = this.diasTrabajados
					.divide(this.diasLaborales, 4, BigDecimal.ROUND_HALF_UP);
			
			septimoDia = salarioDiario
					.multiply(proporcionalSemanal)
					.setScale(2,  BigDecimal.ROUND_HALF_UP)
					;
		} catch(Exception ex) {
			log.error("Problema para obtener el cálculo del septimo día.",  ex);
			septimoDia = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
			proporcionalSemanal = BigDecimal.ZERO.setScale(4, BigDecimal.ROUND_HALF_UP);
		} finally {
			percepcion = new DetNominaPercepcion();
			percepcion.setKey(new DetNominaPercepcionPK(nomina, index));
			percepcion.setClave(CVE_SEPTIMO_DIA);
			percepcion.setNombre("Séptimo día");
			percepcion.setTipoPercepcion(tpSeptimoDia);
			percepcion.setCantidad(proporcionalSemanal);
			percepcion.setImporteGravado(septimoDia);
			percepcion.setImporteExcento(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
			
			if(septimoDia != null && septimoDia.compareTo(BigDecimal.ZERO) > 0)
				nomina.getPercepciones().add(percepcion);
			
			this.tiposPercepcion = null;
			this.diasLaborales = null;
			this.diasTrabajados = null;
		}
		
		return percepcion;
	}

	public void setDiasPeriodo(BigDecimal diasPeriodo) {
		this.diasLaborales = diasPeriodo;
	}

	public void setDiasTrabajados(BigDecimal diasTrabajados) {
		this.diasTrabajados = diasTrabajados;
	}
}
