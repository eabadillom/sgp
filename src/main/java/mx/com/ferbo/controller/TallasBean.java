
package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.com.ferbo.dao.n.TallaDAO;
import mx.com.ferbo.model.CatTalla;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.primefaces.PrimeFaces;

@Named (value = "tallasBean")
@ViewScoped
public class TallasBean implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(TallasBean.class);
    
    private CatTalla talla;
    private List<CatTalla> tallas;
    private TallaDAO talladao;
    private FacesContext fc;
    private PrimeFaces pf;

    private String accion;

    public TallasBean() {
        this.talladao = new TallaDAO();
    }
    
    @PostConstruct
    public void init(){
        fc = FacesContext.getCurrentInstance();
        pf = PrimeFaces.current();
    }

    public List<CatTalla> getTallas() {
        return tallas;
    }

    private String getAccion() {
        return accion;
    }

    private void setAccion(String accion) {
        this.accion = accion;
    }
    
    public void nuevaTalla(){
        this.talla = new CatTalla();
        this.setAccion("Registrar");
    }
    
    public void pasarTalla(CatTalla tallatmp){
        try{
            this.talla = tallatmp;
            this.setAccion("Modificar");
        }
        catch(Exception ex){
            log.debug("Problema en asignar la talla...", ex);
        }
    }
    
    private boolean isPostBack(){
        boolean respuesta = false;
        
        respuesta = fc.isPostback();
        
        return respuesta;
    }
    
        public void listar(boolean bandera){
        try{
            if(!bandera){
                if(this.isPostBack() == false){
                    this.tallas = this.talladao.buscarTodos();
                }
            }
            else{
                this.tallas = this.talladao.buscarTodos();
            }
        }
        catch(Exception ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: no se cargaron los elementos", null));
            pf.ajax().update("message");
            log.debug(ex);
        }
    }
    
    public void registrar(){
        try{
            this.talladao.guardar(talla);
            this.listar(true);
        }
        catch(SGPException ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
            log.debug(ex);
        }
        catch(Exception ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
            log.debug(ex);
        }
    }
    
    public void actualizar(){
        try{
            this.talladao.actualizar(talla);
            this.listar(true);
        }
        catch(SGPException ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
            log.debug(ex);
        }
        catch(Exception ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
            log.debug(ex);
        }
    }
    
    public void eliminar(){
        try{
            this.talladao.eliminar(talla);
            this.listar(true);
        }
        catch(SGPException ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
            log.debug(ex);
        }
        catch(Exception ex){
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            pf.ajax().update("message");
            log.debug(ex);
        }
    }
    
    public void operar() {
        switch (this.getAccion()) {
            case "Registrar":
                this.registrar();
                break;
            case "Modificar":
                this.actualizar();
                break;
        }
    }
}
