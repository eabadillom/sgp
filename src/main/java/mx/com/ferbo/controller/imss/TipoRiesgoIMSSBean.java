package mx.com.ferbo.controller.imss;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.com.ferbo.dao.n.imss.TipoRiesgoIMSSDAO;
import mx.com.ferbo.model.imss.CatTipoRiesgoIMSS;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

/**
 *
 * @author alberto
 */
@Named(value = "tipoRiesgoIMSSBean")
@ViewScoped
public class TipoRiesgoIMSSBean implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(TipoRiesgoIMSSBean.class);
    
    private List<CatTipoRiesgoIMSS> listaTipoRiesgo;
    private CatTipoRiesgoIMSS tipoRiesgoSelected;
    private TipoRiesgoIMSSDAO tipoRiesgoIMSSDAO;
    
    private String accionBoton = "";
    private String iconoBoton = "";

    public TipoRiesgoIMSSBean() 
    {
        this.tipoRiesgoIMSSDAO = new TipoRiesgoIMSSDAO(); 
    }
    
    @PostConstruct
    public void init() 
    {
        this.inicializarListaTipoRiesgo();
    }
    
    public void inicializarListaTipoRiesgo()
    {
        this.listaTipoRiesgo = this.tipoRiesgoIMSSDAO.buscarTodos();
    }
    
    public void incializarSolicitud()
    {
        this.tipoRiesgoSelected = new CatTipoRiesgoIMSS();
        this.accionBoton = "Guardar";
        this.iconoBoton = "pi pi-save";
        
        PrimeFaces.current().ajax().update("form:pnlTipoRiesgo", "form:dialogoTipoRiesgo");
    }
    
    public void editarSolicitud(CatTipoRiesgoIMSS auxTipoSolicitud)
    {
        try
        {
            log.info("Editando un tipo de riesgo de trabajo: {}", auxTipoSolicitud.toString());
            this.tipoRiesgoSelected = auxTipoSolicitud;
            this.accionBoton = "Actualizar";
            this.iconoBoton = "pi pi-check";
            PrimeFaces.current().ajax().update("form:pnlTipoRiesgo", "form:dialogoTipoRiesgo");
        }catch (Exception ex) 
        {
            log.error("Problema al asignar el tipo de riesgo", ex);
        }
    }
    
    public void guardarTipoRiesgo()
    {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Guardar";
        try
        {
            if(this.tipoRiesgoSelected.getClave() == null)
            {
                throw new SGPException("Debes ingresar la clave");
            }
            
            if(this.tipoRiesgoSelected.getClave().length() > 2)
            {
                throw new SGPException("La clave debe contener de 1 a 2 caracteres");
            }
            
            if(this.tipoRiesgoSelected.getDescripcion() == null)
            {
                throw new SGPException("Debes ingresar la descripcion");
            }
            
            if(verificarClave(this.tipoRiesgoSelected.getClave()))
            {
                throw new SGPException("La clave ya se encuentra registrada");
            }
            
            this.tipoRiesgoIMSSDAO.guardar(tipoRiesgoSelected);
            log.info("El tipo de riesgo se guardo correctamente");
            mensaje = "Se guardo correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        }catch(SGPException ex)
        {
            titulo = "Error";
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("Error al registrar la solicitud de incapacidad: {}", ex);
        }catch (Exception e) 
        {
            titulo = "Error";
            mensaje = "Consulte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("ERROR, {}", e);
        }finally
        {
            this.inicializarListaTipoRiesgo();;
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().executeScript("PF('dialogoTipoRiesgo').hide()");
            PrimeFaces.current().ajax().update("form:messages", "form:dtCatalogoTipoRiesgo");
        }
    }
    
    public void actualizarTipoRiesgo()
    {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Actualizar";
        try
        {
            if(this.tipoRiesgoSelected.getClave() == null)
            {
                throw new SGPException("Debes ingresar la clave");
            }
            
            if(this.tipoRiesgoSelected.getClave().length() > 2)
            {
                throw new SGPException("La clave debe contener de 1 a 2 caracteres");
            }
            
            if(this.tipoRiesgoSelected.getDescripcion() == null)
            {
                throw new SGPException("Debes ingresar la descripcion");
            }
            
            this.tipoRiesgoIMSSDAO.actualizar(this.tipoRiesgoSelected);
            log.info("El tipo de riesgo se actualizo correctamente");
            mensaje = "Se actualizo correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        }catch(SGPException ex)
        {
            titulo = "Error";
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("Error al registrar la solicitud de incapacidad: {}", ex);
        }catch (Exception e) 
        {
            titulo = "Error";
            mensaje = "Consulte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("ERROR, {}", e);
        }finally
        {
            this.inicializarListaTipoRiesgo();
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().executeScript("PF('dialogoTipoRiesgo').hide()");
            PrimeFaces.current().ajax().update("form:messages", "form:dtCatalogoTipoRiesgo");
        }
    }
    
    public void metodoBoton()
    {
        switch(this.accionBoton)
        {
            case "Guardar":
                this.guardarTipoRiesgo();
                break;
            case "Actualizar":
                this.actualizarTipoRiesgo();
                break;
        }
    }
    
    public boolean verificarClave(String clave)
    {
        boolean exitoso = false;
        for(CatTipoRiesgoIMSS aux : this.listaTipoRiesgo)
        {
            if(clave.toUpperCase().matches(aux.getClave().toUpperCase()) && this.accionBoton.matches("Guardar"))
            {
                exitoso = true;
            }
        }
        return exitoso;
    }

    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public List<CatTipoRiesgoIMSS> getListaTipoRiesgo() 
    {
        return listaTipoRiesgo;
    }

    public void setListaTipoRiesgo(List<CatTipoRiesgoIMSS> listaTipoRiesgo) 
    {
        this.listaTipoRiesgo = listaTipoRiesgo;
    }

    public CatTipoRiesgoIMSS getTipoRiesgoSelected() 
    {
        return tipoRiesgoSelected;
    }

    public void setTipoRiesgoSelected(CatTipoRiesgoIMSS tipoRiesgoSelected) 
    {
        this.tipoRiesgoSelected = tipoRiesgoSelected;
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
