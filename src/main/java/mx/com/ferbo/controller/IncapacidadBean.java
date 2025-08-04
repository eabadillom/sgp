package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
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

import mx.com.ferbo.dao.n.imss.IncapacidadIMSSDAO;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.imss.DetIncapacidad;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.ManageStatus;
import mx.com.ferbo.util.SGPException;


@Named(value = "incapacidadBean")
@ViewScoped
public class IncapacidadBean implements Serializable {

	private static final long serialVersionUID = 5867165907628761144L;
	private static Logger log = LogManager.getLogger(IncapacidadBean.class);
	
	private HttpServletRequest request;
	private DetEmpleado empleado = null;
	private DetIncapacidad incapacidad = null;
	private List<DetIncapacidad> incapacidades = null;
	private IncapacidadIMSSDAO incapacidadDAO = null;
	
	private Date periodoInicio;
	private Date periodoFin;
	private Boolean mostrarCanceladas;
	private List<Date> diasIncapacidad;
	
	private ManageStatus status;
	
	public IncapacidadBean() {
		this.request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		this.empleado = (DetEmpleado) request.getSession(false).getAttribute("empleado");
		
		this.periodoFin = new Date();
		this.periodoInicio = DateUtil.addMonth(periodoFin, -6);
		this.mostrarCanceladas = Boolean.FALSE;
		this.incapacidadDAO = new IncapacidadIMSSDAO();
	}
	
	@PostConstruct
	public void init() {
		this.cargarIncapacidades();
	}
	
	public void cargarIncapacidades() {
		this.incapacidades = new ArrayList<DetIncapacidad>();
		
		List<DetIncapacidad> incapacidades = this.incapacidadDAO.
				buscarPorParametros(this.empleado.getIdEmpleado(), this.periodoInicio, this.periodoFin);
		
		List<DetIncapacidad> incapacidadesAceptadas = incapacidades
				.stream().filter(item -> "A".equalsIgnoreCase(item.getEstatusSolicitud().getClave()))
				.collect(Collectors.toList());
		
		this.incapacidades.addAll(incapacidadesAceptadas);
		
		if(this.mostrarCanceladas) {
			List<DetIncapacidad> incapacidadesCanceladas = incapacidades
					.stream()
					.filter(item -> "C".equalsIgnoreCase(item.getEstatusSolicitud().getClave()))
					.collect(Collectors.toList());
			this.incapacidades.addAll(incapacidadesCanceladas);
		}
	}
	
	public void cargar(DetIncapacidad incapacidad) {
		FacesMessage message = null;
		FacesMessage.Severity severity = null;
		String mensaje = null;
		String titulo = "Incapacidad";
		
		try {
			log.info("Cargando incapacidad...");
			this.incapacidad = incapacidadDAO.cargar(incapacidad.getIdIncapacidad())
					.orElseThrow(() -> new SGPException("Incapacidad no encontrada."));
			
			this.diasIncapacidad = new ArrayList<Date>();
			
			diasIncapacidad.add(this.incapacidad.getFechaInicio());
			diasIncapacidad.add(this.incapacidad.getFechaFin());
			
		} catch(SGPException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("formActividades:messages", "formActividades:tabView:dg-incapacidad");
		} catch(Exception ex) {
			mensaje = "Ocurrió un problema al cargar la solicitud.";
			severity = FacesMessage.SEVERITY_WARN;
			message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("formActividades:messages", "formActividades:tabView:dg-incapacidad");
		}
	}
	
	public DetIncapacidad getIncapacidad() {
		return incapacidad;
	}

	public void setIncapacidad(DetIncapacidad incapacidad) {
		this.incapacidad = incapacidad;
	}

	public List<DetIncapacidad> getIncapacidades() {
		return incapacidades;
	}

	public void setIncapacidades(List<DetIncapacidad> incapacidades) {
		this.incapacidades = incapacidades;
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

	public Boolean getMostrarCanceladas() {
		return mostrarCanceladas;
	}

	public void setMostrarCanceladas(Boolean mostrarCanceladas) {
		this.mostrarCanceladas = mostrarCanceladas;
	}

	public ManageStatus getStatus() {
		return status;
	}

	public void setStatus(ManageStatus status) {
		this.status = status;
	}

	public List<Date> getDiasIncapacidad() {
		return diasIncapacidad;
	}

	public void setDiasIncapacidad(List<Date> diasIncapacidad) {
		this.diasIncapacidad = diasIncapacidad;
	}
}
