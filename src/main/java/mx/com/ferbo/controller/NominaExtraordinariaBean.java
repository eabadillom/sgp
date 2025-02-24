package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.NominaBL;
import mx.com.ferbo.business.nomina.ParametrosNomina;
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
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.model.sat.CatTipoOtroPago;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import mx.com.ferbo.util.DateUtil;

@Named(value = "nomExtra")
@ViewScoped
public class NominaExtraordinariaBean implements Serializable {

	private static final long serialVersionUID = -7354838193241382936L;
	private static Logger log = LogManager.getLogger(NominaExtraordinariaBean.class);
	
	private ParametrosNomina parametros = null;
	
	private EmpleadoDAO empleadoDAO;
    private EmpresaDAO empresaDAO;
    private NominaDAO nominaDAO;
    
    private List<CatEmpresa> empresas;
    private List<CatTipoPercepcion> tiposPercepcion;
    private List<CatTipoDeduccion> tiposDeduccion;
    private List<CatTipoOtroPago> tiposOtroPago;
    private CatEmpresa empresa;
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
    
    public NominaExtraordinariaBean() {
    	listaNomina = new ArrayList<>();
    	
    	empleadoDAO = new EmpleadoDAO(DetEmpleado.class);
    	empresaDAO = new EmpresaDAO(CatEmpresa.class);
    	nominaDAO = new NominaDAO(DetNomina.class);
	}
    
    @PostConstruct
    public void init() {
    	log.info("====================== entrada init nominaBean ======================");
        
        empresas = empresaDAO.buscarActivo();

        log.info("Inicio del periodo: {}", periodoFin);
        log.info("Fin del periodo: {}", periodoInicio);
        this.nomina = NominaBL.build(NominaBL.TP_NOMINA_ORDINARIA, parametros, null);
        this.percepcion = new DetNominaPercepcion();
        this.otroPago = new DetNominaOtroPago();
        this.deduccion = new DetNominaDeduccion();
        
        this.fecha = DateUtil.now();
        this.anio = DateUtil.getAnio(fecha);
        this.semanasDelAnio = DateUtil.semanasDelAnio(this.anio);
        this.semana = DateUtil.getSemanaAnio(this.fecha);
        
        this.parametros = new ParametrosNomina();
		this.parametros.cargar(periodoInicio, periodoFin);
    }
    
    /*GETTERS Y SETTERS*/

	public List<CatEmpresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<CatEmpresa> empresas) {
		this.empresas = empresas;
	}

	public List<CatTipoPercepcion> getTiposPercepcion() {
		return tiposPercepcion;
	}

	public void setTiposPercepcion(List<CatTipoPercepcion> tiposPercepcion) {
		this.tiposPercepcion = tiposPercepcion;
	}

	public List<CatTipoDeduccion> getTiposDeduccion() {
		return tiposDeduccion;
	}

	public void setTiposDeduccion(List<CatTipoDeduccion> tiposDeduccion) {
		this.tiposDeduccion = tiposDeduccion;
	}

	public List<CatTipoOtroPago> getTiposOtroPago() {
		return tiposOtroPago;
	}

	public void setTiposOtroPago(List<CatTipoOtroPago> tiposOtroPago) {
		this.tiposOtroPago = tiposOtroPago;
	}

	public CatEmpresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(CatEmpresa empresa) {
		this.empresa = empresa;
	}

	public DetNomina getNomina() {
		return nomina;
	}

	public void setNomina(DetNomina nomina) {
		this.nomina = nomina;
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

	public DetNominaDeduccion getDeduccion() {
		return deduccion;
	}

	public void setDeduccion(DetNominaDeduccion deduccion) {
		this.deduccion = deduccion;
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

	public Integer getSemana() {
		return semana;
	}

	public void setSemana(Integer semana) {
		this.semana = semana;
	}

	public List<DetNomina> getListaNomina() {
		return listaNomina;
	}

	public void setListaNomina(List<DetNomina> listaNomina) {
		this.listaNomina = listaNomina;
	}

	public List<Integer> getSemanasDelAnio() {
		return semanasDelAnio;
	}

	public void setSemanasDelAnio(List<Integer> semanasDelAnio) {
		this.semanasDelAnio = semanasDelAnio;
	}

	public Boolean getDetalle() {
		return detalle;
	}

	public void setDetalle(Boolean detalle) {
		this.detalle = detalle;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}
    
    

}
