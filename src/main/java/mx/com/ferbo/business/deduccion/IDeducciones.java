package mx.com.ferbo.business.deduccion;

import mx.com.ferbo.model.DetNomina;

public interface IDeducciones {
	public void procesar(DetNomina nomina, Integer index);
}
