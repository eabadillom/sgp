package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AbstractPercepcion;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPercepcionPK;
import mx.com.ferbo.model.sat.CatTipoPercepcion;

public class SeptimoDiaPercepcion extends AbstractPercepcion implements IPercepcion {
	
	private static Logger log = LogManager.getLogger(SeptimoDiaPercepcion.class);
	
	private BigDecimal salarioDiario = null;
	private BigDecimal diasPeriodo = null;
	private BigDecimal diasTrabajados = null;
	
	public SeptimoDiaPercepcion(List<CatTipoPercepcion> tiposPercepcion, BigDecimal salarioDiario, BigDecimal diasPeriodo, BigDecimal diasTrabajados) {
		this.tiposPercepcion = tiposPercepcion;
		this.salarioDiario = salarioDiario;
		this.diasPeriodo = diasPeriodo;
		this.diasTrabajados = diasTrabajados;
	}

	@Override
	public DetNominaPercepcion calcular(DetNomina nomina, Integer index) {
		DetNominaPercepcion percepcion = null;
		BigDecimal septimoDia = null;
		CatTipoPercepcion tpSeptimoDia = null;
		
		try {
			tpSeptimoDia = this.getTipoPercepcion("001");
			
			septimoDia = this.salarioDiario
					.divide(this.diasPeriodo, 4, BigDecimal.ROUND_HALF_UP)
					.multiply(this.diasTrabajados).setScale(2, BigDecimal.ROUND_HALF_UP)
					;
		} catch(Exception ex) {
			log.error("Problema para obtener el cálculo del septimo día.",  ex);
			septimoDia = BigDecimal.ZERO;
		} finally {
			percepcion = new DetNominaPercepcion();
			percepcion.setKey(new DetNominaPercepcionPK(nomina, index));
			percepcion.setClave("003");
			percepcion.setNombre("Séptimo día");
			percepcion.setTipoPercepcion(tpSeptimoDia);
			percepcion.setImporteGravado(septimoDia);
			percepcion.setImporteExcento(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
			percepcion.setClave("FRB-" + tpSeptimoDia.getClave());
			
			this.tiposPercepcion = null;
			this.salarioDiario = null;
			this.diasPeriodo = null;
			this.diasTrabajados = null;
		}
		
		return percepcion;
	}

	public void setSalarioDiario(BigDecimal salarioDiario) {
		this.salarioDiario = salarioDiario;
	}

	public void setDiasPeriodo(BigDecimal diasPeriodo) {
		this.diasPeriodo = diasPeriodo;
	}

	public void setDiasTrabajados(BigDecimal diasTrabajados) {
		this.diasTrabajados = diasTrabajados;
	}
}
