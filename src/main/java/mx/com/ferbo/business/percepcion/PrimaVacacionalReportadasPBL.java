package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.dao.n.VacacionesDAO;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetVacaciones;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

public class PrimaVacacionalReportadasPBL extends AbstractPBL {
	
	private static Logger log = LogManager.getLogger(PrimaVacacionalReportadasPBL.class);
	
	public PrimaVacacionalReportadasPBL(ParametrosNomina parametros, DetNomina nomina) {
		super(parametros, nomina);
		this.baseCalculo = nomina.getReceptor().getSalarioDiario();
	}
	
	@Override
	public DetNominaPercepcion procesar(DetNomina nomina) {
		DetNominaPercepcion       percepcion    = null;
		
		try {
			this.cantidad = this.calcularCantidad(nomina);
			this.importe = this.calcularImporte(cantidad, baseCalculo);
			
			this.calcularExentoGravado();
			
			log.info("[UI] Limite exento = {} * {} = {}", ValoresBD._15.get(), parametros.getUma().getImporteDiario(), limiteExento);
			log.info("[UI] Importe exento = {}, Importe gravado = {}", importeExento, importeGravado);
			
		} catch(SGPException ex) {
			log.warn("[UI] {}", ex.getMessage());
			cantidad = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
			importeGravado = ValoresBD._CERO.get();
			importeExento = ValoresBD._CERO.get();
		} catch(Exception ex) {
			log.error("Problema para generar la percepción...", ex);
			cantidad = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
			importeGravado = ValoresBD._CERO.get();
			importeExento = ValoresBD._CERO.get();
		} finally {
			percepcion = this.build(nomina, CVE_PRIMA_VACACIONES_REPORTADAS, cantidad, importeExento, importeGravado);
			log.info("Percepcion agregada: {}", percepcion);
		}
		
		return percepcion;
	}
	
	@Override
	protected BigDecimal calcularCantidad(DetNomina nomina) throws SGPException {
		List<DetVacaciones> periodos           = null;
		VacacionesDAO       vacacionesDAO      = null;
		Date                vencimientoPeriodo = null;
		
		BigDecimal          tasa               = null;
		BigDecimal          cantidad           = null;
		
		vencimientoPeriodo = DateUtil.addMonth(parametros.getPeriodoFin(), -6);
		vacacionesDAO = new VacacionesDAO();
		periodos = vacacionesDAO.buscarReportadasPorRfcFecha(nomina.getReceptor().getRfc(), vencimientoPeriodo);
		
		cantidad = ValoresBD._CERO.get();
		
		for(DetVacaciones periodo : periodos) {
			if(periodo == null) {
				log.info("No se encontraron periodos vacacionales para el empleado.");
				throw new SGPException("No se encontraron periodos vacacionales reportados para el empleado.");
			}
			
			log.info("[UI] Periodo: {} al {}, vencimiento del periodo vacacional reportado: {}",
					DateUtil.getString(periodo.getFechaInicio(), DateUtil.FORMATO_DD_MM_YYYY),
					DateUtil.getString(periodo.getFechaFin(), DateUtil.FORMATO_DD_MM_YYYY),
					DateUtil.getString(vencimientoPeriodo, DateUtil.FORMATO_DD_MM_YYYY)
					);
			
			tasa = this.calcularTasa(periodo, periodo.getEmpleado().getDatoEmpresa().getSalarioDiario());
			cantidad = cantidad.add(tasa);
			
			this.agregarPeriodo(nomina, periodo);
		}
		
		
		
		return cantidad;
	}
	
	private BigDecimal calcularTasa(DetVacaciones periodo, BigDecimal salarioDiario) {
		BigDecimal tasa            = null;
		BigDecimal primaVacacional = null;
		BigDecimal diasTotales     = null;
		BigDecimal diasTomados     = null;
		BigDecimal dias = null;
		
		primaVacacional = periodo.getEmpleado().getDatoEmpresa().getPrimaVacacional();
		primaVacacional = primaVacacional.divide(ValoresBD._100.get(), 4, BigDecimal.ROUND_HALF_UP);
		
		diasTotales = new BigDecimal(periodo.getDiasTotales()).setScale(2, BigDecimal.ROUND_HALF_UP);
		diasTomados = new BigDecimal(periodo.getDiasTomados()).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		dias = diasTotales.subtract(diasTomados).setScale(2, BigDecimal.ROUND_HALF_UP);
		tasa = primaVacacional.multiply(dias).setScale(2, BigDecimal.ROUND_HALF_UP);
		log.info("[UI] Tasa prima vacacional = {} * {}", primaVacacional, dias);
		
		return tasa;
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
		
		periodo.setPrimaPagada(true);
		
	}

	@Override
	public BigDecimal calcularLimiteExento() {
		BigDecimal uma = this.parametros.getUma().getImporteDiario();
		
		return ValoresBD._15.get()
				.multiply(uma)
				.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
}
