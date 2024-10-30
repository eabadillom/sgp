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

import mx.com.ferbo.dao.n.sat.TipoHorasDAO;
import mx.com.ferbo.model.sat.CatTipoHoras;
import mx.com.ferbo.util.SGPException;

@Named(value = "tipoHorasBean")
@ViewScoped
public class TipoHorasBean implements Serializable{

    private static final long serialVersionUID = -3103728579268351267L;
    private static Logger log = LogManager.getLogger(TipoHorasBean.class);

    private CatTipoHoras tipoHoras;
    private List<CatTipoHoras> listTipoHoras;

    private TipoHorasDAO catTipoHorasDAO;

    private boolean guardar;
    private boolean primaryEdit;

    public TipoHorasBean(){

        catTipoHorasDAO = new TipoHorasDAO();
        listTipoHoras = new ArrayList<>();

    }

    @PostConstruct
    public void init(){

        listTipoHoras = catTipoHorasDAO.buscarTodos();

    }

    public void nuevo(){
        tipoHoras = new CatTipoHoras();        
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
        String titulo = "Registro guardado";

        try {
            if(guardar){
                catTipoHorasDAO.guardar(tipoHoras);
                mensaje = "Se guardo con éxito";
                severity = FacesMessage.SEVERITY_INFO;
            }else{
                catTipoHorasDAO.actualizar(tipoHoras);
                titulo = "Registro editado";
                mensaje = "Se actualizo con éxito";
                severity = FacesMessage.SEVERITY_INFO;
            }
            listTipoHoras = catTipoHorasDAO.buscarTodos();
            PrimeFaces.current().executeScript("PF('dlgTipoHora').hide();");
        }catch(SGPException ex){
            titulo = "Error";
            mensaje = "Existe un error en el registro" + " consulta al administrador del sistema";
            severity = FacesMessage.SEVERITY_ERROR;
            log.error("Error " + ex.getMessage());
        }catch (Exception e) {
            titulo = "Error";
            mensaje = "Existe un error en el registro" + " consulta al administrador del sistema";
            severity = FacesMessage.SEVERITY_ERROR;
            log.error("Error en registro tipo Hora",e.getMessage());
        }finally{
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages","form:dt-tipoHora");
        }

    }


    public CatTipoHoras getTipoHoras() {
        return tipoHoras;
    }

    public void setTipoHorasDTO(CatTipoHoras tipoHoras) {
        this.tipoHoras = tipoHoras;
    }

    public List<CatTipoHoras> getListTipoHoras() {
        return listTipoHoras;
    }

    public void setListTipoHoras(List<CatTipoHoras> listTipoHoras) {
        this.listTipoHoras = listTipoHoras;
    }

    public boolean isPrimaryEdit() {
        return primaryEdit;
    }

    public void setPrimaryEdit(boolean primaryEdit) {
        this.primaryEdit = primaryEdit;
    }
    
}
