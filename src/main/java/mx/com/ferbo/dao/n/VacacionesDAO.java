
package mx.com.ferbo.dao.n;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetVacaciones;
import mx.com.ferbo.util.SGPException;

public class VacacionesDAO extends BaseDAO {
    
    private static Logger log = LogManager.getLogger(VacacionesDAO.class);
    
    public VacacionesDAO(Class<DetVacaciones> modelClass) {
        super(modelClass);
    }
    
    public VacacionesDAO(){
        super(DetVacaciones.class);
    }
    
    public synchronized DetVacaciones buscarPorId(Integer id) throws SGPException{
        DetVacaciones vacaciones = null;
        EntityManager em = null;
        
        try{
            log.info("Inicia el proceso de obtener las vaciones por id");
            em = super.getEntityManager();
            em.getTransaction().begin();
            vacaciones = em.find(DetVacaciones.class, id);
            em.getTransaction().commit();
            log.info("Finaliza el proceso de obtener las vacaciones por id");
        }
        catch(Exception ex){
            log.warn("Hubo algn probleman al obtener las vacaciones por id: {}", ex);
            super.rollback(em);
            throw new SGPException("Hubo algun problema al obtener las vacaciones");
        }
        finally{
            super.close(em);
        }
        
        return vacaciones;
    }
    
    public synchronized List<DetVacaciones> obtenerTodos() throws SGPException{
        List<DetVacaciones> todasvacaciones = null;
        EntityManager  em = null;
        
        try{
            log.info("Inicia el proceso de obtener todas las vacaciones del empleado");
            em = super.getEntityManager();
            em.getTransaction().begin();
            TypedQuery resultado = em.createQuery("select e from DetVacaciones e", DetVacaciones.class);
            todasvacaciones = resultado.getResultList();
            log.info("Finaliza el proceso de obtener todas las vacaciones del empleado");
        }
        catch(Exception ex){
            log.warn("Hubo algun problema al obtener todas las vacaciones del empleado: {}", ex);
            super.rollback(em);
            throw new SGPException("Hubo algun problema al obtene todas la vacaciones del empleado");
        }
        finally{
            super.close(em);
        }
        
        return todasvacaciones;
    }
    
    public DetVacaciones buscarPeriodoPorFecha(Integer idEmpleado, Date fecha) {
    	DetVacaciones model = null;
    	EntityManager em = null;
    	
    	try {
    		em = this.getEntityManager();
    		model = em.createNamedQuery("DetVacaciones.buscarPeriodoPorEmpleadoFecha", DetVacaciones.class)
    				.setParameter("idEmpleado", idEmpleado)
    				.setParameter("fecha", fecha)
    				.getSingleResult()
    				;
    	} catch(Exception ex) {
    		log.error("Problema para obtener el periodo vacacional...", ex);
    	} finally {
    		this.close(em);
    	}
    	
    	return model;
    }
    
    public synchronized List<DetVacaciones> obtenerPeriodosPorFecha(Integer idEmpleado, Date fechaSeleccionada) throws SGPException{
        List<DetVacaciones> periodos = null;
        EntityManager em = null;
        
        try{
            log.info("Inicia el proceso para obtener los periodos vacacionales en base a una fecha.");
            em = getEntityManager();
            TypedQuery query = em.createQuery("select v from DetVacaciones v where v.empleado.idEmpleado = :idEmpleado and v.fechafin < :fechaSeleccionada", DetVacaciones.class);
            query.setParameter("idEmpleado", idEmpleado);
            query.setParameter("fechaSeleccionada", fechaSeleccionada);
            periodos = query.getResultList();
            log.info("Finaliza el proceso para obtener los periodos vacacionales en base a una fecha.");
        }
        catch(Exception ex){
            log.error("Error al obtener los periodos vacacionales en base a una fecha. " + ex.getMessage());
            rollback(em);
            throw new SGPException("Hubo algun problema al obtener los periodos vacacionales en base a una fecha");
        }
        finally{
            close(em);
        }
        
        return periodos;
    }
}
