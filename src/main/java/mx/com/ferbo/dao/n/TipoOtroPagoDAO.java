package mx.com.ferbo.dao.n;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatTipoOtroPago;

public class TipoOtroPagoDAO extends BaseDAO<CatTipoOtroPago, String> {

	public TipoOtroPagoDAO(Class<CatTipoOtroPago> modelClass) {
		super(modelClass);
	}
	
	public TipoOtroPagoDAO() {
		super(CatTipoOtroPago.class);
	}
}
