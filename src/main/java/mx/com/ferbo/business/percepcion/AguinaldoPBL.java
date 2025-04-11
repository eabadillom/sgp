package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.util.DateUtil;

public class AguinaldoPBL extends AbstractPBL implements IPercepcion {
	
	private static Logger log = LogManager.getLogger(AguinaldoPBL.class);
	
	private ParametrosNomina parametros = null;
	private BigDecimal diasAguinaldo = null;
	private BigDecimal ausencias = null;
	
	
	public AguinaldoPBL(ParametrosNomina parametros, BigDecimal diasAguinaldo, BigDecimal ausencias) {
		this.parametros = parametros;
		this.tiposPercepcion = parametros.getTiposPercepcion();
		this.diasAguinaldo = diasAguinaldo;
		this.ausencias = ausencias;
	}

	@Override
	public DetNominaPercepcion calcular(DetNomina nomina) {
		DetNominaPercepcion percepcion          = null;
		Date                fechaIngreso        = null;
		BigDecimal          salarioDiario       = null;
		Integer             iDiasPeriodo        = null;
		BigDecimal          diasPeriodo         = null;
		BigDecimal          diasTrabajados      = null;
		BigDecimal          proporcionAguinaldo = null;
		BigDecimal          aguinaldo           = null;
		BigDecimal          cantidad            = null;
		BigDecimal          importeExento       = null;
		BigDecimal          importeGravado      = null;
		BigDecimal          limiteExento        = null;
		BigDecimal          uma                 = null;
		
		try {
			fechaIngreso = nomina.getReceptor().getInicioRelacionLaboral();
			
			salarioDiario = nomina.getReceptor().getSalarioDiario();
			
			if(fechaIngreso.compareTo(this.parametros.getFechaInicioAnio()) <= 0) {
				iDiasPeriodo = DateUtil.daysDiff(parametros.getFechaInicioAnio(), parametros.getFechaFinAnio());
			} else {
				iDiasPeriodo = DateUtil.daysDiff(fechaIngreso, parametros.getFechaFinAnio());
			}
			
			diasPeriodo = new BigDecimal(iDiasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			diasTrabajados = diasPeriodo.subtract(ausencias)
					.setScale(2, BigDecimal.ROUND_HALF_UP)
					.divide(ValoresBD._DIAS_ANIO.get(), 2, BigDecimal.ROUND_HALF_UP);
			
			proporcionAguinaldo = diasTrabajados
					.multiply(diasAguinaldo)
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			
			aguinaldo = proporcionAguinaldo
					.multiply(salarioDiario).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			uma = parametros.getUma().getImporteDiario();
			
			limiteExento = ValoresBD._30.get()
					.multiply(uma).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			if(aguinaldo.compareTo(limiteExento) > 0) {
				//El importe de aguinaldo NO está exento de ISR.
				importeGravado = aguinaldo.subtract(limiteExento);
				importeExento = limiteExento.setScale(2, BigDecimal.ROUND_HALF_UP);
			} else {
				//El importe de aguinaldo SI está exento de ISR.
				importeGravado = ValoresBD._CERO.get();
				importeExento = aguinaldo.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			
			cantidad = proporcionAguinaldo.setScale(2, BigDecimal.ROUND_HALF_UP);
			
			log.info("[UI] Limite exento = {} * {} = {}", ValoresBD._30.get(), uma, limiteExento);
			log.info("[UI] Importe exento = {}, Importe gravado = {}", importeExento, importeGravado);
			
		} catch(Exception ex) {
			cantidad = ValoresBD._CERO.get();
			importeExento = ValoresBD._CERO.get();
			importeGravado = ValoresBD._CERO.get();
		} finally {
			percepcion = this.build(nomina, CVE_AGUINALDO, cantidad, importeExento, importeGravado);
			log.info("Percepcion agregada: {}", percepcion);
		}
		
		return percepcion;
	}

}
