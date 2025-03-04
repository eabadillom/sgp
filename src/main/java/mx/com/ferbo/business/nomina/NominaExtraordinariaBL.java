package mx.com.ferbo.business.nomina;

import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetRegistro;

public class NominaExtraordinariaBL extends NominaBL {
	
	private static Logger log = LogManager.getLogger(NominaExtraordinariaBL.class);
	
	private Date periodoInicio = null;
	private Date periodoFin = null;
	private Date fechaInicioAnio = null;
	private Date fechafinAnio = null;
	private Integer anio = null;
	
	public NominaExtraordinariaBL() {
		super();
	}

	public NominaExtraordinariaBL(DetEmpleado empleado, ParametrosNomina parametros) {
		super(empleado, parametros, null);
		this.periodoInicio = parametros.getPeriodoInicio();
		this.periodoFin = parametros.getPeriodoFin();
		this.fechaInicioAnio = null;
		this.fechafinAnio = null;
	}
	
	
	
	public DetNomina calcular(DetNomina nomina, ParametrosNomina parametros) {
		
		try {
			
			
			
		} catch(Exception ex) {
			
		} finally {
			
		}
		
		return nomina;
	}

}
