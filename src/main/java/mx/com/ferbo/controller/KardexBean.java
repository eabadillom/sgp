package mx.com.ferbo.controller;

import java.io.Serializable;

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


@Named(value = "kardexBean")
@ViewScoped
public class KardexBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(KardexBean.class);

    private DetEmpleado empleado;
    private DetEmpleadoFoto empleadoFoto;
    private HttpServletRequest request;

    public KardexBean() {
    	
    }

    @PostConstruct
    public void init() {
    	HttpSession session = null;
    	
        try {
        	request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        	session = request.getSession(false);
            empleado = (DetEmpleado) session.getAttribute("empleado");
            empleadoFoto = (DetEmpleadoFoto) session.getAttribute("fotografia");
        	
            log.info("idEmpleado: {}", empleado.getIdEmpleado());
        	
        } catch (Exception ex) {
            log.warn("EX-0007: " + ex.getMessage() + ". Error al cargar init()");
        }
    }
    
    public DetEmpleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(DetEmpleado empleado) {
        this.empleado = empleado;
    }


	public DetEmpleadoFoto getEmpleadoFoto() {
		return empleadoFoto;
	}

	public void setEmpleadoFoto(DetEmpleadoFoto empleadoFoto) {
		this.empleadoFoto = empleadoFoto;
	}

}
