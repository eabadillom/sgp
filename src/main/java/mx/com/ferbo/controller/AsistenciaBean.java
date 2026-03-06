package mx.com.ferbo.controller;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

import mx.com.ferbo.business.dianolaboral.DiasNoLaboralesBL;
import mx.com.ferbo.business.empleado.EmpleadoBL;
import mx.com.ferbo.business.empleado.RegistroAsistenciaBL;
import mx.com.ferbo.business.incidencia.CalendarioBL;
import mx.com.ferbo.business.incidencia.EstatusIncidenciaBL;
import mx.com.ferbo.business.incidencia.EstatusSolicitudBL;
import mx.com.ferbo.business.incidencia.IncidenciaBL;
import mx.com.ferbo.business.incidencia.SolicitudPermisoBL;
import mx.com.ferbo.dao.n.IncidenciaDAO;
import mx.com.ferbo.dao.n.SolicitudPermisoDAO;
import mx.com.ferbo.dao.n.TipoSolicitudDAO;
import mx.com.ferbo.dao.n.VacacionesDAO;
import mx.com.ferbo.model.CatTipoSolicitud;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.model.DetSolicitudPermiso;
import mx.com.ferbo.model.DetVacaciones;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.ManageStatus;
import mx.com.ferbo.util.SGPException;



/**
 *
 * @author Gabo
 */
@Named(value = "asistenciaBean")
@ViewScoped
public class AsistenciaBean implements Serializable {

    private RegistroAsistenciaBL empleadoAsistencia;
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(AsistenciaBean.class);

    private ScheduleModel calendario;
    private ScheduleEvent evento;
    private final SolicitudPermisoDAO solicitudPermisoDAO;
    private final TipoSolicitudDAO tipoSolicitudDAO;
    private DetSolicitudPermiso solicitudSelected;
    private final IncidenciaDAO incidenciaDAO;
    private Date minDate;
    private Date maxDate;

    private List<DetSolicitudPermiso> lstSolicitudes;
    private List<CatTipoSolicitud> lstTipoSol;
    private List<DetIncidencia> lstIncidencias;
    private List<DetIncidencia> incidenciasBuscada;
    private List<Integer> invalidDays;
    private List<Date> lstRangoRegistro;
    private List<SelectItem> lstTipoSolSelect;
    private List<DetVacaciones> periodosVacacionales;
    private DetVacaciones periodoVacacional;
    private Date fechaSeleccionada;
    private List<Date> fechaDatePickerView;
    private final List<Date> diasDeAsueto;
    private Integer totalDiasTomados;

    private VacacionesDAO vacacionesDAO;
    private Integer diasTotalesPermitidos;

    // Obteniendo Empleado
    private DetEmpleado empleadoSelected;
    private DetIncidencia incidencia;
    private HttpServletRequest request;
    private ManageStatus status;
    private static final String PERMISOS = "P";
    private static final String VACACIONES = "V";
    
    private Date periodoSolicitudInicio;
    private Date periodoSolicitudFin;
    
    public AsistenciaBean() {
        this.diasDeAsueto = DiasNoLaboralesBL.diasDeAsueto();
        calendario = new DefaultScheduleModel();
        solicitudPermisoDAO = new SolicitudPermisoDAO();
        tipoSolicitudDAO = new TipoSolicitudDAO();
        incidenciaDAO = new IncidenciaDAO();
        vacacionesDAO = new VacacionesDAO();
        
        lstTipoSolSelect = new ArrayList<>();
        this.fechaDatePickerView = new ArrayList<>();

        empleadoSelected = new DetEmpleado();
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.empleadoSelected = (DetEmpleado) request.getSession(false).getAttribute("empleado");
        this.invalidDays = SolicitudPermisoBL.obtenerDiasSeleccionados(empleadoSelected.getDatoEmpresa());
        this.empleadoAsistencia = new RegistroAsistenciaBL();
        
        this.periodoSolicitudFin = DateUtil.now();
        this.periodoSolicitudInicio = DateUtil.addMonth(this.periodoSolicitudFin, -1);
        evento = new DefaultScheduleEvent();
        
    }

    @PostConstruct
    public void init() {
        actualizarSolicitudPermiso();
        lstTipoSol.forEach((CatTipoSolicitud tipo) -> {
            lstTipoSolSelect.add(new SelectItem(tipo.getIdTipoSolicitud(),
                    tipo.getDescripcion(),
                    tipo.getIdTipoSolicitud() == 3 ? "Solo 1 día" : tipo.getIdTipoSolicitud() == 4 ? "Más de 1 día" : null));
        });

        status = new ManageStatus();
        inicializaRangoFechas();
        actualizarListas();
        calendario = CalendarioBL.crearCalendario(empleadoSelected);
        this.minDate = DateUtil.now();
    }
    
