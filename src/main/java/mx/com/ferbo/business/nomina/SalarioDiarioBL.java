package mx.com.ferbo.business.nomina;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.SalarioDiarioDAO;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetSalarioDiario;

public class SalarioDiarioBL {
	private static final Logger log = LogManager.getLogger(SalarioDiarioBL.class);
	
	public static synchronized DetSalarioDiario salarioDiarioVigente(DetEmpleado empleado, Date fecha) {
		DetSalarioDiario salarioDiario;
		SalarioDiarioDAO salarioDiarioDAO;
		
		salarioDiarioDAO = new SalarioDiarioDAO();
		
		List<DetSalarioDiario> salariosDiarios = salarioDiarioDAO.buscar(empleado);
		
		List<DetSalarioDiario> tmp = salariosDiarios.stream()
				.filter(s -> s.getFechaRegistro().compareTo(fecha) <= 0)
				.collect(Collectors.toList());
		
		salarioDiario = tmp
				.stream()
				.max(Comparator.comparing(DetSalarioDiario::getFechaRegistro))
				.orElse(new DetSalarioDiario.Builder()
						.fechaRegistro(fecha)
						.datoEmpresa(empleado.getDatoEmpresa())
						.importe(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))
						.build()
						)
				;
		
		log.info("Salario diario vigente: Núm. emp. = {}, fecha = {}, importe = {}",
				empleado.getNumEmpleado(), fecha, salarioDiario.getImporte());
		
		return salarioDiario;
	}
	
	
}
