package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import mx.com.ferbo.business.dianolaboral.DiasNoLaboralesBL;
import mx.com.ferbo.business.incidencia.SolicitudPermisoBL;
import mx.com.ferbo.business.registro.EstatusRegistroBL;
import mx.com.ferbo.business.registro.RegistroBL;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.EstatusRegistroDAO;
import mx.com.ferbo.dao.n.PlantaDAO;
import mx.com.ferbo.dao.n.RegistroDAO;
import mx.com.ferbo.model.CatEstatusRegistro;
import mx.com.ferbo.model.CatPlanta;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.reports.AsistenciaRBL;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.SGPException;
import mx.com.ferbo.util.SGPSecurityException;

@Named(value = "repAsistenciaBean")
@ViewScoped
public class RepAsistenciaBean implements Serializable {

    private static final long serialVersionUID = -1127550691400298355L;
    private static Logger log = LogManager.getLogger(RepAsistenciaBean.class);
    
    private HttpServletRequest request = null;
    private FacesContext context = null;
//    private String contextPath = null;
    private HttpSession session = null;

    private PlantaDAO plantaDAO;
    private RegistroDAO registroDAO;
    private EstatusRegistroDAO statusRegistroDAO;
    private EmpleadoDAO empleadoDAO;
    private DetEmpleado empleado;
    private List<CatPlanta> plantas;
    private List<DetRegistro> registros;
    private List<Date> registrosEmpleado;

    private CatPlanta planta = null;
    private Date fechaInicio;
    private Date fechaFin;

    private StreamedContent pdfFile;
    private StreamedContent xlsFile;

    private String badgeColor;
    private DetRegistro registroSelected;
    private List<CatEstatusRegistro> lstEstatus;
    private List<Date> diasDeshabilitados;
    
    private Boolean desbloquearDiasDeDescanso;
    private Boolean desbloquearDiasNoLaborales;
    private List<Date> diasNoLaborales;
    private List<Integer> invalidDays;

    public RepAsistenciaBean() throws SGPSecurityException {
    	DetEmpleado empleadoSesion = null;
    	
    	this.context = FacesContext.getCurrentInstance();
    	this.request = (HttpServletRequest) context.getExternalContext().getRequest();
    	this.session = request.getSession(false);
    	
    	CatEstatusRegistro status = null;
    	
    	try {
    		log.info("Ejecutando constructor...");
    		empleadoSesion = (DetEmpleado) session.getAttribute("empleado");
    		
    		if(empleadoSesion.getDatoEmpresa().getPerfil().getIdPerfil() != 1)
    			throw new SGPSecurityException("No tiene acceso a este recurso.");
    		
    		
    		plantaDAO = new PlantaDAO();
    		registroDAO = new RegistroDAO();
    		statusRegistroDAO = new EstatusRegistroDAO();
    		empleadoDAO = new EmpleadoDAO();
    		
    		plantas = plantaDAO.buscarTodos();
    		lstEstatus = new ArrayList<CatEstatusRegistro>();
    		status = statusRegistroDAO.buscarPorCodigo("T");
    		lstEstatus.add(status);
    		status = statusRegistroDAO.buscarPorCodigo("R");
    		lstEstatus.add(status);
    		status = statusRegistroDAO.buscarPorCodigo("F");
    		statusRegistroDAO.buscarPorCodigo("J");
    		lstEstatus.add(status);
    		status = statusRegistroDAO.buscarPorCodigo("X");
    		lstEstatus.add(status);
    		status = statusRegistroDAO.buscarPorCodigo("J");
    		lstEstatus.add(status);
    		
    		this.fechaInicio = new Date();
    		this.fechaFin = new Date();
    		this.badgeColor = "";
    		
    		byte[] bytes = {};
    		
    		pdfFile = DefaultStreamedContent.builder().contentType("application/pdf").contentLength(bytes.length)
    				.name("ReporteAsistencia.pdf").stream(() -> new ByteArrayInputStream(bytes)).build();
    		
    		xlsFile = DefaultStreamedContent.builder().contentType("application/vnd.ms-excel").contentLength(bytes.length)
    				.name("ReporteAsistencia.xlsx").stream(() -> new ByteArrayInputStream(bytes)).build();
    		
    		log.info("Termina constructor.");
    	} catch(SGPSecurityException ex) {
    		throw ex;
    	} catch(Exception ex) {
    		log.error("Problema para inicializar el reporte de asistencia...", ex);
    	}
    }

