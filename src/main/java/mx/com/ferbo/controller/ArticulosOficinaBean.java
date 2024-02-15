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

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.CatArticulosDAO;
import mx.com.ferbo.dao.DetSolicitudArticulosDAO;
import mx.com.ferbo.dao.EmpleadoDAO;
import mx.com.ferbo.dao.IncidenciaDAO;
import mx.com.ferbo.dto.CatArticuloDTO;
import mx.com.ferbo.dto.CatEstatusIncidenciaDTO;
import mx.com.ferbo.dto.CatTipoIncidenciaDTO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import mx.com.ferbo.dto.DetIncidenciaDTO;
import mx.com.ferbo.dto.DetSolicitudArticuloDTO;
import mx.com.ferbo.util.SGPException;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named(value = "articuloOficinasBean")
@ViewScoped
public class ArticulosOficinaBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static Logger log = LogManager.getLogger(ArticulosOficinaBean.class);

    private String numeroEmpl;

    private List<CatArticuloDTO> lstArticulosActivas;
    private List<Integer> lstCantidad;
    private List<DetSolicitudArticuloDTO> lstSolicitudArticulos;
    private List<DetSolicitudArticuloDTO> lstSolicitudArticulosRealizadas;

    private DetEmpleadoDTO empleadoSelected;
    private DetSolicitudArticuloDTO solicitud;

    private CatArticulosDAO articulosDAO;
    private final EmpleadoDAO empleadoDAO;
    private DetSolicitudArticulosDAO detSolicitudArticulosDAO;
    private final IncidenciaDAO incidenciaDAO;

    private CatArticuloDTO articuloSelected;
    private Integer cantidadSelected;

    private final FacesContext faceContext;
    private final HttpServletRequest httpServletRequest;

    private final DetIncidenciaDTO incidenciaDTO;
    private final CatTipoIncidenciaDTO catTipoIncidenciaDTO;
    private final CatEstatusIncidenciaDTO catEstatusIncidenciaDTO;

    public ArticulosOficinaBean() {
        lstArticulosActivas = new ArrayList<>();
        lstCantidad = new ArrayList<>(
                Arrays.asList(1, 2, 3));

        empleadoSelected = new DetEmpleadoDTO();
        articuloSelected = new CatArticuloDTO();
        cantidadSelected = 0;

        articulosDAO = new CatArticulosDAO();
        empleadoDAO = new EmpleadoDAO();
        detSolicitudArticulosDAO = new DetSolicitudArticulosDAO();
        incidenciaDAO = new IncidenciaDAO();

        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        this.empleadoSelected = (DetEmpleadoDTO) httpServletRequest.getSession(true).getAttribute("empleado");

        solicitud = new DetSolicitudArticuloDTO();
        incidenciaDTO = new DetIncidenciaDTO();
        catTipoIncidenciaDTO = new CatTipoIncidenciaDTO();
        catEstatusIncidenciaDTO = new CatEstatusIncidenciaDTO();
    }

    @PostConstruct
    public void init() {
        lstSolicitudArticulos = new ArrayList<>();
        solicitud.setEmpleadoSol(empleadoSelected);
        lstSolicitudArticulosRealizadas = detSolicitudArticulosDAO.buscarPorCriterios(solicitud);
        lstArticulosActivas = articulosDAO.buscarActivo();
    }

    public void seleccionarItem(CatArticuloDTO item) {
        articuloSelected = item;
        solicitud = new DetSolicitudArticuloDTO();
        PrimeFaces.current().executeScript("PF('dialogComplementoArtiulo').show();");
    }

    public void preRegistro() {
        solicitud.setFechaCap(new Date());
        solicitud.setEmpleadoSol(empleadoSelected);
        solicitud.setArticulo(articuloSelected);
        lstSolicitudArticulos.add(solicitud);
        PrimeFaces.current().executeInitScript("PF('dialogComplementoArtiulo').hide()");
        PrimeFaces.current().executeInitScript("PF('articuloOficinaDialog').hide()");
        PrimeFaces.current().ajax().update("formActividadesArticulos:messages", "formActividadesArticulos:tabViewA:dt-articuloOficinas", "formActividadesArticulos:tabViewA:btnRegistro");
    }

    public void registro() throws IOException {
        for (DetSolicitudArticuloDTO detSolicitudArticuloDTO : lstSolicitudArticulos) {
            try {
                detSolicitudArticulosDAO.guardar(detSolicitudArticuloDTO);
                lstSolicitudArticulosRealizadas = detSolicitudArticulosDAO.buscarPorCriterios(detSolicitudArticuloDTO);

                catTipoIncidenciaDTO.setIdTipo(4);
                catEstatusIncidenciaDTO.setIdEstatus(1);

                incidenciaDTO.setCatTipoIncidenciaDTO(catTipoIncidenciaDTO);
                incidenciaDTO.setDetEmpleadoDTO(empleadoSelected);
                incidenciaDTO.setCatEstatusIncidenciaDTO(catEstatusIncidenciaDTO);
                incidenciaDTO.setVisible((short) 1);
                incidenciaDTO.setDetSolicitudArticuloDTO(detSolicitudArticuloDTO);
                incidenciaDTO.setFechaCap(new Date());

                incidenciaDAO.guardar(incidenciaDTO);

            } catch (SGPException e) {
                log.warn("EX-0034: " + e.getMessage() + ". Error al guardar el registro de articulos del empleado: " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            }
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("articulosTrabajo.xhtml");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Solicitud Registrada"));
    }

    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public void inicializaArticulo() {
        articuloSelected = new CatArticuloDTO();
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

    public List<CatArticuloDTO> getLstArticulosActivas() {
        return lstArticulosActivas;
    }

    public void setLstArticulosActivas(List<CatArticuloDTO> lstArticulosActivas) {
        this.lstArticulosActivas = lstArticulosActivas;
    }

    public List<Integer> getLstCantidad() {
        return lstCantidad;
    }

    public void setLstCantidad(List<Integer> lstCantidad) {
        this.lstCantidad = lstCantidad;
    }

    public List<DetSolicitudArticuloDTO> getLstSolicitudArticulos() {
        return lstSolicitudArticulos;
    }

    public void setLstSolicitudArticulos(List<DetSolicitudArticuloDTO> lstSolicitudArticulos) {
        this.lstSolicitudArticulos = lstSolicitudArticulos;
    }

    public List<DetSolicitudArticuloDTO> getLstSolicitudArticulosRealizadas() {
        return lstSolicitudArticulosRealizadas;
    }

    public void setLstSolicitudArticulosRealizadas(List<DetSolicitudArticuloDTO> lstSolicitudArticulosRealizadas) {
        this.lstSolicitudArticulosRealizadas = lstSolicitudArticulosRealizadas;
    }

    public DetEmpleadoDTO getEmpleadoSelected() {
        return empleadoSelected;
    }

    public void setEmpleadoSelected(DetEmpleadoDTO empleadoSelected) {
        this.empleadoSelected = empleadoSelected;
    }

    public DetSolicitudArticuloDTO getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(DetSolicitudArticuloDTO solicitud) {
        this.solicitud = solicitud;
    }

    public CatArticulosDAO getArticulosDAO() {
        return articulosDAO;
    }

    public void setArticulosDAO(CatArticulosDAO articulosDAO) {
        this.articulosDAO = articulosDAO;
    }

    public DetSolicitudArticulosDAO getDetSolicitudArticulosDAO() {
        return detSolicitudArticulosDAO;
    }

    public void setDetSolicitudArticulosDAO(DetSolicitudArticulosDAO detSolicitudArticulosDAO) {
        this.detSolicitudArticulosDAO = detSolicitudArticulosDAO;
    }

    public CatArticuloDTO getArticuloSelected() {
        return articuloSelected;
    }

    public void setArticuloSelected(CatArticuloDTO articuloSelected) {
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
