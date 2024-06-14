package mx.com.ferbo.controller;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;

import mx.com.ferbo.business.NominaSemanalBL;
import mx.com.ferbo.dao.CatEmpresaDAO;
import mx.com.ferbo.dao.CatImssCuotasDAO;
import mx.com.ferbo.dao.CatPercepcionesDAO;
import mx.com.ferbo.dao.CatSubsidioDAO;
import mx.com.ferbo.dao.CatTarifaIsrDAO;
import mx.com.ferbo.dao.DetNominaDAO;
import mx.com.ferbo.dao.EmpleadoDAO;
import mx.com.ferbo.dao.RegistroDAO;
import mx.com.ferbo.dto.CatEmpresaDTO;
import mx.com.ferbo.dto.CatImssCuotasDTO;
import mx.com.ferbo.dto.CatPercepcionesDTO;
import mx.com.ferbo.dto.CatSubsidioDTO;
import mx.com.ferbo.dto.CatTarifaIsrDTO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import mx.com.ferbo.dto.DetNominaDTO;
import mx.com.ferbo.dto.DetRegistroDTO;
import mx.com.ferbo.dto.NominaDTO;
import mx.com.ferbo.dto.NominaEmisorDTO;
import mx.com.ferbo.dto.NominaReceptorDTO;
import mx.com.ferbo.util.DateUtils;

