package mx.com.ferbo.business.incidencia;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.IncidenciaDAO;
import mx.com.ferbo.model.CatEstatusIncidencia;
import mx.com.ferbo.model.CatTipoIncidencia;
import mx.com.ferbo.model.DetIncidencia;

public class IncidenciaPermisosBL {
	
	private static Logger log = LogManager.getLogger(IncidenciaPermisosBL.class);

	public static List<DetIncidencia> buscarPendientes() {
		List<DetIncidencia> incidencias;
		IncidenciaDAO incidenciaDAO;
		CatEstatusIncidencia status;
		CatTipoIncidencia tipo;
		
		try {
			tipo = TipoIncidenciaBL.permiso();
			status = EstatusIncidenciaBL.enviado();
			
			incidenciaDAO = new IncidenciaDAO();
			log.info("Buscando incidencias (permisos de ausencia) por status {}", status.getDescripcion());
			incidencias = incidenciaDAO.buscarPorStatusTipo(status, tipo);
			
		} catch(Exception ex) {
			incidencias = new ArrayList<DetIncidencia>();
		}
		
		return incidencias;
	}

}
