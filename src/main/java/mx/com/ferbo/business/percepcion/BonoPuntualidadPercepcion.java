package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AbstractPercepcion;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPercepcionPK;
import mx.com.ferbo.model.DetPercepcionEmpleado;
import mx.com.ferbo.model.DetRegistro;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import mx.com.ferbo.util.SGPException;

public class BonoPuntualidadPercepcion extends AbstractPercepcion implements IPercepcion {
	
	private static Logger log = LogManager.getLogger(BonoPuntualidadPercepcion.class);
	
	private BigDecimal tasaBono = null;
	private BigDecimal diasTrabajados = null;
	private int diasPorPeriodo = 0;
	private BigDecimal salarioDiarioIntegrado = null;
	private BigDecimal proporcionalSeptimoDia = null;
	private Map<String, DetRegistro> mapAsistencias = null;
	
	public BonoPuntualidadPercepcion(List<CatTipoPercepcion> tiposPercepcion, BigDecimal tasaBono, BigDecimal diasTrabajados, 
			Map<String, DetRegistro> mapAsistencias, int diasPorPeriodo,
			BigDecimal salarioDiarioIntegrado, BigDecimal proporcionalSeptimoDia) {
		this.tiposPercepcion = tiposPercepcion;
		this.mapAsistencias = mapAsistencias;
		this.tasaBono = tasaBono;
		this.diasTrabajados = diasTrabajados;
		this.diasPorPeriodo = diasPorPeriodo;
		this.salarioDiarioIntegrado = salarioDiarioIntegrado;
		this.proporcionalSeptimoDia = proporcionalSeptimoDia;
	}

	@Override
	public DetNominaPercepcion calcular(DetNomina nomina, Integer index) {
		DetNominaPercepcion percepcion = null;
		BigDecimal bono = null;
    	BigDecimal diasPeriodo = null;
    	
    	CatTipoPercepcion tpBonoPuntualidad = null;
    	DetPercepcionEmpleado percepcionEmpleado = null;
    	
    	try {
    		tpBonoPuntualidad = this.getTipoPercepcion(P_BONO_PUNTUALIDAD);
    		
    		//TODO VALIDAR PRIMERO SI NO HAY RETARDOS.
    		//En caso de existir retardos en el periodo de calculo, el bono de puntualidad es CERO.
    		if(this.diasTrabajados.intValue() < diasPorPeriodo)
    			throw new SGPException("Los dias trabajados no pueden ser mayores a los dias que componen al periodo de pago.");
    		
    		for(Map.Entry<String, DetRegistro> entry :  this.mapAsistencias.entrySet()) {
    			String codigo = entry.getValue().getIdEstatus().getCodigo();
    			if("R".equalsIgnoreCase(codigo) || "F".equalsIgnoreCase(codigo))
    				throw new SGPException("Existen dias con retardo no justificados o faltas para el empleado.");
    		}
    		
    		diasPeriodo = this.diasTrabajados.add(proporcionalSeptimoDia).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
    		bono = salarioDiarioIntegrado.multiply(this.tasaBono).setScale(2, BigDecimal.ROUND_HALF_UP);
    		bono = bono.multiply(diasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP);
    		
    		percepcionEmpleado = this.buscaPercepcionEmpleado(P_BONO_PUNTUALIDAD);
    		
    		if(    (percepcionEmpleado != null) 
				&& (percepcionEmpleado.getImporteMaximo() != null)
				&& (bono.compareTo(percepcionEmpleado.getImporteMaximo()) > 0) ) {
    			
    			bono = percepcionEmpleado.getImporteMaximo();
    		}
    		
    	} catch(Exception ex) {
    		log.warn("No es posible calcular el bono de puntualidad: {}", ex.getMessage());
    		bono = BigDecimal.ZERO;
    	} finally {
    		percepcion = new DetNominaPercepcion();
    		percepcion.setKey(new DetNominaPercepcionPK(nomina, index));
    		percepcion.setClave(P_BONO_PUNTUALIDAD);
    		percepcion.setNombre("Bono puntualidad");
    		percepcion.setTipoPercepcion(tpBonoPuntualidad);
    		percepcion.setImporteGravado(bono);
    		percepcion.setImporteExcento(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
    		percepcion.setClave("FRB-" + tpBonoPuntualidad.getClave());
    		
    		this.tiposPercepcion = null;
    		this.percepcionesEmpleado = null;
    		this.mapAsistencias = null;
    		this.tasaBono = null;
    		this.diasTrabajados = null;
    		this.salarioDiarioIntegrado = null;
    		this.proporcionalSeptimoDia = null;
    	}
    	
		return percepcion;
	}

	public void setDiasPorPeriodo(int diasPorPeriodo) {
		this.diasPorPeriodo = diasPorPeriodo;
	}

	public void setSalarioDiarioIntegrado(BigDecimal salarioDiarioIntegrado) {
		this.salarioDiarioIntegrado = salarioDiarioIntegrado;
	}

	public void setProporcionalSeptimoDia(BigDecimal proporcionalSeptimoDia) {
		this.proporcionalSeptimoDia = proporcionalSeptimoDia;
	}
}
