package mx.com.ferbo.business.otropago;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaOtroPago;
import mx.com.ferbo.model.DetNominaOtroPagoPK;
import mx.com.ferbo.model.sat.CatTipoOtroPago;

public class ReintegroISROtroPago extends AbstractOtroPago implements IOtroPago {
	
	private static Logger log = LogManager.getLogger(AjusteAlNetoOtroPago.class);
	
	private BigDecimal importeISR = null;
	
	public ReintegroISROtroPago(BigDecimal importeISR) {
		this.importeISR = importeISR;
	}
	
	@Override
	public DetNominaOtroPago calcular(DetNomina nomina, Integer index) {
		DetNominaOtroPago opReintegroISR = null;
		CatTipoOtroPago tpReintegroISR = null;
		
		try {
			tpReintegroISR = this.getTipoOtroPago("001");
			index = this.nuevoIndiceDe(nomina.getOtrosPagos());
		} catch(Exception ex) {
			log.error("Problema para generar el reintegro de ISR...", ex);
			this.importeISR = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		} finally {
			opReintegroISR = new DetNominaOtroPago();
			opReintegroISR.setKey(new DetNominaOtroPagoPK(nomina, index));
			opReintegroISR.setTipoOtroPago(tpReintegroISR);
			opReintegroISR.setClave("FRB-001");
			opReintegroISR.setNombre("Reintegro de ISR pagado en exceso");
			opReintegroISR.setImporte(importeISR);
			opReintegroISR.setProcesar(true);
		}
		return opReintegroISR;
	}
}
