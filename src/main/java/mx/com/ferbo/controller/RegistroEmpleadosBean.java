package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CaptureEvent;

import mx.com.ferbo.business.domicilio.DomicilioBL;
import mx.com.ferbo.business.empleado.EmpleadoBL;
import mx.com.ferbo.business.nomina.NominaBL;
import mx.com.ferbo.business.nomina.NominaPeriodoBL;
import mx.com.ferbo.business.nomina.NominaSemanalBL;
import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.dao.n.AreaDAO;
import mx.com.ferbo.dao.n.AsentamientoDAO;
import mx.com.ferbo.dao.n.BiometricoDAO;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.EmpleadoFotoDAO;
import mx.com.ferbo.dao.n.EmpresaDAO;
import mx.com.ferbo.dao.n.EntidadFederativaDAO;
import mx.com.ferbo.dao.n.ParametroDAO;
import mx.com.ferbo.dao.n.PercepcionDAO;
import mx.com.ferbo.dao.n.PerfilDAO;
import mx.com.ferbo.dao.n.PeriodicidadPagoDAO;
import mx.com.ferbo.dao.n.PlantaDAO;
import mx.com.ferbo.dao.n.PuestoDAO;
import mx.com.ferbo.dao.n.RiesgoPuestoDAO;
import mx.com.ferbo.dao.n.TipoBajaEmpleadoDAO;
import mx.com.ferbo.dao.n.TipoContratoDAO;
import mx.com.ferbo.dao.n.TipoJornadaDAO;
import mx.com.ferbo.dao.n.TipoPercepcionDAO;
import mx.com.ferbo.dao.n.TipoPrestamoDAO;
import mx.com.ferbo.dao.n.TipoRegimenDAO;
import mx.com.ferbo.dao.n.sat.BancoDAO;
import mx.com.ferbo.model.CatArea;
import mx.com.ferbo.model.CatAsentamiento;
import mx.com.ferbo.model.CatEmpresa;
import mx.com.ferbo.model.CatParametro;
import mx.com.ferbo.model.CatPercepcion;
import mx.com.ferbo.model.CatPerfil;
import mx.com.ferbo.model.CatPeriodicidadPago;
import mx.com.ferbo.model.CatPlanta;
import mx.com.ferbo.model.CatPuesto;
import mx.com.ferbo.model.CatTipoBajaEmpleado;
import mx.com.ferbo.model.CatTipoPrestamo;
import mx.com.ferbo.model.DetBiometrico;
import mx.com.ferbo.model.DetDomicilioEmpleado;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetEmpleadoFoto;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPeriodo;
import mx.com.ferbo.model.DetPercepcionEmpleado;
import mx.com.ferbo.model.DetPrestamo;
import mx.com.ferbo.model.InfDatoEmpresa;
import mx.com.ferbo.model.sat.CatBanco;
import mx.com.ferbo.model.sat.CatEntidadFederativa;
import mx.com.ferbo.model.sat.CatRiesgoPuesto;
import mx.com.ferbo.model.sat.CatTipoContrato;
import mx.com.ferbo.model.sat.CatTipoJornada;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import mx.com.ferbo.model.sat.CatTipoRegimen;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.ManageStatus;
import mx.com.ferbo.util.SGPException;

