package mx.com.ferbo.business.nomina;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AbstractDBL;
import mx.com.ferbo.business.deduccion.isr.ISRL174DBL;
import mx.com.ferbo.business.empleado.EmpleadoBL;
import mx.com.ferbo.business.percepcion.AbstractPBL;
import mx.com.ferbo.business.percepcion.AguinaldoPBL;
import mx.com.ferbo.business.percepcion.PrimaVacacionalEnTiempoPBL;
import mx.com.ferbo.business.percepcion.PrimaVacacionalReportadasPBL;
import mx.com.ferbo.business.percepcion.VacacionesReportadasPBL;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.util.SGPException;

public class NominaExtraordinariaBL extends NominaBL {
	
	private static Logger log = LogManager.getLogger(NominaExtraordinariaBL.class);
	
	public NominaExtraordinariaBL() {
		super();
	}
	
	public NominaExtraordinariaBL(ParametrosNomina parametros) {
		this.parametros = parametros;
	}

	public NominaExtraordinariaBL(DetEmpleado empleado, ParametrosNomina parametros) {
		super(empleado, parametros, null);
	}
	
	public DetNomina calcular(DetNomina nomina, DetNominaPercepcion percepcion) {
		String clavePercepcion = null;
		
		try {
			clavePercepcion = percepcion.getClave();
			
			switch (clavePercepcion) {
				case AbstractPBL.CVE_VACACIONES_REPORTADAS:
					calcularVacacionesReportadas(nomina, parametros);
					break;
					
				case AbstractPBL.CVE_PRIMA_VACACIONES_EN_TIEMPO:
					calcularPrimaVacacionesEnTiempo(nomina, parametros);
					break;
					
				case AbstractPBL.CVE_PRIMA_VACACIONES_REPORTADAS:
					calcularPrimaVacacionesReportadas(nomina, parametros);
					break;
				
				case AbstractPBL.CVE_AGUINALDO:
					calcularAguinaldo(nomina, parametros);
					break;
				default:
					throw new UnsupportedOperationException("La percepción no está implementada.");
			}
			
			calcularISRL174(nomina, parametros);
			calcularTotales(nomina, parametros);
			
		} catch(Exception ex) {
			log.error("Problema para calcular la percepcion: {} - {}", percepcion.getClave(), percepcion.getNombre());
		}
		
		return nomina;
	}
	
	/*--------------------------PERCEPCIONES----------------------------------------------------*/
	
	public static synchronized void calcularVacacionesReportadas(DetNomina nomina, ParametrosNomina parametros) {
		DetNominaPercepcion percepcion = null;
		final String clave = AbstractPBL.CVE_VACACIONES_REPORTADAS;
		boolean removedPercepciones = nomina.getPercepciones().removeIf(p -> p.getClave().equalsIgnoreCase(clave));
		if(removedPercepciones)
			log.info("Se encontraron conceptos {}, los cuales fueron eliminados de la lista de percepciones.", clave);
		
		VacacionesReportadasPBL vacacionesBO = new VacacionesReportadasPBL(parametros);
		percepcion = vacacionesBO.calcular(nomina);
		
		if(percepcion.getImporteExento().add(percepcion.getImporteGravado()).compareTo(ValoresBD._CERO.get()) > 0)
			nomina.getPercepciones().add(percepcion);
	}
	
	public static synchronized void calcularPrimaVacacionesEnTiempo(DetNomina nomina, ParametrosNomina parametros) {
		DetNominaPercepcion percepcion = null;
		final String clave = AbstractPBL.CVE_PRIMA_VACACIONES_EN_TIEMPO;
		boolean removePercepciones = nomina.getPercepciones().removeIf( p -> p.getClave().equalsIgnoreCase(clave));
		
		if(removePercepciones)
			log.info("Se encontraron conceptos {}, los cuales fueron eliminados de la lista de percepciones.", clave);
		
		PrimaVacacionalEnTiempoPBL primaBO = new PrimaVacacionalEnTiempoPBL(parametros);
		percepcion = primaBO.calcular(nomina);
		
		if(percepcion.getImporteExento().add(percepcion.getImporteGravado()).compareTo(ValoresBD._CERO.get()) > 0)
			nomina.getPercepciones().add(percepcion);
	}
	
	public static synchronized void calcularPrimaVacacionesReportadas(DetNomina nomina, ParametrosNomina parametros) {
		DetNominaPercepcion percepcion = null;
		
		final String clave = AbstractPBL.CVE_PRIMA_VACACIONES_REPORTADAS;
		boolean removedPercepciones = nomina.getPercepciones().removeIf( p -> p.getClave().equalsIgnoreCase(clave));
		
		if(removedPercepciones)
			log.info("Se encontraron conceptos {}, los cuales fueron eliminados de la lista de percepciones.", clave);
		
		PrimaVacacionalReportadasPBL primaBO = new PrimaVacacionalReportadasPBL(parametros);
		percepcion = primaBO.calcular(nomina);
		
		if(percepcion.getImporteExento().add(percepcion.getImporteGravado()).compareTo(ValoresBD._CERO.get()) > 0)
			nomina.getPercepciones().add(percepcion);
	}
	
	public static synchronized void calcularAguinaldo(DetNomina nomina, ParametrosNomina parametros) {
		DetNominaPercepcion percepcion = null;
		
		DetEmpleado empleado = null;
		AguinaldoPBL aguinaldoBO = null;
		
		try {
			final String clave = AbstractPBL.CVE_AGUINALDO;
			boolean removedPercepciones = nomina.getPercepciones().removeIf( p -> p.getClave().equalsIgnoreCase(clave));
					
			if(removedPercepciones)
				log.info("Se encontraron conceptos {}, los cuales fueron eliminados de la lista de percepciones.", clave);
			
			empleado = EmpleadoBL.load(nomina.getReceptor().getRfc());
			
			//TODO FALTA PROCESAR LAS AUSENCIAS DEL AÑO COMPLETO.
			
			aguinaldoBO = new AguinaldoPBL(parametros, empleado.getDatoEmpresa().getDiasAguinaldo(), ValoresBD._CERO.get());
			
			percepcion = aguinaldoBO.calcular(nomina);
			
			if(percepcion.getImporteExento().add(percepcion.getImporteGravado()).compareTo(ValoresBD._CERO.get()) > 0)
				nomina.getPercepciones().add(percepcion);
			
		} catch (SGPException ex) {
			log.error("Problema para generar la percepción...", ex);
		}
	}
	
	/*--------------------------DEDUCCIONES-----------------------------------------------------*/
	
	public static synchronized void calcularISRL174(DetNomina nomina, ParametrosNomina parametros) {
		DetNominaDeduccion deduccion = null;
		ISRL174DBL isrBO = null;
		BigDecimal importeGravado = null;
		
		
		final String clave = AbstractDBL.CVE_ISR_LEY_174;
		boolean removedPercepciones = nomina.getDeducciones().removeIf( p -> p.getClave().equalsIgnoreCase(clave));
		
		if(removedPercepciones)
			log.info("Se encontraron conceptos {}, los cuales fueron eliminados de la lista de percepciones.", clave);
		
		//Obtener la suma de los importes gravados de las percepciones.
		importeGravado = nomina.getPercepciones().stream()
				.map(d -> d.getImporteGravado())
				.reduce(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal :: add)
				;
		
		isrBO = new ISRL174DBL(parametros, importeGravado);
		
		deduccion = isrBO.calcular(nomina);
		
		nomina.getDeducciones().add(deduccion);
	}
}
