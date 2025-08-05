package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import mx.com.ferbo.business.dianolaboral.DiasDeDescansoObligatorioBL;
import mx.com.ferbo.business.empleado.EmpleadoBL;
import mx.com.ferbo.business.incidencia.IncidenciaBL;
import mx.com.ferbo.business.incidencia.SolicitudPermisoBL;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.IncidenciaDAO;
import mx.com.ferbo.dao.n.TipoSolicitudDAO;
import mx.com.ferbo.dao.n.VacacionesDAO;
import mx.com.ferbo.model.CatTipoSolicitud;
import mx.com.ferbo.model.DetDiaPermiso;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.model.DetRegistroVacaciones;
import mx.com.ferbo.model.DetVacaciones;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.ManageStatus;
import mx.com.ferbo.util.SGPException;

@Named(value = "vacacionesBean")
@ViewScoped
public class VacacionesBean implements Serializable {

	private static final long serialVersionUID = -1098787464152570894L;
	private static Logger log = LogManager.getLogger(VacacionesBean.class);
	
	private HttpServletRequest request;
	
	private DetEmpleado empleado;
	private EmpleadoDAO empleadoDAO;
	private Date periodoInicio;
	private Date periodoFin;
	private Date minDate;
	private List<DetIncidencia> permisos;
	private DetIncidencia permiso;
	private IncidenciaDAO incidenciaDAO;
	private CatTipoSolicitud tipoSolicitud;
	private TipoSolicitudDAO tipoSolicitudDAO;
	private List<DetVacaciones> periodosVacacionales;
	private DetVacaciones periodoVacacional;
	private Integer diasTotalesPermitidos = 0;
	private VacacionesDAO vacacionesDAO = null;
	private List<Date> diasSolicitados;
	
	private List<Integer> invalidDays;
	private List<Date> diasDeAsueto;
	private Boolean mostrarCanceladas;
	
	private ManageStatus status;
	
	public VacacionesBean() throws SGPException {
		this.request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		this.empleadoDAO = new EmpleadoDAO();
		this.empleado = (DetEmpleado) request.getSession(false).getAttribute("empleado");
		this.empleado = this.empleadoDAO.buscarPorId(this.empleado.getIdEmpleado());
		this.diasDeAsueto = DiasDeDescansoObligatorioBL.diasDeAsueto();
		this.invalidDays = SolicitudPermisoBL.obtenerDiasSeleccionados(empleado.getDatoEmpresa());
		this.periodoInicio = DateUtil.addMonth(new Date(), -1);
		this.periodoFin    = DateUtil.addMonth(new Date(),  1);
		this.incidenciaDAO = new IncidenciaDAO();
		this.tipoSolicitudDAO = new TipoSolicitudDAO();
		this.tipoSolicitud = this.tipoSolicitudDAO.buscarPorClave(SolicitudPermisoBL.TP_PERMISO)
				.orElseThrow(() -> new SGPException("Tipo de solicitud no encontrada"));
		this.minDate = DateUtil.addDay(DateUtil.now(), -7);
		this.status = new ManageStatus();
		this.vacacionesDAO = new VacacionesDAO();
		this.mostrarCanceladas = Boolean.FALSE;
	}
	
	@PostConstruct
	public void init() {
		this.cargarPermisos();
	}
	
	public void cargarPermisos() {
		log.info("Cargando lista de permisos del empleado...");
		this.permisos = this.incidenciaDAO.buscarPermisos(this.empleado.getIdEmpleado(), this.periodoInicio, this.periodoFin)
				.stream()
				.filter(item -> SolicitudPermisoBL.TP_VACACIONES
						.equalsIgnoreCase(item.getSolPermiso().getTipoSolicitud().getClave()))
				.collect(Collectors.toList())
				;
		
		if(this.mostrarCanceladas == false)
			permisos = permisos.stream()
			.filter(item -> !"C".equalsIgnoreCase(item.getSolPermiso().getEstatus().getClave()) )
			.collect(Collectors.toList())
			;
	}
	
