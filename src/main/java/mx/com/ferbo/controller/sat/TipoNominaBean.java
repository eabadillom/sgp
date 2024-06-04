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

import mx.com.ferbo.dao.sat.CatTipoNominaDAO;
import mx.com.ferbo.dto.sat.TipoNominaDTO;
import mx.com.ferbo.util.SGPException;

@Named(value = "tipoNominaBean")
@ViewScoped
public class TipoNominaBean implements Serializable{

    private static final long serialVersionUID = -3103728579268351267L;
	private static Logger log = LogManager.getLogger(TipoNominaBean.class);

    private TipoNominaDTO tipoNominaDTO;
    private List<TipoNominaDTO> listDTO;

    private CatTipoNominaDAO catTipoNominaDAO;

    private boolean guardar;
    private boolean primaryEdit;
    
    public TipoNominaBean(){

        listDTO = new ArrayList<>();
        catTipoNominaDAO = new CatTipoNominaDAO();

    }

    @PostConstruct
    public void init(){
        listDTO = catTipoNominaDAO.buscarTodos();
    }

    public void nuevo(){
        tipoNominaDTO = new TipoNominaDTO();
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

                catTipoNominaDAO.guardar(tipoNominaDTO);

                mensaje = "Exito al guardar registro";
                severity = FacesMessage.SEVERITY_INFO;

            }else{

                catTipoNominaDAO.actualizar(tipoNominaDTO);

                mensaje = "Exito al actualizar registro";
                severity = FacesMessage.SEVERITY_INFO;
                titulo = "Registro actualizado";

            }

            listDTO = catTipoNominaDAO.buscarTodos();

            PrimeFaces.current().executeScript("PF('dlgTipoNomina').hide();");

        } catch (SGPException ex) {

            titulo = "Error";
            mensaje = "Consulta al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;

            log.error("Error: ", ex.getMessage());
        }catch(Exception e){

            titulo = "Error";
            mensaje = "Consulta al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;

            log.error("Error: ", e.getMessage());

        }finally{
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages","form:dt-tipoNomina");
        }


    }

    public TipoNominaDTO getTipoNominaDTO() {
        return tipoNominaDTO;
    }

    public void setTipoNominaDTO(TipoNominaDTO tipoNominaDTO) {
        this.tipoNominaDTO = tipoNominaDTO;
    }

    public List<TipoNominaDTO> getListDTO() {
        return listDTO;
    }

    public void setListDTO(List<TipoNominaDTO> listDTO) {
        this.listDTO = listDTO;
    }

    public boolean isPrimaryEdit() {
        return primaryEdit;
    }

    public void setPrimaryEdit(boolean primaryEdit) {
        this.primaryEdit = primaryEdit;
    }

    


}
