package mx.com.ferbo.business.nomina;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AbstractDBL;
import mx.com.ferbo.business.deduccion.isr.ISRL174DBL;
import mx.com.ferbo.business.percepcion.AbstractPBL;
import mx.com.ferbo.business.percepcion.AguinaldoPBL;
import mx.com.ferbo.business.percepcion.PrimaVacacionalEnTiempoPBL;
import mx.com.ferbo.business.percepcion.PrimaVacacionalReportadasPBL;
import mx.com.ferbo.business.percepcion.VacacionesReportadasPBL;
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
	
	public DetNomina calcular(DetNomina nomina, String clavePercepcion)
	throws SGPException {
		AbstractPBL percepcionBO = null;
		DetNominaPercepcion percepcion = null;
		
		try {
			log.info("[UI] Calculando para el empleado {}", nomina.getReceptor().getNombre());
			percepcionBO = NominaExtraordinariaBL.getPercepcionBusinessLogic(parametros, nomina, clavePercepcion);
			percepcion = percepcionBO.procesar(nomina);
			agregarPercepcion(nomina, percepcion);
		} catch(UnsupportedOperationException ex) {
			throw new SGPException("El cálculo de la percepción no está implementado para la nómina extraordinaria");
		} catch(Exception ex) {
			log.error("Problema para calcular la percepcion: {}", clavePercepcion);
		} finally {
			calcularISRL174(nomina, parametros);
			calcularTotales(nomina, parametros);
		}
		
		return nomina;
	}
	
	public void calcular(DetNomina nomina, String clavePercepcion, BigDecimal cantidad)
	throws SGPException {
		AbstractPBL percepcionBO = null;
		DetNominaPercepcion nuevaPercepcion = null;
		
		try {
			log.info("[UI] Recalculando para el empleado {}", nomina.getReceptor().getNombre());
			percepcionBO = NominaExtraordinariaBL.getPercepcionBusinessLogic(parametros, nomina, clavePercepcion);
			nuevaPercepcion = percepcionBO.procesar(nomina, cantidad);
			agregarPercepcion(nomina, nuevaPercepcion);
		} catch(UnsupportedOperationException ex) {
			throw new SGPException("El cálculo de la percepción no está implementado para la nómina extraordinaria");
		} catch(Exception ex) {
			log.error("Problema para calcular la percepcion: {}", clavePercepcion);
		} finally {
			calcularISRL174(nomina, parametros);
			calcularTotales(nomina, parametros);
		}
	}
	
	/*--------------------------PERCEPCIONES----------------------------------------------------*/
	public static AbstractPBL getPercepcionBusinessLogic(ParametrosNomina parametros, DetNomina nomina, String clavePercepcion) {
		AbstractPBL percepcionBO = null;
		
		switch (clavePercepcion) {
		case AbstractPBL.CVE_PRIMA_VACACIONES_EN_TIEMPO:
				percepcionBO = new PrimaVacacionalEnTiempoPBL(parametros, nomina);
				break;
				
			case AbstractPBL.CVE_VACACIONES_REPORTADAS:
				percepcionBO = new VacacionesReportadasPBL(parametros, nomina);
				break;
				
			case AbstractPBL.CVE_PRIMA_VACACIONES_REPORTADAS:
				percepcionBO = new PrimaVacacionalReportadasPBL(parametros, nomina);
				break;
			
			case AbstractPBL.CVE_AGUINALDO:
				percepcionBO = new AguinaldoPBL(parametros, nomina);
				break;
				
			default:
				log.info("[UI] La percepción solicitada no está considerada para la nómina extraordinaria o no está implementada.");
				throw new UnsupportedOperationException("La percepción no está implementada.");
		}
		log.info("[UI] Tipo Percepcion (SAT): {} - {}", clavePercepcion, percepcionBO.getTipoPercepcion(clavePercepcion));
		
		return percepcionBO;
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