@Named(value = "registroEmpleadosBean")
@ViewScoped
public class RegistroEmpleadosBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(RegistroEmpleadosBean.class);
    
    private HttpServletRequest request = null;
    private FacesContext context = null;
    private String contextPath = null;
    private HttpSession session = null;

    private EmpresaDAO            empresaDAO;
    private EmpleadoFotoDAO       empleadoFotoDAO;
    private TipoPrestamoDAO       tipoPrestamoDAO;
    private PerfilDAO             perfilDAO;
    private PlantaDAO             plantaDAO;
    private PuestoDAO             puestoDAO;
    private AreaDAO               areaDAO;
    private EmpleadoDAO           empleadoDAO;
    private BiometricoDAO         biometricoDAO;
    private List<CatTipoContrato> tiposContrato;
    private TipoContratoDAO       tipoContratoDAO;
    private List<CatTipoJornada>  tiposJornada;
    private TipoJornadaDAO        tipoJornadaDAO;
    private List<CatTipoRegimen>  tiposRegimen;
    private TipoRegimenDAO        tipoRegimenDAO;
    private ParametroDAO          parametroDAO;
    private EntidadFederativaDAO  entidadDAO;
    private RiesgoPuestoDAO       riesgoDAO;
    private PeriodicidadPagoDAO   periodicidadDAO;
    private TipoPercepcionDAO     tipoPercepcionDAO;
    private AsentamientoDAO       asentamientoDAO;
    private BancoDAO              bancoDAO;
    private PercepcionDAO         percepcionDAO;
    private TipoBajaEmpleadoDAO   tipoBajaEmpleadoDAO;
    private InfDatoEmpresa        datoEmpresa;
    
    private List<CatPercepcion>   percepciones;
    private List<DetPercepcionEmpleado> percepcionesEmpleado;
    

    private List<DetEmpleado> lstEmpleados;
    private List<DetEmpleado> empleados;
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
    private List<CatAsentamiento> lstAsentamientos;
    private List<DetDomicilioEmpleado> lstDomicilios;
    private List<CatBanco> lstBancos;

    private CatPlanta planta;
    private CatEmpresa empresa;
    private boolean activo;

    private CatTipoBajaEmpleado tipodebaja;
    private List<CatTipoBajaEmpleado> tiposdebaja;
    private boolean statusfechabaja;
    private CatTipoBajaEmpleado tipofinrelacion;
    private String motivofinrelaicion;
    private Date fechafinrelacion;

    private DetEmpleado           empleado;
    private DetBiometrico         detBiometrico;
    private DetEmpleadoFoto       empleadoFoto;
    private DetPercepcionEmpleado percepcionEmpleado;
    private String                biometrico;
    private int                   numBiometrico;
    private DetPrestamo           prestamo;
    private DetDomicilioEmpleado  domicilioEmpleadoSelected;
    private CatPercepcion         percepcion;

    private Integer activeTabIndex = 0;
    private String texto;

    public RegistroEmpleadosBean() {
    	this.context = FacesContext.getCurrentInstance();
    	this.request = (HttpServletRequest) context.getExternalContext().getRequest();
    	this.session = request.getSession(false);
    	
    	try {
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
    		entidadDAO = new EntidadFederativaDAO(CatEntidadFederativa.class);
    		riesgoDAO = new RiesgoPuestoDAO(CatRiesgoPuesto.class);
    		periodicidadDAO = new PeriodicidadPagoDAO(CatPeriodicidadPago.class);
    		tipoPercepcionDAO = new TipoPercepcionDAO();
    		tipoPrestamoDAO = new TipoPrestamoDAO();
    		asentamientoDAO = new AsentamientoDAO();
    		tipoBajaEmpleadoDAO = new TipoBajaEmpleadoDAO();
    		bancoDAO = new BancoDAO();
    		percepcionDAO = new PercepcionDAO();
    		
    		empleado = new DetEmpleado();
    		lstEmpleados = new ArrayList<>();
    		
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
            tiposdebaja = tipoBajaEmpleadoDAO.obtenerTodos();
            lstBancos = bancoDAO.buscarTodos();
            percepciones = percepcionDAO.buscarTodos();
            prestamo = new DetPrestamo();
            this.activo = Boolean.TRUE;
            
            consultaEmpleados();
    	} catch(Exception ex) {
    		log.error("Problema para inicializar el registro de empleados...", ex);
		}
    }

    @PostConstruct
    public void init() {
        try {
        	this.redirigir();
            
        } catch (Exception ex) {
            log.error("Problema para cargar el registro de empleados...", ex);
        }
    }
    
    public void redirigir() {
    	DetEmpleado empleadoSesion = null;
    	String path = null;
    	empleadoSesion = (DetEmpleado) session.getAttribute("empleado");
    	contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
    	ExternalContext context = null;
    	
    	try {
    		if(empleadoSesion.getDatoEmpresa().getPerfil().getIdPerfil() == 1) {
    			return;
    		}
    		
	    	path = this.contextPath + "/unauthorized.xhtml";
	    	log.info("Redirigiendo a {}", path);
	    	context = FacesContext.getCurrentInstance().getExternalContext();
	    	context.redirect(path);
    	} catch (IOException e) {
    		log.error("Problem para redirigir a otra página...", e);
    	}
    }
    
    /*
     * Método para consultar a los empleados
     */
    private void consultaEmpleados() {
    	log.info("Cargando lista de empleados...");
        this.lstEmpleados = empleadoDAO.buscarTodos(false);
        log.info("Lista de empleados completa.");
    }

    /*
     * Método para inicializar objeto empleado
     */
    public void agregarEmpleado() {
    	this.empleado = EmpleadoBL.build();
    	this.datoEmpresa = this.empleado.getDatoEmpresa();
        this.empleadoFoto = new DetEmpleadoFoto();
        this.empleadoFoto.setEmpleado(empleado);
        this.empleado.setEmpleadoFoto(this.empleadoFoto);
        this.detBiometrico = new DetBiometrico();
        this.detBiometrico.setEmpleado(this.empleado);
        
        this.activeTabIndex = 0;
    }
    
    public void editar(DetEmpleado empleado) {
    	try {
    		log.info("Cargando información del empleado: {}", empleado);
			this.empleado = EmpleadoBL.load(empleado.getIdEmpleado());
			this.datoEmpresa = this.empleado.getDatoEmpresa();
			this.percepcionesEmpleado = this.empleado.getPercepcionesEmpleado();
			this.empleadoFoto = empleadoFotoDAO.buscar(this.empleado.getNumEmpleado());
	        if (this.empleadoFoto != null) {
	            log.debug("Foto: {}", this.empleadoFoto.getFotografia());
	        }
	        
	        this.detBiometrico = biometricoDAO.consultaBiometricoByIdEmpleado(this.empleado.getIdEmpleado());
	        log.info("Biometrico: {}", this.detBiometrico);
	        
	        this.nuevaPercepcionEmpleado();
	        this.prestamo = new DetPrestamo();
	        
	        this.percepcion = new CatPercepcion();
	        
		} catch (SGPException ex) {
			log.error("Problema para cargar el detalle del empleado...", ex);
		} finally {
			PrimeFaces.current().ajax().update("form:messages", "form:dlg-empleado", "form:panelDialogFoto", ":form:panelDialogEmpleado");
		}
    }
    
    public String getDialogTitle() {
    	
    	if(this.empleado == null) {
    		return "de empleado nuevo";
    	}
    	
    	if(this.empleado.getIdEmpleado() == null) {
    		return "de empleado nuevo";
    	}
    	
    	return String.format(" de %s %s %s",
    			this.empleado.getNombre(),
    			this.empleado.getPrimerAp(),
    			this.empleado.getSegundoAp());
    }
    
    public void calcularPeriodoVacacional() {
    	
    	try {
    		EmpleadoBL.generarAnioVacaciones(empleado);
    	} catch(SGPException ex) {
    		log.info("Problema para generar el periodo vacacional: {}", ex.getMessage());
    	} catch(Exception ex) {
    		log.info("Problema para generar el periodo vacacional.");
    	}
    }

    public CatAsentamiento obtenerAsentamiento(DetEmpleado empleado) {
        CatAsentamiento asentamiento = null;

        if (empleado.getDomicilio().getAsentamiento() != null) {
            asentamiento = empleado.getDomicilio().getAsentamiento();
            log.trace("Asentamiento obtenido {}", asentamiento.toString());
        } else {
            log.info("No se encontro asentamiento del empleado {}", this.empleado.getIdEmpleado());
            asentamiento = DomicilioBL.build(empleado).getAsentamiento();
            this.domicilioEmpleadoSelected = DomicilioBL.build(empleado);
        }

        return asentamiento;
    }

    public void agregarAsentamientoADomicilio(CatAsentamiento auxAsentamiento) {
        log.debug("Agregando/Actualizando asentamiento en domicilio");
        if (auxAsentamiento != null) {
            this.empleado.getDomicilio().setAsentamiento(auxAsentamiento);
        }

    }

    public void nuevaPercepcionEmpleado() {
        this.percepcionEmpleado = new DetPercepcionEmpleado();
        this.percepcionEmpleado.setEmpleado(this.empleado);
        this.percepcionEmpleado.setActivo(true);
        this.percepcionEmpleado.setPercepcion(new CatPercepcion());
        this.percepcion = null;
    }
    
    public void precalculoPercepcion(DetPercepcionEmpleado percepcionEmpleado) {
    	FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Percepción del empleado";
        
    	DetNominaPeriodo nominaPeriodo = null;
    	ParametrosNomina parametros = null;
    	DetNomina nomina = null;
    	
    	DetNominaPercepcion nominaPercepcion = null;
    	
    	try {
    		this.percepcionEmpleado = percepcionEmpleado;
    		
    		nominaPeriodo = NominaPeriodoBL.get( this.empleado.getDatoEmpresa().getEmpresa(), NominaBL.TP_NOMINA_ORDINARIA, this.empleado.getDatoEmpresa().getPeriodicidadPago(), DateUtil.getAnio(new Date()), DateUtil.getSemanaAnio(new Date()));
    		
    		parametros = new ParametrosNomina();
    		parametros.cargar(nominaPeriodo);
    		
    		nomina = NominaBL.build(NominaBL.TP_NOMINA_ORDINARIA, parametros, this.empleado);
    		
    		nominaPercepcion = NominaSemanalBL.calcular(nomina, parametros, this.percepcionEmpleado.getPercepcion().getClave(), this.percepcionEmpleado.getValor());
    		
    		this.percepcionEmpleado.setImporteExento(nominaPercepcion.getImporteExento());
    		this.percepcionEmpleado.setImporteGravado(nominaPercepcion.getImporteGravado());
    		
    		mensaje = "Precalculo correcto";
            severity = FacesMessage.SEVERITY_INFO;
    	} catch (Exception ex) {
            log.error("Problema para agregar información del empleado...", ex);
            mensaje = "Problema para agregar al empleado.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
        }
    	
    }

    public void agregarPercepcionEmpleado() {
    	FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Percepción del empleado";
    	
    	try {
    		
    		log.info("Agregando percepcion del empleado...");
    		if (this.empleado.getPercepcionesEmpleado() == null) {
    			this.empleado.setPercepcionesEmpleado(new ArrayList<>());
    		}
    		
    		this.percepcionEmpleado.setEmpleado(this.empleado);
    		this.empleado.getPercepcionesEmpleado().add(percepcionEmpleado);
    		this.nuevaPercepcionEmpleado();
    		
    		mensaje = "Percepción agregada correctamente.";
            severity = FacesMessage.SEVERITY_INFO;
            
        } catch (Exception ex) {
            log.error("Problema para agregar información del empleado...", ex);
            mensaje = "Problema para agregar al empleado.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:panelDialogEmpleado:dt-percepcion-empleado");
        }
    	
    }

    public void eliminarPercepcionEmpleado() {
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Percepción del empleado";

        try {
            if (this.percepcionEmpleado == null) {
                throw new SGPException("Debe indicar una percepción");
            }

            this.empleado.getPercepcionesEmpleado().remove(this.percepcionEmpleado);
            this.percepcionEmpleado = new DetPercepcionEmpleado();
            this.percepcionEmpleado.setPercepcion(new CatPercepcion());

            mensaje = "Percepción eliminada correctamente.";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (SGPException ex) {
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para guardar información del empleado...", ex);
            mensaje = "Problema para guardar al empleado.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:panelDialogEmpleado:dt-percepcion-empleado");
        }
    }

    public void agregarPrestamo() {
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Préstamo";

        try {
            if (this.prestamo == null) {
                throw new SGPException("Debe indicar un préstamo");
            }

            if (this.prestamo.getTipoPrestamo() == null) {
                throw new SGPException("Debe indicar el tipo de préstamo");
            }

            if (this.prestamo.getFechaInicio() == null) {
                throw new SGPException("Debe indicar la fecha de inicio del préstamo.");
            }

            if (this.prestamo.getFechaFin() == null) {
                throw new SGPException("Debe indicar la fecha de fin del préstamo.");
            }

            if (this.prestamo.getAcumulado() == null) {
                throw new SGPException("Debe indicar el acumulado del préstamo.");
            }

            if (this.prestamo.getImporte() == null) {
                throw new SGPException("Debe indicar el importe del préstamo.");
            }

            if (this.prestamo.getTotal() == null) {
                throw new SGPException("Debe indicar el total del préstamo.");
            }

            if (this.prestamo.getPeriodicidadPago() == null) {
                throw new SGPException("Debe indicar la periodicidad de pago.");
            }

            if (this.empleado.getPrestamos() == null) {
                this.empleado.setPrestamos(new ArrayList<>());
            }

            prestamo.setEmpleado(this.empleado);

            this.empleado.getPrestamos().add(prestamo);

            prestamo = new DetPrestamo();
            PrimeFaces.current().executeScript("PF('dgPrestamo').hide()");

            mensaje = "Préstamo agregado correctamente.";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (SGPException ex) {
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para guardar información del empleado...", ex);
            mensaje = "Problema para guardar al empleado.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:panelDialogEmpleado:dt-prestamos");
        }
    }

    public void eliminarPrestamo(DetPrestamo prestamo) {
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Préstamo";

        try {
            if (this.prestamo == null) {
                throw new SGPException("Debe indicar un préstamo");
            }

            this.empleado.getPrestamos().remove(this.prestamo);

            prestamo = new DetPrestamo();
            PrimeFaces.current().executeScript("PF('dgPrestamo').hide()");

            mensaje = "Préstamo eliminado correctamente.";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (SGPException ex) {
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para guardar información del empleado...", ex);
            mensaje = "Problema para guardar al empleado.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:panelDialogEmpleado:dt-prestamos");
        }
    }
    
    public synchronized void configuraPercepcion() {
    	FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Guardar empleado";
    	
        try {
        	if(this.percepcion == null)
        		throw new SGPException("Debe seleccionar una percepción.");
        	
        	this.percepcionEmpleado.setPercepcion(this.percepcion);
        	
        	mensaje = "Indique el valor o importe máximo de la percepcion";
        	severity = FacesMessage.SEVERITY_INFO;
        } catch (SGPException ex) {
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para seleccionar la percepcion...", ex);
            mensaje = "Problema para seleccionar la percepción.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public synchronized void guardarEmpleado() {
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Guardar empleado";
        CatParametro pNumeroEmpleado = null;
        String sNumeroEmpleado = null;
        int numeroEmpleado = -1;
        try {
            this.empleado.setPercepcionesEmpleado(this.percepcionesEmpleado);

            if (this.datoEmpresa.getFechaIngreso() == null) {
                log.error("Falta fecha de ingreso en dato empresa");
                throw new SGPException("Debe indicar la fecha de ingreso");
            }

            if (this.datoEmpresa.getHoraEntrada() == null) {
                log.error("Falta hora de entrada en dato empresa");
                throw new SGPException("Debe indicar la hora de entrada");
            }

            if (this.datoEmpresa.getHorasalida() == null) {
                log.error("Falta hora de salida en dato empresa");
                throw new SGPException("Debe indicar la hora de salida");
            }

            if (this.datoEmpresa.getMinutosTolerancia() == null) {
                log.error("Falta los minutos de tolerancia para la hora de entrada en dato empresa");
                throw new SGPException("Debe indicar los minutos de tolerancia para la hora de entrada");
            }
            
            this.empleado.setDatoEmpresa(this.datoEmpresa);

            if (this.empleado.getIdEmpleado() == null) {
                pNumeroEmpleado = this.parametroDAO.buscarPorClave("NBEMP");
                sNumeroEmpleado = pNumeroEmpleado.getValor();
                numeroEmpleado = Integer.parseInt(sNumeroEmpleado);
                sNumeroEmpleado = String.format("%04d", ++numeroEmpleado);
                transformarAMayusculas();
                this.empleado.setNumEmpleado(sNumeroEmpleado);
                this.empleado.setDatoEmpresa(this.datoEmpresa);
                this.empleado.setFechaRegistro(new Date());
                this.empleado.getDomicilio().setEmpleado(this.empleado);
                this.empleado.getEmpleadoConfiguracion().setEmpleado(this.empleado);
                this.empleadoDAO.guardar(empleado);
                pNumeroEmpleado.setValor(sNumeroEmpleado);
                this.parametroDAO.actualizar(pNumeroEmpleado);
            } else {
                this.empleado.getDomicilio().setEmpleado(this.empleado);
                this.empleado.getEmpleadoConfiguracion().setEmpleado(this.empleado);
                this.empleadoDAO.actualizar(this.empleado);
            }

            if (this.empleadoFoto != null) {
                empleadoFotoDAO.actualizar(empleadoFoto);
            }

            if (biometrico != null) {
                detBiometrico.setEmpleado(empleado);
                if (detBiometrico.getIdBiometrico() == null) {
                    biometricoDAO.guardar(detBiometrico);
                } else {
                    biometricoDAO.actualizar(detBiometrico);
                }
                biometrico = null;
            }

            consultaEmpleados();
            PrimeFaces.current().executeScript("PF('dialogEmpleado').hide()");
            mensaje = "El empleado se guardó correctamente.";
            severity = FacesMessage.SEVERITY_INFO;

        } catch (SGPException ex) {
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para guardar información del empleado...", ex);
            mensaje = "Problema para guardar al empleado.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:panelDialogEmpleado");
        }
    }

    public void sinFoto() {
        this.empleadoFoto = null;
    }

    /*
     * Método para eliminar 1 empleado
     */
    public void eliminaEmpleado() {
        try {
            if (this.tipofinrelacion.getTipodebaja().startsWith("T")) {
                this.empleado.setActivo((short) 0);
            }

            if (this.tipofinrelacion.getTipodebaja().startsWith("S")) {
                this.empleado.setActivo((short) 2);
            }

            this.empleado.setFechaModificacion(new Date());
            this.empleado.getDatoEmpresa().setTipodebaja(this.tipofinrelacion);
            this.empleado.getDatoEmpresa().setMotivobaja(this.motivofinrelaicion);
            if (this.fechafinrelacion != null) {
                this.empleado.getDatoEmpresa().setFechaBaja(this.fechafinrelacion);
            }
            this.empleadoDAO.actualizar(this.empleado);
            consultaEmpleados();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Empleado Eliminado"));
            limpiarEliminar();

        } catch (SGPException ex) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al eliminar al empleado"));
            log.warn("EX-0018: " + ex.getMessage() + ". Error al guardar la huella del empleado " + empleado.getNumEmpleado() != null ? empleado.getNumEmpleado() : null);
        }
        PrimeFaces.current().ajax().update("form:messages", "form:dtEmpleados");
    }

    public void limpiarEliminar() {
        this.tipofinrelacion = null;
        this.motivofinrelaicion = null;
        this.fechafinrelacion = null;
        this.statusfechabaja = false;
    }

    public String redirectKardex() {
        String redirect = "/protected/kardexEmpleado.xhtml?faces-redirect=true&idEmpleado=" + empleado.getIdEmpleado();
        return redirect;
    }

    public void oncapture(CaptureEvent captureEvent) {
        if (this.empleadoFoto == null) {
            this.empleadoFoto = new DetEmpleadoFoto();
            this.empleado.setEmpleadoFoto(empleadoFoto);
        }

        this.empleadoFoto.setFotografia("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(captureEvent.getData()));
    }

    public void consultaBiometrico() {
        detBiometrico = biometricoDAO.consultaBiometricoByNumEmpleado(empleado.getNumEmpleado());
        PrimeFaces.current().executeScript("PF('dialogBiometrico').show()");
    }

    public void validaHuella() {
        try {
            if (biometrico == null) {
                throw new SGPException("Error al asignar biométrico");
            }

            asignarBiometricos();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Huella asignada"));
        } catch (SGPException ex) {
            log.warn("EX-0019: Error al consultar por huella al empleado " + detBiometrico.getEmpleado().getNumEmpleado() != null ? detBiometrico.getEmpleado().getNumEmpleado() : null + " y " + ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al asignar biométrico"));
        } finally {
            PrimeFaces.current().ajax().update("form:messages", "form:panelDialogBiometrico");
        }
    }

    public void asignarBiometricos() {
        if (this.detBiometrico == null) {
            this.detBiometrico = new DetBiometrico();
        }

        switch (numBiometrico) {
            case 1:
                detBiometrico.setHuella(biometrico);
                break;
            case 2:
                detBiometrico.setHuella2(biometrico);
                break;
        }

        detBiometrico.setActivo((short) 1);
        detBiometrico.setFechaCaptura(new Date());
    }

    public List<CatAsentamiento> sugerenciasCodigoPostal(String consulta) {
        List<CatAsentamiento> listaSugerencias = asentamientoDAO.buscarPorCodigoPostal(consulta);
        return listaSugerencias;
    }

    public void asignarDomicilio() {
    	FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Domicilio";
    	
    	try {
    		
    		if (this.empleado.getDomicilio() == null) {
                throw new SGPException("No hay objeto Domicilio asignado al empleado.");
            }
    		
    		log.info("Agregando / Actualizando información al domicilio");
            log.debug("Domicilio seleccionado: {}", this.empleado.getDomicilio().toString());
            log.debug("Asentamiento seleccionado: {}", this.empleado.getDomicilio().getAsentamiento().toString());
    		
    		mensaje = "Nuevo domicilio seleccionado.";
            severity = FacesMessage.SEVERITY_INFO;

        } catch (SGPException ex) {
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para seleccionar un domicilio...", ex);
            mensaje = "Problema para seleccionar un domicilio.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:panelDialogEmpleado");
        }
    }

    public void transformarAMayusculas() {
        String nombre = this.empleado.getNombre().toUpperCase();
        String apellidoPa = this.empleado.getPrimerAp().toUpperCase();
        String apellidoMa = this.empleado.getSegundoAp().toUpperCase();
        String curp = this.empleado.getCurp().toUpperCase();

        this.empleado.setNombre(nombre);
        this.empleado.setPrimerAp(apellidoPa);
        this.empleado.setSegundoAp(apellidoMa);
        this.empleado.setCurp(curp);
    }
    
    public void filtrarEmpleados() {
    	
    	//Sin filtro de empresa y planta. Activos seleccionados.
    	//Devuelve todos los empleados.
        if (this.empresa == null && this.planta == null && this.activo == true) {
            this.empleados = this.lstEmpleados.stream()
            		.filter( e -> e.getActivo() == 1)
            		.collect(Collectors.toList());
            return;
        }
        
        //Sin filtro de empresa y planta. Sólo inactivos seleccionados.
        //Devuelve los empleados inactivos.
        if(this.empresa == null && this.planta == null && this.activo == false) {
        	this.empleados = this.lstEmpleados.stream()
        			.filter( e -> e.getActivo() == 0)
        			.collect(Collectors.toList());
        	return;
        }
        
        //Con filtro de empresa, sin filtro de planta. Activos e Inactivos seleccionados.
        //Devuelve los empleados relacionados a una empresa, cualquier planta, activos e inactivos.
        if(this.empresa != null && this.planta == null && this.activo == true) {
        	
        	this.empleados = this.lstEmpleados.stream()
        			.filter( e -> e.getDatoEmpresa() != null)
        			.filter( e -> e.getDatoEmpresa().getEmpresa() != null)
        			.filter( e -> e.getDatoEmpresa().getEmpresa().getIdEmpresa() == this.empresa.getIdEmpresa() )
        			.filter( e -> e.getActivo() == 1 )
        			.collect(Collectors.toList());
        	return;
        }
        
        if(this.empresa != null && this.planta == null && this.activo == false) {
        	
        	this.empleados = this.lstEmpleados.stream()
        			.filter( e -> e.getDatoEmpresa() != null )
        			.filter( e -> e.getDatoEmpresa().getEmpresa() != null )
        			.filter( e -> e.getDatoEmpresa().getEmpresa().getIdEmpresa() == this.empresa.getIdEmpresa() )
        			.filter( e -> e.getActivo() == 0 )
        			.collect(Collectors.toList());
        	return;
        }
        
        //Con filtro de planta, sin filtro de empresa. Activos seleccionados.
        //Devuelve los empleados relacionados a una planta, cualquier empresa, activos.
        if(this.empresa == null && this.planta != null && this.activo == true) {
        	
        	this.empleados = this.lstEmpleados.stream()
        			.filter( e -> e.getDatoEmpresa() != null )
        			.filter( e -> e.getDatoEmpresa().getPlanta() != null )
        			.filter( e -> e.getDatoEmpresa().getPlanta().getIdPlanta() == this.planta.getIdPlanta())
        			.filter( e -> e.getActivo() == 1 )
        			.collect(Collectors.toList());
        	return;
        }
        
        //Con filtro de planta, sin filtro de empresa. Inactivos seleccionados.
        //Devuelve los empleados relacionados a una planta, cualquier empresa, inactivos.
        if(this.empresa == null && this.planta != null && this.activo == false) {
        	
        	this.empleados = this.lstEmpleados.stream()
        			.filter( e -> e.getDatoEmpresa() != null )
        			.filter( e -> e.getDatoEmpresa().getPlanta() != null )
        			.filter( e -> e.getDatoEmpresa().getPlanta().getIdPlanta() == this.planta.getIdPlanta() )
        			.filter( e -> e.getActivo() == 0)
        			.collect(Collectors.toList());
        	return;
        }
        
        if(this.empresa != null && this.planta != null && this.activo == true) {
        	this.empleados = this.lstEmpleados.stream()
        			.filter( e -> e.getDatoEmpresa() != null )
        			.filter( e -> e.getDatoEmpresa().getEmpresa() != null)
        			.filter( e -> e.getDatoEmpresa().getPlanta() != null )
        			.filter( e -> e.getDatoEmpresa().getEmpresa().getIdEmpresa() == this.empresa.getIdEmpresa() )
        			.filter( e -> e.getDatoEmpresa().getPlanta().getIdPlanta() == this.planta.getIdPlanta() )
        			.filter( e -> e.getActivo() == 1 )
        			.collect(Collectors.toList());
        	return;
        }
        
        if(this.empresa != null && this.planta != null && this.activo == false) {
        	this.empleados = this.lstEmpleados.stream()
        			.filter( e -> e.getDatoEmpresa() != null )
        			.filter( e -> e.getDatoEmpresa().getEmpresa() != null)
        			.filter( e -> e.getDatoEmpresa().getPlanta() != null )
        			.filter( e -> e.getDatoEmpresa().getEmpresa().getIdEmpresa() == this.empresa.getIdEmpresa() )
        			.filter( e -> e.getDatoEmpresa().getPlanta().getIdPlanta() == this.planta.getIdPlanta() )
        			.filter( e -> e.getActivo() == 0 )
        			.collect(Collectors.toList());
        	return;
        }
    }

    public void manipularStatusFechaBaja() {
        if (this.tipofinrelacion == null) {
            this.statusfechabaja = false;
            return;
        }

        this.statusfechabaja = this.tipofinrelacion.getTipodebaja().startsWith("T");
    }

    public String obtenerStatusEmpleadoEmpresa(DetEmpleado empleado) {
        return ManageStatus.getEstadoEmpleadoEmpresa(empleado.getActivo());
    }

    public void validarEmpleado(DetEmpleado empleado) {
        EmpleadoBL.validarDatosEmpleado(empleado);
        this.tipofinrelacion = null;
        this.motivofinrelaicion = null;
        this.fechafinrelacion = null;
    }

    public void recalcularVacaciones() {
        try {
            EmpleadoBL.recalcularVacaciones(this.empleado);
        } catch (SGPException sgpEx) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Informacion", sgpEx.getMessage()));
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public List<CatEmpresa> getLstCatEmpresa() {
        return lstCatEmpresa;
    }

    public void setLstCatEmpresa(List<CatEmpresa> lstCatEmpresa) {
        this.lstCatEmpresa = lstCatEmpresa;
    }

    public DetEmpleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(DetEmpleado empleado) {
        this.empleado = empleado;
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

//    public String getRfc() {
//        return rfc;
//    }
//
//    public void setRfc(String rfc) {
//        this.rfc = rfc;
//    }
//
//    public String getCurp() {
//        return curp;
//    }
//
//    public void setCurp(String curp) {
//        this.curp = curp;
//    }
//
//    public String getNss() {
//        return nss;
//    }
//
//    public void setNss(String nss) {
//        this.nss = nss;
//    }

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

    public List<CatAsentamiento> getLstAsentamientos() {
        return lstAsentamientos;
    }

    public void setLstAsentamientos(List<CatAsentamiento> lstAsentamientos) {
        this.lstAsentamientos = lstAsentamientos;
    }

    public List<DetDomicilioEmpleado> getLstDomicilios() {
        return lstDomicilios;
    }

    public void setLstDomicilios(List<DetDomicilioEmpleado> lstDomicilios) {
        this.lstDomicilios = lstDomicilios;
    }

//    public String getCodigoPostal() {
//        return codigoPostal;
//    }
//
//    public void setCodigoPostal(String codigoPostal) {
//        this.codigoPostal = codigoPostal;
//    }

    public DetDomicilioEmpleado getDomicilioEmpleadoSelected() {
        return domicilioEmpleadoSelected;
    }

    public void setDomicilioEmpleadoSelected(DetDomicilioEmpleado domicilioEmpleadoSelected) {
        this.domicilioEmpleadoSelected = domicilioEmpleadoSelected;
    }

    public Integer getActiveTabIndex() {
        return activeTabIndex;
    }

    public void setActiveTabIndex(Integer activeTabIndex) {
        this.activeTabIndex = activeTabIndex;
    }

    public CatPlanta getPlanta() {
        return planta;
    }

    public void setPlanta(CatPlanta planta) {
        this.planta = planta;
    }

    public CatEmpresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(CatEmpresa empresa) {
        this.empresa = empresa;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public CatTipoBajaEmpleado getTipodebaja() {
        return tipodebaja;
    }

    public void setTipodebaja(CatTipoBajaEmpleado tipodebaja) {
        this.tipodebaja = tipodebaja;
    }

    public List<CatTipoBajaEmpleado> getTiposdebaja() {
        return tiposdebaja;
    }

    public void setTiposdebaja(List<CatTipoBajaEmpleado> tiposdebaja) {
        this.tiposdebaja = tiposdebaja;
    }

    public boolean isStatusfechabaja() {
        return statusfechabaja;
    }

    public void setStatusfechabaja(boolean statusfechabaja) {
        this.statusfechabaja = statusfechabaja;
    }

    public CatTipoBajaEmpleado getTipofinrelacion() {
        return tipofinrelacion;
    }

    public void setTipofinrelacion(CatTipoBajaEmpleado tipofinrelacion) {
        this.tipofinrelacion = tipofinrelacion;
    }

    public String getMotivofinrelaicion() {
        return motivofinrelaicion;
    }

    public void setMotivofinrelaicion(String motivofinrelaicion) {
        this.motivofinrelaicion = motivofinrelaicion;
    }

    public Date getfechafinrelacion() {
        return fechafinrelacion;
    }

    public void setfechafinrelacion(Date fechafinrelacion) {
        this.fechafinrelacion = fechafinrelacion;
    }

    public List<CatBanco> getLstBancos() {
        return lstBancos;
    }

    public void setLstBancos(List<CatBanco> lstBancos) {
        this.lstBancos = lstBancos;
    }

	public List<CatPercepcion> getPercepciones() {
		return percepciones;
	}

	public void setPercepciones(List<CatPercepcion> percepciones) {
		this.percepciones = percepciones;
	}

	public CatPercepcion getPercepcion() {
		return percepcion;
	}

	public void setPercepcion(CatPercepcion percepcion) {
		this.percepcion = percepcion;
	}

	public List<DetEmpleado> getEmpleados() {
		return empleados;
	}

	public void setEmpleados(List<DetEmpleado> empleados) {
		this.empleados = empleados;
	}
}
