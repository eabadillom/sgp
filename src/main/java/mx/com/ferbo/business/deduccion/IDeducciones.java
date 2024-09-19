package mx.com.ferbo.business.deduccion;

import java.util.List;

import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;

public interface IDeducciones {
	public List<DetNominaDeduccion> calcular(DetNomina nomina, Integer index);
}
