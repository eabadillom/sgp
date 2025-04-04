package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.util.ArrayList;

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

public class PrimaVacacionalEnTiempoPBL extends AbstractPBL implements IPercepcion {
	
	private static Logger log = LogManager.getLogger(PrimaVacacionalEnTiempoPBL.class);
	
	private ParametrosNomina parametros = null;
	
	public PrimaVacacionalEnTiempoPBL(ParametrosNomina parametros) {
		this.parametros = parametros;
	}

	@Override
	public DetNominaPercepcion calcular(DetNomina nomina) {
		
		DetNominaPercepcion       percepcion    = null;
		DetVacaciones             periodo       = null;
		VacacionesDAO             vacacionesDAO = null;
		
		BigDecimal uma            = null;
		BigDecimal cantidad       = null;
		BigDecimal limiteExento   = null;
		BigDecimal importeGravado = null;
		BigDecimal importeExento  = null;
		BigDecimal importe        = null;
		
		Integer diferenciaEnDias  = null;
		
		try {
			vacacionesDAO = new VacacionesDAO();
			periodo = vacacionesDAO.obtenerPorRfcFecha(nomina.getReceptor().getRfc(), parametros.getPeriodoFin());
			
			if(periodo == null) {
				log.info("No se encontraron periodos vacacionales para el empleado.");
				throw new SGPException("El empleado no ha cumplido su aniversario laboral.");
			}
			
			log.info("El ultimo periodo de vacaciones registrado es: {}", periodo);
			
			diferenciaEnDias = DateUtil.daysDiff(periodo.getFechaFin(), parametros.getPeriodoFin());
			
			if(diferenciaEnDias > 365)
				throw new SGPException("El periodo encontrado tiene más de un año respecto al fin del periodo de cálculo. "
						+ "Se recomienda revisar los periodos vacacionales del empleado.");
			
			cantidad = this.calcularTasa(periodo, periodo.getEmpleado().getDatoEmpresa().getSalarioDiario());
			importe = this.calcularImporte(nomina.getReceptor().getSalarioDiario(), cantidad);
			
			uma = parametros.getUma().getImporteDiario();
			
			limiteExento = ValoresBD._15.get().multiply(uma).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			if(importe.compareTo(limiteExento) > 0) {
				//La prima vacacional NO está exenta de ISR.
				importeGravado = importe.subtract(limiteExento);
				importeExento = limiteExento.setScale(2, BigDecimal.ROUND_HALF_UP);
			} else {
				//La prima vacacional SI está exenta de ISR.
				importeGravado = ValoresBD._CERO.get();
				importeExento = importe.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			
			periodo.setPrimaPagada(true);
			nomina.setVacaciones(new ArrayList<DetVacaciones>());
			nomina.getVacaciones().add(periodo);
			
		} catch(Exception ex) {
			cantidad = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
			importeGravado = ValoresBD._CERO.get();
			importeExento = ValoresBD._CERO.get();
		} finally {
			percepcion = this.build(nomina, CVE_PRIMA_VACACIONES_EN_TIEMPO, cantidad, importeExento, importeGravado);
			log.info("Percepcion agregada: {}", percepcion);
		}
		
		return percepcion;
	}
	
	private BigDecimal calcularTasa(DetVacaciones periodo, BigDecimal salarioDiario) {
		BigDecimal tasa            = null;
		BigDecimal primaVacacional = null;
		BigDecimal diasTotales     = null;
		
		primaVacacional = periodo.getEmpleado().getDatoEmpresa().getPrimaVacacional();
		primaVacacional = primaVacacional.divide(ValoresBD._100.get(), 4, BigDecimal.ROUND_HALF_UP);
		
		diasTotales = new BigDecimal(periodo.getDiasTotales()).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		tasa = primaVacacional.multiply(diasTotales).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		return tasa;
	}
	
	private BigDecimal calcularImporte(BigDecimal salarioDiario, BigDecimal tasa) {
		BigDecimal importe = null;
		
		try {
			importe = salarioDiario.multiply(tasa).setScale(2, BigDecimal.ROUND_HALF_UP);
		} catch(Exception ex) {
			importe = ValoresBD._CERO.get();
		}
		
		return importe;
	}

}
