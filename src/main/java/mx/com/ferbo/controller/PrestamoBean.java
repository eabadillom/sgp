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

import mx.com.ferbo.dao.EmpleadoDAO;
import mx.com.ferbo.dao.PeriodicidadPagoDAO;
import mx.com.ferbo.dao.PrestamoDAO;
import mx.com.ferbo.dao.TipoPrestamoDAO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import mx.com.ferbo.dto.PeriodicidadPagoDTO;
import mx.com.ferbo.dto.PrestamoDTO;
import mx.com.ferbo.dto.TipoPrestamoDTO;

@Named(value = "prestamoBean")
@ViewScoped
public class PrestamoBean implements Serializable {

	private static final long serialVersionUID = -8738629687850811003L;
	private static Logger log = LogManager.getLogger(PrestamoBean.class);
	
	private List<DetEmpleadoDTO> empleados;
	private List<DetEmpleadoDTO> empleadosSelection;
	private EmpleadoDAO empleadoDAO;
	private DetEmpleadoDTO empleado;
	
	private List<TipoPrestamoDTO> tiposPrestamo;
	private TipoPrestamoDTO tipoPrestamo;
	private TipoPrestamoDAO tipoPrestamoDAO;
	
	private List<PeriodicidadPagoDTO> periodicidadesPago;
	private PeriodicidadPagoDAO periodicidadPagoDAO;
	
	private PrestamoDTO prestamo = null;
	private PrestamoDAO prestamoDAO = null;
	private List<PrestamoDTO> prestamos = null;
	
	private String accion = null;
	
	public PrestamoBean() {
		this.empleadoDAO = new EmpleadoDAO();
		this.tipoPrestamoDAO = new TipoPrestamoDAO();
		this.periodicidadPagoDAO = new PeriodicidadPagoDAO();
		this.prestamoDAO = new PrestamoDAO();
	}
	
	@PostConstruct
	public void init() {
		this.empleados = empleadoDAO.buscarActivo();
		this.tiposPrestamo = tipoPrestamoDAO.buscarTodos();
		this.cargaLista();
	}
	
	public void cargaLista() {
		this.periodicidadesPago = periodicidadPagoDAO.buscarTodos(new Date());
		this.accion = null;
	}
	
	public void nuevoPrestamo() {
		this.prestamo = new PrestamoDTO();
		this.accion = "N";
	}
	
	public void mostrarFechas() {
		log.info("Fecha inicio: {}", this.prestamo.getFechaInicio());
		log.info("Fecha Fin   : {}", this.prestamo.getFechaFin());
	}
	
	public void editarPrestamo(DetEmpleadoDTO empleado) {
		this.empleado = empleado;
		this.prestamo = new PrestamoDTO();
		this.prestamo.setEmpleado(empleado);
		log.info("Cargando información de préstamo INFONAVIT del empleado {}, prestamo: {}", this.empleado, this.prestamo);
		this.accion = "E";
		
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
			
			if("N".equalsIgnoreCase(this.accion))
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

	public List<DetEmpleadoDTO> getEmpleados() {
		return empleados;
	}

	public void setEmpleados(List<DetEmpleadoDTO> empleados) {
		this.empleados = empleados;
	}

	public DetEmpleadoDTO getEmpleado() {
		return empleado;
	}

	public void setEmpleado(DetEmpleadoDTO empleado) {
		this.empleado = empleado;
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

	public PrestamoDTO getPrestamo() {
		return prestamo;
	}

	public void setPrestamo(PrestamoDTO prestamo) {
		this.prestamo = prestamo;
	}

	public List<PeriodicidadPagoDTO> getPeriodicidadesPago() {
		return periodicidadesPago;
	}

	public void setPeriodicidadesPago(List<PeriodicidadPagoDTO> periodicidadesPago) {
		this.periodicidadesPago = periodicidadesPago;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public List<DetEmpleadoDTO> getEmpleadosSelection() {
		return empleadosSelection;
	}

	public void setEmpleadosSelection(List<DetEmpleadoDTO> empleadosSelection) {
		this.empleadosSelection = empleadosSelection;
	}

	public List<PrestamoDTO> getPrestamos() {
		return prestamos;
	}

	public void setPrestamos(List<PrestamoDTO> prestamos) {
		this.prestamos = prestamos;
	}

}
