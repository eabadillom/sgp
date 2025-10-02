package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.business.dianolaboral.DiasDeDescansoObligatorioBL;
import mx.com.ferbo.business.empleado.EmpleadoBL;
import mx.com.ferbo.business.incidencia.EstatusIncidenciaBL;
import mx.com.ferbo.business.incidencia.EstatusSolicitudBL;
import mx.com.ferbo.business.incidencia.IncidenciaBL;
import mx.com.ferbo.business.registro.RegistroBL;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.EstatusRegistroDAO;
import mx.com.ferbo.dao.n.IncidenciaDAO;
import mx.com.ferbo.dao.n.RegistroDAO;
import mx.com.ferbo.dao.n.RegistroVacacionesDAO;
import mx.com.ferbo.dao.n.TipoSolicitudDAO;
import mx.com.ferbo.model.CatEstatusIncidencia;
import mx.com.ferbo.model.CatEstatusRegistro;
import mx.com.ferbo.model.CatTipoIncidencia;
import mx.com.ferbo.model.CatTipoSolicitud;
import mx.com.ferbo.model.DetDiaPermiso;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.model.DetRegistroVacaciones;
import mx.com.ferbo.model.DetSolicitudArticulo;
import mx.com.ferbo.model.DetSolicitudPermiso;
import mx.com.ferbo.model.DetSolicitudPrenda;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.ManageStatus;
import mx.com.ferbo.util.SGPException;

/**
 *
 * @author Gabriel
 */
