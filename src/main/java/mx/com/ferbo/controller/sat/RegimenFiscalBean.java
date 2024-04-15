package mx.com.ferbo.controller.sat;

import java.io.Serializable;
import java.util.Date;
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

import mx.com.ferbo.dao.sat.RegimenFiscalDAO;
import mx.com.ferbo.dto.sat.RegimenFiscalDTO;

@Named(value = "regimenFiscalBean")
@ViewScoped
public class RegimenFiscalBean implements Serializable{

	private static final long serialVersionUID = -407357242159744321L;
	private static Logger log = LogManager.getLogger(RegimenFiscalBean.class);
	private String contextPath = null;
	private RegimenFiscalDAO regimenDAO;
	private List<RegimenFiscalDTO> regimenes;
	private RegimenFiscalDTO regimen;
	private boolean nuevo = false;
	
	public RegimenFiscalBean() {
		regimenDAO = new RegimenFiscalDAO();
	}
	
	@PostConstruct
	public void init() {
		contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		regimenes = regimenDAO.buscarActivo(new Date());
	}
	
	public void nuevo() {
		this.regimen = new RegimenFiscalDTO();
		this.nuevo = true;
	}
	
	public void editar() {
		log.info("Editando regimen fiscal: {}", this.regimen);
		this.nuevo = false;
	}
	
	public void guardar() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Guardar regimen fiscal";
		
		try {
			log.info("Guardando regimen fiscal: {}", this.regimen);
			
			if(this.nuevo)
				regimenDAO.guardar(this.regimen);
			else
				regimenDAO.actualizar(this.regimen);
			
			regimenes = regimenDAO.buscarActivo(new Date());
			
			mensaje = "El régimen fiscal se guardó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			PrimeFaces.current().executeScript("PF('dgRegimenFiscal').hide()");
		} catch(Exception ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-regimenFiscal");
		}
	}

	public List<RegimenFiscalDTO> getRegimenes() {
		return regimenes;
	}

	public void setRegimenes(List<RegimenFiscalDTO> regimenes) {
		this.regimenes = regimenes;
	}

	public RegimenFiscalDTO getRegimen() {
		return regimen;
	}

	public void setRegimen(RegimenFiscalDTO regimen) {
		this.regimen = regimen;
	}

}
