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
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.util.SGPException;

/**Cálculo de cuuotas Enfermedades y Maternidad (Gastos Médicos para pensionados y beneficiarios)<br>
 * Fundamento legal: Artículo 25 segundo párrafo LEY DEL SEGURO SOCIAL.
 */
public class IMSSEnfMatGastosMedicosDBL extends AbstractIMSSDBL implements IDeduccion {
	
	private static Logger log = LogManager.getLogger(IMSSEnfMatGastosMedicosDBL.class);
	
	private BigDecimal diasTrabajados = null;
	private BigDecimal ausencias = null;
	private BigDecimal incapacidades = null;
	private BigDecimal sdi = null;
	
	public IMSSEnfMatGastosMedicosDBL(ParametrosNomina parametros, BigDecimal diasTrabajados, BigDecimal ausencias, BigDecimal incapacidades, BigDecimal sdi) {
		this.cuotasIMSS = parametros.getCuotasIMSS();
		this.tiposDeduccion = parametros.getTiposDeduccion();
		this.diasTrabajados = diasTrabajados;
		this.ausencias = ausencias;
		this.incapacidades = incapacidades;
		this.sdi = sdi;
	}

	@Override
	public DetNominaDeduccion calcular(DetNomina nomina) {
		DetNominaDeduccion deduccion  = null;
		BigDecimal         cuota      = null;
		CatCuotaIMSS       tarifaIMSS = null;
		CatTipoDeduccion   tdIMSS     = null;
		Integer            index      = null;
		
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
						
			tarifaIMSS = this.getCuotaIMSS("O", "EM2", 0);
			cuota = this.sdi
					.multiply(tarifaIMSS.getCuota())
					//TODO A LOS DIAS TRABAJADOS SE LES DEBE RESTAR LAS INCAPACIDADES Y A PARTIR DE ELLO SE REALIZA EL CALCULO.
					.multiply(diasTrabajados.subtract(incapacidades))
					.setScale(2, BigDecimal.ROUND_HALF_UP)
					;
			
			log.info("Gastos medicos pensionados y beneficiarios SDI: {} - Dias trabados: {} - Tarifa: {}", this.sdi, this.diasTrabajados, tarifaIMSS.getCuota());
			
		} catch(Exception ex) {
			log.error("No es posible calcular el excedente por Gastos Médicos para pensionados y beneficiarios...", ex);
			cuota = ValoresBD._CERO.get();
		} finally {
			index = this.nuevoIndiceDe(nomina.getDeducciones());
			
			deduccion = new DetNominaDeduccion.Builder()
					.key(new DetNominaDeduccionPK(nomina, index))
					.tipoDeduccion(tdIMSS)
					.clave(CVE_IMSS)
					.nombre("I.M.S.S. (Gastos médicos pensionados y beneficiarios)")
					.importe(cuota)
					.informar(false)
					.procesar(false)
					.build();
		}
		
		return deduccion;
	}
}
