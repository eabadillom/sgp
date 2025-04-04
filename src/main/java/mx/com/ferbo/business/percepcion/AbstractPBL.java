package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.PercepcionDAO;
import mx.com.ferbo.model.CatPercepcion;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPercepcionPK;
import mx.com.ferbo.model.DetPercepcionEmpleado;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import mx.com.ferbo.util.SGPException;

public class AbstractPBL {
	private static Logger log = LogManager.getLogger(AbstractPBL.class);
	
	protected List<CatTipoPercepcion> tiposPercepcion = null;
	protected List<DetPercepcionEmpleado> percepcionesEmpleado = null;
	
	public static final String P_SUELDO           = "001";
	public static final String P_SEXTO_DIA        = "001";
	public static final String P_SEPTIMO_DIA      = "001";
	public static final String P_BONO_PUNTUALIDAD = "010";
	public static final String P_VALES_DESPENSA   = "029";
	
	//Con base en la tabla cat_percepcion
	public static final String CVE_SUELDO                      = "001";
	public static final String CVE_SEXTO_DIA                   = "002";
	public static final String CVE_SEPTIMO_DIA                 = "003";
	public static final String CVE_BONO_PUNTUALIDAD            = "015";
	public static final String CVE_PRIMA_VACACIONES_EN_TIEMPO  = "020";
	public static final String CVE_VACACIONES_REPORTADAS       = "021";
	public static final String CVE_PRIMA_VACACIONES_REPORTADAS = "022";
	public static final String CVE_AGUINALDO                   = "024";
	public static final String CVE_VACACIONES_EN_TIEMPO        = "029";
	public static final String CVE_VALES_DESPENSA              = "032";
	
	public DetNominaPercepcion build(DetNomina nomina, String clave) {
		DetNominaPercepcion percepcion = null;
		PercepcionDAO percepcionDAO = null;
		CatPercepcion catPercepcion = null;
		Integer index = null;
		
		percepcionDAO = new PercepcionDAO();
		catPercepcion = percepcionDAO.buscarPorId(clave);
		
		index = this.nuevoIndiceDe(nomina.getPercepciones());
		
		percepcion = new DetNominaPercepcion();
		percepcion.setKey(new DetNominaPercepcionPK(nomina, index));
		percepcion.setClave(clave);
		percepcion.setNombre(catPercepcion.getNombre());
		percepcion.setTipoPercepcion(catPercepcion.getTipoPercepcion());
		
		return percepcion;
	}
	
	public DetNominaPercepcion build(DetNomina nomina, String clave, BigDecimal cantidad, BigDecimal importeExento, BigDecimal importeGravado) {
		DetNominaPercepcion percepcion = null;
		
		percepcion = this.build(nomina, clave);
		percepcion.setCantidad(cantidad);
		percepcion.setImporteExento(importeExento);
		percepcion.setImporteGravado(importeGravado);
		
		return percepcion;
	}
	
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
	
	public Integer nuevoIndiceDe(List<DetNominaPercepcion> percepciones) {
		Integer maxIndex = null;
		DetNominaPercepcion maxP = null;
		
		try {
			maxP = Collections.max(percepciones, Comparator.comparing(p -> p.getKey().getId()));
			
			if(maxP.getKey().getId() == null)
				throw new SGPException("Existen elementos de \"Percepciones\" que no tienen asignado un consecutivo");
			
			maxIndex = maxP.getKey().getId() + 1;
			
		} catch(Exception ex) {
			maxIndex = 0;
		}
		
		return maxIndex;
	}


	public void setTiposPercepcion(List<CatTipoPercepcion> tiposPercepcion) {
		this.tiposPercepcion = tiposPercepcion;
	}


	public void setPercepcionesEmpleado(List<DetPercepcionEmpleado> percepcionesEmpleado) {
		this.percepcionesEmpleado = percepcionesEmpleado;
	}
}
