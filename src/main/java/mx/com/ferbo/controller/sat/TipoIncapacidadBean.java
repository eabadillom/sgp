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

import mx.com.ferbo.dao.sat.CatTipoIncapacidadDAO;
import mx.com.ferbo.dto.sat.TipoIncapacidadDTO;
import mx.com.ferbo.util.SGPException;

@Named(value = "tipoIncapacidadBean")
@ViewScoped
public class TipoIncapacidadBean implements Serializable {

    private static final long serialVersionUID = -3103728579268351267L;
	private static Logger log = LogManager.getLogger(TipoIncapacidadBean.class);
    
    private TipoIncapacidadDTO tipoIncapacidadDTO;
    private List<TipoIncapacidadDTO> listDTO;

    private CatTipoIncapacidadDAO catTipoIncapacidadDAO;

    private boolean guardar;
    private boolean primaryEdit;

    public TipoIncapacidadBean(){

        listDTO = new ArrayList<>();
        catTipoIncapacidadDAO = new CatTipoIncapacidadDAO();

    }

    @PostConstruct
    public void init(){
        listDTO = catTipoIncapacidadDAO.buscarTodos();
    }

    public void nuevo(){
        tipoIncapacidadDTO = new TipoIncapacidadDTO();
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
        String titulo = "Registro guardar";

        try {

            if(guardar){

                catTipoIncapacidadDAO.guardar(tipoIncapacidadDTO);

                mensaje = "Exito al guardar el registro";
                severity = FacesMessage.SEVERITY_INFO;

            }else{
                
                catTipoIncapacidadDAO.actualizar(tipoIncapacidadDTO);

                titulo = "Registro actualizado";
                mensaje = "Exito al actualizar el registro";
                severity = FacesMessage.SEVERITY_INFO;

            }

            listDTO = catTipoIncapacidadDAO.buscarTodos();

            PrimeFaces.current().executeScript("PF('dlgTipoInca').hide();");

        }catch(SGPException ex){

            titulo = "Error";
            mensaje = "Consulta al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;

            log.info("ERROR", ex.getMessage());
        }catch (Exception e) {
            
            titulo = "Error";
            mensaje = "Consulta al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;

            log.info("ERROR", e.getMessage());

        }finally{

            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages","form:dt-tipoIncapacidad");

        }

    }

    public TipoIncapacidadDTO getTipoIncapacidadDTO() {
        return tipoIncapacidadDTO;
    }

    public void setTipoIncapacidadDTO(TipoIncapacidadDTO tipoIncapacidadDTO) {
        this.tipoIncapacidadDTO = tipoIncapacidadDTO;
    }

    public List<TipoIncapacidadDTO> getListDTO() {
        return listDTO;
    }

    public void setListDTO(List<TipoIncapacidadDTO> listDTO) {
        this.listDTO = listDTO;
    }

    public boolean isPrimaryEdit() {
        return primaryEdit;
    }

    public void setPrimaryEdit(boolean primaryEdit) {
        this.primaryEdit = primaryEdit;
    }



}
