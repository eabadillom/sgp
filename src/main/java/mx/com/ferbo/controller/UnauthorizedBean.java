package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named(value = "unauthorizedBean")
@ViewScoped
public class UnauthorizedBean implements Serializable {

	private static final long serialVersionUID = 4056721079576353673L;
	private static Logger log = LogManager.getLogger(UnauthorizedBean.class);
	
	String contextPath = null;
	private String path = null;
	
	public UnauthorizedBean() {
		
	}
	
	@PostConstruct
	public void init() {
		contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
	}

	public void redirect() {
		
		try {
			log.info("Redirigiendo a {}", path);
			path = this.contextPath + "/protected/kardexEmpleado.xhtml";
			FacesContext.getCurrentInstance().getExternalContext().redirect(path);
		} catch (IOException e) {
			log.error("Problema para redigir al kardex...", e);
		}
	}
}
