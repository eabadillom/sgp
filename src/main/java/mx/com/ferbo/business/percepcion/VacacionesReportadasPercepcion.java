package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.VacacionesDAO;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetVacaciones;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

/* Para el cálculo de las "vacaciones reportadas" (no disfrutadas) se debe considerar
 * lo siguiente:<br>
 * Inicio periodo: La fecha en que inicia un periodo n.
 * Fin periodo: La fecha en que termina un periodo n.
 * Vencimiento: conforme a la LFT, arts. 76 al 81. Al término de un periodo, el colaborador<br>
 * tiene derecho a disfrutar de sus días de vacaciones y al vencimiento de 6 meses posteriores<br>
 * al finalizar dicho periodo, tiene derecho a exigir el pago de las vacaciones no disfrutadas.
 * */

public class VacacionesReportadasPercepcion extends AbstractPercepcion implements IPercepcion {
	
	private static Logger log = LogManager.getLogger(VacacionesReportadasPercepcion.class);
	private ParametrosNomina parametros = null;
	
	public VacacionesReportadasPercepcion(ParametrosNomina parametros) {
		this.tiposPercepcion = parametros.getTiposPercepcion();
		this.parametros = parametros;
	}

	@Override
	public void calcular(DetNomina nomina) {
		DetNominaPercepcion percepcion = null;
		List<DetNominaPercepcion> percepciones = null;
		BigDecimal cantidad = null;
		BigDecimal importeGravado = null;
		BigDecimal importeExento = null;
		
		DetEmpleado empleado = null;
		EmpleadoDAO empleadoDAO = null;
		VacacionesDAO vacacionesDAO = null;
		
		Date vencimientoPeriodo = null;
		List<DetVacaciones> listaPeriodos = null;
		
		try {
			empleadoDAO = new EmpleadoDAO();
			empleado = empleadoDAO.buscarPorRFC(nomina.getReceptor().getRfc());
			vacacionesDAO = new VacacionesDAO();
			listaPeriodos = vacacionesDAO.obtenerPorRfcFecha(nomina.getReceptor().getRfc(), parametros.getPeriodoFin());
			
			if(listaPeriodos.size() == 0)
				log.info("No se encontraron periodos vacacionales para el empleado.");
			
			for(DetVacaciones periodo : listaPeriodos) {
				vencimientoPeriodo = DateUtil.addMonth(periodo.getFechaFin(), 6);
				
				log.info("Periodo: {} al {}, vencimiento del periodo vacacional: {}",
						periodo.getFechaInicio(), periodo.getFechaFin(), vencimientoPeriodo);
				
				this.calcularImporte(periodo, nomina.getReceptor().getSalarioDiario());
			}
			
		} catch(Exception ex) {
			cantidad = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
			importeGravado = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
			importeExento = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		} finally {
			percepcion = this.build(nomina, CVE_VACACIONES_REPORTADAS);
			percepcion.setCantidad(cantidad);
			percepcion.setImporteGravado(importeGravado);
			percepcion.setImporteExcento(importeExento);
		}
	}

	private BigDecimal calcularImporte(DetVacaciones periodo, BigDecimal salarioDiario) throws SGPException {
		BigDecimal importe = null;
		BigDecimal diasTotales = null;
		BigDecimal diasTomados = null;
		BigDecimal diasPendientes = null;
		
		diasTotales = new BigDecimal(periodo.getDiasTotales()).setScale(2, BigDecimal.ROUND_HALF_UP);
		diasTomados = new BigDecimal(periodo.getDiasTomados()).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		diasPendientes = diasTotales.subtract(diasTomados);
		
		log.info("Periodo vacacional: {} al {} - Días no disfrutados: {}",
				DateUtil.getString(periodo.getFechaInicio(), DateUtil.FORMATO_DD_MM_YYYY),
				DateUtil.getString(periodo.getFechaFin(), DateUtil.FORMATO_DD_MM_YYYY),
				diasPendientes);
		
		importe = diasPendientes.multiply(salarioDiario);
		
		return importe;
	}

}
