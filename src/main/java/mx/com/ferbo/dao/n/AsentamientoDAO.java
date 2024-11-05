package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatAsentamiento;
import mx.com.ferbo.model.CatAsentamientoPK;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class AsentamientoDAO extends BaseDAO<CatAsentamiento, CatAsentamientoPK>
{
    private static Logger log = LogManager.getLogger(AsentamientoDAO.class);
    
    public AsentamientoDAO(Class<CatAsentamiento> modelClass) 
    {
        super(modelClass);
    }
    
    public AsentamientoDAO()
    {
        super(CatAsentamiento.class);
    }
    
    public List<CatAsentamiento> buscarTodos() 
    {
        List<CatAsentamiento> modelList = null;
        EntityManager em = null;

        try 
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatAsentamiento.findAll", CatAsentamiento.class).getResultList();
        } catch(Exception ex) 
        {
            log.error("Problema para obtener la lista de asentamientos...",  ex);
        } finally 
        {
            this.close(em);
        }

        return modelList;
    }
    
    public List<CatAsentamiento> buscarPorCodigoPostal(String codigoPostal) 
    {
        List<CatAsentamiento> modelList = null;
        EntityManager em = null;

        try 
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("CatAsentamiento.findByCodigoPostal", CatAsentamiento.class)
                .setParameter("codigoPostal", codigoPostal)
                .getResultList();
            
            log.info("Info Asentamiento: {}", modelList.toString());
            
            for(CatAsentamiento aux : modelList)
            {
                log.debug("Id Asentamiento: {}", aux.getKey().getId());
                log.debug("Id Tipo Asentamiento: {}", aux.getTipoAsentamiento().getId());
                log.debug("Id Entidad Postal: {}", aux.getEntidadPostal().getId());
                log.debug("Id Localidad: {}", aux.getKey().getLocalidad().getKey().getId());
                log.debug("Id Municipio: {}", aux.getKey().getLocalidad().getKey().getMunicipio().getKey().getId());
                log.debug("Id Estado: {}", aux.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getId());
                log.debug("Id Pais: {}", aux.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getPais().getId());
            }
            
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
