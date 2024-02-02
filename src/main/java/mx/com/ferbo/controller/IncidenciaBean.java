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

import mx.com.ferbo.dao.CatTipoSolicitudDAO;
import mx.com.ferbo.dao.EmpleadoDAO;
import mx.com.ferbo.dao.IncidenciaDAO;
import mx.com.ferbo.dto.CatTipoSolicitudDTO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import mx.com.ferbo.dto.DetIncidenciaDTO;
import mx.com.ferbo.util.SGPException;

/**
 *
 * @author Gabriel
 */
@Named("incidenciaBean")
@ViewScoped
public class IncidenciaBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static final Logger log = LogManager.getLogger(IncidenciaBean.class);

    private final IncidenciaDAO incidenciaDAO;
    private DetIncidenciaDTO incidenciaSelected;
    private Date fechaSeleccionada;
    private List<DetIncidenciaDTO> lstIncidencias;
    private List<DetIncidenciaDTO> listaPermisos;
    private List<DetIncidenciaDTO> listaPrendas;
    private List<DetIncidenciaDTO> listaArticulos;
    private List<CatTipoSolicitudDTO> lstTipoSol;
    private final CatTipoSolicitudDAO catTipoSolicitudDAO;
    private List<Date> lstRangoRegistro;
    private List<Integer> invalidDays;
    private Date minDate;

    private DetEmpleadoDTO empleadoSelected;
    private final HttpServletRequest httpServletRequest;
    private final EmpleadoDAO empleadoDAO;

    public IncidenciaBean() {
        incidenciaDAO = new IncidenciaDAO();
        catTipoSolicitudDAO = new CatTipoSolicitudDAO();
        minDate = new Date();

        empleadoDAO = new EmpleadoDAO();
        httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.empleadoSelected = (DetEmpleadoDTO) httpServletRequest.getSession(true).getAttribute("empleado");

        empleadoSelected = empleadoDAO.buscarPorId(empleadoSelected.getIdEmpleado());
    }

    @PostConstruct
    public void init() {
        consultaIncidencias();
        lstTipoSol = catTipoSolicitudDAO.buscarActivo();
        invalidDays = Arrays.asList(0);
    }

    private void consultaIncidencias() {
        lstIncidencias = incidenciaDAO.buscarTodos();

        listaPermisos = lstIncidencias.stream()
                .filter(objeto -> objeto.getCatTipoIncidenciaDTO().getIdTipo().equals(1))
                .map(objeto -> objeto)
                .collect(Collectors.toList());
        
        listaPrendas = lstIncidencias.stream()
                .filter(objeto -> objeto.getCatTipoIncidenciaDTO().getIdTipo().equals(3))
                .map(objeto -> objeto)
                .collect(Collectors.toList());
        
        listaArticulos = lstIncidencias.stream()
                .filter(objeto -> objeto.getCatTipoIncidenciaDTO().getIdTipo().equals(4))
                .map(objeto -> objeto)
                .collect(Collectors.toList());
    }

    public void visualizaDialog() {
        switch (incidenciaSelected.getCatTipoIncidenciaDTO().getIdTipo()) {
            // Tipo Permisos
            case 1:
                switch (incidenciaSelected.getDetSolicitudPermisoDTO().getCatTipoSolicitud().getIdTipoSolicitud()) {
                    case 1://PERMISO
                    case 3://INCAPACDAD CORTA
                        fechaSeleccionada = incidenciaSelected.getDetSolicitudPermisoDTO().getFechaInicio();
                        break;
                    default:
                        lstRangoRegistro = Arrays.asList(incidenciaSelected.getDetSolicitudPermisoDTO().getFechaInicio(), incidenciaSelected.getDetSolicitudPermisoDTO().getFechaFin());
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
        incidenciaSelected.setDetEmpleadoRevDTO(new DetEmpleadoDTO(empleadoSelected.getIdEmpleado()));
        try {
            incidenciaSelected.getCatEstatusIncidenciaDTO().setIdEstatus(aprobada ? 2 : 3);
            incidenciaDAO.actualizar(incidenciaSelected);

            String mensaje = "";
            if (incidenciaSelected.getCatEstatusIncidenciaDTO().getIdEstatus() == 2) {
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
        if(incidenciaSelected.getDetSolicitudPermisoDTO().getIdSolicitud() != null){
            PrimeFaces.current().executeScript("PF('dialogPermisos').hide()");
        }else if(incidenciaSelected.getDetSolicitudPrendaDTO().getIdSolicitud() != null){
            PrimeFaces.current().executeScript("PF('dialogPrendas').hide()");
        }else if(incidenciaSelected.getDetSolicitudArticuloDTO().getIdSolicitud() != null){
            PrimeFaces.current().executeScript("PF('dialogArticulos').hide()");
        }
    }
    
    public void guardarEstatusArticulo(boolean aprobada) {
        incidenciaSelected.setDetEmpleadoRevDTO(new DetEmpleadoDTO(empleadoSelected.getIdEmpleado()));
        try {
            incidenciaSelected.getCatEstatusIncidenciaDTO().setIdEstatus(aprobada ? 2 : 3);
            incidenciaDAO.actualizar(incidenciaSelected);

            String mensaje = "";
            if (incidenciaSelected.getCatEstatusIncidenciaDTO().getIdEstatus() == 2) {
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
        incidenciaSelected.setDetEmpleadoRevDTO(new DetEmpleadoDTO(empleadoSelected.getIdEmpleado()));
        try {
            incidenciaSelected.getCatEstatusIncidenciaDTO().setIdEstatus(aprobada ? 2 : 3);
            incidenciaDAO.actualizar(incidenciaSelected);

            String mensaje = "";
            if (incidenciaSelected.getCatEstatusIncidenciaDTO().getIdEstatus() == 2) {
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
    public DetIncidenciaDTO getIncidenciaSelected() {
        return incidenciaSelected;
    }

    public void setIncidenciaSelected(DetIncidenciaDTO incidenciaSelected) {
        this.incidenciaSelected = incidenciaSelected;
    }

    public List<DetIncidenciaDTO> getLstIncidencias() {
        return lstIncidencias;
    }

    public void setLstIncidencias(List<DetIncidenciaDTO> lstIncidencias) {
        this.lstIncidencias = lstIncidencias;
    }

    public List<DetIncidenciaDTO> getListaPermisos() {
        return listaPermisos;
    }

    public void setListaPermisos(List<DetIncidenciaDTO> listaPermisos) {
        this.listaPermisos = listaPermisos;
    }

    public List<DetIncidenciaDTO> getListaArticulos() {
        return listaArticulos;
    }

    public void setListaArticulos(List<DetIncidenciaDTO> listaArticulos) {
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

    public List<CatTipoSolicitudDTO> getLstTipoSol() {
        return lstTipoSol;
    }

    public void setLstTipoSol(List<CatTipoSolicitudDTO> lstTipoSol) {
        this.lstTipoSol = lstTipoSol;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public DetEmpleadoDTO getEmpleadoSelected() {
        return empleadoSelected;
    }

    public void setEmpleadoSelected(DetEmpleadoDTO empleadoSelected) {
        this.empleadoSelected = empleadoSelected;
    }

    public List<DetIncidenciaDTO> getListaPrendas() {
        return listaPrendas;
    }

    public void setListaPrendas(List<DetIncidenciaDTO> listaPrendas) {
        this.listaPrendas = listaPrendas;
    }
    //</editor-fold>
}