    @PostConstruct
    public void init() {
    	DetEmpleado empleadoSesion = null;
    	empleadoSesion = (DetEmpleado) session.getAttribute("empleado");
    	
    	try {
    		log.info("Ejecutando proceso init...");
    		
    		if(empleadoSesion.getDatoEmpresa().getPerfil().getIdPerfil() == 1) {
    			return;
    		}
    		
    	} catch(Exception ex) {
    		log.error("Problema para entrar al reporte de inventario...", ex);
    	}
    }
    
    public void cargaInfo() {
        FacesMessage message = null;
        Severity severity = null;
        String titulo = "Reporte";
        String mensaje = null;

        Integer idPlanta = null;

        try {
            DateUtil.setTime(fechaInicio, 0, 0, 0, 0);
            DateUtil.setTime(fechaFin, 23, 59, 59, 0);

            log.info("Cargando información de la planta: {}", this.planta);
            log.info("Inicio del periodo de búsqueda: {}", this.fechaInicio);
            log.info("Fin del periodo de búsqueda: {}", this.fechaFin);
            if (fechaInicio.after(fechaFin)) {
                throw new SGPException("La fecha inicio del periodo de búsqueda no puede ser posterior a la de fin de periodo.");
            }

            idPlanta = this.planta == null ? null : this.planta.getIdPlanta();

            registros = registroDAO.buscarPorPlantaPeriodo(idPlanta, this.fechaInicio, this.fechaFin);

            log.info("{}  registro(s) encontrado(s)", registros.size());
            
            this.nuevoRegistro();

        } catch (SGPException ex) {
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
        } catch (Exception ex) {
            mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente. Si el problema persiste, por favor comuniquese con su administrador del sistema.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            if (mensaje != null) {
                message = new FacesMessage(severity, titulo, mensaje);
                FacesContext.getCurrentInstance().addMessage(null, message);
                PrimeFaces.current().ajax().update(":form:messages :form:cmd-exportar-pdf");
            }
        }
    }
    
    public void ajustaHoras() {
    	try {
    		log.info("Ajustando inicio y fin del periodo...");
    		DateUtil.setTime(fechaInicio, 0, 0, 0, 0);
    		DateUtil.setTime(fechaFin, 23, 59, 59, 0);
    	} catch(Exception ex) {
    		log.error("Problema para establecer la hora inicio y fin del periodo...", ex);
    	}
    }
    
    public void nuevoRegistro() {
    	this.registroSelected = new DetRegistro();
    	this.registroSelected.setStatus(EstatusRegistroBL.estatusJustificado());
    	this.empleado = null;
    }
    
    public List<DetEmpleado> buscarEmpleado(String query) {
    	List<DetEmpleado> empleados = null;
    	
    	try {
    		if(query == null)
    			return new ArrayList<DetEmpleado>();
    		
    		if(query.trim().equals(""))
    			return new ArrayList<DetEmpleado>();
    		
    		log.info("Buscando empleados por: {}", query);
    		empleados = empleadoDAO.buscarPorNombrePrimerSegundoApellido(query, this.fechaFin);
    	} catch(Exception ex) {
    		empleados = new ArrayList<DetEmpleado>();
    	}
    	return empleados;
    }
    
