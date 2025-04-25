package mx.com.ferbo.business.percepcion;

import static mx.com.ferbo.enums.ValoresBD._CERO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.dao.n.VacacionesDAO;
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

public class VacacionesReportadasPBL extends AbstractPBL {
	
	private static Logger log = LogManager.getLogger(VacacionesReportadasPBL.class);
	
	public VacacionesReportadasPBL(ParametrosNomina parametros, DetNomina nomina) {
		super(parametros, nomina);
		this.baseCalculo = nomina.getReceptor().getSalarioDiario();
	}

	@Override
	public DetNominaPercepcion procesar(DetNomina nomina) {
		DetNominaPercepcion percepcion = null;
		BigDecimal subtotal = null;
		
		VacacionesDAO vacacionesDAO = null;
		
		Date vencimientoPeriodo = null;
		List<DetVacaciones> periodos = null;
		Integer diasReportadas = null;
		
		try {
			vencimientoPeriodo = DateUtil.addMonth(this.parametros.getPeriodoFin(), -6);
			
			vacacionesDAO = new VacacionesDAO();
			periodos = vacacionesDAO.buscarReportadasPorRfcFecha(nomina.getReceptor().getRfc(), vencimientoPeriodo);
			
			for(DetVacaciones periodo : periodos) {
				if(periodo == null) {
					log.info("[UI] No se encontraron periodos vacacionales para el empleado.");
					throw new SGPException("No se encontraron periodos vacacionales reportados para el empleado.");
				}
				
				log.info("[UI] Periodo: {} al {}, vencimiento del periodo vacacional reportado: {}",
						DateUtil.getString(periodo.getFechaInicio(), DateUtil.FORMATO_DD_MM_YYYY),
						DateUtil.getString(periodo.getFechaFin(), DateUtil.FORMATO_DD_MM_YYYY),
						DateUtil.getString(vencimientoPeriodo, DateUtil.FORMATO_DD_MM_YYYY)
						);
				
				diasReportadas = periodo.getDiasTotales() - periodo.getDiasTomados();
				
				cantidad = cantidad.add(new BigDecimal(diasReportadas).setScale(2, BigDecimal.ROUND_HALF_UP));
				subtotal = this.calcularImporte(periodo, nomina.getReceptor().getSalarioDiario());
				importe = importe.add(subtotal);
				
				log.info("[UI] Importe gravado al 100%");
				log.info("[UI] Importe exento = {}, Importe gravado = {}", importeExento, importe);
				
				this.agregarPeriodo(nomina, periodo);
			}
			
			this.calcularExentoGravado();
			
		} catch(Exception ex) {
			cantidad       = _CERO.get();
			importeGravado = _CERO.get();
			importeExento  = _CERO.get();
		} finally {
			percepcion = this.build(nomina, CVE_VACACIONES_REPORTADAS, cantidad, importeExento, importeGravado);
		}
		
		return percepcion;
	}
	
	private BigDecimal calcularImporte(DetVacaciones periodo, BigDecimal salarioDiario) throws SGPException {
		BigDecimal importe = null;
		BigDecimal diasTotales = null;
		BigDecimal diasTomados = null;
		BigDecimal diasPendientes = null;
		
		diasTotales = new BigDecimal(periodo.getDiasTotales()).setScale(2, BigDecimal.ROUND_HALF_UP);
		diasTomados = new BigDecimal(periodo.getDiasTomados()).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		diasPendientes = diasTotales.subtract(diasTomados);
		
		importe = diasPendientes
				.multiply(salarioDiario)
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		log.info("[UI] Periodo vacacional: {} al {} - Días no disfrutados: {}, Importe = {} * {}",
				DateUtil.getString(periodo.getFechaInicio(), DateUtil.FORMATO_DD_MM_YYYY),
				DateUtil.getString(periodo.getFechaFin(), DateUtil.FORMATO_DD_MM_YYYY),
				diasPendientes, salarioDiario, diasPendientes);
		
		return importe;
	}
	
	@Override
	protected BigDecimal calcularCantidad(DetNomina nomina) throws SGPException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void agregarPeriodo(DetNomina nomina, DetVacaciones periodo) {
		if(nomina.getVacaciones() == null)
			nomina.setVacaciones(new ArrayList<DetVacaciones>());
		
		if(nomina.getVacaciones().contains(periodo)) {
			int index = nomina.getVacaciones().indexOf(periodo);
			periodo = nomina.getVacaciones().get(index);
		} else {
			nomina.getVacaciones().add(periodo);
		}
		
		periodo.setDiasPendientesPagados(true);
	}

	@Override
	protected BigDecimal calcularLimiteExento() {
		return _CERO.get();
	}
}
