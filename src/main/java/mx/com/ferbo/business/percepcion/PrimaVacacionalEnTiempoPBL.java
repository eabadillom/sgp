package mx.com.ferbo.business.percepcion;

import static mx.com.ferbo.enums.ValoresBD._100;
import static mx.com.ferbo.enums.ValoresBD._15;
import static mx.com.ferbo.enums.ValoresBD._CERO;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.dao.n.VacacionesDAO;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetVacaciones;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.SGPException;

public class PrimaVacacionalEnTiempoPBL extends AbstractPBL {
	
	private static Logger log = LogManager.getLogger(PrimaVacacionalEnTiempoPBL.class);
	
	public PrimaVacacionalEnTiempoPBL(ParametrosNomina parametros, DetNomina nomina) {
		super(parametros, nomina);
		this.baseCalculo = nomina.getReceptor().getSalarioDiario();
	}

	@Override
	public DetNominaPercepcion procesar(DetNomina nomina) {
		DetNominaPercepcion       percepcion    = null;
		
		try {
			
			this.cantidad = this.calcularCantidad(nomina);
			this.importe = this.calcularImporte(cantidad, this.baseCalculo);
			
			this.calcularExentoGravado();
			
			log.info("[UI] Limite exento = {} * {} = {}", _15.get(), parametros.getUma().getImporteDiario(), limiteExento);
			log.info("[UI] Importe exento = {}, Importe gravado = {}", importeExento, importeGravado);
			
		} catch(SGPException ex) {
			log.warn("[UI] {}", ex.getMessage());
			cantidad       = _CERO.get();
			importeGravado = _CERO.get();
			importeExento  = _CERO.get();
			
		} catch(Exception ex) {
			
			log.error("Problema para generar la percepción...", ex);
			cantidad       = _CERO.get();
			importeGravado = _CERO.get();
			importeExento  = _CERO.get();
			
		} finally {
			
			percepcion = this.build(nomina, CVE_PRIMA_VACACIONES_EN_TIEMPO, cantidad, importeExento, importeGravado);
			log.info("Percepcion agregada: {}", percepcion);
			
		}
		
		return percepcion;
	}
	
	@Override
	protected BigDecimal calcularCantidad(DetNomina nomina) throws SGPException {
		VacacionesDAO             vacacionesDAO = null;
		DetVacaciones             periodo       = null;
		Integer diferenciaEnDias  = null;
		
		vacacionesDAO = new VacacionesDAO();
		periodo = vacacionesDAO.obtenerPorRfcFecha(nomina.getReceptor().getRfc(), this.parametros.getPeriodoFin());
		
		if(periodo == null) {
			log.info("[UI] No se encontraron periodos vacacionales para el empleado.");
			throw new SGPException("El empleado no ha cumplido su aniversario laboral.");
		}
		log.info("[UI] El ultimo periodo de vacaciones registrado es: {}", periodo);
		
		diferenciaEnDias = DateUtil.daysDiff(periodo.getFechaFin(), this.parametros.getPeriodoFin());
		
		if(diferenciaEnDias > 365)
			throw new SGPException("El periodo encontrado tiene más de un año respecto al fin del periodo de cálculo. "
					+ "Se recomienda revisar los periodos vacacionales del empleado.");
		
		periodo.setPrimaPagada(true);
		nomina.setVacaciones(new ArrayList<DetVacaciones>());
		nomina.getVacaciones().add(periodo);
		
		return this.calcularTasa(periodo, this.baseCalculo);
	}
	
	private BigDecimal calcularTasa(DetVacaciones periodo, BigDecimal salarioDiario) {
		BigDecimal tasa            = null;
		BigDecimal primaVacacional = null;
		BigDecimal diasTotales     = null;
		
		primaVacacional = periodo.getEmpleado().getDatoEmpresa().getPrimaVacacional();
		primaVacacional = primaVacacional.divide(_100.get(), 4, BigDecimal.ROUND_HALF_UP);
		
		diasTotales = new BigDecimal(periodo.getDiasTotales()).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		tasa = primaVacacional.multiply(diasTotales).setScale(2, BigDecimal.ROUND_HALF_UP);
		log.info("[UI] Tasa prima vacacional = {} * {}", primaVacacional, diasTotales);
		
		return tasa;
	}
	
	@Override
	public BigDecimal calcularLimiteExento() {
		return _15.get()
				.multiply(this.parametros.getUma().getImporteDiario())
				.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
}
