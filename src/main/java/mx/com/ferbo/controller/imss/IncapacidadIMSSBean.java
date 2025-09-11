package mx.com.ferbo.controller.imss;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.business.dianolaboral.DiasDeDescansoObligatorioBL;
import mx.com.ferbo.business.incapacidad.IncapacidadBL;
import mx.com.ferbo.business.incidencia.SolicitudPermisoBL;
import mx.com.ferbo.business.registro.RegistroBL;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.EstatusIncapacidadDAO;
import mx.com.ferbo.dao.n.imss.ControlIncapacidadIMSSDAO;
import mx.com.ferbo.dao.n.imss.IncapacidadIMSSDAO;
import mx.com.ferbo.dao.n.imss.RiesgoTrabajoIMSSDAO;
import mx.com.ferbo.dao.n.imss.TipoIncapacidadIMSSDAO;
import mx.com.ferbo.dao.n.imss.TipoRiesgoIMSSDAO;
import mx.com.ferbo.dao.n.sat.TipoIncapacidadSATDAO;
import mx.com.ferbo.model.CatEstatusIncapacidad;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.model.InfDatoEmpresa;
import mx.com.ferbo.model.imss.CatControlIncapacidadIMSS;
import mx.com.ferbo.model.imss.CatRiesgoTrabajoIMSS;
import mx.com.ferbo.model.imss.CatTipoIncapacidadIMSS;
import mx.com.ferbo.model.imss.CatTipoRiesgoIMSS;
import mx.com.ferbo.model.imss.DetIncapacidad;
import mx.com.ferbo.model.sat.CatTipoIncapacidadSAT;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.ManageStatus;
import mx.com.ferbo.util.SGPException;

/**
 *
 * @author alberto
 */
