package mx.com.ferbo.business.otropago;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.TipoOtroPagoDAO;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaOtroPago;
import mx.com.ferbo.model.DetNominaOtroPagoPK;
import mx.com.ferbo.model.sat.CatTipoOtroPago;

public class AjusteNetoOtroPago implements IOtroPago {
	
	private static Logger log = LogManager.getLogger(AjusteNetoOtroPago.class);
	
	private TipoOtroPagoDAO tipoOtroPagoDAO = null;
	
	public AjusteNetoOtroPago() {
		log.debug("Nada que hacer por el momento.");
	}
	
	@Override
	public DetNominaOtroPago calcular(DetNomina nomina, Integer index) {
		DetNominaOtroPago opAjusteAlNeto = null;
		CatTipoOtroPago tpAjusteAlNeto = null;
		try {
			if(this.tipoOtroPagoDAO == null)
				this.tipoOtroPagoDAO = new TipoOtroPagoDAO();
			
			tpAjusteAlNeto = this.tipoOtroPagoDAO.buscarPorId("999");
		} catch(Exception ex) {
			log.error("Problema para generar el ajuste al neto...", ex);
		} finally {
			opAjusteAlNeto = new DetNominaOtroPago();
			opAjusteAlNeto.setKey(new DetNominaOtroPagoPK(nomina, index));
			opAjusteAlNeto.setTipoOtroPago(tpAjusteAlNeto);
			opAjusteAlNeto.setClave("FRB-099");
			opAjusteAlNeto.setNombre("Ajuste al neto");
			opAjusteAlNeto.setImporte(new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		return opAjusteAlNeto;
	}

	public TipoOtroPagoDAO getTipoOtroPagoDAO() {
		return tipoOtroPagoDAO;
	}

	public void setTipoOtroPagoDAO(TipoOtroPagoDAO tipoOtroPagoDAO) {
		this.tipoOtroPagoDAO = tipoOtroPagoDAO;
	}
}
