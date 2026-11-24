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
import mx.com.ferbo.model.CatZonaSalario;
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

			String nombreEmpleado = empleado.getNombre() + " " + empleado.getPrimerAp() + " " + empleado.getSegundoAp();

			List<DetSalarioDiario> salariosDarios = empleado.getDatoEmpresa().getSalariosDiarios();

			if (salariosDarios.size() == 0) {
				log.warn("El empleado " + nombreEmpleado + " no tiene ningun salario registrado. Se le debe registrar uno");
			}
			
			salarioDiario = EmpleadoBL.salarioDiarioVigente( salariosDarios, fecha)
					.getImporte();

			if (empleado.getDatoEmpresa() == null) {
				log.warn("El empleado " + nombreEmpleado + "  no tiene información relacionada con la empresa. Se ñe debe registrar");
			}

			CatZonaSalario zona = empleado.getDatoEmpresa().getZona();
			String claveZona = "";

			if (zona == null) {
				log.warn("El empleado " + nombreEmpleado + " no tiene asignada una zona geográfica de trabajo. Se le debe registrar una.");
			} else {
				claveZona = empleado.getDatoEmpresa().getZona().getClave();
				if (claveZona == null || "".equalsIgnoreCase(claveZona)) {
					log.warn("El empleado " + nombreEmpleado + " no tiene asignada una zona geográfica de trabajo. Se le debe registrar una.");
				} 
			}
			
			importeSalarioMinimo = "G".equalsIgnoreCase(claveZona) ?
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
