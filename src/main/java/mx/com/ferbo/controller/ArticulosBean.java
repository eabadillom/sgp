
package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.com.ferbo.dao.n.ArticuloDAO;
import mx.com.ferbo.model.CatArticulo;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.primefaces.PrimeFaces;
import org.primefaces.PrimeFaces.Dialog;

@Named(value = "articulosBean")
@ViewScoped
public class ArticulosBean implements Serializable{
   
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(AltaSistemaBean.class);
    
    private List<CatArticulo> articulos;
    private CatArticulo articulo;
    private ArticuloDAO articulodao;
    private FacesContext fc;
    private PrimeFaces pf;
    
    private String accion;

    public ArticulosBean() {
        this.articulodao = new ArticuloDAO();
    }
    
    public void init(){
        fc = FacesContext.getCurrentInstance();
        pf = PrimeFaces.current();
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public void NuevoArticulo(){
        this.articulo = new CatArticulo();
        this.accion = "Registrar";
    }
    
    public void pasarArticulo(CatArticulo articulotmp){
        try{
            this.articulo = articulotmp;
            this.accion = "Modificar";
        }
        catch(Exception ex){
            log.debug("Problema en asignar el articulo...", ex);
        }
    }
    
    public void listar(boolean bandera){
        try{
           if(!bandera){
               if(this.isPostBack() == false){
                   this.articulos = this.articulodao.buscarTodos();
               }
           }else{
               this.articulos = this.articulodao.buscarTodos();
           }
        }
        catch(Exception ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: no se cargaron los elementos", null));
            pf.ajax().update("message");
            log.debug(ex);            
        }   
    }
    
    public boolean isPostBack(){
        boolean respuesta = false;
        
        respuesta = FacesContext.getCurrentInstance().isPostback();
        
        return respuesta;
                
    }
    
    public void registrar(){
        try{
            this.articulodao.guardar(articulo);
            this.listar(true);
        }
        catch(SGPException ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
        }
        catch(Exception ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
        }
    }
    
    public void actualizar(){
        try{
            this.articulodao.actualizar(articulo);
            this.listar(true);
        }
        catch(SGPException ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
        }
        catch(Exception ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
        }
    }
    
    public void eliminar(){
        try{
            this.articulodao.eliminar(articulo);
            this.listar(true);
        }
        catch(SGPException ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
        }
        catch(Exception ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
        }
    }
    
     public void operar() {
        switch (accion) {
            case "Registrar":
                this.registrar();
                break;
            case "Modificar":
                this.actualizar();
                break;
        }
    }
}
