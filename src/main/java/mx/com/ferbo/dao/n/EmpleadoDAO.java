package mx.com.ferbo.dao.n;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetEmpleadoFoto;
import mx.com.ferbo.util.SGPException;

public class EmpleadoDAO extends BaseDAO<DetEmpleado, Integer> {

    private static Logger log = LogManager.getLogger(EmpleadoDAO.class);

    public EmpleadoDAO(Class<DetEmpleado> modelClass) {
        super(modelClass);
    }
    
    public EmpleadoDAO()
    {
        super(DetEmpleado.class);
    }

    public DetEmpleado buscarPorId(Integer id, boolean isFullInfo, boolean isGetFoto) {
        DetEmpleado model = null;
        EntityManager emSGP = null;

        try {
            emSGP = getEntityManager();
            model = emSGP.find(this.modelClass, id);
            if (isFullInfo) {
                log.info("id dato empresa: {}", model.getDatoEmpresa().getId() == null ? null : model.getDatoEmpresa().getId());
            }

            if (isGetFoto) {
                log.info("id foto: {}", model.getEmpleadoFoto() == null ? null : model.getEmpleadoFoto().getId());
            }
        } catch (Exception ex) {
            log.error("Problema para obtener el empleado con id " + id, ex);
        } finally {
            close(emSGP);
        }

        return model;
    }

    public DetEmpleado buscarPorNumeroEmpleado(String numeroEmpleado, boolean isFullInfo) {
        DetEmpleado model = null;
        EntityManager emSGP = null;
        try {
            emSGP = this.getEntityManager();
            model = emSGP.createNamedQuery("DetEmpleado.findByNumero", modelClass)
                    .setParameter("numero", numeroEmpleado)
                    .getSingleResult();
            if (isFullInfo) {
                log.info("Dato empresa: {}", model.getDatoEmpresa().getId());
                log.info("Perfil: {}", model.getDatoEmpresa().getPerfil().getIdPerfil());
            }
        } catch (Exception ex) {
            log.error("Problema para obtener el número de empleado " + numeroEmpleado, ex);
        } finally {
            close(emSGP);
        }

        return model;
    }

    public DetEmpleado buscarPorNumeroEmpleadoFoto(String numeroEmpleado, boolean isFullInfo) {
        DetEmpleado model = null;
        EntityManager emSGP = null;
        try {
            emSGP = this.getEntityManager();
            model = emSGP.createNamedQuery("DetEmpleado.findByNumero", modelClass)
                    .setParameter("numero", numeroEmpleado)
                    .getSingleResult();
            if (isFullInfo) {
                log.info("Dato empresa: {}", model.getDatoEmpresa().getId());
                log.info("Perfil: {}", model.getDatoEmpresa().getPerfil().getIdPerfil());
                log.info("Foto id: {}", model.getEmpleadoFoto().getId());
            }
        } catch (Exception ex) {
            log.error("Problema para obtener el número de empleado " + numeroEmpleado, ex);
        } finally {
            close(emSGP);
        }

        return model;
    }

    public List<DetEmpleado> buscarActivoEmpresaIngreso(Integer idEmpresa, Date periodoPagoInicio, Date periodoPagoFin) {
        List<DetEmpleado> modelList = null;
        EntityManager emSGP = null;

        try {
            emSGP = this.getEntityManager();

            modelList = emSGP.createNamedQuery("DetEmpleado.findByActiveEmpresaIngreso", modelClass)
                    .setParameter("idEmpresa", idEmpresa)
                    .setParameter("periodoPagoInicio", periodoPagoInicio)
                    .setParameter("periodoPagoFin", periodoPagoFin)
                    .getResultList()
                    ;

        } catch (Exception ex) {
            log.warn("Problema para obtener la lista de empleados...", ex);
        } finally {
            close(emSGP);

        }

        return modelList;
    }

    public List<DetEmpleado> buscarTodos(boolean isFullInfo) {
        List<DetEmpleado> modelList = null;
        EntityManager emSGP = null;

        try {
            emSGP = getEntityManager();
            modelList = emSGP.createNamedQuery("DetEmpleado.getAll", DetEmpleado.class)
                    .getResultList();
            
            for (DetEmpleado model : modelList) {
                if (isFullInfo == false) {
                    continue;
                }

                log.info("id area: {}", model.getDatoEmpresa().getArea().getIdArea());
                log.info("id dato empresa: {}", model.getDatoEmpresa());
            }
        } catch (Exception ex) {
            log.error("Problema para obtener el listado de empleados...", ex);
        } finally {
            close(emSGP);
        }

        return modelList;
    }
    
