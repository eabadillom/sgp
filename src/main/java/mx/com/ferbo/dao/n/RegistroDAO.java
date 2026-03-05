package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TemporalType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetRegistro;

public class RegistroDAO extends BaseDAO<DetRegistro, Integer> {
	
	private static Logger log = LogManager.getLogger(RegistroDAO.class);

  public RegistroDAO(Class<DetRegistro> modelClass) {
        super(modelClass);
    }
    
    public RegistroDAO()
    {
        super(DetRegistro.class);
    }
    
//    public List<DetRegistro> buscarTodos()
//    {
//        List<DetRegistro> modelList = null;
//        EntityManager em = null;
//        
//        try
//        {
//            em = this.getEntityManager();
//            modelList = em.createNamedQuery("DetRegistro.findAll", DetRegistro.class)
//                .getResultList();
//        }catch (Exception ex) 
//        {
//            log.error("Problema para obtener el listado de registros...", ex);
//            modelList = new ArrayList<>();
//        } finally 
//        {
//            this.close(em);
//        }
//        
//        return modelList;
//    }

    public DetRegistro buscarPorEmpleadoFechaEntrada(Integer idEmpleado, Date fechaEntradaInicio, Date fechaEntradaFin) {
        DetRegistro model = null;
        EntityManager emSGP = null;
        try {
            emSGP = this.getEntityManager();
            model = emSGP.createNamedQuery("DetRegistro.findByIdEmpleadoAndFecha", DetRegistro.class)
                    .setParameter("idEmpleado", idEmpleado)
                    .setParameter("fechaEntradaInicio", fechaEntradaInicio)
                    .setParameter("fechaEntradaFin", fechaEntradaFin)
                    .getSingleResult();
            log.debug("IdEmpleado: {}", model.getEmpleado().getIdEmpleado());
            log.debug("Estatus: {}", model.getStatus().getIdEstatus());
        } catch (Exception ex) {
            model = null;
        } finally {
            close(emSGP);
        }

        return model;
    }
    
    public Optional<DetRegistro> buscarPorEmpleadoFecha(Integer idEmpleado, String clave, Date fecha) {
    	Optional<DetRegistro> optional;
    	DetRegistro model = null;
    	EntityManager em = null;
    	String query = null;
    	
    	try {
    		query = "SELECT dr.* FROM det_registro dr\n"
    				+ "inner join cat_estatus_registro cer on dr.id_estatus = cer.id_estatus\n"
    				+ "WHERE dr.id_empleado = :idEmpleado AND cer.codigo = :clave AND DATE(dr.fecha_entrada ) = :fecha";
    		
    		em = this.getEntityManager();
    		
    		model = (DetRegistro) em.createNativeQuery(query, modelClass)
    				.setParameter("idEmpleado", idEmpleado)
    				.setParameter("clave", clave)
    				.setParameter("fecha", fecha)
    				.getSingleResult()
    				;
    		
    		optional = Optional.of(model);
    	} catch(Exception ex) {
    		log.error("No se encontro informacion para idEmpleado = {}, fecha = {}...\n{}", idEmpleado, fecha, ex);
    		optional = Optional.empty();
    	} finally {
    		this.close(em);
    	}
    	
    	return optional;
    }

    public List<DetRegistro> buscar(Integer idEmpleado, Date fechaEntrada, Date fechaSalida) {
        List<DetRegistro> modelList = null;
        EntityManager em = null;
        try {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("DetRegistro.findByEmpleadoPeriodo", modelClass)
                    .setParameter("idEmpleado", idEmpleado)
                    .setParameter("fechaEntrada", fechaEntrada)
                    .setParameter("fechaSalida", fechaSalida)
                    .getResultList();

            for (DetRegistro r : modelList) {
                log.trace("Registro - idEmpleado: {}", r.getEmpleado().getIdEmpleado());
                log.trace("Status registro: {}", r.getStatus().getIdEstatus());
            }

        } catch (Exception ex) {
            log.error("Problema para obtener el registro de asistencia del empleado...", ex);
        } finally {
            this.close(em);
        }

        return modelList;
    }

    public List<DetRegistro> buscarPorPlantaPeriodo(Integer idPlanta, Date fechaInicio, Date fechaFin) {
        List<DetRegistro> modelList = null;
        EntityManager em = null;
        try {
        	log.info("Cargando información de registros...");
            em = this.getEntityManager();
            modelList = em.createNamedQuery("DetRegistro.findByPlantaPeriodo", DetRegistro.class)
                    .setParameter("idPlanta", idPlanta)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
            for (DetRegistro model : modelList) {
                cargaInfo(model);
            }
            log.info("Terminando carga de información de registros.");
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de registros...", ex);
            modelList = new ArrayList<>();
        } finally {
            this.close(em);
        }
        return modelList;
    }
    
