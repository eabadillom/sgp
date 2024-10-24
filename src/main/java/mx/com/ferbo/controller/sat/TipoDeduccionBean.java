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

import mx.com.ferbo.dao.n.sat.TipoDeduccionDAO;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.util.SGPException;

@Named(value = "tipoDeduccionBean")
@ViewScoped
public class TipoDeduccionBean implements Serializable{

    private static final long serialVersionUID = -3103728579268351267L;
    private static Logger log = LogManager.getLogger(TipoDeduccionBean.class);

    private CatTipoDeduccion tipoDeduccion;
    private List<CatTipoDeduccion> listTipoDeduccion;

    private TipoDeduccionDAO catTipoDeduccionDAO;

    private boolean guardar;
    private boolean primaryEdit;

    public TipoDeduccionBean(){
        catTipoDeduccionDAO = new TipoDeduccionDAO();
        listTipoDeduccion = new ArrayList<>();
    }

    @PostConstruct
    public void init(){

        listTipoDeduccion = catTipoDeduccionDAO.buscarTodos();

    }

    public void nuevo(){
        tipoDeduccion = new CatTipoDeduccion();
        guardar = true;
        primaryEdit = false;
    }

    public void editar(){
        guardar = false;
        primaryEdit = true;
    }

    public void guardar(){

        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Registro Guardado";

        try {
            if(guardar){
                catTipoDeduccionDAO.guardar(tipoDeduccion);
                mensaje = "Se guardo con éxito";
                severity = FacesMessage.SEVERITY_INFO;
            }else{
                catTipoDeduccionDAO.actualizar(tipoDeduccion);
                titulo = "Registro Actualizar";
                mensaje = "Se actualizo con éxito";
                severity = FacesMessage.SEVERITY_INFO;   
            }
            listTipoDeduccion = catTipoDeduccionDAO.buscarTodos();
            PrimeFaces.current().executeScript("PF('dlgTipoDeduccion').hide();");
        } catch (SGPException e) {            
            titulo = "Error";
            mensaje = "Error en el registro consulta al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
            log.error(e.getMessage());
        }catch(Exception ex){
            titulo = "Error";
            mensaje = "Error en el registro consulta al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
            log.error(ex.getMessage());
        }finally{            
            message = new FacesMessage(severity,titulo,mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages","form:dt-tipoDeduccion");
        }
    }

    public CatTipoDeduccion getTipoDeduccion() {
        return tipoDeduccion;
    }

    public void setTipoDeduccionDTO(CatTipoDeduccion tipoDeduccion) {
        this.tipoDeduccion = tipoDeduccion;
    }

    public List<CatTipoDeduccion> getListTipoDeduccion() {
        return listTipoDeduccion;
    }

    public void setListTipoDeduccion(List<CatTipoDeduccion> listTipoDeduccion) {
        this.listTipoDeduccion = listTipoDeduccion;
    }

    public boolean isPrimaryEdit() {
        return primaryEdit;
    }

    public void setPrimaryEdit(boolean primaryEdit) {
        this.primaryEdit = primaryEdit;
    }
    
}
