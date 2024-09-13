package mx.com.ferbo.dao.n;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatTipoDeduccion;

public class TipoDeduccionDAO extends BaseDAO<CatTipoDeduccion, String> {

	public TipoDeduccionDAO(Class<CatTipoDeduccion> modelClass) {
		super(modelClass);
	}
	
	public TipoDeduccionDAO() {
		super(CatTipoDeduccion.class);
	}

}
