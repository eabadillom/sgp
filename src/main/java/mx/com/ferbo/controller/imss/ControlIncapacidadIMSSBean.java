package mx.com.ferbo.controller.imss;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.com.ferbo.dao.n.imss.ControlIncapacidadIMSSDAO;
import mx.com.ferbo.model.imss.CatControlIncapacidadIMSS;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

/**
 *
 * @author alberto
 */
@Named(value = "controlIncapacidadIMSS")
@ViewScoped
public class ControlIncapacidadIMSSBean implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(ControlIncapacidadIMSSBean.class);
    
    private CatControlIncapacidadIMSS controlIncapacidadSelected;
    private List<CatControlIncapacidadIMSS> listControlIncapacidadIMSS;
    private ControlIncapacidadIMSSDAO controIncapacidadIMSSDAO;
    
    private String accionBoton = "";
    private String iconoBoton = "";

    public ControlIncapacidadIMSSBean() 
    {
        this.controIncapacidadIMSSDAO = new ControlIncapacidadIMSSDAO();
    }
    
    @PostConstruct
    public void init() 
    {
        actualizarListaControlIncapacidades();
    }
    
    public void actualizarListaControlIncapacidades()
    {
        this.listControlIncapacidadIMSS = controIncapacidadIMSSDAO.buscarTodos();
    }
    
    public void incializarSolicitud()
    {
        this.controlIncapacidadSelected = new CatControlIncapacidadIMSS();
        this.accionBoton = "Guardar";
        this.iconoBoton = "pi pi-save";
        
        PrimeFaces.current().ajax().update("form:pnlControlIncapacidad", "form:dialogoControlIncapacidades");
    }
    
    public void editarSolicitud(CatControlIncapacidadIMSS aux)
    {
        log.info("Editando un control de incapacidad: {}", aux.toString());
        this.accionBoton = "Actualizar";
        this.iconoBoton = "pi pi-check";
        
        PrimeFaces.current().ajax().update("form:pnlControlIncapacidad", "form:dialogoControlIncapacidades");
    }
    
    public void guardarControlIncapacidad()
    {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Guardar";
        try
        {
            if(this.controlIncapacidadSelected.getClave() == null)
            {
                throw new SGPException("Debes ingresar la clave");
            }
            
            if(this.controlIncapacidadSelected.getClave().length() > 2)
            {
                throw new SGPException("La clave debe contener 1 o 2 caracteres");
            }
            
            if(this.controlIncapacidadSelected.getDescripcion() == null)
            {
                throw new SGPException("Debes ingresar la descripción");
            }
            
            if(verificarClave(this.controlIncapacidadSelected.getClave()))
            {
                throw new SGPException("La clave ya se encuentra registrada");
            }
            
            this.controIncapacidadIMSSDAO.guardar(this.controlIncapacidadSelected);
            log.info("El control de incapacidad se guardo correctamente");
            mensaje = "Se guardo correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        }catch(SGPException ex)
        {
            titulo = "Error";
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("Error al registrar la solicitud de control de incapacidad: {}", ex.getMessage());
        }catch (Exception e) 
        {
            titulo = "Error";
            mensaje = "Consulte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("ERROR, {}", e.getMessage());
        }finally
        {
            actualizarListaControlIncapacidades();
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().executeScript("PF('dialogoControlIncapacidades').hide()");
            PrimeFaces.current().ajax().update("form:messages","form:dtCatalogoControlIncapacidad");
        }
    }
    
    public void actualizarControlIncapacidad()
    {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Actualizar";
        try
        {
            if(this.controlIncapacidadSelected.getClave() == null)
            {
                throw new SGPException("Debes ingresar la clave");
            }
            
            if(this.controlIncapacidadSelected.getClave().length() > 2)
            {
                throw new SGPException("La clave debe ser igual a 1 o 2 caracteres");
            }
            
            if(this.controlIncapacidadSelected.getDescripcion() == null)
            {
                throw new SGPException("Debes ingresar la descripción");
            }
            
            this.controIncapacidadIMSSDAO.actualizar(this.controlIncapacidadSelected);
            log.info("El control de incapacidad se actualizo correctamente");
            mensaje = "Se actualizo correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        }catch(SGPException ex)
        {
            titulo = "Error";
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("Error al registrar la solicitud de control de incapacidad: {}", ex.getMessage());
        }catch (Exception e) 
        {
            titulo = "Error";
            mensaje = "Consulte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("ERROR, {}", e.getMessage());
        }finally
        {
            actualizarListaControlIncapacidades();
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().executeScript("PF('dialogoControlIncapacidades').hide()");
            PrimeFaces.current().ajax().update("form:messages","form:dtCatalogoControlIncapacidad");
        }
    }
    
    public void metodoBoton()
    {
        switch(this.accionBoton)
        {
            case "Guardar":
                this.guardarControlIncapacidad();
                break;
            case "Actualizar":
                this.actualizarControlIncapacidad();
                break;
        }
    }
    
    public boolean verificarClave(String clave)
    {
        boolean exitoso = false;
        for(CatControlIncapacidadIMSS aux : this.listControlIncapacidadIMSS)
        {
            if(clave.toUpperCase().matches(aux.getClave().toUpperCase()) && this.accionBoton.matches("Guardar"))
            {
                exitoso = true;
            }
        }
        return exitoso;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public CatControlIncapacidadIMSS getControlIncapacidadSelected() 
    {
        return controlIncapacidadSelected;
    }

    public void setControlIncapacidadSelected(CatControlIncapacidadIMSS controlIncapacidadSelected) 
    {
        this.controlIncapacidadSelected = controlIncapacidadSelected;
    }

    public List<CatControlIncapacidadIMSS> getListControlIncapacidadIMSS() 
    {
        return listControlIncapacidadIMSS;
    }

    public void setListControlIncapacidadIMSS(List<CatControlIncapacidadIMSS> listControlIncapacidadIMSS) 
    {
        this.listControlIncapacidadIMSS = listControlIncapacidadIMSS;
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
