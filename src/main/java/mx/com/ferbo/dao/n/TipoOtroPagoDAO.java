package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.sat.CatTipoOtroPago;

public class TipoOtroPagoDAO extends BaseDAO<CatTipoOtroPago, String> {
	
	private static Logger log = LogManager.getLogger(TipoOtroPagoDAO.class);

	public TipoOtroPagoDAO(Class<CatTipoOtroPago> modelClass) {
		super(modelClass);
	}
	
	public TipoOtroPagoDAO() {
		super(CatTipoOtroPago.class);
	}
	
	public List<CatTipoOtroPago> buscarTodos() {
		List<CatTipoOtroPago> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("CatTipoOtroPago.findByAll", modelClass)
					.getResultList()
					;
		} catch(Exception ex) {
			log.error("No es posible obtener el listado de tipos de otros pagos...", ex);
		} finally {
			this.close(em);
		}
		
		return modelList;
	}
}
