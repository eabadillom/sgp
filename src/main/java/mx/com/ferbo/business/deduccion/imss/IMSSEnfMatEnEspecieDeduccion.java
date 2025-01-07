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

/**Cálculo de cuota IMSS Enfermedades y Maternidad (En especie - cuota fija y excedente).
 */
public class IMSSEnfMatEnEspecieDeduccion extends AbstractIMSSDeduccion implements IDeduccion {
	
	private static Logger log = LogManager.getLogger(IMSSEnfMatEnEspecieDeduccion.class);
	
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
	public IMSSEnfMatEnEspecieDeduccion(ParametrosNomina parametros, BigDecimal diasTrabajados, BigDecimal ausencias, BigDecimal incapacidades, BigDecimal sdi) {
		this.cuotasIMSS = parametros.getCuotasIMSS();
		this.tiposDeduccion = parametros.getTiposDeduccion();
		this.diasTrabajados = diasTrabajados;
		this.ausencias = ausencias;
		this.incapacidades = incapacidades;
		this.uma = parametros.getUma().getImporteDiario();
		this.sdi = sdi;
	}

	@Override
	public DetNominaDeduccion calcular(DetNomina nomina, Integer index) {
		DetNominaDeduccion deduccion = null;
		BigDecimal cuotaFija         = null;
		BigDecimal cuotaExcedente    = null;
		BigDecimal cuota             = null;
		BigDecimal cero              = null;
		BigDecimal tres              = null;
		BigDecimal limiteUMAs        = null;
		BigDecimal excedente         = null;
		BigDecimal tarifa            = null;
		CatCuotaIMSS tarifaIMSS      = null;
		CatTipoDeduccion tdIMSS      = null;
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
			
			//Constantes necesarias para el cálculo de Enfermedad y Maternidad.
			cero = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
			tres = new BigDecimal("3.00").setScale(2, BigDecimal.ROUND_HALF_UP);
			cuotaExcedente = cero;
			
			
			tarifaIMSS = this.getCuotaIMSS("O", "EM1", 0);
			cuotaFija = this.uma
					.multiply(diasTrabajados)
					.multiply(tarifaIMSS.getCuota())
					.setScale(2, BigDecimal.ROUND_HALF_UP)
					;
			log.info("Enfermedad y Maternidad SDI: {} - UMA: {} - Tarifa Cuota fija: {}", this.sdi, this.uma, tarifaIMSS.getCuota());
			
			limiteUMAs = this.uma.multiply(tres).setScale(2, BigDecimal.ROUND_HALF_UP);
			excedente = this.sdi.subtract(limiteUMAs);
			if(this.sdi.compareTo(limiteUMAs) >= 0) {
				
				tarifaIMSS = this.getCuotaIMSS("O", "EM1", 1);
				tarifa = tarifaIMSS.getCuota();
				cuotaExcedente = excedente
						.multiply(tarifa)
						.multiply(this.diasTrabajados)
						.setScale(2, BigDecimal.ROUND_HALF_UP);
				
				log.info("Enfermedad y Maternidad SDI: {} - UMA: {} - Excedente: {}, Tarifa: {}", this.sdi, this.uma, excedente, tarifa);
			} else {
				cuotaExcedente = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			
			cuota = cuotaFija.add(cuotaExcedente).setScale(2, BigDecimal.ROUND_HALF_UP);
			
			log.info("Enfermedad y Maternidad SDI: {} - UMA: {} - Excedente: {}, Tarifa: {}", this.sdi, this.uma, cuota, tarifa);
			
		} catch(Exception ex) {
			log.warn("No fue posible calcular el excedente por Enfermedad y Maternidad.", ex);
			cuota = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
		} finally {
			deduccion = new DetNominaDeduccion();
			deduccion.setKey(new DetNominaDeduccionPK(nomina, index));
			deduccion.setTipoDeduccion(tdIMSS);
			deduccion.setClave("001");
			deduccion.setNombre("I.M.S.S. (Enfermedad y Maternidad)");
			deduccion.setImporte(cuota);
			deduccion.setInformar(false);
			deduccion.setProcesar(false);
		}
		
		return deduccion;
	}
}