@Named("incidenciaBean")
@ViewScoped
public class IncidenciaBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(IncidenciaBean.class);

    private final IncidenciaDAO incidenciaDAO;
    private final TipoSolicitudDAO tipoSolicitudDAO;
    private DetIncidencia incidenciaSelected;
    private Date fechaSeleccionada;
    private List<DetIncidencia> lstIncidencias;
    private List<DetIncidencia> listaPermisos;
    private List<DetIncidencia> listaPermisosFiltrada;
    private List<DetIncidencia> listaPrendas;
    private List<DetIncidencia> listaArticulos;
    private List<CatTipoSolicitud> lstTipoSol;
    private List<DetIncidencia> listAuxPermisos;
    private List<Date> lstRangoRegistro;
    private final List<Date> diasDeAsueto;
    private List<Integer> invalidDays;
    private List<Date> diasDeVacaciones;
    private Date minDate;
    private Date periodoInicio;
    private Date periodoFin;
    private boolean incidenciaVacaciones;
    private boolean incidenciaPermiso;
    private boolean estatusAceptado;
    private boolean estatusRechazado;
    private boolean estatusCancelado;
    private boolean estatusEnviado;
    private Integer diasVacacionesSolicitados;

    private Date inicio;
    private Date fin;
    private DetRegistro registro;
    private RegistroDAO registroDAO;
    private List<DetRegistro> listRegistro;
    private List<DetRegistro> listRegistroFiltrada;
    private CatEstatusRegistro estatusRegJustificado;
    private EstatusRegistroDAO estatusRegistroDAO;
    private final String retardo = "R";
    private final String justificado = "J";
    private String empleadoAsistenciaTXT = "";

    private DetEmpleado empleadoSelected;
    private final HttpServletRequest httpServletRequest;
    private ManageStatus status;
    private final EmpleadoDAO empleadoDAO;

    private String sGoceSueldo;
    private String descripcionRechazo;

    public IncidenciaBean() {
        incidenciaDAO = new IncidenciaDAO();
        tipoSolicitudDAO = new TipoSolicitudDAO();
        minDate = new Date();

        empleadoDAO = new EmpleadoDAO();
        httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.empleadoSelected = (DetEmpleado) httpServletRequest.getSession(true).getAttribute("empleado");

        empleadoSelected = empleadoDAO.buscarPorId(empleadoSelected.getIdEmpleado());
        inicializarIncidencia();
        this.diasDeAsueto = DiasDeDescansoObligatorioBL.diasDeAsueto();
        this.registroDAO = new RegistroDAO();
        this.estatusRegistroDAO = new EstatusRegistroDAO();

        /* * * * * estaba en el post-construct* * * * * * * */

        consultaIncidencias();
        consultarRegistrosAsistencia();
        this.lstTipoSol = this.tipoSolicitudDAO.buscarActivos();
        this.listRegistro = this.registroDAO.buscarPorEmpleadoEstatus(this.retardo);
        this.periodoFin = new Date();
        this.periodoInicio = DateUtil.inicializaFechaInicioAnioCurso(DateUtil.getAnio(periodoFin));
        this.validarFechaInicioFinIncidencia();
        this.fin = new Date();
        this.inicio = DateUtil.inicializaFechaInicioAnioCurso(DateUtil.getAnio(this.fin));
        this.validarFechaInicioFinRegistro();
        this.incidenciaVacaciones = false;
        this.incidenciaPermiso = false;
        this.estatusAceptado = false;
        this.estatusRechazado = false;
        this.estatusCancelado = false;
        this.estatusEnviado = false;
        this.status = new ManageStatus();
        this.sGoceSueldo = "100.0";
    }

    @PostConstruct
    public void init() {

    }

    public void validarFechaInicioFinIncidencia() {
        if (this.periodoFin.equals(this.periodoInicio)) {
            this.periodoInicio = DateUtil.inicializaFechaInicioAnioCurso(DateUtil.getAnio(periodoFin) - 1);
        }
    }

    private void consultaIncidencias() {
        this.lstIncidencias = incidenciaDAO.buscarTodos();

        this.listaPrendas = lstIncidencias.stream()
                .filter(objeto -> objeto.getTipoIncidencia().getClave().trim().matches("PR"))
                .collect(Collectors.toList());

        this.listaArticulos = lstIncidencias.stream()
                .filter(objeto -> objeto.getTipoIncidencia().getClave().trim().matches("A"))
                .collect(Collectors.toList());
    }

    public List<DetIncidencia> consultarTipoPermisos()
            throws SGPException {
        List<DetIncidencia> listaPeriodo = new ArrayList<DetIncidencia>();

        if (this.periodoInicio == null || this.periodoFin == null)
            throw new SGPException("El periodo de consulta de permisos / vacaciones es incorrecto.");

        listaPeriodo = this.incidenciaDAO.buscarPermisos(this.periodoInicio, this.periodoFin);

        List<DetIncidencia> listaTipoPermiso = new ArrayList<DetIncidencia>();

        if (this.incidenciaPermiso) {
            List<DetIncidencia> listAux = listaPeriodo.stream()
                    .filter(objeto -> objeto.getSolPermiso().getTipoSolicitud().getClave().trim().matches("P"))
                    .collect(Collectors.toList());
            listaTipoPermiso = Stream.concat(listaTipoPermiso.stream(), listAux.stream()).collect(Collectors.toList());
        }

        if (this.incidenciaVacaciones) {
            List<DetIncidencia> listAux = listaPeriodo.stream()
                    .filter(objeto -> objeto.getSolPermiso().getTipoSolicitud().getClave().trim().matches("V"))
                    .collect(Collectors.toList());
            listaTipoPermiso = Stream.concat(listaTipoPermiso.stream(), listAux.stream()).collect(Collectors.toList());
        }

        List<DetIncidencia> listaTipoEstatus = new ArrayList<>();

        if (!this.estatusEnviado && !this.estatusAceptado && !this.estatusRechazado && !this.estatusCancelado) {
            listaTipoEstatus = listaTipoPermiso;
        }

        if (this.estatusEnviado) {
            List<DetIncidencia> listAux = listaTipoPermiso.stream()
                    .filter(objeto -> objeto.getEstatusIncidencia().getIdEstatus().equals(1))
                    .collect(Collectors.toList());
            listaTipoEstatus = Stream.concat(listaTipoEstatus.stream(), listAux.stream()).collect(Collectors.toList());
        }

        if (this.estatusAceptado) {
            List<DetIncidencia> listAux = listaTipoPermiso.stream()
                    .filter(objeto -> objeto.getEstatusIncidencia().getIdEstatus().equals(2))
                    .collect(Collectors.toList());
            listaTipoEstatus = Stream.concat(listaTipoEstatus.stream(), listAux.stream()).collect(Collectors.toList());
        }

        if (this.estatusRechazado) {
            List<DetIncidencia> listAux = listaTipoPermiso.stream()
                    .filter(objeto -> objeto.getEstatusIncidencia().getIdEstatus().equals(3))
                    .collect(Collectors.toList());
            listaTipoEstatus = Stream.concat(listaTipoEstatus.stream(), listAux.stream()).collect(Collectors.toList());
        }

        if (this.estatusCancelado) {
            List<DetIncidencia> listAux = listaTipoPermiso.stream()
                    .filter(objeto -> objeto.getEstatusIncidencia().getIdEstatus().equals(4))
                    .collect(Collectors.toList());
            listaTipoEstatus = Stream.concat(listaTipoEstatus.stream(), listAux.stream()).collect(Collectors.toList());
        }

        return listaTipoEstatus;
    }

    public void visualizaDialog(DetIncidencia solicitudIncidencia) {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Incidencia";
        try {
            incidenciaSelected = solicitudIncidencia;
            empleadoSelected = solicitudIncidencia.getEmpleado();
            List<Date> fechas;
            this.descripcionRechazo = "";
            switch (incidenciaSelected.getTipoIncidencia().getClave()) {
                case IncidenciaBL.TP_PERMISO: // Tipo Permisos
                    EmpleadoBL.empleadoTieneDiasLaborales(incidenciaSelected.getEmpleado());
                    switch (incidenciaSelected.getSolPermiso().getTipoSolicitud().getIdTipoSolicitud()) {
                        case 1:// PERMISO
                            fechaSeleccionada = incidenciaSelected.getSolPermiso().getFechaInicio();
                            break;
                    }
                    this.invalidDays = IncidenciaBL.obtenerDiasSeleccionados(empleadoSelected.getDatoEmpresa());
                    fechas = IncidenciaBL.fechasSolicitudPermiso(incidenciaSelected.getSolPermiso());
                    this.diasDeVacaciones = DateUtil.diasVacacionesSolicitados(fechas,
                            DiasDeDescansoObligatorioBL.diasDeAsueto(), empleadoSelected.getDatoEmpresa());
                    log.info("Dias Solicitados: {}", this.diasDeVacaciones.toString());
                    this.diasVacacionesSolicitados = this.diasDeVacaciones.size();
                    log.info("Total Dias de Vacaciones Solicitados: {}", this.diasVacacionesSolicitados);
                    PrimeFaces.current().executeScript("PF('dialogPermisos').show();");
                    break;

                case IncidenciaBL.TP_VACACIONES: // Tipo Vacaciones
                    this.incidenciaSelected = incidenciaDAO.cargar(solicitudIncidencia.getIdIncidencia())
                            .orElseThrow(() -> new SGPException("No se encontró la incidencia solicitada."));

                    EmpleadoBL.empleadoTieneDiasLaborales(incidenciaSelected.getEmpleado());
                    this.invalidDays = IncidenciaBL.obtenerDiasSeleccionados(empleadoSelected.getDatoEmpresa());

                    fechas = this.incidenciaSelected.getSolPermiso().getDiasPermiso().stream()
                            .map(DetDiaPermiso::getFecha)
                            .collect(Collectors.toList());

                    log.info("Dias Solicitados: {}", fechas.toString());
                    this.diasVacacionesSolicitados = fechas.size();
                    lstRangoRegistro = fechas;

                    log.info("Total Dias de Vacaciones Solicitados: {}", this.diasVacacionesSolicitados);
                    PrimeFaces.current().executeScript("PF('dialogPermisos').show();");
                    break;

                case IncidenciaBL.TP_PRENDA: // Tipo Prendas
                    PrimeFaces.current().executeScript("PF('dialogPrendas').show();");
                    break;
                // Tipo Articulos
                case IncidenciaBL.TP_ARTICULO:
                    PrimeFaces.current().executeScript("PF('dialogArticulos').show();");
                    break;
                default:
                    log.warn("EX-0023: Error al seleccionar opción");
            }
        } catch (SGPException e) {
            log.warn("Error al abrir el registro de incidencia del empleado: {}",
                    empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn("EX-0032: {}, ", e.getMessage());
            mensaje = "Consulte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("formIncidencias:messages");
        } catch (Exception ex) {
            log.warn("Error al abrir el registro de incidencia del empleado: {}",
                    empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            mensaje = "Consulte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("formIncidencias:messages");
        }
    }

    public void inicializarIncidencia() {
        incidenciaSelected = new DetIncidencia();
        incidenciaSelected.setEstatusIncidencia(new CatEstatusIncidencia());
        incidenciaSelected.setSolArticulo(new DetSolicitudArticulo());
        incidenciaSelected.setSolPermiso(new DetSolicitudPermiso());
        incidenciaSelected.setSolPrenda(new DetSolicitudPrenda());
        incidenciaSelected.setTipoIncidencia(new CatTipoIncidencia());
    }

    public void aprobarIncidencia() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Incidencia";

        try {
            BigDecimal valor = null;
            if(incidenciaSelected.getEmpleado().getEmpleadoConfiguracion().getGoceSueldo() == null) {
            	valor = BigDecimal.ZERO;
            } else if (incidenciaSelected.getEmpleado().getEmpleadoConfiguracion().getGoceSueldo() == false) {
                valor = BigDecimal.ZERO;
            } else {
                if (this.sGoceSueldo != null && !this.sGoceSueldo.isEmpty()) {
                    valor = new BigDecimal(this.sGoceSueldo);
                }
            }

            incidenciaSelected.setEstatusIncidencia(EstatusIncidenciaBL.estatusAprobado());
            incidenciaSelected.getSolPermiso().setEstatus(EstatusSolicitudBL.estatusAprobado());
            incidenciaSelected.getSolPermiso().setGoceSueldo(valor);
            incidenciaSelected.setEmpleadoRev(empleadoSelected);
            incidenciaSelected.setFechaMod(new Date());
            incidenciaSelected.getSolPermiso().setFechaMod(new Date());
            incidenciaSelected.getSolPermiso().setEmpleadoRev(empleadoSelected);

            if (incidenciaSelected.getEstatusIncidencia().getClave().trim().matches("A")) {
                RegistroBL.guardarRegistroVacaciones(empleadoSelected, incidenciaSelected,
                        DiasDeDescansoObligatorioBL.diasDeAsueto());
            }

            incidenciaDAO.actualizar(incidenciaSelected);
            log.info("Dias solicitados del empleado {} son: {}", empleadoSelected.getIdEmpleado(),
                    this.diasVacacionesSolicitados);
            mensaje = "Solicitud aprobada correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (SGPException ex) {
            log.warn("Error al guardar el status del registro de la incidencia del empleado: {}",
                    empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn(ex.getMessage());
            mensaje = "Error al actualizar la solicitud";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            consultaIncidencias();
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("formIncidencias:messages", "formIncidencias:tabViewI:dtIncidencias");
            PrimeFaces.current().executeScript("PF('dialogPermisos').hide()");
        }
    }

    public void rechazarIncidencia() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Incidencia";

        try {
            this.incidenciaSelected.setEstatusIncidencia(EstatusIncidenciaBL.estatusRechazado());
            this.incidenciaSelected.setEmpleadoRev(empleadoSelected);
            this.incidenciaSelected.setFechaMod(new Date());

            this.incidenciaSelected.getSolPermiso().setFechaMod(new Date());
            this.incidenciaSelected.getSolPermiso().setEstatus(EstatusSolicitudBL.estatusRechazado());
            this.incidenciaSelected.getSolPermiso().setEmpleadoRev(this.empleadoSelected);
            this.incidenciaSelected.getSolPermiso().setDescripcionRechazo(this.descripcionRechazo);

            incidenciaDAO.actualizar(incidenciaSelected);

            severity = FacesMessage.SEVERITY_INFO;
            mensaje = "Incidencia rechazada";
        } catch (SGPException sgpEx) {
            log.error("Error: no se pudo eliminar la incidencia. " + sgpEx);
            mensaje = "Error al rechazar la incidencia";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            consultaIncidencias();
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("formIncidencias:messages", "formIncidencias:tabViewI:dtIncidencias");
        }
    }

    public void cancelarIncidencia() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Incidencia";
        try {

            String tipoIncidencia = incidenciaSelected.getSolPermiso().getTipoSolicitud().getClave();

            if ("V".equals(tipoIncidencia)) {
                cancelarIncidenciaVacaciones();
            } else {

                incidenciaSelected.setEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
                incidenciaSelected.setEstatusIncidencia(EstatusIncidenciaBL.estatusCancelado());
                incidenciaSelected.setFechaMod(new Date());
                incidenciaSelected.getSolPermiso().setEstatus(EstatusSolicitudBL.estatusCancelado());
                incidenciaSelected.getSolPermiso().setFechaMod(new Date());
                incidenciaSelected.getSolPermiso().setEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));

                log.info("SolicitudPermiso: {}", incidenciaSelected.getTipoIncidencia().toString());
                String clave = "";
                switch (incidenciaSelected.getTipoIncidencia().getClave().trim()) {
                    case "PE":
                        clave += "P";
                        break;
                    case "V":
                        clave += "V";
                        break;
                    default:
                        throw new SGPException(
                                "No hay solicitudes de vacaciones y/o permiso, contacte a su administrador de sistemas");
                }

                incidenciaDAO.actualizar(incidenciaSelected);
            }

            mensaje = "Solicitud cancelada correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (SGPException ex) {
            log.warn("Error al guardar el status del registro de la incidencia del empleado: {}",
                    empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn("EX-0028: ", ex);
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (RuntimeException ex) {
            log.warn("Error al guardar el status del registro de la incidencia del empleado: {}",
                    empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn("EX-0028: ", ex);
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
        } catch (Exception ex) {
            log.warn("Error al guardar el status del registro de la incidencia del empleado: {}",
                    empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn("EX-0028: ", ex);
            mensaje = "Error desconocido, contacte con el administrador se sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            consultaIncidencias();
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("formIncidencias:messages", "formIncidencias:tabViewI:dtIncidencias");
            PrimeFaces.current().executeScript("PF('dialogoCancelar').hide()");
        }
    }

    public void cancelarIncidenciaVacaciones() throws SGPException, RuntimeException, Exception {

        Date inicioPeriodoVacacional = incidenciaSelected.getSolPermiso().getFechaInicio();
        Date finPeriodoVacacional = incidenciaSelected.getSolPermiso().getFechaFin();

        DateUtil.setTime(inicioPeriodoVacacional, 0, 0, 0, 0);
        DateUtil.setTime(finPeriodoVacacional, 23, 59, 59);

        Date hoy = new Date();
        DateUtil.setTime(hoy, 0, 0, 0);

        if (finPeriodoVacacional.before(hoy)) {
            throw new RuntimeException("El periodo vacacional ya ha concluido");
        }

        incidenciaSelected.setEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
        incidenciaSelected.setEstatusIncidencia(EstatusIncidenciaBL.estatusCancelado());
        incidenciaSelected.setFechaMod(new Date());
        incidenciaSelected.getSolPermiso().setEstatus(EstatusSolicitudBL.estatusCancelado());
        incidenciaSelected.getSolPermiso().setFechaMod(new Date());
        incidenciaSelected.getSolPermiso().setEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
        incidenciaDAO.actualizar(incidenciaSelected);

        if (inicioPeriodoVacacional.after(hoy)) {

            List<DetRegistro> registros = RegistroBL.buscarRegistrosVacaciones(incidenciaSelected);

            RegistroVacacionesDAO registroVacacionesDAO = new RegistroVacacionesDAO();

            for (DetRegistro registro : registros) {
                DetRegistroVacaciones registroVacacion = registro.getRegistroVacaciones();
                registroVacacionesDAO.eliminar(registroVacacion);
                registroDAO.eliminar(registro);
            }

        }

        if (inicioPeriodoVacacional.before(hoy) && finPeriodoVacacional.after(hoy)) {
            throw new SGPException("El periodo esta en curso, no se eliminaran los registros");
        }

    }

    public void guardarEstatusArticulo(boolean aprobada) {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Incidencia";
        try {
            if (aprobada) {
                incidenciaSelected.setEstatusIncidencia(EstatusIncidenciaBL.estatusAprobado());
                incidenciaSelected.getSolArticulo().setEstatus(EstatusSolicitudBL.estatusAprobado());
                mensaje = "Solicitud aprobada correctamente";
            } else if (!aprobada) {
                incidenciaSelected.setEstatusIncidencia(EstatusIncidenciaBL.estatusRechazado());
                incidenciaSelected.getSolArticulo().setEstatus(EstatusSolicitudBL.estatusRechazado());
                mensaje = "Solicitud rechazada correctamente";
            }
            incidenciaSelected.setFechaMod(new Date());
            incidenciaSelected.getSolArticulo().setFechaMod(new Date());
            incidenciaSelected.getSolArticulo().setEmpleadoRev(empleadoSelected);
            incidenciaDAO.actualizar(incidenciaSelected);

            severity = FacesMessage.SEVERITY_INFO;
        } catch (SGPException ex) {
            log.warn("Error al guardar el status del registro del artículo del empleado: {}",
                    empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn("EX-0029: ", ex);
            mensaje = "Error al actualizar la solicitud";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            consultaIncidencias();
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("formIncidencias:messages",
                    "formIncidencias:tabViewI:dtArticulosSolicitados");
            PrimeFaces.current().executeScript("PF('dialogArticulos').hide()");
        }
    }

    public void guardarEstatusPrenda(boolean aprobada) {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Incidencia";
        try {
            if (aprobada) {
                incidenciaSelected.setEstatusIncidencia(EstatusIncidenciaBL.estatusAprobado());
                incidenciaSelected.getSolPrenda().setEstatus(EstatusSolicitudBL.estatusAprobado());
                mensaje = "Solicitud aprobada correctamente";
            } else if (!aprobada) {
                incidenciaSelected.setEstatusIncidencia(EstatusIncidenciaBL.estatusRechazado());
                incidenciaSelected.getSolPrenda().setEstatus(EstatusSolicitudBL.estatusRechazado());
                mensaje = "Solicitud rechazada correctamente";
            }
            incidenciaSelected.setFechaMod(new Date());
            incidenciaSelected.getSolPrenda().setFechaMod(new Date());
            incidenciaSelected.getSolPrenda().setEmpleadoRev(new DetEmpleado(empleadoSelected.getIdEmpleado()));
            incidenciaDAO.actualizar(incidenciaSelected);

            severity = FacesMessage.SEVERITY_INFO;
        } catch (SGPException ex) {
            log.warn("Error al guardar el status del registro de la prenda del empleado: {}",
                    empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn("EX-0030: ", ex);
            mensaje = "Error al actualizar la solicitud, contacte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            consultaIncidencias();
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("formIncidencias:messages",
                    "formIncidencias:tabViewI:dtPrendasSolicitados");
            PrimeFaces.current().executeScript("PF('dialogPrendas').hide()");
        }
    }

    public void consultarRegistrosAsistencia() {
        this.estatusRegJustificado = estatusRegistroDAO.buscarPorCodigo(justificado);
    }

    public void validarFechaInicioFinRegistro() {
        if (this.fin.equals(this.inicio)) {
            this.inicio = DateUtil.inicializaFechaInicioAnioCurso(DateUtil.getAnio(fin) - 1);
        }
    }

    public List<DetRegistro> consultarRegistros() {
        List<DetRegistro> listaPeriodo = new ArrayList<DetRegistro>();

        if (this.inicio != null && this.fin != null) {
            listaPeriodo = this.listRegistro.stream()
                    .filter(objeto -> objeto.getFechaEntrada().compareTo(this.inicio) == 0
                            || objeto.getFechaEntrada().compareTo(this.inicio) > 0)
                    .filter(objeto -> objeto.getFechaEntrada().compareTo(this.fin) == 0
                            || objeto.getFechaEntrada().compareTo(this.fin) < 0)
                    .collect(Collectors.toList());
        }

        List<DetRegistro> listaEmpleado = new ArrayList<DetRegistro>();

        if (this.empleadoAsistenciaTXT.equals("")) {
            listaEmpleado = listaPeriodo;
        }

        if (this.empleadoAsistenciaTXT != null && !this.empleadoAsistenciaTXT.equals("")) {
            listaEmpleado = listaPeriodo.stream().filter(objeto -> objeto.getIdEmpleado().getNombre()
                    .contains(this.empleadoAsistenciaTXT.trim().toUpperCase())
                    || objeto.getIdEmpleado().getPrimerAp().contains(this.empleadoAsistenciaTXT.trim().toUpperCase())
                    || objeto.getIdEmpleado().getSegundoAp().contains(this.empleadoAsistenciaTXT.trim().toUpperCase()))
                    .collect(Collectors.toList());
        }

        return listaEmpleado;
    }

    public void editarRegistro(DetRegistro registroAsistencia) {
        this.registro = registroAsistencia;
        log.info("Editando un registro de asistencia: {}", this.registro);
    }

    public void actualizarRegistro() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Registro";
        try {
            this.registro.setStatus(estatusRegJustificado);
            RegistroBL.actualizarRegistroAsistencia(this.registro);
            mensaje = "Se actualizo el registro de asistencia";
            severity = FacesMessage.SEVERITY_INFO;
            PrimeFaces.current().executeScript("PF('dialogoJustificar').hide()");
        } catch (SGPException ex) {
            log.warn("Error al guardar el registro de asistencia del empleado: {}",
                    empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn("EX-0030: ", ex);
            mensaje = "Error al actualizar el registro, contacte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            consultarRegistrosAsistencia();
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("formIncidencias:messages", "formIncidencias:tabViewI:dtRegistro");
        }
    }

    public boolean permiteAprobar() {
        boolean respuesta = false;
        if (this.incidenciaSelected == null)
            return false;

        if (this.incidenciaSelected.getSolPermiso() == null)
            return false;

        if (this.incidenciaSelected.getEstatusIncidencia() == null)
            return false;

        if (this.incidenciaSelected.getEstatusIncidencia().getClave() == null)
            return false;

        switch (this.incidenciaSelected.getEstatusIncidencia().getClave()) {
            case IncidenciaBL.ST_ENVIADA:
                respuesta = true;
                break;
            case IncidenciaBL.ST_APROBADA:
            case IncidenciaBL.ST_RECHAZADA:
            case IncidenciaBL.ST_CANCELADA:
                respuesta = false;
                break;
            default:
                respuesta = false;
        }

        return respuesta;
    }

    public boolean permiteRechazar() {
        boolean respuesta = false;
        if (this.incidenciaSelected == null)
            return false;

        if (this.incidenciaSelected.getSolPermiso() == null)
            return false;

        if (this.incidenciaSelected.getEstatusIncidencia() == null)
            return false;

        if (this.incidenciaSelected.getEstatusIncidencia().getClave() == null)
            return false;

        switch (this.incidenciaSelected.getEstatusIncidencia().getClave()) {
            case IncidenciaBL.ST_ENVIADA:
                respuesta = true;
                break;
            case IncidenciaBL.ST_APROBADA:
            case IncidenciaBL.ST_RECHAZADA:
            case IncidenciaBL.ST_CANCELADA:
                respuesta = false;
                break;
            default:
                respuesta = false;
        }

        return respuesta;
    }

    public boolean permiteCancelar() {
        boolean respuesta = false;
        if (this.incidenciaSelected == null)
            return false;

        if (this.incidenciaSelected.getSolPermiso() == null)
            return false;

        if (this.incidenciaSelected.getEstatusIncidencia() == null)
            return false;

        if (this.incidenciaSelected.getEstatusIncidencia().getClave() == null)
            return false;

        switch (this.incidenciaSelected.getEstatusIncidencia().getClave()) {
            case IncidenciaBL.ST_APROBADA:
                respuesta = true;
                break;
            case IncidenciaBL.ST_ENVIADA:
            case IncidenciaBL.ST_RECHAZADA:
            case IncidenciaBL.ST_CANCELADA:
                respuesta = false;
                break;
            default:
                respuesta = false;
        }

        return respuesta;
    }

    public DetIncidencia getIncidenciaSelected() {
        return incidenciaSelected;
    }

    public void setIncidenciaSelected(DetIncidencia incidenciaSelected) {
        this.incidenciaSelected = incidenciaSelected;
    }

    public List<DetIncidencia> getLstIncidencias() {
        return lstIncidencias;
    }

    public void setLstIncidencias(List<DetIncidencia> lstIncidencias) {
        this.lstIncidencias = lstIncidencias;
    }

    public List<DetIncidencia> getListaPermisos() {
        return listaPermisos;
    }

    public void setListaPermisos(List<DetIncidencia> listaPermisos) {
        this.listaPermisos = listaPermisos;
    }

    public List<DetIncidencia> getListaArticulos() {
        return listaArticulos;
    }

    public void setListaArticulos(List<DetIncidencia> listaArticulos) {
        this.listaArticulos = listaArticulos;
    }

    public Date getFechaSeleccionada() {
        return fechaSeleccionada;
    }

    public void setFechaSeleccionada(Date fechaSeleccionada) {
        this.fechaSeleccionada = fechaSeleccionada;
    }

    public List<Date> getLstRangoRegistro() {
        return lstRangoRegistro;
    }

    public void setLstRangoRegistro(List<Date> lstRangoRegistro) {
        this.lstRangoRegistro = lstRangoRegistro;
    }

    public List<Integer> getInvalidDays() {
        return invalidDays;
    }

    public void setInvalidDays(List<Integer> invalidDays) {
        this.invalidDays = invalidDays;
    }

    public List<CatTipoSolicitud> getLstTipoSol() {
        return lstTipoSol;
    }

    public void setLstTipoSol(List<CatTipoSolicitud> lstTipoSol) {
        this.lstTipoSol = lstTipoSol;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public DetEmpleado getEmpleadoSelected() {
        return empleadoSelected;
    }

    public void setEmpleadoSelected(DetEmpleado empleadoSelected) {
        this.empleadoSelected = empleadoSelected;
    }

    public List<DetIncidencia> getListaPrendas() {
        return listaPrendas;
    }

    public void setListaPrendas(List<DetIncidencia> listaPrendas) {
        this.listaPrendas = listaPrendas;
    }

    public ManageStatus getStatus() {
        return status;
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

    public boolean isIncidenciaVacaciones() {
        return incidenciaVacaciones;
    }

    public void setIncidenciaVacaciones(boolean incidenciaVacaciones) {
        this.incidenciaVacaciones = incidenciaVacaciones;
    }

    public boolean isIncidenciaPermiso() {
        return incidenciaPermiso;
    }

    public void setIncidenciaPermiso(boolean incidenciaPermiso) {
        this.incidenciaPermiso = incidenciaPermiso;
    }

    public boolean isEstatusAceptado() {
        return estatusAceptado;
    }

    public void setEstatusAceptado(boolean estatusAceptado) {
        this.estatusAceptado = estatusAceptado;
    }

    public boolean isEstatusRechazado() {
        return estatusRechazado;
    }

    public void setEstatusRechazado(boolean estatusRechazado) {
        this.estatusRechazado = estatusRechazado;
    }

    public boolean isEstatusCancelado() {
        return estatusCancelado;
    }

    public void setEstatusCancelado(boolean estatusCancelado) {
        this.estatusCancelado = estatusCancelado;
    }

    public boolean isEstatusEnviado() {
        return estatusEnviado;
    }

    public void setEstatusEnviado(boolean estatusEnviado) {
        this.estatusEnviado = estatusEnviado;
    }

    public List<DetIncidencia> getListAuxPermisos() {
        return listAuxPermisos;
    }

    public void setListAuxPermisos(List<DetIncidencia> listAuxPermisos) {
        this.listAuxPermisos = listAuxPermisos;
    }

    public List<DetIncidencia> getListaPermisosFiltrada() {
        return listaPermisosFiltrada;
    }

    public void setListaPermisosFiltrada(List<DetIncidencia> listaPermisosFiltrada) {
        this.listaPermisosFiltrada = listaPermisosFiltrada;
    }

    public Integer getDiasVacacionesSolicitados() {
        return diasVacacionesSolicitados;
    }

    public void setDiasVacacionesSolicitados(Integer diasVacacionesSolicitados) {
        this.diasVacacionesSolicitados = diasVacacionesSolicitados;
    }

    public List<Date> getDiasDeAsueto() {
        return diasDeAsueto;
    }

    public List<Date> getDiasDeVacaciones() {
        return diasDeVacaciones;
    }

    public void setDiasDeVacaciones(List<Date> diasDeVacaciones) {
        this.diasDeVacaciones = diasDeVacaciones;
    }

    public String getsGoceSueldo() {
        return sGoceSueldo;
    }

    public void setsGoceSueldo(String sGoceSueldo) {
        this.sGoceSueldo = sGoceSueldo;
    }

    public String getDescripcionRechazo() {
        return descripcionRechazo;
    }

    public void setDescripcionRechazo(String descripcionRechazo) {
        this.descripcionRechazo = descripcionRechazo;
    }

    public DetRegistro getRegistro() {
        return registro;
    }

    public void setRegistro(DetRegistro registro) {
        this.registro = registro;
    }

    public List<DetRegistro> getListRegistro() {
        return listRegistro;
    }

    public void setListRegistro(List<DetRegistro> listRegistro) {
        this.listRegistro = listRegistro;
    }

    public List<DetRegistro> getListRegistroFiltrada() {
        return listRegistroFiltrada;
    }

    public void setListRegistroFiltrada(List<DetRegistro> listRegistroFiltrada) {
        this.listRegistroFiltrada = listRegistroFiltrada;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public String getEmpleadoAsistenciaTXT() {
        return empleadoAsistenciaTXT;
    }

    public void setEmpleadoAsistenciaTXT(String empleadoAsistenciaTXT) {
        this.empleadoAsistenciaTXT = empleadoAsistenciaTXT;
    }
    // </editor-fold>
}
