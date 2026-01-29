package mx.com.ferbo.controller.sistema;

import java.io.Serializable;
import java.math.BigDecimal;
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

import mx.com.ferbo.business.empleado.EmpleadoBL;
import mx.com.ferbo.business.empleado.SalarioMinimoBL;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.SalarioMinimoDAO;
import mx.com.ferbo.model.CatSalarioMinimo;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetSalarioDiario;
import mx.com.ferbo.util.SGPException;

@Named(value = "salarioMinBean")
@ViewScoped
public class SalarioMinimoBean implements Serializable {

    private static final long serialVersionUID = -4884705779733680337L;
    private static Logger log = LogManager.getLogger(SalarioMinimoBean.class);

    private List<CatSalarioMinimo> salarios;
    private CatSalarioMinimo salario;
    private SalarioMinimoDAO salarioDAO;
    private List<DetEmpleado> empleados;

    private Boolean salarioGeneral;
    private Boolean salarioFrontera;
    private EmpleadoDAO empleadoDAO;
    private BigDecimal salarioSugerido;
    private DetEmpleado empleado;
    private DetSalarioDiario salarioDiario;
    

    public SalarioMinimoBean() {
        salarioDAO = new SalarioMinimoDAO();
        empleadoDAO = new EmpleadoDAO();
        salario = new CatSalarioMinimo();
    }

    @PostConstruct
    public void init() {
        salarios = salarioDAO.buscarTodos();
        this.salarioGeneral = false;
        this.salarioFrontera = false;
        this.salarioSugerido = new BigDecimal("0.00");
        this.empleado = EmpleadoBL.build();
    }

    public void nuevo() {
        this.salario = new CatSalarioMinimo();
        log.info("Nuevo salario minimo: {}", this.salario);
    }

    public void editar() {
        log.info("Editar salario minimo: {}", this.salario);
    }

    public void guardar() {
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Guardar salario minimo";

        try {
            log.info("Guardando salario minimo: {}", this.salario);

            if (this.salario == null) {
                throw new SGPException("No se ha registrado información del salario minimo.");
            }

            if (this.salario.getVigencia() == null) {
                throw new SGPException("Debe indicar una vigencia");
            }

            if (this.salario.getZonaG() == null) {
                throw new SGPException("Debe indicar un importe para la Zona General");
            }

            if (this.salario.getZonaG().compareTo(BigDecimal.ZERO) <= 0) {
                throw new SGPException("El importe para la Zona General es incorrecto.");
            }

            if (this.salario.getZonaLFN() == null) {
                throw new SGPException("Debe indicar un importe para la Zona Libre Frontera Norte");
            }

            if (this.salario.getZonaLFN().compareTo(BigDecimal.ZERO) <= 0) {
                throw new SGPException("El importe para la Zona Libre Frontera Norte es incorrecto.");
            }

            if (this.salario.getId() == null) {
                salarioDAO.guardar(this.salario);
            } else {
                salarioDAO.actualizar(this.salario);
            }

            this.salarios = salarioDAO.buscarTodos();
            
            this.porDebajoMinimo();

            mensaje = "El salario minimo se guardó correctamente";
            severity = FacesMessage.SEVERITY_INFO;

            PrimeFaces.current().executeScript("PF('dgSalario').hide()");
        } catch (SGPException ex) {
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            mensaje = "Existe un problema para guardar el salario minimo.";
            severity = FacesMessage.SEVERITY_WARN;
            log.error("Problema para guardar el salario minimo...", ex);
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:dt-salario");
        }
    }

    public void eliminar() {
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Eliminar salario minimo";

        try {
            this.salarioDAO.eliminar(this.salario);

            this.salarios = salarioDAO.buscarTodos();

            mensaje = "El salario minimo se eliminó correctamente";
            severity = FacesMessage.SEVERITY_INFO;

            PrimeFaces.current().executeScript("PF('dgSalario').hide()");

        } catch (Exception ex) {
            mensaje = "Existe un problema para eliminar el salario minimo.";
            severity = FacesMessage.SEVERITY_WARN;
            log.error("Problema para eliminar el salario minimo...", ex);
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:dt-salario");
        }

        log.info("Eliminar salario minimo: {}", this.salario);

    }
    