    public void asignarEmpleado() {
    	List<DetRegistro> registrosEmp = null;
        try {
        	this.registroSelected.setEmpleado(this.empleado);
        	
        	registrosEmp = RegistroBL.buscarPorEmpleadoPeriodo(this.empleado.getIdEmpleado(), this.fechaInicio, this.fechaFin);
        	this.registrosEmpleado = RegistroBL.toDateList(registrosEmp);
        	
        } catch(Exception ex) {
        	log.warn("Problema para asignar al empleado: {}", ex.getMessage());
        	this.registrosEmpleado = new ArrayList<Date>();
        } finally {
        	PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void mostrarDiasDeDescanso() {
		if(this.desbloquearDiasDeDescanso.booleanValue()) {
			this.invalidDays = new ArrayList<Integer>();
		} else {
			this.invalidDays = SolicitudPermisoBL.obtenerDiasSeleccionados(this.empleado.getDatoEmpresa());
		}
	}
    
    public void mostrarDiasLaborales() {
		if(this.desbloquearDiasNoLaborales.booleanValue()) {
			this.diasNoLaborales = new ArrayList<Date>();
		} else {
			this.diasNoLaborales = DiasNoLaboralesBL.buscarPorPeriodo(new Date(fechaInicio.getTime()), new Date(fechaFin.getTime()));
		}
	}
    
    public void asignarHoraAsistencia() {
    	FacesMessage message = null;
        Severity severity = null;
        String titulo = "Problema con el registro";
        String mensaje = null;
    	
    	try {
    		log.info("Asignando hora de entrada / salida para el día {}...", this.registroSelected.getFecha());
    		
    		if(this.empleado == null)
    			throw new SGPException("Debe indicar un empleado");
    		
    		if(RegistroBL.buscar(this.empleado.getIdEmpleado(), this.registroSelected.getFecha()).isPresent())
    			throw new SGPException("El empleado ya cuenta con un registro de asistencia en la fecha indicada.");
    		
        	this.registroSelected.setFechaEntrada(this.empleado.getDatoEmpresa().getHoraEntrada());
        	this.registroSelected.setFechaSalida(this.empleado.getDatoEmpresa().getHorasalida());
        	
        	log.info("Asistencia: {}, {}", this.registroSelected.getFechaEntrada(), this.registroSelected.getFechaSalida());
    	} catch(SGPException ex) {
    		log.warn("Problema para asignar el horario de entrada / salida del empleado: {}", ex.getMessage());
    		severity = FacesMessage.SEVERITY_WARN;
    		mensaje = ex.getMessage();
    		message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
    	} catch(Exception ex) {
    		log.error("Problema para asignar el horario de entrada / salida del empleado: {}", ex);
    		severity = FacesMessage.SEVERITY_ERROR;
    		mensaje = "Ocurrió un problema con la asignación de horarios de entrada y salida.";
    		message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
    	} finally {
    		PrimeFaces.current().ajax().update(":form:messages");
    	}
    }

    public void editarRegistro(DetRegistro registro) {
    	List<DetRegistro> registrosEmp = null;
    	final Date diaPermitido;
    	
    	try {
    		log.info("Cargando información de registro...");
    		this.registroSelected = registro;
    		this.empleado = this.registroSelected.getEmpleado();
    		
    		this.desbloquearDiasDeDescanso = new Boolean(false);
			this.desbloquearDiasNoLaborales = new Boolean(false);
			
			this.mostrarDiasDeDescanso();
			this.mostrarDiasLaborales();
    		
    		int anio = DateUtil.getAnio(registro.getFechaEntrada());
    		int mes = DateUtil.getMes(registro.getFechaEntrada());
    		int dia = DateUtil.getDia(registro.getFechaEntrada());
    		Date fecha = new Date(this.registroSelected.getFechaEntrada().getTime());
    		DateUtil.setTime(fecha, 0, 0, 0, 0);
    		this.registroSelected.setFecha(fecha);
    		
    		DateUtil.setTime(this.registroSelected.getFecha(), 0, 0, 0, 0);
    		
    		diaPermitido = this.registroSelected.getFecha();
    		this.diasDeshabilitados = new ArrayList<Date>();
    		
    		registrosEmp = RegistroBL.buscarPorEmpleadoPeriodo(this.registroSelected.getEmpleado().getIdEmpleado(), this.fechaInicio, this.fechaFin);
        	this.registrosEmpleado = RegistroBL.toDateList(registrosEmp);
        	this.registrosEmpleado.forEach(t -> DateUtil.setTime(t, 0, 0, 0, 0));
    		
    		this.registrosEmpleado.remove(diaPermitido);
    		this.diasDeshabilitados.addAll(this.registrosEmpleado);
    		this.diasDeshabilitados.addAll(this.diasNoLaborales);
    		
    		if (registro.getStatus().getDescripcion().equals("Falta")) {
    			int horaSalida = DateUtil.getHora(registro.getEmpleado().getDatoEmpresa().getHorasalida());
    			Date diaSalida = DateUtil.getDateTime(anio, mes, dia, horaSalida, 0, 0, 0);
    			registro.setFechaSalida(diaSalida);
    		}
    		log.info("Terminando de cargar la información del registro.");
    	} catch(Exception ex) {
    		log.error("Problema para editar el registro de asistencia...", ex);
    	}
    }
    
    public void guardar() {
    	if(this.registroSelected.getIdRegistro() == null)
    		this.guardarRegistro();
    	else
    		this.actualizarRegistro();
    }

    public void actualizarRegistro() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Actualizar registro";
        
        Date entrada = null;
        Date salida = null;
        
        try {
        	log.info("Actualizando registro...");
        	entrada = new Date(this.registroSelected.getFecha().getTime());
        	DateUtil.setTime(entrada,
        			DateUtil.getHora(this.registroSelected.getFechaEntrada()),
        			DateUtil.getMinuto(this.registroSelected.getFechaEntrada()),
        			DateUtil.getSegundo(this.registroSelected.getFechaEntrada()),
        			0);
        	
        	salida = new Date(this.registroSelected.getFecha().getTime());
        	DateUtil.setTime(salida,
        			DateUtil.getHora(this.registroSelected.getFechaSalida()),
        			DateUtil.getMinuto(this.registroSelected.getFechaSalida()),
        			DateUtil.getSegundo(this.registroSelected.getFechaSalida()),
        			0);
        	
        	this.registroSelected.setFechaEntrada(entrada);
        	this.registroSelected.setFechaSalida(salida);
        	
            if ("F".equalsIgnoreCase(this.registroSelected.getStatus().getCodigo())) {
                int anio = DateUtil.getAnio(this.registroSelected.getFechaEntrada());
                int mes = DateUtil.getMes(this.registroSelected.getFechaEntrada());
                int dia = DateUtil.getDia(this.registroSelected.getFechaEntrada());
                entrada = DateUtil.getDateTime(anio, mes, dia, 0, 0, 0, 0);
                this.registroSelected.setFechaEntrada(entrada);
                this.registroSelected.setFechaSalida(entrada);
            }

            registroDAO.actualizar(this.registroSelected);
            mensaje = "El registro de actualizo correctamente.";
            severity = FacesMessage.SEVERITY_INFO;
            PrimeFaces.current().executeScript("PF('dgEditarRegistro').hide();");
        } catch (SGPException sgpEx) {
            log.error("Ocurrió un problema al actualizar el registro de asistencia...", sgpEx);
            mensaje = "No se actualizo el registro.";
            severity = FacesMessage.SEVERITY_WARN;

        } catch (Exception ex) {
            log.error("Ocurrió un error inesperado al actualizar el registro de asistencia...", ex);
            mensaje = "No se actualizo el registro. Favor de contactar con el admistrador del sistema.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void guardarRegistro() {
    	FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Guardar registro";
        Date entrada = null;
        Date salida = null;
        
        try {
        	entrada = new Date(this.registroSelected.getFecha().getTime());
        	salida = new Date(this.registroSelected.getFecha().getTime());
        	
        	DateUtil.setTime(entrada,
        			DateUtil.getHora(this.registroSelected.getFechaEntrada()),
        			DateUtil.getMinuto(this.registroSelected.getFechaEntrada()),
        			DateUtil.getSegundo(this.registroSelected.getFechaEntrada()),
        			0);
        	
        	DateUtil.setTime(salida,
        			DateUtil.getHora(this.registroSelected.getFechaSalida()),
        			DateUtil.getMinuto(this.registroSelected.getFechaSalida()),
        			DateUtil.getSegundo(this.registroSelected.getFechaSalida()),
        			0);
        	
        	this.registroSelected.setFechaEntrada(entrada);
        	this.registroSelected.setFechaSalida(salida);
        	
        	new RegistroBL().guardar(this.registroSelected);
        	this.registros.add(registroSelected);
        	
        	mensaje = "La información se guardó correctamente.";
            severity = FacesMessage.SEVERITY_INFO;
            PrimeFaces.current().executeScript("PF('dgEditarRegistro').hide();");
        } catch(SGPException ex) {
        	log.warn("Problema al guardar la información del registro: {}", ex.getMessage());
        	mensaje = ex.getMessage();
        	severity = FacesMessage.SEVERITY_WARN;
        } catch(Exception ex) {
        	mensaje = "No es posible guardar el registro de asistencia.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:dt-registros");
        }
        
        
    }
    
    public String obtenerEstatusAsistencia(DetRegistro registro) {
    	try {
    		switch (registro.getStatus().getCodigo()) {
    		
    		case "T":
    		case "J":
    		case "V":
    		case "P":
    		case "X":
    		case "I":
    		case "D":
    			this.badgeColor = new String();
    			this.badgeColor = "success";
    			break;
    			
    		case "R":
    			this.badgeColor = new String();
    			this.badgeColor = "warning";
    			break;
    			
    		case "F":
    			this.badgeColor = new String();
    			this.badgeColor = "danger";
    			break;
    		}
    	} catch(Exception ex) {
    		log.error("Problema para determinar el status del registro de asistencia...", ex);
        }

        return registro.getStatus().getDescripcion();
    }
    
    public void exportarPDF() {
        String message = null;
        Severity severity = null;
        
        Date fechaFinT = null;
        String fileName = null;
        Integer idPlanta = null;
        
        try {
            DateUtil.setTime(fechaInicio, 0, 0, 0, 0);
            DateUtil.setTime(fechaFin, 23, 59, 59, 0);
            log.info("Inicio del periodo de búsqueda: {}", this.fechaInicio);
            log.info("Fin del periodo de búsqueda: {}", this.fechaFin);

            fechaFinT = new Date(this.fechaFin.getTime());
            fechaFinT = DateUtil.addDay(fechaFinT, 1);
            idPlanta = this.planta == null ? null : this.planta.getIdPlanta();
            
            fileName = String.format("ReporteAsistencia_%s_al_%s.pdf",
            		DateUtil.getString(fechaInicio, DateUtil.FORMATO_YYYY_MM_DD),
            		DateUtil.getString(fechaFinT, DateUtil.FORMATO_YYYY_MM_DD));
            
            pdfFile = FacesUtils.getPDF(fileName, new AsistenciaRBL().getPDF(idPlanta, this.fechaInicio, fechaFinT));
            
            log.info("Exportación completa.");
        } catch (Exception e) {
            log.error("Ocurrió un problema al imprimir el reporte de asistencia...", e);
            message = String.format("No es posible exportar el reporte.");
            severity = FacesMessage.SEVERITY_INFO;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public void exportarXLS() {
    	String message = null;
        Severity severity = null;
        
        Date fechaFinT = null;
        String fileName = null;
        Integer idPlanta = null;
        
        try {
            DateUtil.setTime(fechaInicio, 0, 0, 0, 0);
            DateUtil.setTime(fechaFin, 23, 59, 59, 0);
            log.info("Inicio del periodo de búsqueda: {}", this.fechaInicio);
            log.info("Fin del periodo de búsqueda: {}", this.fechaFin);

            fechaFinT = new Date(this.fechaFin.getTime());
            fechaFinT = DateUtil.addDay(fechaFinT, 1);
            idPlanta = this.planta == null ? null : this.planta.getIdPlanta();
            
            fileName = String.format("ReporteAsistencia_%s_al_%s.xlsx",
            		DateUtil.getString(fechaInicio, DateUtil.FORMATO_YYYY_MM_DD),
            		DateUtil.getString(fechaFinT, DateUtil.FORMATO_YYYY_MM_DD));
            
            xlsFile = FacesUtils.getXLSX(fileName, new AsistenciaRBL().getXLSX(idPlanta, this.fechaInicio, fechaFinT));
            
            log.info("Exportación completa.");
        } catch (Exception e) {
            log.error("Ocurrió un problema al imprimir el reporte de asistencia...", e);
            message = String.format("No es posible exportar el reporte.");
            severity = FacesMessage.SEVERITY_INFO;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public List<CatPlanta> getPlantas() {
        return plantas;
    }

    public void setPlantas(List<CatPlanta> plantas) {
        this.plantas = plantas;
    }

    public CatPlanta getPlanta() {
        return planta;
    }

    public void setPlanta(CatPlanta planta) {
        this.planta = planta;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public List<DetRegistro> getRegistros() {
        return registros;
    }

    public void setRegistros(List<DetRegistro> registros) {
        this.registros = registros;
    }

    public StreamedContent getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(StreamedContent pdfFile) {
        this.pdfFile = pdfFile;
    }

    public StreamedContent getXlsFile() {
        return xlsFile;
    }

    public void setXlsFile(StreamedContent xlsFile) {
        this.xlsFile = xlsFile;
    }

    public String getBadgeColor() {
        return badgeColor;
    }

    public void setBadgeColor(String badgeColor) {
        this.badgeColor = badgeColor;
    }

    public DetRegistro getRegistroSelected() {
        return registroSelected;
    }

    public void setRegistroSelected(DetRegistro registroSelected) {
        this.registroSelected = registroSelected;
    }

    public EstatusRegistroDAO getEstatusRegistroDAO() {
        return statusRegistroDAO;
    }

    public void setEstatusRegistroDAO(EstatusRegistroDAO estatusRegistroDAO) {
        this.statusRegistroDAO = estatusRegistroDAO;
    }

    public List<CatEstatusRegistro> getLstEstatus() {
        return lstEstatus;
    }

    public void setLstEstatus(List<CatEstatusRegistro> lstEstatus) {
        this.lstEstatus = lstEstatus;
    }

    public List<Date> getDiasDeshabilitados() {
        return diasDeshabilitados;
    }

    public void setDiasDeshabilitados(List<Date> diasDeshabilitados) {
        this.diasDeshabilitados = diasDeshabilitados;
    }

	public DetEmpleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(DetEmpleado empleado) {
		this.empleado = empleado;
	}

	public List<Date> getRegistrosEmpleado() {
		return registrosEmpleado;
	}

	public void setRegistrosEmpleado(List<Date> registrosEmpleado) {
		this.registrosEmpleado = registrosEmpleado;
	}

	public Boolean getDesbloquearDiasDeDescanso() {
		return desbloquearDiasDeDescanso;
	}

	public void setDesbloquearDiasDeDescanso(Boolean desbloquearDiasDeDescanso) {
		this.desbloquearDiasDeDescanso = desbloquearDiasDeDescanso;
	}

	public Boolean getDesbloquearDiasNoLaborales() {
		return desbloquearDiasNoLaborales;
	}

	public void setDesbloquearDiasNoLaborales(Boolean desbloquearDiasNoLaborales) {
		this.desbloquearDiasNoLaborales = desbloquearDiasNoLaborales;
	}

	public List<Integer> getInvalidDays() {
		return invalidDays;
	}

	public void setInvalidDays(List<Integer> invalidDays) {
		this.invalidDays = invalidDays;
	}

	public List<Date> getDiasNoLaborales() {
		return diasNoLaborales;
	}

	public void setDiasNoLaborales(List<Date> diasNoLaborales) {
		this.diasNoLaborales = diasNoLaborales;
	}

}
