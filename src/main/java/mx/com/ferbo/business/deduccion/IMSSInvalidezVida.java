package mx.com.ferbo.business.deduccion;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.CatCuotaIMSS;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.util.SGPException;

public class IMSSInvalidezVida extends AbstractIMSSDeduccion implements IDeduccion {
	
	private static Logger log = LogManager.getLogger(IMSSInvalidezVida.class);
	
	private Date fechaInicioAnio = null;
	private Date fechaFinAnio = null;
	private BigDecimal totalDiasPeriodo = null;
	private BigDecimal sdi = null;
	
	/**
	 * @param fechaInicioAnio Fecha de inicio del año en curso (correspondiente al cálculo del periodo).
	 * @param fechaFinAnio Fecha de fin del año en curso (correspondiente al cálculo del periodo).
	 * @param totalDiasPeriodo Total de días del periodo (Semanal: 7 días, Quincenal: 15 días, Mensual: 30.4 días)
	 * @param sdi Salario Diario Integrado.
	 */
	public IMSSInvalidezVida(Date fechaInicioAnio, Date fechaFinAnio, BigDecimal totalDiasPeriodo, BigDecimal sdi) {
		this.fechaInicioAnio = fechaInicioAnio;
		this.fechaFinAnio = fechaFinAnio;
		this.totalDiasPeriodo = totalDiasPeriodo;
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
			tarifaIMSS = this.getCuotaIMSS("O", "IV", this.fechaInicioAnio, this.fechaFinAnio, this.sdi);
			cuota = this.sdi
					.multiply(tarifaIMSS.getCuota()).setScale(2, BigDecimal.ROUND_HALF_UP)
					.multiply(totalDiasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP)
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
			deduccion.setProcesar(false);
		}
		
		return deduccion;
	}
}
