package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import mx.com.ferbo.business.incidencia.EstatusIncidenciaBL;
import mx.com.ferbo.business.incidencia.EstatusSolicitudBL;
import mx.com.ferbo.business.notifmovil.NotifMovilBL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.n.ArticuloDAO;
import mx.com.ferbo.dao.n.SolicitudArticuloDAO;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.IncidenciaDAO;
import mx.com.ferbo.dto.NotificacionMovilDTO;
import mx.com.ferbo.model.CatArticulo;
import mx.com.ferbo.model.CatTipoIncidencia;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.model.DetSolicitudArticulo;
import mx.com.ferbo.servlet.NotificacionServlet;
import mx.com.ferbo.util.SGPException;
import mx.com.ferbo.util.ManageStatus;

@Named(value = "articuloOficinasBean")
@ViewScoped
public class ArticulosOficinaBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(ArticulosOficinaBean.class);

    private String numeroEmpl;

    private List<CatArticulo> lstArticulosActivas;
    private List<Integer> lstCantidad;
    private List<DetSolicitudArticulo> lstSolicitudArticulos;
    private List<DetSolicitudArticulo> lstSolicitudArticulosRealizadas;

    private DetEmpleado empleadoSelected;
    private DetSolicitudArticulo solicitud;

    private ArticuloDAO articulosDAO;
    private final EmpleadoDAO empleadoDAO;
    private SolicitudArticuloDAO solicitudArticulosDAO;
    private final IncidenciaDAO incidenciaDAO;

    private CatArticulo articuloSelected;
    private Integer cantidadSelected;

    private final FacesContext faceContext;
    private final HttpServletRequest httpServletRequest;
    private ManageStatus status;

    private DetIncidencia incidencia;
    private CatTipoIncidencia catTipoIncidencia;

    public ArticulosOficinaBean() {
        lstArticulosActivas = new ArrayList<>();
        lstCantidad = new ArrayList<>(
                Arrays.asList(1, 2, 3));

        empleadoSelected = new DetEmpleado();
        articuloSelected = new CatArticulo();
        cantidadSelected = 0;

        articulosDAO = new ArticuloDAO();
        empleadoDAO = new EmpleadoDAO();
        solicitudArticulosDAO = new SolicitudArticuloDAO();
        incidenciaDAO = new IncidenciaDAO();

        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        this.empleadoSelected = (DetEmpleado) httpServletRequest.getSession(true).getAttribute("empleado");

        solicitud = new DetSolicitudArticulo();
    }

    @PostConstruct
    public void init() {
        lstSolicitudArticulos = new ArrayList<>();
        solicitud.setEmpleadoSol(empleadoSelected);
        actualizarListas();
        lstArticulosActivas = articulosDAO.buscarTodosActivos();
        status = new ManageStatus();
    }
    
    public void actualizarListas()
    {
        lstSolicitudArticulosRealizadas = solicitudArticulosDAO.buscarPorIdEmpleado(solicitud.getEmpleadoSol().getIdEmpleado());
    }
    
    public void editarArticulo(DetSolicitudArticulo solicitudArticulo)
    {
        solicitud = solicitudArticulo;
    }

    public void seleccionarItem(CatArticulo item) {
        articuloSelected = item;
        solicitud = new DetSolicitudArticulo();
        PrimeFaces.current().executeScript("PF('dialogComplementoArtiulo').show();");
    }

    public void preRegistro() {
        solicitud.setFechaCap(new Date());
        solicitud.setEmpleadoSol(empleadoSelected);
        solicitud.setArticulo(articuloSelected);
        lstSolicitudArticulos.add(solicitud);
        PrimeFaces.current().executeInitScript("PF('dialogComplementoArtiulo').hide()");
        PrimeFaces.current().executeInitScript("PF('articuloOficinaDialog').hide()");
        PrimeFaces.current().ajax().update("formActividadesArticulos:messages", "formActividadesArticulos:tabView:dt-articuloOficinas", "formActividadesArticulos:tabView:btnRegistro");
    }

    public void registro() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Articulo";
        for (DetSolicitudArticulo solicitudArticulo : lstSolicitudArticulos) {
            try {
                solicitudArticulo.setEstatus(EstatusSolicitudBL.estatusEnviado());
                
                incidencia = new DetIncidencia();
                catTipoIncidencia = new CatTipoIncidencia();
                catTipoIncidencia.setIdTipo(4);

                incidencia.setTipoIncidencia(catTipoIncidencia);
                incidencia.setEmpleado(empleadoSelected);
                incidencia.setEstatusIncidencia(EstatusIncidenciaBL.estatusEnviado());
                incidencia.setVisible((short) 1);
                incidencia.setSolArticulo(solicitudArticulo);
                incidencia.setFechaCap(new Date());

                incidenciaDAO.guardar(incidencia);
                actualizarListas();
                mensaje = "Solicitud Registrada";
                severity = FacesMessage.SEVERITY_INFO;
                NotificacionMovilDTO nmDTO = NotifMovilBL.obtenerMensaje("articulo", this.empleadoSelected);
                NotificacionServlet notifServlet = new NotificacionServlet();
                notifServlet.enviarNotificacion(nmDTO);
            } catch (SGPException e) 
            {
                log.warn("Error al guardar el registro de articulos del empleado: {}", empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
                log.warn("EX-0034: ", e);
                mensaje = e.getMessage();
                severity = FacesMessage.SEVERITY_ERROR;
            }catch(Exception e)
            {
                log.warn("Error al guardar el registro del articulo del empleado: {}", empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
                log.warn("EX-0032: ", e);
                mensaje = "Consulte con su administrador de sistemas";
                severity = FacesMessage.SEVERITY_ERROR;
            }
        }
        lstSolicitudArticulos.clear();
        message = new FacesMessage(severity, titulo, mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
        PrimeFaces.current().ajax().update(":formActividadesArticulos:messages", ":formActividadesArticulos:tabView:dt-articuloOficinas", ":formActividadesArticulos:tabView:btnRegistro");
        PrimeFaces.current().executeScript("PF('articuloOficinaDialog').hide()");
    }
    
    public void actualizarRegistro()
    {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Articulo";
        try
        {
            incidencia = incidenciaDAO.buscarPorArticulo(empleadoSelected.getIdEmpleado(), solicitud.getIdSolicitud());
            if(incidencia == null)
            {
                throw new SGPException("Error al editar la solicitud, favor de contactar al administrador de sistemas");
            }
            
            switch (solicitud.getEstatus().getClave()) 
            {
                case "A":
                    throw new SGPException("No se puede modificar el artículo");
                case "R":
                    throw new SGPException("No se puede modificar el artículo");
                case "C":
                    throw new SGPException("No se puede modificar el artículo");
            }
            
            solicitud.setEstatus(EstatusSolicitudBL.estatusCancelado());
            incidencia.setEstatusIncidencia(EstatusIncidenciaBL.estatusCancelado());
            incidencia.setSolArticulo(solicitud);
            incidenciaDAO.actualizar(incidencia);                    
            
            actualizarListas();
            mensaje = "Se actualizo la solicitud correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        }catch(SGPException e)
        {
            log.warn("Error al actualizar el registro del articulo del empleado: {}", empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn("EX-0032: ", e);
            mensaje = e.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
        }catch(Exception e)
        {
            log.warn("Error al actualizar el registro del articulo del empleado: {}", empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn("EX-0032: ", e);
            mensaje = "Consulte con su administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
        }finally
        {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update(":formActividadesArticulos:messages", ":formActividadesArticulos:tabView:dt-articuloOficinas-sol");
            PrimeFaces.current().executeScript("PF('dialogCambiarEstatus').hide()");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public void inicializaArticulo() {
        articuloSelected = new CatArticulo();
    }

    public void eliminaSolicitud() {
        lstSolicitudArticulos.remove(solicitud);
    }

    public String getNumeroEmpl() {
        return numeroEmpl;
    }

    public void setNumeroEmpl(String numeroEmpl) {
        this.numeroEmpl = numeroEmpl;
    }

    public List<CatArticulo> getLstArticulosActivas() {
        return lstArticulosActivas;
    }

    public void setLstArticulosActivas(List<CatArticulo> lstArticulosActivas) {
        this.lstArticulosActivas = lstArticulosActivas;
    }

    public List<Integer> getLstCantidad() {
        return lstCantidad;
    }

    public void setLstCantidad(List<Integer> lstCantidad) {
        this.lstCantidad = lstCantidad;
    }

    public List<DetSolicitudArticulo> getLstSolicitudArticulos() {
        return lstSolicitudArticulos;
    }

    public void setLstSolicitudArticulos(List<DetSolicitudArticulo> lstSolicitudArticulos) {
        this.lstSolicitudArticulos = lstSolicitudArticulos;
    }

    public List<DetSolicitudArticulo> getLstSolicitudArticulosRealizadas() {
        return lstSolicitudArticulosRealizadas;
    }

    public void setLstSolicitudArticulosRealizadas(List<DetSolicitudArticulo> lstSolicitudArticulosRealizadas) {
        this.lstSolicitudArticulosRealizadas = lstSolicitudArticulosRealizadas;
    }

    public DetEmpleado getEmpleadoSelected() {
        return empleadoSelected;
    }

    public void setEmpleadoSelected(DetEmpleado empleadoSelected) {
        this.empleadoSelected = empleadoSelected;
    }

    public DetSolicitudArticulo getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(DetSolicitudArticulo solicitud) {
        this.solicitud = solicitud;
    }

    public ArticuloDAO getArticulosDAO() {
        return articulosDAO;
    }

    public void setArticulosDAO(ArticuloDAO articulosDAO) {
        this.articulosDAO = articulosDAO;
    }

    public SolicitudArticuloDAO getSolicitudArticulosDAO() {
        return solicitudArticulosDAO;
    }

    public void setDetSolicitudArticulosDAO(SolicitudArticuloDAO solicitudArticulosDAO) {
        this.solicitudArticulosDAO = solicitudArticulosDAO;
    }

    public CatArticulo getArticuloSelected() {
        return articuloSelected;
    }

    public void setArticuloSelected(CatArticulo articuloSelected) {
        this.articuloSelected = articuloSelected;
    }

    public Integer getCantidadSelected() {
        return cantidadSelected;
    }

    public void setCantidadSelected(Integer cantidadSelected) {
        this.cantidadSelected = cantidadSelected;
    }
    
    public ManageStatus getStatus() {
        return status;
    }
//</editor-fold>
}
