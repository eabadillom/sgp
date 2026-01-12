package mx.com.ferbo.controller;

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

import mx.com.ferbo.business.catalogos.Subsidio2BL;
import mx.com.ferbo.dao.n.Subsidio2DAO;
import mx.com.ferbo.model.CatSubsidio2;
import mx.com.ferbo.util.SGPException;


@Named(value = "subsidio2Bean")
@ViewScoped
public class Subsidio2Bean implements Serializable {

	private static final long serialVersionUID = -4892707760091002440L;
	private static Logger log = LogManager.getLogger(Subsidio2Bean.class);
	
	private CatSubsidio2 subsidio = null;
	private Subsidio2DAO subsidioDAO = null;
	private List<CatSubsidio2> subsidios = null;
	
	public Subsidio2Bean() {
		this.subsidioDAO = new Subsidio2DAO();		
	}
	
	@PostConstruct
	public void init() {
		this.subsidios = this.subsidioDAO.buscarTodos();
	}
	
	public void nuevo() {
		this.subsidio = new CatSubsidio2();
		log.info("Creando nuevo registro de subsidio al empleo.");
	}
	
	public void guardar() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Subsidio";
		
		try {
			
			mensaje = "Nomina cargada correctamente.";
    		severity = FacesMessage.SEVERITY_INFO;
    		
    		Subsidio2BL.guardar(subsidio);
    		
    		this.subsidios = subsidioDAO.buscarTodos();
    		
		} catch(SGPException ex){
			log.error("Problema para procesar el registro de subsidio al empleo...", ex);
			mensaje = ex.getMessage();
    		severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			log.error("Problema para procesar el registro de subsidio al empleo...", ex);
			mensaje = "Hay un problema para procesar la nómina.";
    		severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
    		FacesContext.getCurrentInstance().addMessage(null, message);
    		PrimeFaces.current().ajax().update("form:messages");
    		PrimeFaces.current().executeScript("PF('dgSubsidio').hide()");
		}
	}
	
	public List<CatSubsidio2> getSubsidios() {
		return subsidios;
	}

	public void setSubsidios(List<CatSubsidio2> subsidios) {
		this.subsidios = subsidios;
	}

	public CatSubsidio2 getSubsidio() {
		return subsidio;
	}

	public void setSubsidio(CatSubsidio2 subsidio) {
		this.subsidio = subsidio;
	}
	
	
	

}
