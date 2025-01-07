package mx.com.ferbo.business.deduccion.imss;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.IDeduccion;
import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.model.CatCuotaIMSS;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.util.SGPException;

public class IMSSInvalidezVida extends AbstractIMSSDeduccion implements IDeduccion {
	
	private static Logger log = LogManager.getLogger(IMSSInvalidezVida.class);
	
	private BigDecimal diasTrabajados = null;
	private BigDecimal sdi = null;
	
	/**
	 * @param fechaInicioAnio Fecha de inicio del año en curso (correspondiente al cálculo del periodo).
	 * @param fechaFinAnio Fecha de fin del año en curso (correspondiente al cálculo del periodo).
	 * @param diasTrabajados Total de días del periodo (Semanal: 7 días, Quincenal: 15 días, Mensual: 30.4 días)
	 * @param sdi Salario Diario Integrado.
	 */
	public IMSSInvalidezVida(ParametrosNomina parametros, BigDecimal diasTrabajados, BigDecimal sdi) {
		this.diasTrabajados = diasTrabajados;
		this.cuotasIMSS = parametros.getCuotasIMSS();
		this.tiposDeduccion = parametros.getTiposDeduccion();
		this.sdi = sdi;
	}

	@Override
	public DetNominaDeduccion calcular(DetNomina nomina, Integer index) {
		DetNominaDeduccion deduccion = null;
		
		BigDecimal cuota = null;
		CatCuotaIMSS tarifaIMSS = null;
		CatTipoDeduccion tdIMSS = null;
		
		try {
			if(this.cuotasIMSS == null)
				throw new SGPException("No se establecio la lista de cuotas del IMSS.");
			
			if(this.cuotasIMSS.size() <= 0)
				throw new SGPException("No se establecio la lista de cuotas del IMSS.");
			
			if(this.tiposDeduccion == null)
				throw new SGPException("No se establecio la lista de tipos de deduccion.");
			
			if(this.tiposDeduccion.size() <= 0)
				throw new SGPException("No se establecio la lista de tipos de deduccion.");
			
			tdIMSS = this.getTipoDeduccion("001");
			
			tarifaIMSS = this.getCuotaIMSS("O", "IV", 0);
			cuota = this.sdi
					.multiply(tarifaIMSS.getCuota())
					.multiply(diasTrabajados).setScale(2, BigDecimal.ROUND_HALF_UP)
					;
			
		} catch(Exception ex) {
			log.error("No es posible calcular el la cuota por Invalidez y Vida...", ex);
			cuota = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		} finally {
			deduccion = new DetNominaDeduccion();
			deduccion.setKey(new DetNominaDeduccionPK(nomina, index));
			deduccion.setTipoDeduccion(tdIMSS);
			deduccion.setClave("001");
			deduccion.setNombre("I.M.S.S. (Invalidez y vida)");
			deduccion.setImporte(cuota);
			deduccion.setInformar(false);
			deduccion.setProcesar(false);
		}
		
		return deduccion;
	}
}
