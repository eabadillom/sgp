package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static mx.com.ferbo.enums.ValoresBD.*;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetPercepcionEmpleado;
import mx.com.ferbo.util.SGPException;

/** Esta implementación del cálculo de vales de despensa tiene como base la UMA, para determinar de manera
 * general a todos los trabajadores la misma cantidad otorgada como apoyo de vales de despensa.
 * Se otorga el 40% de la UMA diaria, por cada día trabajado de la semana + el proporcional del septimo día.
*
*/
public class ValesDespensaPBL extends AbstractPBL {
	
	private static Logger log = LogManager.getLogger(ValesDespensaPBL.class);
	
	private BigDecimal diasTrabajados = null;
	private BigDecimal uma = null;
	private BigDecimal tasaVales = null;
	private BigDecimal diasPeriodo = null;
	
	public ValesDespensaPBL(ParametrosNomina parametros, BigDecimal diasTrabajados, BigDecimal uma, BigDecimal tasaVales, BigDecimal diasPeriodo) {
		super(parametros);
		this.diasTrabajados = diasTrabajados;
		this.uma = uma;
		this.tasaVales = tasaVales;
		this.diasPeriodo = diasPeriodo;
	}
	
	@Override
	public DetNominaPercepcion procesar(DetNomina nomina) {
		DetNominaPercepcion percepcion = null;
		DetPercepcionEmpleado percepcionEmpleado = null;
		
		try {
			this.baseCalculo = this.uma;
			
			/* El importe de los vales de despensa estarán excluidos del
			 * cálculo del Salario Base de Cotización (IMSS) siempre y cuando
			 * no superen el 40% de la UMA mensual (se debe revisar el ajuste
			 * a UMA diaria o semanal, para una correcta aplicación del
			 * criterio).
			 * 
			 * En caso de exceder el valor deL 40% de la UMA, la diferencia
			 * se calculará de manera diaria y se sumará al SBC.
			 * */
			//TODO Pendiente aplicar criterio de exención para SBC.
			
			
			//Cálculo de importes exento y gravado (para LISR, Art. 93, parrafo penultimo).
    		if(this.diasTrabajados.compareTo(_CERO.get()) == 0)
    			throw new SGPException("No es posible asignar vales de despensa.");
    		
//    		importe = uma.multiply(tasaVales).setScale(4, BigDecimal.ROUND_HALF_UP);
//    		importe = importe.multiply(diasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
    		this.cantidad = this.calcularCantidad(nomina);
    		this.importe = this.calcularImporte(this.cantidad, this.baseCalculo);
    		
			percepcionEmpleado = this.buscaPercepcionEmpleado(P_VALES_DESPENSA);
    		
    		if(    (percepcionEmpleado != null) 
    			&& (percepcionEmpleado.getActivo())
				&& (percepcionEmpleado.getImporteMaximo() != null)
				&& (importe.compareTo(percepcionEmpleado.getImporteMaximo()) > 0) ) {
    			
    			importe = percepcionEmpleado.getImporteMaximo();
    		}
    		
    		this.calcularExentoGravado();
    		
		} catch(SGPException ex){
			log.warn("No es posible calcular los vales de despensa: {}", ex.getMessage());
			importeExento = _CERO.get();
			importeGravado = _CERO.get();
    	} catch(Exception ex) {
    		log.error("No es posible calcular los vales de despensa...", ex);
    		importeExento = ValoresBD._CERO.get();
			importeGravado = ValoresBD._CERO.get();
    	} finally {
    		percepcion = this.build(nomina, CVE_VALES_DESPENSA, _CERO.get(), importeExento, importeGravado);
    	}
		
		return percepcion;
	}
	
	@Override
	protected BigDecimal calcularCantidad(DetNomina nomina) throws SGPException {
		return this.tasaVales.setScale(4, BigDecimal.ROUND_HALF_UP)
				.multiply(this.diasPeriodo);
	}

	@Override
	public BigDecimal calcularLimiteExento() {
		/* Los vales de despensa están exentos de ISR hasta por 7 veces la UMA diaria.
		 */
		return this.uma
				.multiply(_7.get())
				.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
}
