package mx.com.ferbo.business.empleado;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.EmpleadoDAO;
import mx.com.ferbo.dao.n.SalarioMinimoDAO;
import mx.com.ferbo.model.CatSalarioMinimo;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetSalarioDiario;
import mx.com.ferbo.util.SGPException;

public class SalarioMinimoBL {
	
	private static Logger log = LogManager.getLogger(SalarioMinimoBL.class);
	
	public static CatSalarioMinimo salarioMinimoVigente(Date fecha)
	throws SGPException {
		CatSalarioMinimo salarioMinimo = null;
		SalarioMinimoDAO salarioMinimoDAO = new SalarioMinimoDAO();
		
		/* En casos muy específicos como este, se usa el método "buscarTodos()", debido a que
		 * el catálogo de salarios mínimos contiene poca información y se actualiza cada año.
		 * Sólo en el caso en el número de registros fuera muy alto, se debería implementar
		 * la búsqueda mediante una consulta SQL que entregue una menor cantidad de información.
		 * */
		salarioMinimo = salarioMinimoDAO.buscarTodos()
				.stream()
				.filter(salario -> salario.getVigencia().compareTo(fecha) <= 0)
				.max(Comparator.comparing(CatSalarioMinimo::getVigencia))
				.orElseThrow(() -> new SGPException("No hay salarios minimos registrados"))
				;
		
		return salarioMinimo;
	}
	
	public static List<DetEmpleado> validarSalariosMinimos(Date fecha)
	throws SGPException {
		List<DetEmpleado> resultado = null;
		List<DetEmpleado> empleados = null;
		EmpleadoDAO empleadoDAO = new EmpleadoDAO();
		CatSalarioMinimo salarioMinimo;
		BigDecimal importeSalarioMinimo;
		BigDecimal salarioDiario;
		DetSalarioDiario salarioDiarioVigente = null;
		
		salarioMinimo = salarioMinimoVigente(fecha);
		empleados = empleadoDAO.buscarActivosConSalarioDiario(null, null, fecha);
		
		resultado = new ArrayList<DetEmpleado>();
		log.info("Salario minimo: {}", salarioMinimo);
		for(DetEmpleado empleado : empleados) {
			log.debug("Empleado: {}", empleado);
			
			salarioDiario = EmpleadoBL.salarioDiarioVigente(empleado.getDatoEmpresa().getSalariosDiarios() , fecha)
					.getImporte();
			importeSalarioMinimo = "G".equalsIgnoreCase(empleado.getDatoEmpresa().getZona().getClave()) ?
					salarioMinimo.getZonaG() : salarioMinimo.getZonaLFN();
			
			log.debug("Salario diario del empleado: {}", salarioDiario);
			
			if(salarioDiario.compareTo(importeSalarioMinimo) > 0 )
				continue;
			
			salarioDiarioVigente = EmpleadoBL.salarioDiarioVigente(empleado.getDatoEmpresa().getSalariosDiarios(), fecha);
			
			empleado.getDatoEmpresa().setSalarioDiario(salarioDiarioVigente.getImporte());
			resultado.add(empleado);
			
			log.debug("Zona: {}, Salario Mínimo vigente: {}, Salario diario del empleado: {}",
					empleado.getDatoEmpresa().getZona(),
					importeSalarioMinimo,
					salarioDiarioVigente.getImporte());
		}
		
		return resultado;
	}
}
