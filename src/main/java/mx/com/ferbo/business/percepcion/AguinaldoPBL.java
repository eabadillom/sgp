package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.enums.ValoresBD;

import static mx.com.ferbo.enums.ValoresBD.*;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.util.DateUtil;

public class AguinaldoPBL extends AbstractPBL {
	
	private static Logger log = LogManager.getLogger(AguinaldoPBL.class);
	
	private BigDecimal ausencias = null;
	
	
	public AguinaldoPBL(ParametrosNomina parametros, DetNomina nomina) {
		super(parametros, nomina);
		this.ausencias = _CERO.get();
		
		this.cantidad       = ValoresBD._CERO.get();
		this.importe        = ValoresBD._CERO.get();
		this.importeExento  = ValoresBD._CERO.get();
		this.importeGravado = ValoresBD._CERO.get();
	}
	
	@Override
	public BigDecimal calcularLimiteExento() {
		BigDecimal uma = parametros.getUma().getImporteDiario();
		return _30.get().multiply(uma).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	@Override
	public DetNominaPercepcion procesar(DetNomina nomina) {
		DetNominaPercepcion percepcion          = null;
		
		try {
			this.baseCalculo = nomina.getReceptor().getSalarioDiario();
			
			this.cantidad = this.calcularCantidad(nomina);
			this.importe  = this.calcularImporte(this.cantidad, this.baseCalculo);
			
			log.info("[UI] Importe aguinaldo = Proporcion aguinaldo * salario diario = {} * {}",
					this.cantidad, this.baseCalculo);
			
			this.calcularExentoGravado();
			
			log.info("[UI] Limite exento = {} * {} = {}", _30.get(), parametros.getUma().getImporteDiario(), limiteExento);
			log.info("[UI] Importe exento = {}, Importe gravado = {}", importeExento, importeGravado);
			
		} catch(Exception ex) {
			this.cantidad       = _CERO.get();
			this.importeExento  = _CERO.get();
			this.importeGravado = _CERO.get();
		} finally {
			percepcion = this.build(nomina, CVE_AGUINALDO, cantidad, importeExento, importeGravado);
			log.info("Percepcion agregada: {}", percepcion);
		}
		
		return percepcion;
	}

	@Override
	protected BigDecimal calcularCantidad(DetNomina nomina) {
		Date                fechaIngreso        = null;
		
		Integer             iDiasPeriodo        = null;
		BigDecimal          diasPeriodo         = null;
		BigDecimal          diasTrabajados      = null;
		BigDecimal          proporcionAguinaldo = null;
		
		//TODO Falta determinar las ausencias del año para el trabajador.
		
		fechaIngreso = nomina.getReceptor().getInicioRelacionLaboral();
		
		if(fechaIngreso.compareTo(this.parametros.getFechaInicioAnio()) <= 0) {
			iDiasPeriodo = DateUtil.daysDiff(parametros.getFechaInicioAnio(), parametros.getFechaFinAnio()) - 1;
		} else {
			iDiasPeriodo = DateUtil.daysDiff(fechaIngreso, parametros.getFechaFinAnio());
		}
		
		diasPeriodo = new BigDecimal(iDiasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		diasTrabajados = diasPeriodo.subtract(ausencias)
				.setScale(2, BigDecimal.ROUND_HALF_UP)
				.divide(_DIAS_ANIO.get(), 2, BigDecimal.ROUND_HALF_UP);
		
		log.info("[UI] dias trabajados = (dias del periodo - ausencias) / 365 = ({} - {}) / 365", diasPeriodo, ausencias);
		
		proporcionAguinaldo = diasTrabajados
				.multiply(nomina.getReceptor().getDiasAguinaldo())
				.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		log.info("[UI] Proporcion aguinaldo = dias trabajados * dias de aguinaldo = {} * {}", diasTrabajados, nomina.getReceptor().getDiasAguinaldo());
		
		cantidad = proporcionAguinaldo.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		return cantidad;
	}
}
