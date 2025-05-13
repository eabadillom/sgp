package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetPercepcionEmpleado;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.util.SGPException;

/**Clase para el cálculo del Bono de puntualidad.<br>
 * De acuerdo con la LSS, el bono de puntualidad no debe exceder del 10% del Salario Base de Cotización.<br>
 * En caso de exceder el 10%, se considerará como parte del SBC.
 * 
 */
public class BonoPuntualidadPBL extends AbstractPBL {
	
	private static Logger log = LogManager.getLogger(BonoPuntualidadPBL.class);
	
	private Map<String, DetRegistro> mapAsistencias = null;
	
	private BigDecimal tasaBono               = null;
	private BigDecimal diasLaborales          = null;
	private BigDecimal diasNoLaborales        = null;
	private BigDecimal diasTrabajados         = null;
	
	private Boolean    procesaRetardos        = null;
	
	public BonoPuntualidadPBL(
			ParametrosNomina parametros, DetNomina nomina, BigDecimal tasaBono, 
			Map<String, DetRegistro> mapAsistencias, BigDecimal diasLaborales, BigDecimal diasNoLaborales, BigDecimal diasTrabajados,
			BigDecimal salarioDiarioIntegrado, BigDecimal proporcionalSeptimoDia
	) {
		super(parametros, nomina);
		this.baseCalculo     = this.nomina.getReceptor().getSalarioDiarioIntegrado();
		this.mapAsistencias  = mapAsistencias;
		this.tasaBono        = tasaBono;
		this.diasLaborales   = diasLaborales;
		this.diasNoLaborales = diasNoLaborales;
		this.diasTrabajados  = diasTrabajados;
		
	}
	
	@Override
	public DetNominaPercepcion procesar(DetNomina nomina) throws SGPException {
		this.cantidad = this.calcularCantidad(nomina);
		return this.procesar(nomina, this.cantidad);
	}
	
	@Override
	public DetNominaPercepcion procesar(DetNomina nomina, BigDecimal cantidad) {
		DetNominaPercepcion percepcion = null;
		DetPercepcionEmpleado percepcionEmpleado = null;
		
		try {
			this.cantidad = cantidad;
			this.importe = this.calcularImporte(this.cantidad, this.baseCalculo);
			
			percepcionEmpleado = this.buscaPercepcionEmpleado(P_BONO_PUNTUALIDAD);
			
			if(    (percepcionEmpleado != null)
					&& (percepcionEmpleado.getActivo())
					&& (percepcionEmpleado.getImporteMaximo() != null)
					&& (importe.compareTo(percepcionEmpleado.getImporteMaximo()) > 0) ) {
				
				importe = percepcionEmpleado.getImporteMaximo();
			}
			
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

	public void setProcesaRetardos(Boolean procesaRetardos) {
		this.procesaRetardos = procesaRetardos;
	}
	
	@Override
	protected BigDecimal calcularCantidad(DetNomina nomina)
	throws SGPException {
		BigDecimal diasPeriodo = null;
		BigDecimal cantidad = null;
		
		//TODO VALIDAR PRIMERO SI NO HAY RETARDOS.
		//En caso de existir retardos en el periodo de calculo, el bono de puntualidad es CERO.
		if(this.diasTrabajados.compareTo(this.diasLaborales) < 0)
			throw new SGPException("El empleado tiene ausencias, por lo que no se otorgará el bono de puntualidad.");
		
		if(this.procesaRetardos == null)
			this.procesaRetardos = Boolean.FALSE;
		
		for(Map.Entry<String, DetRegistro> entry : this.mapAsistencias.entrySet()) {
			log.info("Entry: {}", entry);
			String claveStatusRegistro = entry.getValue().getIdEstatus().getCodigo();
			if(procesaRetardos.booleanValue() && ("R".equalsIgnoreCase(claveStatusRegistro) || "F".equalsIgnoreCase(claveStatusRegistro)) )
				throw new SGPException("Existen dias con retardo no justificados o faltas para el empleado.");
		}
		
		diasPeriodo = this.diasTrabajados.add(this.diasNoLaborales).setScale(2, RoundingMode.HALF_UP);
		
		cantidad = this.tasaBono.multiply(diasPeriodo).setScale(5, RoundingMode.HALF_UP);
		
		return cantidad ;
	}

	@Override
	public BigDecimal calcularLimiteExento() {
		return ValoresBD._CERO.get();
	}
}
