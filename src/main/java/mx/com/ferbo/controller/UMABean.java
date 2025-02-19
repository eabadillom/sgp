package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.UMADAO;
import mx.com.ferbo.model.CatUMA;
import mx.com.ferbo.util.SGPException;
import org.primefaces.PrimeFaces;
import mx.com.ferbo.util.DateUtil;

/**
 *
 * @author alberto
 */
@Named(value = "umaBean")
@ViewScoped
public class UMABean implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(UMABean.class);
    
    private CatUMA umaSelected = null;
    private UMADAO umaDAO = null;
    private List<CatUMA> catUMAS = null;
    private List<CatUMA> catUMASeleccionada = null;
    
    private Date fechaPublicacion;
    private Date fechaAplicacion;
    private Date fechaVigencia;
    
    private String accionBoton = "";
    private String iconoBoton = "";
    
    public UMABean()
    {
        this.catUMASeleccionada = new ArrayList<CatUMA>(); 
        this.umaDAO = new UMADAO();
    }
    
    @PostConstruct
    public void init() 
    {
        actualizarListaUmas();
    }
    
    public void actualizarListaUmas()
    {
        this.catUMAS = this.umaDAO.obtenerLista();
    }
    
    public void incializarUMA()
    {
        log.info("Creando una UMA...");
        this.accionBoton = "Guardar";
        this.iconoBoton = "pi pi-save";
        this.umaSelected = new CatUMA();
        this.fechaAplicacion = null;
        this.fechaPublicacion = null;
        this.fechaVigencia = null;
        
        PrimeFaces.current().ajax().update("form:pnlContentUMA", "form:manageUMADialog");
    }
    
    public void editarUMA(CatUMA auxUma)
    {
        log.info("Editando una UMA: {}", auxUma.toString());
        this.accionBoton = "Actualizar";
        this.iconoBoton = "pi pi-check";
        this.fechaAplicacion = DateUtil.toDate(auxUma.getFechaAplicacion());
        this.fechaPublicacion = DateUtil.toDate(auxUma.getFechaPublicacion());
        this.fechaVigencia = DateUtil.toDate(auxUma.getFechaVigencia());
        
        PrimeFaces.current().ajax().update("form:pnlContentUMA", "form:manageUMADialog");
    }
    
    public void metodoBoton()
    {
        switch(this.accionBoton)
        {
            case "Guardar":
                this.guardarUMA();
                break;
            case "Actualizar":
                this.actualizarUMA();
                break;
        }
    }
    
    public void guardarUMA()
    {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "UMA Agregada";
        try 
        {
            if(this.umaSelected.getAnio() == null)
            {
                throw new SGPException("Error. Debes ingresar un año");
            }
            
            if(this.umaSelected.getImporteDiario() == null)
            {
                throw new SGPException("Error. Debes ingresar un importe");
            }
            
            if(this.fechaPublicacion == null)
            {
                throw new SGPException("Error. Debes ingresar una fecha de publicacion");
            }
            
            if(this.fechaAplicacion == null)
            {
                throw new SGPException("Error. Debes ingresar una fecha de aplicación");
            }
            
            if(this.fechaVigencia == null)
            {
                throw new SGPException("Error. Debes ingresar una fecha de vigencia");
            }
            
            BigDecimal vMensual = this.calcularValorMensual(this.umaSelected.getImporteDiario());
            this.umaSelected.setImporteMensual(vMensual);
            BigDecimal vAnual = this.calcularValorAnual(this.umaSelected.getImporteMensual());
            this.umaSelected.setImporteAnual(vAnual);

            this.umaSelected.setFechaPublicacion(DateUtil.toLocalDate(fechaPublicacion));
            this.umaSelected.setFechaAplicacion(DateUtil.toLocalDate(fechaAplicacion));
            this.umaSelected.setFechaVigencia(DateUtil.toLocalDate(fechaVigencia));

            this.umaDAO.guardar(umaSelected);
            log.info("La UMA se guardo correctamente");
            mensaje = "Se guardo correctamente";
            severity = FacesMessage.SEVERITY_INFO;
            PrimeFaces.current().ajax().update("form:dtUma");
        }catch (SGPException ex) 
        {
            titulo = "Error";
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
            log.warn("Error al guardar la uma {}", ex.getMessage());
        }catch (Exception e) 
        {
            titulo = "Error";
            mensaje = "Consulte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("ERROR, {}", e.getMessage());
        }finally
        {
            actualizarListaUmas();
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
            PrimeFaces.current().executeScript("PF('manageUMADialog').hide()");
        }
    }
    
    public void actualizarUMA()
    {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "UMA Agregada";
        try 
        {
            if(this.umaSelected.getAnio() == null)
            {
                throw new SGPException("Error. Debes ingresar un año");
            }
            
            if(this.umaSelected.getImporteDiario() == null)
            {
                throw new SGPException("Error. Debes ingresar un importe");
            }
            
            if(this.fechaPublicacion == null)
            {
                throw new SGPException("Error. Debes ingresar una fecha de publicacion");
            }
            
            if(this.fechaAplicacion == null)
            {
                throw new SGPException("Error. Debes ingresar una fecha de aplicación");
            }
            
            if(this.fechaVigencia == null)
            {
                throw new SGPException("Error. Debes ingresar una fecha de vigencia");
            }
            
            BigDecimal vMensual = this.calcularValorMensual(this.umaSelected.getImporteDiario());
            this.umaSelected.setImporteMensual(vMensual);
            BigDecimal vAnual = this.calcularValorAnual(this.umaSelected.getImporteMensual());
            this.umaSelected.setImporteAnual(vAnual);

            this.umaSelected.setFechaPublicacion(DateUtil.toLocalDate(fechaPublicacion));
            this.umaSelected.setFechaAplicacion(DateUtil.toLocalDate(fechaAplicacion));
            this.umaSelected.setFechaVigencia(DateUtil.toLocalDate(fechaVigencia));

            this.umaDAO.actualizar(umaSelected);
            log.info("La UMA se actualizo correctamente");
            mensaje = "Se actualizo correctamente";
            severity = FacesMessage.SEVERITY_INFO;
            PrimeFaces.current().ajax().update("form:dtUma");
        }catch (SGPException ex) 
        {
            titulo = "Error";
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
            log.warn("Error al guardar la uma {}", ex.getMessage());
        }catch (Exception e) 
        {
            titulo = "Error";
            mensaje = "Consulte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("ERROR, {}", e.getMessage());
        }finally
        {
            actualizarListaUmas();
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
            PrimeFaces.current().executeScript("PF('manageUMADialog').hide()");
        }
    }
    
    public BigDecimal calcularValorMensual(BigDecimal valorDiario)
    {
        BigDecimal valorMensual = null;
        BigDecimal numVeces = new BigDecimal("30.4");
        
        valorMensual = valorDiario.multiply(numVeces).setScale(2, BigDecimal.ROUND_HALF_UP);
        
        return valorMensual;
    }
    
    public BigDecimal calcularValorAnual(BigDecimal valorMensual)
    {
        BigDecimal valorAnual = null;
        BigDecimal numVeces = new BigDecimal("12").setScale(2, BigDecimal.ROUND_HALF_UP);
        
        valorAnual = valorMensual.multiply(numVeces).setScale(2, BigDecimal.ROUND_HALF_UP);
        
        return valorAnual;
    }
    
    /*public void realizarAccionBoton(String componente, String value, String icon, String listener, String process, String update) 
    {
        FacesContext context = FacesContext.getCurrentInstance();// Obtener el componente por su ID
        CommandButton boton = (CommandButton) FacesContext.getCurrentInstance().getViewRoot().findComponent(componente); // Cambia al ID del formulario que contiene el botón
        ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
        MethodExpression methodExpresion = expressionFactory.createMethodExpression(context.getELContext(), listener, null, new Class<?>[]{BehaviorEvent.class});
        
        if (boton != null) 
        {
            // Modificar propiedades del botón
            AjaxBehavior ajaxBehavior = (AjaxBehavior) context.getApplication().createBehavior(AjaxBehavior.BEHAVIOR_ID);
            ajaxBehavior.addAjaxBehaviorListener(new AjaxBehaviorListenerImpl(methodExpresion, methodExpresion));
            
            boton.setValue(value);
            boton.setIcon(icon);
            boton.setProcess(process);
            boton.setUpdate(update);
            boton.addClientBehavior("actionListener", ajaxBehavior);
        }
    }*/
    
    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public CatUMA getUmaSelected() {
        return umaSelected;
    }

    public void setUmaSelected(CatUMA umaSelected) {
        this.umaSelected = umaSelected;
    }

    public List<CatUMA> getCatUMAS() {
        return catUMAS;
    }

    public void setCatUMAS(List<CatUMA> catUMAS) {
        this.catUMAS = catUMAS;
    }

    public List<CatUMA> getCatUMASeleccionada() {
        return catUMASeleccionada;
    }

    public void setCatUMASeleccionada(List<CatUMA> catUMASeleccionada) {
        this.catUMASeleccionada = catUMASeleccionada;
    }
    
    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Date getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(Date fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }
    
    public String getAccionBoton() {
        return accionBoton;
    }

    public void setAccionBoton(String accionBoton) {
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
