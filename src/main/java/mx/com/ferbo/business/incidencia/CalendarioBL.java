package mx.com.ferbo.business.incidencia;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import mx.com.ferbo.business.dianolaboral.DiasDeDescansoObligatorioBL;
import mx.com.ferbo.dao.n.RegistroDAO;
import mx.com.ferbo.model.CatDiaNoLaboral;
import mx.com.ferbo.model.CatEstatusRegistro;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.model.InfDatoEmpresa;
import mx.com.ferbo.util.DateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author alberto
 */
public class CalendarioBL implements Serializable
{
    private static final Logger log = LogManager.getLogger(CalendarioBL.class);
    private static final long serialVersionUID = 1L;
    
    public static boolean eventoExiste(List<ScheduleEvent<?>> eventos, ScheduleEvent nuevoEvento) 
    {
        // Verificar si ya existe un evento con la misma fecha y hora
        for(ScheduleEvent evento : eventos) 
        {
            // Comparar por fecha de inicio y fin
            if(evento.getStartDate().toLocalDate().equals(nuevoEvento.getStartDate().toLocalDate()) && evento.getEndDate().toLocalDate().equals(nuevoEvento.getEndDate().toLocalDate())) 
            {
                return true;  // El evento ya existe
            }
        }
        return false;  // El evento no existe
    }
    
    public static ScheduleModel crearCalendario(DetEmpleado empleado)
    {
        RegistroDAO registroDAO = new RegistroDAO();
        List<DetRegistro> lstRegistros = registroDAO.consultaRegistrosPorIdEmp(empleado.getIdEmpleado());
        int totalDiasTrabajado = validaDiasDescanso(empleado.getDatoEmpresa());
        
        ScheduleModel calendario = new DefaultScheduleModel();
        
        for(DefaultScheduleEvent auxEventDiasDescansoObligatorio : generaEventosDiasDescansoObligatorio(DiasDeDescansoObligatorioBL.diasDescansoAnual()))
        {
            calendario.addEvent(auxEventDiasDescansoObligatorio);
        }
        
        for(DefaultScheduleEvent auxEventRegistros : generaEventosRegistros(lstRegistros))
        {
            calendario.addEvent(auxEventRegistros);
        }
        
        if(totalDiasTrabajado < 7)
        {
            for(DefaultScheduleEvent auxEventDiasDescanso : generaEventosDescanso(calendario, empleado))
            {
                calendario.addEvent(auxEventDiasDescanso);
            }
        }
        return calendario;
    }
    
    public static List<DefaultScheduleEvent> generaEventosRegistros(List<DetRegistro> registros) 
    {
        int retardosSemana = 0;
        int diaInicioSemana = 5; //JUEVES
        int diaInicioRegistros;
        
        CatEstatusRegistro estatusReg = null;
        int coloReg = 0;
        
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        sdf.setTimeZone(TimeZone.getTimeZone(ZoneId.of("GMT-6").normalized()));
        
        List<DefaultScheduleEvent> eventoDia = new ArrayList();
        
        for(DetRegistro registro : registros) 
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(registro.getFechaEntrada());

            if (cal.get(Calendar.DAY_OF_WEEK) == diaInicioSemana) 
            {
                retardosSemana = 0;
                diaInicioRegistros = diaInicioSemana;
            } else 
            {
                diaInicioRegistros = cal.get(Calendar.DAY_OF_WEEK);
            }
            
            estatusReg = obtenerRegistroPorEstatus(registro.getStatus());
            
            if(estatusReg != null)
            {
                coloReg = colorRegistroEstatus(estatusReg.getCodigo());
                DefaultScheduleEvent eventoEstatus = DefaultScheduleEvent.builder()
                    .title(estatusReg.getDescripcion())
                    .allDay(true)
                    .styleClass(estiloByTipo(coloReg))
                    .startDate(DateUtil.toLocalDateTime(registro.getFechaEntrada()))
                    .endDate(DateUtil.toLocalDateTime(registro.getFechaEntrada()))
                    .description(null)
                    .dynamicProperty("tipoSolicitud", estatusReg.getDescripcion())
                    .build();
                eventoDia.add(eventoEstatus);
            }
            
            if(registro.getStatus().getIdEstatus() == 2) 
            {
                retardosSemana += 1;
            }

            //Evento de entrada
            DefaultScheduleEvent eventoEntrada = DefaultScheduleEvent.builder()
                    .title("Entrada " + sdf.format(registro.getFechaEntrada()))
                    .startDate(DateUtil.toLocalDateTime(registro.getFechaEntrada()))
                    .endDate(DateUtil.toLocalDateTime(registro.getFechaEntrada()))
                    .description(null)
                    .dynamicProperty("estatus", registro.getStatus().getDescripcion())
                    .build();
            eventoDia.add(eventoEntrada);

            //Genera horario de comida
            DefaultScheduleEvent eventoComida = DefaultScheduleEvent.builder()
                    .title("Comida\n 2:00 PM - 03:00 PM")
                    .startDate(DateUtil.toLocalDateTime(generaHorarioComida(true, registro.getFechaEntrada())))
                    .endDate(DateUtil.toLocalDateTime(generaHorarioComida(false, registro.getFechaEntrada())))
                    .description(null)
                    .styleClass(estiloByTipo(11))
                    .dynamicProperty("tipoSolicitud", "Horario de comida")
                    .dynamicProperty("idTipoSolicitud", 3)
                    .build();
            eventoDia.add(eventoComida);

            //Evento de salida
            if(registro.getFechaSalida() != null) 
            {
                DefaultScheduleEvent  eventoSalida = DefaultScheduleEvent.builder()
                        .title("Salida " + sdf.format(registro.getFechaSalida()))
                        .startDate(DateUtil.toLocalDateTime(registro.getFechaSalida()))
                        .endDate(DateUtil.toLocalDateTime(registro.getFechaSalida()))
                        .description(sdf.format(registro.getFechaSalida()))
                        .dynamicProperty("estatus", registro.getStatus().getDescripcion())
                        .build();
                eventoDia.add(eventoSalida);
            }
        }
        