@Named(value = "nominaBean")
@ViewScoped
public class NominaBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(NominaBean.class);

    private RegistroDAO registroDAO;
    private CatPercepcionesDAO catPercepcionesDAO;
    private EmpleadoDAO empleadoDAO;
    private CatTarifaIsrDAO catTarifaIsrSemanalDAO;
    private CatSubsidioDAO catSubsidioDAO;
    private CatImssCuotasDAO catImssCuotasDAO;
    private DetNominaDAO detNominaDAO;
    private CatEmpresaDAO empresaDAO;
    
    private List<DetEmpleadoDTO> lstEmpleadosTmp;

    private List<CatEmpresaDTO> lstEmpresas;
    private List<DetRegistroDTO> lstRegistrosAlAnio;
    private List<CatPercepcionesDTO> lstPercepcionActual;
    private List<CatTarifaIsrDTO> lstIsrActualMensual;
    private List<DetEmpleadoDTO> lstEmpleados;

    private List<DetNominaDTO> lstNominaByFecha;
    private List<DetRegistroDTO> lstRegistrosEmpleado;
    private List<DetRegistroDTO> lstRegistrosEmpleadoTmp;

    private DetEmpleadoDTO empleadoLogin;
    private DetEmpleadoDTO empleadoSelected;
    private CatEmpresaDTO empresaSelected;
    private DetRegistroDTO registroNuevo;
    private CatPercepcionesDTO percepcion;
    private CatTarifaIsrDTO isr;
    private CatSubsidioDTO subsidio;
    private CatImssCuotasDTO imss;
    private NominaDTO nomina;
    
    private float sdi;
    private float parteProporcionalDiaDescanso = 0;
    private float salSem = 0;
    private float bono = 0;
    private float vales = 0;
    private float total = 0;
    private float baseIsr = 0;
    private float excedente = 0;
    private float impuestoPrevio = 0;
    private float impuestoAntesDeSubsidio = 0;
    private float subsidioAcreditado = 0;
    private float isrARetener = 0;
    private float enfMatEsp = 0;
    private float enfMatGM = 0;
    private float enfMatDinero = 0;
    private float totalEnfMat = 0;
    private float invVida = 0;
    private float cesVejez = 0;
    private float imssARetener = 0;
    private int faltas;

    private Date fecha;
    private int year;

    private Date periodoInicio;
    private Date periodoFin;
    private Integer semana;

    private HttpServletRequest request;

    private boolean divVisibleCalculoNomina;
    private boolean divDataTableEmpleados;
    private boolean divDataTableEmpresas;

    private DateFormat dateFormatYYYYMMDD;
    private DateFormat dateFormatYYYY;
    private String strDate;
    private String strYear;
    //</editor-fold>

    private List<NominaDTO> listaNomina;

    public NominaBean() {
        log.info("====================== entrada constructor nominaBean ======================");
        lstEmpleadosTmp = new ArrayList<>();

        lstPercepcionActual = new ArrayList<>();
        lstNominaByFecha = new ArrayList<>();
        lstEmpresas = new ArrayList<>();

        listaNomina = new ArrayList<>();

        registroDAO = new RegistroDAO();
        catPercepcionesDAO = new CatPercepcionesDAO();
        catTarifaIsrSemanalDAO = new CatTarifaIsrDAO();
        empleadoDAO = new EmpleadoDAO();
        catSubsidioDAO = new CatSubsidioDAO();
        catImssCuotasDAO = new CatImssCuotasDAO();
        detNominaDAO = new DetNominaDAO();
        empresaDAO = new CatEmpresaDAO();

        Integer idEmpleado = searchIdEmpleado();
        if (idEmpleado == null) {
            request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            empleadoLogin = (DetEmpleadoDTO) request.getSession(true).getAttribute("empleado");
            empleadoSelected = empleadoDAO.buscarPorIdSDI(empleadoLogin.getIdEmpleado());
        } else {
            request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            empleadoLogin = (DetEmpleadoDTO) request.getSession(true).getAttribute("empleado");
            empleadoSelected = empleadoDAO.buscarPorIdSDI(idEmpleado);
        }

        Integer idEmpresa = searchIdEmpresa();
        if (idEmpresa == null) {
            request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            empleadoLogin = (DetEmpleadoDTO) request.getSession(true).getAttribute("empleado");
            empleadoLogin = empleadoDAO.buscarPorId(empleadoLogin.getIdEmpleado(),  true);
            empresaSelected = empresaDAO.buscarPorId(empleadoLogin.getCatEmpresaDTO().getIdEmpresa());
        } else {
            request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            empleadoLogin = (DetEmpleadoDTO) request.getSession(true).getAttribute("empleado");
            empresaSelected = empresaDAO.buscarPorId(idEmpresa);
        }
        log.info("====================== salida constructor nominaBean ======================");
        log.info("Largo de Lista constructor {}", listaNomina.size());
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

        dateFormatYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
        strDate = dateFormatYYYYMMDD.format(fecha) + "%";
    }

    @PostConstruct
    public void init() {
        log.info("====================== entrada init nominaBean ======================");
        this.configuraPeriodo();
        
        
        lstEmpresas = empresaDAO.buscarActivo();
        lstPercepcionActual = catPercepcionesDAO.buscarActivo();
        percepcion = (CatPercepcionesDTO) lstPercepcionActual.get(0);

        isr = new CatTarifaIsrDTO();
        subsidio = new CatSubsidioDTO();
        imss = new CatImssCuotasDTO();
        
        log.info("Miercoles pasado: {}", periodoFin);
        log.info("Jueves pasado: {}", periodoInicio);
        
        
        this.nomina = this.iniciaNominaDTOVacio();
      
    }
    
    private NominaDTO iniciaNominaDTOVacio() {
    	NominaDTO nomina = new NominaDTO();
    	nomina.setEmisor(new NominaEmisorDTO());
    	nomina.setReceptor(new NominaReceptorDTO());
    	nomina.setConceptos(new ArrayList<>());
    	nomina.setPercepciones(new ArrayList<>());
    	nomina.setDeducciones(new ArrayList<>());
		return nomina;
	}

	public void calculaFechaFin() {
    	log.info("Fecha Inicio: {}", this.periodoInicio);
    	log.info("Fecha Fin: {}", this.periodoFin);
    	this.periodoFin = new Date(this.periodoInicio.getTime());
    	this.periodoFin = DateUtils.addDay(this.periodoFin, 6);
    	DateUtils.setTime(this.periodoFin, 23, 59, 59, 999);
    	this.semana = DateUtils.getSemanaAnio(this.periodoInicio);
    	log.info("Fecha Inicio: {}", this.periodoInicio);
    	log.info("Fecha Fin: {}", this.periodoFin);
    }
    
    public void calculaFechaInicio() {
    	log.info("Fecha Inicio: {}", this.periodoInicio);
    	log.info("Fecha Fin: {}", this.periodoFin);
    	this.periodoInicio = new Date(this.periodoFin.getTime());
    	this.periodoInicio = DateUtils.addDay(this.periodoInicio, -6);
    	DateUtils.setTime(this.periodoInicio, 0, 0, 0, 0);
    	this.semana = DateUtils.getSemanaAnio(this.periodoInicio);
    	log.info("Fecha Inicio: {}", this.periodoInicio);
    	log.info("Fecha Fin: {}", this.periodoFin);
    }

    public void calculandoNomina() {
        listaNomina.clear();
        
        List<DetEmpleadoDTO> listaEmpleados = empleadoDAO.buscarActivoAndEmpresa(empresaSelected.getIdEmpresa());
      
        procesaListaEmpleados(listaEmpleados);

        PrimeFaces.current().ajax().update("formNomina:dtNomina");
        PrimeFaces.current().executeScript("PF('empresaDialog').hide()");
    }
    
    private void procesaListaEmpleados(List<DetEmpleadoDTO> listaEmpleados) {
    	NominaDTO nomina = null;
    	try {
    		for (DetEmpleadoDTO detEmpleadoDTO : listaEmpleados) {
    			nomina = this.procesaEmpleado(detEmpleadoDTO);
    			listaNomina.add(nomina);
    		}
    		log.info("Lista nomina: {}", this.listaNomina);
    	} catch(Exception ex) {
    		log.error("Problema para procesar la nómina", ex);
    	}
    }
    
    public NominaDTO procesaEmpleado(DetEmpleadoDTO detEmpleadoDTO) {
    	NominaDTO nomina = null;
    	NominaSemanalBL nominaSemanalBO = null;
    	log.info("Empleado: {} {} {}, Salario diario: {}", detEmpleadoDTO.getNombre(), detEmpleadoDTO.getPrimerAp(), detEmpleadoDTO.getSegundoAp(), detEmpleadoDTO.getSueldoDiario());
		
		nominaSemanalBO = new NominaSemanalBL(detEmpleadoDTO, periodoInicio, periodoFin);
		
		nomina = nominaSemanalBO.calculoNomina();
		
		
		
//		baseIsr(); baseIsr = 0f;
//		isrRetener();
		
//		enfMatEsp();
//		enfMatGM();
//		enfMatDinero();
//		totalEnfMat();
//		invVida();
//		cesantiaVejez();
//		totalImssRetener();
		
		
//		nomina.setRetInvYVida(invVida);
//		nomina.setRetCesantia(cesVejez);
//		nomina.setRetEnfYMatObrero(totalEnfMat);
//		nomina.setPrestamoInfonavitFd((float) 0);
//		nomina.setPrestamoInfonavitCf((float) 0);
//		nomina.setSubsAlEmpleoAcreditado(subsidio.getCantidadSubsidio());
//		nomina.setSubsAlEmpleoMes((float) 0);
//		nomina.setIsrAntesDeSubsAlEmpleo(impuestoAntesDeSubsidio);
//		nomina.setIsrMes(impuestoAntesDeSubsidio - subsidio.getCantidadSubsidio());
//		nomina.setImss(imssARetener);
//		nomina.setPrestamoFonacot((float) 0);
//		nomina.setAjusteEnSubsidioParaElEmpleo((float) 0);
//		nomina.setSubsEntregadoQueNoCorrespondia((float) 0);
//		nomina.setAjusteAlNeto((float) 0);
//		nomina.setIsrDeAjusteMensual((float) 0);
//		nomina.setIsrAjustadoPorSubsidio((float) 0);
//		nomina.setAjusteAlSubsidioCausado((float) 0);
//		nomina.setPensionAlimienticia((float) 0);
//		nomina.setOtrasDeducciones((float) 0);
//		nomina.setTotalDeducciones((float) 0 + (float) 0 + (impuestoAntesDeSubsidio - subsidio.getCantidadSubsidio()) + imssARetener + (float) 0 + (float) 0 + (float) 0 + (float) 0 + (float) 0);
//		nomina.setNeto(total - ((float) 0 + (float) 0 + (impuestoAntesDeSubsidio - subsidio.getCantidadSubsidio()) + imssARetener + (float) 0 + (float) 0 + (float) 0 + (float) 0 + (float) 0));
//		nomina.setInvalidezYVida((float) 0);
//		nomina.setCesantiaYVejez((float) 0);
//		nomina.setEnfYMatPatron((float) 0);
//		nomina.setFondoRetiroSar((float) 0);
//		nomina.setImpuestoEstatal((float) 0);
//		nomina.setRiesgoDeTrabajo9((float) 0);
//		nomina.setImssEmpresa((float) 0);
//		nomina.setInfonavitEmpresa((float) 0);
//		nomina.setGuarderiaImss7((float) 0);
//		nomina.setOtrasObligaciones((float) 0);
//		nomina.setTotalObligaciones((float) 0);
//		nomina.setFechaCreacion(fecha);
//		nomina.setIdEmpleadoCreador(empleadoLogin);
		
		return nomina;
    }
    
    public void cargaEmpleadoNomina() {
    	log.info("Cargando información de nómina: {}", this.nomina);
    }
    
