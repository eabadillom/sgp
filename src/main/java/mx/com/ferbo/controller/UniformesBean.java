package mx.com.ferbo.controller;

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
import mx.com.ferbo.business.incidencia.EstatusIncidenciaBL;
import mx.com.ferbo.business.incidencia.EstatusSolicitudBL;
import mx.com.ferbo.business.notifmovil.NotifMovilBL;
import mx.com.ferbo.business.sgpapiclient.SGPApiClientBL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.ResponsiveOption;

import mx.com.ferbo.dao.n.PrendaDAO;
import mx.com.ferbo.dao.n.TallaDAO;
import mx.com.ferbo.dao.n.SolicitudPrendaDAO;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.IncidenciaDAO;
import mx.com.ferbo.dto.NotificacionMovilDTO;
import mx.com.ferbo.model.CatPrenda;
import mx.com.ferbo.model.CatTalla;
import mx.com.ferbo.model.CatTipoIncidencia;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.model.DetSolicitudPrenda;
import mx.com.ferbo.util.SGPException;
import mx.com.ferbo.util.ManageStatus;

@Named(value = "uniformesBean")
@ViewScoped
public class UniformesBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(UniformesBean.class);

    private String numeroEmpl;

    private List<CatPrenda> lstPrendasActivas;
    private List<CatTalla> lstTallasActivas;
    private List<DetSolicitudPrenda> lstSolicitudPrendas;
    private List<DetSolicitudPrenda> lstSolicitudPrendasRealizadas;
    private List<ResponsiveOption> responsiveOptions;
    
    private DetEmpleado empleadoSelected;
    private DetSolicitudPrenda solicitud;

    private PrendaDAO uniformesDAO;
    private final EmpleadoDAO empleadoDAO;
    private final SolicitudPrendaDAO solicitudPrendaDAO;
    private final TallaDAO tallaDAO;
    private final IncidenciaDAO incidenciaDAO;

    private CatPrenda prendaSelected;
    private CatTalla tallaSelected;
    private Integer cantidadSelected;

    private final FacesContext faceContext;
    private final HttpServletRequest httpServletRequest;
    private ManageStatus status;
    
    private DetIncidencia incidencia;
    private CatTipoIncidencia catTipoIncidencia;

    public UniformesBean() {
        lstPrendasActivas = new ArrayList<>();
        lstTallasActivas = new ArrayList<>();

        empleadoSelected = new DetEmpleado();
        prendaSelected = new CatPrenda();
        tallaSelected = new CatTalla();
        cantidadSelected = 0;

        uniformesDAO = new PrendaDAO();
        tallaDAO = new TallaDAO();
        empleadoDAO = new EmpleadoDAO();
        solicitudPrendaDAO = new SolicitudPrendaDAO();
        incidenciaDAO = new IncidenciaDAO();

        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        this.empleadoSelected = (DetEmpleado) httpServletRequest.getSession(true).getAttribute("empleado");
        
        solicitud = new DetSolicitudPrenda();
        }

    @PostConstruct
    public void init() {
        lstSolicitudPrendas = new ArrayList<>();
        solicitud.setEmpleadoSol(empleadoSelected);
        actualizarListas();
        lstPrendasActivas = uniformesDAO.buscarTodosActivos();
        lstTallasActivas = tallaDAO.buscarTodosActivos();

        responsiveOptions = new ArrayList<>();
        responsiveOptions.add(new ResponsiveOption("560px", 3, 3));
        responsiveOptions.add(new ResponsiveOption("280px", 2, 2));
        responsiveOptions.add(new ResponsiveOption("140px", 1, 1));
        status = new ManageStatus();
    }
    
    public void actualizarListas()
    {
        lstSolicitudPrendasRealizadas = solicitudPrendaDAO.buscarPorIdEmpleado(solicitud.getEmpleadoSol().getIdEmpleado());
    }

    public void seleccionarItem(CatPrenda item) {
        prendaSelected = item;
        solicitud = new DetSolicitudPrenda();
        PrimeFaces.current().executeScript("PF('dialogComplementoPrenda').show();");
    }

    public void preRegistro() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Uniforme";
        try
        {
            if (solicitud.getCantidad() == null) {
                throw new SGPException("No se a agregado el número de piezas");
            }
            if (solicitud.getCantidad() == 0) {
                throw new SGPException("No se puede pedir 0 piezas");
            }
            if (solicitud.getTalla() == null) {
                throw new SGPException("No se a seleccionado la talla de la prenda");
            }
            solicitud.setFechaCap(new Date());
            solicitud.setEmpleadoSol(empleadoSelected);
            solicitud.setPrenda(prendaSelected);
            lstSolicitudPrendas.add(solicitud);
            mensaje = "Prenda agregada";
            severity = FacesMessage.SEVERITY_INFO;
            PrimeFaces.current().executeInitScript("PF('dialogComplementoPrenda').hide()");
            PrimeFaces.current().executeInitScript("PF('uniformeDialog').hide()");
        } catch (SGPException e){
            log.warn("Error al guardar el registro de la prenda del empleado: {}", empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn("EX-0032: ", e);
            mensaje = e.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
        }catch(Exception e){
            log.warn("Error al guardar el registro del articulo del empleado: {}", empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn("EX-0032: ", e);
            mensaje = "Consulte con su administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
        }finally{
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("formActividadesUniformes:messages", "formActividadesUniformes:tabView:pnlPrendas", ":formActividadesUniformes:tabView:dt-uniformes", ":formActividadesUniformes:tabView:btnRegistro");
        }
    }
    
    public void editarUniforme(DetSolicitudPrenda solicitudPrenda)
    {
        solicitud = solicitudPrenda;
    }

    public void registro() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Uniforme";
        for (DetSolicitudPrenda detSolicitudPrenda : lstSolicitudPrendas) {
            try {
                detSolicitudPrenda.setEstatus(EstatusSolicitudBL.estatusEnviado());
                incidencia = new DetIncidencia();
                catTipoIncidencia = new CatTipoIncidencia();
                catTipoIncidencia.setIdTipo(3);
                
                incidencia.setTipoIncidencia(catTipoIncidencia);
                incidencia.setEmpleado(empleadoSelected);
                incidencia.setEstatusIncidencia(EstatusIncidenciaBL.enviado());
                incidencia.setVisible((short) 1);
                incidencia.setSolPrenda(detSolicitudPrenda);
                incidencia.setFechaCap(new Date());

                incidenciaDAO.guardar(incidencia);
                actualizarListas();
                mensaje = "Solicitud Registrada";
                severity = FacesMessage.SEVERITY_INFO;
                NotificacionMovilDTO nmDTO = NotifMovilBL.obtenerMensaje("uniforme", this.empleadoSelected);
                SGPApiClientBL sgpApiClient = new SGPApiClientBL();
                sgpApiClient.enviarNotificacion(nmDTO);
            } catch (SGPException e) 
            {
                log.warn("Error al guardar el registro de la prenda del empleado: {}", empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
                log.warn("EX-0032: ", e);
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
        lstSolicitudPrendas.clear();
        message = new FacesMessage(severity, titulo, mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
        PrimeFaces.current().ajax().update(":formActividadesUniformes:messages", ":formActividadesUniformes:tabView:dt-uniformes", ":formActividadesUniformes:tabView:btnRegistro");
    }
    
    public void actualizarRegistro()
    {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Uniforme";
        try
        {
            incidencia = incidenciaDAO.buscarPorPrenda(empleadoSelected.getIdEmpleado(), solicitud.getIdSolicitud());
            if(incidencia == null)
            {
                throw new SGPException("Error al editar la solicitud, favor de contactar al administrador de sistemas");
            }
            
            switch (solicitud.getEstatus().getClave()) 
            {
                case "A":
                    throw new SGPException("No se puede modificar la prenda");
                case "R":
                    throw new SGPException("No se puede modificar la prenda");
                case "C":
                    throw new SGPException("No se puede modificar la prenda");
            }
            
            solicitud.setEstatus(EstatusSolicitudBL.estatusCancelado());
            incidencia.setSolPrenda(solicitud);
            incidencia.setEstatusIncidencia(EstatusIncidenciaBL.cancelado());
            incidenciaDAO.actualizar(incidencia);
            actualizarListas();
            mensaje = "Se actualizo la solicitud correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        }
        catch(SGPException e)
        {
            log.warn("Error al actualizar el registro de la prenda del empleado: {}", empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn("EX-0032: ", e);
            mensaje = e.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
        }catch(Exception e)
        {
            log.warn("Error al actualizar el registro de la prenda del empleado: {}", empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn("EX-0032: ", e);
            mensaje = "Consulte con el administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
        }finally
        {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update(":formActividadesUniformes:messages", ":formActividadesUniformes:tabView:dt-uniformes-sol");
            PrimeFaces.current().executeScript("PF('dialogCambiarEstatus').hide()");
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public void inicializaPrenda() {
        prendaSelected = new CatPrenda();
    }

    public void eliminaSolicitud() {
        lstSolicitudPrendas.remove(solicitud);
    }

    public List<CatPrenda> getLstPrendasActivas() {
        return lstPrendasActivas;
    }

    public void setLstPrendasActivas(List<CatPrenda> lstPrendasActivas) {
        this.lstPrendasActivas = lstPrendasActivas;
    }

    public DetEmpleado getEmpleadoSelected() {
        return empleadoSelected;
    }

    public void setEmpleadoSelected(DetEmpleado empleadoSelected) {
        this.empleadoSelected = empleadoSelected;
    }

    public PrendaDAO getUniformesDAO() {
        return uniformesDAO;
    }

    public void setUniformesDAO(PrendaDAO uniformesDAO) {
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

    public CatPrenda getPrendaSelected() {
        return prendaSelected;
    }

    public void setPrendaSelected(CatPrenda prendaSelected) {
        this.prendaSelected = prendaSelected;
    }

    public List<CatTalla> getLstTallasActivas() {
        return lstTallasActivas;
    }

    public void setLstTallasActivas(List<CatTalla> lstTallasActivas) {
        this.lstTallasActivas = lstTallasActivas;
    }

    public CatTalla getTallaSelected() {
        return tallaSelected;
    }

    public void setTallaSelected(CatTalla tallaSelected) {
        this.tallaSelected = tallaSelected;
    }

    public Integer getCantidadSelected() {
        return cantidadSelected;
    }

    public void setCantidadSelected(Integer cantidadSelected) {
        this.cantidadSelected = cantidadSelected;
    }

    public List<DetSolicitudPrenda> getLstSolicitudPrendas() {
        return lstSolicitudPrendas;
    }

    public void setLstSolicitudPrendas(List<DetSolicitudPrenda> lstSolicitudPrendas) {
        this.lstSolicitudPrendas = lstSolicitudPrendas;
    }

    public DetSolicitudPrenda getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(DetSolicitudPrenda solicitud) {
        this.solicitud = solicitud;
    }

    public List<DetSolicitudPrenda> getLstSolicitudPrendasRealizadas() {
        return lstSolicitudPrendasRealizadas;
    }

    public void setLstSolicitudPrendasRealizadas(List<DetSolicitudPrenda> lstSolicitudPrendasRealizadas) {
        this.lstSolicitudPrendasRealizadas = lstSolicitudPrendasRealizadas;
    }

    public List<ResponsiveOption> getResponsiveOptions() {
        return responsiveOptions;
    }

    public void setResponsiveOptions(List<ResponsiveOption> responsiveOptions) {
        this.responsiveOptions = responsiveOptions;
    }
    
    public ManageStatus getStatus() {
        return status;
    }
    //</editor-fold>
}
