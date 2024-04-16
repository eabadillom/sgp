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

import mx.com.ferbo.dao.sat.TipoRegimenDAO;
import mx.com.ferbo.dto.sat.TipoRegimenDTO;

@Named(value = "tipoRegimenBean")
@ViewScoped
public class TipoRegimenBean implements Serializable {

	private static final long serialVersionUID = -3103728579268351267L;
	private static Logger log = LogManager.getLogger(TipoRegimenBean.class);
	private String contextPath = null;
	private TipoRegimenDAO tipoRegimenDAO;
	private List<TipoRegimenDTO> regimenes;
	private TipoRegimenDTO regimen;
	private boolean nuevo = false;
	
	public TipoRegimenBean() {
		this.tipoRegimenDAO = new TipoRegimenDAO();
	}
	
	@PostConstruct
	public void init() {
		this.contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		regimenes = tipoRegimenDAO.buscarTodos();
	}
	
	public void nuevo() {
		this.regimen = new TipoRegimenDTO();
		this.nuevo = true;
	}
	
	public void editar( ) {
		log.info("Editando tipo de régimen: {}", this.regimen);
		this.nuevo = false;
	}
	
	public void guardar() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Guardar tipo de régimen";
		
		try {
			log.info("Guardando tipo de régimen: {}", this.regimen);
			
			if(this.nuevo)
				this.tipoRegimenDAO.guardar(this.regimen);
			else
				this.tipoRegimenDAO.actualizar(this.regimen);
			
			regimenes = this.tipoRegimenDAO.buscarTodos();
			
			mensaje = "El tipo de régimen se guardó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			PrimeFaces.current().executeScript("PF('dgTipoRegimen').hide()");
		} catch(Exception ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-tipoRegimen");
		}
	}

	public List<TipoRegimenDTO> getRegimenes() {
		return regimenes;
	}

	public void setRegimenes(List<TipoRegimenDTO> regimenes) {
		this.regimenes = regimenes;
	}

	public TipoRegimenDTO getRegimen() {
		return regimen;
	}

	public void setRegimen(TipoRegimenDTO regimen) {
		this.regimen = regimen;
	}

}
