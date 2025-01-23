package mx.com.ferbo.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import mx.com.ferbo.business.dianolaboral.DiasDeDescansoObligatorioBL;
import mx.com.ferbo.business.empleado.RegistroAsistenciaBL;
import mx.com.ferbo.dao.n.IncidenciaDAO;
import mx.com.ferbo.dao.n.TipoSolicitudDAO;
import mx.com.ferbo.dao.n.RegistroDAO;
import mx.com.ferbo.dao.n.SolicitudPermisoDAO;
import mx.com.ferbo.dao.n.VacacionesDAO;
import mx.com.ferbo.model.CatDiaNoLaboral;
import mx.com.ferbo.model.CatEstatusIncidencia;
import mx.com.ferbo.model.CatTipoIncidencia;
import mx.com.ferbo.model.CatTipoSolicitud;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.model.DetSolicitudPermiso;
import mx.com.ferbo.model.DetVacaciones;
import mx.com.ferbo.model.InfDatoEmpresa;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;
import mx.com.ferbo.util.ManageStatus;

/**
 *
 * @author Gabo
 */
@Named(value = "asistenciaBean")
@ViewScoped
public class AsistenciaBean implements Serializable {

    private DiasDeDescansoObligatorioBL diasDeDescansoObligatorio;
    private RegistroAsistenciaBL empleadoAsistencia;
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(AsistenciaBean.class);

    private ScheduleModel calendario;
    private ScheduleEvent evento;
    private final RegistroDAO registroDAO;
    private final SolicitudPermisoDAO solicitudPermisoDAO;
    private final TipoSolicitudDAO tipoSolicitudDAO;
    private DetSolicitudPermiso solicitudSelected;
    private final IncidenciaDAO incidenciaDAO;
    private final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
    private Date minDate;
    private Date maxDate;

    private List<DetRegistro> lstRegistros;
    private List<DetSolicitudPermiso> lstSolicitudes;
    private List<CatTipoSolicitud> lstTipoSol;
    private List<DetIncidencia> lstIncidencias;
    private List<DetIncidencia> incidenciasBuscada;
    private List<Integer> invalidDays;
    private List<Date> lstRangoRegistro;
    private List<SelectItem> lstTipoSolSelect;
    private Date fechaSeleccionada;
    private List<Date> fechaDatePickerView;
    private List<Date> diasDeAsueto;
    private Integer totalDiasTomados;

    private VacacionesDAO vacacionesDAO;
    private Integer diasTotalesPermitidos;

    // Obteniendo Empleado
    private DetEmpleado empleadoSelected;
    private DetIncidencia incidencia;
    private CatTipoIncidencia catTipoIncidencia;
    private CatEstatusIncidencia catEstatusIncidencia;
    private final HttpServletRequest httpServletRequest;
    private ManageStatus status;
    private final String permisos = "P";
    private final String vacaciones = "V";

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public AsistenciaBean() {
        calendario = new DefaultScheduleModel();
        registroDAO = new RegistroDAO();
        solicitudPermisoDAO = new SolicitudPermisoDAO();
        tipoSolicitudDAO = new TipoSolicitudDAO();
        incidenciaDAO = new IncidenciaDAO();
        vacacionesDAO = new VacacionesDAO();
        inicializaSolicitud();
        sdf.setTimeZone(TimeZone.getTimeZone(ZoneId.of("GMT-6").normalized()));
        lstTipoSolSelect = new ArrayList<>();
        this.fechaDatePickerView = new ArrayList<>();

        empleadoSelected = new DetEmpleado();
        httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.empleadoSelected = (DetEmpleado) httpServletRequest.getSession(true).getAttribute("empleado");
        invalidDays = this.obtenerDiasSeleccionados(empleadoSelected.getDatoEmpresa());
        this.empleadoAsistencia = new RegistroAsistenciaBL();
        this.diasDeDescansoObligatorio = new DiasDeDescansoObligatorioBL();
    }

