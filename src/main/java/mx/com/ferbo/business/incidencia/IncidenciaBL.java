package mx.com.ferbo.business.incidencia;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.incapacidad.IncapacidadBL;
import mx.com.ferbo.dao.n.IncidenciaDAO;
import mx.com.ferbo.dao.n.TipoIncidenciaDAO;
import mx.com.ferbo.model.CatTipoIncidencia;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.model.DetSolicitudArticulo;
import mx.com.ferbo.model.DetSolicitudPermiso;
import mx.com.ferbo.model.DetSolicitudPrenda;
import mx.com.ferbo.model.DetVacaciones;
import mx.com.ferbo.model.InfDatoEmpresa;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

/**
 *
 * @author alberto
 */
public class IncidenciaBL implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(IncidenciaBL.class);
    
    public static final String TP_PERMISO = "PE";
    public static final String TP_VACACIONES = "V";
    public static final String TP_PRENDA = "PR";
    public static final String TP_ARTICULO = "A";
    
    public static final String ST_ENVIADA = "E";
    public static final String ST_APROBADA = "A";
    public static final String ST_RECHAZADA = "R";
    public static final String ST_CANCELADA = "C";
    
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
    	incidencia.setEstatusIncidencia(EstatusIncidenciaBL.estatusEnviado());
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
    			incidencia.getSolPermiso().getFechaFin())
    	;
    	
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
}
