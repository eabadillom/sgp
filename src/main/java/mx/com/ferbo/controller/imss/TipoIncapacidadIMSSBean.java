package mx.com.ferbo.controller.imss;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.com.ferbo.dao.n.imss.TipoIncapacidadIMSSDAO;
import mx.com.ferbo.dao.n.sat.TipoIncapacidadSATDAO;
import mx.com.ferbo.model.imss.CatTipoIncapacidadIMSS;
import mx.com.ferbo.model.sat.CatTipoIncapacidadSAT;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

/**
 *
 * @author alberto
 */
@Named(value = "tipoIncapacidadIMSS")
@ViewScoped
public class TipoIncapacidadIMSSBean implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(TipoIncapacidadIMSSBean.class);
    
    private List<CatTipoIncapacidadIMSS> listaTipoIncapacidadesIMSS;
    private CatTipoIncapacidadIMSS tipoIncapacidadSelected;
    private TipoIncapacidadIMSSDAO incapacidadIMSSDAO;
    
    private List<CatTipoIncapacidadSAT> listaIncapacidadesSAT;
    private CatTipoIncapacidadSAT tipoIncapacidadSAT;
    private TipoIncapacidadSATDAO incapacidadSATDAO;
    
    private String accionBoton = "";
    private String iconoBoton = "";
    
    public TipoIncapacidadIMSSBean() 
    {
        this.listaTipoIncapacidadesIMSS = new ArrayList<>();
        this.incapacidadIMSSDAO = new TipoIncapacidadIMSSDAO();
        this.incapacidadSATDAO = new TipoIncapacidadSATDAO();
    }
    
    @PostConstruct
    public void init() 
    {
        actualizarListaIncapacidades();
        this.listaIncapacidadesSAT = this.incapacidadSATDAO.buscarTodos();
    }
    
    public void actualizarListaIncapacidades()
    {
        this.listaTipoIncapacidadesIMSS = this.incapacidadIMSSDAO.buscarTodos();
    }
    
    public void incializarSolicitud()
    {
        this.tipoIncapacidadSelected = new CatTipoIncapacidadIMSS();
        this.tipoIncapacidadSAT = new CatTipoIncapacidadSAT();
        this.accionBoton = "Guardar";
        this.iconoBoton = "pi pi-save";
        
        PrimeFaces.current().ajax().update("form:pnlIncapacidad", "form:dialogoIncapacidades");
    }
    
    public void editarSolicitud(CatTipoIncapacidadSAT auxTipoIncapacidadSat)
    {
        try
        {
            log.info("Editando un tipo de incapacidad: {}", this.tipoIncapacidadSelected);
            this.tipoIncapacidadSAT = auxTipoIncapacidadSat;
            this.accionBoton = "Actualizar";
            this.iconoBoton = "pi pi-check";
            
            PrimeFaces.current().ajax().update("form:pnlIncapacidad", "form:dialogoIncapacidades");
        }catch (Exception ex) 
        {
            log.error("Problema al asignar la incapacidad del SAT", ex);
        }
    }
    
    public void guardarIncapacidad()
    {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Guardar";
        try
        {
            if(this.tipoIncapacidadSelected.getClave() == null)
            {
                throw new SGPException("Debes ingresar la clave");
            }
            
            if(this.tipoIncapacidadSelected.getClave().length() > 5)
            {
                throw new SGPException("La clave debe contener de 1 a 5 caracteres");
            }
            
            if(this.tipoIncapacidadSelected.getDescripcion() == null)
            {
                throw new SGPException("Debes ingresar la descripcion");
            }
            
            if(this.tipoIncapacidadSelected.getPorcentagePago() == null)
            {
                throw new SGPException("Debes ingresar el porcentaje");
            }
            
            if(this.tipoIncapacidadSelected.getMaxDias() == null)
            {
                throw new SGPException("Debes ingresar el total de dias");
            }
            
            if(this.tipoIncapacidadSAT.getDescripcion() == null)
            {
                throw new SGPException("Debes seleccionar el tipo de incapacidad del SAT");
            }
            
            if(verificarClave(this.tipoIncapacidadSelected.getClave()))
            {
                throw new SGPException("La clave ya se encuentra registrada");
            }
            
            this.tipoIncapacidadSelected.setIncapacidadSAT(this.tipoIncapacidadSAT);
            this.incapacidadIMSSDAO.guardar(this.tipoIncapacidadSelected);
            log.info("El control de incapacidad se guardo correctamente");
            mensaje = "Se guardo correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        }catch(SGPException ex)
        {
            titulo = "Error";
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("Error al registrar la solicitud de incapacidad: {}", ex.getMessage());
        }catch (Exception e) 
        {
            titulo = "Error";
            mensaje = "Consulte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("ERROR, {}", e.getMessage());
        }finally
        {
            actualizarListaIncapacidades();
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().executeScript("PF('dialogoIncapacidades').hide()");
            PrimeFaces.current().ajax().update("form:messages","form:dtCatalogoIncapacidad");
        }
    }
    
    public void actualizarIncapacidad()
    {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Actualizar";
        try
        {
            if(this.tipoIncapacidadSelected.getClave() == null)
            {
                throw new SGPException("Debes ingresar la clave");
            }
            
            if(this.tipoIncapacidadSelected.getClave().length() > 5)
            {
                throw new SGPException("La clave debe ser de 1 a 5 caracteres");
            }
            
            if(this.tipoIncapacidadSelected.getDescripcion() == null)
            {
                throw new SGPException("Debes ingresar la descripcion");
            }
            
            if(this.tipoIncapacidadSelected.getPorcentagePago() == null)
            {
                throw new SGPException("Debes ingresar el porcentaje");
            }
            
            if(this.tipoIncapacidadSelected.getMaxDias() == null)
            {
                throw new SGPException("Debes ingresar el total de dias");
            }
            
            if(this.tipoIncapacidadSAT.getDescripcion() == null)
            {
                throw new SGPException("Debes seleccionar el tipo de incapacidad del SAT");
            }
            
            this.tipoIncapacidadSelected.setIncapacidadSAT(this.tipoIncapacidadSAT);
            this.incapacidadIMSSDAO.actualizar(this.tipoIncapacidadSelected);
            log.info("El control de incapacidad se actualizo correctamente");
            mensaje = "Se actualizo correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        }catch(SGPException ex)
        {
            titulo = "Error";
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("Error al registrar la solicitud de incapacidad: {}", ex.getMessage());
        }catch (Exception e) 
        {
            titulo = "Error";
            mensaje = "Consulte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("ERROR, {}", e.getMessage());
        }finally
        {
            actualizarListaIncapacidades();
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().executeScript("PF('dialogoIncapacidades').hide()");
            PrimeFaces.current().ajax().update("form:messages","form:dtCatalogoIncapacidad");
        }
    }
    
    public void metodoBoton()
    {
        switch(this.accionBoton)
        {
            case "Guardar":
                this.guardarIncapacidad();
                break;
            case "Actualizar":
                this.actualizarIncapacidad();
                break;
        }
    }
    
    public boolean verificarClave(String clave)
    {
        boolean exitoso = false;
        for(CatTipoIncapacidadIMSS aux : this.listaTipoIncapacidadesIMSS)
        {
            if(clave.toUpperCase().matches(aux.getClave().toUpperCase()) && this.accionBoton.matches("Guardar"))
            {
                exitoso = true;
            }
        }
        return exitoso;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public CatTipoIncapacidadIMSS getTipoIncapacidadSelected() 
    {
        return tipoIncapacidadSelected;
    }

    public void setTipoIncapacidadSelected(CatTipoIncapacidadIMSS tipoIncapacidadSelected) 
    {
        this.tipoIncapacidadSelected = tipoIncapacidadSelected;
    }

    public List<CatTipoIncapacidadIMSS> getListaTipoIncapacidadesIMSS() 
    {
        return listaTipoIncapacidadesIMSS;
    }

    public void setListaTipoIncapacidadesIMSS(List<CatTipoIncapacidadIMSS> listaTipoIncapacidadesIMSS) 
    {
        this.listaTipoIncapacidadesIMSS = listaTipoIncapacidadesIMSS;
    }
    
    public CatTipoIncapacidadSAT getTipoIncapacidadSAT() 
    {
        return tipoIncapacidadSAT;
    }

    public void setTipoIncapacidadSAT(CatTipoIncapacidadSAT tipoIncapacidadSAT) 
    {
        this.tipoIncapacidadSAT = tipoIncapacidadSAT;
    }
    
    public List<CatTipoIncapacidadSAT> getListaIncapacidadesSAT() 
    {
        return listaIncapacidadesSAT;
    }

    public void setListaIncapacidadesSAT(List<CatTipoIncapacidadSAT> listaIncapacidadesSAT) 
    {
        this.listaIncapacidadesSAT = listaIncapacidadesSAT;
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
