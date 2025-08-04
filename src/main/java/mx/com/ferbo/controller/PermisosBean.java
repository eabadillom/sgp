package mx.com.ferbo.controller;

import java.io.Serializable;
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

import mx.com.ferbo.business.incidencia.IncidenciaBL;
import mx.com.ferbo.business.incidencia.SolicitudPermisoBL;
import mx.com.ferbo.dao.n.IncidenciaDAO;
import mx.com.ferbo.dao.n.TipoSolicitudDAO;
import mx.com.ferbo.model.CatTipoSolicitud;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.ManageStatus;
import mx.com.ferbo.util.SGPException;

@Named(value = "permisosBean")
@ViewScoped
public class PermisosBean implements Serializable {

	private static final long serialVersionUID = 1414465268518305782L;
	private static Logger log = LogManager.getLogger(PermisosBean.class);
	
	private HttpServletRequest request;
	
	private DetEmpleado empleado;
	private Date periodoInicio;
	private Date periodoFin;
	private Date minDate;
	private List<DetIncidencia> permisos;
	private DetIncidencia permiso;
	private IncidenciaDAO incidenciaDAO;
	private CatTipoSolicitud tipoSolicitud;
	private TipoSolicitudDAO tipoSolicitudDAO;
	private Boolean mostrarCanceladas;
	
	private ManageStatus status;
	
	public PermisosBean() throws SGPException {
		this.request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		this.empleado = (DetEmpleado) this.request.getSession(false).getAttribute("empleado");
		this.periodoInicio = DateUtil.addMonth(new Date(), -1);
		this.periodoFin    = DateUtil.addMonth(new Date(),  1);
		this.incidenciaDAO = new IncidenciaDAO();
		this.tipoSolicitudDAO = new TipoSolicitudDAO();
		this.tipoSolicitud = this.tipoSolicitudDAO.buscarPorClave(SolicitudPermisoBL.TP_PERMISO)
				.orElseThrow(() -> new SGPException("Tipo de solicitud no encontrada"));
		this.minDate = DateUtil.addDay(DateUtil.now(), -7);
		this.status = new ManageStatus();
		this.mostrarCanceladas = Boolean.FALSE;
	}
	
	@PostConstruct
	public void init() {
		this.cargarPermisos();
	}
	
	public void cargarPermisos() {
		log.info("Cargando lista de permisos del empleado...");
		permisos = incidenciaDAO.buscarPermisos(periodoInicio, periodoFin)
				.stream()
				.filter(item -> SolicitudPermisoBL.TP_PERMISO
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
		
		try {
			this.permiso = IncidenciaBL.create(IncidenciaBL.TP_PERMISO, empleado);
		} catch(Exception ex) {
			log.error("Problema para crear una solicitud de permiso...", ex);
		} finally {
			PrimeFaces.current().ajax().update("");
		}
	}
	
	public void cargarPermiso(DetIncidencia permiso) {
		log.info("Cargando permiso...");
		this.permiso = permiso;
	}
	
	public void seleccionarFecha() {
		log.info("Fecha seleccionada: {}", this.permiso.getSolPermiso().getFechaInicio());
	}
	
	public void guardar() {
		FacesMessage message = null;
		FacesMessage.Severity severity = null;
		String mensaje = null;
		String titulo = "Solicitud";
		
		try {
			log.info("Guardando solicitud de permiso...");
			
			this.permiso.getSolPermiso().setFechaFin(this.permiso.getSolPermiso().getFechaInicio());
			this.permiso.getSolPermiso().setTipoSolicitud(tipoSolicitud);
			
			IncidenciaBL.guardar(this.permiso);
			this.cargarPermisos();
			
			PrimeFaces.current().executeScript("PF('dgPermiso').hide();");
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
			PrimeFaces.current().ajax().update("formActividades:messages", "formActividades:tabView:dt-permisos");
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
			
			PrimeFaces.current().executeScript("PF('dgPermiso').hide();");
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
			PrimeFaces.current().ajax().update("formActividades:messages", "formActividades:tabView:dt-permisos");
		}
	}
	
	public void cancelar() {
		FacesMessage message = null;
		FacesMessage.Severity severity = null;
		String mensaje = null;
		String titulo = "Solicitud";
		
		try {
			log.info("Cancelando solicitud de permiso...");
			IncidenciaBL.cancelar(this.permiso);
			this.cargarPermisos();
			
			PrimeFaces.current().executeScript("PF('dgPermiso').hide();");
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
			PrimeFaces.current().ajax().update("formActividades:messages", "formActividades:tabView:dt-permisos");
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

	public ManageStatus getStatus() {
		return status;
	}

	public void setStatus(ManageStatus status) {
		this.status = status;
	}

	public Boolean getMostrarCanceladas() {
		return mostrarCanceladas;
	}

	public void setMostrarCanceladas(Boolean mostrarCanceladas) {
		this.mostrarCanceladas = mostrarCanceladas;
	}
	

}
