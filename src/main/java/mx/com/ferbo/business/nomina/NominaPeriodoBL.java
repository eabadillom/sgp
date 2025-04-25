package mx.com.ferbo.business.nomina;

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.NominaPeriodoDAO;
import mx.com.ferbo.model.CatEmpresa;
import mx.com.ferbo.model.CatPeriodicidadPago;
import mx.com.ferbo.model.DetNominaPeriodo;
import mx.com.ferbo.model.DetNominaPeriodoPK;
import mx.com.ferbo.util.SGPException;

public class NominaPeriodoBL {
	
	private static Logger log = LogManager.getLogger(NominaPeriodoBL.class);
	
	public static DetNominaPeriodo build(CatEmpresa empresa, String tipoNomina, CatPeriodicidadPago periodicidad, Integer anio)
	throws SGPException {
		DetNominaPeriodo model = null;
		DetNominaPeriodo find = null;
		NominaPeriodoDAO periodoDAO = null;
		
		Integer periodo = null;
		LocalDate periodoInicio = null;
		LocalDate periodoFin = null;
		LocalDate fechaPago = null;
		
		try {
			if(empresa == null)
				throw new SGPException("Debe indicar la empresa para cálculo de nómina.");			
			
			if(tipoNomina == null)
				throw new SGPException("Debe proporcionar el tipo de nómina (O: Ordinaria, E: Extraordinaria)");
			
			if(periodicidad == null)
				throw new SGPException("Debe proporcionar el tipo de periodo (Para nómina ordinaria: D: Diario, S: Semanal, ");
						
			if(periodicidad.getPeriodicidad().equalsIgnoreCase("99")) {
				periodoInicio = LocalDate.now();
				periodoFin = LocalDate.now();
				fechaPago = LocalDate.now();
			}
			
			periodoDAO = new NominaPeriodoDAO();
			
			find = periodoDAO.buscarUltimo(empresa.getIdEmpresa(), tipoNomina, periodicidad.getPeriodicidad(), anio);
			
			if(find == null) {
				periodo = 1;
			} else {
				periodo = find.getKey().getPeriodo() + 1;
			}
			
			model = new DetNominaPeriodo();
			model.setKey(new DetNominaPeriodoPK(empresa, tipoNomina, periodicidad, anio, periodo));
			model.setPeriodoInicio(periodoInicio);
			model.setPeriodoFin(periodoFin);
			model.setFechaPago(fechaPago);
			
		} finally {
			
		}
		
		return model;
	}
	
	public static void guardar(DetNominaPeriodo nominaPeriodo) {
		NominaPeriodoDAO periodoDAO = null;
		
		try {
			periodoDAO = new NominaPeriodoDAO();
			periodoDAO.guardar(nominaPeriodo);
			
		} catch(Exception ex) {
			log.error("Problema para guardar el periodo de nómina...", ex);
		}
	}

	public static void actualizar(DetNominaPeriodo nominaPeriodo) {
		NominaPeriodoDAO periodoDAO = null;
		
		try {
			periodoDAO = new NominaPeriodoDAO();
			periodoDAO.actualizar(nominaPeriodo);
			
		} catch(Exception ex) {
			log.error("Problema para guardar el periodo de nómina...", ex);
		}
		
	}

	public static DetNominaPeriodo cargar(DetNominaPeriodoPK key) {
		DetNominaPeriodo model = null;
		NominaPeriodoDAO periodoDAO = null;
		
		try {
			periodoDAO = new NominaPeriodoDAO();
			model = periodoDAO.buscarPorId(key);
			
		} catch(Exception ex) {
			log.error("Problema para obtener el periodo por id: " + key.toString(), ex);
		}
		
		return model;
	}
}
