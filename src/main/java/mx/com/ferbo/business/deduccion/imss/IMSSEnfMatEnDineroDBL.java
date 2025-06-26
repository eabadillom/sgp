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

/**Cálculo de cuotas para Enfermedades y Maternidad (En dinero)<br>
 * Fundamento legal: Art. 107 Fracción I LEY DEL SEGURO SOCIAL
 */
public class IMSSEnfMatEnDineroDBL extends AbstractIMSSDBL implements IDeduccion {
	
	private static Logger log = LogManager.getLogger(IMSSEnfMatEnDineroDBL.class);
	
	private BigDecimal diasTrabajados = null;
	private BigDecimal ausencias = null;
	private BigDecimal incapacidades = null;
	private BigDecimal sdi = null;
	
	/**
	 * @param fechaInicioAnio Fecha de inicio del año en curso (correspondiente al cálculo del periodo).
	 * @param fechaFinAnio Fecha de fin del año en curso (correspondiente al cálculo del periodo).
	 * @param diasTrabajados Total de días del periodo (Semanal: 7 días, Quincenal: 15 días, Mensual: 30.4 días)
	 * @param sdi Salario Diario Integrado.
	 */
	public IMSSEnfMatEnDineroDBL(ParametrosNomina parametros, BigDecimal diasTrabajados, BigDecimal ausencias, BigDecimal incapacidades, BigDecimal sdi) {
		this.cuotasIMSS = parametros.getCuotasIMSS();
		this.tiposDeduccion = parametros.getTiposDeduccion();
		this.diasTrabajados = diasTrabajados;
		this.ausencias = ausencias;
		this.incapacidades = incapacidades;
		this.sdi = sdi;
	}

	@Override
	public DetNominaDeduccion calcular(DetNomina nomina) {
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
			
			tdIMSS = this.getTipoDeduccion(TD_IMSS);
			
			tarifaIMSS = this.getCuotaIMSS("O", "EM3", 0);
			log.info("TARIFA IMSS: {}", tarifaIMSS);
			cuota = this.sdi
					.multiply(tarifaIMSS.getCuota())
					//TODO A LOS DIAS TRABAJADOS SE LES DEBE RESTAR LAS INCAPACIDADES Y A PARTIR DE ELLO SE REALIZA EL CALCULO.
					.multiply(diasTrabajados.subtract(incapacidades))
					.setScale(2, BigDecimal.ROUND_HALF_UP)
					;
			
		} catch(Exception ex) {
			log.error("No es posible calcular el excedente En Dinero...", ex);
			cuota = ValoresBD._CERO.get();
		} finally {
			deduccion = new DetNominaDeduccion.Builder()
					.nomina(nomina)
					.clave(CVE_IMSS)
					.nombre("I.M.S.S. (En dinero)")
					.tipoDeduccion(tdIMSS)
					.importe(cuota)
					.informar(false)
					.procesar(false)
					.build();
		}
		
		return deduccion;
	}
}
