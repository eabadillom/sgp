package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.IncidenciaDAO;
import mx.com.ferbo.dao.n.TipoSolicitudDAO;
import mx.com.ferbo.model.CatTipoSolicitud;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.util.SGPException;

/**
 *
 * @author Gabriel
 */
@Named("incidenciaBean")
@ViewScoped
public class IncidenciaBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(IncidenciaBean.class);

    private final IncidenciaDAO incidenciaDAO;
    private DetIncidencia incidenciaSelected;
    private Date fechaSeleccionada;
    private List<DetIncidencia> lstIncidencias;
    private List<DetIncidencia> listaPermisos;
    private List<DetIncidencia> listaPrendas;
    private List<DetIncidencia> listaArticulos;
    private List<CatTipoSolicitud> lstTipoSol;
    private final TipoSolicitudDAO catTipoSolicitudDAO;
    private List<Date> lstRangoRegistro;
    private List<Integer> invalidDays;
    private Date minDate;

    private DetEmpleado empleadoSelected;
    private final HttpServletRequest httpServletRequest;
    private final EmpleadoDAO empleadoDAO;

    public IncidenciaBean() {
        incidenciaDAO = new IncidenciaDAO();
        catTipoSolicitudDAO = new TipoSolicitudDAO();
        minDate = new Date();

        empleadoDAO = new EmpleadoDAO();
        httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.empleadoSelected = (DetEmpleado) httpServletRequest.getSession(true).getAttribute("empleado");

        empleadoSelected = empleadoDAO.buscarPorId(empleadoSelected.getIdEmpleado());
    }

    @PostConstruct
    public void init() {
        consultaIncidencias();
        lstTipoSol = catTipoSolicitudDAO.buscarActivos();
        invalidDays = Arrays.asList(0);
    }

    private void consultaIncidencias() {
        lstIncidencias = incidenciaDAO.buscarTodos();

        listaPermisos = lstIncidencias.stream()
                .filter(objeto -> objeto.getIdTipo().getIdTipo().equals(1))
                .map(objeto -> objeto)
                .collect(Collectors.toList());
        
        listaPrendas = lstIncidencias.stream()
                .filter(objeto -> objeto.getIdTipo().getIdTipo().equals(3))
                .map(objeto -> objeto)
                .collect(Collectors.toList());
        
        listaArticulos = lstIncidencias.stream()
                .filter(objeto -> objeto.getIdTipo().getIdTipo().equals(4))
                .map(objeto -> objeto)
                .collect(Collectors.toList());
    }

    public void visualizaDialog() {
        switch (incidenciaSelected.getIdTipo().getIdTipo()) {
            // Tipo Permisos
            case 1:
                switch (incidenciaSelected.getIdSolPermiso().getIdTipoSolicitud().getIdTipoSolicitud()) {
                    case 1://PERMISO
                    case 3://INCAPACDAD CORTA
                        fechaSeleccionada = incidenciaSelected.getIdSolPermiso().getFechaInicio();
                        break;
                    default:
                        lstRangoRegistro = Arrays.asList(incidenciaSelected.getIdSolPermiso().getFechaInicio(), incidenciaSelected.getIdSolPermiso().getFechaFin());
                        break;
                }
                PrimeFaces.current().executeScript("PF('dialogPermisos').show();");
                break;
            // Tipo Vacaciones
            case 2:
                PrimeFaces.current().executeScript("PF('dialogPermisos').show();");
                break;
            // Tipo Prendas
            case 3:
                PrimeFaces.current().executeScript("PF('dialogPrendas').show();");
                break;
            // Tipo Articulos
            case 4:
                PrimeFaces.current().executeScript("PF('dialogArticulos').show();");
                break;
            default:
                log.warn("EX-0023: Error al seleccionar opción");
        }
    }

    public void guardarEstatusIncidencia(boolean aprobada) {
        incidenciaSelected.setIdEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
        try {
            incidenciaSelected.getIdEstatus().setIdEstatus(aprobada ? 2 : 3);
            incidenciaDAO.actualizar(incidenciaSelected);

            String mensaje = "";
            if (incidenciaSelected.getIdEstatus().getIdEstatus() == 2) {
                mensaje = "Solicitud aprobada correctamente";
            } else {
                mensaje = "Solicitud rechazada correctamente";
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensaje));
        } catch (SGPException ex) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al actualizar la solicitud"));
            log.warn("EX-0028: " + ex.getMessage() + ". Error al guardar el status del registro de la incidencia del empleado: " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
        }
        consultaIncidencias();
        PrimeFaces.current().ajax().update("formIncidencias:messages", "formIncidencias:tabViewI:dtIncidencias");
        if(incidenciaSelected.getIdSolPermiso().getIdSolicitud() != null){
            PrimeFaces.current().executeScript("PF('dialogPermisos').hide()");
        }else if(incidenciaSelected.getIdSolPrenda().getIdSolicitud() != null){
            PrimeFaces.current().executeScript("PF('dialogPrendas').hide()");
        }else if(incidenciaSelected.getIdSolArticulo().getIdSolicitud() != null){
            PrimeFaces.current().executeScript("PF('dialogArticulos').hide()");
        }
    }
    
    public void guardarEstatusArticulo(boolean aprobada) {
        incidenciaSelected.setIdEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
        try {
            incidenciaSelected.getIdEstatus().setIdEstatus(aprobada ? 2 : 3);
            incidenciaDAO.actualizar(incidenciaSelected);

            String mensaje = "";
            if (incidenciaSelected.getIdEstatus().getIdEstatus() == 2) {
                mensaje = "Solicitud aprobada correctamente";
            } else {
                mensaje = "Solicitud rechazada correctamente";
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensaje));
        } catch (SGPException ex) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al actualizar la solicitud"));
            log.warn("EX-0029: " + ex.getMessage() + ". Error al guardar el status del registro del artículo del empleado: " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
        }
        consultaIncidencias();
        PrimeFaces.current().ajax().update("formIncidencias:messages", "formIncidencias:tabViewI:dtArticulosSolicitados");
        PrimeFaces.current().executeScript("PF('dialogArticulos').hide()");
    }
    
    public void guardarEstatusPrenda(boolean aprobada) {
        incidenciaSelected.setIdEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
        try {
            incidenciaSelected.getIdEstatus().setIdEstatus(aprobada ? 2 : 3);
            incidenciaDAO.actualizar(incidenciaSelected);

            String mensaje = "";
            if (incidenciaSelected.getIdEstatus().getIdEstatus() == 2) {
                mensaje = "Solicitud aprobada correctamente";
            } else {
                mensaje = "Solicitud rechazada correctamente";
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensaje));
        } catch (SGPException ex) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al actualizar la solicitud"));
            log.warn("EX-0030: " + ex.getMessage() + ". Error al guardar el status del registro de la prenda del empleado: " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
        }
        consultaIncidencias();
        PrimeFaces.current().ajax().update("formIncidencias:messages", "formIncidencias:tabViewI:dtPrendasSolicitados");
        PrimeFaces.current().executeScript("PF('dialogPrendas').hide()");
    }

    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public DetIncidencia getIncidenciaSelected() {
        return incidenciaSelected;
    }

    public void setIncidenciaSelected(DetIncidencia incidenciaSelected) {
        this.incidenciaSelected = incidenciaSelected;
    }

    public List<DetIncidencia> getLstIncidencias() {
        return lstIncidencias;
    }

    public void setLstIncidencias(List<DetIncidencia> lstIncidencias) {
        this.lstIncidencias = lstIncidencias;
    }

    public List<DetIncidencia> getListaPermisos() {
        return listaPermisos;
    }

    public void setListaPermisos(List<DetIncidencia> listaPermisos) {
        this.listaPermisos = listaPermisos;
    }

    public List<DetIncidencia> getListaArticulos() {
        return listaArticulos;
    }

    public void setListaArticulos(List<DetIncidencia> listaArticulos) {
        this.listaArticulos = listaArticulos;
    }

    public Date getFechaSeleccionada() {
        return fechaSeleccionada;
    }

    public void setFechaSeleccionada(Date fechaSeleccionada) {
        this.fechaSeleccionada = fechaSeleccionada;
    }

    public List<Date> getLstRangoRegistro() {
        return lstRangoRegistro;
    }

    public void setLstRangoRegistro(List<Date> lstRangoRegistro) {
        this.lstRangoRegistro = lstRangoRegistro;
    }

    public List<Integer> getInvalidDays() {
        return invalidDays;
    }

    public void setInvalidDays(List<Integer> invalidDays) {
        this.invalidDays = invalidDays;
    }

    public List<CatTipoSolicitud> getLstTipoSol() {
        return lstTipoSol;
    }

    public void setLstTipoSol(List<CatTipoSolicitud> lstTipoSol) {
        this.lstTipoSol = lstTipoSol;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public DetEmpleado getEmpleadoSelected() {
        return empleadoSelected;
    }

    public void setEmpleadoSelected(DetEmpleado empleadoSelected) {
        this.empleadoSelected = empleadoSelected;
    }

    public List<DetIncidencia> getListaPrendas() {
        return listaPrendas;
    }

    public void setListaPrendas(List<DetIncidencia> listaPrendas) {
        this.listaPrendas = listaPrendas;
    }
    //</editor-fold>
}
