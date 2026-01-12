package mx.com.ferbo.business.catalogos;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.Subsidio2DAO;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.CatSubsidio2;
import mx.com.ferbo.util.SGPException;

public class Subsidio2BL {
	private static Logger log = LogManager.getLogger(Subsidio2BL.class);
	
	public static void guardar(CatSubsidio2 subsidio)
	throws SGPException {
		Subsidio2DAO subsidioDAO = null;
		
		log.info("Validando subsidio: {}", subsidio);
		
		if(subsidio == null)
			throw new SGPException("Hay un problema con la información del subsidio. Por favor contacte con su administrador de sistemas.");
		
		if(subsidio.getTasa() == null || subsidio.getTasa().compareTo(ValoresBD._CERO.get()) <= 0)
			throw new SGPException("El valor de la tasa de subsidio es incorrecto.");
		
		if(subsidio.getImporteMaximo() == null || subsidio.getImporteMaximo().compareTo(ValoresBD._CERO.get()) <= 0)
			throw new SGPException("El valor del importe máximo es incorrecto.");
		
		if(subsidio.getVigenciaInicio() == null)
			throw new SGPException("Debe indicar el inicio de vigencia del subsidio.");
		
		if(subsidio.getVigenciaFin() == null)
			throw new SGPException("Debe indicar el fin de vigencia del subsidio.");
		
		log.info("Guardando subsidio...");
		
		subsidioDAO = new Subsidio2DAO();
		
		if(subsidio.getId() == null)
			subsidioDAO.guardar(subsidio);
		else
			subsidioDAO.actualizar(subsidio);
		
	}
	
}
