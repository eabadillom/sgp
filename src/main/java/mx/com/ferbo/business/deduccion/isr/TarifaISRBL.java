package mx.com.ferbo.business.deduccion.isr;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.CatTarifaISR;
import mx.com.ferbo.util.SGPException;

public class TarifaISRBL {
	
	private static Logger log = LogManager.getLogger(TarifaISRBL.class);
	
	private List<CatTarifaISR> tablaISRSemanal = null;
	private BigDecimal baseISR = null;
	
	public TarifaISRBL(List<CatTarifaISR> tablaISRSemanal, BigDecimal baseISR) {
		this.tablaISRSemanal = tablaISRSemanal;
		this.baseISR = baseISR;
	}

	public CatTarifaISR calcular() throws SGPException {
		CatTarifaISR tarifaISR = null;
		List<CatTarifaISR> resultList = null;
		
		if(this.baseISR == null)
			throw new SGPException("No se proporcionó la base para el ISR.");
		
		if(this.tablaISRSemanal == null)
			throw new SGPException("No se proporcionó la tabla de ISR.");
		
		if(this.tablaISRSemanal.size() <= 0)
			throw new SGPException("No se proporcionó la tabla de ISR.");
		
		
		try {
			resultList = this.tablaISRSemanal.stream()
					.filter(i -> i.getLimiteInferior().compareTo(baseISR) <= 0 
					&& i.getLimiteSuperior().compareTo(baseISR) >= 0)
					.collect(Collectors.toList());
			if(resultList.size() > 0)
				tarifaISR = resultList.get(0);
			else
				throw new SGPException("No se encontró una tarifa de ISR para la base proporcionada.");
		} catch(Exception ex) {
			throw new SGPException("No es posible determinar la tasa de ISR...", ex);
		} finally {
			log.info("Tarifa ISR: {}", tarifaISR);
		}
		
		return tarifaISR;
	}
}
