package mx.com.ferbo.business.percepcion;

import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaPercepcion;

public interface IPercepcion {
	public DetNominaPercepcion calcular(DetNomina nomina, Integer index);
}
