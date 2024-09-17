package mx.com.ferbo.dao.n;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatConcepto;

public class ConceptoDAO extends BaseDAO<CatConcepto, String> {

	public ConceptoDAO(Class<CatConcepto> modelClass) {
		super(modelClass);
	}
}
