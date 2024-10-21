package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.util.List;

import mx.com.ferbo.business.deduccion.AbstractPercepcion;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPercepcionPK;
import mx.com.ferbo.model.sat.CatTipoPercepcion;

public class SueldoPercepcion extends AbstractPercepcion implements IPercepcion {
	
	private BigDecimal salarioDiario = null;
	private BigDecimal diasTrabajados = null;
	
	public SueldoPercepcion(List<CatTipoPercepcion> tiposPercepcion, BigDecimal salarioDiario, BigDecimal diasTrabajados) {
		this.tiposPercepcion = tiposPercepcion;
		this.salarioDiario = salarioDiario;
		this.diasTrabajados = diasTrabajados;
	}

	@Override
	public DetNominaPercepcion calcular(DetNomina nomina, Integer index) {
		DetNominaPercepcion percepcion = null;
		BigDecimal salarioSemanal = null;
		CatTipoPercepcion tpSueldo = null;
		try {
			tpSueldo = this.getTipoPercepcion("001");
			
			salarioSemanal = this.salarioDiario
					.multiply(diasTrabajados)
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			
		} catch(Exception ex) {
			salarioSemanal = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		} finally {
			percepcion = new DetNominaPercepcion();
			percepcion.setKey(new DetNominaPercepcionPK(nomina, index));
			percepcion.setClave("001");
			percepcion.setNombre("Sueldo");
			percepcion.setTipoPercepcion(tpSueldo);
			percepcion.setImporteGravado(salarioSemanal);
			percepcion.setImporteExcento(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
			percepcion.setClave("FRB-" + tpSueldo.getClave());
			
			this.tiposPercepcion = null;
			this.tiposPercepcion = null;
			this.salarioDiario = null;
			this.diasTrabajados = null;
		}
		return percepcion;
	}

	public void setSalarioDiario(BigDecimal salarioDiario) {
		this.salarioDiario = salarioDiario;
	}
	
	public void setDiasTrabajados(BigDecimal diasTrabajados) {
		this.diasTrabajados = diasTrabajados;
	}
}
