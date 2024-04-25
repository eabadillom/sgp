package mx.com.ferbo.controller.sistema;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.EmpleadoDAO;
import mx.com.ferbo.dto.DatoEmpresaDTO;
import mx.com.ferbo.dto.DetEmpleadoDTO;

@Named(value = "updEmpleado")
@ViewScoped
public class ActualizaInfoEmpleadoBean implements Serializable {

	private static final long serialVersionUID = -8904812106747715167L;
	private static Logger log = LogManager.getLogger(ActualizaInfoEmpleadoBean.class);
	private String contextPath = null;
	private EmpleadoDAO empleadoDAO = null;
	private List<DetEmpleadoDTO> empleados = null;
	private DetEmpleadoDTO empleado = null;
	
	public ActualizaInfoEmpleadoBean() {
		this.empleadoDAO = new EmpleadoDAO();
	}
	
	@PostConstruct
	public void init() {
		this.contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		empleados = empleadoDAO.buscarTodos();
	}
	
	public void actualizar() {
		DatoEmpresaDTO datoEmpresa = null;
		
		try {
			log.info("Actualizando empleado: {}", this.empleado);
			this.empleado = this.empleadoDAO.buscarPorId(this.empleado.getIdEmpleado(), true);
			
			if(this.empleado.getDatoEmpresa() != null)
				return;
			
			datoEmpresa = new DatoEmpresaDTO();
			
			datoEmpresa.setArea(empleado.getCatAreaDTO());
			datoEmpresa.setEmpresa(empleado.getCatEmpresaDTO());
			datoEmpresa.setPerfil(empleado.getCatPerfilDTO());
			datoEmpresa.setPlanta(empleado.getCatPlantaDTO());
			datoEmpresa.setPuesto(empleado.getCatPuestoDTO());
			
			datoEmpresa.setFechaIngreso(empleado.getFechaIngreso());
			datoEmpresa.setNss(empleado.getNss());
			datoEmpresa.setRfc(empleado.getRfc());
			datoEmpresa.setSalarioDiario(empleado.getSueldoDiario());
			
			this.empleado.setDatoEmpresa(datoEmpresa);
			this.empleadoDAO.actualizar(this.empleado);
			
		} catch(Exception ex) {
			
		} finally {
			
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
	
	
}
