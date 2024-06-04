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

import mx.com.ferbo.dao.sat.CatTipoHorasDAO;
import mx.com.ferbo.dto.sat.TipoHorasDTO;
import mx.com.ferbo.util.SGPException;

@Named(value = "tipoHorasBean")
@ViewScoped
public class TipoHorasBean implements Serializable{

    private static final long serialVersionUID = -3103728579268351267L;
    private static Logger log = LogManager.getLogger(TipoHorasBean.class);

    private TipoHorasDTO tipoHorasDTO;
    private List<TipoHorasDTO> listTipoHoras;

    private CatTipoHorasDAO catTipoHorasDAO;

    private boolean guardar;
    private boolean primaryEdit;

    public TipoHorasBean(){

        catTipoHorasDAO = new CatTipoHorasDAO();
        listTipoHoras = new ArrayList<>();

    }

    @PostConstruct
    public void init(){

        listTipoHoras = catTipoHorasDAO.buscarTodos();

    }

    public void nuevo(){
        tipoHorasDTO = new TipoHorasDTO();        
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

                catTipoHorasDAO.guardar(tipoHorasDTO);

                mensaje = "Se guardo con éxito";
                severity = FacesMessage.SEVERITY_INFO;

            }else{

                catTipoHorasDAO.actualizar(tipoHorasDTO);

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


    public TipoHorasDTO getTipoHorasDTO() {
        return tipoHorasDTO;
    }

    public void setTipoHorasDTO(TipoHorasDTO tipoHorasDTO) {
        this.tipoHorasDTO = tipoHorasDTO;
    }

    public List<TipoHorasDTO> getListTipoHoras() {
        return listTipoHoras;
    }

    public void setListTipoHoras(List<TipoHorasDTO> listTipoHoras) {
        this.listTipoHoras = listTipoHoras;
    }

    public boolean isPrimaryEdit() {
        return primaryEdit;
    }

    public void setPrimaryEdit(boolean primaryEdit) {
        this.primaryEdit = primaryEdit;
    }

    
    
}
