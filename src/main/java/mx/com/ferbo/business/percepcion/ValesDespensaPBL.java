package mx.com.ferbo.business.percepcion;

import static mx.com.ferbo.enums.ValoresBD._7;
import static mx.com.ferbo.enums.ValoresBD._CERO;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.util.SGPException;

/** Esta implementación del cálculo de vales de despensa tiene como base la UMA, para determinar de manera
 * general a todos los trabajadores la misma cantidad otorgada como apoyo de vales de despensa.
 * Se otorga el 40% de la UMA diaria, por cada día trabajado de la semana + el proporcional del septimo día.
*
*/
public class ValesDespensaPBL extends AbstractPBL {
	
	private static Logger log = LogManager.getLogger(ValesDespensaPBL.class);
	
	private BigDecimal diasTrabajados = null;
	private BigDecimal tasaVales = null;
	private BigDecimal diasPeriodo = null;
	
	public ValesDespensaPBL(ParametrosNomina parametros, DetNomina nomina) {
		super(parametros, nomina);
		this.baseCalculo = parametros.getUma().getImporteDiario();
		this.diasTrabajados = nomina.getDiasLaborados();
		this.diasPeriodo = new BigDecimal(parametros.getDiasPeriodo()).setScale(2, RoundingMode.HALF_UP);
		//TODO La tasa de vales de despensa debe ser una propiedad que está configurada para el empleado,
		//no en los parametros de la nómina.
		this.tasaVales = parametros.getValeDespensa();
	}
	
	@Override
	public DetNominaPercepcion procesar(DetNomina nomina) throws SGPException {
		DetNominaPercepcion percepcion = null;
		
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
			throw new SGPException("No es posible asignar vales de despensa porque el empleado no tiene asistencia en el periodo.");
		
		this.cantidad = this.calcularCantidad(nomina);
		percepcion = this.procesar(nomina, this.cantidad);
		
		return percepcion;
	}
	
	@Override
	public DetNominaPercepcion procesar(DetNomina nomina, BigDecimal cantidad) throws SGPException {
		DetNominaPercepcion percepcion = null;
		
		try {
			
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
			if(cantidad == null)
				throw new SGPException("Debe indicar una cantidad.");
			
			this.cantidad = cantidad;
			
			if(this.cantidad.compareTo(ValoresBD._CERO.get()) < 0)
				throw new SGPException("La cantidad indicada es incorrecta");
			
    		this.importe = this.calcularImporte(this.cantidad, this.baseCalculo);
    		
    		this.calcularExentoGravado();
    		
		} catch(SGPException ex) {
			log.warn("[UI] {}", ex.getMessage());
			this.cantidad       = ValoresBD._CERO.get();
			this.importe        = ValoresBD._CERO.get();
			this.importeExento  = ValoresBD._CERO.get();
			this.importeGravado = ValoresBD._CERO.get();
			throw ex;
		} catch(Exception ex) {
			log.error("Problema para generar la percepción...", ex);
			this.cantidad       = ValoresBD._CERO.get();
			this.importe        = ValoresBD._CERO.get();
			this.importeExento  = ValoresBD._CERO.get();
			this.importeGravado = ValoresBD._CERO.get();
		} finally {
    		percepcion = this.build(nomina, CVE_VALES_DESPENSA, this.cantidad, this.importeExento, this.importeGravado);
    	}
		
		return percepcion;
	}
	
	@Override
	protected BigDecimal calcularCantidad(DetNomina nomina) throws SGPException {
		return this.tasaVales.setScale(4, RoundingMode.HALF_UP)
				.multiply(this.diasPeriodo);
	}

	@Override
	public BigDecimal calcularLimiteExento() {
		/* Los vales de despensa están exentos de ISR hasta por 7 veces la UMA diaria.
		 */
		return this.baseCalculo
				.multiply(_7.get())
				.setScale(2, RoundingMode.HALF_UP);
	}
}
