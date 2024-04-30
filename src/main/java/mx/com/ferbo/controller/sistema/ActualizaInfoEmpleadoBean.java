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

import mx.com.ferbo.dao.EmpleadoDAO;
import mx.com.ferbo.dto.DatoEmpresaDTO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import mx.com.ferbo.dto.EmpleadoFotoDTO;

@Named(value = "updEmpleado")
@ViewScoped
public class ActualizaInfoEmpleadoBean implements Serializable {

	private static final long serialVersionUID = -8904812106747715167L;
	private static Logger log = LogManager.getLogger(ActualizaInfoEmpleadoBean.class);
	private EmpleadoDAO empleadoDAO = null;
	private List<DetEmpleadoDTO> empleados = null;
	private DetEmpleadoDTO empleado = null;
	
	public ActualizaInfoEmpleadoBean() {
		this.empleadoDAO = new EmpleadoDAO();
	}
	
	@PostConstruct
	public void init() {
		empleados = empleadoDAO.buscarTodos();
	}
	
	public void actualizar() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Actualizar datos del empleado";
		
		DatoEmpresaDTO datoEmpresa = null;
		EmpleadoFotoDTO empleadoFoto = null;
		
		try {
			log.info("Actualizando empleado: {}", this.empleado);
			this.empleado = this.empleadoDAO.buscarPorId(this.empleado.getIdEmpleado(), true);
			
			if(this.empleado.getDatoEmpresa() == null) {
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
			}
				
			if(this.empleado.getEmpleadoFoto() == null) {
				empleadoFoto = new EmpleadoFotoDTO();
				empleadoFoto.setFotografia(empleado.getFotografia());
				this.empleado.setEmpleadoFoto(empleadoFoto);
			}
			
			this.empleadoDAO.actualizar(this.empleado);
			
			mensaje = "La información del empleado se actualizó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch(Exception ex) {
			log.error("Problema para obtener los datos del empleado...", ex);
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
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
