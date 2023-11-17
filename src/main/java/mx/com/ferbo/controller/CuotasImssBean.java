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
import mx.com.ferbo.dao.CatImssCuotasDAO;
import mx.com.ferbo.dto.CatImssCuotasDTO;
import mx.com.ferbo.util.SGPException;

import org.apache.log4j.Logger; 
/**
 *
 * @author erale
 */
@Named(value = "cuotasImssBean")
@ViewScoped
public class CuotasImssBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static final Logger log = Logger.getLogger(CuotasImssBean.class); 
    
    private Date fechaCap;
    private CatImssCuotasDTO cuotaImssNueva;
    private final CatImssCuotasDAO catImssCuotasDAO;
    private List<CatImssCuotasDTO> cuotaImssSeleccionada;
    private List<CatImssCuotasDTO> cuotasImss;
    private String cuotaSelect;

    public CuotasImssBean() {
        cuotaImssSeleccionada = new ArrayList<>();
        cuotasImss = new ArrayList<>();
        catImssCuotasDAO = new CatImssCuotasDAO();
        cuotasImss = catImssCuotasDAO.buscarTodos();
    }
    
    @PostConstruct
    public void init() {
        cuotaImssNueva = new CatImssCuotasDTO();
        cuotaImssSeleccionada = catImssCuotasDAO.buscarActivo();
    }
    
    public void crearCuotaImssNueva() {
        try {
            if (!cuotaImssNueva.getCuota().equals("")) {
                catImssCuotasDAO.guardar(cuotaImssNueva);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/sgp/protected/cuotasImss.xhtml");
            }
        } catch (IOException | SGPException ex) {
            log.warn("EX-0037: " + ex.getMessage() + ". Error al guardar las cuotas del IMSS. " + cuotaImssNueva.getCuota() != null ? cuotaImssNueva.getCuota() : null); 
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters&Setters"> 
    public Date getFechaCap() {
        return fechaCap;
    }

    public void setFechaCap(Date fechaCap) {
        this.fechaCap = fechaCap;
    }

    public CatImssCuotasDTO getCuotaImssNueva() {
        return cuotaImssNueva;
    }

    public void setCuotaImssNueva(CatImssCuotasDTO cuotaImssNueva) {
        this.cuotaImssNueva = cuotaImssNueva;
    }

    public List<CatImssCuotasDTO> getCuotaImssSeleccionada() {
        return cuotaImssSeleccionada;
    }

    public void setCuotaImssSeleccionada(List<CatImssCuotasDTO> cuotaImssSeleccionada) {
        this.cuotaImssSeleccionada = cuotaImssSeleccionada;
    }

    public List<CatImssCuotasDTO> getCuotasImss() {
        return cuotasImss;
    }

    public void setCuotasImss(List<CatImssCuotasDTO> cuotasImss) {
        this.cuotasImss = cuotasImss;
    }

    public String getCuotaSelect() {
        return cuotaSelect;
    }

    public void setCuotaSelect(String cuotaSelect) {
        this.cuotaSelect = cuotaSelect;
    }
    //</editor-fold> 
}
