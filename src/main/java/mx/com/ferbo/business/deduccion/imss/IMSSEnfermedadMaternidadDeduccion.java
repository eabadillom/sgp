package mx.com.ferbo.business.deduccion.imss;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.IDeduccion;
import mx.com.ferbo.model.CatCuotaIMSS;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.util.SGPException;

public class IMSSEnfermedadMaternidadDeduccion extends AbstractIMSSDeduccion implements IDeduccion {
	
	private static Logger log = LogManager.getLogger(IMSSEnfermedadMaternidadDeduccion.class);
	
	private BigDecimal diasTrabajados = null;
	private BigDecimal uma = null;
	private BigDecimal sdi = null;
	
	/**
	 * @param fechaInicioAnio Fecha de inicio del año en curso (correspondiente al cálculo del periodo).
	 * @param fechaFinAnio Fecha de fin del año en curso (correspondiente al cálculo del periodo).
	 * @param diasTrabajados De acuerdo al periodo semanal, el total de días trabajados + proporcional septimo día.
	 * @param uma Unidad de Medida y Actualización (proporcionada por el INEGI)
	 * @param sdi Salario Diario Integrado.
	 */
	public IMSSEnfermedadMaternidadDeduccion(BigDecimal diasTrabajados, BigDecimal uma, BigDecimal sdi) {
		this.diasTrabajados = diasTrabajados;
		this.uma = uma;
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
			cuotaFija = this.uma.multiply(diasTrabajados).multiply(tarifaIMSS.getCuota());
			
			
			
			
			limiteUMAs = this.uma.multiply(tres).setScale(2, BigDecimal.ROUND_HALF_UP);
			excedente = this.sdi.subtract(limiteUMAs);
			
			if(this.sdi.compareTo(limiteUMAs) >= 0) {
				
				tarifaIMSS = this.getCuotaIMSS("O", "EM1", 1);
				tarifa = tarifaIMSS.getCuota();
				cuotaExcedente = excedente
						.multiply(tarifa)
						.multiply(this.diasTrabajados)
						.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			
			cuota = cuotaFija.add(cuotaExcedente).setScale(2, BigDecimal.ROUND_HALF_UP);
			
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
