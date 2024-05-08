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

import mx.com.ferbo.dao.sat.CatTipoOtroPagoDAO;
import mx.com.ferbo.dto.sat.TipoOtroPagoDTO;
import mx.com.ferbo.util.SGPException;

@Named(value = "tipoOtroPagoBean")
@ViewScoped
public class TipoOtroPagoBean implements Serializable {

    private static final long serialVersionUID = -3103728579268351267L;
    private static Logger log = LogManager.getLogger(TipoOtroPagoBean.class);
    
    private CatTipoOtroPagoDAO catTipoOtroPagoDAO;
    
    private TipoOtroPagoDTO tipoOtroPagoDTO;
    private List<TipoOtroPagoDTO> listDTO;

    private boolean guardar;
    private boolean primaryEdit;


    public TipoOtroPagoBean(){
        
        listDTO = new ArrayList<>();
        catTipoOtroPagoDAO = new CatTipoOtroPagoDAO();

    }

    @PostConstruct
    public void init(){

        listDTO = catTipoOtroPagoDAO.buscarTodos();

    }

    public void nuevo(){

        tipoOtroPagoDTO = new TipoOtroPagoDTO();
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
                
                catTipoOtroPagoDAO.guardar(tipoOtroPagoDTO);
                
                mensaje = "Éxito al guardar registro";
                severity = FacesMessage.SEVERITY_INFO;                

            }else{

                catTipoOtroPagoDAO.actualizar(tipoOtroPagoDTO);

                titulo = "Registro actualizado";
                mensaje = "Éxito al guardar registro";
                severity = FacesMessage.SEVERITY_INFO;

            }

            listDTO = catTipoOtroPagoDAO.buscarTodos();

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

    public TipoOtroPagoDTO getTipoOtroPagoDTO() {
        return tipoOtroPagoDTO;
    }

    public void setTipoOtroPagoDTO(TipoOtroPagoDTO tipoOtroPagoDTO) {
        this.tipoOtroPagoDTO = tipoOtroPagoDTO;
    }

    public List<TipoOtroPagoDTO> getListDTO() {
        return listDTO;
    }

    public void setListDTO(List<TipoOtroPagoDTO> listDTO) {
        this.listDTO = listDTO;
    }

    public boolean isPrimaryEdit() {
        return primaryEdit;
    }

    public void setPrimaryEdit(boolean primaryEdit) {
        this.primaryEdit = primaryEdit;
    }

    

}
