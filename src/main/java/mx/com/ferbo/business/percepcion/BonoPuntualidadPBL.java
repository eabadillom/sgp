package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.util.SGPException;

/**Clase para el cálculo del Bono de puntualidad.<br>
 * De acuerdo con la LSS, el bono de puntualidad no debe exceder del 10% del Salario Base de Cotización.<br>
 * En caso de exceder el 10%, se considerará como parte del SBC.
 * 
 */
public class BonoPuntualidadPBL extends AbstractPBL {
	
	private static Logger log = LogManager.getLogger(BonoPuntualidadPBL.class);
	
	
	private BigDecimal tasaBono               = null;
	private BigDecimal diasLaborales          = null;
	private BigDecimal diasNoLaborales        = null;
	private BigDecimal diasTrabajados         = null;
	
	public BonoPuntualidadPBL(ParametrosNomina parametros, DetNomina nomina) {
		super(parametros, nomina);
		this.baseCalculo     = this.nomina.getReceptor().getSalarioDiarioIntegrado();
		this.diasTrabajados = nomina.getDiasLaborados();
		this.diasLaborales  = nomina.getDiasLaborales();
		this.diasNoLaborales = nomina.getDiasNoLaborales();
		
		//TODO Reemplazar posteriormente la tasa de bono de puntualidad por algún parametro proveniente
		//de la configuración del empleado.
		this.tasaBono        = this.parametros.getBonoPuntualidad();
		
	}
	
	
	@Override
	public DetNominaPercepcion procesar(DetNomina nomina) throws SGPException {
		this.cantidad = this.calcularCantidad(nomina);
		return this.procesar(nomina, this.cantidad);
	}
	
	@Override
	public DetNominaPercepcion procesar(DetNomina nomina, BigDecimal cantidad) {
		DetNominaPercepcion percepcion = null;
		
		try {
			this.cantidad = cantidad;
			this.importe = this.calcularImporte(this.cantidad, this.baseCalculo);
			
			this.calcularExentoGravado();
			
    	} catch(Exception ex) {
    		log.warn("No es posible calcular el bono de puntualidad: {}", ex.getMessage());
    		this.cantidad       = ValoresBD._CERO.get();
    		this.importe        = ValoresBD._CERO.get();
    		this.importeExento  = ValoresBD._CERO.get();
    		this.importeGravado = ValoresBD._CERO.get();
    	} finally {
    		percepcion = this.build(nomina, CVE_BONO_PUNTUALIDAD, this.cantidad, this.importeExento, this.importeGravado);
    	}
		
		return percepcion;
	}

	@Override
	protected BigDecimal calcularCantidad(DetNomina nomina)
	throws SGPException {
		BigDecimal diasPeriodo = null;
		BigDecimal cantidad = null;
		
		diasPeriodo = this.diasTrabajados.add(this.diasNoLaborales).setScale(2, RoundingMode.HALF_UP);
		cantidad = this.tasaBono.multiply(diasPeriodo).setScale(5, RoundingMode.HALF_UP);
		
		return cantidad ;
	}

	@Override
	public BigDecimal calcularLimiteExento() {
		return ValoresBD._CERO.get();
	}
}
