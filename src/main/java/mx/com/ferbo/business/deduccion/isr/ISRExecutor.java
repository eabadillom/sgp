package mx.com.ferbo.business.deduccion.isr;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.deduccion.IDeducciones;
import mx.com.ferbo.business.nomina.ParametrosNomina;
import mx.com.ferbo.dao.n.NominaFormulaDAO;
import mx.com.ferbo.model.CatTarifaISR;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.NominaFormula;
import mx.com.ferbo.model.sat.CatTipoDeduccion;
import mx.com.ferbo.model.sat.CatTipoOtroPago;


/**El patron de diseño Reflection esta pensado solo para periodos de convivencia, en los que
 * se anticipa un cambio en el calculo (en este caso del ISR), para poder instalar de manera
 * anticipada los cambios a las reglas de negocio que establezca la autoridad (SAT, IMSS, etc).
 */
public class ISRExecutor {
	
	private static Logger log = LogManager.getLogger(ISRExecutor.class);
	
	private NominaFormulaDAO formulaDAO = null;
	
	private ParametrosNomina       parametros     = null;
	private Date                   periodoInicio  = null;
	private Date                   periodoFin     = null;
	private List<DetNomina>        nominaMensual  = null;
	private List<CatTipoDeduccion> tiposDeduccion = null;
	private List<CatTipoOtroPago>  tiposOtroPago  = null;
	private List<CatTarifaISR>     tablaISR       = null;
	
	@Deprecated
	public ISRExecutor(Date periodoInicio, Date periodoFin, List<CatTipoDeduccion> tiposDeduccion, List<CatTipoOtroPago> tiposOtroPago, List<CatTarifaISR> tablaISR, List<DetNomina> nominaMensual) {
		this.periodoInicio  = periodoInicio;
		this.periodoFin     = periodoFin;
		this.tiposDeduccion = tiposDeduccion;
		this.tiposOtroPago  = tiposOtroPago;
		this.tablaISR       = tablaISR;
		this.nominaMensual  = nominaMensual;
	}
	
	public ISRExecutor(ParametrosNomina parametros, List<DetNomina> nominaMensual) {
		
		this.periodoInicio  = parametros.getPeriodoInicio();
		this.periodoFin     = parametros.getPeriodoFin();
		this.tiposDeduccion = parametros.getTiposDeduccion();
		this.tiposOtroPago  = parametros.getTiposOtroPago();
		this.tablaISR       = parametros.getTablaISR();
		this.nominaMensual  = nominaMensual;
	}
	
	public IDeducciones loadClass(String clave, LocalDate fecha) {
		IDeducciones   instance    = null;
		Class<?>       clazz       = null;
		Constructor<?> constructor = null;
		NominaFormula  formula     = null;
		
		try {
			if(formulaDAO == null)
				formulaDAO = new NominaFormulaDAO();
			formula = formulaDAO.buscarVigente(clave, fecha);
			
			clazz = Class.forName(formula.getClase());
//			constructor = clazz.getDeclaredConstructor(Date.class, Date.class, List.class, List.class, List.class, List.class);
			constructor = clazz.getDeclaredConstructor(ParametrosNomina.class, List.class);
//			instance = (IDeducciones) constructor.newInstance(this.periodoInicio, this.periodoFin, this.tiposDeduccion, this.tiposOtroPago, this.tablaISR, this.nominaMensual);
			instance = (IDeducciones) constructor.newInstance(this.parametros, this.nominaMensual);
			
			log.info("Clase para cálculo de ISR: {}", formula.getClase());
		} catch(ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
			log.error("Problema para obtener el objeto de cálculo para subsidio al empleo...", ex);
		} catch(Exception ex) {
			log.error("Problema para obtener el objeto de cálculo para subsidio al empleo...", ex);
		}
		
		return instance;
	}
}