	public void nuevoPermiso() {
		FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Cargar informacion";
        
		try {
			this.permiso = IncidenciaBL.create(IncidenciaBL.TP_VACACIONES, this.empleado);
			DiasDeDescansoObligatorioBL.diasDescansoEstanActualizados();
            EmpleadoBL.empleadoTieneDiasLaborales(this.empleado);
            this.periodosVacacionales = this.periodosPorFechaActual();
            this.periodoVacacional = null;
            this.diasSolicitados = new ArrayList<Date>();
			
            PrimeFaces.current().executeScript("PF('dialogVacaciones').show()");
		} catch(Exception ex) {
			log.error("Problema para crear una solicitud de vacaciones...", ex);
			mensaje = "Problema para generar la solicitud de vacaciones.";
            severity = FacesMessage.SEVERITY_ERROR;
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update(":formActividades:messages");
		}
	}
	
	public List<DetVacaciones> periodosPorFechaActual() {
        List<DetVacaciones> periodos = new ArrayList<DetVacaciones>();
        this.diasTotalesPermitidos = 0;
        
        log.info("Buscando periodos vacacionales del empleado...");
        List<DetVacaciones> periodosTmp = vacacionesDAO.cargarPeriodos(this.empleado.getIdEmpleado(), DateUtil.now());
        
        for (DetVacaciones periodo : periodosTmp) {
        	
        	if (periodo.getDiasTomados() < periodo.getDiasTotales() && (periodo.getDiasPagados() + periodo.getDiasTomados()) < periodo.getDiasTotales()) {
        		periodos.add(periodo);
        		this.diasTotalesPermitidos += (periodo.getDiasTotales() - periodo.getDiasPagados() - periodo.getDiasTomados());
        	}
        }
        
        return periodos;
    }
	
	public Integer diasDisponibles(DetVacaciones periodo) {
		Integer diasTomados = periodo.getDiasTomados();
    	Integer diasRegistro = 0;
    	
    	for(DetRegistroVacaciones registroV : periodo.getRegistroVacaciones()) {
    		
    		if( registroV.getRegistro() == null )
    			continue;
    		
    		if( ! "V".equalsIgnoreCase(registroV.getRegistro().getStatus().getCodigo()))
    			continue;
    		
    		diasRegistro++;
    	}
    	
    	Integer saldo = periodo.getDiasTotales() - diasTomados - diasRegistro;
    	
    	return saldo;
	}
	
	public void onPeriodoSelect(SelectEvent<DetVacaciones> event) {
    	log.debug("Seleccionando periodo vacacional: {}", event.getObject().getIdVacaciones());
    	DetVacaciones periodo = (DetVacaciones)event.getObject();
    	String mensaje = String.format("Puede seleccionar %d días", (periodo.getDiasTotales() - periodo.getDiasTomados()));
    	FacesMessage msg = new FacesMessage("Seleccione sus días",  mensaje);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        PrimeFaces.current().ajax().update("formActividades:messages", "formActividades:tabView:dpView");
    }
    
