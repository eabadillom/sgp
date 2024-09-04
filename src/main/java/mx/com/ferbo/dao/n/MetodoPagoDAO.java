package mx.com.ferbo.dao.n;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatMetodoPago;

public class MetodoPagoDAO extends BaseDAO<CatMetodoPago, String> {

	public MetodoPagoDAO(Class<CatMetodoPago> modelClass) {
		super(modelClass);
	}
}
