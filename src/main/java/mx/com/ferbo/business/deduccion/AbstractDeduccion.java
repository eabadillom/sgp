package mx.com.ferbo.business.deduccion;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.util.SGPException;

public abstract class AbstractDeduccion {
	private static Logger log = LogManager.getLogger(AbstractDeduccion.class);
	protected List<CatTipoDeduccion> tiposDeduccion = null;
	
	public static final String D_ISR = "002";
	
	public CatTipoDeduccion getTipoDeduccion(String clave) {
		CatTipoDeduccion tipoDeduccion = null;
		List<CatTipoDeduccion> collect = null;
		try {
			collect = this.tiposDeduccion.stream()
			.filter(t -> clave.equals(t.getClave()))
			.collect(Collectors.toList())
			;
			
			if(collect.size() > 0)
				tipoDeduccion = collect.get(0);
		} catch(Exception ex) {
			log.warn("No es posible determinar el tipo de deducción: " + clave, ex);
		}
		
		return tipoDeduccion;
	}
	
	public void setTiposDeduccion(List<CatTipoDeduccion> tiposDeduccion) {
		this.tiposDeduccion = tiposDeduccion;
	}
	
	public Integer nuevoIndiceDe(List<DetNominaDeduccion> deducciones) {
		Integer maxIndex = null;
		DetNominaDeduccion maxD = null;
		
		try {
			maxD = Collections.max(deducciones, Comparator.comparing(d -> d.getKey().getId()));
			
			if(maxD.getKey().getId() == null)
				throw new SGPException("Existen elementos de \"Deducciones\" que no tienen asignado un consecutivo");
			
			maxIndex = maxD.getKey().getId() + 1;
			
		} catch(Exception ex) {
			maxIndex = 0;
		}
		
		return maxIndex;
	}
}
