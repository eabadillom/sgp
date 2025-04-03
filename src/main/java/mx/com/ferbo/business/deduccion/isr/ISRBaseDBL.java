package mx.com.ferbo.business.deduccion.isr;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AbstractDBL;
import mx.com.ferbo.business.deduccion.IDeduccion;
import mx.com.ferbo.dao.n.TipoDeduccionDAO;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.util.SGPException;

public class ISRBaseDBL extends AbstractDBL implements IDeduccion {
	
	private static Logger log = LogManager.getLogger(ISRBaseDBL.class);
	
	private TipoDeduccionDAO tipoDeduccionDAO = null;
	private List<DetNominaPercepcion> percepciones = null;
	
	public ISRBaseDBL(List<DetNominaPercepcion> percepciones) {
		this.percepciones = percepciones;
	}

	@Override
	public DetNominaDeduccion calcular(DetNomina nomina) {
		DetNominaDeduccion deduccion = null;
		CatTipoDeduccion   tdISR     = null;
		BigDecimal         baseISR   = null;
		Integer            index     = null;
		
		try {
			if(this.percepciones == null)
				throw new SGPException("No hay percepciones gravadas.");
			
			if(this.percepciones.size() == 0)
				throw new SGPException("No hay percepciones gravadas.");
			
			tdISR = this.getTipoDeduccion(TD_ISR);
			
			baseISR = this.percepciones.stream()
					.map(item -> item.getImporteGravado())
					.reduce(BigDecimal.ZERO, BigDecimal :: add);
			
			if(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(baseISR) > 0 )
				throw new SGPException("No hay percepciones gravadas.");
			
		} catch(Exception ex) {
			log.error("Problema para obtener la base para el ISR...", ex);
			baseISR = ValoresBD._CERO.get();
		} finally {
			log.info("Percepciones gravadas: {}", baseISR);
			deduccion = new DetNominaDeduccion.Builder()
					.key(new DetNominaDeduccionPK(nomina, index))
					.tipoDeduccion(tdISR)
					.clave(CVE_IMSS)
					.nombre("Base I.S.R.")
					.importe(baseISR)
					.informar(false)
					.procesar(false)
					.build();
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
