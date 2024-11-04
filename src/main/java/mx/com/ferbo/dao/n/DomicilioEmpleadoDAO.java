package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetDomicilioEmpleado;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class DomicilioEmpleadoDAO extends BaseDAO<DetDomicilioEmpleado, Integer>
{
    private static Logger log = LogManager.getLogger(DomicilioEmpleadoDAO.class);

    public DomicilioEmpleadoDAO(Class<DetDomicilioEmpleado> modelClass) 
    {
        super(modelClass);
    }
    
    public DomicilioEmpleadoDAO()
    {
        super(DetDomicilioEmpleado.class);
    }
    
    public List<DetDomicilioEmpleado> buscarTodos()
    {
        List<DetDomicilioEmpleado> modelList = null;
        EntityManager em = null;

        try 
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("DetDomicilioEmpleado.findAll", DetDomicilioEmpleado.class)
                .getResultList();
        } catch(Exception ex) 
        {
            log.error("Problema para obtener la lista de asentamientos...",  ex);
        } finally 
        {
            this.close(em);
        }

        return modelList;
    }
    
}
