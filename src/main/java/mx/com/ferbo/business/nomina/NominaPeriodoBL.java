package mx.com.ferbo.business.nomina;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.NominaPeriodoDAO;
import mx.com.ferbo.model.CatEmpresa;
import mx.com.ferbo.model.CatPeriodicidadPago;
import mx.com.ferbo.model.DetNominaPeriodo;
import mx.com.ferbo.model.DetNominaPeriodoPK;
import mx.com.ferbo.util.DateUtil;
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
			
			if(anio == null)
				throw new SGPException("Debe proporcionar el año del periodo.");
						
			if(periodicidad.getPeriodicidad().equalsIgnoreCase(CatPeriodicidadPago.P_OTRA_PERIODICIDAD)) {
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
	
	public static DetNominaPeriodo get(CatEmpresa empresa, String tipoNomina, CatPeriodicidadPago periodicidad, Integer anio, Integer periodo)
	throws SGPException {
		DetNominaPeriodo nominaPeriodo = null;
		List<DetNominaPeriodo> nominaPeriodos = null;
		NominaPeriodoDAO periodoDAO = null;
		
		//Primero buscamos los periodos de nómina con base en los atributos "empresa", "Tipo nomina",
		//"Periodicidad de pago" y "Año" (condiciones iniciales).
		//Esto nos debería devolver los periodos de nómina registrados en la base de datos.
		periodoDAO = new NominaPeriodoDAO();
		nominaPeriodos = periodoDAO.buscar(empresa.getIdEmpresa(), tipoNomina, periodicidad.getPeriodicidad(), anio);
		
		//Si no obtenemos resultados, es que no hay periodos de nómina registrados con las condiciones iniciales antes mencionadas.
		if(nominaPeriodos == null || nominaPeriodos.size() <= 0) {
			nominaPeriodo = new DetNominaPeriodo.Builder()
			.empresa(empresa).tipoNomina(tipoNomina).periodicidad(periodicidad).anio(anio).periodo(periodo)
			.build();
			calculaFechasPeriodo(nominaPeriodo, periodicidad, anio, periodo);
			
			return nominaPeriodo;
		}
		
		//Una vez que tenemos la lista de los periodos de nómina con base en los criterios antes mencionados,
		//buscamos ahora por "periodo" (número de semana en periodicidad de pago semanal, número de mes en periodicidad
		//mensual, etc)
		Optional<DetNominaPeriodo> primerNominaPeriodo = nominaPeriodos.stream()
			.filter(p -> p.getKey().getPeriodo().equals(periodo))
			.findFirst();
		
		//Si encontramos el elemento solicitado en la lista, lo devolvemos.
		if(primerNominaPeriodo.isPresent())
			return primerNominaPeriodo.get();
		
		//Si no lo encontramos, lo creamos
		nominaPeriodo = new DetNominaPeriodo.Builder()
				.empresa(empresa).tipoNomina(tipoNomina).periodicidad(periodicidad).anio(anio).periodo(periodo)
				.build();
		
		return nominaPeriodo;
	}
	
	
	
	private static void calculaFechasPeriodo(DetNominaPeriodo nominaPeriodo, CatPeriodicidadPago periodicidad, Integer anio, Integer periodo) {
		
		LocalDate periodoInicio = null;
		LocalDate periodoFin = null;
		
		switch(periodicidad.getPeriodicidad()) {
		case CatPeriodicidadPago.P_SEMANAL:
			periodoInicio = DateUtil.getLunesDeSemanaLocalDate(anio, periodo);
			periodoFin = periodoInicio.plusDays(6);
			break;
			
		default:
			throw new UnsupportedOperationException("El cálculo de fechas para esta periodicidad no está definido.");
		}
		
		
		
		nominaPeriodo.setPeriodoInicio(periodoInicio);
    	nominaPeriodo.setPeriodoFin(periodoFin);
    	nominaPeriodo.setFechaPago(periodoFin);
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
