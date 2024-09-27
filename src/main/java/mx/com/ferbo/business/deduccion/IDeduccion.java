package mx.com.ferbo.business.deduccion;

import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;

public interface IDeduccion {
	public DetNominaDeduccion calcular(DetNomina nomina, Integer index);
}
