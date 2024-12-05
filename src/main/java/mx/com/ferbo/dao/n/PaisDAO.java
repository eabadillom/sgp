package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Pais;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class PaisDAO extends BaseDAO<Pais, String>
{
    private static Logger log = LogManager.getLogger(PaisDAO.class);

    public PaisDAO(Class modelClass) {
        super(modelClass);
    }
    
    public PaisDAO()
    {
        super(Pais.class);
    }
    
    public List<Pais> buscarTodos()
    {
        List<Pais> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("Pais.findAll", Pais.class)
                .getResultList();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el pais: {}", ex.getMessage());
        }finally 
        {
            this.close(em);
        }
        
        return modelList;
    }
    
    public synchronized Pais obtenrPorId(Integer id) throws SGPException{
        Pais resultado = null;
        EntityManager em = null;
        
        try{ 
            log.info("Comienza el proceso de obtener el registro por id");
            em = super.getEntityManager();
            em.getTransaction().begin();
            resultado = em.find(Pais.class, id);
            em.getTransaction().commit();
            log.info("Finaliza el proceso de obtener el registro por id");
        }
        catch (Exception ex){
            log.error("Hubo algun problema al obtener el registro por id. " + ex.getMessage());
            super.rollback(em);
            throw new SGPException("Hubo algun problema al obtener el registro");
        }
        finally{
            super.close(em);
        }
        
        return resultado;
    }
    
    public List<Pais> buscarPorId(Integer idPais)
    {
        List<Pais> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("Pais.findById", Pais.class)
                .setParameter("cd_pais", idPais)
                .getResultList();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el pais: {}", ex.getMessage());
        }finally 
        {
            this.close(em);
        }
        
        return modelList;
    }
    
    public List<Pais> buscarPorClave(String clavePais)
    {
        List<Pais> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("Pais.findByClave", Pais.class)
                .setParameter("nb_clave", clavePais)
                .getResultList();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el pais: {}", ex.getMessage());
        }finally 
        {
            this.close(em);
        }
        
        return modelList;
    }
    
    /*public synchronized void actualizar(Integer id, String nuevaclave, String nuevonombre) throws SGPException{
         EntityManager em = null;
        String consulta = "";

        try {
            log.info("Inicia el proceso para actualizar el registro en la tabla de pais");
            em = super.getEntityManager();
            consulta += "update Pais e set e.nombrePais = :nuevonombre, e.clave = :nuevaclave ";
            consulta += "where e.id = :id";
            em.getTransaction().begin();
            Query query = em.createQuery(consulta);
            query.setParameter("nuevonombre", nuevonombre);
            query.setParameter("nuevaclave", nuevaclave);
            query.setParameter("id", id);
            query.executeUpdate();
            em.getTransaction().commit();
            log.info("Finaliza el proceso para actualizar el registro en la tabla de pais");
        } catch (Exception ex) {
            log.error("Hubo algun problema al momento de actualizar el registro en la tabla de pais" + ex);
            super.rollback(em);
            throw new SGPException("No se pudo actualizar el registro de pais");
        } finally {
            super.close(em);
        }
    }*/
}