    @PostConstruct
    public void init() {
        evento = new DefaultScheduleEvent();
        actualizarSolicitudPermiso();
        lstTipoSol.forEach((CatTipoSolicitud tipo) -> {
            lstTipoSolSelect.add(new SelectItem(tipo.getIdTipoSolicitud(),
                    tipo.getDescripcion(),
                    tipo.getIdTipoSolicitud() == 3 ? "Solo 1 día" : tipo.getIdTipoSolicitud() == 4 ? "Más de 1 día" : null));
        });

        status = new ManageStatus();
        inicializaRangoFechas();
        actualizarListas();
        this.diasDeAsueto = diasDeDescansoObligatorio.getDiasAsueto();
        generaEventosRegistros(lstRegistros);
        generaEventosIncidencias(lstIncidencias);
        generaEventosDiasDescansoObligatorio(this.diasDeDescansoObligatorio.getDiasNoLaboralSelected());
        this.minDate = DateUtil.now();
    }

    public void inicializaRangoFechas() {
        lstRangoRegistro = new ArrayList<>();
    }

    private void generaEventosRegistros(List<DetRegistro> registros) {
        int retardosSemana = 0;
        int diaInicioSemana = 5; //JUEVES
        int diaInicioRegistros;
        for (DetRegistro registro : registros) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(registro.getFechaEntrada());

            if (cal.get(Calendar.DAY_OF_WEEK) == diaInicioSemana) {
                retardosSemana = 0;
                diaInicioRegistros = diaInicioSemana;
            } else {
                diaInicioRegistros = cal.get(Calendar.DAY_OF_WEEK);
            }

            if (registro.getIdEstatus().getIdEstatus() == 2) {
                retardosSemana += 1;
            }

            //Evento de entrada
            DefaultScheduleEvent eventoEntrada = DefaultScheduleEvent.builder()
                    .title("Entrada " + sdf.format(registro.getFechaEntrada()))
                    .startDate(convertirDateToLocalDateTime(registro.getFechaEntrada()))
                    .endDate(convertirDateToLocalDateTime(registro.getFechaEntrada()))
                    .description(null)
                    .dynamicProperty("estatus", registro.getIdEstatus().getDescripcion())
                    .build();

            calendario.addEvent(eventoEntrada);

            //Genera horario de comida
            DefaultScheduleEvent eventoComida = DefaultScheduleEvent.builder()
                    .title("Comida\n 2:00 PM - 03:00 PM")
                    .startDate(convertirDateToLocalDateTime(generaHorarioComida(true, registro.getFechaEntrada())))
                    .endDate(convertirDateToLocalDateTime(generaHorarioComida(false, registro.getFechaEntrada())))
                    .description(null)
                    .styleClass(estiloByTipo(0))
                    .dynamicProperty("tipoSolicitud", "Horario de comida")
                    .dynamicProperty("idTipoSolicitud", 3)
                    .build();
            calendario.addEvent(eventoComida);

            //Evento de salida
            if (registro.getFechaSalida() != null) {
                DefaultScheduleEvent eventoSalida = DefaultScheduleEvent.builder()
                        .title("Salida " + sdf.format(registro.getFechaSalida()))
                        .startDate(convertirDateToLocalDateTime(registro.getFechaSalida()))
                        .endDate(convertirDateToLocalDateTime(registro.getFechaSalida()))
                        .description(sdf.format(registro.getFechaSalida()))
                        .dynamicProperty("estatus", registro.getIdEstatus().getDescripcion())
                        .build();
                calendario.addEvent(eventoSalida);
            }
        }

    }

    private void generaEventosIncidencias(List<DetIncidencia> incidencias) {
        for (DetIncidencia incidencia : incidencias) {
            LocalDateTime localStartDate = DateUtil.toLocalDateTime(incidencia.getIdSolPermiso().getFechaInicio());
            LocalDateTime localEndDate = DateUtil.toLocalDateTime(incidencia.getIdSolPermiso().getFechaFin());
            DefaultScheduleEvent eventoEntrada = DefaultScheduleEvent.builder()
                    .title(incidencia.getIdSolPermiso().getIdTipoSolicitud().getDescripcion())
                    .startDate(localStartDate)
                    .endDate((incidencia.getIdSolPermiso().getIdTipoSolicitud().getIdTipoSolicitud() == 2) ? localEndDate.plusDays(1) : localEndDate)
                    .allDay(true)
                    .description(null)
                    .styleClass(estiloByTipo(incidencia.getIdSolPermiso().getIdTipoSolicitud().getIdTipoSolicitud()))
                    .dynamicProperty("tipoSolicitud", incidencia.getIdSolPermiso().getIdTipoSolicitud().getDescripcion())
                    .dynamicProperty("idTipoSolicitud", incidencia.getIdSolPermiso().getIdTipoSolicitud().getIdTipoSolicitud())
                    .build();
            calendario.addEvent(eventoEntrada);
        }
    }

    private void generaEventosDiasDescansoObligatorio(List<CatDiaNoLaboral> diasNoLaboral) {
        for (CatDiaNoLaboral auxDiasNoLaboral : diasNoLaboral) {
            DefaultScheduleEvent eventoEntrada = DefaultScheduleEvent.builder()
                    .title(auxDiasNoLaboral.getDescripcion())
                    .startDate(DateUtil.toLocalDateTime(auxDiasNoLaboral.getFecha()))
                    .endDate(DateUtil.toLocalDateTime(auxDiasNoLaboral.getFecha()))
                    .allDay(true)
                    .description(auxDiasNoLaboral.getDescripcion())
                    .styleClass(estiloByTipo(5))
                    .dynamicProperty("tipoSolicitud", "Descanso Obligatorio")
                    .dynamicProperty("idTipoSolicitud", 5)
                    .build();
            calendario.addEvent(eventoEntrada);
        }
    }

    public LocalDateTime convertirDateToLocalDateTime(Date fecha) {
        return fecha.toInstant()
                .atZone(ZoneId.of("GMT-6"))
                .toLocalDateTime();
    }

    public void eventoSeleccionado(SelectEvent<ScheduleEvent> selectEvent) {
        evento = selectEvent.getObject();
    }

    public void diaSeleccionado(SelectEvent<LocalDateTime> selectEvent) {
        evento = DefaultScheduleEvent.builder().startDate(selectEvent.getObject()).endDate(selectEvent.getObject()).build();
    }

    private String findBgColor(Integer idEstatus) {
        String color;
        switch (idEstatus) {
            case 1:
                color = "#689F38";
                break;
            case 2:
                color = "#023e8a";
                break;
            case 3:
                color = "#ef6262";
                break;
            case 4:
                color = "#e9c46a";
                break;
            case 5:
                color = "#2a9d8f";
                break;
            default:
                color = "#00000000";
                break;
        }
        return color;
    }

    private String estiloByTipo(int idTipoEvento) {
        String estilo;
        switch (idTipoEvento) {
            case 1:
                estilo = "ferbo-evento-permiso";
                break;
            case 2:
                estilo = "ferbo-evento-vacaciones";
                break;
            case 3:
                estilo = "ferbo-evento-incapacidad-c";
                break;
            case 4:
                estilo = "ferbo-evento-incapacidad-l";
                break;
            case 5:
                estilo = "ferbo-evento-descansoObligatorio";
                break;
            default:
                estilo = "ferbo-evento-comida";
                break;
        }
        return estilo;
    }

    public void actualizarSolicitudPermiso() {
        lstSolicitudes = solicitudPermisoDAO.buscarPorTipoSolicitud(empleadoSelected.getIdEmpleado(), permisos, vacaciones);
        lstTipoSol = tipoSolicitudDAO.buscarPermisosyVacaciones(permisos, vacaciones);
    }

    public void actualizarListas() {
        lstRegistros = registroDAO.consultaRegistrosPorIdEmp(empleadoSelected.getIdEmpleado());
        lstIncidencias = incidenciaDAO.buscarPorIdEmpleado(empleadoSelected.getIdEmpleado());
        incidenciasBuscada = incidenciaDAO.buscarPorIdEmpleadoPermiso(empleadoSelected.getIdEmpleado());
    }

    public void guardaSolicitud() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Solicitud";
        try {
            if (solicitudSelected.getIdTipoSolicitud() == null) {
                throw new SGPException("Error. Debes selecionar una solicitud");
            }

            if (fechaSeleccionada == null && lstRangoRegistro == null) {
                throw new SGPException("Error. Debes seleccionar una fecha");
            }

            if (fechaSeleccionada != null) {
                DateUtil.setTime(fechaSeleccionada, 0, 0, 0, 0);
                solicitudSelected.setFechaInicio(fechaSeleccionada);
                solicitudSelected.setFechaFin(fechaSeleccionada);
            }

            if (!lstRangoRegistro.isEmpty()) {
                DateUtil.setTime(lstRangoRegistro.get(0), 0, 0, 0, 0);
                solicitudSelected.setFechaInicio(lstRangoRegistro.get(0));
                DateUtil.setTime(lstRangoRegistro.get(1), 0, 0, 0, 0);
                solicitudSelected.setFechaFin(lstRangoRegistro.size() > 1 ? lstRangoRegistro.get(1) : lstRangoRegistro.get(0));

                List<Date> fechas = DateUtil.generarArreglosFechas(lstRangoRegistro.get(0), lstRangoRegistro.get(1));
                List<Date> fechasSinDiasFeriados = empleadoAsistencia.diasVacacionesSolicitados(fechas, this.diasDeAsueto, empleadoSelected.getDatoEmpresa());

                if (fechasSinDiasFeriados.size() > this.diasTotalesPermitidos) {
                    throw new SGPException("El numero de dias seleccionados es mayor a los permitidos");
                }
            }

            if (solicitudSelected.getFechaInicio() == null || solicitudSelected.getFechaFin() == null) {
                throw new SGPException("Error. Debes seleccionar una fecha");
            }

            validarSolicitud(empleadoSelected.getIdEmpleado(), solicitudSelected.getFechaInicio(), solicitudSelected.getFechaFin());

            /*InfDatoEmpresa empleadoEmpresa = empleadoSelected.getDatoEmpresa();
            Integer horaEntrada = DateUtil.getHora(empleadoEmpresa.getHoraEntrada());
            boolean existeRegistro = this.empleadoAsistencia.validarFechasVacaciones(empleadoSelected.getIdEmpleado(), solicitudSelected.getFechaInicio(), solicitudSelected.getFechaFin(), horaEntrada);
            if(existeRegistro == true)
            {
                log.info("Existe por lo menos un registro de vacaciones del empleado {}", empleadoSelected.getNumEmpleado());
                throw new SGPException("Error. El periodo que solicitaste ya se encuentra aceptado");
            }*/
            solicitudSelected.setAprobada((short) 1);
            solicitudSelected.setFechaCap(new Date());
            solicitudSelected.setIdEmpleadoSol(new DetEmpleado(empleadoSelected.getIdEmpleado()));
            solicitudPermisoDAO.guardar(solicitudSelected);

            catTipoIncidencia = new CatTipoIncidencia();
            switch (solicitudSelected.getIdTipoSolicitud().getIdTipoSolicitud()) {
                // Tipo Permisos
                case 1:
                    catTipoIncidencia.setIdTipo(1);
                    break;
                // Tipo Vacaciones
                case 2:
                    catTipoIncidencia.setIdTipo(2);
                    break;
                // Incapacidad Corta
                case 3:
                    catTipoIncidencia.setIdTipo(1);
                    break;
                // Incapacidad Larga
                case 4:
                    catTipoIncidencia.setIdTipo(1);
                    break;
                default:
                    log.warn("EX-0023: Error al seleccionar opción");
            }
            catEstatusIncidencia = new CatEstatusIncidencia();
            catEstatusIncidencia.setIdEstatus(1);

            incidencia = new DetIncidencia();
            incidencia.setIdTipo(catTipoIncidencia);
            incidencia.setIdEmpleado(empleadoSelected);
            incidencia.setIdEstatus(catEstatusIncidencia);
            incidencia.setVisible((short) 1);
            incidencia.setIdSolPermiso(solicitudSelected);
            incidencia.setFechaCap(new Date());

            incidenciaDAO.guardar(incidencia);

            actualizarSolicitudPermiso();
            mensaje = "Se guardo correctamente";
            severity = FacesMessage.SEVERITY_INFO;
            PrimeFaces.current().ajax().update("formActividades:tabView:dtSolicitudes");
        } catch (SGPException ex) {
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
            log.warn("EX-0022: " + ex.getMessage() + ". Error al registrar la solicitud de permiso el empleado: " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
        } catch (NullPointerException ex) {
            mensaje = "Error al registrar la solicitud, no terminaste de capturar los campos";
            severity = FacesMessage.SEVERITY_ERROR;
            log.error("Error al registrar la solicitud de permiso del empleado: {}", ex);
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            inicializaSolicitud();
            fechaSeleccionada = null;
            if (!lstRangoRegistro.isEmpty()) {
                lstRangoRegistro.clear();
            }
            PrimeFaces.current().ajax().update("formActividades:messages");
            PrimeFaces.current().executeScript("PF('dialogVacaciones').hide()");
        }
    }

    public void actualizarSolicitud() {
        actualizarListas();
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Solicitud";
        try {
            if (incidenciasBuscada == null) {
                throw new SGPException("Error con la conexión a la base de datos!!!");
            }

            switch ((int) solicitudSelected.getAprobada()) {
                case 2:
                    throw new SGPException("No se puede modificar el permiso");
                case 3:
                    throw new SGPException("No se puede modificar el permiso");
                case 4:
                    throw new SGPException("No se puede modificar el permiso");
            }

            for (DetIncidencia auxIncidenciaBuscada : incidenciasBuscada) {
                if (Objects.equals(auxIncidenciaBuscada.getIdSolPermiso().getIdSolicitud(), solicitudSelected.getIdSolicitud())) {
                    catEstatusIncidencia = new CatEstatusIncidencia();
                    catEstatusIncidencia.setIdEstatus(4);

                    auxIncidenciaBuscada.setIdEstatus(catEstatusIncidencia);
                    auxIncidenciaBuscada.setIdSolPermiso(solicitudSelected);
                    incidenciaDAO.actualizar(auxIncidenciaBuscada);
                }
            }
            solicitudSelected.setAprobada((short) 4);
            solicitudPermisoDAO.actualizar(solicitudSelected);

            actualizarListas();
            mensaje = "Se modifico correctamente";
            severity = FacesMessage.SEVERITY_INFO;
            PrimeFaces.current().ajax().update("formActividades:tabView:dtSolicitudes");
        } catch (SGPException e) {
            mensaje = e.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
        } catch (Exception e) {
            log.warn("EX-0032: " + e.getMessage() + ". Error al actualizar el registro del permiso del empleado: " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update(":formActividades:messages");
            PrimeFaces.current().executeScript("PF('dialogVacacionesView').hide()");
        }
    }

    public void inicializaSolicitud() {
        solicitudSelected = new DetSolicitudPermiso();
        CatTipoSolicitud tipoSolicitud = new CatTipoSolicitud();
        solicitudSelected.setIdTipoSolicitud(tipoSolicitud);
        this.inicializaRangoFechas();
    }

    public void cerrarDialogoVacacionesView() {
        inicializaSolicitud();
        this.fechaDatePickerView = new ArrayList<>();
    }

    public void actualizaCalendarioSeleccionado() {
        List<Date> fechas = null;
        this.totalDiasTomados = null;
        switch ((int) solicitudSelected.getIdTipoSolicitud().getIdTipoSolicitud()) {
            case 1://PERMISO
            case 3://INCAPACIDAD CORTA
                fechas = DateUtil.generarArreglosFechas(solicitudSelected.getFechaInicio(), solicitudSelected.getFechaFin());
                this.totalDiasTomados = empleadoAsistencia.diasVacacionesSolicitados(fechas, this.diasDeAsueto, empleadoSelected.getDatoEmpresa()).size();
                this.fechaDatePickerView.addAll(fechas);
                break;
            case 2://VACACIONES
            case 4://INCAPACIDAD LARGA
                fechas = DateUtil.generarArreglosFechas(solicitudSelected.getFechaInicio(), solicitudSelected.getFechaFin());
                this.totalDiasTomados = empleadoAsistencia.diasVacacionesSolicitados(fechas, this.diasDeAsueto, empleadoSelected.getDatoEmpresa()).size();
                this.fechaDatePickerView = Arrays.asList(solicitudSelected.getFechaInicio(), solicitudSelected.getFechaFin());
                break;
        }
        PrimeFaces.current().executeScript("PF('dialogVacacionesView').show();");
    }

    private Date generaHorarioComida(boolean inicio, Date fecha) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        if (inicio) {
            calendar.set(Calendar.HOUR_OF_DAY, 15);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, 16);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        }
        return calendar.getTime();
    }

    public List<Integer> obtenerDiasSeleccionados(InfDatoEmpresa empleadoEmpresa) {
        List<Integer> diasSeleccionados = new ArrayList<>();

        if (empleadoEmpresa.getDiaLunes() != true) {
            diasSeleccionados.add(1);
        }
        if (empleadoEmpresa.getDiaMartes() != true) {
            diasSeleccionados.add(2);
        }
        if (empleadoEmpresa.getDiaMiercoles() != true) {
            diasSeleccionados.add(3);
        }
        if (empleadoEmpresa.getDiaJueves() != true) {
            diasSeleccionados.add(4);
        }
        if (empleadoEmpresa.getDiaViernes() != true) {
            diasSeleccionados.add(5);
        }
        if (empleadoEmpresa.getDiaSabado() != true) {
            diasSeleccionados.add(6);
        }
        if (empleadoEmpresa.getDiaDomingo() != true) {
            diasSeleccionados.add(0);
        }
        log.trace("Dias de bloqueo: {}", diasSeleccionados.toString());
        return diasSeleccionados;
    }

    public void validarSolicitud(Integer idEmpleado, Date fechaInicio, Date fechaFin) throws SGPException {
        List<DetSolicitudPermiso> solicitudes = this.solicitudPermisoDAO.buscarPorIdEmpleadoFechasClave(idEmpleado, fechaInicio, fechaFin, permisos, vacaciones);
        log.trace("Solicitudes: {}", solicitudes.toString());

        if (!solicitudes.isEmpty()) {
            throw new SGPException("Error. El periodo que solicitaste ya se encuentra registrado");
        }
    }

    public List<DetVacaciones> periodosPorFechaActual() {
        List<DetVacaciones> periodos = new ArrayList<DetVacaciones>();
        this.diasTotalesPermitidos = 0;
        try {
            List<DetVacaciones> periodosTmp = vacacionesDAO.obtenerPeriodosPorFecha(empleadoSelected.getIdEmpleado(), DateUtil.now());

            for (DetVacaciones periodo : periodosTmp) {
                if (periodo.getDiastomados() < periodo.getDiastotales() && (periodo.getDiaspagados() + periodo.getDiastomados()) < periodo.getDiastotales()) {
                    periodos.add(periodo);
                    this.diasTotalesPermitidos += (periodo.getDiastotales() - periodo.getDiaspagados() - periodo.getDiastomados());
                }
            }
        } catch (SGPException sgpEx) {
            log.info(sgpEx.getMessage());
        }
        return periodos;
    }

    public String foramatoPeriodo(DetVacaciones vacacion) {

        String sfechaInicio = "";
        sfechaInicio += vacacion.getFechainicio();

        String sfechaFin = "";
        sfechaFin += vacacion.getFechafin();

        String resultado = "";

        resultado = "Desde " + sfechaInicio + " al " + sfechaFin;
        resultado = resultado.replaceAll(" 00:00:00.0", "");
        return resultado;
    }

    public Date maximoDiasVacaciones() {

        this.maxDate = DateUtil.now();

        this.maxDate = DateUtil.addDay(this.maxDate, this.diasTotalesPermitidos);

        return this.maxDate;
    }

    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public ScheduleModel getCalendario() {
        return calendario;
    }

    public void setCalendario(ScheduleModel calendario) {
        this.calendario = calendario;
    }

    public ScheduleEvent getEvento() {
        return evento;
    }

    public void setEvento(ScheduleEvent evento) {
        this.evento = evento;
    }

    public Date getMinDate() {
        return minDate;
    }

    public List<DetSolicitudPermiso> getLstSolicitudes() {
        return lstSolicitudes;
    }

    public void setLstSolicitudes(List<DetSolicitudPermiso> lstSolicitudes) {
        this.lstSolicitudes = lstSolicitudes;
    }

    public DetSolicitudPermiso getSolicitudSelected() {
        return solicitudSelected;
    }

    public void setSolicitudSelected(DetSolicitudPermiso solicitudSelected) {
        this.solicitudSelected = solicitudSelected;
    }

    public List<Integer> getInvalidDays() {
        return invalidDays;
    }

    public List<CatTipoSolicitud> getLstTipoSol() {
        return lstTipoSol;
    }

    public List<Date> getLstRangoRegistro() {
        return lstRangoRegistro;
    }

    public void setLstRangoRegistro(List<Date> lstRangoRegistro) {
        this.lstRangoRegistro = lstRangoRegistro;
    }

    public Date getFechaSeleccionada() {
        return fechaSeleccionada;
    }

    public void setFechaSeleccionada(Date fechaSeleccionada) {
        this.fechaSeleccionada = fechaSeleccionada;
    }

    public DetEmpleado getEmpleadoSelected() {
        return empleadoSelected;
    }

    public void setEmpleadoSelected(DetEmpleado empleadoSelected) {
        this.empleadoSelected = empleadoSelected;
    }

    public List<SelectItem> getLstTipoSolSelect() {
        return lstTipoSolSelect;
    }

    public void setLstTipoSolSelect(List<SelectItem> lstTipoSolSelect) {
        this.lstTipoSolSelect = lstTipoSolSelect;
    }

    public ManageStatus getStatus() {
        return status;
    }

    public DiasDeDescansoObligatorioBL getDiasDeDescansoObligatorio() {
        return diasDeDescansoObligatorio;
    }

    public RegistroAsistenciaBL getEmpleadoAsistencia() {
        return empleadoAsistencia;
    }

    public List<Date> getFechaDatePickerView() {
        return fechaDatePickerView;
    }

    public void setFechaDatePickerView(List<Date> fechaDatePickerView) {
        this.fechaDatePickerView = fechaDatePickerView;
    }

    public Integer getTotalDiasTomados() {
        return totalDiasTomados;
    }

    public void setTotalDiasTomados(Integer totalDiasTomados) {
        this.totalDiasTomados = totalDiasTomados;
    }

    public Integer getDiasTotalesPermitidos() {
        return diasTotalesPermitidos;
    }

    public void setDiasTotalesPermitidos(Integer diasTotalesPermitidos) {
        this.diasTotalesPermitidos = diasTotalesPermitidos;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    //</editor-fold>
}
