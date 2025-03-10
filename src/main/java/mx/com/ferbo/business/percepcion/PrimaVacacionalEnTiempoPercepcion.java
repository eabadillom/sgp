package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.dao.n.VacacionesDAO;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetVacaciones;
import mx.com.ferbo.util.SGPException;

public class PrimaVacacionalEnTiempoPercepcion extends AbstractPercepcion implements IPercepcion {
	
	private static Logger log = LogManager.getLogger(PrimaVacacionalEnTiempoPercepcion.class);
	
	private BigDecimal quince = new BigDecimal("15.00").setScale(2, BigDecimal.ROUND_HALF_UP);
	
	private ParametrosNomina parametros = null;
	private BigDecimal uma = null;
	
	public PrimaVacacionalEnTiempoPercepcion(ParametrosNomina parametros) {
		this.parametros = parametros;
	}

	@Override
	public void calcular(DetNomina nomina) {
		
		DetNominaPercepcion       percepcion    = null;
		List<DetNominaPercepcion> percepciones  = null;
		List<DetVacaciones>       listaPeriodos = null;
		DetVacaciones             periodo       = null;
		
		VacacionesDAO vacacionesDAO = null;
		
		BigDecimal cantidad       = null;
		BigDecimal importeGravado = null;
		BigDecimal importeExento  = null;
		
		BigDecimal importe        = null;
		BigDecimal limiteExcento = null;
		
		try {
			vacacionesDAO = new VacacionesDAO();
			listaPeriodos = vacacionesDAO.obtenerPorRfcFecha(nomina.getReceptor().getRfc(), parametros.getPeriodoFin());
			
			if(listaPeriodos.size() == 0)
				log.info("No se encontraron periodos vacacionales para el empleado.");
			
			final Date vencimientoPeriodo = this.parametros.getPeriodoFin();

			periodo = listaPeriodos.stream()
					.filter(p -> p.getPrimaPagada() == true)
					.filter(p -> p.getFechaFin().before(vencimientoPeriodo))
					.max((p1, p2) -> p1.getFechaFin().compareTo(p2.getFechaFin()))
					.get()
					;
			log.info("El ultimo periodo de vacaciones registrado es: {} al {}", periodo.getFechaInicio(), periodo.getFechaFin());
				
			importe = this.calcularImporte(periodo, nomina.getReceptor().getSalarioDiario());
			
			uma = parametros.getUma().getImporteDiario();
			limiteExcento = quince.multiply(uma).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			if(importe.compareTo(limiteExcento) > 0) {
				//La prima vacacional NO está excenta de ISR.
				importeGravado = importe.subtract(limiteExcento);
				importeExento = limiteExcento.setScale(2, BigDecimal.ROUND_HALF_UP);
			} else {
				//La prima vacacional SI está excenta de ISR.
				importeGravado = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
				importeExento = importe.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
						
		} catch(Exception ex) {
			cantidad = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
			importeGravado = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
			importeExento = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		} finally {
			percepcion = this.build(nomina, CVE_PRIMA_VACACIONES_EN_TIEMPO);
			percepcion.setCantidad(cantidad);
			percepcion.setImporteGravado(importeGravado);
			percepcion.setImporteExcento(importeExento);
		}
	}
	
	private BigDecimal calcularImporte(DetVacaciones periodo, BigDecimal salarioDiario)
	throws SGPException {
		BigDecimal importe = null;
		
		
		
		return importe;
	}

}
