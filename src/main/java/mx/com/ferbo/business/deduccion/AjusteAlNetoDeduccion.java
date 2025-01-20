package mx.com.ferbo.business.deduccion;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.sat.CatTipoDeduccion;

public class AjusteAlNetoDeduccion extends AbstractDeduccion implements IDeduccion {
	
	private static Logger log = LogManager.getLogger(AjusteAlNetoDeduccion.class);
	
	private BigDecimal ajusteAlNeto = null;
	
	public AjusteAlNetoDeduccion(BigDecimal ajusteAlNeto) {
		this.ajusteAlNeto = ajusteAlNeto;
	}

	@Override
	public DetNominaDeduccion calcular(DetNomina nomina, Integer index) {
		DetNominaDeduccion deduccion = null;
		CatTipoDeduccion tdAjusteAlNeto = null;
		
		try {
			tdAjusteAlNeto = this.getTipoDeduccion(D_AJUSTE_AL_NETO);
		} catch(Exception ex) {
			log.error("Problema para generar el ajuste al neto...", ex);
			this.ajusteAlNeto = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		} finally {
			deduccion = new DetNominaDeduccion();
			deduccion.setKey(new DetNominaDeduccionPK(nomina, index));
			deduccion.setTipoDeduccion(tdAjusteAlNeto);
			deduccion.setClave(CVE_AJUSTE_AL_NETO);
			deduccion.setNombre("Ajuste al neto");
			deduccion.setImporte(ajusteAlNeto);
			deduccion.setInformar(true);
			deduccion.setProcesar(true);
		}
		
		return deduccion;
	}
}
