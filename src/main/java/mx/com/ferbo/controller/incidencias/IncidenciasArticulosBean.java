package mx.com.ferbo.controller.incidencias;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.com.ferbo.business.incidencia.IncidenciaBL;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
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
public class IncidenciasArticulosBean implements Serializable 
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(IncidenciasArticulosBean.class);
    
    private static final String ARTICULO = "A";
    
    private DetEmpleado empleadoSesion;
    private DetEmpleado empleadoSelected;
    private DetIncidencia incidenciaSelected;
    private List<DetIncidencia> listaArticulos;
    private ManageStatus status = new ManageStatus();
    
    private Date fechaInicio;
    private Date fechaFin;
    private String filtroEmpleado = "";
    
    @PostConstruct
    public void init() {
        this.empleadoSesion = (DetEmpleado) getValorEnSesion("empleado");
        log.info("El empleado {} entrando a la sección de articulos", this.empleadoSesion.getNombre());
    }
    
    public void cargar(Date inicio, Date fin) {
        this.fechaInicio = inicio;
        this.fechaFin = fin;
        this.consultarArticulos();
    }
    
    public Object getValorEnSesion(String nombre) {
        return FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get(nombre)
                ;
    }
    
    public void consultarArticulos(){
        DateUtil.setTime(this.fechaInicio, 0, 0, 0);
        DateUtil.setTime(this.fechaFin, 11, 59, 59);
        this.listaArticulos = IncidenciaBL.consultarIncidencias(this.fechaInicio, this.fechaFin, ARTICULO);
    }
    
    public List<DetIncidencia> filtrarArticulos(){
        return IncidenciaBL.filtrarPorEmpleado(this.listaArticulos, this.filtroEmpleado);
    }
    
    public void visualizaDialog(DetIncidencia solicitudIncidencia) {
        try {
            this.incidenciaSelected = solicitudIncidencia;
            this.empleadoSelected = solicitudIncidencia.getEmpleado();
            switch (this.incidenciaSelected.getTipoIncidencia().getClave()) {
                case IncidenciaBL.TP_ARTICULO: // Tipo Articulos
                    PrimeFaces.current().executeScript("PF('dialogArticulos').show();");
                    break;
                default:
                    log.warn("EX-0023: Error al seleccionar una opción");
                    throw new SGPException("Error al abrir un articulo");
            }
        } catch (SGPException e) {
            log.warn("Error al abrir el registro de incidencia del empleado: {}", this.empleadoSelected.getNumEmpleado() != null ? this.empleadoSelected.getNumEmpleado() : null);
            log.warn("EX-0032: {}, ", e.getMessage());
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Incidencia", e.getMessage());
        } catch (Exception ex) {
            log.warn("Error al abrir el registro de incidencia del empleado: {}", this.empleadoSelected.getNumEmpleado() != null ? this.empleadoSelected.getNumEmpleado() : null);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Incidencia", "Consulte al administrador de sistemas");
        } finally{
            PrimeFaces.current().ajax().update("formIncidencias:messages");
        }
    }
    
    public void guardarEstatusArticulo(boolean aprobada) {
        try {
            String mensaje = aprobada ? "Solicitud aprobada correctamente" : "Solicitud rechazada correctamente";
            
            IncidenciaBL.actualizarIncidencia(this.incidenciaSelected, this.empleadoSesion, aprobada);
            
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Incidencia", mensaje);
        } catch (SGPException ex) {
            log.warn("Error al guardar el status del registro del artículo del empleado: {}", this.empleadoSelected.getNumEmpleado() != null ? this.empleadoSelected.getNumEmpleado() : null);
            log.warn("EX-0029: ", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Incidencia", "Error al actualizar la solicitud");
        } finally {
            this.consultarArticulos();
            PrimeFaces.current().ajax().update("formIncidencias:messages", "formIncidencias:tabViewI:dtArticulosSolicitados");
            PrimeFaces.current().executeScript("PF('dialogArticulos').hide()");
        }
    }

    public DetIncidencia getIncidenciaSelected() {
        return incidenciaSelected;
    }

    public void setIncidenciaSelected(DetIncidencia incidenciaSelected) {
        this.incidenciaSelected = incidenciaSelected;
    }

    public List<DetIncidencia> getListaArticulos() {
        return listaArticulos;
    }

    public void setListaArticulos(List<DetIncidencia> listaArticulos) {
        this.listaArticulos = listaArticulos;
    }
    
    public ManageStatus getStatus() {
        return status;
    }

    public void setStatus(ManageStatus status) {
        this.status = status;
    }

    public String getFiltroEmpleado() {
        return filtroEmpleado;
    }

    public void setFiltroEmpleado(String filtroEmpleado) {
        this.filtroEmpleado = filtroEmpleado;
    }
    
}
