/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.UMADAO;
import mx.com.ferbo.model.CatUMA;
import mx.com.ferbo.util.SGPException;
import org.primefaces.PrimeFaces;

/**
 *
 * @author alberto
 */
@Named(value = "umaBean")
@ViewScoped
public class UMABean implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(UMABean.class);
    
    private CatUMA uma = null;
    private UMADAO umaDAO = null;
    private List<CatUMA> catUMAS = null;
    private List<CatUMA> catUMASeleccionada = null;
    
    public UMABean()
    {
        this.catUMASeleccionada = new ArrayList<CatUMA>(); 
        this.umaDAO = new UMADAO();
    }
    
    @PostConstruct
    public void init() 
    {
        this.catUMAS = this.umaDAO.obtenerLista();
    }
    
    public void actualizarListaUma()
    {
        this.catUMAS = this.umaDAO.obtenerLista();
    }
    
    public void crearUMA()
    {
        try
        {
            if(this.uma.getImporteAnual() == null)
            {
                BigDecimal vMensual = this.calcularValorMensual(this.uma.getImporteDiario());
                this.uma.setImporteMensual(vMensual);
                BigDecimal vAnual = this.calcularValorAnual(this.uma.getImporteMensual());
                this.uma.setImporteAnual(vAnual);
                this.umaDAO.guardar(uma);
                actualizarListaUma();
                PrimeFaces.current().ajax().update("form:messages", "form:dt-uma");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("UMA Añadida"));
            }else
            {
                BigDecimal vMensual = this.calcularValorMensual(this.uma.getImporteDiario());
                this.uma.setImporteMensual(vMensual);
                BigDecimal vAnual = this.calcularValorAnual(this.uma.getImporteMensual());
                this.uma.setImporteAnual(vAnual);
                this.umaDAO.actualizar(uma);
                PrimeFaces.current().ajax().update("form:messages", "form:dt-uma");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("UMA Actualizada"));
            }
            PrimeFaces.current().executeScript("PF('manageUMADialog').hide()");
        }catch(SGPException ex)
        {
            log.warn("Error al guardar la uma." + ex.getMessage());
        }
    }
    
    public void openNew()
    {
        this.uma = new CatUMA();
    }
    
    public BigDecimal calcularValorMensual(BigDecimal valorDiario)
    {
        BigDecimal valorMensual = null;
        BigDecimal numVeces = new BigDecimal("30.4");
        
        valorMensual = valorDiario.multiply(numVeces);
        
        return valorMensual;
    }
    
    public BigDecimal calcularValorAnual(BigDecimal valorMensual)
    {
        BigDecimal valorAnual = null;
        BigDecimal numVeces = new BigDecimal("12");
        
        valorAnual = valorMensual.multiply(numVeces);
        
        return valorAnual;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public CatUMA getUma() {
        return uma;
    }

    public void setUma(CatUMA uma) {
        this.uma = uma;
    }

    public List<CatUMA> getCatUMAS() {
        return catUMAS;
    }

    public void setCatUMAS(List<CatUMA> catUMAS) {
        this.catUMAS = catUMAS;
    }

    public List<CatUMA> getCatUMASeleccionada() {
        return catUMASeleccionada;
    }

    public void setCatUMASeleccionada(List<CatUMA> catUMASeleccionada) {
        this.catUMASeleccionada = catUMASeleccionada;
    }
    //</editor-fold> 
    
}
