package mx.com.ferbo.controller;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import mx.com.ferbo.dao.CatPercepcionesDAO;
import mx.com.ferbo.dao.CatTarifaIsrDAO;
import mx.com.ferbo.dao.EmpleadoDAO;
import mx.com.ferbo.dto.CatPercepcionesDTO;
import mx.com.ferbo.dto.CatTarifaIsrDTO;
import mx.com.ferbo.dto.DetEmpleadoDTO;
import java.util.Calendar;
import java.util.Comparator;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import mx.com.ferbo.dao.CatEmpresaDAO;
import mx.com.ferbo.dao.CatImssCuotasDAO;
import mx.com.ferbo.dao.CatSubsidioDAO;
import mx.com.ferbo.dao.DetNominaDAO;
import mx.com.ferbo.dao.RegistroDAO;
import mx.com.ferbo.dto.CatEmpresaDTO;
import mx.com.ferbo.dto.CatImssCuotasDTO;
import mx.com.ferbo.dto.CatSubsidioDTO;
import mx.com.ferbo.dto.DetNominaDTO;
import mx.com.ferbo.dto.DetRegistroDTO;
import mx.com.ferbo.util.SGPException;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author erale
 */
@Named(value = "nominaBean")
@ViewScoped
public class NominaBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static Logger log = LogManager.getLogger(NominaBean.class);

    //<editor-fold defaultstate="collapsed" desc="Constantes">
    private static final int DIAS_TRABAJADOS = 6;
    private static final int SEPTIMO_DIA = 1;
    private static final int DIAS_ANIO = 365;
    //</editor-fold> 
    //<editor-fold defaultstate="collapsed" desc="DAOs">
    private RegistroDAO registroDAO;
    private CatPercepcionesDAO catPercepcionesDAO;
    private EmpleadoDAO empleadoDAO;
    private CatTarifaIsrDAO catTarifaIsrSemanalDAO;
    private CatSubsidioDAO catSubsidioDAO;
    private CatImssCuotasDAO catImssCuotasDAO;
    private DetNominaDAO detNominaDAO;
    private CatEmpresaDAO empresaDAO;
    //</editor-fold> 
    //<editor-fold defaultstate="collapsed" desc="DTOs">
    private List<DetEmpleadoDTO> lstEmpleadosTmp;
    private List<DetRegistroDTO> lstRegistrosEmpleadoIncidencias;
    List<DetRegistroDTO> lstRegistrosPorEmpresaSemanaAnteriorTmp;

    private List<CatEmpresaDTO> lstEmpresas;
    private List<DetRegistroDTO> lstRegistrosAlAnio;
    private List<CatPercepcionesDTO> lstPercepcionActual;
    private List<CatTarifaIsrDTO> lstIsrActualSemanal;
    private List<CatTarifaIsrDTO> lstIsrActualMensual;
    private List<CatSubsidioDTO> lstSubsidioActual;
    private List<CatImssCuotasDTO> lstCuotaImssActual;
    private List<DetEmpleadoDTO> lstEmpleados;
    private List<DetRegistroDTO> lstRegistrosEmpleadoSemanaAnterior;
    private List<DetRegistroDTO> lstRegistrosPorEmpresaSemanaAnterior;

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
    private DetNominaDTO nomina;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Objetos">
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

    private Date juevesPasado, miercolesPasado;

    private HttpServletRequest httpServletRequest;

    private boolean divVisibleCalculoNomina;
    private boolean divDataTableEmpleados;
    private boolean divDataTableEmpresas;

    private DateFormat dateFormatYYYYMMDD;
    private DateFormat dateFormatYYYY;
    private String strDate;
    private String strYear;
    //</editor-fold>

    private List<DetEmpleadoDTO> listaEmpleadosDeEmpresaSeleccionada;
    private List<DetRegistroDTO> listaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnterior;
    private List<DetRegistroDTO> listaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnteriorConFaltas;
    private List<DetRegistroDTO> listaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnteriorConFaltasTmp;
    private List<DetNominaDTO> listaNomina;

    public NominaBean() {
        log.info("====================== entrada constructor nominaBean ======================");
        lstEmpleadosTmp = new ArrayList<>();
        lstRegistrosEmpleadoIncidencias = new ArrayList<>();
        lstRegistrosPorEmpresaSemanaAnteriorTmp = new ArrayList<>();

        listaEmpleadosDeEmpresaSeleccionada = new ArrayList<>();
        listaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnterior = new ArrayList<>();
        listaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnteriorConFaltas = new ArrayList<>();
        listaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnteriorConFaltasTmp = new ArrayList<>();

        lstRegistrosEmpleadoSemanaAnterior = new ArrayList<>();
        lstRegistrosEmpleadoIncidencias = new ArrayList<>();
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
            httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            empleadoLogin = (DetEmpleadoDTO) httpServletRequest.getSession(true).getAttribute("empleado");
            empleadoSelected = empleadoDAO.buscarPorIdSDI(empleadoLogin.getIdEmpleado());
        } else {
            httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            empleadoLogin = (DetEmpleadoDTO) httpServletRequest.getSession(true).getAttribute("empleado");
            empleadoSelected = empleadoDAO.buscarPorIdSDI(idEmpleado);
            //divDataTableEmpleados = !divDataTableEmpleados;
        }

        Integer idEmpresa = searchIdEmpresa();
        if (idEmpresa == null) {
            httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            empleadoLogin = (DetEmpleadoDTO) httpServletRequest.getSession(true).getAttribute("empleado");
            // Utilizado en obteniendoListaEmpleadosDeEmpresaSeleccionada();
            empresaSelected = empresaDAO.buscarPorId(empleadoLogin.getCatEmpresaDTO().getIdEmpresa());
        } else {
            httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            empleadoLogin = (DetEmpleadoDTO) httpServletRequest.getSession(true).getAttribute("empleado");
            // Utilizado en obteniendoListaEmpleadosDeEmpresaSeleccionada();
            empresaSelected = empresaDAO.buscarPorId(idEmpresa);
            // divDataTableEmpresas = !divDataTableEmpresas;
        }

        fecha = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
        cal.setTime(fecha);
        year = cal.get(Calendar.YEAR);

        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        miercolesPasado = cal.getTime();
        cal.add(Calendar.DATE, -6);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        juevesPasado = cal.getTime();

        dateFormatYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
        strDate = dateFormatYYYYMMDD.format(fecha) + "%";
        log.info("====================== salida constructor nominaBean ======================");
        log.info("Largo de Lista constructor" + listaNomina.size());
    }

    @PostConstruct
    public void init() {
        log.info("====================== entrada init nominaBean ======================");
        lstEmpresas = empresaDAO.buscarActivo();
        // Utilizado en obteniendoListaEmpleadosDeEmpresaSeleccionada();
        lstEmpleados = empleadoDAO.buscarActivoConSDI();

        lstRegistrosEmpleadoSemanaAnterior = registroDAO.buscarRegistroNomina(juevesPasado, miercolesPasado);

        lstPercepcionActual = catPercepcionesDAO.buscarActivo();
        percepcion = (CatPercepcionesDTO) lstPercepcionActual.get(0);
        lstIsrActualSemanal = catTarifaIsrSemanalDAO.buscarActualesSemanal(year);
        lstSubsidioActual = catSubsidioDAO.buscarActuales(year);
        lstCuotaImssActual = catImssCuotasDAO.buscarActuales(year);

        isr = new CatTarifaIsrDTO();
        subsidio = new CatSubsidioDTO();
        imss = new CatImssCuotasDTO();
      
    }

    public void calculandoNomina() {
        listaNomina.clear();
        listaEmpleadosDeEmpresaSeleccionada = obteniendoListaEmpleadosDeEmpresaSeleccionada(empresaSelected.getIdEmpresa());
        listaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnterior = obteniendoListaRegistrosEmpleadosDeEmpresaSeleccionada();
      
        rellenandoRegistrosConFaltasActual();

        PrimeFaces.current().ajax().update("formNomina:dtNomina");
        PrimeFaces.current().executeScript("PF('empresaDialog').hide()");
    }

    private List<DetEmpleadoDTO> obteniendoListaEmpleadosDeEmpresaSeleccionada(Integer idEmpresa) {
        lstEmpleadosTmp = lstEmpleados.stream().filter(i -> i.getCatEmpresaDTO().getIdEmpresa().equals(idEmpresa)).sorted(Comparator.comparingInt(DetEmpleadoDTO::getIdEmpleado)).collect(Collectors.toList());
        return lstEmpleadosTmp;
    }

    private List<DetRegistroDTO> obteniendoListaRegistrosEmpleadosDeEmpresaSeleccionada() {
        lstRegistrosPorEmpresaSemanaAnterior = lstRegistrosEmpleadoSemanaAnterior.stream().filter(rg -> lstEmpleadosTmp.stream().anyMatch(empl -> empl.getIdEmpleado().equals(rg.getDetEmpleadoDTO().getIdEmpleado()))).collect(Collectors.toList());
        lstRegistrosPorEmpresaSemanaAnterior.sort(Comparator.comparingInt(DetEmpleadoDTO -> DetEmpleadoDTO.getDetEmpleadoDTO().getIdEmpleado()));
        return lstRegistrosPorEmpresaSemanaAnterior;
    }

    private void rellenandoRegistrosConFaltasActual() { // Mandar llamar la sublista de empleados de lstRegistrosEmpleadoSemanaAnterior
        for (DetEmpleadoDTO detEmpleadoDTO : lstEmpleadosTmp) {
            lstRegistrosPorEmpresaSemanaAnteriorTmp = lstRegistrosPorEmpresaSemanaAnterior.stream().filter(i -> i.getDetEmpleadoDTO().getIdEmpleado().equals(detEmpleadoDTO.getIdEmpleado())).collect(Collectors.toList());
            DetRegistroDTO registroNuevo = new DetRegistroDTO(0, detEmpleadoDTO.getIdEmpleado(), new Date(), new Date(), 3, "Falta");
            for (int i = 0; i <= lstRegistrosPorEmpresaSemanaAnteriorTmp.size(); i++) {
                if (lstRegistrosPorEmpresaSemanaAnteriorTmp.size() < 6) {
                    lstRegistrosPorEmpresaSemanaAnteriorTmp.add(registroNuevo);
                }
            }
            for (DetRegistroDTO registro : lstRegistrosPorEmpresaSemanaAnteriorTmp) {
                if (registro.getCatEstatusRegistroDTO().getIdEstatus() == 2 || registro.getCatEstatusRegistroDTO().getIdEstatus() == 3) {
                    lstRegistrosEmpleadoIncidencias.add(registro);
                }
            }
            sdi(detEmpleadoDTO);
            salarioSemanal(detEmpleadoDTO);
            bonoPuntualidad();
            valesDespensa();
            totalPercepciones(detEmpleadoDTO);

            baseIsr();
            isrRetener();

            enfMatEsp();
            enfMatGM();
            enfMatDinero();
            totalEnfMat();
            invVida();
            cesantiaVejez();
            totalImssRetener();

            nomina = new DetNominaDTO();
            nomina.setIdEmpleado(detEmpleadoDTO);
            nomina.setSueldo(salSem);
            nomina.setSeptimoDia((float)detEmpleadoDTO.getSueldoDiario());
            nomina.setHorasExtras((float) 0);
            nomina.setDestajos((float) 0);
            nomina.setPremiosEficiencia((float) 0);
            nomina.setBonoPuntualidad(bono);
            nomina.setDespensa(vales);
            nomina.setOtrasPercepciones((float) 0);
            nomina.setTotalPercepciones(total);
            nomina.setRetInvYVida(invVida);
            nomina.setRetCesantia(cesVejez);
            nomina.setRetEnfYMatObrero(totalEnfMat);
            nomina.setPrestamoInfonavitFd((float) 0);
            nomina.setPrestamoInfonavitCf((float) 0);
            nomina.setSubsAlEmpleoAcreditado(subsidio.getCantidadSubsidio());
            nomina.setSubsAlEmpleoMes((float) 0);
            nomina.setIsrAntesDeSubsAlEmpleo(impuestoAntesDeSubsidio);
            nomina.setIsrMes(impuestoAntesDeSubsidio - subsidio.getCantidadSubsidio());
            nomina.setImss(imssARetener);
            nomina.setPrestamoFonacot((float) 0);
            nomina.setAjusteEnSubsidioParaElEmpleo((float) 0);
            nomina.setSubsEntregadoQueNoCorrespondia((float) 0);
            nomina.setAjusteAlNeto((float) 0);
            nomina.setIsrDeAjusteMensual((float) 0);
            nomina.setIsrAjustadoPorSubsidio((float) 0);
            nomina.setAjusteAlSubsidioCausado((float) 0);
            nomina.setPensionAlimienticia((float) 0);
            nomina.setOtrasDeducciones((float) 0);
            nomina.setTotalDeducciones((float) 0 + (float) 0 + (impuestoAntesDeSubsidio - subsidio.getCantidadSubsidio()) + imssARetener + (float) 0 + (float) 0 + (float) 0 + (float) 0 + (float) 0);
            nomina.setNeto(total - ((float) 0 + (float) 0 + (impuestoAntesDeSubsidio - subsidio.getCantidadSubsidio()) + imssARetener + (float) 0 + (float) 0 + (float) 0 + (float) 0 + (float) 0));
            nomina.setInvalidezYVida((float) 0);
            nomina.setCesantiaYVejez((float) 0);
            nomina.setEnfYMatPatron((float) 0);
            nomina.setFondoRetiroSar((float) 0);
            nomina.setImpuestoEstatal((float) 0);
            nomina.setRiesgoDeTrabajo9((float) 0);
            nomina.setImssEmpresa((float) 0);
            nomina.setInfonavitEmpresa((float) 0);
            nomina.setGuarderiaImss7((float) 0);
            nomina.setOtrasObligaciones((float) 0);
            nomina.setTotalObligaciones((float) 0);
            nomina.setFechaCreacion(fecha);
            nomina.setIdEmpleadoCreador(empleadoLogin);

            listaNomina.add(nomina);

            lstRegistrosEmpleadoIncidencias.clear();
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Cálculo de Nómina">
    private float sdi(DetEmpleadoDTO empleado) {
        sdi = ((DIAS_ANIO + percepcion.getDiasAguinaldo() + (percepcion.getDiasVacaciones() * percepcion.getPrimaVacacional())) / DIAS_ANIO) * empleado.getSueldoDiario();
        sdi = (float) (Math.round(sdi * 100d) / 100d);
        return sdi;
    }

    private float salarioSemanal(DetEmpleadoDTO empleado) {
        if (lstRegistrosEmpleadoIncidencias.isEmpty()) {
            salSem = empleado.getSueldoDiario() * DIAS_TRABAJADOS + SEPTIMO_DIA;
            salSem = (float) (Math.round(salSem * 100d) / 100d);
            return salSem;
        } else {
            parteProporcionalDiaDescanso = (1 * ((float) DIAS_TRABAJADOS - (float) lstRegistrosEmpleadoIncidencias.size()) / (float) DIAS_TRABAJADOS);
            parteProporcionalDiaDescanso = (float) (Math.round(parteProporcionalDiaDescanso * 100d) / 100d);
            salSem = empleadoSelected.getSueldoDiario() * ((DIAS_TRABAJADOS - lstRegistrosEmpleadoIncidencias.size()) + parteProporcionalDiaDescanso);
            salSem = (float) (Math.round(salSem * 100d) / 100d);
            return salSem;
        }
    }

    private float bonoPuntualidad() {
        if (lstRegistrosEmpleadoIncidencias.isEmpty()) {
            // bono = (sdi() * percepcion.getBonoPuntualidad()) * (DIAS_TRABAJADOS + SEPTIMO_DIA);
            bono = (sdi * percepcion.getBonoPuntualidad()) * (DIAS_TRABAJADOS + SEPTIMO_DIA);
            bono = (float) (Math.round(bono * 100d) / 100d);
            /*return bono;
        } else {
            return bono;*/
        }
        return bono;
    }

    private float valesDespensa() {
        vales = (percepcion.getUma() * percepcion.getValeDespensa()) * (DIAS_TRABAJADOS + SEPTIMO_DIA);
        vales = (float) (Math.round(vales * 100d) / 100d);
        return vales;
    }

    private float totalPercepciones(DetEmpleadoDTO empleado) {
        if (lstRegistrosEmpleadoIncidencias.isEmpty()) {
            total = (DIAS_TRABAJADOS + SEPTIMO_DIA) * empleado.getSueldoDiario() + bonoPuntualidad() + valesDespensa();
            total = (float) (Math.round(total * 100d) / 100d);
            return total;
        } else {
            total = (DIAS_TRABAJADOS - lstRegistrosEmpleadoIncidencias.size()) * empleado.getSueldoDiario() + valesDespensa();
            total = (float) (Math.round(total * 100d) / 100d);
            return total;
        }
    }

//</editor-fold> 
    
    //<editor-fold defaultstate="collapsed" desc="Cálculo de ISR">
    public float baseIsr() {
        // baseIsr = salSem + empleadoSelected.getSueldoDiario() + bono + vales;
        baseIsr = total;
        // baseIsr = (float) (Math.round(total * 100d) / 100d);
        return baseIsr;
    }

    public float isrRetener() {
        isr = lstIsrActualSemanal.stream().filter(i -> i.getLimiteSuperior() >= baseIsr).findFirst().get();
        subsidio = lstSubsidioActual.stream().filter(s -> s.getHastaIngresosDe() >= baseIsr).findFirst().get();

        excedente = baseIsr - isr.getLimiteInferior();
        excedente = (float) (Math.round(excedente * 100d) / 100d);
        impuestoPrevio = (float) (excedente * (isr.getPorcAplExceLimInf() / 100));
        impuestoPrevio = (float) (Math.round(impuestoPrevio * 100d) / 100d);
        impuestoAntesDeSubsidio = impuestoPrevio + isr.getCuotaFija();
        impuestoAntesDeSubsidio = (float) (Math.round(impuestoAntesDeSubsidio * 100d) / 100d);
        isrARetener = impuestoAntesDeSubsidio - subsidio.getCantidadSubsidio();
        isrARetener = (float) (Math.round(isrARetener * 100d) / 100d);
        return isrARetener; //isrRetener = baseIsr 
    }
    //</editor-fold> 

    //<editor-fold defaultstate="collapsed" desc="Cálculo de IMSS">
    public float enfMatEsp() {
        imss = lstCuotaImssActual.stream().filter(s -> s.getCuota().equals("T")).findFirst().get();
        enfMatEsp = percepcion.getUma() * 3;
        if (sdi > enfMatEsp) {
            enfMatEsp = ((sdi - enfMatEsp) * imss.getEnfMatEspCtAd()) * (DIAS_TRABAJADOS + SEPTIMO_DIA);
            enfMatEsp = (float) (Math.round(enfMatEsp * 100d) / 100d);
        } else {
            enfMatEsp = 0;
        }
        return enfMatEsp;
    }

    public float enfMatGM() {
        imss = lstCuotaImssActual.stream().filter(s -> s.getCuota().equals("T")).findFirst().get();
        return enfMatGM = (float) (Math.round((sdi * imss.getEnfMatGastosMed()) * (DIAS_TRABAJADOS + SEPTIMO_DIA) * 100d) / 100d);
    }

    public float enfMatDinero() {
        imss = lstCuotaImssActual.stream().filter(s -> s.getCuota().equals("T")).findFirst().get();
        return enfMatDinero = (float) (Math.round((sdi * imss.getEnfMatDinero()) * (DIAS_TRABAJADOS + SEPTIMO_DIA) * 100d) / 100d);
    }

    public float totalEnfMat() {
        return totalEnfMat = enfMatEsp + enfMatGM + enfMatDinero;
    }

    public float invVida() {
        imss = lstCuotaImssActual.stream().filter(s -> s.getCuota().equals("T")).findFirst().get();
        return invVida = (float) (Math.round((sdi * imss.getInvVida()) * (DIAS_TRABAJADOS + SEPTIMO_DIA) * 100d) / 100d);
    }

    public float cesantiaVejez() {
        imss = lstCuotaImssActual.stream().filter(s -> s.getCuota().equals("T")).findFirst().get();
        return cesVejez = (float) (Math.round((sdi * imss.getRetCesantiaVejezCeav()) * (DIAS_TRABAJADOS + SEPTIMO_DIA) * 100d) / 100d);
    }

    public float totalImssRetener() {
        return imssARetener = (float) (Math.round((totalEnfMat + invVida + cesVejez) * 100d) / 100d);
    }
    //</editor-fold> 

    //<editor-fold defaultstate="collapsed" desc="Guardando Guardar Nómina">
    public void guardarNominaEmpleado() {
        int cont = 0;
        for(DetNominaDTO nomina : listaNomina){
            try {
                detNominaDAO.guardar(nomina);
                cont++;
            } catch (SGPException ex) {
                log.warn("EX-0039: " + ex.getMessage() + ". Error al guardar la nómina del empleado: " + nomina.getIdEmpleado().getNumEmpleado() != null ? nomina.getIdEmpleado().getNumEmpleado() : null); 
            }
        }
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
        cal.setTime(fecha);
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Se ejecuto la Nómina de la Empresa: " + lstEmpresas.stream().filter(i -> i.getIdEmpresa().equals(empresaSelected.getIdEmpresa())).findFirst().orElse(null).getDescripcion() + ", de la Semana: " + cal.get(Calendar.WEEK_OF_YEAR) + " con: " + cont + " registros.", null));
        PrimeFaces.current().ajax().update("formNomina:messages");
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

    public List<CatTarifaIsrDTO> getLstIsrActualSemanal() {
        return lstIsrActualSemanal;
    }

    public void setLstIsrActualSemanal(List<CatTarifaIsrDTO> lstIsrActualSemanal) {
        this.lstIsrActualSemanal = lstIsrActualSemanal;
    }

    public List<CatTarifaIsrDTO> getLstIsrActualMensual() {
        return lstIsrActualMensual;
    }

    public void setLstIsrActualMensual(List<CatTarifaIsrDTO> lstIsrActualMensual) {
        this.lstIsrActualMensual = lstIsrActualMensual;
    }

    public List<CatSubsidioDTO> getLstSubsidioActual() {
        return lstSubsidioActual;
    }

    public void setLstSubsidioActual(List<CatSubsidioDTO> lstSubsidioActual) {
        this.lstSubsidioActual = lstSubsidioActual;
    }

    public List<CatImssCuotasDTO> getLstCuotaImssActual() {
        return lstCuotaImssActual;
    }

    public void setLstCuotaImssActual(List<CatImssCuotasDTO> lstCuotaImssActual) {
        this.lstCuotaImssActual = lstCuotaImssActual;
    }

    public List<DetEmpleadoDTO> getLstEmpleados() {
        return lstEmpleados;
    }

    public void setLstEmpleados(List<DetEmpleadoDTO> lstEmpleados) {
        this.lstEmpleados = lstEmpleados;
    }

    public List<DetRegistroDTO> getLstRegistrosEmpleadoSemanaAnterior() {
        return lstRegistrosEmpleadoSemanaAnterior;
    }

    public void setLstRegistrosEmpleadoSemanaAnterior(List<DetRegistroDTO> lstRegistrosEmpleadoSemanaAnterior) {
        this.lstRegistrosEmpleadoSemanaAnterior = lstRegistrosEmpleadoSemanaAnterior;
    }

    public List<DetRegistroDTO> getLstRegistrosPorEmpresaSemanaAnterior() {
        return lstRegistrosPorEmpresaSemanaAnterior;
    }

    public void setLstRegistrosPorEmpresaSemanaAnterior(List<DetRegistroDTO> lstRegistrosPorEmpresaSemanaAnterior) {
        this.lstRegistrosPorEmpresaSemanaAnterior = lstRegistrosPorEmpresaSemanaAnterior;
    }

    public List<DetRegistroDTO> getLstRegistrosEmpleadoIncidencias() {
        return lstRegistrosEmpleadoIncidencias;
    }

    public void setLstRegistrosEmpleadoIncidencias(List<DetRegistroDTO> lstRegistrosEmpleadoIncidencias) {
        this.lstRegistrosEmpleadoIncidencias = lstRegistrosEmpleadoIncidencias;
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

    public DetNominaDTO getNomina() {
        return nomina;
    }

    public void setNomina(DetNominaDTO nomina) {
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

    public Date getJuevesPasado() {
        return juevesPasado;
    }

    public void setJuevesPasado(Date juevesPasado) {
        this.juevesPasado = juevesPasado;
    }

    public Date getMiercolesPasado() {
        return miercolesPasado;
    }

    public void setMiercolesPasado(Date miercolesPasado) {
        this.miercolesPasado = miercolesPasado;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
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

    public List<DetRegistroDTO> getLstRegistrosPorEmpresaSemanaAnteriorTmp() {
        return lstRegistrosPorEmpresaSemanaAnteriorTmp;
    }

    public void setLstRegistrosPorEmpresaSemanaAnteriorTmp(List<DetRegistroDTO> lstRegistrosPorEmpresaSemanaAnteriorTmp) {
        this.lstRegistrosPorEmpresaSemanaAnteriorTmp = lstRegistrosPorEmpresaSemanaAnteriorTmp;
    }

    public List<DetEmpleadoDTO> getListaEmpleadosDeEmpresaSeleccionada() {
        return listaEmpleadosDeEmpresaSeleccionada;
    }

    public void setListaEmpleadosDeEmpresaSeleccionada(List<DetEmpleadoDTO> listaEmpleadosDeEmpresaSeleccionada) {
        this.listaEmpleadosDeEmpresaSeleccionada = listaEmpleadosDeEmpresaSeleccionada;
    }

    public List<DetRegistroDTO> getListaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnterior() {
        return listaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnterior;
    }

    public void setListaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnterior(List<DetRegistroDTO> listaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnterior) {
        this.listaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnterior = listaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnterior;
    }

    public List<DetRegistroDTO> getListaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnteriorConFaltas() {
        return listaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnteriorConFaltas;
    }

    public void setListaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnteriorConFaltas(List<DetRegistroDTO> listaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnteriorConFaltas) {
        this.listaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnteriorConFaltas = listaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnteriorConFaltas;
    }

    public List<DetRegistroDTO> getListaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnteriorConFaltasTmp() {
        return listaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnteriorConFaltasTmp;
    }

    public void setListaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnteriorConFaltasTmp(List<DetRegistroDTO> listaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnteriorConFaltasTmp) {
        this.listaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnteriorConFaltasTmp = listaRegistrosEmpleadosDeEmpresaSeleccionadaSemanaAnteriorConFaltasTmp;
    }

    public List<DetNominaDTO> getListaNomina() {
        return listaNomina;
    }

    public void setListaNomina(List<DetNominaDTO> listaNomina) {
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

}
