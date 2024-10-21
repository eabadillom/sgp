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
import mx.com.ferbo.dao.n.SolicitudArticuloDAO;
import mx.com.ferbo.dao.n.SolicitudPermisoDAO;
import mx.com.ferbo.dao.n.SolicitudPrendaDAO;
import mx.com.ferbo.dao.n.TipoSolicitudDAO;
import mx.com.ferbo.model.CatEstatusIncidencia;
import mx.com.ferbo.model.CatTipoIncidencia;
import mx.com.ferbo.model.CatTipoSolicitud;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.model.DetSolicitudArticulo;
import mx.com.ferbo.model.DetSolicitudPermiso;
import mx.com.ferbo.model.DetSolicitudPrenda;
import mx.com.ferbo.util.ManageStatus;
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
    private final SolicitudPermisoDAO solicitudPermisoDAO;
    private final TipoSolicitudDAO tipoSolicitudDAO;
    private final SolicitudPrendaDAO solicitudPrendaDAO;
    private final SolicitudArticuloDAO solicitudArticulosDAO;
    private DetIncidencia incidenciaSelected;
    private Date fechaSeleccionada;
    private List<DetIncidencia> lstIncidencias;
    private List<DetIncidencia> listaPermisos;
    private List<DetIncidencia> listaPrendas;
    private List<DetIncidencia> listaArticulos;
    private List<CatTipoSolicitud> lstTipoSol;
    private List<DetSolicitudArticulo> listArticulos;
    private List<DetSolicitudPermiso> listPermisos;
    private List<DetSolicitudPrenda> listPrendas;
    private List<Date> lstRangoRegistro;
    private List<Integer> invalidDays;
    private Date minDate;

    private DetEmpleado empleadoSelected;
    private final HttpServletRequest httpServletRequest;
    private ManageStatus status;
    private final EmpleadoDAO empleadoDAO;
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public IncidenciaBean() {
        incidenciaDAO = new IncidenciaDAO();
        tipoSolicitudDAO = new TipoSolicitudDAO();
        minDate = new Date();

        empleadoDAO = new EmpleadoDAO();
        httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.empleadoSelected = (DetEmpleado) httpServletRequest.getSession(true).getAttribute("empleado");
        
        solicitudArticulosDAO = new SolicitudArticuloDAO();
        solicitudPermisoDAO = new SolicitudPermisoDAO();
        solicitudPrendaDAO = new SolicitudPrendaDAO();
        
        empleadoSelected = empleadoDAO.buscarPorId(empleadoSelected.getIdEmpleado());
        inicializarIncidencia();
    }

    @PostConstruct
    public void init() {
        consultaIncidencias();
        lstTipoSol = tipoSolicitudDAO.buscarActivos();
        invalidDays = Arrays.asList(0);
        listArticulos = solicitudArticulosDAO.buscarPorIdEmpleado(empleadoSelected.getIdEmpleado());
        listPermisos = solicitudPermisoDAO.buscarPorIdEmpleado(empleadoSelected.getIdEmpleado());
        listPrendas = solicitudPrendaDAO.buscarPorIdEmpleado(empleadoSelected.getIdEmpleado());
        status = new ManageStatus();
    }

    private void consultaIncidencias() {
        lstIncidencias = incidenciaDAO.buscarTodos();

        listaPermisos = lstIncidencias.stream()
                .filter(objeto -> objeto.getIdTipo().getIdTipo().equals(1) ||  objeto.getIdTipo().getIdTipo().equals(2))
                .collect(Collectors.toList());
        
        listaPrendas = lstIncidencias.stream()
                .filter(objeto -> objeto.getIdTipo().getIdTipo().equals(3))
                .collect(Collectors.toList());
        
        listaArticulos = lstIncidencias.stream()
                .filter(objeto -> objeto.getIdTipo().getIdTipo().equals(4))
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
    
    public void inicializarIncidencia()
    {
        incidenciaSelected = new DetIncidencia();
        incidenciaSelected.setIdEstatus(new CatEstatusIncidencia());
        incidenciaSelected.setIdSolArticulo(new DetSolicitudArticulo());
        incidenciaSelected.setIdSolPermiso(new DetSolicitudPermiso());
        incidenciaSelected.setIdSolPrenda(new DetSolicitudPrenda());
        incidenciaSelected.setIdTipo(new CatTipoIncidencia());
    }

    public void guardarEstatusIncidencia(boolean aprobada) {
        incidenciaSelected.setIdEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
        try {
            incidenciaSelected.getIdEstatus().setIdEstatus(aprobada ? 2 : 3);
            incidenciaSelected.setFechaMod(new Date());
            
            for(DetSolicitudPermiso auxPermiso: listPermisos)
            {
                if(auxPermiso.getIdSolicitud().equals(incidenciaSelected.getIdSolPermiso().getIdSolicitud()))
                {
                    auxPermiso.setAprobada(aprobada ? (short) 2 : (short) 3);
                    auxPermiso.setFechaMod(new Date());
                    auxPermiso.setIdEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
                    solicitudPermisoDAO.actualizar(auxPermiso);
                    incidenciaSelected.setIdSolPermiso(auxPermiso);
                }
            }
            
            incidenciaSelected.getIdSolPermiso().setFechaMod(new Date());
            incidenciaSelected.getIdSolPermiso().setIdEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
            
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
        }
    }
    
    public void guardarEstatusArticulo(boolean aprobada) {
        incidenciaSelected.setIdEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
        try {
            incidenciaSelected.getIdEstatus().setIdEstatus(aprobada ? 2 : 3);
            incidenciaSelected.setFechaMod(new Date());
            
            for(DetSolicitudArticulo auxArticulo: listArticulos)
            {
                if(auxArticulo.getIdSolicitud().equals(incidenciaSelected.getIdSolArticulo().getIdSolicitud()))
                {
                    auxArticulo.setAprobada(aprobada ? (short) 2 : (short) 3);
                    auxArticulo.setFechaMod(new Date());
                    auxArticulo.setIdEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
                    solicitudArticulosDAO.actualizar(auxArticulo);
                    incidenciaSelected.setIdSolArticulo(auxArticulo);
                }
            }
            
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
        if(incidenciaSelected.getIdSolArticulo().getIdSolicitud() != null){
            PrimeFaces.current().executeScript("PF('dialogArticulos').hide()");
        }
    }
    
    public void guardarEstatusPrenda(boolean aprobada) {
        incidenciaSelected.setIdEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
        try {
            incidenciaSelected.getIdEstatus().setIdEstatus(aprobada ? 2 : 3);
            incidenciaSelected.setFechaMod(new Date());
            
            for(DetSolicitudPrenda auxPrenda : listPrendas)
            {
                if(auxPrenda.getIdSolicitud().equals(incidenciaSelected.getIdSolPrenda().getIdSolicitud()))
                {
                    auxPrenda.setAprobada(aprobada ? (short) 2 : (short) 3);
                    auxPrenda.setFechaMod(new Date());
                    auxPrenda.setIdEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
                    solicitudPrendaDAO.actualizar(auxPrenda);
                    incidenciaSelected.setIdSolPrenda(auxPrenda);
                }
            }
            
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
        if(incidenciaSelected.getIdSolPrenda().getIdSolicitud() != null){
            PrimeFaces.current().executeScript("PF('dialogPrendas').hide()");
        }
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
    
    public ManageStatus getStatus() {
        return status;
    }
    //</editor-fold>
}
