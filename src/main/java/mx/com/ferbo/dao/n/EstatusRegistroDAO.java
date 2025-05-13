package mx.com.ferbo.dao.n;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatEstatusRegistro;

public class EstatusRegistroDAO extends BaseDAO<CatEstatusRegistro, Integer> {
	private static Logger log = LogManager.getLogger(EstatusRegistroDAO.class);

	public EstatusRegistroDAO(Class<CatEstatusRegistro> modelClass) {
		super(modelClass);
	}

	public EstatusRegistroDAO() {
		super(CatEstatusRegistro.class);
	}

	public CatEstatusRegistro buscarPorCodigo(String codigo) {
		CatEstatusRegistro model = null;
		EntityManager em = null;

		try {
			em = this.getEntityManager();
			model = em.createNamedQuery("CatEstatusRegistro.findByCodigo", CatEstatusRegistro.class)
					.setParameter("codigo", codigo)
					.getSingleResult()
					;
		} catch (Exception ex) {
			log.error("Problema para obtener el estatus del registro", ex);
		} finally {
			this.close(em);
		}

		return model;
	}
	
	public List<CatEstatusRegistro> buscarTodos() {
		List<CatEstatusRegistro> modelList = null;
		EntityManager em = null;
		
		try {
			em = this.getEntityManager();
			modelList = em.createNamedQuery("CatEstatusRegistro.findAll", this.modelClass)
					.getResultList()
					;
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de estatus de registro...", ex);
		} finally {
			this.close(em);
		}
		
		return modelList;
	}

}