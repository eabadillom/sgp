package mx.com.ferbo.business.incidencia;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import mx.com.ferbo.business.registro.EstatusRegistroBL;
import mx.com.ferbo.business.registro.RegistroBL;
import mx.com.ferbo.model.DetDiaPermiso;
import mx.com.ferbo.model.DetIncidencia;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.model.DetSolicitudPermiso;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

public class SolicitudVacacionesBL {
	
	public static List<DetDiaPermiso> toDiaPermisoList(DetSolicitudPermiso solicitud, List<Date> diasSolicitados) {
		List<DetDiaPermiso> diasPermiso = new ArrayList<DetDiaPermiso>();
		diasSolicitados.forEach(t -> {
			diasPermiso.add(DetDiaPermiso.builder().fecha(t).solicitudPermiso(solicitud).build());
		});
		
		return diasPermiso;
	}
	
	public static Validator validator() {
		return new Validator();
	}
	
	public static final class Validator {
		private DetIncidencia incidencia;
		private List<Date> diasSolicitados;
		
		public Validator() {
		}

		public Validator incidencia(DetIncidencia incidencia) {
			this.incidencia = incidencia;
			return this;
		}

		/**Verificar que la información de la solicitud de vacaciones está completa y correcta.
		 * @param incidencia
		 * @throws SGPException
		 */
		public boolean validate()
		throws SGPException {
			
			if(incidencia == null)
				throw new SGPException("La información de la solicitud es incorrecta.");
			
			if(incidencia.getSolPermiso() == null)
				throw new SGPException("La información de la solicitud es incorrecta.");
			
			if(incidencia.getSolPermiso().getVacaciones() == null)
				throw new SGPException("Debe indicar el periodo vacacional.");
			
			
			if(incidencia.getSolPermiso().getDiasPermiso() == null)
				throw new SGPException("Debe indicar al menos un día de vacaciones.");
			
			if(incidencia.getSolPermiso().getDiasPermiso().size() <= 0)
				throw new SGPException("Debe indicar al menos un día de vacaciones.");
			
			if(incidencia.getEmpleado() == null)
				throw new SGPException("Debe indicar un empleado solicitante.");
			
			incidencia.getSolPermiso().setEmpleadoSol(incidencia.getEmpleado());
			
			this.diasSolicitados = incidencia.getSolPermiso().getDiasPermiso().stream()
					.map(DetDiaPermiso::getFecha)
					.collect(Collectors.toList());
			
			Date fechaInicio = this.diasSolicitados.stream().min(Comparator.naturalOrder()).orElseThrow(() -> new SGPException("No hay fecha seleccionada."));
			Date fechaFin = this.diasSolicitados.stream().max(Comparator.naturalOrder()).orElseThrow(() -> new SGPException("No hay fecha seleccionada"));
			
			incidencia.getSolPermiso().setFechaInicio(fechaInicio);
			incidencia.getSolPermiso().setFechaFin(fechaFin);
			incidencia.setFechaCap(DateUtil.now());
			
			if(incidencia.getSolPermiso().getVacaciones().getDiasDisponibles() < this.diasSolicitados.size())
				throw new SGPException("No puede solicitar más días de los permitidos por el periodo indicado.");
			
			List<DetRegistro> registros = new ArrayList<DetRegistro>();
			
			for(Date d : diasSolicitados) {
				Optional<DetRegistro> r = RegistroBL.buscar(incidencia.getEmpleado().getIdEmpleado(), d);
				
				if(r.isPresent() == false)
					continue;
				
				switch (r.get().getStatus().getCodigo()) {
				case EstatusRegistroBL.AUSENCIA:
					continue;

				default:
					registros.add(r.get());
				}
			}
			
			if(registros.size() > 0)
				throw new SGPException("Existen registros de asistencia que coinciden con su solicitud.");
			
			return true;
		}
	}
}
