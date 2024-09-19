package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;

import mx.com.ferbo.dao.n.TipoPercepcionDAO;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPercepcionPK;
import mx.com.ferbo.model.sat.CatTipoPercepcion;

public class SueldoPercepcion implements IPercepcion {
	
	private BigDecimal salarioDiario = null;
	private BigDecimal diasTrabajados = null;
	private TipoPercepcionDAO tipoPercepcionDAO = null;
	
	public SueldoPercepcion(BigDecimal salarioDiario, BigDecimal diasTrabajados) {
		this.salarioDiario = salarioDiario;
		this.diasTrabajados = diasTrabajados;
	}

	@Override
	public DetNominaPercepcion calcular(DetNomina nomina, Integer index) {
		DetNominaPercepcion percepcion = null;
		BigDecimal salarioSemanal = null;
		CatTipoPercepcion tpSueldo = null;
		try {
			if(tipoPercepcionDAO == null)
				tipoPercepcionDAO = new TipoPercepcionDAO();
			
			tpSueldo = tipoPercepcionDAO.buscarPorId("001");
			
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
		}
		return percepcion;
	}

	public BigDecimal getSalarioDiario() {
		return salarioDiario;
	}

	public void setSalarioDiario(BigDecimal salarioDiario) {
		this.salarioDiario = salarioDiario;
	}
	
	public BigDecimal getDiasTrabajados() {
		return diasTrabajados;
	}

	public void setDiasTrabajados(BigDecimal diasTrabajados) {
		this.diasTrabajados = diasTrabajados;
	}

	public TipoPercepcionDAO getTipoPercepcionDAO() {
		return tipoPercepcionDAO;
	}

	public void setTipoPercepcionDAO(TipoPercepcionDAO tipoPercepcionDAO) {
		this.tipoPercepcionDAO = tipoPercepcionDAO;
	}

}
