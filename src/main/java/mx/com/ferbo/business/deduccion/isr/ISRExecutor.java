package mx.com.ferbo.business.deduccion.isr;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.NominaFormulaDAO;
import mx.com.ferbo.model.NominaFormula;

public class ISRExecutor {
	
	private static Logger log = LogManager.getLogger(ISRExecutor.class);
	
	private NominaFormulaDAO formulaDAO = null;
	
	public ISRExecutor() {
		
	}
	
	public IISR loadClass(String clave, LocalDate fecha) {
		IISR instance = null;
		Class<?> clazz = null;
		Constructor<?> constructor = null;
		
		NominaFormula formula = null;
		
		try {
			if(formulaDAO == null)
				formulaDAO = new NominaFormulaDAO();
			formula = formulaDAO.buscarVigente(clave, fecha);
			
			clazz = Class.forName(formula.getClase());
			constructor = clazz.getDeclaredConstructor(LocalDate.class);
			instance = (IISR) constructor.newInstance(fecha);
			
			log.info("Clase para cálculo de subsidio al empleo: {}", formula.getClase());
		} catch(ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
			log.error("Problema para obtener el objeto de cálculo para subsidio al empleo...", ex);
		} catch(Exception ex) {
			log.error("Problema para obtener el objeto de cálculo para subsidio al empleo...", ex);
		}
		
		return instance;
	}

}
