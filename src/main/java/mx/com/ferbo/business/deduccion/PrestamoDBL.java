package mx.com.ferbo.business.deduccion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.PrestamoDAO;
import mx.com.ferbo.enums.ValoresBD;
import mx.com.ferbo.model.DetEmpleado;
import mx.com.ferbo.model.DetNomina;
import mx.com.ferbo.model.DetNominaDeduccion;
import mx.com.ferbo.model.DetNominaDeduccionPK;
import mx.com.ferbo.model.DetPrestamo;
import mx.com.ferbo.util.SGPException;

public class PrestamoDBL extends AbstractDBL implements IDeducciones {
	
	private static Logger log = LogManager.getLogger(PrestamoDBL.class);
	
	private DetEmpleado empleado = null;
	private PrestamoDAO prestamoDAO = null;
	private Date fecha = null;
	
	public PrestamoDBL(DetEmpleado empleado) {
		this.empleado = empleado;
		this.prestamoDAO = new PrestamoDAO();
	}

	@Override
	public void procesar(DetNomina nomina) {
		BigDecimal totalPrestamos = null;
		List<DetPrestamo> prestamos = null;
		DetNominaDeduccion deduccion = null;
		List<DetNominaDeduccion> prestamosDeduccion = null;
		
		Integer idx = null;
		
		try {
			idx = this.nuevoIndiceDe(nomina.getDeducciones());
			
			prestamosDeduccion = new ArrayList<DetNominaDeduccion>();
			prestamos = prestamoDAO.buscarPorEmpleadoVigente(empleado.getIdEmpleado(), fecha);
			
			if(prestamos.size() <= 0)
				throw new SGPException("No hay préstamos para el empleado.");
			
			totalPrestamos = ValoresBD._CERO.get();
			
			for(DetPrestamo prestamo : prestamos) {
				deduccion = new DetNominaDeduccion();
				deduccion.setKey(new DetNominaDeduccionPK(nomina, idx++));
				deduccion.setTipoDeduccion(prestamo.getTipoPrestamo().getTipoDeduccion());
				deduccion.setNombre(prestamo.getTipoPrestamo().getDescripcion());
				deduccion.setClave(prestamo.getTipoPrestamo().getTipoPrestamo());
				deduccion.setImporte(prestamo.getImporte());
				deduccion.setInformar(true);
				deduccion.setProcesar(true);
				totalPrestamos = totalPrestamos.add(prestamo.getImporte());
				
				prestamosDeduccion.add(deduccion);
			}
			
			
			nomina.getDeducciones().addAll(prestamosDeduccion);
		} catch(SGPException  ex){
			log.warn(ex.getMessage());
			totalPrestamos = ValoresBD._CERO.get();
		} catch(Exception ex) {
			log.error("Problema para procesar los préstamos del empleado...", ex);
			totalPrestamos = ValoresBD._CERO.get();
		}
	}

	public void setEmpleado(DetEmpleado empleado) {
		this.empleado = empleado;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
}
