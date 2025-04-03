package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetPercepcionEmpleado;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import mx.com.ferbo.util.SGPException;

public class ValesDespensaPercepcion extends AbstractPercepcion implements IPercepcion {
	
	private static Logger log = LogManager.getLogger(ValesDespensaPercepcion.class);
	
	private BigDecimal siete = new BigDecimal("7.00").setScale(2, BigDecimal.ROUND_HALF_UP);
	
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
	public DetNominaPercepcion calcular(DetNomina nomina) {
		DetNominaPercepcion percepcion = null;
		BigDecimal importeVales = null;
		
		DetPercepcionEmpleado percepcionEmpleado = null;
		
		BigDecimal limiteExcento = null;
		BigDecimal importeGravado = null;
		BigDecimal importeExento = null;
		
		try {
    		if(this.diasTrabajados.compareTo(BigDecimal.ZERO) == 0)
    			throw new SGPException("No es posible asignar vales de despensa.");
    		
    		importeVales = uma.multiply(tasaVales).setScale(4, BigDecimal.ROUND_HALF_UP);
    		importeVales = importeVales.multiply(diasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
			percepcionEmpleado = this.buscaPercepcionEmpleado(P_VALES_DESPENSA);
    		
    		if(    (percepcionEmpleado != null) 
    			&& (percepcionEmpleado.getActivo())
				&& (percepcionEmpleado.getImporteMaximo() != null)
				&& (importeVales.compareTo(percepcionEmpleado.getImporteMaximo()) > 0) ) {
    			
    			importeVales = percepcionEmpleado.getImporteMaximo();
    		}
    		
    		/* Los vales de despensa están exentos de ISR hasta por 7 veces la UMA diaria.
    		 */
    		limiteExcento = this.uma.multiply(siete).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
    		if(importeVales.compareTo(limiteExcento) > 0) {
    			importeGravado = importeVales.subtract(limiteExcento);
    			importeExento = limiteExcento.setScale(2, BigDecimal.ROUND_HALF_UP);
    		} else {
    			importeGravado = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
    			importeExento = importeVales.setScale(2, BigDecimal.ROUND_HALF_UP);
    		}
    		
		} catch(SGPException ex){
			log.warn("No es posible calcular los vales de despensa: {}", ex.getMessage());
			importeExento = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
			importeGravado = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
    	} catch(Exception ex) {
    		log.error("No es posible calcular los vales de despensa...", ex);
    		importeExento = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
			importeGravado = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
    	} finally {
    		percepcion = this.build(nomina, CVE_VALES_DESPENSA, ValoresBD._CERO.get(), importeExento, importeGravado);
    	}
		
		return percepcion;
	}
}
