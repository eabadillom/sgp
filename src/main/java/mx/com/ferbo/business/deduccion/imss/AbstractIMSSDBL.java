package mx.com.ferbo.business.deduccion.imss;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AbstractDBL;
import mx.com.ferbo.model.CatCuotaIMSS;
import mx.com.ferbo.util.SGPException;

public abstract class AbstractIMSSDBL extends AbstractDBL {
	private static Logger log = LogManager.getLogger(AbstractIMSSDBL.class);
	protected List<CatCuotaIMSS> cuotasIMSS = null;
	
	public CatCuotaIMSS getCuotaIMSS(String tipoCuota, String clave, Date fechaInicio, Date fechaFin, BigDecimal base) {
		CatCuotaIMSS tarifa = null;
		List<CatCuotaIMSS> collect = null;
		
		try {
			collect = this.cuotasIMSS.stream()
					.filter(t -> 
						( t.getClave().equals(clave)
							&& t.getTipoCuota().equals(tipoCuota)
							&& (t.getBaseMinimo().compareTo(base) <= 0 && t.getBaseMaximo().compareTo(base) >= 0)
						)
					)
					.collect(Collectors.toList())
					;
			if(collect.size() > 0)
				tarifa = collect.get(0);
			else {
				throw new SGPException(String.format("No se encontró configuración vigente para la cuota del IMSS % - %s - %s - %s - %s", tipoCuota, clave, fechaInicio, fechaFin, base));
			}
		} catch(Exception ex) {
			log.warn("No es posible determinar la cuota del IMSS...", ex);
		}
		
		return tarifa;
	}
	
	public CatCuotaIMSS getCuotaIMSS(String tipoCuota, String clave, Integer numero) {
		CatCuotaIMSS tarifa = null;
		List<CatCuotaIMSS> collect = null;
		
		try {
			collect = this.cuotasIMSS.stream()
					.filter(t -> 
						( t.getTipoCuota().equals(tipoCuota)
							&& t.getClave().equals(clave)
							&& t.getNumero().equals(numero)
							/*&&
							(
								(t.getVigenciaInicio().compareTo(fecha) <= 0 && t.getVigenciaFin().compareTo(fecha) >= 0)
								|| (t.getVigenciaInicio().compareTo(fecha) >= 0 && t.getVigenciaFin() == null )
							)*/
						)
					)
					.collect(Collectors.toList())
					;
			if(collect.size() > 0)
				tarifa = collect.get(0);
			else {
				throw new SGPException(String.format("No se encontró configuración vigente para la cuota del IMSS %S - %s - %s - %s", tipoCuota, clave, numero));
			}
		} catch(Exception ex) {
			log.warn("No es posible determinar la cuota del IMSS...", ex);
		}
		
		return tarifa;
	}

	public void setCuotasIMSS(List<CatCuotaIMSS> cuotasIMSS) {
		this.cuotasIMSS = cuotasIMSS;
	}
	
	
}
