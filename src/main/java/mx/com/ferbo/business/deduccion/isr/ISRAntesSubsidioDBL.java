package mx.com.ferbo.business.deduccion.isr;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.AbstractDBL;
import mx.com.ferbo.business.deduccion.IDeduccion;
import mx.com.ferbo.dao.n.TipoDeduccionDAO;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.CatTarifaISR;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.util.SGPException;

public class ISRAntesSubsidioDBL extends AbstractDBL implements IDeduccion {
	
	private static Logger log = LogManager.getLogger(ISRAntesSubsidioDBL.class);
	
	private BigDecimal       baseISR = null;
	private CatTarifaISR     tarifaISR = null;
	private TipoDeduccionDAO tipoDeduccionDAO = null;
	private BigDecimal       excedente = null;
	private BigDecimal       isrPrevio = null;
	
	public ISRAntesSubsidioDBL(BigDecimal baseISR, CatTarifaISR tarifaISR) {
		this.baseISR   = baseISR;
		this.tarifaISR = tarifaISR;
	}

	@Override
	public DetNominaDeduccion calcular(DetNomina nomina) {
		DetNominaDeduccion deduccion      = null;
		CatTipoDeduccion   tdISR          = null;
		BigDecimal         isrPreSubsidio = null;
		
		try {
			if(baseISR == null)
				throw new SGPException("No se proporcionó la base para el ISR.");
			
			if(tarifaISR == null)
				throw new SGPException("No se proporcionó una tarifa para ISR.");
			
			if(this.tipoDeduccionDAO == null)
				this.tipoDeduccionDAO = new TipoDeduccionDAO();
			
			tdISR = this.tipoDeduccionDAO.buscarPorId(TD_ISR);
			
			//Comienza el cálculo de ISR antes del subsidio al empleo...
			this.excedente = baseISR.subtract(tarifaISR.getLimiteInferior());
			this.isrPrevio = this.calculoISRPrevio();
			
			isrPreSubsidio = this.isrPrevio.add(tarifaISR.getCuotaFija());
		} catch(Exception ex) {
			log.error("Problema para obtener el ISR antes del subsidio al empleo...", ex);
			isrPreSubsidio = BigDecimal.ZERO.setScale(2,  RoundingMode.HALF_UP);
		} finally {
			deduccion = new DetNominaDeduccion.Builder()
					.nomina(nomina)
					.tipoDeduccion(tdISR)
					.clave(CVE_ISR_ANTES_DE_SUBSIDIO)
					.nombre("I.S.R. antes de Subs al empleo")
					.importe(isrPreSubsidio)
					.informar(true)
					.procesar(false)
					.build();
		}
		
		return deduccion;
	}
	
	private BigDecimal calculoISRPrevio() {
		BigDecimal isrPrevio = null;
		BigDecimal porcentajeExcedente = null;
		BigDecimal porcentaje = null;
		
		try {
			porcentajeExcedente = tarifaISR.getPorcAplExceLimInf();
			
			if(porcentajeExcedente.compareTo(ValoresBD._100.get()) >= 0)
				porcentaje = porcentajeExcedente;
			else
				porcentaje = porcentajeExcedente.divide(ValoresBD._100.get(), 5, RoundingMode.HALF_UP);
			
			isrPrevio = this.excedente.multiply(porcentaje).setScale(2, RoundingMode.HALF_UP);
		} catch(Exception ex) {
			log.error("Problema para obtener el ISR previo", ex);
			isrPrevio = ValoresBD._CERO.get();
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
