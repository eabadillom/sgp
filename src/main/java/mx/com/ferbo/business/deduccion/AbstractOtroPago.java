package mx.com.ferbo.business.deduccion;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.DetNominaOtroPago;
import mx.com.ferbo.model.sat.CatTipoOtroPago;
import mx.com.ferbo.util.SGPException;

public class AbstractOtroPago {
	private static Logger log = LogManager.getLogger(AbstractOtroPago.class);
	
	public static final String OP_SUBSIDIO_AL_SALARIO = "002";
	
	protected List<CatTipoOtroPago> tiposOtroPago = null;
	
	public CatTipoOtroPago getTipoOtroPago(String clave) {
		CatTipoOtroPago tipoOtroPago = null;
		List<CatTipoOtroPago> collect = null;
		try {
			collect = this.tiposOtroPago.stream()
					.filter(t -> clave.equals(t.getClave()))
					.collect(Collectors.toList())
					;
			if(collect.size() > 0)
				tipoOtroPago = collect.get(0);
		} catch(Exception ex) {
			log.warn("No es posible determinar el tipo de otro pago: " + clave, ex);
		}
		
		return tipoOtroPago;
	}

	public void setTiposOtroPago(List<CatTipoOtroPago> tiposOtroPago) {
		this.tiposOtroPago = tiposOtroPago;
	}
	
	public Integer nuevoIndiceDe(List<DetNominaOtroPago> otrosPagos) {
		Integer maxIndex = null;
		DetNominaOtroPago maxOP = null;
		
		try {
			maxOP = Collections.max(otrosPagos, Comparator.comparing(o -> o.getKey().getId()));
			if(maxOP.getKey().getId() == null)
				throw new SGPException("Existen elementos de \"Otros pagos\" que no tienen asignado un consecutivo.");
			
			maxIndex = maxOP.getKey().getId() + 1;
		} catch(Exception ex) {
			maxIndex = 0;
		}
		
		return maxIndex;
	}
}
