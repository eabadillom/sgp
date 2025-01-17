package mx.com.ferbo.business.nomina;

import java.util.Date;
import java.util.List;

import mx.com.ferbo.dao.n.ConceptoDAO;
import mx.com.ferbo.dao.n.CuotaIMSSDAO;
import mx.com.ferbo.dao.n.DiaNoLaboralDAO;
import mx.com.ferbo.dao.n.MetodoPagoDAO;
import mx.com.ferbo.dao.n.PercepcionesDAO;
import mx.com.ferbo.dao.n.PeriodicidadPagoDAO;
import mx.com.ferbo.dao.n.RegimenFiscalDAO;
import mx.com.ferbo.dao.n.TarifaISRDAO;
import mx.com.ferbo.dao.n.TipoDeduccionDAO;
import mx.com.ferbo.dao.n.TipoOtroPagoDAO;
import mx.com.ferbo.dao.n.UMADAO;
import mx.com.ferbo.dao.n.UnidadSATDAO;
import mx.com.ferbo.dao.n.UsoCFDIDAO;
import mx.com.ferbo.dao.n.sat.TipoPercepcionDAO;
import mx.com.ferbo.model.CatCuotaIMSS;
import mx.com.ferbo.model.CatDiaNoLaboral;
import mx.com.ferbo.model.CatPercepciones;
import mx.com.ferbo.model.CatPeriodicidadPago;
import mx.com.ferbo.model.CatTarifaISR;
import mx.com.ferbo.model.CatUMA;
import mx.com.ferbo.model.sat.CatConcepto;
import mx.com.ferbo.model.sat.CatMetodoPago;
import mx.com.ferbo.model.sat.CatRegimenFiscal;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.model.sat.CatTipoOtroPago;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import mx.com.ferbo.model.sat.CatUnidadSAT;
import mx.com.ferbo.model.sat.CatUsoCFDI;
import mx.com.ferbo.util.DateUtil;

public class ParametrosNomina {
	
	private Integer anio         = null;
	private Integer semanaAnio   = null;
	
	private Date periodoInicio   = null;
	private Date periodoFin      = null;
	private Date fechaInicioAnio = null;
	private Date fechaFinAnio    = null;
	
	private CatUMA                  uma = null;
	private CatPercepciones         parametrosPercepciones = null;
	private CatMetodoPago           metodoPago = null;
	private CatConcepto             concepto = null;
	private CatUnidadSAT            unidadSAT = null;
	private CatPeriodicidadPago     periodicidad = null;
	private CatRegimenFiscal        regimenFiscalReceptor = null;
	private CatUsoCFDI              usoCFDI = null;
	private List<CatDiaNoLaboral>   diasNoLaborales = null;
	private List<CatTarifaISR>      tablaISR = null;
	private List<CatTipoPercepcion> tiposPercepcion = null;
	private List<CatTipoOtroPago>   tiposOtroPago = null;
	private List<CatTipoDeduccion>  tiposDeduccion = null;
	private List<CatCuotaIMSS>      cuotasIMSS = null;
	
	private DiaNoLaboralDAO     diaNLDAO = null;
	private PercepcionesDAO     catPercepcionesDAO = null;
	private TarifaISRDAO        tarifaISRDAO = null;
	private MetodoPagoDAO       metodoPagoDAO = null;
	private ConceptoDAO         conceptoDAO = null;
	private UnidadSATDAO        unidadSATDAO = null;
	private PeriodicidadPagoDAO periodicidadDAO = null;
	private RegimenFiscalDAO    regimenFiscalDAO = null;
	private UsoCFDIDAO          usoCfdiDAO = null;
	private TipoPercepcionDAO   tipoPercepcionDAO = null;
	private TipoDeduccionDAO    tipoDeduccionDAO = null;
	private CuotaIMSSDAO        cuotasIMSSDAO = null;
	private TipoOtroPagoDAO     tipoOtroPagoDAO = null;
	private UMADAO              umaDAO = null;
	
