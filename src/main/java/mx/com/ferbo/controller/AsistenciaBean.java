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

import mx.com.ferbo.dao.n.IncidenciaDAO;
import mx.com.ferbo.dao.n.TipoSolicitudDAO;
import mx.com.ferbo.dao.n.RegistroDAO;
import mx.com.ferbo.dao.n.SolicitudPermisoDAO;
import mx.com.ferbo.model.CatEstatusIncidencia;
import mx.com.ferbo.model.CatTipoIncidencia;
import mx.com.ferbo.model.CatTipoSolicitud;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.model.DetSolicitudPermiso;
import mx.com.ferbo.util.SGPException;
import mx.com.ferbo.util.ManageStatus;

/**
 *
 * @author Gabo
 */
@Named(value = "asistenciaBean")
@ViewScoped
public class AsistenciaBean implements Serializable {
    
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
    private final Date minDate = new Date();

    private List<DetRegistro> lstRegistros;
    private List<DetSolicitudPermiso> lstSolicitudes;
    private List<CatTipoSolicitud> lstTipoSol;
    private List<DetIncidencia> lstIncidencias;
    private List<DetIncidencia> incidenciasBuscada;
    private final List<Integer> invalidDays;
    private List<Date> lstRangoRegistro;
    private List<SelectItem> lstTipoSolSelect;
    private Date fechaSeleccionada;

