package mx.com.ferbo.controller;

import java.io.IOException;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.n.ArticuloDAO;
import mx.com.ferbo.dao.n.SolicitudArticuloDAO;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.IncidenciaDAO;
import mx.com.ferbo.model.CatArticulo;
import mx.com.ferbo.model.CatEstatusIncidencia;
import mx.com.ferbo.model.CatTipoIncidencia;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.model.DetSolicitudArticulo;
import mx.com.ferbo.util.SGPException;

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

    private final DetIncidencia incidencia;
    private final CatTipoIncidencia catTipoIncidencia;
    private final CatEstatusIncidencia catEstatusIncidencia;

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
        incidencia = new DetIncidencia();
        catTipoIncidencia = new CatTipoIncidencia();
        catEstatusIncidencia = new CatEstatusIncidencia();
    }

    @PostConstruct
    public void init() {
        lstSolicitudArticulos = new ArrayList<>();
        solicitud.setIdEmpleadoSol(empleadoSelected);
        lstSolicitudArticulosRealizadas = solicitudArticulosDAO.buscarPorIdEmpleado(solicitud.getIdEmpleadoSol().getIdEmpleado());
        lstArticulosActivas = articulosDAO.buscarTodosActivos();
    }

    public void seleccionarItem(CatArticulo item) {
        articuloSelected = item;
        solicitud = new DetSolicitudArticulo();
        PrimeFaces.current().executeScript("PF('dialogComplementoArtiulo').show();");
    }

    public void preRegistro() {
        solicitud.setFechaCap(new Date());
        solicitud.setIdEmpleadoSol(empleadoSelected);
        solicitud.setIdArticulo(articuloSelected);
        lstSolicitudArticulos.add(solicitud);
        PrimeFaces.current().executeInitScript("PF('dialogComplementoArtiulo').hide()");
        PrimeFaces.current().executeInitScript("PF('articuloOficinaDialog').hide()");
        PrimeFaces.current().ajax().update("formActividadesArticulos:messages", "formActividadesArticulos:tabViewA:dt-articuloOficinas", "formActividadesArticulos:tabViewA:btnRegistro");
    }

    public void registro() throws IOException {
        for (DetSolicitudArticulo solicitudArticulo : lstSolicitudArticulos) {
            try {
                solicitudArticulosDAO.guardar(solicitudArticulo);
                lstSolicitudArticulosRealizadas = solicitudArticulosDAO.buscarPorIdEmpleado(solicitudArticulo.getIdEmpleadoSol().getIdEmpleado());

                catTipoIncidencia.setIdTipo(4);
                catEstatusIncidencia.setIdEstatus(1);

                incidencia.setIdTipo(catTipoIncidencia);
                incidencia.setIdEmpleado(empleadoSelected);
                incidencia.setIdEstatus(catEstatusIncidencia);
                incidencia.setVisible((short) 1);
                incidencia.setIdSolArticulo(solicitudArticulo);
                incidencia.setFechaCap(new Date());

                incidenciaDAO.guardar(incidencia);

            } catch (SGPException e) {
                log.warn("EX-0034: " + e.getMessage() + ". Error al guardar el registro de articulos del empleado: " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            }
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("articulosTrabajo.xhtml");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Solicitud Registrada"));
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
//</editor-fold>
}
