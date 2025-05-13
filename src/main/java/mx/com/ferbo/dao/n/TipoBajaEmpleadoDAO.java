
package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatTipoBajaEmpleado;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TipoBajaEmpleadoDAO extends BaseDAO<CatTipoBajaEmpleado, Integer>{
    
    private static Logger log = LogManager.getLogger(TipoBajaEmpleadoDAO.class);
    
    public TipoBajaEmpleadoDAO(Class<CatTipoBajaEmpleado> modelClass) {
        super(modelClass);
    }

    public TipoBajaEmpleadoDAO() {
        super(CatTipoBajaEmpleado.class);
    }
    
    public synchronized CatTipoBajaEmpleado obtenerPorId(Integer id) throws SGPException{
        CatTipoBajaEmpleado tipodebaja = null;
        EntityManager em = null;
        
        try{
            log.info("Inicia el proceso de obtner un tipo de baja en base a su id.");
            em = super.getEntityManager();
            em.getTransaction().begin();
            tipodebaja = em.find(CatTipoBajaEmpleado.class, id);
            em.getTransaction().commit();
            log.info("Finaliza el proceso de obtner un tipo de baja en base a su id.");
        }
        catch(Exception ex){
            log.warn("Hubo algun problema al obtner un tipo de baja en base a su id: " + ex);
            super.rollback(em);
            throw new SGPException("Hubo algun problema al obtner el tipo de baja en base al id: " + id);
        }
        finally{
            super.close(em);
        }
        return tipodebaja;
    }
    
    public synchronized List<CatTipoBajaEmpleado> obtenerTodos() throws SGPException{
        List<CatTipoBajaEmpleado> tiposdebaja = null;
        EntityManager em = null;
        
        try{
            log.info("Inicia el proceso de obtener todos los tipos de baja");
            em = super.getEntityManager();
            TypedQuery<CatTipoBajaEmpleado> query = em.createQuery("select e from CatTipoBajaEmpleado e", CatTipoBajaEmpleado.class);
            tiposdebaja = query.getResultList();
            log.info("Finaliza el proceso de obtener todos los tipos de baja");
        }
        catch(Exception ex){
            log.warn("Hubo algun problema al obtener todos los tipos de baja: " + ex);
            super.rollback(em);
         throw new SGPException("Hubo algun problema en obtener todos los tipos de baja");
        }
        finally{
            super.close(em);
        }
        return tiposdebaja;
    }
    
}
