package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
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

import mx.com.ferbo.business.nomina.NominaBL;
import mx.com.ferbo.business.nomina.NominaExtraordinariaBL;
import mx.com.ferbo.business.nomina.NominaPeriodoBL;
import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.business.nomina.PercepcionBL;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.EmpresaDAO;
import mx.com.ferbo.dao.n.NominaDAO;
import mx.com.ferbo.dao.n.PercepcionDAO;
import mx.com.ferbo.dao.n.PeriodicidadPagoDAO;
import mx.com.ferbo.model.CatEmpresa;
import mx.com.ferbo.model.CatPercepcion;
import mx.com.ferbo.model.CatPeriodicidadPago;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPeriodo;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.ManageStatus;
import mx.com.ferbo.util.SGPException;

@Named(value = "nominaE")
@ViewScoped
public class NominaExtraordinariaBean implements Serializable {

	private static final long serialVersionUID = -7354838193241382936L;
	private static Logger log = LogManager.getLogger(NominaExtraordinariaBean.class);
	
	private Integer anio = null;
	private Date fecha = null;
	private List<Integer> semanasDelAnio = null;
	private Date periodoInicio = null;
	private Date periodoFin = null;
	private DetNominaPeriodo nominaPeriodo = null;
	private DetNomina nomina = null;
	private NominaExtraordinariaBL nominaBO = null;
	
	private CatEmpresa empresa = null;
	private List<CatEmpresa> empresas = null;
	private EmpresaDAO empresaDAO = null;
	
	private CatPeriodicidadPago periodicidad = null;
	private PeriodicidadPagoDAO periodicidadDAO = null;
	
	private EmpleadoDAO empleadoDAO = null;
	private NominaDAO nominaDAO = null;
	private List<DetNomina> listaNomina = null;
	private List<DetNomina> listaNominaSelected = null;
	private ParametrosNomina parametros = null;
	
	private CatPercepcion percepcion = null;
	private List<CatPercepcion> percepciones = null;
	private PercepcionDAO percepcionDAO = null;
	
	private DetNominaPercepcion nomPercepcion = null;
	private List<CatTipoPercepcion> tiposPercepcion = null;

    public NominaExtraordinariaBean() {
    	log.info("Entrando al constructor de NominaExtraordinariaBean");
    	empresaDAO = new EmpresaDAO();
    	periodicidadDAO = new PeriodicidadPagoDAO();
    	empleadoDAO = new EmpleadoDAO();
    	nominaDAO = new NominaDAO();
    	percepcionDAO = new PercepcionDAO();
	}
    
    @PostConstruct
    public void init() {
    	log.info("====================== Entrada a nómina extraordinaria ======================");
    	this.empresas = empresaDAO.buscarActivo();
    	this.listaNomina = new ArrayList<DetNomina>();
    	this.listaNominaSelected = new ArrayList<DetNomina>();
    	//De acuerdo a la definición del complemento de nómina 1.2, la periodicidad de pago para
    	//nóminas extraordinarias es 99 (Otro).
    	this.periodicidad = periodicidadDAO.buscarPorId("99");
    	this.nomPercepcion = PercepcionBL.build();
    	this.percepciones = percepcionDAO.buscarTodos();
    }
    
    public String statusNomina(DetNomina nomina) {
    	if(nomina == null)
    		return "";
    	
        return ManageStatus.getEstadoEmpleadoEmpresa((nomina.getId() == null ? (short) 2 : (short) 1));
    }
    
    public void calculaPeriodo() {
    	Integer anio = null;
    	try {
    		if(this.empresa == null)
    			throw new SGPException("Debe seleccionar una empresa");
    		
    		if(this.nominaPeriodo == null)
    			anio = DateUtil.getAnio(DateUtil.now());
    		else
    			anio = this.nominaPeriodo.getKey().getAnio();
    		this.nominaPeriodo = NominaPeriodoBL.build(this.empresa, NominaBL.TP_NOMINA_EXTRAORDINARIA, this.periodicidad, anio);
    		this.nominaPeriodo.setPeriodoInicio(DateUtil.setAnio(this.nominaPeriodo.getPeriodoInicio(), anio));
    		this.nominaPeriodo.setPeriodoFin(DateUtil.setAnio(this.nominaPeriodo.getPeriodoFin(), anio));
    		this.nominaPeriodo.setFechaPago(DateUtil.setAnio(this.nominaPeriodo.getFechaPago(), anio));
    		
    	} catch (SGPException ex) {
			log.error("Problema para generar el periodo...", ex);
		}
    }
    
    public void calcularNomina() {
    	FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Nómina";
		
		List<DetEmpleado> listaEmpleados = null;
		
		try {
			this.listaNomina.clear();
			listaEmpleados = this.empleadoDAO.buscarActivoEmpresaIngreso(empresa.getIdEmpresa(), DateUtil.toDate(this.nominaPeriodo.getPeriodoInicio()), DateUtil.toDate(this.nominaPeriodo.getPeriodoFin()));
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
    		PrimeFaces.current().ajax().update(":form:messages");
    	}
		
    }
    
