package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    private List<DetIncidencia> incidenciasBuscada;

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
    private CatEstatusIncidencia catEstatusIncidencia;

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
        solicitud.setIdEmpleadoSol(empleadoSelected);
        actualizarListas();
        lstArticulosActivas = articulosDAO.buscarTodosActivos();
        status = new ManageStatus();
    }
    
    public void actualizarListas()
    {
        lstSolicitudArticulosRealizadas = solicitudArticulosDAO.buscarPorIdEmpleado(solicitud.getIdEmpleadoSol().getIdEmpleado());
        incidenciasBuscada = incidenciaDAO.buscarPorIdEmpleadoArticulo(solicitud.getIdEmpleadoSol().getIdEmpleado());
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
        PrimeFaces.current().ajax().update("formActividadesArticulos:messages", "formActividadesArticulos:tabView:dt-articuloOficinas", "formActividadesArticulos:tabView:btnRegistro");
    }

    public void registro() throws IOException {
        for (DetSolicitudArticulo solicitudArticulo : lstSolicitudArticulos) {
            try {
                solicitudArticulo.setAprobada((short) 1);
                solicitudArticulosDAO.guardar(solicitudArticulo);
                actualizarListas();
                
                incidencia = new DetIncidencia();
                catTipoIncidencia = new CatTipoIncidencia();
                catEstatusIncidencia = new CatEstatusIncidencia();

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
    
    public void actualizarRegistro()
    {
        actualizarListas();
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Articulo";
        try
        {
            if(incidenciasBuscada == null)
            {
                throw new SGPException("Error con la conexión a la base de datos!!!");
            }
            
            switch (solicitud.getAprobada().intValue()) 
            {
                case 2:
                    throw new SGPException("No se puede modificar el artículo");
                case 3:
                    throw new SGPException("No se puede modificar el artículo");
                case 4:
                    throw new SGPException("No se puede modificar el artículo");
            }
            
            for(DetIncidencia auxIncidenciaBuscada : incidenciasBuscada)
            {
                if(Objects.equals(auxIncidenciaBuscada.getIdSolArticulo().getIdSolicitud(), solicitud.getIdSolicitud()))
                {
                    catEstatusIncidencia = new CatEstatusIncidencia();
                    catEstatusIncidencia.setIdEstatus(4);
                    
                    auxIncidenciaBuscada.setIdEstatus(catEstatusIncidencia);
                    auxIncidenciaBuscada.setIdSolArticulo(solicitud);
                    incidenciaDAO.actualizar(auxIncidenciaBuscada);                    
                }
            }
            
            solicitud.setAprobada((short) 4);
            solicitudArticulosDAO.actualizar(solicitud);
            
            actualizarListas();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Estatus Modificada"));
            PrimeFaces.current().ajax().update("formActividadesArticulos:tabView:dt-uniformes-sol");
            PrimeFaces.current().executeScript("PF('dialogCambiarEstatus').hide()");
        }catch(SGPException e)
        {
            mensaje = e.getMessage();
            severity = FacesMessage.SEVERITY_INFO;
        }catch(Exception e)
        {
            log.warn("EX-0032: " + e.getMessage() + ". Error al actualizar el registro del articulo del empleado: " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
        }finally
        {
            if(severity != null)
            {
                message = new FacesMessage(severity, titulo, mensaje);
                FacesContext.getCurrentInstance().addMessage(null, message);
                PrimeFaces.current().ajax().update(":formActividadesArticulos:messages");
                PrimeFaces.current().executeScript("PF('dialogCambiarEstatus').hide()");
            }
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

    public List<DetIncidencia> getIncidenciasBuscada() {
        return incidenciasBuscada;
    }

    public void setIncidenciasBuscada(List<DetIncidencia> incidenciasBuscada) {
        this.incidenciasBuscada = incidenciasBuscada;
    }
    
    public ManageStatus getStatus() {
        return status;
    }
//</editor-fold>
}
