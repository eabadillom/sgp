package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatAsentamiento;
import mx.com.ferbo.model.CatAsentamientoPK;
import mx.com.ferbo.util.SGPException;
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
            
            log.trace("Info Asentamiento: {}", modelList.toString());
            
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
    

    public synchronized List<CatAsentamiento> obtenerTodosPorTipo(Integer idPais, Integer idEstado, Integer idMunicipio, Integer idLocalidad, short idTipo) throws SGPException{
        EntityManager em = null;
        List<CatAsentamiento> asentamientos = null;
        try{
            log.info("Inicia proceso de obtener todos los asentamientos.");
            em = super.getEntityManager();
            TypedQuery<CatAsentamiento> resultado = em.createQuery("select e from  CatAsentamiento e where e.key.localidad.key.municipio.key.estado.key.pais.id = :idPais and e.key.localidad.key.municipio.key.estado.key.id = :idEstado and e.key.localidad.key.municipio.key.id = :idMunicipio and e.key.localidad.key.id = :idLocalidad and e.tipoAsentamiento.id = :idTipo", CatAsentamiento.class);
            resultado.setParameter("idPais", idPais);
            resultado.setParameter("idEstado", idEstado);
            resultado.setParameter("idMunicipio", idMunicipio);
            resultado.setParameter("idLocalidad", idLocalidad);
            resultado.setParameter("idTipo", idTipo);
            asentamientos = resultado.getResultList();
            
        }
        catch(Exception ex){
            super.rollback(em);
            log.error("Hubo algun problema al obtener todos los registros de la tabla CatLAsentamiento");
            throw new SGPException("Problema al obtener los registros" + ex);
        }
        finally{
            log.info("Finaliza proceso de obtener todos los asentamientos");
            super.close(em);
        }
        return asentamientos;
    }
    
    public CatAsentamiento buscarPorParametros(Integer idAsentamiento, Integer idLocalidad, Integer idMunicipio, Integer idEstado, Integer idPais) 
    {
        CatAsentamiento model = null;
        EntityManager em = null;

        try 
        {
            em = this.getEntityManager();
            model = em.createNamedQuery("CatAsentamiento.findByParametros", CatAsentamiento.class)
                .setParameter("idAsentamiento", idAsentamiento)
                .setParameter("idLocalidad", idLocalidad)
                .setParameter("idMunicipio", idMunicipio)
                .setParameter("idEstado", idEstado)
                .setParameter("idPais", idPais)
                .getSingleResult();
            
            log.trace("Info Asentamiento: {}", model.toString());
            
            if(model != null)
            {
                log.debug("Id Asentamiento: {}", model.getKey().getId());
                log.debug("Id Tipo Asentamiento: {}", model.getTipoAsentamiento().getId());
                log.debug("Id Entidad Postal: {}", model.getEntidadPostal().getId());
                log.debug("Id Localidad: {}", model.getKey().getLocalidad().getKey().getId());
                log.debug("Id Municipio: {}", model.getKey().getLocalidad().getKey().getMunicipio().getKey().getId());
                log.debug("Id Estado: {}", model.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getId());
                log.debug("Id Pais: {}", model.getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getPais().getId());
            }
            
        } catch(Exception ex) 
        {
            log.error("Problema para obtener el asentamiento...",  ex);
        } finally 
        {
            this.close(em);
        }

        return model;
    }
    
}
