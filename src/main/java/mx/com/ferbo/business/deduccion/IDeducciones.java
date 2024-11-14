package mx.com.ferbo.business.deduccion;

import java.util.List;

import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;

public interface IDeducciones {
	public List<DetNominaDeduccion> procesar(DetNomina nomina, Integer index);
}
