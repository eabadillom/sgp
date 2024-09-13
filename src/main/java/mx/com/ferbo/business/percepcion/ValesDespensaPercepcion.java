package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.TipoPercepcionDAO;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPercepcionPK;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import mx.com.ferbo.util.SGPException;

public class ValesDespensaPercepcion implements IPercepcion {
	
	private static Logger log = LogManager.getLogger(ValesDespensaPercepcion.class);
	
	private BigDecimal diasTrabajados = null;
	private BigDecimal uma = null;
	private BigDecimal tasaVales = null;
	private BigDecimal diasPeriodo = null;
	
	private TipoPercepcionDAO tipoPercepcionDAO = null;
	
	public ValesDespensaPercepcion(BigDecimal diasTrabajados, BigDecimal uma, BigDecimal tasaVales, BigDecimal diasPeriodo) {
		this.diasTrabajados = diasTrabajados;
		this.uma = uma;
		this.tasaVales = tasaVales;
		this.diasPeriodo = diasPeriodo;
	}
	
	@Override
	public DetNominaPercepcion calcular(DetNomina nomina, Integer index) {
		DetNominaPercepcion percepcion = null;
		BigDecimal vales = null;
		CatTipoPercepcion tpValeDespensa = null;
		
		try {
			if(this.tipoPercepcionDAO == null)
    			this.tipoPercepcionDAO = new TipoPercepcionDAO();
			tpValeDespensa = tipoPercepcionDAO.buscarPorId("029");
			
    		if(this.diasTrabajados.compareTo(BigDecimal.ZERO) == 0)
    			throw new SGPException("No es posible asignar vales de despensa.");
    		
    		vales = uma.multiply(tasaVales).setScale(4, BigDecimal.ROUND_HALF_UP);
    		vales = vales.multiply(diasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
    	} catch(Exception ex) {
    		vales = BigDecimal.ZERO;
    	} finally {
    		percepcion = new DetNominaPercepcion();
    		percepcion.setKey(new DetNominaPercepcionPK(nomina, index));
			percepcion.setClave("032");
			percepcion.setNombre("Despensa");
			percepcion.setTipoPercepcion(tpValeDespensa);
			percepcion.setImporteGravado(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
			percepcion.setImporteExcento(vales);
			percepcion.setClave("FRB-" + tpValeDespensa.getClave());
    	}
		
		return percepcion;
	}

	public TipoPercepcionDAO getTipoPercepcionDAO() {
		return tipoPercepcionDAO;
	}

	public void setTipoPercepcionDAO(TipoPercepcionDAO tipoPercepcionDAO) {
		this.tipoPercepcionDAO = tipoPercepcionDAO;
	}

}
