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

import mx.com.ferbo.dao.sat.CatRiesgoPuestoDAO;
import mx.com.ferbo.dto.sat.RiesgoPuestoDTO;
import mx.com.ferbo.util.SGPException;

@Named(value = "riesgoPuestoBean")
@ViewScoped
public class RiesgoPuestoBean implements Serializable {
    
    private static final long serialVersionUID = -3103728579268351267L;
    private static Logger log = LogManager.getLogger(RiesgoPuestoBean.class);

    private RiesgoPuestoDTO riesgoPuestoDTO;
    private List<RiesgoPuestoDTO> listDTO;

    private CatRiesgoPuestoDAO catRiesgoPuestoDAO;

    private boolean guardar;
    private boolean primaryEdit;

    public RiesgoPuestoBean(){

        catRiesgoPuestoDAO = new CatRiesgoPuestoDAO();
        listDTO = new ArrayList<>();
    }

    @PostConstruct
    public void init(){
        listDTO = catRiesgoPuestoDAO.buscarTodos();
    }

    public void nuevo(){

        riesgoPuestoDTO = new RiesgoPuestoDTO();

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

                catRiesgoPuestoDAO.guardar(riesgoPuestoDTO);

                mensaje = "Éxito al guardar el registro";
                severity = FacesMessage.SEVERITY_INFO;

            }else{

                catRiesgoPuestoDAO.actualizar(riesgoPuestoDTO);

                mensaje = "Éxito al actualizar registro";
                titulo = "REgistro actualizado";
                severity = FacesMessage.SEVERITY_INFO;

            }

            listDTO = catRiesgoPuestoDAO.buscarTodos();

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

    public RiesgoPuestoDTO getRiesgoPuestoDTO() {
        return riesgoPuestoDTO;
    }

    public void setRiesgoPuestoDTO(RiesgoPuestoDTO riesgoPuestoDTO) {
        this.riesgoPuestoDTO = riesgoPuestoDTO;
    }

    public List<RiesgoPuestoDTO> getListDTO() {
        return listDTO;
    }

    public void setListDTO(List<RiesgoPuestoDTO> listDTO) {
        this.listDTO = listDTO;
    }

    public boolean isPrimaryEdit() {
        return primaryEdit;
    }

    public void setPrimaryEdit(boolean primaryEdit) {
        this.primaryEdit = primaryEdit;
    }

    

}
