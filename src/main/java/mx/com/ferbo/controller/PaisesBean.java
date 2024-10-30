package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.PaisDAO;
import mx.com.ferbo.model.Pais;

@Named(value = "paisesBean")
@ViewScoped
public class PaisesBean implements Serializable {

    private static final long serialVersionUID = -7294511691759768677L;
    private static Logger log = LogManager.getLogger(PaisesBean.class);
    private Pais pais = null;
    private PaisDAO paisDAO = null;
    private List<Pais> listaPaises = null;
    private List<Pais> listaPaisesSelected = null;

    public PaisesBean() 
    {
        paisDAO = new PaisDAO();
        listaPaisesSelected = new ArrayList<>();
    }

    @PostConstruct
    public void init() 
    {
        listaPaises = paisDAO.buscarTodos();
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public List<Pais> getListaPaisesSelected() {
        return listaPaisesSelected;
    }

    public void setListaPaisesSelected(List<Pais> listaPaisesSelected) {
        this.listaPaisesSelected = listaPaisesSelected;
    }
    
    public List<Pais> getListaPaises() 
    {
        return listaPaises;
    }

    public void setListaPaises(List<Pais> listaPaises) 
    {
        this.listaPaises = listaPaises;
    }
    
}
