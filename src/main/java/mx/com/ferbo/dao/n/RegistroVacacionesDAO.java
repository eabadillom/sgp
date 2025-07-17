	package mx.com.ferbo.dao.n;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetRegistroVacaciones;

public class RegistroVacacionesDAO extends BaseDAO<DetRegistroVacaciones, Integer> {

	public RegistroVacacionesDAO(Class<DetRegistroVacaciones> modelClass) {
		super(modelClass);
	}
	
	public RegistroVacacionesDAO() {
		super(DetRegistroVacaciones.class);
	}
	
	

}
