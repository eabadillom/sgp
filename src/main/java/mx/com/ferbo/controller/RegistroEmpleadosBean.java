package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
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
import org.primefaces.event.TabChangeEvent;

import mx.com.ferbo.dao.n.AreaDAO;
import mx.com.ferbo.dao.n.BiometricoDAO;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.EmpleadoFotoDAO;
import mx.com.ferbo.dao.n.EmpresaDAO;
import mx.com.ferbo.dao.n.ParametroDAO;
import mx.com.ferbo.dao.n.PerfilDAO;
import mx.com.ferbo.dao.n.PlantaDAO;
import mx.com.ferbo.dao.n.PuestoDAO;
import mx.com.ferbo.dao.n.TipoContratoDAO;
import mx.com.ferbo.dao.n.TipoJornadaDAO;
import mx.com.ferbo.dao.n.TipoRegimenDAO;
import mx.com.ferbo.model.CatArea;
import mx.com.ferbo.model.CatEmpresa;
import mx.com.ferbo.model.CatParametro;
import mx.com.ferbo.model.CatPerfil;
import mx.com.ferbo.model.CatPlanta;
import mx.com.ferbo.model.CatPuesto;
import mx.com.ferbo.model.DetBiometrico;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetEmpleadoFoto;
import mx.com.ferbo.model.InfDatoEmpresa;
import mx.com.ferbo.model.sat.CatTipoContrato;
import mx.com.ferbo.model.sat.CatTipoJornada;
import mx.com.ferbo.model.sat.CatTipoRegimen;
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

    private DetEmpleado empleadoSelected;

    private List<DetEmpleado> lstEmpleados;
    private List<DetEmpleado> lstEmpleadosSelected;
    private List<CatEmpresa> lstCatEmpresa;
    private List<CatPerfil> lstCatPerfil;
    private List<CatPlanta> lstCatPlanta;
    private List<CatPuesto> lstCatPuesto;
    private List<CatArea> lstCatArea;

    private EmpresaDAO empresaDAO;
    private EmpleadoFotoDAO empleadoFotoDAO;
    private PerfilDAO perfilDAO;
    private PlantaDAO plantaDAO;
    private PuestoDAO puestoDAO;
    private AreaDAO areaDAO;
    private EmpleadoDAO empleadoDAO;
    
    private InfDatoEmpresa datoEmpresa;
    private BiometricoDAO biometricoDAO;
    private List<CatTipoContrato> tiposContrato;
    private TipoContratoDAO tipoContratoDAO;
    private List<CatTipoJornada> tiposJornada;
    private TipoJornadaDAO tipoJornadaDAO;
    private List<CatTipoRegimen> tiposRegimen;
    private TipoRegimenDAO tipoRegimenDAO;
    private ParametroDAO parametroDAO;

    private DetBiometrico detBiometrico;
    private DetEmpleadoFoto empleadoFoto;
    private String biometrico;
    private int numBiometrico;
    
    private String curp;
    private String rfc;
    private String nss;
    
    public RegistroEmpleadosBean() {
    	empleadoFotoDAO = new EmpleadoFotoDAO(DetEmpleadoFoto.class);
        empresaDAO = new EmpresaDAO(CatEmpresa.class);
        perfilDAO = new PerfilDAO(CatPerfil.class);
        plantaDAO = new PlantaDAO(CatPlanta.class);
        puestoDAO = new PuestoDAO(CatPuesto.class);
        biometricoDAO = new BiometricoDAO(DetBiometrico.class);
        areaDAO = new AreaDAO(CatArea.class);
        empleadoDAO = new EmpleadoDAO(DetEmpleado.class);
        tipoContratoDAO = new TipoContratoDAO(CatTipoContrato.class);
        tipoJornadaDAO = new TipoJornadaDAO(CatTipoJornada.class);
        tipoRegimenDAO = new TipoRegimenDAO(CatTipoRegimen.class);
        parametroDAO = new ParametroDAO(CatParametro.class);
        
        empleadoSelected = new DetEmpleado();
        lstEmpleados = new ArrayList<>();
        lstEmpleadosSelected = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        try {
            lstCatEmpresa = empresaDAO.buscarActivo();
            lstCatPerfil = perfilDAO.buscarActivo();
            lstCatPlanta = plantaDAO.buscarActivo();
            lstCatPuesto = puestoDAO.buscarActivo();
            lstCatArea = areaDAO.buscarActivo();
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
    	this.lstEmpleados = empleadoDAO.buscarTodos(false);
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
        this.empleadoSelected = new DetEmpleado();
        this.empleadoSelected.setActivo((short)1);
        this.datoEmpresa = new InfDatoEmpresa();
        this.empleadoSelected.setDatoEmpresa(this.datoEmpresa);
    }
    
    public void editar() {
    	log.info("Cargando información del empleado: {}", this.empleadoSelected);
//    	Integer idEmpleado = this.empleadoSelected.getIdEmpleado();
//    	DetEmpleado e = empleadoDAO.buscarPorId(idEmpleado, true, true);
//    	InfDatoEmpresa datoEmpresa = e.getDatoEmpresa();
    	InfDatoEmpresa datoEmpresa = this.empleadoSelected.getDatoEmpresa();
    	this.empleadoFoto = empleadoFotoDAO.buscar(this.empleadoSelected.getNumEmpleado());
    	if(datoEmpresa == null) {
    		this.datoEmpresa = new InfDatoEmpresa();
    		this.empleadoSelected.setDatoEmpresa(this.datoEmpresa);
    	} else {
    		this.datoEmpresa = datoEmpresa;
    		this.rfc = this.datoEmpresa.getRfc();
    		this.nss = this.datoEmpresa.getNss();
    		this.curp = this.empleadoSelected.getCurp();
    	}
    	
    	this.detBiometrico = biometricoDAO.consultaBiometricoByNumEmpleado(this.empleadoSelected.getNumEmpleado());
    	
    	log.info("Empleado seleccionado: {}", this.empleadoSelected.getIdEmpleado());
    	if(empleadoFoto != null)
    		log.debug("Foto: {}", empleadoFoto.getFotografia());
    	PrimeFaces.current().ajax().update("formRegistroEmpleado:panelDialogFoto");
    }
    
    public void tabChange(TabChangeEvent event) {
    	FacesMessage msg = new FacesMessage("Información...", event.getTab().getTitle());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        PrimeFaces.current().ajax().update("formRegistroEmpleado:messages", "formRegistroEmpleado:panelDialogEmpleado:previewFotografia");
    }

    /*
     * Método para guardar empleado
     */
    public synchronized void guardarEmpleado() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Guardar empleado";
		CatParametro pNumeroEmpleado = null;
		String sNumeroEmpleado = null;
		int numeroEmpleado = -1;
    	try {
    		
    		if (this.empleadoSelected.getIdEmpleado() == null) {
    			
    			pNumeroEmpleado = this.parametroDAO.buscarPorClave("NBEMP");
    			sNumeroEmpleado = pNumeroEmpleado.getValor();
        		numeroEmpleado = Integer.parseInt(sNumeroEmpleado);
        		sNumeroEmpleado = String.format("%04d", ++numeroEmpleado);
        		this.empleadoSelected.setNumEmpleado(sNumeroEmpleado);
        		this.empleadoSelected.setDatoEmpresa(this.datoEmpresa);
        		this.empleadoSelected.setFechaRegistro(new Date());
    			empleadoDAO.guardar(empleadoSelected);
    		} else {
    			empleadoDAO.actualizar(empleadoSelected);
    		}
    		
    		if(this.empleadoFoto != null) {
    			empleadoFotoDAO.actualizar(empleadoFoto);
    		}
    		
    		if (biometrico != null) {
                try {
                    detBiometrico.setIdEmpleado(empleadoSelected);
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
    		
    		detBiometrico = new DetBiometrico();
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
    }

    /*
     * Método para eliminar listado de empleados
     */
    public void eliminaEmpleadosSeleccionados() {
        List<String> numEmpl = new ArrayList<>();
        try {
            for (DetEmpleado empleado : lstEmpleadosSelected) {
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
    
    public void sinFoto() {
    	this.empleadoFoto = null;
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
 
    public String redirectKardex() {
        String redirect = "/protected/kardexEmpleado.xhtml?faces-redirect=true&idEmpleado=" + empleadoSelected.getIdEmpleado();
        return redirect;
    }

    public void oncapture(CaptureEvent captureEvent) {
    	if(this.empleadoFoto == null) {
    		this.empleadoFoto = new DetEmpleadoFoto();
    		this.empleadoSelected.setEmpleadoFoto(empleadoFoto);
    	}
    	
        this.empleadoFoto.setFotografia("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(captureEvent.getData()));
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
            
            log.warn("EX-0019: Error al consultar por huella al empleado " + detBiometrico.getIdEmpleado().getNumEmpleado() != null ? detBiometrico.getIdEmpleado().getNumEmpleado() : null);
        }
        PrimeFaces.current().ajax().update("formRegistroEmpleado:messages", "formRegistroEmpleado:panelDialogBiometrico");
    }
    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">

    public List<CatEmpresa> getLstCatEmpresa() {
        return lstCatEmpresa;
    }

    public void setLstCatEmpresa(List<CatEmpresa> lstCatEmpresa) {
        this.lstCatEmpresa = lstCatEmpresa;
    }

    public DetEmpleado getEmpleadoSelected() {
        return empleadoSelected;
    }

    public void setEmpleadoSelected(DetEmpleado empleadoSelected) {
        this.empleadoSelected = empleadoSelected;
    }

    public List<CatPerfil> getLstCatPerfil() {
        return lstCatPerfil;
    }

    public void setLstCatPerfil(List<CatPerfil> lstCatPerfil) {
        this.lstCatPerfil = lstCatPerfil;
    }

    public List<CatPlanta> getLstCatPlanta() {
        return lstCatPlanta;
    }

    public void setLstCatPlanta(List<CatPlanta> lstCatPlanta) {
        this.lstCatPlanta = lstCatPlanta;
    }

    public List<CatPuesto> getLstCatPuesto() {
        return lstCatPuesto;
    }

    public void setLstCatPuesto(List<CatPuesto> lstCatPuesto) {
        this.lstCatPuesto = lstCatPuesto;
    }

    public List<CatArea> getLstCatArea() {
        return lstCatArea;
    }

    public void setLstCatArea(List<CatArea> lstCatArea) {
        this.lstCatArea = lstCatArea;
    }

    public List<DetEmpleado> getLstEmpleados() {
        return lstEmpleados;
    }

    public void setLstEmpleados(List<DetEmpleado> lstEmpleados) {
        this.lstEmpleados = lstEmpleados;
    }

    public List<DetEmpleado> getLstEmpleadosSelected() {
        return lstEmpleadosSelected;
    }

    public void setLstEmpleadosSelected(List<DetEmpleado> lstEmpleadosSelected) {
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
    
    public DetBiometrico getDetBiometrico() {
        return detBiometrico;
    }

    public void setDetBiometrico(DetBiometrico detBiometrico) {
        this.detBiometrico = detBiometrico;
    }

	public InfDatoEmpresa getDatoEmpresa() {
		return datoEmpresa;
	}

	public void setDatoEmpresa(InfDatoEmpresa datoEmpresa) {
		this.datoEmpresa = datoEmpresa;
	}

	public List<CatTipoContrato> getTiposContrato() {
		return tiposContrato;
	}

	public void setTiposContrato(List<CatTipoContrato> tiposContrato) {
		this.tiposContrato = tiposContrato;
	}

	public List<CatTipoJornada> getTiposJornada() {
		return tiposJornada;
	}

	public void setTiposJornada(List<CatTipoJornada> tiposJornada) {
		this.tiposJornada = tiposJornada;
	}

	public List<CatTipoRegimen> getTiposRegimen() {
		return tiposRegimen;
	}

	public void setTiposRegimen(List<CatTipoRegimen> tiposRegimen) {
		this.tiposRegimen = tiposRegimen;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	public String getNss() {
		return nss;
	}

	public void setNss(String nss) {
		this.nss = nss;
	}

	public DetEmpleadoFoto getEmpleadoFoto() {
		return empleadoFoto;
	}

	public void setEmpleadoFoto(DetEmpleadoFoto empleadoFoto) {
		this.empleadoFoto = empleadoFoto;
	}
    
//</editor-fold> 
}
