package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.CuotaIMSSDAO;
import mx.com.ferbo.model.CatCuotaIMSS;
import mx.com.ferbo.model.CatCuotaIMSSPK;
import mx.com.ferbo.util.SGPException; 
import org.primefaces.PrimeFaces;
/**
 *
 * @author erale
 */
@Named(value = "cuotasImssBean")
@ViewScoped
public class CuotasImssBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(CuotasImssBean.class); 
    
    private CatCuotaIMSS cuotaIMSSNueva;
    private final CuotaIMSSDAO cuotaIMSSDAO;
    private List<CatCuotaIMSS> cuotaIMSSSeleccionada;
    private List<CatCuotaIMSS> cuotasIMSS;

    public CuotasImssBean() 
    {
        this.cuotaIMSSSeleccionada = new ArrayList<>();
        this.cuotaIMSSDAO = new CuotaIMSSDAO();
    }
    
    @PostConstruct
    public void init() 
    {
        this.cuotasIMSS = this.cuotaIMSSDAO.obtenerLista();
    }
    
    public void actualizarListaCuotas()
    {
        this.cuotasIMSS = this.cuotaIMSSDAO.obtenerLista();
    }
    
    public void crearCuotaIMSSNueva() {
        try {
            if (this.cuotaIMSSNueva.getKey().getNumeroClave() == null) {
                Integer numeroClave = this.obtenerNoClaveMax();
                this.cuotaIMSSNueva.getKey().setNumeroClave(numeroClave);
                this.cuotaIMSSDAO.guardar(this.cuotaIMSSNueva);
                this.actualizarListaCuotas();
                PrimeFaces.current().ajax().update("form:messages", "form:dt-percepcion");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cuota Añadida"));
            }else{
                this.cuotaIMSSDAO.actualizar(this.cuotaIMSSNueva);
                PrimeFaces.current().ajax().update("form:messages", "form:dt-percepcion");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cuota Actualizada"));
            }
            PrimeFaces.current().executeScript("PF('manageProductDialog').hide()");
        } catch (SGPException ex) {
            log.warn("EX-0037: " + ex.getMessage() + ". Error al guardar las cuotas del IMSS. " + cuotaIMSSNueva.getCuota() != null ? cuotaIMSSNueva.getCuota() : null); 
        }
    }
    
    public Integer obtenerNoClaveMax()
    {
        Integer maxNumero = 0;
        Integer iterador = 0;
        String clave = this.cuotaIMSSNueva.getKey().getClave();
        
        for(CatCuotaIMSS auxCatCuotaIMSS : this.cuotasIMSS)
        {
            if(auxCatCuotaIMSS.getKey().getClave().equals(clave))
            {
                iterador++;
            }
        }
        
        maxNumero = iterador;        
        return maxNumero;
    }
    
    public void openNew() {
        this.cuotaIMSSNueva = new CatCuotaIMSS();
        this.cuotaIMSSNueva.setKey(new CatCuotaIMSSPK());
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public CatCuotaIMSS getCuotaIMSSNueva() {
        return cuotaIMSSNueva;
    }
    
    public void setCuotaIMSSNueva(CatCuotaIMSS cuotaImssNueva) {
        this.cuotaIMSSNueva = cuotaImssNueva;
    }

    public List<CatCuotaIMSS> getCuotaIMSSSeleccionada() {
        return cuotaIMSSSeleccionada;
    }

    public void setCuotaIMSSSeleccionada(List<CatCuotaIMSS> cuotaIMSSSeleccionada) {
        this.cuotaIMSSSeleccionada = cuotaIMSSSeleccionada;
    }

    public List<CatCuotaIMSS> getCuotasIMSS() {
        return cuotasIMSS;
    }

    public void setCuotasIMSS(List<CatCuotaIMSS> cuotasIMSS) {
        this.cuotasIMSS = cuotasIMSS;
    }
    //</editor-fold> 
}
