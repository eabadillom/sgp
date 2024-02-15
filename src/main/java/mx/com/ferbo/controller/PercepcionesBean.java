package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.com.ferbo.dao.CatPercepcionesDAO;
import mx.com.ferbo.dto.CatPercepcionesDTO;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 

/**
 *
 * @author erale
 */
@Named(value = "percepcionesBean")
@ViewScoped
public class PercepcionesBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static Logger log = LogManager.getLogger(PercepcionesBean.class); 
    
    private Date fechaCap;
    private CatPercepcionesDTO percepcionSeleccionada;
    private CatPercepcionesDTO percepcionNueva;
    private CatPercepcionesDAO catPercepcionesDAO;
    private List<CatPercepcionesDTO> percepcionBuscada;
    private List<CatPercepcionesDTO> percepcionActual;
    private List<CatPercepcionesDTO> percepciones;

    public PercepcionesBean() {
        percepcionBuscada = new ArrayList<>();
        percepcionActual = new ArrayList<>();
        catPercepcionesDAO = new CatPercepcionesDAO();
        percepcionNueva = new CatPercepcionesDTO();
        percepciones = catPercepcionesDAO.buscarTodos();
    }
    
    @PostConstruct
    public void init() {
        percepciones = catPercepcionesDAO.buscarTodos();
        percepcionNueva = new CatPercepcionesDTO();
        percepcionActual = catPercepcionesDAO.buscarActivo();
    }
    
    // Caluclo de Percepcion del empleado
    public void crearPercepcionNueva(){
        try {
            catPercepcionesDAO.guardar(percepcionNueva);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/sgp/protected/percepciones.xhtml");
        } catch (SGPException | IOException ex) { 
            log.warn("EX-0036: " + ex.getMessage() + ". Error al guardar la percepción. " + percepcionNueva.getUma() != null ? percepcionNueva.getUma() : null); 
        } 
    }

    //<editor-fold defaultstate="collapsed" desc="Getters&Setters"> 
    public Date getFechaCap() {
        return fechaCap;
    }

    public void setFechaCap(Date fechaCap) {
        this.fechaCap = fechaCap;
    }

    public CatPercepcionesDTO getPercepcionSeleccionada() {
        return percepcionSeleccionada;
    }

    public void setPercepcionSeleccionada(CatPercepcionesDTO percepcionSeleccionada) {
        this.percepcionSeleccionada = percepcionSeleccionada;
    }

    public CatPercepcionesDAO getCatPercepcionesDAO() {
        return catPercepcionesDAO;
    }

    public void setCatPercepcionesDAO(CatPercepcionesDAO catPercepcionesDAO) {
        this.catPercepcionesDAO = catPercepcionesDAO;
    }

    public List<CatPercepcionesDTO> getPercepcionBuscada() {
        return percepcionBuscada;
    }

    public void setPercepcionBuscada(List<CatPercepcionesDTO> percepcionBuscada) {
        this.percepcionBuscada = percepcionBuscada;
    }

    public List<CatPercepcionesDTO> getPercepcionActual() {
        return percepcionActual;
    }

    public void setPercepcionActual(List<CatPercepcionesDTO> percepcionActual) {
        this.percepcionActual = percepcionActual;
    }

    public CatPercepcionesDTO getPercepcionNueva() {
        return percepcionNueva;
    }

    public void setPercepcionNueva(CatPercepcionesDTO percepcionNueva) {
        this.percepcionNueva = percepcionNueva;
    }
    //</editor-fold>    
    
}
