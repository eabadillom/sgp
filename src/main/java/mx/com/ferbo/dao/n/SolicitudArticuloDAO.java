/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetSolicitudArticulo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class SolicitudArticuloDAO extends BaseDAO<DetSolicitudArticulo, Integer>
{
    private static Logger log = LogManager.getLogger(DetSolicitudArticulo.class);

    public SolicitudArticuloDAO(Class<DetSolicitudArticulo> modelClass) {
        super(modelClass);
    }
    
    public SolicitudArticuloDAO()
    {
        super(DetSolicitudArticulo.class);
    }
    
    public List<DetSolicitudArticulo> buscarTodos()
    {
        List<DetSolicitudArticulo> modelList = null;
        EntityManager em = null;

        try 
        {
            em = getEntityManager();
            modelList = em.createNamedQuery("DetSolicitudArticulo.findAll", DetSolicitudArticulo.class)
                .getResultList();

        } catch (Exception ex) 
        {
            log.error("Problema para obtener el listado de articulos...", ex);
        } finally 
        {
            close(em);
        }

        return modelList;
    }
    
    public List<DetSolicitudArticulo> buscarPorIdEmpleado(Integer idEmpleado)
    {
        List<DetSolicitudArticulo> modelList = null;
        EntityManager emSGP = null;

        try {
            emSGP = getEntityManager();
            modelList = emSGP.createNamedQuery("DetSolicitudArticulo.findArticulosIdEmpleado", DetSolicitudArticulo.class)
                .setParameter("numEmpl", idEmpleado)
                .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de articulos por id...", ex);
        } finally {
            close(emSGP);
        }

        return modelList;
    }
    
}
