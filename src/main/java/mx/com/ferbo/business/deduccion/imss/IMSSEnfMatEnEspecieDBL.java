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

/**Cálculo de cuota IMSS Enfermedades y Maternidad (En especie - cuota fija y excedente).
 * Fundamento legal: Art. 106 LEY DEL SEGURO SOCIAL
 * Transitorio Art. Decimo noveno LEY DEL SEGURO SOCIAL.
 * 
 * Ausencias e incapacidades. Fundamento legal: Art. 31 LEY DEL SEGURO SOCIAL
 */
public class IMSSEnfMatEnEspecieDBL extends AbstractIMSSDBL implements IDeduccion {
	
	private static Logger log = LogManager.getLogger(IMSSEnfMatEnEspecieDBL.class);
	
	private BigDecimal diasTrabajados = null;
	private BigDecimal ausencias = null;
	private BigDecimal incapacidades = null;
	private BigDecimal uma = null;
	private BigDecimal sdi = null;
	
	/**
	 * @param parametros Información necesaria para el proceso de nómina.
	 * @param diasTrabajados De acuerdo al periodo semanal, el total de días trabajados + proporcional septimo día.
	 * @param uma Unidad de Medida y Actualización (proporcionada por el INEGI)
	 * @param sdi Salario Diario Integrado.
	 */
	public IMSSEnfMatEnEspecieDBL(ParametrosNomina parametros, BigDecimal diasTrabajados, BigDecimal ausencias, BigDecimal incapacidades, BigDecimal sdi) {
		this.cuotasIMSS = parametros.getCuotasIMSS();
		this.tiposDeduccion = parametros.getTiposDeduccion();
		this.diasTrabajados = diasTrabajados;
		this.ausencias = ausencias;
		this.incapacidades = incapacidades;
		this.uma = parametros.getUma().getImporteDiario();
		this.sdi = sdi;
	}

	@Override
	public DetNominaDeduccion calcular(DetNomina nomina) {
		DetNominaDeduccion deduccion      = null;
		BigDecimal         cuotaFija      = null;
		BigDecimal         cuotaExcedente = null;
		BigDecimal         cuota          = null;
		BigDecimal         limiteUMAs     = null;
		BigDecimal         excedente      = null;
		BigDecimal         tarifa         = null;
		CatCuotaIMSS       tarifaIMSS     = null;
		CatTipoDeduccion   tdIMSS         = null;
		
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
			
			cuotaExcedente = ValoresBD._CERO.get();
			
			tarifaIMSS = this.getCuotaIMSS("O", "EM1", 0);
			//Fundamento legal: Art. 106 LEY DEL SEGURO SOCIAL Fracción I. Transitorio Art. Decimo noveno Parrafo 1 de la LSS
			cuotaFija = this.uma
					//TODO A LOS DIAS TRABAJADOS SE LES DEBE RESTAR LAS INCAPACIDADES Y A PARTIR DE ELLO SE REALIZA EL CALCULO.
					.multiply(diasTrabajados.subtract(this.incapacidades))
					.multiply(tarifaIMSS.getCuota())
					.setScale(2, BigDecimal.ROUND_HALF_UP)
					;
			log.info("Enfermedad y Maternidad SDI: {} - UMA: {} - Tarifa Cuota fija: {}", this.sdi, this.uma, tarifaIMSS.getCuota());
			
			limiteUMAs = this.uma.multiply(ValoresBD._3.get()).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			//Fundamento legal: Art. 106 LEY DEL SEGURO SOCIAL Fracción II. Transitorio Art. Decimo noveno Parrafo 2 de la LSS
			excedente = this.sdi.subtract(limiteUMAs);
			if(this.sdi.compareTo(limiteUMAs) >= 0) {
				
				tarifaIMSS = this.getCuotaIMSS("O", "EM1", 1);
				tarifa = tarifaIMSS.getCuota();
				cuotaExcedente = excedente
						//TODO A LOS DIAS TRABAJADOS SE LES DEBE RESTAR LAS INCAPACIDADES Y A PARTIR DE ELLO SE REALIZA EL CALCULO.
						.multiply(diasTrabajados.subtract(this.incapacidades))
						.multiply(tarifa)
						.setScale(2, BigDecimal.ROUND_HALF_UP);
				
				log.info("Enfermedad y Maternidad SDI: {} - UMA: {} - Excedente: {}, Tarifa: {}", this.sdi, this.uma, excedente, tarifa);
			} else {
				cuotaExcedente = ValoresBD._CERO.get();
			}
			
			cuota = cuotaFija.add(cuotaExcedente).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			log.info("Enfermedad y Maternidad SDI: {} - UMA: {} - Excedente: {}, Tarifa: {}", this.sdi, this.uma, cuota, tarifa);
			
		} catch(Exception ex) {
			log.warn("No fue posible calcular el excedente por Enfermedad y Maternidad.", ex);
			cuota = ValoresBD._CERO.get();
		} finally {
			deduccion = new DetNominaDeduccion.Builder()
					.nomina(nomina)
					.tipoDeduccion(tdIMSS)
					.clave(CVE_IMSS)
					.nombre("I.M.S.S. (Enfermedad y Maternidad)")
					.importe(cuota)
					.informar(false)
					.procesar(false)
					.build();
		}
		
		return deduccion;
	}
}
