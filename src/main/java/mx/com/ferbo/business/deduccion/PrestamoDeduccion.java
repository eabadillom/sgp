package mx.com.ferbo.business.deduccion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.PrestamoDAO;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.DetPrestamo;
import mx.com.ferbo.util.SGPException;

public class PrestamoDeduccion extends AbstractDeduccion implements IDeducciones {
	
	private static Logger log = LogManager.getLogger(PrestamoDeduccion.class);
	
	private DetEmpleado empleado = null;
	private PrestamoDAO prestamoDAO = null;
	
	public PrestamoDeduccion(DetEmpleado empleado) {
		this.empleado = empleado;
		this.prestamoDAO = new PrestamoDAO();
	}

	@Override
	public List<DetNominaDeduccion> calcular(DetNomina nomina, Integer index) {
		BigDecimal totalPrestamos = null;
		List<DetPrestamo> prestamos = null;
		DetNominaDeduccion deduccion = null;
		List<DetNominaDeduccion> deducciones = null;
		
		Integer idx = null;
		
		try {
			idx = this.nuevoIndiceDe(nomina.getDeducciones());
			
			deducciones = new ArrayList<DetNominaDeduccion>();
			prestamos = prestamoDAO.buscar(empleado.getIdEmpleado());
			
			if(prestamos.size() <= 0)
				throw new SGPException("No hay préstamos para el empleado.");
			
			totalPrestamos = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
			
			for(DetPrestamo prestamo : prestamos) {
				deduccion = new DetNominaDeduccion();
				deduccion.setKey(new DetNominaDeduccionPK(nomina, idx++));
				deduccion.setTipoDeduccion(prestamo.getTipoPrestamo().getTipoDeduccion());
				deduccion.setNombre(prestamo.getTipoPrestamo().getDescripcion());
				deduccion.setClave(prestamo.getTipoPrestamo().getTipoPrestamo());
				deduccion.setImporte(prestamo.getImporte());
				deduccion.setProcesar(true);
				totalPrestamos = totalPrestamos.add(prestamo.getImporte());
				
				deducciones.add(deduccion);
			}
			
		} catch(Exception ex) {
			log.error("Problema para procesar los préstamos del empleado");
			totalPrestamos = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		return deducciones;
	}

	public void setEmpleado(DetEmpleado empleado) {
		this.empleado = empleado;
	}




}
