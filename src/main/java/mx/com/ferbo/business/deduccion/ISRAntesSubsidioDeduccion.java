package mx.com.ferbo.business.deduccion;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.TipoDeduccionDAO;
import mx.com.ferbo.model.CatTarifaISR;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.util.SGPException;

public class ISRAntesSubsidioDeduccion implements IDeduccion {
	
	private static Logger log = LogManager.getLogger(ISRAntesSubsidioDeduccion.class);
	
	private BigDecimal baseISR = null;
	private CatTarifaISR tarifaISR = null;
	private TipoDeduccionDAO tipoDeduccionDAO = null;
	private BigDecimal excedente = null;
	private BigDecimal isrPrevio = null;
	
	public ISRAntesSubsidioDeduccion(BigDecimal baseISR, CatTarifaISR tarifaISR) {
		this.baseISR = baseISR;
		this.tarifaISR = tarifaISR;
	}

	@Override
	public DetNominaDeduccion calcular(DetNomina nomina, Integer index) {
		DetNominaDeduccion deduccion = null;
		BigDecimal isrPreSubsidio = null;
		CatTipoDeduccion tdISR = null;
		
		try {
			if(baseISR == null)
				throw new SGPException("No se proporcionó la base para el ISR.");
			
			if(tarifaISR == null)
				throw new SGPException("No se proporcionó una tarifa para ISR.");
			
			if(this.tipoDeduccionDAO == null)
				this.tipoDeduccionDAO = new TipoDeduccionDAO();
			
			tdISR = this.tipoDeduccionDAO.buscarPorId("002");
			
			//Comienza el cálculo de ISR antes del subsidio al empleo...
			this.excedente = baseISR.subtract(tarifaISR.getLimiteInferior());
			this.isrPrevio = this.calculoISRPrevio();
			
			isrPreSubsidio = this.isrPrevio.add(tarifaISR.getCuotaFija());
		} catch(Exception ex) {
			log.error("Problema para obtener el ISR antes del subsidio al empleo...", ex);
			isrPreSubsidio = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		} finally {
			deduccion = new DetNominaDeduccion();
			deduccion.setKey(new DetNominaDeduccionPK(nomina, index));
			deduccion.setTipoDeduccion(tdISR);
			deduccion.setClave("---");
			deduccion.setNombre("I.S.R. antes de Subs al empleo");
			deduccion.setImporte(isrPreSubsidio);
			deduccion.setProcesar(false);
		}
		
		return deduccion;
	}
	
	private BigDecimal calculoISRPrevio() {
		BigDecimal isrPrevio = null;
		BigDecimal porcentajeExcedente = null;
		BigDecimal cien = null;
		BigDecimal porcentaje = null;
		
		try {
			porcentajeExcedente = tarifaISR.getPorcAplExceLimInf();
			
			cien = new BigDecimal("100.00").setScale(2, BigDecimal.ROUND_HALF_UP);
			if(porcentajeExcedente.compareTo(cien) >= 0)
				porcentaje = porcentajeExcedente;
			else
				porcentaje = porcentajeExcedente.divide(cien, 4, BigDecimal.ROUND_HALF_UP);
			
			isrPrevio = this.excedente.multiply(porcentaje).setScale(2, BigDecimal.ROUND_HALF_UP);
		} catch(Exception ex) {
			log.error("Problema para obtener el ISR previo");
			isrPrevio = BigDecimal.ZERO;
		}
		
		return isrPrevio;
	}

	public BigDecimal getExcedente() {
		return excedente;
	}

	public BigDecimal getIsrPrevio() {
		return isrPrevio;
	} 

}
