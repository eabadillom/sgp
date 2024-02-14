package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import mx.com.ferbo.dao.CatAreaDAO;
import mx.com.ferbo.dao.CatEmpresaDAO;
import mx.com.ferbo.dao.CatPerfilDAO;
import mx.com.ferbo.dao.CatPlantaDAO;
import mx.com.ferbo.dao.CatPuestoDAO;
import mx.com.ferbo.dao.EmpleadoDAO;
import mx.com.ferbo.dto.CatAreaDTO;
import mx.com.ferbo.dto.CatEmpresaDTO;
import mx.com.ferbo.dto.CatPerfilDTO;
import mx.com.ferbo.dto.CatPlantaDTO;
import mx.com.ferbo.dto.CatPuestoDTO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Named(value = "kardexBean")
@ViewScoped
public class KardexBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static Logger log = LogManager.getLogger(KardexBean.class);

    private List<CatEmpresaDTO> lstCatEmpresa;
    private List<CatPerfilDTO> lstCatPerfil;
    private List<CatPlantaDTO> lstCatPlanta;
    private List<CatPuestoDTO> lstCatPuesto;
    private List<CatAreaDTO> lstCatArea;

    private final CatEmpresaDAO catEmpresaDAO;
    private final CatPerfilDAO catPerfilDAO;
    private final CatPlantaDAO catPlantaDAO;
    private final CatPuestoDAO catPuestoDAO;
    private final CatAreaDAO catAreaDAO;
    private final EmpleadoDAO empleadoDAO;
    
    // Obteniendo Empleado
    private DetEmpleadoDTO empleadoSelected;
    private HttpServletRequest httpServletRequest;

    public KardexBean() {
        catEmpresaDAO = new CatEmpresaDAO();
        catPerfilDAO = new CatPerfilDAO();
        catPlantaDAO = new CatPlantaDAO();
        catPuestoDAO = new CatPuestoDAO();
        catAreaDAO = new CatAreaDAO();
        empleadoDAO = new EmpleadoDAO();
        Integer idEmpleado = searchIdEmpleado();
        if(idEmpleado == null) {
            httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            empleadoSelected = (DetEmpleadoDTO) httpServletRequest.getSession(true).getAttribute("empleado");
            empleadoSelected = empleadoDAO.buscarPorIdSDI(empleadoSelected.getIdEmpleado());
        } else {
            empleadoSelected = empleadoDAO.buscarPorIdSDI(idEmpleado);
        }
    }

    @PostConstruct
    public void init() {
        try {
            lstCatEmpresa = catEmpresaDAO.buscarActivo();
            lstCatPerfil = catPerfilDAO.buscarActivo();
            lstCatPlanta = catPlantaDAO.buscarActivo();
            lstCatPuesto = catPuestoDAO.buscarActivo();
            lstCatArea = catAreaDAO.buscarActivo();
        } catch (Exception ex) {
            log.warn("EX-0007: " + ex.getMessage() + ". Error al cargar init()");
        }
    }
    
    private Integer searchIdEmpleado(){
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = params.get("idEmpleado");
        if (isNumeric(id)) {
            return Integer.valueOf(id);
        } else {
            return null;
        }
    }
    
    private static boolean isNumeric(final String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        Integer.valueOf(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}

//<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public DetEmpleadoDTO getEmpleadoSelected() {
        return empleadoSelected;
    }

    public void setEmpleadoSelected(DetEmpleadoDTO empleadoSelected) {
        this.empleadoSelected = empleadoSelected;
    }

    public List<CatEmpresaDTO> getLstCatEmpresa() {
        return lstCatEmpresa;
    }

    public void setLstCatEmpresa(List<CatEmpresaDTO> lstCatEmpresa) {
        this.lstCatEmpresa = lstCatEmpresa;
    }

    public List<CatPerfilDTO> getLstCatPerfil() {
        return lstCatPerfil;
    }

    public void setLstCatPerfil(List<CatPerfilDTO> lstCatPerfil) {
        this.lstCatPerfil = lstCatPerfil;
    }

    public List<CatPlantaDTO> getLstCatPlanta() {
        return lstCatPlanta;
    }

    public void setLstCatPlanta(List<CatPlantaDTO> lstCatPlanta) {
        this.lstCatPlanta = lstCatPlanta;
    }

    public List<CatPuestoDTO> getLstCatPuesto() {
        return lstCatPuesto;
    }

    public void setLstCatPuesto(List<CatPuestoDTO> lstCatPuesto) {
        this.lstCatPuesto = lstCatPuesto;
    }

    public List<CatAreaDTO> getLstCatArea() {
        return lstCatArea;
    }

    public void setLstCatArea(List<CatAreaDTO> lstCatArea) {
        this.lstCatArea = lstCatArea;
    }
    //</editor-fold>

}
