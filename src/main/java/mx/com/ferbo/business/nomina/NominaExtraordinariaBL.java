package mx.com.ferbo.business.nomina;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.percepcion.AbstractPercepcion;
import mx.com.ferbo.business.percepcion.VacacionesReportadasPercepcion;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;

public class NominaExtraordinariaBL extends NominaBL {
	
	private static Logger log = LogManager.getLogger(NominaExtraordinariaBL.class);
	
	private Date periodoInicio = null;
	private Date periodoFin = null;
	private Date fechaInicioAnio = null;
	private Date fechafinAnio = null;
	private Integer anio = null;
	
	public NominaExtraordinariaBL() {
		super();
	}

	public NominaExtraordinariaBL(DetEmpleado empleado, ParametrosNomina parametros) {
		super(empleado, parametros, null);
		this.periodoInicio = parametros.getPeriodoInicio();
		this.periodoFin = parametros.getPeriodoFin();
		this.fechaInicioAnio = null;
		this.fechafinAnio = null;
	}
	
	public DetNomina calcular(DetNomina nomina, ParametrosNomina parametros, DetNominaPercepcion percepcion) {
		String clavePercepcion = null;
		
		try {
			clavePercepcion = percepcion.getClave();
			
			switch (clavePercepcion) {
			case AbstractPercepcion.CVE_VACACIONES_REPORTADAS:
				calcularVacacionesReportadas(nomina, parametros);
				break;

			default:
				log.info("haciendo algo general...");
				break;
			}
			
		} catch(Exception ex) {
			log.error("Problema para calcular la percepcion: {} - {}", percepcion.getClave(), percepcion.getNombre());
		}
		
		return nomina;
	}
	
	public static synchronized void calcularVacacionesReportadas(DetNomina nomina, ParametrosNomina parametros) {
		VacacionesReportadasPercepcion vacacionesBO = new VacacionesReportadasPercepcion(parametros);
		vacacionesBO.calcular(nomina);
	}

}
