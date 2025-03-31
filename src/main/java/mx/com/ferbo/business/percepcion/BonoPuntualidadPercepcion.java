package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetPercepcionEmpleado;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import mx.com.ferbo.util.SGPException;

/**Clase para el cálculo del Bono de puntualidad.<br>
 * De acuerdo con la LSS, el bono de puntualidad no debe exceder del 10% del Salario Base de Cotización.<br>
 * En caso de exceder el 10%, se considerará como parte del SBC.
 * 
 */
public class BonoPuntualidadPercepcion extends AbstractPercepcion implements IPercepcion {
	
	private static Logger log = LogManager.getLogger(BonoPuntualidadPercepcion.class);
	
	private Map<String, DetRegistro> mapAsistencias = null;
	
	private BigDecimal tasaBono               = null;
	private BigDecimal diasLaborales          = null;
	private BigDecimal diasNoLaborales        = null;
	private BigDecimal diasTrabajados         = null;
	private BigDecimal salarioDiarioIntegrado = null;
	
	private Boolean    procesaRetardos        = null;
	
	public BonoPuntualidadPercepcion(
			List<CatTipoPercepcion> tiposPercepcion, BigDecimal tasaBono, Map<String, DetRegistro> mapAsistencias, 
			BigDecimal diasLaborales, BigDecimal diasNoLaborales, BigDecimal diasTrabajados, BigDecimal salarioDiarioIntegrado,
			BigDecimal proporcionalSeptimoDia
	) {
		this.tiposPercepcion = tiposPercepcion;
		this.mapAsistencias = mapAsistencias;
		this.tasaBono = tasaBono;
		this.diasLaborales = diasLaborales;
		this.diasNoLaborales = diasNoLaborales;
		this.diasTrabajados = diasTrabajados;
		this.salarioDiarioIntegrado = salarioDiarioIntegrado;
	}

	@Override
	public DetNominaPercepcion calcular(DetNomina nomina) {
		DetNominaPercepcion percepcion = null;
		BigDecimal bono = null;
    	BigDecimal diasPeriodo = null;
    	
    	DetPercepcionEmpleado percepcionEmpleado = null;
    	
    	try {
    		//TODO VALIDAR PRIMERO SI NO HAY RETARDOS.
    		//En caso de existir retardos en el periodo de calculo, el bono de puntualidad es CERO.
    		if(this.diasTrabajados.compareTo(diasLaborales) < 0)
    			throw new SGPException("El empleado tiene ausencias, por lo que no se otorgará el bono de puntualidad.");
    		
    		if(this.procesaRetardos == null)
    			this.procesaRetardos = new Boolean(false);
    		
    		for(Map.Entry<String, DetRegistro> entry : this.mapAsistencias.entrySet()) {
    			log.info("Entry: {}", entry);
    			String claveStatusRegistro = entry.getValue().getIdEstatus().getCodigo();
    			if(procesaRetardos.booleanValue() && ("R".equalsIgnoreCase(claveStatusRegistro) || "F".equalsIgnoreCase(claveStatusRegistro)) )
    				throw new SGPException("Existen dias con retardo no justificados o faltas para el empleado.");
    		}
    		
    		diasPeriodo = this.diasTrabajados.add(this.diasNoLaborales).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
    		bono = salarioDiarioIntegrado.multiply(this.tasaBono).setScale(5, BigDecimal.ROUND_HALF_UP);
    		bono = bono.multiply(diasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
    		percepcionEmpleado = this.buscaPercepcionEmpleado(P_BONO_PUNTUALIDAD);
    		
    		if(    (percepcionEmpleado != null)
    			&& (percepcionEmpleado.getActivo())
				&& (percepcionEmpleado.getImporteMaximo() != null)
				&& (bono.compareTo(percepcionEmpleado.getImporteMaximo()) > 0) ) {
    			
    			bono = percepcionEmpleado.getImporteMaximo();
    		}
    		
    	} catch(Exception ex) {
    		log.warn("No es posible calcular el bono de puntualidad: {}", ex.getMessage());
    		bono = BigDecimal.ZERO;
    	} finally {
    		percepcion = this.build(nomina, CVE_BONO_PUNTUALIDAD, null, ValoresBD._CERO.getValor(), bono);
    	}
    	
    	return percepcion;
	}

	public void setProcesaRetardos(Boolean procesaRetardos) {
		this.procesaRetardos = procesaRetardos;
	}
}
