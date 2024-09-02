package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.business.NominaSemanalBL;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.EmpresaDAO;
import mx.com.ferbo.dao.n.PercepcionesDAO;
import mx.com.ferbo.dao.n.SubsidioDAO;
import mx.com.ferbo.dao.n.TarifaISRDAO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import mx.com.ferbo.model.CatEmpresa;
import mx.com.ferbo.model.CatPercepciones;
import mx.com.ferbo.model.CatSubsidio;
import mx.com.ferbo.model.CatTarifaISR;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaEmisor;
import mx.com.ferbo.model.DetNominaReceptor;
import mx.com.ferbo.util.DateUtils;

@Named(value = "nominaBean")
@ViewScoped
public class NominaBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(NominaBean.class);

    private EmpleadoDAO empleadoDAO;
    private EmpresaDAO empresaDAO;
    private PercepcionesDAO catPercepcionesDAO;
    private TarifaISRDAO tarifaISRDAO;
    private SubsidioDAO subsidioDAO;
    
    private List<DetEmpleadoDTO> lstEmpleadosTmp;
    private List<CatEmpresa> lstEmpresas;
    private CatPercepciones parametrosPercepciones;
    private List<CatTarifaISR> tablaISRsemanal;
    private List<CatSubsidio> tablaSubsidioSemanal;
    private DetEmpleadoDTO empleadoSelected;
    private CatEmpresa empresaSelected;
    private DetNomina nomina;
    
    private Date fecha;
    private int year;

    private Date periodoInicio;
    private Date periodoFin;
    private Date fechaInicioAnio;
    private Date fechafinAnio;
    private Integer semana;

    private List<DetNomina> listaNomina;

    public NominaBean() {
        log.info("====================== entrada constructor nominaBean ======================");
        lstEmpleadosTmp = new ArrayList<>();

        lstEmpresas = new ArrayList<>();

        listaNomina = new ArrayList<>();

        empleadoDAO = new EmpleadoDAO(DetEmpleado.class);
        empresaDAO = new EmpresaDAO(CatEmpresa.class);
        catPercepcionesDAO = new PercepcionesDAO(CatPercepciones.class);
        tarifaISRDAO = new TarifaISRDAO(CatTarifaISR.class);
        subsidioDAO = new SubsidioDAO(CatSubsidio.class);

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
    }

    public void calculandoNomina() {
        listaNomina.clear();
        List<DetEmpleado> listaEmpleados = empleadoDAO.buscarActivoEmpresaIngreso(empresaSelected.getIdEmpresa(), this.periodoInicio);
      
        procesaListaEmpleados(listaEmpleados);

        PrimeFaces.current().ajax().update("formNomina:dtNomina");
        PrimeFaces.current().executeScript("PF('empresaDialog').hide()");
    }
    
    private void procesaListaEmpleados(List<DetEmpleado> listaEmpleados) {
    	DetNomina nomina = null;
    	try {
    		this.parametrosPercepciones = catPercepcionesDAO.buscarActual(this.periodoInicio);
    		this.tablaISRsemanal = tarifaISRDAO.buscar(fechaInicioAnio, fechafinAnio, "s");
    		this.tablaSubsidioSemanal = subsidioDAO.buscar(fechaInicioAnio, fechafinAnio, "s");
    		
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
    	log.info("Empleado: {} {} {}, Salario diario: {}", empleado.getNombre(), empleado.getPrimerAp(), empleado.getSegundoAp(), empleado.getSueldoDiario());
		nominaSemanalBO = new NominaSemanalBL(empleado, periodoInicio, periodoFin);
		nominaSemanalBO.setParametrosPercepciones(parametrosPercepciones);
		nominaSemanalBO.setTablaISRSemanal(this.tablaISRsemanal);
		nominaSemanalBO.setTablaSubsidioSemanal(this.tablaSubsidioSemanal);
		nomina = nominaSemanalBO.calculoNomina();
		return nomina;
    }
    
    public void cargaEmpleadoNomina() {
    	log.info("Cargando información de nómina: {}", this.nomina);
    }

    public void guardarNominaEmpleado() {

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

    public DetEmpleadoDTO getEmpleadoSelected() {
        return empleadoSelected;
    }

    public void setEmpleadoSelected(DetEmpleadoDTO empleadoSelected) {
        this.empleadoSelected = empleadoSelected;
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
