package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import mx.com.ferbo.business.empleado.EmpleadoBL;
import mx.com.ferbo.business.nomina.NominaBL;
import mx.com.ferbo.business.nomina.NominaPeriodoBL;
import mx.com.ferbo.business.nomina.NominaSemanalBL;
import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.business.percepcion.PercepcionBL;
import mx.com.ferbo.business.percepcion.PrimaVacacionalEnTiempoPBL;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.EmpresaDAO;
import mx.com.ferbo.dao.n.NominaDAO;
import mx.com.ferbo.dao.n.PercepcionDAO;
import mx.com.ferbo.dao.n.PeriodicidadPagoDAO;
import mx.com.ferbo.dao.n.VacacionesDAO;
import mx.com.ferbo.dto.ui.Asistencia;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.CatEmpresa;
import mx.com.ferbo.model.CatPercepcion;
import mx.com.ferbo.model.CatPeriodicidadPago;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaOtroPago;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPeriodo;
import mx.com.ferbo.model.DetPercepcionEmpleado;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.model.DetVacaciones;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.model.sat.CatTipoOtroPago;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import mx.com.ferbo.util.BitacoraUIAppender;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.ManageStatus;
import mx.com.ferbo.util.SGPException;

@Named(value = "nominaS")
@ViewScoped
public class NominaSemanalBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(NominaSemanalBean.class);

    private ParametrosNomina parametros = null;
    
    private EmpleadoDAO empleadoDAO;
    private EmpresaDAO empresaDAO;
    private NominaDAO nominaDAO;
    private PercepcionDAO percepcionDAO = null;
    private VacacionesDAO vacacionesDAO = null;
    
    private List<CatEmpresa>        lstEmpresas;
    private List<CatPercepcion>     catalogoPercepciones = null;
    private List<CatTipoPercepcion> tiposPercepcion;
    private List<CatTipoDeduccion>  tiposDeduccion;
    private List<CatTipoOtroPago>   tiposOtroPago;
    
    private CatEmpresa          empresaSelected;
    private CatPercepcion       percepcionCatalogo;
    private DetNomina           nomina;
    private DetNominaPercepcion percepcion;
    private DetNominaOtroPago   otroPago;
    private DetNominaDeduccion  deduccion;
    
    private DetNominaPeriodo    nominaPeriodo;
    private CatPeriodicidadPago periodicidad = null;
    private PeriodicidadPagoDAO periodicidadDAO = null;
    
    private Integer anio;
    private Date    fecha;
    private Date    periodoInicio;
    private Date    periodoFin;
    private Integer semana;
    
    private List<DetNomina>     listaNomina;
    private List<Integer>       semanasDelAnio;
    private List<Asistencia>    asistencias;
    private List<DetVacaciones> periodosVacacionales;
    private DetVacaciones       periodoVacacional;
    
    private Boolean detalle = true;
    private String bitacora = null;
    private StreamedContent file = null;

	public NominaSemanalBean() {
		listaNomina     = new ArrayList<>();
		empleadoDAO     = new EmpleadoDAO();
		empresaDAO      = new EmpresaDAO();
		nominaDAO       = new NominaDAO();
		periodicidadDAO = new PeriodicidadPagoDAO();
		percepcionDAO   = new PercepcionDAO();
		vacacionesDAO   = new VacacionesDAO();
		periodosVacacionales = new ArrayList<DetVacaciones>();
	}
    
    @PostConstruct
    public void init() {
    	BitacoraUIAppender.limpiar();
        log.info("====================== entrada init NominaSemanalBean ======================");
        this.lstEmpresas = empresaDAO.buscarActivo();
        this.catalogoPercepciones = percepcionDAO.buscarTodos();

        log.info("Inicio del periodo: {}", periodoFin);
        log.info("Fin del periodo: {}", periodoInicio);
        
        this.periodicidad = periodicidadDAO.buscarPorId(CatPeriodicidadPago.P_SEMANAL);
        this.percepcion   = new DetNominaPercepcion();
        this.otroPago     = new DetNominaOtroPago();
        this.deduccion    = new DetNominaDeduccion();
        
        this.fecha          = DateUtil.now();
        this.anio           = DateUtil.getAnio(fecha);
        this.semanasDelAnio = DateUtil.semanasDelAnio(this.anio);
        this.semana         = DateUtil.getSemanaAnio(this.fecha);
        
        this.periodoVacacional = new DetVacaciones();
        
        byte bytes[] = {};
        this.file = DefaultStreamedContent.builder()
				.contentType("text/plain")
				.contentLength(bytes.length)
				.name("bitacora.txt").stream(() -> new ByteArrayInputStream(bytes))
				.build();
    }
    
    public void calculaSemanas() {
    	this.semanasDelAnio = DateUtil.semanasDelAnio(this.anio);
    	this.semana = null;
    	this.periodoInicio = null;
    	this.periodoFin = null;
    }
    
    public void calculaPeriodo() {
    	
    	try {
    		if(this.empresaSelected == null)
    			throw new SGPException("Debe seleccionar una empresa");
    		
    		if(this.nominaPeriodo == null)
    			this.anio = DateUtil.getAnio(DateUtil.now());
    		else {
    			this.nominaPeriodo.getKey().setAnio(this.anio);
    			this.nominaPeriodo.getKey().setPeriodo(this.semana);
    		}
    		
    		this.nominaPeriodo = NominaPeriodoBL.get(this.empresaSelected, NominaBL.TP_NOMINA_ORDINARIA, this.periodicidad, this.anio, this.semana);
    		this.periodoInicio = DateUtil.toDate(this.nominaPeriodo.getPeriodoInicio());
    		this.periodoFin = DateUtil.toDate(this.nominaPeriodo.getPeriodoFin());
    		
    	} catch(SGPException ex) {
    		log.error("Problema para generar el periodo...", ex);
    	}
    }
    
    public void calculaFechasPeriodo() {
    	this.periodoInicio = DateUtil.getLunesDeSemanaDate(this.anio, this.semana);
    	this.periodoFin = new Date(this.periodoInicio.getTime());
    	this.periodoFin = DateUtil.addDay(this.periodoFin, 6);
    	
    	log.info("Fecha Inicio: {}", this.periodoInicio);
    	log.info("Fecha Fin: {}", this.periodoFin);
    }

    public void calculandoNomina() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Nómina";
		
    	List<DetEmpleado> listaEmpleados = null;
    	try {
    		log.info("Cargando nómina para el periodo {}", this.nominaPeriodo);
    		
    		this.listaNomina.clear();
    		listaEmpleados = EmpleadoBL.buscarActivosPorEmpresa(empresaSelected.getIdEmpresa(), this.periodoInicio, this.periodoFin);
    		this.procesaListaEmpleados(listaEmpleados);
    		
    		mensaje = "Nomina cargada correctamente.";
    		severity = FacesMessage.SEVERITY_INFO;
    	} catch(Exception ex) {
    		log.error("Problema para procesar la nómina.");
    		mensaje = "Hay un problema para procesar la nómina.";
    		severity = FacesMessage.SEVERITY_ERROR;
    	} finally {
    		if(listaEmpleados != null)
    			listaEmpleados.clear();
    		message = new FacesMessage(severity, titulo, mensaje);
    		FacesContext.getCurrentInstance().addMessage(null, message);
    		PrimeFaces.current().ajax().update("form:messages", "form:dtNomina");
    		PrimeFaces.current().executeScript("PF('empresaDialog').hide()");
    	}
    }
    
    private void procesaListaEmpleados(List<DetEmpleado> listaEmpleados) {
    	DetNomina nomina = null;
    	try {
    		this.parametros = new ParametrosNomina();
    		this.parametros.cargar(nominaPeriodo);
    		
    		this.tiposPercepcion = this.parametros.getTiposPercepcion();
    		this.tiposOtroPago = this.parametros.getTiposOtroPago();
    		this.tiposDeduccion = this.parametros.getTiposDeduccion();
    		
    		for (DetEmpleado empleado : listaEmpleados) {
    			
    			nomina = NominaBL.load(parametros, empleado.getDatoEmpresa().getRfc());
    			
    			if(nomina == null) {
    				nomina = this.procesaEmpleado(empleado);
    			} else {
    				nomina.getPercepciones().stream()
    					.forEach(item -> item.setImporte(item.getImporteExento().add(item.getImporteGravado())));
    				nomina.getNominaVacaciones().stream()
    					.forEach(item -> item.getNomina().getVacaciones().add(item.getVacaciones()));
    			}
    			
    			listaNomina.add(nomina);
    		}
    		log.info("Lista nomina: {}", this.listaNomina);
    	} catch(Exception ex) {
    		log.error("Problema para procesar la nómina", ex);
    	}
    }
    
    public DetNomina procesaEmpleado(DetEmpleado empleado) {
    	DetNomina nomina = null;
    	NominaSemanalBL nominaSemanalBO = null;
    	
    	List<DetPercepcionEmpleado> percepcionesEmpleado = null;
    	Map<String, DetRegistro> mapAsistencias = null;
    	
    	empleado.setPercepcionesEmpleado(percepcionesEmpleado);
    	mapAsistencias = NominaSemanalBL.getAsistencias(empleado, this.parametros);
    	
		nominaSemanalBO = new NominaSemanalBL(empleado, this.parametros, mapAsistencias);
		nomina = nominaSemanalBO.calcular();
		
		return nomina;
    }
    
    public void cargaEmpleadoNomina(DetNomina nomina) {
    	log.info("Cargando información de nómina: {}", nomina);
    	this.nomina = nomina;
    }

    public String statusNomina(DetNomina nomina) {
    	if(nomina == null)
    		return "";
    	
        return ManageStatus.getEstadoEmpleadoEmpresa((nomina.getId() == null ? (short) 2 : (short) 1));
    }
    
    public void setPercepcionCatalogo() {
    	this.percepcion.setClave(this.percepcionCatalogo.getClave());
    	this.percepcion.setTipoPercepcion(this.percepcionCatalogo.getTipoPercepcion());
    	this.percepcion.setNombre(this.percepcionCatalogo.getNombre());
    }
    
    public void nuevaPercepcion() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Percepción";
		try {
			this.percepcion = NominaBL.nuevaPercepcion(this.nomina);
			this.percepcionCatalogo = null;
		} catch(SGPException ex) {
			log.warn("Problema para crear la percepcion: {}", ex.getMessage());
    		mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
		} catch(Exception ex) {
			mensaje = "Hay un problema para crear la percepcion.";
			severity = FacesMessage.SEVERITY_ERROR;
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
		}
    }
    
    public List<CatTipoPercepcion> buscaTipoPercepcion(String query) {
    	List<CatTipoPercepcion> tiposPercepcion = null;
    	
    	try {
    		final String queryLowerCase = query.toLowerCase();
    		tiposPercepcion = this.tiposPercepcion.stream()
    				.filter( p -> p.getClave().toLowerCase().contains(queryLowerCase)
    						|| p.getDescripcion().toLowerCase().contains(queryLowerCase) )
    				.collect(Collectors.toList())
    				;
    	} catch(Exception ex) {
    		log.warn("No fue posible encontrar el tipo de percepcion solicitado: {}", query);
    		tiposPercepcion = new ArrayList<CatTipoPercepcion>();
    	}
    	
    	return tiposPercepcion;
    }
    
    public void agregarPercepcion() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Percepción";
		
		try {
			NominaSemanalBL.agregarPercepcion(this.nomina, this.percepcion);
			NominaSemanalBL.procesarISR(this.parametros, this.nomina);
			
			this.percepcion = new DetNominaPercepcion();
			this.actualizar();
			
			PrimeFaces.current().executeScript("PF('dlgAddPercepcion').hide();");
			
		} catch(Exception ex) {
			log.error("Problema para agregar la percepcion...", ex);
    		mensaje = "Hay un problema para agregar la percepción.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
    	} finally {
    		PrimeFaces.current().ajax().update("form:messages", "form:tv-nomina");
    	}
    }
    
    public void agregaPeriodoVacacional() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Periodo vacacional";
		
		try {
			this.periodosVacacionales = vacacionesDAO.buscarPeriodosEnTiempoNoPagados(this.nomina.getReceptor().getRfc());
			
			PrimeFaces.current().executeScript("PF('dlgAddPeriodoVacacional').show();");
			
		} catch(Exception ex) {
			log.error("Problema para cargar los periodos vacacionales...", ex);
    		mensaje = "Hay un problema para cargar los periodos vacacionales.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
    	} finally {
    		PrimeFaces.current().ajax().update("form:messages", "form:dlg-add-periodo-vacacional");
    	}
    }
    
    public void procesaPeriodoVacacional(DetVacaciones periodoVacacional, String tipoPrima) {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Periodo vacacional";
		
		DetNominaPercepcion primaVacacional = null;
		PrimaVacacionalEnTiempoPBL primaEnTiempoBO = null;
		
		try {
			
			if("T".equalsIgnoreCase(tipoPrima)) {
				primaEnTiempoBO = (PrimaVacacionalEnTiempoPBL) NominaSemanalBL.getPercepcionBusinessLogic(parametros, nomina, PercepcionBL.CVE_PRIMA_VACACIONES_EN_TIEMPO);
				primaEnTiempoBO.setPeriodo(periodoVacacional);
				primaVacacional = primaEnTiempoBO.procesar(nomina, primaEnTiempoBO.calcularCantidad(nomina));
			} else if ("R".equalsIgnoreCase(tipoPrima)) {
				log.info("Nada que realizar por el momento...");
			} else {
				throw new SGPException("El tipo de prima vacacional es incorrecto.");
			}
			
			NominaBL.agregarPercepcion(nomina, primaVacacional);
			NominaBL.calcularTotales(nomina, parametros);
			PrimeFaces.current().executeScript("PF('dlgAddPeriodoVacacional').hide();");
		} catch(Exception ex) {
			log.error("Problema para agregar el periodo vacacional", ex);
    		mensaje = "Hay un problema para agregar los periodos vacacionales.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update("form:messages", "form:tv-nomina", "form:dtNomina");
		}
    }
    
    public void actualizarPorCantidad(DetNominaPercepcion percepcion) {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Percepción";
		
		try {
			if(percepcion == null)
				throw new SGPException("No se ha definido una percepcion.");
			
			if(percepcion.getCantidad() == null)
				throw new SGPException("No se ha definido una cantidad");
			
			if(percepcion.getCantidad().compareTo(ValoresBD._CERO.get()) <= 0)
				throw new SGPException("La cantidad es incorrecta");
			
			NominaSemanalBL.calcular(nomina, parametros, percepcion.getClave(), percepcion.getCantidad());
			
			NominaSemanalBL.procesarISR(parametros, nomina);
			
			this.actualizar();
		} catch(Exception ex) {
			log.error("Problema para agregar la percepcion...", ex);
    		mensaje = "Hay un problema para agregar la percepción.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			
			PrimeFaces.current().ajax().update("form:messages", "form:tv-nomina");
		}
    	
    }
    
    public void actualizarPercepcion(DetNominaPercepcion percepcion) {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Percepción";
		List<String> listaDiasLaboralesEmpleado = null;
		BigDecimal diasLaboralesEmpleado = null;
		List<String> listaDiasNoLaboralesEmpleado = null;
		BigDecimal diasNoLaboralesEmpleado = null;
		BigDecimal diasTrabajados = null;
		DetEmpleado empleado = null;
		
		try {
			empleado = empleadoDAO.buscarPorRFC(this.nomina.getReceptor().getRfc());
			
			listaDiasLaboralesEmpleado = NominaSemanalBL.getDiasLaboralesPorSemana(empleado);
			listaDiasNoLaboralesEmpleado = NominaSemanalBL.getDiasNoLaboralesPorSemana(empleado);
			diasLaboralesEmpleado   = new BigDecimal(listaDiasLaboralesEmpleado.size()).setScale(2, BigDecimal.ROUND_HALF_UP);
			diasNoLaboralesEmpleado = new BigDecimal(listaDiasNoLaboralesEmpleado.size()).setScale(2, BigDecimal.ROUND_HALF_UP);
			diasTrabajados = percepcion.getCantidad();
			
			if(percepcion.getCantidad() != null && PercepcionBL.CVE_SUELDO.equalsIgnoreCase(percepcion.getClave())) {
				NominaSemanalBL.calcularSueldo(nomina, parametros,  diasLaboralesEmpleado, diasNoLaboralesEmpleado, diasTrabajados, null);
			}
			NominaSemanalBL.procesarISR(parametros, nomina);
			
			this.actualizar();
			
		} catch(Exception ex) {
			log.error("Problema para agregar la percepcion...", ex);
    		mensaje = "Hay un problema para agregar la percepción.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update("form:messages", "form:tv-nomina");
		}
    }
    
    public void eliminarPercepcion(DetNominaPercepcion percepcion) {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Percepción";
		
		try {
			NominaBL.eliminarPercepcion(nomina, percepcion);
			NominaSemanalBL.procesarISR(parametros, nomina);
			this.actualizar();
			
			mensaje = "Percepción eliminada correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			
			PrimeFaces.current().executeScript("PF('dlgEliminaPercepcion').hide();");
		} catch(SGPException ex) {
			log.warn("Problema para eliminar la percepcion: {}", ex.getMessage());
    		mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			mensaje = "Hay un problema para eliminar la percepcion seleccionada.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:tv-nomina");
		}
    }
    
    public void nuevoOtroPago() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Percepción";
		try {
			this.otroPago = NominaBL.nuevoOtroPago(this.nomina);
		} catch(SGPException ex) {
			log.warn("Problema para crear el pago: {}", ex.getMessage());
    		mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
		} catch(Exception ex) {
			mensaje = "Hay un problema para crear el pago.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
		}
    }
    
    public List<CatTipoOtroPago> buscaTipoOtroPago(String query) {
    	List<CatTipoOtroPago> tiposOtroPago = null;
    	
    	try {
    		final String queryLowerCase = query.toLowerCase();
    		tiposOtroPago = this.tiposOtroPago.stream()
    				.filter(o -> o.getClave().toLowerCase().contains(queryLowerCase)
    						|| o.getDescripcion().toLowerCase().contains(queryLowerCase) )
    				.collect(Collectors.toList())
    				;
    		
    	} catch(Exception ex) {
    		log.warn("No fue posible encontrar el tipo de otro pago solicitado: {}", query);
    		tiposOtroPago = new ArrayList<CatTipoOtroPago>();
    	}
    	
    	return tiposOtroPago;
    }
    
    public void agregarOtroPago() {
    	FacesMessage message = null;
    	Severity severity = null;
    	String mensaje = null;
    	String titulo = "Otro pago";
    	
    	try {
    		NominaBL.agregarOtroPago(this.nomina, this.otroPago);
			this.otroPago = new DetNominaOtroPago();
			
			this.actualizar();
			
			PrimeFaces.current().executeScript("PF('dlgAddOtroPago').hide()");
    		
    	} catch(Exception ex) {
    		log.error("Problema para agregar el pago...", ex);
    		mensaje = "Hay un problema para agregar el pago.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
    	} finally {
    		PrimeFaces.current().ajax().update("form:messages", "form:tv-nomina");
    	}
    }
    
    public void eliminarOtroPago(DetNominaOtroPago otroPago) {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Otro pago";
		
		try {
			NominaBL.eliminarOtroPago(this.nomina, otroPago);
			this.actualizar();
			
			mensaje = "Otro pago eliminado correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			
			PrimeFaces.current().executeScript("PF('dlgEliminaOtroPago').hide();");
		} catch(SGPException ex) {
			log.warn("Problema para eliminar el pago: {}", ex.getMessage());
    		mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			mensaje = "Hay un problema para eliminar el pago.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:tv-nomina");
		}
    }
    
    public void nuevaDeduccion() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Percepción";
		try {
			this.deduccion = NominaBL.nuevaDeduccion(this.nomina);
		} catch(SGPException ex) {
			log.warn("Problema para crear la deducción: {}", ex.getMessage());
    		mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
		} catch(Exception ex) {
			mensaje = "Hay un problema para crear la deducción.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
		}
    }
    
    public List<CatTipoDeduccion> buscaTipoDeduccion(String query) {
    	List<CatTipoDeduccion> tiposDeduccion = null;
    	
    	try {
    		final String queryLowerCase = query.toLowerCase();
    		tiposDeduccion = this.tiposDeduccion.stream()
    				.filter( d -> d.getClave().toLowerCase().contains(queryLowerCase)
    				      || d.getDescripcion().toLowerCase().contains(queryLowerCase) )
    				.collect(Collectors.toList())
    				;
    	} catch(Exception ex) {
    		log.warn("No fue posible encontrar el tipo de deducción solicitado: {}", query);
    		tiposDeduccion = new ArrayList<CatTipoDeduccion>();
    	}
    	
    	return tiposDeduccion;
    }
    
    public void agregarDeduccion() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Deducción";
		
		try {
			NominaBL.agregarDeduccion(this.nomina, this.deduccion);
			this.deduccion = new DetNominaDeduccion();
			
			this.actualizar();
			
			PrimeFaces.current().executeScript("PF('dlgAddDeduccion').hide();");
			
		} catch(Exception ex) {
			log.error("Problema para agregar la deducción...", ex);
    		mensaje = "Hay un problema para agregar la deducción.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
    	} finally {
    		PrimeFaces.current().ajax().update("form:messages", "form:tv-nomina");
    	}
    }
    
    public void eliminarDeduccion(DetNominaDeduccion deduccion) {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Deducción";
		
		try {
			NominaBL.eliminarDeduccion(this.nomina, deduccion);
			this.actualizar();
			
			mensaje = "Deducción eliminada correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
			
		} catch(SGPException ex) {
			log.warn("Problema para eliminar la deducción: {}", ex.getMessage());
    		mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch(Exception ex) {
			mensaje = "Hay un problema para eliminar la deducción seleccionada.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:tv-nomina");
		}
    }
    
    public void actualizar() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Nómina";
		
    	try {
    		NominaSemanalBL.procesarISR(this.parametros, this.nomina);
    		NominaBL.calcularTotales(nomina, parametros);
    	} catch(Exception ex) {
    		log.error("Problema para recalcular la nómina...", ex);
    		mensaje = "Hay un problema para actualizar la nómina.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
    	} finally {
    		PrimeFaces.current().ajax().update("form:messages", "form:dtNomina");
    	}
    }
    
    public void actualizarImporte(DetNominaPercepcion percepcion) {
    	PercepcionBL percepcionBO = null;
    	
    	percepcionBO = NominaSemanalBL.getPercepcionBusinessLogic(parametros, nomina, percepcion.getClave());
    	percepcionBO.setImporte(percepcion.getImporte());
    	percepcionBO.calcularExentoGravado();
    	
    	percepcion.setImporteExento(percepcionBO.getImporteExento());
    	percepcion.setImporteGravado(percepcionBO.getImporteGravado());
    	
    	this.actualizar();
    }

    public void guardarNominaEmpleado() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Nómina";
    	
    	try {
    		log.info("Guardando nomina...");
    		
    		if(this.listaNomina == null)
    			throw new SGPException("No hay información de nómina.");
    	
    		if(this.listaNomina.size() <= 0)
    			throw new SGPException("No hay información de nómina.");
    		
    		for(DetNomina nomina : listaNomina) {
    			
    			if(nomina.getId() == null)
    				nominaDAO.guardar(nomina);
    			else
    				nominaDAO.actualizar(nomina);
    			
    			log.info("Nomina: {}", nomina);
    		}
    		
    		if(nomina.getVacaciones() != null) {
    			nomina.getVacaciones().stream()
    			.forEach(v -> {
    				try {
    					vacacionesDAO.actualizar(v);
    				} catch(Exception ex) {
    					log.error("Problema para actualizar el periodo vacacional...", ex);
    				}
    			});
    		}    		
    		log.info("Nomina guardada correctamente.");
    		
    		mensaje = "La información se guardó correctamente.";
    		severity = FacesMessage.SEVERITY_INFO;
    	} catch(SGPException ex) {
    		mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
    	} catch(Exception ex) {
    		log.error("Problema para guardar la nómina...", ex);
			mensaje = "Hay un problema para guardar nómina.";
			severity = FacesMessage.SEVERITY_ERROR;
    	} finally {
    		message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
    	}
    }
    
    public void actualizarNomina() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Nómina";
		
		try {
			log.info("Actualizando nomina...");
			
			if(this.nomina.getId() == null) {
				nominaDAO.guardar(nomina);
			} else {
				nominaDAO.actualizar(nomina);
			}
			
			log.info("Nomina actualizada correctamente.");
			
			mensaje = "La información se actualizó correctamente.";
    		severity = FacesMessage.SEVERITY_INFO;
			PrimeFaces.current().executeScript("PF('dgEmpleado').hide()");
		} catch(SGPException ex) {
    		mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
    	} catch(Exception ex) {
    		log.error("Problema para guardar la nómina...", ex);
			mensaje = "Hay un problema para guardar nómina.";
			severity = FacesMessage.SEVERITY_ERROR;
    	} finally {
    		message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages");
    	}
    	
    }
    
    public void eliminarNomina() {
    	FacesMessage message = null;
    	Severity severity = null;
		String mensaje = null;
		String titulo = "Nómina";
		
		try {
			log.info("Eliminando información de la nomina...");
			
			if(this.nomina.getUuid() != null && this.nomina.getUuid().trim().length() > 0) {
				throw new SGPException("La nomina del empleado ya se encuentra timbrada. No es posible eliminar la información.");
			}
			
			if(nomina.getId() == null)
				throw new SGPException("La nómina del empleado no está guardada");
			
			if(nomina.getUuid() != null)
				throw new SGPException("La nómina del empleado ya se encuentra timbrada. No es posible eliminarla.");
			
			this.nominaDAO.eliminar(nomina);
			this.listaNomina.remove(nomina);
			
			log.info("Nomina actualizada correctamente.");
			
			mensaje = "La información se actualizó correctamente.";
    		severity = FacesMessage.SEVERITY_INFO;
			PrimeFaces.current().executeScript("PF('dgEmpleado').hide()");
		} catch(SGPException ex) {
    		mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
    	} catch(Exception ex) {
    		log.error("Problema para guardar la nómina...", ex);
			mensaje = "Hay un problema para guardar nómina.";
			severity = FacesMessage.SEVERITY_ERROR;
    	} finally {
    		message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("form:messages", "form:dtNomina");
    	}
    	
    }
    
    public void descargarBitacora() {
    	this.bitacora = BitacoraUIAppender.getMensajes().stream()
    			.collect(Collectors.joining());
    	
    	byte bytes[] = this.bitacora.getBytes();
    	InputStream input = new ByteArrayInputStream(bytes);
    	this.file = DefaultStreamedContent.builder()
    			.contentType("text/plain")
    			.name("Bitacora.txt")
				.stream(() -> input)
				.build();
    }
    
    public void cargarBitacora() {
    	
    }
    
    public List<DetNominaOtroPago> filtraOtroPagos() {
    	return this.nomina.getOtrosPagos().stream()
    			.filter(d -> this.detalle || Boolean.TRUE.equals(d.getInformar()))
    			.collect(Collectors.toList());
    }
    
    public List<DetNominaDeduccion> filtrarDeducciones() {
    	return this.nomina.getDeducciones().stream()
                .filter(d -> this.detalle || Boolean.TRUE.equals(d.getInformar()))
                .collect(Collectors.toList());
    }
    
    public List<CatEmpresa> getLstEmpresas() {
        return lstEmpresas;
    }

    public void setLstEmpresas(List<CatEmpresa> lstEmpresas) {
        this.lstEmpresas = lstEmpresas;
    }

    public CatEmpresa getEmpresaSelected() {
        return empresaSelected;
    }

    public void setEmpresaSelected(CatEmpresa empresaSelected) {
        this.empresaSelected = empresaSelected;
    }

    public DetNomina getNomina() {
        return nomina;
    }

    public void setNomina(DetNomina nomina) {
        this.nomina = nomina;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(Date juevesPasado) {
        this.periodoInicio = juevesPasado;
    }

    public Date getPeriodoFin() {
        return periodoFin;
    }

    public void setPeriodoFin(Date periodoFin) {
        this.periodoFin = periodoFin;
    }

    public List<DetNomina> getListaNomina() {
        return listaNomina;
    }

    public void setListaNomina(List<DetNomina> listaNomina) {
        this.listaNomina = listaNomina;
    }

	public Integer getSemana() {
		return semana;
	}

	public void setSemana(Integer semana) {
		this.semana = semana;
	}

	public Boolean getDetalle() {
		return detalle;
	}

	public void setDetalle(Boolean detalle) {
		this.detalle = detalle;
	}

	public DetNominaDeduccion getDeduccion() {
		return deduccion;
	}

	public void setDeduccion(DetNominaDeduccion deduccion) {
		this.deduccion = deduccion;
	}

	public DetNominaPercepcion getPercepcion() {
		return percepcion;
	}

	public void setPercepcion(DetNominaPercepcion percepcion) {
		this.percepcion = percepcion;
	}

	public DetNominaOtroPago getOtroPago() {
		return otroPago;
	}

	public void setOtroPago(DetNominaOtroPago otroPago) {
		this.otroPago = otroPago;
	}

	public List<Integer> getSemanasDelAnio() {
		return semanasDelAnio;
	}

	public void setSemanasDelAnio(List<Integer> semanasDelAnio) {
		this.semanasDelAnio = semanasDelAnio;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public List<Asistencia> getAsistencias() {
		return asistencias;
	}

	public void setAsistencias(List<Asistencia> asistencias) {
		this.asistencias = asistencias;
	}

	public List<CatPercepcion> getCatalogoPercepciones() {
		return catalogoPercepciones;
	}

	public void setCatalogoPercepciones(List<CatPercepcion> catalogoPercepciones) {
		this.catalogoPercepciones = catalogoPercepciones;
	}

	public CatPercepcion getPercepcionCatalogo() {
		return percepcionCatalogo;
	}

	public void setPercepcionCatalogo(CatPercepcion percepcionCatalogo) {
		this.percepcionCatalogo = percepcionCatalogo;
	}

	public List<CatTipoPercepcion> getTiposPercepcion() {
		return tiposPercepcion;
	}

	public void setTiposPercepcion(List<CatTipoPercepcion> tiposPercepcion) {
		this.tiposPercepcion = tiposPercepcion;
	}

	public String getBitacora() {
		return bitacora;
	}

	public void setBitacora(String bitacora) {
		this.bitacora = bitacora;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public List<DetVacaciones> getPeriodosVacacionales() {
		return periodosVacacionales;
	}

	public void setPeriodosVacacionales(List<DetVacaciones> periodosVacacionales) {
		this.periodosVacacionales = periodosVacacionales;
	}

	public DetVacaciones getPeriodoVacacional() {
		return periodoVacacional;
	}

	public void setPeriodoVacacional(DetVacaciones periodoVacacional) {
		this.periodoVacacional = periodoVacacional;
	}
}
