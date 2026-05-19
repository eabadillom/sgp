package mx.com.ferbo.business.incidencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.dianolaboral.DiasNoLaboralesBL;
import mx.com.ferbo.business.incapacidad.IncapacidadBL;
import mx.com.ferbo.business.notifmovil.NotifMovilBL;
import mx.com.ferbo.business.registro.RegistroBL;
import mx.com.ferbo.business.sgpapiclient.SGPApiClientBL;
import mx.com.ferbo.dao.n.IncidenciaDAO;
import mx.com.ferbo.dao.n.RegistroVacacionesDAO;
import mx.com.ferbo.dao.n.TipoIncidenciaDAO;
import mx.com.ferbo.dto.NotificacionMovilDTO;
import mx.com.ferbo.model.CatEstatusIncidencia;
import mx.com.ferbo.model.CatTipoIncidencia;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.model.DetRegistroVacaciones;
import mx.com.ferbo.model.DetSolicitudArticulo;
import mx.com.ferbo.model.DetSolicitudPermiso;
import mx.com.ferbo.model.DetSolicitudPrenda;
import mx.com.ferbo.model.DetVacaciones;
import mx.com.ferbo.model.InfDatoEmpresa;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.SGPException;

/**
 *
 * @author alberto
 */
public class IncidenciaBL implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(IncidenciaBL.class);
    
    public static final String TP_PERMISO    = TipoIncidenciaBL.TP_PERMISO;
    public static final String TP_VACACIONES = TipoIncidenciaBL.TP_VACACIONES;
    public static final String TP_PRENDA     = TipoIncidenciaBL.TP_PRENDA;
    public static final String TP_ARTICULO   = TipoIncidenciaBL.TP_ARTICULO;
    
    public static final String ST_ENVIADA    = EstatusIncidenciaBL.ST_ENVIADA;
    public static final String ST_APROBADA   = EstatusIncidenciaBL.ST_APROBADA;
    public static final String ST_RECHAZADA  = EstatusIncidenciaBL.ST_RECHAZADA;
    public static final String ST_CANCELADA  = EstatusIncidenciaBL.ST_CANCELADA;
    
    @Deprecated
    public static List<Date> fechasSolicitudPermiso(DetSolicitudPermiso auxSolicitudPermiso)
    {
        Date fechaInicio = auxSolicitudPermiso.getFechaInicio();
        Date fechaFin = auxSolicitudPermiso.getFechaFin();
        List<Date> arregloFechas = DateUtil.generarArreglosFechas(fechaInicio, fechaFin);
        return arregloFechas;
    }
    
    public static List<Integer> obtenerDiasSeleccionados(InfDatoEmpresa empleadoEmpresa) {
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
    
    public static DetIncidencia create(String claveTipoIncidencia, DetEmpleado empleado)
    throws SGPException {
    	DetIncidencia incidencia = null;
    	CatTipoIncidencia tipoIncidencia = null;
    	TipoIncidenciaDAO tipoIncidenciaDAO = null;
    	
    	tipoIncidenciaDAO = new TipoIncidenciaDAO();
    	
    	incidencia = new DetIncidencia();
    	tipoIncidencia = tipoIncidenciaDAO.buscarPorClave(claveTipoIncidencia)
    			.orElseThrow(() -> new SGPException("Tipo de incidencia no encontrada."));
    	incidencia.setTipoIncidencia(tipoIncidencia);
    	incidencia.setEmpleado(empleado);
    	incidencia.setEstatusIncidencia(EstatusIncidenciaBL.enviado());
    	incidencia.setVisible((short) 1);
    	incidencia.setFechaCap(new Date());
    	
    	switch(claveTipoIncidencia) {
    		case TP_PERMISO:
    			incidencia.setSolPermiso(createSolicitudPermiso(empleado));
    			tipoIncidencia = tipoIncidenciaDAO.buscarPorClave(TP_PERMISO).orElseThrow(() -> new SGPException("Tipo de incidencia no encontrado."));
    			incidencia.setTipoIncidencia(tipoIncidencia);
    			break;
    		case TP_VACACIONES:
    			incidencia.setSolPermiso(createSolicitudVacaciones(empleado));
    			tipoIncidencia = tipoIncidenciaDAO.buscarPorClave(TP_VACACIONES).orElseThrow(() -> new SGPException("Tipo de incidencia no encontrado."));
    			incidencia.setTipoIncidencia(tipoIncidencia);
    			break;
    		case TP_PRENDA:
    			incidencia.setSolPrenda(createSolicitudPrenda(empleado));
    			break;
    		case TP_ARTICULO:
    			incidencia.setSolArticulo(createSolicitudArticulo(empleado));
    			break;
    		default:
    			throw new SGPException(String.format("Tipo de incidencia no permitido: %s", claveTipoIncidencia));
    	}
    	
    	return incidencia;
    }
    
    private static DetSolicitudPermiso createSolicitudPermiso(DetEmpleado empleado)
    throws SGPException {
    	return SolicitudPermisoBL.createSolicitudPermiso(empleado);
    }
    
    private static DetSolicitudPermiso createSolicitudVacaciones(DetEmpleado empleado)
    throws SGPException {
    	return SolicitudPermisoBL.createSolicitudVacaciones(empleado);
    }
    
    public static DetSolicitudArticulo createSolicitudArticulo(DetEmpleado empleado) {
    	throw new UnsupportedOperationException("Implementación pendiente...");
    }
    
    public static DetSolicitudPrenda createSolicitudPrenda(DetEmpleado empleado) {
    	throw new UnsupportedOperationException("Implementación pendiente..."); 
    }
    
    /**Se debe invocar a este método cuando se desea guardar un periodo vacacional o una solicitud de permiso de ausencia.
     * 
     * @param incidencia Objeto incidencia.
     * @param vacaciones Periodo vacacional sobre el que se solicita el permiso de vacaciones.
     * @throws SQLException En caso de ocurrir una validación incorrecta o un problema al guardar en la base de datos.
     */
    public static void guardar(DetIncidencia incidencia, DetVacaciones periodoVacacional)
    throws SGPException {
    	
    	if(TP_VACACIONES.equalsIgnoreCase(incidencia.getTipoIncidencia().getClave())) {
    		incidencia.getSolPermiso().setVacaciones(periodoVacacional);
    	}
    	
    	SolicitudPermisoBL.validarSolicitudPermiso(incidencia.getSolPermiso());
    	
    	IncapacidadBL.validarRegistrosIncapacidades(
    			incidencia.getEmpleado(),
    			incidencia.getSolPermiso().getFechaInicio(),
    			incidencia.getSolPermiso().getFechaFin())
    			;
    }
    
    public static void guardar(DetIncidencia incidencia)
    throws SGPException {
    	SolicitudPermisoBL.validarSolicitudPermiso(incidencia.getSolPermiso());
    	
    	IncapacidadBL.validarRegistrosIncapacidades(
    			incidencia.getEmpleado(),
    			incidencia.getSolPermiso().getFechaInicio(),
    			incidencia.getSolPermiso().getFechaFin());
    	
    	IncidenciaDAO incidenciaDAO = new IncidenciaDAO();
    	
    	if(incidencia.getIdIncidencia() == null)
    		incidenciaDAO.guardar(incidencia);
        else
			incidenciaDAO.actualizar(incidencia);
    }
    
    public static void cancelar(DetIncidencia incidencia)
    throws SGPException {
    
    	switch(incidencia.getTipoIncidencia().getClave()) {
    	case TP_PERMISO:
    		SolicitudPermisoBL.cancelar(incidencia);
    		break;
    	case TP_VACACIONES:
    		SolicitudPermisoBL.cancelar(incidencia);
    		break;
    		
    	case TP_ARTICULO:
    		
    		
    		break;
    		
    	case TP_PRENDA:
    		
    		break;
		default:
			throw new SGPException("Tipo de incidencia no válido");
    	}
    
    }
    
    public static void enviarNotificacion(DetIncidencia incidencia) {
    	SGPApiClientBL sgpApiClient = null;
    	NotificacionMovilDTO msjNotificacion = null;
    	String mensaje = "";
    	try {
    		sgpApiClient = new SGPApiClientBL();
    		switch(incidencia.getTipoIncidencia().getClave())
    		{
    		case TP_VACACIONES: 
    			mensaje = "vacaciones";
    			break;
    		case TP_PERMISO:
    			mensaje = "permiso";
    			break;
    		}
    		msjNotificacion = NotifMovilBL.obtenerMensaje(mensaje, incidencia.getEmpleado());
    		sgpApiClient.enviarNotificacion(msjNotificacion);
    	} catch(Exception ex) {
    		log.error("Problema para enviar la notificacion al SGP-Movil...", ex);
    	}
    }
    
    public static List<DetIncidencia> consultarIncidencias(Date fechaInicio, Date fechaFin, String clave)
    {
        IncidenciaDAO incidenciaDAO = new IncidenciaDAO();
        List<DetIncidencia> resultado = incidenciaDAO.buscarPorPerido(fechaInicio, fechaFin);
        
        resultado = resultado.stream()
                .filter(objeto -> objeto.getTipoIncidencia().getClave().trim().matches(clave))
                .collect(Collectors.toList());
        
        return resultado;
    }
    
    public static List<DetIncidencia> filtrarPorEmpleado(List<DetIncidencia> listIncidencias, String empleado)
    {
        Stream<DetIncidencia> stream = listIncidencias.stream();
        
        if (empleado != null && !empleado.trim().isEmpty()) {
            String filtro = empleado.trim().toUpperCase();
            
            stream = stream.filter(obj -> {
                String nombre = obj.getEmpleado().getNombre().toUpperCase();
                String ap1    = obj.getEmpleado().getPrimerAp().toUpperCase();
                String ap2    = obj.getEmpleado().getSegundoAp().toUpperCase();
                return nombre.contains(filtro) || ap1.contains(filtro) || ap2.contains(filtro);
            });
        }
        
        return stream.collect(Collectors.toList());
    }
    
    public static List<DetIncidencia> filtrarPorTipoIncidencia(List<DetIncidencia> listPermisos, String PERMISO, String VACACIONES, boolean incidenciaPermiso, boolean incidenciaVacaciones)
    {
        Stream<DetIncidencia> resultado = listPermisos.stream()
            // Filtrar por tipo (permiso / vacaciones)
            .filter(obj -> {
                String clave = obj.getSolPermiso().getTipoSolicitud().getClave();
                boolean esPermiso = incidenciaPermiso && clave.matches(PERMISO);
                boolean esVacaciones = incidenciaVacaciones && clave.matches(VACACIONES);
                return esPermiso || esVacaciones;
            })
            ;

        return resultado.collect(Collectors.toList());
    }
    
    public static List<DetIncidencia> filtrarPorEstatus(List<DetIncidencia> listPermisos, boolean estatusEnviado, boolean estatusAceptado, boolean estatusRechazado, boolean estatusCancelado){
        Stream<DetIncidencia> resultado = listPermisos.stream()
            .filter(obj -> {
                String est = obj.getEstatusIncidencia().getClave();

                // Si no se seleccionó ningún estatus, aceptar todos
                if (!estatusEnviado && !estatusAceptado && !estatusRechazado && !estatusCancelado) {
                    return true;
                }

                boolean enviado   = estatusEnviado   && est.equals(ST_ENVIADA);
                boolean aceptado  = estatusAceptado  && est.equals(ST_APROBADA);
                boolean rechazado = estatusRechazado && est.equals(ST_RECHAZADA);
                boolean cancelado = estatusCancelado && est.equals(ST_CANCELADA);

                return enviado || aceptado || rechazado || cancelado;
            })
            ;
        
        return resultado.collect(Collectors.toList());
    }
    
    public static void aprobarIncidencia(DetEmpleado autorizador, DetIncidencia incidencia, String goceSueldo) throws SGPException
    {
        IncidenciaDAO incidenciaDAO = new IncidenciaDAO();
        
        Boolean isGoceSueldo = incidencia.getEmpleado().getEmpleadoConfiguracion().getGoceSueldo();
        
        BigDecimal valor = Optional.ofNullable(goceSueldo)
            .filter(s -> Boolean.TRUE.equals(isGoceSueldo))
            .filter(s -> !s.trim().isEmpty())
            .map(BigDecimal::new)
            .orElse(BigDecimal.ZERO);

        incidencia.setEstatusIncidencia(EstatusIncidenciaBL.aprobado());
        incidencia.getSolPermiso().setEstatus(EstatusSolicitudBL.estatusAprobado());
        incidencia.getSolPermiso().setGoceSueldo(valor);
        incidencia.setEmpleadoRev(autorizador);
        incidencia.setFechaMod(new Date());
        incidencia.getSolPermiso().setFechaMod(incidencia.getFechaMod());
        incidencia.getSolPermiso().setEmpleadoRev(autorizador);

        if (incidencia.getEstatusIncidencia().getClave().trim().matches(ST_APROBADA)) {
            RegistroBL.guardarRegistroVacaciones(incidencia, DiasNoLaboralesBL.diasDeAsueto());
        }

        incidenciaDAO.actualizar(incidencia);
    }
    
    public static void rechazarIncidencia(DetIncidencia incidencia, DetEmpleado empleadoRev, String descripcionRechazo) throws SGPException
    {
        IncidenciaDAO incidenciaDAO = new IncidenciaDAO();
        
        incidencia.setEstatusIncidencia(EstatusIncidenciaBL.rechazado());
        incidencia.setEmpleadoRev(empleadoRev);
        incidencia.setFechaMod(new Date());
        incidencia.getSolPermiso().setFechaMod(new Date());
        incidencia.getSolPermiso().setEstatus(EstatusSolicitudBL.estatusRechazado());
        incidencia.getSolPermiso().setEmpleadoRev(empleadoRev);
        incidencia.getSolPermiso().setDescripcionRechazo(descripcionRechazo);
        
        incidenciaDAO.actualizar(incidencia);
    }
    
    public static void cancelarIncidencia(DetIncidencia incidencia, DetEmpleado empleadoRev) throws SGPException
    {
        IncidenciaDAO incidenciaDAO = new IncidenciaDAO();
        
        incidencia.setEmpleadoRev(new DetEmpleado(empleadoRev.getIdEmpleado()));
        incidencia.setEstatusIncidencia(EstatusIncidenciaBL.cancelado());
        incidencia.setFechaMod(new Date());
        incidencia.getSolPermiso().setEstatus(EstatusSolicitudBL.estatusCancelado());
        incidencia.getSolPermiso().setFechaMod(new Date());
        incidencia.getSolPermiso().setEmpleadoRev(new DetEmpleado(empleadoRev.getIdEmpleado()));
        
        incidenciaDAO.actualizar(incidencia);
    }
    
    public static void cancelarVacaciones(DetIncidencia incidencia) throws SGPException, RuntimeException
    {
        Date inicioPeriodoVacacional = incidencia.getSolPermiso().getFechaInicio();
        Date finPeriodoVacacional = incidencia.getSolPermiso().getFechaFin();

        DateUtil.setTime(inicioPeriodoVacacional, 0, 0, 0, 0);
        DateUtil.setTime(finPeriodoVacacional, 23, 59, 59);

        Date hoy = new Date();
        DateUtil.setTime(hoy, 0, 0, 0);

        if (finPeriodoVacacional.before(hoy)) {
            throw new RuntimeException("El periodo vacacional ya ha concluido");
        }

        if (inicioPeriodoVacacional.after(hoy)) {

            List<DetRegistro> registros = RegistroBL.buscarRegistrosVacaciones(incidencia);

            RegistroVacacionesDAO registroVacacionesDAO = new RegistroVacacionesDAO();

            for (DetRegistro registro : registros) {
                DetRegistroVacaciones registroVacacion = registro.getRegistroVacaciones();
                registroVacacionesDAO.eliminar(registroVacacion);
                RegistroBL.eliminarRegistro(registro);
            }

        }

        if (inicioPeriodoVacacional.before(hoy) && finPeriodoVacacional.after(hoy)) {
            throw new SGPException("El periodo esta en curso, no se eliminaran los registros");
        }
    }
    
    /*Metodo para actualizar una incidencia del tipo articulo o prenda*/
    public static void actualizarIncidencia(DetIncidencia incidencia, DetEmpleado empleado, boolean aprobado) throws SGPException
    {
        IncidenciaDAO incidenciaDAO = new IncidenciaDAO();
        
        FacesUtils.requireNonNull(incidencia, "Error al actualizar la incidencia");
        FacesUtils.requireNonNull(empleado, "Error al actualizar la incidencia");
        
        CatEstatusIncidencia estatusInc = aprobado ? EstatusIncidenciaBL.aprobado() : EstatusIncidenciaBL.rechazado();
        
        incidencia.setEstatusIncidencia(estatusInc);
        incidencia.setFechaMod(new Date());
        
        actualizarSolicitudArticulo(incidencia.getSolArticulo(), empleado, aprobado);
        actualizarSolicitudPrenda(incidencia.getSolPrenda(), empleado, aprobado);
        actualizarSolicitudPermiso(incidencia.getSolPermiso(), empleado, aprobado);
        
        incidenciaDAO.actualizar(incidencia);
    }
    
    private static void actualizarSolicitudArticulo(DetSolicitudArticulo solicitud, DetEmpleado emp, boolean aprobado) {
        if (solicitud == null) return;
        solicitud.setEstatus(aprobado ? EstatusSolicitudBL.estatusAprobado() : EstatusSolicitudBL.estatusRechazado());
        solicitud.setFechaMod(new Date());
        solicitud.setEmpleadoRev(emp);
    }

    private static void actualizarSolicitudPrenda(DetSolicitudPrenda solicitud, DetEmpleado emp, boolean aprobado) {
        if (solicitud == null) return;
        solicitud.setEstatus(aprobado ? EstatusSolicitudBL.estatusAprobado() : EstatusSolicitudBL.estatusRechazado());
        solicitud.setFechaMod(new Date());
        solicitud.setEmpleadoRev(emp);
    }

    private static void actualizarSolicitudPermiso(DetSolicitudPermiso solicitud, DetEmpleado emp, boolean aprobado) {
        if (solicitud == null) return;
        solicitud.setEstatus(aprobado ? EstatusSolicitudBL.estatusAprobado() : EstatusSolicitudBL.estatusRechazado());
        solicitud.setFechaMod(new Date());
        solicitud.setEmpleadoRev(emp);
    }
}
