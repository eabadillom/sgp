package mx.com.ferbo.business.percepcion;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;
import mx.com.ferbo.model.DetNominaPercepcionPK;
import mx.com.ferbo.model.sat.CatTipoPercepcion;
import mx.com.ferbo.util.FormatUtil;

public class SeptimoDiaPercepcion extends AbstractPercepcion implements IPercepcion {
	
	private static Logger log = LogManager.getLogger(SeptimoDiaPercepcion.class);
	
	private ParametrosNomina parametros = null;
	private BigDecimal diasLaborales    = null;
	private BigDecimal diasNoLaborales  = null;
	private BigDecimal diasVacaciones   = null;
	private BigDecimal diasTrabajados   = null;
	
	public SeptimoDiaPercepcion(ParametrosNomina parametros, BigDecimal diasLaborales, BigDecimal diasNoLaborales, BigDecimal diasTrabajados, BigDecimal diasVacaciones) {
		this.parametros = parametros;
		this.tiposPercepcion = parametros.getTiposPercepcion();
		this.diasLaborales = diasLaborales;
		this.diasNoLaborales = diasNoLaborales;
		this.diasTrabajados = diasTrabajados;
		this.diasVacaciones = diasVacaciones;
	}

	@Override
	public DetNominaPercepcion calcular(DetNomina nomina) {
		DetNominaPercepcion percepcion = null;
		
		BigDecimal          salarioDiario = null;
		BigDecimal          septimoDia = null;
		BigDecimal          proporcionalSemanal = null;
		BigDecimal          t = null;
		Integer             diaDescanso = null;
		String              sDiaDescanso = null;
		
		List<DetNominaPercepcion> percepciones = null;
		
		try {
			percepciones = nomina.getPercepciones();
			salarioDiario = nomina.getReceptor().getSalarioDiario();
			
			boolean removedPercepciones = percepciones.removeIf(d -> CVE_SEPTIMO_DIA.equalsIgnoreCase(d.getClave()));
			if(removedPercepciones)
				log.info("Se encontraron conceptos {}, los cuales fueron eliminados para el reproceso de SEPTIMO DIA.", CVE_SEPTIMO_DIA);
			
			t = new BigDecimal(parametros.getDiasPeriodo())
					.setScale(2, BigDecimal.ROUND_HALF_UP)
					.subtract(diasNoLaborales)
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			
			proporcionalSemanal = diasTrabajados
					.add(diasVacaciones)
					.divide(t, 4, BigDecimal.ROUND_HALF_UP)
					;
			
			septimoDia = salarioDiario
					.multiply(proporcionalSemanal)
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			
			for(int i = 0; i < diasNoLaborales.intValue(); i++) {
				diaDescanso = diasLaborales.add(new BigDecimal(i+1)).intValue();
				sDiaDescanso = String.format("%s dia", FormatUtil.convertirAOrdinal(diaDescanso));
				sDiaDescanso = FormatUtil.capitalizarPrimeraLetra(sDiaDescanso);
				this.percepcionDiaDescanso(nomina, CVE_SEPTIMO_DIA, sDiaDescanso, proporcionalSemanal, septimoDia);
			}
			
			log.info("Sexto y septimo día se agregaron directamente al objeto nomina.percepciones");
		} catch(Exception ex) {
			log.error("Problema para obtener el cálculo del septimo día.",  ex);
			septimoDia = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
			proporcionalSemanal = BigDecimal.ZERO.setScale(4, BigDecimal.ROUND_HALF_UP);
		}
		
		return percepcion;
	}
	
	private void percepcionDiaDescanso(DetNomina nomina, String clave, String nombre, BigDecimal proporcionalSemanal, BigDecimal septimoDia) {
		DetNominaPercepcion percepcion = null;
		CatTipoPercepcion   tpSeptimoDia = null;
		Integer             index = null;
		
		index = this.nuevoIndiceDe(nomina.getPercepciones());
		tpSeptimoDia = this.getTipoPercepcion(P_SEPTIMO_DIA);
		
		percepcion = new DetNominaPercepcion();
		percepcion.setKey(new DetNominaPercepcionPK(nomina, index));
		percepcion.setClave(clave);
		percepcion.setNombre(nombre);
		percepcion.setTipoPercepcion(tpSeptimoDia);
		percepcion.setCantidad(proporcionalSemanal);
		percepcion.setImporteGravado(septimoDia);
		percepcion.setImporteExcento(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
		
		if(percepcion.getImporteExento().add(percepcion.getImporteGravado()).compareTo(BigDecimal.ZERO) > 0)
			nomina.getPercepciones().add(percepcion);
	}
}
