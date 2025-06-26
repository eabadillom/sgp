package mx.com.ferbo.business.deduccion.imss;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.IDeduccion;
import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.CatCuotaIMSS;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.util.SGPException;

/**Cálculo de Invalidez y Vida.<br>
 * Fundamento legal: Art. 147 LEY DEL SEGURO SOCIAL.
 */
public class IMSSInvalidezVidaDBL extends AbstractIMSSDBL implements IDeduccion {
	
	private static Logger log = LogManager.getLogger(IMSSInvalidezVidaDBL.class);
	
	private BigDecimal diasTrabajados = null;
	private BigDecimal ausencias      = null;
	private BigDecimal incapacidades  = null; 
	private BigDecimal sdi            = null;
	
	/**
	 * @param fechaInicioAnio Fecha de inicio del año en curso (correspondiente al cálculo del periodo).
	 * @param fechaFinAnio Fecha de fin del año en curso (correspondiente al cálculo del periodo).
	 * @param diasTrabajados Total de días del periodo (Semanal: 7 días, Quincenal: 15 días, Mensual: 30.4 días)
	 * @param sdi Salario Diario Integrado.
	 */
	public IMSSInvalidezVidaDBL(ParametrosNomina parametros, BigDecimal diasTrabajados, BigDecimal ausencias, BigDecimal incapacidades, BigDecimal sdi) {
		this.diasTrabajados = diasTrabajados;
		this.ausencias      = ausencias;
		this.incapacidades  = incapacidades;
		this.cuotasIMSS     = parametros.getCuotasIMSS();
		this.tiposDeduccion = parametros.getTiposDeduccion();
		this.sdi            = sdi;
	}

	@Override
	public DetNominaDeduccion calcular(DetNomina nomina) {
		DetNominaDeduccion deduccion  = null;
		BigDecimal         cuota      = null;
		CatCuotaIMSS       tarifaIMSS = null;
		CatTipoDeduccion   tdIMSS     = null;
		
		try {
			if(this.cuotasIMSS == null)
				throw new SGPException("No se establecio la lista de cuotas del IMSS.");
			
			if(this.cuotasIMSS.size() <= 0)
				throw new SGPException("No se establecio la lista de cuotas del IMSS.");
			
			if(this.tiposDeduccion == null)
				throw new SGPException("No se establecio la lista de tipos de deduccion.");
			
			if(this.tiposDeduccion.size() <= 0)
				throw new SGPException("No se establecio la lista de tipos de deduccion.");
			
			tdIMSS = this.getTipoDeduccion(TD_IMSS);
			
			tarifaIMSS = this.getCuotaIMSS("O", "IV", 0);
			cuota = this.sdi
					.multiply(tarifaIMSS.getCuota())
					//TOTAL DIAS PERIODO - AUSENCIAS - INCAPACIDADES
					.multiply(diasTrabajados.subtract(ausencias).subtract(incapacidades))
					.setScale(2, BigDecimal.ROUND_HALF_UP)
					;
			
		} catch(Exception ex) {
			log.error("No es posible calcular el la cuota por Invalidez y Vida...", ex);
			cuota = ValoresBD._CERO.get();
		} finally {
			
			deduccion = new DetNominaDeduccion.Builder()
					.nomina(nomina)
					.tipoDeduccion(tdIMSS)
					.clave(CVE_IMSS)
					.nombre("I.M.S.S. (Invalidez y vida)")
					.importe(cuota)
					.informar(false)
					.procesar(false)
					.build();
		}
		
		return deduccion;
	}
}
