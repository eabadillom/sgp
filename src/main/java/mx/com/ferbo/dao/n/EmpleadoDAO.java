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
import mx.com.ferbo.model.DetPercepcionEmpleado;
import mx.com.ferbo.model.DetPrestamo;
import mx.com.ferbo.model.DetSalarioDiario;
import mx.com.ferbo.model.DetVacaciones;
import mx.com.ferbo.util.SGPException;

public class EmpleadoDAO extends BaseDAO<DetEmpleado, Integer> {

    private static Logger log = LogManager.getLogger(EmpleadoDAO.class);

    public EmpleadoDAO(Class<DetEmpleado> modelClass) {
        super(modelClass);
    }
    
	public EmpleadoDAO() {
		super(DetEmpleado.class);
	}

    public DetEmpleado buscarPorId(Integer id, boolean isFullInfo) {
        DetEmpleado model = null;
        EntityManager emSGP = null;

        try {
            emSGP = getEntityManager();
            model = emSGP.find(this.modelClass, id);
            if (isFullInfo) {
            	
            	for(DetSalarioDiario salarioDiario : model.getDatoEmpresa().getSalariosDiarios()) {
            		log.debug("Salario diario: {}", salarioDiario);
            	}
            	
                log.debug("id dato empresa: {}", model.getDatoEmpresa().getId() == null ? null : model.getDatoEmpresa().getId());
                if(model.getDatoEmpresa().getPerfil() == null)
                	log.debug("El empleado no tiene definido el objeto empresa.");
                else
                	log.debug("Id perfil: {}", model.getDatoEmpresa().getPerfil().getIdPerfil());
                
                if(model.getDatoEmpresa().getEmpresa() == null)
                	log.debug("El empleado no tiene asignada una empresa");
                else
                	log.debug("Id empresa: {}", model.getDatoEmpresa().getEmpresa().getIdEmpresa());
                
                if(model.getDatoEmpresa().getPlanta() == null)
                	log.debug("El empleado no tiene asignada una planta.");
                else
                	log.debug("Id planta: {}", model.getDatoEmpresa().getPlanta().getIdPlanta());
                
                if(model.getDatoEmpresa().getArea() == null)
                	log.debug("El empleado no tiene asignada una área laboral.");
                else
                	log.debug("Id area: {}", model.getDatoEmpresa().getArea().getIdArea());
                
                if(model.getDatoEmpresa().getPuesto() == null)
                	log.debug("El empleado no tiene asignado un puesto laboral.");
                else
                	log.debug("Id puesto: {}", model.getDatoEmpresa().getPuesto().getIdPuesto());
                
                if(model.getDatoEmpresa().getTipoContrato() == null)
                	log.debug("El empleado no tiene asignado un tipo de contrato.");
                else
                	log.debug("Id tipo contrato: {}", model.getDatoEmpresa().getTipoContrato().getClave());
                
                if(model.getDatoEmpresa().getTipoJornada() == null)
                	log.debug("El empleado no tiene asignado un tipo de jornada");
                else
                	log.debug("Id tipo jornada: {}", model.getDatoEmpresa().getTipoJornada().getClave());
                
                if(model.getDatoEmpresa().getTipoRegimen() == null)
                	log.debug("El empleado no tiene asignado un tipo de régimen.");
                else
                	log.debug("Id tipo régimen: {}", model.getDatoEmpresa().getTipoRegimen().getClave());
                
                if(model.getDatoEmpresa().getEntidadFederativa() == null)
                	log.debug("El empleado no tiene asignada una entidad federativa.");
                else
                	log.debug("Id entidad federativa: {}", model.getDatoEmpresa().getEntidadFederativa().getClave());
                
                if(model.getDatoEmpresa().getRiesgoPuesto() == null)
                	log.debug("El empleado no tiene asignado un riesgo de puesto laboral.");
                else
                	log.debug("Id riesgo puesto: {}", model.getDatoEmpresa().getRiesgoPuesto().getClave());
                
                if(model.getDatoEmpresa().getPeriodicidadPago() == null)
                	log.debug("El empleado no tiene asignada una periodicidad de pago.");
                else
                	log.debug("Id periodicidad pago: {}", model.getDatoEmpresa().getPeriodicidadPago().getPeriodicidad());
                
                if(model.getDatoEmpresa().getTipodebaja() == null)
                	log.debug("El empleado no tiene asignado un tipo de baja.");
                else
                	log.debug("Id tipo de baja: {}", model.getDatoEmpresa().getTipodebaja().getTipodebaja());
                
                if(model.getDatoEmpresa().getBanco() == null)
                	log.debug("El empleado no tiene asignado un banco.");
                else
                	log.debug("Id banco: {}", model.getDatoEmpresa().getBanco().getIdBanco());
                
                if(model.getDomicilio() == null)
                	log.debug("El empleado no tiene asignado un domicilio");
                else
                	log.debug("Id domicilio: {}", model.getDomicilio().getId());
                
                if(model.getDomicilio() != null && model.getDomicilio().getAsentamiento() == null) {
                	log.debug("El empleado no tiene asignada la información de su domicilio (asentamiento).");
                } else if(model.getDomicilio() != null && model.getDomicilio().getAsentamiento() != null) {
                	log.debug("Id Asentamiento: {}", model.getDomicilio().getAsentamiento().getKey().getId());
                	log.debug("Id Localidad: {}", model.getDomicilio().getAsentamiento().getKey().getLocalidad().getKey().getId());
                	log.debug("Id Municipio: {}", model.getDomicilio().getAsentamiento().getKey().getLocalidad().getKey().getMunicipio().getKey().getId());
                	log.debug("Id Estado: {}", model.getDomicilio().getAsentamiento().getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getId());
                	log.debug("Id Pais: {}", model.getDomicilio().getAsentamiento().getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getPais().getId());
                }
                
                for(DetVacaciones v : model.getVacaciones()) {
                	log.debug("Periodo Vacacional: {}", v.getIdVacaciones());
                	
                	v.getRegistroVacaciones().stream().forEach(item -> {
                		if(item.getRegistro() == null)
                			return;
                		if(!"V".equalsIgnoreCase(item.getRegistro().getStatus().getCodigo()))
                			return;
                		
                		log.debug("Id registro vacaciones: {}", item.getRegistro().getIdRegistro());
                	});
                }
                
                for(DetPercepcionEmpleado p : model.getPercepcionesEmpleado()) {
                	log.debug("Percepción del empleado: {}", p.getId());
                }
                
                for(DetPrestamo p : model.getPrestamos()) {
                	log.debug("Préstamo del empleado: {}", p.getIdPrestamo());
                }
                
                if(model.getEmpleadoConfiguracion() == null)
                	log.debug("El empleado no tiene asignado un objeto de configuración de nómina.");
                else
                	log.debug("Id Empleado configuración: {}", model.getEmpleadoConfiguracion().getIdEmpleadoConf());
            }

        } catch (Exception ex) {
            log.error("Problema para obtener el empleado con id " + id, ex);
        } finally {
            close(emSGP);
        }

        return model;
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
    
    public DetEmpleado buscarPorRFC(String rfc, boolean isFullInfo) {
    	DetEmpleado model = null;
    	EntityManager em = null;
    	try {
    		em = this.getEntityManager();
    		model = em.createNamedQuery("DetEmpleado.findByRFC", modelClass)
    				.setParameter("rfc", rfc)
    				.getSingleResult();
    		
    		if (isFullInfo) {
            	
                log.debug("id dato empresa: {}", model.getDatoEmpresa().getId() == null ? null : model.getDatoEmpresa().getId());
                if(model.getDatoEmpresa().getPerfil() == null)
                	log.debug("El empleado no tiene definido el objeto empresa.");
                else
                	log.debug("Id perfil: {}", model.getDatoEmpresa().getPerfil().getIdPerfil());
                
                if(model.getDatoEmpresa().getEmpresa() == null)
                	log.debug("El empleado no tiene asignada una empresa");
                else
                	log.debug("Id empresa: {}", model.getDatoEmpresa().getEmpresa().getIdEmpresa());
                
                if(model.getDatoEmpresa().getPlanta() == null)
                	log.debug("El empleado no tiene asignada una planta.");
                else
                	log.debug("Id planta: {}", model.getDatoEmpresa().getPlanta().getIdPlanta());
                
                if(model.getDatoEmpresa().getArea() == null)
                	log.debug("El empleado no tiene asignada una área laboral.");
                else
                	log.debug("Id area: {}", model.getDatoEmpresa().getArea().getIdArea());
                
                if(model.getDatoEmpresa().getPuesto() == null)
                	log.debug("El empleado no tiene asignado un puesto laboral.");
                else
                	log.debug("Id puesto: {}", model.getDatoEmpresa().getPuesto().getIdPuesto());
                
                if(model.getDatoEmpresa().getTipoContrato() == null)
                	log.debug("El empleado no tiene asignado un tipo de contrato.");
                else
                	log.debug("Id tipo contrato: {}", model.getDatoEmpresa().getTipoContrato().getClave());
                
                if(model.getDatoEmpresa().getTipoJornada() == null)
                	log.debug("El empleado no tiene asignado un tipo de jornada");
                else
                	log.debug("Id tipo jornada: {}", model.getDatoEmpresa().getTipoJornada().getClave());
                
                if(model.getDatoEmpresa().getTipoRegimen() == null)
                	log.debug("El empleado no tiene asignado un tipo de régimen.");
                else
                	log.debug("Id tipo régimen: {}", model.getDatoEmpresa().getTipoRegimen().getClave());
                
                if(model.getDatoEmpresa().getEntidadFederativa() == null)
                	log.debug("El empleado no tiene asignada una entidad federativa.");
                else
                	log.debug("Id entidad federativa: {}", model.getDatoEmpresa().getEntidadFederativa().getClave());
                
                if(model.getDatoEmpresa().getRiesgoPuesto() == null)
                	log.debug("El empleado no tiene asignado un riesgo de puesto laboral.");
                else
                	log.debug("Id riesgo puesto: {}", model.getDatoEmpresa().getRiesgoPuesto().getClave());
                
                if(model.getDatoEmpresa().getPeriodicidadPago() == null)
                	log.debug("El empleado no tiene asignada una periodicidad de pago.");
                else
                	log.debug("Id periodicidad pago: {}", model.getDatoEmpresa().getPeriodicidadPago().getPeriodicidad());
                
                if(model.getDatoEmpresa().getTipodebaja() == null)
                	log.debug("El empleado no tiene asignado un tipo de baja.");
                else
                	log.debug("Id tipo de baja: {}", model.getDatoEmpresa().getTipodebaja().getTipodebaja());
                
                if(model.getDatoEmpresa().getBanco() == null)
                	log.debug("El empleado no tiene asignado un banco.");
                else
                	log.debug("Id banco: {}", model.getDatoEmpresa().getBanco().getIdBanco());
                
                if(model.getDomicilio() == null)
                	log.debug("El empleado no tiene asignado un domicilio");
                else
                	log.debug("Id domicilio: {}", model.getDomicilio().getId());
                
                if(model.getDomicilio() != null && model.getDomicilio().getAsentamiento() == null) {
                	log.debug("El empleado no tiene asignada la información de su domicilio (asentamiento).");
                } else if(model.getDomicilio() != null && model.getDomicilio().getAsentamiento() != null) {
                	log.debug("Id Asentamiento: {}", model.getDomicilio().getAsentamiento().getKey().getId());
                	log.debug("Id Localidad: {}", model.getDomicilio().getAsentamiento().getKey().getLocalidad().getKey().getId());
                	log.debug("Id Municipio: {}", model.getDomicilio().getAsentamiento().getKey().getLocalidad().getKey().getMunicipio().getKey().getId());
                	log.debug("Id Estado: {}", model.getDomicilio().getAsentamiento().getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getId());
                	log.debug("Id Pais: {}", model.getDomicilio().getAsentamiento().getKey().getLocalidad().getKey().getMunicipio().getKey().getEstado().getKey().getPais().getId());
                }
                
                for(DetVacaciones v : model.getVacaciones()) {
                	log.debug("Periodo Vacacional: {}", v.getIdVacaciones());
                }
                
                for(DetPercepcionEmpleado p : model.getPercepcionesEmpleado()) {
                	log.debug("Percepción del empleado: {}", p.getId());
                }
                
                for(DetPrestamo p : model.getPrestamos()) {
                	log.debug("Préstamo del empleado: {}", p.getIdPrestamo());
                }
                
                if(model.getEmpleadoConfiguracion() == null)
                	log.debug("El empleado no tiene asignado un objeto de configuración de nómina.");
                else
                	log.debug("Id Empleado configuración: {}", model.getEmpleadoConfiguracion().getIdEmpleadoConf());
            }

    	} catch(Exception ex) {
    		log.error("Problema para obtener el empleado por RFC: " + rfc, ex);
    	} finally {
    		this.close(em);
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
    
    public List<DetEmpleado> buscarTodosActivos(Short activo)
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
    
    public List<DetEmpleado> buscarActivos(Integer idEmpresa, Integer idPlanta, Date fecha) {
    	List<DetEmpleado> modelList = null;
    	EntityManager em = null;
    	
    	try {
    		em = this.getEntityManager();
    		modelList = em.createNamedQuery("DetEmpleado.findByActiveEmpresaPlanta", this.modelClass)
    				.setParameter("idEmpresa", idEmpresa)
    				.setParameter("idPlanta", idPlanta)
    				.setParameter("fecha", fecha)
    				.getResultList()
    				;
    		
    	} catch(Exception ex) {
    		log.error("Problema para obtener la lista de empleados...", ex);
    	} finally {
    		this.close(em);
    	}
    	
    	return modelList;
    }
    
    public List<DetEmpleado> buscarActivosConSalarioDiario(Integer idEmpresa, Integer idPlanta, Date fecha) {
    	List<DetEmpleado> modelList = null;
    	EntityManager em = null;
    	
    	try {
    		em = this.getEntityManager();
    		modelList = em.createNamedQuery("DetEmpleado.findByActiveEmpresaPlanta", this.modelClass)
    				.setParameter("idEmpresa", idEmpresa)
    				.setParameter("idPlanta", idPlanta)
    				.setParameter("fecha", fecha)
    				.getResultList()
    				;
    		
    		for(DetEmpleado model : modelList) {
    			model.getDatoEmpresa().getSalariosDiarios().stream().forEach(sd -> log.debug("Salario diario: {}", sd.getId()));
    			log.debug("Zona: {}", model.getDatoEmpresa().getZona().getClave());
    		}
    	} catch(Exception ex) {
    		log.error("Problema para obtener la lista de empleados...", ex);
    	} finally {
    		this.close(em);
    	}
    	
    	return modelList;
    }

    public DetEmpleadoFoto buscarFoto(String numeroEmpleado) {
        DetEmpleadoFoto foto = null;
        return foto;
    }
    
    public synchronized List<DetEmpleado> empleadosSalarioMinimoGeneral(BigDecimal salarioMinimo, Date hoy) throws SGPException{
        List<DetEmpleado> empleados = null;
        EntityManager em = null;
        
        try{
            log.info("Inicio el proceso de obtener los empleados por debajo del salario minimo de ZG");
            em = getEntityManager();
            TypedQuery<DetEmpleado> resultado = em.createQuery("select e from DetEmpleado e where e.datoEmpresa.salarioDiario < :salarioMinimo and (e.datoEmpresa.fechaBaja is null or e.datoEmpresa.fechaBaja > :hoy)", DetEmpleado.class);
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
            TypedQuery<DetEmpleado> resultado = em.createQuery("select e from DetEmpleado e where e.datoEmpresa.salarioDiario < :salarioMinimo and (e.datoEmpresa.fechaBaja is null or e.datoEmpresa.fechaBaja > :hoy)", DetEmpleado.class);
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
    
    public List<DetEmpleado> buscarPorCURP(String curp) {
    	List<DetEmpleado> registrosEmpleado = null;
    	EntityManager em = null;
    	
    	try {
    		em = this.getEntityManager();
    		
    		registrosEmpleado = em.createNamedQuery("DetEmpleado.findByCURP", modelClass)
    				.setParameter("curp", curp)
    				.getResultList()
    				;
    		
    	} catch(Exception ex) {
    		log.error("Problema para obtener la lista de registros del empleado con curp {}...\n{}", curp, ex);
    	} finally {
    		this.close(em);
    	}
    	
    	return registrosEmpleado;
    }
    
    public List<DetEmpleado> buscarPorNombrePrimerSegundoApellido(String query) {
    	List<DetEmpleado> modelList = null;
    	EntityManager em = null;
    	
    	try {
    		em = this.getEntityManager();
    		modelList = em.createNamedQuery("DetEmpleado.fullNameContains", modelClass)
    				.setParameter("query", String.format("%%%s%%", query))
    				.getResultList();
    		
    		modelList.stream().forEach(item -> log.debug("Id dato empresa: {}", item.getDatoEmpresa().getId()));
    		log.info("Longitud de la lista de empleados: {}", modelList.size());
    	} catch(Exception ex) {
    		log.error("Problema para obtener la lista de empleados con la palabra {}, \n{}", query, ex);
    	} finally {
    		this.close(em);
    	}
    	
    	return modelList;
    }
}
