package mx.com.ferbo.dao.n;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatTipoPercepcion;

public class TipoPercepcionDAO extends BaseDAO<CatTipoPercepcion, String> {
	
	private Logger log = LogManager.getLogger(TipoPercepcionDAO.class);

	public TipoPercepcionDAO(Class<CatTipoPercepcion> modelClass) {
		super(modelClass);
	}
	
	

}