    public List<DetEmpleado> buscarTodosActivos(String activo)
    {
        List<DetEmpleado> modelList = null;
        EntityManager emSGP = null;
        
        try
        {
            emSGP = getEntityManager();
            modelList = emSGP.createNamedQuery("DetEmpleado.getActive", DetEmpleado.class)
                .setParameter("activo", activo)
                .getResultList();
        }catch(Exception ex)
        {
            log.error("Problema para obtener el listado de empleados activos...", ex);
        }finally
        {
            close(emSGP);
        }
        
        return modelList;
    }

    public DetEmpleadoFoto buscarFoto(String numeroEmpleado) {
        DetEmpleadoFoto foto = null;
        return foto;
    }
    
    public DetEmpleado buscarPorRFC(String rfc) {
    	DetEmpleado model = null;
    	EntityManager em = null;
    	try {
    		em = this.getEntityManager();
    		model = em.createNamedQuery("DetEmpleado.findByRFC", modelClass)
    				.setParameter("rfc", rfc)
    				.getSingleResult();

    	} catch(Exception ex) {
    		log.error("Problema para obtener el empleado por RFC: " + rfc, ex);
    	} finally {
    		this.close(em);
    	}
    	
    	return model;
    }

    public synchronized List<DetEmpleado> empleadosSalarioMinimoGeneral(BigDecimal salarioMinimo, Date hoy) throws SGPException{
        List<DetEmpleado> empleados = null;
        EntityManager em = null;
        
        try{
            log.info("Inicio el proceso de obtener los empleados por debajo del salario minimo de ZG");
            em = getEntityManager();
            TypedQuery resultado = em.createQuery("select e from DetEmpleado e where e.datoEmpresa.salarioDiario < :salarioMinimo and (e.datoEmpresa.fechaBaja is null or e.datoEmpresa.fechaBaja > :hoy)", DetEmpleado.class);
            resultado.setParameter("salarioMinimo", salarioMinimo);
            resultado.setParameter("hoy", hoy);
            empleados = resultado.getResultList();
            log.info("Finaliza el proceso de obtener los empleados por debajo del salario minimo de ZG");
        }
        catch(Exception ex){
            log.warn("Hubo algun problema al momento de obtener los empleados por debajo del salario minimo de ZG. " + ex.getMessage());
            rollback(em);
            throw new SGPException("Hubo algun problema al momento de obtener los empleados por debajo del salario minimo de ZG");
        }
        finally{
            close(em);
        }
        
        return empleados;
    }
    
    public synchronized List<DetEmpleado> empleadosSalarioMinimoFrontera(BigDecimal salarioMinimo, Date hoy) throws SGPException{
        List<DetEmpleado> empleados = null;
        EntityManager em = null;
        
        try{
            log.info("Inicio el proceso de obtener los empleados por debajo del salario minimo de ZF");
            em = getEntityManager();
            TypedQuery resultado = em.createQuery("select e from DetEmpleado e where e.datoEmpresa.salarioDiario < :salarioMinimo and (e.datoEmpresa.fechaBaja is null or e.datoEmpresa.fechaBaja > :hoy)", DetEmpleado.class);
            resultado.setParameter("salarioMinimo", salarioMinimo);
            resultado.setParameter("hoy", hoy);
            empleados = resultado.getResultList();
            log.info("Finaliza el proceso de obtener los empleados por debajo del salario minimo de ZF");
        }
        catch(Exception ex){
            log.warn("Hubo algun problema al momento de obtener los empleados por debajo del salario minimo de ZF");
            rollback(em);
            throw new SGPException("Hubo algun problema al momento de obtener los empleados por debajo del salario minimo de ZF");
        }
        finally{
            close(em);
        }
        
        return empleados;
    }
    
    public DetEmpleado obetenerVacacionesEmpleado(Integer id) {
        DetEmpleado model = null;
        EntityManager emSGP = null;

        try {
            emSGP = getEntityManager();
            model = emSGP.find(this.modelClass, id);
                log.info("Vacaciones: {}", model.getVacaciones() == null ? null : model.getVacaciones());
            
        } catch (Exception ex) {
            log.error("Problema para obtener vacaciones del empleado con id " + id, ex);
        } finally {
            close(emSGP);
        }

        return model;
    }
}
