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

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.sat.CatOrigRecursoDAO;
import mx.com.ferbo.dto.sat.OrigRecursoDTO;
import mx.com.ferbo.util.SGPException;

@Named(value = "origRecursoBean")
@ViewScoped
public class OrigRecursoBean implements Serializable{
    
    private static final long serialVersionUID = -3103728579268351267L;
    private OrigRecursoDTO origRecursoDTO;
    private List<OrigRecursoDTO> listOrigRecursos;

    CatOrigRecursoDAO catOrigRecursoDAO;

    boolean guardar;
    boolean primaryEdit;

    public OrigRecursoBean(){
        catOrigRecursoDAO = new CatOrigRecursoDAO();
        listOrigRecursos = new ArrayList<>();
    }

    @PostConstruct
    public void init(){
        listOrigRecursos = catOrigRecursoDAO.buscarTodos();
    }

    public void nuevo(){
        origRecursoDTO = new OrigRecursoDTO();
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
            
            if (guardar) {

                catOrigRecursoDAO.guardar(origRecursoDTO);

                mensaje = "Exito al guardar";
                severity = FacesMessage.SEVERITY_INFO;
                
            }else{

                catOrigRecursoDAO.actualizar(origRecursoDTO);

                titulo = "Registro Editado";
                mensaje = "Exito al editar";
                severity = FacesMessage.SEVERITY_WARN;
            }

            listOrigRecursos = catOrigRecursoDAO.buscarTodos();
            PrimeFaces.current().executeScript("PF('dlgOrigenRecursos').hide();");

        }catch(SGPException e ) {
            titulo = "Error";
            mensaje = "Error en registro consulta al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;            
        }catch (Exception ex) {
            titulo = "Error";
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;            
        }finally{

            message = new FacesMessage(severity, mensaje, titulo);
            FacesContext.getCurrentInstance().addMessage(null, message);            
            PrimeFaces.current().ajax().update("form:messages","form:dt-origRecurso");
        }


    }

    public OrigRecursoDTO getOrigRecursoDTO() {
        return origRecursoDTO;
    }

    public void setOrigRecursoDTO(OrigRecursoDTO origRecursoDTO) {
        this.origRecursoDTO = origRecursoDTO;
    }

    public List<OrigRecursoDTO> getListOrigRecursos() {
        return listOrigRecursos;
    }

    public void setListOrigRecursos(List<OrigRecursoDTO> listOrigRecursos) {
        this.listOrigRecursos = listOrigRecursos;
    }

    public boolean isPrimaryEdit() {
        return primaryEdit;
    }

    public void setPrimaryEdit(boolean primaryEdit) {
        this.primaryEdit = primaryEdit;
    }


    

    

}
