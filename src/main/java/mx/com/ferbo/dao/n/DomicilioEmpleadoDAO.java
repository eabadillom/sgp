package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatAsentamiento;
import mx.com.ferbo.model.DetDomicilioEmpleado;
import mx.com.ferbo.model.DetEmpleado;
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
            log.error("Problema para obtener la lista de domicilios...",  ex);
        } finally 
        {
            this.close(em);
        }

        return modelList;
    }
    
    @Override
    public DetDomicilioEmpleado buscarPorId(Integer idEmpleado)
    {
        DetDomicilioEmpleado model = null;
        EntityManager em = null;

        try 
        {
            em = this.getEntityManager();
            model = em.createNamedQuery("DetDomicilioEmpleado.findIdEmpleado", DetDomicilioEmpleado.class)
                .setParameter("idEmpleado", idEmpleado)
                .getSingleResult();
            
        } catch(Exception ex) 
        {
            log.warn("No hay domicilio para el empleado {} : {}",  idEmpleado, ex.getMessage());
        } finally 
        {
            this.close(em);
        }

        return model;
    }
    
    public DetDomicilioEmpleado buscarPorParametros(DetEmpleado auxEmpleado, CatAsentamiento auxAsentamiento)
    {
        DetDomicilioEmpleado model = null;
        EntityManager em = null;

        try 
        {
            em = this.getEntityManager();
            model = em.createNamedQuery("DetDomicilioEmpleado.findParametros", DetDomicilioEmpleado.class)
                .setParameter("idEmpleado", auxEmpleado.getIdEmpleado())
                .setParameter("idAsentamiento", auxAsentamiento.getKey().getId())
                .setParameter("idLocalidad", auxAsentamiento.getKey().getLocalidad().getKey().getId())
                .setParameter("idMunicipio", auxAsentamiento.getKey().getLocalidad().getKey().getMunicipio().getKey().getId())
                .setParameter("idEstado", auxAsentamiento.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getId())
                .setParameter("idPais", auxAsentamiento.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getPais().getId())
                .getSingleResult();
            
        } catch(Exception ex) 
        {
            log.warn("No hay domicilio para el empleado {} : {}",  auxEmpleado.getIdEmpleado(), ex.getMessage());
        } finally 
        {
            this.close(em);
        }
        
        return model;
    }
    
}
