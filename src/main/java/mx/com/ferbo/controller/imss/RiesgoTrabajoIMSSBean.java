package mx.com.ferbo.controller.imss;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.com.ferbo.dao.n.imss.RiesgoTrabajoIMSSDAO;
import mx.com.ferbo.model.imss.CatRiesgoTrabajoIMSS;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

/**
 *
 * @author alberto
 */
@Named(value = "riesgoTrabajoIMSSBean")
@ViewScoped
public class RiesgoTrabajoIMSSBean implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(RiesgoTrabajoIMSSBean.class);
    
    private CatRiesgoTrabajoIMSS riesgoTrabajoSelected;
    private List<CatRiesgoTrabajoIMSS> listaRiesgoTrabajo;
    private RiesgoTrabajoIMSSDAO riesgoTrabajoDAO;
    
    private String accionBoton = "";
    private String iconoBoton = "";
    
    public RiesgoTrabajoIMSSBean() 
    {
        this.riesgoTrabajoDAO = new RiesgoTrabajoIMSSDAO();
    }
    
    @PostConstruct
    public void init() 
    {
        actualizarListaRiesgoTrabajo();
    }
    
    public void actualizarListaRiesgoTrabajo()
    {
        this.listaRiesgoTrabajo = this.riesgoTrabajoDAO.buscarTodos();
    }
    
    public void incializarSolicitud()
    {
        this.riesgoTrabajoSelected = new CatRiesgoTrabajoIMSS();
        accionBoton = "Guardar";
        this.iconoBoton = "pi pi-save";
        
        PrimeFaces.current().ajax().update("form:pnlRiesgoTrabajo", "form:dialogoRiesgoTrabajo");
    }
    
    public void editarSolicitud(CatRiesgoTrabajoIMSS aux)
    {
        log.info("Editando un riesgo de trabajo: {}", aux.toString());
        accionBoton = "Actualizar";
        this.iconoBoton = "pi pi-check";
        
        PrimeFaces.current().ajax().update("form:pnlRiesgoTrabajo", "form:dialogoRiesgoTrabajo");
    }
    
    public void guardarRiesgoTrabajo()
    {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Guardar";
        try
        {
            if(this.riesgoTrabajoSelected.getClave() == null)
            {
                throw new SGPException("Debes ingresar la clave");
            }
            
            if(this.riesgoTrabajoSelected.getClave().length() > 5)
            {
                throw new SGPException("La clave debe contener de 1 a 5 caracteres");
            }
            
            if(this.riesgoTrabajoSelected.getDescripcion() == null)
            {
                throw new SGPException("Debes ingresar la descripción");
            }
            
            if(verificarClave(this.riesgoTrabajoSelected.getClave()))
            {
                throw new SGPException("La clave ya se encuentra registrada");
            }
            
            this.riesgoTrabajoDAO.guardar(this.riesgoTrabajoSelected);
            log.info("El riesgo de trabajo se guardo correctamente");
            mensaje = "Se guardo correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        }catch(SGPException ex)
        {
            titulo = "Error";
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("Error al registrar la solicitud de riesgo de trabajo: {}", ex);
        }catch (Exception e) 
        {
            titulo = "Error";
            mensaje = "Consulte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("ERROR, {}", e);
        }finally
        {
            actualizarListaRiesgoTrabajo();
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().executeScript("PF('dialogoRiesgoTrabajo').hide()");
            PrimeFaces.current().ajax().update("form:messages","form:dtCatalogoRiesgoTrabajo");
        }
    }
    
    public void actualizarRiesgoTrabajo()
    {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Actualizar";
        try
        {
            if(this.riesgoTrabajoSelected.getClave() == null)
            {
                throw new SGPException("Debes ingresar la clave");
            }
            
            if(this.riesgoTrabajoSelected.getClave().length() > 5)
            {
                throw new SGPException("La clave debe contener de 1 a 5 caracteres");
            }
            
            if(this.riesgoTrabajoSelected.getDescripcion() == null)
            {
                throw new SGPException("Debes ingresar la descripción");
            }
            
            this.riesgoTrabajoDAO.actualizar(this.riesgoTrabajoSelected);
            log.info("El riesgo de trabajo se actualizo correctamente");
            mensaje = "Se actualizo correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        }catch(SGPException ex)
        {
            titulo = "Error";
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("Error al actualizar la solicitud de riesgo de trabajo: {}", ex);
        }catch (Exception e) 
        {
            titulo = "Error";
            mensaje = "Consulte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("ERROR, {}", e);
        }finally
        {
            actualizarListaRiesgoTrabajo();
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().executeScript("PF('dialogoRiesgoTrabajo').hide()");
            PrimeFaces.current().ajax().update("form:messages","form:dtCatalogoRiesgoTrabajo");
        }
    }
    
    public void metodoBoton()
    {
        switch(this.accionBoton)
        {
            case "Guardar":
                this.guardarRiesgoTrabajo();
                break;
            case "Actualizar":
                this.actualizarRiesgoTrabajo();
                break;
        }
    }
    
    public boolean verificarClave(String clave)
    {
        boolean exitoso = false;
        for(CatRiesgoTrabajoIMSS aux : this.listaRiesgoTrabajo)
        {
            if(clave.toUpperCase().matches(aux.getClave().toUpperCase()) && this.accionBoton.matches("Guardar"))
            {
                exitoso = true;
            }
        }
        return exitoso;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public CatRiesgoTrabajoIMSS getRiesgoTrabajoSelected() 
    {
        return riesgoTrabajoSelected;
    }

    public void setRiesgoTrabajoSelected(CatRiesgoTrabajoIMSS riesgoTrabajoSelected) 
    {
        this.riesgoTrabajoSelected = riesgoTrabajoSelected;
    }

    public List<CatRiesgoTrabajoIMSS> getListaRiesgoTrabajo() 
    {
        return listaRiesgoTrabajo;
    }

    public void setListaRiesgoTrabajo(List<CatRiesgoTrabajoIMSS> listaRiesgoTrabajo) 
    {
        this.listaRiesgoTrabajo = listaRiesgoTrabajo;
    }

    public String getAccionBoton() 
    {
        return accionBoton;
    }

    public void setAccionBoton(String accionBoton) 
    {
        this.accionBoton = accionBoton;
    }
    
    public String getIconoBoton() 
    {
        return iconoBoton;
    }

    public void setIconoBoton(String iconoBoton) 
    {
        this.iconoBoton = iconoBoton;
    }
    //</editor-fold>
    
}