//    //<editor-fold defaultstate="collapsed" desc="Cálculo de ISR">
//    public float baseIsr() {
//        // baseIsr = salSem + empleadoSelected.getSueldoDiario() + bono + vales;
//        baseIsr = total;
//        // baseIsr = (float) (Math.round(total * 100d) / 100d);
//        return baseIsr;
//    }

//    public float isrRetener() {
//        isr = lstIsrActualSemanal.stream().filter(i -> i.getLimiteSuperior() >= baseIsr).findFirst().get();
//        subsidio = lstSubsidioActual.stream().filter(s -> s.getHastaIngresosDe() >= baseIsr).findFirst().get();
//
//        excedente = baseIsr - isr.getLimiteInferior();
//        excedente = (float) (Math.round(excedente * 100d) / 100d);
//        impuestoPrevio = (float) (excedente * (isr.getPorcAplExceLimInf() / 100));
//        impuestoPrevio = (float) (Math.round(impuestoPrevio * 100d) / 100d);
//        impuestoAntesDeSubsidio = impuestoPrevio + isr.getCuotaFija();
//        impuestoAntesDeSubsidio = (float) (Math.round(impuestoAntesDeSubsidio * 100d) / 100d);
//        isrARetener = impuestoAntesDeSubsidio - subsidio.getCantidadSubsidio();
//        isrARetener = (float) (Math.round(isrARetener * 100d) / 100d);
//        return isrARetener; //isrRetener = baseIsr 
//    }

    //<editor-fold defaultstate="collapsed" desc="Cálculo de IMSS">