    public void onPeriodoUnselect(SelectEvent<DetVacaciones> event) {
    	log.debug("Seleccionando periodo vacacional: {}", event.getObject().getIdVacaciones());
    	FacesMessage msg = new FacesMessage("Product Unselected", String.valueOf(event.getObject().getIdVacaciones()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
	
	public void cargarPermiso(DetIncidencia permiso) {
		FacesMessage message = null;
		FacesMessage.Severity severity = null;
		String mensaje = null;
		String titulo = "Solicitud";
		try {
			log.info("Cargando permiso...");
			
			this.permiso = incidenciaDAO.cargar(permiso.getIdIncidencia())
					.orElseThrow(() -> new SGPException("Incidencia no encontrada."));
			
			this.periodoVacacional = this.permiso.getSolPermiso().getVacaciones();
			
			this.periodosVacacionales = this.periodosPorFechaActual();
			if(this.periodosVacacionales.contains(this.periodoVacacional) == false)
				this.periodosVacacionales.add(periodoVacacional);
			
			if(this.permiso.getSolPermiso().getDiasPermiso() == null
					|| this.permiso.getSolPermiso().getDiasPermiso().size() == 0)
				this.diasSolicitados = DateUtil.generarArreglosFechas(permiso.getSolPermiso().getFechaInicio(), permiso.getSolPermiso().getFechaFin());
			else
				this.diasSolicitados = this.permiso.getSolPermiso().getDiasPermiso().stream()
				.map(DetDiaPermiso::getFecha)
				.collect(Collectors.toList())
				;
		} catch(SGPException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("formActividades:messages", "formActividades:tabView:dg-vacaciones");
		} catch(Exception ex) {
			mensaje = "Ocurrió un problema al cargar la solicitud.";
			severity = FacesMessage.SEVERITY_WARN;
			message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("formActividades:messages", "formActividades:tabView:dg-vacaciones");
		}
	}
	
	public void seleccionarFecha() {
		Date fechaInicio = null;
		Date fechaFin = null;
		try {
			log.info("Fecha seleccionada: {}", this.permiso.getSolPermiso().getFechaInicio());
			this.diasSolicitados.stream().forEach(dia -> log.info("Dia solicitado: {}", dia ));
			
			fechaInicio = this.diasSolicitados.stream().min(Comparator.naturalOrder()).orElseThrow(() -> new SGPException("No hay fecha seleccionada."));
			fechaFin = this.diasSolicitados.stream().max(Comparator.naturalOrder()).orElseThrow(() -> new SGPException("No hay fecha seleccionada"));
			
			this.permiso.getSolPermiso().setFechaInicio(fechaInicio);
			this.permiso.getSolPermiso().setFechaFin(fechaFin);
			
			log.info("Dias solicitados: {}", this.diasSolicitados.size());
		} catch(Exception ex) {
			log.error("Problema para obtener los días solicitados...", ex);
		}
	}
	
	public void guardar() {
		FacesMessage message = null;
		FacesMessage.Severity severity = null;
		String mensaje = null;
		String titulo = "Solicitud";
		
		Date fechaInicio = null;
		Date fechaFin = null;
		
		try {
			
			
			log.info("Guardando solicitud de permiso...");
			
			this.permiso.getSolPermiso().setFechaFin(this.permiso.getSolPermiso().getFechaInicio());
			this.permiso.getSolPermiso().setVacaciones(periodoVacacional);
			
			fechaInicio = this.diasSolicitados.stream().min(Comparator.naturalOrder()).orElseThrow(() -> new SGPException("No hay fecha seleccionada."));
			fechaFin = this.diasSolicitados.stream().max(Comparator.naturalOrder()).orElseThrow(() -> new SGPException("No hay fecha seleccionada"));
			
			this.permiso.getSolPermiso().setFechaInicio(fechaInicio);
			this.permiso.getSolPermiso().setFechaFin(fechaFin);
			
			this.diasSolicitados.stream().forEach(dia -> {
				DetDiaPermiso diaPermiso = new DetDiaPermiso();
				diaPermiso.setSolicitudPermiso(this.permiso.getSolPermiso());
				diaPermiso.setFecha(dia);
				this.permiso.getSolPermiso().getDiasPermiso().add(diaPermiso);
			});
			
			log.info("Tipo de incidencia: {}", this.permiso.getTipoIncidencia().getClave());
			
			log.info("Tipo de solicitud: {}", this.permiso.getSolPermiso().getTipoSolicitud().getClave());
			
			
			IncidenciaBL.guardar(this.permiso);
			this.cargarPermisos();
			
			PrimeFaces.current().executeScript("PF('dgVacaciones').hide();");
			mensaje = "La solicitud se guardó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(SGPException ex){
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			mensaje = "Ocurrió un problema al guardar la solicitud del permiso.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("formActividades:messages", "formActividades:tabView:dtVacaciones");
		}
	}
	
	public void actualizar() {
		FacesMessage message = null;
		FacesMessage.Severity severity = null;
		String mensaje = null;
		String titulo = "Solicitud";
		
		try {
			log.info("Actualizando solicitud de permiso...");
			
			this.permiso.getSolPermiso().setFechaFin(this.permiso.getSolPermiso().getFechaInicio());
			this.permiso.getSolPermiso().setTipoSolicitud(tipoSolicitud);
			
			IncidenciaBL.guardar(this.permiso);
			this.cargarPermisos();
			
			PrimeFaces.current().executeScript("PF('dgVacaciones').hide();");
			mensaje = "Tu solicitud de vacaciones se guardó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(SGPException ex){
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			mensaje = "Ocurrió un problema al guardar la solicitud del permiso.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("formActividades:messages", "formActividades:tabView:dt-permisos");
		}
	}
	
	public void cancelar() {
		FacesMessage message = null;
		FacesMessage.Severity severity = null;
		String mensaje = null;
		String titulo = "Solicitud";
		
		try {
			log.info("Cancelando solicitud de vacaciones...");
			IncidenciaBL.cancelar(this.permiso);
			this.cargarPermisos();
			
			PrimeFaces.current().executeScript("PF('dgVacaciones').hide();");
			mensaje = "La solicitud se guardó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(SGPException ex){
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			mensaje = "Ocurrió un problema al guardar la solicitud de vacaciones.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("formActividades:messages", "formActividades:tabView:dt-vacaciones");
		}
		
		
	}
	
	public boolean permiteActualizar() {
		boolean respuesta = false;
		
		if(permiso == null)
			return respuesta;
		
		if(permiso.getIdIncidencia() == null)
			return respuesta;
		
		switch(permiso.getSolPermiso().getEstatus().getClave()) {
			case SolicitudPermisoBL.STATUS_ENVIADA:
				respuesta = true;
				break;
			case SolicitudPermisoBL.STATUS_APROBADA:
			case SolicitudPermisoBL.STATUS_CANCELADA:
			case SolicitudPermisoBL.STATUS_RECHAZADA:
				respuesta = false;
				break;
		}
		
		return respuesta;
	}
	
	public boolean permiteCancelar() {
		boolean respuesta = false;
		
		if(permiso == null)
			return respuesta;
		
		if(permiso.getIdIncidencia() == null)
			return respuesta;
		
		switch(permiso.getSolPermiso().getEstatus().getClave()) {
			case SolicitudPermisoBL.STATUS_ENVIADA:
			case SolicitudPermisoBL.STATUS_APROBADA:
				respuesta = true;
				break;
			case SolicitudPermisoBL.STATUS_RECHAZADA:
			case SolicitudPermisoBL.STATUS_CANCELADA:
				respuesta = false;
				break;
		}
		
		return respuesta;
	}
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public Date getPeriodoInicio() {
		return periodoInicio;
	}

	public void setPeriodoInicio(Date periodoInicio) {
		this.periodoInicio = periodoInicio;
	}

	public Date getPeriodoFin() {
		return periodoFin;
	}

	public void setPeriodoFin(Date periodoFin) {
		this.periodoFin = periodoFin;
	}

	public Date getMinDate() {
		return minDate;
	}

	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}

	public List<DetIncidencia> getPermisos() {
		return permisos;
	}

	public void setPermisos(List<DetIncidencia> permisos) {
		this.permisos = permisos;
	}

	public DetIncidencia getPermiso() {
		return permiso;
	}

	public void setPermiso(DetIncidencia permiso) {
		this.permiso = permiso;
	}

	public ManageStatus getStatus() {
		return status;
	}

	public void setStatus(ManageStatus status) {
		this.status = status;
	}

	public List<DetVacaciones> getPeriodosVacacionales() {
		return periodosVacacionales;
	}

	public void setPeriodosVacacionales(List<DetVacaciones> periodosVacacionales) {
		this.periodosVacacionales = periodosVacacionales;
	}

	public DetVacaciones getPeriodoVacacional() {
		return periodoVacacional;
	}

	public void setPeriodoVacacional(DetVacaciones periodoVacacional) {
		this.periodoVacacional = periodoVacacional;
	}

	public Integer getDiasTotalesPermitidos() {
		return diasTotalesPermitidos;
	}

	public void setDiasTotalesPermitidos(Integer diasTotalesPermitidos) {
		this.diasTotalesPermitidos = diasTotalesPermitidos;
	}

	public List<Date> getDiasSolicitados() {
		return diasSolicitados;
	}

	public void setDiasSolicitados(List<Date> diasSolicitados) {
		this.diasSolicitados = diasSolicitados;
	}

	public List<Integer> getInvalidDays() {
		return invalidDays;
	}

	public void setInvalidDays(List<Integer> invalidDays) {
		this.invalidDays = invalidDays;
	}

	public List<Date> getDiasDeAsueto() {
		return diasDeAsueto;
	}

	public Boolean getMostrarCanceladas() {
		return mostrarCanceladas;
	}

	public void setMostrarCanceladas(Boolean mostrarCanceladas) {
		this.mostrarCanceladas = mostrarCanceladas;
	}

}
