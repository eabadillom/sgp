package mx.com.ferbo.controller.incidencias;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.com.ferbo.business.registro.RegistroBL;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.ManageStatus;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

/**
 *
 * @author alberto
 */
@Named
@ViewScoped
public class IncidenciasAusenciasBean implements Serializable 
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(IncidenciasAusenciasBean.class);
    
    private DetEmpleado empleadoSelected;
    private DetRegistro registroAusencia;
    private List<DetRegistro> listRegistro;
    private List<DetRegistro> listRegistroFiltrada;
    private ManageStatus status = new ManageStatus();
    
    private Date fechaInicio;
    private Date fechaFin;
    private String empleadoAsistenciaTXT = "";
    
    @PostConstruct
    public void init() {
        this.empleadoSelected = (DetEmpleado) getValorEnSesion("empleado");
        log.info("El empleado {} entrando a la sección de ausencias", this.empleadoSelected.getNombre());
    }
    
    public void cargar(Date inicio, Date fin) {
        this.fechaInicio = inicio;
        this.fechaFin = fin;
        this.consultarRegistros();
    }
    
    public Object getValorEnSesion(String nombre) {
        return FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get(nombre)
                ;
    }
    
    public void consultarRegistros(){
        DateUtil.setTime(this.fechaInicio, 0, 0, 0);
        DateUtil.setTime(this.fechaFin, 0, 0, 0);
        this.listRegistro = RegistroBL.buscarPorEmpleadoAusencias(this.fechaInicio, this.fechaFin);
    }
    
    public List<DetRegistro> filtrarRegistros() {
        return RegistroBL.filtrarAusencias(listRegistro, empleadoAsistenciaTXT);
    }
    
    public void editarRegistro(DetRegistro registroAsistencia) {
        this.registroAusencia = registroAsistencia;
        log.info("Editando un registro de asistencia: {}", this.registroAusencia);
    }

    public void actualizarRegistro() {
        try {
            RegistroBL.actualizarRegistroAsistencia(this.registroAusencia);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Registro", "Se actualizo el registro de asistencia");
        } catch (SGPException ex) {
            log.warn("Error al guardar el registro de asistencia del empleado: {}", this.registroAusencia.getEmpleado() != null ? this.registroAusencia.getEmpleado().getNumEmpleado() : null);
            log.warn("EX-0030: ", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Registro", "Error al actualizar el registro, contacte al administrador de sistemas");
        } finally {
            PrimeFaces.current().executeScript("PF('dialogoJustificar').hide()");
            PrimeFaces.current().ajax().update("formIncidencias:messages", "formIncidencias:tabViewI:dtRegistro");
        }
    }
    
    public DetRegistro getRegistroAusencia() {
        return registroAusencia;
    }

    public void setRegistroAusencia(DetRegistro registroAusencia) {
        this.registroAusencia = registroAusencia;
    }

    public List<DetRegistro> getListRegistro() {
        return listRegistro;
    }

    public void setListRegistro(List<DetRegistro> listRegistro) {
        this.listRegistro = listRegistro;
    }

    public List<DetRegistro> getListRegistroFiltrada() {
        return listRegistroFiltrada;
    }

    public void setListRegistroFiltrada(List<DetRegistro> listRegistroFiltrada) {
        this.listRegistroFiltrada = listRegistroFiltrada;
    }
    
    public String getEmpleadoAsistenciaTXT() {
        return empleadoAsistenciaTXT;
    }

    public void setEmpleadoAsistenciaTXT(String empleadoAsistenciaTXT) {
        this.empleadoAsistenciaTXT = empleadoAsistenciaTXT;
    }
    
    public ManageStatus getStatus() {
        return status;
    }

    public void setStatus(ManageStatus status) {
        this.status = status;
    }
    
}
