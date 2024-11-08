package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatEntidadPostal;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntidadPostalDAO extends BaseDAO {

    private static Logger log = LogManager.getLogger(FpClientDAO.class);

    public EntidadPostalDAO(Class<CatEntidadPostal> modelClass) {
        super(modelClass);
    }

    public EntidadPostalDAO() {
        super(CatEntidadPostal.class);
    }

    public synchronized List<CatEntidadPostal> obtenerTodos() throws SGPException {
        EntityManager em = null;
        List<CatEntidadPostal> entidadespostales = null;
        try {
            log.info("Inicia proceso de obtener todos los entidadespostales.");
            em = super.getEntityManager();
            TypedQuery<CatEntidadPostal> resultado = em.createQuery("select e from  CatEntidadPostal e", CatEntidadPostal.class);
            entidadespostales = resultado.getResultList();

        } catch (Exception ex) {
            super.rollback(em);
            log.error("Hubo algun problema al obtener todos los registros de la tabla CatEntidadPostal");
            throw new SGPException("Problema al obtener los registros" + ex);
        } finally {
            log.info("Finaliza proceso de obtener todos los entidades postales");
            super.close(em);
        }
        return entidadespostales;
    }

    public synchronized CatEntidadPostal buscarPorId(Integer id) throws SGPException {

        EntityManager em = null;
        CatEntidadPostal entidadpostal = null;

        try {
            log.info("Inicia proceso de obtener elemento con id: {}", id);
            em = super.getEntityManager();
            em.getTransaction().begin();
            entidadpostal = em.find(CatEntidadPostal.class, id);
            em.getTransaction().commit();
        } catch (Exception ex) {
            super.rollback(em);
            log.error("Hubo algun problema al buscar el elemento con id: " + id);
            throw new SGPException("Problema al obtener el elemento por id " + ex);
        } finally {
            super.close(em);
            log.info("Finaliza proceso de obtener elemento con id: {}", id);
        }
        return entidadpostal;
    }

    public synchronized void guardar(CatEntidadPostal entidadpostal) throws SGPException {

        EntityManager em = null;

        try {
            log.info("Inicia proceso de guardar el registro en la tabla CatEntidadPostal");
            em = super.getEntityManager();
            em.getTransaction().begin();
            em.persist(entidadpostal);
            em.getTransaction().commit();
        } catch (Exception ex) {
            super.rollback(em);
            log.error("Hubo algun proble al guardar el registro {} en la tabla CatEntidadPostal", entidadpostal);
            throw new SGPException("Problema al guardar el registro " + ex);
        } finally {
            super.close(em);
            log.info("Finaliza prodceo de guardar el registro en la tabla CatEntidadPostal");
        }
    }

    public synchronized void actualizar(CatEntidadPostal entidadpostal) throws SGPException {

        EntityManager em = null;

        try {
            log.info("Inicia proceso de actualizar el registro de la tabla CatEntidadPostal");
            em = super.getEntityManager();
            em.getTransaction().begin();
            em.merge(entidadpostal);
            em.getTransaction().commit();
        } catch (Exception ex) {
            super.rollback(em);
            log.error("Hubo algun problema al actualizar el registro {} en la tabla CatEntidadPostal", entidadpostal);
            throw new SGPException("Problema al actualizar el registro " + ex);
        } finally {
            super.close(em);
            log.info("Finaliza prodceo de actualizar el registro de la tabla CatEntidadPostal");
        }
    }

    public synchronized void eliminar(CatEntidadPostal entidadpostal) throws SGPException {

        EntityManager em = null;

        try {
            log.info("Inicia proceso de eliminar el registro de la tabla CatEntidadPostal");
            em = super.getEntityManager();
            em.getTransaction().begin();
            em.remove(em.contains(entidadpostal) ? entidadpostal : em.merge(entidadpostal));
            em.getTransaction().commit();
        } catch (Exception ex) {
            super.rollback(em);
            log.error("Hubo algun problema al eliminar el registro {} de la tabla CatEntidadPostal", entidadpostal);
            throw new SGPException("Problema al eliminar el registro " + ex);
        } finally {
            super.close(em);
            log.info("Finaliza proceso de actualizar el registro de la tabla CatEntidadPostal");
        }
    }
}
