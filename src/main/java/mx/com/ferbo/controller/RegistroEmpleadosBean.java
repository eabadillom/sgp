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

import mx.com.ferbo.dao.n.AreaDAO;
import mx.com.ferbo.dao.n.BiometricoDAO;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.EmpleadoFotoDAO;
import mx.com.ferbo.dao.n.EmpresaDAO;
import mx.com.ferbo.dao.n.EntidadFederativaDAO;
import mx.com.ferbo.dao.n.ParametroDAO;
import mx.com.ferbo.dao.n.PercepcionEmpleadoDAO;
import mx.com.ferbo.dao.n.PerfilDAO;
import mx.com.ferbo.dao.n.PeriodicidadPagoDAO;
import mx.com.ferbo.dao.n.PlantaDAO;
import mx.com.ferbo.dao.n.PrestamoDAO;
import mx.com.ferbo.dao.n.PuestoDAO;
import mx.com.ferbo.dao.n.RiesgoPuestoDAO;
import mx.com.ferbo.dao.n.TipoContratoDAO;
import mx.com.ferbo.dao.n.TipoJornadaDAO;
import mx.com.ferbo.dao.n.TipoPercepcionDAO;
import mx.com.ferbo.dao.n.TipoPrestamoDAO;
import mx.com.ferbo.dao.n.TipoRegimenDAO;
import mx.com.ferbo.model.CatArea;
import mx.com.ferbo.model.CatEmpresa;
import mx.com.ferbo.model.CatParametro;
import mx.com.ferbo.model.CatPerfil;
import mx.com.ferbo.model.CatPeriodicidadPago;
import mx.com.ferbo.model.CatPlanta;
import mx.com.ferbo.model.CatPuesto;
import mx.com.ferbo.model.CatTipoPrestamo;
import mx.com.ferbo.model.DetBiometrico;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetEmpleadoFoto;
import mx.com.ferbo.model.DetPercepcionEmpleado;
import mx.com.ferbo.model.DetPrestamo;
import mx.com.ferbo.model.InfDatoEmpresa;
import mx.com.ferbo.model.sat.CatEntidadFederativa;
import mx.com.ferbo.model.sat.CatRiesgoPuesto;
import mx.com.ferbo.model.sat.CatTipoContrato;
import mx.com.ferbo.model.sat.CatTipoJornada;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import mx.com.ferbo.model.sat.CatTipoRegimen;
import mx.com.ferbo.util.SGPException;

