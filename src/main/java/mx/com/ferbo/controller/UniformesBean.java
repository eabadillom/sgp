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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.ResponsiveOption;

import mx.com.ferbo.dao.n.PrendaDAO;
import mx.com.ferbo.dao.n.TallaDAO;
import mx.com.ferbo.dao.n.SolicitudPrendaDAO;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.IncidenciaDAO;
import mx.com.ferbo.model.CatEstatusIncidencia;
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
    private List<DetIncidencia> incidenciasBuscada;
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
    private CatEstatusIncidencia catEstatusIncidencia;

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
        solicitud.setIdEmpleadoSol(empleadoSelected);
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
        lstSolicitudPrendasRealizadas = solicitudPrendaDAO.buscarPorIdEmpleado(solicitud.getIdEmpleadoSol().getIdEmpleado());
        incidenciasBuscada = incidenciaDAO.buscarPorIdEmpleadoPrenda(solicitud.getIdEmpleadoSol().getIdEmpleado());
    }

    public void seleccionarItem(CatPrenda item) {
        prendaSelected = item;
        solicitud = new DetSolicitudPrenda();
        PrimeFaces.current().executeScript("PF('dialogComplementoPrenda').show();");
    }

    public void preRegistro() {
        if (solicitud.getCantidad() == 0) {
            FacesContext.getCurrentInstance().addMessage(FacesContext.getCurrentInstance().getViewRoot().findComponent("formActividadesUniformes:tabView:spCantidad").getClientId(),
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Campo requerido"));
            PrimeFaces.current().ajax().update("formActividadesUniformes:tabView:pnlPrendas");
            return;
        }
        if (solicitud.getIdTalla().getIdTalla() == null) {
            FacesContext.getCurrentInstance().addMessage(FacesContext.getCurrentInstance().getViewRoot().findComponent("formActividadesUniformes:tabView:soTalla").getClientId(),
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Campo requerido"));
            PrimeFaces.current().ajax().update("formActividadesUniformes:tabView:pnlPrendas");
            return;
        }
        solicitud.setFechaCap(new Date());
        solicitud.setIdEmpleadoSol(empleadoSelected);
        solicitud.setIdPrenda(prendaSelected);
        lstSolicitudPrendas.add(solicitud);
        PrimeFaces.current().executeInitScript("PF('dialogComplementoPrenda').hide()");
        PrimeFaces.current().executeInitScript("PF('uniformeDialog').hide()");
        PrimeFaces.current().ajax().update("formActividadesUniformes:messages", "formActividadesUniformes:tabView:dt-uniformes", "formActividadesUniformes:tabView:btnRegistro");

    }

    public void registro() throws IOException {
        for (DetSolicitudPrenda detSolicitudPrenda : lstSolicitudPrendas) {
            try {
                detSolicitudPrenda.setAprobada((short) 1);
                solicitudPrendaDAO.guardar(detSolicitudPrenda);
                actualizarListas();
                
                incidencia = new DetIncidencia();
                catTipoIncidencia = new CatTipoIncidencia();
                catEstatusIncidencia = new CatEstatusIncidencia();
                
                catTipoIncidencia.setIdTipo(3);
                catEstatusIncidencia.setIdEstatus(1);
                
                incidencia.setIdTipo(catTipoIncidencia);
                incidencia.setIdEmpleado(empleadoSelected);
                incidencia.setIdEstatus(catEstatusIncidencia);
                incidencia.setVisible((short) 1);
                incidencia.setIdSolPrenda(detSolicitudPrenda);
                incidencia.setFechaCap(new Date());

                incidenciaDAO.guardar(incidencia);
            } catch (SGPException e) {
                log.warn("EX-0032: " + e.getMessage() + ". Error al guardar el registro de la prenda del empleado: " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            }
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("uniformes.xhtml");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Solicitud Registrada"));
    }
    
    public void actualizarRegistro()
    {
        actualizarListas();
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Uniforme";
        try
        {
            if(incidenciasBuscada == null)
            {
                throw new SGPException("Error con la conexión a la base de datos!!!");
            }
            
            switch (solicitud.getAprobada().intValue()) 
            {
                case 2:
                    throw new SGPException("No se puede modificar la prenda");
                case 3:
                    throw new SGPException("No se puede modificar la prenda");
                case 4:
                    throw new SGPException("No se puede modificar la prenda");
            }
            
            for(DetIncidencia auxIncidenciaBuscada : incidenciasBuscada)
            {
                if(auxIncidenciaBuscada.getIdSolPrenda().getIdSolicitud().equals(solicitud.getIdSolicitud()))
                {
                    catEstatusIncidencia = new CatEstatusIncidencia();
                    catEstatusIncidencia.setIdEstatus(4);
            
                    auxIncidenciaBuscada.setIdEstatus(catEstatusIncidencia);
                    auxIncidenciaBuscada.setIdSolPrenda(solicitud);
                    incidenciaDAO.actualizar(auxIncidenciaBuscada);                    
                }
            }
            
            solicitud.setAprobada((short) 4);
            solicitudPrendaDAO.actualizar(solicitud);
            actualizarListas();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Estatus Modificada"));
            PrimeFaces.current().ajax().update("formActividadesUniformes:tabView:dt-uniformes-sol");
            PrimeFaces.current().executeScript("PF('dialogCambiarEstatus').hide()");
        }
        catch(SGPException e)
        {
            mensaje = e.getMessage();
            severity = FacesMessage.SEVERITY_INFO;
        }catch(Exception e)
        {
            log.warn("EX-0032: " + e.getMessage() + ". Error al actualizar el registro de la prenda del empleado: " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
        }finally
        {
            if(severity != null)
            {
                message = new FacesMessage(severity, titulo, mensaje);
                FacesContext.getCurrentInstance().addMessage(null, message);
                PrimeFaces.current().ajax().update(":formActividadesUniformes:messages");
                PrimeFaces.current().executeScript("PF('dialogCambiarEstatus').hide()");
            }
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
