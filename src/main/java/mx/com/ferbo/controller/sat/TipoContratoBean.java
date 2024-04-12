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

import mx.com.ferbo.dao.sat.TipoContratoDAO;
import mx.com.ferbo.dto.sat.TipoContratoDTO;

@Named(value = "tipoContratoBean")
@ViewScoped
public class TipoContratoBean implements Serializable {

	private static final long serialVersionUID = 5567114413253634617L;
	private static Logger log = LogManager.getLogger(TipoContratoBean.class);
	private String contextPath = null;
	
	private List<TipoContratoDTO> tiposContrato;
	private TipoContratoDTO tipoContrato;
	private TipoContratoDAO tipoContratoDAO;
	
	private boolean nuevo = false;
	
	public TipoContratoBean() {
		this.tipoContratoDAO = new TipoContratoDAO();
	}
	
	@PostConstruct
	public void init() {
		contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		this.tiposContrato = tipoContratoDAO.buscarTodos();
	}
	
	public void nuevo() {
		this.tipoContrato = new TipoContratoDTO();
		this.nuevo = true;
	}
	
	public void editar() {
		log.info("Editando tipo de contrato: {}", this.tipoContrato);
		this.nuevo = false;
	}
	
	public void guardar() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Guardar tipo de contrato";
		try {
			log.info("Guardando tipo de contrato: {}", this.tipoContrato);
			
			if(this.nuevo)
				tipoContratoDAO.guardar(this.tipoContrato);
			else
				tipoContratoDAO.actualizar(this.tipoContrato);
			
			tiposContrato = tipoContratoDAO.buscarTodos();
			
			mensaje = "El tipo de contrato se guardó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			PrimeFaces.current().executeScript("PF('dgTipoContrato').hide()");
		} catch(Exception ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-tipoContrato");
		}
	}

	public List<TipoContratoDTO> getTiposContrato() {
		return tiposContrato;
	}

	public void setTiposContrato(List<TipoContratoDTO> tiposContrato) {
		this.tiposContrato = tiposContrato;
	}

	public TipoContratoDTO getTipoContrato() {
		return tipoContrato;
	}

	public void setTipoContrato(TipoContratoDTO tipoContrato) {
		this.tipoContrato = tipoContrato;
	}
}
