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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.PercepcionesDAO;
import mx.com.ferbo.model.CatPercepciones;
import mx.com.ferbo.util.SGPException; 

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
    private CatPercepciones percepcionSeleccionada;
    private CatPercepciones percepcionNueva;
    private PercepcionesDAO percepcionesDAO;
    private List<CatPercepciones> percepcionBuscada;
    private List<CatPercepciones> percepcionActual;
    private List<CatPercepciones> percepciones;

    public PercepcionesBean() {
        percepcionBuscada = new ArrayList<>();
        percepcionActual = new ArrayList<>();
        percepcionesDAO = new PercepcionesDAO();
        percepcionNueva = new CatPercepciones();
    }
    
    @PostConstruct
    public void init() {
        percepciones = percepcionesDAO.buscarTodos();
        percepcionNueva = new CatPercepciones();
        percepcionNueva.setActivo((short)1);
        percepcionActual = percepcionesDAO.buscarTodosActivos();
    }
    
    public void actualizar()
    {
        percepcionActual = percepcionesDAO.buscarTodosActivos();
    }
    
    // Caluclo de Percepcion del empleado
    public void crearPercepcionNueva(){
        try {
            percepcionesDAO.guardar(percepcionNueva);
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

    public CatPercepciones getPercepcionSeleccionada() {
        return percepcionSeleccionada;
    }

    public void setPercepcionSeleccionada(CatPercepciones percepcionSeleccionada) {
        this.percepcionSeleccionada = percepcionSeleccionada;
    }

    public PercepcionesDAO getCatPercepcionesDAO() {
        return percepcionesDAO;
    }

    public void setPercepcionesDAO(PercepcionesDAO percepcionesDAO) {
        this.percepcionesDAO = percepcionesDAO;
    }

    public List<CatPercepciones> getPercepcionBuscada() {
        return percepcionBuscada;
    }

    public void setPercepcionBuscada(List<CatPercepciones> percepcionBuscada) {
        this.percepcionBuscada = percepcionBuscada;
    }

    public List<CatPercepciones> getPercepcionActual() {
        return percepcionActual;
    }

    public void setPercepcionActual(List<CatPercepciones> percepcionActual) {
        this.percepcionActual = percepcionActual;
    }

    public CatPercepciones getPercepcionNueva() {
        return percepcionNueva;
    }

    public void setPercepcionNueva(CatPercepciones percepcionNueva) {
        this.percepcionNueva = percepcionNueva;
    }
    //</editor-fold>    
    
}