	public ParametrosNomina() {
		this.diaNLDAO           = new DiaNoLaboralDAO();
		this.catPercepcionesDAO = new PercepcionesDAO();
		this.tarifaISRDAO       = new TarifaISRDAO();
		this.metodoPagoDAO      = new MetodoPagoDAO();
		this.conceptoDAO        = new ConceptoDAO();
		this.unidadSATDAO       = new UnidadSATDAO();
		this.periodicidadDAO    = new PeriodicidadPagoDAO();
		this.regimenFiscalDAO   = new RegimenFiscalDAO();
		this.usoCfdiDAO         = new UsoCFDIDAO();
		this.tipoPercepcionDAO  = new TipoPercepcionDAO();
		this.tipoOtroPagoDAO    = new TipoOtroPagoDAO();
		this.tipoDeduccionDAO   = new TipoDeduccionDAO();
		this.cuotasIMSSDAO      = new CuotaIMSSDAO();
		this.umaDAO             = new UMADAO();
	}
	
	public void cargar(Date periodoInicio, Date periodoFin) {
		//Cálculo de fechas importantes del periodo de pago de nómina.
		this.anio            = DateUtil.getAnio(periodoFin);
		this.fechaInicioAnio = DateUtil.getFirstDayOfyear(periodoFin);
		this.fechaFinAnio    = DateUtil.getLastDayOfYear(periodoFin);
		this.periodoInicio   = new Date(periodoInicio.getTime());
		this.periodoFin      = new Date(periodoFin.getTime());
		this.diasNoLaborales = this.diaNLDAO.buscarPorPeriodo("MX", periodoInicio, periodoFin);
		this.semanaAnio      = DateUtil.getSemanaAnio(this.periodoInicio);
		
		//Catálogos SAT
		this.parametrosPercepciones = this.catPercepcionesDAO.buscarActual(periodoInicio);
		this.tablaISR               = this.tarifaISRDAO.buscar(this.fechaInicioAnio, this.fechaFinAnio);
		this.metodoPago             = this.metodoPagoDAO.buscarPorId("PUE");
		this.concepto               = this.conceptoDAO.buscarPorId("84111505");
		this.unidadSAT              = this.unidadSATDAO.buscarPorId("ACT");
		this.periodicidad           = this.periodicidadDAO.buscarPorId("02");
		this.regimenFiscalReceptor  = this.regimenFiscalDAO.buscarPorId("605");
		this.usoCFDI                = this.usoCfdiDAO.buscarPorId("CN01");
		this.tiposPercepcion        = this.tipoPercepcionDAO.buscarTodos();
		this.tiposDeduccion         = this.tipoDeduccionDAO.buscarTodos();
		this.cuotasIMSS             = this.cuotasIMSSDAO.buscarPorPeriodo(periodoFin);
		this.tiposOtroPago          = this.tipoOtroPagoDAO.buscarTodos();
		this.uma                    = this.umaDAO.buscarVigentePorFecha(DateUtil.toLocalDate(periodoFin));
	}

	public CatPercepciones getParametrosPercepciones() {
		return parametrosPercepciones;
	}

	public CatMetodoPago getMetodoPago() {
		return metodoPago;
	}

	public CatConcepto getConcepto() {
		return concepto;
	}

	public CatUnidadSAT getUnidadSAT() {
		return unidadSAT;
	}

	public CatPeriodicidadPago getPeriodicidad() {
		return periodicidad;
	}

	public CatRegimenFiscal getRegimenFiscalReceptor() {
		return regimenFiscalReceptor;
	}

	public CatUsoCFDI getUsoCFDI() {
		return usoCFDI;
	}

	public List<CatDiaNoLaboral> getDiasNoLaborales() {
		return diasNoLaborales;
	}

	public List<CatTarifaISR> getTablaISR() {
		return tablaISR;
	}

	public List<CatTipoPercepcion> getTiposPercepcion() {
		return tiposPercepcion;
	}

	public List<CatTipoOtroPago> getTiposOtroPago() {
		return tiposOtroPago;
	}

	public List<CatTipoDeduccion> getTiposDeduccion() {
		return tiposDeduccion;
	}

	public List<CatCuotaIMSS> getCuotasIMSS() {
		return cuotasIMSS;
	}

	public CatUMA getUma() {
		return uma;
	}

	public Date getPeriodoInicio() {
		return periodoInicio;
	}

	public Date getPeriodoFin() {
		return periodoFin;
	}

	public Date getFechaInicioAnio() {
		return fechaInicioAnio;
	}

	public Date getFechaFinAnio() {
		return fechaFinAnio;
	}

	public Integer getAnio() {
		return anio;
	}

	public Integer getSemanaAnio() {
		return semanaAnio;
	}
	
}
