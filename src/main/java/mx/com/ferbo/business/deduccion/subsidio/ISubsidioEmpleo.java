package mx.com.ferbo.business.deduccion.subsidio;

import java.math.BigDecimal;

import mx.com.ferbo.util.SGPException;

public interface ISubsidioEmpleo {
	
	public static final String PERIODO_SEMANAL = "s";
	public static final String PERIODO_MENSUAL = "m";
	
	public BigDecimal calcular(String periodo, BigDecimal baseISR) throws SGPException;

}
