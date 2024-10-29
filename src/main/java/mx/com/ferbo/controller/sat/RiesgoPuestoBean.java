package mx.com.ferbo.controller.sat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.n.sat.RiesgoPuestoDAO;
import mx.com.ferbo.model.sat.CatRiesgoPuesto;
import mx.com.ferbo.util.SGPException;

@Named(value = "riesgoPuestoBean")
@ViewScoped
public class RiesgoPuestoBean implements Serializable {
    
    private static final long serialVersionUID = -3103728579268351267L;
    private static Logger log = LogManager.getLogger(RiesgoPuestoBean.class);

    private CatRiesgoPuesto riesgoPuesto;
    private List<CatRiesgoPuesto> listRiesgoPuesto;

    private RiesgoPuestoDAO catRiesgoPuestoDAO;

    private boolean guardar;
    private boolean primaryEdit;

    public RiesgoPuestoBean(){
        catRiesgoPuestoDAO = new RiesgoPuestoDAO();
        listRiesgoPuesto = new ArrayList<>();
    }

    @PostConstruct
    public void init(){
        listRiesgoPuesto = catRiesgoPuestoDAO.buscarTodos();
    }

    public void nuevo(){
        riesgoPuesto = new CatRiesgoPuesto();
        guardar = true;
        primaryEdit = false;
    }

    public void editar(){
        guardar = false;
        primaryEdit = true;
    }

    public void guardar(){

        FacesMessage messages = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Registro guardado";
        
        try {
            if(guardar){
                catRiesgoPuestoDAO.guardar(riesgoPuesto);
                mensaje = "Éxito al guardar el registro";
                severity = FacesMessage.SEVERITY_INFO;

            }else{
                catRiesgoPuestoDAO.actualizar(riesgoPuesto);
                mensaje = "Éxito al actualizar registro";
                titulo = "REgistro actualizado";
                severity = FacesMessage.SEVERITY_INFO;
            }
            listRiesgoPuesto = catRiesgoPuestoDAO.buscarTodos();
            PrimeFaces.current().executeScript("PF('dlgRiesgoPuesto').hide();");
        }catch(SGPException ex){
            titulo = "Error";
            mensaje = "Consulta al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
            log.error("Error" + ex.getMessage());
        }catch (Exception e) {
            titulo = "Error";
            mensaje = "Consulta al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
            log.error("ERROR" + e.getMessage());
        }finally{
            messages = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, messages);
            PrimeFaces.current().ajax().update("form:messages","form:dt-riesgoPuesto");
        }

    }

    public CatRiesgoPuesto getRiesgoPuesto() {
        return riesgoPuesto;
    }

    public void setRiesgoPuesto(CatRiesgoPuesto riesgoPuesto) {
        this.riesgoPuesto = riesgoPuesto;
    }

    public List<CatRiesgoPuesto> getList() {
        return listRiesgoPuesto;
    }

    public void setListDTO(List<CatRiesgoPuesto> listRiesgo) {
        this.listRiesgoPuesto = listRiesgo;
    }

    public boolean isPrimaryEdit() {
        return primaryEdit;
    }

    public void setPrimaryEdit(boolean primaryEdit) {
        this.primaryEdit = primaryEdit;
    }

    

}
