package mx.com.ferbo.business.incidencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.com.ferbo.dao.n.SolicitudPermisoDAO;

import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetSolicitudPermiso;
import mx.com.ferbo.model.InfDatoEmpresa;
import mx.com.ferbo.model.imss.DetIncapacidad;

import mx.com.ferbo.util.SGPException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class SolicitudPermisoBL implements Serializable
{
    private static final Logger log = LogManager.getLogger(SolicitudPermisoBL.class);
    private static final long serialVersionUID = 1L;
    
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
    
    //Valida que una solicitud de permiso que no se empalme con otro registro durante un periodo solicitado
    public static void validarSolicitudPermiso(DetSolicitudPermiso solicitudPermiso) throws SGPException 
    {
        String permisos = "P";
        String vacaciones = "V";
        String enviada = "E";
        String aprobada = "A";
        
        DetEmpleado empleado = solicitudPermiso.getEmpleadoSol();
        Date fechaInicio = solicitudPermiso.getFechaInicio();
        Date fechaFin = solicitudPermiso.getFechaFin();
        
        SolicitudPermisoDAO solicitudPermisoDAO = new SolicitudPermisoDAO();
        List<DetSolicitudPermiso> solicitudes = solicitudPermisoDAO.buscarPorIdEmpleadoFechasClave(empleado.getIdEmpleado(), fechaInicio, fechaFin, permisos, vacaciones, enviada, aprobada);
        log.trace("Solicitudes: {}", solicitudes.toString());

        if (!solicitudes.isEmpty()) 
        {
            throw new SGPException("Error. El periodo que solicitaste ya se encuentra registrado");
        }
    }
    
    //Valida si existe un periodo de una solicitud de permiso y/o vacaciones durante un periodo de una incapacidad
    public static void validarPeriodoSolicitudPermiso(DetIncapacidad incapacidad) throws SGPException
    {
        String enviada = "E";
        String aprobada = "A";
        log.info("Entrando a validar vacaciones y/o permisos");
        Integer idEmpleadoInc = incapacidad.getIdEmpleadoInc().getIdEmpleado();
        Date fechaInicio = incapacidad.getFechaInicio();
        Date fechaFin = incapacidad.getFechaFin();
        log.trace("Fecha Inicial: {} y Fecha Final: {} del Empleado: {}", fechaInicio, fechaFin, idEmpleadoInc);
        
        SolicitudPermisoDAO solicitudPermisoDAO = new SolicitudPermisoDAO();
        List<DetSolicitudPermiso> lstSolicitudes = solicitudPermisoDAO.buscarPorPeriodo(idEmpleadoInc, fechaInicio, fechaFin, enviada, aprobada);
        
        if(!lstSolicitudes.isEmpty())
        {
            log.trace("Los registros de vacacione y/o permisos son: {}", lstSolicitudes.toString());
            throw new SGPException("Ya existe un periodo de vacaciones y/o permiso");
        }
    }
    
}
