package mx.com.ferbo.controller.incidencias;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.com.ferbo.business.dianolaboral.DiasDeDescansoObligatorioBL;
import mx.com.ferbo.business.empleado.EmpleadoBL;
import mx.com.ferbo.business.incidencia.IncidenciaBL;
import mx.com.ferbo.dao.n.IncidenciaDAO;
import mx.com.ferbo.dao.n.TipoSolicitudDAO;
import mx.com.ferbo.model.CatTipoSolicitud;
import mx.com.ferbo.model.DetDiaPermiso;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.ManageStatus;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

/**
 *
 * @author alberto
 */
@Named
@ViewScoped
public class IncidenciasSolicitudesBean implements Serializable 
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(IncidenciasSolicitudesBean.class);
    
    private final IncidenciaDAO incidenciaDAO = new IncidenciaDAO();
    private final TipoSolicitudDAO tipoSolicitudDAO = new TipoSolicitudDAO();
    
    private DetEmpleado empleadoSesion;
    private DetEmpleado empleadoSelected;
    private DetIncidencia incidenciaSelected;
    private List<DetIncidencia> listaPermisos;
    private List<DetIncidencia> listaPermisosFiltrada;
    private List<CatTipoSolicitud> lstTipoSol;
    private List<Date> lstRangoRegistro;
    private List<Date> diasDeAsueto;
    private List<Integer> invalidDays;
    private List<Date> diasDeVacaciones;
    private ManageStatus status = new ManageStatus();
    
    private boolean incidenciaVacaciones = false;
    private boolean incidenciaPermiso = false;
    private boolean estatusAceptado = false;
    private boolean estatusRechazado = false;
    private boolean estatusCancelado = false;
    private boolean estatusEnviado = false;
    
    private Date fechaInicio;
    private Date fechaFin;
    private Date fechaSeleccionada;
    private String filtroEmpleado = "";
    
    private Integer diasVacacionesSolicitados;
    
    private static final String PERMISO = "P";
    private static final String VACACIONES = "V";
    private String goceSueldo;
    private String descripcionRechazo;
    
    @PostConstruct
    public void init() {
        this.empleadoSesion = (DetEmpleado) getValorEnSesion("empleado");
        log.info("El empleado {} entrando a la sección de vacaciones / permisos", this.empleadoSesion.getNombre());
        this.lstTipoSol = this.tipoSolicitudDAO.buscarActivos();
        this.diasDeAsueto = DiasDeDescansoObligatorioBL.diasDeAsueto();
        this.goceSueldo = "100.0";
    }
    
    public void cargar(Date inicio, Date fin) {
        this.fechaInicio = inicio;
        this.fechaFin = fin;
        this.consultarPermisos();
    }
    
    public Object getValorEnSesion(String nombre) {
        return FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get(nombre)
                ;
    }
    
    public void consultarPermisos(){
        DateUtil.setTime(this.fechaInicio, 0, 0, 0);
        DateUtil.setTime(this.fechaFin, 11, 59, 59);
        this.listaPermisos = this.incidenciaDAO.buscarPermisos(this.fechaInicio, this.fechaFin);
    }
    
    public List<DetIncidencia> consultarTipoPermisos() {
        List<DetIncidencia> resultado = IncidenciaBL.filtrarPorTipoIncidencia(listaPermisos, PERMISO, VACACIONES, incidenciaPermiso, incidenciaVacaciones);
        resultado = IncidenciaBL.filtrarPorEstatus(resultado, estatusEnviado, estatusAceptado, estatusRechazado, estatusCancelado);
        resultado = IncidenciaBL.filtrarPorEmpleado(resultado, this.filtroEmpleado);

        return resultado;
    }

    public void visualizaDialog(DetIncidencia solicitudIncidencia) {
        try {
            this.incidenciaSelected = solicitudIncidencia;
            this.empleadoSelected = solicitudIncidencia.getEmpleado();
            List<Date> fechas;
            this.descripcionRechazo = "";
            switch (this.incidenciaSelected.getTipoIncidencia().getClave()) {
                case IncidenciaBL.TP_PERMISO: // Tipo Permisos
                    EmpleadoBL.empleadoTieneDiasLaborales(incidenciaSelected.getEmpleado());
                    switch (incidenciaSelected.getSolPermiso().getTipoSolicitud().getIdTipoSolicitud()) {
                        case 1:// PERMISO
                            fechaSeleccionada = incidenciaSelected.getSolPermiso().getFechaInicio();
                            break;
                    }
                    this.invalidDays = IncidenciaBL.obtenerDiasSeleccionados(empleadoSelected.getDatoEmpresa());
                    fechas = IncidenciaBL.fechasSolicitudPermiso(incidenciaSelected.getSolPermiso());
                    this.diasDeVacaciones = DateUtil.diasVacacionesSolicitados(fechas, DiasDeDescansoObligatorioBL.diasDeAsueto(), empleadoSelected.getDatoEmpresa());
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
                            .collect(Collectors.toList())
                            ;

                    log.info("Dias Solicitados: {}", fechas.toString());
                    this.diasVacacionesSolicitados = fechas.size();
                    lstRangoRegistro = fechas;

                    log.info("Total Dias de Vacaciones Solicitados: {}", this.diasVacacionesSolicitados);
                    PrimeFaces.current().executeScript("PF('dialogPermisos').show();");
                    break;
                default:
                    log.warn("EX-0023: Error al seleccionar opción");
            }
        } catch (SGPException e) {
            log.warn("Error al abrir el registro de incidencia del empleado: {}", empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn("EX-0032: {}, ", e.getMessage());
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Incidencia", "Consulte al administrador de sistemas");
            PrimeFaces.current().ajax().update("formIncidencias:messages");
        } catch (Exception ex) {
            log.warn("Error al abrir el registro de incidencia del empleado: {}", empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Incidencia", "Consulte al administrador de sistemas");
            PrimeFaces.current().ajax().update("formIncidencias:messages");
        }
    }

    public void aprobarIncidencia() {
        try {
            IncidenciaBL.aprobarIncidencia(this.incidenciaSelected, this.empleadoSesion, this.goceSueldo);
            log.info("Dias solicitados del empleado {} son: {}", this.empleadoSelected.getIdEmpleado(), this.diasVacacionesSolicitados);
            
            this.consultarPermisos();
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Incidencia", "Solicitud aprobada correctamente");
        } catch (SGPException ex) {
            log.warn("Error al guardar el status del registro de la incidencia del empleado: {}", empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn(ex.getMessage());
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Incidencia", "Error al actualizar la solicitud");
        } finally {
            PrimeFaces.current().ajax().update("formIncidencias:messages", "formIncidencias:tabViewI:dtIncidencias");
            PrimeFaces.current().executeScript("PF('dialogPermisos').hide()");
        }
    }

    public void rechazarIncidencia() {
        try {
            IncidenciaBL.rechazarIncidencia(this.incidenciaSelected, this.empleadoSesion, this.descripcionRechazo);
            
            this.consultarPermisos();
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Incidencia", "Incidencia rechazada");
        } catch (SGPException sgpEx) {
            log.error("Error: no se pudo eliminar la incidencia. " + sgpEx);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Incidencia", "Error al rechazar la incidencia");
        } finally {
            PrimeFaces.current().ajax().update("formIncidencias:messages", "formIncidencias:tabViewI:dtIncidencias");
        }
    }

    public void cancelarIncidencia() 
    {
        try {
            String tipoIncidencia = incidenciaSelected.getSolPermiso().getTipoSolicitud().getClave();
            
            switch(tipoIncidencia){
                case VACACIONES:
                    IncidenciaBL.cancelarIncidencia(this.incidenciaSelected, this.empleadoSesion);
                    IncidenciaBL.cancelarVacaciones(this.incidenciaSelected);
                    break;
                case PERMISO:
                    IncidenciaBL.cancelarIncidencia(this.incidenciaSelected, this.empleadoSesion);
                    break;
                default:
                    throw new SGPException("No hay solicitudes de vacaciones y/o permiso, contacte a su administrador de sistemas");
            }
            
            this.consultarPermisos();
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Incidencia", "Solicitud cancelada correctamente");
        } catch (SGPException ex) {
            log.warn("Error al guardar el status del registro de la incidencia del empleado: {}", empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.warn("EX-0028: ", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Incidencia", ex.getMessage());
        } catch (RuntimeException ex) {
            log.error("Error al guardar el status del registro de la incidencia del empleado: {}", empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.error("EX-0028: ", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Incidencia", ex.getMessage());
        } catch (Exception ex) {
            log.error("Error al guardar el status del registro de la incidencia del empleado: {}", empleadoSelected.getNumEmpleado() != null ? empleadoSelected.getNumEmpleado() : null);
            log.error("EX-0028: ", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Incidencia", "Error desconocido, contacte con el administrador se sistemas");
        } finally {
            PrimeFaces.current().ajax().update("formIncidencias:messages", "formIncidencias:tabViewI:dtIncidencias");
            PrimeFaces.current().executeScript("PF('dialogoCancelar').hide()");
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

    public List<DetIncidencia> getListaPermisosFiltrada() {
        return listaPermisosFiltrada;
    }

    public void setListaPermisosFiltrada(List<DetIncidencia> listaPermisosFiltrada) {
        this.listaPermisosFiltrada = listaPermisosFiltrada;
    }

    public List<CatTipoSolicitud> getLstTipoSol() {
        return lstTipoSol;
    }

    public void setLstTipoSol(List<CatTipoSolicitud> lstTipoSol) {
        this.lstTipoSol = lstTipoSol;
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

    public Integer getDiasVacacionesSolicitados() {
        return diasVacacionesSolicitados;
    }

    public void setDiasVacacionesSolicitados(Integer diasVacacionesSolicitados) {
        this.diasVacacionesSolicitados = diasVacacionesSolicitados;
    }

    public List<Date> getDiasDeAsueto() {
        return diasDeAsueto;
    }

    public void setDiasDeAsueto(List<Date> diasDeAsueto) {
        this.diasDeAsueto = diasDeAsueto;
    }

    public List<Date> getDiasDeVacaciones() {
        return diasDeVacaciones;
    }

    public void setDiasDeVacaciones(List<Date> diasDeVacaciones) {
        this.diasDeVacaciones = diasDeVacaciones;
    }
    
    public Date getFechaSeleccionada() {
        return fechaSeleccionada;
    }

    public void setFechaSeleccionada(Date fechaSeleccionada) {
        this.fechaSeleccionada = fechaSeleccionada;
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
    
    public String getGoceSueldo() {
        return goceSueldo;
    }

    public void setGoceSueldo(String goceSueldo) {
        this.goceSueldo = goceSueldo;
    }

    public String getDescripcionRechazo() {
        return descripcionRechazo;
    }

    public void setDescripcionRechazo(String descripcionRechazo) {
        this.descripcionRechazo = descripcionRechazo;
    }
    
    public ManageStatus getStatus() {
        return status;
    }

    public void setStatus(ManageStatus status) {
        this.status = status;
    }
    
    public String getFiltroEmpleado() {
        return filtroEmpleado;
    }

    public void setFiltroEmpleado(String filtroEmpleado) {
        this.filtroEmpleado = filtroEmpleado;
    }
    
}