    public String tituloDialogDebajoMinimo() {
    	if(this.salario == null)
    		return "";
    	return String.format("Empleados con salario inferior a $ %s (ZG), $ %s (ZLFN)", this.salario.getZonaG(), this.salario.getZonaLFN());
    }
    
    public void porDebajoMinimo() {

        try {
        	this.empleados = SalarioMinimoBL.validarSalariosMinimos(this.salario.getVigencia());

            this.salarioSugerido = new BigDecimal("0.00");

        } catch (SGPException sgpEx) {
            log.error("No se puedo obtener a los empleados con salario por debajo del minimo. " + sgpEx.getMessage());
        } catch (Exception ex) {
            log.error("Error desconocido...", ex);
        } finally {
        	PrimeFaces.current().ajax().update("form:dtEmpleadoPorDebajo");
        }
    }
    
    public void nuevoSalarioDiario(DetEmpleado empleado) {
    	this.salarioDiario = new DetSalarioDiario.Builder().build();
    }
    
    public void agregarSalarioDiario() {
    	try {
			EmpleadoBL.agregarSalarioDiario(this.empleado, this.salarioDiario);
			this.salarioDiario = new DetSalarioDiario.Builder().build();
		} catch (SGPException ex) {
			log.error("Hubo un problema para agregar el salario diario...", ex);
		}
    }

    public void actualizarSalarios() {

        if (this.empleados.isEmpty()) {
            return;
        }

        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Actualizar salario empleado";

        for (DetEmpleado empleado : this.empleados) {
            try {

                empleadoDAO.actualizar(empleado);
                mensaje = "El salario minimo de " + empleado.getNombre() + " " + empleado.getPrimerAp() + " " + empleado.getSegundoAp() + " se actualizo correctamente";
                severity = FacesMessage.SEVERITY_INFO;

            } catch (SGPException sgpEx) {

                mensaje = "El salario minimo de " + empleado.getNombre() + " " + empleado.getPrimerAp() + " " + empleado.getSegundoAp() + " no se actualizo correctamente";
                severity = FacesMessage.SEVERITY_ERROR;

                log.error("No se pudo actualizar el salario. " + sgpEx.getMessage());
            } finally {
                message = new FacesMessage(severity, titulo, mensaje);
                FacesContext.getCurrentInstance().addMessage(null, message);
                PrimeFaces.current().ajax().update("form:messages");
            }
        }  
        porDebajoMinimo();
    }

    public void limpiarVariables() {
        this.empleados = null;
        this.salarioSugerido = new BigDecimal("0.00");
        this.salarioGeneral = false;
        this.salarioFrontera = false;
    }

    public List<CatSalarioMinimo> getSalarios() {
        return salarios;
    }

    public void setSalarios(List<CatSalarioMinimo> salarios) {
        this.salarios = salarios;
    }

    public CatSalarioMinimo getSalario() {
        return salario;
    }

    public void setSalario(CatSalarioMinimo salario) {
        this.salario = salario;
    }

    public List<DetEmpleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<DetEmpleado> empleados) {
        this.empleados = empleados;
    }

    public Boolean getSalarioGeneral() {
        return salarioGeneral;
    }

    public void setSalarioGeneral(Boolean salarioGeneral) {
        this.salarioGeneral = salarioGeneral;
    }

    public Boolean getSalarioFrontera() {
        return salarioFrontera;
    }

    public void setSalarioFrontera(Boolean salarioFrontera) {
        this.salarioFrontera = salarioFrontera;
    }

    public BigDecimal getSalarioSugerido() {
        return salarioSugerido;
    }

    public void setSalarioSugerido(BigDecimal salarioSugerido) {
        this.salarioSugerido = salarioSugerido;
    }

	public DetEmpleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(DetEmpleado empleado) {
		this.empleado = empleado;
	}

	public DetSalarioDiario getSalarioDiario() {
		return salarioDiario;
	}

	public void setSalarioDiario(DetSalarioDiario salarioDiario) {
		this.salarioDiario = salarioDiario;
	}
}
