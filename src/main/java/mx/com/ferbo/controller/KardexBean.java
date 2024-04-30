package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.EmpleadoDAO;
import mx.com.ferbo.dto.CatAreaDTO;
import mx.com.ferbo.dto.CatEmpresaDTO;
import mx.com.ferbo.dto.CatPerfilDTO;
import mx.com.ferbo.dto.CatPlantaDTO;
import mx.com.ferbo.dto.CatPuestoDTO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import mx.com.ferbo.dto.EmpleadoFotoDTO;


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

    private EmpleadoDAO empleadoDAO;
    private DetEmpleadoDTO empleadoSelected;
    private EmpleadoFotoDTO empleadoFoto;
    private HttpServletRequest request;

    public KardexBean() {
        empleadoDAO = new EmpleadoDAO();
    }

    @PostConstruct
    public void init() {
    	HttpSession session = null;
    	
        try {
        	request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        	session = request.getSession(false);
            empleadoSelected = (DetEmpleadoDTO) session.getAttribute("empleado");
            empleadoFoto = (EmpleadoFotoDTO) session.getAttribute("fotografia");
            empleadoSelected = empleadoDAO.buscarPorId(empleadoSelected.getIdEmpleado(), true);
        	
            log.info("idEmpleado: {}", empleadoSelected.getIdEmpleado());
        	
        } catch (Exception ex) {
            log.warn("EX-0007: " + ex.getMessage() + ". Error al cargar init()");
        }
    }
    
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

	public EmpleadoFotoDTO getEmpleadoFoto() {
		return empleadoFoto;
	}

	public void setEmpleadoFoto(EmpleadoFotoDTO empleadoFoto) {
		this.empleadoFoto = empleadoFoto;
	}

}
