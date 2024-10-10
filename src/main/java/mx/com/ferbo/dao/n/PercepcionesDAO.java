package mx.com.ferbo.dao.n;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatPercepciones;

public class PercepcionesDAO extends BaseDAO<CatPercepciones, Integer> {

    private static Logger log = LogManager.getLogger(PercepcionesDAO.class);

    public PercepcionesDAO(Class<CatPercepciones> modelClass) {
        super(modelClass);
    }
    
    public PercepcionesDAO()
    {
        super(CatPercepciones.class);
    }

    public List<CatPercepciones> buscarTodos() 
    {
        List<CatPercepciones> modelList = null;
        EntityManager em = null;

        try 
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatPercepciones.findAll", modelClass)
                .getResultList();
        } catch (Exception ex) 
        {
            log.error("Problema para obtener el listado de percepciones...", ex);
        } finally 
        {
            this.close(em);
        }

        return modelList;
    }
    
    public List<CatPercepciones> buscarTodosActivos() 
    {
        List<CatPercepciones> modelList = null;
        EntityManager em = null;

        try 
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatPercepciones.findByActive", modelClass)
                .getResultList();
        } catch (Exception ex) 
        {
            log.error("Problema para obtener el listado de percepciones activas...", ex);
        } finally 
        {
            this.close(em);
        }

        return modelList;
    }

    public CatPercepciones buscarActual(Date fechaCorte) {
        CatPercepciones model = null;
        EntityManager em = null;

        try {
            em = this.getEntityManager();
            model = em.createNamedQuery("CatPercepciones.findVigente", modelClass)
                .setParameter("fechaCorte", fechaCorte)
                .getSingleResult();
        } catch (Exception ex) {
            log.error("Problema para obtener la percepcion actual...", ex);
        } finally {
            this.close(em);
        }

        return model;
    }

}
