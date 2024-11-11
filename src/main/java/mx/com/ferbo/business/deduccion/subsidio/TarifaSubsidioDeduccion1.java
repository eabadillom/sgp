package mx.com.ferbo.business.deduccion.subsidio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.SubsidioDAO;
import mx.com.ferbo.model.CatSubsidio;
import mx.com.ferbo.util.SGPException;

public class TarifaSubsidioDeduccion1 implements ISubsidioEmpleo {
	
	private static Logger log = LogManager.getLogger(TarifaSubsidioDeduccion1.class);
	
	private SubsidioDAO subsidioDAO = null;
	private List<CatSubsidio> tablaSubsidio = null;
	private LocalDate fecha = null;
	
	public TarifaSubsidioDeduccion1(LocalDate fecha) {
		subsidioDAO = new SubsidioDAO();
		this.fecha = fecha;
	}
	
	public BigDecimal calcular(String periodo, BigDecimal baseISR) throws SGPException {
		CatSubsidio tarifaSubsidio = null;
		List<CatSubsidio> resultList = null;
		
		if(baseISR == null)
			throw new SGPException("No se proporcionó la base para el ISR.");
		
		this.tablaSubsidio = subsidioDAO.buscarPorPeriodo(periodo);
		
		if(this.tablaSubsidio == null)
			throw new SGPException("No se proporcionó la tabla de subsidio al salario.");
		
		if(this.tablaSubsidio.size() <= 0)
			throw new SGPException("No se proporcionó la tabla de subsidio al salario.");
		
		try {
			resultList = this.tablaSubsidio.stream()
					.filter(s -> s.getParaIngresosDe().compareTo(baseISR) <= 0
							&& s.getHastaIngresosDe().compareTo(baseISR) >= 0)
					.collect(Collectors.toList())
					;
			
			if(resultList.size() > 0) {
				tarifaSubsidio = resultList.get(0);
			} else {
				throw new SGPException(String.format("No se encontró una tarifa de subsidio para la base calculada: Base ISR = %s", baseISR.toString()));
			}
			log.info("Fecha: {}, Base ISR: {}, Subsidio acreditado: {}", this.fecha, baseISR, tarifaSubsidio);
		} catch(Exception ex) {
			log.warn(ex);
			throw new SGPException("No es posible determinar el subsidio al empleo...", ex);
		}
		return tarifaSubsidio.getCantidadSubsidio();
	}
}
