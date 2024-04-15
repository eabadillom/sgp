package mx.com.ferbo.controller.sat;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.sat.TipoJornadaDAO;
import mx.com.ferbo.dto.sat.TipoJornadaDTO;

@Named(value = "tipoJornadaBean")
@ViewScoped
public class TipoJornadaBean implements Serializable {

	private static final long serialVersionUID = -5233310947735706214L;
	private static Logger log = LogManager.getLogger(TipoJornadaBean.class);
	private String contextPath = null;
	
	private List<TipoJornadaDTO> tiposJornada;
	private TipoJornadaDTO tipoJornada;
	private TipoJornadaDAO tipoJornadaDAO;
	
	private boolean nuevo = false;
	
	public TipoJornadaBean() {
		this.tipoJornadaDAO = new TipoJornadaDAO();
	}
	
	@PostConstruct
	public void init() {
		this.contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		this.tiposJornada = tipoJornadaDAO.buscarTodos();
	}

	public List<TipoJornadaDTO> getTiposJornada() {
		return tiposJornada;
	}

	public void setTiposJornada(List<TipoJornadaDTO> tiposJornada) {
		this.tiposJornada = tiposJornada;
	}

	public TipoJornadaDTO getTipoJornada() {
		return tipoJornada;
	}

	public void setTipoJornada(TipoJornadaDTO tipoJornada) {
		this.tipoJornada = tipoJornada;
	}
	
	public void nuevo() {
		this.tipoJornada = new TipoJornadaDTO();
		this.nuevo = true;
	}
	
	public void editar() {
		log.info("Editando tipo de jornada: {}", this.tipoJornada);
		this.nuevo = false;
	}
	
	public void guardar() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Guardar tipo de jornada";
		try {
			log.info("Guardando tipo de jornada: {}", this.tipoJornada);
			
			if(this.nuevo)
				tipoJornadaDAO.guardar(this.tipoJornada);
			else
				tipoJornadaDAO.actualizar(this.tipoJornada);
			
			tiposJornada = tipoJornadaDAO.buscarTodos();
			
			mensaje = "El tipo de jornada se guardó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			PrimeFaces.current().executeScript("PF('dgTipoJornada').hide()");
		} catch(Exception ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-tipoJornada");
		}
	}

}
