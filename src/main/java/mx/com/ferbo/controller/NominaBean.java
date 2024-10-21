package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.business.NominaSemanalBL;
import mx.com.ferbo.dao.n.ConceptoDAO;
import mx.com.ferbo.dao.n.CuotaIMSSDAO;
import mx.com.ferbo.dao.n.DiaNoLaboralDAO;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.EmpresaDAO;
import mx.com.ferbo.dao.n.MetodoPagoDAO;
import mx.com.ferbo.dao.n.NominaDAO;
import mx.com.ferbo.dao.n.PercepcionEmpleadoDAO;
import mx.com.ferbo.dao.n.PercepcionesDAO;
import mx.com.ferbo.dao.n.PeriodicidadPagoDAO;
import mx.com.ferbo.dao.n.RegimenFiscalDAO;
import mx.com.ferbo.dao.n.SubsidioDAO;
import mx.com.ferbo.dao.n.TarifaISRDAO;
import mx.com.ferbo.dao.n.TipoDeduccionDAO;
import mx.com.ferbo.dao.n.TipoOtroPagoDAO;
import mx.com.ferbo.dao.n.TipoPercepcionDAO;
import mx.com.ferbo.dao.n.UnidadSATDAO;
import mx.com.ferbo.dao.n.UsoCFDIDAO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import mx.com.ferbo.model.CatCuotaIMSS;
import mx.com.ferbo.model.CatDiaNoLaboral;
import mx.com.ferbo.model.CatEmpresa;
import mx.com.ferbo.model.CatPercepciones;
import mx.com.ferbo.model.CatPeriodicidadPago;
import mx.com.ferbo.model.CatSubsidio;
import mx.com.ferbo.model.CatTarifaISR;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaEmisor;
import mx.com.ferbo.model.DetNominaReceptor;
import mx.com.ferbo.model.DetPercepcionEmpleado;
import mx.com.ferbo.model.sat.CatConcepto;
import mx.com.ferbo.model.sat.CatMetodoPago;
import mx.com.ferbo.model.sat.CatRegimenFiscal;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.model.sat.CatTipoOtroPago;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import mx.com.ferbo.model.sat.CatUnidadSAT;
import mx.com.ferbo.model.sat.CatUsoCFDI;
import mx.com.ferbo.util.DateUtils;
import mx.com.ferbo.util.SGPException;