//    public float enfMatEsp() {
//        imss = lstCuotaImssActual.stream().filter(s -> s.getCuota().equals("T")).findFirst().get();
//        enfMatEsp = percepcion.getUma().multiply(new BigDecimal(3).setScale(2, BigDecimal.ROUND_HALF_UP)).floatValue();
//        if (sdi > enfMatEsp) {
//            enfMatEsp = ((sdi - enfMatEsp) * imss.getEnfMatEspCtAd()) * (DIAS_TRABAJADOS + SEPTIMO_DIA);
//            enfMatEsp = (float) (Math.round(enfMatEsp * 100d) / 100d);
//        } else {
//            enfMatEsp = 0;
//        }
//        return enfMatEsp;
//    }

//    public float enfMatGM() {
//        imss = lstCuotaImssActual.stream().filter(s -> s.getCuota().equals("T")).findFirst().get();
//        return enfMatGM = (float) (Math.round((sdi * imss.getEnfMatGastosMed()) * (DIAS_TRABAJADOS + SEPTIMO_DIA) * 100d) / 100d);
//    }

//    public float enfMatDinero() {
//        imss = lstCuotaImssActual.stream().filter(s -> s.getCuota().equals("T")).findFirst().get();
//        return enfMatDinero = (float) (Math.round((sdi * imss.getEnfMatDinero()) * (DIAS_TRABAJADOS + SEPTIMO_DIA) * 100d) / 100d);
//    }

    public float totalEnfMat() {
        return totalEnfMat = enfMatEsp + enfMatGM + enfMatDinero;
    }

