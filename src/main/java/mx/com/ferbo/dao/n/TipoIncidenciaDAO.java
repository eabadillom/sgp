package mx.com.ferbo.dao.n;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatTipoIncidencia;

public class TipoIncidenciaDAO extends BaseDAO<CatTipoIncidencia, Integer> {
	
	private static Logger log = LogManager.getLogger(TipoIncidenciaDAO.class);

	public TipoIncidenciaDAO(Class<CatTipoIncidencia> modelClass) {
		super(modelClass);
	}
	
	public TipoIncidenciaDAO() {
		super(CatTipoIncidencia.class);
	}
	
	public Optional<CatTipoIncidencia> buscarPorClave(String clave) {
		Optional<CatTipoIncidencia> result;
		CatTipoIncidencia model = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			model = em.createNamedQuery("CatTipoIncidencia.findByClave", this.modelClass)
					.setParameter("clave", clave)
					.getSingleResult()
					;
			
			result = Optional.of(model);
			
		} catch(Exception ex) {
			log.error("Problema para obtener el tipo de incidencia por clave: {}... {}", clave, ex);
			result = Optional.empty();
		} finally {
			this.close(em);
		}
		
		return result;
	}
	
}
