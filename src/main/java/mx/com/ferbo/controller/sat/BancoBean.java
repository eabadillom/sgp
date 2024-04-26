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

import mx.com.ferbo.dao.sat.CatBancoDAO;
import mx.com.ferbo.dto.sat.BancoDTO;
import mx.com.ferbo.util.SGPException;

@Named(value = "bancoBean")
@ViewScoped
public class BancoBean implements Serializable {


	private static final long serialVersionUID = -3103728579268351267L;
	private static Logger log = LogManager.getLogger(BancoBean.class);

    private List<BancoDTO> listBancoDTO;
    private CatBancoDAO catBancoDAO;

    private BancoDTO bancoDTO;
    private Boolean nuevo;
    
    public BancoBean(){        

        listBancoDTO = new ArrayList<>();
        catBancoDAO = new CatBancoDAO();
    }

    @PostConstruct
    public void init(){
        listBancoDTO = catBancoDAO.buscarTodos();
    }

    public void nuevo(){
        bancoDTO = new BancoDTO();
        nuevo = true;
    }

    public void editar(){        
        nuevo = false;
    }

    public void save(){

        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Banco Guardado";
        
        try {

            if(this.nuevo){
                catBancoDAO.guardar(bancoDTO);
                mensaje = "Registrado con éxito";
                severity = FacesMessage.SEVERITY_INFO;            

            }else{
                catBancoDAO.actualizar(bancoDTO);
                mensaje = "Editado con éxito";
                severity = FacesMessage.SEVERITY_WARN;            
            }

            listBancoDTO = catBancoDAO.buscarTodos();

        } catch (SGPException e) {                
            mensaje = e.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("Error" + e.getMessage());
        }finally{
            message = new FacesMessage(severity,titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null,message);
            PrimeFaces.current().ajax().update("form:messages","form:dt-bancos");
        }


    }

    public BancoDTO getBancoDTO() {
        return bancoDTO;
    }

    public void setBancoDTO(BancoDTO bancoDTO) {
        this.bancoDTO = bancoDTO;
    }

    public List<BancoDTO> getListBancoDTO() {
        return listBancoDTO;
    }

    public void setListBancoDTO(List<BancoDTO> listBancoDTO) {
        this.listBancoDTO = listBancoDTO;
    }

    

}
