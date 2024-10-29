package mx.com.ferbo.business.deduccion;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.DetPercepcionEmpleado;
import mx.com.ferbo.model.sat.CatTipoPercepcion;

public class AbstractPercepcion {
	private static Logger log = LogManager.getLogger(AbstractPercepcion.class);
	
	protected List<CatTipoPercepcion> tiposPercepcion = null;
	protected List<DetPercepcionEmpleado> percepcionesEmpleado = null;
	
	public static final String P_BONO_PUNTUALIDAD = "010";
	public static final String P_VALES_DESPENSA = "029";
	
	public CatTipoPercepcion getTipoPercepcion(String clave) {
		CatTipoPercepcion tipoPercepcion = null;
		List<CatTipoPercepcion> collect = null;
		
		try {
			collect = this.tiposPercepcion.stream()
					.filter(t -> clave.equals(t.getClave()))
					.collect(Collectors.toList())
					;
			
			if(collect.size() > 0)
				tipoPercepcion = collect.get(0);
		} catch(Exception ex) {
			log.warn("No es posible determinar el tipo de percepción: " + clave, ex);
		}
		
		return tipoPercepcion;
	}
	
	
	public DetPercepcionEmpleado buscaPercepcionEmpleado(String clave) {
		DetPercepcionEmpleado percepcionEmpleado = null;
		List<DetPercepcionEmpleado> collect = null;
		
		try {
			collect = this.percepcionesEmpleado.stream()
					.filter(p -> clave.equals(p.getTipoPercepcion().getClave()))
					.collect(Collectors.toList())
					;
			
			if(collect.size() > 0)
				percepcionEmpleado = collect.get(0);
		} catch(Exception ex) {
			log.warn("No es posible determinar la percepción predeterminada para el empleado: " + clave, ex);
		}
		
		return percepcionEmpleado;
	}


	public void setTiposPercepcion(List<CatTipoPercepcion> tiposPercepcion) {
		this.tiposPercepcion = tiposPercepcion;
	}


	public void setPercepcionesEmpleado(List<DetPercepcionEmpleado> percepcionesEmpleado) {
		this.percepcionesEmpleado = percepcionesEmpleado;
	}
}