    public void actualizarSolicitudPermiso() {
    	log.info("Buscando solicitudes de permisos...");
    	lstSolicitudes = solicitudPermisoDAO.buscarPorEmpleadoPeriodo(empleadoSelected.getIdEmpleado(), this.periodoSolicitudInicio, this.periodoSolicitudFin);
        lstTipoSol = tipoSolicitudDAO.buscarPermisosyVacaciones(PERMISOS, VACACIONES);
    }
    
    public void actualizarListas() {
        lstIncidencias = incidenciaDAO.buscarPorIdEmpleadoPermiso(empleadoSelected.getIdEmpleado());
    }

    public void inicializaRangoFechas() {
        lstRangoRegistro = new ArrayList<>();
    }
    
    public void eventoSeleccionado(SelectEvent<ScheduleEvent> selectEvent) {
        evento = selectEvent.getObject();
    }

    public void diaSeleccionado(SelectEvent<LocalDateTime> selectEvent) {
        evento = DefaultScheduleEvent.builder().startDate(selectEvent.getObject()).endDate(selectEvent.getObject()).build();
    }
    
    public void onPeriodoSelect(SelectEvent<DetVacaciones> event) {
    	log.debug("Seleccionando periodo vacacional: {}", event.getObject().getIdVacaciones());
    	FacesMessage msg = new FacesMessage("PERIODO SELECCIONADO", String.valueOf(event.getObject().getIdVacaciones()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
        PrimeFaces.current().ajax().update("formActividades:tabView:pnlDetalleVacaciones");
    }
    
    public void onPeriodoUnselect(SelectEvent<DetVacaciones> event) {
    	log.debug("Seleccionando periodo vacacional: {}", event.getObject().getIdVacaciones());
    	FacesMessage msg = new FacesMessage("Product Unselected", String.valueOf(event.getObject().getIdVacaciones()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void guardaSolicitud() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Solicitud";
        
        try {
            if (solicitudSelected.getTipoSolicitud() == null) {
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
                List<Date> fechasSinDiasFeriados = DateUtil.diasVacacionesSolicitados(fechas, this.diasDeAsueto, empleadoSelected.getDatoEmpresa());

                if (fechasSinDiasFeriados.size() > this.diasTotalesPermitidos) {
                    throw new SGPException("El numero de dias seleccionados es mayor a los permitidos");
                }
            }
            
            IncidenciaBL.guardar(incidencia, periodoVacacional);
            
            mensaje = "Se guardo correctamente";
            severity = FacesMessage.SEVERITY_INFO;
            PrimeFaces.current().ajax().update("formActividades:tabView:dtSolicitudes");
        } catch (SGPException ex) {
            log.warn("Error al registrar la solicitud de permiso el empleado: {}", empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn("EX-0022: ", ex);
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
        } catch (NullPointerException ex) {
            log.error("Error al registrar la solicitud de permiso del empleado {}", empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.error("EX-0033: ", ex);
            mensaje = "Consulte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            actualizarListas();
            actualizarSolicitudPermiso();
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
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Solicitud";
        try {
            switch (solicitudSelected.getEstatus().getClave()) {
                case "A":
                case "R":
                case "C":
                    throw new SGPException("No se puede modificar el permiso");
            }
            
            solicitudSelected.setEstatus(EstatusSolicitudBL.estatusCancelado());
            incidencia = this.incidenciaDAO.buscarPorPermiso(empleadoSelected.getIdEmpleado(), solicitudSelected.getIdSolicitud());
            incidencia.setEstatusIncidencia(EstatusIncidenciaBL.estatusCancelado());
            incidencia.setSolPermiso(solicitudSelected);
            incidenciaDAO.actualizar(incidencia);
            
            mensaje = "Se modifico correctamente";
            severity = FacesMessage.SEVERITY_INFO;
            PrimeFaces.current().ajax().update("formActividades:tabView:dtSolicitudes");
        } catch (SGPException e) {
            log.warn("Error al actualizar el registro del permiso del empleado: {}", empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn(e);
            mensaje = e.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
        } catch (Exception e) {
            log.warn("Error al actualizar el registro del permiso del empleado: {}", empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn("EX-0032: ", e);
            mensaje = "Consulte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            actualizarListas();
            actualizarSolicitudPermiso();
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update(":formActividades:messages");
            PrimeFaces.current().executeScript("PF('dialogVacacionesView').hide()");
        }
    }
    
    public void validarDiasEmpleado() {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = "Cargar informacion";
         

        try {
            
            DiasNoLaboralesBL.diasDescansoEstanActualizados();
            EmpleadoBL.empleadoTieneDiasLaborales(empleadoSelected);
            
            this.inicializaSolicitud();
            
            this.periodosVacacionales = this.periodosPorFechaActual();
            this.periodoVacacional = null;
            
            PrimeFaces.current().executeScript("PF('dialogVacaciones').show()");
        } catch (SGPException sgpEx) {
            mensaje = sgpEx.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update(":formActividades:messages");
            log.error("Error al obtener los dias laborables del empleado: {}", sgpEx);
        }
    }

    public void inicializaSolicitud() {
        solicitudSelected = new DetSolicitudPermiso();
        CatTipoSolicitud tipoSolicitud = new CatTipoSolicitud();
        solicitudSelected.setTipoSolicitud(tipoSolicitud);
        this.inicializaRangoFechas();
    }

    public void cerrarDialogoVacacionesView() {
        inicializaSolicitud();
        this.fechaDatePickerView = new ArrayList<>();
    }

    public void actualizaCalendarioSeleccionado() {
        List<Date> fechas = null;
        this.totalDiasTomados = null;
        switch (solicitudSelected.getTipoSolicitud().getClave()) 
        {
            case "P"://PERMISO
                fechas = DateUtil.generarArreglosFechas(solicitudSelected.getFechaInicio(), solicitudSelected.getFechaFin());
                this.totalDiasTomados = DateUtil.diasVacacionesSolicitados(fechas, this.diasDeAsueto, this.empleadoSelected.getDatoEmpresa()).size();
                this.fechaDatePickerView.addAll(fechas);
                break;
            case VACACIONES://VACACIONES
                fechas = DateUtil.generarArreglosFechas(solicitudSelected.getFechaInicio(), solicitudSelected.getFechaFin());
                this.totalDiasTomados = DateUtil.diasVacacionesSolicitados(fechas, this.diasDeAsueto, this.empleadoSelected.getDatoEmpresa()).size();
                this.fechaDatePickerView = Arrays.asList(solicitudSelected.getFechaInicio(), solicitudSelected.getFechaFin());
                break;
        }
        PrimeFaces.current().executeScript("PF('dialogVacacionesView').show();");
    }
    
    public List<DetVacaciones> periodosPorFechaActual() {
        List<DetVacaciones> periodos = new ArrayList<DetVacaciones>();
        this.diasTotalesPermitidos = 0;
        try {
        	log.info("Buscando periodos vacacionales del empleado...");
            List<DetVacaciones> periodosTmp = vacacionesDAO.obtenerPeriodosPorFecha(empleadoSelected.getIdEmpleado(), DateUtil.now());

            for (DetVacaciones periodo : periodosTmp) {
                if (periodo.getDiasTomados() < periodo.getDiasTotales() && (periodo.getDiasPagados() + periodo.getDiasTomados()) < periodo.getDiasTotales()) {
                    periodos.add(periodo);
                    this.diasTotalesPermitidos += (periodo.getDiasTotales() - periodo.getDiasPagados() - periodo.getDiasTomados());
                }
            }
        } catch (SGPException sgpEx) {
            log.info(sgpEx.getMessage());
        }
        return periodos;
    }

    public String formatoPeriodo(DetVacaciones vacacion) {
    	String resultado = null;
    	String sFechaInicio = null;
    	String sFechaFin = null;
    	
    	try {
    		sFechaInicio = DateUtil.getString(vacacion.getFechaInicio(), DateUtil.FORMATO_DD_MM_YYYY);
    		sFechaFin = DateUtil.getString(vacacion.getFechaFin(), DateUtil.FORMATO_DD_MM_YYYY);
    		
    		resultado = String.format("Desde %s al %s", sFechaInicio, sFechaFin);
    	} catch(Exception ex) {
    		log.error("Problema para generar convertir el periodo vacacional a string...", ex);
    	}
        
        return resultado;
    }

    public Date maximoDiasVacaciones() {

        this.maxDate = DateUtil.now();

        this.maxDate = DateUtil.addDay(this.maxDate, this.diasTotalesPermitidos);

        return this.maxDate;
    }
    
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

    public List<Date> getDiasDeAsueto() {
        return diasDeAsueto;
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

	public List<DetVacaciones> getPeriodosVacacionales() {
		return periodosVacacionales;
	}

	public void setPeriodosVacacionales(List<DetVacaciones> periodosVacacionales) {
		this.periodosVacacionales = periodosVacacionales;
	}

	public DetVacaciones getPeriodoVacacional() {
		return periodoVacacional;
	}

	public void setPeriodoVacacional(DetVacaciones periodoVacacional) {
		this.periodoVacacional = periodoVacacional;
	}

	public Date getPeriodoSolicitudInicio() {
		return periodoSolicitudInicio;
	}

	public void setPeriodoSolicitudInicio(Date periodoSolicitudInicio) {
		this.periodoSolicitudInicio = periodoSolicitudInicio;
	}

	public Date getPeriodoSolicitudFin() {
		return periodoSolicitudFin;
	}

	public void setPeriodoSolicitudFin(Date periodoSolicitudFin) {
		this.periodoSolicitudFin = periodoSolicitudFin;
	}
}