        return eventoDia;
    }
    
    public static List<DefaultScheduleEvent> generaEventosDiasDescansoObligatorio(List<CatDiaNoLaboral> diasNoLaboral) 
    {
        List<DefaultScheduleEvent> listDescanso = new ArrayList();
        for(CatDiaNoLaboral auxDiasNoLaboral : diasNoLaboral) 
        {
            DefaultScheduleEvent eventoDescanso = DefaultScheduleEvent.builder()
                    .title(auxDiasNoLaboral.getDescripcion())
                    .startDate(DateUtil.toLocalDateTime(auxDiasNoLaboral.getFecha()))
                    .endDate(DateUtil.toLocalDateTime(auxDiasNoLaboral.getFecha()))
                    .allDay(true)
                    .description(auxDiasNoLaboral.getDescripcion())
                    .styleClass(estiloByTipo(10))
                    .dynamicProperty("tipoSolicitud", "Descanso Obligatorio")
                    .dynamicProperty("idTipoSolicitud", 5)
                    .build();
            listDescanso.add(eventoDescanso);
        }
        return listDescanso;
    }
    
    public static List<DefaultScheduleEvent> generaEventosDescanso(ScheduleModel modelCalendario, DetEmpleado empleado)
    {
        List<ScheduleEvent<?>> eventos = modelCalendario.getEvents();
        List<DefaultScheduleEvent> listDescanso = new ArrayList();
        InfDatoEmpresa empleadoEmpresa = empleado.getDatoEmpresa();
        
        Date fechaSistema = new Date();
        LocalDate fechaInicio = DateUtil.inicializaFechaInicioAnioEnCurso(DateUtil.getAnio(fechaSistema));
        LocalDate fechaActual = DateUtil.inicializaFechaTerminoAnioEnCurso(DateUtil.getAnio(fechaSistema));
        
        if(fechaActual.isEqual(fechaInicio))
        {
            fechaInicio = DateUtil.inicializaFechaInicioAnioEnCurso(DateUtil.getAnio(fechaSistema) - 1);
        }
        
        Set<DayOfWeek> diasDeDescanso = obtenerNombreDiasDeDescanso(empleadoEmpresa);
        
        List<LocalDate> arregloFechas = DateUtil.arregloLocalDate(fechaInicio, fechaActual);
        
        for(LocalDate auxFecha : arregloFechas) 
        {
            DayOfWeek diaDeLaSemana = auxFecha.getDayOfWeek();
            // Si es un día de descanso, lo marcamos en el calendario
            if(diasDeDescanso.contains(diaDeLaSemana)) 
            {
                DefaultScheduleEvent eventoDeDescanso = DefaultScheduleEvent.builder()
                    .title("Descanso")
                    .startDate(auxFecha.atStartOfDay())
                    .endDate(auxFecha.atStartOfDay())
                    .allDay(true)
                    .description("Dia de descanso del empleado")
                    .styleClass(estiloByTipo(6))
                    .dynamicProperty("tipoSolicitud", "Descanso")
                    .dynamicProperty("idTipoSolicitud", 5)
                    .build();
                
                if (!eventoExiste(eventos, eventoDeDescanso)) 
                {
                    listDescanso.add(eventoDeDescanso);
                }
            }
        }
        
        return listDescanso;
    }
    
    public static int colorRegistroEstatus(String codigo)
    {
        int auxColor = 0;
        
        switch(codigo)
        {
            case "T":
                auxColor = 1;
                break;
            case "R":
                auxColor = 2;
                break;
            case "F":
                auxColor = 3;
                break;
            case "J":
                auxColor = 4;
                break;
            case "X":
                auxColor = 5;
                break;
            case "D":
                auxColor = 6;
                break;
            case "V":
                auxColor = 7;
                break;
            case "P":
                auxColor = 8;
                break;
            case "I":
                auxColor = 9;
                break;
            default:
                auxColor = 12;
        }
        
        return auxColor;
    }
    
    public static String estiloByTipo(int idTipoEvento) 
    {
        String estilo = "";
        switch(idTipoEvento) 
        {
            case 1:
                estilo = "ferbo-evento-aTiempo";//A Tiempo
                break;
            case 2:
                estilo = "ferbo-evento-retardo";//Retardo
                break;
            case 3:
                estilo = "ferbo-evento-falta";//Falta
                break;
            case 4:
                estilo = "ferbo-evento-justificado";//Justificado
                break;
            case 5:
                estilo = "ferbo-evento-asistenciaDiaNoLaboral";//Asistencia en dia no laboral
                break;
            case 6:
                estilo = "ferbo-evento-descanso";//Descanso
                break;
            case 7:
                estilo = "ferbo-evento-vacaciones";//Vacaciones
                break;
            case 8:
                estilo = "ferbo-evento-permiso";//Permiso
                break;
            case 9:
                estilo = "ferbo-evento-incapacidad";//Incapacidad
                break;
            case 10:
                estilo = "ferbo-evento-diaFestivo";//Dia festivo
                break;
            case 11:
                estilo = "ferbo-evento-comida";//Comida
                break;
            default:
                estilo = "ferbo-evento-none";
        }
        return estilo;
    }
    
    public static CatEstatusRegistro obtenerRegistroPorEstatus(CatEstatusRegistro auxEstatusRegistro)
    {
        CatEstatusRegistro aux = null;
        
        switch(auxEstatusRegistro.getCodigo())
        {
            case "T":
                aux = auxEstatusRegistro; //A Tiempo
                break;
            case "R":
                aux = auxEstatusRegistro; //Retardo
                break;
            case "F":
                aux = auxEstatusRegistro; //Falta
                break;
            case "J":
                aux = auxEstatusRegistro; //Justificado
                break;
            case "X":
                aux = auxEstatusRegistro; //Asistencia en dia no laboral
                break;
            case "D":
                aux = auxEstatusRegistro; //Descanso
                break;
            case "P":
                aux = auxEstatusRegistro; //Permiso
                break;
            case "V":
                aux = auxEstatusRegistro; //Vacaciones
                break;
            case "I":
                aux = auxEstatusRegistro; //Incapacidad
                break;
        }
        
        return aux;
    }
    
    public static Set<DayOfWeek> obtenerNombreDiasDeDescanso(InfDatoEmpresa empleadoEmpresa)
    {
        Set<DayOfWeek> diasDeDescanso = new HashSet<>();
        
        if(empleadoEmpresa.getDiaLunes() == false) {
            diasDeDescanso.add(DayOfWeek.MONDAY);
        }
        
        if(empleadoEmpresa.getDiaMartes() == false) {
            diasDeDescanso.add(DayOfWeek.TUESDAY);
        }
        
        if(empleadoEmpresa.getDiaMiercoles() == false) {
            diasDeDescanso.add(DayOfWeek.WEDNESDAY);
        }
        
        if(empleadoEmpresa.getDiaJueves() == false) {
            diasDeDescanso.add(DayOfWeek.THURSDAY);
        }
        
        if(empleadoEmpresa.getDiaViernes() == false) {
            diasDeDescanso.add(DayOfWeek.FRIDAY);
        }
        
        if(empleadoEmpresa.getDiaSabado() == false) {
            diasDeDescanso.add(DayOfWeek.SATURDAY);
        }
        
        if(empleadoEmpresa.getDiaDomingo() == false) {
            diasDeDescanso.add(DayOfWeek.SUNDAY);
        }
        
        return diasDeDescanso;
    }
    
    public static int validaDiasDescanso(InfDatoEmpresa empleadoEmpresa)
    {
        int diasDescanso = 0;
        
        if(empleadoEmpresa.getDiaLunes() == false) {
            diasDescanso += 1;
        }
        
        if(empleadoEmpresa.getDiaMartes() == false) {
            diasDescanso += 1;
        }
        
        if(empleadoEmpresa.getDiaMiercoles() == false) {
            diasDescanso += 1;
        }
        
        if(empleadoEmpresa.getDiaJueves() == false) {
            diasDescanso += 1;
        }
        
        if(empleadoEmpresa.getDiaViernes() == false) {
            diasDescanso += 1;
        }
        
        if(empleadoEmpresa.getDiaSabado() == false) {
            diasDescanso += 1;
        }
        
        if(empleadoEmpresa.getDiaDomingo() == false) {
            diasDescanso += 1;
        }
        
        return diasDescanso;
    }
    
    public static Date generaHorarioComida(boolean inicio, Date fecha) 
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        if(inicio)
        {
            calendar.set(Calendar.HOUR_OF_DAY, 15);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        }else 
        {
            calendar.set(Calendar.HOUR_OF_DAY, 16);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        }
        return calendar.getTime();
    }
    
}
