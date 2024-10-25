package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.com.ferbo.dao.n.PrendaDAO;
import mx.com.ferbo.model.CatPrenda;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.primefaces.PrimeFaces;

@Named(value = "prendasBean")
@ViewScoped
public class PrendasBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(PrendasBean.class);

    private CatPrenda prenda;
    private PrendaDAO prendadao;
    private List<CatPrenda> prendas;
    private FacesContext fc;
    private PrimeFaces pf;

    private String accion;

    public PrendasBean() {
        this.prendadao = new PrendaDAO();
    }
    
    @PostConstruct
    public void init(){
        fc = FacesContext.getCurrentInstance();
        pf = PrimeFaces.current();
    }

    public List<CatPrenda> getPrendas() {
        return prendas;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }
    
    public void nuevaPrenda(){
        this.prenda = new CatPrenda();
        this.setAccion("Registrar");
    }
    
    public void pasarPrenda(CatPrenda prendatmp){
        try{
            this.prenda = prendatmp;
            this.setAccion("Modificar");
        }
        catch(Exception ex){
            log.debug("Problema en asignar la prenda...", ex);
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
                    this.prendas = this.prendadao.buscarTodos();
                }
            }
            else{
                this.prendas = this.prendadao.buscarTodos();
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
            this.prendadao.guardar(prenda);
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
            this.prendadao.actualizar(prenda);
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
            this.prendadao.eliminar(prenda);
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
