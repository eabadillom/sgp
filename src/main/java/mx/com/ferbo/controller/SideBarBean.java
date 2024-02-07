package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dto.DetEmpleadoDTO;




@Named(value = "SideBarBean")
@ViewScoped
public class SideBarBean implements Serializable  {
	
	
	 private static final long serialVersionUID = 1L;
	 private DetEmpleadoDTO empleadoSelected;
	 private HttpServletRequest httpServletRequest;
	 private static Logger log = LogManager.getLogger(SideBarBean.class);
	    
	   public SideBarBean() {
		   httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
           empleadoSelected = (DetEmpleadoDTO) httpServletRequest.getSession(true).getAttribute("empleado");
	}

	@PostConstruct
	    public void init() {
		   
	   }

	public void killSesion() throws IOException {
    	log.info("Entrada al killSesion");
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

        if(session.getAttribute("empleado") != null){
            session.removeAttribute("empleado");
        }
        FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("JSESSIONID", null, null);
        
        String context = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        FacesContext.getCurrentInstance().getExternalContext().redirect(context);
        

        log.info("Finalizando sesion..........");
    }
    
	public DetEmpleadoDTO getEmpleadoSelected() {
		return empleadoSelected;
	}

	public void setEmpleadoSelected(DetEmpleadoDTO empleadoSelected) {
		this.empleadoSelected = empleadoSelected;
	}

	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}

	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		SideBarBean.log = log;
	}

}
