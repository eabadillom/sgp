package mx.com.ferbo.business.deduccion.isr;

import java.math.BigDecimal;
import java.time.LocalDate;

import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.util.SGPException;

public interface IISR {
	
	public static final String PERIODO_SEMANAL = "s";
	public static final String PERIODO_MENSUAL = "m";
	
	public void calcular(DetNomina nomina, LocalDate fecha, String periodo, BigDecimal baseISR) throws SGPException;

}
