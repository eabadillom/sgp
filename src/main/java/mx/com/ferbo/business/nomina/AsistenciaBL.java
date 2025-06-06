package mx.com.ferbo.business.nomina;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import mx.com.ferbo.dto.ui.Asistencia;
import mx.com.ferbo.dto.ui.AsistenciaStatus;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.util.DateUtil;

public class AsistenciaBL {
	
	public static List<Asistencia> getAsistenciaSemanal(Map<String, DetRegistro> mapAsistencias) {
		List<Asistencia> asistenciaList = new ArrayList<Asistencia>();
		Asistencia asistencia = null;
		
		for(Map.Entry<String, DetRegistro> entry : mapAsistencias.entrySet()) {
			DetRegistro registro = entry.getValue();
			
			asistencia = new Asistencia();
			asistencia.setEntrada(registro.getFechaEntrada());
			asistencia.setSalida(registro.getFechaSalida());
			
			asistencia.setStatus(new AsistenciaStatus());
			asistencia.getStatus().setClave(registro.getStatus().getCodigo());
			asistencia.getStatus().setNombre(registro.getStatus().getDescripcion());
			asistencia.setDiaSemana(DateUtil.getDiaSemana(registro.getFechaEntrada()));
			
			asistenciaList.add(asistencia);
		}
		
		asistenciaList.sort(Comparator.comparing(Asistencia :: getEntrada));
		
		return asistenciaList;
	}
	

}
