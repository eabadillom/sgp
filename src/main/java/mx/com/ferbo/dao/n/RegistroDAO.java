package mx.com.ferbo.dao.n;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
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
    
    public List<DetRegistro> buscarTodos()
    {
        List<DetRegistro> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("DetRegistro.findAll", DetRegistro.class)
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
            log.debug("IdEmpleado: {}", model.getIdEmpleado().getIdEmpleado());
            log.debug("Estatus: {}", model.getStatus().getIdEstatus());
        } catch (Exception ex) {
            model = null;
        } finally {
            close(emSGP);
        }

        return model;
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
                log.trace("Registro - idEmpleado: {}", r.getIdEmpleado().getIdEmpleado());
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
            em = this.getEntityManager();
            modelList = em.createNamedQuery("DetRegistro.findByPlantaPeriodo", DetRegistro.class)
                    .setParameter("idPlanta", idPlanta)
                    .setParameter("fechaInicio", fechaInicio)
                    .setParameter("fechaFin", fechaFin)
                    .getResultList();
            for (DetRegistro model : modelList) {
                cargaInfo(model);
            }
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
    		log.trace("IdEmpleado: {}", model.getIdEmpleado().getIdEmpleado());
    		log.trace("Id Planta: {}", model.getIdEmpleado().getDatoEmpresa().getPlanta().getIdPlanta());
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
    
    public DetRegistro buscarPorDia(Integer idEmp, Date inicioDiaActual, Date finDiaActual)
    {
        DetRegistro model = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            model = em.createNamedQuery("DetRegistro.findToday", DetRegistro.class)
                .setParameter("idEmp", idEmp)
                .setParameter("inicioDia", inicioDiaActual, TemporalType.TIMESTAMP)
                .setParameter("finDia", finDiaActual, TemporalType.TIMESTAMP)
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
    
    public List<DetRegistro> buscarPorEmpleadoEstatus(String clave)
    {
        List<DetRegistro> modelList = null;
        EntityManager em = null;
        
        try
        {
            em = this.getEntityManager();
            modelList = em.createNamedQuery("DetRegistro.findByEstatus", DetRegistro.class)
                .setParameter("codigo", clave)
                .getResultList();
            
            for (DetRegistro r : modelList) {
                log.debug("Registro - idEmpleado: {}", r.getIdEmpleado().getIdEmpleado());
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
    
}
