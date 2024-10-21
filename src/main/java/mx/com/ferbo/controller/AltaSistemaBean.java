package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.com.ferbo.dao.n.FpClientDAO;
import mx.com.ferbo.dao.n.PlantaDAO;
import mx.com.ferbo.dao.n.RolSistemaDAO;
import mx.com.ferbo.model.CatPlanta;
import mx.com.ferbo.model.DetFpClient;
import mx.com.ferbo.model.DetRolSistema;
import mx.com.ferbo.util.SGPException;
import mx.com.ferbo.util.Seguridad;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.primefaces.PrimeFaces;
import org.primefaces.PrimeFaces.Dialog;

@Named(value = "altasistemaBean")
@ViewScoped
public class AltaSistemaBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(AltaSistemaBean.class);

    private FpClientDAO fpclientdao;
    private PlantaDAO plantadao;
    private RolSistemaDAO rolsistemadao;
    private DetFpClient sistema;
    private List<DetFpClient> sistemas;
    private List<CatPlanta> plantas;
    private List<DetRolSistema> roles;
    private String clave1;
    private String clave2;

    private String accion = "";

    public AltaSistemaBean() {
        fpclientdao = new FpClientDAO();
        plantadao = new PlantaDAO(CatPlanta.class);
        rolsistemadao = new RolSistemaDAO(DetRolSistema.class);
    }

    @PostConstruct
    public void init() {
        this.plantas = this.plantadao.buscarTodos();
        this.roles = this.rolsistemadao.obtenerTodos();
    }

    public List<DetFpClient> getSistemas() {
        return sistemas;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public DetFpClient getSistema() {
        return sistema;
    }

    public void setSistema(DetFpClient sistematmp) {
        this.sistema = sistematmp;
    }

    public List<CatPlanta> getPlantas() {
        return plantas;
    }

    public List<DetRolSistema> getRoles() {
        return roles;
    }

    public String getClave1() {
        return clave1;
    }

    public void setClave1(String clave1) {
        this.clave1 = clave1;
    }

    public String getClave2() {
        return clave2;
    }

    public void setClave2(String clave2) {
        this.clave2 = clave2;
    }

    public void nuevoSistema() {
        this.sistema = new DetFpClient();
        this.accion = "Registrar";
    }

    public void buscarId(DetFpClient sistematemp) {
        try {
            //this.sistema = this.fpclientdao.buscarPorId(sistematemp.getIdFpClient());
            this.sistema = sistematemp;
            this.accion = "Modificar";
        } catch (Exception ex) {
            log.info("Problema en obtener elemento no encontrado...", ex);
        }
    }

    public void registrar() {
        try{
            fpclientdao.guardar(sistema);
        this.listar(true);
        }
        catch(SGPException ex){
            
        }
        catch(Exception ex){
            
        }
    }

    public void actualizar() {
        try{
        this.fpclientdao.actualizar(sistema);
        this.listar(true);
        }
        catch(SGPException ex){
        }
        catch(Exception ex){
        }
    }

    public void eliminar() {
        try{
        this.fpclientdao.eliminar(sistema);
        this.listar(true);
        }
        catch(SGPException ex){
        }
        catch(Exception ex){
            
        }
    }

    public void operar() {
        switch (accion) {
            case "Registrar":
                this.registrar();
                break;
            case "Modificar":
                this.actualizar();
                break;
        }
    }

    public void listar(boolean flag) {
        try {
            if (!flag) {
                if (this.isPostBack() == false) {
                    this.sistemas = this.fpclientdao.obtenerTodos();
                }
            } else {
                this.sistemas = this.fpclientdao.obtenerTodos();
            }

        } catch (Exception ex) {
            log.info(ex);
        }
    }

    private boolean isPostBack() {
        boolean respuesta = false;

        respuesta = FacesContext.getCurrentInstance().isPostback();

        return respuesta;
    }

    public void statusSeguridad(DetFpClient sistematmp) {

        this.sistema = this.fpclientdao.buscarPorId(sistematmp.getIdFpClient());

        if (sistema.getPassword() != null) {
            this.accion = "Actualizar la ";
        } else {
            this.accion = "Guardar nueva";
        }
    }

    public void guardarClave() {
        PrimeFaces current = PrimeFaces.current();
        try {
            if (clave1 == null || clave2 == null) {
                throw new SGPException("Ninguno de los campos debe estar vacio. \nIntente de nuevo.");
            }

            if (clave1.equals("") || clave2.equals("")) {
                throw new SGPException("La clave debe terner al menos un caracter. \nIntente de nuevo.");
            }

            if (clave1.contains(" ") || clave2.contains(" ")) {
                throw new SGPException("La clave no debe contener espacios en blanco. \nIntente de nuevo.");
            }

            if (!clave1.contains(clave2)) {
                throw new SGPException("Los campos no coinciden. \nIntente de nuevo.");
            }
            
            clave2 = Seguridad.Cifrar(clave1);
            this.sistema.setPassword(clave2);
            this.fpclientdao.actualizar(sistema);
            
            current.executeScript("PF('dlgSeguridad').hide()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito: Clave guardada correctamente", null));
            clave1 = null;
            clave2 = null;
            PrimeFaces.current().ajax().update("message");

        } catch (SGPException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Error: " + ex.getMessage(), null));
            current.executeScript("PF('dlgSeguridad').show()");
            log.info(ex.getMessage());
            PrimeFaces.current().ajax().update("message");
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            current.executeScript("PF('dlgSeguridad').show()");
            log.info(ex.getMessage());
            PrimeFaces.current().ajax().update("message");
        }
    }

    public void limpiar() {
        clave1 = null;
        clave2 = null;
    }
}