    // Obteniendo Empleado
    private DetEmpleado empleadoSelected;
    private DetIncidencia incidencia;
    private CatTipoIncidencia catTipoIncidencia;
    private CatEstatusIncidencia catEstatusIncidencia;
    private final HttpServletRequest httpServletRequest;
    private ManageStatus status;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public AsistenciaBean() {
        calendario = new DefaultScheduleModel();
        registroDAO = new RegistroDAO();
        solicitudPermisoDAO = new SolicitudPermisoDAO();
        tipoSolicitudDAO = new TipoSolicitudDAO();
        incidenciaDAO = new IncidenciaDAO();
        inicializaSolicitud();
        sdf.setTimeZone(TimeZone.getTimeZone(ZoneId.of("GMT-6").normalized()));
        lstRangoRegistro = new ArrayList<>();
        invalidDays = new ArrayList<>();
        lstTipoSolSelect = new ArrayList<>();
        invalidDays.add(0);

        empleadoSelected = new DetEmpleado();
        httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.empleadoSelected = (DetEmpleado) httpServletRequest.getSession(true).getAttribute("empleado");
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
        
        actualizarListas();
        generaEventosRegistros(lstRegistros);
        generaEventosIncidencias(lstIncidencias);
        status = new ManageStatus();
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
                    .backgroundColor(findBgColor(registro.getIdEstatus().getIdEstatus()))
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
            DefaultScheduleEvent eventoEntrada = DefaultScheduleEvent.builder()
                    .title(incidencia.getIdSolPermiso().getIdTipoSolicitud().getDescripcion())
                    .startDate(convertirDateToLocalDateTime(incidencia.getIdSolPermiso().getFechaInicio()))
                    .endDate(convertirDateToLocalDateTime(incidencia.getIdSolPermiso().getFechaFin()))
                    .allDay(true)
                    .description(null)
                    .styleClass(estiloByTipo(incidencia.getIdSolPermiso().getIdTipoSolicitud().getIdTipoSolicitud()))
                    .dynamicProperty("tipoSolicitud", incidencia.getIdSolPermiso().getIdTipoSolicitud().getDescripcion())
                    .dynamicProperty("idTipoSolicitud", incidencia.getIdSolPermiso().getIdTipoSolicitud().getIdTipoSolicitud())
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
                color = "#ef6262";
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
            default:
                estilo = "ferbo-evento-comida";
                break;
        }
        return estilo;
    }
    
    public void actualizarSolicitudPermiso()
    {
        lstSolicitudes = solicitudPermisoDAO.buscarPorIdEmpleado(empleadoSelected.getIdEmpleado());
        lstTipoSol = tipoSolicitudDAO.buscarActivos();
    }
    
    public void actualizarListas()
    {
        lstRegistros = registroDAO.consultaRegistrosPorIdEmp(empleadoSelected.getIdEmpleado());
        lstIncidencias = incidenciaDAO.buscarPorIdEmpleado(empleadoSelected.getIdEmpleado());
        incidenciasBuscada = incidenciaDAO.buscarPorIdEmpleadoPermiso(empleadoSelected.getIdEmpleado());
    }

    public void guardaSolicitud() {
        try {
            if (fechaSeleccionada != null) {
                solicitudSelected.setFechaInicio(fechaSeleccionada);
                solicitudSelected.setFechaFin(fechaSeleccionada);
            } else {
                solicitudSelected.setFechaInicio(lstRangoRegistro.get(0));
                solicitudSelected.setFechaFin(lstRangoRegistro.size() > 1 ? lstRangoRegistro.get(1) : lstRangoRegistro.get(0));
            }
            solicitudSelected.setAprobada((short)1);
            solicitudSelected.setFechaCap(new Date());
            solicitudSelected.setIdEmpleadoSol(new DetEmpleado(empleadoSelected.getIdEmpleado()));
            solicitudPermisoDAO.guardar(solicitudSelected);
            
            catTipoIncidencia = new CatTipoIncidencia();
            switch (solicitudSelected.getIdTipoSolicitud().getIdTipoSolicitud()) 
            {
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Solicitud registrada"));
        } catch (SGPException ex) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al registrar la solicitud"));
            log.warn("EX-0022: " + ex.getMessage() + ". Error al registrar la solicitud de permiso el empleado: " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
        }
        inicializaSolicitud();
        fechaSeleccionada = null;
        if(lstRangoRegistro != null){
            lstRangoRegistro.clear();
        }
        PrimeFaces.current().executeScript("PF('dialogVacaciones').hide()");
        PrimeFaces.current().ajax().update("formActividades:messages", "formActividades:tabView:dtSolicitudes");
    }
    
    public void actualizarSolicitud() 
    {
        actualizarListas();
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Permiso";
        try
        {
            if(incidenciasBuscada == null)
            {
                throw new SGPException("Error con la conexión a la base de datos!!!");
            }
            
            switch (solicitudSelected.getAprobada().intValue()) 
            {
                case 2:
                    throw new SGPException("No se puede modificar el permiso");
                case 3:
                    throw new SGPException("No se puede modificar el permiso");
                case 4:
                    throw new SGPException("No se puede modificar el permiso");
            }
            
            for(DetIncidencia auxIncidenciaBuscada : incidenciasBuscada)
            {
                if(Objects.equals(auxIncidenciaBuscada.getIdSolPermiso().getIdSolicitud(), solicitudSelected.getIdSolicitud()))
                {
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Estatus Modificada"));
            PrimeFaces.current().ajax().update("formActividades:tabView:dtSolicitudes");
            PrimeFaces.current().executeScript("PF('dialogVacacionesView').hide()");
        }catch(SGPException e)
        {
            mensaje = e.getMessage();
            severity = FacesMessage.SEVERITY_INFO;
        }catch(Exception e)
        {
            log.warn("EX-0032: " + e.getMessage() + ". Error al actualizar el registro del permiso del empleado: " + empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
        }finally
        {
            if(severity != null)
            {
                message = new FacesMessage(severity, titulo, mensaje);
                FacesContext.getCurrentInstance().addMessage(null, message);
                PrimeFaces.current().ajax().update(":formActividades:messages");
                PrimeFaces.current().executeScript("PF('dialogVacacionesView').hide()");
            }
        }
    }

    public void inicializaSolicitud() {
        solicitudSelected = new DetSolicitudPermiso();
        solicitudSelected.setIdTipoSolicitud(new CatTipoSolicitud());
    }

    public void actualizaCalendarioSeleccionado() {
        switch (solicitudSelected.getIdTipoSolicitud().getIdTipoSolicitud()) {
            case 1://PERMISO
            case 3://INCAPACIDAD CORTA
                fechaSeleccionada = solicitudSelected.getFechaInicio();
                break;
            default:
                lstRangoRegistro = Arrays.asList(solicitudSelected.getFechaInicio(), solicitudSelected.getFechaFin());
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
    //</editor-fold>
}
