package mx.com.ferbo.business.deduccion;

import static mx.com.ferbo.enums.ValoresBD._CERO;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.sat.CatTipoDeduccion;

public class AjusteAlNetoDBL extends AbstractDBL implements IDeduccion {
	
	private static Logger log = LogManager.getLogger(AjusteAlNetoDBL.class);
	
	private BigDecimal ajusteAlNeto = null;
	
	public AjusteAlNetoDBL(BigDecimal ajusteAlNeto) {
		this.ajusteAlNeto = ajusteAlNeto;
	}

	@Override
	public DetNominaDeduccion calcular(DetNomina nomina) {
		DetNominaDeduccion deduccion = null;
		CatTipoDeduccion   tdAjusteAlNeto = null;
		Integer            index = null;
		
		try {
			tdAjusteAlNeto = this.getTipoDeduccion(TD_AJUSTE_AL_NETO);
		} catch(Exception ex) {
			log.error("Problema para generar el ajuste al neto...", ex);
			this.ajusteAlNeto = _CERO.get();
		} finally {
			index = this.nuevoIndiceDe(nomina.getDeducciones());
			
			deduccion = new DetNominaDeduccion.Builder()
					.key(new DetNominaDeduccionPK(nomina, index))
					.tipoDeduccion(tdAjusteAlNeto)
					.clave(CVE_AJUSTE_AL_NETO)
					.nombre("Ajuste al neto")
					.importe(ajusteAlNeto)
					.informar(true)
					.procesar(true)
					.build();
		}
		
		return deduccion;
	}
}
