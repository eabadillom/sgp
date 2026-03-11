package mx.com.ferbo.business.incidencia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.IncidenciaDAO;
import mx.com.ferbo.dao.n.IncidenciaPermisoDAO;
import mx.com.ferbo.model.CatEstatusIncidencia;
import mx.com.ferbo.model.CatTipoIncidencia;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetIncidencia;

public class IncidenciaVacacionesBL {
	
	public static Logger log = LogManager.getLogger(IncidenciaVacacionesBL.class);
	
	public static List<DetIncidencia> buscarPendientes() {
		List<DetIncidencia> incidencias;
		IncidenciaDAO incidenciaDAO = null;
		CatEstatusIncidencia status;
		CatTipoIncidencia tipo;
		
		try {
			tipo = TipoIncidenciaBL.vacaciones();
			log.debug("Tipo incidencia: {}", tipo);
			status = EstatusIncidenciaBL.enviado();
			log.debug("Status incidencia: {}", status);
			
			incidenciaDAO = new IncidenciaDAO();
			incidencias = incidenciaDAO.buscarPorStatusTipo(status, tipo);
			
		} catch(Exception ex) {
			incidencias = new ArrayList<DetIncidencia>();
		}
		
		return incidencias;
	}
	
	public static List<DetIncidencia> buscarPendientes(DetEmpleado empleado) {
		List<DetIncidencia> incidencias;
		IncidenciaDAO incidenciaDAO = null;
		CatEstatusIncidencia status;
		CatTipoIncidencia tipo;
		
		try {
			tipo = TipoIncidenciaBL.vacaciones();
			status = EstatusIncidenciaBL.enviado();
			
			incidenciaDAO = new IncidenciaDAO();
			incidencias = incidenciaDAO.buscarPorStatusTipo(empleado, status, tipo);
			
		} catch(Exception ex) {
			incidencias = new ArrayList<DetIncidencia>();
		}
		
		return incidencias;
	}
	
	public static List<DetIncidencia> buscarEnviadas(DetEmpleado empleado, Date inicio, Date fin) {
		List<DetIncidencia> incidencias;
		IncidenciaPermisoDAO incidenciaDAO = null;
		CatEstatusIncidencia status;
		CatTipoIncidencia tipo;
		
		try {
			tipo = TipoIncidenciaBL.vacaciones();
			status = EstatusIncidenciaBL.enviado();
			
			incidenciaDAO = new IncidenciaPermisoDAO();
			incidencias = incidenciaDAO.buscarPor(empleado, inicio, fin, status, tipo);
			
		} catch(Exception ex) {
			incidencias = new ArrayList<DetIncidencia>();
		}
		
		return incidencias;
	}
	
	public static List<DetIncidencia> buscarAprobadas(DetEmpleado empleado, Date inicio, Date fin) {
		List<DetIncidencia> incidencias;
		IncidenciaPermisoDAO incidenciaDAO = null;
		CatEstatusIncidencia status;
		CatTipoIncidencia tipo;
		
		try {
			tipo = TipoIncidenciaBL.vacaciones();
			status = EstatusIncidenciaBL.aprobado();
			
			incidenciaDAO = new IncidenciaPermisoDAO();
			incidencias = incidenciaDAO.buscarPor(empleado, inicio, fin, status, tipo);
			
		} catch(Exception ex) {
			incidencias = new ArrayList<DetIncidencia>();
		}
		
		return incidencias;
	}
	
	public static List<DetIncidencia> buscarCanceladas(DetEmpleado empleado, Date inicio, Date fin) {
		List<DetIncidencia> incidencias;
		IncidenciaPermisoDAO incidenciaDAO = null;
		CatEstatusIncidencia status;
		CatTipoIncidencia tipo;
		
		try {
			tipo = TipoIncidenciaBL.vacaciones();
			status = EstatusIncidenciaBL.cancelado();
			
			incidenciaDAO = new IncidenciaPermisoDAO();
			incidencias = incidenciaDAO.buscarPor(empleado, inicio, fin, status, tipo);
			
		} catch(Exception ex) {
			incidencias = new ArrayList<DetIncidencia>();
		}
		
		return incidencias;
	}
	
	public static List<DetIncidencia> buscarRechazadas(DetEmpleado empleado, Date inicio, Date fin) {
		List<DetIncidencia> incidencias;
		IncidenciaPermisoDAO incidenciaDAO = null;
		CatEstatusIncidencia status;
		CatTipoIncidencia tipo;
		
		try {
			tipo = TipoIncidenciaBL.vacaciones();
			status = EstatusIncidenciaBL.rechazado();
			
			incidenciaDAO = new IncidenciaPermisoDAO();
			incidencias = incidenciaDAO.buscarPor(empleado, inicio, fin, status, tipo);
			
		} catch(Exception ex) {
			incidencias = new ArrayList<DetIncidencia>();
		}
		
		return incidencias;
	}
	

}