@Named(value = "nominaBean")
@ViewScoped
public class NominaBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(NominaBean.class);

    private EmpleadoDAO empleadoDAO;
    private EmpresaDAO empresaDAO;
    private DiaNoLaboralDAO diaNLDAO = null;
    private PercepcionesDAO catPercepcionesDAO;
    private TarifaISRDAO tarifaISRDAO;
    private SubsidioDAO subsidioDAO;
    private NominaDAO nominaDAO;
    private MetodoPagoDAO metodoPagoDAO;
    private ConceptoDAO conceptoDAO;
    private UnidadSATDAO unidadSATDAO;
    private PeriodicidadPagoDAO periodicidadDAO;
    private RegimenFiscalDAO regimenFiscalDAO;
    private UsoCFDIDAO usoCfdiDAO;
    private TipoPercepcionDAO tipoPercepcionDAO = null;
    private TipoDeduccionDAO tipoDeduccionDAO = null;
    private CuotaIMSSDAO cuotasIMSSDAO = null;
    private TipoOtroPagoDAO tipoOtroPagoDAO = null;
    private PercepcionEmpleadoDAO percepcionEmpleadoDAO = null;
    
    private List<DetEmpleadoDTO> lstEmpleadosTmp;
    private List<CatEmpresa> lstEmpresas;
    private List<CatDiaNoLaboral> diasNoLaborales;
    private CatPercepciones parametrosPercepciones;
    private List<CatTarifaISR> tablaISRsemanal;
    private List<CatTarifaISR> tablaISRmensual;
    private List<CatSubsidio> tablaSubsidioSemanal;
    private List<CatSubsidio> tablaSubsidioMensual;
    private List<CatTipoPercepcion> tiposPercepcion;
    private List<CatTipoDeduccion> tiposDeduccion;
    private List<CatCuotaIMSS> cuotasIMSS;
    private List<CatTipoOtroPago> tiposOtroPago;
    private CatEmpresa empresaSelected;
    private DetNomina nomina;
    private CatMetodoPago metodoPago;
    private CatConcepto concepto;
    private CatUnidadSAT unidadSAT;
    private CatPeriodicidadPago periodicidad;
    private CatRegimenFiscal regimenFiscalReceptor;
    private CatUsoCFDI usoCFDI;
    
    private Date fecha;
    private Integer year;

    private Date periodoInicio;
    private Date periodoFin;
    private Date fechaInicioAnio;
    private Date fechafinAnio;
    private Integer semana;
    private Boolean esUltimaSemanaMes;

    private List<DetNomina> listaNomina;

	public NominaBean() {
		log.info("====================== entrada constructor nominaBean ======================");
		lstEmpleadosTmp = new ArrayList<>();

		lstEmpresas = new ArrayList<>();

		listaNomina = new ArrayList<>();

		empleadoDAO = new EmpleadoDAO(DetEmpleado.class);
		empresaDAO = new EmpresaDAO(CatEmpresa.class);
		diaNLDAO = new DiaNoLaboralDAO();
		catPercepcionesDAO = new PercepcionesDAO(CatPercepciones.class);
		tarifaISRDAO = new TarifaISRDAO(CatTarifaISR.class);
		subsidioDAO = new SubsidioDAO(CatSubsidio.class);
		nominaDAO = new NominaDAO(DetNomina.class);
		metodoPagoDAO = new MetodoPagoDAO(CatMetodoPago.class);
		conceptoDAO = new ConceptoDAO(CatConcepto.class);
		unidadSATDAO = new UnidadSATDAO(CatUnidadSAT.class);
		periodicidadDAO = new PeriodicidadPagoDAO(CatPeriodicidadPago.class);
		regimenFiscalDAO = new RegimenFiscalDAO(CatRegimenFiscal.class);
		usoCfdiDAO = new UsoCFDIDAO(CatUsoCFDI.class);
		tipoPercepcionDAO = new TipoPercepcionDAO();
		tipoDeduccionDAO = new TipoDeduccionDAO();
		cuotasIMSSDAO = new CuotaIMSSDAO();
		tipoOtroPagoDAO = new TipoOtroPagoDAO();
		percepcionEmpleadoDAO = new PercepcionEmpleadoDAO();

		log.info("====================== salida constructor nominaBean ======================");
		log.info("Largo de Lista constructor {}", listaNomina.size());
	}
    
    @PostConstruct
    public void init() {
        log.info("====================== entrada init nominaBean ======================");
        this.configuraPeriodo();
        lstEmpresas = empresaDAO.buscarActivo();

        log.info("Miercoles pasado: {}", periodoFin);
        log.info("Jueves pasado: {}", periodoInicio);
        this.nomina = this.iniciaNominaDTOVacio();
    }
    
    public void configuraPeriodo() {
    	fecha = DateUtils.now();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-06:00"));
        cal.setTimeZone(TimeZone.getTimeZone("GMT-06:00"));
        cal.setTime(fecha);
        year = cal.get(Calendar.YEAR);

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

    private DetNomina iniciaNominaDTOVacio() {
    	DetNomina nomina = new DetNomina();
    	nomina.setEmisor(new DetNominaEmisor());
    	nomina.setReceptor(new DetNominaReceptor());
    	nomina.setConceptos(new ArrayList<>());
    	nomina.setPercepciones(new ArrayList<>());
    	nomina.setDeducciones(new ArrayList<>());
		return nomina;
	}

	public void calculaFechaFin() {
		Integer anioActual = null;
    	log.info("Fecha Inicio: {}", this.periodoInicio);
    	log.info("Fecha Fin: {}", this.periodoFin);
    	this.periodoFin = new Date(this.periodoInicio.getTime());
    	this.periodoFin = DateUtils.addDay(this.periodoFin, 6);
    	DateUtils.setTime(this.periodoFin, 23, 59, 59, 999);
    	
    	this.semana = DateUtils.getSemanaAnio(this.periodoInicio);
    	
    	anioActual = DateUtils.getAnio(periodoInicio);
    	this.fechaInicioAnio = DateUtils.getDate(anioActual, DateUtils.ENERO, 1);
    	DateUtils.setTime(this.fechaInicioAnio, 0, 0, 0, 0);
    	this.fechafinAnio = DateUtils.getDate(anioActual, DateUtils.DICIEMBRE, 31);
		DateUtils.setTime(this.fechafinAnio, 23, 59, 59, 000);
		
    	log.info("Fecha Inicio: {}", this.periodoInicio);
    	log.info("Fecha Fin: {}", this.periodoFin);
    	
    	esUltimaSemanaMes = NominaSemanalBL.esUltimaSemanaMes(periodoInicio, periodoFin);
    }
    
    public void calculaFechaInicio() {
    	Integer anioActual = null;
    	log.info("Fecha Inicio: {}", this.periodoInicio);
    	log.info("Fecha Fin: {}", this.periodoFin);
    	this.periodoInicio = new Date(this.periodoFin.getTime());
    	this.periodoInicio = DateUtils.addDay(this.periodoInicio, -6);
    	DateUtils.setTime(this.periodoInicio, 0, 0, 0, 0);
    	
    	this.semana = DateUtils.getSemanaAnio(this.periodoInicio);
    	
    	anioActual = DateUtils.getAnio(periodoInicio);
    	this.fechaInicioAnio = DateUtils.getDate(anioActual, DateUtils.ENERO, 1);
    	DateUtils.setTime(this.fechaInicioAnio, 0, 0, 0, 0);
    	this.fechafinAnio = DateUtils.getDate(anioActual, DateUtils.DICIEMBRE, 31);
		DateUtils.setTime(this.fechafinAnio, 23, 59, 59, 000);
		
    	log.info("Fecha Inicio: {}", this.periodoInicio);
    	log.info("Fecha Fin: {}", this.periodoFin);
    	
    	this.year = new Integer(anioActual);
    }

    public void calculandoNomina() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Nómina";
		
    	List<DetEmpleado> listaEmpleados = null;
    	try {
    		this.listaNomina.clear();
    		this.listaNomina = nominaDAO.buscarPorPeriodo(
    				this.periodoInicio.toInstant().atZone(ZoneId.of("GMT-6")).toLocalDate(),
    				this.periodoFin.toInstant().atZone(ZoneId.of("GMT-6")).toLocalDate()
    		);
    		
    		if(this.listaNomina.size() <= 0) {
    			listaEmpleados = empleadoDAO.buscarActivoEmpresaIngreso(empresaSelected.getIdEmpresa(), this.periodoInicio);
    			procesaListaEmpleados(listaEmpleados);
    		}
    		
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
    		this.diasNoLaborales = diaNLDAO.buscarPorPeriodo("MX", periodoInicio, periodoFin);
    		this.parametrosPercepciones = catPercepcionesDAO.buscarActual(this.periodoInicio);
    		this.tablaISRsemanal = tarifaISRDAO.buscar(fechaInicioAnio, fechafinAnio, "s");
    		this.tablaSubsidioSemanal = subsidioDAO.buscar(fechaInicioAnio, fechafinAnio, "s");
    		this.metodoPago = this.metodoPagoDAO.buscarPorId("PUE");
    		this.concepto = this.conceptoDAO.buscarPorId("84111505");
    		this.unidadSAT = this.unidadSATDAO.buscarPorId("ACT");
    		this.periodicidad = this.periodicidadDAO.buscarPorId("02");
    		this.regimenFiscalReceptor = this.regimenFiscalDAO.buscarPorId("605");
    		this.usoCFDI = this.usoCfdiDAO.buscarPorId("CN01");
    		this.tiposPercepcion = this.tipoPercepcionDAO.buscarTodos();
    		this.tiposDeduccion = this.tipoDeduccionDAO.buscarTodos();
    		this.cuotasIMSS = this.cuotasIMSSDAO.buscarPorPeriodo(fechaInicioAnio, fechafinAnio);
    		this.tiposOtroPago = this.tipoOtroPagoDAO.buscarTodos();
    		
    		if(esUltimaSemanaMes) {
    			this.tablaISRmensual = tarifaISRDAO.buscar(fechaInicioAnio, fechafinAnio, "m");
        		this.tablaSubsidioMensual = subsidioDAO.buscar(fechaInicioAnio, fechafinAnio, "m");
    		}
    		
    		for (DetEmpleado empleado : listaEmpleados) {
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
    	log.info("Empleado: {} {} {}, Salario diario: {}", empleado.getNombre(), empleado.getPrimerAp(), empleado.getSegundoAp(), empleado.getSueldoDiario());
    	percepcionesEmpleado = percepcionEmpleadoDAO.buscarPorEmpleado(empleado.getIdEmpleado());
    	empleado.setPercepcionesEmpleado(percepcionesEmpleado);
    	
		nominaSemanalBO = new NominaSemanalBL(empleado, periodoInicio, periodoFin);
		nominaSemanalBO.setDiasNoLaborales(this.diasNoLaborales);
		nominaSemanalBO.setParametrosPercepciones(parametrosPercepciones);
		nominaSemanalBO.setTablaISRSemanal(this.tablaISRsemanal);
		nominaSemanalBO.setTablaISRMensual(tablaISRmensual);
		nominaSemanalBO.setTablaSubsidioSemanal(this.tablaSubsidioSemanal);
		nominaSemanalBO.setTablaSubsidioMensual(this.tablaSubsidioMensual);
		nominaSemanalBO.setMetodoPago(this.metodoPago);
		nominaSemanalBO.setConcepto(this.concepto);
		nominaSemanalBO.setUnidadSAT(this.unidadSAT);
		nominaSemanalBO.setAnio(this.year);
		nominaSemanalBO.setPeriodicidad(this.periodicidad);
		nominaSemanalBO.setRegimenFiscalReceptor(this.regimenFiscalReceptor);
		nominaSemanalBO.setUsoCFDI(this.usoCFDI);
		nominaSemanalBO.setTiposPercepcion(this.tiposPercepcion);
		nominaSemanalBO.setTiposDeduccion(this.tiposDeduccion);
		nominaSemanalBO.setCuotasIMSS(this.cuotasIMSS);
		nominaSemanalBO.setTiposOtroPago(this.tiposOtroPago);
		nomina = nominaSemanalBO.calculoNomina();
		return nomina;
    }
    
    public void cargaEmpleadoNomina() {
    	log.info("Cargando información de nómina: {}", this.nomina);
    	
    	//Si el ID de Nómina es NULL, entonces el objeto de nómina fue generado por el proceso de cálculo y
    	//la información debería estar completamente cargada en memoria.
    	if(this.nomina.getId() == null)
    		return;
    	
    	//En el caso del ID de Nómina diferente de NULL, el objeto de nómina fue extraido por consulta a la
    	//base de datos sin el detalle completo, por lo que debe extraerse a través del DAO.
    	this.nomina = nominaDAO.buscarPorId(this.nomina.getId());
    }
    
    public void actualizar() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Nómina";
		
    	BigDecimal totalPercepciones = null;
    	BigDecimal totalOtrosPagos = null;
    	BigDecimal totalDeducciones = null;
    	BigDecimal subtotal = null;
    	BigDecimal descuentos = null;
    	BigDecimal total = null;
    	
    	try {
    		log.info("Actualizando {}", this.nomina);
    		
    		totalPercepciones = this.nomina.getPercepciones().stream()
    				.map(item -> item.getImporteExcento().add(item.getImporteGravado()))
    				.reduce(BigDecimal.ZERO, BigDecimal :: add);
    		
    		totalOtrosPagos = this.nomina.getOtrosPagos().stream()
    				.map(item -> item.getImporte())
    				.reduce(BigDecimal.ZERO, BigDecimal :: add);
    		
    		totalDeducciones = this.nomina.getDeducciones().stream()
    				.filter(d -> d.getProcesar())
    				.map(item -> item.getImporte())
    				.reduce(BigDecimal.ZERO, BigDecimal :: add);
    		
    		subtotal = totalPercepciones.add(BigDecimal.ZERO);
    		descuentos = totalDeducciones.subtract(totalOtrosPagos);
    		total = subtotal.subtract(descuentos);
    		
    		log.info("Subtotal recalculado: {}", subtotal);
    		log.info("Descuentos: {}", descuentos);
    		log.info("Total: {}", total);
    		this.nomina.setSubtotal(subtotal);
    		this.nomina.setDescuento(descuentos);
    		this.nomina.setTotal(total);
    		
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
    			log.info("Nomina: {}", nomina);
    			nominaDAO.guardar(nomina);
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
			
			if(this.nomina == null)
				throw new SGPException("No hay información de nómina.");
			nominaDAO.actualizar(nomina);
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
    
    public List<DetEmpleadoDTO> getLstEmpleadosTmp() {
        return lstEmpleadosTmp;
    }

    public void setLstEmpleadosTmp(List<DetEmpleadoDTO> lstEmpleadosTmp) {
        this.lstEmpleadosTmp = lstEmpleadosTmp;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

}
