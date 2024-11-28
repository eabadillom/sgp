package mx.com.ferbo.business.deduccion.subsidio;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.Subsidio2DAO;
import mx.com.ferbo.dao.n.UMADAO;
import mx.com.ferbo.model.CatSubsidio2;
import mx.com.ferbo.model.CatUMA;
import mx.com.ferbo.util.SGPException;


/**Clase que se encarga de obtener el importe para el subsidio al empleo.
 * Con base en la publicación del Diario Oficial de la Federación
 * el 1 de mayo de 2024.
 * 
 * La base para el ISR queda obsoleta. Este parámetro permanece en el método calculo(...)
 * por compatibilidad hacia atrás con la lógica anterior y con el resto de código para
 * el cálculo de ISR después del subsidio al empleo.
 */
public class TarifaSubsidioDeduccion2 implements ISubsidioEmpleo {
	
	private static Logger log = LogManager.getLogger(TarifaSubsidioDeduccion2.class);
	
	private LocalDate fecha = null;
	private Subsidio2DAO subsidioDAO = null;
	private UMADAO umaDAO = null;
	
	public TarifaSubsidioDeduccion2(LocalDate fecha) {
		this.fecha = fecha;
		this.subsidioDAO = new Subsidio2DAO();
		this.umaDAO = new UMADAO();
	}
	
	@Override
	public BigDecimal calcular(String periodo, BigDecimal baseISR) throws SGPException {
		BigDecimal importeSubsidio = null;
		CatSubsidio2 tarifaSubsidio = null;
		CatUMA uma = null;
		log.info("Calculando subsidio al empleo 2024...");
		
		if(baseISR == null)
			throw new SGPException("No se proporcionó la base para el Subsidio al empleo.");
		
		tarifaSubsidio = this.subsidioDAO.buscarVigente(fecha);
		if(tarifaSubsidio == null)
			throw new SGPException("No se encontró la tarifa para el Subsidio al empleo.");
		
		uma = this.umaDAO.buscarVigente(fecha);
		if(uma == null)
			throw new SGPException("No se encontró la UMA.");
		
		try {
			if(ISubsidioEmpleo.PERIODO_SEMANAL.equalsIgnoreCase(periodo)) {
				importeSubsidio = this.calcularSemanal(tarifaSubsidio, uma);
			}
			
			if(ISubsidioEmpleo.PERIODO_MENSUAL.equalsIgnoreCase(periodo)) {
				importeSubsidio = this.calcularMensual(tarifaSubsidio, uma);
			}
			
			log.info("Fecha: {}, Base ISR: {}, Subsidio acreditado: {}", this.fecha, baseISR, "(Falta indicar la clase BL que genera el subsidio.)");
		} catch(Exception ex) {
			log.warn(ex);
			throw new SGPException("No es posible determinar el subsidio al empleo...", ex);
		}
		
		return importeSubsidio;
	}
	
	private BigDecimal calcularSemanal(CatSubsidio2 tarifa, CatUMA uma) {
		BigDecimal importeSubsidio = null;
		BigDecimal diasPeriodo = new BigDecimal(7).setScale(0, BigDecimal.ROUND_HALF_UP);
		BigDecimal tasaSubsidio = null;
		BigDecimal importeUMA = null;
		
		tasaSubsidio = tarifa.getTasa();
		importeUMA = uma.getImporteDiario();
		
		importeSubsidio = tasaSubsidio
				.multiply(importeUMA).setScale(2, BigDecimal.ROUND_HALF_UP)
				.multiply(diasPeriodo).setScale(2, BigDecimal.ROUND_HALF_UP)
				;
		
		return importeSubsidio;
	}
	
	private BigDecimal calcularMensual(CatSubsidio2 tarifa, CatUMA uma) {
		BigDecimal importeSubsidio = null;
		BigDecimal diasPeriodo = new BigDecimal(30.4).setScale(1, BigDecimal.ROUND_HALF_UP);
		BigDecimal tasaSubsidio = null;
		BigDecimal importeUMA = null;
		
		tasaSubsidio = tarifa.getTasa();
		importeUMA = uma.getImporteMensual();
		
		importeSubsidio = tasaSubsidio
				.multiply(importeUMA).setScale(2, BigDecimal.ROUND_HALF_UP)
				.divide(diasPeriodo, BigDecimal.ROUND_HALF_UP)
				;
		
		return importeSubsidio;
	}

}
