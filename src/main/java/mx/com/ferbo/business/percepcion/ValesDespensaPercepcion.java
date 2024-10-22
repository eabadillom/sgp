package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AbstractPercepcion;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPercepcionPK;
import mx.com.ferbo.model.DetPercepcionEmpleado;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import mx.com.ferbo.util.SGPException;

public class ValesDespensaPercepcion extends AbstractPercepcion implements IPercepcion {
	
	private static Logger log = LogManager.getLogger(ValesDespensaPercepcion.class);
	
	private BigDecimal diasTrabajados = null;
	private BigDecimal uma = null;
	private BigDecimal tasaVales = null;
	private BigDecimal diasPeriodo = null;
	
	public ValesDespensaPercepcion(List<CatTipoPercepcion> tiposPercepcion, BigDecimal diasTrabajados, BigDecimal uma, BigDecimal tasaVales, BigDecimal diasPeriodo) {
		this.tiposPercepcion = tiposPercepcion;
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
		DetPercepcionEmpleado percepcionEmpleado = null;
		
		try {
			tpValeDespensa = this.getTipoPercepcion(P_VALES_DESPENSA);
			
    		if(this.diasTrabajados.compareTo(BigDecimal.ZERO) == 0)
    			throw new SGPException("No es posible asignar vales de despensa.");
    		
    		vales = uma.multiply(tasaVales).setScale(4, BigDecimal.ROUND_HALF_UP);
    		vales = vales.multiply(diasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
			percepcionEmpleado = this.buscaPercepcionEmpleado(P_VALES_DESPENSA);
    		
    		if(    (percepcionEmpleado != null) 
				&& (percepcionEmpleado.getImporteMaximo() != null)
				&& (vales.compareTo(percepcionEmpleado.getImporteMaximo()) > 0) ) {
    			
    			vales = percepcionEmpleado.getImporteMaximo();
    		}
    		
		} catch(SGPException ex){
			log.warn("No es posible calcular los vales de despensa: {}", ex.getMessage());
			vales = BigDecimal.ZERO;
    	} catch(Exception ex) {
    		log.error("No es posible calcular los vales de despensa...", ex);
    		vales = BigDecimal.ZERO;
    	} finally {
    		percepcion = new DetNominaPercepcion();
    		percepcion.setKey(new DetNominaPercepcionPK(nomina, index));
			percepcion.setClave(P_VALES_DESPENSA);
			percepcion.setNombre("Despensa");
			percepcion.setTipoPercepcion(tpValeDespensa);
			percepcion.setImporteGravado(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
			percepcion.setImporteExcento(vales);
			percepcion.setClave("FRB-" + tpValeDespensa.getClave());
			
			this.tiposPercepcion = null;
			this.percepcionesEmpleado = null;
			this.diasTrabajados = null;
			this.uma = null;
			this.tasaVales = null;
			this.diasPeriodo = null;
    	}
		
		return percepcion;
	}
}
