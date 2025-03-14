package mx.com.ferbo.business.nomina;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.percepcion.AbstractPercepcion;
import mx.com.ferbo.business.percepcion.PrimaVacacionalEnTiempoPercepcion;
import mx.com.ferbo.business.percepcion.PrimaVacacionalReportadasPercepcion;
import mx.com.ferbo.business.percepcion.VacacionesReportadasPercepcion;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;

public class NominaExtraordinariaBL extends NominaBL {
	
	private static Logger log = LogManager.getLogger(NominaExtraordinariaBL.class);
	
//	private Date periodoInicio = null;
//	private Date periodoFin = null;
//	private Date fechaInicioAnio = null;
//	private Date fechafinAnio = null;
//	private Integer anio = null;
	
	public NominaExtraordinariaBL() {
		super();
	}

	public NominaExtraordinariaBL(DetEmpleado empleado, ParametrosNomina parametros) {
		super(empleado, parametros, null);
//		this.periodoInicio = parametros.getPeriodoInicio();
//		this.periodoFin = parametros.getPeriodoFin();
//		this.fechaInicioAnio = null;
//		this.fechafinAnio = null;
	}
	
	public DetNomina calcular(DetNomina nomina, ParametrosNomina parametros, DetNominaPercepcion percepcion) {
		String clavePercepcion = null;
		
		try {
			clavePercepcion = percepcion.getClave();
			
			switch (clavePercepcion) {
			case AbstractPercepcion.CVE_VACACIONES_REPORTADAS:
				calcularVacacionesReportadas(nomina, parametros);
				break;
				
			case AbstractPercepcion.CVE_PRIMA_VACACIONES_EN_TIEMPO:
				calcularPrimaVacacionesEnTiempo(nomina, parametros);
				break;
				
			case AbstractPercepcion.CVE_PRIMA_VACACIONES_REPORTADAS:
				calcularPrimaVacacionesReportadas(nomina, parametros);
				break;
			default:
				throw new UnsupportedOperationException("La percepción no está implementada.");
			}
			
		} catch(Exception ex) {
			log.error("Problema para calcular la percepcion: {} - {}", percepcion.getClave(), percepcion.getNombre());
		}
		
		return nomina;
	}
	
	/*-----------------------------------------------------------------------------------------------------------*/
	
	public static synchronized void calcularVacacionesReportadas(DetNomina nomina, ParametrosNomina parametros) {
		DetNominaPercepcion percepcion = null;
		final String clave = AbstractPercepcion.CVE_VACACIONES_REPORTADAS;
		boolean removedPercepciones = nomina.getPercepciones().removeIf(p -> p.getClave().equalsIgnoreCase(clave));
		if(removedPercepciones)
			log.info("Se encontraron conceptos {}, los cuales fueron eliminados de la lista de percepciones.", clave);
		
		VacacionesReportadasPercepcion vacacionesBO = new VacacionesReportadasPercepcion(parametros);
		percepcion = vacacionesBO.calcular(nomina);
		
		if(percepcion.getImporteExento().add(percepcion.getImporteGravado()).compareTo(ValoresBD.CERO.getValor()) > 0)
			nomina.getPercepciones().add(percepcion);
	}
	
	public static synchronized void calcularPrimaVacacionesEnTiempo(DetNomina nomina, ParametrosNomina parametros) {
		DetNominaPercepcion percepcion = null;
		final String clave = AbstractPercepcion.CVE_PRIMA_VACACIONES_EN_TIEMPO;
		boolean removePercepciones = nomina.getPercepciones().removeIf( p -> p.getClave().equalsIgnoreCase(clave));
		
		if(removePercepciones)
			log.info("Se encontraron conceptos {}, los cuales fueron eliminados de la lista de percepciones.", clave);
		
		PrimaVacacionalEnTiempoPercepcion primaBO = new PrimaVacacionalEnTiempoPercepcion(parametros);
		percepcion = primaBO.calcular(nomina);
		
		if(percepcion.getImporteExento().add(percepcion.getImporteGravado()).compareTo(ValoresBD.CERO.getValor()) > 0)
			nomina.getPercepciones().add(percepcion);
	}
	
	public static synchronized void calcularPrimaVacacionesReportadas(DetNomina nomina, ParametrosNomina parametros) {
		DetNominaPercepcion percepcion = null;
		
		final String clave = AbstractPercepcion.CVE_PRIMA_VACACIONES_REPORTADAS;
		boolean removePercepciones = nomina.getPercepciones().removeIf( p -> p.getClave().equalsIgnoreCase(clave));
		
		if(removePercepciones)
			log.info("Se encontraron conceptos {}, los cuales fueron eliminados de la lista de percepciones.", clave);
		
		PrimaVacacionalReportadasPercepcion primaBO = new PrimaVacacionalReportadasPercepcion(parametros);
		percepcion = primaBO.calcular(nomina);
		
		if(percepcion.getImporteExento().add(percepcion.getImporteGravado()).compareTo(ValoresBD.CERO.getValor()) > 0)
			nomina.getPercepciones().add(percepcion);
	}
}
