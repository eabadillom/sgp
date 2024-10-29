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

import mx.com.ferbo.dao.n.sat.TipoOtroPagoDAO;
import mx.com.ferbo.model.sat.CatTipoOtroPago;
import mx.com.ferbo.util.SGPException;

@Named(value = "tipoOtroPagoBean")
@ViewScoped
public class TipoOtroPagoBean implements Serializable {

    private static final long serialVersionUID = -3103728579268351267L;
    private static Logger log = LogManager.getLogger(TipoOtroPagoBean.class);
    
    private TipoOtroPagoDAO tipoOtroPagoDAO;
    
    private CatTipoOtroPago tipoOtroPago;
    private List<CatTipoOtroPago> listTipoOtroPago;

    private boolean guardar;
    private boolean primaryEdit;


    public TipoOtroPagoBean(){
        
        listTipoOtroPago = new ArrayList<>();
        tipoOtroPagoDAO = new TipoOtroPagoDAO();

    }

    @PostConstruct
    public void init(){

        listTipoOtroPago = tipoOtroPagoDAO.buscarTodos();

    }

    public void nuevo(){

        tipoOtroPago = new CatTipoOtroPago();
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

            if (guardar) {
                
                tipoOtroPagoDAO.guardar(tipoOtroPago);
                
                mensaje = "Éxito al guardar registro";
                severity = FacesMessage.SEVERITY_INFO;                

            }else{

                tipoOtroPagoDAO.actualizar(tipoOtroPago);

                titulo = "Registro actualizado";
                mensaje = "Éxito al guardar registro";
                severity = FacesMessage.SEVERITY_INFO;

            }

            listTipoOtroPago = tipoOtroPagoDAO.buscarTodos();

            PrimeFaces.current().executeScript("PF('dlgTipoOtroPa').hide();");
            
        }catch(SGPException ex){

            titulo = "Error";
            mensaje = "Conuslta al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;

            log.error("Error" + ex.getMessage());

        }catch (Exception e) {
            
            titulo = "Error";
            mensaje = "Conuslta al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;

            log.error("Error" + e.getMessage());

        }finally{

            messages = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, messages);
            PrimeFaces.current().ajax().update("form:messages","form:dt-tipoOtroPa");

        }

    }

    public CatTipoOtroPago getTipoOtroPago() {
        return tipoOtroPago;
    }

    public void setTipoOtroPago(CatTipoOtroPago tipoOtroPago) {
        this.tipoOtroPago = tipoOtroPago;
    }

    public List<CatTipoOtroPago> getListTipoOtroPago() {
        return listTipoOtroPago;
    }

    public void setListTipoOtroPago(List<CatTipoOtroPago> listTipoOtroPago) {
        this.listTipoOtroPago = listTipoOtroPago;
    }

    public boolean isPrimaryEdit() {
        return primaryEdit;
    }

    public void setPrimaryEdit(boolean primaryEdit) {
        this.primaryEdit = primaryEdit;
    }
    
}
