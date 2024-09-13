package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.TipoPercepcionDAO;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPercepcionPK;
import mx.com.ferbo.model.sat.CatTipoPercepcion;

public class SeptimoDiaPercepcion implements IPercepcion {
	
	private static Logger log = LogManager.getLogger(SeptimoDiaPercepcion.class);
	
	private BigDecimal salarioDiario = null;
	private BigDecimal diasPeriodo = null;
	private BigDecimal diasTrabajados = null;
	
	private TipoPercepcionDAO tipoPercepcionDAO = null;
	
	public SeptimoDiaPercepcion(BigDecimal salarioDiario, BigDecimal diasPeriodo, BigDecimal diasTrabajados) {
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
			if(tipoPercepcionDAO == null)
				tipoPercepcionDAO = new TipoPercepcionDAO();
			tpSeptimoDia = tipoPercepcionDAO.buscarPorId("001");
			
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
		}
		
		return percepcion;
	}

	public BigDecimal getSalarioDiario() {
		return salarioDiario;
	}

	public void setSalarioDiario(BigDecimal salarioDiario) {
		this.salarioDiario = salarioDiario;
	}

	public BigDecimal getDiasPeriodo() {
		return diasPeriodo;
	}

	public void setDiasPeriodo(BigDecimal diasPeriodo) {
		this.diasPeriodo = diasPeriodo;
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
