package mx.com.ferbo.business.deduccion;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.TipoDeduccionDAO;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.util.SGPException;

public class BaseISRDeduccion implements IDeduccion {
	
	private static Logger log = LogManager.getLogger(BaseISRDeduccion.class);
	
	private TipoDeduccionDAO tipoDeduccionDAO = null;
	private List<DetNominaPercepcion> percepciones = null;
	
	public BaseISRDeduccion(List<DetNominaPercepcion> percepciones) {
		this.percepciones = percepciones;
	}

	@Override
	public DetNominaDeduccion calcular(DetNomina nomina, Integer index) {
		DetNominaDeduccion deduccion = null;
		CatTipoDeduccion tdISR = null;
		BigDecimal isr = null;
		
		try {
			if(this.percepciones == null)
				throw new SGPException("No hay percepciones gravadas.");
			
			if(this.percepciones.size() == 0)
				throw new SGPException("No hayy percepciones gravadas.");
			
			if(tipoDeduccionDAO == null)
				tipoDeduccionDAO = new TipoDeduccionDAO();
			
			tdISR = this.tipoDeduccionDAO.buscarPorId("002");
			
			isr = this.percepciones.stream()
					.map(item -> item.getImporteGravado())
					.reduce(BigDecimal.ZERO, BigDecimal :: add);
			
			if(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(isr) > 0 )
				throw new SGPException("No hay percepciones gravadas.");
			
		} catch(Exception ex) {
			log.error("Problema para obtener la base para el ISR...", ex);
			isr = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		} finally {
			deduccion = new DetNominaDeduccion();
			deduccion.setKey(new DetNominaDeduccionPK(nomina, index));
			deduccion.setTipoDeduccion(tdISR);
			deduccion.setClave("---");
			deduccion.setNombre("Base I.S.R.");
			deduccion.setImporte(isr);
			deduccion.setProcesar(false);
		}
		
		return deduccion;
	}

	public TipoDeduccionDAO getTipoDeduccionDAO() {
		return tipoDeduccionDAO;
	}

	public void setTipoDeduccionDAO(TipoDeduccionDAO tipoDeduccionDAO) {
		this.tipoDeduccionDAO = tipoDeduccionDAO;
	}

}
