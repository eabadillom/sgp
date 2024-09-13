package mx.com.ferbo.business.deduccion;

import java.util.List;

import mx.com.ferbo.model.CatTarifaISR;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.NominaPercepcion;

public class ISRDeduccion implements IDeduccion {
	
	private List<CatTarifaISR> tablaISRSemanal;
	private List<NominaPercepcion> percepcionesGravadas;
	
	
	
	@Override
	public DetNominaDeduccion calcular(DetNomina nomina, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<CatTarifaISR> getTablaISRSemanal() {
		return tablaISRSemanal;
	}

	public void setTablaISRSemanal(List<CatTarifaISR> tablaISRSemanal) {
		this.tablaISRSemanal = tablaISRSemanal;
	}

	public List<NominaPercepcion> getPercepcionesGravadas() {
		return percepcionesGravadas;
	}

	public void setPercepcionesGravadas(List<NominaPercepcion> percepcionesGravadas) {
		this.percepcionesGravadas = percepcionesGravadas;
	}
	
}
