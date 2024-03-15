package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named(value = "ajustesBean")
@ViewScoped
public class AjustesBean implements Serializable {

	private static final long serialVersionUID = 572901091404696084L;
	private static Logger log = LogManager.getLogger(AjustesBean.class);
	private String contextPath = null;
	
	public AjustesBean() {
		
	}
	
	@PostConstruct
	public void init() {
		contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
	}
	
	public void configNonWorkingDays() {
		String path = null;
		try {
			log.info("Redirigiendo a catálogo de dias no laborales...");
			path = this.contextPath + "/protected/settings/diasNoLaborales.xhtml";
			FacesContext.getCurrentInstance().getExternalContext().redirect(path);
		} catch (IOException ex) {
			log.error("Problema para redirigir al catalogo de dias no laborales...", ex);
		}
	}
	
	public void prestamosInfonavit() {
		String path = null;
		try {
			log.info("Redirigiendo a préstamos INFONAVIT...");
			path = this.contextPath + "/protected/settings/prestamosInfonavit.xhtml";
			FacesContext.getCurrentInstance().getExternalContext().redirect(path);
		} catch (IOException ex) {
			log.error("Problema para redirigir al catalogo de dias no laborales...", ex);
		}
	}
	
	public void periodicidadPago() {
		String path = null;
		try {
			log.info("Redirigiendo a periodicidad de pago...");
			path = this.contextPath + "/protected/settings/periodicidadPago.xhtml";
			FacesContext.getCurrentInstance().getExternalContext().redirect(path);
		} catch (IOException ex) {
			log.error("Problema para redirigir al catalogo de dias no laborales...", ex);
		}
	}

}
