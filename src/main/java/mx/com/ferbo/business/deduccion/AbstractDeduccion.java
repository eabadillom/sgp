package mx.com.ferbo.business.deduccion;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.sat.CatTipoDeduccion;

public abstract class AbstractDeduccion {
	private static Logger log = LogManager.getLogger(AbstractDeduccion.class);
	protected List<CatTipoDeduccion> tiposDeduccion = null;
	
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
			log.warn("No es posible determinar el tipo de dedcuccion: " + clave, ex);
		}
		
		return tipoDeduccion;
	}
	
	public void setTiposDeduccion(List<CatTipoDeduccion> tiposDeduccion) {
		this.tiposDeduccion = tiposDeduccion;
	}
}