@Named(value = "registroEmpleadosBean")
@ViewScoped
public class RegistroEmpleadosBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(RegistroEmpleadosBean.class);

    private EmpresaDAO empresaDAO;
    private EmpleadoFotoDAO empleadoFotoDAO;
    private PrestamoDAO prestamoDAO;
    private TipoPrestamoDAO tipoPrestamoDAO;
    private PerfilDAO perfilDAO;
    private PlantaDAO plantaDAO;
    private PuestoDAO puestoDAO;
    private AreaDAO areaDAO;
    private EmpleadoDAO empleadoDAO;
    private PercepcionEmpleadoDAO percepcionEmpleadoDAO;
    private List<DetPercepcionEmpleado> percepcionesEmpleado;
    private InfDatoEmpresa datoEmpresa;
    private BiometricoDAO biometricoDAO;
    private List<CatTipoContrato> tiposContrato;
    private TipoContratoDAO tipoContratoDAO;
    private List<CatTipoJornada> tiposJornada;
    private TipoJornadaDAO tipoJornadaDAO;
    private List<CatTipoRegimen> tiposRegimen;
    private TipoRegimenDAO tipoRegimenDAO;
    private ParametroDAO parametroDAO;
    private EntidadFederativaDAO entidadDAO;
    private RiesgoPuestoDAO riesgoDAO;
    private PeriodicidadPagoDAO periodicidadDAO;
    private TipoPercepcionDAO tipoPercepcionDAO;

    private List<DetEmpleado> lstEmpleados;
    private List<DetEmpleado> lstEmpleadosSelected;
    private List<CatEmpresa> lstCatEmpresa;
    private List<CatPerfil> lstCatPerfil;
    private List<CatPlanta> lstCatPlanta;
    private List<CatPuesto> lstCatPuesto;
    private List<CatArea> lstCatArea;
    private List<CatEntidadFederativa> entidadesFederativas;
    private List<CatRiesgoPuesto> riesgosPuesto;
    private List<CatPeriodicidadPago> periodicidadesPago;
    private List<CatTipoPercepcion> tiposPercepcion;
    private List<CatTipoPrestamo> tiposPrestamo;

    private DetEmpleado empleadoSelected;
    private DetBiometrico detBiometrico;
    private DetEmpleadoFoto empleadoFoto;
    private DetPercepcionEmpleado percepcionEmpleado;
    private String biometrico;
    private int numBiometrico;
    private DetPrestamo prestamo;
    
    private String curp;
    private String rfc;
    private String nss;
    
    public RegistroEmpleadosBean() {
    	empleadoFotoDAO = new EmpleadoFotoDAO(DetEmpleadoFoto.class);
    	prestamoDAO = new PrestamoDAO();
    	percepcionEmpleadoDAO = new PercepcionEmpleadoDAO();
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
        entidadDAO = new EntidadFederativaDAO(CatEntidadFederativa.class);
        riesgoDAO = new RiesgoPuestoDAO(CatRiesgoPuesto.class);
        periodicidadDAO = new PeriodicidadPagoDAO(CatPeriodicidadPago.class);
        tipoPercepcionDAO = new TipoPercepcionDAO();
        tipoPrestamoDAO = new TipoPrestamoDAO();
        
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
            entidadesFederativas = entidadDAO.buscarTodos();
            riesgosPuesto = riesgoDAO.buscarTodos();
            periodicidadesPago = periodicidadDAO.buscarActivos(new Date());
            tiposPercepcion = tipoPercepcionDAO.buscarTodos();
            tiposPrestamo = tipoPrestamoDAO.buscarTodos();
            
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
    	List<DetPrestamo> prestamos = null;
    	log.info("Cargando información del empleado: {}", this.empleadoSelected);
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
    	
    	percepcionesEmpleado = percepcionEmpleadoDAO.buscarPorEmpleado(this.empleadoSelected.getIdEmpleado());
    	prestamos = prestamoDAO.buscar(this.empleadoSelected.getIdEmpleado());
    	this.empleadoSelected.setPrestamos(prestamos); 
    	this.prestamo = new DetPrestamo();
    	
    	log.info("Empleado seleccionado: {}", this.empleadoSelected.getIdEmpleado());
    	if(empleadoFoto != null)
    		log.debug("Foto: {}", empleadoFoto.getFotografia());
    	
    	this.detBiometrico = biometricoDAO.consultaBiometricoByIdEmpleado(this.empleadoSelected.getIdEmpleado());
    	
		log.info("Biometrico: {}", this.detBiometrico);
		
		this.nuevaPercepcionEmpleado();
    	
    	PrimeFaces.current().ajax().update("formRegistroEmpleado:panelDialogFoto");
    }
    
    public void nuevaPercepcionEmpleado() {
    	this.percepcionEmpleado = new DetPercepcionEmpleado();
		this.percepcionEmpleado.setEmpleado(this.empleadoSelected);
		this.percepcionEmpleado.setActivo(true);
    }
    
    public void agregarPercepcionEmpleado() {
    	log.info("Agregando percepcion del empleado...");
    	if(this.percepcionesEmpleado == null)
    		this.percepcionesEmpleado = new ArrayList<>();
    	
    	this.percepcionEmpleado.setEmpleado(this.empleadoSelected);
    	this.percepcionesEmpleado.add(percepcionEmpleado);
    	this.nuevaPercepcionEmpleado();
    }
    
    public void eliminarPercepcionEmpleado() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Percepción del empleado";
		
		try {
			if(this.percepcionEmpleado == null)
				throw new SGPException("Debe indicar una percepción");
			
			this.percepcionesEmpleado.remove(this.percepcionEmpleado);
			this.percepcionEmpleado = new DetPercepcionEmpleado();
			
			mensaje = "Percepción eliminada correctamente.";
    		severity = FacesMessage.SEVERITY_INFO;
		} catch(SGPException ex) {
    		mensaje = ex.getMessage();
    		severity = FacesMessage.SEVERITY_WARN;
    	} catch(Exception ex) {
    		log.error("Problema para guardar información del empleado...", ex);
    		mensaje = "Problema para guardar al empleado.";
    		severity = FacesMessage.SEVERITY_ERROR;
    	} finally {
    		message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update("formRegistroEmpleado:messages", "formRegistroEmpleado:panelDialogEmpleado:dt-percepcion-empleado");
    	}
    }
    
    public void agregarPrestamo() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Préstamo";
		
		try {
			if(this.prestamo == null)
				throw new SGPException("Debe indicar un préstamo");
			
			if(this.prestamo.getTipoPrestamo() == null)
				throw new SGPException("Debe indicar el tipo de préstamo");
			
			if(this.prestamo.getFechaInicio() == null)
				throw new SGPException("Debe indicar la fecha de inicio del préstamo.");
			
			if(this.prestamo.getFechaFin() == null)
				throw new SGPException("Debe indicar la fecha de fin del préstamo.");
			
			if(this.prestamo.getAcumulado() == null)
				throw new SGPException("Debe indicar el acumulado del préstamo.");
			
			if(this.prestamo.getImporte() == null)
				throw new SGPException("Debe indicar el importe del préstamo.");
			
			if(this.prestamo.getTotal() == null)
				throw new SGPException("Debe indicar el total del préstamo.");
			
			if(this.prestamo.getPeriodicidadPago() == null)
				throw new SGPException("Debe indicar la periodicidad de pago.");
			
			if(this.empleadoSelected.getPrestamos() == null)
				this.empleadoSelected.setPrestamos(new ArrayList<>());
			
			prestamo.setEmpleado(this.empleadoSelected);
			
			this.empleadoSelected.getPrestamos().add(prestamo);
			
			prestamo = new DetPrestamo();
			PrimeFaces.current().executeScript("PF('dgPrestamo').hide()");
			
			mensaje = "Préstamo agregado correctamente.";
    		severity = FacesMessage.SEVERITY_INFO;
		} catch(SGPException ex) {
    		mensaje = ex.getMessage();
    		severity = FacesMessage.SEVERITY_WARN;
    	} catch(Exception ex) {
    		log.error("Problema para guardar información del empleado...", ex);
    		mensaje = "Problema para guardar al empleado.";
    		severity = FacesMessage.SEVERITY_ERROR;
    	} finally {
    		message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update("formRegistroEmpleado:messages", "formRegistroEmpleado:panelDialogEmpleado:dt-prestamos");
    	}
    }
    
    public void eliminarPrestamo(DetPrestamo prestamo) {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Préstamo";
		
		try {
			if(this.prestamo == null)
				throw new SGPException("Debe indicar un préstamo");
			
			this.empleadoSelected.getPrestamos().remove(this.prestamo);
			
			prestamo = new DetPrestamo();
			PrimeFaces.current().executeScript("PF('dgPrestamo').hide()");
			
			mensaje = "Préstamo eliminado correctamente.";
    		severity = FacesMessage.SEVERITY_INFO;
		} catch(SGPException ex) {
    		mensaje = ex.getMessage();
    		severity = FacesMessage.SEVERITY_WARN;
    	} catch(Exception ex) {
    		log.error("Problema para guardar información del empleado...", ex);
    		mensaje = "Problema para guardar al empleado.";
    		severity = FacesMessage.SEVERITY_ERROR;
    	} finally {
    		message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
	        PrimeFaces.current().ajax().update("formRegistroEmpleado:messages", "formRegistroEmpleado:panelDialogEmpleado:dt-prestamos");
    	}
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
    		this.empleadoSelected.setPercepcionesEmpleado(this.percepcionesEmpleado);
    		
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
    			detBiometrico.setIdEmpleado(empleadoSelected);
    			if (detBiometrico.getIdBiometrico() == null) {
    				biometricoDAO.guardar(detBiometrico);
    			} else {
    				biometricoDAO.actualizar(detBiometrico);
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
    		log.error("Problema para guardar información del empleado...", ex);
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
        	this.empleadoSelected.setActivo((short)0);
        	this.empleadoSelected.setFechaModificacion(new Date());
        	empleadoDAO.actualizar(empleadoSelected);
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

	public List<CatEntidadFederativa> getEntidadesFederativas() {
		return entidadesFederativas;
	}

	public void setEntidadesFederativas(List<CatEntidadFederativa> entidadesFederativas) {
		this.entidadesFederativas = entidadesFederativas;
	}

	public List<CatRiesgoPuesto> getRiesgosPuesto() {
		return riesgosPuesto;
	}

	public void setRiesgosPuesto(List<CatRiesgoPuesto> riesgosPuesto) {
		this.riesgosPuesto = riesgosPuesto;
	}

	public List<CatPeriodicidadPago> getPeriodicidadesPago() {
		return periodicidadesPago;
	}

	public void setPeriodicidadesPago(List<CatPeriodicidadPago> periodicidadesPago) {
		this.periodicidadesPago = periodicidadesPago;
	}

	public List<DetPercepcionEmpleado> getPercepcionesEmpleado() {
		return percepcionesEmpleado;
	}

	public void setPercepcionesEmpleado(List<DetPercepcionEmpleado> percepcionesEmpleado) {
		this.percepcionesEmpleado = percepcionesEmpleado;
	}

	public List<CatTipoPercepcion> getTiposPercepcion() {
		return tiposPercepcion;
	}

	public void setTiposPercepcion(List<CatTipoPercepcion> tiposPercepcion) {
		this.tiposPercepcion = tiposPercepcion;
	}

	public DetPercepcionEmpleado getPercepcionEmpleado() {
		return percepcionEmpleado;
	}

	public void setPercepcionEmpleado(DetPercepcionEmpleado percepcionEmpleado) {
		this.percepcionEmpleado = percepcionEmpleado;
	}

	public List<CatTipoPrestamo> getTiposPrestamo() {
		return tiposPrestamo;
	}

	public void setTiposPrestamo(List<CatTipoPrestamo> tiposPrestamo) {
		this.tiposPrestamo = tiposPrestamo;
	}

	public DetPrestamo getPrestamo() {
		return prestamo;
	}

	public void setPrestamo(DetPrestamo prestamo) {
		this.prestamo = prestamo;
	}
}
