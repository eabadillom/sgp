package mx.com.ferbo.business.nomina;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPercepcionPK;

public class PercepcionBL {
	private static Logger log = LogManager.getLogger(PercepcionBL.class);
	
	public static DetNominaPercepcion build () {
		DetNominaPercepcion percepcion = null;
		log.info("Preparando nueva percepción....");
		percepcion = new DetNominaPercepcion();
		percepcion.setKey(new DetNominaPercepcionPK(null, -1));
		return percepcion;
	}

}
