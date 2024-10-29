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

import mx.com.ferbo.dao.n.sat.OrigenRecursosDAO;
import mx.com.ferbo.model.sat.CatOrigRecurso;
import mx.com.ferbo.util.SGPException;

@Named(value = "origRecursoBean")
@ViewScoped
public class OrigRecursoBean implements Serializable{
    
    private static final long serialVersionUID = -3103728579268351267L;
    private CatOrigRecurso origRecurso;
    private List<CatOrigRecurso> listOrigRecursos;

    private OrigenRecursosDAO origRecursoDAO;

    boolean guardar;
    boolean primaryEdit;

    public OrigRecursoBean(){
        origRecursoDAO = new OrigenRecursosDAO();
        listOrigRecursos = new ArrayList<>();
    }

    @PostConstruct
    public void init(){
        listOrigRecursos = origRecursoDAO.buscarTodos();
    }

    public void nuevo(){
        origRecurso = new CatOrigRecurso();
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
                origRecursoDAO.guardar(origRecurso);
                mensaje = "Exito al guardar";
                severity = FacesMessage.SEVERITY_INFO;                
            }else{
                origRecursoDAO.actualizar(origRecurso);
                titulo = "Registro Editado";
                mensaje = "Exito al editar";
                severity = FacesMessage.SEVERITY_WARN;
            }
            listOrigRecursos = origRecursoDAO.buscarTodos();
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

    public CatOrigRecurso getOrigRecurso() {
        return origRecurso;
    }

    public void setOrigRecurso(CatOrigRecurso origRecursoDTO) {
        this.origRecurso = origRecursoDTO;
    }

    public List<CatOrigRecurso> getListOrigRecursos() {
        return listOrigRecursos;
    }

    public void setListOrigRecursos(List<CatOrigRecurso> listOrigRecursos) {
        this.listOrigRecursos = listOrigRecursos;
    }

    public boolean isPrimaryEdit() {
        return primaryEdit;
    }

    public void setPrimaryEdit(boolean primaryEdit) {
        this.primaryEdit = primaryEdit;
    }

}
