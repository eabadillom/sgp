package mx.com.ferbo.business.empleado;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import mx.com.ferbo.dao.n.EmpleadoFotoDAO;
import mx.com.ferbo.dao.n.EstatusRegistroDAO;
import mx.com.ferbo.dao.n.RegistroDAO;
import mx.com.ferbo.dao.n.TokenDAO;
import mx.com.ferbo.model.CatEstatusRegistro;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetEmpleadoConfiguracion;
import mx.com.ferbo.model.DetEmpleadoFoto;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.model.DetToken;
import mx.com.ferbo.model.InfDatoEmpresa;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
public class RegistroAsistenciaBL 
{
    private static final Logger log = LogManager.getLogger(RegistroAsistenciaBL.class);
    
    private EmpleadoFotoDAO empleadoFotoDAO = null;
    private EstatusRegistroDAO estatusDAO = null;
    private TokenDAO detTokenDAO = null;
    private RegistroDAO registroDAO = null;
    
    public RegistroAsistenciaBL() 
    {
        this.registroDAO = new RegistroDAO(DetRegistro.class);
        this.estatusDAO = new EstatusRegistroDAO(CatEstatusRegistro.class);
        this.detTokenDAO = new TokenDAO(DetToken.class);
        this.empleadoFotoDAO = new EmpleadoFotoDAO(DetEmpleadoFoto.class);
    }
    
    public CatEstatusRegistro buscarRegistroPorId(Integer id)
    {
        CatEstatusRegistro estatusRegistro = this.estatusDAO.buscarPorId(id);
        return estatusRegistro;
    }
    
    public DetToken buscarEmpleadoPorToken(String token)
    {
        DetToken tokenEmpleado = this.detTokenDAO.buscarPorToken(token);
        return tokenEmpleado;
    }
    
    public void actualizarToken(DetToken tokenEmpleado) throws SGPException
    {
        try
        {
            this.detTokenDAO.actualizar(tokenEmpleado);
        }catch(SGPException ex)
        {
            log.error("Error al actualizar el token del empleado");
            throw new SGPException("Error al actualizar el token");
        }
    }
    
    public DetEmpleadoFoto buscarFotoEmpleado(Integer numeroEmpleado)
    {
        DetEmpleadoFoto fotoEmpleado = this.empleadoFotoDAO.buscarPorId(numeroEmpleado);
        return fotoEmpleado;
    }
    
    public DetRegistro buscarPorEmpleadoFechaEntrada(Integer idEmpleado, Date fechaEntrada, Date fechaSalida)
    {
        DetRegistro empleadoBuscado = this.registroDAO.buscarPorEmpleadoFechaEntrada(idEmpleado, fechaEntrada, fechaSalida);
        return empleadoBuscado;
    }
    
    public DetToken obtenerToken(String numeroEmpleado, String token, Date fechaActual) throws Exception
    {
        DetToken tokenEmpleado = null;

        log.info("Buscando biometricos para el empleado numero: " + numeroEmpleado);

        if (numeroEmpleado == null) {
                throw new Exception("Información incorrecta.");
        }

        if ("".equalsIgnoreCase(numeroEmpleado.trim()))
                throw new Exception("Información incorrecta.");

        log.info("Buscando token........: " + token);
        tokenEmpleado = this.buscarEmpleadoPorToken(token);
        log.info("Token: {}, Caducidad: {}", tokenEmpleado.getNbToken(), tokenEmpleado.getCaducidad());

        if(tokenEmpleado.getEmpleado().getNumEmpleado().equals(numeroEmpleado) == false)
                throw new SGPException("La información proporcionada es incorrecta.");

        if (tokenEmpleado.getCaducidad().before(fechaActual)) {
                log.info("La fecha recuperada es valida: {}", tokenEmpleado.getCaducidad());
                throw new SGPException("El token ha expirado.");
        }

        if(tokenEmpleado.isValido() == false) {
                log.info("El token no es válido: {}", tokenEmpleado.isValido());
                throw new SGPException("El token no es válido.");
        }

        return tokenEmpleado;
    }
    
    public void registroAsistenciaEmpleado(DetEmpleado empleado, Date fechaActual) throws SGPException
    {
        InfDatoEmpresa empleadoEmpresa = empleado.getDatoEmpresa();
        log.trace("Info dato empresa: {}", empleadoEmpresa.toString());
        
        Date horaLimiteEntrada = new Date();
        Date fechaEntradaInicio = new Date();
        Date fechaEntradaFin = new Date();
        
        Integer horaEntrada = DateUtil.getHora(empleadoEmpresa.getHoraEntrada());
        Integer minEntrada = DateUtil.getMinuto(empleadoEmpresa.getHoraEntrada());
        Integer minutosTolerancia = empleadoEmpresa.getMinutosTolerancia();
        log.trace("Hora de entrada: {}:{}, con {} minutos de tolerancia", horaEntrada, minEntrada, minutosTolerancia);
        
        DateUtil.setTime(fechaEntradaInicio, 0, 0, 0, 0);
        DateUtil.setTime(fechaEntradaFin, 23, 59, 59, 999);

        log.info("Buscando entrada del empleado {} entre las {} y las {}", empleado.getNumEmpleado(),
                    DateUtil.getString(fechaEntradaInicio, DateUtil.FORMATO_YYYY_MM_DD_HH_MM_SS),
                    DateUtil.getString(fechaEntradaFin, DateUtil.FORMATO_YYYY_MM_DD_HH_MM_SS));
        
        boolean tipoRegistro;
        DetRegistro registro = this.buscarPorEmpleadoFechaEntrada(empleado.getIdEmpleado(), fechaEntradaInicio, fechaEntradaFin);
        if(registro == null || registro.getIdRegistro() == null)
            tipoRegistro = true;
        else
            tipoRegistro = false;
        
        DateUtil.setTime(horaLimiteEntrada, horaEntrada, (minEntrada+minutosTolerancia), 0, 0);
        log.trace("Hora limite de entrada: {}", horaLimiteEntrada);            

        if(tipoRegistro == true)
        {
            log.info("Registrando entrada...");
            registro = validarConfiguracion(empleado, horaLimiteEntrada, fechaActual);
            this.registroDAO.guardar(registro);
            log.info("Entrada registrada correctamente");
        }
        
        if(tipoRegistro == false)
        {       
            log.info("Registrando salida...");
            registro.setFechaSalida(fechaActual);
            this.registroDAO.actualizar(registro);
            log.info("Salida registrada correctamente");
        }
        
    }
    
