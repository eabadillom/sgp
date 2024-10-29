package mx.com.ferbo.business.otropago;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AbstractOtroPago;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaOtroPago;
import mx.com.ferbo.model.DetNominaOtroPagoPK;
import mx.com.ferbo.model.sat.CatTipoOtroPago;

public class AjusteAlNetoOtroPago extends AbstractOtroPago implements IOtroPago {
	
	private static Logger log = LogManager.getLogger(AjusteAlNetoOtroPago.class);
	
	private BigDecimal ajusteAlNeto = null;
	
	public AjusteAlNetoOtroPago(BigDecimal ajusteAlNeto) {
		this.ajusteAlNeto = ajusteAlNeto;
	}
	
	@Override
	public DetNominaOtroPago calcular(DetNomina nomina, Integer index) {
		DetNominaOtroPago opAjusteAlNeto = null;
		CatTipoOtroPago tpAjusteAlNeto = null;
		try {
			tpAjusteAlNeto = this.getTipoOtroPago("999");
		} catch(Exception ex) {
			log.error("Problema para generar el ajuste al neto...", ex);
			ajusteAlNeto = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		} finally {
			opAjusteAlNeto = new DetNominaOtroPago();
			opAjusteAlNeto.setKey(new DetNominaOtroPagoPK(nomina, index));
			opAjusteAlNeto.setTipoOtroPago(tpAjusteAlNeto);
			opAjusteAlNeto.setClave("FRB-999");
			opAjusteAlNeto.setNombre("Ajuste al neto");
			opAjusteAlNeto.setImporte(ajusteAlNeto);
			opAjusteAlNeto.setProcesar(true);
		}
		return opAjusteAlNeto;
	}
}
