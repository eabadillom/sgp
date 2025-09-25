package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.business.registro.EstatusRegistroBL;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaIncidencia;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.util.SGPException;

/**Clase para el cálculo del Bono de puntualidad.<br>
 * De acuerdo con la LSS, el bono de puntualidad no debe exceder del 10% del Salario Base de Cotización.<br>
 * En caso de exceder el 10%, se considerará como parte del SBC.
 * 
 */
public class BonoPuntualidadPBL extends PercepcionBL {
	
	private static Logger log = LogManager.getLogger(BonoPuntualidadPBL.class);
	
	private BigDecimal diasPeriodo     = null;
	
	public BonoPuntualidadPBL(ParametrosNomina parametros, DetNomina nomina) {
		super(parametros, nomina);
		this.baseCalculo     = this.nomina.getReceptor().getSalarioDiarioIntegrado();
		this.valor        = ValoresBD._1.get();
		this.diasPeriodo = new BigDecimal(this.parametros.getDiasPeriodo()).setScale(2, RoundingMode.HALF_UP);
	}
	
	@Override
	public BigDecimal calcularLimiteExento() {
		return ValoresBD._CERO.get();
	}
	
	@Override
	protected BigDecimal calcularCantidad(DetNomina nomina)
	throws SGPException {
		BigDecimal cantidad = null;
		log.info("[UI] Cantidad = Tasa Bono puntualidad x Dias Periodo = {} x {}", this.valor, this.diasPeriodo);
		cantidad = this.valor
				.multiply(diasPeriodo)
				.setScale(5, RoundingMode.HALF_UP);
		
		return cantidad ;
	}
	
	@Override
	public DetNominaPercepcion procesar(DetNomina nomina) throws SGPException {
		this.cantidad = this.calcularCantidad(nomina);
		return this.procesar(nomina, this.cantidad);
	}
	
	@Override
	public DetNominaPercepcion procesar(DetNomina nomina, BigDecimal cantidad) {
		DetNominaPercepcion percepcion = null;
		List<DetNominaIncidencia> ausencias = null;
				
		try {
			ausencias = nomina.getIncidencias().stream()
					.filter(item -> EstatusRegistroBL.AUSENCIA.equalsIgnoreCase(item.getClave()))
					.collect(Collectors.toList())
					;
			log.info("[UI] Ausencias: {}", ausencias.size());
			
			if(ausencias.size() > 0)
				throw new SGPException("[UI] El empleado tiene ausencias. No es posible asignar bono de puntualidad");
			
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
    		percepcion = this.build(nomina, CVE_BONO_PUNTUALIDAD, this.cantidad, this.importe, this.importeExento, this.importeGravado);
    	}
		
		return percepcion;
	}
}
