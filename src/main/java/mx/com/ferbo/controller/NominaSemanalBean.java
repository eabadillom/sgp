package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
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

import mx.com.ferbo.business.nomina.AsistenciaBL;
import mx.com.ferbo.business.nomina.NominaBL;
import mx.com.ferbo.business.nomina.NominaSemanalBL;
import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.business.percepcion.AbstractPercepcion;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.EmpresaDAO;
import mx.com.ferbo.dao.n.NominaDAO;
import mx.com.ferbo.dto.ui.Asistencia;
import mx.com.ferbo.model.CatEmpresa;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaOtroPago;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetPercepcionEmpleado;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.model.sat.CatTipoOtroPago;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
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
    
    private List<CatEmpresa> lstEmpresas;
    private List<CatTipoPercepcion> tiposPercepcion;
    private List<CatTipoDeduccion> tiposDeduccion;
    private List<CatTipoOtroPago> tiposOtroPago;
    private CatEmpresa empresaSelected;
    private DetNomina nomina;
    private DetNominaPercepcion percepcion;
    private DetNominaOtroPago otroPago;
    private DetNominaDeduccion deduccion;
    
    private Integer anio;
    private Date fecha;
    private Date periodoInicio;
    private Date periodoFin;
    private Integer semana;
    
    private List<DetNomina> listaNomina;
    private List<Integer> semanasDelAnio;
    private List<Asistencia> asistencias;
    
    private Boolean detalle = true;

	public NominaSemanalBean() {
		listaNomina = new ArrayList<>();

		empleadoDAO = new EmpleadoDAO(DetEmpleado.class);
		empresaDAO = new EmpresaDAO(CatEmpresa.class);
		nominaDAO = new NominaDAO(DetNomina.class);
	}
    
    @PostConstruct
    public void init() {
        log.info("====================== entrada init NominaSemanalBean ======================");
        this.configuraPeriodo();
        lstEmpresas = empresaDAO.buscarActivo();

        log.info("Inicio del periodo: {}", periodoFin);
        log.info("Fin del periodo: {}", periodoInicio);
        try {
			this.nomina = NominaBL.build(NominaBL.TP_NOMINA_ORDINARIA, parametros, null);
		} catch (SGPException ex) {
			log.info("Problema para generar el objeto nómina...", ex);
		}
        this.percepcion = new DetNominaPercepcion();
        this.otroPago = new DetNominaOtroPago();
        this.deduccion = new DetNominaDeduccion();
        
        this.fecha = DateUtil.now();
        this.anio = DateUtil.getAnio(fecha);
        this.semanasDelAnio = DateUtil.semanasDelAnio(this.anio);
        this.semana = DateUtil.getSemanaAnio(this.fecha);
    }
    
    public void configuraPeriodo() {
    	this.fecha = DateUtil.now();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-06:00"));
        cal.setTimeZone(TimeZone.getTimeZone("GMT-06:00"));
        cal.setTime(fecha);

        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        periodoFin = cal.getTime();
        
        
        cal.add(Calendar.DATE, -6);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        periodoInicio = cal.getTime();
        
        
    }
    
    public void calculaSemanas() {
    	this.semanasDelAnio = DateUtil.semanasDelAnio(this.anio);
    	this.semana = null;
    	this.periodoInicio = null;
    	this.periodoFin = null;
    }
    
    public void calculaFechasPeriodo() {
    	this.periodoInicio = DateUtil.getLunesDeSemanaDate(this.anio, this.semana);
    	this.periodoFin = new Date(this.periodoInicio.getTime());
    	this.periodoFin = DateUtil.addDay(this.periodoFin, 6);
    	
    	log.info("Fecha Inicio: {}", this.periodoInicio);
    	log.info("Fecha Fin: {}", this.periodoFin);
    }

	public void calculaFechaFin() {
    	log.debug("Fecha Inicio: {}", this.periodoInicio);
    	log.debug("Fecha Fin: {}", this.periodoFin);
    	this.periodoFin = new Date(this.periodoInicio.getTime());
    	this.periodoFin = DateUtil.addDay(this.periodoFin, 6);
    	DateUtil.setTime(this.periodoFin, 23, 59, 59, 999);
    	
    	this.semana = DateUtil.getSemanaAnio(this.periodoInicio);
    	
    	log.info("Fecha Inicio: {}", this.periodoInicio);
    	log.info("Fecha Fin: {}", this.periodoFin);
    }
    
    public void calculaFechaInicio() {
    	log.debug("Fecha Inicio: {}", this.periodoInicio);
    	log.debug("Fecha Fin: {}", this.periodoFin);
    	this.periodoInicio = new Date(this.periodoFin.getTime());
    	this.periodoInicio = DateUtil.addDay(this.periodoInicio, -6);
    	DateUtil.setTime(this.periodoInicio, 0, 0, 0, 0);
    	
    	this.semana = DateUtil.getSemanaAnio(this.periodoInicio);
    	
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
    		this.listaNomina.clear();
    		listaEmpleados = empleadoDAO.buscarActivoEmpresaIngreso(empresaSelected.getIdEmpresa(), this.periodoInicio, this.periodoFin);
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
    		PrimeFaces.current().ajax().update(":formNomina:messages", "formNomina:dtNomina");
    		PrimeFaces.current().executeScript("PF('empresaDialog').hide()");
    	}
    }
    
    private void procesaListaEmpleados(List<DetEmpleado> listaEmpleados) {
    	DetNomina nomina = null;
    	try {
    		this.parametros = new ParametrosNomina();
    		this.parametros.cargar(periodoInicio, periodoFin);
    		
    		this.tiposPercepcion = this.parametros.getTiposPercepcion();
    		this.tiposOtroPago = this.parametros.getTiposOtroPago();
    		this.tiposDeduccion = this.parametros.getTiposDeduccion();
    		
    		for (DetEmpleado empleado : listaEmpleados) {
    			
    			nomina = nominaDAO.buscarPorPeriodoEmpleado(DateUtil.toLocalDate(this.parametros.getPeriodoInicio()), DateUtil.toLocalDate(this.parametros.getPeriodoFin()), empleado.getDatoEmpresa().getRfc());
    			
    			if(nomina == null)
    				nomina = this.procesaEmpleado(empleado);
    			
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
    	DetEmpleado empleado = empleadoDAO.buscarPorRFC(nomina.getReceptor().getRfc());
    	
    	Map<String, DetRegistro> mapAsistencias = NominaSemanalBL.getAsistencias(empleado, this.parametros);
    	List<Asistencia> asistencias = AsistenciaBL.getAsistenciaSemanal(mapAsistencias);
    	
    	this.asistencias = asistencias;
    	
    	//Si el ID de Nómina es NULL, entonces el objeto de nómina fue generado por el proceso de cálculo y
    	//la información debería estar completamente cargada en memoria.
    	if(nomina.getId() == null) {
    		this.nomina = nomina;
    		return;
    	}
    	
    	//En el caso del ID de Nómina diferente de NULL, el objeto de nómina fue extraido por consulta a la
    	//base de datos sin el detalle completo, por lo que debe extraerse a través del DAO.
    	this.nomina = nominaDAO.buscarPorId(nomina.getId());
    }

    public String statusNomina(DetNomina nomina) {
    	if(nomina == null)
    		return "";
    	
        return ManageStatus.getEstadoEmpleadoEmpresa((nomina.getId() == null ? (short) 2 : (short) 1));
    }
    
    public void nuevaPercepcion() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Percepción";
		try {
			this.percepcion = NominaBL.nuevaPercepcion(this.nomina);
		} catch(SGPException ex) {
			log.warn("Problema para crear la percepcion: {}", ex.getMessage());
    		mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("formNomina:messages");
		} catch(Exception ex) {
			mensaje = "Hay un problema para crear la percepcion.";
			severity = FacesMessage.SEVERITY_ERROR;
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("formNomina:messages");
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
			NominaSemanalBL.procesarISR(this.nomina, this.parametros);
			
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
    		PrimeFaces.current().ajax().update("formNomina:messages", "formNomina:tv-nomina");
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
			
			if(percepcion.getCantidad() != null && AbstractPercepcion.CVE_SUELDO.equalsIgnoreCase(percepcion.getClave())) {
				NominaSemanalBL.calcularSueldo(nomina, parametros,  diasLaboralesEmpleado, diasNoLaboralesEmpleado, diasTrabajados, null);
			}
			NominaSemanalBL.procesarISR(nomina, parametros);
			
			this.actualizar();
			
		} catch(Exception ex) {
			log.error("Problema para agregar la percepcion...", ex);
    		mensaje = "Hay un problema para agregar la percepción.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} finally {
			PrimeFaces.current().ajax().update("formNomina:messages", "formNomina:tv-nomina");
		}
    }
    
    public void eliminarPercepcion(DetNominaPercepcion percepcion) {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Percepción";
		
		try {
			NominaBL.eliminarPercepcion(nomina, percepcion);
			NominaSemanalBL.procesarISR(nomina, parametros);
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
			PrimeFaces.current().ajax().update("formNomina:messages", "formNomina:tv-nomina");
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
			PrimeFaces.current().ajax().update("formNomina:messages");
		} catch(Exception ex) {
			mensaje = "Hay un problema para crear el pago.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("formNomina:messages");
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
    		PrimeFaces.current().ajax().update("formNomina:messages", "formNomina:tv-nomina");
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
			PrimeFaces.current().ajax().update("formNomina:messages", "formNomina:tv-nomina");
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
			PrimeFaces.current().ajax().update("formNomina:messages");
		} catch(Exception ex) {
			mensaje = "Hay un problema para crear la deducción.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update("formNomina:messages");
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
    		PrimeFaces.current().ajax().update("formNomina:messages", "formNomina:tv-nomina");
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
			PrimeFaces.current().ajax().update("formNomina:messages", "formNomina:tv-nomina");
		}
    }
    
    public void actualizar() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Nómina";
		
    	try {
    		NominaBL.calcularTotales(nomina, parametros);
    	} catch(Exception ex) {
    		log.error("Problema para recalcular la nómina...", ex);
    		mensaje = "Hay un problema para actualizar la nómina.";
			severity = FacesMessage.SEVERITY_ERROR;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
    	} finally {
    		PrimeFaces.current().ajax().update(":formNomina:messages", ":formNomina:dtNomina");
    	}
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
			PrimeFaces.current().ajax().update(":formNomina:messages");
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
			PrimeFaces.current().ajax().update(":formNomina:messages");
    	}
    	
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
}
