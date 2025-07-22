package mx.com.ferbo.business.registro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.dianolaboral.DiasDeDescansoObligatorioBL;
import mx.com.ferbo.business.empleado.EmpleadoBL;
import mx.com.ferbo.business.incidencia.SolicitudPermisoBL;
import mx.com.ferbo.dao.n.RegistroDAO;
import mx.com.ferbo.dao.n.RegistroVacacionesDAO;
import mx.com.ferbo.model.CatEstatusRegistro;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetEmpleadoConfiguracion;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.model.DetRegistroVacaciones;
import mx.com.ferbo.model.InfDatoEmpresa;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

/**
 *
 * @author alberto
 */
public class RegistroBL implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(RegistroBL.class);
    
    private static final String VACACIONES = "V";
    private static final String PERMISO = "P";
    
    /*Valida las fechas de solicitud permiso en registro de asistencia de un empleado*/
    public boolean validarFechasVacaciones(DetEmpleado empleado, Date fechaInicial, Date fechaFinal) 
    {
        boolean existenVacaciones = false;
        InfDatoEmpresa empleadoEmpresa = empleado.getDatoEmpresa();
        Integer horaEntrada = DateUtil.getHora(empleadoEmpresa.getHoraEntrada());
        RegistroDAO registroDAO = new RegistroDAO();
        List<DetRegistro> registro = null;

        Date fechaInicialEntrada = DateUtil.getDate(DateUtil.getAnio(fechaInicial), DateUtil.getMes(fechaInicial), DateUtil.getDia(fechaInicial), horaEntrada, 0, 0);
        Date fechaFinEntrada = DateUtil.getDate(DateUtil.getAnio(fechaFinal), DateUtil.getMes(fechaFinal), DateUtil.getDia(fechaFinal), horaEntrada, 0, 0);
        registro = registroDAO.buscar(empleado.getIdEmpleado(), fechaInicialEntrada, fechaFinEntrada);

        if(!registro.isEmpty()) 
        {
            existenVacaciones = true;
        }

        return existenVacaciones;
    }
    
    //Busca los registro de incapacidades uno por uno en la seccion de registro asistencia
    public static List<DetRegistro> obtenerRegistroAsistencia(DetEmpleado empleado, Date fechaInicio, Date fechaFin, String clave) throws SGPException
    {
        DetRegistro registro = null;
        RegistroDAO registroDAO = new RegistroDAO();
        InfDatoEmpresa empleadoEmpresa = empleado.getDatoEmpresa();
        Integer horaEntrada = DateUtil.getHora(empleadoEmpresa.getHoraEntrada());
        Integer horaSalida = DateUtil.getHora(empleadoEmpresa.getHorasalida());
        log.info("Buscando registros del empleado {} en asistencia", empleado.getIdEmpleado());
        
        DateUtil.setTime(fechaInicio, horaEntrada, 0, 0, 0);
        DateUtil.setTime(fechaFin, horaSalida, 0, 0, 0);
        
        List<Date> listaFechasAsistencia = DateUtil.generarArreglosFechas(fechaInicio, fechaFin);
        listaFechasAsistencia = DateUtil.diasVacacionesSolicitados(listaFechasAsistencia, DiasDeDescansoObligatorioBL.diasDeAsueto(), empleado.getDatoEmpresa());
        int totalFechasIncapacidades = listaFechasAsistencia.size();
        
        List<DetRegistro> listaRegistroAsistencia = registroDAO.buscarPorEmpPeriodoSolicitud(empleado.getIdEmpleado(), clave, fechaInicio, fechaFin);
        int totalRegistros = listaRegistroAsistencia.size();
        horaEntrada = 12;
        
        log.trace("Tamaño rango fechas {}, Tamaño rango registros {}", totalFechasIncapacidades, totalRegistros);
        
        if(totalRegistros > 0)
        {
            listaRegistroAsistencia.clear();
            for(Date aux : listaFechasAsistencia)
            {
                Date auxFecha = DateUtil.getDate(DateUtil.getAnio(aux), DateUtil.getMes(aux), DateUtil.getDia(aux), horaEntrada, 0, 0);
                registro = registroDAO.buscarPorEstatus(empleado.getIdEmpleado(), auxFecha, clave);
                if(registro != null)
                {
                    listaRegistroAsistencia.add(registro);
                }
            }
            
            log.info("El empleado {} tiene {} registros en asistencia", empleado.getIdEmpleado(), totalRegistros);
            
            if(listaRegistroAsistencia.isEmpty())
            {
                throw new SGPException("No se encontraron registros en asistencia");
            }
        }else
        {
            log.info("El empleado {} tiene {} registros en asistencia", empleado.getIdEmpleado(), totalRegistros);
        }
        
        return listaRegistroAsistencia;
    }
    
    //Quita los registros en asistencia de unas vacaciones/permiso/incapacidad
    public static int cancelarRegistroAsistencia(List<DetRegistro> listaRegistros) throws SGPException
    {
        int totalRegistros = 0;
        RegistroDAO registroDAO = new RegistroDAO();
        try
        {
            for(DetRegistro auxRegistro : listaRegistros)
            {
                registroDAO.eliminar(auxRegistro);
                totalRegistros = totalRegistros + 1;
            }
        }catch(SGPException ex)
        {
            log.info("Error al borrar un registo de asistencia: {}", ex);
            throw new SGPException("Error al cancelar la incapacidad");
        }
        return totalRegistros;
    }
    
    /*Actualiza el registro de asistencia de un empleado*/
    public static void actualizarRegistroAsistencia(DetRegistro registro) throws SGPException
    {
        try
        {
            RegistroDAO registroDAO = new RegistroDAO();
            registroDAO.actualizar(registro);
        }catch(SGPException ex)
        {
            log.info("Error al actualizar un registo de asistencia: {}", ex);
            throw new SGPException("Error al actualizar el registro del empleado");
        }
    }
    
    public static DetRegistro validarConfiguracion(DetEmpleado empleado, Date horaLimiteEntrada, Date fechaActual) throws SGPException 
    {
        DetEmpleadoConfiguracion empleadoConf = empleado.getEmpleadoConfiguracion();
        DetRegistro registro = new DetRegistro();
        CatEstatusRegistro statusEnTiempo = EstatusRegistroBL.estatusATiempo();
        CatEstatusRegistro statusRetardo = EstatusRegistroBL.estatusRetardo();
        Date horaSistema = new Date();

        registro.setFechaEntrada(fechaActual);
        registro.setFechaSalida(null);
        registro.setIdEmpleado(empleado);
        log.info("Hora actual del sistema: {}", horaSistema);
        if (empleadoConf == null || empleadoConf.getRetardo() == null) {
            log.info("Registro sin la configuracion de retardo");
            registro.setStatus((horaSistema.after(horaLimiteEntrada)) ? statusRetardo : statusEnTiempo);
        } else {
            log.info("Registro con la configuracion de retardo: {}", empleadoConf.toString());
            if (empleadoConf.getRetardo() == true) {
                registro.setStatus((horaSistema.after(horaLimiteEntrada)) ? statusRetardo : statusEnTiempo);
            } else {
                registro.setStatus(statusEnTiempo);
            }
        }
        return registro;
    }
    
    /*Guarda los registros del tipo solicitud permiso en registro asistencia*/
    public static void guardarRegistroVacaciones(DetEmpleado empleado, DetIncidencia incidencia, List<Date> diasAsueto) throws SGPException 
    {
        EmpleadoBL.empleadoTieneDiasLaborales(empleado);
        
        int cantidadRegistrosGuardados = 0;
        
        InfDatoEmpresa empleadoEmpresa = empleado.getDatoEmpresa();
        CatEstatusRegistro statusVacaciones = null;
        
        switch(incidencia.getSolPermiso().getTipoSolicitud().getClave())
        {
            case VACACIONES:
                statusVacaciones = EstatusRegistroBL.estatusVacaciones();
                break;
            case PERMISO:
                statusVacaciones = EstatusRegistroBL.estatusPermiso();
                break;
            default:
            	throw new SGPException(String.format("Status no permitido: %s", incidencia.getSolPermiso().getTipoSolicitud().getClave()));
        }

        Integer horaEntrada = DateUtil.getHora(empleadoEmpresa.getHoraEntrada());
        Integer horaSalida = DateUtil.getHora(empleadoEmpresa.getHorasalida());
        
        List<Date> listaFechas = DateUtil.generarArreglosFechas(incidencia.getSolPermiso().getFechaInicio(), incidencia.getSolPermiso().getFechaFin());
        log.trace("Lista de Fechas: {}", listaFechas);
        
        listaFechas = DateUtil.diasVacacionesSolicitados(listaFechas, diasAsueto, empleado.getDatoEmpresa());
        
        for(Date dia : listaFechas) {
        	
            DetRegistro registro = new DetRegistro();
            registro.setIdEmpleado(empleado);
            registro.setStatus(statusVacaciones);
            
            Date registroEntrada = DateUtil.getDateTime(DateUtil.getAnio(dia), DateUtil.getMes(dia), DateUtil.getDia(dia), horaEntrada, 0, 0, 0);
            log.trace("Dia hora entrada: {}", registroEntrada);
            registro.setFechaEntrada(registroEntrada);

            Date registroSalida = DateUtil.getDateTime(DateUtil.getAnio(dia), DateUtil.getMes(dia), DateUtil.getDia(dia), horaSalida, 0, 0, 0);
            log.trace("Dia hora salida: {}", registroSalida);
            registro.setFechaSalida(registroSalida);
            
            if(SolicitudPermisoBL.TP_VACACIONES.equalsIgnoreCase(incidencia.getSolPermiso().getTipoSolicitud().getClave())) {
            	DetRegistroVacaciones registroVacaciones = new DetRegistroVacaciones();
            	registroVacaciones.setRegistro(registro);
            	registroVacaciones.setVacaciones(incidencia.getSolPermiso().getVacaciones());
            	RegistroVacacionesDAO registroVacacionesDAO = new RegistroVacacionesDAO();
            	registroVacacionesDAO.actualizar(registroVacaciones);
            } else {
            	RegistroDAO registroDAO = new RegistroDAO();
            	registroDAO.guardar(registro);
            }

            cantidadRegistrosGuardados += 1;
        }
        
        log.info("Num. registros de incapacidad guardados del empleado {} en asistencia: {}", empleado.getIdEmpleado(), cantidadRegistrosGuardados);
    }
    
    /*Guarda los registros de incapacidad en registro de asistencia*/
    public static void guardarRegistroIncapacidad(DetEmpleado empleadoInc, Date fechaInicio, Date fechaFin, List<Date> diasAsueto) throws SGPException
    {
        EmpleadoBL.empleadoTieneDiasLaborales(empleadoInc);
        
        int cantidadRegistrosGuardados = 0;
        RegistroDAO registroDAO = new RegistroDAO();
        InfDatoEmpresa empleadoEmpresa = empleadoInc.getDatoEmpresa();
        
        Integer horaEntrada = DateUtil.getHora(empleadoEmpresa.getHoraEntrada());
        Integer horaSalida = DateUtil.getHora(empleadoEmpresa.getHorasalida());
        try
        {
            List<Date> listaFechas = DateUtil.generarArreglosFechas(fechaInicio, fechaFin);
            log.trace("Lista de Fechas: {}", listaFechas);
            listaFechas = DateUtil.diasVacacionesSolicitados(listaFechas, diasAsueto, empleadoInc.getDatoEmpresa());

            for(Date dia : listaFechas)
            {
                DetRegistro registro = new DetRegistro();
                registro.setIdEmpleado(empleadoInc);
                registro.setStatus(EstatusRegistroBL.estatusIncapacidad());

                Date registroEntrada = DateUtil.getDateTime(DateUtil.getAnio(dia), DateUtil.getMes(dia), DateUtil.getDia(dia), horaEntrada, 0, 0, 0);
                registro.setFechaEntrada(registroEntrada);
                log.trace("Hora entrada: {}", registroEntrada);
                
                Date registroSalida = DateUtil.getDateTime(DateUtil.getAnio(dia), DateUtil.getMes(dia), DateUtil.getDia(dia), horaSalida, 0, 0, 0);
                registro.setFechaSalida(registroSalida);
                log.trace("Hora salida: {}", registroSalida);
                
                registroDAO.guardar(registro);
                cantidadRegistrosGuardados += 1;
            }
            
            log.info("Num. registros de incapacidad guardados del empleado {} en asistencia: {}", empleadoInc.getIdEmpleado(), cantidadRegistrosGuardados);
        }catch(SGPException ex)
        {
            log.info("Error al registrar la incapacidad: {}", ex);
            throw new SGPException("Error al registrar la incapacidad");
        }catch (Exception e) 
        {
            log.info("Error: ", e);
        }
    }
    
    public static void actualizarRegistroIncapacidad(DetEmpleado empleadoInc, List<Date> listaFechas, List<Date> diasAsueto) throws SGPException
    {
        EmpleadoBL.empleadoTieneDiasLaborales(empleadoInc);
        
        RegistroDAO registroDAO = new RegistroDAO();
        InfDatoEmpresa empleadoEmpresa = empleadoInc.getDatoEmpresa();
        Integer horaEntrada = DateUtil.getHora(empleadoEmpresa.getHoraEntrada());
        Integer horaSalida = DateUtil.getHora(empleadoEmpresa.getHorasalida());
        try
        {
            log.trace("Lista de Fechas: {}", listaFechas);
            listaFechas = DateUtil.diasVacacionesSolicitados(listaFechas, diasAsueto, empleadoInc.getDatoEmpresa());
            for(Date dia : listaFechas)
            {
                DetRegistro registro = new DetRegistro();
                registro.setIdEmpleado(empleadoInc);
                registro.setStatus(EstatusRegistroBL.estatusIncapacidad());

                Date registroEntrada = DateUtil.getDateTime(DateUtil.getAnio(dia), DateUtil.getMes(dia), DateUtil.getDia(dia), horaEntrada, 0, 0, 0);
                registro.setFechaEntrada(registroEntrada);
                log.trace("Dia hora entrada: {}", registroEntrada);

                Date registroSalida = DateUtil.getDateTime(DateUtil.getAnio(dia), DateUtil.getMes(dia), DateUtil.getDia(dia), horaSalida, 0, 0, 0);
                registro.setFechaSalida(registroSalida);
                log.trace("Dia hora salida: {}", registroSalida);

                registroDAO.guardar(registro);
            }
        }catch(SGPException ex)
        {
            log.info("Error al actualizar la incapacidad: {}", ex);
            throw new SGPException("Error al actualizar la incapacidad");
        }catch (Exception e) 
        {
            log.info("Error: ", e);
        }
    }
    
}
