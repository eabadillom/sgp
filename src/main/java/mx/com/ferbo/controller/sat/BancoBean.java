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

import mx.com.ferbo.dao.n.sat.BancoDAO;
import mx.com.ferbo.model.sat.CatBanco;
import mx.com.ferbo.util.SGPException;

@Named(value = "bancoBean")
@ViewScoped
public class BancoBean implements Serializable {


	private static final long serialVersionUID = -3103728579268351267L;
	private static Logger log = LogManager.getLogger(BancoBean.class);

    private List<CatBanco> listBanco;
    private BancoDAO bancoDAO;

    private CatBanco banco;

    private Boolean nuevo;
    private Boolean primaryKey;
    
    public BancoBean()
    {        
        bancoDAO = new BancoDAO();
        listBanco = new ArrayList<>();
        banco = new CatBanco();
    }

    @PostConstruct
    public void init(){
        listBanco = bancoDAO.buscarTodos();
    }

    public void nuevo(){
        banco = new CatBanco();
        nuevo = true;
        primaryKey = false;
    }

    public void editar(){        
        nuevo = false;
        primaryKey = true;
    }

    public void save(){

        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Banco Guardado";
        
        try {

            if(this.nuevo){
                bancoDAO.guardar(banco);
                mensaje = "Registrado con éxito";
                severity = FacesMessage.SEVERITY_INFO;            

            }else{
                bancoDAO.actualizar(banco);
                mensaje = "Editado con éxito";
                severity = FacesMessage.SEVERITY_INFO;            
            }

            listBanco = bancoDAO.buscarTodos();
            PrimeFaces.current().executeScript("PF('dlgBanco').hide();");

        } catch (SGPException e) {                
            titulo = "Error Registro";
            mensaje = "Existe algun error en el registro";
            severity = FacesMessage.SEVERITY_ERROR;
            log.error("Error: " + e.getMessage());
            
        }catch(Exception ex){

            titulo = "Error Registro";
            mensaje = "Existe algun error en el registro";
            severity = FacesMessage.SEVERITY_ERROR;
            log.error("Error: " + ex.getMessage());

        }
        finally{
            message = new FacesMessage(severity,titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null,message);
            PrimeFaces.current().ajax().update("form:messages","form:dt-bancos");
        }


    }

    public CatBanco getBanco() {
        return banco;
    }

    public void setBanco(CatBanco banco) {
        this.banco = banco;
    }

    public List<CatBanco> getListBanco() {
        return listBanco;
    }

    public void setListBanco(List<CatBanco> listBanco) {
        this.listBanco = listBanco;
    }

    public Boolean getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
    
}
