package mx.com.ferbo.business.vacaciones;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.registro.EstatusRegistroBL;
import mx.com.ferbo.dao.n.VacacionesDAO;
import mx.com.ferbo.model.CatEstatusRegistro;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.model.DetVacaciones;

public class VacacionesBL {
	
	public static Logger log = LogManager.getLogger(VacacionesBL.class);
	
	public static List<DetVacaciones> cargarPeriodosConSaldo(Integer idEmpleado, Date fecha) {
		List<DetVacaciones> periodos = new ArrayList<DetVacaciones>();
		VacacionesDAO vacacionesDAO = new VacacionesDAO();
        
        log.info("Buscando periodos vacacionales del empleado...");
        List<DetVacaciones> periodosTmp = vacacionesDAO.cargarPeriodos(idEmpleado, fecha);
        
        for (DetVacaciones periodo : periodosTmp) {
        	if(periodo.getDiasPendientesPagados())
        		continue;
        	
        	if (periodo.getDiasTomados() < periodo.getDiasTotales() && (periodo.getDiasPagados() + periodo.getDiasTomados()) < periodo.getDiasTotales()) {
        		periodos.add(periodo);
        	}
        }
        
        return periodos;
	}
	
	public static Integer diasDisponibles(DetVacaciones periodo) {
		Integer diasDisponibles = null;
		CatEstatusRegistro status = EstatusRegistroBL.estatusVacaciones();
		
		if(periodo.getDiasPendientesPagados())
			return 0;
		
		List<DetRegistro> diasVacaciones = periodo.getRegistroVacaciones()
			.stream()
			.filter(item -> item.getRegistro() != null)
			.filter(item -> status.getCodigo().equals(item.getRegistro().getStatus().getCodigo()))
			.map(item -> item.getRegistro())
			.collect(Collectors.toList())
			;
		
		Integer diasTotales = periodo.getDiasTotales();
		Integer diasTomados = periodo.getDiasTomados();
		Integer diasDisfrutados = diasVacaciones.size();
		
		diasDisponibles = diasTotales - diasTomados - diasDisfrutados;
		
		return diasDisponibles;
	}

}
