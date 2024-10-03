/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.controller;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.com.ferbo.dao.n.SubsidioDAO;
import mx.com.ferbo.model.CatSubsidio;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named(value = "subsidioBean")
@ViewScoped
public class SubsidioBean implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(SubsidioBean.class);
    
    private CatSubsidio subsidio = null;
    private SubsidioDAO subsidioDAO = null;
    private List<CatSubsidio> listSubsidio = null;
    private List<CatSubsidio> listSubsidioSelected = null;
    private Date fecha;
    private SimpleDateFormat formato = null;

    public SubsidioBean() 
    {
        this.subsidioDAO = new SubsidioDAO();
        this.listSubsidioSelected = new ArrayList<>();
        formato = new SimpleDateFormat("dd/MM/yyyy");
    }
    
    @PostConstruct
    public void init() 
    {
        this.fecha();
        this.listSubsidio = this.subsidioDAO.listaSubsidio(this.getFecha());
    }
    
    public void fecha()
    {
        try
        {
            this.setFecha(formato.parse("01/05/2023"));
        }catch(ParseException ex)
        {
            log.error("Error al darle formato de fecha.." + ex);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public CatSubsidio getSubsidio() {
        return subsidio;
    }

    public void setSubsidio(CatSubsidio subsidio) {
        this.subsidio = subsidio;
    }

    public List<CatSubsidio> getListSubsidio() {
        return listSubsidio;
    }

    public void setListSubsidio(List<CatSubsidio> listSubsidio) {
        this.listSubsidio = listSubsidio;
    }

    public List<CatSubsidio> getListSubsidioSelected() {
        return listSubsidioSelected;
    }

    public void setListSubsidioSelected(List<CatSubsidio> listSubsidioSelected) {
        this.listSubsidioSelected = listSubsidioSelected;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    //</editor-fold>
    
}