    public DetRegistro validarConfiguracion(DetEmpleado empleado, Date horaLimiteEntrada, Date fechaActual) throws SGPException
    {
        DetEmpleadoConfiguracion empleadoConf = empleado.getEmpleadoConfiguracion();
        DetRegistro registro = new DetRegistro();
        CatEstatusRegistro statusEnTiempo = this.buscarRegistroPorId(1);
        CatEstatusRegistro statusRetardo = this.buscarRegistroPorId(2);
        Date horaSistema = new Date();
        
        registro.setFechaEntrada(fechaActual);
        registro.setFechaSalida(null);
        registro.setIdEmpleado(empleado);
        log.info("Hora actual del sistema: {}", horaSistema);
        if(empleadoConf == null || empleadoConf.getRetardo() == null)
        {
            log.info("Registro sin la configuracion de retardo");
            registro.setIdEstatus((horaSistema.after(horaLimiteEntrada)) ? statusRetardo : statusEnTiempo);
        }else
        {
            log.info("Registro con la configuracion de retardo: {}", empleadoConf.toString());
            if(empleadoConf.getRetardo() == true)
            {
                registro.setIdEstatus((horaSistema.after(horaLimiteEntrada)) ? statusRetardo : statusEnTiempo);
            }else
            {
                registro.setIdEstatus(statusEnTiempo);
            }
        }
        return registro;
    }
    
    public void guardarRegistroVacaciones(DetEmpleado empleado, DetIncidencia incidencia, List<Date> diasAsueto) throws SGPException
    {
        InfDatoEmpresa empleadoEmpresa = empleado.getDatoEmpresa();
        CatEstatusRegistro statusVacaciones = estatusDAO.buscarPorId(5);
        
        Integer horaEntrada = DateUtil.getHora(empleadoEmpresa.getHoraEntrada());
        Integer horaSalida = horaEntrada + 9;
        
        List<Date> listaFechas = DateUtil.generarArreglosFechas(incidencia.getIdSolPermiso().getFechaInicio(), incidencia.getIdSolPermiso().getFechaFin());
        log.trace("Lista de Fechas: {}", listaFechas);
        
        listaFechas = diasVacacionesSolicitados(listaFechas, diasAsueto, empleado.getDatoEmpresa());
        
        for(Date dia : listaFechas)
        {
            DetRegistro registro = new DetRegistro();
            registro.setIdEmpleado(empleado);
            registro.setIdEstatus(statusVacaciones);
            
            Date registroEntrada = DateUtil.getDate(DateUtil.getAnio(dia), DateUtil.getMes(dia), DateUtil.getDia(dia), horaEntrada, 0, 0);
            log.trace("Dia hora entrada: {}", registroEntrada);
            registro.setFechaEntrada(registroEntrada);

            Date registroSalida = DateUtil.getDate(DateUtil.getAnio(dia), DateUtil.getMes(dia), DateUtil.getDia(dia), horaSalida, 0, 0);
            log.trace("Dia hora salida: {}", registroSalida);
            registro.setFechaSalida(registroSalida);
            
            registroDAO.guardar(registro);
        }
    }
    
    public List<Date> diasVacacionesSolicitados(List<Date> fechas, List<Date> diasAsueto, InfDatoEmpresa empleadoEmpresa)
    {
        log.trace("Dias antes de festividades: {}", fechas.size());
        List<Date> diasDeDescanso = DateUtil.diasLaborales(fechas, diasAsueto);
        log.trace("Dias despues de festividades: {}", diasDeDescanso.size());
        
        Map<DayOfWeek, Boolean> diasEmpleado = new HashMap<>();
        diasEmpleado.put(DayOfWeek.MONDAY, empleadoEmpresa.getDiaLunes());
        diasEmpleado.put(DayOfWeek.TUESDAY, empleadoEmpresa.getDiaMartes());
        diasEmpleado.put(DayOfWeek.WEDNESDAY, empleadoEmpresa.getDiaMiercoles());
        diasEmpleado.put(DayOfWeek.THURSDAY, empleadoEmpresa.getDiaJueves());
        diasEmpleado.put(DayOfWeek.FRIDAY, empleadoEmpresa.getDiaViernes());
        diasEmpleado.put(DayOfWeek.SATURDAY, empleadoEmpresa.getDiaSabado());
        diasEmpleado.put(DayOfWeek.SUNDAY, empleadoEmpresa.getDiaDomingo());

        List<Date> diasDeVacaciones = diasDeDescanso.stream()
            .filter(dia -> diasEmpleado.getOrDefault(DateUtil.toLocalDate(dia).getDayOfWeek(), true))
            .collect(Collectors.toList());

        log.trace("Dias de descanso: {}", diasDeVacaciones.size());
        log.trace("Y son: {}", diasDeVacaciones.toString());
        return diasDeVacaciones;
    }
    
}