//    public float invVida() {
//        imss = lstCuotaImssActual.stream().filter(s -> s.getCuota().equals("T")).findFirst().get();
//        return invVida = (float) (Math.round((sdi * imss.getInvVida()) * (DIAS_TRABAJADOS + SEPTIMO_DIA) * 100d) / 100d);
//    }

//    public float cesantiaVejez() {
//        imss = lstCuotaImssActual.stream().filter(s -> s.getCuota().equals("T")).findFirst().get();
//        return cesVejez = (float) (Math.round((sdi * imss.getRetCesantiaVejezCeav()) * (DIAS_TRABAJADOS + SEPTIMO_DIA) * 100d) / 100d);
//    }

    public float totalImssRetener() {
        return imssARetener = (float) (Math.round((totalEnfMat + invVida + cesVejez) * 100d) / 100d);
    }
    //</editor-fold> 

    //<editor-fold defaultstate="collapsed" desc="Guardando Guardar Nómina">
    public void guardarNominaEmpleado() {

    }
    //</editor-fold> 
    
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Celda Modificada", "Anterior: " + oldValue + ", Actual:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Métodos que sirven para obtener el Id del Empleado o Empresa del request">
    private Integer searchIdEmpleado() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = params.get("idEmpleado");
        if (isNumeric(id)) {
            return Integer.valueOf(id);
        } else {
            return null;
        }
    }

    private Integer searchIdEmpresa() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = params.get("idEmpresa");
        if (isNumeric(id)) {
            return Integer.valueOf(id);
        } else {
            return null;
        }
    }

    private static boolean isNumeric(final String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.valueOf(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="ExcelNómina">
    public void exportNomina(Object document) {
        List<String> columnNames = getColumnNames();

        StringBuilder headerRow = new StringBuilder();
        for (String columnName : columnNames) {
            headerRow.append(columnName).append(",");
        }
        headerRow.deleteCharAt(headerRow.length() - 1);

        String data = document.toString();
        data = headerRow.toString() + "\n" + data;
        document = data;
    }
    //</editor-fold> 

    //<editor-fold defaultstate="collapsed" desc="Getters && Setters">
    public List<DetEmpleadoDTO> getLstEmpleadosTmp() {
        return lstEmpleadosTmp;
    }

    public void setLstEmpleadosTmp(List<DetEmpleadoDTO> lstEmpleadosTmp) {
        this.lstEmpleadosTmp = lstEmpleadosTmp;
    }

    public RegistroDAO getRegistroDAO() {
        return registroDAO;
    }

    public void setRegistroDAO(RegistroDAO registroDAO) {
        this.registroDAO = registroDAO;
    }

    public CatPercepcionesDAO getCatPercepcionesDAO() {
        return catPercepcionesDAO;
    }

    public void setCatPercepcionesDAO(CatPercepcionesDAO catPercepcionesDAO) {
        this.catPercepcionesDAO = catPercepcionesDAO;
    }

    public EmpleadoDAO getEmpleadoDAO() {
        return empleadoDAO;
    }

    public void setEmpleadoDAO(EmpleadoDAO empleadoDAO) {
        this.empleadoDAO = empleadoDAO;
    }

    public CatTarifaIsrDAO getCatTarifaIsrSemanalDAO() {
        return catTarifaIsrSemanalDAO;
    }

    public void setCatTarifaIsrSemanalDAO(CatTarifaIsrDAO catTarifaIsrSemanalDAO) {
        this.catTarifaIsrSemanalDAO = catTarifaIsrSemanalDAO;
    }

    public CatSubsidioDAO getCatSubsidioDAO() {
        return catSubsidioDAO;
    }

    public void setCatSubsidioDAO(CatSubsidioDAO catSubsidioDAO) {
        this.catSubsidioDAO = catSubsidioDAO;
    }

    public CatImssCuotasDAO getCatImssCuotasDAO() {
        return catImssCuotasDAO;
    }

    public void setCatImssCuotasDAO(CatImssCuotasDAO catImssCuotasDAO) {
        this.catImssCuotasDAO = catImssCuotasDAO;
    }

    public DetNominaDAO getDetNominaDAO() {
        return detNominaDAO;
    }

    public void setDetNominaDAO(DetNominaDAO detNominaDAO) {
        this.detNominaDAO = detNominaDAO;
    }

    public CatEmpresaDAO getEmpresaDAO() {
        return empresaDAO;
    }

    public void setEmpresaDAO(CatEmpresaDAO empresaDAO) {
        this.empresaDAO = empresaDAO;
    }

    public List<CatEmpresaDTO> getLstEmpresas() {
        return lstEmpresas;
    }

    public void setLstEmpresas(List<CatEmpresaDTO> lstEmpresas) {
        this.lstEmpresas = lstEmpresas;
    }

    public List<DetRegistroDTO> getLstRegistrosAlAnio() {
        return lstRegistrosAlAnio;
    }

    public void setLstRegistrosAlAnio(List<DetRegistroDTO> lstRegistrosAlAnio) {
        this.lstRegistrosAlAnio = lstRegistrosAlAnio;
    }

    public List<CatPercepcionesDTO> getLstPercepcionActual() {
        return lstPercepcionActual;
    }

    public void setLstPercepcionActual(List<CatPercepcionesDTO> lstPercepcionActual) {
        this.lstPercepcionActual = lstPercepcionActual;
    }

    public List<CatTarifaIsrDTO> getLstIsrActualMensual() {
        return lstIsrActualMensual;
    }

    public void setLstIsrActualMensual(List<CatTarifaIsrDTO> lstIsrActualMensual) {
        this.lstIsrActualMensual = lstIsrActualMensual;
    }

//    public List<CatImssCuotasDTO> getLstCuotaImssActual() {
//        return lstCuotaImssActual;
//    }
//
//    public void setLstCuotaImssActual(List<CatImssCuotasDTO> lstCuotaImssActual) {
//        this.lstCuotaImssActual = lstCuotaImssActual;
//    }

    public List<DetEmpleadoDTO> getLstEmpleados() {
        return lstEmpleados;
    }

    public void setLstEmpleados(List<DetEmpleadoDTO> lstEmpleados) {
        this.lstEmpleados = lstEmpleados;
    }

    public List<DetNominaDTO> getLstNominaByFecha() {
        return lstNominaByFecha;
    }

    public void setLstNominaByFecha(List<DetNominaDTO> lstNominaByFecha) {
        this.lstNominaByFecha = lstNominaByFecha;
    }

    public List<DetRegistroDTO> getLstRegistrosEmpleado() {
        return lstRegistrosEmpleado;
    }

    public void setLstRegistrosEmpleado(List<DetRegistroDTO> lstRegistrosEmpleado) {
        this.lstRegistrosEmpleado = lstRegistrosEmpleado;
    }

    public List<DetRegistroDTO> getLstRegistrosEmpleadoTmp() {
        return lstRegistrosEmpleadoTmp;
    }

    public void setLstRegistrosEmpleadoTmp(List<DetRegistroDTO> lstRegistrosEmpleadoTmp) {
        this.lstRegistrosEmpleadoTmp = lstRegistrosEmpleadoTmp;
    }

    public DetEmpleadoDTO getEmpleadoLogin() {
        return empleadoLogin;
    }

    public void setEmpleadoLogin(DetEmpleadoDTO empleadoLogin) {
        this.empleadoLogin = empleadoLogin;
    }

    public DetEmpleadoDTO getEmpleadoSelected() {
        return empleadoSelected;
    }

    public void setEmpleadoSelected(DetEmpleadoDTO empleadoSelected) {
        this.empleadoSelected = empleadoSelected;
    }

    public CatEmpresaDTO getEmpresaSelected() {
        return empresaSelected;
    }

    public void setEmpresaSelected(CatEmpresaDTO empresaSelected) {
        this.empresaSelected = empresaSelected;
    }

    public DetRegistroDTO getRegistroNuevo() {
        return registroNuevo;
    }

    public void setRegistroNuevo(DetRegistroDTO registroNuevo) {
        this.registroNuevo = registroNuevo;
    }

    public CatPercepcionesDTO getPercepcion() {
        return percepcion;
    }

    public void setPercepcion(CatPercepcionesDTO percepcion) {
        this.percepcion = percepcion;
    }

    public CatTarifaIsrDTO getIsr() {
        return isr;
    }

    public void setIsr(CatTarifaIsrDTO isr) {
        this.isr = isr;
    }

    public CatSubsidioDTO getSubsidio() {
        return subsidio;
    }

    public void setSubsidio(CatSubsidioDTO subsidio) {
        this.subsidio = subsidio;
    }

    public CatImssCuotasDTO getImss() {
        return imss;
    }

    public void setImss(CatImssCuotasDTO imss) {
        this.imss = imss;
    }

    public NominaDTO getNomina() {
        return nomina;
    }

    public void setNomina(NominaDTO nomina) {
        this.nomina = nomina;
    }

    public float getSdi() {
        return sdi;
    }

    public void setSdi(float sdi) {
        this.sdi = sdi;
    }

    public float getParteProporcionalDiaDescanso() {
        return parteProporcionalDiaDescanso;
    }

    public void setParteProporcionalDiaDescanso(float parteProporcionalDiaDescanso) {
        this.parteProporcionalDiaDescanso = parteProporcionalDiaDescanso;
    }

    public float getSalSem() {
        return salSem;
    }

    public void setSalSem(float salSem) {
        this.salSem = salSem;
    }

    public float getBono() {
        return bono;
    }

    public void setBono(float bono) {
        this.bono = bono;
    }

    public float getVales() {
        return vales;
    }

    public void setVales(float vales) {
        this.vales = vales;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getBaseIsr() {
        return baseIsr;
    }

    public void setBaseIsr(float baseIsr) {
        this.baseIsr = baseIsr;
    }

    public float getExcedente() {
        return excedente;
    }

    public void setExcedente(float excedente) {
        this.excedente = excedente;
    }

    public float getImpuestoPrevio() {
        return impuestoPrevio;
    }

    public void setImpuestoPrevio(float impuestoPrevio) {
        this.impuestoPrevio = impuestoPrevio;
    }

    public float getImpuestoAntesDeSubsidio() {
        return impuestoAntesDeSubsidio;
    }

    public void setImpuestoAntesDeSubsidio(float impuestoAntesDeSubsidio) {
        this.impuestoAntesDeSubsidio = impuestoAntesDeSubsidio;
    }

    public float getSubsidioAcreditado() {
        return subsidioAcreditado;
    }

    public void setSubsidioAcreditado(float subsidioAcreditado) {
        this.subsidioAcreditado = subsidioAcreditado;
    }

    public float getIsrARetener() {
        return isrARetener;
    }

    public void setIsrARetener(float isrARetener) {
        this.isrARetener = isrARetener;
    }

    public float getEnfMatEsp() {
        return enfMatEsp;
    }

    public void setEnfMatEsp(float enfMatEsp) {
        this.enfMatEsp = enfMatEsp;
    }

    public float getEnfMatGM() {
        return enfMatGM;
    }

    public void setEnfMatGM(float enfMatGM) {
        this.enfMatGM = enfMatGM;
    }

    public float getEnfMatDinero() {
        return enfMatDinero;
    }

    public void setEnfMatDinero(float enfMatDinero) {
        this.enfMatDinero = enfMatDinero;
    }

    public float getTotalEnfMat() {
        return totalEnfMat;
    }

    public void setTotalEnfMat(float totalEnfMat) {
        this.totalEnfMat = totalEnfMat;
    }

    public float getInvVida() {
        return invVida;
    }

    public void setInvVida(float invVida) {
        this.invVida = invVida;
    }

    public float getCesVejez() {
        return cesVejez;
    }

    public void setCesVejez(float cesVejez) {
        this.cesVejez = cesVejez;
    }

    public float getImssARetener() {
        return imssARetener;
    }

    public void setImssARetener(float imssARetener) {
        this.imssARetener = imssARetener;
    }

    public int getFaltas() {
        return faltas;
    }

    public void setFaltas(int faltas) {
        this.faltas = faltas;
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

    public HttpServletRequest getHttpServletRequest() {
        return request;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

    public boolean isDivVisibleCalculoNomina() {
        return divVisibleCalculoNomina;
    }

    public void setDivVisibleCalculoNomina(boolean divVisibleCalculoNomina) {
        this.divVisibleCalculoNomina = divVisibleCalculoNomina;
    }

    public boolean isDivDataTableEmpleados() {
        return divDataTableEmpleados;
    }

    public void setDivDataTableEmpleados(boolean divDataTableEmpleados) {
        this.divDataTableEmpleados = divDataTableEmpleados;
    }

    public boolean isDivDataTableEmpresas() {
        return divDataTableEmpresas;
    }

    public void setDivDataTableEmpresas(boolean divDataTableEmpresas) {
        this.divDataTableEmpresas = divDataTableEmpresas;
    }

    public DateFormat getDateFormatYYYYMMDD() {
        return dateFormatYYYYMMDD;
    }

    public void setDateFormatYYYYMMDD(DateFormat dateFormatYYYYMMDD) {
        this.dateFormatYYYYMMDD = dateFormatYYYYMMDD;
    }

    public DateFormat getDateFormatYYYY() {
        return dateFormatYYYY;
    }

    public void setDateFormatYYYY(DateFormat dateFormatYYYY) {
        this.dateFormatYYYY = dateFormatYYYY;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getStrYear() {
        return strYear;
    }

    public void setStrYear(String strYear) {
        this.strYear = strYear;
    }

    public List<NominaDTO> getListaNomina() {
        return listaNomina;
    }

    public void setListaNomina(List<NominaDTO> listaNomina) {
        this.listaNomina = listaNomina;
    }

    public List<String> getColumnNames() {
        List<String> columnNames = new ArrayList<>();
        columnNames.add("Num. Empl.");
        columnNames.add("Empleado");
        columnNames.add("Sueldo");
        columnNames.add("Séptimo día");
        columnNames.add("Horas extras");
        columnNames.add("Destajos");
        columnNames.add("Premios eficiencia");
        columnNames.add("Bono puntualidad");
        columnNames.add("Despensa");
        columnNames.add("*Otras Percepciones*");
        columnNames.add("*TOTAL PERCEPCIONES*");
        columnNames.add("Ret. Inv. Y Vida");
        columnNames.add("Ret. Cesantia");
        columnNames.add("Ret. Enf. y Mat. obrero");
        columnNames.add("Préstamo infonavit (FD)");
        columnNames.add("Préstamo infonavit (CF)");
        columnNames.add("Subs al Empleo acreditado");
        columnNames.add("Subs al Empleo (mes)");
        columnNames.add("I.S.R. antes de Subs al Empleo");
        columnNames.add("I.S.R. (mes)");
        columnNames.add("I.M.S.S.");
        columnNames.add("Préstamo FONACOT");
        columnNames.add("Ajuste en Subsidio para el empleo");
        columnNames.add("Subs entregado que no correspondía");
        columnNames.add("Ajuste al neto");
        columnNames.add("ISR de ajuste mensual");
        columnNames.add("ISR ajustado por subsidio");
        columnNames.add("Ajuste al Subsidio Causado");
        columnNames.add("Pension Alimienticia");
        columnNames.add("*Otras Deducciones*");
        columnNames.add("*TOTAL DEDUCCIONES*");
        columnNames.add("*NETO*");
        columnNames.add("Invalidez y Vida");
        columnNames.add("Cesantia y Vejez");
        columnNames.add("Enf. y Mat. Patron");
        columnNames.add("2% Fondo retiro SAR (8)");
        columnNames.add("2% Impuesto estatal");
        columnNames.add("Riesgo de trabajo (9)");
        columnNames.add("I.M.S.S. empresa");
        columnNames.add("Infonavit empresa");
        columnNames.add("Guarderia I.M.S.S. (7)");
        columnNames.add("*Otras Obligaciones*");
        columnNames.add("*TOTAL OBLIGACIONES*");

        return columnNames;
    }
    //</editor-fold> 

	public Integer getSemana() {
		return semana;
	}

	public void setSemana(Integer semana) {
		this.semana = semana;
	}

}
