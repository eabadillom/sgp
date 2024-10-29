package mx.com.ferbo.dao.n;

import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatPlanta;
import mx.com.ferbo.model.DetFpClient;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FpClientDAO extends BaseDAO<DetFpClient, Integer> {

    private static Logger log = LogManager.getLogger(FpClientDAO.class);
    EntityManagerFactory emf = null;

    public FpClientDAO(Class<DetFpClient> modelClass) {
        super(modelClass);
    }

    public FpClientDAO() {
        super(DetFpClient.class);

    }

    @Override
    public synchronized void guardar(DetFpClient cliente) throws SGPException {
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("sgpPU");
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
        } catch (Exception ex) {
            super.rollback(em);
            log.info("Problema en la base de datos al momento de registrar...", ex);
            throw new SGPException("No se pudo guardar el elemento");
        } finally {
           super.close(em);
        }
    }

    @Override
    public synchronized void actualizar(DetFpClient cliente) throws SGPException {

        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("sgpPU");
            em = emf.createEntityManager();
            em.getTransaction().begin();
            cliente = em.merge(cliente);
            em.getTransaction().commit();

        } catch (Exception ex) {
            super.rollback(em);
            log.info("Problema en la base de datos al momento de actualizar registro...", ex );
            throw new SGPException("No se pudo actualizar el elemento");
        } finally {
            super.close(em);
        }
    }

    @Override
    public synchronized DetFpClient buscarPorId(Integer id) {
        EntityManager em = null;
        DetFpClient buscado = null;
        try {
            emf = Persistence.createEntityManagerFactory("sgpPU");
            em = emf.createEntityManager();
            em.getTransaction().begin();
            buscado = em.find(DetFpClient.class, id);
            em.getTransaction().commit();
        } catch (Exception ex) {
            super.rollback(em);
            log.info("Problema en la base de datos al momento de buscar por id...", ex);
            try {
                throw new SGPException("No se encuentra el elemento buscado");
            } catch (SGPException ex1) {
                java.util.logging.Logger.getLogger(FpClientDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            super.close(em);
        }
        return buscado;
    }

    @Override
    public synchronized void eliminar(DetFpClient cliente) throws SGPException{
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("sgpPU");
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.remove(em.contains(cliente) ? cliente : em.merge(cliente));
            em.getTransaction().commit();
        } catch (Exception ex) {
            super.rollback(em);
            log.info("Problema en la base de datos al momento de eliminar registro...", ex);
            throw new SGPException("No se pudo eliminar el elemento");
        } finally {
            super.close(em);
        }
    }

    public synchronized List<DetFpClient> obtenerTodos() throws SGPException{
        EntityManager em = null;
        List<DetFpClient> clientes = null;
        
        try{
            emf = Persistence.createEntityManagerFactory("sgpPU");
            em = emf.createEntityManager();
            TypedQuery<DetFpClient> resultado =  em.createQuery("select e from DetFpClient e", DetFpClient.class);
            clientes = resultado.getResultList();
        }
        catch(Exception ex){
            super.rollback(em);
            log.info("Problema en la base de datos al momento de obtener los registros...", ex);
            throw new SGPException("No se puedieron obtener los registros");
            
        }
        finally{
            super.close(em);
        }
        return clientes;
    }

}
