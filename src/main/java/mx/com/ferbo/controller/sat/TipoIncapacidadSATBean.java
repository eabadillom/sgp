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

import mx.com.ferbo.dao.n.sat.TipoIncapacidadSATDAO;
import mx.com.ferbo.model.sat.CatTipoIncapacidadSAT;
import mx.com.ferbo.util.SGPException;

@Named(value = "tipoIncapacidadBean")
@ViewScoped
public class TipoIncapacidadSATBean implements Serializable {

    private static final long serialVersionUID = -3103728579268351267L;
	private static Logger log = LogManager.getLogger(TipoIncapacidadSATBean.class);
    
    private CatTipoIncapacidadSAT tipoIncapacidad;
    private List<CatTipoIncapacidadSAT> listTipoIncapacidad;

    private TipoIncapacidadSATDAO tipoIncapacidadDAO;

    private boolean guardar;
    private boolean primaryEdit;

    public TipoIncapacidadSATBean(){

        listTipoIncapacidad = new ArrayList<>();
        tipoIncapacidadDAO = new TipoIncapacidadSATDAO();

    }

    @PostConstruct
    public void init(){
        listTipoIncapacidad = tipoIncapacidadDAO.buscarTodos();
    }

    public void nuevo(){
        tipoIncapacidad = new CatTipoIncapacidadSAT();
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

                tipoIncapacidadDAO.guardar(tipoIncapacidad);

                mensaje = "Exito al guardar el registro";
                severity = FacesMessage.SEVERITY_INFO;

            }else{
                
                tipoIncapacidadDAO.actualizar(tipoIncapacidad);

                titulo = "Registro actualizado";
                mensaje = "Exito al actualizar el registro";
                severity = FacesMessage.SEVERITY_INFO;

            }

            listTipoIncapacidad = tipoIncapacidadDAO.buscarTodos();

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
            PrimeFaces.current().ajax().update("form:messages","form:dt-tipoInca");

        }

    }

    public CatTipoIncapacidadSAT getTipoIncapacidad() {
        return tipoIncapacidad;
    }

    public void setTipoIncapacidad(CatTipoIncapacidadSAT tipoIncapacidad) {
        this.tipoIncapacidad = tipoIncapacidad;
    }

    public List<CatTipoIncapacidadSAT> getListTipoIncapacidad() {
        return listTipoIncapacidad;
    }

    public void setLisTipoIncapacidad(List<CatTipoIncapacidadSAT> listTipoIncapacidad) {
        this.listTipoIncapacidad = listTipoIncapacidad;
    }

    public boolean isPrimaryEdit() {
        return primaryEdit;
    }

    public void setPrimaryEdit(boolean primaryEdit) {
        this.primaryEdit = primaryEdit;
    }
    
}
