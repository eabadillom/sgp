/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetSolicitudPrenda;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class SolicitudPrendaDAO extends BaseDAO<DetSolicitudPrenda, Integer>
{
    private static Logger log = LogManager.getLogger(SolicitudPrendaDAO.class);
    
    public SolicitudPrendaDAO(Class<DetSolicitudPrenda> modelClass) 
    {
        super(modelClass);
    }
    
    public SolicitudPrendaDAO()
    {
        super(DetSolicitudPrenda.class);
    }
    
    public List<DetSolicitudPrenda> buscarTodos() 
    {
        List<DetSolicitudPrenda> modelList = null;
        EntityManager em = null;

        try 
        {
            em = getEntityManager();
            modelList = em.createNamedQuery("DetSolicitudPrenda.findAll", DetSolicitudPrenda.class)
                .getResultList();
        } catch (Exception ex) 
        {
            log.error("Problema para obtener el listado de prendas...", ex);
        } finally 
        {
            close(em);
        }

        return modelList;
    }
    
    public List<DetSolicitudPrenda> buscarPorIdEmpleado(Integer idEmpleado)
    {
        List<DetSolicitudPrenda> modelList = null;
        EntityManager em = null;

        try 
        {
            em = getEntityManager();
            modelList = em.createNamedQuery("DetSolicitudPrenda.findPrendasIdEmpleado", DetSolicitudPrenda.class)
                .setParameter("numEmpl", idEmpleado)
                .getResultList();

        } catch (Exception ex) 
        {
            log.error("Problema para obtener el listado de prendas por id...", ex);
        } finally 
        {
            close(em);
        }

        return modelList;
    }
    
}
