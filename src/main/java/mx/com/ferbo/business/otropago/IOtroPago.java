package mx.com.ferbo.business.otropago;

import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaOtroPago;

public interface IOtroPago {
	public DetNominaOtroPago calcular(DetNomina nomina, Integer index);
}