@Named(value = "registroIncBean")
@ViewScoped
public class IncapacidadIMSSBean implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(IncapacidadIMSSBean.class);
    
    private final HttpServletRequest httpServletRequest;
    
    private IncapacidadIMSSDAO registroIncapacidadDAO;
    private EmpleadoDAO empleadoDAO;
    private TipoIncapacidadIMSSDAO tipoIncapacidadIMSSDAO;
    private ControlIncapacidadIMSSDAO controIncapacidadIMSSDAO;
    private RiesgoTrabajoIMSSDAO riesgoTrabajoIMSSDAO;
    private TipoRiesgoIMSSDAO tipoRiesgoIMSSDAO;
    private EstatusIncapacidadDAO estatusSolicitudDAO;
    private TipoIncapacidadSATDAO incapacidadSATDAO;
    
    private List<DetIncapacidad> listaRegistroIncapacidad;
    private List<DetIncapacidad> listaRegistroFiltrada;
    private List<CatTipoIncapacidadIMSS> listaIncapacidadesIMSS;
    private List<CatControlIncapacidadIMSS> listaControlIncapacidadIMSS;
    private List<CatRiesgoTrabajoIMSS> listaRiesgoTrabajo;
    private List<DetEmpleado> listaEmpleados;
    private List<CatTipoRiesgoIMSS> listaTipoRiesgoIMSS;
    
    /*Atributos para el registro de incapacidad*/
    private DetIncapacidad incapacidad;
    private CatTipoIncapacidadIMSS tipoIncapacidad;
    private CatControlIncapacidadIMSS controlIncapacidad;
    private CatRiesgoTrabajoIMSS riesgoTrabajo;
    private DetEmpleado empleadoInc;
    private DetEmpleado empleadoRev;
    private CatTipoRiesgoIMSS tipoRiesgo;
    private CatEstatusIncapacidad estatusSolicitud;
    private Date auxIncapacidadFechaFin;
    private int diasIncapacidad;
    
    private final String riesgoTrabajoSAT = "01";
    private final String accidenteTrabajo = "ATRB";
    private final String accidenteTrabajoDefuncion = "D";
    private final String estatusIncidenciaAprobada = "A";
    private final String estatusIncidenciaCancelada = "C";
    private CatEstatusIncapacidad estatusSolicitudAprobada;
    private CatEstatusIncapacidad estatusSolicitudCancelada;
    private CatRiesgoTrabajoIMSS riesgoIncapacidadDefuncion;
    private CatTipoIncapacidadSAT tipoIncapacidadSAT;
    private CatTipoIncapacidadIMSS tipoIncapacidadIMSS;
    private String accionBoton = "";
    private String iconoBoton = "";
    private String diseñoBoton = "";
    private String claveDefuncion = "";
    private int estadoAccionBoton;
    private ManageStatus status;
    
    private Date periodoInicio;
    private Date periodoFin;
    private boolean estatusAprobado;
    private boolean estatusCancelado;
    private boolean tpIncapacidadSelecciondado;
    
    public IncapacidadIMSSBean() {
        this.registroIncapacidadDAO = new IncapacidadIMSSDAO();
        this.empleadoDAO = new EmpleadoDAO();
        this.tipoIncapacidadIMSSDAO = new TipoIncapacidadIMSSDAO();
        this.controIncapacidadIMSSDAO = new ControlIncapacidadIMSSDAO();
        this.riesgoTrabajoIMSSDAO = new RiesgoTrabajoIMSSDAO();
        this.tipoRiesgoIMSSDAO = new TipoRiesgoIMSSDAO();
        this.estatusSolicitudDAO = new EstatusIncapacidadDAO();
        this.incapacidadSATDAO = new TipoIncapacidadSATDAO();
        
        this.httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.empleadoRev = (DetEmpleado) httpServletRequest.getSession(true).getAttribute("empleado");
        
		this.periodoFin = new Date();
		this.periodoInicio = DateUtil.getFirstDayOfyear(this.periodoFin);
		DateUtil.setTime(this.periodoInicio, 0, 0, 0);
		this.validarFechaInicioFin();
		
		this.actualizarListaRegistroIncapacidades();
		
		
		
		this.listaIncapacidadesIMSS = this.tipoIncapacidadIMSSDAO.buscarTodos();
		this.listaControlIncapacidadIMSS = this.controIncapacidadIMSSDAO.buscarTodos();
		this.listaRiesgoTrabajo = this.riesgoTrabajoIMSSDAO.buscarTodos();
		this.listaEmpleados = this.empleadoDAO.buscarTodosActivos((short) 1);
		this.listaTipoRiesgoIMSS = this.tipoRiesgoIMSSDAO.buscarTodos();

		this.status = new ManageStatus();

		
		this.estatusAprobado = false;
		this.estatusCancelado = false;
		this.estatusSolicitudAprobada = this.estatusSolicitudDAO.buscarPorClave(this.estatusIncidenciaAprobada);
		this.estatusSolicitudCancelada = this.estatusSolicitudDAO.buscarPorClave(this.estatusIncidenciaCancelada);
		this.riesgoIncapacidadDefuncion = this.riesgoTrabajoIMSSDAO.buscarPorClave(this.accidenteTrabajoDefuncion);
		this.tipoIncapacidadSAT = this.incapacidadSATDAO.buscarPorId(this.riesgoTrabajoSAT);
		this.tipoIncapacidadIMSS = this.tipoIncapacidadIMSSDAO.buscarPorClave(this.accidenteTrabajo);        
        
        
        
    }
    
    @PostConstruct
	public void init() {

	}
    
	public void actualizarListaRegistroIncapacidades() {
		this.listaRegistroIncapacidad = registroIncapacidadDAO.buscarPorPeriodo(this.periodoInicio, this.periodoFin);
	}
    
	public void validarFechaInicioFin() {
		if (this.periodoFin.equals(this.periodoInicio)) {
			this.periodoInicio = DateUtil.addYear(this.periodoFin, -1);
		}
	}
    
	public List<DetIncapacidad> consultarIncapacidades() {
		List<DetIncapacidad> listaPeriodo = new ArrayList<DetIncapacidad>();
		List<DetIncapacidad> listAprobado = null;
		List<DetIncapacidad> listCancelado = null;
		
		if(this.estatusAprobado) {
			listAprobado = this.listaRegistroIncapacidad.stream()
					.filter(i -> i.getEstatusSolicitud().getClave().equalsIgnoreCase("A"))
					.collect(Collectors.toList())
					;
			
			listaPeriodo.addAll(listAprobado);
		}
		
		if(this.estatusCancelado) {
			listCancelado = this.listaRegistroIncapacidad.stream()
					.filter(i -> i.getEstatusSolicitud().getClave().equalsIgnoreCase("C"))
					.collect(Collectors.toList())
					;
			listaPeriodo.addAll(listCancelado);
		}

		return listaPeriodo;
	}
    
    public void inicializaIncapacidad() {
    	this.incapacidad = IncapacidadBL.create();
    }
    
    public void incializarSolicitud() {
        log.info("Iniciando un registro de incapacidad");
        this.inicializaIncapacidad();
        this.accionBoton = "Guardar";
        this.iconoBoton = "pi pi-save";
        this.diseñoBoton = "ui-button-success";
        this.estadoAccionBoton = 1;
        this.tpIncapacidadSelecciondado = false;
         
        PrimeFaces.current().ajax().update("form:pnlRegistoIncapacidad", "form:dialogoRegistoIncapacidad");
    }
    
    public void editarSolicitud(DetIncapacidad aux)
    {
        log.info("Editando un registro de incapacidad: {}", aux.toString());
        this.tipoIncapacidad = aux.getTipoIncapacidad();
        this.controlIncapacidad = aux.getControlIncapacidad();
        
        if(this.tipoIncapacidad.getIncapacidadSAT().getClave().matches(this.riesgoTrabajoSAT))
        {
            this.riesgoTrabajo = aux.getSecuelaRiesgoTrabajo();
            this.tipoRiesgo = aux.getTipoRiesgo();
            this.claveDefuncion = this.deshabilitarCamposPorDefuncion(this.riesgoTrabajo.getDescripcion());
        }else
        {
            this.riesgoTrabajo = new CatRiesgoTrabajoIMSS();
            this.tipoRiesgo = new CatTipoRiesgoIMSS();
        }
        
        this.empleadoInc = aux.getEmpleado();
//        this.auxIncapacidadFechaInicial = aux.getFechaInicio();
        this.auxIncapacidadFechaFin = aux.getFechaFin();
        this.diasIncapacidad = aux.getDiasAutorizados();
        this.accionBoton = "Actualizar";
        this.iconoBoton = "pi pi-check";
        this.diseñoBoton = "ui-button-info";
        this.estadoAccionBoton = 2;
        this.tpIncapacidadSelecciondado = deshabilitarTPIncapacidad(this.tipoIncapacidad);
        
        PrimeFaces.current().ajax().update("form:pnlRegistoIncapacidad", "form:dialogoRegistoIncapacidad");
    }
    
	public void cancelarSolicitud(DetIncapacidad incapacidad) throws SGPException {
		FacesMessage message = null;
		FacesMessage.Severity severity = null;
		String mensaje = null;
		String titulo = "Incapacidad";

		try {
			this.incapacidad = registroIncapacidadDAO.cargar(incapacidad.getIdIncapacidad())
					.orElseThrow(() -> new SGPException("No se encontró información del registro."));

			log.info("Cancelar el registro de incapacidad: {}", incapacidad.toString());

			if (riesgoTrabajoSAT
					.equalsIgnoreCase(this.incapacidad.getTipoIncapacidad().getIncapacidadSAT().getClave())) {
				this.riesgoTrabajo = incapacidad.getSecuelaRiesgoTrabajo();
				this.tipoRiesgo = incapacidad.getTipoRiesgo();
				this.claveDefuncion = this.deshabilitarCamposPorDefuncion(this.riesgoTrabajo.getDescripcion());
				this.tpIncapacidadSelecciondado = true;
			} else {
				this.riesgoTrabajo = new CatRiesgoTrabajoIMSS();
				this.tipoRiesgo = new CatTipoRiesgoIMSS();
			}

			this.tpIncapacidadSelecciondado = deshabilitarTPIncapacidad(this.incapacidad.getTipoIncapacidad());
			
			mensaje = "Cancelar";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (SGPException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
			log.warn("Error al registrar la incapacidad: ", ex);
		} catch (Exception e) {
			mensaje = "Consulte al administrador de sistemas";
			severity = FacesMessage.SEVERITY_ERROR;
			log.error("ERROR: ", e);
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:pnlRegistoIncapacidad",
					"form:dialogoRegistoIncapacidad");
		}
	}
    
    public void guardar() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Incapacidad";
        boolean riesgoTrabajoDefuncion = false;
        
        try {
            if(this.incapacidad.getEmpleado() == null)
                throw new SGPException("Debes seleccionar al empleado");
            
            if(this.incapacidad.getTipoIncapacidad() == null)
                throw new SGPException("Debes seleccionar el tipo de incapacidad");
            
            if(this.incapacidad.getControlIncapacidad() == null)
                throw new SGPException("Debes seleccionar el control de incapacidad");
            
            if(this.incapacidad.getSecuelaRiesgoTrabajo() == null && this.incapacidad.getTipoIncapacidad().getIncapacidadSAT().getClave().matches("01"))
                throw new SGPException("Debes seleccionar un riesgo de trabajo");
            
            if(this.incapacidad.getTipoRiesgo() == null && this.incapacidad.getTipoIncapacidad().getIncapacidadSAT().getClave().matches("01"))
                throw new SGPException("Debes seleccionar un tipo de riesgo de trabajo");
            
            if(this.incapacidad.getDiasAutorizados() == null)
                throw new SGPException("Debes ingresar los dias autorizados");
            
            if(this.incapacidad.getFechaInicio() == null)
                throw new SGPException("Debes ingresar la fecha de inicio");
            
            if(this.incapacidad.getDescripcion() == null)
                throw new SGPException("Debes ingresa la descripción");
            
            InfDatoEmpresa datoEmpresaEmpleado = this.incapacidad.getEmpleado().getDatoEmpresa();
            
            if(datoEmpresaEmpleado == null)
                throw new SGPException("El empleado no tiene información empresarial, consulta con RH");
            
            //Verifica si el folio de la incapacidad ya se encuentra registrado en otra incapacidad previa
            this.validarFolioIMSS(this.incapacidad);
            
            this.incapacidad.setAutorizador(this.empleadoRev);
            
            if(this.incapacidad.getDiasAutorizados() == 0) {
                this.incapacidad.setFechaFin(this.incapacidad.getFechaInicio());
            } else {
                Date fechaFin = DateUtil.agregaFechaFin(this.incapacidad.getFechaInicio(), this.incapacidad.getDiasAutorizados());
                this.incapacidad.setFechaFin(fechaFin);
            }
            
            Date fechaCaptura = new Date();
            this.incapacidad.setFechaCaptura(fechaCaptura);
            this.estatusSolicitud = this.estatusSolicitudDAO.buscarPorClave(this.estatusIncidenciaAprobada);
            this.incapacidad.setEstatusSolicitud(this.estatusSolicitud);
            
            //Valida la duracion maxima de una incapacidad
            this.validarMaxTiempoIncapacidad(this.incapacidad);
            
            //Valida si es una defuncion, si ya existe un registro de 
            //incapacidad - permiso - vacaciones en el mismo periodo de fechas
            riesgoTrabajoDefuncion = this.validarSolicitudIncapacidad(this.incapacidad);
            
            if(riesgoTrabajoDefuncion == false)
                //Guarda el registro de incapacidad en asistencia
                RegistroBL.guardarRegistroIncapacidad(this.incapacidad, this.incapacidad.getFechaInicio(), this.incapacidad.getFechaFin(), DiasDeDescansoObligatorioBL.diasDeAsueto());
            
            this.registroIncapacidadDAO.guardar(this.incapacidad);
            
            actualizarListaRegistroIncapacidades();
            
            PrimeFaces.current().executeScript("PF('dialogoRegistoIncapacidad').hide()");
            PrimeFaces.current().ajax().update("form:dtRegistroIncapacidad");
            
            log.info("La incapacidad se guardo correctamente");
            mensaje = "Se guardo correctamente la incapacidad";
            severity = FacesMessage.SEVERITY_INFO;
        } catch(SGPException ex) {
            titulo = "Error";
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("Error al registrar la incapacidad: ", ex);
        } catch (Exception e) {
            titulo = "Error";
            mensaje = "Consulte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
            log.info("ERROR: ", e);
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void actualizarRegistroIncapacidad() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Incapacidad";
        
        try {
            if(this.riesgoTrabajo == null && this.tipoIncapacidad.getIncapacidadSAT().equals(this.tipoIncapacidadSAT))
                throw new SGPException("Debes seleccionar un riesgo de trabajo");
            
            if(this.tipoIncapacidad.getIdTpIncapacidad() == null)
                throw new SGPException("Debes seleccionar el tipo de incapacidad");
            
            if(this.controlIncapacidad.getIdControlIncapacidad() == null)
                throw new SGPException("Debes seleccionar el control de incapacidad");
            
            if(this.riesgoTrabajo == null && this.tipoIncapacidad.getIncapacidadSAT().equals(this.tipoIncapacidadSAT))
                throw new SGPException("Debes seleccionar un riesgo de trabajo");
            
            this.incapacidad.setEmpleado(this.empleadoInc);
            this.incapacidad.setTipoIncapacidad(this.tipoIncapacidad);
            this.incapacidad.setControlIncapacidad(this.controlIncapacidad);
            
            if(this.tipoIncapacidad.getIncapacidadSAT().getClave().matches(riesgoTrabajoSAT)) {
                this.incapacidad.setSecuelaRiesgoTrabajo(this.riesgoTrabajo);
                this.incapacidad.setTipoRiesgo(this.tipoRiesgo);
            } else {
                this.incapacidad.setSecuelaRiesgoTrabajo(null);
                this.incapacidad.setTipoRiesgo(null);
            }
            
            this.incapacidad.setAutorizador(this.empleadoRev);
            
            if(this.incapacidad.getTipoIncapacidad().getClave().matches("ENFG") && this.incapacidad.getTipoIncapacidad().getIncapacidadSAT().getClave().matches("02")) {
                Date fechaFin = DateUtil.agregaFechaFin(this.incapacidad.getFechaInicio(), this.incapacidad.getDiasAutorizados());
                this.incapacidad.setFechaFin(fechaFin);
                this.actualizarFechasIncapacidad(this.incapacidad, this.auxIncapacidadFechaFin, this.diasIncapacidad);
            }
            
            this.registroIncapacidadDAO.actualizar(this.incapacidad);
            
            log.info("La incapacidad se actualizo correctamente");
            mensaje = "Se actualizo correctamente la incapacidad";
            severity = FacesMessage.SEVERITY_INFO;
        } catch(SGPException ex) {
            titulo = "Error";
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
            log.error("Error al actualizar la incapacidad: ", ex);
        } catch (Exception e) {
            titulo = "Error";
            mensaje = "Consulte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
            log.error("ERROR: {}", e);
        } finally {
            actualizarListaRegistroIncapacidades();
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().executeScript("PF('dialogoRegistoIncapacidad').hide()");
            PrimeFaces.current().ajax().update("form:messages","form:dtRegistroIncapacidad");
        }
    }
    
	public void cancelar() {
		FacesMessage message = null;
		FacesMessage.Severity severity = null;
		String mensaje = null;
		String titulo = "Incapacidad";
		try {
			if (this.riesgoTrabajo == null
					&& this.tipoIncapacidad.getIncapacidadSAT().getClave().matches(riesgoTrabajoSAT))
				throw new SGPException("Debes seleccionar un riesgo de trabajo");

			this.estatusSolicitud = this.estatusSolicitudDAO.buscarPorClave(this.estatusIncidenciaCancelada);
			this.incapacidad.setEstatusSolicitud(this.estatusSolicitud);
			this.incapacidad.setAutorizador(this.empleadoRev);

			// Obtiene los registros de incapacidad que estan en asistencia(det_registro)
//			String clave = "I";
//			List<DetRegistro> listaRegistroIncapacidades = RegistroBL.obtenerRegistroAsistencia(
//					this.incapacidad.getEmpleado(), this.incapacidad.getFechaInicio(), this.incapacidad.getFechaFin(),
//					clave);
			
//			List<DetRegistro> listaRegistroIncapacidades = this.incapacidad.getRegistrosIncapacidad()
//					.stream()
//					.map( item -> item.getRegistro())
//					.collect(Collectors.toList());

			// Elimina registros de incapacidad en asistencia
			
			this.incapacidad.getRegistrosIncapacidad().stream().forEach(item -> {
				if("I".equalsIgnoreCase(item.getRegistro().getStatus().getCodigo())) {
					item.setRegistro(null);
				}
			});
			
//			if (listaRegistroIncapacidades.isEmpty() == false) {
//				int registrosBorrados = RegistroBL.cancelarRegistroAsistencia(listaRegistroIncapacidades);
//				log.info("Registros eliminados del empleado {} en asistencia: {}",
//						this.incapacidad.getEmpleado().getIdEmpleado(), registrosBorrados);
//			}

			this.registroIncapacidadDAO.actualizar(this.incapacidad);

			log.info("La incapacidad se cancelo correctamente");
			mensaje = "Se cancelo correctamente la incapacidad";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (SGPException ex) {
			titulo = "Error";
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_ERROR;
			log.info("Error al cancelar la incapacidad: ", ex);
		} catch (Exception e) {
			titulo = "Error";
			mensaje = "Consulte al administrador de sistemas";
			severity = FacesMessage.SEVERITY_ERROR;
			log.info("ERROR, {}", e);
		} finally {
			actualizarListaRegistroIncapacidades();
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().executeScript("PF('dialogoRegistoIncapacidad').hide()");
			PrimeFaces.current().ajax().update("form:messages", "form:dtRegistroIncapacidad");
		}
	}
    
    //Valida si ya existe un registro de incapacidad, con orden de prioridad con defuncion
    //y despues las demas incapacidades
    public void validarRegistroIncapacidad(DetIncapacidad incapacidad) throws SGPException
    {
        log.info("Entrando a validar incapacidades");
        DetEmpleado empleado = incapacidad.getEmpleado();
        Date fechaInicio = incapacidad.getFechaInicio();
        Date fechaFinal = incapacidad.getFechaFin();
        String claveEstatus = this.estatusIncidenciaAprobada;
        
        List<DetIncapacidad> auxRegistroIncapacidad = this.registroIncapacidadDAO.buscarPorPeriodo(empleado.getIdEmpleado(), fechaInicio, fechaFinal, claveEstatus);
        
        if(auxRegistroIncapacidad.isEmpty())
        {
            log.info("No se encontraron registros de incapacidades");
        }else
        {
            for(DetIncapacidad auxIncapacidad : auxRegistroIncapacidad)
            {
                boolean auxRegDefuncion = this.validaRegistroEmpleadoDefuncion(auxIncapacidad);
                if(auxIncapacidad.getSecuelaRiesgoTrabajo() != null && auxIncapacidad.getSecuelaRiesgoTrabajo().getClave().trim().matches("D") && auxRegDefuncion == true)
                {
                    throw new SGPException("Ya tiene un registro de defuncion");
                }
            }

            if(!auxRegistroIncapacidad.isEmpty())
            {
                log.trace("Registros de incapacidades del empleado {} encontradas: {}", empleadoInc.getIdEmpleado(), auxRegistroIncapacidad.toString());
                throw new SGPException("El empleado ya cuenta con un registro de incapacidad");
            }
        }
    }
    
    //Valida si el empleado tiene los atributos de baja
    public boolean validaRegistroEmpleadoDefuncion(DetIncapacidad auxIncapacidad)
    {
        boolean regDefuncion = false;
        DetEmpleado empleado = auxIncapacidad.getEmpleado();
        InfDatoEmpresa empleadoEmpresa= empleado.getDatoEmpresa();
        
        if(empleado.getActivo() == 0 && empleadoEmpresa.getFechaBaja() != null)
        {
            regDefuncion = true;
        }
        
        return regDefuncion;
    }
    
    //Valida si la solicitud es del tipo defuncion y modifica el 
    //registro del empleado y de asistencia de la anterior incapacidad
    public boolean validarSolicitudIncapacidad(DetIncapacidad incapacidad) throws SGPException
    {
        boolean regDefuncion = false;
        CatControlIncapacidadIMSS conIncapacidad = incapacidad.getControlIncapacidad();
        CatRiesgoTrabajoIMSS secuelaRiesgoTrabajo = incapacidad.getSecuelaRiesgoTrabajo(); 
        String descripcion = "";
        String clave = "";
        
        if(conIncapacidad != null)
        {
            descripcion = conIncapacidad.getDescripcion();
            clave = conIncapacidad.getClave();
        }
        
        if(secuelaRiesgoTrabajo != null)
        {
            descripcion = secuelaRiesgoTrabajo.getDescripcion();
            clave = secuelaRiesgoTrabajo.getClave();
        }
        
        //if((secuelaRiesgoTrabajo != null && secuelaRiesgoTrabajo.getClave().matches(this.riesgoIncapacidadDefuncion.getClave())) || conIncapacidad.getClave().trim().matches("D"))
        if(clave.trim().matches("D"))
        {
            //En caso de incapacidad del tipo defuncion, se da de baja al empleado
            String descripcionDefuncion = this.riesgoIncapacidadDefuncion.getDescripcion();
            log.info("Descripcion del tipo baja defuncion: {}", descripcionDefuncion);
            this.modificarRegistroIncapacidad(incapacidad, descripcion);
            regDefuncion = true;
        }else
        {
            //Valida si tiene registro de incapacidad
            this.validarRegistroIncapacidad(this.incapacidad);

            //Valida si hay una solicitud de permiso y/o vacaciones ya registrada en el periodo de la incapacidad
            SolicitudPermisoBL.validarPeriodoSolicitudPermiso(this.incapacidad);
            regDefuncion = false;
        }
        
        return regDefuncion;
    }
    
    //Al tener un registro de incapacidad del tipo defuncion dentro del periodo de una 
    //incapacidad anterior, se elimina el registro parcial en asistencia
    public void modificarRegistroIncapacidad(DetIncapacidad incapacidad, String descripcion) throws SGPException
    {
        DetEmpleado empleado = incapacidad.getEmpleado();
        Date fechaInicial = incapacidad.getFechaInicio();
        
        log.info("Baja del empleado {} en la fecha {}", empleado.getIdEmpleado(), fechaInicial, descripcion);
        DetIncapacidad auxRegistroIncapacidad = this.registroIncapacidadDAO.buscarPorEmpleadoUltimoPeriodo(empleado.getIdEmpleado(), fechaInicial);
        
        if(auxRegistroIncapacidad != null)
        {
            log.trace("Registro de incapacidad encontrado: {}", auxRegistroIncapacidad.toString());
            log.trace("Fecha original: {}", fechaInicial);
            Date fechaIni = DateUtil.moverFechaUnDiaAdelante(fechaInicial);
            log.trace("Fecha adelantada: {}", fechaIni);
            
            String clave = "I";
            List<DetRegistro> listaRegistroIncapacidades = RegistroBL.obtenerRegistroAsistencia(empleado, fechaIni, auxRegistroIncapacidad.getFechaFin(), clave);
            int numeroRegistrosIncapacidades = listaRegistroIncapacidades.size();

            if(numeroRegistrosIncapacidades > 0)
            {
                int registrosBorrados = RegistroBL.cancelarRegistroAsistencia(listaRegistroIncapacidades);
                log.info("Registros eliminados de asistencia: {}", registrosBorrados);
            }
            
        }
        
        this.actualizarRegistroEmpleado(empleado, fechaInicial, descripcion);
    }
    
    //Actualiza los datos del empleado en caso de ser incapacidad por defuncion
    public void actualizarRegistroEmpleado(DetEmpleado empleado, Date fecha, String descripcion) throws SGPException
    {
        try
        {
            InfDatoEmpresa empleadoEmpresa = empleado.getDatoEmpresa();
            empleadoEmpresa.setFechaBaja(fecha);
            empleadoEmpresa.setMotivobaja(descripcion);
            empleado.setDatoEmpresa(empleadoEmpresa);
            empleado.setActivo((short)0);
            this.empleadoDAO.actualizar(empleado);
        }catch(SGPException ex)
        {
            log.info("Error al actualizar al empleado: {}", ex);
            throw new SGPException("No se actualizo el empleado");
        }
    }
    
    public void actualizarFechasIncapacidad(DetIncapacidad incapacidad, Date auxiliarFecha, int primerosDiasIncapacidad) throws SGPException
    {
        log.info("Actualizando fechas de la incapacidad: {}", incapacidad.toString());
        DetEmpleado empleadoIncapacidad = incapacidad.getEmpleado();
        Date fechaFin = incapacidad.getFechaFin();
        //this.lstAuxRegistros = this.obtenerRegistroAsistencia(empleadoInc, auxIncapacidadFechaInicial, auxIncapacidadFechaFin);
        
        if(primerosDiasIncapacidad < incapacidad.getDiasAutorizados())
        {
            Date auxFecha = DateUtil.moverFechaUnDiaAdelante(auxiliarFecha);
            log.info("Fecha inicial: {} y Fecha final: {}", auxFecha, fechaFin);
            RegistroBL.guardarRegistroIncapacidad(null, auxFecha, fechaFin, DiasDeDescansoObligatorioBL.diasDeAsueto());
        }
        
        if(incapacidad.getDiasAutorizados() < primerosDiasIncapacidad)
        {
            Date auxFecha = DateUtil.moverFechaUnDiaAdelante(fechaFin);
            log.info("Fecha inicial: {} y Fecha final: {}", auxFecha, auxiliarFecha);
            //Obtiene los registros de incapacidad que estan en asistencia(det_registro)
            String clave = "I";
            List<DetRegistro> listaRegistroIncapacidades = RegistroBL.obtenerRegistroAsistencia(empleadoIncapacidad, auxFecha, auxiliarFecha, clave);
            
            //Elimina registros de incapacidad en asistencia
            if(!listaRegistroIncapacidades.isEmpty())
            {
                int registrosBorrados = RegistroBL.cancelarRegistroAsistencia(listaRegistroIncapacidades);
                log.info("Registros eliminados de asistencia: {}", registrosBorrados);
            }
        }
        
    }
    
    public void validarFolioIMSS(DetIncapacidad registroIncapacidad) throws SGPException
    {
        boolean existe = this.listaRegistroIncapacidad.stream()
            .anyMatch(aux -> registroIncapacidad.getFolio()
            .equalsIgnoreCase(aux.getFolio()));

        if (existe){
            throw new SGPException("Ya se encuentra el folio registrado");
        }
        
    }
    
    public void validarMaxTiempoIncapacidad(DetIncapacidad auxRegistroIncapacidad) throws SGPException
    {
        String tipoClave = incapacidad.getTipoIncapacidad().getClave().trim().toUpperCase();
        String controlClave = incapacidad.getControlIncapacidad() != null
                ? incapacidad.getControlIncapacidad().getClave().trim().toUpperCase() : "";
        int dias = incapacidad.getDiasAutorizados();
        int maxDias = incapacidad.getTipoIncapacidad().getMaxDias();

        switch (tipoClave) 
        {
            case "ATRB":
            case "ATRY":
                if (dias > maxDias) {
                    throw new SGPException("No puedes ingresar más de 364 días de incapacidad");
                }
                break;

            case "ENFG":
                if (dias > maxDias) {
                    throw new SGPException("No puedes ingresar más de 361 días de incapacidad");
                }
                break;

            case "MAT":
                switch (controlClave) {
                    case "PR":
                    case "PO":
                        if (dias > maxDias) {
                            throw new SGPException("No puedes ingresar más de 84 días de incapacidad");
                        }
                        break;
                    case "E":
                        if (dias > 21) {
                            throw new SGPException("No puedes ingresar más de 21 días de incapacidad");
                        }
                        break;
                }
                break;

            case "L140":
                if (dias > maxDias) {
                    throw new SGPException("No puedes ingresar más de 28 días de incapacidad");
                }
                break;
        }
    }
    
    public boolean deshabilitarTPIncapacidad(CatTipoIncapacidadIMSS tpIncapacidad)
    {
        if (tpIncapacidad == null || tpIncapacidad.getClave() == null) return false;
        
        String clave = tpIncapacidad.getClave().trim().toUpperCase();
        return clave.equals("ENFG");
    }
    
    public String deshabilitarCamposPorDefuncion(String descripcion)
    {
        if(descripcion == null) return "";
        
        String desc = descripcion.toUpperCase();
        return (desc.contains("DEFUNCION") || desc.contains("DEFUNCIÓN")) ? "D" : "";
    }
    
    public void metodoBoton()
    {
        switch(this.accionBoton)
        {
            case "Guardar":
                this.guardar();
                break;
            case "Actualizar":
                this.actualizarRegistroIncapacidad();
                break;
            case "Cancelar":
                this.cancelar();
                break;
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public List<DetIncapacidad> getListaRegistroIncapacidad() {
        return listaRegistroIncapacidad;
    }

    public void setListaRegistroIncapacidad(List<DetIncapacidad> listaRegistroIncapacidad) {
        this.listaRegistroIncapacidad = listaRegistroIncapacidad;
    }

    public List<DetIncapacidad> getListaRegistroFiltrada() {
        return listaRegistroFiltrada;
    }

    public void setListaRegistroFiltrada(List<DetIncapacidad> listaRegistroFiltrada) {
        this.listaRegistroFiltrada = listaRegistroFiltrada;
    }

    public List<CatTipoIncapacidadIMSS> getListaIncapacidadesIMSS() {
        return listaIncapacidadesIMSS;
    }

    public void setListaIncapacidadesIMSS(List<CatTipoIncapacidadIMSS> listaIncapacidadesIMSS) {
        this.listaIncapacidadesIMSS = listaIncapacidadesIMSS;
    }

    public List<CatControlIncapacidadIMSS> getListaControlIncapacidadIMSS() {
        return listaControlIncapacidadIMSS;
    }

    public void setListaControlIncapacidadIMSS(List<CatControlIncapacidadIMSS> listaControlIncapacidadIMSS) {
        this.listaControlIncapacidadIMSS = listaControlIncapacidadIMSS;
    }

    public List<CatRiesgoTrabajoIMSS> getListaRiesgoTrabajo() {
        return listaRiesgoTrabajo;
    }

    public void setListaRiesgoTrabajo(List<CatRiesgoTrabajoIMSS> listaRiesgoTrabajo) {
        this.listaRiesgoTrabajo = listaRiesgoTrabajo;
    }

    public List<DetEmpleado> getListaEmpleados() {
        return listaEmpleados;
    }

    public void setListaEmpleados(List<DetEmpleado> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    public DetIncapacidad getIncapacidad() {
        return incapacidad;
    }

    public void setIncapacidad(DetIncapacidad registroIncapacidad) {
        this.incapacidad = registroIncapacidad;
    }

    public CatTipoIncapacidadIMSS getTipoIncapacidad() {
        return tipoIncapacidad;
    }

    public void setTipoIncapacidad(CatTipoIncapacidadIMSS tipoIncapacidad) {
        this.tipoIncapacidad = tipoIncapacidad;
    }

    public CatControlIncapacidadIMSS getControlIncapacidad() {
        return controlIncapacidad;
    }

    public void setControlIncapacidad(CatControlIncapacidadIMSS controlIncapacidad) {
        this.controlIncapacidad = controlIncapacidad;
    }

    public CatRiesgoTrabajoIMSS getRiesgoTrabajo() {
        return riesgoTrabajo;
    }

    public void setRiesgoTrabajo(CatRiesgoTrabajoIMSS riesgoTrabajo) {
        this.riesgoTrabajo = riesgoTrabajo;
    }

    public DetEmpleado getEmpleadoRev() {
        return empleadoRev;
    }

    public void setEmpleadoRev(DetEmpleado empleadoRev) {
        this.empleadoRev = empleadoRev;
    }

    public DetEmpleado getEmpleadoInc() {
        return empleadoInc;
    }

    public void setEmpleadoInc(DetEmpleado empleadoInc) {
        this.empleadoInc = empleadoInc;
    }

    public List<CatTipoRiesgoIMSS> getListaTipoRiesgoIMSS() {
        return listaTipoRiesgoIMSS;
    }

    public void setListaTipoRiesgoIMSS(List<CatTipoRiesgoIMSS> listaTipoRiesgoIMSS) {
        this.listaTipoRiesgoIMSS = listaTipoRiesgoIMSS;
    }

    public CatTipoRiesgoIMSS getTipoRiesgo() {
        return tipoRiesgo;
    }

    public void setTipoRiesgo(CatTipoRiesgoIMSS tipoRiesgo) {
        this.tipoRiesgo = tipoRiesgo;
    }

    public CatEstatusIncapacidad getEstatusSolicitud() {
        return estatusSolicitud;
    }

    public void setEstatusSolicitud(CatEstatusIncapacidad estatusSolicitud) {
        this.estatusSolicitud = estatusSolicitud;
    }

    public CatEstatusIncapacidad getEstatusSolicitudAprobada() {
        return estatusSolicitudAprobada;
    }

    public void setEstatusSolicitudAprobada(CatEstatusIncapacidad estatusSolicitudAprobada) {
        this.estatusSolicitudAprobada = estatusSolicitudAprobada;
    }

    public CatEstatusIncapacidad getEstatusSolicitudCancelada() {
        return estatusSolicitudCancelada;
    }

    public void setEstatusSolicitudCancelada(CatEstatusIncapacidad estatusSolicitudCancelada) {
        this.estatusSolicitudCancelada = estatusSolicitudCancelada;
    }
    
    public CatRiesgoTrabajoIMSS getRiesgoIncapacidadDefuncion() {
        return riesgoIncapacidadDefuncion;
    }

    public void setRiesgoIncapacidadDefuncion(CatRiesgoTrabajoIMSS riesgoIncapacidadDefuncion) {
        this.riesgoIncapacidadDefuncion = riesgoIncapacidadDefuncion;
    }

    public CatTipoIncapacidadIMSS getTipoIncapacidadIMSS() {
        return tipoIncapacidadIMSS;
    }

    public void setTipoIncapacidadIMSS(CatTipoIncapacidadIMSS tipoIncapacidadIMSS) {
        this.tipoIncapacidadIMSS = tipoIncapacidadIMSS;
    }

    public CatTipoIncapacidadSAT getTipoIncapacidadSAT() {
        return tipoIncapacidadSAT;
    }

    public void setTipoIncapacidadSAT(CatTipoIncapacidadSAT tipoIncapacidadSAT) {
        this.tipoIncapacidadSAT = tipoIncapacidadSAT;
    }
    
    public String getAccionBoton() {
        return accionBoton;
    }

    public void setAccionBoton(String accionBoton) {
        this.accionBoton = accionBoton;
    }

    public String getIconoBoton() {
        return iconoBoton;
    }

    public void setIconoBoton(String iconoBoton) {
        this.iconoBoton = iconoBoton;
    }

    public String getDiseñoBoton() {
        return diseñoBoton;
    }

    public void setDiseñoBoton(String diseñoBoton) {
        this.diseñoBoton = diseñoBoton;
    }
    
    public int getEstadoAccionBoton() {
        return estadoAccionBoton;
    }

    public void setEstadoAccionBoton(int estadoAccionBoton) {
        this.estadoAccionBoton = estadoAccionBoton;
    }

    public String getClaveDefuncion() {
        return claveDefuncion;
    }

    public void setClaveDefuncion(String claveDefuncion) {
        this.claveDefuncion = claveDefuncion;
    }

    public ManageStatus getStatus() {
        return status;
    }
    
    public Date getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(Date periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public Date getPeriodoFin() {
        return periodoFin;
    }

    public void setPeriodoFin(Date periodoFin) {
        this.periodoFin = periodoFin;
    }
    
    public boolean isEstatusAprobado() {
        return estatusAprobado;
    }

    public void setEstatusAprobado(boolean estatusAprobado) {
        this.estatusAprobado = estatusAprobado;
    }

    public boolean isEstatusCancelado() {
        return estatusCancelado;
    }

    public void setEstatusCancelado(boolean estatusCancelado) {
        this.estatusCancelado = estatusCancelado;
    }
    
    public boolean isTpIncapacidadSelecciondado() {
        return tpIncapacidadSelecciondado;
    }

    public void setTpIncapacidadSelecciondado(boolean tpIncapacidadSelecciondado) {
        this.tpIncapacidadSelecciondado = tpIncapacidadSelecciondado;
    }
}