    private void procesaListaEmpleados(List<DetEmpleado> listaEmpleados) {
    	DetNomina nomina = null;
    	Integer idNominaTmp = -1;
    	
    	this.parametros = new ParametrosNomina();
    	this.parametros.cargar(DateUtil.toDate(this.nominaPeriodo.getPeriodoInicio()), DateUtil.toDate(this.nominaPeriodo.getPeriodoFin()));
    	
    	this.tiposPercepcion = this.parametros.getTiposPercepcion();
    	
    	for(DetEmpleado empleado : listaEmpleados) {
    		nomina = nominaDAO.buscar(this.empresa.getRfc(), this.nominaPeriodo.getKey().getTipoNomina(), this.nominaPeriodo.getKey().getAnio(), this.nominaPeriodo.getKey().getPeriodo(), empleado.getRfc());
    		
    		if(nomina == null) {
    			nomina = this.procesaEmpleado(empleado);
    			nomina.setId(idNominaTmp--);
    		}
    		
    		this.listaNomina.add(nomina);
    	}
    	log.info("Lista nomina: {}", this.listaNomina);
    }
    
    private DetNomina procesaEmpleado(DetEmpleado empleado) {
    	DetNomina nomina = null;
		nomina = NominaBL.build(NominaBL.TP_NOMINA_EXTRAORDINARIA, this.parametros, empleado);
		return nomina;
    }
    
    public void setTipoPercepcion() {
    	this.nomPercepcion.setClave(this.percepcion.getClave());
    	this.nomPercepcion.setTipoPercepcion(this.percepcion.getTipoPercepcion());
    	this.nomPercepcion.setNombre(this.percepcion.getNombre());
    }
    
    public void nuevaPercepcion() {
    	log.info("Agregando nueva percepcion...");
    	
    	this.nomPercepcion = PercepcionBL.build();
    	log.info("Percepcion: {}", this.nomPercepcion);
    	
    	PrimeFaces.current().ajax().update("form:dlg-percepcion-all");
    }
    
    public void agregarPercepcion() {
    	log.info("Agregando percepcion a los empleados seleccionados...");
    	
    	log.info("Percepcion: {}", this.nomPercepcion);
    	this.nominaBO = new NominaExtraordinariaBL();
    	
    	for(DetNomina nomina : this.listaNominaSelected) {
    		nomina.getPercepciones().add(nomPercepcion);
    		log.info("Agreagndo percepción al empleado: {}", nomina.getReceptor().getNombre());
    	}
    	
    	
    	
    }
    
    
    /***************GETTERS Y SETTERS***************/
	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public List<Integer> getSemanasDelAnio() {
		return semanasDelAnio;
	}

	public void setSemanasDelAnio(List<Integer> semanasDelAnio) {
		this.semanasDelAnio = semanasDelAnio;
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

	public DetNominaPeriodo getNominaPeriodo() {
		return nominaPeriodo;
	}

	public void setNominaPeriodo(DetNominaPeriodo nominaPeriodo) {
		this.nominaPeriodo = nominaPeriodo;
	}

	public CatEmpresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(CatEmpresa empresa) {
		this.empresa = empresa;
	}

	public List<CatEmpresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<CatEmpresa> empresas) {
		this.empresas = empresas;
	}

	public List<DetNomina> getListaNomina() {
		return listaNomina;
	}

	public void setListaNomina(List<DetNomina> listaNomina) {
		this.listaNomina = listaNomina;
	}

	public List<DetNomina> getListaNominaSelected() {
		return listaNominaSelected;
	}

	public void setListaNominaSelected(List<DetNomina> listaNominaSelected) {
		this.listaNominaSelected = listaNominaSelected;
	}

	public DetNominaPercepcion getNomPercepcion() {
		return nomPercepcion;
	}

	public void setNomPercepcion(DetNominaPercepcion percepcion) {
		this.nomPercepcion = percepcion;
	}
	
	public CatPercepcion getPercepcion() {
		return percepcion;
	}

	public void setPercepcion(CatPercepcion percepcion) {
		this.percepcion = percepcion;
	}

	public List<CatPercepcion> getPercepciones() {
		return percepciones;
	}

	public void setPercepciones(List<CatPercepcion> percepciones) {
		this.percepciones = percepciones;
	}

	public List<CatTipoPercepcion> getTiposPercepcion() {
		return tiposPercepcion;
	}

	public void setTiposPercepcion(List<CatTipoPercepcion> tiposPercepcion) {
		this.tiposPercepcion = tiposPercepcion;
	}

	public DetNomina getNomina() {
		return nomina;
	}

	public void setNomina(DetNomina nomina) {
		this.nomina = nomina;
	}
}
