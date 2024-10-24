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

import mx.com.ferbo.dao.n.sat.TipoPercepcionDAO;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import mx.com.ferbo.util.SGPException;

@Named(value = "tipoPercepcionBean")
@ViewScoped
public class TipoPercepcionBean implements Serializable{

    private static final long  serialVersionUID = -3103728579268351267L;
    private static Logger log = LogManager.getLogger(TipoPercepcionBean.class);
    
    private CatTipoPercepcion tipoPercepcion;
    private List<CatTipoPercepcion> listTipoPercepcion;

    private TipoPercepcionDAO tipoPercepcionDAO;

    private boolean guardar;
    private boolean primaryEdit;

    public TipoPercepcionBean(){
        listTipoPercepcion = new ArrayList<>();
        tipoPercepcionDAO = new TipoPercepcionDAO();
    }

    @PostConstruct
    public void init(){
        listTipoPercepcion = tipoPercepcionDAO.buscarTodos();
    }

    public void nuevo(){
        tipoPercepcion = new CatTipoPercepcion();
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
                tipoPercepcionDAO.guardar(tipoPercepcion);   
                
                mensaje = "Éxito al guardar registro";
                severity = FacesMessage.SEVERITY_INFO;
                
            }else{

                tipoPercepcionDAO.actualizar(tipoPercepcion);

                mensaje = "Éxito al guardar registro";
                severity = FacesMessage.SEVERITY_INFO;
                titulo = "Registro actualizado";

            }

            listTipoPercepcion = tipoPercepcionDAO.buscarTodos();

            PrimeFaces.current().executeScript("PF('dlgTipoPercepcion').hide();");  
            
        }catch(SGPException ex){

            titulo = "Error";
            mensaje = "Consulta al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;

            log.error("Error" + ex.getMessage());
        }catch (Exception e) {
            
            titulo = "Error";
            mensaje = "Consulta al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;

            log.error("Error" + e.getMessage());    
        }finally{
            
            messages = new FacesMessage(severity, titulo,mensaje);
            FacesContext.getCurrentInstance().addMessage(null, messages);
            PrimeFaces.current().ajax().update("form:messages","form:dt-tipoPercepcion");

        }

    }

    public CatTipoPercepcion getTipoPercepcion() {
        return tipoPercepcion;
    }

    public void setTipoPercepcion(CatTipoPercepcion tipoPercepcion) {
        this.tipoPercepcion = tipoPercepcion;
    }

    public List<CatTipoPercepcion> getListTipoPercepcion() {
        return listTipoPercepcion;
    }

    public void setListDto(List<CatTipoPercepcion> listTipoPercepcion) {
        this.listTipoPercepcion = listTipoPercepcion;
    }

    public boolean isPrimaryEdit() {
        return primaryEdit;
    }

    public void setPrimaryEdit(boolean primaryEdit) {
        this.primaryEdit = primaryEdit;
    }
    
}
