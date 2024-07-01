package mx.com.ferbo.controller.sistema;

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

import mx.com.ferbo.dao.TipoPrestamoDAO;
import mx.com.ferbo.dao.sat.TipoDeduccionDAO;
import mx.com.ferbo.dto.TipoPrestamoDTO;
import mx.com.ferbo.dto.sat.TipoDeduccionDTO;
import mx.com.ferbo.util.SGPException;

@Named(value = "tipoPrestamoBean")
@ViewScoped
public class TipoPrestamoBean implements Serializable {

	private static final long serialVersionUID = 5769168586730592300L;
	private static Logger log = LogManager.getLogger(TipoPrestamoBean.class);
	
	private TipoPrestamoDAO tipoPrestamoDAO;
	private List<TipoPrestamoDTO> tiposPrestamo = null;
	private TipoPrestamoDTO tipoPrestamo = null;
	
	private TipoDeduccionDAO tipoDeduccionDAO = null;
	private List<TipoDeduccionDTO> tiposDeduccion = null;
	
	private boolean crear = false;
	
	public TipoPrestamoBean() {
		this.tipoPrestamoDAO = new TipoPrestamoDAO();
		this.tipoDeduccionDAO = new TipoDeduccionDAO();
	}
	
	@PostConstruct
	public void init() {
		this.tiposDeduccion = tipoDeduccionDAO.buscarTodos();
		this.tiposPrestamo = tipoPrestamoDAO.buscarTodos();
	}
	
	public void nuevo() {
		log.info("Creando nuevo tipo de préstamo...");
		this.crear = true;
	}
	
	public void editar() {
		log.info("Editando el tipo de préstamo: {}", this.tipoPrestamo);
		this.crear = false;
	}
	
	public void guardar() {
		FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Guardar tipo de préstamo...";
        
        try {
        	if(this.tipoPrestamo == null)
        		throw new SGPException("No hay un tipo de préstamo seleccionado.");
        	
        	if(this.tipoPrestamo.getClave() == null || this.tipoPrestamo.getClave().equalsIgnoreCase(""))
        		throw new SGPException("Debe indicar la clave del tipo de préstamo");
        	
        	if(this.tipoPrestamo.getDescripcion() == null || this.tipoPrestamo.getDescripcion().equalsIgnoreCase(""))
        		throw new SGPException("Debe indicar una descripción del tipo de préstamo");
        	
        	if(this.tipoPrestamo.getTipoDeduccion() == null)
        		throw new SGPException("Debe seleccionar un tipo de deducción (SAT) asociado al tipo de préstamo.");
        	
        	
        	if(this.crear)
        		tipoPrestamoDAO.guardar(this.tipoPrestamo);
        	else
        		tipoPrestamoDAO.actualizar(this.tipoPrestamo);
        	
        	mensaje = "La información se guardó correctamente.";
        	severity = FacesMessage.SEVERITY_INFO;
        	PrimeFaces.current().executeScript("PF('dlgTipoPrestamo').hide()");
        } catch(SGPException ex) {
        	mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
        } catch(Exception ex) {
        	mensaje = "Error en el registro consulta al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
            log.error("Problema para guardar el tipo de préstamo...", ex);
        } finally {
        	message = new FacesMessage(severity,titulo,mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages","form:dt-tipoPrestamo");
        }
	}

	public List<TipoPrestamoDTO> getTiposPrestamo() {
		return tiposPrestamo;
	}

	public void setTiposPrestamo(List<TipoPrestamoDTO> tiposPrestamo) {
		this.tiposPrestamo = tiposPrestamo;
	}

	public TipoPrestamoDTO getTipoPrestamo() {
		return tipoPrestamo;
	}

	public void setTipoPrestamo(TipoPrestamoDTO tipoPrestamo) {
		this.tipoPrestamo = tipoPrestamo;
	}

	public List<TipoDeduccionDTO> getTiposDeduccion() {
		return tiposDeduccion;
	}

	public void setTiposDeduccion(List<TipoDeduccionDTO> tiposDeduccion) {
		this.tiposDeduccion = tiposDeduccion;
	}

	public boolean isCrear() {
		return crear;
	}

	public void setCrear(boolean crear) {
		this.crear = crear;
	}
	
	
}
