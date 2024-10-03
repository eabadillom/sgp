/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.com.ferbo.dao.n.TarifaISRDAO;
import mx.com.ferbo.model.CatTarifaISR;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

/**
 *
 * @author alberto
 */
@Named(value = "isrBean")
@ViewScoped
public class ISRBean implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(UMABean.class);
    
    private CatTarifaISR tarifaISR = null;
    private TarifaISRDAO tarifaISRDAO = null;
    private List<CatTarifaISR> listTarifaISR = null;
    private List<CatTarifaISR> listTarifaISRSelected = null;
    
    public ISRBean()
    {
        this.listTarifaISRSelected = new ArrayList<>();
        this.tarifaISRDAO = new TarifaISRDAO(); 
    }
    
    @PostConstruct
    public void init() 
    {
        this.listTarifaISR = this.tarifaISRDAO.buscarTodos();
    }
    
    public void actualizarListaISR()
    {
        this.listTarifaISR = this.tarifaISRDAO.buscarTodos();
    }
    
    public void crearISRNuevo()
    {
        try
        {
            if(this.tarifaISR.getIdIsr() == null)
            {
                this.tarifaISRDAO.guardar(this.tarifaISR);
                this.actualizarListaISR();
                PrimeFaces.current().ajax().update("form:messages", "form:dt-isr");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ISR Añadida"));
            }else
            {
                this.tarifaISRDAO.actualizar(this.tarifaISR);
                PrimeFaces.current().ajax().update("form:messages", "form:dt-isr");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ISR Actualizada"));
            }
            PrimeFaces.current().executeScript("PF('manageISRDialog').hide()");
        }catch(SGPException ex)
        {
            log.error("Error al guadar el ISR.." + ex);
        }
    }
    
    public void borrarISRRegistro()
    {
        try
        {
            if(this.tarifaISR != null)
            {
                this.tarifaISRDAO.eliminar(this.tarifaISR);
                this.listTarifaISRSelected.remove(this.tarifaISR);
                this.tarifaISR = null;
                this.actualizarListaISR();
                PrimeFaces.current().ajax().update("form:messages", "form:dt-isr");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ISR Borrada"));
            }
            PrimeFaces.current().executeScript("PF('borrarISRDialog').hide()");
        }catch(SGPException ex)
        {
            log.error("Error al eliminar una ISR.." + ex);
        }
    }
    
    public void openNew()
    {
        this.tarifaISR = new CatTarifaISR();
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public CatTarifaISR getTarifaISR() {
        return tarifaISR;
    }

    public void setTarifaISR(CatTarifaISR tarifaISR) {
        this.tarifaISR = tarifaISR;
    }

    public List<CatTarifaISR> getListTarifaISR() {
        return listTarifaISR;
    }

    public void setListTarifaISR(List<CatTarifaISR> listTarifaISR) {
        this.listTarifaISR = listTarifaISR;
    }

    public List<CatTarifaISR> getListTarifaISRSelected() {
        return listTarifaISRSelected;
    }

    public void setListTarifaISRSelected(List<CatTarifaISR> listTarifaISRSelected) {
        this.listTarifaISRSelected = listTarifaISRSelected;
    }
    //</editor-fold>
    
}
