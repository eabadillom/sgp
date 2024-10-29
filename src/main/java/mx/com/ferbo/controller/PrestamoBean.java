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
import org.primefaces.event.TabChangeEvent;

import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.PeriodicidadPagoDAO;
import mx.com.ferbo.dao.n.PrestamoDAO;
import mx.com.ferbo.dao.n.TipoPrestamoDAO;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.CatPeriodicidadPago;
import mx.com.ferbo.model.DetPrestamo;
import mx.com.ferbo.model.CatTipoPrestamo;

@Named(value = "prestamoBean")
@ViewScoped
public class PrestamoBean implements Serializable {

	private static final long serialVersionUID = -8738629687850811003L;
	private static Logger log = LogManager.getLogger(PrestamoBean.class);
	
	private List<DetEmpleado> empleados;
	private List<DetEmpleado> empleadosSelection;
	private EmpleadoDAO empleadoDAO;
	private DetEmpleado empleado;
	
	private List<CatTipoPrestamo> tiposPrestamo;
	private CatTipoPrestamo tipoPrestamo;
	private TipoPrestamoDAO tipoPrestamoDAO;
	
	private List<CatPeriodicidadPago> periodicidadesPago;
	private PeriodicidadPagoDAO periodicidadPagoDAO;
	
	private DetPrestamo prestamo = null;
	private PrestamoDAO prestamoDAO = null;
	private List<DetPrestamo> prestamos = null;
	
	private String accion = null;
	
	public PrestamoBean() {
		this.empleadoDAO = new EmpleadoDAO();
		this.tipoPrestamoDAO = new TipoPrestamoDAO();
		this.periodicidadPagoDAO = new PeriodicidadPagoDAO();
		this.prestamoDAO = new PrestamoDAO();
	}
	
	@PostConstruct
	public void init() {
		this.empleados = empleadoDAO.buscarTodos(false);
		this.tiposPrestamo = tipoPrestamoDAO.buscarTodos();
		this.cargaLista();
	}
	
	public void cargaLista() {
		this.periodicidadesPago = periodicidadPagoDAO.buscarTodosActivos(new Date());
		this.accion = null;
	}
	
	public void nuevoPrestamo() {
		this.prestamo = new DetPrestamo();
		this.accion = "N";
	}
	
	public void mostrarFechas() {
		log.info("Fecha inicio: {}", this.prestamo.getFechaInicio());
		log.info("Fecha Fin   : {}", this.prestamo.getFechaFin());
	}
	
	public void editarPrestamo(DetEmpleado empleado) {
		this.empleado = empleado;
		this.prestamo = new DetPrestamo();
		this.prestamo.setEmpleado(empleado);
		log.info("Cargando información de préstamo INFONAVIT del empleado {}, prestamo: {}", this.empleado, this.prestamo);
		this.accion = "N";
		
		this.prestamos = prestamoDAO.buscar(this.empleado.getIdEmpleado());
		log.info("Registros INFONAVIT: {}", this.prestamos.size());
	}
	
	public void guardaPrestamo() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Guardar préstamo";
		
		try {
			log.info("Guardando préstamo: {}", this.prestamo);
			
			if("N".equalsIgnoreCase(this.accion) || this.prestamo.getIdPrestamo() == null)
				this.prestamoDAO.guardar(this.prestamo);
			else
				this.prestamoDAO.actualizar(this.prestamo);
			
			this.cargaLista();
			this.accion = null;
			
			mensaje = "El préstamo se guardó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			PrimeFaces.current().executeScript("PF('dgPrestamo').hide()");
		} catch(Exception ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dt-empleado");
		}
	}
	
	public void onTabChange(TabChangeEvent event) {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Préstamo";
		
		String tabId = null;
		try {
			tabId = event.getTab().getId();
			
			if("tab-agregar".equalsIgnoreCase(tabId)) {
				mensaje = "Agregar préstamo";
				this.accion = "E";
			} else if("tab-prestamos".equalsIgnoreCase(tabId)) {
				this.prestamos = prestamoDAO.buscar(this.empleado.getIdEmpleado());
				log.info("Registros INFONAVIT: {}", this.prestamos.size());
				
				mensaje = "Préstamos del empleado";
			} else {
				log.info("No hay etiqueta seleccionada.");
			}
			
			severity = FacesMessage.SEVERITY_INFO;
		} catch(Exception ex) {
			log.error("Ocurrió un problema con la consulta del préstamo...", ex);
			mensaje = "Ocurrió un problema con la consulta del préstamo.";
			severity = FacesMessage.SEVERITY_WARN;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update(":form:messages");
		}
	}

	public List<DetEmpleado> getEmpleados() {
		return empleados;
	}

	public void setEmpleados(List<DetEmpleado> empleados) {
		this.empleados = empleados;
	}

	public DetEmpleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(DetEmpleado empleado) {
		this.empleado = empleado;
	}
	
	public List<CatTipoPrestamo> getTiposPrestamo() {
		return tiposPrestamo;
	}

	public void setTiposPrestamo(List<CatTipoPrestamo> tiposPrestamo) {
		this.tiposPrestamo = tiposPrestamo;
	}

	public CatTipoPrestamo getTipoPrestamo() {
		return tipoPrestamo;
	}

	public void setTipoPrestamo(CatTipoPrestamo tipoPrestamo) {
		this.tipoPrestamo = tipoPrestamo;
	}

	public DetPrestamo getPrestamo() {
		return prestamo;
	}

	public void setPrestamo(DetPrestamo prestamo) {
		this.prestamo = prestamo;
	}

	public List<CatPeriodicidadPago> getPeriodicidadesPago() {
		return periodicidadesPago;
	}

	public void setPeriodicidadesPago(List<CatPeriodicidadPago> periodicidadesPago) {
		this.periodicidadesPago = periodicidadesPago;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public List<DetEmpleado> getEmpleadosSelection() {
		return empleadosSelection;
	}

	public void setEmpleadosSelection(List<DetEmpleado> empleadosSelection) {
		this.empleadosSelection = empleadosSelection;
	}

	public List<DetPrestamo> getPrestamos() {
		return prestamos;
	}

	public void setPrestamos(List<DetPrestamo> prestamos) {
		this.prestamos = prestamos;
	}

}
