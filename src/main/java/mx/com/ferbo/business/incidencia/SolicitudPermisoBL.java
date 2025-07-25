package mx.com.ferbo.business.incidencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.EstatusIncidenciaDAO;
import mx.com.ferbo.dao.n.EstatusSolicitudDAO;
import mx.com.ferbo.dao.n.IncidenciaDAO;
import mx.com.ferbo.dao.n.SolicitudPermisoDAO;
import mx.com.ferbo.dao.n.TipoSolicitudDAO;
import mx.com.ferbo.model.CatEstatusIncidencia;
import mx.com.ferbo.model.CatEstatusSolicitud;
import mx.com.ferbo.model.CatTipoSolicitud;
import mx.com.ferbo.model.DetDiaPermiso;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.model.DetSolicitudPermiso;
import mx.com.ferbo.model.InfDatoEmpresa;
import mx.com.ferbo.model.imss.DetIncapacidad;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

/**
 *
 * @author alberto
 */
public class SolicitudPermisoBL implements Serializable
{
    private static final Logger log = LogManager.getLogger(SolicitudPermisoBL.class);
    private static final long serialVersionUID = 1L;
    
    public static final String TP_VACACIONES = "V";
    public static final String TP_PERMISO = "P";
    public static final String TP_INCAPACIDAD_CORTA = "IC";
    public static final String TP_INCAPACIDAD_LARGA = "IL";
    
    public static final String STATUS_ENVIADA = "E";
    public static final String STATUS_APROBADA = "A";
    public static final String STATUS_RECHAZADA = "R";
    public static final String STATUS_CANCELADA = "C";
    
    public static List<Integer> obtenerDiasSeleccionados(InfDatoEmpresa empleadoEmpresa) 
    {
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
    
    private static DetSolicitudPermiso create(DetEmpleado empleado) {
    	DetSolicitudPermiso solicitud = new DetSolicitudPermiso();
    	solicitud.setEmpleadoSol(empleado);
    	solicitud.setEstatus(EstatusSolicitudBL.estatusEnviado());
    	solicitud.setFechaCap(new Date());
    	return solicitud;
    }
    
    public static DetSolicitudPermiso createSolicitudPermiso(DetEmpleado empleado)
    throws SGPException {
    	DetSolicitudPermiso solicitud = create(empleado);
    	
    	TipoSolicitudDAO tipoSolicitudDAO = new TipoSolicitudDAO();
    	CatTipoSolicitud tipoPermiso;
    	tipoPermiso = tipoSolicitudDAO.buscarPorClave(TP_PERMISO).orElseThrow(() -> new SGPException("Tipo de solicitud no soportada."));
    	solicitud.setTipoSolicitud(tipoPermiso);
    	
    	return solicitud;
    }
    
    public static DetSolicitudPermiso createSolicitudVacaciones(DetEmpleado empleado)
    throws SGPException {
    	DetSolicitudPermiso solicitud = create(empleado);
    	
    	TipoSolicitudDAO tipoSolicitudDAO = new TipoSolicitudDAO();
    	CatTipoSolicitud tipoVacaciones;
    	tipoVacaciones = tipoSolicitudDAO.buscarPorClave(TP_VACACIONES).orElseThrow(() -> new SGPException("Tipo de solicitud no soportada."));;
    	
    	solicitud.setTipoSolicitud(tipoVacaciones);
    	solicitud.setDiasPermiso(new ArrayList<DetDiaPermiso>());
    	
    	return solicitud;
    }
    
    
    
    //Valida que una solicitud de permiso que no se empalme con otro registro durante un periodo solicitado
    public static void validarSolicitudPermiso(DetSolicitudPermiso solicitudPermiso) throws SGPException {
        
        DetEmpleado empleado = solicitudPermiso.getEmpleadoSol();
        Date fechaInicio = solicitudPermiso.getFechaInicio();
        Date fechaFin = solicitudPermiso.getFechaFin();
        
        SolicitudPermisoDAO solicitudPermisoDAO = new SolicitudPermisoDAO();
        List<DetSolicitudPermiso> solicitudes = solicitudPermisoDAO.buscarPorIdEmpleadoFechasClave(empleado.getIdEmpleado(), fechaInicio, fechaFin, TP_PERMISO, TP_VACACIONES, STATUS_ENVIADA, STATUS_APROBADA);
        log.trace("Solicitudes: {}", solicitudes.toString());

        if (solicitudes.isEmpty())
        	return;
        
    	DetSolicitudPermiso solicitudPrevia = solicitudes.get(0);
    	String mensajeError = String.format("El permiso solicitado conincide con otro registro previo: %s (inicio: %s - fin: %s)",
        		solicitudPrevia.getTipoSolicitud().getDescripcion(),
        		DateUtil.getString(solicitudPrevia.getFechaInicio(), DateUtil.FORMATO_DD_MM_YYYY),
        		DateUtil.getString(solicitudPrevia.getFechaFin(), DateUtil.FORMATO_DD_MM_YYYY));
        throw new SGPException(mensajeError);
    }
    
    //Valida si existe un periodo de una solicitud de permiso y/o vacaciones durante un periodo de una incapacidad
    public static void validarPeriodoSolicitudPermiso(DetIncapacidad incapacidad) throws SGPException {
        log.info("Entrando a validar vacaciones y/o permisos");
        Integer idEmpleadoInc = incapacidad.getIdEmpleadoInc().getIdEmpleado();
        Date fechaInicio = incapacidad.getFechaInicio();
        Date fechaFin = incapacidad.getFechaFin();
        log.trace("Fecha Inicial: {} y Fecha Final: {} del Empleado: {}", fechaInicio, fechaFin, idEmpleadoInc);
        
        SolicitudPermisoDAO solicitudPermisoDAO = new SolicitudPermisoDAO();
        List<DetSolicitudPermiso> lstSolicitudes = solicitudPermisoDAO.buscarPorPeriodo(idEmpleadoInc, fechaInicio, fechaFin, STATUS_ENVIADA, STATUS_APROBADA);
        
        if(!lstSolicitudes.isEmpty())
        {
            log.trace("Los registros de vacacione y/o permisos son: {}", lstSolicitudes.toString());
            throw new SGPException("Ya existe un periodo de vacaciones y/o permiso");
        }
    }
    
    public static void cancelar(DetIncidencia incidencia)
    throws SGPException {
    	EstatusIncidenciaDAO estatusIncidenciaDAO = new EstatusIncidenciaDAO();
    	EstatusSolicitudDAO estatusSolicitudDAO = new EstatusSolicitudDAO();
    	IncidenciaDAO incidenciaDAO = new IncidenciaDAO();
    	
    	CatEstatusIncidencia incidenciaCancelada = estatusIncidenciaDAO.buscarPorClave("C");
    	CatEstatusSolicitud solicitudPermisoCancelada = estatusSolicitudDAO.buscarPorClave("C");
    	
    	switch(incidencia.getSolPermiso().getTipoSolicitud().getClave()) {
    		case TP_PERMISO:
    		case TP_VACACIONES:
    			incidencia.setEstatusIncidencia(incidenciaCancelada);
    			incidencia.getSolPermiso().setEstatus(solicitudPermisoCancelada);
    			break;
    		default:
    			throw new UnsupportedOperationException("El tipo de solicitud no está soportado.");
    	}
    	
    	incidencia.setFechaMod(new Date());
    	incidenciaDAO.actualizar(incidencia);
    	
    }
}
