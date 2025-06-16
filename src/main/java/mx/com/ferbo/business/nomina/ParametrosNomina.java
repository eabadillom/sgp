package mx.com.ferbo.business.nomina;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.ConceptoDAO;
import mx.com.ferbo.dao.n.CuotaIMSSDAO;
import mx.com.ferbo.dao.n.DiaNoLaboralDAO;
import mx.com.ferbo.dao.n.EstatusRegistroDAO;
import mx.com.ferbo.dao.n.MetodoPagoDAO;
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
import mx.com.ferbo.model.CatEmpresa;
import mx.com.ferbo.model.CatEstatusRegistro;
import mx.com.ferbo.model.CatPeriodicidadPago;
import mx.com.ferbo.model.CatTarifaISR;
import mx.com.ferbo.model.CatUMA;
import mx.com.ferbo.model.DetNominaPeriodo;
import mx.com.ferbo.model.sat.CatConcepto;
import mx.com.ferbo.model.sat.CatMetodoPago;
import mx.com.ferbo.model.sat.CatRegimenFiscal;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.model.sat.CatTipoOtroPago;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import mx.com.ferbo.model.sat.CatUnidadSAT;
import mx.com.ferbo.model.sat.CatUsoCFDI;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

public class ParametrosNomina {
	
	private static Logger log = LogManager.getLogger(ParametrosNomina.class);
	
	private Integer anio         = null;
	private Integer periodo      = null;
	private Integer diasPeriodo  = null;
	
	private Date periodoInicio   = null;
	private Date periodoFin      = null;
	private Date fechaInicioAnio = null;
	private Date fechaFinAnio    = null;
	private Date fechaEmision    = null;
	
	private CatEmpresa               empresa                = null;
	private CatUMA                   uma                    = null;
	private CatMetodoPago            metodoPago             = null;
	private CatConcepto              concepto               = null;
	private CatUnidadSAT             unidadSAT              = null;
	private CatPeriodicidadPago      periodicidad           = null;
	private CatRegimenFiscal         regimenFiscalReceptor  = null;
	private CatUsoCFDI               usoCFDI                = null;
	private List<CatDiaNoLaboral>    diasNoLaborales        = null;
	private List<CatTarifaISR>       tablaISR               = null;
	private List<CatTipoPercepcion>  tiposPercepcion        = null;
	private List<CatTipoOtroPago>    tiposOtroPago          = null;
	private List<CatTipoDeduccion>   tiposDeduccion         = null;
	private List<CatCuotaIMSS>       cuotasIMSS             = null;
	private List<CatEstatusRegistro> statusRegistros        = null;
	
	private DiaNoLaboralDAO     diaNLDAO           = null;
	private TarifaISRDAO        tarifaISRDAO       = null;
	private MetodoPagoDAO       metodoPagoDAO      = null;
	private ConceptoDAO         conceptoDAO        = null;
	private UnidadSATDAO        unidadSATDAO       = null;
	private RegimenFiscalDAO    regimenFiscalDAO   = null;
	private UsoCFDIDAO          usoCfdiDAO         = null;
	private TipoPercepcionDAO   tipoPercepcionDAO  = null;
	private TipoDeduccionDAO    tipoDeduccionDAO   = null;
	private CuotaIMSSDAO        cuotasIMSSDAO      = null;
	private TipoOtroPagoDAO     tipoOtroPagoDAO    = null;
	private UMADAO              umaDAO             = null;
	private EstatusRegistroDAO  statusRegistroDAO  = null;
	
	private BigDecimal          bonoPuntualidad    = null;
	private BigDecimal          valeDespensa       = null;
	
	public BigDecimal getValeDespensa() {
		return valeDespensa;
	}

