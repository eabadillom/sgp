/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.dao.n;

import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.CatArticulo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class ArticuloDAO extends BaseDAO<CatArticulo, Integer>
{
    private static Logger log = LogManager.getLogger(CatArticulo.class);

    public ArticuloDAO(Class<CatArticulo> modelClass) {
        super(modelClass);
    }
    
    public ArticuloDAO()
    {
        super(CatArticulo.class);
    }
    
    public List<CatArticulo> buscarTodos()
    {
        List<CatArticulo> modelList = null;
        EntityManager emSGP = null;

        try {
            emSGP = getEntityManager();
            modelList = emSGP.createNamedQuery("CatArticulo.findAll", CatArticulo.class)
                .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de articulos...", ex);
        } finally {
            close(emSGP);
        }

        return modelList;
    }
    
    public List<CatArticulo> buscarTodosActivos()
    {
        List<CatArticulo> modelList = null;
        EntityManager emSGP = null;

        try {
            emSGP = getEntityManager();
            modelList = emSGP.createNamedQuery("CatArticulo.findAllActive", CatArticulo.class)
                .getResultList();
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de articulos activos...", ex);
        } finally {
            close(emSGP);
        }

        return modelList;
    }
    
}
