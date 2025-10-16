
package mx.com.ferbo.dao.n;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetVacaciones;
import mx.com.ferbo.util.SGPException;

public class VacacionesDAO extends BaseDAO<DetVacaciones, Integer> {
    
    private static Logger log = LogManager.getLogger(VacacionesDAO.class);
    
    public VacacionesDAO(Class<DetVacaciones> modelClass) {
        super(modelClass);
    }
    
    public VacacionesDAO(){
        super(DetVacaciones.class);
    }
    
    public synchronized List<DetVacaciones> obtenerTodos() throws SGPException{
        List<DetVacaciones> todasvacaciones = null;
        EntityManager  em = null;
        
        try{
            log.info("Inicia el proceso de obtener todas las vacaciones del empleado");
            em = super.getEntityManager();
            em.getTransaction().begin();
            TypedQuery<DetVacaciones> resultado = em.createQuery("select e from DetVacaciones e", DetVacaciones.class);
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
        List<DetVacaciones> modelList = null;
        EntityManager em = null;
        
        try{
            log.info("Inicia el proceso para obtener los periodos vacacionales en base a una fecha.");
            em = getEntityManager();
            modelList = em.createNamedQuery("DetVacaciones.buscarPeriodosDisponibles", DetVacaciones.class)
            		.setParameter("idEmpleado", idEmpleado)
            		.setParameter("fecha", fechaSeleccionada)
            		.getResultList();
            log.info("Finaliza el proceso para obtener los periodos vacacionales en base a una fecha.");
        } catch(Exception ex) {
            log.error("Error al obtener los periodos vacacionales en base a una fecha. " + ex.getMessage());
            this.rollback(em);
            throw new SGPException("Hubo algun problema al obtener los periodos vacacionales en base a una fecha");
        } finally {
            this.close(em);
        }
        
        return modelList;
    }
    
    public synchronized List<DetVacaciones> cargarPeriodos(Integer idEmpleado, Date fecha) {
    	List<DetVacaciones> modelList = null;
    	EntityManager em = null;
    	
    	try {
    		em = this.getEntityManager();
    		
    		modelList = em.createNamedQuery("DetVacaciones.buscarPeriodosConSaldo", this.modelClass)
    				.setParameter("idEmpleado", idEmpleado)
    				.setParameter("fecha", fecha)
    				.getResultList()
    				;
    		
    		for(DetVacaciones model : modelList) {
    			model.getRegistroVacaciones().stream().forEach(item -> log.debug("id registro: {}", item.getRegistro().getStatus().getIdEstatus()));
    		}
    		
            log.info("Finaliza el proceso para obtener los periodos vacacionales en base a una fecha.");
    	} catch(Exception ex) {
    		log.error("Problema para obtener los periodos vacacionales...", ex);
    	} finally {
    		this.close(em);
    	}
    	
    	return modelList;
    }
    
    public DetVacaciones obtenerPorRfcFecha(String rfc, Date fecha) {
    	DetVacaciones model = null;
    	EntityManager em = null;
    	String sql = null;
    	
    	try {
    		sql = "select * from det_vacaciones dv\n"
    				+ "inner join\n"
    				+ "(\n"
    				+ "	select\n"
    				+ "		v.id_empleado,\n"
    				+ "		max(v.fh_fin) as fh_fin\n"
    				+ "	from det_vacaciones v\n"
    				+ "	inner join det_empleado e\n"
    				+ "		on v.id_empleado = e.id_empleado\n"
    				+ "	inner join inf_empleado_empresa ee\n"
    				+ "		on e.id_empleado_empresa = ee.id_empleado_empresa\n"
    				+ "	where (v.nu_dias_totales - v.nu_dias_tomados) > 0\n"
    				+ "		and ee.nb_rfc = :rfc\n"
    				+ "		and v.fh_fin < :fecha\n"
    				+ "	group by id_empleado\n"
    				+ ") t on dv.id_empleado = t.id_empleado and dv.fh_fin = t.fh_fin\n"
    				;
    		
    		em = this.getEntityManager();
    		model = (DetVacaciones) em.createNativeQuery(sql, DetVacaciones.class)
    				.setParameter("rfc", rfc)
    				.setParameter("fecha", fecha)
    				.getSingleResult()
    				;
    	} catch(NoResultException ex) {
    		log.warn("No se encontró información para la consulta: {}", ex.getMessage());
    	}catch(Exception ex) {
    		log.error("Problema para obtener la lista de periodos vacacionales...", ex);
    	} finally {
    		this.close(em);
    	}
    	
    	return model;
    }
    
    public DetVacaciones buscarEnTiempoPorRfcFecha(String rfc, Date fecha) {
    	DetVacaciones model = null;
    	EntityManager em = null;
    	String sql = null;
    	
    	try {
    		sql = "select\n"
    				+ "	nv.id_nom_vacaciones,\n"
    				+ "	dv.*\n"
    				+ "from det_vacaciones dv\n"
    				+ "inner join (\n"
    				+ "	select\n"
    				+ "		v.id_empleado,\n"
    				+ "		max(v.fh_fin) as fh_fin\n"
    				+ "	from det_vacaciones v\n"
    				+ "	inner join det_empleado e on v.id_empleado = e.id_empleado\n"
    				+ "	inner join inf_empleado_empresa ee on e.id_empleado_empresa = ee.id_empleado_empresa\n"
    				+ "	where (v.st_prima_pagada = false or v.st_prima_pagada is null)\n"
    				+ "		and (ee.fh_ingreso <= :fecha and ee.fh_baja is null or ee.fh_ingreso <= :fecha and ee.fh_baja >= :fecha)\n"
    				+ "		and (ee.nb_rfc = :rfc)\n"
    				+ "		and v.fh_fin < :fecha\n"
    				+ "	group by id_empleado\n"
    				+ ") t on dv.id_empleado = t.id_empleado and dv.fh_fin = t.fh_fin\n"
    				+ "left outer join det_nom_vacaciones nv\n"
    				+ "on dv.id_vacaciones = nv.id_vacaciones\n"
    				+ "where (nv.id_nom_vacaciones is null)"
    				;
    		em = this.getEntityManager();
    		model = (DetVacaciones) em.createNativeQuery(sql, modelClass)
    				.setParameter("rfc", rfc)
    				.setParameter("fecha", fecha)
    				.getSingleResult();
    	} catch(NoResultException ex) {
    		
    	} catch(Exception ex) {
    		
    	} finally {
    		this.close(em);
    	}
    	
    	return model;
    }
    
    @SuppressWarnings("unchecked")
	public List<DetVacaciones> buscarReportadasPorRfcFecha(String rfc, Date fecha) {
    	List<DetVacaciones> modelList = null;
    	EntityManager em = null;
    	String sql = null;
    	
    	try {
    		sql = "select\n"
    				+ "	v.*\n"
    				+ "from det_vacaciones v\n"
    				+ "inner join det_empleado e on v.id_empleado = e.id_empleado\n"
    				+ "inner join inf_empleado_empresa ee on e.id_empleado_empresa = ee.id_empleado_empresa\n"
    				+ "where ee.nb_rfc = :rfc and v.fh_fin < :fecha\n"
    				+ "	and ( (v.nu_dias_totales - v.nu_dias_tomados) > 0)\n"
    				+ "	and (v.st_dias_pend_pagados = 0)"
    				;
    		
    		em = this.getEntityManager();
    		modelList = (List<DetVacaciones>) em.createNativeQuery(sql, modelClass)
    				.setParameter("rfc", rfc)
    				.setParameter("fecha", fecha)
    				.getResultList()
    				;
    		
    	} catch(Exception ex) {
    		log.error("Problema para obtener la lista de periodos vacacionales...", ex);
    	} finally {
    		this.close(em);
    	}
    	
    	return modelList;
    }
    
    public List<DetVacaciones> buscarPeriodosEnTiempoNoPagados(String rfc) {
    	List<DetVacaciones> modelList = null;
    	EntityManager em = null;
    	
    	try {
    		em = this.getEntityManager();
    		modelList = em.createNamedQuery("DetVacaciones.buscarPeriodosNoPagados", this.modelClass)
    				.setParameter("rfc", rfc)
    				.getResultList();
    		
    	} catch(Exception ex) {
    		log.error("Problema para obtener la lista de periodos vacacionales...", ex);
    	} finally {
    		this.close(em);
    	}
    	
    	return modelList;
    }
}