    private void cargaInfo(DetRegistro model) {
    	try {
    		log.trace("IdEmpleado: {}", model.getEmpleado().getIdEmpleado());
    		log.trace("Id Planta: {}", model.getEmpleado().getDatoEmpresa().getPlanta().getIdPlanta());
    	} catch(Exception ex) {
    		log.warn("Problema para cargar el detalle del empleado...", ex);
    	}
    }
    
    public List<DetRegistro> consultaRegistrosPorIdEmp(Integer idEmp)
    {
        List<DetRegistro> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("DetRegistro.findByIdEmpDesc", DetRegistro.class)
                .setParameter("idEmp", idEmp)
                .getResultList();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el listado de registros...", ex);
            modelList = new ArrayList<>();
        } finally 
        {
            this.close(em);
        }
        
        return modelList;
    }
    
    public List<DetRegistro> buscarPorIdEmpleadoActivo(Integer idEmp, Date fechaEntrada)
    {
        List<DetRegistro> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("DetRegistro.findByIdEmplActivo", DetRegistro.class)
                .setParameter("idEmp", idEmp)
                .setParameter("fechaEntrada", fechaEntrada)
                .getResultList();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el listado de registros...", ex);
            modelList = new ArrayList<>();
        } finally 
        {
            this.close(em);
        }
        
        return modelList;
    }
    
	public DetRegistro buscarPorDia(Integer idEmp, Date inicioDiaActual, Date finDiaActual) {
		DetRegistro model = null;
		EntityManager em = null;

		try {
			em = this.getEntityManager();
			model = em.createNamedQuery("DetRegistro.findToday", DetRegistro.class)
					.setParameter("idEmp", idEmp)
					.setParameter("inicioDia", inicioDiaActual)
					.setParameter("finDia", finDiaActual)
					.getSingleResult();
		} catch(NoResultException ex) {
			log.warn("Registro no encontrado: idEmpleado = {}, Fecha hora inicio = {}, Fecha hora fin = {}", idEmp, inicioDiaActual, finDiaActual);
		} catch (Exception ex) {
			log.error("Problema para obtener el registro...", ex);
		} finally {
			this.close(em);
		}

		return model;
	}
    
    public List<DetRegistro> buscarPorEmpPeriodoSolicitud(Integer idEmp, String codigo, Date fechaInicial, Date fechaFin)
    {
        List<DetRegistro> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("DetRegistro.findByPeriodoSolicitud", DetRegistro.class)
                .setParameter("idEmp", idEmp)
                .setParameter("codigo", codigo)
                .setParameter("fechaInicial", fechaInicial, TemporalType.TIMESTAMP)
                .setParameter("fechaFinal", fechaFin, TemporalType.TIMESTAMP)
                .getResultList();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el listado de registros...", ex);
            modelList = new ArrayList<>();
        } finally 
        {
            this.close(em);
        }
        
        return modelList;
    }
    
    public DetRegistro buscarPorEstatus(Integer idEmp, Date fecha, String clave)
    {
        DetRegistro model = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            model = em.createNamedQuery("DetRegistro.findByFechaEstatus", DetRegistro.class)
                .setParameter("idEmp", idEmp)
                .setParameter("fecha", fecha, TemporalType.TIMESTAMP)
                .setParameter("codigo", clave)
                .getSingleResult();
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el registro...", ex);
        } finally 
        {
            this.close(em);
        }
        
        return model;
    }
    
    public List<DetRegistro> buscarPorEmpleadoEstatus(Date fechaInicio, Date fechaFin, String codigo)
    {
        List<DetRegistro> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("DetRegistro.findByEstatus", DetRegistro.class)
                .setParameter("fechaInicio", fechaInicio)
                .setParameter("fechaFin", fechaFin)
                .setParameter("codigo", codigo)
                .getResultList();
            
            for (DetRegistro r : modelList) {
                log.debug("Registro - idEmpleado: {}", r.getEmpleado().getIdEmpleado());
                log.debug("Status registro: {}", r.getStatus().getIdEstatus());
            }
        }catch (Exception ex) 
        {
            log.error("Problema para obtener el registro...", ex);
        } finally 
        {
            this.close(em);
        }
        
        return modelList;
    }
    
    public List<DetRegistro> buscarPorEmpleadoPeriodo(Integer idEmpleado, Date periodoInicio, Date periodoFin) {
    	List<DetRegistro> modelList = null;
    	EntityManager em = null;
    	
    	try {
    		em = this.getEntityManager();
    		modelList = em.createNamedQuery("DetRegistro.findByIdEmpleadoPeriodo", modelClass)
    				.setParameter("idEmpleado", idEmpleado)
    				.setParameter("periodoInicio", periodoInicio)
    				.setParameter("periodoFin", periodoFin)
    				.getResultList();
    		
    	} catch(Exception ex) {
    		if(modelList == null)
    			modelList = new ArrayList<DetRegistro>();
    	} finally {
    		this.close(em);
    	}
    	
    	return modelList;
    }
    
}
