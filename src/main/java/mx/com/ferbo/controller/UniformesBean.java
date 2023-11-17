package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.CatPrendaDAO;
import mx.com.ferbo.dao.CatTallaDAO;
import mx.com.ferbo.dao.DetSolicitudPrendaDAO;
import mx.com.ferbo.dao.EmpleadoDAO;
import mx.com.ferbo.dao.IncidenciaDAO;
import mx.com.ferbo.dto.CatEstatusIncidenciaDTO;
import mx.com.ferbo.dto.CatPrendaDTO;
import mx.com.ferbo.dto.CatTallaDTO;
import mx.com.ferbo.dto.CatTipoIncidenciaDTO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import mx.com.ferbo.dto.DetIncidenciaDTO;
import mx.com.ferbo.dto.DetSolicitudPrendaDTO;
import mx.com.ferbo.util.SGPException;
import org.primefaces.model.ResponsiveOption;
import org.apache.log4j.Logger;

@Named(value = "uniformesBean")
@ViewScoped
public class UniformesBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static final Logger log = Logger.getLogger(UniformesBean.class);

    private String numeroEmpl;

    private List<CatPrendaDTO> lstPrendasActivas;
    private List<CatTallaDTO> lstTallasActivas;
    private List<DetSolicitudPrendaDTO> lstSolicitudPrendas;
    private List<DetSolicitudPrendaDTO> lstSolicitudPrendasRealizadas;
    private List<ResponsiveOption> responsiveOptions;

    private DetEmpleadoDTO empleadoSelected;
    private DetSolicitudPrendaDTO solicitud;

    private CatPrendaDAO uniformesDAO;
    private final EmpleadoDAO empleadoDAO;
    private final DetSolicitudPrendaDAO detSolicitudPrendaDAO;
    private final CatTallaDAO tallaDAO;
    private final IncidenciaDAO incidenciaDAO;

    private CatPrendaDTO prendaSelected;
    private CatTallaDTO tallaSelected;
    private Integer cantidadSelected;

    private final FacesContext faceContext;
    private final HttpServletRequest httpServletRequest;
    
    private final DetIncidenciaDTO incidenciaDTO;
    private final CatTipoIncidenciaDTO catTipoIncidenciaDTO;
    private final CatEstatusIncidenciaDTO catEstatusIncidenciaDTO;

    public UniformesBean() {
        lstPrendasActivas = new ArrayList<>();
        lstTallasActivas = new ArrayList<>();

        empleadoSelected = new DetEmpleadoDTO();
        prendaSelected = new CatPrendaDTO();
        tallaSelected = new CatTallaDTO();
        cantidadSelected = 0;

        uniformesDAO = new CatPrendaDAO();
        tallaDAO = new CatTallaDAO();
        empleadoDAO = new EmpleadoDAO();
        detSolicitudPrendaDAO = new DetSolicitudPrendaDAO();
        incidenciaDAO = new IncidenciaDAO();

        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        this.empleadoSelected = (DetEmpleadoDTO) httpServletRequest.getSession(true).getAttribute("empleado");

        solicitud = new DetSolicitudPrendaDTO();
        incidenciaDTO = new DetIncidenciaDTO();
        catTipoIncidenciaDTO = new CatTipoIncidenciaDTO();
        catEstatusIncidenciaDTO = new CatEstatusIncidenciaDTO();
    }

    @PostConstruct
    public void init() {
        lstSolicitudPrendas = new ArrayList<>();
        solicitud.setEmpleadoSol(empleadoSelected);
        lstSolicitudPrendasRealizadas = detSolicitudPrendaDAO.buscarPorCriterios(solicitud);
        lstPrendasActivas = uniformesDAO.buscarActivo();
        lstTallasActivas = tallaDAO.buscarActivo();

        responsiveOptions = new ArrayList<>();
        responsiveOptions.add(new ResponsiveOption("560px", 3, 3));
        responsiveOptions.add(new ResponsiveOption("280px", 2, 2));
        responsiveOptions.add(new ResponsiveOption("140px", 1, 1));
    }

    public void seleccionarItem(CatPrendaDTO item) {
        prendaSelected = item;
        solicitud = new DetSolicitudPrendaDTO();
        PrimeFaces.current().executeScript("PF('dialogComplementoPrenda').show();");
    }

    public void preRegistro() {
        if (solicitud.getCantidad() == 0) {
            FacesContext.getCurrentInstance().addMessage(FacesContext.getCurrentInstance().getViewRoot().findComponent("formActividadesUniformes:tabView:spCantidad").getClientId(),
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Campo requerido"));
            PrimeFaces.current().ajax().update("formActividadesUniformes:tabView:pnlPrendas");
            return;
        }
        if (solicitud.getTalla() == null) {
            FacesContext.getCurrentInstance().addMessage(FacesContext.getCurrentInstance().getViewRoot().findComponent("formActividadesUniformes:tabView:soTalla").getClientId(),
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Campo requerido"));
            PrimeFaces.current().ajax().update("formActividadesUniformes:tabView:pnlPrendas");
            return;
        }
        solicitud.setFechaCap(new Date());
        solicitud.setEmpleadoSol(empleadoSelected);
        solicitud.setPrenda(prendaSelected);
        lstSolicitudPrendas.add(solicitud);
        PrimeFaces.current().executeInitScript("PF('dialogComplementoPrenda').hide()");
        PrimeFaces.current().executeInitScript("PF('uniformeDialog').hide()");
        PrimeFaces.current().ajax().update("formActividadesUniformes:messages", "formActividadesUniformes:tabView:dt-uniformes", "formActividadesUniformes:tabView:btnRegistro");

    }

    public void registro()  throws IOException {
        for (DetSolicitudPrendaDTO detSolicitudPrendaDTO : lstSolicitudPrendas) {
            try {
                detSolicitudPrendaDAO.guardar(detSolicitudPrendaDTO);
                lstSolicitudPrendasRealizadas = detSolicitudPrendaDAO.buscarPorCriterios(detSolicitudPrendaDTO);
                
                catTipoIncidenciaDTO.setIdTipo(3);
                catEstatusIncidenciaDTO.setIdEstatus(1);

                incidenciaDTO.setCatTipoIncidenciaDTO(catTipoIncidenciaDTO);
                incidenciaDTO.setDetEmpleadoDTO(empleadoSelected);
                incidenciaDTO.setCatEstatusIncidenciaDTO(catEstatusIncidenciaDTO);
                incidenciaDTO.setVisible((short) 1);
                incidenciaDTO.setDetSolicitudPrendaDTO(detSolicitudPrendaDTO);
                incidenciaDTO.setFechaCap(new Date());

                incidenciaDAO.guardar(incidenciaDTO);
            } catch (SGPException e) {
                log.warn("EX-0032: " + e.getMessage() + ". Error al guardar el registro de la prenda del empleado: " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            }
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("uniformes.xhtml");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Solicitud Registrada"));
    }

    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public void inicializaPrenda() {
        prendaSelected = new CatPrendaDTO();
    }

    public void eliminaSolicitud() {
        lstSolicitudPrendas.remove(solicitud);
    }

    public List<CatPrendaDTO> getLstPrendasActivas() {
        return lstPrendasActivas;
    }

    public void setLstPrendasActivas(List<CatPrendaDTO> lstPrendasActivas) {
        this.lstPrendasActivas = lstPrendasActivas;
    }

    public DetEmpleadoDTO getEmpleadoSelected() {
        return empleadoSelected;
    }

    public void setEmpleadoSelected(DetEmpleadoDTO empleadoSelected) {
        this.empleadoSelected = empleadoSelected;
    }

    public CatPrendaDAO getUniformesDAO() {
        return uniformesDAO;
    }

    public void setUniformesDAO(CatPrendaDAO uniformesDAO) {
        this.uniformesDAO = uniformesDAO;
    }

    public EmpleadoDAO getEmpleadoDAO() {
        return empleadoDAO;
    }

    public String getNumeroEmpl() {
        return numeroEmpl;
    }

    public void setNumeroEmpl(String numeroEmpl) {
        this.numeroEmpl = numeroEmpl;
    }

    public CatPrendaDTO getPrendaSelected() {
        return prendaSelected;
    }

    public void setPrendaSelected(CatPrendaDTO prendaSelected) {
        this.prendaSelected = prendaSelected;
    }

    public List<CatTallaDTO> getLstTallasActivas() {
        return lstTallasActivas;
    }

    public void setLstTallasActivas(List<CatTallaDTO> lstTallasActivas) {
        this.lstTallasActivas = lstTallasActivas;
    }

    public CatTallaDTO getTallaSelected() {
        return tallaSelected;
    }

    public void setTallaSelected(CatTallaDTO tallaSelected) {
        this.tallaSelected = tallaSelected;
    }

    public Integer getCantidadSelected() {
        return cantidadSelected;
    }

    public void setCantidadSelected(Integer cantidadSelected) {
        this.cantidadSelected = cantidadSelected;
    }

    public List<DetSolicitudPrendaDTO> getLstSolicitudPrendas() {
        return lstSolicitudPrendas;
    }

    public void setLstSolicitudPrendas(List<DetSolicitudPrendaDTO> lstSolicitudPrendas) {
        this.lstSolicitudPrendas = lstSolicitudPrendas;
    }

    public DetSolicitudPrendaDTO getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(DetSolicitudPrendaDTO solicitud) {
        this.solicitud = solicitud;
    }

    public List<DetSolicitudPrendaDTO> getLstSolicitudPrendasRealizadas() {
        return lstSolicitudPrendasRealizadas;
    }

    public void setLstSolicitudPrendasRealizadas(List<DetSolicitudPrendaDTO> lstSolicitudPrendasRealizadas) {
        this.lstSolicitudPrendasRealizadas = lstSolicitudPrendasRealizadas;
    }

    public List<ResponsiveOption> getResponsiveOptions() {
        return responsiveOptions;
    }

    public void setResponsiveOptions(List<ResponsiveOption> responsiveOptions) {
        this.responsiveOptions = responsiveOptions;
    }
    //</editor-fold>
}
