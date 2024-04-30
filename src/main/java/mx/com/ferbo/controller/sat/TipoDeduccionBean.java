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

import mx.com.ferbo.dao.sat.CatTipoDeduccionDAO;
import mx.com.ferbo.dto.sat.TipoDeduccionDTO;
import mx.com.ferbo.util.SGPException;

@Named(value = "tipoDeduccionBean")
@ViewScoped
public class TipoDeduccionBean implements Serializable{

    private static final long serialVersionUID = -3103728579268351267L;
    private static Logger log = LogManager.getLogger(TipoDeduccionBean.class);

    private TipoDeduccionDTO tipoDeduccionDTO;
    private List<TipoDeduccionDTO> listTipoDeduccion;

    private CatTipoDeduccionDAO catTipoDeduccionDAO;

    private boolean guardar;
    

    public TipoDeduccionBean(){

        catTipoDeduccionDAO = new CatTipoDeduccionDAO();
        listTipoDeduccion = new ArrayList<>();

    }

    @PostConstruct
    public void init(){

        listTipoDeduccion = catTipoDeduccionDAO.buscarTodos();

    }

    public void nuevo(){
        tipoDeduccionDTO = new TipoDeduccionDTO();
        guardar = true;
    }

    public void editar(){
        guardar = false;
    }

    public void guardar(){

        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Registro Guardado";

        try {

            if(guardar){

                catTipoDeduccionDAO.guardar(tipoDeduccionDTO);

                mensaje = "Se guardo con éxito";
                severity = FacesMessage.SEVERITY_INFO;

            }else{

                catTipoDeduccionDAO.actualizar(tipoDeduccionDTO);

                titulo = "Registro Actualizar";
                mensaje = "Se actualizo con éxito";
                severity = FacesMessage.SEVERITY_INFO;
                
            }
            
            listTipoDeduccion = catTipoDeduccionDAO.buscarTodos();

        } catch (SGPException e) {            

            titulo = "Error en registro";
            mensaje = e.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;

            log.error(e.getMessage());

        }finally{
            
            message = new FacesMessage(severity,titulo,mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);

            PrimeFaces.current().ajax().update("form:messages","form:dt-tipoDeduccion");

        }

    }


    public TipoDeduccionDTO getTipoDeduccionDTO() {
        return tipoDeduccionDTO;
    }

    public void setTipoDeduccionDTO(TipoDeduccionDTO tipoDeduccionDTO) {
        this.tipoDeduccionDTO = tipoDeduccionDTO;
    }

    public List<TipoDeduccionDTO> getListTipoDeduccion() {
        return listTipoDeduccion;
    }

    public void setListTipoDeduccion(List<TipoDeduccionDTO> listTipoDeduccion) {
        this.listTipoDeduccion = listTipoDeduccion;
    }



    
}
