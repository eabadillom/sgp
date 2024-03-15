package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.EmpleadoDAO;
import mx.com.ferbo.dto.DetEmpleadoDTO;

@Named(value = "pInfonavitBean")
@ViewScoped
public class PrestamoInfonavitBean implements Serializable {

	private static final long serialVersionUID = -8738629687850811003L;
	private static Logger log = LogManager.getLogger(PrestamoInfonavitBean.class);
	
	private List<DetEmpleadoDTO> empleados;
	private EmpleadoDAO empleadoDAO;
	private DetEmpleadoDTO empleado;
	
	public PrestamoInfonavitBean() {
		this.empleadoDAO = new EmpleadoDAO();
	}
	
	@PostConstruct
	public void init() {
		this.empleados = empleadoDAO.buscarActivo();
	}
	
	public void cargaPrestamo() {
		log.info("Cargando información de préstamo INFONAVIT del empleado {}", this.empleado);
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

}
