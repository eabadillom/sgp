package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CaptureEvent;

import mx.com.ferbo.dao.CatAreaDAO;
import mx.com.ferbo.dao.CatEmpresaDAO;
import mx.com.ferbo.dao.CatPerfilDAO;
import mx.com.ferbo.dao.CatPlantaDAO;
import mx.com.ferbo.dao.CatPuestoDAO;
import mx.com.ferbo.dao.DatoEmpresaDAO;
import mx.com.ferbo.dao.DetBiometricoDAO;
import mx.com.ferbo.dao.EmpleadoDAO;
import mx.com.ferbo.dao.sat.TipoContratoDAO;
import mx.com.ferbo.dao.sat.TipoJornadaDAO;
import mx.com.ferbo.dao.sat.TipoRegimenDAO;
import mx.com.ferbo.dto.CatAreaDTO;
import mx.com.ferbo.dto.CatEmpresaDTO;
import mx.com.ferbo.dto.CatPerfilDTO;
import mx.com.ferbo.dto.CatPlantaDTO;
import mx.com.ferbo.dto.CatPuestoDTO;
import mx.com.ferbo.dto.DatoEmpresaDTO;
import mx.com.ferbo.dto.DetBiometricoDTO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import mx.com.ferbo.dto.sat.TipoContratoDTO;
import mx.com.ferbo.dto.sat.TipoJornadaDTO;
import mx.com.ferbo.dto.sat.TipoRegimenDTO;
import mx.com.ferbo.util.SGPException;


/**
 *
 * @author Gabo
 */
