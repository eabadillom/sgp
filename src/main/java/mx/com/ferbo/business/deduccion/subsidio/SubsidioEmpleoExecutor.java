package mx.com.ferbo.business.deduccion.subsidio;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.NominaFormulaDAO;
import mx.com.ferbo.model.NominaFormula;

/**El patron de diseño Reflection esta pensado solo para periodos de convivencia, en los que
 * se anticipa un cambio en el calculo (en este caso del Subsidio al empleo), para poder instalar
 * de manera anticipada los cambios a las reglas de negocio que establezca la autoridad
 * (SAT, IMSS, etc).
 */
public class SubsidioEmpleoExecutor {
	
	private static Logger log = LogManager.getLogger(SubsidioEmpleoExecutor.class);
	
	private NominaFormulaDAO formulaDAO = null;
	
	public SubsidioEmpleoExecutor() {
	}
	
	public ISubsidioEmpleo loadClass(String clave, LocalDate fecha) {
		ISubsidioEmpleo instance = null;
		Class<?> clazz = null;
		Constructor<?> constructor = null;
		
		NominaFormula formula = null;
		
		try {
			if(formulaDAO == null)
				formulaDAO = new NominaFormulaDAO();
			formula = formulaDAO.buscarVigente(clave, fecha);
			
			clazz = Class.forName(formula.getClase());
			constructor = clazz.getDeclaredConstructor(LocalDate.class);
			instance = (ISubsidioEmpleo) constructor.newInstance(fecha);
			
			log.info("Clase para cálculo de subsidio al empleo: {}", formula.getClase());
		} catch(ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
			log.error("Problema para obtener el objeto de cálculo para subsidio al empleo...", ex);
		} catch(Exception ex) {
			log.error("Problema para obtener el objeto de cálculo para subsidio al empleo...", ex);
		}
		
		return instance;
	}
}
