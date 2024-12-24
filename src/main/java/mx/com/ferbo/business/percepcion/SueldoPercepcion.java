package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.util.List;

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
	public DetNominaPercepcion calcular(DetNomina nomina) {
		DetNominaPercepcion percepcion = null;
		BigDecimal cantidad = null;
		BigDecimal salarioSemanal = null;
		CatTipoPercepcion tpSueldo = null;
		
		Integer idxP = null;
		
		try {
			idxP = this.nuevoIndiceDe(nomina.getPercepciones());
			
			tpSueldo = this.getTipoPercepcion(P_SUELDO);
			
			salarioSemanal = this.salarioDiario
					.multiply(diasTrabajados)
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			
			cantidad = diasTrabajados.setScale(2, BigDecimal.ROUND_HALF_UP);
			
		} catch(Exception ex) {
			salarioSemanal = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
			cantidad = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		} finally {
			percepcion = new DetNominaPercepcion();
			percepcion.setKey(new DetNominaPercepcionPK(nomina, idxP));
			percepcion.setClave(CVE_SUELDO);
			percepcion.setNombre("Sueldo");
			percepcion.setCantidad(cantidad);
			percepcion.setTipoPercepcion(tpSueldo);
			percepcion.setImporteGravado(salarioSemanal);
			percepcion.setImporteExcento(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
			
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
