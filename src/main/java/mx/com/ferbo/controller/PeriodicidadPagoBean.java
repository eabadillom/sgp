package mx.com.ferbo.controller;

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

import mx.com.ferbo.dao.PeriodicidadPagoDAO;
import mx.com.ferbo.dto.PeriodicidadPagoDTO;
import mx.com.ferbo.util.DateUtils;

@Named(value = "perPagoBean")
@ViewScoped
public class PeriodicidadPagoBean implements Serializable{

	private static final long serialVersionUID = 7841681509085397650L;
	private static Logger log = LogManager.getLogger(PeriodicidadPagoBean.class);
	private List<PeriodicidadPagoDTO> periodicidades;
	private PeriodicidadPagoDTO periodo;
	private PeriodicidadPagoDAO periodicidadDAO;
	private Date fechaActual;
	private String accion;
	
	
	public PeriodicidadPagoBean() {
		this.periodicidadDAO = new PeriodicidadPagoDAO();
	}
	
	@PostConstruct
	public void init() {
		this.fechaActual = new Date();
		DateUtils.setTime(fechaActual, 0, 0, 0, 0);
		this.cargaLista();
	}
	
	public void cargaLista() {
		this.periodicidades = periodicidadDAO.buscarActivo(fechaActual);
	}
	
	public void nuevo() {
		log.info("Agregando nueva periodicidad de pago...");
		this.periodo = new PeriodicidadPagoDTO();
		this.accion = "N";
	}
	
	public void editar() {
		log.info("Editando periodicidad de pago: {}", this.periodo);
		this.accion = "E";
	}
	
	public void guardar() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Guardar periodicidad de pago";
		
		try {
			log.info("Guardando periodicidad de pago: {}", this.periodo);
			
			if("N".equalsIgnoreCase(this.accion))
				this.periodicidadDAO.guardar(this.periodo);
			else
				this.periodicidadDAO.actualizar(this.periodo);
			
			this.cargaLista();
			this.accion = null;
			
			mensaje = "La periodicidad de pago se guardó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			PrimeFaces.current().executeScript("PF('dgPeriodicidad').hide()");
		} catch(Exception ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-periodicidad");
		}
	}
	
	public void cancelar() {
		log.info("Cancelando guardado de periodicidad de pago.");
	}

	public List<PeriodicidadPagoDTO> getPeriodicidades() {
		return periodicidades;
	}

	public void setPeriodicidades(List<PeriodicidadPagoDTO> periodicidades) {
		this.periodicidades = periodicidades;
	}

	public PeriodicidadPagoDTO getPeriodo() {
		return periodo;
	}

	public void setPeriodo(PeriodicidadPagoDTO periodo) {
		this.periodo = periodo;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}
}
