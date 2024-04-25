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
	
	public void empresas() {
		String path = null;
		try {
			log.info("Redirigiendo a catálogo de empresas...");
			path = this.contextPath + "/protected/settings/sistema/empresas.xhtml";
			FacesContext.getCurrentInstance().getExternalContext().redirect(path);
		} catch (IOException ex) {
			log.error("Problema para redirigir al catalogo de empresas...", ex);
		}
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
	
	public void salarioMinimo() {
		String path = null;
		try {
			log.info("Redirigiendo a catálogo de salarios mínimos...");
			path = this.contextPath + "/protected/settings/sistema/salarioMinimo.xhtml";
			FacesContext.getCurrentInstance().getExternalContext().redirect(path);
		} catch (IOException ex) {
			log.error("Problema para redirigir al catalogo de salarios minimos...", ex);
		}
	}
	
	public void prestamos() {
		String path = null;
		try {
			log.info("Redirigiendo a préstamos...");
			path = this.contextPath + "/protected/settings/prestamos.xhtml";
			FacesContext.getCurrentInstance().getExternalContext().redirect(path);
		} catch (IOException ex) {
			log.error("Problema para redirigir al registro de préstamos...", ex);
		}
	}
	
	public void periodicidadPago() {
		String path = null;
		try {
			log.info("Redirigiendo a periodicidad de pago...");
			path = this.contextPath + "/protected/settings/periodicidadPago.xhtml";
			FacesContext.getCurrentInstance().getExternalContext().redirect(path);
		} catch (IOException ex) {
			log.error("Problema para redirigir al catalogo periodicidad de pago...", ex);
		}
	}
	
	public void regimenFiscal() {
		String path = null;
		try {
			log.info("Redirigiendo a catálogo régimen fiscal...");
			path = this.contextPath + "/protected/settings/sat/regimenFiscal.xhtml";
			FacesContext.getCurrentInstance().getExternalContext().redirect(path);
		} catch (IOException ex) {
			log.error("Problema para redirigir al catalogo regimen fiscal...", ex);
		}
	}
	
	public void tipoContrato() {
		String path = null;
		try {
			log.info("Redirigiendo a catálogo tipo de contrato...");
			path = this.contextPath + "/protected/settings/sat/tipoContrato.xhtml";
			FacesContext.getCurrentInstance().getExternalContext().redirect(path);
		} catch (IOException ex) {
			log.error("Problema para redirigir al catalogo tipo de contrato...", ex);
		}
	}
	
	public void tipoRegimen() {
		String path = null;
		try {
			log.info("Redirigiendo a catálogo tipo de régimen...");
			path = this.contextPath + "/protected/settings/sat/tipoRegimen.xhtml";
			FacesContext.getCurrentInstance().getExternalContext().redirect(path);
		} catch (IOException ex) {
			log.error("Problema para redirigir al catalogo tipos de regimen...", ex);
		}
	}
	
	public void tipoJornada() {
		String path = null;
		try {
			log.info("Redirigiendo a catálogo tipo de jornada...");
			path = this.contextPath + "/protected/settings/sat/tipoJornada.xhtml";
			FacesContext.getCurrentInstance().getExternalContext().redirect(path);
		} catch (IOException ex) {
			log.error("Problema para redirigir al catalogo tipos de jornada...", ex);
		}
	}

}
