package mx.com.ferbo.business.nomina;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;

public class PercepcionBL {
	private static Logger log = LogManager.getLogger(PercepcionBL.class);
	
	public static DetNominaPercepcion build () {
		DetNominaPercepcion percepcion = null;
		log.info("Preparando nueva percepción....");
		percepcion = build(null);
		return percepcion;
	}
	
	public static DetNominaPercepcion build(DetNomina nomina) {
		DetNominaPercepcion percepcion = null;
		log.info("Preparando nueva percepción....");
		percepcion = new DetNominaPercepcion();
		percepcion.setId(null);
		percepcion.setNomina(nomina);
		percepcion.setCantidad(null);
		percepcion.setImporteExento(ValoresBD._CERO.get());
		percepcion.setImporteGravado(ValoresBD._CERO.get());
		return percepcion;
	}

}
