package mx.com.ferbo.business.otropago;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.DetNominaOtroPago;
import mx.com.ferbo.model.sat.CatTipoOtroPago;

public class AbstractOtroPago {
	private static Logger log = LogManager.getLogger(AbstractOtroPago.class);
	
	public static final String OP_SUBSIDIO_AL_SALARIO = "002";
	public static final String OP_ISR_AJUSTADO_POR_SUBSIDIO = "007";
	public static final String OP_AJUSTE_AL_NETO      = "999";
	
	public static final String CVE_SUBSIDIO_AL_SALARIO = "035";
	public static final String CVE_ISR_AJUSTADO_POR_SUBSIDIO = "105";
	public static final String CVE_AJUSTE_AL_NETO = "004";
	
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
		
		
		return 0;
	}
}