	public ParametrosNomina() {
		this.diaNLDAO           = new DiaNoLaboralDAO();
		this.tarifaISRDAO       = new TarifaISRDAO();
		this.metodoPagoDAO      = new MetodoPagoDAO();
		this.conceptoDAO        = new ConceptoDAO();
		this.unidadSATDAO       = new UnidadSATDAO();
		this.regimenFiscalDAO   = new RegimenFiscalDAO();
		this.usoCfdiDAO         = new UsoCFDIDAO();
		this.tipoPercepcionDAO  = new TipoPercepcionDAO();
		this.tipoOtroPagoDAO    = new TipoOtroPagoDAO();
		this.tipoDeduccionDAO   = new TipoDeduccionDAO();
		this.cuotasIMSSDAO      = new CuotaIMSSDAO();
		this.umaDAO             = new UMADAO();
		this.statusRegistroDAO  = new EstatusRegistroDAO();
		this.fechaEmision       = new Date();
	}
	
	public void cargar(DetNominaPeriodo nominaPeriodo) {
		this.periodoInicio = DateUtil.toDate(nominaPeriodo.getPeriodoInicio());
		this.periodoFin = DateUtil.toDate(nominaPeriodo.getPeriodoFin());
		this.fechaInicioAnio = DateUtil.getFirstDayOfyear(periodoFin);
		this.fechaFinAnio    = DateUtil.getLastDayOfYear(periodoFin);
		
		this.anio            = nominaPeriodo.getKey().getAnio();
		this.periodo         = nominaPeriodo.getKey().getPeriodo();
		this.diasPeriodo     = DateUtil.daysDiff(periodoInicio, periodoFin);
		this.diasNoLaborales = this.diaNLDAO.buscarPorPeriodo("MX", periodoInicio, periodoFin);
		this.empresa         = nominaPeriodo.getKey().getEmpresa();
		
		//Catálogos SAT
		this.periodicidad           = nominaPeriodo.getKey().getPeriodicidad();
		this.tablaISR               = this.tarifaISRDAO.buscar(this.fechaInicioAnio, this.fechaFinAnio);
		this.metodoPago             = this.metodoPagoDAO.buscarPorId("PUE");
		this.concepto               = this.conceptoDAO.buscarPorId("84111505");
		this.unidadSAT              = this.unidadSATDAO.buscarPorId("ACT");
		this.regimenFiscalReceptor  = this.regimenFiscalDAO.buscarPorId("605");
		this.usoCFDI                = this.usoCfdiDAO.buscarPorId("CN01");
		this.tiposPercepcion        = this.tipoPercepcionDAO.buscarTodos();
		this.tiposDeduccion         = this.tipoDeduccionDAO.buscarTodos();
		this.cuotasIMSS             = this.cuotasIMSSDAO.buscarPorPeriodo(periodoFin);
		this.tiposOtroPago          = this.tipoOtroPagoDAO.buscarTodos();
		this.uma                    = this.umaDAO.buscarVigentePorFecha(DateUtil.toLocalDate(periodoFin));
		
		//Status de registro de asistencia
		this.statusRegistros        = this.statusRegistroDAO.buscarTodos();
		
		this.fechaEmision           = new Date();
		
		try {
			log.info("[UI] Año: {}, Periodo {}: del {} al {}, ",
					this.anio, nominaPeriodo.getKey().getPeriodo(), DateUtil.getString(this.periodoInicio, DateUtil.FORMATO_DD_MM_YYYY), DateUtil.getString(this.periodoFin, DateUtil.FORMATO_DD_MM_YYYY));
			log.info("EMISOR: {}", this.empresa.getRazonSocial());
			
		} catch (SGPException e) {
			log.error("Problema para generar el objeto de parámetros para nómina...", e);
		}
		
		
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

	public Integer getPeriodo() {
		return periodo;
	}

	public Integer getDiasPeriodo() {
		return diasPeriodo;
	}

	public List<CatEstatusRegistro> getStatusRegistros() {
		return statusRegistros;
	}
	
	public BigDecimal getBonoPuntualidad() {
		return bonoPuntualidad;
	}

	public CatEmpresa getEmpresa() {
		return empresa;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}
	
}
