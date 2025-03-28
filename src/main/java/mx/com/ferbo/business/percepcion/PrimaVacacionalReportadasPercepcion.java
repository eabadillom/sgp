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

public class PrimaVacacionalReportadasPercepcion extends AbstractPercepcion implements IPercepcion {
	
	private static Logger log = LogManager.getLogger(PrimaVacacionalReportadasPercepcion.class);
	
	private ParametrosNomina parametros = null;
	
	public PrimaVacacionalReportadasPercepcion(ParametrosNomina parametros) {
		this.parametros = parametros;
	}
	
	@Override
	public DetNominaPercepcion calcular(DetNomina nomina) {
		DetNominaPercepcion       percepcion    = null;
		List<DetVacaciones>       periodos      = null;
		VacacionesDAO             vacacionesDAO = null;
		
		BigDecimal uma            = null;
		BigDecimal cantidad       = null;
		BigDecimal limiteExento   = null;
		BigDecimal importeGravado = null;
		BigDecimal importeExento  = null;
		BigDecimal importe        = null;
		BigDecimal tasa           = null;
		
		Date vencimientoPeriodo = null;
		
		try {
			vencimientoPeriodo = DateUtil.addMonth(parametros.getPeriodoFin(), -6);
			
			vacacionesDAO = new VacacionesDAO();
			
			periodos = vacacionesDAO.buscarReportadasPorRfcFecha(nomina.getReceptor().getRfc(), vencimientoPeriodo);
			
			cantidad       = ValoresBD.CERO.getValor();
			importe        = ValoresBD.CERO.getValor();
			importeExento  = ValoresBD.CERO.getValor();
			importeGravado = ValoresBD.CERO.getValor();
			
			for(DetVacaciones periodo : periodos) {
				if(periodo == null) {
					log.info("No se encontraron periodos vacacionales para el empleado.");
					throw new SGPException("No se encontraron periodos vacacionales reportados para el empleado.");
				}
				
				log.info("Periodo: {} al {}, vencimiento del periodo vacacional reportado: {}",
						DateUtil.getString(periodo.getFechaInicio(), DateUtil.FORMATO_DD_MM_YYYY),
						DateUtil.getString(periodo.getFechaFin(), DateUtil.FORMATO_DD_MM_YYYY),
						DateUtil.getString(vencimientoPeriodo, DateUtil.FORMATO_DD_MM_YYYY)
						);
				
				tasa = this.calcularTasa(periodo, periodo.getEmpleado().getDatoEmpresa().getSalarioDiario());
				cantidad = cantidad.add(tasa);
				importe = importe.add(this.calcularImporte(nomina.getReceptor().getSalarioDiario(), cantidad));
				
				if(nomina.getVacaciones() == null)
					nomina.setVacaciones(new ArrayList<DetVacaciones>());
				
				periodo.setDiasTomados(periodo.getDiasTotales());
				periodo.setDiasPendientesPagados(true);
				nomina.getVacaciones().add(periodo);
			}
			
			uma = parametros.getUma().getImporteDiario();
			
			limiteExento = ValoresBD.QUINCE.getValor().multiply(uma).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			if(importe.compareTo(limiteExento) > 0) {
				//La prima vacacional NO está excenta de ISR.
				importeGravado = importe.subtract(limiteExento);
				importeExento = limiteExento.setScale(2, BigDecimal.ROUND_HALF_UP);
			} else {
				//La prima vacacional SI está excenta de ISR.
				importeGravado = ValoresBD.CERO.getValor();
				importeExento = importe.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			
		} catch(Exception ex) {
			cantidad = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
			importeGravado = ValoresBD.CERO.getValor();
			importeExento = ValoresBD.CERO.getValor();
		} finally {
			percepcion = this.build(nomina, CVE_PRIMA_VACACIONES_REPORTADAS, cantidad, importeExento, importeGravado);
		}
		
		return percepcion;
	}
	
	private BigDecimal calcularTasa(DetVacaciones periodo, BigDecimal salarioDiario) {
		BigDecimal tasa            = null;
		BigDecimal primaVacacional = null;
		BigDecimal diasTotales     = null;
		BigDecimal diasTomados     = null;
		BigDecimal dias = null;
		
		primaVacacional = periodo.getEmpleado().getDatoEmpresa().getPrimaVacacional();
		primaVacacional = primaVacacional.divide(ValoresBD.CIEN.getValor(), 4, BigDecimal.ROUND_HALF_UP);
		
		diasTotales = new BigDecimal(periodo.getDiasTotales()).setScale(2, BigDecimal.ROUND_HALF_UP);
		diasTomados = new BigDecimal(periodo.getDiasTomados()).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		dias = diasTotales.subtract(diasTomados).setScale(2, BigDecimal.ROUND_HALF_UP);
		tasa = primaVacacional.multiply(dias).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		return tasa;
	}
	
	private BigDecimal calcularImporte(BigDecimal salarioDiario, BigDecimal tasa) {
		BigDecimal importe = null;
		
		try {
			importe = salarioDiario.multiply(tasa).setScale(2, BigDecimal.ROUND_HALF_UP);
		} catch(Exception ex) {
			importe = ValoresBD.CERO.getValor();
		}
		
		return importe;
	}

}
