package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetEmpleadoFoto;
import mx.com.ferbo.util.DateUtil;

@Named(value = "SideBarBean")
@ViewScoped
public class SideBarBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private DetEmpleado empleadoSelected;
	private DetEmpleadoFoto empleadoFoto;
	private HttpServletRequest request;
	private static Logger log = LogManager.getLogger(SideBarBean.class);
	private Date fechaActual;
	private Integer diaActual;
	private Boolean showUniformes;
	private Boolean showArticulos;
	
	public SideBarBean() {
		fechaActual = new Date();
		diaActual = DateUtil.getDia(fechaActual);
		log.info("Dia actual: {}", diaActual);
	}
	
	@PostConstruct
	public void init() {
		HttpSession session = null;
		
		try {
			request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			session = request.getSession(false);
			empleadoSelected = (DetEmpleado) session.getAttribute("empleado");
			empleadoFoto = (DetEmpleadoFoto) session.getAttribute("fotografia");
			showUniformes = validarUniformes();
			showArticulos = validarArticulos();
			
		} catch(Exception ex) {
			
		}
	}

	private Boolean validarUniformes() {
		Boolean isValid = null;
		
		switch(diaActual) {
		case 24:
		case 25:
		case 26:
		case 27:
		case 28:
			isValid = new Boolean(true);
			break;
		default:
			isValid = new Boolean(false);
		}
		return isValid;
	}
	
	private Boolean validarArticulos() {
		Boolean isValid = null;
		
		switch(diaActual) {
		case 24:
		case 25:
		case 26:
		case 27:
		case 28:
			isValid = new Boolean(true);
			break;
		default:
			isValid = new Boolean(false);
		}
		return isValid;
	}

	public void killSesion() throws IOException {
		log.info("Entrada al killSesion");
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

		if (session.getAttribute("empleado") != null) {
			session.removeAttribute("empleado");
		}
		FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("JSESSIONID", null, null);

		String context = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		FacesContext.getCurrentInstance().getExternalContext().redirect(context);

		log.info("Finalizando sesion..........");
	}

	public DetEmpleado getEmpleadoSelected() {
		return empleadoSelected;
	}

	public void setEmpleadoSelected(DetEmpleado empleadoSelected) {
		this.empleadoSelected = empleadoSelected;
	}

	public HttpServletRequest getHttpServletRequest() {
		return request;
	}

	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.request = httpServletRequest;
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		SideBarBean.log = log;
	}

	public Integer getDiaActual() {
		return diaActual;
	}

	public void setDiaActual(Integer diaActual) {
		this.diaActual = diaActual;
	}

	public Boolean getShowUniformes() {
		return showUniformes;
	}

	public void setShowUniformes(Boolean showUniformes) {
		this.showUniformes = showUniformes;
	}

	public Boolean getShowArticulos() {
		return showArticulos;
	}

	public void setShowArticulos(Boolean showArticulos) {
		this.showArticulos = showArticulos;
	}

	public DetEmpleadoFoto getEmpleadoFoto() {
		return empleadoFoto;
	}

	public void setEmpleadoFoto(DetEmpleadoFoto empleadoFoto) {
		this.empleadoFoto = empleadoFoto;
	}

}
