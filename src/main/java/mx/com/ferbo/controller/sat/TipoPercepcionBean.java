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

import mx.com.ferbo.dao.sat.CatTipoPercepcionDAO;
import mx.com.ferbo.dto.sat.TipoPercepcionDTO;
import mx.com.ferbo.util.SGPException;

@Named(value = "tipoPercepcionBean")
@ViewScoped
public class TipoPercepcionBean implements Serializable{

    private static final long  serialVersionUID = -3103728579268351267L;
    private static Logger log = LogManager.getLogger(TipoPercepcionBean.class);
    
    private TipoPercepcionDTO tipoPercepcionDTO;
    private List<TipoPercepcionDTO> listDto;

    private CatTipoPercepcionDAO catTipoPercepcionDAO;

    private boolean guardar;
    private boolean primaryEdit;

    public TipoPercepcionBean(){
        listDto = new ArrayList<>();
        catTipoPercepcionDAO = new CatTipoPercepcionDAO();
    }

    @PostConstruct
    public void init(){
        listDto = catTipoPercepcionDAO.buscarTodos();
    }

    public void nuevo(){
        tipoPercepcionDTO = new TipoPercepcionDTO();
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
                catTipoPercepcionDAO.guardar(tipoPercepcionDTO);   
                
                mensaje = "Éxito al guardar registro";
                severity = FacesMessage.SEVERITY_INFO;
                
            }else{

                catTipoPercepcionDAO.actualizar(tipoPercepcionDTO);

                mensaje = "Éxito al guardar registro";
                severity = FacesMessage.SEVERITY_INFO;
                titulo = "Registro actualizado";

            }

            listDto = catTipoPercepcionDAO.buscarTodos();

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

    public TipoPercepcionDTO getTipoPercepcionDTO() {
        return tipoPercepcionDTO;
    }

    public void setTipoPercepcionDTO(TipoPercepcionDTO tipoPercepcionDTO) {
        this.tipoPercepcionDTO = tipoPercepcionDTO;
    }

    public List<TipoPercepcionDTO> getListDto() {
        return listDto;
    }

    public void setListDto(List<TipoPercepcionDTO> listDto) {
        this.listDto = listDto;
    }

    public boolean isPrimaryEdit() {
        return primaryEdit;
    }

    public void setPrimaryEdit(boolean primaryEdit) {
        this.primaryEdit = primaryEdit;
    }

    


}
