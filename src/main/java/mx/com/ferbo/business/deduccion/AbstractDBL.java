package mx.com.ferbo.business.deduccion;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.sat.CatTipoDeduccion;

/**Abstract Deduccion Business Logic
 * 
 */
public abstract class AbstractDBL {
	private static Logger log = LogManager.getLogger(AbstractDBL.class);
	protected List<CatTipoDeduccion> tiposDeduccion = null;
	
	public static final String TD_IMSS                   = "001";
	public static final String TD_ISR                    = "002";
	public static final String TD_ISR_LEY_174            = "002";
	public static final String TD_ISR_ANTES_DE_SUBSIDIO  = "002";
	public static final String TD_AJUSTE_ISR_MENSUAL     = "002";
	public static final String TD_AJUSTE_AL_SUBSIDIO     = "107";
	public static final String TD_AJUSTE_AL_NETO         = "004";
	
	public static final String CVE_ISR                   = "045";
	public static final String CVE_ISR_LEY_174           = "043";
	public static final String CVE_ISR_ANTES_DE_SUBSIDIO = "041";
	public static final String CVE_AJUSTE_ISR_MENSUAL    = "104";
	public static final String CVE_AJUSTE_AL_SUBSIDIO    = "107";
	public static final String CVE_IMSS                  = "052";
	public static final String CVE_AJUSTE_AL_NETO        = "099";
	public static final String CVE_PRESTAMO_FONACOT      = "061";
	
	public DetNominaDeduccion build(DetNomina nomina, String clave, String nombre, BigDecimal importe, CatTipoDeduccion tipoDeduccion, Boolean informar, Boolean procesar) {
		DetNominaDeduccion deduccion = null;
		
		deduccion = new DetNominaDeduccion();
		deduccion.setNomina(nomina);
		deduccion.setClave(clave);
		deduccion.setNombre(nombre);
		deduccion.setImporte(importe);
		deduccion.setTipoDeduccion(tipoDeduccion);
		deduccion.setInformar(informar);
		deduccion.setProcesar(procesar);
		
		return deduccion;
	}
	
	
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
			
			log.info("Tipo de deducción (SAT) asignado: {}", tipoDeduccion);
		} catch(Exception ex) {
			log.warn("No es posible determinar el tipo de deducción: " + clave, ex);
		}
		
		return tipoDeduccion;
	}
	
	public void setTiposDeduccion(List<CatTipoDeduccion> tiposDeduccion) {
		this.tiposDeduccion = tiposDeduccion;
	}
	
	public Integer nuevoIndiceDe(List<DetNominaDeduccion> deducciones) {
//		Integer maxIndex = null;
//		DetNominaDeduccion maxD = null;
//		
//		try {
//			maxD = Collections.max(deducciones, Comparator.comparing(d -> d.getKey().getId()));
//			
//			if(maxD.getKey().getId() == null)
//				throw new SGPException("Existen elementos de \"Deducciones\" que no tienen asignado un consecutivo");
//			
//			maxIndex = maxD.getKey().getId() + 1;
//			
//		} catch(Exception ex) {
//			maxIndex = 0;
//		}
//		
//		return maxIndex;
		return 0;
	}
	
	public DetNominaDeduccion getDeduccion(DetNomina nomina, int idx, String tipoDeduccion, String clave, String nombre, Boolean informar, Boolean procesar, BigDecimal importe ) {
		DetNominaDeduccion deduccion = null;
		CatTipoDeduccion tipo = null;
		
		tipo = this.getTipoDeduccion(tipoDeduccion);
		deduccion = new DetNominaDeduccion();
		deduccion.setNomina(nomina);
		deduccion.setTipoDeduccion(tipo);
		deduccion.setClave(clave);
		deduccion.setNombre(nombre);
		deduccion.setInformar(informar);
		deduccion.setProcesar(procesar);
		deduccion.setImporte(importe);
		
		return deduccion;
	}
}
