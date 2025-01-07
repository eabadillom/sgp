package mx.com.ferbo.business.deduccion.imss;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.IDeduccion;
import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.model.CatCuotaIMSS;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.util.SGPException;

/**Cálculo de cuota IMSS Riesgo de trabajo.
 */
public class IMSSRiesgoTrabajoDeduccion extends AbstractIMSSDeduccion implements IDeduccion {
	
	private static Logger log = LogManager.getLogger(IMSSRiesgoTrabajoDeduccion.class);
	
	private BigDecimal diasTrabajados = null;
	private BigDecimal sdi = null;
	
	public IMSSRiesgoTrabajoDeduccion(ParametrosNomina parametros, BigDecimal diasTrabajados, BigDecimal sdi) {
		this.diasTrabajados = diasTrabajados;
		this.sdi = sdi;
		this.cuotasIMSS = parametros.getCuotasIMSS();
		this.tiposDeduccion = parametros.getTiposDeduccion();
	}

	@Override
	public DetNominaDeduccion calcular(DetNomina nomina, Integer index) {
		DetNominaDeduccion deduccion = null;
		CatCuotaIMSS tarifaIMSS = null;
		CatTipoDeduccion tdIMSS = null;
		BigDecimal cuota = null;
		
		try {
			if(this.cuotasIMSS == null)
				throw new SGPException("No se establecio la lista de cuotas del IMSS.");
			
			if(this.cuotasIMSS.size() <= 0)
				throw new SGPException("No se establecio la lista de cuotas del IMSS.");
			
			if(this.tiposDeduccion == null)
				throw new SGPException("No se establecio la lista de tipos de deduccion.");
			
			if(this.tiposDeduccion.size() <= 0)
				throw new SGPException("No se establecio la lista de tipos de deduccion.");
			
			tdIMSS = this.getTipoDeduccion("001");
			
			tarifaIMSS = this.getCuotaIMSS("O", "RT", 0);
			cuota = this.sdi
					.multiply(tarifaIMSS.getCuota())
					.multiply(diasTrabajados)
					.setScale(2, BigDecimal.ROUND_HALF_UP)
					;
			
		} catch(Exception ex) {
			log.warn("No fue posible calcular el excedente por Enfermedad y Maternidad.", ex);
			cuota = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		} finally {
			deduccion = new DetNominaDeduccion();
			deduccion.setKey(new DetNominaDeduccionPK(nomina, index));
			deduccion.setTipoDeduccion(tdIMSS);
			deduccion.setClave("001");
			deduccion.setNombre("I.M.S.S. (Riesgo de trabajo)");
			deduccion.setImporte(cuota);
			deduccion.setInformar(false);
			deduccion.setProcesar(false);
		}
		
		return deduccion;
	}

}
