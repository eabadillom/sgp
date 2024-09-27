package mx.com.ferbo.business.deduccion;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.CatSubsidio;
import mx.com.ferbo.util.SGPException;

public class TarifaSubsidioDeduccion {
	
	private static Logger log = LogManager.getLogger(TarifaSubsidioDeduccion.class);
	
	private List<CatSubsidio> tablaSubsidio = null;
	private BigDecimal baseISR = null;
	
	public TarifaSubsidioDeduccion(List<CatSubsidio> tablaSubsidio, BigDecimal baseISR) {
		this.tablaSubsidio = tablaSubsidio;
		this.baseISR = baseISR;
	}
	
	public CatSubsidio calcular() throws SGPException {
		CatSubsidio tarifaSubsidio = null;
		List<CatSubsidio> resultList = null;
		
		if(this.baseISR == null)
			throw new SGPException("No se proporcionó la base para el ISR.");
		
		if(this.tablaSubsidio == null)
			throw new SGPException("No se proporcionó la tabla de subsidio al salario.");
		
		if(this.tablaSubsidio.size() <= 0)
			throw new SGPException("No se proporcionó la tabla de subsidio al salario.");
		
		try {
			resultList = this.tablaSubsidio.stream()
					.filter(s -> s.getParaIngresosDe().compareTo(baseISR) <= 0
							&& s.getHastaIngresosDe().compareTo(baseISR) >= 0)
					.collect(Collectors.toList())
					;
			
			if(resultList.size() > 0) {
				tarifaSubsidio = resultList.get(0);
			} else {
				throw new SGPException(String.format("No se encontró una tarifa de subsidio para la base calculada: Base ISR = %s", baseISR.toString()));
			}
		} catch(Exception ex) {
			log.warn(ex);
			throw new SGPException("No es posible determinar el subsidio al empleo...", ex);
		}
		return tarifaSubsidio;
	}
}
