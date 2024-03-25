package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Named(value = "reportesBean")
@ViewScoped
public class ReportesBean implements Serializable{

	private static final long serialVersionUID = -1913238724563520797L;
	private static Logger log = LogManager.getLogger(ReportesBean.class);
	private String contextPath = null;
	
	public ReportesBean() {
		
	}
	
	@PostConstruct
	public void init() {
		contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
	}
	
	public void reporteAsistencia() {
		String path = null;
		try {
			log.info("Redirigiendo a reporte asistencia...");
			path = this.contextPath + "/protected/reportes/asistencia.xhtml";
			FacesContext.getCurrentInstance().getExternalContext().redirect(path);
		} catch (IOException ex) {
			log.error("Problema para redirigir al registro de préstamos...", ex);
		}
	}

}