@Named(value = "registroEmpleadosBean")
@ViewScoped
public class RegistroEmpleadosBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(RegistroEmpleadosBean.class);

    private DetEmpleadoDTO empleadoSelected;

    private List<DetEmpleadoDTO> lstEmpleados;
    private List<DetEmpleadoDTO> lstEmpleadosSelected;
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
    private DatoEmpresaDTO datoEmpresa;
    private final DetBiometricoDAO biometricoDAO;
    private List<TipoContratoDTO> tiposContrato;
    private TipoContratoDAO tipoContratoDAO;
    private List<TipoJornadaDTO> tiposJornada;
    private TipoJornadaDAO tipoJornadaDAO;
    private List<TipoRegimenDTO> tiposRegimen;
    private TipoRegimenDAO tipoRegimenDAO;

    private DetBiometricoDTO detBiometrico;
    private String biometrico;
    private int numBiometrico;

    public RegistroEmpleadosBean() {
        catEmpresaDAO = new CatEmpresaDAO();
        catPerfilDAO = new CatPerfilDAO();
        catPlantaDAO = new CatPlantaDAO();
        catPuestoDAO = new CatPuestoDAO();
        biometricoDAO = new DetBiometricoDAO();
        catAreaDAO = new CatAreaDAO();
        empleadoDAO = new EmpleadoDAO();
        tipoContratoDAO = new TipoContratoDAO();
        tipoJornadaDAO = new TipoJornadaDAO();
        tipoRegimenDAO = new TipoRegimenDAO();
        
        empleadoSelected = new DetEmpleadoDTO();
        lstEmpleados = new ArrayList<>();
        lstEmpleadosSelected = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        try {
            lstCatEmpresa = catEmpresaDAO.buscarActivo();
            lstCatPerfil = catPerfilDAO.buscarActivo();
            lstCatPlanta = catPlantaDAO.buscarActivo();
            lstCatPuesto = catPuestoDAO.buscarActivo();
            lstCatArea = catAreaDAO.buscarActivo();
            tiposContrato = tipoContratoDAO.buscarTodos();
            tiposJornada = tipoJornadaDAO.buscarTodos();
            tiposRegimen = tipoRegimenDAO.buscarTodos();
            consultaEmpleados();
        } catch (Exception ex) {
            log.warn("EX-0008: " + ex.getMessage() + ". Error al cargar init()");
        }
    }

    /*
     * Método para consultar a los empleados
     */
    private void consultaEmpleados() {
        lstEmpleados = empleadoDAO.buscarActivoConSDI();
        biometrico = null;
    }

    /*
     * Método para obtener el mensaje a mostrar en el botón eliminar
     */
    public String getMensajeBotonEliminar() {
        if (isEmpleadoSeleccionado()) {
            int size = this.lstEmpleadosSelected.size();
            return size > 1 ? size + " empleados seleccionados" : "1 empleado seleccionado";
        }
        return "Eliminar";
    }

    /*
     * Método para eliminar verificar la seleccioón de n empleados
     */
    public boolean isEmpleadoSeleccionado() {
        return this.lstEmpleadosSelected != null && !this.lstEmpleadosSelected.isEmpty();
    }

    /*
     * Método para inicializar objeto empleado
     */
    public void agregarEmpleado() {
        this.empleadoSelected = new DetEmpleadoDTO();
        this.datoEmpresa = new DatoEmpresaDTO();
        this.empleadoSelected.setDatoEmpresa(this.datoEmpresa);
    }
    
    public void editar() {
    	Integer idEmpleado = this.empleadoSelected.getIdEmpleado();
    	DetEmpleadoDTO e = empleadoDAO.buscarPorId(idEmpleado, true);
    	DatoEmpresaDTO datoEmpresa = e.getDatoEmpresa();
    	if(datoEmpresa == null) {
    		this.datoEmpresa = new DatoEmpresaDTO();
    		this.empleadoSelected.setDatoEmpresa(this.datoEmpresa);
    	} else {
    		this.datoEmpresa = datoEmpresa;
    	}
    	log.info("Empleado seleccionado: {}", this.empleadoSelected.getIdEmpleado());
    }

    /*
     * Método para guardar empleado
     */
    public void guardarEmpleado() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Guardar empleado";
		
    	try {
    		this.empleadoSelected.setDatoEmpresa(this.datoEmpresa);
    		
    		if (this.empleadoSelected.getIdEmpleado() == null) {
    			empleadoDAO.guardar(empleadoSelected);
    		} else {
    			empleadoDAO.actualizar(empleadoSelected);
    		}
    		
    		if (biometrico != null) {
                try {
                    detBiometrico.setDetEmpleadoDTO(empleadoSelected);
                    if (detBiometrico.getIdBiometrico() == null) {
                        biometricoDAO.guardar(detBiometrico);
                    } else {
                        biometricoDAO.actualizar(detBiometrico);
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Biométrico registrado"));
                } catch (SGPException ex) {
                    FacesContext.getCurrentInstance()
                            .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al registrar biométrico"));
                    log.warn("EX-0015: " + ex.getMessage() + ". Error al guardar la huella del empleado " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
                }
                biometrico = null;
            }
    		
    		detBiometrico = new DetBiometricoDTO();
	        consultaEmpleados();
	        PrimeFaces.current().executeScript("PF('dialogEmpleado').hide()");
    		 
    		mensaje = "El empleado se guardó correctamente.";
    		severity = FacesMessage.SEVERITY_INFO;
    	} catch(SGPException ex) {
    		mensaje = ex.getMessage();
    		severity = FacesMessage.SEVERITY_WARN;
    	} catch(Exception ex) {
    		mensaje = "Problema para guardar al empleado.";
    		severity = FacesMessage.SEVERITY_ERROR;
    	} finally {
    		message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update("formRegistroEmpleado:messages", "formRegistroEmpleado:dtEmpleados");
			
    	}
    	
    	
        

//        detBiometrico = new DetBiometricoDTO();
//        consultaEmpleados();
//        PrimeFaces.current().executeScript("PF('dialogEmpleado').hide()");
//        PrimeFaces.current().ajax().update("formRegistroEmpleado:messages", "formRegistroEmpleado:dtEmpleados");
    }

    /*
     * Método para eliminar listado de empleados
     */
    public void eliminaEmpleadosSeleccionados() {
        List<String> numEmpl = new ArrayList<>();
        try {
            for (DetEmpleadoDTO empleado : lstEmpleadosSelected) {
                empleadoDAO.eliminar(empleado);
                numEmpl.add(empleado.getNumEmpleado());
            }
            consultaEmpleados();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Empleados Eliminados"));
        } catch (SGPException ex) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al eliminar uno o varios empleados"));
            String result = !numEmpl.isEmpty() ? numEmpl.toString() : null;
            log.warn("EX-0017: " + ex.getMessage() + ". Error al eliminar uno o varios empleados: " + result);
        }
        PrimeFaces.current().ajax().update("formRegistroEmpleado:messages", "formRegistroEmpleado:dtEmpleados");
        this.lstEmpleadosSelected.clear();
    }

    /*
     * Método para eliminar 1 empleado
     */
    public void eliminaEmpleado() {
        try {
            empleadoDAO.eliminar(empleadoSelected);
            consultaEmpleados();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Empleado Eliminado"));
        } catch (SGPException ex) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al eliminar al empleado"));
            log.warn("EX-0018: " + ex.getMessage() + ". Error al guardar la huella del empleado " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
        }
        PrimeFaces.current().ajax().update("formRegistroEmpleado:messages", "formRegistroEmpleado:dtEmpleados");
    }
 
    /* 
     * Método para redirigir al kárdex
     */
    public String redirectKardex() {
        String redirect = "/protected/kardexEmpleado.xhtml?faces-redirect=true&idEmpleado=" + empleadoSelected.getIdEmpleado();
        return redirect;
    }

    public void oncapture(CaptureEvent captureEvent) {
        empleadoSelected.setFotografia("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(captureEvent.getData()));
    }

    public void consultaBiometrico() {
        detBiometrico = biometricoDAO.consultaBiometricoByNumEmpleado(empleadoSelected.getNumEmpleado());
        PrimeFaces.current().executeScript("PF('dialogBiometrico').show()");
    }

    public void validaHuella() {
        if (biometrico != null) {
            if (numBiometrico == 1) {
                detBiometrico.setHuella(biometrico);
            } else {
                detBiometrico.setHuella2(biometrico);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Huella asignada"));
        } else {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al asignar biométrico"));
            
            log.warn("EX-0019: Error al consultar por huella al empleado " + detBiometrico.getDetEmpleadoDTO().getNumEmpleado() != null ? detBiometrico.getDetEmpleadoDTO().getNumEmpleado() : null);
        }
        PrimeFaces.current().ajax().update("formRegistroEmpleado:messages", "formRegistroEmpleado:panelDialogBiometrico");
    }
    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">

    public List<CatEmpresaDTO> getLstCatEmpresa() {
        return lstCatEmpresa;
    }

    public void setLstCatEmpresa(List<CatEmpresaDTO> lstCatEmpresa) {
        this.lstCatEmpresa = lstCatEmpresa;
    }

    public DetEmpleadoDTO getEmpleadoSelected() {
        return empleadoSelected;
    }

    public void setEmpleadoSelected(DetEmpleadoDTO empleadoSelected) {
        this.empleadoSelected = empleadoSelected;
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

    public List<DetEmpleadoDTO> getLstEmpleados() {
        return lstEmpleados;
    }

    public void setLstEmpleados(List<DetEmpleadoDTO> lstEmpleados) {
        this.lstEmpleados = lstEmpleados;
    }

    public List<DetEmpleadoDTO> getLstEmpleadosSelected() {
        return lstEmpleadosSelected;
    }

    public void setLstEmpleadosSelected(List<DetEmpleadoDTO> lstEmpleadosSelected) {
        this.lstEmpleadosSelected = lstEmpleadosSelected;
    }

    public String getBiometrico() {
        return biometrico;
    }

    public void setBiometrico(String biometrico) {
        this.biometrico = biometrico;
    }

    public int getNumBiometrico() {
        return numBiometrico;
    }

    public void setNumBiometrico(int numBiometrico) {
        this.numBiometrico = numBiometrico;
    }
    
    public DetBiometricoDTO getDetBiometrico() {
        return detBiometrico;
    }

    public void setDetBiometrico(DetBiometricoDTO detBiometrico) {
        this.detBiometrico = detBiometrico;
    }

	public DatoEmpresaDTO getDatoEmpresa() {
		return datoEmpresa;
	}

	public void setDatoEmpresa(DatoEmpresaDTO datoEmpresa) {
		this.datoEmpresa = datoEmpresa;
	}

	public List<TipoContratoDTO> getTiposContrato() {
		return tiposContrato;
	}

	public void setTiposContrato(List<TipoContratoDTO> tiposContrato) {
		this.tiposContrato = tiposContrato;
	}

	public List<TipoJornadaDTO> getTiposJornada() {
		return tiposJornada;
	}

	public void setTiposJornada(List<TipoJornadaDTO> tiposJornada) {
		this.tiposJornada = tiposJornada;
	}

	public List<TipoRegimenDTO> getTiposRegimen() {
		return tiposRegimen;
	}

	public void setTiposRegimen(List<TipoRegimenDTO> tiposRegimen) {
		this.tiposRegimen = tiposRegimen;
	}
    
//</editor-fold> 
}
